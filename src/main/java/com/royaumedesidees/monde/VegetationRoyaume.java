package com.royaumedesidees.monde;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Végétation du Royaume : herbes, fleurs, plantes d'eau, neige, buissons et petits arbres, choisis d'après
 * les vrais lieux. Comme le relief, tout dépend uniquement des coordonnées (jamais de la seed) :
 * un chunk régénéré serait identique. <b>Ne plus modifier après la v0.1</b>, comme {@link ReliefRoyaume}.
 *
 * <ul>
 *   <li>Jardin de Milan (villa italienne) : cyprès, pins parasols, lauriers, coquelicots et marguerites ;</li>
 *   <li>Port-Royal (marais du Rhodon) : saules, roseaux, nénuphars, herbiers, fougères, aucune fleur ;</li>
 *   <li>Puy de Dôme (Auvergne) : prairie rase, bleuets, épicéas et hêtres au pied du dôme ;</li>
 *   <li>Hippone (côte d'Afrique du Nord) : garrigue, oliviers, quelques pins, lentisques, allium sauvage.</li>
 * </ul>
 *
 * <p>Les arbres sont placés sur une grille : au plus un arbre par case, à une position fixée par la case.
 * Un chunk dessine aussi les morceaux des arbres voisins qui débordent chez lui.
 */
public final class VegetationRoyaume {
    /** Taille d'une case de la grille des arbres. */
    private static final int CASE = 11;

    /** Pas d'arbres près des emplacements réservés aux structures (centre, abbaye, ville). */
    private static final int[][] SANS_ARBRES = {
            {0, 0, 170},
            {ReliefRoyaume.VALLEE_X, ReliefRoyaume.VALLEE_Z, 70},
            {ReliefRoyaume.VILLE_X, ReliefRoyaume.VILLE_Z, 100},
    };

    private enum Arbre { CYPRES, PIN_PARASOL, SAULE, EPICEA, HETRE, OLIVIER }

    private VegetationRoyaume() {
    }

    public static void decorer(ChunkAccess chunk, Heightmap fondOcean, Heightmap surfaceMonde) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        Poseur poseur = new Poseur(chunk, fondOcean, surfaceMonde);
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                plantes(poseur, minX + lx, minZ + lz);
            }
        }
        // Les couronnes débordent d'au plus 3 blocs du tronc.
        for (int cx = Math.floorDiv(minX - 3, CASE); cx <= Math.floorDiv(minX + 18, CASE); cx++) {
            for (int cz = Math.floorDiv(minZ - 3, CASE); cz <= Math.floorDiv(minZ + 18, CASE); cz++) {
                arbreDeLaCase(poseur, cx, cz);
            }
        }
    }

    /** Une plante, un buisson ou de la neige au-dessus du sol d'une colonne. */
    private static void plantes(Poseur poseur, int x, int z) {
        ReliefRoyaume.Colonne colonne = ReliefRoyaume.colonne(x, z);
        if (!colonne.ile() || colonne.rebord() > 4) {
            return;
        }
        int y = colonne.surface() + 1;
        int tirage = hachage(x, z, 17) % 1000;
        if (colonne.sousLEau()) {
            if (colonne.zoneSol() == Zone.PORT_ROYAL) {
                plantesDEau(poseur, colonne, x, z, tirage);
            }
            return;
        }
        BlockState sol = poseur.lire(x, colonne.surface(), z);
        boolean herbe = sol.is(Blocks.GRASS_BLOCK);

        switch (colonne.zoneSol()) {
            case JARDIN_MILAN -> {
                if (!herbe) {
                    return;
                }
                if (tirage < 260) {
                    poseur.poser(x, y, z, Blocks.SHORT_GRASS);
                } else if (tirage < 285) {
                    poseur.poserDouble(x, y, z, Blocks.TALL_GRASS);
                } else if (tirage < 335) {
                    Block[] fleurs = {Blocks.POPPY, Blocks.POPPY, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.DANDELION};
                    poseur.poser(x, y, z, fleurs[hachage(x, z, 23) % fleurs.length]);
                } else if (tirage < 340) {
                    poseur.poserDouble(x, y, z, Blocks.ROSE_BUSH);
                } else if (tirage < 347) {
                    buisson(poseur, x, y, z);                    // laurier
                }
            }
            case PORT_ROYAL -> {
                // Aucune fleur à Port-Royal. Roseaux au bord de l'eau, herbes hautes et fougères dans le marais.
                if (tirage < 450 && bordDEau(x, z) && (herbe || sol.is(Blocks.MUD))) {
                    int hauteur = 1 + hachage(x, z, 31) % 3;
                    for (int i = 0; i < hauteur; i++) {
                        poseur.poser(x, y + i, z, Blocks.SUGAR_CANE);
                    }
                    return;
                }
                if (!herbe) {
                    return;
                }
                if (tirage < 330) {
                    poseur.poser(x, y, z, Blocks.SHORT_GRASS);
                } else if (tirage < 430) {
                    poseur.poserDouble(x, y, z, Blocks.TALL_GRASS);
                } else if (tirage < 480) {
                    poseur.poser(x, y, z, Blocks.FERN);
                } else if (tirage < 500) {
                    poseur.poserDouble(x, y, z, Blocks.LARGE_FERN);
                }
            }
            case PUY_DE_DOME -> {
                if (colonne.surface() >= ReliefRoyaume.NEIGE_Y) {
                    poseur.poser(x, y, z, Blocks.SNOW);
                    return;
                }
                if (!herbe) {
                    return;
                }
                if (tirage < 380) {
                    poseur.poser(x, y, z, Blocks.SHORT_GRASS);
                } else if (tirage < 400 && colonne.altitudeDome() < 0.3) {
                    poseur.poser(x, y, z, Blocks.FERN);
                } else if (tirage < 412) {
                    poseur.poser(x, y, z, hachage(x, z, 23) % 2 == 0 ? Blocks.CORNFLOWER : Blocks.AZURE_BLUET);
                }
            }
            case HIPPONE -> {
                if (sol.is(Blocks.COARSE_DIRT)) {
                    if (tirage < 300) {
                        poseur.poser(x, y, z, Blocks.DEAD_BUSH);
                    }
                    return;
                }
                if (!herbe) {
                    return;
                }
                if (tirage < 170) {
                    poseur.poser(x, y, z, Blocks.SHORT_GRASS);
                } else if (tirage < 180) {
                    poseur.poserDouble(x, y, z, Blocks.TALL_GRASS);
                } else if (tirage < 195) {
                    poseur.poser(x, y, z, Blocks.ALLIUM);        // ail sauvage de la garrigue
                } else if (tirage < 210) {
                    buisson(poseur, x, y, z);                    // lentisque
                }
            }
        }
    }

    /** Nénuphars sur l'eau peu profonde, herbiers dans les étangs. */
    private static void plantesDEau(Poseur poseur, ReliefRoyaume.Colonne colonne, int x, int z, int tirage) {
        int profondeur = ReliefRoyaume.NIVEAU_MER - colonne.surface();
        if (profondeur <= 2 && tirage < 120) {
            poseur.poser(x, ReliefRoyaume.NIVEAU_MER + 1, z, Blocks.LILY_PAD);
        } else if (profondeur >= 2 && tirage < 350) {
            poseur.poser(x, colonne.surface() + 1, z, Blocks.SEAGRASS);
        }
    }

    /** Petit buisson persistant d'un ou deux blocs de feuilles. */
    private static void buisson(Poseur poseur, int x, int y, int z) {
        BlockState feuilles = feuilles(Blocks.OAK_LEAVES);
        poseur.poser(x, y, z, feuilles);
        if (hachage(x, z, 29) % 2 == 0) {
            poseur.poserSiLibre(x, y + 1, z, feuilles);
        }
    }

    private static boolean bordDEau(int x, int z) {
        return ReliefRoyaume.colonne(x + 1, z).sousLEau() || ReliefRoyaume.colonne(x - 1, z).sousLEau()
                || ReliefRoyaume.colonne(x, z + 1).sousLEau() || ReliefRoyaume.colonne(x, z - 1).sousLEau();
    }

    private static void arbreDeLaCase(Poseur poseur, int cx, int cz) {
        int h = hachage(cx, cz, 41);
        // Position dans la case, à 2 blocs des bords pour que deux arbres voisins ne se touchent pas.
        int x = cx * CASE + 2 + (h >> 7) % (CASE - 4);
        int z = cz * CASE + 2 + (h >> 14) % (CASE - 4);
        ReliefRoyaume.Colonne colonne = ReliefRoyaume.colonne(x, z);
        if (!colonne.ile() || colonne.sousLEau() || colonne.rebord() > 2 || colonne.affleurement() || colonne.petitPuy()) {
            return;
        }
        int chance = h % 100;
        int choix = (h >> 21) % 100;
        Arbre arbre = switch (colonne.zoneSol()) {
            case JARDIN_MILAN -> chance < 20 ? (choix < 55 ? Arbre.CYPRES : Arbre.PIN_PARASOL) : null;
            // Saules seulement dans le fond humide de la vallée.
            case PORT_ROYAL -> chance < 22 && colonne.surface() <= ReliefRoyaume.NIVEAU_MER + 3 ? Arbre.SAULE : null;
            case PUY_DE_DOME -> chance < 24 && colonne.altitudeDome() < 0.3 ? (choix < 60 ? Arbre.EPICEA : Arbre.HETRE) : null;
            case HIPPONE -> chance < 26 && colonne.surface() > ReliefRoyaume.NIVEAU_MER + 3 ? (choix < 80 ? Arbre.OLIVIER : Arbre.PIN_PARASOL) : null;
        };
        if (arbre == null) {
            return;
        }
        for (int[] interdit : SANS_ARBRES) {
            long dx = x - interdit[0];
            long dz = z - interdit[1];
            if (dx * dx + dz * dz < (long) interdit[2] * interdit[2]) {
                return;
            }
        }
        int base = colonne.surface() + 1;
        int variante = h >> 24;
        switch (arbre) {
            case CYPRES -> cypres(poseur, x, base, z, variante);
            case PIN_PARASOL -> pinParasol(poseur, x, base, z, variante);
            case SAULE -> saule(poseur, x, base, z, variante);
            case EPICEA -> epicea(poseur, x, base, z, variante);
            case HETRE -> hetre(poseur, x, base, z, variante);
            case OLIVIER -> olivier(poseur, x, base, z, variante);
        }
    }

    /** Cyprès d'Italie : colonne étroite et sombre. */
    private static void cypres(Poseur poseur, int x, int base, int z, int variante) {
        BlockState tronc = Blocks.SPRUCE_LOG.defaultBlockState();
        BlockState feuilles = feuilles(Blocks.SPRUCE_LEAVES);
        int hauteur = 7 + variante % 3;
        for (int y = base + 1; y <= base + hauteur; y++) {
            boolean large = y > base + 1 && y < base + hauteur - 1;
            for (int[] d : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                if (large) {
                    poseur.poserSiLibre(x + d[0], y, z + d[1], feuilles);
                }
            }
        }
        poseur.poserSiLibre(x, base + hauteur, z, feuilles);
        poseur.poserSiLibre(x, base + hauteur + 1, z, feuilles);
        for (int y = base; y < base + hauteur; y++) {
            poseur.poser(x, y, z, tronc);
        }
    }

    /** Pin parasol : long tronc nu, couronne large et plate. */
    private static void pinParasol(Poseur poseur, int x, int base, int z, int variante) {
        BlockState tronc = Blocks.SPRUCE_LOG.defaultBlockState();
        BlockState feuilles = feuilles(Blocks.OAK_LEAVES);
        int hauteur = 6 + variante % 2;
        int couronne = base + hauteur;
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                int distance = dx * dx + dz * dz;
                if (distance <= 10) {
                    poseur.poserSiLibre(x + dx, couronne, z + dz, feuilles);
                }
                if (distance <= 4) {
                    poseur.poserSiLibre(x + dx, couronne + 1, z + dz, feuilles);
                }
            }
        }
        for (int y = base; y < couronne; y++) {
            poseur.poser(x, y, z, tronc);
        }
    }

    /** Saule des marais : tronc court, couronne ronde dont les bords retombent. */
    private static void saule(Poseur poseur, int x, int base, int z, int variante) {
        BlockState tronc = Blocks.OAK_LOG.defaultBlockState();
        BlockState feuilles = feuilles(Blocks.OAK_LEAVES);
        int sommet = base + 4 + variante % 2;
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                int distance = dx * dx + dz * dz;
                if (distance > 10) {
                    continue;
                }
                poseur.poserSiLibre(x + dx, sommet, z + dz, feuilles);
                if (distance <= 4) {
                    poseur.poserSiLibre(x + dx, sommet + 1, z + dz, feuilles);
                }
                // Rameaux qui pendent sur le pourtour.
                if (distance >= 7 && hachage(x + dx, z + dz, 53) % 3 != 0) {
                    int longueur = 1 + hachage(x + dx, z + dz, 59) % 3;
                    for (int i = 1; i <= longueur; i++) {
                        poseur.poserSiLibre(x + dx, sommet - i, z + dz, feuilles);
                    }
                }
            }
        }
        for (int y = base; y < sommet; y++) {
            poseur.poser(x, y, z, tronc);
        }
    }

    /** Petit épicéa en cône. */
    private static void epicea(Poseur poseur, int x, int base, int z, int variante) {
        BlockState tronc = Blocks.SPRUCE_LOG.defaultBlockState();
        BlockState feuilles = feuilles(Blocks.SPRUCE_LEAVES);
        int hauteur = 6 + variante % 2;
        int[] rayons = {2, 1, 2, 1, 1, 0};
        for (int i = 0; i < rayons.length; i++) {
            int y = base + hauteur - rayons.length + i;
            int rayon = rayons[rayons.length - 1 - i];
            for (int dx = -rayon; dx <= rayon; dx++) {
                for (int dz = -rayon; dz <= rayon; dz++) {
                    if (rayon > 0 && Math.abs(dx) == rayon && Math.abs(dz) == rayon) {
                        continue;
                    }
                    poseur.poserSiLibre(x + dx, y, z + dz, feuilles);
                }
            }
        }
        poseur.poserSiLibre(x, base + hauteur, z, feuilles);
        for (int y = base; y < base + hauteur; y++) {
            poseur.poser(x, y, z, tronc);
        }
    }

    /** Hêtre : couronne ronde sur un tronc moyen. */
    private static void hetre(Poseur poseur, int x, int base, int z, int variante) {
        BlockState tronc = Blocks.OAK_LOG.defaultBlockState();
        BlockState feuilles = feuilles(Blocks.OAK_LEAVES);
        int sommet = base + 5 + variante % 2;
        for (int dy = -2; dy <= 1; dy++) {
            int rayon = dy == 1 ? 1 : 2;
            for (int dx = -rayon; dx <= rayon; dx++) {
                for (int dz = -rayon; dz <= rayon; dz++) {
                    boolean coin = Math.abs(dx) == rayon && Math.abs(dz) == rayon;
                    if (coin && (dy == 1 || hachage(x + dx, z + dz, dy + 60) % 2 == 0)) {
                        continue;
                    }
                    poseur.poserSiLibre(x + dx, sommet + dy, z + dz, feuilles);
                }
            }
        }
        for (int y = base; y < sommet; y++) {
            poseur.poser(x, y, z, tronc);
        }
    }

    /** Olivier : tronc court et tordu d'acacia (écorce grise), couronne large et plate. */
    private static void olivier(Poseur poseur, int x, int base, int z, int variante) {
        BlockState tronc = Blocks.ACACIA_LOG.defaultBlockState();
        BlockState feuilles = feuilles(Blocks.OAK_LEAVES);
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int[] penche = directions[variante % 4];
        int hx = x + penche[0];
        int hz = z + penche[1];
        int couronne = base + 3;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (Math.abs(dx) == 2 && Math.abs(dz) == 2) {
                    continue;
                }
                poseur.poserSiLibre(hx + dx, couronne, hz + dz, feuilles);
                if (Math.abs(dx) + Math.abs(dz) <= 1) {
                    poseur.poserSiLibre(hx + dx, couronne + 1, hz + dz, feuilles);
                }
            }
        }
        poseur.poser(x, base, z, tronc);
        poseur.poser(x, base + 1, z, tronc);
        poseur.poser(hx, base + 2, hz, tronc);
    }

    /** Feuilles persistantes : sinon, celles trop loin d'un tronc tomberaient en poussière. */
    private static BlockState feuilles(Block bloc) {
        return bloc.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
    }

    /** Nombre pseudo-aléatoire fixe pour une colonne et un usage (sel). */
    private static int hachage(int x, int z, int sel) {
        int h = x * 73856093 ^ z * 83492791 ^ sel * 19349663;
        h ^= h >>> 13;
        h *= 0x5bd1e995;
        h ^= h >>> 15;
        return h & Integer.MAX_VALUE;
    }

    /** Pose des blocs dans le chunk en cours, en ignorant ce qui tombe hors de lui. */
    private static final class Poseur {
        private final ChunkAccess chunk;
        private final Heightmap fondOcean;
        private final Heightmap surfaceMonde;
        private final int minX;
        private final int minZ;
        private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        Poseur(ChunkAccess chunk, Heightmap fondOcean, Heightmap surfaceMonde) {
            this.chunk = chunk;
            this.fondOcean = fondOcean;
            this.surfaceMonde = surfaceMonde;
            this.minX = chunk.getPos().getMinBlockX();
            this.minZ = chunk.getPos().getMinBlockZ();
        }

        BlockState lire(int x, int y, int z) {
            return chunk.getBlockState(pos.set(x, y, z));
        }

        void poser(int x, int y, int z, Block bloc) {
            poser(x, y, z, bloc.defaultBlockState());
        }

        void poserDouble(int x, int y, int z, Block bloc) {
            poser(x, y, z, bloc.defaultBlockState());
            poser(x, y + 1, z, bloc.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
        }

        /** Pose sans écraser le sol ni un tronc (pour les feuilles). */
        void poserSiLibre(int x, int y, int z, BlockState etat) {
            if (dansLeChunk(x, y, z)) {
                BlockState present = lire(x, y, z);
                if (present.isAir() || present.canBeReplaced()) {
                    ecrire(x, y, z, etat);
                }
            }
        }

        void poser(int x, int y, int z, BlockState etat) {
            if (dansLeChunk(x, y, z)) {
                ecrire(x, y, z, etat);
            }
        }

        private boolean dansLeChunk(int x, int y, int z) {
            return x >= minX && x < minX + 16 && z >= minZ && z < minZ + 16
                    && y >= chunk.getMinBuildHeight() && y < chunk.getMaxBuildHeight();
        }

        private void ecrire(int x, int y, int z, BlockState etat) {
            chunk.setBlockState(pos.set(x, y, z), etat, false);
            fondOcean.update(x - minX, y, z - minZ, etat);
            surfaceMonde.update(x - minX, y, z - minZ, etat);
        }
    }
}
