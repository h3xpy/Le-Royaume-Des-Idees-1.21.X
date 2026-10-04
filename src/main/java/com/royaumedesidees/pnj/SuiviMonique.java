package com.royaumedesidees.pnj;

import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.grace.Voie;
import com.royaumedesidees.jardin.Culpabilite;
import com.royaumedesidees.registre.ModEntites;
import com.royaumedesidees.registre.ModMonde;
import com.royaumedesidees.registre.ModPiecesJointes;
import com.royaumedesidees.structures.DonneesStructures;
import com.royaumedesidees.structures.StructuresJardin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.List;

/**
 * La Monique du serveur — il n'y en a qu'une, comme dans l'histoire. Toutes les deux secondes, dans le Royaume :
 * <ol>
 *   <li>on cherche le plus grand pécheur : le joueur sans la Voie du Cœur qui a le plus de Culpabilité ;</li>
 *   <li>Monique reste auprès de celui qu'elle accompagne, sauf si un autre a strictement plus de Culpabilité (pas
 *       d'allers-retours entre deux pécheurs à égalité) ; chacun est prévenu quand elle change ;</li>
 *   <li>si personne n'a besoin d'elle, elle rentre prier au jardin de la villa d'Augustin, où elle vit ;</li>
 *   <li>si elle n'est pas là (jamais apparue, partie avec un chunk déchargé…), elle réapparaît auprès de son
 *       pécheur, ou au jardin si celui-ci est chargé. S'il y en avait plusieurs, les autres s'effacent.</li>
 * </ol>
 */
public final class SuiviMonique {
    /** Où vit Monique : le jardin à colonnade de la villa (Augustin avait l'usage du « hortulus », Confessions, VIII, 8). */
    public static final BlockPos JARDIN = StructuresJardin.POS_JARDIN;

    private SuiviMonique() {
    }

    public static void tick(LevelTickEvent.Post evenement) {
        if (!(evenement.getLevel() instanceof ServerLevel monde) || !monde.dimension().equals(ModMonde.ROYAUME)
                || monde.getGameTime() % 40 != 0) {
            return;
        }
        List<? extends Monique> moniques = monde.getEntities(ModEntites.MONIQUE.get(), m -> true);
        Monique monique = moniques.isEmpty() ? null : moniques.get(0);
        for (int i = 1; i < moniques.size(); i++) {
            moniques.get(i).discard();
        }

        ServerPlayer actuel = monique == null ? null : monique.joueurCible();
        if (actuel != null && !estPecheur(actuel)) {
            actuel = null;
        }
        ServerPlayer pire = null;
        for (ServerPlayer joueur : monde.players()) {
            if (estPecheur(joueur) && (pire == null || Culpabilite.niveau(joueur) > Culpabilite.niveau(pire))) {
                pire = joueur;
            }
        }
        ServerPlayer nouvelle = actuel != null && (pire == null || Culpabilite.niveau(pire) <= Culpabilite.niveau(actuel)) ? actuel : pire;

        if (monique == null) {
            BlockPos ou = nouvelle != null ? placePres(nouvelle)
                    : (DonneesStructures.de(monde).version("villa_augustin") > 0 && monde.isPositionEntityTicking(JARDIN) ? JARDIN : null);
            if (ou == null) {
                return;
            }
            monique = ModEntites.MONIQUE.get().create(monde);
            if (monique == null) {
                return;
            }
            monique.moveTo(ou.getX() + 0.5, ou.getY(), ou.getZ() + 0.5, 0.0F, 0.0F);
            monique.setMaison(JARDIN, 3);
            monde.addFreshEntity(monique);
        }

        ServerPlayer ancienne = monique.joueurCible();
        if (nouvelle != null && monique.distanceToSqr(nouvelle) > 32 * 32) {
            // Son pécheur est parti loin (téléportation, course) : là où elle est restée, le serveur ne la fait plus
            // bouger. On la ramène auprès de lui.
            BlockPos pres = placePres(nouvelle);
            monique.teleportTo(pres.getX() + 0.5, pres.getY(), pres.getZ() + 0.5);
        }
        if (ancienne == nouvelle) {
            return;
        }
        monique.setCible(nouvelle);
        if (nouvelle != null) {
            boolean premiereFois = !nouvelle.getData(ModPiecesJointes.MONIQUE_APPARUE);
            nouvelle.setData(ModPiecesJointes.MONIQUE_APPARUE, true);
            nouvelle.sendSystemMessage(Component.translatable("message.royaumedesidees.monique." + (premiereFois ? "arrive" : "revient")));
        }
        if (ancienne != null && ancienne.level() == monde && Grace.voie(ancienne) == Voie.COEUR) {
            // Son pécheur s'est converti : elle pleure de joie (Confessions, III, 12).
            ancienne.sendSystemMessage(Component.translatable("message.royaumedesidees.monique.consolee"));
        } else if (ancienne != null && ancienne.level() == monde) {
            ancienne.sendSystemMessage(nouvelle != null
                    ? Component.translatable("message.royaumedesidees.monique.quitte", nouvelle.getDisplayName())
                    : Component.translatable("message.royaumedesidees.monique.rentre"));
        }
    }

    /** Un pécheur dont Monique pourrait s'occuper : dans le Royaume, sans la Voie du Cœur, avec de la Culpabilité. */
    private static boolean estPecheur(ServerPlayer joueur) {
        return !joueur.isSpectator() && Grace.dansRoyaume(joueur) && Grace.voie(joueur) != Voie.COEUR
                && Culpabilite.niveau(joueur) > 0;
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
                            && monde.getBlockState(pos.below()).isFaceSturdy(monde, pos.below(), Direction.UP)) {
                        return pos;
                    }
                }
            }
        }
        return centre;
    }
}
