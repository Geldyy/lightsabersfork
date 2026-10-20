package com.fiskmods.pika.lightsabers.entity;

import java.util.UUID;

import com.fiskmods.pika.lightsabers.block.entity.KyberPedestalBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ForcePhantomEntity extends Monster {

	private UUID trialPlayerUUID = null;
	private BlockPos pedestalPos = null;
	private int parriedHits = 0;
	private int meditationTicks = 0;
	private boolean resolved = false;

	public ForcePhantomEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
		this.setCustomName(Component.translatable("entity.lightsabersfork.force_phantom"));
		this.setCustomNameVisible(true);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.32D)
				.add(Attributes.ATTACK_DAMAGE, 6.0D)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.25D, false));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public void setupTrial(UUID playerUUID, BlockPos pedestal) {
		this.trialPlayerUUID = playerUUID;
		this.pedestalPos = pedestal;
	}

	public void recordParry(ServerPlayer player) {
		if (resolved)
			return;

		this.parriedHits++;
		if (this.level() instanceof ServerLevel sl) {
			sl.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY() + 1.2D, this.getZ(), 10, 0.3D, 0.3D, 0.3D, 0.1D);
		}
		this.level().playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.2F, 1.2F);

		if (this.parriedHits >= 3) {
			resolveJedi(player);
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.level().isClientSide || resolved)
			return;

		ServerLevel sl = (ServerLevel) this.level();

		// Particle ambiance
		if (this.tickCount % 4 == 0) {
			sl.sendParticles(ParticleTypes.SOUL, this.getX() + (sl.random.nextDouble() - 0.5D) * 0.6D,
					this.getY() + sl.random.nextDouble() * 1.8D,
					this.getZ() + (sl.random.nextDouble() - 0.5D) * 0.6D, 1, 0, 0.02, 0, 0.02);
		}

		if (trialPlayerUUID != null) {
			ServerPlayer player = sl.getServer().getPlayerList().getPlayer(trialPlayerUUID);
			if (player != null && player.isAlive() && player.distanceToSqr(this) <= 36.0D) {
				// Peaceful Meditation detection
				boolean isMeditating = player.getPersistentData().getBoolean("IsMeditating");
				if (isMeditating) {
					meditationTicks++;
					if (meditationTicks % 10 == 0) {
						sl.sendParticles(ParticleTypes.ENCHANT, player.getX(), player.getY() + 1.0D, player.getZ(), 8,
								0.3D, 0.3D, 0.3D, 0.05D);
					}
					if (meditationTicks >= 60) {
						resolveJedi(player);
					}
				} else {
					meditationTicks = Math.max(0, meditationTicks - 1);
				}
			}
		}
	}

	public void resolveJedi(ServerPlayer player) {
		if (resolved)
			return;
		resolved = true;

		if (this.level() instanceof ServerLevel sl) {
			sl.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, this.getX(), this.getY() + 1.0D, this.getZ(), 40, 0.4D,
					0.6D, 0.4D, 0.1D);
			sl.playSound(null, this.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.4F);
		}

		if (pedestalPos != null) {
			BlockEntity be = this.level().getBlockEntity(pedestalPos);
			if (be instanceof KyberPedestalBlockEntity pBe) {
				pBe.resolveJedi(player);
			}
		}

		this.discard();
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);

		if (resolved)
			return;
		resolved = true;

		if (source.getEntity() instanceof ServerPlayer sp) {
			if (pedestalPos != null) {
				BlockEntity be = this.level().getBlockEntity(pedestalPos);
				if (be instanceof KyberPedestalBlockEntity pBe) {
					pBe.resolveSith(sp);
				}
			}
		}
	}
}
