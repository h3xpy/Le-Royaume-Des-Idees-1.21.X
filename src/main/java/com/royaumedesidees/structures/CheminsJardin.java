package com.royaumedesidees.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Chemins de terre du Jardin de Milan, et l'outil qui les trace. Un chemin suit une courbe douce passant par des points
 * (en coordonnées continues : le centre du bloc x est x + 0,5), épouse le relief, monte les marches du terrain par
 * des marches de pierre moussue, écarte les arbres qui le barrent, et s'éclaire de lanternes de loin en loin.
 * Chaque chemin est une structure à part, dont la boîte ne chevauche pas celles des bâtiments qu'il relie.
 */
public final class CheminsJardin {
    private CheminsJardin() {
    }

    /** De la porte ouest de la villa (escalier du triclinium) à l'allée des vergers. */
    private static final double[][] VILLA_VERGERS = {
            {-216.5, -192.5}, {-230.0, -197.0}, {-248.0, -195.0}, {-264.0, -186.0}, {-279.0, -174.0}, {-292.5, -169.0}};

    /** De la porte nord du jardin de la villa (péristyle) à la haie du jardin du figuier. */
    private static final double[][] VILLA_FIGUIER = {
            {-199.5, -220.5}, {-205.0, -231.0}, {-219.0, -238.0}, {-232.0, -246.0}, {-239.0, -253.5}, {-242.5, -255.0}};

    public static final StructureRoyaume CHEMIN_VILLA_VERGERS = new StructureRoyaume("chemin_villa_vergers", 1,
            boite(VILLA_VERGERS, -293, -217), pose -> tracer(pose, VILLA_VERGERS, 1.3, 71, true));

    public static final StructureRoyaume CHEMIN_VILLA_FIGUIER = new StructureRoyaume("chemin_villa_figuier", 1,
            boite(VILLA_FIGUIER, -243, -193), pose -> tracer(pose, VILLA_FIGUIER, 1.3, 72, true));

    /** Premier bloc du chemin des vergers (pour la vérification). */
    public static final BlockPos POS_CHEMIN = new BlockPos(-220, Decor.sol(-220, -194), -194);

    /** Boîte d'un chemin, bornée en x pour ne jamais toucher les structures voisines. */
    private static BoundingBox boite(double[][] points, int xMin, int xMax) {
        double zMin = Double.MAX_VALUE;
        double zMax = -Double.MAX_VALUE;
        for (double[] p : points) {
            zMin = Math.min(zMin, p[1]);
            zMax = Math.max(zMax, p[1]);
        }
        int z1 = (int) Math.floor(zMin) - 7;
        int z2 = (int) Math.ceil(zMax) + 7;
        if (points == VILLA_FIGUIER) {
            z2 = Math.min(z2, -221);
        }
        int y1 = Decor.solMin(xMin, z1, xMax, z2) - 2;
        int y2 = Decor.solMax(xMin, z1, xMax, z2) + 18;
        return new BoundingBox(xMin, y1, z1, xMax, y2, z2);
    }

    /** Point de la courbe de Catmull-Rom entre p1 et p2, pour t de 0 à 1. */
    private static double[] courbe(double[] p0, double[] p1, double[] p2, double[] p3, double t) {
        double t2 = t * t;
        double t3 = t2 * t;
        double[] r = new double[2];
        for (int i = 0; i < 2; i++) {
            r[i] = 0.5 * (2 * p1[i] + (-p0[i] + p2[i]) * t + (2 * p0[i] - 5 * p1[i] + 4 * p2[i] - p3[i]) * t2
                    + (-p0[i] + 3 * p1[i] - 3 * p2[i] + p3[i]) * t3);
        }
        return r;
    }

