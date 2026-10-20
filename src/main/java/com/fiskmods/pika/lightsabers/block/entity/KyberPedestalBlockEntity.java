package com.fiskmods.pika.lightsabers.block.entity;

import java.util.UUID;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.ALBlocks;
import com.fiskmods.pika.lightsabers.ALEntities;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.entity.ForcePhantomEntity;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class KyberPedestalBlockEntity extends BlockEntity {

	// 0 = IDLE, 1 = ACTIVE, 2 = JEDI_CLEARED, 3 = SITH_CLEARED
	private int trialState = 0;
	private UUID activePlayerUUID = null;

	public KyberPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(ALBlockEntities.KYBER_PEDESTAL.get(), pos, state);
	}

	public void interact(ServerPlayer player) {
		if (trialState != 0) {
			if (trialState == 2) {
				player.displayClientMessage(Component.translatable("message.lightsabersfork.pedestal_jedi_cleared"), true);
			} else if (trialState == 3) {
				player.displayClientMessage(Component.translatable("message.lightsabersfork.pedestal_sith_cleared"), true);
			}
			return;
		}

		startTrial(player);
	}

	public void startTrial(ServerPlayer player) {
		this.trialState = 1;
		this.activePlayerUUID = player.getUUID();
		setChanged();

		if (this.level instanceof ServerLevel sl) {
			// Mystical fog / darkness immersion
			player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 400, 0, false, false));

			sl.playSound(null, this.worldPosition, SoundEvents.WARDEN_HEARTBEAT, SoundSource.BLOCKS, 1.2F, 0.8F);
			sl.playSound(null, this.worldPosition, SoundEvents.PORTAL_TRAVEL, SoundSource.BLOCKS, 0.8F, 1.2F);

			sl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.worldPosition.getX() + 0.5D,
					this.worldPosition.getY() + 1.2D, this.worldPosition.getZ() + 0.5D, 25, 0.3D, 0.3D, 0.3D, 0.05D);

			// Spawn the Phantom
			ForcePhantomEntity phantom = new ForcePhantomEntity(ALEntities.FORCE_PHANTOM.get(), sl);
			phantom.setPos(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 1.0D,
					this.worldPosition.getZ() + 3.0D);
			phantom.setupTrial(player.getUUID(), this.worldPosition);
			sl.addFreshEntity(phantom);

			player.displayClientMessage(Component.translatable("message.lightsabersfork.trial_started"), true);
		}
	}

	public void resolveJedi(ServerPlayer player) {
		this.trialState = 2;
		setChanged();

		if (this.level instanceof ServerLevel sl) {
			sl.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, this.worldPosition.getX() + 0.5D,
					this.worldPosition.getY() + 1.2D, this.worldPosition.getZ() + 0.5D, 35, 0.3D, 0.5D, 0.3D, 0.1D);
			sl.playSound(null, this.worldPosition, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.2F, 1.5F);
			sl.playSound(null, this.worldPosition, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.2F);

			// Reward pure Jedi Kyber crystal
			ItemStack drop = new ItemStack(ALItems.LIGHT_BLUE_CRYSTAL.get());
			Block.popResource(sl, this.worldPosition.above(), drop);

			PlayerForceData data = PlayerForceData.get(player);
			if (data != null) {
				data.addForcePoints(2);
				data.sync(player);
			}

			player.displayClientMessage(Component.translatable("message.lightsabersfork.trial_jedi_success"), false);
		}
	}

	public void resolveSith(ServerPlayer player) {
		this.trialState = 3;
		setChanged();

		if (this.level instanceof ServerLevel sl) {
			sl.sendParticles(ParticleTypes.LAVA, this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 1.2D,
					this.worldPosition.getZ() + 0.5D, 30, 0.4D, 0.6D, 0.4D, 0.1D);
			sl.playSound(null, this.worldPosition, SoundEvents.WITHER_DEATH, SoundSource.BLOCKS, 1.0F, 1.0F);
			sl.playSound(null, this.worldPosition, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.BLOCKS, 0.8F, 1.2F);

			// Reward bled red crystal and unstable focus crystal
			ItemStack redCrystal = new ItemStack(ALItems.RED_CRYSTAL.get());
			ItemStack focusCrystal = new ItemStack(ALItems.FOCUSING_CRYSTAL_CRACKED.get());
			Block.popResource(sl, this.worldPosition.above(), redCrystal);
			Block.popResource(sl, this.worldPosition.above(), focusCrystal);

			// Room Corruption: Transform light_forcestone to dark_forcestone in an 8-block radius
			BlockPos.betweenClosedStream(this.worldPosition.offset(-8, -4, -8), this.worldPosition.offset(8, 6, 8))
					.forEach(targetPos -> {
						BlockState state = sl.getBlockState(targetPos);
						if (state.is(ALBlocks.LIGHT_FORCESTONE.get())) {
							sl.setBlock(targetPos, ALBlocks.DARK_FORCESTONE.get().defaultBlockState(), 3);
						} else if (state.is(ALBlocks.LIGHT_FORCESTONE_PILLAR.get())) {
							sl.setBlock(targetPos, ALBlocks.DARK_FORCESTONE_PILLAR.get().defaultBlockState(), 3);
						}
					});

			PlayerForceData data = PlayerForceData.get(player);
			if (data != null) {
				data.addForcePoints(2);
				data.sync(player);
			}

			player.displayClientMessage(Component.translatable("message.lightsabersfork.trial_sith_success"), false);
		}
	}

	public static void tick(Level level, BlockPos pos, BlockState state, KyberPedestalBlockEntity be) {
		if (level.isClientSide)
			return;

		// Idle ambient particle
		if (be.trialState == 0 && level.random.nextFloat() < 0.1F) {
			((ServerLevel) level).sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5D, pos.getY() + 1.2D,
					pos.getZ() + 0.5D, 1, 0.2D, 0.2D, 0.2D, 0.05D);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt("TrialState", this.trialState);
		if (this.activePlayerUUID != null) {
			tag.putUUID("ActivePlayerUUID", this.activePlayerUUID);
		}
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.trialState = tag.getInt("TrialState");
		if (tag.hasUUID("ActivePlayerUUID")) {
			this.activePlayerUUID = tag.getUUID("ActivePlayerUUID");
		}
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
