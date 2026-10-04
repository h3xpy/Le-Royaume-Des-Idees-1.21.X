package com.royaumedesidees.bloc;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Le Confessionnal : une cabine de bois de deux blocs de haut, comme une porte. On s'y confesse avec la commande
 * {@code /confesse <ton péché>} en se tenant à 5 blocs ou moins ; un clic droit le rappelle.
 */
public class ConfessionnalBloc extends HorizontalDirectionalBlock {
    public static final MapCodec<ConfessionnalBloc> CODEC = simpleCodec(ConfessionnalBloc::new);
    public static final EnumProperty<DoubleBlockHalf> MOITIE = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public ConfessionnalBloc(Properties proprietes) {
        super(proprietes);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(MOITIE, DoubleBlockHalf.LOWER));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> constructeur) {
        constructeur.add(FACING, MOITIE);
    }

    /** Pose : la grille regarde le joueur, et il faut un bloc libre au-dessus pour la moitié haute. */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext contexte) {
        BlockPos pos = contexte.getClickedPos();
        Level niveau = contexte.getLevel();
        if (pos.getY() < niveau.getMaxBuildHeight() - 1 && niveau.getBlockState(pos.above()).canBeReplaced(contexte)) {
            return defaultBlockState().setValue(FACING, contexte.getHorizontalDirection().getOpposite());
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level niveau, BlockPos pos, BlockState etat, @Nullable LivingEntity poseur, ItemStack pile) {
        niveau.setBlock(pos.above(), etat.setValue(MOITIE, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
    }

    /** Les deux moitiés vont ensemble : casser l'une casse l'autre. */
    @Override
    protected BlockState updateShape(BlockState etat, Direction direction, BlockState voisin, LevelAccessor niveau, BlockPos pos, BlockPos posVoisin) {
        DoubleBlockHalf moitie = etat.getValue(MOITIE);
        boolean versLAutreMoitie = direction == (moitie == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN);
        if (versLAutreMoitie && !(voisin.is(this) && voisin.getValue(MOITIE) != moitie)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(etat, direction, voisin, niveau, pos, posVoisin);
    }

    /** En créatif, casser la moitié haute ne doit pas faire tomber d'objet en double. */
    @Override
    public BlockState playerWillDestroy(Level niveau, BlockPos pos, BlockState etat, Player joueur) {
        if (!niveau.isClientSide && joueur.isCreative() && etat.getValue(MOITIE) == DoubleBlockHalf.UPPER) {
            // On retire la moitié basse sans la faire tomber : c'est elle qui porte le butin.
            BlockPos dessous = pos.below();
            BlockState basse = niveau.getBlockState(dessous);
            if (basse.is(this) && basse.getValue(MOITIE) == DoubleBlockHalf.LOWER) {
                niveau.setBlock(dessous, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                niveau.levelEvent(joueur, LevelEvent.PARTICLES_DESTROY_BLOCK, dessous, Block.getId(basse));
            }
        }
        return super.playerWillDestroy(niveau, pos, etat, joueur);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState etat, Level niveau, BlockPos pos, Player joueur, BlockHitResult impact) {
        if (!niveau.isClientSide) {
            joueur.displayClientMessage(Component.translatable("message.royaumedesidees.confessionnal.mode_emploi"), true);
        }
        return InteractionResult.sidedSuccess(niveau.isClientSide);
    }

    @Override
    public void appendHoverText(net.minecraft.world.item.ItemStack pile, net.minecraft.world.item.Item.TooltipContext contexte,
                                java.util.List<net.minecraft.network.chat.Component> lignes, net.minecraft.world.item.TooltipFlag options) {
        lignes.add(net.minecraft.network.chat.Component.translatable("block.royaumedesidees.confessionnal.aide").withStyle(net.minecraft.ChatFormatting.GRAY));
    }
}
