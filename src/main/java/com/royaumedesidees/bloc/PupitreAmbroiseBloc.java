package com.royaumedesidees.bloc;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Le Pupitre d'Ambroise : le lutrin où l'évêque lit en silence, sans bouger les lèvres (Confessions, VI, 3). Il crée
 * une zone de silence de 8 blocs autour de lui (voir {@link ZonesSilence}) : le chat y est muet et les monstres n'y
 * repèrent pas les joueurs. Quelques lettres s'en échappent, comme d'une table d'enchantement.
 */
public class PupitreAmbroiseBloc extends HorizontalDirectionalBlock implements EntityBlock {
    public static final MapCodec<PupitreAmbroiseBloc> CODEC = simpleCodec(PupitreAmbroiseBloc::new);

    public PupitreAmbroiseBloc(Properties proprietes) {
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
    protected VoxelShape getShape(BlockState etat, BlockGetter niveau, BlockPos pos, CollisionContext contexte) {
        return LecternBlock.SHAPE_COMMON;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState etat, BlockGetter niveau, BlockPos pos, CollisionContext contexte) {
        return LecternBlock.SHAPE_COLLISION;
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState etat) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState etat) {
        return new PupitreAmbroiseBlocEntite(pos, etat);
    }

    @Override
    public void animateTick(BlockState etat, Level niveau, BlockPos pos, RandomSource hasard) {
        if (hasard.nextInt(3) == 0) {
            niveau.addParticle(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 1.6, pos.getZ() + 0.5,
                    (hasard.nextFloat() - 0.5) * 2.0, -0.6 - hasard.nextFloat() * 0.6, (hasard.nextFloat() - 0.5) * 2.0);
        }
    }

    /**
     * Appelé après chaque changement de la grille de craft (voir {@code CraftingMenuMixin}) : si le résultat est un
     * pupitre et que le joueur n'a pas encore tenu le silence d'Ambroise, le résultat reste vide.
     */
    public static void verifierCraft(net.minecraft.world.inventory.AbstractContainerMenu menu, net.minecraft.server.level.ServerPlayer joueur,
                                     net.minecraft.world.inventory.ResultContainer resultat) {
        if (!resultat.getItem(0).is(com.royaumedesidees.registre.ModItems.PUPITRE_AMBROISE.get())
                || joueur.getData(com.royaumedesidees.registre.ModPiecesJointes.PUPITRE_APPRIS)) {
            return;
        }
        net.minecraft.world.item.ItemStack vide = net.minecraft.world.item.ItemStack.EMPTY;
        resultat.setItem(0, vide);
        menu.setRemoteSlot(0, vide);
        joueur.connection.send(new net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket(menu.containerId,
                menu.incrementStateId(), 0, vide));
        joueur.displayClientMessage(net.minecraft.network.chat.Component.translatable("message.royaumedesidees.pupitre.pas_appris")
                .withStyle(net.minecraft.ChatFormatting.GRAY), true);
    }

    @Override
    public void appendHoverText(net.minecraft.world.item.ItemStack pile, net.minecraft.world.item.Item.TooltipContext contexte,
                                java.util.List<net.minecraft.network.chat.Component> lignes, net.minecraft.world.item.TooltipFlag options) {
        lignes.add(net.minecraft.network.chat.Component.translatable("block.royaumedesidees.pupitre_ambroise.aide").withStyle(net.minecraft.ChatFormatting.GRAY));
        lignes.add(net.minecraft.network.chat.Component.translatable("block.royaumedesidees.pupitre_ambroise.recette").withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
    }
}
