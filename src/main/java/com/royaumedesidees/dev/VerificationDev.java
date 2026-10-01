package com.royaumedesidees.dev;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.monde.ReliefRoyaume;
import com.royaumedesidees.registre.ModMonde;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

/**
 * Contrôle automatique réservé au développement : actif seulement si le jeu est lancé avec
 * {@code -Droyaumedesidees.verification=true}. Au démarrage d'un monde, il génère quelques chunks
 * du Royaume et vérifie que le sol est à la hauteur prévue par {@link ReliefRoyaume}.
 * Le résultat est écrit dans le journal ; en jeu normal, cette classe ne fait rien.
 */
public final class VerificationDev {
    private static final int[][] POINTS = {
            {0, 0},        // plaine au-dessus de la Caverne
            {-400, -400},  // Jardin de Milan
            {400, -400},   // Port-Royal
            {480, 470},    // cratère du Puy de Dôme
            {560, 470},    // flanc du Puy de Dôme
            {-500, 480},   // mer d'Hippone
            {-337, 324},   // plateau d'Hippone
            {0, -960},     // falaise nord
            {0, -1100},    // vide au-delà du bord
    };

    public static void activerSiDemande() {
        if (Boolean.getBoolean(RoyaumeDesIdees.MODID + ".verification")) {
            NeoForge.EVENT_BUS.addListener(VerificationDev::verifier);
        }
    }

    private static void verifier(ServerStartedEvent evenement) {
        ServerLevel royaume = evenement.getServer().getLevel(ModMonde.ROYAUME);
        if (royaume == null) {
            RoyaumeDesIdees.LOGGER.error("[verification] ECHEC : la dimension {} n'est pas chargée", ModMonde.ROYAUME.location());
            return;
        }
        int erreurs = 0;
        for (int[] point : POINTS) {
            int x = point[0];
            int z = point[1];
            royaume.getChunk(x >> 4, z >> 4);
            ReliefRoyaume.Colonne colonne = ReliefRoyaume.colonne(x, z);
            int obtenu = royaume.getHeight(Heightmap.Types.OCEAN_FLOOR, x, z) - 1;
            int attendu = colonne.ile() ? colonne.surface() : royaume.getMinBuildHeight() - 1;
            String bloc = royaume.getBlockState(new BlockPos(x, Math.max(obtenu, 0), z)).getBlock().getName().getString();
            boolean ok = obtenu == attendu;
            if (!ok) {
                erreurs++;
            }
            RoyaumeDesIdees.LOGGER.info("[verification] ({}, {}) {} : sol à y={} (attendu {}), bloc {} {}",
                    x, z, colonne.zone(), obtenu, attendu, bloc, ok ? "OK" : "ECHEC");
        }
        RoyaumeDesIdees.LOGGER.info("[verification] Royaume : {}", erreurs == 0 ? "OK" : erreurs + " ECHEC(S)");
    }

    private VerificationDev() {
    }
}
