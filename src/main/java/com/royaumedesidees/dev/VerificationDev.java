package com.royaumedesidees.dev;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.monde.GenerateurRoyaume;
import com.royaumedesidees.monde.ReliefRoyaume;
import com.royaumedesidees.registre.ModMonde;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

/**
 * Contrôle automatique réservé au développement : actif seulement si le jeu est lancé avec
 * {@code -Droyaumedesidees.verification=true}. Au démarrage d'un monde, il génère quelques chunks
 * du Royaume et vérifie que le sol est à la hauteur et dans le bloc prévus par le générateur.
 * Le résultat est écrit dans le journal ; en jeu normal, cette classe ne fait rien.
 */
public final class VerificationDev {
    private static final int[][] POINTS = {
            {0, 0},        // plaine au-dessus de la Caverne
            {-260, -260},  // Jardin de Milan
            {300, -290},   // butte de l'abbaye, au fond de la vallée de Port-Royal
            {250, -250},   // marais de Port-Royal
            {260, 260},    // sommet enneigé du dôme
            {305, 305},    // limite de la neige (Poêle de Descartes)
            {470, 120},    // cratère d'un petit puy
            {-330, 320},   // mer d'Hippone
            {-208, 203},   // plateau de la ville d'Hippone
            {-420, 110},   // garrigue d'Hippone
            {742, 270},    // îlot flottant
            {0, -900},     // vide au-delà du bord
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
            boolean ok;
            String detail;
            if (!colonne.ile()) {
                ok = true;
                for (int y = royaume.getMinBuildHeight(); y < royaume.getMaxBuildHeight(); y++) {
                    ok &= royaume.getBlockState(new BlockPos(x, y, z)).isAir();
                }
                detail = "colonne vide";
            } else {
                BlockPos sol = new BlockPos(x, colonne.surface(), z);
                BlockState obtenu = royaume.getBlockState(sol);
                BlockState attendu = GenerateurRoyaume.bloc(colonne, ReliefRoyaume.pente(colonne, x, z), x, colonne.surface(), z);
                BlockState dessus = royaume.getBlockState(sol.above());
                boolean dessusLibre = colonne.sousLEau()
                        ? dessus.is(Blocks.WATER)
                        : dessus.isAir() || !dessus.isSolidRender(royaume, sol.above()) || dessus.is(BlockTags.LOGS);
                ok = obtenu == attendu && dessusLibre;
                detail = "sol y=" + colonne.surface() + " " + nom(obtenu) + " (attendu " + nom(attendu) + "), dessus " + nom(dessus);
            }
            if (colonne.ile()) {
                // Le biome à la surface doit être celui de la zone.
                String biome = royaume.getBiome(new BlockPos(x, colonne.surface() + 1, z)).unwrapKey().map(cle -> cle.location().getPath()).orElse("?");
                boolean bonBiome = biome.equals(colonne.zone().name().toLowerCase(java.util.Locale.ROOT));
                ok &= bonBiome;
                detail += ", biome " + biome;
            }
            if (!ok) {
                erreurs++;
            }
            RoyaumeDesIdees.LOGGER.info("[verification] ({}, {}) {} : {} {}", x, z, colonne.zone(), detail, ok ? "OK" : "ECHEC");
        }
        // Le cœur de la Caverne doit être dans son biome.
        String biomeCaverne = royaume.getBiome(new BlockPos(0, 65, 0)).unwrapKey().map(cle -> cle.location().getPath()).orElse("?");
        boolean caverneOk = biomeCaverne.equals("caverne_platon");
        if (!caverneOk) {
            erreurs++;
        }
        RoyaumeDesIdees.LOGGER.info("[verification] (0, 65, 0) biome {} {}", biomeCaverne, caverneOk ? "OK" : "ECHEC");
        RoyaumeDesIdees.LOGGER.info("[verification] Royaume : {}", erreurs == 0 ? "OK" : erreurs + " ECHEC(S)");
    }

    private static String nom(BlockState etat) {
        return etat.getBlock().getName().getString();
    }

    private VerificationDev() {
    }
}
