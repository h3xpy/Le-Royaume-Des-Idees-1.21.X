package com.royaumedesidees.portail;

import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.registre.ModBlocs;
import com.royaumedesidees.registre.ModMonde;
import com.royaumedesidees.registre.ModPiecesJointes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

/**
 * Les deux voyages par portail :
 * <ul>
 *   <li>aller : de n'importe quel portail de bibliothèques vers le point d'arrivée fixe de la Caverne,
 *       en retenant d'où vient le joueur ;</li>
 *   <li>retour : du portail de Pierre d'Ombre vers le portail de départ, ou vers le point d'apparition du
 *       monde s'il a disparu.</li>
 * </ul>
 * Il n'y a jamais de portail généré automatiquement dans le Royaume : un seul point d'arrivée, connu.
 */
public final class VoyageRoyaume {
    /** Les joueurs arrivent tournés vers le nord (orientation 180), face au mur des ombres. */
    public static final float ORIENTATION_ARRIVEE = 180f;

    private VoyageRoyaume() {
    }

    public static DimensionTransition destination(ServerLevel niveau, Entity entite) {
        if (!niveau.dimension().equals(ModMonde.ROYAUME)) {
            ServerLevel royaume = niveau.getServer().getLevel(ModMonde.ROYAUME);
            if (royaume == null) {
                return null;
            }
            entite.setData(ModPiecesJointes.POINT_RETOUR, new PointRetour(true, niveau.dimension(), entite.blockPosition(), entite.getYRot()));
            Vec3 arrivee = new Vec3(CaverneRoyaume.ARRIVEE_X + 0.5, CaverneRoyaume.ARRIVEE_Y, CaverneRoyaume.ARRIVEE_Z + 0.5);
            return new DimensionTransition(royaume, arrivee, Vec3.ZERO, ORIENTATION_ARRIVEE, 0f,
                    DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
        }

        PointRetour retour = entite.getData(ModPiecesJointes.POINT_RETOUR);
        ServerLevel depart = retour.defini() ? niveau.getServer().getLevel(retour.dimension()) : null;
        if (depart != null && portailPresent(depart, retour.pos())) {
            Vec3 pos = Vec3.atBottomCenterOf(retour.pos());
            return new DimensionTransition(depart, pos, Vec3.ZERO, retour.yRot(), 0f,
                    DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
        }
        // Portail de départ détruit (ou inconnu) : retour au point d'apparition du monde.
        ServerLevel overworld = niveau.getServer().getLevel(Level.OVERWORLD);
        return new DimensionTransition(overworld, entite, DimensionTransition.PLAY_PORTAL_SOUND);
    }

    /** Vrai s'il reste un bloc de portail à moins de 2 blocs de la position enregistrée. */
    private static boolean portailPresent(ServerLevel niveau, BlockPos pos) {
        for (BlockPos voisin : BlockPos.betweenClosed(pos.offset(-2, -2, -2), pos.offset(2, 2, 2))) {
            if (niveau.getBlockState(voisin).is(ModBlocs.PORTAIL_ROYAUME.get())) {
                return true;
            }
        }
        return false;
    }
}
