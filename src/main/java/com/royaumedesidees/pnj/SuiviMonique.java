package com.royaumedesidees.pnj;

import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.jardin.Culpabilite;
import com.royaumedesidees.registre.ModEntites;
import com.royaumedesidees.registre.ModPiecesJointes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Fait apparaître la Monique de chaque joueur et la ramène quand il revient. Toutes les deux secondes, pour un joueur
 * dans le Royaume :
 * <ul>
 *   <li>s'il a de la Culpabilité et que Monique n'est encore jamais venue, elle arrive (une fois pour toutes) ;</li>
 *   <li>si elle est déjà venue mais n'est pas là (retour dans le Royaume, reconnexion, redémarrage), elle revient.</li>
 * </ul>
 */
public final class SuiviMonique {
    private SuiviMonique() {
    }

    public static void tick(PlayerTickEvent.Post evenement) {
        if (!(evenement.getEntity() instanceof ServerPlayer joueur) || joueur.tickCount % 40 != 0
                || joueur.isSpectator() || !Grace.dansRoyaume(joueur)) {
            return;
        }
        boolean apparue = joueur.getData(ModPiecesJointes.MONIQUE_APPARUE);
        if (!apparue) {
            if (Culpabilite.niveau(joueur) <= 0) {
                return;
            }
            joueur.setData(ModPiecesJointes.MONIQUE_APPARUE, true);
            joueur.sendSystemMessage(Component.translatable("message.royaumedesidees.monique.arrive"));
        }
        if (cherche(joueur) == null) {
            faireApparaitre(joueur);
        }
    }

    /** La Monique de ce joueur, si elle est là. */
    public static Monique cherche(ServerPlayer joueur) {
        for (Monique monique : joueur.serverLevel().getEntities(ModEntites.MONIQUE.get(),
                m -> joueur.getUUID().equals(m.proprietaire()))) {
            return monique;
        }
        return null;
    }

    private static void faireApparaitre(ServerPlayer joueur) {
        ServerLevel monde = joueur.serverLevel();
        Monique monique = ModEntites.MONIQUE.get().create(monde);
        if (monique == null) {
            return;
        }
        BlockPos pos = placePres(joueur);
        monique.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, joueur.getYRot() + 180.0F, 0.0F);
        monique.setProprietaire(joueur);
        monde.addFreshEntity(monique);
    }

    /** Une place libre à un ou deux blocs du joueur (deux blocs d'air sur un sol solide), ou sa propre place. */
    public static BlockPos placePres(ServerPlayer joueur) {
        BlockPos centre = joueur.blockPosition();
        ServerLevel monde = joueur.serverLevel();
        for (int r = 2; r >= 1; r--) {
            for (int[] d : new int[][]{{-r, 0}, {r, 0}, {0, -r}, {0, r}, {-r, -r}, {r, r}}) {
                for (int dy = 1; dy >= -2; dy--) {
                    BlockPos pos = centre.offset(d[0], dy, d[1]);
                    if (monde.getBlockState(pos).isAir() && monde.getBlockState(pos.above()).isAir()
                            && monde.getBlockState(pos.below()).isFaceSturdy(monde, pos.below(), net.minecraft.core.Direction.UP)) {
                        return pos;
                    }
                }
            }
        }
        return centre;
    }
}
