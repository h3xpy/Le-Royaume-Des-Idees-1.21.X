package com.royaumedesidees.dev;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.portail.CadrePortail;
import com.royaumedesidees.monde.GenerateurRoyaume;
import com.royaumedesidees.monde.ReliefRoyaume;
import com.royaumedesidees.registre.ModBlocs;
import com.royaumedesidees.registre.ModMonde;
import com.royaumedesidees.structures.DonneesStructures;
import com.royaumedesidees.structures.PoseurStructures;
import com.royaumedesidees.structures.StructureRoyaume;
import com.royaumedesidees.structures.StructuresRoyaume;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import java.util.Optional;

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
        // La Caverne : salle creusée, sol sous le point d'arrivée, tunnel ouvert.
        int[][] vides = {{0, 60, 0}, {CaverneRoyaume.ARRIVEE_X, CaverneRoyaume.ARRIVEE_Y, CaverneRoyaume.ARRIVEE_Z},
                {CaverneRoyaume.ARRIVEE_X, CaverneRoyaume.ARRIVEE_Y + 1, CaverneRoyaume.ARRIVEE_Z}};
        for (int[] p : vides) {
            boolean libre = royaume.getBlockState(new BlockPos(p[0], p[1], p[2])).isAir();
            erreurs += libre ? 0 : 1;
            RoyaumeDesIdees.LOGGER.info("[verification] Caverne ({}, {}, {}) vide : {}", p[0], p[1], p[2], libre ? "OK" : "ECHEC");
        }
        BlockPos solArrivee = new BlockPos(CaverneRoyaume.ARRIVEE_X, CaverneRoyaume.ARRIVEE_Y - 1, CaverneRoyaume.ARRIVEE_Z);
        boolean solOk = royaume.getBlockState(solArrivee).isSolidRender(royaume, solArrivee);
        erreurs += solOk ? 0 : 1;
        RoyaumeDesIdees.LOGGER.info("[verification] Caverne sol d'arrivée {} : {}", nom(royaume.getBlockState(solArrivee)), solOk ? "OK" : "ECHEC");
        for (int z = CaverneRoyaume.TUNNEL_DEBUT_Z; z < 200; z += 15) {
            int x = (int) Math.round(CaverneRoyaume.axeTunnel(z));
            int surface = ReliefRoyaume.colonne(x, z).surface();
            int sol = CaverneRoyaume.solTunnel(z);
            if (sol >= surface) {
                RoyaumeDesIdees.LOGGER.info("[verification] Tunnel : sortie à l'air libre vers z = {} (sol y={})", z, surface);
                break;
            }
            royaume.getChunk(x >> 4, z >> 4);
            boolean libre = royaume.getBlockState(new BlockPos(x, sol + 1, z)).isAir() && royaume.getBlockState(new BlockPos(x, sol + 2, z)).isAir();
            boolean plancher = !royaume.getBlockState(new BlockPos(x, sol, z)).isAir();
            erreurs += libre && plancher ? 0 : 1;
            RoyaumeDesIdees.LOGGER.info("[verification] Tunnel ({}, {}, {}) : {}", x, sol + 1, z, libre && plancher ? "OK" : "ECHEC");
        }
        // Versions lues dans la sauvegarde, avant toute pose (0 = jamais posée dans ce monde).
        DonneesStructures lues = DonneesStructures.de(royaume);
        RoyaumeDesIdees.LOGGER.info("[verification] Versions lues au chargement : caverne={}, portail_retour={}",
                lues.version("caverne"), lues.version("portail_retour"));
        // Structures : pose forcée, contrôle de quelques blocs, puis casse et repose d'un bloc du cadre.
        for (StructureRoyaume structure : StructuresRoyaume.TOUTES) {
            PoseurStructures.poser(royaume, structure, true);
            boolean notee = DonneesStructures.de(royaume).version(structure.id()) == structure.version();
            erreurs += notee ? 0 : 1;
            RoyaumeDesIdees.LOGGER.info("[verification] Structure {} posée et notée v{} : {}", structure.id(), structure.version(), notee ? "OK" : "ECHEC");
        }
        BlockPos feu = new BlockPos(0, CaverneRoyaume.sol(0, CaverneRoyaume.FEU_Z) + 1, CaverneRoyaume.FEU_Z);
        BlockPos ecran = new BlockPos(0, 55, CaverneRoyaume.MUR_Z + 4);
        BlockPos cadre = new BlockPos(StructuresRoyaume.PORTAIL_X, StructuresRoyaume.PORTAIL_Y, StructuresRoyaume.PORTAIL_Z);
        BlockPos interieur = cadre.offset(1, 1, 0);
        erreurs += controle(royaume, feu, Blocks.CAMPFIRE, "feu de la Caverne");
        erreurs += controle(royaume, ecran, Blocks.CALCITE, "écran du mur des ombres");
        erreurs += controle(royaume, cadre, ModBlocs.PIERRE_OMBRE_TAILLEE.get(), "cadre du portail de retour");
        erreurs += controle(royaume, interieur, ModBlocs.PORTAIL_ROYAUME.get(), "intérieur du portail de retour");
        royaume.setBlock(cadre, Blocks.AIR.defaultBlockState(), 2);
        PoseurStructures.poser(royaume, StructuresRoyaume.PORTAIL_RETOUR, true);
        erreurs += controle(royaume, cadre, ModBlocs.PIERRE_OMBRE_TAILLEE.get(), "cadre reposé après casse");

        erreurs += verifierPortails(evenement.getServer().overworld());
        erreurs += verifierButinVillage(evenement.getServer().overworld());

        // Le cœur de la Caverne doit être dans son biome.
        String biomeCaverne = royaume.getBiome(new BlockPos(0, 65, 0)).unwrapKey().map(cle -> cle.location().getPath()).orElse("?");
        boolean caverneOk = biomeCaverne.equals("caverne_platon");
        if (!caverneOk) {
            erreurs++;
        }
        RoyaumeDesIdees.LOGGER.info("[verification] (0, 65, 0) biome {} {}", biomeCaverne, caverneOk ? "OK" : "ECHEC");
        RoyaumeDesIdees.LOGGER.info("[verification] Royaume : {}", erreurs == 0 ? "OK" : erreurs + " ECHEC(S)");
    }

    /** Tire 1000 coffres de maison de village : Tolle, Lege doit sortir dans environ 15 % d'entre eux. */
    private static int verifierButinVillage(ServerLevel overworld) {
        net.minecraft.world.level.storage.loot.LootTable table = overworld.getServer().reloadableRegistries()
                .getLootTable(net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_PLAINS_HOUSE);
        int avecLivre = 0;
        for (int i = 0; i < 1000; i++) {
            net.minecraft.world.level.storage.loot.LootParams parametres = new net.minecraft.world.level.storage.loot.LootParams.Builder(overworld)
                    .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, net.minecraft.world.phys.Vec3.ZERO)
                    .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.CHEST);
            if (table.getRandomItems(parametres).stream().anyMatch(pile -> pile.is(com.royaumedesidees.registre.ModItems.TOLLE_LEGE.get()))) {
                avecLivre++;
            }
        }
        boolean ok = avecLivre >= 100 && avecLivre <= 200;
        RoyaumeDesIdees.LOGGER.info("[verification] Tolle, Lege dans {} coffres de village sur 1000 : {}", avecLivre, ok ? "OK" : "ECHEC");
        return ok ? 0 : 1;
    }

    /** Intérieur du portail de test construit dans l'Overworld, pour le voyage de {@link VisiteDev}. */
    public static volatile BlockPos portailTest;

    /**
     * Construit deux cadres de bibliothèques près du point d'apparition de l'Overworld : vérifie qu'un cadre sans
     * toutes ses lanternes refuse de s'allumer, qu'il s'allume avec, et qu'un portail s'éteint si on casse son cadre.
     */
    private static int verifierPortails(ServerLevel overworld) {
        int erreurs = 0;
        BlockPos spawn = overworld.getSharedSpawnPos();
        BlockPos premier = cadreDeTest(overworld, spawn.offset(8, 0, 8), false);
        boolean refuse = CadrePortail.trouverPourAllumage(overworld, premier).isEmpty();
        erreurs += refuse ? 0 : 1;
        RoyaumeDesIdees.LOGGER.info("[verification] Portail sans sa 4e lanterne refusé : {}", refuse ? "OK" : "ECHEC");
        overworld.setBlockAndUpdate(premier.offset(-1, 0, 0), Blocks.LANTERN.defaultBlockState());
        Optional<CadrePortail> cadre = CadrePortail.trouverPourAllumage(overworld, premier.offset(0, 2, 0));
        cadre.ifPresent(c -> c.allumer(overworld));
        BlockPos interieur = premier.offset(1, 1, 0);
        erreurs += controle(overworld, interieur, ModBlocs.PORTAIL_ROYAUME.get(), "portail allumé dans l'Overworld");
        portailTest = interieur;

        BlockPos second = cadreDeTest(overworld, spawn.offset(8, 0, 16), true);
        CadrePortail.trouverPourAllumage(overworld, second).ifPresent(c -> c.allumer(overworld));
        erreurs += controle(overworld, second.offset(1, 1, 0), ModBlocs.PORTAIL_ROYAUME.get(), "second portail allumé");
        overworld.setBlockAndUpdate(second.offset(0, 2, 0), Blocks.AIR.defaultBlockState());
        erreurs += controle(overworld, second.offset(1, 2, 0), Blocks.AIR, "second portail éteint après casse du cadre");
        return erreurs;
    }

    /**
     * Cadre de bibliothèques de 4 sur 5 dans le plan z, posé au sol, avec ses lanternes (sauf celle du coin bas
     * gauche si {@code complet} est faux). Renvoie le coin bas gauche du cadre.
     */
    private static BlockPos cadreDeTest(ServerLevel niveau, BlockPos pres, boolean complet) {
        niveau.getChunk(pres.getX() >> 4, pres.getZ() >> 4);
        int y = niveau.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pres.getX(), pres.getZ());
        BlockPos origine = new BlockPos(pres.getX(), y, pres.getZ());
        for (BlockPos pos : BlockPos.betweenClosed(origine.offset(-2, 0, -2), origine.offset(5, 7, 2))) {
            niveau.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
        for (BlockPos pos : BlockPos.betweenClosed(origine.offset(-2, -1, -2), origine.offset(5, -1, 2))) {
            niveau.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0 || i == 3 || j == 0 || j == 4) {
                    niveau.setBlockAndUpdate(origine.offset(i, j, 0), Blocks.BOOKSHELF.defaultBlockState());
                }
            }
        }
        niveau.setBlockAndUpdate(origine.offset(0, 5, 0), Blocks.LANTERN.defaultBlockState());
        niveau.setBlockAndUpdate(origine.offset(3, 5, 0), Blocks.SOUL_LANTERN.defaultBlockState());
        niveau.setBlockAndUpdate(origine.offset(4, 0, 0), Blocks.LANTERN.defaultBlockState());
        if (complet) {
            niveau.setBlockAndUpdate(origine.offset(-1, 0, 0), Blocks.LANTERN.defaultBlockState());
        }
        return origine;
    }

    private static int controle(ServerLevel niveau, BlockPos pos, net.minecraft.world.level.block.Block attendu, String quoi) {
        BlockState etat = niveau.getBlockState(pos);
        boolean ok = etat.is(attendu);
        RoyaumeDesIdees.LOGGER.info("[verification] {} en {} : {} {}", quoi, pos.toShortString(), nom(etat), ok ? "OK" : "ECHEC");
        return ok ? 0 : 1;
    }

    private static String nom(BlockState etat) {
        return etat.getBlock().getName().getString();
    }

    private VerificationDev() {
    }
}
