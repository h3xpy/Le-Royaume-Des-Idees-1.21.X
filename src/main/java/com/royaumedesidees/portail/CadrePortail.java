package com.royaumedesidees.portail;

import com.royaumedesidees.bloc.PortailRoyaumeBloc;
import com.royaumedesidees.registre.ModBlocs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * Le cadre d'un portail du Royaume : un rectangle vertical de 4 blocs de large sur 5 de haut (bords
 * extérieurs), dont l'intérieur fait 2 sur 3. Dans l'Overworld, il est en bibliothèques avec une lanterne
 * à chaque coin ; le portail de retour du Royaume est en Pierre d'Ombre taillée.
 *
 * @param origine coin bas du cadre (le plus petit x ou z selon l'axe)
 * @param axe     axe horizontal le long duquel s'étend le cadre (X ou Z)
 */
public record CadrePortail(BlockPos origine, Direction.Axis axe) {
    public static final int LARGEUR = 4;
    public static final int HAUTEUR = 5;

    /** Position d'une case du cadre : i le long de l'axe (0 à 3), j vers le haut (0 à 4). */
    public BlockPos case_(int i, int j) {
        return axe == Direction.Axis.X ? origine.offset(i, j, 0) : origine.offset(0, j, i);
    }

    private static boolean surLeBord(int i, int j) {
        return i == 0 || i == LARGEUR - 1 || j == 0 || j == HAUTEUR - 1;
    }

    /**
     * Cherche un cadre de bibliothèques complet (avec ses quatre lanternes et un intérieur libre) dont fait partie
     * le bloc {@code pos}.
     */
    public static Optional<CadrePortail> trouverPourAllumage(LevelReader niveau, BlockPos pos) {
        for (Direction.Axis axe : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
            for (int i = 0; i < LARGEUR; i++) {
                for (int j = 0; j < HAUTEUR; j++) {
                    if (!surLeBord(i, j)) {
                        continue;
                    }
                    BlockPos origine = axe == Direction.Axis.X ? pos.offset(-i, -j, 0) : pos.offset(0, -j, -i);
                    CadrePortail cadre = new CadrePortail(origine, axe);
                    if (cadre.bordEn(niveau, Blocks.BOOKSHELF) && cadre.interieurLibre(niveau) && cadre.lanternesAuxCoins(niveau)) {
                        return Optional.of(cadre);
                    }
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Vrai si le bloc de portail en {@code pos} est toujours entouré d'un cadre complet (en bibliothèques ou en
     * Pierre d'Ombre taillée). Les lanternes ne comptent que pour l'allumage.
     */
    public static boolean portailEncadre(LevelReader niveau, BlockPos pos, Direction.Axis axe) {
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 3; j++) {
                BlockPos origine = axe == Direction.Axis.X ? pos.offset(-i, -j, 0) : pos.offset(0, -j, -i);
                CadrePortail cadre = new CadrePortail(origine, axe);
                if (cadre.bordEn(niveau, Blocks.BOOKSHELF) || cadre.bordEn(niveau, ModBlocs.PIERRE_OMBRE_TAILLEE.get())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean bordEn(LevelReader niveau, Block materiau) {
        for (int i = 0; i < LARGEUR; i++) {
            for (int j = 0; j < HAUTEUR; j++) {
                if (surLeBord(i, j) && !niveau.getBlockState(case_(i, j)).is(materiau)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean interieurLibre(LevelReader niveau) {
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 3; j++) {
                BlockState etat = niveau.getBlockState(case_(i, j));
                if (!etat.isAir() && !etat.is(ModBlocs.PORTAIL_ROYAUME.get())) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Chaque coin doit avoir une lanterne (normale ou d'âme) contre lui, hors du cadre : dessus, à côté, devant ou derrière. */
    private boolean lanternesAuxCoins(LevelReader niveau) {
        int[][] coins = {{0, 0}, {LARGEUR - 1, 0}, {0, HAUTEUR - 1}, {LARGEUR - 1, HAUTEUR - 1}};
        for (int[] coin : coins) {
            BlockPos pos = case_(coin[0], coin[1]);
            boolean trouvee = false;
            for (Direction direction : Direction.values()) {
                BlockState voisin = niveau.getBlockState(pos.relative(direction));
                if (voisin.is(Blocks.LANTERN) || voisin.is(Blocks.SOUL_LANTERN)) {
                    trouvee = true;
                    break;
                }
            }
            if (!trouvee) {
                return false;
            }
        }
        return true;
    }

    /** Remplit l'intérieur de blocs de portail. */
    public void allumer(LevelAccessor niveau) {
        BlockState portail = ModBlocs.PORTAIL_ROYAUME.get().defaultBlockState().setValue(PortailRoyaumeBloc.AXE, axe);
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 3; j++) {
                niveau.setBlock(case_(i, j), portail, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
            }
        }
    }
}
