package com.fiskmods.pika.lightsabers.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fiskmods.pika.lightsabers.ALAttributes;
import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALEntities;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.ALSounds;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.events.LightsaberCombatEvents;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityLightsaber extends ThrowableItemProjectile {
	private boolean isReturning = false;
	private int returnSlot = -1;

	private Vec3 startPos;
	private double maxDistance = 12.0;
	private double sweepSize = 2.0;
	private float weaponDamage = 5.0F;
	private boolean isEffectivelyDouble = false;
	private final Set<UUID> hitEntities = new HashSet<>();

	public EntityLightsaber(EntityType<? extends ThrowableItemProjectile> type, Level level) {
		super(type, level);
	}

	public EntityLightsaber(Level level, LivingEntity thrower, ItemStack lightsaberItem, int returnSlot) {
		super(ALEntities.LIGHTSABER.get(), thrower, level);
		this.setItem(lightsaberItem.copy());
		this.returnSlot = returnSlot;
		this.startPos = thrower.position();

		if (thrower instanceof Player player) {
			PlayerForceData data = PlayerForceData.get(player);
			LightsaberData lsData = lightsaberItem.get(ALDataComponents.LIGHTSABER_DATA.get());

			if (lsData != null) {
				this.isEffectivelyDouble = lsData.isDoubleBladed() && !ItemLightsaber.isSingleBladeMode(lightsaberItem);

				if (data != null) {
					float sbMult = LightsaberCombatEvents.getSbMult(lightsaberItem);

					double bonusDist = 0.0;
					double bonusSweep = 0.0;

					if (data.hasSkill(
							ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "sb_adv_throw"))) {
						bonusDist = 20.0;
						bonusSweep = 3.0;
					} else if (data.hasSkill(
							ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "sb_better_throw"))) {
						bonusDist = 12.0;
						bonusSweep = 1.0;
					}

					this.maxDistance = 12.0 + (bonusDist * sbMult);
					this.sweepSize = 2.0 + (bonusSweep * sbMult);
				}

				float baseDmg = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
				this.weaponDamage = LightsaberCombatEvents.getSweepDamage(data, lightsaberItem, baseDmg,
						this.isEffectivelyDouble);
			}
		}
	}

	@Override
	protected Item getDefaultItem() {
		return ALItems.LIGHTSABER.get();
	}

	@Override
	protected double getDefaultGravity() {
		return 0.01D;
	}

	private List<Item> getFlyingFocusCrystals() {
		List<Item> all = new ArrayList<>();
		LightsaberData data = this.getItem().get(ALDataComponents.LIGHTSABER_DATA.get());
		if (data != null) {
			if (data.focusCrystals() != null)
				all.addAll(data.focusCrystals());
			if (data.isDoubleBladed() && data.secFocusCrystals() != null)
				all.addAll(data.secFocusCrystals());
		}
		return all;
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide) {
			Entity thrower = this.getOwner();

			if (this.tickCount % 3 == 0) {
				this.level().playSound(null, this.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS,
						1.0F, 1.0F);
			}

			if (!this.isReturning && this.startPos != null) {
				if (this.distanceToSqr(this.startPos) > this.maxDistance * this.maxDistance) {
					this.isReturning = true;
				}
			}

			if (thrower == null || !thrower.isAlive()) {
				this.spawnAtLocation(this.getItem());
				this.discard();
				return;
			}

			if (this.isReturning) {
				if (this.distanceTo(thrower) <= 2.0F) {
					if (thrower instanceof Player player) {
						boolean added = false;
						ItemStack stackToReturn = this.getItem();

						if (this.returnSlot == -2 && player.getOffhandItem().isEmpty()) {
							player.setItemInHand(InteractionHand.OFF_HAND, stackToReturn);
							added = true;
						} else if (this.returnSlot >= 0 && this.returnSlot < 9
								&& player.getInventory().getItem(this.returnSlot).isEmpty()) {
							player.getInventory().setItem(this.returnSlot, stackToReturn);
							added = true;
						} else if (player.getOffhandItem().isEmpty()) {
							player.setItemInHand(InteractionHand.OFF_HAND, stackToReturn);
							added = true;
						} else if (player.getInventory().add(stackToReturn)) {
							added = true;
						}

						if (!added) {
							this.spawnAtLocation(stackToReturn);
						}
					} else {
						this.spawnAtLocation(this.getItem());
					}
					this.discard();
					return;
				} else {
					Vec3 target = thrower.position().add(0, thrower.getBbHeight() * 0.6, 0);
					Vec3 dir = target.subtract(this.position()).normalize().scale(1.5D);
					this.setDeltaMovement(dir);
				}
			}

			AABB sweepBox = this.getBoundingBox().inflate(this.sweepSize / 2.0D);
			List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, sweepBox);

			for (LivingEntity target : targets) {
				if (target == thrower || this.hitEntities.contains(target.getUUID()))
					continue;

				if (target instanceof net.minecraft.world.entity.decoration.ArmorStand) {
					this.hitEntities.add(target.getUUID());
					if (!this.level().isClientSide && thrower instanceof ServerPlayer sp) {
						PlayerForceData data = PlayerForceData.get(sp);
						if (data != null) {
							data.recordSaberTraining(sp, 2);
						}
					}
					this.playSound(ALSounds.LIGHTSABER_HIT.get(), 1.0F, 1.2F);
					continue;
				}

				boolean isImmune = target.getAttribute(ALAttributes.LIGHTSABER_IMMUNITY) != null
						&& target.getAttributeValue(ALAttributes.LIGHTSABER_IMMUNITY) > 0.0D;

				if (!isImmune) {
					for (ItemStack armor : target.getArmorSlots()) {
						if (!armor.isEmpty()) {
							net.minecraft.world.item.component.ItemAttributeModifiers modifiers = armor.getOrDefault(
									net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS,
									net.minecraft.world.item.component.ItemAttributeModifiers.EMPTY);
							for (net.minecraft.world.item.component.ItemAttributeModifiers.Entry entry : modifiers
									.modifiers()) {
								if (entry.attribute().is(ALAttributes.LIGHTSABER_IMMUNITY.getKey())) {
									isImmune = true;
									break;
								}
							}
						}
						if (isImmune)
							break;
					}
				}

				if (isImmune) {
					this.isReturning = true;
					this.level().playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS,
							1.0F, 1.2F);
					if (this.level() instanceof ServerLevel sl) {
						sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY() + 0.5, this.getZ(), 15,
								0.2, 0.2, 0.2, 0.1);
					}
					continue;
				}

				target.invulnerableTime = 0;
				target.hurt(this.damageSources().thrown(this, thrower), this.weaponDamage);
				this.hitEntities.add(target.getUUID());

				if (this.level() instanceof ServerLevel sl) {
					sl.sendParticles(ParticleTypes.SWEEP_ATTACK, target.getX(), target.getY(0.5D), target.getZ(),
							this.isEffectivelyDouble ? 3 : 1, 0.3, 0.1, 0.3, 0.0);
				}

				if (thrower instanceof Player player) {
					PlayerForceData data = PlayerForceData.get(player);
					if (data != null && data.hasSkill(
							ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "ls_focusing"))) {
						for (Item crystalItem : getFlyingFocusCrystals()) {
							String crystalStr = BuiltInRegistries.ITEM.getKey(crystalItem).getPath().toLowerCase();

							if (crystalStr.contains("fine_cut")) {
								target.invulnerableTime = 0;
								target.hurt(this.damageSources().magic(), 3.0f);
							} else if (crystalStr.contains("pulse")) {
								List<LivingEntity> chainTargets = new ArrayList<>();
								LivingEntity currentChainSource = target;

								for (int bounce = 0; bounce < 5; bounce++) {
									List<LivingEntity> possibleTargets = this.level().getEntitiesOfClass(
											LivingEntity.class, currentChainSource.getBoundingBox().inflate(3.0D));
									final LivingEntity finalSource = currentChainSource;
									possibleTargets.sort(Comparator.comparingDouble(e -> e.distanceToSqr(finalSource)));

									boolean foundNext = false;
									for (LivingEntity possible : possibleTargets) {
										if (possible != player && possible != target
												&& !chainTargets.contains(possible)) {
											chainTargets.add(possible);
											currentChainSource = possible;
											foundNext = true;

											possible.invulnerableTime = 0;
											possible.hurt(this.damageSources().lightningBolt(),
													this.weaponDamage * 0.35f);
											if (this.level() instanceof ServerLevel sl) {
												sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, possible.getX(),
														possible.getY() + 1, possible.getZ(), 5, 0.2, 0.2, 0.2, 0.1);
											}
											break;
										}
									}
									if (!foundNext)
										break;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void onHit(net.minecraft.world.phys.HitResult result) {
		if (result.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
			net.minecraft.world.phys.BlockHitResult blockResult = (net.minecraft.world.phys.BlockHitResult) result;
			net.minecraft.world.level.block.state.BlockState state = this.level()
					.getBlockState(blockResult.getBlockPos());
			if (!this.level().isClientSide && this.getOwner() instanceof ServerPlayer sp) {
				if (state.is(net.minecraft.world.level.block.Blocks.TARGET)) {
					if (this.distanceToSqr(sp) >= 36.0D) {
						PlayerForceData data = PlayerForceData.get(sp);
						if (data != null) {
							data.recordSaberTraining(sp, 3);
						}
					}
				}
			}

			if (state.is(net.minecraft.tags.BlockTags.LEAVES)
					|| state.getCollisionShape(this.level(), blockResult.getBlockPos()).isEmpty()) {
				return;
			}

			if (!this.level().isClientSide) {
				this.isReturning = true;
				this.level().playSound(null, this.blockPosition(), ALSounds.LIGHTSABER_HIT.get(), SoundSource.PLAYERS,
						1.0F, 1.2F);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (!this.level().isClientSide
				&& result.getEntity() instanceof net.minecraft.world.entity.decoration.ArmorStand) {
			if (this.getOwner() instanceof ServerPlayer sp) {
				long now = this.level().getGameTime();
				if (now - sp.getPersistentData().getLong("LastThrowTrainTick") >= 40) {
					sp.getPersistentData().putLong("LastThrowTrainTick", now);
					PlayerForceData data = PlayerForceData.get(sp);
					if (data != null) {
						data.recordSaberTraining(sp, 2);
					}
				}
			}
		}
	}
}