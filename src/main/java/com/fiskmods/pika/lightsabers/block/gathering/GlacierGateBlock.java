package com.fiskmods.pika.lightsabers.block.gathering;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.block.entity.GlacierGateBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlacierGateBlock extends BaseEntityBlock {

	public static final MapCodec<GlacierGateBlock> CODEC = simpleCodec(GlacierGateBlock::new);
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

	public GlacierGateBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, false));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(OPEN);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(OPEN) ? Shapes.empty() : Shapes.block();
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GlacierGateBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ALBlockEntities.GLACIER_GATE.get(), GlacierGateBlockEntity::tick);
	}

	public void triggerMelting(Level level, BlockPos pos, BlockState state) {
		if (state.getValue(OPEN))
			return;

		level.setBlock(pos, state.setValue(OPEN, true), 3);
		level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F);
		level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.8F, 1.2F);

		if (level instanceof ServerLevel sl) {
			sl.sendParticles(ParticleTypes.SPLASH, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 20, 0.4D,
					0.4D, 0.4D, 0.1D);
			sl.sendParticles(ParticleTypes.CLOUD, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 10, 0.3D,
					0.3D, 0.3D, 0.05D);
		}

		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof GlacierGateBlockEntity gateBe) {
			gateBe.startMeltCountdown();
		}
	}
}
