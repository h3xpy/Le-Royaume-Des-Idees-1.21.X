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
    /** Niveau du sol de la bibliothèque (dessus du dallage). */
    private static final int LF = solMax(LX - 7, LZ - 15, LX + 7, LZ + 9) + 1;

    public static final StructureRoyaume BIBLIOTHEQUE_AMBROISE = new StructureRoyaume("bibliotheque_ambroise", 1,
            new BoundingBox(LX - 10, solMin(LX - 10, LZ - 17, LX + 10, LZ + 18) - 2, LZ - 17, LX + 10, LF + 15, LZ + 18),
            StructuresPnj::bibliotheque);

    /** Centre de l'abside, au fond (nord) de la nef. */
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
        for (int x = LX - 10; x <= LX + 10; x++) {
            for (int z = LZ - 17; z <= LZ + 18; z++) {
                pose.remplir(x, sol(x, z) + 1, z, x, f + 15, z, AIR);
            }
        }
        // Podium de pierre.
        for (int dx = -7; dx <= 7; dx++) {
            for (int dz = -15; dz <= 9; dz++) {
                if (dz < -10 && !(dansAbside(dx, dz) || murAbside(dx, dz))) {
                    continue;
                }
                int x = LX + dx;
                int z = LZ + dz;
                for (int y = sol(x, z); y <= f - 1; y++) {
                    pose.poser(x, y, z, hasard(x, y, z, 71) < 0.15 ? Blocks.MOSSY_STONE_BRICKS : Blocks.STONE_BRICKS);
                }
                if (Math.abs(dx) == 7 || dz == 9) {
                    pose.poser(x, f - 1, z, Blocks.SMOOTH_STONE);
                }
            }
        }
        // Nef : murs de brique sur un socle de pierre, sol en mosaïque, bas-côtés en terre cuite.
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
                    dallage = Math.abs(dx) == 2 ? Blocks.POLISHED_DEEPSLATE
                            : (dx == 0 && Math.floorMod(dz, 4) == 0 ? Blocks.CHISELED_QUARTZ_BLOCK : Blocks.CALCITE);
                } else {
                    dallage = Blocks.TERRACOTTA;
                }
                pose.poser(x, f - 1, z, dallage);
                pose.poser(x, f + 6, z, Blocks.SPRUCE_PLANKS);
            }
        }
        // Colonnes de marbre entre la nef et les bas-côtés.
        for (int dz = -8; dz <= 6; dz += 3) {
            for (int sx : new int[]{-3, 3}) {
                pose.remplir(LX + sx, f, LZ + dz, LX + sx, f + 5, LZ + dz, Blocks.QUARTZ_PILLAR.defaultBlockState());
            }
        }
        // Fenêtres hautes, en verre blanc ; porte au sud, surmontée d'un oculus.
        for (int dz : new int[]{-7, -3, 1, 5}) {
            for (int sx : new int[]{-6, 6}) {
                pose.poserRaccorde(LX + sx, f + 3, LZ + dz, Blocks.WHITE_STAINED_GLASS_PANE.defaultBlockState());
                pose.poserRaccorde(LX + sx, f + 4, LZ + dz, Blocks.WHITE_STAINED_GLASS_PANE.defaultBlockState());
            }
        }
        pose.remplir(LX - 1, f, LZ + 8, LX + 1, f + 2, LZ + 8, AIR);
        pose.remplir(LX - 1, f - 1, LZ + 8, LX + 1, f - 1, LZ + 8, Blocks.POLISHED_ANDESITE.defaultBlockState());
        pose.poserRaccorde(LX, f + 4, LZ + 8, Blocks.WHITE_STAINED_GLASS_PANE.defaultBlockState());

        // Abside : une marche, un dallage de marbre, des murs en demi-cercle, une demi-coupole de tuiles.
        for (int dx = -5; dx <= 5; dx++) {
            for (int dz = -15; dz <= -11; dz++) {
                int x = LX + dx;
                int z = LZ + dz;
                if (murAbside(dx, dz)) {
                    for (int y = f; y <= f + 4; y++) {
                        pose.poser(x, y, z, y == f ? Blocks.STONE_BRICKS : Blocks.BRICKS);
                    }
                    pose.poser(x, f + 5, z, Blocks.BRICK_SLAB);
                } else if (dansAbside(dx, dz)) {
                    pose.poser(x, f - 1, z, Blocks.STONE_BRICKS);
                    pose.poser(x, f, z, (dx + dz) % 2 == 0 ? Blocks.SMOOTH_QUARTZ : Blocks.CALCITE);
                    pose.poser(x, f + 5, z, Blocks.BRICK_SLAB);
                }
            }
        }
        for (int dx = -3; dx <= 3; dx++) {
            pose.poserRaccorde(LX + dx, f, ABSIDE_Z, Decor.escalier(Blocks.SMOOTH_QUARTZ_STAIRS, Direction.NORTH));
            // Haut de l'arc : l'abside est plus basse que la nef, sans ce rang on verrait le ciel au-dessus.
            pose.poser(LX + dx, f + 5, ABSIDE_Z, Blocks.BRICKS);
        }
        // Le Pupitre d'Ambroise, tourné vers la nef, et derrière lui la chaire de l'évêque.
        pose.poser(LX, f + 1, LZ - 12, ModBlocs.PUPITRE_AMBROISE.get().defaultBlockState()
                .setValue(PupitreAmbroiseBloc.FACING, Direction.SOUTH));
        pose.poserRaccorde(LX, f + 1, LZ - 13, Decor.escalier(Blocks.DARK_OAK_STAIRS, Direction.NORTH));
        pose.poser(LX - 2, f + 1, LZ - 12, Decor.bougies(Blocks.WHITE_CANDLE, 3));
        pose.poser(LX + 2, f + 1, LZ - 12, Decor.bougies(Blocks.WHITE_CANDLE, 3));

        // Rayonnages le long des murs, deux lutrins dans la nef, lanternes au plafond.
        for (int dz = -9; dz <= 7; dz++) {
            for (int sx : new int[]{-5, 5}) {
                pose.remplir(LX + sx, f, LZ + dz, LX + sx, f + 2, LZ + dz, Blocks.BOOKSHELF.defaultBlockState());
            }
        }
        Decor.lutrin(pose, LX - 2, f, LZ - 3, Direction.EAST,
                Decor.livre("Hexaemeron", "Ambrosius", "livre.royaumedesidees.hexaemeron.page", 2));
        pose.poser(LX + 2, f, LZ - 3, Blocks.LECTERN.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.WEST));
        for (int dz : new int[]{-7, -2, 3}) {
            pose.poser(LX, f + 5, LZ + dz, Decor.lanterne(true));
        }
        for (int sx : new int[]{-4, 4}) {
            Decor.candelabre(pose, LX + sx, f, LZ + 7);
        }

        // Toit à deux pans, faîtage nord-sud, pignons de brique.
        for (int dx = -7; dx <= 7; dx++) {
            int e = 7 - Math.abs(dx);
            int y = f + 7 + e / 2;
            SlabType type = e % 2 == 0 ? SlabType.BOTTOM : SlabType.TOP;
            for (int dz = -11; dz <= 9; dz++) {
                pose.poser(LX + dx, y, LZ + dz, Blocks.BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, type));
            }
            if (Math.abs(dx) <= 6) {
                // Pignons, et comble muré au-dessus du plafond de bois (on ne voit pas le jour sous le toit).
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

        // Dehors : marches, cyprès, et l'écriteau de l'entrée.
        Decor.descente(pose, LX, LZ + 9, Direction.SOUTH, f, 1, Blocks.STONE_BRICK_STAIRS, Blocks.STONE_BRICKS);
        for (int sx : new int[]{-5, 5}) {
            Decor.cypres(pose, LX + sx, sol(LX + sx, LZ + 12) + 1, LZ + 12, 8);
        }
        Decor.panneau(pose, Blocks.DARK_OAK_SIGN, LX + 3, f, LZ + 9, 0,
                "panneau.royaumedesidees.bibliotheque.1", "panneau.royaumedesidees.bibliotheque.2",
                "panneau.royaumedesidees.bibliotheque.3");
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
