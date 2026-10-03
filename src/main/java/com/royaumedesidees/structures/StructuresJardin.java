package com.royaumedesidees.structures;

import com.royaumedesidees.bloc.ConfessionnalBloc;
import com.royaumedesidees.bloc.EtalVergerBloc;
import com.royaumedesidees.bloc.FeuillesPoirierBloc;
import com.royaumedesidees.registre.ModBlocs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.CaveVinesBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import static com.royaumedesidees.structures.Decor.hasard;
import static com.royaumedesidees.structures.Decor.sol;
import static com.royaumedesidees.structures.Decor.solMax;
import static com.royaumedesidees.structures.Decor.solMin;

/**
 * Structures du Jardin de Milan (v0.2), au plus près des <i>Confessions</i> :
 * <ul>
 *   <li>la maison d'Augustin à Milan, une domus romaine avec son atrium, son tablinum où traînent les livres des
 *       Platoniciens et la table de jeu, et le petit jardin à colonnade dont il avait l'usage (VIII, 6 et 8) ;</li>
 *   <li>le jardin du figuier : Augustin s'y jette au sol en pleurant, entend la voix d'un enfant venue de la maison
 *       voisine, revient au banc d'Alypius où il avait laissé le livre de l'Apôtre, et lit Romains 13 (VIII, 12) ;</li>
 *   <li>les vergers : un poirier près de « notre vigne », chargé de poires ni belles ni bonnes, volées la nuit et
 *       jetées aux porcs (II, 4) ;</li>
 *   <li>le Confessionnal du centre, sous un baldaquin, avec la première phrase des Confessions (I, 1).</li>
 * </ul>
 * Les chemins qui relient la villa aux vergers et au figuier sont dans {@link CheminsJardin}.
 */
public final class StructuresJardin {
    private StructuresJardin() {
    }

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    // ================================================================== Confessionnal du centre

    private static final int CONF_X = 18;
    private static final int CONF_Z = -14;
    /** Couche de l'estrade (un bloc au-dessus du point le plus haut du sol) ; on marche un bloc plus haut. */
    private static final int CONF_ESTRADE = solMax(CONF_X - 4, CONF_Z - 4, CONF_X + 4, CONF_Z + 4) + 1;
    private static final int CONF_PIED = CONF_ESTRADE + 1;

    /** Le Confessionnal près de l'Autel, sous un baldaquin de marbre, grille tournée vers l'Autel (ouest). */
    public static final StructureRoyaume CONFESSIONNAL_CENTRE = new StructureRoyaume("confessionnal_centre", 2,
            new BoundingBox(CONF_X - 7, solMin(CONF_X - 7, CONF_Z - 7, CONF_X + 7, CONF_Z + 7) - 1, CONF_Z - 7,
                    CONF_X + 7, CONF_PIED + 11, CONF_Z + 7),
            StructuresJardin::confessionnal);

    /** Direction qui mène vers le centre (0, 0) depuis le décalage (dx, dz). */
    private static Direction versCentre(int dx, int dz) {
        if (Math.abs(dx) >= Math.abs(dz)) {
            return dx > 0 ? Direction.WEST : Direction.EAST;
        }
        return dz > 0 ? Direction.NORTH : Direction.SOUTH;
    }

    private static void confessionnal(Pose pose) {
        int e = CONF_ESTRADE;
        int p = CONF_PIED;
        for (int dx = -7; dx <= 7; dx++) {
            for (int dz = -7; dz <= 7; dz++) {
                int x = CONF_X + dx;
                int z = CONF_Z + dz;
                pose.remplir(x, sol(x, z) + 1, z, x, p + 11, z, AIR);
            }
        }
        // Estrade octogonale en mosaïque (rayons noirs sur fond blanc), bordée de marbre, et une marche tout autour.
        for (int dx = -5; dx <= 5; dx++) {
            for (int dz = -5; dz <= 5; dz++) {
                int m = Math.max(Math.abs(dx), Math.abs(dz));
                int d = Math.abs(dx) + Math.abs(dz);
                boolean estrade = m <= 4 && d <= 6;
                boolean marche = !estrade && m <= 5 && d <= 7;
                if (!estrade && !marche) {
                    continue;
                }
                int x = CONF_X + dx;
                int z = CONF_Z + dz;
                pose.remplir(x, sol(x, z), z, x, e - 1, z, Blocks.STONE_BRICKS.defaultBlockState());
                if (marche) {
                    pose.poserRaccorde(x, e, z, Decor.escalier(Blocks.SMOOTH_QUARTZ_STAIRS, versCentre(dx, dz)));
                } else if (d == 6 || m == 4) {
                    pose.poser(x, e, z, Blocks.SMOOTH_QUARTZ);
                } else if (dx == 0 || dz == 0 || Math.abs(dx) == Math.abs(dz)) {
                    pose.poser(x, e, z, Blocks.POLISHED_DEEPSLATE);
                } else {
                    pose.poser(x, e, z, Blocks.CALCITE);
                }
            }
        }
        // Baldaquin : quatre colonnes, plafond et corniche de marbre, toit en pyramide d'ardoise, cloche au sommet.
        for (int sx : new int[]{-3, 3}) {
            for (int sz : new int[]{-3, 3}) {
                pose.remplir(CONF_X + sx, p, CONF_Z + sz, CONF_X + sx, p + 3, CONF_Z + sz, Blocks.QUARTZ_PILLAR.defaultBlockState());
            }
        }
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                int m = Math.max(Math.abs(dx), Math.abs(dz));
                int x = CONF_X + dx;
                int z = CONF_Z + dz;
                if (m <= 3) {
                    pose.poser(x, p + 4, z, Blocks.SMOOTH_QUARTZ);
                } else {
                    pose.poser(x, p + 4, z, Blocks.SMOOTH_QUARTZ_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
                }
                for (int etage = 0; etage < 3; etage++) {
                    int anneau = 3 - etage;
                    if (m == anneau) {
                        pose.poserRaccorde(x, p + 5 + etage, z, Decor.escalier(Blocks.DEEPSLATE_TILE_STAIRS, versCentre(dx, dz)));
                    } else if (m < anneau) {
                        pose.poser(x, p + 5 + etage, z, Blocks.DEEPSLATE_TILES);
                    }
                }
            }
        }
        pose.poser(CONF_X, p + 8, CONF_Z, Blocks.BELL);
        pose.poser(CONF_X, p + 3, CONF_Z - 2, Decor.lanterne(true));
        pose.poser(CONF_X, p + 3, CONF_Z + 2, Decor.lanterne(true));

        // Le Confessionnal, l'agenouilloir devant la grille, les bougies et les fleurs blanches.
        BlockState conf = ModBlocs.CONFESSIONNAL.get().defaultBlockState().setValue(ConfessionnalBloc.FACING, Direction.WEST);
        pose.poser(CONF_X, p, CONF_Z, conf.setValue(ConfessionnalBloc.MOITIE, DoubleBlockHalf.LOWER));
        pose.poser(CONF_X, p + 1, CONF_Z, conf.setValue(ConfessionnalBloc.MOITIE, DoubleBlockHalf.UPPER));
        pose.poser(CONF_X - 1, p, CONF_Z, Blocks.DARK_OAK_TRAPDOOR.defaultBlockState()
                .setValue(TrapDoorBlock.FACING, Direction.EAST).setValue(TrapDoorBlock.HALF, Half.BOTTOM));
        pose.poser(CONF_X - 2, p, CONF_Z - 2, Decor.bougies(Blocks.WHITE_CANDLE, 3));
        pose.poser(CONF_X - 2, p, CONF_Z + 2, Decor.bougies(Blocks.WHITE_CANDLE, 2));
        pose.poser(CONF_X + 2, p, CONF_Z - 2, Decor.bougies(Blocks.WHITE_CANDLE, 2));
        pose.poser(CONF_X + 2, p, CONF_Z + 2, Decor.bougies(Blocks.WHITE_CANDLE, 4));
        pose.poser(CONF_X + 3, p, CONF_Z - 1, Blocks.POTTED_LILY_OF_THE_VALLEY);
        pose.poser(CONF_X + 3, p, CONF_Z + 1, Blocks.POTTED_LILY_OF_THE_VALLEY);
        pose.poser(CONF_X - 3, p, CONF_Z - 1, Blocks.POTTED_WHITE_TULIP);
        pose.poser(CONF_X - 3, p, CONF_Z + 1, Blocks.POTTED_WHITE_TULIP);

