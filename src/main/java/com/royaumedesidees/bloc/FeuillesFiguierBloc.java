package com.royaumedesidees.bloc;

import com.mojang.serialization.MapCodec;
import com.royaumedesidees.registre.ModPiecesJointes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Feuilles de figuier. En 386, Augustin pleurait sous un figuier quand une voix d'enfant chanta « prends, lis » :
 * la première fois qu'un joueur touche un figuier, il entend ces mots.
 */
public class FeuillesFiguierBloc extends LeavesBlock {
    public static final MapCodec<FeuillesFiguierBloc> CODEC = simpleCodec(FeuillesFiguierBloc::new);

    public FeuillesFiguierBloc(Properties proprietes) {
        super(proprietes);
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState etat, Level niveau, BlockPos pos, Player joueur, BlockHitResult impact) {
        if (joueur.getData(ModPiecesJointes.FIGUIER_ENTENDU)) {
            return InteractionResult.PASS;
        }
        if (!niveau.isClientSide) {
            joueur.setData(ModPiecesJointes.FIGUIER_ENTENDU, true);
            joueur.displayClientMessage(Component.translatable("message.royaumedesidees.figuier.voix"), true);
            niveau.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.4F);
        }
        return InteractionResult.sidedSuccess(niveau.isClientSide);
    }
}
