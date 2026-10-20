package com.fiskmods.pika.lightsabers.block.entity;

import java.util.UUID;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ResonantKyberBlockEntity extends BlockEntity {

	private UUID ownerUUID = null;
	private int soundCooldown = 0;

	public ResonantKyberBlockEntity(BlockPos pos, BlockState state) {
		super(ALBlockEntities.RESONANT_KYBER.get(), pos, state);
	}

	public void setOwner(UUID uuid) {
		this.ownerUUID = uuid;
		setChanged();
	}

	public boolean isAttunedTo(Player player) {
		return ownerUUID == null || ownerUUID.equals(player.getUUID());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, ResonantKyberBlockEntity be) {
		if (!(level instanceof ServerLevel sl))
			return;

		ServerPlayer target = null;
		if (be.ownerUUID != null) {
			target = sl.getServer().getPlayerList().getPlayer(be.ownerUUID);
		} else {
			// Auto-attune to the nearest force-sensitive player in range
			for (ServerPlayer sp : sl.players()) {
				if (sp.blockPosition().closerThan(pos, 32.0D)) {
					PlayerForceData d = PlayerForceData.get(sp);
					if (d != null && d.isForceSensitive()) {
						be.setOwner(sp.getUUID());
						target = sp;
						break;
					}
				}
			}
		}

		if (target == null || !target.blockPosition().closerThan(pos, 32.0D)) {
			return;
		}

		double dist = Math.sqrt(pos.distSqr(target.blockPosition()));

		// Dynamic Echo Chime interval based on distance
		if (be.soundCooldown <= 0) {
			float volume = (float) Math.max(0.15D, 1.0D - (dist / 32.0D));
			float pitch = 1.0F + (float) ((32.0D - dist) / 32.0D) * 0.4F;
			sl.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, volume, pitch);
			be.soundCooldown = Math.max(12, (int) (dist * 3.5D));
		} else {
			be.soundCooldown--;
		}

		// When meditating, the crystal shines brightly through the dark
		boolean isMeditating = target.getPersistentData().getBoolean("IsMeditating");
		if (isMeditating || sl.random.nextFloat() < 0.2F) {
			sl.sendParticles(ParticleTypes.INSTANT_EFFECT, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 3,
					0.2D, 0.2D, 0.2D, 0.05D);
			sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 2,
					0.15D, 0.15D, 0.15D, 0.02D);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		if (ownerUUID != null) {
			tag.putUUID("OwnerUUID", ownerUUID);
		}
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.hasUUID("OwnerUUID")) {
			ownerUUID = tag.getUUID("OwnerUUID");
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
