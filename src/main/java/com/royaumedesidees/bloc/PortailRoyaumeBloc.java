package com.royaumedesidees.bloc;

import com.royaumedesidees.portail.CadrePortail;
import com.royaumedesidees.portail.VoyageRoyaume;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Bloc qui remplit un portail du Royaume. Comme le portail du Nether, c'est une plaque fine orientée selon l'axe
 * du cadre, et il faut y rester un moment pour partir (instantané en créatif). Seuls les joueurs voyagent.
 */
public class PortailRoyaumeBloc extends Block implements Portal {
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

    /** Si un bloc du cadre est cassé, le portail s'éteint. */
    @Override
    protected BlockState updateShape(BlockState etat, Direction direction, BlockState voisin, LevelAccessor niveau, BlockPos pos, BlockPos posVoisin) {
        boolean dansLePlan = direction.getAxis() == etat.getValue(AXE) || direction.getAxis().isVertical();
        if (dansLePlan && !voisin.is(this) && !CadrePortail.portailEncadre(niveau, pos, etat.getValue(AXE))) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(etat, direction, voisin, niveau, pos, posVoisin);
    }

    @Override
    protected void entityInside(BlockState etat, Level niveau, BlockPos pos, Entity entite) {
        if (entite instanceof Player && entite.canUsePortal(false)) {
            entite.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public int getPortalTransitionTime(ServerLevel niveau, Entity entite) {
        boolean invulnerable = entite instanceof Player joueur && joueur.getAbilities().invulnerable;
        return Math.max(1, niveau.getGameRules().getInt(invulnerable
                ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY
                : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY));
    }

    @Nullable
    @Override
    public DimensionTransition getPortalDestination(ServerLevel niveau, Entity entite, BlockPos pos) {
        return VoyageRoyaume.destination(niveau, entite);
    }

    @Override
    public Transition getLocalTransition() {
        return Transition.CONFUSION;
    }

    /** Lettres dorées qui s'envolent du portail (il est silencieux : la musique est celle du Royaume). */
    @Override
    public void animateTick(BlockState etat, Level niveau, BlockPos pos, RandomSource hasard) {
        for (int i = 0; i < 2; i++) {
            niveau.addParticle(ParticleTypes.ENCHANT,
                    pos.getX() + hasard.nextDouble(), pos.getY() + hasard.nextDouble(), pos.getZ() + hasard.nextDouble(),
                    (hasard.nextDouble() - 0.5) * 0.5, hasard.nextDouble() * 0.6, (hasard.nextDouble() - 0.5) * 0.5);
        }
    }
}
