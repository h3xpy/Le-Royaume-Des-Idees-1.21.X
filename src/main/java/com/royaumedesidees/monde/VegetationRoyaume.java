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
 * Végétation du Royaume : herbes, fleurs, roseaux, neige et petits arbres. Comme le relief, tout dépend
 * uniquement des coordonnées (jamais de la seed) : un chunk régénéré serait identique.
 * <b>Ne plus modifier après la v0.1</b>, pour la même raison que {@link ReliefRoyaume}.
 *
 * <p>Les arbres sont placés sur une grille : au plus un arbre par case, à une position fixée par la case.
 * Un chunk dessine aussi les morceaux des arbres voisins qui débordent chez lui.
 */
public final class VegetationRoyaume {
    /** Pas d'arbres près des emplacements réservés aux structures (centre, abbaye, ville). */
    private static final int[][] SANS_ARBRES = {
            {0, 0, 170},
            {ReliefRoyaume.VALLEE_X, ReliefRoyaume.VALLEE_Z, 80},
            {ReliefRoyaume.VILLE_X, ReliefRoyaume.VILLE_Z, 100},
    };

    private enum Arbre {
        CHENE(12, 22),
        OLIVIER(11, 30),
        EPICEA(13, 25);

        /** Taille d'une case de la grille, et chance sur 100 qu'une case porte un arbre. */
        final int case_;
        final int chance;

