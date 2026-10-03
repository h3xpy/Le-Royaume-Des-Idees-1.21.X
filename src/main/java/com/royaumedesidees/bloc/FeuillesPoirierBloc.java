package com.royaumedesidees.bloc;

import com.mojang.serialization.MapCodec;
import com.royaumedesidees.jardin.Culpabilite;
import com.royaumedesidees.registre.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Feuilles de poirier des vergers du Jardin. Chargées de poires, un clic droit en cueille une : c'est un vol
 * (les vergers ont un propriétaire), donc +1 de Culpabilité et un message de honte à tout le serveur.
 * Les poires repoussent lentement, en moyenne une fois par jour de jeu.
 */
public class FeuillesPoirierBloc extends LeavesBlock {
    public static final MapCodec<FeuillesPoirierBloc> CODEC = simpleCodec(FeuillesPoirierBloc::new);
    public static final BooleanProperty POIRES = BooleanProperty.create("poires");
    /** Un bloc reçoit un tick aléatoire environ toutes les 68 s : 1 chance sur 18 donne environ un jour de jeu. */
    private static final int CHANCE_REPOUSSE = 18;

    public FeuillesPoirierBloc(Properties proprietes) {
        super(proprietes);
        registerDefaultState(defaultBlockState().setValue(POIRES, true));
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> constructeur) {
        super.createBlockStateDefinition(constructeur);
        constructeur.add(POIRES);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState etat) {
        return !etat.getValue(POIRES) || super.isRandomlyTicking(etat);
    }

    @Override
    protected void randomTick(BlockState etat, ServerLevel niveau, BlockPos pos, RandomSource hasard) {
        if (!etat.getValue(POIRES) && hasard.nextInt(CHANCE_REPOUSSE) == 0) {
            niveau.setBlockAndUpdate(pos, etat.setValue(POIRES, true));
            return;
        }
        super.randomTick(etat, niveau, pos, hasard);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState etat, Level niveau, BlockPos pos, Player joueur, BlockHitResult impact) {
        if (!etat.getValue(POIRES)) {
            return InteractionResult.PASS;
        }
        if (niveau.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        niveau.setBlockAndUpdate(pos, etat.setValue(POIRES, false));
        niveau.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.9F);
        popResource(niveau, pos, new ItemStack(ModItems.POIRE_VOLEE.get()));
        if (joueur instanceof ServerPlayer serveur) {
            Culpabilite.volerPoire(serveur);
        }
        return InteractionResult.CONSUME;
    }
}
