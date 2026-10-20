package com.fiskmods.pika.lightsabers.network.payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.fiskmods.pika.lightsabers.ALForceAbilitiesConfig;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.entity.TelekineticBlockEntity;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UseForcePayload(boolean isKeyDown) implements CustomPacketPayload {
	public static final Type<UseForcePayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "use_force"));

	public static final StreamCodec<ByteBuf, UseForcePayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL,
			UseForcePayload::isKeyDown, UseForcePayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(UseForcePayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				PlayerForceData data = PlayerForceData.get(player);
				if (data != null && data.isForceSensitive() && data.getSelectedPower() != null) {

					ResourceLocation selectedPowerId = data.getSelectedPower();
					String power = selectedPowerId.getPath();
					int currentLevel = data.getCurrentSelectedPowerLevel();
					ServerLevel world = player.serverLevel();

					float costMult = data.getPowerCostMultiplier(selectedPowerId);
					float rangeMult = data.getRangeMultiplier(selectedPowerId);

					if (payload.isKeyDown()) {

						if (data.isForceSilenced()) {
							player.displayClientMessage(
									Component.translatable("message.lightsabersfork.force_silenced")
											.withStyle(ChatFormatting.DARK_PURPLE),
									true);
							player.playNotifySound(SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS, 1.0F,
									0.5F);
							return;
						}

						String currentActiveChannel = player.getPersistentData().getString("ForceChanneling");
						if ("force_choke".equals(currentActiveChannel) || "force_drain".equals(currentActiveChannel)) {
							if (!power.equals(currentActiveChannel)) {
								player.displayClientMessage(
										Component.translatable("message.lightsabersfork.channeling_locked")
												.withStyle(ChatFormatting.RED),
										true);
								player.playNotifySound(SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS, 0.7F,
										0.6F);
								return;
							}
						}

						if (!power.equals("force_telekinesis") && data.isPowerOnCooldown(selectedPowerId)) {
							float remainingSec = data.getPowerCooldown(selectedPowerId) / 20.0F;
							player.displayClientMessage(
									Component.translatable("message.lightsabersfork.power_on_cooldown",
											String.format(Locale.ROOT, "%.1f", remainingSec))
											.withStyle(ChatFormatting.RED),
									true);
							player.playNotifySound(SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS, 0.7F,
									0.6F);
							return;
						}

						if (power.equals("force_telekinesis")) {

							if (data.getTelekineticEntityId() != -1) {
								Entity grabbed = world.getEntity(data.getTelekineticEntityId());
								if (grabbed != null && grabbed.isAlive()) {
									Vec3 look = player.getLookAngle();
									double throwForce = (currentLevel >= 3)
											? ALForceAbilitiesConfig.TELEKINESIS_THROW_FORCE_T3.get()
											: ((currentLevel == 2)
													? ALForceAbilitiesConfig.TELEKINESIS_THROW_FORCE_T2.get()
													: ALForceAbilitiesConfig.TELEKINESIS_THROW_FORCE_T1.get());
									grabbed.setDeltaMovement(look.scale(throwForce).add(0, 0.35D, 0));
									grabbed.hurtMarked = true;
									grabbed.getPersistentData().putUUID("TK_ThrownBy", player.getUUID());

									world.playSound(null, player.getX(), player.getY(), player.getZ(),
											SoundEvents.WIND_CHARGE_THROW, SoundSource.PLAYERS, 1.2F, 0.8F);
								}
								data.clearTelekineticEntity();
								data.triggerPowerCooldown(selectedPowerId);
								data.recordPowerUse(selectedPowerId, player);
								data.sync(player);
								return;
							}

							List<TelekineticBlockEntity> heldBlocks = world.getEntitiesOfClass(
									TelekineticBlockEntity.class, player.getBoundingBox().inflate(24.0D),
									e -> e.getOwnerId() == player.getId() && !e.isThrown());

							if (!heldBlocks.isEmpty()) {
								TelekineticBlockEntity held = heldBlocks.get(0);
								double force = (currentLevel >= 3)
										? ALForceAbilitiesConfig.TELEKINESIS_THROW_FORCE_T3.get()
										: ((currentLevel == 2)
												? ALForceAbilitiesConfig.TELEKINESIS_THROW_FORCE_T2.get()
												: ALForceAbilitiesConfig.TELEKINESIS_THROW_FORCE_T1.get());
								held.throwBlock(player.getLookAngle(), force);

								world.playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.WIND_CHARGE_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
								data.recordPowerUse(selectedPowerId, player);
								data.sync(player);
								return;
							}

							double reach = ((currentLevel >= 3)
									? ALForceAbilitiesConfig.TELEKINESIS_REACH_T3.get()
									: ((currentLevel == 2)
											? ALForceAbilitiesConfig.TELEKINESIS_REACH_T2.get()
											: ALForceAbilitiesConfig.TELEKINESIS_REACH_T1.get()))
									* rangeMult;
							Vec3 eye = player.getEyePosition();
							Vec3 look = player.getLookAngle();
							Vec3 end = eye.add(look.scale(reach));

							BlockHitResult bHit = world.clip(new ClipContext(eye, end, ClipContext.Block.OUTLINE,
									ClipContext.Fluid.NONE, player));
							double blockDistSqr = (bHit.getType() == HitResult.Type.BLOCK)
									? eye.distanceToSqr(bHit.getLocation())
									: Double.MAX_VALUE;

							AABB searchBox = player.getBoundingBox().expandTowards(look.scale(reach)).inflate(2.0D);
							List<Entity> candidateEntities = world.getEntities(player, searchBox,
									e -> e.isAlive() && !(e instanceof TelekineticBlockEntity));

							Entity bestTarget = null;
							double closestDist = Double.MAX_VALUE;

							for (Entity candidate : candidateEntities) {
								AABB cBox = candidate.getBoundingBox().inflate(0.4D);
								Optional<Vec3> hit = cBox.clip(eye, end);
								if (hit.isPresent()) {
									double d = eye.distanceToSqr(hit.get());
									if (d < closestDist) {
										closestDist = d;
										bestTarget = candidate;
									}
								}
							}

							boolean hitEntityCloser = bestTarget != null && closestDist < blockDistSqr;

							if (hitEntityCloser) {
								if (data.isPowerOnCooldown(selectedPowerId)) {
									float remainingSec = data.getPowerCooldown(selectedPowerId) / 20.0F;
									player.displayClientMessage(
											Component.translatable("message.lightsabersfork.power_on_cooldown",
													String.format(Locale.ROOT, "%.1f", remainingSec))
													.withStyle(ChatFormatting.RED),
											true);
									player.playNotifySound(SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS,
											0.7F, 0.6F);
									return;
								}

								boolean isBoss = isTitanOrBoss(bestTarget);
								boolean isGrandmasterOrEmperor = data.getJediRank() >= 6 || data.getSithRank() >= 6;

								if (isBoss && !isGrandmasterOrEmperor) {
									player.displayClientMessage(
											Component.translatable("message.lightsabersfork.tk_boss_too_strong")
													.withStyle(ChatFormatting.RED),
											true);
									world.playSound(null, player.blockPosition(), SoundEvents.WARDEN_ROAR,
											SoundSource.HOSTILE, 1.0F, 0.8F);
									return;
								}

								if (currentLevel < 2) {
									player.displayClientMessage(
											Component.translatable("message.lightsabersfork.tk_req_level_2")
													.withStyle(ChatFormatting.RED),
											true);
									player.playNotifySound(SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS,
											1.0F, 0.5F);
									return;
								}

								if (currentLevel == 2 && bestTarget instanceof Monster && !isBoss) {
									player.displayClientMessage(Component
											.translatable("message.lightsabersfork.tk_req_level_3")
											.withStyle(ChatFormatting.RED), true);
									player.playNotifySound(SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS,
											1.0F, 0.5F);
									return;
								}

								float grabCost = ((currentLevel >= 3) ? 20.0F : ((currentLevel >= 2) ? 5.0F : 3.0F)) * costMult;
								if (data.consumeForceEnergy(grabCost)) {
									data.setTelekineticEntityId(bestTarget.getId());
									data.setTelekineticDistance(
											(float) Math.max(3.0D, eye.distanceTo(bestTarget.position())));
									data.setTelekineticHoldTicks(0);

									if (bestTarget instanceof Warden) {
										data.setTelekineticMaxTicks(200);
									} else if (bestTarget instanceof EnderDragon) {
										data.setTelekineticMaxTicks(100);
									} else {
										data.setTelekineticMaxTicks(-1);
									}

									world.playSound(null, bestTarget.getX(), bestTarget.getY(), bestTarget.getZ(),
											SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.2F, 0.5F);
									data.recordPowerUse(selectedPowerId, player);
									data.sync(player);
								}
								return;
							}

							float blockGrabCost = ((currentLevel >= 3) ? 20.0F : ((currentLevel >= 2) ? 5.0F : 3.0F)) * costMult;
							if (data.consumeForceEnergy(blockGrabCost)) {

								if (bHit.getType() == HitResult.Type.BLOCK) {
									BlockPos pos = bHit.getBlockPos();
									BlockState state = world.getBlockState(pos);

									if (canPickUpBlock(state, world, pos, currentLevel)) {
										world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

										double initDist = Math.max(2.5D,
												player.getEyePosition().distanceTo(Vec3.atCenterOf(pos)));
										boolean isSith = (data.getSithRank() > data.getJediRank())
												|| "ASSASSIN".equals(data.getSithSpecialization())
												|| "INQUISITOR".equals(data.getSithSpecialization());

										TelekineticBlockEntity tkEntity = new TelekineticBlockEntity(world,
												pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state, player,
												currentLevel, (float) initDist, isSith);
										world.addFreshEntity(tkEntity);

										world.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS,
												1.0F, 0.6F);
										data.recordPowerUse(selectedPowerId, player);
									} else {
										data.addForceEnergy(blockGrabCost);
										player.displayClientMessage(
												Component.translatable("message.lightsabersfork.tk_block_too_heavy")
														.withStyle(ChatFormatting.RED),
												true);
										player.playNotifySound(SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS,
												1.0F, 0.5F);
									}
								} else {
									data.addForceEnergy(blockGrabCost);
								}
								data.sync(player);
							}
						}

						else if (power.equals("force_dash")) {
							if (currentLevel == 1 && !player.onGround()) {
								player.displayClientMessage(
										Component.translatable("message.lightsabersfork.dash_ground_required")
												.withStyle(ChatFormatting.RED),
										true);
								return;
							}
							float dashCost = ((currentLevel >= 3)
									? ALForceAbilitiesConfig.DASH_COST_T3.get().floatValue()
									: ((currentLevel >= 2)
											? ALForceAbilitiesConfig.DASH_COST_T2.get().floatValue()
											: ALForceAbilitiesConfig.DASH_COST_T1.get().floatValue()))
									* data.getMobilityCostMultiplier() * costMult;
							if (data.consumeForceEnergy(dashCost)) {
								Vec3 look = player.getLookAngle();
								double dashSpeed = (currentLevel >= 3)
										? ALForceAbilitiesConfig.DASH_SPEED_T3.get()
										: ((currentLevel >= 2)
												? ALForceAbilitiesConfig.DASH_SPEED_T2.get()
												: ALForceAbilitiesConfig.DASH_SPEED_T1.get());
								Vec3 dashVec = new Vec3(look.x * dashSpeed, look.y * 0.75D, look.z * dashSpeed);
								player.setDeltaMovement(dashVec);
								player.hurtMarked = true;
								player.connection
										.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(
												player));
								player.fallDistance = 0.0f;
								player.getPersistentData().putBoolean("ForceJumpNoFall", true);

								if (currentLevel >= 3) {
									AABB allyBox = player.getBoundingBox().inflate(6.0D);
									List<LivingEntity> allies = world.getEntitiesOfClass(LivingEntity.class, allyBox,
											e -> e != player && (e.isAlliedTo(player)
													|| (e instanceof net.minecraft.world.entity.TamableAnimal tame
															&& tame.isOwnedBy(player))));
									for (LivingEntity ally : allies) {
										ally.setDeltaMovement(dashVec);
										ally.hurtMarked = true;
										ally.fallDistance = 0.0f;
									}
								}

								world.playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.8F);
								world.sendParticles(ParticleTypes.POOF, player.getX(), player.getY() + 1.0,
										player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
								data.triggerPowerCooldown(selectedPowerId);
								data.recordPowerUse(selectedPowerId, player);
								data.sync(player);
							}
						}

						else if (power.equals("force_push")) {
							float pushCost = ((currentLevel >= 4)
									? ALForceAbilitiesConfig.PUSH_COST_T4.get().floatValue()
									: ((currentLevel >= 3)
											? ALForceAbilitiesConfig.PUSH_COST_T3.get().floatValue()
											: ((currentLevel >= 2)
													? ALForceAbilitiesConfig.PUSH_COST_T2.get().floatValue()
													: ALForceAbilitiesConfig.PUSH_COST_T1.get().floatValue()))) * costMult;
							if (data.consumeForceEnergy(pushCost)) {
								Vec3 look = player.getLookAngle();

								if (currentLevel >= 4) {
									double maxReach = ALForceAbilitiesConfig.PUSH_REACH_T4.get() * rangeMult;
									Vec3 implodeCenter = player.getEyePosition().add(look.scale(10.0D));
									AABB box = player.getBoundingBox().inflate(maxReach);
									List<Entity> candidates = world.getEntities(player, box,
											e -> e.isAlive() && !(e instanceof TelekineticBlockEntity));

									for (Entity e : candidates) {
										Vec3 toE = e.position().subtract(player.position()).normalize();
										if (look.dot(toE) > 0.25D) {
											Vec3 suction = implodeCenter.subtract(e.position()).normalize().scale(3.5D);
											e.setDeltaMovement(suction.add(0, 0.4D, 0));
											e.hurtMarked = true;
											e.hurt(world.damageSources().magic(), 18.0f);
										}
									}

									world.playSound(null, implodeCenter.x, implodeCenter.y, implodeCenter.z,
											SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 2.0F, 0.5F);
									world.playSound(null, implodeCenter.x, implodeCenter.y, implodeCenter.z,
											SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.5F, 1.0F);
									world.sendParticles(ParticleTypes.GUST_EMITTER_LARGE, implodeCenter.x,
											implodeCenter.y, implodeCenter.z, 1, 0, 0, 0, 0);
									world.sendParticles(ParticleTypes.SONIC_BOOM, implodeCenter.x, implodeCenter.y,
											implodeCenter.z, 2, 0.5, 0.5, 0.5, 0);
								} else {
									double reach = ((currentLevel >= 3)
											? ALForceAbilitiesConfig.PUSH_REACH_T3.get()
											: ((currentLevel >= 2)
													? ALForceAbilitiesConfig.PUSH_REACH_T2.get()
													: ALForceAbilitiesConfig.PUSH_REACH_T1.get())) * rangeMult;
									double dotReq = (currentLevel >= 3) ? 0.35D : ((currentLevel >= 2) ? 0.4D : 0.6D);
									double strength = (currentLevel >= 3) ? 4.0D : ((currentLevel >= 2) ? 3.0D : 2.2D);

									AABB box = player.getBoundingBox().inflate(reach);
									List<Entity> entities = world.getEntities(player, box);

									for (Entity e : entities) {
										Vec3 dir = e.position().subtract(player.position()).normalize();
										if (look.dot(dir) > dotReq) {
											if (e instanceof Projectile proj && currentLevel >= 2) {
												proj.setDeltaMovement(proj.getDeltaMovement().scale(-2.0));
												proj.hurtMarked = true;
											} else if (e instanceof LivingEntity living) {
												int armor = living.getArmorValue();
												boolean isRavager = living instanceof net.minecraft.world.entity.monster.Ravager;
												boolean isBoss = isTitanOrBoss(living);

												if (currentLevel == 1 && (armor > 15 || isRavager || isBoss))
													continue;
												if (currentLevel == 2 && (armor > 20 || isBoss))
													continue;

												living.setDeltaMovement(dir.x * strength, 0.6D, dir.z * strength);
												living.hurtMarked = true;
												if (currentLevel >= 3) {
													living.hurt(world.damageSources().magic(), 4.0f);
												}
											}
										}
									}

									if (currentLevel >= 3) {
										destroyFragileBlocksInCone(world, player, (int) Math.round(10 * rangeMult));
									}

									world.playSound(null, player.getX(), player.getY(), player.getZ(),
											SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 1.0F, 0.8F);
									world.sendParticles(ParticleTypes.CLOUD, player.getX() + look.x, player.getEyeY(),
											player.getZ() + look.z, 25, 0.8, 0.8, 0.8, 0.2);
								}

								data.triggerPowerCooldown(selectedPowerId);
								data.recordPowerUse(selectedPowerId, player);
								data.sync(player);
							}
						}

						else if (power.equals("force_pull")) {
							float pullCost = ((currentLevel >= 3)
									? ALForceAbilitiesConfig.PULL_COST_T3.get().floatValue()
									: ((currentLevel >= 2)
											? ALForceAbilitiesConfig.PULL_COST_T2.get().floatValue()
											: ALForceAbilitiesConfig.PULL_COST_T1.get().floatValue())) * costMult;
							if (data.consumeForceEnergy(pullCost)) {
								Vec3 look = player.getLookAngle();
								double radius = ((currentLevel >= 3)
										? ALForceAbilitiesConfig.PULL_RADIUS_T3.get()
										: ((currentLevel >= 2)
												? ALForceAbilitiesConfig.PULL_RADIUS_T2.get()
												: ALForceAbilitiesConfig.PULL_RADIUS_T1.get())) * rangeMult;
								int maxTargets = (currentLevel >= 3)
										? ALForceAbilitiesConfig.PULL_MAX_TARGETS_T3.get()
										: ((currentLevel >= 2)
												? ALForceAbilitiesConfig.PULL_MAX_TARGETS_T2.get()
												: ALForceAbilitiesConfig.PULL_MAX_TARGETS_T1.get());

								AABB box = player.getBoundingBox().inflate(radius);
								List<Entity> entities = world.getEntities(player, box);
								int pulledCount = 0;

								for (Entity e : entities) {
									if (pulledCount >= maxTargets)
										break;
									Vec3 toEntity = e.position().subtract(player.position()).normalize();
									if (look.dot(toEntity) > 0.35D) {
										Vec3 pullDir = player.position().subtract(e.position()).normalize();

										if (e instanceof ItemEntity item) {
											item.setPos(player.getX(), player.getY(), player.getZ());
											pulledCount++;
										} else if (e instanceof LivingEntity living) {
											int armor = living.getArmorValue();
											boolean isRavager = living instanceof net.minecraft.world.entity.monster.Ravager;
											boolean isBoss = isTitanOrBoss(living);

											if (currentLevel == 1 && (armor > 15 || isRavager || isBoss))
												continue;
											if (currentLevel == 2 && (armor > 20 || isBoss))
												continue;

											pulledCount++;
											if (currentLevel >= 3) {
												ItemStack weapon = living.getMainHandItem();
												if (!weapon.isEmpty()) {
													ItemStack droppedWeapon = weapon.copy();
													living.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);

													ItemEntity weaponEntity = new ItemEntity(world, living.getX(),
															living.getY() + 0.5D, living.getZ(), droppedWeapon);
													weaponEntity.setDeltaMovement(pullDir.x * 1.5D, 0.4D,
															pullDir.z * 1.5D);
													world.addFreshEntity(weaponEntity);

													world.playSound(null, living.blockPosition(),
															SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.5F);
												}
											}

											double pullStrength = ((currentLevel >= 3)
													? ALForceAbilitiesConfig.PULL_STRENGTH_T3.get()
													: ((currentLevel >= 2)
															? ALForceAbilitiesConfig.PULL_STRENGTH_T2.get()
															: ALForceAbilitiesConfig.PULL_STRENGTH_T1.get()));
											living.setDeltaMovement(pullDir.x * pullStrength, 0.3D, pullDir.z * pullStrength);
											living.hurtMarked = true;
											living.addEffect(
													new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 4));
										}
									}
								}
								world.playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.ILLUSIONER_PREPARE_BLINDNESS, SoundSource.PLAYERS, 1.0F, 1.5F);
								data.triggerPowerCooldown(selectedPowerId);
								data.recordPowerUse(selectedPowerId, player);
								data.sync(player);
							}
						}

						else if (power.equals("force_heal")) {
							float healCost = ((currentLevel >= 3)
									? ALForceAbilitiesConfig.HEAL_COST_T3.get().floatValue()
									: ((currentLevel == 2)
											? ALForceAbilitiesConfig.HEAL_COST_T2.get().floatValue()
											: ALForceAbilitiesConfig.HEAL_COST_T1.get().floatValue())) * costMult;
							if (data.consumeForceEnergy(healCost)) {
								float healAmount = (currentLevel >= 3)
										? ALForceAbilitiesConfig.HEAL_AMOUNT_T3.get().floatValue()
										: ((currentLevel == 2)
												? ALForceAbilitiesConfig.HEAL_AMOUNT_T2.get().floatValue()
												: ALForceAbilitiesConfig.HEAL_AMOUNT_T1.get().floatValue());
								player.heal(healAmount);
								player.clearFire();

								if (currentLevel >= 2) {
									double allyRadius = ((currentLevel >= 3)
											? ALForceAbilitiesConfig.HEAL_RADIUS_T3.get()
											: ALForceAbilitiesConfig.HEAL_RADIUS_T2.get()) * rangeMult;
									int maxAllies = (currentLevel >= 3) ? 99 : 3;
									List<LivingEntity> allies = world.getEntitiesOfClass(LivingEntity.class,
											player.getBoundingBox().inflate(allyRadius));
									int healedCount = 0;
									for (LivingEntity ally : allies) {
										if (ally != player && (ally instanceof Player || ally.isAlliedTo(player))) {
											if (healedCount >= maxAllies)
												break;
											float allyHeal = (currentLevel >= 3)
													? ALForceAbilitiesConfig.HEAL_ALLY_AMOUNT_T3.get().floatValue()
													: ALForceAbilitiesConfig.HEAL_ALLY_AMOUNT_T2.get().floatValue();
											ally.heal(allyHeal);
											ally.clearFire();
											if (currentLevel >= 3) {
												ally.removeEffect(MobEffects.POISON);
												ally.removeEffect(MobEffects.WITHER);
												ally.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
												ally.removeEffect(MobEffects.WEAKNESS);
											}
											world.sendParticles(ParticleTypes.HAPPY_VILLAGER, ally.getX(),
													ally.getY() + 1.0, ally.getZ(), 10, 0.5, 0.5, 0.5, 0.1);
											healedCount++;
										}
									}
								}

								if (currentLevel >= 3) {
									player.removeEffect(MobEffects.POISON);
									player.removeEffect(MobEffects.WITHER);
									player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
									player.removeEffect(MobEffects.WEAKNESS);
								}

								world.playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
								world.sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.0,
										player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
								data.triggerPowerCooldown(selectedPowerId);
								data.recordPowerUse(selectedPowerId, player);
								data.sync(player);
							}
						}

						else if (power.equals("force_repulse")) {
							float repulseCost = ((currentLevel >= 3)
									? ALForceAbilitiesConfig.REPULSE_COST_T3.get().floatValue()
									: ((currentLevel >= 2)
											? ALForceAbilitiesConfig.REPULSE_COST_T2.get().floatValue()
											: ALForceAbilitiesConfig.REPULSE_COST_T1.get().floatValue()))
									* costMult;
							if (data.consumeForceEnergy(repulseCost)) {
								double radius = ((currentLevel >= 3)
										? ALForceAbilitiesConfig.REPULSE_RADIUS_T3.get()
										: ((currentLevel >= 2)
												? ALForceAbilitiesConfig.REPULSE_RADIUS_T2.get()
												: ALForceAbilitiesConfig.REPULSE_RADIUS_T1.get()))
										* rangeMult;
								int maxTargets = (currentLevel >= 3) ? 20 : ((currentLevel >= 2) ? 12 : 8);
								float dmg = (currentLevel >= 3)
										? ALForceAbilitiesConfig.REPULSE_DAMAGE_T3.get().floatValue()
										: ((currentLevel >= 2)
												? ALForceAbilitiesConfig.REPULSE_DAMAGE_T2.get().floatValue()
												: ALForceAbilitiesConfig.REPULSE_DAMAGE_T1.get().floatValue());
								double pushForce = (currentLevel >= 3) ? 3.5D : ((currentLevel >= 2) ? 2.8D : 2.2D);

								AABB box = player.getBoundingBox().inflate(radius);
								List<LivingEntity> targets = world.getEntitiesOfClass(LivingEntity.class, box,
										e -> e != player && e.isAlive());

								int hit = 0;
								for (LivingEntity target : targets) {
									if (hit >= maxTargets)
										break;
									hit++;
									Vec3 dir = target.position().subtract(player.position()).normalize();
									target.setDeltaMovement(dir.x * pushForce, 0.85D, dir.z * pushForce);
									target.hurtMarked = true;
									target.hurt(world.damageSources().magic(), dmg);
								}

								List<Projectile> projs = world.getEntitiesOfClass(Projectile.class, box);
								for (Projectile p : projs) {
									Vec3 pDir = p.position().subtract(player.position()).normalize().scale(2.2D);
									p.setDeltaMovement(pDir);
									p.hurtMarked = true;
								}

								world.playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.2F, 1.0F);
								world.sendParticles(ParticleTypes.SONIC_BOOM, player.getX(), player.getY() + 1.0,
										player.getZ(), 1, 0, 0, 0, 0);
								data.triggerPowerCooldown(selectedPowerId);
								data.recordPowerUse(selectedPowerId, player);
								data.sync(player);
							}
						}

						else if (power.equals("force_jump")) {
							float jumpCost = ((currentLevel >= 3)
									? ALForceAbilitiesConfig.JUMP_COST_T3.get().floatValue()
									: ((currentLevel == 2)
											? ALForceAbilitiesConfig.JUMP_COST_T2.get().floatValue()
											: ALForceAbilitiesConfig.JUMP_COST_T1.get().floatValue()))
									* data.getMobilityCostMultiplier() * costMult;
							if (data.consumeForceEnergy(jumpCost)) {
								Vec3 look = player.getLookAngle();
								double yVel = (currentLevel >= 3)
										? ALForceAbilitiesConfig.JUMP_STRENGTH_T3.get()
										: ((currentLevel == 2)
												? ALForceAbilitiesConfig.JUMP_STRENGTH_T2.get()
												: ALForceAbilitiesConfig.JUMP_STRENGTH_T1.get());
								double forward = (currentLevel >= 3) ? 0.7D : ((currentLevel == 2) ? 0.45D : 0.25D);
								player.setDeltaMovement(look.x * forward, yVel, look.z * forward);
								player.hurtMarked = true;
								player.fallDistance = 0.0f;
								player.getPersistentData().putBoolean("ForceJumpNoFall", true);
								player.connection.send(
										new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(
												player));
								world.playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 1.0F, 1.2F);
								world.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(),
										15, 0.4, 0.1, 0.4, 0.1);
								data.triggerPowerCooldown(selectedPowerId);
								data.recordPowerUse(selectedPowerId, player);
								data.sync(player);
							}
						}

						else if (power.equals("force_stasis")) {
							float stasisCost = ((currentLevel >= 3)
									? ALForceAbilitiesConfig.STASIS_COST_T3.get().floatValue()
									: ((currentLevel == 2)
											? ALForceAbilitiesConfig.STASIS_COST_T2.get().floatValue()
											: ALForceAbilitiesConfig.STASIS_COST_T1.get().floatValue()))
									* costMult;
							if (data.consumeForceEnergy(stasisCost)) {
								double reach = ((currentLevel >= 3)
										? ALForceAbilitiesConfig.STASIS_REACH_T3.get()
										: ((currentLevel == 2)
												? ALForceAbilitiesConfig.STASIS_REACH_T2.get()
												: ALForceAbilitiesConfig.STASIS_REACH_T1.get()))
										* rangeMult;
								int maxTargets = (currentLevel >= 3) ? 20 : ((currentLevel >= 2) ? 10 : 4);
								Vec3 eye = player.getEyePosition();
								Vec3 look = player.getLookAngle();
								Vec3 end = eye.add(look.scale(reach));

								AABB searchBox = player.getBoundingBox().expandTowards(look.scale(reach))
										.inflate(currentLevel >= 3 ? 6.0D : 2.5D);
								List<Entity> candidates = world.getEntities(player, searchBox,
										e -> e.isAlive() && !(e instanceof TelekineticBlockEntity));

								Entity primaryTarget = null;
								double closestDist = Double.MAX_VALUE;
								for (Entity candidate : candidates) {
									AABB cBox = candidate.getBoundingBox().inflate(0.5D);
									Optional<Vec3> hit = cBox.clip(eye, end);
									if (hit.isPresent()) {
										double d = eye.distanceToSqr(hit.get());
										if (d < closestDist) {
											closestDist = d;
											primaryTarget = candidate;
										}
									}
								}

								List<Entity> targetsToFreeze = new ArrayList<>();
								if (primaryTarget != null) {
									targetsToFreeze.add(primaryTarget);
								}

								if (currentLevel >= 2) {
									List<Projectile> projectiles = world.getEntitiesOfClass(Projectile.class, searchBox);
									for (Projectile proj : projectiles) {
										if (targetsToFreeze.size() >= maxTargets)
											break;
										targetsToFreeze.add(proj);
									}

									if (currentLevel >= 3) {
										for (Entity c : candidates) {
											if (targetsToFreeze.size() >= maxTargets)
												break;
											if (!targetsToFreeze.contains(c) && (c instanceof LivingEntity)) {
												Vec3 toC = c.position().subtract(player.position()).normalize();
												if (look.dot(toC) > 0.35 || (primaryTarget != null
														&& c.distanceToSqr(primaryTarget) <= 36.0D)) {
													targetsToFreeze.add(c);
												}
											}
										}
									} else {
										for (Entity c : candidates) {
											if (targetsToFreeze.size() >= maxTargets)
												break;
											if (!targetsToFreeze.contains(c) && (c instanceof LivingEntity)) {
												Vec3 toC = c.position().subtract(player.position()).normalize();
												if (look.dot(toC) > 0.5) {
													targetsToFreeze.add(c);
												}
											}
										}
									}
								}

								int baseTicks = (currentLevel >= 3)
										? ALForceAbilitiesConfig.STASIS_DURATION_TICKS_T3.get()
										: ((currentLevel == 2)
												? ALForceAbilitiesConfig.STASIS_DURATION_TICKS_T2.get()
												: ALForceAbilitiesConfig.STASIS_DURATION_TICKS_T1.get());

								for (Entity target : targetsToFreeze) {
									int duration = (target instanceof Player || isTitanOrBoss(target)) ? baseTicks / 2
											: baseTicks;
									target.getPersistentData().putInt("ForceStasisTicks", duration);
									target.getPersistentData().putDouble("ForceStasisX", target.getX());
									target.getPersistentData().putDouble("ForceStasisY", target.getY());
									target.getPersistentData().putDouble("ForceStasisZ", target.getZ());
									target.getPersistentData().putFloat("ForceStasisYRot", target.getYRot());
									target.getPersistentData().putFloat("ForceStasisXRot", target.getXRot());
									target.setDeltaMovement(Vec3.ZERO);
									target.hurtMarked = true;

									if (target instanceof LivingEntity living) {
										living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration,
												127, false, false));
										living.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration, 10,
												false, false));
									}

									world.sendParticles(ParticleTypes.INSTANT_EFFECT, target.getX(),
											target.getY() + target.getBbHeight() * 0.5D, target.getZ(), 20, 0.5, 0.5,
											0.5, 0.1);
								}

								world.playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.2F, 1.8F);
								world.playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1.5F, 1.2F);
								world.sendParticles(ParticleTypes.ENCHANT, player.getX() + look.x, player.getEyeY(),
										player.getZ() + look.z, 25, 0.5, 0.5, 0.5, 0.2);

								data.triggerPowerCooldown(selectedPowerId);
								data.recordPowerUse(selectedPowerId, player);
								data.sync(player);
							}
						}

						else if (power.equals("force_slam")) {
							float slamCost = ((currentLevel >= 3)
									? ALForceAbilitiesConfig.SLAM_COST_T3.get().floatValue()
									: ((currentLevel == 2)
											? ALForceAbilitiesConfig.SLAM_COST_T2.get().floatValue()
											: ALForceAbilitiesConfig.SLAM_COST_T1.get().floatValue()))
									* costMult;
							if (data.consumeForceEnergy(slamCost)) {
								double radius = ((currentLevel >= 2)
										? ALForceAbilitiesConfig.SLAM_RADIUS_T2.get()
										: ALForceAbilitiesConfig.SLAM_RADIUS_T1.get()) * rangeMult;
								int maxTargets = (currentLevel >= 3) ? 10 : ((currentLevel >= 2) ? 6 : 4);
								Vec3 look = player.getLookAngle();
								AABB box = player.getBoundingBox().inflate(radius);
								List<LivingEntity> targets = world.getEntitiesOfClass(LivingEntity.class, box,
										e -> e != player && e.isAlive());

								float baseDamage = (currentLevel >= 3)
										? ALForceAbilitiesConfig.SLAM_DAMAGE_T3.get().floatValue()
										: ((currentLevel == 2)
												? ALForceAbilitiesConfig.SLAM_DAMAGE_T2.get().floatValue()
												: ALForceAbilitiesConfig.SLAM_DAMAGE_T1.get().floatValue());
								double downwardForce = (currentLevel >= 3) ? -4.0D
										: ((currentLevel >= 2) ? -3.2D : -2.5D);

								int hit = 0;
								for (LivingEntity target : targets) {
									if (hit >= maxTargets)
										break;
									Vec3 toTarget = target.position().subtract(player.position()).normalize();
									if (currentLevel >= 3 || look.dot(toTarget) > 0.25D) {
										hit++;
										target.setDeltaMovement(0.0D, downwardForce, 0.0D);
										target.hurtMarked = true;
										target.hurt(world.damageSources().flyIntoWall(), baseDamage);
										target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));

										if (target instanceof Player pTarget) {
											pTarget.disableShield();
										} else {
											target.stopUsingItem();
										}

										world.sendParticles(ParticleTypes.EXPLOSION, target.getX(),
												target.getY() + 0.2D, target.getZ(), 3, 0.3, 0.1, 0.3, 0.05);
										world.sendParticles(ParticleTypes.CRIT, target.getX(), target.getY() + 0.5D,
												target.getZ(), 15, 0.4, 0.4, 0.4, 0.2);
									}
								}

								if (currentLevel >= 3) {
									world.sendParticles(ParticleTypes.SONIC_BOOM, player.getX(), player.getY() + 0.2D,
											player.getZ(), 1, 0, 0, 0, 0);
								}

								world.playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.MACE_SMASH_GROUND, SoundSource.PLAYERS, 1.4F, 0.7F);
								world.playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0F, 0.6F);

								data.triggerPowerCooldown(selectedPowerId);
								data.recordPowerUse(selectedPowerId, player);
								data.sync(player);
							}
						}

						else if (power.equals("force_barrier") || power.equals("force_fortify")) {
							float startCost = ((currentLevel >= 3)
									? ALForceAbilitiesConfig.BARRIER_START_COST_T3.get().floatValue()
									: ((currentLevel == 2)
											? ALForceAbilitiesConfig.BARRIER_START_COST_T2.get().floatValue()
											: ALForceAbilitiesConfig.BARRIER_START_COST_T1.get().floatValue()))
									* costMult;
							String prevChannel = player.getPersistentData().getString("ForceChanneling");
							if (!"force_barrier".equals(prevChannel) && !"force_fortify".equals(prevChannel)) {
								if (!data.consumeForceEnergy(startCost)) {
									return;
								}
								data.addMasteryTicks(selectedPowerId, 400);
								data.recordPowerUse(selectedPowerId, player);
								player.getPersistentData().putFloat("BarrierAbsorbedDmg", 0.0F);
							}
							player.getPersistentData().putString("ForceChanneling", "force_barrier");
							player.getPersistentData().putInt("BarrierLevel", currentLevel);
							player.getPersistentData().putInt("FortifyLevel", currentLevel);

							if (currentLevel == 3) {
								BlockPos center = player.blockPosition();
								player.getPersistentData().putLong("DomeCenter", center.asLong());
								PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new SyncBarrierPayload(
										player.getUUID(), player.getId(), true, center, 3, true));
							} else if (currentLevel == 2) {
								Vec3 eye = player.getEyePosition();
								Vec3 lookDir = player.getLookAngle();
								double barrierReach = ALForceAbilitiesConfig.BARRIER_RADIUS_T2.get() * rangeMult * 3.5D;
								AABB searchBox = player.getBoundingBox().inflate(barrierReach);
								List<LivingEntity> possibleAllies = world.getEntitiesOfClass(LivingEntity.class,
										searchBox, e -> e != player && (e instanceof Player || e.isAlliedTo(player)));

								LivingEntity targetAlly = null;
								double closestDot = 0.7D;

								for (LivingEntity ally : possibleAllies) {
									Vec3 toAlly = ally.position().subtract(eye).normalize();
									double dot = lookDir.dot(toAlly);
									if (dot > closestDot) {
										closestDot = dot;
										targetAlly = ally;
									}
								}

								int targetId = targetAlly != null ? targetAlly.getId() : player.getId();
								player.getPersistentData().putInt("FortifyTargetId", targetId);
								PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new SyncBarrierPayload(
										player.getUUID(), targetId, false, BlockPos.ZERO, 2, true));
							} else {
								player.getPersistentData().putInt("FortifyTargetId", player.getId());
								PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new SyncBarrierPayload(
										player.getUUID(), player.getId(), false, BlockPos.ZERO, 1, true));
							}
						} else {

							if (data.getForceEnergy() < 6.0f) {
								return;
							}
							String prevChannel = player.getPersistentData().getString("ForceChanneling");
							player.getPersistentData().putString("ForceChanneling", power);
							if (!power.equals(prevChannel)) {
								data.addMasteryTicks(selectedPowerId, 400);
								data.recordPowerUse(selectedPowerId, player);
							}
						}
					} else {
						String activeChannel = player.getPersistentData().getString("ForceChanneling");
						if ("force_barrier".equals(activeChannel) || "force_fortify".equals(activeChannel)) {
							PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
									new SyncBarrierPayload(player.getUUID(), 0, false, BlockPos.ZERO, 0, false));
							player.getPersistentData().putFloat("BarrierAbsorbedDmg", 0.0F);
						}
						if (!activeChannel.isEmpty()) {
							ResourceLocation channeledId = ResourceLocation
									.fromNamespaceAndPath(AdvancedLightsabers.MODID, activeChannel);
							data.triggerPowerCooldown(channeledId);
							data.sync(player);
						}
						player.getPersistentData().putString("ForceChanneling", "");
					}
				} else if (!payload.isKeyDown()) {
					String activeChannel = player.getPersistentData().getString("ForceChanneling");
					if ("force_barrier".equals(activeChannel) || "force_fortify".equals(activeChannel)) {
						PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
								new SyncBarrierPayload(player.getUUID(), 0, false, BlockPos.ZERO, 0, false));
						player.getPersistentData().putFloat("BarrierAbsorbedDmg", 0.0F);
					}
					player.getPersistentData().putString("ForceChanneling", "");
				}
			}
		});
	}

	private static void destroyFragileBlocksInCone(ServerLevel world, Player player, int maxDist) {
		Vec3 eye = player.getEyePosition();
		Vec3 look = player.getLookAngle();
		BlockPos playerPos = player.blockPosition();

		for (int dx = -maxDist; dx <= maxDist; dx++) {
			for (int dy = -2; dy <= 4; dy++) {
				for (int dz = -maxDist; dz <= maxDist; dz++) {
					BlockPos targetPos = playerPos.offset(dx, dy, dz);
					Vec3 toTarget = Vec3.atCenterOf(targetPos).subtract(eye);
					double dist = toTarget.length();

					if (dist > 1.0D && dist <= maxDist && look.dot(toTarget.normalize()) > 0.65D) {
						BlockState state = world.getBlockState(targetPos);
						if (isFragileBlock(state)) {
							world.destroyBlock(targetPos, true, player);
						}
					}
				}
			}
		}
	}

	private static boolean isFragileBlock(BlockState state) {
		return state.is(Blocks.GLASS) || state.is(Blocks.GLASS_PANE) || state.getBlock() instanceof StainedGlassBlock
				|| state.getBlock() instanceof StainedGlassPaneBlock || state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.DOORS) || state.is(BlockTags.TRAPDOORS) || state.is(BlockTags.FLOWER_POTS);
	}

	private static boolean isTitanOrBoss(Entity entity) {
		return entity instanceof Warden || entity instanceof EnderDragon || entity instanceof WitherBoss;
	}

	private static boolean canPickUpBlock(BlockState state, ServerLevel level, BlockPos pos, int tier) {
		if (state.isAir() || state.getDestroySpeed(level, pos) < 0)
			return false;
		float destroySpeed = state.getDestroySpeed(level, pos);

		if (tier == 1) {
			if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.ANCIENT_DEBRIS))
				return false;
			return destroySpeed <= 2.0F;
		} else if (tier == 2) {
			if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.ANCIENT_DEBRIS))
				return false;
			return destroySpeed <= 5.5F;
		} else {
			return destroySpeed < 100.0F;
		}
	}
}