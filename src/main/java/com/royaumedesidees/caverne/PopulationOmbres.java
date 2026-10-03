package com.royaumedesidees.caverne;

import com.royaumedesidees.entite.Ombre;
import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.registre.ModEntites;
import com.royaumedesidees.registre.ModMonde;
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

    /** Fait apparaître une Ombre au pied de l'écran du mur des ombres. */
    private static void apparaitre(ServerLevel niveau) {
        int x = niveau.random.nextIntBetweenInclusive(-16, 16);
        int z = CaverneRoyaume.MUR_Z + 6 + niveau.random.nextInt(4);
        Ombre ombre = ModEntites.OMBRE.get().create(niveau);
        if (ombre == null) {
            return;
        }
        ombre.moveTo(x + 0.5, CaverneRoyaume.sol(x, z) + 1, z + 0.5, niveau.random.nextFloat() * 360F, 0F);
        ombre.restrictTo(Ombre.CENTRE_ERRANCE, Ombre.RAYON_ERRANCE);
        ombre.finalizeSpawn(niveau, niveau.getCurrentDifficultyAt(ombre.blockPosition()), MobSpawnType.EVENT, null);
        niveau.addFreshEntity(ombre);
    }
}
