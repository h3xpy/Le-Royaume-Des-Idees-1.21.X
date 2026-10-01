package com.royaumedesidees.bloc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Bloc qui remplit le cadre de bibliothèques une fois allumé. Comme le portail du Nether,
 * c'est une plaque fine orientée selon l'axe du cadre. Le voyage est ajouté à l'étape 7.
 */
public class PortailRoyaumeBloc extends Block {
    public static final EnumProperty<Direction.Axis> AXE = BlockStateProperties.HORIZONTAL_AXIS;
    private static final VoxelShape FORME_X = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    private static final VoxelShape FORME_Z = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    public PortailRoyaumeBloc(Properties proprietes) {
        super(proprietes);
        registerDefaultState(stateDefinition.any().setValue(AXE, Direction.Axis.X));
    }

    @Override
    protected VoxelShape getShape(BlockState etat, BlockGetter niveau, BlockPos pos, CollisionContext contexte) {
        return etat.getValue(AXE) == Direction.Axis.Z ? FORME_Z : FORME_X;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> constructeur) {
        constructeur.add(AXE);
    }
}
