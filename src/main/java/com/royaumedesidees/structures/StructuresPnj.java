package com.royaumedesidees.structures;

import com.royaumedesidees.bloc.PupitreAmbroiseBloc;
import com.royaumedesidees.registre.ModBlocs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import static com.royaumedesidees.structures.Decor.hasard;
import static com.royaumedesidees.structures.Decor.sol;
import static com.royaumedesidees.structures.Decor.solMax;
import static com.royaumedesidees.structures.Decor.solMin;

/**
 * Lieux des PNJ de la v0.3 :
 * <ul>
 *   <li>la Bibliothèque d'Ambroise, à Milan : une petite basilique de brique (comme Saint-Ambroise de Milan), une nef à
 *       colonnes bordée de rayonnages, et au fond l'abside avec le Pupitre d'Ambroise et la chaire de l'évêque ;</li>
 *   <li>la cellule de Pascal, sur la butte de Port-Royal : pierre grise, toit d'ardoise, un bureau et les comptes de
 *       son père. C'est la version 1 de la structure {@code port_royal} : en v0.4, l'abbaye la remplacera.</li>
 * </ul>
 */
public final class StructuresPnj {
    private StructuresPnj() {
    }

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    // ================================================================== Bibliothèque d'Ambroise

    private static final int LX = -180;
    private static final int LZ = -330;
    /** Niveau du sol de la bibliothèque et de son atrium (dessus du dallage). */
    private static final int LF = solMax(LX - 7, LZ - 15, LX + 7, LZ + 21) + 1;

    /**
     * La Bibliothèque d'Ambroise, d'après Saint-Ambroise de Milan : un atrium à arcades devant l'église (le
     * « quadriportique »), une façade à portail de marbre et oculus, un clocher, des murs de brique à contreforts et
     * bandes lombardes ; dedans, une nef à colonnes bordée de rayonnages, des lustres, des bannières, et au fond
     * l'abside à mosaïque d'or, avec le Pupitre d'Ambroise sous un ciborium à colonnes de porphyre.
     */
    public static final StructureRoyaume BIBLIOTHEQUE_AMBROISE = new StructureRoyaume("bibliotheque_ambroise", 2,
            new BoundingBox(LX - 12, solMin(LX - 12, LZ - 17, LX + 12, LZ + 27) - 2, LZ - 17, LX + 12, LF + 24, LZ + 27),
            StructuresPnj::bibliotheque);

    /** Rang de la marche de l'abside, au fond (nord) de la nef. */
    private static final int ABSIDE_Z = LZ - 10;

    private static boolean dansAbside(int dx, int dz) {
        return dz < -10 && dx * dx + (dz + 10) * (dz + 10) <= 20;
    }

    private static boolean murAbside(int dx, int dz) {
        double d = Math.sqrt(dx * dx + (dz + 10) * (dz + 10));
        return dz < -10 && d > 3.5 && d <= 4.6;
    }

    private static void bibliotheque(Pose pose) {
        int f = LF;
        for (int x = LX - 12; x <= LX + 12; x++) {
            for (int z = LZ - 17; z <= LZ + 27; z++) {
                pose.remplir(x, sol(x, z) + 1, z, x, f + 24, z, AIR);
            }
        }
        // Podium de pierre sous l'église, l'abside et l'atrium.
        for (int dx = -8; dx <= 8; dx++) {
            for (int dz = -15; dz <= 21; dz++) {
                boolean eglise = Math.abs(dx) <= 7 && dz >= -10 && dz <= 8;
                boolean abside = dansAbside(dx, dz) || murAbside(dx, dz);
                boolean atrium = Math.abs(dx) <= 7 && dz >= 9;
                if (!eglise && !abside && !atrium) {
                    continue;
                }
                int x = LX + dx;
                int z = LZ + dz;
                for (int y = sol(x, z); y <= f - 1; y++) {
                    pose.poser(x, y, z, hasard(x, y, z, 71) < 0.15 ? Blocks.MOSSY_STONE_BRICKS : Blocks.STONE_BRICKS);
                }
                if (Math.abs(dx) == 7 && !abside) {
                    pose.poser(x, f - 1, z, Blocks.SMOOTH_STONE);
                }
            }
        }
        nef(pose, f);
        abside(pose, f);
        toitEglise(pose, f);
        facade(pose, f);
        atrium(pose, f);
        clocher(pose, f);
        Decor.descente(pose, LX, LZ + 21, Direction.SOUTH, f, 1, Blocks.STONE_BRICK_STAIRS, Blocks.STONE_BRICKS);
        CheminsJardin.tracer(pose, new double[][]{{LX + 0.5, LZ + 22.5}, {LX + 0.5, LZ + 27.5}}, 1.2, 64, false);
        for (int sx : new int[]{-4, 4}) {
            Decor.cypres(pose, LX + sx, sol(LX + sx, LZ + 24) + 1, LZ + 24, 8);
        }
        Decor.panneau(pose, Blocks.DARK_OAK_SIGN, LX + 2, sol(LX + 2, LZ + 24) + 1, LZ + 24, 0,
                "panneau.royaumedesidees.bibliotheque.1", "panneau.royaumedesidees.bibliotheque.2",
                "panneau.royaumedesidees.bibliotheque.3");
    }