        Arbre(int case_, int chance) {
            this.case_ = case_;
            this.chance = chance;
        }
    }

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
        for (Arbre arbre : Arbre.values()) {
            int t = arbre.case_;
            for (int cx = Math.floorDiv(minX - 3, t); cx <= Math.floorDiv(minX + 18, t); cx++) {
                for (int cz = Math.floorDiv(minZ - 3, t); cz <= Math.floorDiv(minZ + 18, t); cz++) {
                    arbreDeLaCase(poseur, arbre, cx, cz);
                }
            }
        }
    }

    /** Une plante (ou de la neige) au-dessus du sol d'une colonne. */
    private static void plantes(Poseur poseur, int x, int z) {
        ReliefRoyaume.Colonne colonne = ReliefRoyaume.colonne(x, z);
        if (!colonne.ile() || colonne.sousLEau() || colonne.rebord() > 4) {
            return;
        }
        int y = colonne.surface() + 1;
        BlockState sol = poseur.lire(x, colonne.surface(), z);
        int tirage = hachage(x, z, 17) % 1000;

        switch (colonne.zoneSol()) {
            case JARDIN_MILAN -> {
                if (!sol.is(Blocks.GRASS_BLOCK)) {
                    return;
                }
                if (tirage < 280) {
                    poseur.poser(x, y, z, Blocks.SHORT_GRASS);
                } else if (tirage < 310) {
                    poseur.poserDouble(x, y, z, Blocks.TALL_GRASS);
                } else if (tirage < 360) {
                    Block[] fleurs = {Blocks.POPPY, Blocks.DANDELION, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER};
                    poseur.poser(x, y, z, fleurs[hachage(x, z, 23) % fleurs.length]);
                }
            }
            case PORT_ROYAL -> {
                // Aucune fleur à Port-Royal. Des roseaux au bord des mares.
                if (colonne.surface() <= ReliefRoyaume.NIVEAU_MER + 1 && tirage < 350 && bordDeMare(x, z)) {
                    int hauteur = 1 + hachage(x, z, 31) % 3;
                    for (int i = 0; i < hauteur; i++) {
                        poseur.poser(x, y + i, z, Blocks.SUGAR_CANE);
                    }
                    return;
                }
                if (!sol.is(Blocks.GRASS_BLOCK)) {
                    return;
                }
                if (tirage < 300) {
                    poseur.poser(x, y, z, Blocks.SHORT_GRASS);
                } else if (tirage < 360) {
                    poseur.poserDouble(x, y, z, Blocks.TALL_GRASS);
                } else if (tirage < 400) {
                    poseur.poser(x, y, z, Blocks.FERN);
                }
            }
            case PUY_DE_DOME -> {
                if (colonne.surface() >= ReliefRoyaume.NEIGE_Y) {
                    poseur.poser(x, y, z, Blocks.SNOW);
                    return;
                }
                if (!sol.is(Blocks.GRASS_BLOCK)) {
                    return;
                }
                if (tirage < 350) {
                    poseur.poser(x, y, z, Blocks.SHORT_GRASS);
                } else if (tirage < 370 && colonne.altitudeDome() < 0.4) {
                    poseur.poser(x, y, z, Blocks.FERN);
                } else if (tirage < 385) {
                    poseur.poser(x, y, z, hachage(x, z, 23) % 2 == 0 ? Blocks.CORNFLOWER : Blocks.AZURE_BLUET);
                }
            }
            case HIPPONE -> {
                // Garrigue sèche : herbe rare, buissons morts sur la terre nue, un peu d'allium (comme la lavande).
                if (sol.is(Blocks.COARSE_DIRT) || sol.is(Blocks.PODZOL)) {
                    if (tirage < 60) {
                        poseur.poser(x, y, z, Blocks.DEAD_BUSH);
                    }
                    return;
                }
                if (!sol.is(Blocks.GRASS_BLOCK)) {
                    return;
                }
                if (tirage < 150) {
                    poseur.poser(x, y, z, Blocks.SHORT_GRASS);
                } else if (tirage < 165) {
                    poseur.poserDouble(x, y, z, Blocks.TALL_GRASS);
                } else if (tirage < 185) {
                    poseur.poser(x, y, z, Blocks.ALLIUM);
                }
            }
        }
    }

    private static boolean bordDeMare(int x, int z) {
        return ReliefRoyaume.colonne(x + 1, z).sousLEau() || ReliefRoyaume.colonne(x - 1, z).sousLEau()
                || ReliefRoyaume.colonne(x, z + 1).sousLEau() || ReliefRoyaume.colonne(x, z - 1).sousLEau();
    }

    private static void arbreDeLaCase(Poseur poseur, Arbre arbre, int cx, int cz) {
        int t = arbre.case_;
        int h = hachage(cx, cz, 41 + arbre.ordinal());
        if (h % 100 >= arbre.chance) {
            return;
        }
        // Position dans la case, à 2 blocs des bords pour que deux arbres voisins ne se touchent pas.
        int x = cx * t + 2 + (h >> 7) % (t - 4);
        int z = cz * t + 2 + (h >> 14) % (t - 4);
        ReliefRoyaume.Colonne colonne = ReliefRoyaume.colonne(x, z);
        if (!colonne.ile() || colonne.sousLEau() || colonne.rebord() > 2 || colonne.affleurement()) {
            return;
        }
        Arbre attendu = switch (colonne.zoneSol()) {
            case JARDIN_MILAN -> Arbre.CHENE;
            case HIPPONE -> colonne.surface() > ReliefRoyaume.NIVEAU_MER + 3 ? Arbre.OLIVIER : null;
            case PUY_DE_DOME -> colonne.altitudeDome() < 0.35 && colonne.surface() < ReliefRoyaume.NEIGE_Y ? Arbre.EPICEA : null;
            case PORT_ROYAL -> null;
        };
        if (attendu != arbre) {
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
        int variante = h >> 20;
        switch (arbre) {
            case CHENE -> chene(poseur, x, base, z, variante);
            case OLIVIER -> olivier(poseur, x, base, z, variante);
            case EPICEA -> epicea(poseur, x, base, z, variante);
        }
    }

    private static void chene(Poseur poseur, int x, int base, int z, int variante) {
        BlockState tronc = Blocks.OAK_LOG.defaultBlockState();
        BlockState feuilles = feuilles(Blocks.OAK_LEAVES);
        int hauteur = 4 + variante % 2;
        int sommet = base + hauteur;
        for (int dy = -2; dy <= 1; dy++) {
            int rayon = dy >= 0 ? 1 : 2;
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

    /** Tronc court et tordu d'acacia (écorce grise), couronne large et plate. */
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
