package com.royaumedesidees.entite;

import com.royaumedesidees.caverne.SouvenirRoyaume;
import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.registre.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

/**
 * Une Ombre de la Caverne de Platon. Comme dans l'allégorie, c'est l'ombre d'une statue de bois portée derrière le
 * muret, projetée à plat sur la paroi : elle glisse le long de l'écran du mur des ombres. Tant qu'aucun joueur ne
 * tient la Lanterne de Diogène à moins de 8 blocs, elle est intouchable. Révélée par la Lanterne, elle se détache
 * du mur : ce n'est qu'une statue de bois (Diogène cherchait un homme…), qui attaque faiblement et peut être
 * détruite (butin : Pierre d'Ombre).
 */
public class Ombre extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> REVELEE = SynchedEntityData.defineId(Ombre.class, EntityDataSerializers.BOOLEAN);
    public static final double RAYON_LANTERNE = 8;
    /** Les Ombres glissent contre l'écran du mur nord, entre x = -17 et 17. */
    public static final int Z_ECRAN = CaverneRoyaume.MUR_Z + 5;
    public static final int X_ECRAN = 17;

    public Ombre(EntityType<? extends Ombre> type, Level niveau) {
        super(type, niveau);
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder attributs() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder donnees) {
        super.defineSynchedData(donnees);
        donnees.define(REVELEE, false);
    }

    public boolean estRevelee() {
        return entityData.get(REVELEE);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false) {
            @Override
            public boolean canUse() {
                return estRevelee() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return estRevelee() && super.canContinueToUse();
            }
        });
        goalSelector.addGoal(4, new LongerLEcran());
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                return estRevelee() && super.canUse();
            }
        });
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide || tickCount % 5 != 0) {
            return;
        }
        boolean revelee = lanterneProche();
        entityData.set(REVELEE, revelee);
        if (!revelee && getTarget() != null) {
            setTarget(null);
        }
    }

    /** Vrai si un joueur tient la Lanterne de Diogène (en main ou dans l'autre main) à moins de 8 blocs. */
    private boolean lanterneProche() {
        if (!SouvenirRoyaume.actif(level())) {
            return false;
        }
        return !level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(RAYON_LANTERNE), joueur ->
                joueur.isAlive() && !joueur.isSpectator() && distanceToSqr(joueur) <= RAYON_LANTERNE * RAYON_LANTERNE
                        && (joueur.getMainHandItem().is(ModItems.LANTERNE_DIOGENE.get()) || joueur.getOffhandItem().is(ModItems.LANTERNE_DIOGENE.get()))
        ).isEmpty();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (!estRevelee() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean hurt(DamageSource source, float degats) {
        if (!estRevelee() && source.getEntity() instanceof Player joueur && level() instanceof ServerLevel serveur) {
            // Le coup traverse l'ombre : un peu de fumée, et on explique pourquoi.
            joueur.displayClientMessage(Component.translatable("message.royaumedesidees.ombre.intouchable"), true);
            serveur.sendParticles(ParticleTypes.SMOKE, getX(), getY(1.0), getZ(), 6, 0.2, 0.4, 0.2, 0.01);
        }
        return super.hurt(source, degats);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (source.getEntity() instanceof Player joueur) {
            joueur.displayClientMessage(Component.translatable("message.royaumedesidees.ombre.statue"), true);
        }
    }

    /**
     * Tant qu'elle n'est pas révélée, l'Ombre va et vient le long de l'écran, comme les ombres que les porteurs de
     * statues projettent en marchant derrière le muret. Révélée, elle quitte le mur pour attaquer ; ensuite, elle
     * y retourne.
     */
    private class LongerLEcran extends Goal {
        LongerLEcran() {
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return !estRevelee() && getNavigation().isDone();
        }

        @Override
        public boolean canContinueToUse() {
            return !estRevelee() && !getNavigation().isDone();
        }

        @Override
        public void start() {
            int x = getRandom().nextIntBetweenInclusive(-X_ECRAN, X_ECRAN);
            getNavigation().moveTo(x + 0.5, CaverneRoyaume.sol(x, Z_ECRAN) + 1, Z_ECRAN + 0.5, 0.6);
        }
    }
}