    /**
     * Trace un chemin. {@code demiLargeur} vaut environ 1 pour une allée de deux blocs, 1,3 pour un chemin de trois.
     * Avec {@code lanternes}, un poteau à lanterne est planté tous les 22 blocs environ, d'un côté puis de l'autre.
     */
    static void tracer(Pose pose, double[][] points, double demiLargeur, int graine, boolean lanternes) {
        // 1. Échantillonne la courbe et note, pour chaque bloc couvert, sa distance à l'axe et la direction du chemin.
        Map<Long, double[]> cellules = new HashMap<>();
        Map<Long, int[]> poteaux = new HashMap<>();
        double parcouru = 0;
        double prochainPoteau = 8;
        int cote = 1;
        double[] avant = null;
        for (int i = 0; i < points.length - 1; i++) {
            double[] p0 = points[Math.max(0, i - 1)];
            double[] p1 = points[i];
            double[] p2 = points[i + 1];
            double[] p3 = points[Math.min(points.length - 1, i + 2)];
            for (double t = 0; t <= 1.0; t += 0.05) {
                double[] c = courbe(p0, p1, p2, p3, t);
                double[] d = courbe(p0, p1, p2, p3, Math.min(1.0, t + 0.01));
                double[] g = courbe(p0, p1, p2, p3, Math.max(0.0, t - 0.01));
                double tx = d[0] - g[0];
                double tz = d[1] - g[1];
                double n = Math.max(1e-6, Math.hypot(tx, tz));
                tx /= n;
                tz /= n;
                if (avant != null) {
                    parcouru += Math.hypot(c[0] - avant[0], c[1] - avant[1]);
                }
                avant = c;
                int r = (int) Math.ceil(demiLargeur) + 1;
                for (int x = (int) Math.floor(c[0]) - r; x <= (int) Math.floor(c[0]) + r; x++) {
                    for (int z = (int) Math.floor(c[1]) - r; z <= (int) Math.floor(c[1]) + r; z++) {
                        double dist = Math.hypot(x + 0.5 - c[0], z + 0.5 - c[1]);
                        if (dist > demiLargeur) {
                            continue;
                        }
                        long cle = BlockPos.asLong(x, 0, z);
                        double[] deja = cellules.get(cle);
                        if (deja == null || dist < deja[0]) {
                            cellules.put(cle, new double[]{dist, tx, tz});
                        }
                    }
                }
                if (lanternes && parcouru >= prochainPoteau) {
                    prochainPoteau += 22;
                    double ecart = demiLargeur + 1.2;
                    int px = (int) Math.floor(c[0] - tz * ecart * cote);
                    int pz = (int) Math.floor(c[1] + tx * ecart * cote);
                    poteaux.put(BlockPos.asLong(px, 0, pz), new int[]{px, pz});
                    cote = -cote;
                }
            }
        }

        // 2. Le sol : terre battue, un peu de terre grossière, des bords irréguliers où l'herbe reprend.
        for (Map.Entry<Long, double[]> entree : cellules.entrySet()) {
            int x = BlockPos.getX(entree.getKey());
            int z = BlockPos.getZ(entree.getKey());
            int y = Decor.sol(x, z);
            if (!pose.dedans(x, y, z)) {
                continue;
            }
            ecarterArbre(pose, x, y, z);
            for (int dy = 1; dy <= 4; dy++) {
                BlockState dessus = pose.lire(x, y + dy, z);
                if (!dessus.isAir() && dessus.canBeReplaced()) {
                    pose.poser(x, y + dy, z, Blocks.AIR);
                }
            }
            if (!pose.lire(x, y + 1, z).isAir()) {
                continue;
            }
            double dist = entree.getValue()[0];
            double h = Decor.hasard(x, z, graine);
            boolean bord = dist > demiLargeur - 0.55;
            if (bord && h < 0.3) {
                continue;
            }
            pose.poser(x, y, z, h < 0.12 ? Blocks.COARSE_DIRT : (bord && h < 0.45 ? Blocks.MOSS_BLOCK : Blocks.DIRT_PATH));
        }

        // 3. Les marches : là où le chemin monte d'un bloc, une marche de pierre moussue.
        for (Map.Entry<Long, double[]> entree : cellules.entrySet()) {
            int x = BlockPos.getX(entree.getKey());
            int z = BlockPos.getZ(entree.getKey());
            int y = Decor.sol(x, z);
            double[] v = entree.getValue();
            Direction axe = Math.abs(v[1]) > Math.abs(v[2])
                    ? (v[1] > 0 ? Direction.EAST : Direction.WEST)
                    : (v[2] > 0 ? Direction.SOUTH : Direction.NORTH);
            for (Direction sens : new Direction[]{axe, axe.getOpposite()}) {
                int nx = x + sens.getStepX();
                int nz = z + sens.getStepZ();
                if (cellules.containsKey(BlockPos.asLong(nx, 0, nz)) && Decor.sol(nx, nz) == y + 1
                        && pose.dedans(x, y + 1, z) && pose.lire(x, y + 1, z).isAir()) {
                    pose.poser(x, y, z, Blocks.COARSE_DIRT);
                    pose.poserRaccorde(x, y + 1, z, Decor.escalier(Blocks.MOSSY_COBBLESTONE_STAIRS, sens));
                    break;
                }
            }
        }

        // 4. Les poteaux à lanterne.
        for (int[] p : poteaux.values()) {
            if (cellules.containsKey(BlockPos.asLong(p[0], 0, p[1]))) {
                continue;
            }
            int y = Decor.sol(p[0], p[1]);
            if (!pose.dedans(p[0], y + 3, p[1])) {
                continue;
            }
            ecarterArbre(pose, p[0], y, p[1]);
            pose.poserRaccorde(p[0], y + 1, p[1], Blocks.SPRUCE_FENCE.defaultBlockState());
            pose.poserRaccorde(p[0], y + 2, p[1], Blocks.SPRUCE_FENCE.defaultBlockState());
            pose.poser(p[0], y + 3, p[1], Decor.lanterne(false));
        }
    }

    /** Si un tronc pousse sur ce bloc, retire tout l'arbre (bois et feuilles reliés, à 5 blocs au plus). */
    private static void ecarterArbre(Pose pose, int x, int y, int z) {
        BlockPos tronc = null;
        for (int dy = 1; dy <= 3; dy++) {
            if (pose.lire(x, y + dy, z).is(BlockTags.LOGS)) {
                tronc = new BlockPos(x, y + dy, z);
                break;
            }
        }
        if (tronc == null) {
            return;
        }
        ArrayDeque<BlockPos> file = new ArrayDeque<>();
        Set<BlockPos> vus = new HashSet<>();
        file.add(tronc);
        vus.add(tronc);
        while (!file.isEmpty() && vus.size() < 600) {
            BlockPos ici = file.poll();
            pose.poser(ici.getX(), ici.getY(), ici.getZ(), Blocks.AIR);
            for (Direction direction : Direction.values()) {
                BlockPos voisin = ici.relative(direction);
                if (vus.contains(voisin) || Math.abs(voisin.getX() - x) > 5 || Math.abs(voisin.getZ() - z) > 5
                        || !pose.dedans(voisin.getX(), voisin.getY(), voisin.getZ())) {
                    continue;
                }
                BlockState etat = pose.lire(voisin.getX(), voisin.getY(), voisin.getZ());
                if (etat.is(BlockTags.LOGS) || etat.is(BlockTags.LEAVES)) {
                    vus.add(voisin);
                    file.add(voisin);
                }
            }
        }
    }
}
