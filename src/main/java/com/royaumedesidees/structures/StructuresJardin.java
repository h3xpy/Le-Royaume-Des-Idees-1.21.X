package com.royaumedesidees.structures;

import com.royaumedesidees.bloc.ConfessionnalBloc;
import com.royaumedesidees.bloc.EtalVergerBloc;
import com.royaumedesidees.bloc.FeuillesPoirierBloc;
import com.royaumedesidees.monde.ReliefRoyaume;
import com.royaumedesidees.registre.ModBlocs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Structures du Jardin de Milan (v0.2) : la villa d'Augustin, les vergers de poiriers, le figuier, et le
 * Confessionnal du centre de l'île. Elles s'adaptent au relief (collines du Jardin) : podium sous la villa,
 * murets et arbres qui suivent le sol.
 */
public final class StructuresJardin {
    private StructuresJardin() {
    }

    /** Hauteur du sol généré en (x, z). */
    private static int sol(int x, int z) {
        return ReliefRoyaume.colonne(x, z).surface();
    }

    private static int solMax(int x1, int z1, int x2, int z2) {
        int max = Integer.MIN_VALUE;
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                max = Math.max(max, sol(x, z));
            }
        }
        return max;
    }

    private static int solMin(int x1, int z1, int x2, int z2) {
        int min = Integer.MAX_VALUE;
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                min = Math.min(min, sol(x, z));
            }
        }
        return min;
    }

    // ------------------------------------------------------------------ Confessionnal du centre

    private static final int CONF_X = 18;
    private static final int CONF_Z = -14;
    private static final int CONF_Y = sol(CONF_X, CONF_Z) + 1;

    /** Le Confessionnal près de l'Autel, sur un petit dallage, grille tournée vers l'Autel (ouest). */
    public static final StructureRoyaume CONFESSIONNAL_CENTRE = new StructureRoyaume("confessionnal_centre", 1,
            new BoundingBox(CONF_X - 3, CONF_Y - 3, CONF_Z - 3, CONF_X + 3, CONF_Y + 4, CONF_Z + 3),
            StructuresJardin::confessionnalCentre);

    private static void confessionnalCentre(Pose pose) {
        for (int x = CONF_X - 2; x <= CONF_X + 2; x++) {
            for (int z = CONF_Z - 2; z <= CONF_Z + 2; z++) {
                pose.remplir(x, sol(x, z), z, x, CONF_Y - 1, z, Blocks.STONE_BRICKS.defaultBlockState());
                pose.poser(x, CONF_Y - 1, z, (x + z) % 2 == 0 ? Blocks.POLISHED_DIORITE : Blocks.POLISHED_ANDESITE);
                pose.remplir(x, CONF_Y, z, x, CONF_Y + 3, z, Blocks.AIR.defaultBlockState());
            }
        }
        BlockState conf = ModBlocs.CONFESSIONNAL.get().defaultBlockState().setValue(ConfessionnalBloc.FACING, Direction.WEST);
        pose.poser(CONF_X, CONF_Y, CONF_Z, conf.setValue(ConfessionnalBloc.MOITIE, DoubleBlockHalf.LOWER));
        pose.poser(CONF_X, CONF_Y + 1, CONF_Z, conf.setValue(ConfessionnalBloc.MOITIE, DoubleBlockHalf.UPPER));
        for (int dz : new int[]{-2, 2}) {
            pose.poser(CONF_X - 2, CONF_Y, CONF_Z + dz, Blocks.STONE_BRICK_WALL);
            pose.poser(CONF_X - 2, CONF_Y + 1, CONF_Z + dz, Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, false));
        }
    }

    // ------------------------------------------------------------------ Figuier

    private static final int FIG_X = -260;
    private static final int FIG_Z = -260;
    private static final int FIG_Y = sol(FIG_X, FIG_Z) + 1;

    /** Le grand figuier sous lequel Augustin pleurait, avec un banc de pierre. */
    public static final StructureRoyaume FIGUIER = new StructureRoyaume("figuier", 1,
            new BoundingBox(FIG_X - 9, FIG_Y - 3, FIG_Z - 9, FIG_X + 9, FIG_Y + 12, FIG_Z + 9),
            StructuresJardin::figuier);

    private static void figuier(Pose pose) {
        // Dégage la place autour (herbes, arbres voisins) jusqu'au-dessus de la couronne.
        for (int x = FIG_X - 9; x <= FIG_X + 9; x++) {
            for (int z = FIG_Z - 9; z <= FIG_Z + 9; z++) {
                pose.remplir(x, sol(x, z) + 1, z, x, FIG_Y + 12, z, Blocks.AIR.defaultBlockState());
            }
        }
        grandFiguier(pose, FIG_X, FIG_Y, FIG_Z, 1.0);
        // Banc de pierre au pied, côté sud.
        for (int dx = -1; dx <= 1; dx++) {
            int x = FIG_X + dx;
            int z = FIG_Z + 4;
            pose.remplir(x, sol(x, z), z, x, FIG_Y - 1, z, Blocks.STONE_BRICKS.defaultBlockState());
            pose.poser(x, FIG_Y, z, Blocks.SMOOTH_STONE_SLAB);
        }
    }

    /**
     * Un figuier : tronc court qui se divise en quatre branches basses, large couronne aplatie. {@code taille}
     * réduit l'arbre (les petits figuiers autour de la villa).
     */
    private static void grandFiguier(Pose pose, int x0, int y0, int z0, double taille) {
        BlockState feuilles = ModBlocs.FEUILLES_FIGUIER.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
        int tronc = (int) Math.round(3 * taille);
        int rayon = (int) Math.round(6 * taille);
        int couronne = y0 + tronc + (int) Math.round(2 * taille);
        for (int dy = -1; dy <= 2; dy++) {
            int r = Math.max(1, dy == 2 ? rayon - 3 : (dy == -1 ? rayon - 2 : rayon));
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    double d = (dx * dx + dz * dz) / (double) (r * r);
                    if (d <= 1.0 && (d < 0.85 || (dx * 31 + dz * 17 + dy) % 3 != 0)) {
                        pose.poser(x0 + dx, couronne + dy, z0 + dz, feuilles);
                    }
                }
            }
        }
        BlockState vertical = ModBlocs.BOIS_FIGUIER.get().defaultBlockState();
        pose.remplir(x0, y0, z0, x0, y0 + tronc, z0, vertical);
        int longueur = Math.max(1, (int) Math.round(3 * taille));
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState branche = vertical.setValue(RotatedPillarBlock.AXIS, direction.getAxis());
            for (int i = 1; i <= longueur; i++) {
                pose.poser(x0 + direction.getStepX() * i, y0 + tronc + (i + 1) / 2, z0 + direction.getStepZ() * i, branche);
            }
        }
    }

    // ------------------------------------------------------------------ Villa d'Augustin

    private static final int VILLA_X = -200;
    private static final int VILLA_Z = -190;
    /** Demi-largeur de la villa : 21 × 21 blocs. */
    private static final int VILLA_DEMI = 10;
    private static final int VILLA_SOL = solMax(VILLA_X - VILLA_DEMI, VILLA_Z - VILLA_DEMI, VILLA_X + VILLA_DEMI, VILLA_Z + VILLA_DEMI) + 1;
    private static final int VILLA_BAS = solMin(VILLA_X - 16, VILLA_Z - 16, VILLA_X + 16, VILLA_Z + 16);

    /**
     * La villa romaine d'Augustin à Milan : un podium, des murs blanchis à la chaux avec une bande rouge, un toit
     * de tuiles en croupe, un atrium à ciel ouvert avec son bassin (impluvium) et quatre colonnes, un lutrin, et
     * l'Étal du verger devant l'entrée. Deux petits figuiers dans le jardin.
     */
    public static final StructureRoyaume VILLA_AUGUSTIN = new StructureRoyaume("villa_augustin", 1,
            new BoundingBox(VILLA_X - 16, VILLA_BAS - 2, VILLA_Z - 16, VILLA_X + 16, VILLA_SOL + 12, VILLA_Z + 16),
            StructuresJardin::villa);

    private static void villa(Pose pose) {
        int x1 = VILLA_X - VILLA_DEMI;
        int x2 = VILLA_X + VILLA_DEMI;
        int z1 = VILLA_Z - VILLA_DEMI;
        int z2 = VILLA_Z + VILLA_DEMI;
        int f = VILLA_SOL;

        // Dégage tout le terrain du domaine au-dessus du sol.
        for (int x = VILLA_X - 16; x <= VILLA_X + 16; x++) {
            for (int z = VILLA_Z - 16; z <= VILLA_Z + 16; z++) {
                pose.remplir(x, sol(x, z) + 1, z, x, f + 12, z, Blocks.AIR.defaultBlockState());
            }
        }
        // Podium de pierre jusqu'au sol, dallage en mosaïque.
        for (int x = x1 - 1; x <= x2 + 1; x++) {
            for (int z = z1 - 1; z <= z2 + 3; z++) {
                pose.remplir(x, sol(x, z), z, x, f - 1, z, Blocks.STONE_BRICKS.defaultBlockState());
                boolean bordure = x == x1 - 1 || x == x2 + 1 || z == z1 - 1 || z >= z2 + 1;
                pose.poser(x, f - 1, z, bordure ? Blocks.SMOOTH_STONE.defaultBlockState()
                        : ((x + z) % 4 == 0 ? Blocks.POLISHED_ANDESITE : Blocks.POLISHED_DIORITE).defaultBlockState());
            }
        }
        // Marches de l'entrée, au sud.
        for (int dx = -2; dx <= 2; dx++) {
            for (int marche = 1; marche <= f - VILLA_BAS; marche++) {
                int z = z2 + 3 + marche;
                int y = f - 1 - marche;
                if (y < sol(VILLA_X + dx, z)) {
                    break;
                }
                pose.remplir(VILLA_X + dx, sol(VILLA_X + dx, z), z, VILLA_X + dx, y, z, Blocks.STONE_BRICKS.defaultBlockState());
            }
        }

        // Murs : chaux blanche (calcite), plinthe rouge comme les fresques romaines.
        int hauteurMur = 5;
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                if (x != x1 && x != x2 && z != z1 && z != z2) {
                    continue;
                }
                for (int y = f; y < f + hauteurMur; y++) {
                    BlockState mur = y == f ? Blocks.RED_TERRACOTTA.defaultBlockState() : Blocks.CALCITE.defaultBlockState();
                    pose.poser(x, y, z, mur);
                }
            }
        }
        // Porte d'entrée (fauces) au sud, fenêtres hautes sur les côtés.
        pose.remplir(VILLA_X - 1, f, z2, VILLA_X + 1, f + 2, z2, Blocks.AIR.defaultBlockState());
        for (int i = -6; i <= 6; i += 4) {
            pose.poser(x1, f + 3, VILLA_Z + i, Blocks.AIR);
            pose.poser(x2, f + 3, VILLA_Z + i, Blocks.AIR);
            pose.poser(VILLA_X + i, f + 3, z1, Blocks.AIR);
        }

        // Atrium à ciel ouvert : bassin (impluvium) et quatre colonnes.
        int a = 3;
        pose.remplir(VILLA_X - 1, f - 1, VILLA_Z - 1, VILLA_X + 1, f - 1, VILLA_Z + 1, Blocks.WATER.defaultBlockState());
        for (int dx : new int[]{-a, a}) {
            for (int dz : new int[]{-a, a}) {
                pose.remplir(VILLA_X + dx, f, VILLA_Z + dz, VILLA_X + dx, f + hauteurMur - 1, VILLA_Z + dz, Blocks.QUARTZ_PILLAR.defaultBlockState());
            }
        }
        // Le lutrin d'Augustin, au fond de l'atrium.
        pose.poser(VILLA_X, f, VILLA_Z - 6, Blocks.LECTERN.defaultBlockState().setValue(LecternBlock.FACING, Direction.SOUTH));
        // Quelques pots de fleurs le long des murs.
        for (int i = -7; i <= 7; i += 7) {
            pose.poser(x1 + 1, f, VILLA_Z + i, Blocks.POTTED_POPPY);
            pose.poser(x2 - 1, f, VILLA_Z + i, Blocks.POTTED_OXEYE_DAISY);
        }

        // Toit en croupe de tuiles, en demi-blocs qui montent vers l'atrium resté ouvert.
        for (int x = x1 - 1; x <= x2 + 1; x++) {
            for (int z = z1 - 1; z <= z2 + 1; z++) {
                int bord = Math.min(Math.min(x - (x1 - 1), (x2 + 1) - x), Math.min(z - (z1 - 1), (z2 + 1) - z));
                boolean atrium = Math.abs(x - VILLA_X) <= a && Math.abs(z - VILLA_Z) <= a;
                if (atrium) {
                    continue;
                }
                int y = f + hauteurMur + bord / 2;
                SlabType moitie = bord % 2 == 0 ? SlabType.BOTTOM : SlabType.TOP;
                pose.poser(x, y, z, Blocks.BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, moitie));
            }
        }

        // L'Étal du verger devant l'entrée, et deux petits figuiers dans le jardin.
        pose.poser(VILLA_X + 3, f, z2 + 2, ModBlocs.ETAL_VERGER.get().defaultBlockState().setValue(EtalVergerBloc.FACING, Direction.SOUTH));
        grandFiguier(pose, VILLA_X - 13, sol(VILLA_X - 13, VILLA_Z + 13) + 1, VILLA_Z + 13, 0.5);
        grandFiguier(pose, VILLA_X + 13, sol(VILLA_X + 13, VILLA_Z - 13) + 1, VILLA_Z - 13, 0.5);
    }

    // ------------------------------------------------------------------ Vergers

    /** Trois vergers clos de 8 poiriers : coin nord-ouest (x, z), nom du propriétaire. */
    private static final int[][] VERGERS = {{-352, -184}, {-328, -184}, {-340, -167}};
    private static final String[] PROPRIETAIRES = {"lucius", "severe", "verecundus"};
    private static final int VERGER_LARGEUR = 18;
    private static final int VERGER_PROFONDEUR = 12;
    private static final int VERGERS_BAS = solMin(-354, -186, -306, -152);
    private static final int VERGERS_HAUT = solMax(-354, -186, -306, -152);

    /** Les vergers de poiriers, clos d'un muret, avec un panneau « Défense de voler ». */
    public static final StructureRoyaume VERGERS_STRUCTURE = new StructureRoyaume("vergers", 1,
            new BoundingBox(-354, VERGERS_BAS - 1, -186, -306, VERGERS_HAUT + 10, -152),
            StructuresJardin::vergers);

    private static void vergers(Pose pose) {
        for (int i = 0; i < VERGERS.length; i++) {
            verger(pose, VERGERS[i][0], VERGERS[i][1], PROPRIETAIRES[i]);
        }
    }

    private static void verger(Pose pose, int x1, int z1, String proprietaire) {
        int x2 = x1 + VERGER_LARGEUR;
        int z2 = z1 + VERGER_PROFONDEUR;
        // Dégage l'intérieur et pose le muret qui suit le relief, avec une entrée au sud.
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                int s = sol(x, z);
                pose.remplir(x, s + 1, z, x, s + 9, z, Blocks.AIR.defaultBlockState());
                boolean muret = x == x1 || x == x2 || z == z1 || z == z2;
                boolean entree = z == z2 && Math.abs(x - (x1 + x2) / 2) <= 1;
                if (muret && !entree) {
                    pose.poser(x, s + 1, z, Blocks.MOSSY_COBBLESTONE);
                }
            }
        }
        // Poiriers en rangées, 4 blocs d'écart.
        for (int x = x1 + 3; x <= x2 - 3; x += 4) {
            for (int z = z1 + 3; z <= z2 - 3; z += 4) {
                poirier(pose, x, sol(x, z) + 1, z);
            }
        }
        // Le panneau à l'entrée.
        int xp = (x1 + x2) / 2 + 3;
        int zp = z2 + 1;
        int yp = sol(xp, zp) + 1;
        pose.poser(xp, yp, zp, Blocks.OAK_SIGN.defaultBlockState().setValue(StandingSignBlock.ROTATION, 0));
        if (pose.niveau().getBlockEntity(new BlockPos(xp, yp, zp)) instanceof SignBlockEntity panneau) {
            SignText texte = new SignText()
                    .setMessage(0, Component.translatable("panneau.royaumedesidees.verger." + proprietaire))
                    .setMessage(1, Component.translatable("panneau.royaumedesidees.verger.defense"))
                    .setMessage(2, Component.translatable("panneau.royaumedesidees.verger.voler"));
            panneau.setText(texte, true);
        }
    }

    /** Un poirier : tronc de chêne, couronne ronde de feuilles de poirier, la plupart chargées de poires. */
    private static void poirier(Pose pose, int x, int y, int z) {
        BlockState feuilles = ModBlocs.FEUILLES_POIRIER.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
        int sommet = y + 4;
        for (int dy = -1; dy <= 1; dy++) {
            int r = dy == 1 ? 1 : 2;
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (r == 2 && Math.abs(dx) == 2 && Math.abs(dz) == 2) {
                        continue;
                    }
                    boolean poires = (dx * 7 + dz * 13 + dy * 5 + x + z) % 3 != 0;
                    pose.poser(x + dx, sommet + dy, z + dz, feuilles.setValue(FeuillesPoirierBloc.POIRES, poires));
                }
            }
        }
        pose.remplir(x, y, z, x, sommet - 1, z, Blocks.OAK_LOG.defaultBlockState());
    }

    // ------------------------------------------------------------------ Repères (vérification, visite)

    public static final BlockPos POS_CONFESSIONNAL = new BlockPos(CONF_X, CONF_Y, CONF_Z);
    public static final BlockPos POS_FIGUIER = new BlockPos(FIG_X, FIG_Y, FIG_Z);
    public static final BlockPos POS_LUTRIN = new BlockPos(VILLA_X, VILLA_SOL, VILLA_Z - 6);
    public static final BlockPos POS_ETAL = new BlockPos(VILLA_X + 3, VILLA_SOL, VILLA_Z + VILLA_DEMI + 2);
    /** Pied du premier poirier du verger de Lucius. */
    public static final BlockPos POS_POIRIER = new BlockPos(VERGERS[0][0] + 3, sol(VERGERS[0][0] + 3, VERGERS[0][1] + 3) + 1, VERGERS[0][1] + 3);
    public static final BlockPos POS_PANNEAU = new BlockPos(VERGERS[0][0] + VERGER_LARGEUR / 2 + 3,
            sol(VERGERS[0][0] + VERGER_LARGEUR / 2 + 3, VERGERS[0][1] + VERGER_PROFONDEUR + 1) + 1, VERGERS[0][1] + VERGER_PROFONDEUR + 1);
}
