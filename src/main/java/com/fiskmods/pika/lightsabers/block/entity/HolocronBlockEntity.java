package com.fiskmods.pika.lightsabers.block.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HolocronBlockEntity extends BlockEntity {

	private final Set<UUID> playersUsing = new HashSet<>();
	private int clientUsersCount = 0;

	public float openTimer = 0.0F;
	public float prevOpenTimer = 0.0F;
	public int openTicks = 0;
	public int prevOpenTicks = 0;

	public HolocronBlockEntity(BlockPos pos, BlockState state) {
		super(ALBlockEntities.HOLOCRON.get(), pos, state);
	}

	public void setPlayerUsing(Player player, boolean isUsing) {
		if (isUsing) {
			playersUsing.add(player.getUUID());
		} else {
			playersUsing.remove(player.getUUID());
		}
		setChanged();
		if (level != null && !level.isClientSide()) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
	}

	public boolean isBeingUsed() {
		if (level != null && level.isClientSide()) {
			return clientUsersCount > 0;
		}
		if (level != null) {
			for (UUID uuid : playersUsing) {
				Player p = level.getPlayerByUUID(uuid);
				if (p != null) {
					PlayerForceData data = PlayerForceData.get(p);
					if (data != null && data.isForceSensitive()) {
						return true;
					}
				}
			}
			return false;
		}
		return !playersUsing.isEmpty();
	}

	public static void tick(Level level, BlockPos pos, BlockState state, HolocronBlockEntity tile) {
		tile.prevOpenTimer = tile.openTimer;
		tile.prevOpenTicks = tile.openTicks;

		boolean isBeingUsed = tile.isBeingUsed();

		if (level.isClientSide() && !isBeingUsed) {
			if (MeditationStateManager.isMeditating() && MeditationStateManager.isPlayerFloating()) {
				Vec3 center = MeditationStateManager.getMeditationCenterPos();
				if (center != null && pos.closerToCenterThan(center, 9.5D)) {
					isBeingUsed = true;
				}
			}
		}

		if (!level.isClientSide() && !tile.playersUsing.isEmpty()) {
			Set<UUID> disconnected = new HashSet<>();

			for (UUID uuid : tile.playersUsing) {
				Player player = level.getPlayerByUUID(uuid);

				if (player == null || !player.isAlive()
						|| player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 100.0) {
					disconnected.add(uuid);
					continue;
				}

				PlayerForceData data = PlayerForceData.get(player);
				if (data != null) {
					if (player instanceof ServerPlayer serverPlayer) {
						data.addMeditationTicks(1, serverPlayer);
						boolean wasSensitive = data.isForceSensitive();
						data.tickMeditationFpProgress(1, serverPlayer);
						if (!wasSensitive && data.isForceSensitive()) {
							tile.setChanged();
							level.sendBlockUpdated(pos, state, state, 3);
						}
					}

					if (player.tickCount % 20 == 0) {
						float xpGain = 2.0F * data.getMeditationXpMultiplier();
						data.addForceXp(xpGain);
					}

					if (player.tickCount % 100 == 0 && player instanceof ServerPlayer serverPlayer) {
						data.sync(serverPlayer);
					}
				}
			}

			if (!disconnected.isEmpty()) {
				tile.playersUsing.removeAll(disconnected);
				tile.setChanged();
				level.sendBlockUpdated(pos, state, state, 3);
			}

			if (!tile.playersUsing.isEmpty()) {
				double radius = 5.2D;
				net.minecraft.world.phys.AABB sanctuaryBox = new net.minecraft.world.phys.AABB(pos).inflate(radius);
				Vec3 center = Vec3.atCenterOf(pos);

				for (net.minecraft.world.entity.Entity entity : level.getEntities(null, sanctuaryBox)) {
					if (entity.distanceToSqr(center) > radius * radius) {
						continue;
					}

					if (entity instanceof net.minecraft.world.entity.LivingEntity living) {
						for (net.minecraft.world.InteractionHand hand : net.minecraft.world.InteractionHand.values()) {
							net.minecraft.world.item.ItemStack stack = living.getItemInHand(hand);
							if (stack.getItem() instanceof com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber
									&& com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber.isActive(stack)) {
								com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber.setBladeActive(stack, false);
								level.playSound(null, living.blockPosition(),
										com.fiskmods.pika.lightsabers.ALSounds.LIGHTSABER_OFF.get(),
										net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 1.0F);
							}
						}
					}

					if (entity instanceof Player p && tile.playersUsing.contains(p.getUUID())) {
						continue;
					}

					boolean isHostileMob = entity instanceof net.minecraft.world.entity.monster.Enemy
							|| (entity instanceof net.minecraft.world.entity.Mob mob && mob.getTarget() != null);

					boolean isAggressivePlayer = false;
					if (entity instanceof Player otherPlayer) {
						net.minecraft.world.item.ItemStack main = otherPlayer.getMainHandItem();
						if (main.getItem() instanceof net.minecraft.world.item.SwordItem
								|| main.getItem() instanceof net.minecraft.world.item.AxeItem
								|| main.getItem() instanceof com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber
								|| otherPlayer.isUsingItem()) {
							isAggressivePlayer = true;
						}
					}

					boolean isHostileProjectile = entity instanceof net.minecraft.world.entity.projectile.Projectile
							|| entity instanceof com.fiskmods.pika.lightsabers.entity.EntityLightsaber;

					if (isHostileMob || isAggressivePlayer || isHostileProjectile) {
						if (entity instanceof net.minecraft.world.entity.Mob mob) {
							mob.setTarget(null);
						}

						Vec3 pushDir = entity.position().subtract(center);
						if (pushDir.lengthSqr() < 0.001D) {
							pushDir = new Vec3(1, 0, 0);
						} else {
							pushDir = pushDir.normalize();
						}

						entity.setDeltaMovement(pushDir.x * 0.45D, 0.15D, pushDir.z * 0.45D);
						entity.hurtMarked = true;
						if (entity instanceof ServerPlayer sp) {
							sp.connection
									.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(sp));
						}
					}
				}
			}
		}

		if (!isBeingUsed) {
			tile.openTimer *= 0.85F;
		} else if (tile.openTimer < 1.0F) {
			tile.openTimer += 0.05F;
			tile.openTimer *= 1.05F;
		}

		if (tile.openTimer < 1.0E-6F) {
			tile.openTimer = 0.0F;
		}

		if (tile.openTimer == 0.0F) {
			tile.openTicks = 0;
		} else if (tile.openTimer >= 1.0F) {
			tile.openTicks++;
		}

		tile.openTimer = Mth.clamp(tile.openTimer, 0.0F, 1.0F);
	}

	private int getForceSensitiveUsersCount() {
		if (level == null)
			return 0;
		int count = 0;
		for (UUID uuid : playersUsing) {
			Player p = level.getPlayerByUUID(uuid);
			if (p != null) {
				PlayerForceData data = PlayerForceData.get(p);
				if (data != null && data.isForceSensitive()) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt("UsersCount", getForceSensitiveUsersCount());
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("UsersCount")) {
			this.clientUsersCount = tag.getInt("UsersCount");
		}
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = super.getUpdateTag(registries);
		tag.putInt("UsersCount", getForceSensitiveUsersCount());
		return tag;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}