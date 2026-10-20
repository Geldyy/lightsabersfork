package com.fiskmods.pika.lightsabers.block.gathering;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SolarLensBlock extends Block {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

	public SolarLensBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		boolean isNoonSunlight = checkNoonSunlight(level, pos);
		boolean wasPowered = state.getValue(POWERED);

		if (isNoonSunlight && !wasPowered) {
			activateLens(level, pos, state, null);
		} else if (!isNoonSunlight && wasPowered) {
			level.setBlock(pos, state.setValue(POWERED, false), 3);
		}

		level.scheduleTick(pos, this, 40);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!level.isClientSide) {
			level.scheduleTick(pos, this, 20);
		}
	}

	private boolean checkNoonSunlight(ServerLevel level, BlockPos pos) {
		if (!level.isDay() || level.isRaining()) {
			return false;
		}
		if (!level.canSeeSky(pos.above())) {
			return false;
		}
		long dayTime = level.getDayTime() % 24000L;
		// High noon: between 5000 and 7000 ticks
		return dayTime >= 5000L && dayTime <= 7000L;
	}

	public void activateLens(Level level, BlockPos pos, BlockState state, Player player) {
		if (level.isClientSide)
			return;

		level.setBlock(pos, state.setValue(POWERED, true), 3);
		level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.2F, 1.4F);

		if (level instanceof ServerLevel sl) {
			sl.sendParticles(ParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, 25, 0.3D,
					0.5D, 0.3D, 0.08D);
			sl.sendParticles(ParticleTypes.FLAME, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, 15, 0.2D,
					0.3D, 0.2D, 0.04D);
		}

		// Trigger all GlacierGateBlocks in a 24-block radius to open/melt
		AABB searchArea = new AABB(pos).inflate(24.0D);
		for (BlockPos targetPos : BlockPos.betweenClosed((int) searchArea.minX, (int) searchArea.minY,
				(int) searchArea.minZ, (int) searchArea.maxX, (int) searchArea.maxY, (int) searchArea.maxZ)) {
			BlockState targetState = level.getBlockState(targetPos);
			if (targetState.getBlock() instanceof GlacierGateBlock gate) {
				gate.triggerMelting(level, targetPos, targetState);
			}
		}
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		// Can also be manually focused using heat/lightsaber
		if (!state.getValue(POWERED)) {
			activateLens(level, pos, state, player);
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
