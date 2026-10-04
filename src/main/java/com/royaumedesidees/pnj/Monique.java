package com.royaumedesidees.pnj;

import com.royaumedesidees.grace.CompteursJour;
import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.grace.Voie;
import com.royaumedesidees.jardin.Culpabilite;
import com.royaumedesidees.registre.ModSons;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.UUID;

/**
 * Sainte Monique, la mère d'Augustin, qui a prié et pleuré des années pour sa conversion (« il est impossible que le
 * fils de tant de larmes périsse », Confessions, III, 12).
 * <p>
 * Chaque joueur a sa Monique : elle apparaît à son premier péché dans le Royaume et le suit partout dans la
 * dimension (voir {@link SuiviMonique}). Elle pleure tant qu'il n'a pas la Voie du Cœur, plus fort à partir de la
 * Culpabilité III : ce sont ses sanglots qu'on entend, là où elle est. Invincible ; frappée, elle pleure plus fort.
 * Nourrie, elle rapporte de la Grâce une fois par jour. Elle n'est jamais sauvegardée : quand son joueur quitte le
 * Royaume ou le serveur, elle s'en va, et elle revient avec lui.
 */
public class Monique extends PnjRoyaume {
    /** Durée du plus long des trois extraits de sanglots (5,2 s), arrondie : jamais deux sanglots à la fois. */
    private static final int DUREE_SANGLOT = 110;

    private UUID proprietaire;
    private long prochainSanglot;

    public Monique(EntityType<? extends Monique> type, Level niveau) {
        super(type, niveau, ChatFormatting.LIGHT_PURPLE);
    }

    public void setProprietaire(ServerPlayer joueur) {
        this.proprietaire = joueur.getUUID();
    }

    public UUID proprietaire() {
        return proprietaire;
    }

    private ServerPlayer joueur() {
        return proprietaire == null || !(level() instanceof ServerLevel monde) ? null
                : (ServerPlayer) monde.getPlayerByUUID(proprietaire);
    }

    @Override
    protected boolean aUneMaison() {
        return false;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new SuivreSonFils());
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    /** Vrai tant que son joueur n'a pas la Voie du Cœur. */
    public boolean pleure() {
        ServerPlayer joueur = joueur();
        return joueur != null && Grace.voie(joueur) != Voie.COEUR;
    }

    @Override
    public void tick() {
        super.tick();
        if (!(level() instanceof ServerLevel monde)) {
            return;
        }
        ServerPlayer joueur = joueur();
        if (joueur == null || joueur.level() != monde || joueur.isSpectator()) {
            discard();
            return;
        }
        if (pleure()) {
            if (tickCount % 15 == 0) {
                monde.sendParticles(ParticleTypes.FALLING_WATER, getX(), getEyeY() - 0.15, getZ(), 2, 0.15, 0.05, 0.15, 0.0);
            }
            if (monde.getGameTime() >= prochainSanglot) {
                sangloter(monde, joueur, false);
            }
        }
    }

    /** Un sanglot, puis un silence de 8 à 20 secondes ; plus fort si le joueur croule sous la Culpabilité. */
    private void sangloter(ServerLevel monde, ServerPlayer joueur, boolean plusFort) {
        float volume = plusFort || Culpabilite.niveau(joueur) >= 3 ? 1.2F : 0.6F;
        monde.playSound(null, getX(), getY(), getZ(), ModSons.SANGLOTS.get(), SoundSource.NEUTRAL, volume, 0.95F + getRandom().nextFloat() * 0.1F);
        prochainSanglot = monde.getGameTime() + DUREE_SANGLOT + 160 + getRandom().nextInt(240);
    }

    @Override
    protected void reagirCoup(ServerPlayer joueur) {
        parler(joueur, "coup");
        if (level() instanceof ServerLevel monde && monde.getGameTime() >= prochainSanglot - 240) {
            sangloter(monde, joueur, true);
        }
    }

    @Override
    protected void parleAvec(ServerPlayer joueur) {
        ItemStack main = joueur.getMainHandItem();
        if (joueur.getUUID().equals(proprietaire) && main.has(DataComponents.FOOD)) {
            nourrir(joueur, main);
            return;
        }
        if (!joueur.getUUID().equals(proprietaire)) {
            parler(joueur, "pas_mon_fils");
        } else {
            parler(joueur, pleure() ? "bonjour" : "apaisee");
        }
    }

    /** Nourrir Monique : +3 de Grâce, une fois par jour de jeu. Après, elle accepte, mais ça ne rapporte rien. */
    private void nourrir(ServerPlayer joueur, ItemStack nourriture) {
        nourriture.consume(1, joueur);
        heal(4.0F);
        if (level() instanceof ServerLevel monde) {
            monde.sendParticles(ParticleTypes.HEART, getX(), getEyeY() + 0.3, getZ(), 3, 0.3, 0.2, 0.3, 0.0);
        }
        CompteursJour compteurs = Grace.compteurs(joueur);
        if (compteurs.monique()) {
            parler(joueur, "repue");
            return;
        }
        Grace.noterCompteurs(joueur, new CompteursJour(compteurs.jour(), true, compteurs.frappes()));
        parler(joueur, "nourrie");
        Grace.ajouter(joueur, 3, "monique");
    }

    /** Suit son joueur à pas lents ; s'il est trop loin ou hors de vue, elle le rejoint d'un coup. */
    private class SuivreSonFils extends Goal {
        private int prochainChemin;

        SuivreSonFils() {
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            ServerPlayer joueur = joueur();
            return joueur != null && distanceToSqr(joueur) > 9.0;
        }

        @Override
        public boolean canContinueToUse() {
            ServerPlayer joueur = joueur();
            return joueur != null && distanceToSqr(joueur) > 4.0;
        }

        @Override
        public void stop() {
            getNavigation().stop();
        }

        @Override
        public void tick() {
            ServerPlayer joueur = joueur();
            if (joueur == null) {
                return;
            }
            getLookControl().setLookAt(joueur, 10.0F, getMaxHeadXRot());
            if (distanceToSqr(joueur) > 16.0 * 16.0) {
                BlockPos pres = SuiviMonique.placePres(joueur);
                moveTo(pres.getX() + 0.5, pres.getY(), pres.getZ() + 0.5, getYRot(), getXRot());
                getNavigation().stop();
                return;
            }
            if (--prochainChemin <= 0) {
                prochainChemin = 10;
                getNavigation().moveTo(joueur, 0.55);
            }
        }
    }
}
