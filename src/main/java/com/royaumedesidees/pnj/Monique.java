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
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.UUID;

/**
 * Sainte Monique, la mère d'Augustin, qui a prié et pleuré des années pour sa conversion (« il est impossible que le
 * fils de tant de larmes périsse », Confessions, III, 12).
 * <p>
 * Il n'y a qu'une Monique sur le serveur. Elle vit au jardin de la villa d'Augustin, et va pleurer auprès du plus grand
 * pécheur du Royaume (voir {@link SuiviMonique}) : elle le suit, se téléporte s'il s'éloigne, et pleure, plus fort à
 * partir de sa Culpabilité III. Quand plus personne n'a besoin d'elle, elle rentre prier au jardin, en silence.
 * Invincible ; frappée, elle pleure plus fort. Tout le monde peut la nourrir, une fois par jour, contre de la Grâce.
 * Elle n'est jamais sauvegardée : elle réapparaît au jardin ou auprès de son pécheur.
 */
public class Monique extends PnjRoyaume {
    /** Durée du plus long des trois extraits de sanglots (5,2 s), arrondie : jamais deux sanglots à la fois. */
    private static final int DUREE_SANGLOT = 110;

    private UUID cible;
    private long prochainSanglot;

    public Monique(EntityType<? extends Monique> type, Level niveau) {
        super(type, niveau, ChatFormatting.LIGHT_PURPLE);
    }

    /** Le pécheur auprès de qui elle pleure (null : elle prie au jardin). */
    public UUID cible() {
        return cible;
    }

    public void setCible(ServerPlayer joueur) {
        this.cible = joueur == null ? null : joueur.getUUID();
        if (cible == null && maison() != null) {
            restrictTo(maison(), 3);
        } else {
            clearRestriction();
        }
    }

    ServerPlayer joueurCible() {
        return cible == null || !(level() instanceof ServerLevel monde) ? null : (ServerPlayer) monde.getPlayerByUUID(cible);
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
        goalSelector.addGoal(1, new SuivreLePecheur());
        goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 0.6));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.3));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    /** Vrai tant qu'elle accompagne un pécheur. */
    public boolean pleure() {
        return joueurCible() != null;
    }

    @Override
    public void tick() {
        super.tick();
        if (!(level() instanceof ServerLevel monde)) {
            return;
        }
        ServerPlayer joueur = joueurCible();
        if (joueur == null) {
            if (cible != null) {
                setCible(null);
            }
            return;
        }
        if (tickCount % 15 == 0) {
            monde.sendParticles(ParticleTypes.FALLING_WATER, getX(), getEyeY() - 0.15, getZ(), 2, 0.15, 0.05, 0.15, 0.0);
        }
        if (monde.getGameTime() >= prochainSanglot) {
            sangloter(monde, Culpabilite.niveau(joueur) >= 3);
        }
    }

    /** Un sanglot, puis un silence de 8 à 20 secondes. */
    private void sangloter(ServerLevel monde, boolean plusFort) {
        float volume = plusFort ? 1.2F : 0.6F;
        monde.playSound(null, getX(), getY(), getZ(), ModSons.SANGLOTS.get(), SoundSource.NEUTRAL, volume, 0.95F + getRandom().nextFloat() * 0.1F);
        prochainSanglot = monde.getGameTime() + DUREE_SANGLOT + 160 + getRandom().nextInt(240);
    }

    @Override
    protected void reagirCoup(ServerPlayer joueur) {
        parler(joueur, "coup");
        if (level() instanceof ServerLevel monde && monde.getGameTime() >= prochainSanglot - 240) {
            sangloter(monde, true);
        }
    }

    @Override
    protected void parleAvec(ServerPlayer joueur) {
        ItemStack main = joueur.getMainHandItem();
        if (main.has(DataComponents.FOOD)) {
            nourrir(joueur, main);
        } else if (joueur.getUUID().equals(cible)) {
            parler(joueur, "bonjour");
        } else if (Grace.voie(joueur) == Voie.COEUR) {
            parler(joueur, "apaisee");
        } else {
            parler(joueur, pleure() ? "pas_mon_fils" : "priere");
        }
    }

    /** Nourrir Monique : +3 de Grâce, une fois par jour de jeu et par joueur. Après, elle remercie, sans plus. */
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

    /** Suit son pécheur à pas lents ; s'il est trop loin, elle le rejoint d'un coup. */
    private class SuivreLePecheur extends Goal {
        private int prochainChemin;

        SuivreLePecheur() {
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            ServerPlayer joueur = joueurCible();
            return joueur != null && distanceToSqr(joueur) > 9.0;
        }

        @Override
        public boolean canContinueToUse() {
            ServerPlayer joueur = joueurCible();
            return joueur != null && distanceToSqr(joueur) > 4.0;
        }

        @Override
        public void stop() {
            getNavigation().stop();
        }

        @Override
        public void tick() {
            ServerPlayer joueur = joueurCible();
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