    /** La nef : murs de brique, colonnes, mosaïque, rayonnages, lutrins, lustres, bannières, plafond de bois. */
    private static void nef(Pose pose, int f) {
        for (int dx = -6; dx <= 6; dx++) {
            for (int dz = -10; dz <= 8; dz++) {
                int x = LX + dx;
                int z = LZ + dz;
                boolean mur = Math.abs(dx) == 6 || dz == 8 || (dz == -10 && Math.abs(dx) > 3);
                if (mur) {
                    for (int y = f; y <= f + 5; y++) {
                        pose.poser(x, y, z, y == f ? Blocks.STONE_BRICKS : Blocks.BRICKS);
                    }
                    continue;
                }
                Block dallage;
                if (Math.abs(dx) <= 2) {
                    // Allée centrale : champ blanc, bordure noire, motifs de marbre ciselé tous les quatre rangs.
                    dallage = Math.abs(dx) == 2 ? Blocks.POLISHED_DEEPSLATE
                            : (Math.floorMod(dz, 4) == 0 ? Blocks.CHISELED_QUARTZ_BLOCK
                            : (dx == 0 ? Blocks.CALCITE : Blocks.POLISHED_DIORITE));
                } else {
                    dallage = Math.floorMod(dz, 2) == 0 ? Blocks.TERRACOTTA : Blocks.RED_TERRACOTTA;
                }
                pose.poser(x, f - 1, z, dallage);
                pose.poser(x, f + 6, z, Blocks.SPRUCE_PLANKS);
            }
        }
        // Colonnes de marbre, chacune portant une bannière pourpre tournée vers la nef.
        for (int dz = -8; dz <= 6; dz += 3) {
            for (int sx : new int[]{-3, 3}) {
                pose.remplir(LX + sx, f, LZ + dz, LX + sx, f + 5, LZ + dz, Blocks.QUARTZ_PILLAR.defaultBlockState());
                Direction versNef = sx < 0 ? Direction.EAST : Direction.WEST;
                pose.poser(LX + sx + versNef.getStepX(), f + 4, LZ + dz,
                        Blocks.PURPLE_WALL_BANNER.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, versNef));
            }
        }
        // Fenêtres hautes en verre blanc.
        for (int dz : new int[]{-7, -3, 1, 5}) {
            for (int sx : new int[]{-6, 6}) {
                pose.poserRaccorde(LX + sx, f + 3, LZ + dz, Blocks.WHITE_STAINED_GLASS_PANE.defaultBlockState());
                pose.poserRaccorde(LX + sx, f + 4, LZ + dz, Blocks.WHITE_STAINED_GLASS_PANE.defaultBlockState());
            }
        }
        // Rayonnages : livres et étagères sculptées en alternance.
        for (int dz = -9; dz <= 7; dz++) {
            for (int sx : new int[]{-5, 5}) {
                for (int y = f; y <= f + 2; y++) {
                    boolean sculptee = y == f + 1 && Math.floorMod(dz, 3) == 1;
                    pose.poser(LX + sx, y, LZ + dz, sculptee
                            ? Blocks.CHISELED_BOOKSHELF.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, sx < 0 ? Direction.EAST : Direction.WEST)
                            : Blocks.BOOKSHELF.defaultBlockState());
                }
            }
        }
        Decor.lutrin(pose, LX - 2, f, LZ - 3, Direction.EAST,
                Decor.livre("Hexaemeron", "Ambrosius", "livre.royaumedesidees.hexaemeron.page", 2));
        pose.poser(LX + 2, f, LZ - 3, Blocks.LECTERN.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.WEST));
        // Lustres : une chaîne et une lanterne, au-dessus de l'allée.
        for (int dz : new int[]{-7, -2, 3}) {
            pose.poser(LX, f + 5, LZ + dz, Blocks.CHAIN);
            pose.poser(LX, f + 4, LZ + dz, Decor.lanterne(true));
        }
        for (int sx : new int[]{-4, 4}) {
            Decor.candelabre(pose, LX + sx, f, LZ + 7);
            Decor.candelabre(pose, LX + sx, f, LZ - 9);
        }
    }

    /**
     * L'abside : une marche, un dallage de marbre, trois fenêtres, et sous la demi-coupole une mosaïque d'or bordée de
     * lapis (celle de Saint-Ambroise est d'or). Le Pupitre d'Ambroise sous un ciborium à colonnes de porphyre, la
     * chaire de l'évêque derrière.
     */
    private static void abside(Pose pose, int f) {
        for (int dx = -5; dx <= 5; dx++) {
            for (int dz = -15; dz <= -11; dz++) {
                int x = LX + dx;
                int z = LZ + dz;
                if (murAbside(dx, dz)) {
                    for (int y = f; y <= f + 5; y++) {
                        pose.poser(x, y, z, y == f ? Blocks.STONE_BRICKS : Blocks.BRICKS);
                    }
                    pose.poser(x, f + 6, z, Blocks.BRICK_SLAB);
                } else if (dansAbside(dx, dz)) {
                    double d = Math.sqrt(dx * dx + (dz + 10) * (dz + 10));
                    pose.poser(x, f - 1, z, Blocks.STONE_BRICKS);
                    pose.poser(x, f, z, (dx + dz) % 2 == 0 ? Blocks.SMOOTH_QUARTZ : Blocks.CALCITE);
                    pose.poser(x, f + 5, z, d > 2.6 ? Blocks.LAPIS_BLOCK : Blocks.GOLD_BLOCK);
                    pose.poser(x, f + 6, z, Blocks.BRICK_SLAB);
                }
            }
        }
        // Trois fenêtres étroites au fond de l'abside.
        for (int[] fe : new int[][]{{0, -14}, {-3, -13}, {3, -13}}) {
            pose.poserRaccorde(LX + fe[0], f + 2, LZ + fe[1], Blocks.YELLOW_STAINED_GLASS_PANE.defaultBlockState());
            pose.poserRaccorde(LX + fe[0], f + 3, LZ + fe[1], Blocks.YELLOW_STAINED_GLASS_PANE.defaultBlockState());
        }
        for (int dx = -3; dx <= 3; dx++) {
            pose.poserRaccorde(LX + dx, f, ABSIDE_Z, Decor.escalier(Blocks.SMOOTH_QUARTZ_STAIRS, Direction.NORTH));
            // Haut de l'arc : l'abside est plus basse que la nef ; une rangée de marbre ferme l'arc.
            pose.poser(LX + dx, f + 5, ABSIDE_Z, Blocks.SMOOTH_QUARTZ);
        }
        // Le Pupitre d'Ambroise, tourné vers la nef ; la chaire derrière ; le ciborium au-dessus.
        pose.poser(LX, f + 1, LZ - 12, ModBlocs.PUPITRE_AMBROISE.get().defaultBlockState()
                .setValue(PupitreAmbroiseBloc.FACING, Direction.SOUTH));
        pose.poserRaccorde(LX, f + 1, LZ - 13, Decor.escalier(Blocks.DARK_OAK_STAIRS, Direction.NORTH));
        for (int sx : new int[]{-1, 1}) {
            for (int sz : new int[]{-11, -13}) {
                for (int y = f + 1; y <= f + 3; y++) {
                    pose.poserRaccorde(LX + sx, y, LZ + sz, Blocks.RED_NETHER_BRICK_WALL.defaultBlockState());
                }
            }
        }
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -13; dz <= -11; dz++) {
                pose.poser(LX + dx, f + 4, LZ + dz, Blocks.SMOOTH_QUARTZ_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM));
            }
        }
        pose.poser(LX - 2, f + 1, LZ - 12, Decor.bougies(Blocks.WHITE_CANDLE, 3));
        pose.poser(LX + 2, f + 1, LZ - 12, Decor.bougies(Blocks.WHITE_CANDLE, 3));
    }

    /** Toit de tuiles à deux pans, comble muré, bandes lombardes (petits arcs sous l'avant-toit) et contreforts. */
    private static void toitEglise(Pose pose, int f) {
        for (int dx = -7; dx <= 7; dx++) {
            int e = 7 - Math.abs(dx);
            int y = f + 7 + e / 2;
            SlabType type = e % 2 == 0 ? SlabType.BOTTOM : SlabType.TOP;
            for (int dz = -11; dz <= 9; dz++) {
                pose.poser(LX + dx, y, LZ + dz, Blocks.BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, type));
            }
            if (Math.abs(dx) <= 6) {
                for (int yy = f + 6; yy < y; yy++) {
                    pose.poser(LX + dx, yy, LZ + 8, Blocks.BRICKS);
                    pose.poser(LX + dx, yy, LZ - 10, Blocks.BRICKS);
                    for (int dz = -9; dz <= 7; dz++) {
                        if (yy > f + 6 || Math.abs(dx) == 6) {
                            pose.poser(LX + dx, yy, LZ + dz, Blocks.BRICKS);
                        }
                    }
                }
            }
        }
        for (int dz = -10; dz <= 8; dz++) {
            for (int sx : new int[]{-7, 7}) {
                Direction versMur = sx < 0 ? Direction.EAST : Direction.WEST;
                // Bande lombarde : une frise de petits arcs, sous l'avant-toit.
                pose.poserRaccorde(LX + sx, f + 6, LZ + dz, Decor.escalierRenverse(Blocks.BRICK_STAIRS,
                        Math.floorMod(dz, 2) == 0 ? versMur : (Math.floorMod(dz, 4) == 1 ? Direction.SOUTH : Direction.NORTH)));
                // Contreforts tous les trois rangs.
                if (Math.floorMod(dz, 3) == 0 && dz > -10 && dz < 8) {
                    for (int y = f; y <= f + 4; y++) {
                        pose.poserRaccorde(LX + sx, y, LZ + dz, Blocks.BRICK_WALL.defaultBlockState());
                    }
                }
            }
        }
    }

    /** La façade : un portail de marbre en plein cintre, un oculus, un pignon à frise d'arcs. */
    private static void facade(Pose pose, int f) {
        int z = LZ + 8;
        pose.remplir(LX - 1, f, z, LX + 1, f + 2, z, AIR);
        pose.remplir(LX - 1, f - 1, z, LX + 1, f - 1, z, Blocks.POLISHED_ANDESITE.defaultBlockState());
        for (int sx : new int[]{-2, 2}) {
            pose.remplir(LX + sx, f, z, LX + sx, f + 3, z, Blocks.QUARTZ_PILLAR.defaultBlockState());
        }
        pose.poser(LX - 1, f + 3, z, Decor.escalierRenverse(Blocks.SMOOTH_QUARTZ_STAIRS, Direction.EAST));
        pose.poser(LX, f + 3, z, Blocks.SMOOTH_QUARTZ);
        pose.poser(LX + 1, f + 3, z, Decor.escalierRenverse(Blocks.SMOOTH_QUARTZ_STAIRS, Direction.WEST));
        pose.remplir(LX - 1, f + 4, z, LX + 1, f + 4, z, Blocks.SMOOTH_QUARTZ.defaultBlockState());
        // Oculus de verre, cerclé de marbre.
        pose.poserRaccorde(LX, f + 6, z, Blocks.YELLOW_STAINED_GLASS_PANE.defaultBlockState());
        for (int[] c : new int[][]{{-1, 6}, {1, 6}, {0, 5}, {0, 7}}) {
            pose.poser(LX + c[0], f + c[1], z, Blocks.SMOOTH_QUARTZ);
        }
        // Frise d'arcs dans le pignon.
        for (int dx = -5; dx <= 5; dx += 2) {
            if (Math.abs(dx) > 1) {
                pose.poser(LX + dx, f + 6, z + 1, Decor.escalierRenverse(Blocks.BRICK_STAIRS, Direction.NORTH));
            }
        }
    }

    /**
     * L'atrium à arcades devant l'église (le quadriportique de Saint-Ambroise) : un mur d'enceinte, un portique couvert
     * sur les quatre côtés, des arcs de brique sur piliers, et au centre une cour de gazon avec une fontaine (cantharus).
     */
    private static void atrium(Pose pose, int f) {
        for (int dx = -7; dx <= 7; dx++) {
            for (int dz = 9; dz <= 21; dz++) {
                int x = LX + dx;
                int z = LZ + dz;
                boolean enceinte = Math.abs(dx) == 7 || dz == 21;
                boolean arcade = (Math.abs(dx) == 5 && dz >= 10 && dz <= 19) || ((dz == 10 || dz == 19) && Math.abs(dx) <= 5);
                boolean portique = Math.abs(dx) >= 5 || dz <= 10 || dz >= 19;
                if (enceinte) {
                    boolean porte = dz == 21 && Math.abs(dx) <= 1;
                    for (int y = f; y <= f + 3; y++) {
                        if (!porte || y == f + 3) {
                            pose.poser(x, y, z, y == f ? Blocks.STONE_BRICKS : Blocks.BRICKS);
                        }
                    }
                    pose.poser(x, f + 4, z, Blocks.BRICK_SLAB);
                    if (porte) {
                        pose.poser(x, f - 1, z, Blocks.POLISHED_ANDESITE);
                    }
                    continue;
                }
                if (portique) {
                    pose.poser(x, f - 1, z, Math.floorMod(dx + dz, 2) == 0 ? Blocks.POLISHED_ANDESITE : Blocks.SMOOTH_STONE);
                    pose.poser(x, f + 4, z, Blocks.BRICK_SLAB);
                    if (arcade) {
                        boolean pilier = (Math.abs(dx) == 5 && Math.floorMod(dz - 10, 3) == 0)
                                || ((dz == 10 || dz == 19) && (Math.abs(dx) == 5 || Math.abs(dx) == 2));
                        if (pilier) {
                            pose.remplir(x, f, z, x, f + 3, z, Blocks.BRICKS.defaultBlockState());
                        } else {
                            pose.poser(x, f + 3, z, Blocks.BRICKS);
                        }
                    }
                    continue;
                }
                // Cour : gazon, allées de gravier en croix.
                pose.poser(x, f - 2, z, Blocks.DIRT);
                pose.poser(x, f - 1, z, dx == 0 || dz == 15 ? Blocks.GRAVEL : Blocks.GRASS_BLOCK);
            }
        }
        // Arcs arrondis : un demi-escalier renversé contre chaque pilier, sous la rangée de brique.
        for (int dx = -5; dx <= 5; dx++) {
            for (int dz = 10; dz <= 19; dz++) {
                boolean arcade = (Math.abs(dx) == 5) || dz == 10 || dz == 19;
                if (!arcade || !pose.lire(LX + dx, f + 2, LZ + dz).isAir()) {
                    continue;
                }
                for (Direction d : Direction.Plane.HORIZONTAL) {
                    int vx = LX + dx + d.getStepX();
                    int vz = LZ + dz + d.getStepZ();
                    boolean voisinSurLigne = Math.abs(vx - LX) <= 5 && (vz - LZ >= 10 && vz - LZ <= 19)
                            && (Math.abs(vx - LX) == 5 || vz - LZ == 10 || vz - LZ == 19);
                    if (voisinSurLigne && pose.lire(vx, f + 2, vz).is(Blocks.BRICKS)) {
                        pose.poser(LX + dx, f + 2, LZ + dz, Decor.escalierRenverse(Blocks.BRICK_STAIRS, d));
                        break;
                    }
                }
            }
        }
        // Fontaine (cantharus) au centre, et quatre lauriers en fleur.
        int fz = LZ + 15;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                int x = LX + dx;
                int z = fz + dz;
                if (dx == 0 && dz == 0) {
                    pose.poser(x, f - 1, z, Blocks.CHISELED_STONE_BRICKS);
                    pose.poser(x, f, z, Blocks.QUARTZ_PILLAR);
                    pose.poser(x, f + 1, z, Blocks.DECORATED_POT);
                } else {
                    pose.poser(x, f - 2, z, Blocks.POLISHED_DIORITE);
                    pose.poser(x, f - 1, z, Blocks.WATER);
                }
            }
        }
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (Math.max(Math.abs(dx), Math.abs(dz)) == 2) {
                    pose.poser(LX + dx, f - 1, fz + dz, Blocks.SMOOTH_STONE);
                    pose.poser(LX + dx, f, fz + dz, Blocks.SMOOTH_STONE_SLAB);
                }
            }
        }
        for (int sx : new int[]{-3, 3}) {
            for (int sz : new int[]{12, 17}) {
                pose.poser(LX + sx, f, LZ + sz, Blocks.FLOWERING_AZALEA);
            }
        }
        for (int[] l : new int[][]{{-6, 15}, {6, 15}, {0, 20}, {-3, 9}, {3, 9}}) {
            pose.poser(LX + l[0], f + 3, LZ + l[1], Decor.lanterne(true));
        }
    }

    /** Le clocher, à l'angle de la façade et de l'atrium : brique, bandes lombardes, chambre des cloches, flèche. */
    private static void clocher(Pose pose, int f) {
        int x0 = LX + 9;
        int z0 = LZ + 7;
        int haut = f + 17;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                int x = x0 + dx;
                int z = z0 + dz;
                boolean angle = dx != 0 && dz != 0;
                for (int y = sol(x, z); y <= haut; y++) {
                    boolean baie = !angle && y >= f + 13 && y <= f + 15;
                    BlockState bloc;
                    if (dx == 0 && dz == 0) {
                        bloc = y == f + 12 ? Blocks.SPRUCE_PLANKS.defaultBlockState() : AIR;
                    } else if (baie) {
                        bloc = AIR;
                    } else if (angle) {
                        bloc = Blocks.STONE_BRICKS.defaultBlockState();
                    } else {
                        bloc = (y - f) % 6 == 5 ? Blocks.CHISELED_STONE_BRICKS.defaultBlockState() : Blocks.BRICKS.defaultBlockState();
                    }
                    pose.poser(x, y, z, bloc);
                }
            }
        }
        pose.poser(x0, f + 13, z0, Blocks.BELL);
        // Flèche en pyramide de tuiles.
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                int m = Math.max(Math.abs(dx), Math.abs(dz));
                if (m == 2) {
                    pose.poserRaccorde(x0 + dx, haut + 1, z0 + dz, Decor.escalier(Blocks.BRICK_STAIRS,
                            Math.abs(dx) >= Math.abs(dz) ? (dx > 0 ? Direction.WEST : Direction.EAST) : (dz > 0 ? Direction.NORTH : Direction.SOUTH)));
                } else if (m == 1) {
                    pose.poserRaccorde(x0 + dx, haut + 2, z0 + dz, Decor.escalier(Blocks.BRICK_STAIRS,
                            Math.abs(dx) >= Math.abs(dz) ? (dx > 0 ? Direction.WEST : Direction.EAST) : (dz > 0 ? Direction.NORTH : Direction.SOUTH)));
                    pose.poser(x0 + dx, haut + 1, z0 + dz, Blocks.BRICKS);
                } else {
                    pose.remplir(x0, haut + 1, z0, x0, haut + 3, z0, Blocks.BRICKS.defaultBlockState());
                    pose.poser(x0, haut + 4, z0, Blocks.LIGHTNING_ROD);
                }
            }
        }
    }

    // ================================================================== Cellule de Pascal (Port-Royal, version 1)

    private static final int PX = 300;
    private static final int PZ = -290;
    private static final int PF = solMax(PX - 4, PZ - 4, PX + 4, PZ + 4) + 1;

    public static final StructureRoyaume PORT_ROYAL = new StructureRoyaume("port_royal", 1,
            new BoundingBox(PX - 7, solMin(PX - 7, PZ - 7, PX + 7, PZ + 8) - 2, PZ - 7, PX + 7, PF + 10, PZ + 8),
            StructuresPnj::celluleDePascal);

    private static void celluleDePascal(Pose pose) {
        int f = PF;
        for (int x = PX - 7; x <= PX + 7; x++) {
            for (int z = PZ - 7; z <= PZ + 8; z++) {
                pose.remplir(x, sol(x, z) + 1, z, x, f + 10, z, AIR);
            }
        }
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                int x = PX + dx;
                int z = PZ + dz;
                pose.remplir(x, sol(x, z), z, x, f - 2, z, Blocks.COBBLESTONE.defaultBlockState());
                pose.poser(x, f - 1, z, Blocks.POLISHED_ANDESITE);
                if (Math.max(Math.abs(dx), Math.abs(dz)) == 3) {
                    boolean angle = Math.abs(dx) == 3 && Math.abs(dz) == 3;
                    for (int y = f; y <= f + 3; y++) {
                        pose.poser(x, y, z, angle ? Blocks.POLISHED_ANDESITE : (y == f ? Blocks.COBBLESTONE : Blocks.STONE_BRICKS));
                    }
                }
            }
        }
        // Porte au sud, deux petites fenêtres à barreaux.
        BlockState porte = Blocks.SPRUCE_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.NORTH)
                .setValue(DoorBlock.HINGE, DoorHingeSide.LEFT);
        pose.poser(PX, f, PZ + 3, porte.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        pose.poser(PX, f + 1, PZ + 3, porte.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        pose.poserRaccorde(PX - 3, f + 2, PZ, Blocks.IRON_BARS.defaultBlockState());
        pose.poserRaccorde(PX + 3, f + 2, PZ, Blocks.IRON_BARS.defaultBlockState());
        // Toit en pyramide d'ardoise.
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                int m = 4 - Math.max(Math.abs(dx), Math.abs(dz));
                pose.poser(PX + dx, f + 4 + m / 2, PZ + dz, Blocks.DEEPSLATE_TILE_SLAB.defaultBlockState()
                        .setValue(SlabBlock.TYPE, m % 2 == 0 ? SlabType.BOTTOM : SlabType.TOP));
            }
        }
        // Dedans : le bureau, les comptes du père de Pascal sur un lutrin, une chaise, une paillasse, une bougie.
        Decor.lutrin(pose, PX, f, PZ - 2, Direction.SOUTH,
                Decor.livre("Comptes de Rouen", "Étienne Pascal", "livre.royaumedesidees.comptes.page", 2));
        pose.poserRaccorde(PX, f, PZ - 1, Decor.escalier(Blocks.SPRUCE_STAIRS, Direction.SOUTH));
        pose.poser(PX - 2, f, PZ - 2, Blocks.SPRUCE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
        pose.poser(PX - 2, f + 1, PZ - 2, Decor.bougies(Blocks.WHITE_CANDLE, 1));
        pose.poser(PX - 1, f, PZ - 2, Blocks.SPRUCE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
        pose.poser(PX - 1, f + 1, PZ - 2, Blocks.WHITE_CARPET);
        pose.poser(PX - 2, f, PZ, Blocks.CHISELED_BOOKSHELF.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.EAST));
        pose.poser(PX + 2, f, PZ + 1, Blocks.BROWN_CARPET);
        pose.poser(PX + 2, f, PZ + 2, Blocks.BROWN_CARPET);
        pose.poser(PX + 2, f, PZ - 2, Blocks.LANTERN);

        // Dehors : une croix de bois sur la butte, et l'écriteau (l'abbaye arrive en v0.4).
        int xc = PX;
        int zc = PZ + 6;
        int s = sol(xc, zc);
        for (int dy = 1; dy <= 3; dy++) {
            pose.poserRaccorde(xc, s + dy, zc, Blocks.SPRUCE_FENCE.defaultBlockState());
        }
        pose.poserRaccorde(xc - 1, s + 2, zc, Blocks.SPRUCE_FENCE.defaultBlockState());
        pose.poserRaccorde(xc + 1, s + 2, zc, Blocks.SPRUCE_FENCE.defaultBlockState());
        Decor.panneau(pose, Blocks.SPRUCE_SIGN, PX + 2, sol(PX + 2, PZ + 4) + 1, PZ + 4, 0,
                "panneau.royaumedesidees.port_royal.1", "panneau.royaumedesidees.port_royal.2",
                "panneau.royaumedesidees.port_royal.3");
    }

    // ================================================================== Repères

    /** Pupitre d'Ambroise (centre de la zone de silence). */
    public static final BlockPos POS_PUPITRE = new BlockPos(LX, LF + 1, LZ - 12);
    /** Maison d'Ambroise : devant son pupitre. */
    public static final BlockPos POS_AMBROISE = new BlockPos(LX, LF + 1, LZ - 11);
    /** Entrée de la bibliothèque. */
    public static final BlockPos POS_ENTREE_BIBLIOTHEQUE = new BlockPos(LX, LF, LZ + 7);
    /** Maison de Pascal : au milieu de sa cellule. */
    public static final BlockPos POS_PASCAL = new BlockPos(PX, PF, PZ);
}
