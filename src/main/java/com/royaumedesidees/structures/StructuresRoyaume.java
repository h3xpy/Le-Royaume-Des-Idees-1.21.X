package com.royaumedesidees.structures;

import com.royaumedesidees.bloc.PortailRoyaumeBloc;
import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.monde.ReliefRoyaume;
import com.royaumedesidees.registre.ModBlocs;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;
import java.util.Optional;

/**
 * Registre des structures du Royaume. Pour modifier une structure déjà publiée, changer son constructeur
 * et augmenter sa version : elle sera reposée dans les mondes existants. Ne jamais changer un identifiant.
 */
public final class StructuresRoyaume {
    /**
     * Aménagement de la Caverne de Platon : l'écran clair du mur des ombres, éclairé comme par le feu, le muret
     * derrière lequel passent les montreurs de marionnettes, le feu, et les poteaux où sont enchaînés les prisonniers.
     */
    public static final StructureRoyaume CAVERNE = new StructureRoyaume("caverne", 1,
            new BoundingBox(-22, 43, CaverneRoyaume.MUR_Z - 2, 22, 66, CaverneRoyaume.FEU_Z + 3),
            StructuresRoyaume::caverne);

    /** Portail de retour en Pierre d'Ombre, à côté de la sortie du tunnel de la Caverne. */
    public static final StructureRoyaume PORTAIL_RETOUR;

    /** Portail de retour : cadre de 4 de large (x 6 à 9) sur 5 de haut, dans le plan z = 130. */
    public static final int PORTAIL_X = 6;
    public static final int PORTAIL_Z = 130;
    /** y du bas du cadre, posé sur le point le plus haut du terrain sous la plateforme. */
    public static final int PORTAIL_Y;
    private static final int PORTAIL_SOL_MIN;

    static {
        int haut = Integer.MIN_VALUE;
        int bas = Integer.MAX_VALUE;
        for (int x = PORTAIL_X - 1; x <= PORTAIL_X + 4; x++) {
            for (int z = PORTAIL_Z - 1; z <= PORTAIL_Z + 1; z++) {
                int surface = ReliefRoyaume.colonne(x, z).surface();
                haut = Math.max(haut, surface);
                bas = Math.min(bas, surface);
            }
        }
        PORTAIL_Y = haut + 1;
        PORTAIL_SOL_MIN = bas;
        PORTAIL_RETOUR = new StructureRoyaume("portail_retour", 1,
                new BoundingBox(PORTAIL_X - 1, PORTAIL_SOL_MIN, PORTAIL_Z - 1, PORTAIL_X + 4, PORTAIL_Y + 6, PORTAIL_Z + 1),
                StructuresRoyaume::portailRetour);
    }

    public static final List<StructureRoyaume> TOUTES = List.of(CAVERNE, PORTAIL_RETOUR);

    private StructuresRoyaume() {
    }

    public static Optional<StructureRoyaume> parId(String id) {
        return TOUTES.stream().filter(structure -> structure.id().equals(id)).findFirst();
    }

    private static void caverne(Pose pose) {
        int murZ = CaverneRoyaume.MUR_Z;
        BlockState bordure = Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState();

        // Mur des ombres : un écran de calcite bordé de pierre noire, adossé à la paroi nord.
        pose.remplir(-20, 44, murZ - 2, 20, 63, murZ + 3, Blocks.DEEPSLATE_BRICKS);
        for (int x = -20; x <= 20; x++) {
            for (int y = 44; y <= 63; y++) {
                boolean bord = Math.abs(x) == 20 || y == 63 || y <= 46;
                pose.poser(x, y, murZ + 4, bord ? bordure : Blocks.CALCITE.defaultBlockState());
            }
        }
        // La lumière du feu sur l'écran : des blocs de lumière invisibles, sinon il resterait dans le noir.
        BlockState lumiere = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 11);
        for (int x = -15; x <= 15; x += 10) {
            pose.poser(x, 49, murZ + 6, lumiere);
            pose.poser(x, 57, murZ + 6, lumiere);
        }

