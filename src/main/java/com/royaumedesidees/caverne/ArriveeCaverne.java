package com.royaumedesidees.caverne;

import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.registre.ModBlocs;
import com.royaumedesidees.registre.ModPiecesJointes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Arrivée dans la Caverne. À la première entrée, le joueur apparaît au point fixe, face au mur des ombres,
 * entouré de Chaînes de la Caverne qu'il doit briser pour bouger. Les fois suivantes, il arrive libre, à côté.
 */
public final class ArriveeCaverne {
    /** Les arrivées suivantes : 3 blocs à l'est du point fixe, pour ne pas retomber dans de vieilles chaînes. */
    private static final int DECALAGE_LIBRE = 3;

    private ArriveeCaverne() {
    }

    /** Où faire apparaître ce joueur dans la Caverne. */
    public static Vec3 position(Entity entite) {
        boolean dejaVenu = entite.getData(ModPiecesJointes.ENCHAINE);
        int x = CaverneRoyaume.ARRIVEE_X + (dejaVenu ? DECALAGE_LIBRE : 0);
        int z = CaverneRoyaume.ARRIVEE_Z;
        return new Vec3(x + 0.5, CaverneRoyaume.sol(x, z) + 1, z + 0.5);
    }

    /** Après le voyage : à la première entrée, enchaîne le joueur là où il est arrivé. */
    public static void apresArrivee(Entity entite) {
        if (!(entite instanceof Player joueur) || !(entite.level() instanceof ServerLevel niveau) || joueur.getData(ModPiecesJointes.ENCHAINE)) {
            return;
        }
        enchainer(niveau, joueur.blockPosition());
        joueur.setData(ModPiecesJointes.ENCHAINE, true);
        joueur.displayClientMessage(Component.translatable("message.royaumedesidees.caverne.enchaine"), false);
    }

    /**
     * Pose une cage de chaînes autour d'un joueur debout en {@code pieds} : les 8 cases autour, à hauteur des pieds
     * et de la tête, et une au-dessus de la tête. Seules les cases vides sont remplies.
     */
    public static void enchainer(ServerLevel niveau, BlockPos pieds) {
        BlockState chaine = ModBlocs.CHAINE_CAVERNE.get().defaultBlockState();
        for (int dy = 0; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx != 0 || dz != 0) {
                        poserSiVide(niveau, pieds.offset(dx, dy, dz), chaine);
                    }
                }
            }
        }
        poserSiVide(niveau, pieds.above(2), chaine);
    }

    private static void poserSiVide(ServerLevel niveau, BlockPos pos, BlockState etat) {
        if (niveau.getBlockState(pos).isAir()) {
            niveau.setBlockAndUpdate(pos, etat);
        }
    }
}
