package com.royaumedesidees.bloc;

import com.mojang.serialization.MapCodec;
import com.royaumedesidees.registre.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Étal du verger : une caisse de poires avec un tronc à aumônes. En attendant les marchands (v0.3) et les Sols
 * (v0.5), une émeraude donne 3 poires achetées honnêtement, donc sans Culpabilité.
 */
public class EtalVergerBloc extends HorizontalDirectionalBlock {
    public static final MapCodec<EtalVergerBloc> CODEC = simpleCodec(EtalVergerBloc::new);
    public static final int POIRES_PAR_EMERAUDE = 3;

    public EtalVergerBloc(Properties proprietes) {
        super(proprietes);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> constructeur) {
        constructeur.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext contexte) {
        return defaultBlockState().setValue(FACING, contexte.getHorizontalDirection().getOpposite());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pile, BlockState etat, Level niveau, BlockPos pos, Player joueur,
                                              InteractionHand main, BlockHitResult impact) {
        if (!pile.is(Items.EMERALD)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!niveau.isClientSide) {
            pile.consume(1, joueur);
            ItemStack poires = new ItemStack(ModItems.POIRE.get(), POIRES_PAR_EMERAUDE);
            if (!joueur.getInventory().add(poires)) {
                joueur.drop(poires, false);
            }
            niveau.playSound(null, pos, SoundEvents.VILLAGER_YES, SoundSource.BLOCKS, 0.8F, 1.1F);
            joueur.displayClientMessage(Component.translatable("message.royaumedesidees.etal.achat", POIRES_PAR_EMERAUDE), true);
        }
        return ItemInteractionResult.sidedSuccess(niveau.isClientSide);
    }

    /** Sans émeraude : on lit l'écriteau. */
    @Override
    protected InteractionResult useWithoutItem(BlockState etat, Level niveau, BlockPos pos, Player joueur, BlockHitResult impact) {
        if (!niveau.isClientSide) {
            joueur.displayClientMessage(Component.translatable("message.royaumedesidees.etal.ecriteau", POIRES_PAR_EMERAUDE), true);
        }
        return InteractionResult.sidedSuccess(niveau.isClientSide);
    }
}
