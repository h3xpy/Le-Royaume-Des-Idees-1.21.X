package com.royaumedesidees.entite;

import com.royaumedesidees.caverne.SouvenirRoyaume;
import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.registre.ModItems;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Une Ombre de la Caverne de Platon : la silhouette d'un prisonnier projetée sur le mur. Tant qu'aucun joueur ne
 * tient la Lanterne de Diogène à moins de 8 blocs, elle est intouchable et ne fait que passer. Révélée par la
 * Lanterne, elle montre sa vraie forme, attaque faiblement et peut être tuée (butin : Pierre d'Ombre).
 */
public class Ombre extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> REVELEE = SynchedEntityData.defineId(Ombre.class, EntityDataSerializers.BOOLEAN);
    public static final double RAYON_LANTERNE = 8;
    /** Les Ombres errent devant l'écran du mur nord. */
    public static final BlockPos CENTRE_ERRANCE = new BlockPos(0, CaverneRoyaume.SOL_Y + 1, CaverneRoyaume.MUR_Z + 8);
    public static final int RAYON_ERRANCE = 16;

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
        goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
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
        if (!hasRestriction()) {
            restrictTo(CENTRE_ERRANCE, RAYON_ERRANCE);
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
}