        // Derrière (à l'est) : deux cyprès et une haie basse ; devant : l'inscription, et du muguet autour.
        for (int sz : new int[]{-3, 3}) {
            Decor.cypres(pose, CONF_X + 6, sol(CONF_X + 6, CONF_Z + sz) + 1, CONF_Z + sz, 7);
        }
        for (int dz = -1; dz <= 1; dz++) {
            pose.poser(CONF_X + 6, sol(CONF_X + 6, CONF_Z + dz) + 1, CONF_Z + dz, Decor.feuilles(Blocks.AZALEA_LEAVES));
        }
        for (int dx = -6; dx <= 6; dx++) {
            for (int dz = -6; dz <= 6; dz++) {
                int m = Math.max(Math.abs(dx), Math.abs(dz));
                if (m == 6 && dx < 5 && hasard(dx, dz, 41) < 0.3 && !(dx == -6 && dz == 0)) {
                    pose.poser(CONF_X + dx, sol(CONF_X + dx, CONF_Z + dz) + 1, CONF_Z + dz, Blocks.LILY_OF_THE_VALLEY);
                }
            }
        }
        Decor.panneau(pose, Blocks.DARK_OAK_SIGN, CONF_X - 6, sol(CONF_X - 6, CONF_Z) + 1, CONF_Z, 4,
                "panneau.royaumedesidees.confessionnal.1", "panneau.royaumedesidees.confessionnal.2",
                "panneau.royaumedesidees.confessionnal.3", "panneau.royaumedesidees.confessionnal.4");
    }

    // ================================================================== Arbres du Jardin

    /**
     * Un figuier : tronc court et trapu qui se divise en trois tiges obliques, puis en branches basses ; large
     * couronne aplatie faite de plusieurs touffes, haute assez pour qu'on puisse se tenir dessous. {@code taille}
     * réduit l'arbre (les petits figuiers de la villa).
     */
    static void figuier(Pose pose, int x0, int y0, int z0, double taille) {
        BlockState bois = ModBlocs.BOIS_FIGUIER.get().defaultBlockState();
        BlockState feuilles = Decor.feuilles(ModBlocs.FEUILLES_FIGUIER.get());
        double hauteur = 4.5 * taille;
        double angle0 = hasard(x0, z0, 11) * Math.PI * 2;
        double[][] coudes = new double[3][];
        double[][] bouts = new double[4][];
        for (int i = 0; i < 3; i++) {
            double a = angle0 + i * Math.PI * 2 / 3 + (hasard(x0 + i, z0, 12) - 0.5) * 0.7;
            coudes[i] = new double[]{x0 + 0.5 + Math.cos(a) * 2.4 * taille, y0 + hauteur, z0 + 0.5 + Math.sin(a) * 2.4 * taille};
            bouts[i] = new double[]{x0 + 0.5 + Math.cos(a) * 4.4 * taille, y0 + hauteur + 0.6, z0 + 0.5 + Math.sin(a) * 4.4 * taille};
        }
        bouts[3] = new double[]{x0 + 0.5, y0 + hauteur + 1.6 * taille, z0 + 0.5};
        double rx = Math.max(1.6, 3.3 * taille);
        double ry = Math.max(1.2, 2.0 * taille);
        int basFeuillage = y0 + Math.max(2, (int) Math.round(3.4 * taille));
        int portee = (int) Math.ceil(4.4 * taille + rx) + 1;
        for (int x = x0 - portee; x <= x0 + portee; x++) {
            for (int z = z0 - portee; z <= z0 + portee; z++) {
                for (int y = basFeuillage; y <= y0 + hauteur + 3.5 * taille + 1; y++) {
                    for (double[] b : bouts) {
                        double ex = (x + 0.5 - b[0]) / rx;
                        double ey = (y + 0.5 - b[1]) / ry;
                        double ez = (z + 0.5 - b[2]) / rx;
                        if (ex * ex + ey * ey + ez * ez <= 1.0 - 0.28 * hasard(x, y, z, 13)) {
                            pose.poser(x, y, z, feuilles);
                            break;
                        }
                    }
                }
            }
        }
        int fut = Math.max(1, (int) Math.round(1.5 * taille));
        pose.remplir(x0, y0, z0, x0, y0 + fut, z0, bois);
        if (taille >= 0.8) {
            // Pied évasé.
            pose.poser(x0 + 1, y0, z0, bois);
            pose.poser(x0, y0, z0 + 1, bois);
            pose.poser(x0 - 1, y0, z0, bois);
        }
        double[] depart = {x0 + 0.5, y0 + fut + 0.5, z0 + 0.5};
        for (int i = 0; i < 3; i++) {
            ligneDeBois(pose, depart, coudes[i], bois);
            ligneDeBois(pose, coudes[i], new double[]{bouts[i][0], bouts[i][1] - 0.6, bouts[i][2]}, bois);
        }
    }

    /** Pose du bois le long d'un segment, en orientant chaque bûche selon la pente du segment. */
    private static void ligneDeBois(Pose pose, double[] a, double[] b, BlockState bois) {
        double dx = b[0] - a[0];
        double dy = b[1] - a[1];
        double dz = b[2] - a[2];
        double longueur = Math.sqrt(dx * dx + dy * dy + dz * dz);
        Direction.Axis axe = Math.abs(dy) >= Math.max(Math.abs(dx), Math.abs(dz)) ? Direction.Axis.Y
                : (Math.abs(dx) > Math.abs(dz) ? Direction.Axis.X : Direction.Axis.Z);
        for (double t = 0; t <= longueur; t += 0.3) {
            double f = t / Math.max(longueur, 1e-6);
            pose.poser((int) Math.floor(a[0] + dx * f), (int) Math.floor(a[1] + dy * f), (int) Math.floor(a[2] + dz * f),
                    bois.setValue(RotatedPillarBlock.AXIS, axe));
        }
    }

    /** Un poirier : tronc de chêne, petite branche, couronne ovale et irrégulière, chargée de poires aux deux tiers. */
    static void poirier(Pose pose, int x, int y, int z) {
        BlockState feuilles = Decor.feuilles(ModBlocs.FEUILLES_POIRIER.get());
        int h = 3 + (hasard(x, z, 5) < 0.5 ? 0 : 1);
        double rx = 2.3 + hasard(x, z, 6) * 0.6;
        double ry = 1.9;
        int cy = y + h + 1;
        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    double e = dx * dx / (rx * rx) + dy * dy / (ry * ry) + dz * dz / (rx * rx);
                    if (e <= 1.0 - 0.3 * hasard(x + dx, cy + dy, z + dz, 7)) {
                        boolean poires = hasard(x + dx, cy + dy, z + dz, 8) < 0.65;
                        pose.poser(x + dx, cy + dy, z + dz, feuilles.setValue(FeuillesPoirierBloc.POIRES, poires));
                    }
                }
            }
        }
        pose.remplir(x, y, z, x, cy, z, Blocks.OAK_LOG.defaultBlockState());
        Direction branche = Direction.from2DDataValue((int) (hasard(x, z, 9) * 4));
        pose.poser(x + branche.getStepX(), y + h, z + branche.getStepZ(),
                Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, branche.getAxis()));
    }

    // ================================================================== Jardin du figuier

    private static final int FX = -260;
    private static final int FZ = -260;
    private static final int FY = sol(FX, FZ) + 1;
    /** Demi-côté de la haie du jardin. */
    private static final int G = 12;
    /** Maison voisine, au nord de la haie : d'une de ses fenêtres vient la voix de l'enfant. */
    private static final int MX1 = FX - 4;
    private static final int MX2 = FX + 4;
    private static final int MZ1 = FZ - 19;
    private static final int MZ2 = FZ - 14;
    private static final int MY = solMax(MX1, MZ1, MX2, MZ2) + 1;
    /** Banc semi-circulaire (exèdre) d'Alypius, et son lutrin. */
    private static final int EX = FX + 6;
    private static final int EZ = FZ + 8;
    private static final int PUITS_X = FX - 7;
    private static final int PUITS_Z = FZ + 7;

    /** Le jardin du figuier, entouré d'une haie, avec l'exèdre d'Alypius, un puits et la maison voisine. */
    public static final StructureRoyaume FIGUIER = new StructureRoyaume("figuier", 2,
            new BoundingBox(FX - 16, solMin(FX - 16, FZ - 20, FX + 16, FZ + 16) - 3, FZ - 20,
                    FX + 16, Math.max(FY, MY) + 14, FZ + 16),
            StructuresJardin::jardinFiguier);

    private static boolean haie(int dx, int dz) {
        int m = Math.max(Math.abs(dx), Math.abs(dz));
        int d = Math.abs(dx) + Math.abs(dz);
        return (m == G && d <= 2 * G - 3) || (m < G && d >= 2 * G - 3 && d <= 2 * G - 2);
    }

    private static void jardinFiguier(Pose pose) {
        int haut = Math.max(FY, MY) + 14;
        for (int x = FX - 16; x <= FX + 16; x++) {
            for (int z = FZ - 20; z <= FZ + 16; z++) {
                pose.remplir(x, sol(x, z) + 1, z, x, haut, z, AIR);
            }
        }
        // Allées : de la porte est (où arrive le chemin de la villa) jusqu'au pied du figuier, et vers l'exèdre.
        CheminsJardin.tracer(pose, new double[][]{{FX + 16.5, FZ + 5.0}, {FX + 11.5, FZ + 5.0}, {FX + 7.0, FZ + 4.0},
                {FX + 3.6, FZ + 2.6}}, 1.0, 81, false);
        CheminsJardin.tracer(pose, new double[][]{{FX + 7.5, FZ + 4.5}, {FX + 6.0, FZ + 6.0}, {EX - 0.5, EZ - 0.5}}, 0.8, 82, false);

        // La haie, d'azalées en fleur par endroits, et les deux piliers taillés de la porte.
        for (int dx = -G; dx <= G; dx++) {
            for (int dz = -G; dz <= G; dz++) {
                if (!haie(dx, dz) || (dx == G && (dz == 4 || dz == 5))) {
                    continue;
                }
                int x = FX + dx;
                int z = FZ + dz;
                Block espece = hasard(x, z, 31) < 0.25 ? Blocks.FLOWERING_AZALEA_LEAVES : Blocks.AZALEA_LEAVES;
                int s = sol(x, z);
                pose.poser(x, s + 1, z, Decor.feuilles(espece));
                if (dx == G && (dz == 3 || dz == 6)) {
                    pose.poser(x, s + 2, z, Decor.feuilles(Blocks.AZALEA_LEAVES));
                    pose.poser(x, s + 3, z, Decor.feuilles(Blocks.FLOWERING_AZALEA_LEAVES));
                }
            }
        }

        // Au pied du figuier, la terre est nue, moussue : c'est là qu'Augustin s'est jeté au sol.
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (dx * dx + dz * dz > 7) {
                    continue;
                }
                int x = FX + dx;
                int z = FZ + dz;
                int s = sol(x, z);
                double h = hasard(x, z, 32);
                if (h < 0.35) {
                    pose.poser(x, s, z, Blocks.ROOTED_DIRT);
                } else if (h < 0.7) {
                    pose.poser(x, s, z, Blocks.MOSS_BLOCK);
                }
                if (hasard(x, z, 33) < 0.3 && (dx != 0 || dz != 0)) {
                    pose.poser(x, s + 1, z, Blocks.MOSS_CARPET);
                }
            }
        }
        figuier(pose, FX, FY, FZ, 1.25);

        // L'exèdre d'Alypius, ouverte vers le figuier, et le livre de l'Apôtre posé là (« ibi enim posueram codicem »).
        double vx = FX - EX;
        double vz = FZ - EZ;
        double n = Math.hypot(vx, vz);
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                double r = Math.hypot(dx, dz);
                if (r < 2.2 || r > 3.2 || (dx * vx + dz * vz) / (r * n) > 0.2) {
                    continue;
                }
                Direction dos = Math.abs(dx) >= Math.abs(dz) ? (dx > 0 ? Direction.EAST : Direction.WEST)
                        : (dz > 0 ? Direction.SOUTH : Direction.NORTH);
                int x = EX + dx;
                int z = EZ + dz;
                pose.poserRaccorde(x, sol(x, z) + 1, z, Decor.escalier(Blocks.SMOOTH_QUARTZ_STAIRS, dos));
            }
        }
        Decor.lutrin(pose, EX, sol(EX, EZ) + 1, EZ, Direction.SOUTH,
                Decor.livre("Ad Romanos", "Paulus", "livre.royaumedesidees.romains.page", 3));

        puits(pose, PUITS_X, PUITS_Z);
        maisonVoisine(pose);

        // Fleurs du jardin, plus rares à l'ombre du figuier.
        for (int dx = -G + 1; dx <= G - 1; dx++) {
            for (int dz = -G + 1; dz <= G - 1; dz++) {
                int x = FX + dx;
                int z = FZ + dz;
                if (haie(dx, dz)) {
                    continue;
                }
                int s = sol(x, z);
                if (!pose.lire(x, s, z).is(Blocks.GRASS_BLOCK) || !pose.lire(x, s + 1, z).isAir()) {
                    continue;
                }
                double h = hasard(x, z, 34) * (dx * dx + dz * dz < 40 ? 3.0 : 1.0);
                if (h < 0.025) {
                    Decor.plante2(pose, x, s + 1, z, hasard(x, z, 35) < 0.5 ? Blocks.ROSE_BUSH : Blocks.PEONY);
                } else if (h < 0.12) {
                    Block[] fleurs = {Blocks.POPPY, Blocks.CORNFLOWER, Blocks.OXEYE_DAISY, Blocks.LILY_OF_THE_VALLEY,
                            Blocks.ALLIUM, Blocks.AZURE_BLUET};
                    pose.poser(x, s + 1, z, fleurs[(int) (hasard(x, z, 36) * fleurs.length)]);
                } else if (h < 0.22) {
                    pose.poser(x, s + 1, z, Blocks.SHORT_GRASS);
                }
            }
        }
    }

    /** Un puits romain : margelle de pierre moussue, deux montants, une traverse et sa chaîne. */
    private static void puits(Pose pose, int cx, int cz) {
        int s = sol(cx, cz);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                int x = cx + dx;
                int z = cz + dz;
                if (dx == 0 && dz == 0) {
                    pose.poser(x, s - 1, z, Blocks.WATER);
                    pose.poser(x, s, z, Blocks.WATER);
                } else {
                    pose.remplir(x, Math.min(sol(x, z), s), z, x, s + 1, z, Blocks.MOSSY_STONE_BRICKS.defaultBlockState());
                }
            }
        }
        for (int dx : new int[]{-1, 1}) {
            pose.poserRaccorde(cx + dx, s + 2, cz, Blocks.SPRUCE_FENCE.defaultBlockState());
            pose.poserRaccorde(cx + dx, s + 3, cz, Blocks.SPRUCE_FENCE.defaultBlockState());
        }
        pose.remplir(cx - 1, s + 4, cz, cx + 1, s + 4, cz, Blocks.SPRUCE_SLAB.defaultBlockState());
        pose.poser(cx, s + 3, cz, Blocks.CHAIN);
    }

    /** La maison voisine, modeste : murs de brique crue, toit de tuiles, fenêtres ouvertes sur le jardin. */
    private static void maisonVoisine(Pose pose) {
        for (int x = MX1; x <= MX2; x++) {
            for (int z = MZ1; z <= MZ2; z++) {
                pose.remplir(x, sol(x, z), z, x, MY - 2, z, Blocks.STONE_BRICKS.defaultBlockState());
                pose.poser(x, MY - 1, z, Blocks.SPRUCE_PLANKS);
                boolean mur = x == MX1 || x == MX2 || z == MZ1 || z == MZ2;
                if (!mur) {
                    continue;
                }
                boolean angle = (x == MX1 || x == MX2) && (z == MZ1 || z == MZ2);
                for (int y = MY; y <= MY + 3; y++) {
                    BlockState bloc = angle ? Blocks.STRIPPED_OAK_LOG.defaultBlockState()
                            : (y == MY ? Blocks.COBBLESTONE.defaultBlockState() : Blocks.MUD_BRICKS.defaultBlockState());
                    pose.poser(x, y, z, bloc);
                }
            }
        }
        // Fenêtres côté jardin, avec leur jardinière, et une porte à l'est.
        for (int x : new int[]{FX - 2, FX + 2}) {
            pose.poser(x, MY + 1, MZ2, AIR);
            pose.poser(x, MY + 2, MZ2, AIR);
            pose.poser(x, MY, MZ2 + 1, Blocks.SPRUCE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
            pose.poser(x, MY + 1, MZ2 + 1, x < FX ? Blocks.POTTED_RED_TULIP : Blocks.POTTED_POPPY);
        }
        BlockState porte = Blocks.SPRUCE_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.WEST)
                .setValue(DoorBlock.HINGE, DoorHingeSide.LEFT);
        pose.poser(MX2, MY, FZ - 17, porte.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        pose.poser(MX2, MY + 1, FZ - 17, porte.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        // Dedans : l'enfant qui chante « Tolle, lege » (un bloc musical sous la fenêtre), un tonneau, une lanterne.
        pose.poser(FX - 2, MY, MZ2 - 1, Blocks.NOTE_BLOCK);
        pose.poser(MX1 + 1, MY, MZ1 + 1, Blocks.BARREL);
        pose.poser(FX, MY + 3, FZ - 17, Decor.lanterne(true));
        // Toit à deux pans, faîtage est-ouest, pignons de brique crue.
        for (int z = MZ1 - 1; z <= MZ2 + 1; z++) {
            int e = Math.min(z - (MZ1 - 1), (MZ2 + 1) - z);
            int y = MY + 4 + e / 2;
            SlabType type = e % 2 == 0 ? SlabType.BOTTOM : SlabType.TOP;
            for (int x = MX1 - 1; x <= MX2 + 1; x++) {
                pose.poser(x, y, z, Blocks.BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, type));
            }
            if (e >= 2 && z > MZ1 && z < MZ2) {
                pose.poser(MX1, MY + 4, z, Blocks.MUD_BRICKS);
                pose.poser(MX2, MY + 4, z, Blocks.MUD_BRICKS);
            }
        }
    }

    // ================================================================== Villa d'Augustin

    private static final int VX = -200;
    private static final int VZ = -190;
    /** Niveau du sol de la maison et de son jardin (dessus du dallage). */
    public static final int VILLA_SOL = solMax(VX - 11, VZ - 23, VX + 11, VZ + 13) + 1;
    private static final int VILLA_BAS = solMin(VX - 16, VZ - 30, VX + 16, VZ + 16);

    /** La maison milanaise d'Augustin : domus à atrium, jardin à colonnade au nord, Étal du verger devant l'entrée. */
    public static final StructureRoyaume VILLA_AUGUSTIN = new StructureRoyaume("villa_augustin", 2,
            new BoundingBox(VX - 16, VILLA_BAS - 2, VZ - 30, VX + 16, VILLA_SOL + 14, VZ + 16),
            StructuresJardin::villa);

    /** Pièces de la maison, en décalage (dx, dz) depuis le centre de l'atrium. */
    private enum Piece { ATRIUM, TABLINUM, CHAMBRE_AUGUSTIN, BIBLIOTHEQUE, TRICLINIUM, CUISINE, VESTIBULE, CHAMBRE_AMIS, CELLIER, MUR }

    private static Piece piece(int dx, int dz) {
        if (Math.abs(dx) <= 4 && Math.abs(dz) <= 4) {
            return Piece.ATRIUM;
        }
        if (dz >= -9 && dz <= -6) {
            if (Math.abs(dx) <= 2) {
                return Piece.TABLINUM;
            }
            if (dx <= -4) {
                return Piece.CHAMBRE_AUGUSTIN;
            }
            if (dx >= 4) {
                return Piece.BIBLIOTHEQUE;
            }
        }
        if (Math.abs(dz) <= 4) {
            if (dx <= -6) {
                return Piece.TRICLINIUM;
            }
            if (dx >= 6) {
                return Piece.CUISINE;
            }
        }
        if (dz >= 6 && dz <= 9) {
            if (Math.abs(dx) <= 1) {
                return Piece.VESTIBULE;
            }
            if (dx <= -3) {
                return Piece.CHAMBRE_AMIS;
            }
            if (dx >= 3) {
                return Piece.CELLIER;
            }
        }
        return Piece.MUR;
    }

    /** Passages entre les pièces : hauteur de l'ouverture (0 si mur plein). */
    private static int passage(int dx, int dz) {
        if (dz == -5 && Math.abs(dx) <= 2) {
            return 3;
        }
        if ((dz == -5 || dz == 5) && Math.abs(dx) == 4) {
            return 2;
        }
        if (dz == 5 && Math.abs(dx) <= 1) {
            return 3;
        }
        if (dx == -5 && Math.abs(dz) <= 1) {
            return 3;
        }
        if (dx == 5 && dz == 0) {
            return 2;
        }
        return 0;
    }

    private static void villa(Pose pose) {
        int f = VILLA_SOL;
        for (int x = VX - 16; x <= VX + 16; x++) {
            for (int z = VZ - 30; z <= VZ + 16; z++) {
                pose.remplir(x, sol(x, z) + 1, z, x, f + 14, z, AIR);
            }
        }
        // Podium de pierre sous la maison, le porche et le jardin ; quelques pierres moussues pour l'âge.
        for (int x = VX - 11; x <= VX + 11; x++) {
            for (int z = VZ - 23; z <= VZ + 13; z++) {
                for (int y = sol(x, z); y <= f - 1; y++) {
                    pose.poser(x, y, z, hasard(x, y, z, 51) < 0.18 ? Blocks.MOSSY_STONE_BRICKS : Blocks.STONE_BRICKS);
                }
                boolean bord = x == VX - 11 || x == VX + 11 || z == VZ - 23 || z == VZ + 13;
                if (bord) {
                    pose.poser(x, f - 1, z, Blocks.SMOOTH_STONE);
                }
            }
        }
        maison(pose, f);
        toit(pose, f);
        meubles(pose, f);
        peristyle(pose, f);
        abords(pose, f);
    }

    private static void maison(Pose pose, int f) {
        for (int dx = -10; dx <= 10; dx++) {
            for (int dz = -10; dz <= 10; dz++) {
                int x = VX + dx;
                int z = VZ + dz;
                boolean exterieur = Math.abs(dx) == 10 || Math.abs(dz) == 10;
                if (exterieur) {
                    boolean pilastre = (Math.abs(dx) == 10 && Math.abs(dz) % 5 == 0)
                            || (Math.abs(dz) == 10 && Math.abs(dx) % 5 == 0 && dx != 0)
                            || (dz == 10 && Math.abs(dx) == 2);
                    for (int y = f; y <= f + 4; y++) {
                        BlockState bloc = pilastre ? Blocks.QUARTZ_PILLAR.defaultBlockState()
                                : (y == f ? Blocks.RED_TERRACOTTA.defaultBlockState() : Blocks.CALCITE.defaultBlockState());
                        pose.poser(x, y, z, bloc);
                    }
                    continue;
                }
                Piece piece = piece(dx, dz);
                pose.poser(x, f - 1, z, dallage(piece, dx, dz));
                if (piece == Piece.MUR) {
                    int ouverture = passage(dx, dz);
                    for (int y = f; y <= f + 4; y++) {
                        BlockState bloc;
                        if (y - f < ouverture) {
                            bloc = AIR;
                        } else if (y == f) {
                            bloc = Blocks.RED_TERRACOTTA.defaultBlockState();
                        } else if (y == f + 3) {
                            bloc = Blocks.YELLOW_TERRACOTTA.defaultBlockState();
                        } else {
                            bloc = Blocks.CALCITE.defaultBlockState();
                        }
                        pose.poser(x, y, z, bloc);
                    }
                    if (ouverture > 0) {
                        pose.poser(x, f - 1, z, Blocks.POLISHED_ANDESITE);
                    }
                }
            }
        }
        // Portes extérieures : entrée au sud (3 de large, linteau de marbre), porte ouest du triclinium, porte nord.
        pose.remplir(VX - 1, f, VZ + 10, VX + 1, f + 2, VZ + 10, AIR);
        pose.remplir(VX - 1, f - 1, VZ + 10, VX + 1, f - 1, VZ + 10, Blocks.POLISHED_ANDESITE.defaultBlockState());
        pose.remplir(VX - 2, f + 3, VZ + 10, VX + 2, f + 3, VZ + 10, Blocks.SMOOTH_QUARTZ.defaultBlockState());
        pose.remplir(VX - 10, f, VZ - 3, VX - 10, f + 1, VZ - 3, AIR);
        pose.poser(VX - 10, f - 1, VZ - 3, Blocks.POLISHED_ANDESITE);
        pose.remplir(VX, f, VZ - 10, VX, f + 1, VZ - 10, AIR);
        pose.poser(VX, f - 1, VZ - 10, Blocks.POLISHED_ANDESITE);
        // Petites fenêtres hautes, à barreaux, comme dans les maisons romaines.
        int[][] fenetres = {{-6, -10}, {6, -10}, {-6, 10}, {6, 10}, {-10, 3}, {10, -3}, {10, 3}};
        for (int[] fe : fenetres) {
            pose.poserRaccorde(VX + fe[0], f + 3, VZ + fe[1], Blocks.IRON_BARS.defaultBlockState());
        }
        // Atrium : bassin (impluvium) au fond de mosaïque claire, nénuphar, quatre colonnes et architrave de marbre.
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                pose.poser(VX + dx, f - 2, VZ + dz, Blocks.POLISHED_DIORITE);
                pose.poser(VX + dx, f - 1, VZ + dz, Blocks.WATER);
            }
        }
        pose.poser(VX + 1, f, VZ - 1, Blocks.LILY_PAD);
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                if (Math.max(Math.abs(dx), Math.abs(dz)) == 4) {
                    pose.poser(VX + dx, f + 4, VZ + dz, Blocks.SMOOTH_QUARTZ);
                }
            }
        }
        for (int sx : new int[]{-4, 4}) {
            for (int sz : new int[]{-4, 4}) {
                pose.remplir(VX + sx, f, VZ + sz, VX + sx, f + 3, VZ + sz, Blocks.QUARTZ_PILLAR.defaultBlockState());
            }
        }
    }

    /** Le dallage de chaque pièce : mosaïques noir et blanc, terre cuite dans les chambres, pierre en cuisine. */
    private static BlockState dallage(Piece piece, int dx, int dz) {
        int m = Math.max(Math.abs(dx), Math.abs(dz));
        Block bloc = switch (piece) {
            case ATRIUM -> m <= 1 ? Blocks.POLISHED_DIORITE : (m == 2 ? Blocks.POLISHED_ANDESITE
                    : (m == 4 && ((dx + dz) & 1) == 0 ? Blocks.POLISHED_DEEPSLATE : Blocks.CALCITE));
            case TABLINUM -> Math.abs(dx) == 2 || dz == -9 || dz == -6 ? Blocks.POLISHED_DEEPSLATE
                    : (dx == 0 && dz == -8 ? Blocks.CHISELED_QUARTZ_BLOCK : Blocks.CALCITE);
            case TRICLINIUM -> dx == -9 || dx == -6 || Math.abs(dz) == 4 ? Blocks.POLISHED_DEEPSLATE
                    : (((dx + dz) & 1) == 0 ? Blocks.RED_TERRACOTTA : Blocks.CALCITE);
            case VESTIBULE -> Math.abs(dx) == 1 ? Blocks.POLISHED_DEEPSLATE : Blocks.CALCITE;
            case CUISINE -> Blocks.POLISHED_ANDESITE;
            case CELLIER -> Blocks.PACKED_MUD;
            case CHAMBRE_AUGUSTIN, CHAMBRE_AMIS, BIBLIOTHEQUE -> Blocks.TERRACOTTA;
            case MUR -> Blocks.STONE_BRICKS;
        };
        return bloc.defaultBlockState();
    }

    /**
     * Toit de tuiles : versants extérieurs jusqu'à un faîtage, puis versants intérieurs qui descendent vers
     * l'ouverture de l'atrium (le compluvium, qui mène la pluie au bassin). Plafond de bois au-dessous.
     */
    private static void toit(Pose pose, int f) {
        for (int x = VX - 11; x <= VX + 11; x++) {
            for (int z = VZ - 11; z <= VZ + 11; z++) {
                int ouest = x - (VX - 11);
                int est = (VX + 11) - x;
                int nord = z - (VZ - 11);
                int sud = (VZ + 11) - z;
                int bord = Math.min(Math.min(ouest, est), Math.min(nord, sud));
                if (bord >= 8) {
                    continue;
                }
                Direction versInterieur = bord == ouest ? Direction.EAST : bord == est ? Direction.WEST
                        : bord == nord ? Direction.SOUTH : Direction.NORTH;
                int h = Math.min(bord, 7 - bord);
                int y = f + 5 + h;
                Direction dos = bord <= 3 ? versInterieur : versInterieur.getOpposite();
                pose.poserRaccorde(x, y, z, Decor.escalier(Blocks.BRICK_STAIRS, dos));
                if (bord == 0) {
                    pose.poser(x, f + 4, z, Blocks.SMOOTH_QUARTZ_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
                } else if (bord < 7) {
                    pose.poser(x, f + 5, z, Blocks.SPRUCE_PLANKS);
                    for (int yy = f + 6; yy < y; yy++) {
                        pose.poser(x, yy, z, Blocks.BRICKS);
                    }
                }
            }
        }
    }

    private static void meubles(Pose pose, int f) {
        // Atrium : piédestaux à amphores aux coins du bassin, bancs de marbre, lanternes sous l'architrave.
        for (int sx : new int[]{-3, 3}) {
            for (int sz : new int[]{-3, 3}) {
                pose.poser(VX + sx, f, VZ + sz, Blocks.CHISELED_QUARTZ_BLOCK);
                pose.poser(VX + sx, f + 1, VZ + sz, Blocks.DECORATED_POT);
            }
        }
        for (int sz : new int[]{-3, 3}) {
            pose.poserRaccorde(VX - 4, f, VZ + sz, Decor.escalier(Blocks.SMOOTH_QUARTZ_STAIRS, Direction.WEST));
            pose.poserRaccorde(VX + 4, f, VZ + sz, Decor.escalier(Blocks.SMOOTH_QUARTZ_STAIRS, Direction.EAST));
        }
        int[][] lanternesAtrium = {{-4, 0}, {4, 0}, {0, -4}, {0, 4}};
        for (int[] l : lanternesAtrium) {
            pose.poser(VX + l[0], f + 3, VZ + l[1], Decor.lanterne(true));
        }
        // Lanternes pendues au plafond de chaque pièce.
        int[][] lanternes = {{0, -7}, {6, -7}, {-6, -7}, {-7, 0}, {7, 0}, {0, 8}, {-6, 8}, {6, 7}};
        for (int[] l : lanternes) {
            pose.poser(VX + l[0], f + 4, VZ + l[1], Decor.lanterne(true));
        }

        // Tablinum, le bureau d'Augustin : les livres des Platoniciens sur le lutrin, la table de jeu (mensa lusoria)
        // sur laquelle Ponticianus trouva le livre de l'Apôtre (VIII, 6), deux sièges, deux candélabres.
        Decor.lutrin(pose, VX - 1, f, VZ - 8, Direction.SOUTH,
                Decor.livre("Libri Platonicorum", "Marius Victorinus", "livre.royaumedesidees.platoniciens.page", 3));
        pose.poserRaccorde(VX - 1, f, VZ - 7, Decor.escalier(Blocks.SPRUCE_STAIRS, Direction.SOUTH));
        pose.poserRaccorde(VX + 1, f, VZ - 8, Blocks.DARK_OAK_FENCE.defaultBlockState());
        pose.poser(VX + 1, f + 1, VZ - 8, Blocks.GREEN_CARPET);
        pose.poserRaccorde(VX + 1, f, VZ - 7, Decor.escalier(Blocks.SPRUCE_STAIRS, Direction.SOUTH));
        Decor.candelabre(pose, VX - 2, f, VZ - 9);
        Decor.candelabre(pose, VX + 2, f, VZ - 9);

        // Bibliothèque : rayonnages, étagères sculptées, table de lecture.
        for (int dx = 5; dx <= 9; dx++) {
            pose.remplir(VX + dx, f, VZ - 9, VX + dx, f + 2, VZ - 9, Blocks.BOOKSHELF.defaultBlockState());
        }
        for (int dz = -8; dz <= -7; dz++) {
            pose.poser(VX + 9, f, VZ + dz, Blocks.CHISELED_BOOKSHELF.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.WEST));
            pose.poser(VX + 9, f + 1, VZ + dz, Blocks.CHISELED_BOOKSHELF.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.WEST));
        }
        pose.poserRaccorde(VX + 6, f, VZ - 7, Blocks.SPRUCE_FENCE.defaultBlockState());
        pose.poser(VX + 6, f + 1, VZ - 7, Blocks.WHITE_CARPET);
        Decor.candelabre(pose, VX + 9, f, VZ - 6);

        // Chambre d'Augustin : un lit romain (lectus), un coffre, un candélabre.
        lit(pose, VX - 9, VZ - 9, VX - 8, VZ - 9, f, Blocks.RED_CARPET);
        pose.poser(VX - 5, f, VZ - 9, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
        Decor.candelabre(pose, VX - 9, f, VZ - 6);

        // Triclinium : trois lits de table en U autour de la table, des amphores de vin.
        int[][] lits = {{-9, -1}, {-9, 0}, {-9, 1}, {-8, -2}, {-7, -2}, {-8, 2}, {-7, 2}};
        for (int[] l : lits) {
            pose.poser(VX + l[0], f, VZ + l[1], Blocks.RED_WOOL);
        }
        pose.poserRaccorde(VX - 8, f, VZ, Blocks.DARK_OAK_FENCE.defaultBlockState());
        pose.poser(VX - 8, f + 1, VZ, Blocks.WHITE_CARPET);
        pose.poser(VX - 9, f, VZ + 4, Blocks.DECORATED_POT);
        pose.poser(VX - 9, f, VZ - 4, Blocks.DECORATED_POT);
        Decor.candelabre(pose, VX - 6, f, VZ + 4);

        // Cuisine : four, fumoir, chaudron d'eau, tonneaux, amphores, table.
        pose.poser(VX + 9, f, VZ - 1, Blocks.SMOKER.defaultBlockState().setValue(FurnaceBlock.FACING, Direction.WEST));
        pose.poser(VX + 9, f, VZ, Blocks.FURNACE.defaultBlockState().setValue(FurnaceBlock.FACING, Direction.WEST));
        pose.poser(VX + 9, f, VZ + 2, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
        pose.poser(VX + 9, f, VZ - 3, Blocks.BARREL);
        pose.poser(VX + 9, f, VZ - 4, Blocks.BARREL);
        for (int dx = 6; dx <= 8; dx++) {
            pose.poser(VX + dx, f, VZ + 4, Blocks.DECORATED_POT);
        }
        pose.poserRaccorde(VX + 7, f, VZ - 2, Blocks.SPRUCE_FENCE.defaultBlockState());
        pose.poser(VX + 7, f + 1, VZ - 2, Blocks.OAK_PRESSURE_PLATE);

        // Chambre des amis (Alypius et Nebridius vivaient avec lui à Milan) : deux lits.
        lit(pose, VX - 9, VZ + 9, VX - 8, VZ + 9, f, Blocks.BLUE_CARPET);
        lit(pose, VX - 6, VZ + 9, VX - 5, VZ + 9, f, Blocks.LIGHT_BLUE_CARPET);
        pose.poser(VX - 3, f, VZ + 9, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH));
        Decor.candelabre(pose, VX - 9, f, VZ + 6);

        // Cellier : amphores et tonneaux.
        int[][] amphores = {{6, 8}, {7, 8}, {8, 8}, {6, 9}, {8, 9}, {9, 8}};
        for (int[] a : amphores) {
            pose.poser(VX + a[0], f, VZ + a[1], Blocks.DECORATED_POT);
        }
        pose.poser(VX + 3, f, VZ + 8, Blocks.BARREL);
        pose.poser(VX + 3, f, VZ + 9, Blocks.BARREL);
        pose.poser(VX + 9, f, VZ + 6, Blocks.HAY_BLOCK);
    }

    /** Un lit romain : deux blocs de laine blanche, couverts d'une étoffe de couleur. */
    private static void lit(Pose pose, int x1, int z1, int x2, int z2, int f, Block etoffe) {
        pose.poser(x1, f, z1, Blocks.WHITE_WOOL);
        pose.poser(x2, f, z2, Blocks.WHITE_WOOL);
        pose.poser(x1, f + 1, z1, etoffe);
        pose.poser(x2, f + 1, z2, etoffe);
    }

    /**
     * Le petit jardin dont Augustin avait l'usage (« hortulus quidam erat hospitii nostri ») : portique à colonnes sur
     * trois côtés et contre la maison, plates-bandes bordées de buis, roses et lis, fontaine au centre, porte au nord.
     */
    private static void peristyle(Pose pose, int f) {
        int zNord = VZ - 22;
        int zSud = VZ - 11;
        for (int dx = -10; dx <= 10; dx++) {
            for (int z = zNord; z <= zSud; z++) {
                int x = VX + dx;
                boolean mur = Math.abs(dx) == 10 || z == zNord;
                boolean portique = Math.abs(dx) >= 8 || z <= zNord + 2 || z >= zSud - 1;
                if (mur) {
                    for (int y = f; y <= f + 2; y++) {
                        pose.poser(x, y, z, y == f ? Blocks.RED_TERRACOTTA : Blocks.CALCITE);
                    }
                    pose.poser(x, f + 3, z, Blocks.BRICK_SLAB);
                    continue;
                }
                if (portique) {
                    pose.poser(x, f - 1, z, ((dx + z) & 1) == 0 ? Blocks.POLISHED_ANDESITE : Blocks.SMOOTH_STONE);
                    pose.poser(x, f + 3, z, Blocks.BRICK_SLAB);
                    boolean ligneColonnes = (Math.abs(dx) == 8 && z >= zNord + 2 && z <= zSud - 1)
                            || ((z == zNord + 2 || z == zSud - 1) && Math.abs(dx) <= 8);
                    boolean colonne = ligneColonnes && ((Math.abs(dx) == 8 && ((z - zNord) & 1) == 0)
                            || (Math.abs(dx) < 8 && (dx & 1) == 0 && dx != 0));
                    if (colonne) {
                        pose.remplir(x, f, z, x, f + 2, z, Blocks.QUARTZ_PILLAR.defaultBlockState());
                    }
                    continue;
                }
                // Le jardin : terre, allées de gravier en croix, fontaine au centre.
                pose.poser(x, f - 2, z, Blocks.DIRT);
                boolean allee = dx == 0 || z == VZ - 16;
                pose.poser(x, f - 1, z, allee ? Blocks.GRAVEL : Blocks.GRASS_BLOCK);
            }
        }
        // Porte nord, sous un arc de marbre.
        pose.remplir(VX, f, zNord, VX, f + 1, zNord, AIR);
        pose.poser(VX, f - 1, zNord, Blocks.POLISHED_ANDESITE);
        pose.remplir(VX - 1, f + 2, zNord, VX + 1, f + 2, zNord, Blocks.SMOOTH_QUARTZ.defaultBlockState());
        // Lanternes sous le portique.
        int[][] lanternes = {{-9, -16}, {9, -16}, {-5, -21}, {5, -21}, {-5, -12}, {5, -12}};
        for (int[] l : lanternes) {
            pose.poser(VX + l[0], f + 2, VZ + l[1], Decor.lanterne(true));
        }
        // Fontaine : bassin d'eau autour d'un pilier qui porte une urne, margelle de demi-dalles.
        int fz = VZ - 16;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                int m = Math.max(Math.abs(dx), Math.abs(dz));
                int x = VX + dx;
                int z = fz + dz;
                if (m == 2) {
                    pose.poser(x, f - 1, z, Blocks.SMOOTH_STONE);
                    pose.poser(x, f, z, Blocks.SMOOTH_STONE_SLAB);
                } else if (m == 1) {
                    pose.poser(x, f - 2, z, Blocks.POLISHED_DIORITE);
                    pose.poser(x, f - 1, z, Blocks.WATER);
                } else {
                    pose.poser(x, f - 1, z, Blocks.CHISELED_STONE_BRICKS);
                    pose.poser(x, f, z, Blocks.QUARTZ_PILLAR);
                    pose.poser(x, f + 1, z, Blocks.DECORATED_POT);
                }
            }
        }
        // Plates-bandes : bordures de buis, rosiers, lis, fleurs, et deux azalées en fleur.
        for (int dx = -7; dx <= 7; dx++) {
            for (int z = zNord + 3; z <= zSud - 2; z++) {
                int x = VX + dx;
                int dz = z - fz;
                if (dx == 0 || dz == 0 || Math.max(Math.abs(dx), Math.abs(dz)) <= 2) {
                    continue;
                }
                boolean bordure = Math.abs(dx) == 1 || Math.abs(dz) == 1 || Math.abs(dx) == 7
                        || z == zNord + 3 || z == zSud - 2;
                double h = hasard(x, z, 52);
                if (bordure) {
                    pose.poser(x, f, z, Decor.feuilles(Blocks.AZALEA_LEAVES));
                } else if (Math.abs(dx) == 4 && Math.abs(dz) == 2) {
                    Decor.plante2(pose, x, f, z, Blocks.ROSE_BUSH);
                } else if (Math.abs(dx) == 5 && Math.abs(dz) != 2) {
                    pose.poser(x, f, z, Blocks.FLOWERING_AZALEA);
                } else if (h < 0.45) {
                    Block[] fleurs = {Blocks.LILY_OF_THE_VALLEY, Blocks.WHITE_TULIP, Blocks.POPPY, Blocks.ALLIUM, Blocks.OXEYE_DAISY};
                    pose.poser(x, f, z, fleurs[(int) (hasard(x, z, 53) * fleurs.length)]);
                } else if (h < 0.6) {
                    pose.poser(x, f, z, Blocks.SHORT_GRASS);
                }
            }
        }
    }

    /** Escaliers, porche et Étal, cyprès, lavandes, petits figuiers, panneaux de direction. */
    private static void abords(Pose pose, int f) {
        // Porche dallé devant l'entrée.
        for (int dx = -11; dx <= 11; dx++) {
            for (int dz = 11; dz <= 12; dz++) {
                pose.poser(VX + dx, f - 1, VZ + dz, (dx & 1) == 0 ? Blocks.POLISHED_ANDESITE : Blocks.SMOOTH_STONE);
            }
        }
        // L'Étal du verger sous un auvent rayé rouge et blanc.
        pose.poser(VX + 3, f, VZ + 12, ModBlocs.ETAL_VERGER.get().defaultBlockState().setValue(EtalVergerBloc.FACING, Direction.SOUTH));
        for (int dx : new int[]{2, 4}) {
            pose.poserRaccorde(VX + dx, f, VZ + 13, Blocks.DARK_OAK_FENCE.defaultBlockState());
            pose.poserRaccorde(VX + dx, f + 1, VZ + 13, Blocks.DARK_OAK_FENCE.defaultBlockState());
        }
        for (int dx = 2; dx <= 4; dx++) {
            for (int dz = 11; dz <= 13; dz++) {
                pose.poser(VX + dx, f + 2, VZ + dz, Blocks.DARK_OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
                pose.poser(VX + dx, f + 3, VZ + dz, (dx & 1) == 0 ? Blocks.RED_CARPET : Blocks.WHITE_CARPET);
            }
        }
        pose.poser(VX - 5, f, VZ + 11, Blocks.POTTED_RED_TULIP);
        pose.poser(VX + 6, f, VZ + 11, Blocks.POTTED_RED_TULIP);

        // Escaliers : grand perron au sud, porte ouest (vers les vergers), porte nord du jardin (vers le figuier).
        Decor.descente(pose, VX, VZ + 13, Direction.SOUTH, f, 2, Blocks.STONE_BRICK_STAIRS, Blocks.STONE_BRICKS);
        Decor.descente(pose, VX - 11, VZ - 3, Direction.WEST, f, 1, Blocks.STONE_BRICK_STAIRS, Blocks.STONE_BRICKS);
        Decor.descente(pose, VX, VZ - 23, Direction.NORTH, f, 1, Blocks.STONE_BRICK_STAIRS, Blocks.STONE_BRICKS);
        // Bouts de chemin jusqu'au bord du domaine, où les chemins du Jardin prennent le relais.
        CheminsJardin.tracer(pose, new double[][]{{VX - 11.5, VZ - 2.5}, {VX - 16.5, VZ - 2.5}}, 1.2, 61, false);
        CheminsJardin.tracer(pose, new double[][]{{VX + 0.5, VZ - 23.5}, {VX + 0.5, VZ - 30.5}}, 1.2, 62, false);
        CheminsJardin.tracer(pose, new double[][]{{VX + 0.5, VZ + 14.5}, {VX + 0.5, VZ + 16.5}}, 2.2, 63, false);

        // Cyprès de part et d'autre des escaliers sud et nord.
        for (int sx : new int[]{-5, 5}) {
            Decor.cypres(pose, VX + sx, sol(VX + sx, VZ + 15) + 1, VZ + 15, 8);
            int xn = VX + sx - Integer.signum(sx) * 2;
            Decor.cypres(pose, xn, sol(xn, VZ - 26) + 1, VZ - 26, 8);
        }
        // Lavandes (lilas) au pied du podium, à l'est et à l'ouest.
        for (int dz = -8; dz <= 8; dz += 2) {
            if (dz < -1 && dz > -6) {
                continue;
            }
            Decor.plante2(pose, VX - 12, sol(VX - 12, VZ + dz) + 1, VZ + dz, Blocks.LILAC);
            if (Math.abs(dz) <= 3) {
                Decor.plante2(pose, VX + 12, sol(VX + 12, VZ + dz) + 1, VZ + dz, Blocks.LILAC);
            }
        }
        // Deux petits figuiers à l'est de la maison.
        for (int sz : new int[]{-7, 7}) {
            figuier(pose, VX + 12, sol(VX + 12, VZ + sz) + 1, VZ + sz, 0.55);
        }
        // Panneaux de direction aux deux sorties.
        Decor.panneau(pose, Blocks.SPRUCE_SIGN, VX - 13, sol(VX - 13, VZ - 5) + 1, VZ - 5, 4,
                "panneau.royaumedesidees.direction.vergers.1", "panneau.royaumedesidees.direction.vergers.2");
        Decor.panneau(pose, Blocks.SPRUCE_SIGN, VX + 2, sol(VX + 2, VZ - 27) + 1, VZ - 27, 8,
                "panneau.royaumedesidees.direction.figuier.1", "panneau.royaumedesidees.direction.figuier.2");
    }

    // ================================================================== Vergers

    /** Trois vergers clos de 8 poiriers : coin nord-ouest (x, z), propriétaire, porte au sud (vrai) ou au nord. */
    private static final int[][] VERGERS = {{-352, -184}, {-328, -184}, {-340, -167}};
    private static final String[] PROPRIETAIRES = {"lucius", "severe", "verecundus"};
    private static final boolean[] PORTE_AU_SUD = {true, true, false};
    private static final int VERGER_LARGEUR = 18;
    private static final int VERGER_PROFONDEUR = 12;
    private static final int VERGERS_BAS = solMin(-356, -188, -294, -150);
    private static final int VERGERS_HAUT = solMax(-356, -188, -294, -150);
    /** Sol aplani de la porcherie. */
    private static final int PORCHERIE_Y = solMax(-318, -165, -306, -157);

    /**
     * Les vergers de poiriers, clos de murets, le long d'une allée ; la vigne de Patricius tout à côté (« in vicinia
     * nostrae vineae ») et la porcherie où finissaient les poires volées.
     */
    public static final StructureRoyaume VERGERS_STRUCTURE = new StructureRoyaume("vergers", 2,
            new BoundingBox(-356, VERGERS_BAS - 2, -188, -294, VERGERS_HAUT + 12, -150),
            StructuresJardin::vergers);

    private static void vergers(Pose pose) {
        for (int x = -356; x <= -294; x++) {
            for (int z = -188; z <= -150; z++) {
                pose.remplir(x, sol(x, z) + 1, z, x, VERGERS_HAUT + 12, z, AIR);
            }
        }
        // L'allée, entre les vergers du nord et celui du sud, et son embranchement vers la porcherie.
        CheminsJardin.tracer(pose, new double[][]{{-293.5, -169.0}, {-352.5, -169.0}}, 1.0, 91, true);
        CheminsJardin.tracer(pose, new double[][]{{-311.5, -169.0}, {-311.5, -165.5}}, 1.0, 92, false);
        for (int i = 0; i < VERGERS.length; i++) {
            verger(pose, VERGERS[i][0], VERGERS[i][1], PROPRIETAIRES[i], PORTE_AU_SUD[i], i);
        }
        vigne(pose);
        porcherie(pose);
    }

    private static void verger(Pose pose, int x1, int z1, String proprietaire, boolean porteAuSud, int numero) {
        int x2 = x1 + VERGER_LARGEUR;
        int z2 = z1 + VERGER_PROFONDEUR;
        int gx = (x1 + x2) / 2;
        int zPorte = porteAuSud ? z2 : z1;
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                int s = sol(x, z);
                boolean muret = x == x1 || x == x2 || z == z1 || z == z2;
                if (!muret) {
                    double h = hasard(x, z, 93);
                    if (h < 0.08) {
                        pose.poser(x, s + 1, z, Blocks.SHORT_GRASS);
                    } else if (h < 0.11) {
                        pose.poser(x, s + 1, z, hasard(x, z, 94) < 0.5 ? Blocks.DANDELION : Blocks.OXEYE_DAISY);
                    }
                    continue;
                }
                if (z == zPorte && x == gx) {
                    // Le portail, de bois, entre deux piliers.
                    pose.poserRaccorde(x, s + 1, z, Blocks.OAK_FENCE_GATE.defaultBlockState()
                            .setValue(FenceGateBlock.FACING, porteAuSud ? Direction.SOUTH : Direction.NORTH));
                    continue;
                }
                double h = hasard(x, z, 95);
                Block base = h < 0.5 ? Blocks.COBBLESTONE : (h < 0.85 ? Blocks.MOSSY_COBBLESTONE : Blocks.ANDESITE);
                pose.poser(x, s + 1, z, base);
                pose.poserRaccorde(x, s + 2, z, Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState());
                if (z == zPorte && Math.abs(x - gx) == 1) {
                    pose.poser(x, s + 3, z, Decor.lanterne(false));
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                int x = x1 + 3 + 4 * i;
                int z = z1 + 3 + 4 * j;
                poirier(pose, x, sol(x, z) + 1, z);
            }
        }
        // Un détail par verger : une échelle contre un arbre, des balles de foin, un composteur.
        int tx = x1 + 3;
        int tz = z1 + 3;
        int ty = sol(tx, tz) + 1;
        if (numero == 0) {
            for (int dy = 0; dy <= 2; dy++) {
                pose.poser(tx + 1, ty + dy, tz, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.EAST));
            }
        } else if (numero == 1) {
            int x = x2 - 2;
            int z = z1 + 1;
            int s = sol(x, z);
            pose.poser(x, s + 1, z, Blocks.HAY_BLOCK);
            pose.poser(x - 1, s + 1, z, Blocks.HAY_BLOCK);
            pose.poser(x, s + 2, z, Blocks.HAY_BLOCK);
            pose.poser(x + 1, s + 1, z + 1, Blocks.BARREL);
        } else {
            pose.poser(x1 + 1, sol(x1 + 1, z2 - 1) + 1, z2 - 1, Blocks.COMPOSTER);
        }
        // Le panneau « Défense de voler », accroché au muret à côté du portail.
        int xp = gx + 2;
        int zp = porteAuSud ? zPorte + 1 : zPorte - 1;
        Decor.panneauMural(pose, Blocks.OAK_WALL_SIGN, xp, sol(xp, zPorte) + 1, zp, porteAuSud ? Direction.SOUTH : Direction.NORTH,
                "panneau.royaumedesidees.verger." + proprietaire, "panneau.royaumedesidees.verger.defense",
                "panneau.royaumedesidees.verger.voler");
    }

    /** La vigne de Patricius, le père d'Augustin : treilles de bois couvertes de feuilles, grappes qui luisent. */
    private static void vigne(Pose pose) {
        for (int x = -305; x <= -296; x += 3) {
            for (int z = -183; z <= -175; z++) {
                int s = sol(x, z);
                pose.poser(x, s, z, Blocks.COARSE_DIRT);
                boolean poteau = (z + 183) % 4 == 0;
                if (poteau) {
                    pose.poserRaccorde(x, s + 1, z, Blocks.SPRUCE_FENCE.defaultBlockState());
                    pose.poserRaccorde(x, s + 2, z, Blocks.SPRUCE_FENCE.defaultBlockState());
                } else if (hasard(x, z, 96) < 0.8) {
                    pose.poser(x, s + 2, z, Blocks.CAVE_VINES.defaultBlockState()
                            .setValue(CaveVines.BERRIES, hasard(x, z, 97) < 0.7).setValue(CaveVinesBlock.AGE, 25));
                }
                pose.poser(x, s + 3, z, Decor.feuilles(Blocks.JUNGLE_LEAVES));
                for (int dx : new int[]{-1, 1}) {
                    if (hasard(x + dx, z, 98) < 0.55) {
                        pose.poser(x + dx, sol(x + dx, z) + 3, z, Decor.feuilles(Blocks.JUNGLE_LEAVES));
                    }
                }
            }
        }
        Decor.panneau(pose, Blocks.SPRUCE_SIGN, -301, sol(-301, -172) + 1, -172, 0,
                "panneau.royaumedesidees.vigne.1", "panneau.royaumedesidees.vigne.2");
    }

    /**
     * La porcherie : enclos de bois sur un sol aplani (sinon les cochons sautent la clôture là où le terrain fait
     * une marche), boue, abreuvoir, abri, et trois cochons qui attendent les poires.
     */
    private static void porcherie(Pose pose) {
        int x1 = -318;
        int x2 = -306;
        int z1 = -165;
        int z2 = -157;
        int py = PORCHERIE_Y;
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                int s = sol(x, z);
                pose.remplir(x, s, z, x, py - 1, z, Blocks.DIRT.defaultBlockState());
                boolean bord = x == x1 || x == x2 || z == z1 || z == z2;
                double h = hasard(x, z, 99);
                pose.poser(x, py, z, bord ? Blocks.GRASS_BLOCK : (h < 0.5 ? Blocks.MUD : (h < 0.75 ? Blocks.COARSE_DIRT : Blocks.GRASS_BLOCK)));
                if (bord) {
                    if (x == -312 && z == z1) {
                        pose.poserRaccorde(x, py + 1, z, Blocks.OAK_FENCE_GATE.defaultBlockState()
                                .setValue(FenceGateBlock.FACING, Direction.NORTH));
                    } else {
                        pose.poserRaccorde(x, py + 1, z, Blocks.OAK_FENCE.defaultBlockState());
                    }
                }
            }
        }
        Decor.descente(pose, -312, z1, Direction.NORTH, py + 1, 0, Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.COBBLESTONE);
        pose.poser(-316, py + 1, -159, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
        // Le foin loin de la clôture : un cochon monté dessus sauterait par-dessus.
        pose.poser(-310, py + 1, -160, Blocks.HAY_BLOCK);
        // Abri : un toit de planches sur quatre poteaux.
        for (int x = -317; x <= -314; x++) {
            for (int z = -164; z <= -162; z++) {
                boolean coin = (x == -317 || x == -314) && (z == -164 || z == -162);
                if (coin) {
                    pose.poserRaccorde(x, py + 1, z, Blocks.SPRUCE_FENCE.defaultBlockState());
                    pose.poserRaccorde(x, py + 2, z, Blocks.SPRUCE_FENCE.defaultBlockState());
                }
                pose.poser(x, py + 3, z, Blocks.SPRUCE_SLAB);
            }
        }
        Decor.panneau(pose, Blocks.OAK_SIGN, -310, sol(-310, -166) + 1, -166, 8,
                "panneau.royaumedesidees.porcherie.1", "panneau.royaumedesidees.porcherie.2",
                "panneau.royaumedesidees.porcherie.3", "panneau.royaumedesidees.porcherie.4");
        // Une nouvelle pose (nouvelle version) remplace les cochons au lieu d'en ajouter.
        pose.niveau().getEntitiesOfClass(net.minecraft.world.entity.animal.Pig.class,
                new net.minecraft.world.phys.AABB(x1, py - 2, z1, x2 + 1, py + 6, z2 + 1)).forEach(net.minecraft.world.entity.Entity::discard);
        Decor.animaux(pose, EntityType.PIG, 3, -312, py + 1, -161);
    }

    // ================================================================== Repères (vérification, visite)

    public static final BlockPos POS_CONFESSIONNAL = new BlockPos(CONF_X, CONF_PIED, CONF_Z);
    public static final BlockPos POS_FIGUIER = new BlockPos(FX, FY, FZ);
    public static final BlockPos POS_LUTRIN_FIGUIER = new BlockPos(EX, sol(EX, EZ) + 1, EZ);
    public static final BlockPos POS_LUTRIN = new BlockPos(VX - 1, VILLA_SOL, VZ - 8);
    public static final BlockPos POS_ETAL = new BlockPos(VX + 3, VILLA_SOL, VZ + 12);
    public static final BlockPos POS_ATRIUM = new BlockPos(VX, VILLA_SOL, VZ + 3);
    /** Pied du premier poirier du verger de Lucius. */
    public static final BlockPos POS_POIRIER = new BlockPos(VERGERS[0][0] + 3, sol(VERGERS[0][0] + 3, VERGERS[0][1] + 3) + 1, VERGERS[0][1] + 3);
    /** Panneau du verger de Lucius, accroché au muret sud, à droite du portail. */
    public static final BlockPos POS_PANNEAU = new BlockPos(VERGERS[0][0] + VERGER_LARGEUR / 2 + 2,
            sol(VERGERS[0][0] + VERGER_LARGEUR / 2 + 2, VERGERS[0][1] + VERGER_PROFONDEUR) + 1, VERGERS[0][1] + VERGER_PROFONDEUR + 1);
    public static final BlockPos POS_PORCHERIE = new BlockPos(-312, PORCHERIE_Y + 1, -161);
}
