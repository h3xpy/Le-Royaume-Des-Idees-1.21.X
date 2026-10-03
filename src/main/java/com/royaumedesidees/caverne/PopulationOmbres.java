package com.royaumedesidees.caverne;

import com.royaumedesidees.entite.Ombre;
import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.registre.ModEntites;
import com.royaumedesidees.registre.ModMonde;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * Tant qu'un joueur est dans la Caverne, il y a toujours quelques Ombres qui errent devant le mur. Une Ombre
 * manquante (tuée à la Lanterne) réapparaît au bout de quelques secondes.
 */
public final class PopulationOmbres {
    public static final int NOMBRE = 6;
    /** La Caverne et un peu de marge autour. */
    private static final AABB CAVERNE = new AABB(-70, 38, -70, 70, 92, 70);

    private PopulationOmbres() {
    }

    public static void tick(LevelTickEvent.Post evenement) {
        if (!(evenement.getLevel() instanceof ServerLevel niveau) || !niveau.dimension().equals(ModMonde.ROYAUME)
                || niveau.getGameTime() % 100 != 0) {
            return;
        }
        boolean joueurDansLaCaverne = false;
        for (ServerPlayer joueur : niveau.players()) {
            if (CAVERNE.contains(joueur.position())) {
                joueurDansLaCaverne = true;
                break;
            }
        }
        if (!joueurDansLaCaverne) {
            return;
        }
        int presentes = niveau.getEntitiesOfClass(Ombre.class, CAVERNE).size();
        if (presentes < NOMBRE) {
            apparaitre(niveau);
        }
    }

    /** Fait apparaître une Ombre contre l'écran du mur des ombres, sur un sol libre (quelques essais au plus). */
    private static void apparaitre(ServerLevel niveau) {
        for (int essai = 0; essai < 8; essai++) {
            int x = niveau.random.nextIntBetweenInclusive(-Ombre.X_ECRAN, Ombre.X_ECRAN);
            BlockPos pieds = new BlockPos(x, CaverneRoyaume.sol(x, Ombre.Z_ECRAN) + 1, Ombre.Z_ECRAN);
            if (niveau.getBlockState(pieds).isAir() && niveau.getBlockState(pieds.above()).isAir()
                    && niveau.getBlockState(pieds.below()).isSolidRender(niveau, pieds.below())) {
                apparaitre(niveau, pieds);
                return;
            }
        }
    }

    private static void apparaitre(ServerLevel niveau, BlockPos pieds) {
        Ombre ombre = ModEntites.OMBRE.get().create(niveau);
        if (ombre == null) {
            return;
        }
        // Tournée vers l'est ou l'ouest : elle glisse le long de l'écran, de profil.
        ombre.moveTo(pieds.getX() + 0.5, pieds.getY(), pieds.getZ() + 0.5, niveau.random.nextBoolean() ? 90F : 270F, 0F);
        ombre.finalizeSpawn(niveau, niveau.getCurrentDifficultyAt(ombre.blockPosition()), MobSpawnType.EVENT, null);
        boolean ajoutee = niveau.addFreshEntity(ombre);
        com.royaumedesidees.RoyaumeDesIdees.LOGGER.debug("[ombres] apparition en {} : {}, dans un bloc : {}",
                ombre.blockPosition().toShortString(), ajoutee, !niveau.getBlockState(ombre.blockPosition()).isAir());
    }
}
