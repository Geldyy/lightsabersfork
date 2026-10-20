package com.fiskmods.pika.lightsabers.block.entity;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.block.gathering.GlacierGateBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GlacierGateBlockEntity extends BlockEntity {

	// 3600 ticks = 3 minutes open
	public static final int OPEN_TICKS = 3600;
	private int meltTimer = 0;

	public GlacierGateBlockEntity(BlockPos pos, BlockState state) {
		super(ALBlockEntities.GLACIER_GATE.get(), pos, state);
	}

	public void startMeltCountdown() {
		this.meltTimer = OPEN_TICKS;
		setChanged();
	}

	public static void tick(Level level, BlockPos pos, BlockState state, GlacierGateBlockEntity be) {
		if (level.isClientSide)
			return;

		if (state.getValue(GlacierGateBlock.OPEN)) {
			if (be.meltTimer > 0) {
				be.meltTimer--;

				// Warning sounds 15 seconds before refreezing
				if (be.meltTimer == 300) {
					level.playSound(null, pos, SoundEvents.POWDER_SNOW_STEP, SoundSource.BLOCKS, 1.0F, 0.7F);
					if (level instanceof ServerLevel sl) {
						sl.sendParticles(ParticleTypes.SNOWFLAKE, pos.getX() + 0.5D, pos.getY() + 0.5D,
								pos.getZ() + 0.5D, 8, 0.3D, 0.3D, 0.3D, 0.02D);
					}
				}

				if (be.meltTimer <= 0) {
					// Refreeze the gate
					level.setBlock(pos, state.setValue(GlacierGateBlock.OPEN, false), 3);
					level.playSound(null, pos, SoundEvents.PLAYER_HURT_FREEZE, SoundSource.BLOCKS, 1.2F, 0.8F);
					level.playSound(null, pos, SoundEvents.GLASS_PLACE, SoundSource.BLOCKS, 1.0F, 0.9F);
					if (level instanceof ServerLevel sl) {
						sl.sendParticles(ParticleTypes.SNOWFLAKE, pos.getX() + 0.5D, pos.getY() + 0.5D,
								pos.getZ() + 0.5D, 20, 0.4D, 0.4D, 0.4D, 0.05D);
					}
				}
				be.setChanged();
			}
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt("MeltTimer", this.meltTimer);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.meltTimer = tag.getInt("MeltTimer");
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, registries);
		return tag;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