        // Le muret entre les prisonniers et le feu, avec un passage au milieu.
        for (int x = -14; x <= 14; x++) {
            if (Math.abs(x) <= 1) {
                continue;
            }
            int sol = CaverneRoyaume.sol(x, -2);
            pose.poser(x, sol, -2, Blocks.DEEPSLATE_BRICKS);
            pose.poser(x, sol + 1, -2, Blocks.DEEPSLATE_BRICKS);
        }

        // Le feu : un foyer en croix de feux de camp sur un lit de pierre.
        int[][] foyer = {{0, 0}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : foyer) {
            int x = d[0];
            int z = CaverneRoyaume.FEU_Z + d[1];
            int sol = CaverneRoyaume.sol(x, z);
            pose.poser(x, sol, z, Blocks.COBBLED_DEEPSLATE);
            pose.poser(x, sol + 1, z, Blocks.CAMPFIRE);
        }

        // Les poteaux des prisonniers, de part et d'autre du point d'arrivée, avec leurs chaînes au sol.
        int z = CaverneRoyaume.ARRIVEE_Z;
        for (int x : new int[]{-12, -6, 6, 12}) {
            int sol = CaverneRoyaume.sol(x, z);
            pose.poser(x, sol + 1, z, Blocks.DEEPSLATE_BRICKS);
            pose.poser(x, sol + 2, z, Blocks.DEEPSLATE_BRICKS);
            pose.poser(x, sol + 3, z, Blocks.DEEPSLATE_BRICK_SLAB);
            BlockState chaine = Blocks.CHAIN.defaultBlockState().setValue(ChainBlock.AXIS, Direction.Axis.X);
            pose.poser(x + 1, CaverneRoyaume.sol(x + 1, z) + 1, z, chaine);
            pose.poser(x - 1, CaverneRoyaume.sol(x - 1, z) + 1, z, chaine);
        }
    }

    private static void portailRetour(Pose pose) {
        int x0 = PORTAIL_X;
        int y0 = PORTAIL_Y;
        int z = PORTAIL_Z;
        // Dégage la place (herbes, buissons) au-dessus de la plateforme.
        pose.remplir(x0 - 1, y0, z - 1, x0 + 4, y0 + 6, z + 1, Blocks.AIR);
        // Plateforme en Pierre d'Ombre, soutenue jusqu'au sol là où le terrain descend.
        for (int x = x0 - 1; x <= x0 + 4; x++) {
            for (int dz = -1; dz <= 1; dz++) {
                int surface = ReliefRoyaume.colonne(x, z + dz).surface();
                pose.remplir(x, surface, z + dz, x, y0 - 1, z + dz, ModBlocs.PIERRE_OMBRE.get().defaultBlockState());
            }
        }
        // Cadre de 4 sur 5 en Pierre d'Ombre taillée, intérieur en portail.
        BlockState cadre = ModBlocs.PIERRE_OMBRE_TAILLEE.get().defaultBlockState();
        BlockState portail = ModBlocs.PORTAIL_ROYAUME.get().defaultBlockState().setValue(PortailRoyaumeBloc.AXE, Direction.Axis.X);
        for (int x = x0; x <= x0 + 3; x++) {
            for (int y = y0; y <= y0 + 4; y++) {
                boolean bord = x == x0 || x == x0 + 3 || y == y0 || y == y0 + 4;
                pose.poser(x, y, z, bord ? cadre : portail);
            }
        }
        // Lanternes d'âme sur les deux coins du haut, en écho aux lanternes du portail de l'Overworld.
        pose.poser(x0, y0 + 5, z, Blocks.SOUL_LANTERN);
        pose.poser(x0 + 3, y0 + 5, z, Blocks.SOUL_LANTERN);
    }
}
