package com.fiskmods.pika.lightsabers.events;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALForceAbilitiesConfig;
import com.fiskmods.pika.lightsabers.ALSounds;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.network.payload.BroadcastLightningPayload;
import com.fiskmods.pika.lightsabers.network.payload.MeditationTogglePayload;
import com.fiskmods.pika.lightsabers.network.payload.SyncBarrierPayload;
import com.fiskmods.pika.lightsabers.network.payload.SyncMeditationPayload;
import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID)
public class LightsaberCombatEvents {

	private static final ResourceLocation RANGE_MOD_ID = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "ls_range");
	private static final ResourceLocation DMG_MOD_ID = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
			"ls_damage");
	private static final ResourceLocation SPEED_MOD_ID = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "ls_speed");
	private static final ResourceLocation KB_MOD_ID = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
			"ls_knockback");

	private static final ResourceLocation PASSIVE_ARMOR_ID = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "passive_armor");
	private static final ResourceLocation PASSIVE_KB_ID = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "passive_kb");
	private static final ResourceLocation PASSIVE_SPEED_ID = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "passive_speed");
	private static final ResourceLocation SITH_DARK_SPEED_ID = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "sith_dark_speed");

	private static boolean hasSkill(PlayerForceData data, String skillName) {
		if (data == null)
			return false;
		return data.hasSkill(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, skillName));
	}

	public static float getSbMult(ItemStack stack) {
		LightsaberData lsData = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
		boolean isDouble = lsData != null && lsData.isDoubleBladed();
		boolean isSingleMode = ItemLightsaber.isSingleBladeMode(stack);
		if (!isDouble)
			return 1.0f;
		if (isSingleMode)
			return 0.5f;
		return 0.0f;
	}

	public static float getDbMult(ItemStack stack) {
		LightsaberData lsData = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
		boolean isDouble = lsData != null && lsData.isDoubleBladed();
		boolean isSingleMode = ItemLightsaber.isSingleBladeMode(stack);
		if (isDouble && !isSingleMode)
			return 1.0f;
		return 0.0f;
	}

	public static boolean isInCombatCooldown(Player player) {
		if (player.isCreative())
			return false;
		long lastCombat = player.getPersistentData().getLong("LastCombatTime");
		if (lastCombat <= 0)
			return false;
		long diff = player.level().getGameTime() - lastCombat;
		return diff < 6000L;
	}

	public static int getRemainingCombatCooldownSeconds(Player player) {
		long lastCombat = player.getPersistentData().getLong("LastCombatTime");
		if (lastCombat <= 0)
			return 0;
		long diff = player.level().getGameTime() - lastCombat;
		if (diff >= 6000L)
			return 0;
		return (int) Math.ceil((6000L - diff) / 20.0);
	}

	private static List<Item> getAllFocusCrystals(LightsaberData data) {
		List<Item> allCrystals = new ArrayList<>();
		if (data.focusCrystals() != null)
			allCrystals.addAll(data.focusCrystals());
		if (data.isDoubleBladed() && data.secFocusCrystals() != null)
			allCrystals.addAll(data.secFocusCrystals());
		return allCrystals;
	}

	public record LightningTargets(LivingEntity primary, List<LivingEntity> chains) {
		public List<LivingEntity> all() {
			List<LivingEntity> list = new ArrayList<>();
			if (primary != null)
				list.add(primary);
			list.addAll(chains);
			return list;
		}
	}

	private static LightningTargets findSithLightningTargets(ServerLevel world, Player caster, double reach,
			double dotThreshold, int maxChains, double chainJumpDist) {
		Vec3 eye = caster.getEyePosition();
		Vec3 look = caster.getViewVector(1.0F);
		Vec3 dest = eye.add(look.scale(reach));

		BlockHitResult blockHit = world
				.clip(new ClipContext(eye, dest, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, caster));
		double maxForwardDist = (blockHit.getType() != HitResult.Type.MISS) ? eye.distanceTo(blockHit.getLocation())
				: reach;

		AABB searchBox = caster.getBoundingBox().expandTowards(look.scale(reach)).inflate(2.0D);
		List<LivingEntity> candidates = world.getEntitiesOfClass(LivingEntity.class, searchBox,
				e -> e != caster && e.isAlive() && !caster.isAlliedTo(e) && !(e instanceof ArmorStand));

		LivingEntity primary = null;
		double closestDistSq = Double.MAX_VALUE;
		for (LivingEntity e : candidates) {
			Vec3 toTarget = e.getEyePosition().subtract(eye);
			double dist = toTarget.length();
			if (dist > maxForwardDist)
				continue;

			if (look.dot(toTarget.normalize()) < dotThreshold)
				continue;

			BlockHitResult losHit = world.clip(new ClipContext(eye, e.getEyePosition(), ClipContext.Block.COLLIDER,
					ClipContext.Fluid.NONE, caster));
			if (losHit.getType() != HitResult.Type.MISS && eye.distanceTo(losHit.getLocation()) < dist - 0.5D) {
				continue;
			}

			double dSq = eye.distanceToSqr(e.position());
			if (dSq < closestDistSq) {
				closestDistSq = dSq;
				primary = e;
			}
		}

		List<LivingEntity> chains = new ArrayList<>();
		if (primary != null && maxChains > 0 && chainJumpDist > 0.0D) {
			AABB chainBox = primary.getBoundingBox().inflate(chainJumpDist);
			final LivingEntity p = primary;
			List<LivingEntity> nearby = world.getEntitiesOfClass(LivingEntity.class, chainBox,
					sec -> sec != caster && sec != p && sec.isAlive() && !caster.isAlliedTo(sec)
							&& !(sec instanceof ArmorStand));

			nearby.sort(Comparator.comparingDouble(sec -> p.distanceToSqr(sec)));
			for (LivingEntity sec : nearby) {
				if (chains.size() >= maxChains)
					break;
				BlockHitResult chainLos = world.clip(new ClipContext(p.getEyePosition(),
						sec.getEyePosition(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, p));
				if (chainLos.getType() == HitResult.Type.MISS || p.getEyePosition().distanceTo(chainLos.getLocation()) >= p.getEyePosition().distanceTo(sec.getEyePosition()) - 0.3D) {
					chains.add(sec);
				}
			}
		}

		return new LightningTargets(primary, chains);
	}

	private static boolean isValidEjTarget(Player caster, LivingEntity e) {
		if (e == null || !e.isAlive() || e instanceof ArmorStand || e instanceof Villager || caster.isAlliedTo(e)) {
			return false;
		}
		if (e instanceof Player sp) {
			PlayerForceData sData = PlayerForceData.get(sp);
			return sData != null && sData.getSithRank() > 0;
		}
		if (e.isInvertedHealAndHarm()) {
			return true;
		}
		if (e instanceof Monster) {
			return true;
		}
		return false;
	}

	private static LightningTargets findElectricJudgmentTargets(ServerLevel world, Player caster, double reach,
			double dotThreshold, int maxChains, double chainJumpDist, boolean hasSubChains) {
		Vec3 eye = caster.getEyePosition();
		Vec3 look = caster.getViewVector(1.0F);
		Vec3 dest = eye.add(look.scale(reach));

		BlockHitResult blockHit = world
				.clip(new ClipContext(eye, dest, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, caster));
		double maxForwardDist = (blockHit.getType() != HitResult.Type.MISS) ? eye.distanceTo(blockHit.getLocation())
				: reach;

		AABB searchBox = caster.getBoundingBox().expandTowards(look.scale(reach)).inflate(2.0D);
		List<LivingEntity> candidates = world.getEntitiesOfClass(LivingEntity.class, searchBox,
				e -> e != caster && e.isAlive() && isValidEjTarget(caster, e));

		LivingEntity primary = null;
		double closestDistSq = Double.MAX_VALUE;
		for (LivingEntity e : candidates) {
			Vec3 toTarget = e.getEyePosition().subtract(eye);
			double dist = toTarget.length();
			if (dist > maxForwardDist)
				continue;

			if (look.dot(toTarget.normalize()) < dotThreshold)
				continue;

			BlockHitResult losHit = world.clip(new ClipContext(eye, e.getEyePosition(), ClipContext.Block.COLLIDER,
					ClipContext.Fluid.NONE, caster));
			if (losHit.getType() != HitResult.Type.MISS && eye.distanceTo(losHit.getLocation()) < dist - 0.5D) {
				continue;
			}

			double dSq = eye.distanceToSqr(e.position());
			if (dSq < closestDistSq) {
				closestDistSq = dSq;
				primary = e;
			}
		}

		List<LivingEntity> chains = new ArrayList<>();
		if (primary != null && maxChains > 0 && chainJumpDist > 0.0D) {
			AABB chainBox = primary.getBoundingBox().inflate(chainJumpDist);
			final LivingEntity p = primary;
			List<LivingEntity> nearby = world.getEntitiesOfClass(LivingEntity.class, chainBox,
					sec -> sec != caster && sec != p && sec.isAlive() && isValidEjTarget(caster, sec));

			nearby.sort(Comparator.comparingDouble(sec -> p.distanceToSqr(sec)));
			List<LivingEntity> firstLevelChains = new ArrayList<>();
			for (LivingEntity sec : nearby) {
				if (firstLevelChains.size() >= maxChains)
					break;
				BlockHitResult chainLos = world.clip(new ClipContext(p.getEyePosition(),
						sec.getEyePosition(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, p));
				if (chainLos.getType() == HitResult.Type.MISS || p.getEyePosition().distanceTo(chainLos.getLocation()) >= p.getEyePosition().distanceTo(sec.getEyePosition()) - 0.3D) {
					firstLevelChains.add(sec);
				}
			}
			chains.addAll(firstLevelChains);

			if (hasSubChains) {
				List<LivingEntity> subChains = new ArrayList<>();
				for (LivingEntity chainNode : firstLevelChains) {
					AABB subBox = chainNode.getBoundingBox().inflate(chainJumpDist);
					List<LivingEntity> subNearby = world.getEntitiesOfClass(LivingEntity.class, subBox,
							sub -> sub != caster && sub != p && !firstLevelChains.contains(sub)
									&& !subChains.contains(sub) && sub.isAlive() && isValidEjTarget(caster, sub));
					subNearby.sort(Comparator.comparingDouble(sub -> chainNode.distanceToSqr(sub)));
					for (LivingEntity sub : subNearby) {
						BlockHitResult subLos = world.clip(new ClipContext(chainNode.getEyePosition(),
								sub.getEyePosition(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, chainNode));
						if (subLos.getType() == HitResult.Type.MISS || chainNode.getEyePosition().distanceTo(subLos.getLocation()) >= chainNode.getEyePosition().distanceTo(sub.getEyePosition()) - 0.3D) {
							subChains.add(sub);
							break;
						}
					}
				}
				chains.addAll(subChains);
			}
		}

		return new LightningTargets(primary, chains);
	}

	@SubscribeEvent
	public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
		if (event.getLevel() instanceof ServerLevel level) {
			Vec3 expPos = event.getExplosion().center();
			AABB checkArea = new AABB(expPos.x - 16, expPos.y - 16, expPos.z - 16, expPos.x + 16, expPos.y + 16,
					expPos.z + 16);
			List<Player> players = level.getEntitiesOfClass(Player.class, checkArea);
			for (Player player : players) {
				PlayerForceData data = PlayerForceData.get(player);
				if (data != null && "GUARD".equals(data.getJediSpecialization())) {
					event.getAffectedBlocks().clear();
					break;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
		if (event.getNewAboutToBeSetTarget() instanceof Player player) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null && data.isForceSensitive()) {
				LivingEntity attacker = event.getEntity();

				// Jedi Grandmaster or Sith Emperor: monsters will not attack unless provoked
				if ((data.getJediRank() >= 6 || data.getSithRank() >= 6) && attacker instanceof Monster monster) {
					if (monster.getLastHurtByMob() != player) {
						event.setCanceled(true);
						return;
					}
				}

				// Jedi Rank 5: Phantoms never target; 50% reduced aggro range for monsters
				if (data.getJediRank() >= 5) {
					if (attacker instanceof Phantom) {
						event.setCanceled(true);
						return;
					}
					double distSq = attacker.distanceToSqr(player);
					if (distSq > 64.0D && attacker.getLastHurtByMob() != player) {
						event.setCanceled(true);
						return;
					}
				}

				// Jedi Rank 4: Neutral mobs never aggro even if accidentally hit
				if (data.getJediRank() >= 4) {
					if (attacker instanceof IronGolem || attacker instanceof ZombifiedPiglin || attacker instanceof Wolf
							|| attacker instanceof Bee || attacker instanceof PolarBear) {
						event.setCanceled(true);
						return;
					}
				}

				// Jedi Rank 2: Neutral animals never provoke without reason; 25% reduced monster aggro
				if (data.getJediRank() >= 2) {
					if (attacker instanceof Wolf || attacker instanceof Bee || attacker instanceof PolarBear
							|| attacker instanceof IronGolem || attacker instanceof ZombifiedPiglin) {
						if (attacker.getLastHurtByMob() != player) {
							event.setCanceled(true);
							return;
						}
					}
					if (attacker instanceof Monster) {
						double distSq = attacker.distanceToSqr(player);
						if (distSq > 144.0D && attacker.getLastHurtByMob() != player) {
							event.setCanceled(true);
							return;
						}
					}
				}

				// Sith Rank 4: Pests (Spiders, Silverfish, Endermites) do not attack unless struck
				if (data.getSithRank() >= 4) {
					if (attacker instanceof Spider || attacker instanceof Silverfish || attacker instanceof Endermite) {
						if (attacker.getLastHurtByMob() != player) {
							event.setCanceled(true);
							return;
						}
					}
				}
			}
		}
	}

	private static void handleChanneledPowers(Player player, PlayerForceData data) {
		String channeledPower = player.getPersistentData().getString("ForceChanneling");
		if (channeledPower == null || channeledPower.isEmpty())
			return;

		ResourceLocation powerId = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, channeledPower);

		data.addMasteryTicks(powerId, 8);

		ServerLevel world = (ServerLevel) player.level();
		Vec3 look = player.getLookAngle();
		int currentLevel = data.getCurrentSelectedPowerLevel();
		float costMult = data.getPowerCostMultiplier(powerId);
		float rangeMult = data.getRangeMultiplier(powerId);

		switch (channeledPower) {
		case "force_lightning":
			float flDrainRate = ((currentLevel >= 3)
					? ALForceAbilitiesConfig.LIGHTNING_HOLD_COST_T3.get().floatValue()
					: ((currentLevel == 2)
							? ALForceAbilitiesConfig.LIGHTNING_HOLD_COST_T2.get().floatValue()
							: ALForceAbilitiesConfig.LIGHTNING_HOLD_COST_T1.get().floatValue())) / 20.0f;

			if (!data.consumeForceEnergy(flDrainRate * costMult)) {
				player.getPersistentData().putString("ForceChanneling", "");
				data.triggerPowerCooldown(powerId);
				data.sync((ServerPlayer) player);
				return;
			}

			if (player.tickCount % 2 == 0) {
				PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
						new BroadcastLightningPayload(player.getId(), false, currentLevel));
			}

			if (player.tickCount % 8 == 0) {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER,
						SoundSource.PLAYERS, 0.3F, 2.0F);
			}

			double sithReach = ((currentLevel >= 3)
					? ALForceAbilitiesConfig.LIGHTNING_REACH_T3.get()
					: ((currentLevel == 2)
							? ALForceAbilitiesConfig.LIGHTNING_REACH_T2.get()
							: ALForceAbilitiesConfig.LIGHTNING_REACH_T1.get())) * rangeMult;

			int sithMaxChains = (currentLevel >= 3)
					? (ALForceAbilitiesConfig.LIGHTNING_MAX_TARGETS_T3.get() - 1)
					: ((currentLevel == 2)
							? (ALForceAbilitiesConfig.LIGHTNING_MAX_TARGETS_T2.get() - 1)
							: 0);
			if ("INQUISITOR".equals(data.getSithSpecialization())) {
				sithMaxChains += 1;
			}
			double sithChainDist = (currentLevel >= 3)
					? ALForceAbilitiesConfig.LIGHTNING_CHAIN_RANGE_T3.get()
					: ((currentLevel == 2)
							? ALForceAbilitiesConfig.LIGHTNING_CHAIN_RANGE_T2.get()
							: 0.0D);

			LightningTargets sithTargets = findSithLightningTargets(world, player, sithReach, 0.65D, sithMaxChains, sithChainDist);

			if (sithTargets.primary() != null) {
				float primaryDmg10 = (currentLevel >= 3)
						? ALForceAbilitiesConfig.LIGHTNING_DAMAGE_PRIMARY_T3.get().floatValue()
						: ((currentLevel == 2)
								? ALForceAbilitiesConfig.LIGHTNING_DAMAGE_PRIMARY_T2.get().floatValue()
								: ALForceAbilitiesConfig.LIGHTNING_DAMAGE_PRIMARY_T1.get().floatValue());

				float chainDmg10 = (currentLevel >= 3)
						? ALForceAbilitiesConfig.LIGHTNING_DAMAGE_CHAIN_T3.get().floatValue()
						: ((currentLevel == 2)
								? ALForceAbilitiesConfig.LIGHTNING_DAMAGE_CHAIN_T2.get().floatValue()
								: primaryDmg10);

				float sithCorruptChance = (currentLevel >= 3)
						? ALForceAbilitiesConfig.LIGHTNING_CORRUPT_CHANCE_T3.get().floatValue()
						: ((currentLevel == 2)
								? ALForceAbilitiesConfig.LIGHTNING_CORRUPT_CHANCE_T2.get().floatValue()
								: ALForceAbilitiesConfig.LIGHTNING_CORRUPT_CHANCE_T1.get().floatValue());

				float armorPen = (currentLevel >= 3)
						? ALForceAbilitiesConfig.LIGHTNING_ARMOR_PENETRATION_T3.get().floatValue()
						: 0.0f;

				for (LivingEntity t : sithTargets.all()) {
					boolean isPrimary = (t == sithTargets.primary());
					float dmg10 = isPrimary ? primaryDmg10 : chainDmg10;

					Vec3 mot = t.getDeltaMovement();
					t.setDeltaMovement(0.0D, Math.min(mot.y, 0.0D), 0.0D);
					t.hurtMarked = true;

					if (currentLevel == 1) {
						t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 0, false, false));
						t.setSprinting(false);
					} else if (currentLevel == 2) {
						t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 0, false, false));
						t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1, false, false));
						t.setRemainingFireTicks(30);
					} else if (currentLevel >= 3) {
						t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 0, false, false));
						t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1, false, false));
					}

					if (player.tickCount % 10 == 0) {
						t.invulnerableTime = 0;
						if (armorPen > 0.0f) {
							float bypassDmg = dmg10 * armorPen;
							float normalDmg = dmg10 - bypassDmg;
							t.hurt(world.damageSources().generic(), bypassDmg);
							t.invulnerableTime = 0;
							t.hurt(world.damageSources().lightningBolt(), normalDmg);
						} else {
							DamageSource dSource = data.hasArmorPiercingForce() ? world.damageSources().magic()
									: world.damageSources().lightningBolt();
							t.hurt(dSource, dmg10);
						}

						float returnRatio = data.getDamageToManaReturnRatio();
						if (returnRatio > 0.0f) {
							data.addForceEnergy(dmg10 * returnRatio);
						}
					}

					if (sithCorruptChance > 0.0F && player.tickCount % 20 == 0) {
						if (t.getType() == net.minecraft.world.entity.EntityType.VILLAGER) {
							if (world.random.nextFloat() < sithCorruptChance) {
								((Mob) t).convertTo(net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER, true);
							}
						} else if (t.getType() == net.minecraft.world.entity.EntityType.ZOMBIE
								|| t.getType() == net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER) {
							if (world.random.nextFloat() < sithCorruptChance) {
								((Mob) t).convertTo(net.minecraft.world.entity.EntityType.SKELETON, true);
							}
						}
					}
				}
			}
			break;

		case "electric_judgement":
		case "electric_judgment":
			float ejDrainRate = ((currentLevel >= 4)
					? ALForceAbilitiesConfig.EJ_HOLD_COST_T4.get().floatValue()
					: ((currentLevel == 3)
							? ALForceAbilitiesConfig.EJ_HOLD_COST_T3.get().floatValue()
							: ((currentLevel == 2)
									? ALForceAbilitiesConfig.EJ_HOLD_COST_T2.get().floatValue()
									: ALForceAbilitiesConfig.EJ_HOLD_COST_T1.get().floatValue()))) / 20.0f;

			if (!data.consumeForceEnergy(ejDrainRate * costMult)) {
				player.getPersistentData().putString("ForceChanneling", "");
				data.triggerPowerCooldown(powerId);
				data.sync((ServerPlayer) player);
				return;
			}

			if (player.tickCount % 2 == 0) {
				PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
						new BroadcastLightningPayload(player.getId(), true, currentLevel));
			}

			if (player.tickCount % 4 == 0) {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME,
						SoundSource.PLAYERS, 0.15F, 1.8F);
			}
			if (player.tickCount % 8 == 0) {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER,
						SoundSource.PLAYERS, 0.3F, 2.0F);
			}

			double jediReach = ((currentLevel >= 4)
					? ALForceAbilitiesConfig.EJ_REACH_T4.get()
					: ((currentLevel == 3)
							? ALForceAbilitiesConfig.EJ_REACH_T3.get()
							: ((currentLevel == 2)
									? ALForceAbilitiesConfig.EJ_REACH_T2.get()
									: ALForceAbilitiesConfig.EJ_REACH_T1.get()))) * rangeMult;

			int jediMaxChains = (currentLevel >= 4)
					? (ALForceAbilitiesConfig.EJ_MAX_TARGETS_T4.get() - 1)
					: ((currentLevel == 3)
							? (ALForceAbilitiesConfig.EJ_MAX_TARGETS_T3.get() - 1)
							: ((currentLevel == 2)
									? (ALForceAbilitiesConfig.EJ_MAX_TARGETS_T2.get() - 1)
									: 0));

			double jediChainDist = (currentLevel >= 4)
					? ALForceAbilitiesConfig.EJ_CHAIN_RANGE_T4.get()
					: ((currentLevel == 3)
							? ALForceAbilitiesConfig.EJ_CHAIN_RANGE_T3.get()
							: ((currentLevel == 2)
									? ALForceAbilitiesConfig.EJ_CHAIN_RANGE_T2.get()
									: 0.0D));

			boolean jediHasSubChains = (currentLevel == 3);

			LightningTargets jediTargets = findElectricJudgmentTargets(world, player, jediReach, 0.50D, jediMaxChains,
					jediChainDist, jediHasSubChains);

			List<LivingEntity> allEjTargets = jediTargets.all();
			// If no valid targets found, channel immediately turns off
			if (allEjTargets.isEmpty()) {
				player.getPersistentData().putString("ForceChanneling", "");
				data.triggerPowerCooldown(powerId);
				data.sync((ServerPlayer) player);
				return;
			}

			float undeadDmg10 = (currentLevel >= 4)
					? ALForceAbilitiesConfig.EJ_DAMAGE_UNDEAD_T4.get().floatValue()
					: ((currentLevel == 3)
							? ALForceAbilitiesConfig.EJ_DAMAGE_UNDEAD_T3.get().floatValue()
							: ((currentLevel == 2)
									? ALForceAbilitiesConfig.EJ_DAMAGE_UNDEAD_T2.get().floatValue()
									: ALForceAbilitiesConfig.EJ_DAMAGE_UNDEAD_T1.get().floatValue()));

			float sithDmg10 = (currentLevel >= 4)
					? ALForceAbilitiesConfig.EJ_DAMAGE_SITH_T4.get().floatValue()
					: ((currentLevel == 3)
							? ALForceAbilitiesConfig.EJ_DAMAGE_SITH_T3.get().floatValue()
							: ((currentLevel == 2)
									? ALForceAbilitiesConfig.EJ_DAMAGE_SITH_T2.get().floatValue()
									: ALForceAbilitiesConfig.EJ_DAMAGE_SITH_T1.get().floatValue()));

			float jediConvertChance = (currentLevel >= 4)
					? ALForceAbilitiesConfig.EJ_CONVERT_CHANCE_T4.get().floatValue()
					: ((currentLevel == 3)
							? ALForceAbilitiesConfig.EJ_CONVERT_CHANCE_T3.get().floatValue()
							: ((currentLevel == 2)
									? ALForceAbilitiesConfig.EJ_CONVERT_CHANCE_T2.get().floatValue()
									: ALForceAbilitiesConfig.EJ_CONVERT_CHANCE_T1.get().floatValue()));

			for (LivingEntity t : allEjTargets) {
				boolean isUndead = t.isInvertedHealAndHarm();

				// Macht-Schwebung vs Undead for Tier 2, 3, 4
				if (isUndead && currentLevel >= 2) {
					Vec3 mot = t.getDeltaMovement();
					double targetY = (mot.y < 0.08D) ? 0.08D : 0.02D;
					t.setDeltaMovement(mot.x * 0.8D, targetY, mot.z * 0.8D);
					t.fallDistance = 0.0F;
					t.hurtMarked = true;
				} else {
					Vec3 mot = t.getDeltaMovement();
					t.setDeltaMovement(0.0D, Math.min(mot.y, 0.0D), 0.0D);
					t.hurtMarked = true;
				}

				// Status effects
				if (currentLevel == 3) {
					if (t instanceof Player) {
						t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1, false, false));
					} else {
						t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 2, false, false));
					}
				} else {
					t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 0, false, false));
					t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1, false, false));
				}

				if (player.tickCount % 10 == 0) {
					if (t instanceof Player sithPlayer) {
						PlayerForceData targetData = PlayerForceData.get(sithPlayer);
						if (targetData != null && targetData.getSithRank() > 0) {
							t.invulnerableTime = 0;
							t.hurt(world.damageSources().magic(), sithDmg10);

							if (currentLevel >= 2) {
								float cdIncrease = ALForceAbilitiesConfig.EJ_SITH_COOLDOWN_INCREASE_T2.get().floatValue();
								if (cdIncrease > 0.0f) {
									for (ResourceLocation pId : PlayerForceData.ALL_ACTIVE_POWERS) {
										int curCd = targetData.getPowerCooldown(pId);
										if (curCd > 0) {
											targetData.setPowerCooldown(pId, (int) (curCd * (1.0f + cdIncrease)));
										}
									}
								}
							}

							if (currentLevel >= 3) {
								int silTicks = ALForceAbilitiesConfig.EJ_SILENCE_TICKS_T3.get();
								targetData.setForceSilenceTicks(silTicks);
								sithPlayer.displayClientMessage(
										Component.translatable("message.lightsabersfork.force_silence")
												.withStyle(ChatFormatting.DARK_PURPLE),
										true);
								targetData.sync((ServerPlayer) sithPlayer);
							}
						}
					} else {
						float finalDmg = isUndead ? undeadDmg10 : sithDmg10;

						if (isUndead && currentLevel >= 3 && finalDmg >= t.getHealth()) {
							// electric judgement soll Untote in "Luft" auflösen, keine Drops oder sowas
							world.sendParticles(ParticleTypes.POOF, t.getX(), t.getY() + t.getBbHeight() * 0.5D, t.getZ(),
									25, 0.4, 0.4, 0.4, 0.05);
							world.sendParticles(ParticleTypes.ELECTRIC_SPARK, t.getX(), t.getY() + t.getBbHeight() * 0.5D, t.getZ(),
									20, 0.3, 0.3, 0.3, 0.1);
							world.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, t.getX(), t.getY() + t.getBbHeight() * 0.5D, t.getZ(),
									15, 0.3, 0.3, 0.3, 0.1);
							world.playSound(null, t.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.2F);
							world.playSound(null, t.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.8F, 1.6F);
							t.discard();
						} else {
							t.invulnerableTime = 0;
							t.hurt(world.damageSources().magic(), finalDmg);
						}
					}
				}

				// Conversion logic
				if (jediConvertChance > 0.0F && player.tickCount % 20 == 0 && t.isAlive()) {
					if (world.random.nextFloat() < jediConvertChance) {
						if (ALForceAbilitiesConfig.EJ_CONVERT_WITCH.get() && t instanceof Witch witch) {
							world.sendParticles(ParticleTypes.HAPPY_VILLAGER, witch.getX(), witch.getY() + 1.0D, witch.getZ(), 20, 0.4, 0.4, 0.4, 0.05);
							world.playSound(null, witch.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 1.0F, 1.2F);
							Villager villager = witch.convertTo(EntityType.VILLAGER, false);
							if (villager != null) {
								villager.setHealth(villager.getMaxHealth());
							}
						} else if (ALForceAbilitiesConfig.EJ_CONVERT_SKELETON.get() && t instanceof AbstractSkeleton skel) {
							world.sendParticles(ParticleTypes.HAPPY_VILLAGER, skel.getX(), skel.getY() + 1.0D, skel.getZ(), 20, 0.4, 0.4, 0.4, 0.05);
							world.playSound(null, skel.blockPosition(), SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.PLAYERS, 1.0F, 1.0F);
							Zombie zombie = skel.convertTo(EntityType.ZOMBIE, false);
							if (zombie != null) {
								zombie.setHealth(zombie.getMaxHealth());
							}
						} else if (ALForceAbilitiesConfig.EJ_CONVERT_ZOMBIE.get() && t instanceof Zombie zombie) {
							world.sendParticles(ParticleTypes.HAPPY_VILLAGER, zombie.getX(), zombie.getY() + 1.0D, zombie.getZ(), 20, 0.4, 0.4, 0.4, 0.05);
							world.playSound(null, zombie.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 1.0F, 1.0F);
							Villager villager = zombie.convertTo(EntityType.VILLAGER, false);
							if (villager != null) {
								villager.setHealth(villager.getMaxHealth());
							}
						} else if (t instanceof EnderMan enderman) {
							world.sendParticles(ParticleTypes.PORTAL, enderman.getX(), enderman.getY() + 1.0D, enderman.getZ(), 40, 0.5, 1.0, 0.5, 0.1);
							world.sendParticles(ParticleTypes.POOF, enderman.getX(), enderman.getY() + 1.0D, enderman.getZ(), 25, 0.5, 0.8, 0.5, 0.05);
							world.playSound(null, enderman.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.2F, 1.0F);
							if (ALForceAbilitiesConfig.EJ_ENDERMAN_FAKEOUT.get()) {
								enderman.setLastHurtByMob(null);
								enderman.setTarget(null);
								world.playSound(null, enderman.blockPosition(), SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 0.8F, 1.2F);
							} else {
								Villager villager = enderman.convertTo(EntityType.VILLAGER, false);
								if (villager != null) {
									villager.setHealth(villager.getMaxHealth());
								}
							}
						}
					}
				}
			}
			break;

		case "force_choke":
			float chokeDrainRate = ((currentLevel >= 3)
					? ALForceAbilitiesConfig.CHOKE_HOLD_COST_T3.get().floatValue()
					: ((currentLevel == 2)
							? ALForceAbilitiesConfig.CHOKE_HOLD_COST_T2.get().floatValue()
							: ALForceAbilitiesConfig.CHOKE_HOLD_COST_T1.get().floatValue())) / 20.0f;

			if (!data.consumeForceEnergy(chokeDrainRate * costMult)) {
				player.getPersistentData().putString("ForceChanneling", "");
				data.triggerPowerCooldown(powerId);
				data.sync((ServerPlayer) player);
				return;
			}

			double chokeReach = ((currentLevel >= 3)
					? ALForceAbilitiesConfig.CHOKE_REACH_T3.get()
					: ((currentLevel == 2)
							? ALForceAbilitiesConfig.CHOKE_REACH_T2.get()
							: ALForceAbilitiesConfig.CHOKE_REACH_T1.get())) * rangeMult;
			int chokeMaxTargets = (currentLevel >= 3)
					? ALForceAbilitiesConfig.CHOKE_MAX_TARGETS_T3.get()
					: ((currentLevel == 2)
							? ALForceAbilitiesConfig.CHOKE_MAX_TARGETS_T2.get()
							: ALForceAbilitiesConfig.CHOKE_MAX_TARGETS_T1.get());
			AABB chokeBox = player.getBoundingBox().inflate(chokeReach);
			List<LivingEntity> chokeCandidates = world.getEntitiesOfClass(LivingEntity.class, chokeBox,
					e -> e != player && e.isAlive() && !player.isAlliedTo(e) && !(e instanceof ArmorStand));

			chokeCandidates.sort(Comparator.comparingDouble(e -> player.distanceToSqr(e)));
			int chokedCount = 0;

			for (LivingEntity cTarget : chokeCandidates) {
				if (chokedCount >= chokeMaxTargets)
					break;
				Vec3 toTarget = cTarget.position().subtract(player.position()).normalize();
				if (look.dot(toTarget) > 0.80) {
					chokedCount++;
					cTarget.setDeltaMovement(0, 0.05, 0);
					cTarget.hurtMarked = true;
					world.sendParticles(ParticleTypes.SMOKE, cTarget.getX(), cTarget.getEyeY(), cTarget.getZ(), 2, 0.1,
							0.1, 0.1, 0.0);

					if (cTarget instanceof Player pVictim) {
						pVictim.disableShield();
						pVictim.stopUsingItem();
						PlayerForceData vData = PlayerForceData.get(pVictim);
						if (vData != null) {
							vData.setForceSilenceTicks(20);
							vData.sync((ServerPlayer) pVictim);
						}
					} else {
						cTarget.stopUsingItem();
					}

					if (player.tickCount % 10 == 0) {
						float chokeDmg = (currentLevel >= 3)
								? ALForceAbilitiesConfig.CHOKE_DAMAGE_T3.get().floatValue()
								: ((currentLevel == 2)
										? ALForceAbilitiesConfig.CHOKE_DAMAGE_T2.get().floatValue()
										: ALForceAbilitiesConfig.CHOKE_DAMAGE_T1.get().floatValue());
						DamageSource cSource = (currentLevel >= 2) ? world.damageSources().genericKill()
								: world.damageSources().drown();

						double execHp = ALForceAbilitiesConfig.CHOKE_EXECUTE_THRESHOLD.get();
						if (currentLevel >= 3 && (cTarget.getHealth() / cTarget.getMaxHealth()) <= execHp) {
							cTarget.invulnerableTime = 0;
							cTarget.hurt(world.damageSources().genericKill(), 999.0f);
						} else {
							cTarget.invulnerableTime = 0;
							cTarget.hurt(cSource, chokeDmg);
						}

						world.playSound(null, cTarget.getX(), cTarget.getY(), cTarget.getZ(), SoundEvents.PLAYER_BREATH,
								SoundSource.PLAYERS, 1.0F, 0.5F);
					}
				}
			}
			break;

		case "force_drain":
			float drainDrainRate = ((currentLevel >= 3)
					? ALForceAbilitiesConfig.DRAIN_HOLD_COST_T3.get().floatValue()
					: ((currentLevel == 2)
							? ALForceAbilitiesConfig.DRAIN_HOLD_COST_T2.get().floatValue()
							: ALForceAbilitiesConfig.DRAIN_HOLD_COST_T1.get().floatValue())) / 20.0f;

			if (!data.consumeForceEnergy(drainDrainRate * costMult)) {
				player.getPersistentData().putString("ForceChanneling", "");
				data.triggerPowerCooldown(powerId);
				data.sync((ServerPlayer) player);
				return;
			}

			double drainReach = ((currentLevel >= 3)
					? ALForceAbilitiesConfig.DRAIN_REACH_T3.get()
					: ((currentLevel == 2)
							? ALForceAbilitiesConfig.DRAIN_REACH_T2.get()
							: ALForceAbilitiesConfig.DRAIN_REACH_T1.get())) * rangeMult;
			int drainMaxTargets = (currentLevel >= 3)
					? ALForceAbilitiesConfig.DRAIN_MAX_TARGETS_T3.get()
					: ((currentLevel == 2)
							? ALForceAbilitiesConfig.DRAIN_MAX_TARGETS_T2.get()
							: ALForceAbilitiesConfig.DRAIN_MAX_TARGETS_T1.get());
			if ("INQUISITOR".equals(data.getSithSpecialization())) {
				drainMaxTargets += 1;
			}

			AABB drainBox = player.getBoundingBox().inflate(drainReach);
			List<LivingEntity> drainCandidates = world.getEntitiesOfClass(LivingEntity.class, drainBox,
					e -> e != player && e.isAlive() && !player.isAlliedTo(e) && !(e instanceof ArmorStand));

			drainCandidates.sort(Comparator.comparingDouble(e -> player.distanceToSqr(e)));
			int drainedCount = 0;

			for (LivingEntity dTarget : drainCandidates) {
				if (drainedCount >= drainMaxTargets)
					break;
				Vec3 toTarget = dTarget.position().subtract(player.position()).normalize();
				if (look.dot(toTarget) > 0.75) {
					drainedCount++;
					world.sendParticles(ParticleTypes.DAMAGE_INDICATOR, dTarget.getX(), dTarget.getY() + 1,
							dTarget.getZ(), 2, 0.2, 0.2, 0.2, 0.1);

					if (dTarget instanceof Player pVictim) {
						PlayerForceData vData = PlayerForceData.get(pVictim);
						if (vData != null) {
							vData.setForceSilenceTicks(20);
							vData.sync((ServerPlayer) pVictim);
						}
					}

					if (currentLevel >= 3) {
						dTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1, false, false));
					}

					if (player.tickCount % 10 == 0) {
						float drainMult = data.getDrainMultiplier();
						float baseDmg = (currentLevel >= 3)
								? ALForceAbilitiesConfig.DRAIN_HP_T3.get().floatValue()
								: ((currentLevel == 2)
										? ALForceAbilitiesConfig.DRAIN_HP_T2.get().floatValue()
										: ALForceAbilitiesConfig.DRAIN_HP_T1.get().floatValue());
						float drainAmount = baseDmg * drainMult;

						dTarget.invulnerableTime = 0;
						dTarget.hurt(world.damageSources().magic(), drainAmount);
						player.heal(drainAmount);

						float siphonedEnergy = (currentLevel >= 3)
								? ALForceAbilitiesConfig.DRAIN_ENERGY_T3.get().floatValue()
								: ((currentLevel == 2)
										? ALForceAbilitiesConfig.DRAIN_ENERGY_T2.get().floatValue()
										: ALForceAbilitiesConfig.DRAIN_ENERGY_T1.get().floatValue());
						if (dTarget instanceof Player targetPlayer) {
							PlayerForceData targetData = PlayerForceData.get(targetPlayer);
							if (targetData != null && targetData.consumeForceEnergy(siphonedEnergy)) {
								data.addForceEnergy(siphonedEnergy * drainMult);
								targetData.sync((ServerPlayer) targetPlayer);
							}
							if (currentLevel >= 2) {
								targetPlayer.getFoodData().setFoodLevel(Math.max(0, targetPlayer.getFoodData().getFoodLevel() - 1));
								player.getFoodData().eat(1, 0.2F);
							}
						} else {
							if (currentLevel >= 3) {
								data.addForceEnergy(siphonedEnergy * 0.5f * drainMult);
							}
						}
					}
				}
			}
			break;

		case "force_barrier":
		case "force_fortify":
			int barrierLevel = player.getPersistentData().getInt("BarrierLevel");
			if (barrierLevel <= 0)
				barrierLevel = player.getPersistentData().getInt("FortifyLevel");
			if (barrierLevel <= 0)
				barrierLevel = 1;

			float barrierDrainRate = ((barrierLevel >= 3)
					? ALForceAbilitiesConfig.BARRIER_HOLD_COST_T3.get().floatValue()
					: ((barrierLevel == 2)
							? ALForceAbilitiesConfig.BARRIER_HOLD_COST_T2.get().floatValue()
							: ALForceAbilitiesConfig.BARRIER_HOLD_COST_T1.get().floatValue())) / 20.0f;

			if (!data.consumeForceEnergy(barrierDrainRate * costMult)) {
				shatterShield((ServerPlayer) player);
				return;
			}

			if (barrierLevel == 3) {
				BlockPos domePos = BlockPos.of(player.getPersistentData().getLong("DomeCenter"));
				Vec3 centerVec = Vec3.atCenterOf(domePos);
				double domeRadius = ALForceAbilitiesConfig.BARRIER_RADIUS_T3.get() * rangeMult;

				double groundY = getBarrierGroundY(world, domePos.getX(), domePos.getY(), domePos.getZ());
				double minY = Math.min(domePos.getY() - domeRadius, groundY) - 2.0D;
				double maxY = domePos.getY() + domeRadius + 2.0D;
				AABB domeArea = new AABB(centerVec.x - domeRadius - 2.5D, minY, centerVec.z - domeRadius - 2.5D,
						centerVec.x + domeRadius + 2.5D, maxY, centerVec.z + domeRadius + 2.5D);

				List<Mob> mobs = world.getEntitiesOfClass(Mob.class, domeArea);
				for (Mob mob : mobs) {
					if (!mob.isAlive() || mob.isAlliedTo(player)) {
						continue;
					}

					boolean isAggressive = (mob.getTarget() == player)
							|| (mob.getTarget() != null && mob.getTarget().isAlliedTo(player))
							|| (mob instanceof Monster);

					if (!isAggressive) {
						continue;
					}

					Vec3 mPos = mob.position();
					double dx = mPos.x - centerVec.x;
					double dz = mPos.z - centerVec.z;
					double horizDist = Math.sqrt(dx * dx + dz * dz);
					double nx, nz;
					if (horizDist > 0.05D) {
						nx = dx / horizDist;
						nz = dz / horizDist;
					} else {
						nx = 1.0D;
						nz = 0.0D;
					}

					double mobGroundY = getBarrierGroundY(world, (int) Math.floor(mPos.x), domePos.getY(),
							(int) Math.floor(mPos.z));
					boolean withinY = (mPos.y >= mobGroundY - 1.5D) && (mPos.y <= domePos.getY() + domeRadius + 1.0D);
					if (!withinY) {
						continue;
					}

					boolean isInside = isInsideBarrierTier3(mPos, domePos, domeRadius, world);
					boolean isTryingToEnter = !isInside && isInsideBarrierTier3(mPos, domePos, domeRadius + 0.8D, world);

					if (isInside || isTryingToEnter) {
						if (isInside) {
							// Eject aggressive mob out of the barrier
							double throwSpeed = 1.35D;
							mob.setDeltaMovement(nx * throwSpeed, 0.45D, nz * throwSpeed);
							mob.hurtMarked = true;
							mob.hasImpulse = true;
						} else {
							// Repel mob from entering the barrier
							double repelSpeed = 0.85D;
							mob.setDeltaMovement(nx * repelSpeed, 0.15D, nz * repelSpeed);
							mob.hurtMarked = true;
							mob.hasImpulse = true;
						}

						long lastHit = mob.getPersistentData().getLong("LastBarrierHit");
						long gameTime = world.getGameTime();
						if (gameTime - lastHit >= 10) {
							mob.getPersistentData().putLong("LastBarrierHit", gameTime);

							float currentAbsorbed = player.getPersistentData().getFloat("BarrierAbsorbedDmg") + 1.0F;
							player.getPersistentData().putFloat("BarrierAbsorbedDmg", currentAbsorbed);

							float maxCapacity = ALForceAbilitiesConfig.BARRIER_CAPACITY_T3.get().floatValue();
							float energyPerDmg = ALForceAbilitiesConfig.BARRIER_ENERGY_PER_DAMAGE_T3.get().floatValue();
							float barrierCostMult = data.getPowerCostMultiplier(
									ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_barrier"));
							float energyCost = 1.0F * energyPerDmg * barrierCostMult;

							world.playSound(null, mob.getX(), mob.getY(), mob.getZ(),
									SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 1.3F);
							world.sendParticles(ParticleTypes.ELECTRIC_SPARK, mob.getX(), mob.getY() + 1.0D, mob.getZ(),
									12, 0.25, 0.25, 0.25, 0.1);

							if (!data.consumeForceEnergy(energyCost) || currentAbsorbed >= maxCapacity) {
								shatterShield((ServerPlayer) player);
								return;
							}
						}
					}
				}

				List<Projectile> projectiles = world.getEntitiesOfClass(Projectile.class, domeArea);
				for (Projectile proj : projectiles) {
					if (isInsideBarrierTier3(proj.position(), domePos, domeRadius + 0.4D, world)) {
						world.playSound(null, proj.getX(), proj.getY(), proj.getZ(), SoundEvents.SHIELD_BLOCK,
								SoundSource.PLAYERS, 0.8F, 1.4F);
						world.sendParticles(ParticleTypes.ELECTRIC_SPARK, proj.getX(), proj.getY(), proj.getZ(), 8, 0.2,
								0.2, 0.2, 0.1);

						if (proj.getOwner() != null && proj.getOwner() != player) {
							Vec3 toSender = proj.getOwner().getEyePosition().subtract(proj.position()).normalize();
							proj.setDeltaMovement(toSender.scale(1.5D));
							proj.setOwner(player);
						} else {
							proj.discard();
						}
						data.consumeForceEnergy(2.0F * costMult);
					}
				}
			} else {
				int targetId = player.getPersistentData().getInt("FortifyTargetId");
				Entity shieldedEntity = world.getEntity(targetId);
				if (shieldedEntity != null && shieldedEntity.isAlive()) {
					double r = ((barrierLevel >= 2)
							? ALForceAbilitiesConfig.BARRIER_RADIUS_T2.get()
							: ALForceAbilitiesConfig.BARRIER_RADIUS_T1.get()) * rangeMult;
					AABB bubbleBox = shieldedEntity.getBoundingBox().inflate(r);
					List<Projectile> projs = world.getEntitiesOfClass(Projectile.class, bubbleBox);
					for (Projectile proj : projs) {
						world.playSound(null, proj.getX(), proj.getY(), proj.getZ(), SoundEvents.SHIELD_BLOCK,
								SoundSource.PLAYERS, 0.8F, 1.4F);
						world.sendParticles(ParticleTypes.ELECTRIC_SPARK, proj.getX(), proj.getY(), proj.getZ(), 6, 0.1,
								0.1, 0.1, 0.1);
						proj.discard();
						data.consumeForceEnergy(1.5F * costMult);
					}
				} else {
					shatterShield((ServerPlayer) player);
				}
			}
			break;
		}

		if (player.tickCount % 5 == 0) {
			data.sync((ServerPlayer) player);
		}
	}

	private static void shatterShield(ServerPlayer player) {
		player.getPersistentData().putString("ForceChanneling", "");
		player.getPersistentData().putFloat("BarrierAbsorbedDmg", 0.0F);
		PlayerForceData data = PlayerForceData.get(player);
		if (data != null) {
			data.triggerPowerCooldown(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_barrier"));
			data.sync(player);
		}
		player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GLASS_BREAK,
				SoundSource.PLAYERS, 1.0F, 0.8F);
		player.serverLevel().sendParticles(ParticleTypes.POOF, player.getX(), player.getY() + 1.0D, player.getZ(), 20,
				0.5, 0.5, 0.5, 0.1);
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
				new SyncBarrierPayload(player.getUUID(), 0, false, BlockPos.ZERO, 0, false));
	}

	public static double getBarrierGroundY(Level level, int x, int startY, int z) {
		if (level == null) {
			return startY;
		}
		BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(x, startY, z);
		int minBuild = level.getMinBuildHeight();
		int maxScan = Math.max(minBuild, startY - 64);
		while (mpos.getY() > maxScan) {
			net.minecraft.world.level.block.state.BlockState st = level.getBlockState(mpos);
			if (!st.isAir() && (st.blocksMotion() || !st.getFluidState().isEmpty())) {
				break;
			}
			mpos.move(net.minecraft.core.Direction.DOWN);
		}
		return mpos.getY() + 1.0D;
	}

	public static boolean isInsideBarrierTier3(Vec3 pos, BlockPos domePos, double domeRadius, Level level) {
		Vec3 center = Vec3.atCenterOf(domePos);
		double dx = pos.x - center.x;
		double dz = pos.z - center.z;
		double horizDist = Math.sqrt(dx * dx + dz * dz);
		if (horizDist > domeRadius) {
			return false;
		}
		if (pos.y >= center.y) {
			return pos.distanceTo(center) <= domeRadius;
		}
		double groundY = getBarrierGroundY(level, (int) Math.floor(pos.x), domePos.getY(), (int) Math.floor(pos.z));
		return pos.y >= groundY - 1.5D && pos.y <= center.y + 0.5D;
	}

	private static void accelerateEffect(LivingEntity entity, net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect) {
		MobEffectInstance inst = entity.getEffect(effect);
		if (inst != null && inst.getDuration() > 20) {
			entity.removeEffect(effect);
			entity.addEffect(new MobEffectInstance(effect, inst.getDuration() - 20, inst.getAmplifier(), inst.isAmbient(),
					inst.isVisible(), inst.showIcon()));
		}
	}

	@SubscribeEvent
	public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
		if (event.getEntity() instanceof Player player) {
			if (event.getEffectInstance() != null && event.getEffectInstance().is(MobEffects.DARKNESS)) {
				PlayerForceData data = PlayerForceData.get(player);
				if (data != null && data.isImmuneToWardenDarkness()) {
					Entity source = event.getEffectSource();
					boolean isWardenSource = source instanceof Warden || source == null
							|| !player.level().getEntitiesOfClass(Warden.class, player.getBoundingBox().inflate(50.0D)).isEmpty();
					if (isWardenSource) {
						event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();

		// Lightsaber heat & anti-freezing protection
		if (player.getTicksFrozen() > 0) {
			ItemStack main = player.getMainHandItem();
			ItemStack off = player.getOffhandItem();
			boolean activeMain = main.getItem() instanceof ItemLightsaber && main.getOrDefault(ALDataComponents.IS_ACTIVE.get(), false);
			boolean activeOff = off.getItem() instanceof ItemLightsaber && off.getOrDefault(ALDataComponents.IS_ACTIVE.get(), false);
			if (activeMain || activeOff) {
				player.setTicksFrozen(Math.max(0, player.getTicksFrozen() - 8));
				if (player.tickCount % 6 == 0 && !player.level().isClientSide()) {
					((ServerLevel) player.level()).sendParticles(ParticleTypes.SMOKE, player.getX(), player.getY() + 0.6D, player.getZ(), 2, 0.2, 0.2, 0.2, 0.01);
				}
			}
		}

		PlayerForceData data = PlayerForceData.get(player);

		if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
			if (player.getPersistentData().getBoolean("IsFreeMeditation") && player.getPersistentData().getBoolean("IsMeditating")) {
				if (data != null) {
					data.addMeditationTicks(1, serverPlayer);
					data.tickMeditationFpProgress(1, serverPlayer);

					if (player.tickCount % 20 == 0) {
						float xpGain = 2.0F * data.getMeditationXpMultiplier();
						data.addForceXp(xpGain);
					}

					if (player.tickCount % 100 == 0) {
						data.sync(serverPlayer);
					}
				}
			}
		}

		if (data != null && data.isForceSensitive()) {
			if (!player.level().isClientSide) {
				boolean stateChanged = data.regenerateEnergy(player);
				handleChanneledPowers(player, data);

				if (data.isImmuneToWardenDarkness() && player.hasEffect(MobEffects.DARKNESS)) {
					player.removeEffect(MobEffects.DARKNESS);
				}

				if (data.getHungerMultiplier() < 1.0f && player.isSprinting()) {
					if (player.getFoodData().getExhaustionLevel() > 0.05f) {
						player.getFoodData().setExhaustion(player.getFoodData().getExhaustionLevel() * 0.98f);
					}
				}
				if (data.getJediRank() >= 1 && player.getHealth() < player.getMaxHealth()
						&& player.getFoodData().getFoodLevel() >= 18 && player.tickCount % 40 == 0) {
					player.heal(0.5f);
				}

				if (player.tickCount % 20 == 0) {
					String activeChannel = player.getPersistentData().getString("ForceChanneling");
					if (!activeChannel.isEmpty()) {
						ResourceLocation cId = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, activeChannel);
						data.addAmfP(cId, 0.5f);
						data.addAmfI(cId, 0.5f);
					}
					if (data.getTelekineticEntityId() != -1) {
						ResourceLocation tkId = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_telekinesis");
						data.addAmfP(tkId, 0.5f);
						data.addAmfI(tkId, 0.5f);
					}
				}

				if (data.getTelekineticEntityId() != -1) {
					ServerLevel sWorld = (ServerLevel) player.level();
					Entity grabbed = sWorld.getEntity(data.getTelekineticEntityId());

					if (player.isCrouching() || grabbed == null || !grabbed.isAlive()
							|| grabbed.distanceToSqr(player) > 1024.0D) {
						data.clearTelekineticEntity();
						data.triggerPowerCooldown(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_telekinesis"));
						data.sync((ServerPlayer) player);
					} else {
						data.incrementTelekineticHoldTicks();
						boolean isBoss = grabbed instanceof Warden || grabbed instanceof EnderDragon;

						int tkLevel = data.getPowerLevel(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_telekinesis"));
						float baseDrainRate = isBoss ? 0.75F : ((tkLevel >= 3) ? 0.0F : ((tkLevel >= 2) ? 0.0375F : 0.025F));
						float tkCostMult = data.getPowerCostMultiplier(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_telekinesis"));
						float drainRate = baseDrainRate * tkCostMult;
						if (drainRate > 0.0F && !data.consumeForceEnergy(drainRate)) {
							data.clearTelekineticEntity();
							data.triggerPowerCooldown(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_telekinesis"));
							data.sync((ServerPlayer) player);
							sWorld.playSound(null, grabbed.blockPosition(), SoundEvents.SHIELD_BREAK,
									SoundSource.PLAYERS, 1.0F, 0.8F);
							return;
						}

						if (data.getTelekineticMaxTicks() > 0
								&& data.getTelekineticHoldTicks() >= data.getTelekineticMaxTicks()) {
							data.clearTelekineticEntity();
							data.triggerPowerCooldown(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_telekinesis"));
							data.sync((ServerPlayer) player);
							sWorld.playSound(null, grabbed.blockPosition(), SoundEvents.WARDEN_ROAR,
									SoundSource.HOSTILE, 1.2F, 1.0F);
							sWorld.sendParticles(ParticleTypes.SONIC_BOOM, grabbed.getX(), grabbed.getY() + 1.0D,
									grabbed.getZ(), 1, 0, 0, 0, 0);
							return;
						}

						Vec3 eye = player.getEyePosition();
						Vec3 look = player.getLookAngle();
						Vec3 targetPos = eye.add(look.scale(data.getTelekineticDistance())).subtract(0,
								grabbed.getBbHeight() / 2.0D, 0);

						Vec3 delta = targetPos.subtract(grabbed.position());
						double speed = Math.min(delta.length(), 2.2D);
						Vec3 motion = delta.normalize().scale(speed * 0.75D);

						grabbed.setDeltaMovement(motion);
						grabbed.fallDistance = 0.0F;
						grabbed.hurtMarked = true;

						if (grabbed instanceof EnderDragon) {
							grabbed.setPos(targetPos.x, targetPos.y, targetPos.z);
						}

						if (player.tickCount % 2 == 0) {
							boolean isSith = (data.getSithRank() > data.getJediRank());
							sWorld.sendParticles(isSith ? ParticleTypes.SMOKE : ParticleTypes.ELECTRIC_SPARK,
									grabbed.getX() + (sWorld.random.nextDouble() - 0.5D) * grabbed.getBbWidth(),
									grabbed.getY() + sWorld.random.nextDouble() * grabbed.getBbHeight(),
									grabbed.getZ() + (sWorld.random.nextDouble() - 0.5D) * grabbed.getBbWidth(), 2, 0,
									0.02, 0, 0.05);
						}
					}
				}

				boolean isRegenerating = data.getForceEnergy() < data.getMaxForceEnergy();
				boolean isChanneling = !player.getPersistentData().getString("ForceChanneling").isEmpty()
						|| data.getTelekineticEntityId() != -1;
				boolean hasCooldowns = data.hasActiveCooldowns();

				if (stateChanged || (player.tickCount % 5 == 0 && (isRegenerating || isChanneling || hasCooldowns))) {
					data.sync((ServerPlayer) player);
				}

				if (data.getSithRank() >= 1 && player.isSprinting() && player.tickCount % 5 == 0) {
					List<Animal> animals = player.level().getEntitiesOfClass(Animal.class, player.getBoundingBox().inflate(4.0D));
					for (Animal a : animals) {
						Vec3 push = a.position().subtract(player.position()).normalize().scale(0.35D);
						a.setDeltaMovement(push.x, 0.1D, push.z);
					}
				}

				if (player.tickCount % 20 == 0) {
					ServerLevel world = (ServerLevel) player.level();

					if (data.hasLightAura()) {
						List<LivingEntity> allies = world.getEntitiesOfClass(LivingEntity.class,
								player.getBoundingBox().inflate(10.0D));
						for (LivingEntity ally : allies) {
							if (ally == player || ally.isAlliedTo(player)) {
								ally.heal(1.0F);
							}
						}
					}
					if (data.isImmuneToDarkness()) {
						player.removeEffect(MobEffects.BLINDNESS);
						player.removeEffect(MobEffects.DARKNESS);
					}

					if (data.getJediRank() >= 3) {
						player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 40, 0, false, false, false));
					}

					if (data.getJediRank() >= 3 && player.tickCount % 60 == 0) {
						accelerateEffect(player, MobEffects.POISON);
						accelerateEffect(player, MobEffects.CONFUSION);
						accelerateEffect(player, MobEffects.HUNGER);
					}

					if ("GUARD".equals(data.getJediSpecialization())) {
						List<Villager> villagers = world.getEntitiesOfClass(Villager.class, player.getBoundingBox().inflate(16.0D));
						for (Villager v : villagers) {
							if (v.getBrain().hasMemoryValue(net.minecraft.world.entity.ai.memory.MemoryModuleType.HURT_BY_ENTITY)) {
								v.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.HURT_BY_ENTITY);
							}
						}
					}

					if (data.getSithRank() >= 1 && player.hasEffect(MobEffects.HUNGER)) {
						player.removeEffect(MobEffects.HUNGER);
					}

					if (data.getSithRank() >= 2) {
						List<LivingEntity> wounded = world.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(16.0D),
								e -> e != player && e.isAlive() && (e.getHealth() / e.getMaxHealth() <= 0.35f));
						for (LivingEntity w : wounded) {
							w.addEffect(new MobEffectInstance(MobEffects.GLOWING, 30, 0, false, false));
						}
					}

					if (data.getSithRank() >= 2 && world.getMaxLocalRawBrightness(player.blockPosition()) == 0) {
						player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 80, 0, false, false, false));
					}

					if (data.getSithRank() >= 3 && player.getHealth() <= 6.0F) {
						long gameTime = world.getGameTime();
						if (data.canTriggerBloodrush(gameTime)) {
							data.triggerBloodrush(gameTime);
							player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 0));
							player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120, 0));
							world.playSound(null, player.getX(), player.getY(), player.getZ(),
									SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1.0F, 1.2F);
							world.sendParticles(ParticleTypes.ANGRY_VILLAGER, player.getX(), player.getEyeY(), player.getZ(),
									10, 0.4, 0.4, 0.4, 0.1);
						}
					}

					if (data.hasFearAura()) {
						List<Monster> enemies = world.getEntitiesOfClass(Monster.class,
								player.getBoundingBox().inflate(5.0D));
						for (Monster monster : enemies) {
							monster.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, false, false));
						}
					}

					if (data.getSithRank() >= 6) {
						List<Monster> fearful = world.getEntitiesOfClass(Monster.class,
								player.getBoundingBox().inflate(12.0D));
						for (Monster m : fearful) {
							Vec3 flee = m.position().subtract(player.position()).normalize();
							m.getNavigation().moveTo(m.getX() + flee.x * 6, m.getY(), m.getZ() + flee.z * 6, 1.35D);
							if (m.horizontalCollision) {
								m.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3, false, false));
								m.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 2, false, false));
							}
						}
					}

					if (data.hasUndeadRepellingAura()) {
						List<LivingEntity> nearby = world.getEntitiesOfClass(LivingEntity.class,
								player.getBoundingBox().inflate(6.0D));
						for (LivingEntity living : nearby) {
							if (living.isInvertedHealAndHarm() && living instanceof Mob mob) {
								Vec3 pushDir = mob.position().subtract(player.position()).normalize().scale(0.4D);
								mob.setDeltaMovement(pushDir.x, 0.1, pushDir.z);
							}
						}
					}
				}
			}
		}

		if (player.tickCount % 2 != 0)
			return;

		AttributeInstance armorAttr = player.getAttribute(Attributes.ARMOR);
		AttributeInstance playerKbAttr = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
		AttributeInstance moveSpeedAttr = player.getAttribute(Attributes.MOVEMENT_SPEED);
		AttributeInstance atkSpeedAttr = player.getAttribute(Attributes.ATTACK_SPEED);

		if (data != null && data.isForceSensitive()) {
			if (armorAttr != null)
				applyModifier(armorAttr, PASSIVE_ARMOR_ID, data.getPassiveArmorBonus(),
						AttributeModifier.Operation.ADD_VALUE);
			if (playerKbAttr != null)
				applyModifier(playerKbAttr, PASSIVE_KB_ID, data.getPassiveKnockbackResistanceBonus(),
						AttributeModifier.Operation.ADD_VALUE);
			if (moveSpeedAttr != null)
				applyModifier(moveSpeedAttr, PASSIVE_SPEED_ID, data.getPassiveSpeedBonus(),
						AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
			if (atkSpeedAttr != null) {
				if (data.getSithRank() >= 2 && player.level().getMaxLocalRawBrightness(player.blockPosition()) == 0) {
					applyModifier(atkSpeedAttr, SITH_DARK_SPEED_ID, 0.10D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
				} else {
					atkSpeedAttr.removeModifier(SITH_DARK_SPEED_ID);
				}
			}
		} else {
			if (armorAttr != null)
				armorAttr.removeModifier(PASSIVE_ARMOR_ID);
			if (playerKbAttr != null)
				playerKbAttr.removeModifier(PASSIVE_KB_ID);
			if (moveSpeedAttr != null)
				moveSpeedAttr.removeModifier(PASSIVE_SPEED_ID);
			if (atkSpeedAttr != null)
				atkSpeedAttr.removeModifier(SITH_DARK_SPEED_ID);
		}

		ItemStack handItem = player.getMainHandItem();
		AttributeInstance rangeAttr = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
		AttributeInstance dmgAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
		AttributeInstance speedAttr = player.getAttribute(Attributes.ATTACK_SPEED);
		AttributeInstance kbAttr = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);

		if (rangeAttr == null || dmgAttr == null || speedAttr == null || kbAttr == null)
			return;

		if (handItem.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(handItem)) {
			if (data == null)
				return;

			float sbMult = getSbMult(handItem);
			float dbMult = getDbMult(handItem);

			double bonusRange = 0.0;
			double bonusDamage = 0.0;
			double bonusSpeed = 0.0;
			double bonusKbRes = 0.0;

			if (hasSkill(data, "ls_kyber_resonance"))
				bonusDamage += 1.0;
			if (hasSkill(data, "ls_lethal_precision"))
				bonusDamage += 3.0;

			if (sbMult > 0) {
				if (hasSkill(data, "sb_better_form")) {
					bonusRange += (1.75 * sbMult);
					bonusKbRes += (0.15 * sbMult);
				}
				if (hasSkill(data, "sb_adv_form")) {
					bonusRange += (0.5 * sbMult);
					bonusDamage += (1.0 * sbMult);
					bonusKbRes += (0.15 * sbMult);
				}
				if (hasSkill(data, "sb_adv_speed"))
					bonusSpeed += (1.4 * sbMult);
				if (hasSkill(data, "sb_ult_speed"))
					bonusSpeed += (100.0 * sbMult);
			}

			if (dbMult > 0) {
				if (hasSkill(data, "db_better_form")) {
					bonusRange += (1.75 * dbMult);
					bonusKbRes += (0.15 * dbMult);
				}
				if (hasSkill(data, "db_adv_form")) {
					bonusRange += (0.5 * dbMult);
					bonusDamage += (1.0 * dbMult);
					bonusKbRes += (0.15 * dbMult);
				}
			}

			if (hasSkill(data, "ls_focusing")) {
				LightsaberData lsData = handItem.get(ALDataComponents.LIGHTSABER_DATA.get());
				if (lsData != null) {
					for (Item crystalItem : getAllFocusCrystals(lsData)) {
						String crystalStr = BuiltInRegistries.ITEM.getKey(crystalItem).getPath().toLowerCase();

						if (crystalStr.contains("compressed") || crystalStr.contains("fine_cut"))
							bonusDamage += 1.0;
						else if (crystalStr.contains("cracked") || crystalStr.contains("shoto"))
							bonusDamage += 2.0;
						else if (crystalStr.contains("extended"))
							bonusDamage -= 2.0;
						else if (crystalStr.contains("unstable") || crystalStr.contains("pulse"))
							bonusDamage += 4.0;

						if (crystalStr.contains("shoto"))
							bonusRange -= 2.0;
						if (crystalStr.contains("extended"))
							bonusRange += 2.0;
					}
				}
			}

			applyModifier(rangeAttr, RANGE_MOD_ID, bonusRange, AttributeModifier.Operation.ADD_VALUE);
			applyModifier(dmgAttr, DMG_MOD_ID, bonusDamage, AttributeModifier.Operation.ADD_VALUE);
			applyModifier(speedAttr, SPEED_MOD_ID, bonusSpeed, AttributeModifier.Operation.ADD_VALUE);
			applyModifier(kbAttr, KB_MOD_ID, bonusKbRes, AttributeModifier.Operation.ADD_VALUE);

		} else {
			rangeAttr.removeModifier(RANGE_MOD_ID);
			dmgAttr.removeModifier(DMG_MOD_ID);
			speedAttr.removeModifier(SPEED_MOD_ID);
			kbAttr.removeModifier(KB_MOD_ID);
		}
	}

	private static void applyModifier(AttributeInstance instance, ResourceLocation id, double value,
			AttributeModifier.Operation op) {
		instance.removeModifier(id);
		if (value != 0)
			instance.addTransientModifier(new AttributeModifier(id, value, op));
	}

	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		LivingEntity victim = event.getEntity();

		if (event.getSource().getEntity() instanceof Player attacker) {
			attacker.getPersistentData().putLong("LastCombatTime", attacker.level().getGameTime());
		}

		if (event.getEntity() instanceof Player p) {
			if (p.getPersistentData().contains("ActiveHolocronPos")) {
				event.setCanceled(true);
				return;
			}
			if (p.getPersistentData().getBoolean("IsFreeMeditation") || p.getPersistentData().getBoolean("IsMeditating")) {
				p.getPersistentData().remove("IsMeditating");
				p.getPersistentData().remove("IsFreeMeditation");
				if (p instanceof ServerPlayer sp) {
					PacketDistributor.sendToPlayer(sp, new MeditationTogglePayload(false, BlockPos.ZERO, false));
					PacketDistributor.sendToPlayersTrackingEntityAndSelf(sp, new SyncMeditationPayload(sp.getId(), false, false));
				}
			}
			p.getPersistentData().putLong("LastCombatTime", p.level().getGameTime());
			if (event.getSource().is(DamageTypeTags.IS_FREEZING)) {
				ItemStack main = p.getMainHandItem();
				ItemStack off = p.getOffhandItem();
				boolean activeMain = main.getItem() instanceof ItemLightsaber && main.getOrDefault(ALDataComponents.IS_ACTIVE.get(), false);
				boolean activeOff = off.getItem() instanceof ItemLightsaber && off.getOrDefault(ALDataComponents.IS_ACTIVE.get(), false);
				if (activeMain || activeOff) {
					event.setCanceled(true);
					p.setTicksFrozen(0);
					return;
				}
			}
		}

		if (victim instanceof LivingEntity livingVictim && !livingVictim.level().isClientSide()) {
			ServerLevel sWorld = (ServerLevel) livingVictim.level();

			for (ServerPlayer caster : sWorld.players()) {
				String chan = caster.getPersistentData().getString("ForceChanneling");
				if ("force_barrier".equals(chan) || "force_fortify".equals(chan)) {
					int bLevel = caster.getPersistentData().getInt("BarrierLevel");
					if (bLevel <= 0)
						bLevel = caster.getPersistentData().getInt("FortifyLevel");
					if (bLevel <= 0)
						bLevel = 1;

					boolean isProtected = false;
					if (bLevel >= 3) {
						BlockPos domePos = BlockPos.of(caster.getPersistentData().getLong("DomeCenter"));
						PlayerForceData cData = PlayerForceData.get(caster);
						double domeRadius = ALForceAbilitiesConfig.BARRIER_RADIUS_T3.get()
								* (cData != null ? cData.getRangeMultiplier(ResourceLocation
										.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_barrier")) : 1.0F);
						if (isInsideBarrierTier3(livingVictim.position(), domePos, domeRadius, sWorld)) {
							if (livingVictim == caster || livingVictim.isAlliedTo(caster)
									|| !(livingVictim instanceof Monster)) {
								isProtected = true;
							}
						}
					} else {
						int targetId = caster.getPersistentData().getInt("FortifyTargetId");
						if (targetId == livingVictim.getId()) {
							isProtected = true;
						}
					}

					if (isProtected) {
						PlayerForceData cData = PlayerForceData.get(caster);
						if (cData == null)
							continue;

						float maxCapacity = (bLevel >= 3)
								? ALForceAbilitiesConfig.BARRIER_CAPACITY_T3.get().floatValue()
								: ((bLevel == 2)
										? ALForceAbilitiesConfig.BARRIER_CAPACITY_T2.get().floatValue()
										: ALForceAbilitiesConfig.BARRIER_CAPACITY_T1.get().floatValue());

						float energyPerDmg = (bLevel >= 3)
								? ALForceAbilitiesConfig.BARRIER_ENERGY_PER_DAMAGE_T3.get().floatValue()
								: ((bLevel == 2)
										? ALForceAbilitiesConfig.BARRIER_ENERGY_PER_DAMAGE_T2.get().floatValue()
										: ALForceAbilitiesConfig.BARRIER_ENERGY_PER_DAMAGE_T1.get().floatValue());

						float costMult = cData.getPowerCostMultiplier(
								ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_barrier"));
						float currentAbsorbed = caster.getPersistentData().getFloat("BarrierAbsorbedDmg");
						float incomingDmg = event.getAmount();
						float remainingCap = Math.max(0.0F, maxCapacity - currentAbsorbed);

						if (incomingDmg <= remainingCap) {
							event.setCanceled(true);
							float energyCost = incomingDmg * energyPerDmg * costMult;
							caster.getPersistentData().putFloat("BarrierAbsorbedDmg", currentAbsorbed + incomingDmg);

							sWorld.playSound(null, livingVictim.getX(), livingVictim.getY(), livingVictim.getZ(),
									SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
							sWorld.sendParticles(ParticleTypes.ELECTRIC_SPARK, livingVictim.getX(),
									livingVictim.getY() + 1.0D, livingVictim.getZ(), 10, 0.2, 0.2, 0.2, 0.1);

							if (!cData.consumeForceEnergy(energyCost)) {
								cData.setForceEnergy(0.0F);
								shatterShield(caster);
							} else if (currentAbsorbed + incomingDmg >= maxCapacity) {
								shatterShield(caster);
							} else {
								cData.sync(caster);
							}
						} else {
							// Damage exceeds remaining capacity: absorb what's left, remainder punches through, shield shatters
							float absorbedPortion = remainingCap;
							float residualDmg = incomingDmg - absorbedPortion;
							event.setAmount(residualDmg);

							float energyCost = absorbedPortion * energyPerDmg * costMult;
							cData.consumeForceEnergy(energyCost);
							shatterShield(caster);
						}
						return;
					}
				}
			}
		}

		if (victim instanceof Player hitPlayer) {
			PlayerForceData hitData = PlayerForceData.get(hitPlayer);
			if (hitData != null) {
				float prevHitEnergy = hitData.getForceEnergy();
				hitData.onTakeDamageConvertAnger(event.getAmount());
				if (hitData.getForceEnergy() != prevHitEnergy && hitPlayer instanceof ServerPlayer sp) {
					hitData.sync(sp);
				}

				// Neutral Rank 3: Passive arrow deflection chance while sprinting (15%)
				if (hitData.getForceRank() >= 3 && hitPlayer.isSprinting()
						&& event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
					if (hitPlayer.getRandom().nextFloat() < 0.15F) {
						event.setCanceled(true);
						hitPlayer.level().playSound(null, hitPlayer.blockPosition(), ALSounds.LIGHTSABER_HIT.get(),
								SoundSource.PLAYERS, 0.9F, 1.4F);
						if (hitPlayer.level() instanceof ServerLevel sl) {
							sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, hitPlayer.getX(), hitPlayer.getEyeY(),
									hitPlayer.getZ(), 8, 0.2, 0.2, 0.2, 0.1);
						}
						if (event.getSource().getDirectEntity() instanceof Projectile projectile) {
							projectile.discard();
						}
						return;
					}
				}

				if (hitPlayer.getHealth() - event.getAmount() <= 0.0F
						&& hitData.canTriggerDeathProtection(hitPlayer.level().getGameTime())) {
					event.setCanceled(true);
					hitPlayer.setHealth(4.0F);
					hitPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1));
					hitPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
					hitData.triggerDeathProtection(hitPlayer.level().getGameTime());
					hitPlayer.level().playSound(null, hitPlayer.blockPosition(), SoundEvents.TOTEM_USE,
							SoundSource.PLAYERS, 1.0F, 1.2F);
					if (hitPlayer.level() instanceof ServerLevel sl) {
						sl.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, hitPlayer.getX(), hitPlayer.getEyeY(),
								hitPlayer.getZ(), 30, 0.5, 0.5, 0.5, 0.2);
					}
					hitData.sync((ServerPlayer) hitPlayer);
					return;
				}
			}
		}

		if (event.getSource().getEntity() instanceof Player attacker) {
			PlayerForceData attackerData = PlayerForceData.get(attacker);
			if (attackerData != null) {
				float healthPercent = victim.getHealth() / victim.getMaxHealth();
				float executeMult = attackerData.getExecuteDamageMultiplier(healthPercent);
				float sneakMult = attacker.isCrouching() ? attackerData.getSneakCritMultiplier() : 1.0f;
				event.setAmount(event.getAmount() * executeMult * sneakMult);
			}
		}

		if (victim instanceof Player defender && defender.isUsingItem()) {
			ItemStack useItem = defender.getUseItem();
			if (useItem.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(useItem)) {
				LightsaberData lsData = useItem.get(ALDataComponents.LIGHTSABER_DATA.get());
				PlayerForceData forceData = PlayerForceData.get(defender);

				if (lsData != null && forceData != null) {
					float projBlockChance = 0.0f;
					float meleeBlockChance = 0.0f;

					if (hasSkill(forceData, "ls_block")) {
						projBlockChance = 0.35f;
						meleeBlockChance = 0.50f;
					}
					if (hasSkill(forceData, "ls_better_block")) {
						projBlockChance = 0.50f;
						meleeBlockChance = 0.70f;
					}
					if (hasSkill(forceData, "ls_adv_block")) {
						projBlockChance = 0.70f;
						meleeBlockChance = 0.80f;
					}

					boolean isEffectivelyDouble = lsData.isDoubleBladed() && !ItemLightsaber.isSingleBladeMode(useItem);
					if (isEffectivelyDouble) {
						projBlockChance += 0.20f;
						if (hasSkill(forceData, "db_ult_block")) {
							projBlockChance = 1.0f;
							meleeBlockChance = 0.90f;
						}
					}

					if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
						if (projBlockChance > 0 && Math.random() <= projBlockChance) {
							event.setCanceled(true);
							defender.level().playSound(null, defender.blockPosition(), ALSounds.LIGHTSABER_HIT.get(),
									SoundSource.PLAYERS, 1.0F, 1.4F + (float) (Math.random() * 0.2F));
							if (defender.level() instanceof ServerLevel sl)
								sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, defender.getX(), defender.getEyeY(),
										defender.getZ(), 12, 0.2, 0.2, 0.2, 0.1);
							if (event.getSource().getDirectEntity() instanceof Projectile projectile)
								projectile.discard();
						}
					} else if (event.getSource().getEntity() instanceof LivingEntity attacker) {
						if (meleeBlockChance > 0 && Math.random() <= meleeBlockChance) {
							event.setCanceled(true);
							defender.level().playSound(null, defender.blockPosition(), ALSounds.LIGHTSABER_HIT.get(),
									SoundSource.PLAYERS, 1.0F, 1.2F + (float) (Math.random() * 0.2F));
							ItemStack attackerWeapon = attacker.getMainHandItem();
							if (attackerWeapon.getItem() instanceof ItemLightsaber
									&& ItemLightsaber.isActive(attackerWeapon)) {
								if (defender.level() instanceof ServerLevel sl)
									sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, defender.getX(), defender.getEyeY(),
											defender.getZ(), 16, 0.2, 0.2, 0.2, 0.1);
							} else if (!attackerWeapon.isEmpty() && attackerWeapon.isDamageableItem()) {
								attackerWeapon.hurtAndBreak(25, attacker, EquipmentSlot.MAINHAND);
								if (defender.level() instanceof ServerLevel sl)
									sl.sendParticles(ParticleTypes.LAVA, attacker.getX(), attacker.getEyeY(),
											attacker.getZ(), 6, 0.2, 0.2, 0.2, 0.1);
							}
						}
					}
				}
			}
		}
	}

	public static double getSweepRadius(PlayerForceData forceData, ItemStack stack, boolean isEffectivelyDouble) {
		double inflateRadius = isEffectivelyDouble ? 3.0D : 1.5D;
		if (forceData != null) {
			float sbMult = getSbMult(stack);
			float dbMult = getDbMult(stack);

			float bestSbBonus = 0.0f;
			if (hasSkill(forceData, "sb_better_swerve"))
				bestSbBonus = 1.0F;
			if (hasSkill(forceData, "sb_adv_swerve"))
				bestSbBonus = 3.0F;

			float bestDbBonus = 0.0f;
			if (hasSkill(forceData, "db_adv_swerve"))
				bestDbBonus = 3.0F;

			inflateRadius += (bestSbBonus * sbMult) + (bestDbBonus * dbMult);
		}
		return inflateRadius;
	}

	public static float getSweepDamage(PlayerForceData forceData, ItemStack stack, float weaponTotalDmg,
			boolean isEffectivelyDouble) {
		float sweepMultiplier = 1.0F;
		if (forceData != null) {
			float sbMult = getSbMult(stack);
			float dbMult = getDbMult(stack);

			float bestSbBonus = 0.0f;
			if (hasSkill(forceData, "sb_better_swerve"))
				bestSbBonus = 0.40F;
			if (hasSkill(forceData, "sb_adv_swerve"))
				bestSbBonus = 0.75F;

			float bestDbBonus = 0.0f;
			if (hasSkill(forceData, "db_adv_swerve"))
				bestDbBonus = 0.75F;

			sweepMultiplier += (bestSbBonus * sbMult) + (bestDbBonus * dbMult);
		}
		float damageFactor = isEffectivelyDouble ? 0.50F : 0.80F;
		return (1.0F + weaponTotalDmg * damageFactor) * sweepMultiplier;
	}

	public static double getSweepMaxDistSqr(boolean isEffectivelyDouble) {
		return isEffectivelyDouble ? 16.0D : 6.25D;
	}

	@SubscribeEvent
	public static void onPlayerAttack(AttackEntityEvent event) {
		Player player = event.getEntity();
		player.getPersistentData().putLong("LastCombatTime", player.level().getGameTime());
		ItemStack stack = player.getMainHandItem();

		if (!(stack.getItem() instanceof ItemLightsaber) || !ItemLightsaber.isActive(stack))
			return;

		LightsaberData lsData = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
		if (lsData == null)
			return;

		PlayerForceData forceData = PlayerForceData.get(player);
		if (event.getTarget() instanceof LivingEntity target) {

			float weaponTotalDmg = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
			boolean isSweep = player.getAttackStrengthScale(0.5F) > 0.9F && player.onGround() && !player.isSprinting()
					&& player.walkDist == player.walkDistO;
			boolean isEffectivelyDouble = lsData.isDoubleBladed() && !ItemLightsaber.isSingleBladeMode(stack);

			if (isSweep) {
				double inflateRadius = getSweepRadius(forceData, stack, isEffectivelyDouble);
				float sweepDamage = getSweepDamage(forceData, stack, weaponTotalDmg, isEffectivelyDouble);
				double maxDistSqr = getSweepMaxDistSqr(isEffectivelyDouble);

				AABB sweepBox = target.getBoundingBox().inflate(inflateRadius, 0.5D, inflateRadius);
				List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, sweepBox);

				for (LivingEntity nearby : targets) {
					if (nearby != player && nearby != target && !player.isAlliedTo(nearby)
							&& !(nearby instanceof ArmorStand)) {
						if (player.distanceToSqr(nearby) < maxDistSqr) {
							nearby.invulnerableTime = 0;
							nearby.knockback(0.4F, Mth.sin(player.getYRot() * ((float) Math.PI / 180F)),
									-Mth.cos(player.getYRot() * ((float) Math.PI / 180F)));
							nearby.hurt(player.damageSources().playerAttack(player), sweepDamage);
						}
					}
				}

				if (player.level() instanceof ServerLevel serverLevel) {
					double px = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
					double pz = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
					double waveOffset = isEffectivelyDouble ? 1.5D : 1.0D;
					serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + px * waveOffset,
							player.getY(0.5D), player.getZ() + pz * waveOffset, isEffectivelyDouble ? 3 : 1, 0.3, 0.1,
							0.3, 0.0);
				}
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, isEffectivelyDouble ? 0.85F : 1.0F);
			}

			if (hasSkill(forceData, "ls_focusing")) {
				boolean isUndead = target.isInvertedHealAndHarm();

				for (Item crystalItem : getAllFocusCrystals(lsData)) {
					String crystalStr = BuiltInRegistries.ITEM.getKey(crystalItem).getPath().toLowerCase();

					if (crystalStr.contains("prismatic")) {
						if (isUndead) {
							target.invulnerableTime = 0;
							target.hurt(player.damageSources().magic(), 4.0f);
						}
					} else if (crystalStr.contains("invert")) {
						if (isUndead)
							doAoEDamage(player.level(), player, target, 3.0, weaponTotalDmg * 0.40f);
					} else if (crystalStr.contains("fine_cut")) {
						target.invulnerableTime = 0;
						target.hurt(player.damageSources().magic(), 3.0f);
					} else if (crystalStr.contains("unstable")) {
						doAoEDamage(player.level(), player, target, 3.0, weaponTotalDmg * 0.35f);
						if (player.level() instanceof ServerLevel sl) {
							sl.sendParticles(ParticleTypes.LAVA, target.getX(), target.getY() + 1, target.getZ(), 10,
									0.5, 0.5, 0.5, 0.1);
						}
						if (player.getRandom().nextFloat() < 0.10f) {
							ItemLightsaber.setBladeActive(stack, false);
							player.level().playSound(null, player.blockPosition(), ALSounds.LIGHTSABER_OFF.get(),
									SoundSource.PLAYERS, 1.0F, 1.0F);
							if (player.level() instanceof ServerLevel sl)
								sl.sendParticles(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY() + 1,
										player.getZ(), 15, 0.5, 0.5, 0.5, 0.05);
							player.displayClientMessage(
									Component.translatable("message.lightsabersfork.unstable_overheat")
											.withStyle(ChatFormatting.RED),
									true);
						}
					} else if (crystalStr.contains("pulse")) {
						List<LivingEntity> chainTargets = new ArrayList<>();
						LivingEntity currentChainSource = target;

						for (int bounce = 0; bounce < 5; bounce++) {
							List<LivingEntity> possibleTargets = player.level().getEntitiesOfClass(LivingEntity.class,
									currentChainSource.getBoundingBox().inflate(3.0D));

							final LivingEntity finalSource = currentChainSource;
							possibleTargets.sort(Comparator.comparingDouble(e -> e.distanceToSqr(finalSource)));

							boolean foundNext = false;
							for (LivingEntity possible : possibleTargets) {
								if (possible != player && possible != target && !chainTargets.contains(possible)) {
									chainTargets.add(possible);
									currentChainSource = possible;
									foundNext = true;

									possible.invulnerableTime = 0;
									possible.hurt(player.damageSources().lightningBolt(), weaponTotalDmg * 0.35f);
									if (player.level() instanceof ServerLevel sl) {
										sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, possible.getX(),
												possible.getY() + 1, possible.getZ(), 5, 0.2, 0.2, 0.2, 0.1);
									}
									break;
								}
							}
							if (!foundNext)
								break;
						}

						if (player.getRandom().nextFloat() < 0.15f) {
							player.invulnerableTime = 0;
							player.hurt(player.damageSources().magic(), weaponTotalDmg * 0.50f);
						}
					}
				}
			}
		}
	}

	private static void doAoEDamage(net.minecraft.world.level.Level level, Player attacker, LivingEntity center,
			double radius, float damage) {
		List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class,
				center.getBoundingBox().inflate(radius));
		for (LivingEntity aoeTarget : nearby) {
			if (aoeTarget != attacker && aoeTarget != center) {
				aoeTarget.invulnerableTime = 0;
				aoeTarget.hurt(attacker.damageSources().playerAttack(attacker), damage);
			}
		}
	}

	@SubscribeEvent
	public static void onAttackArmorStand(AttackEntityEvent event) {
		Player player = event.getEntity();
		ItemStack stack = player.getMainHandItem();
		if (!(stack.getItem() instanceof ItemLightsaber) || !ItemLightsaber.isActive(stack))
			return;

		if (event.getTarget() instanceof net.minecraft.world.entity.decoration.ArmorStand stand) {
			event.setCanceled(true);

			if (player instanceof ServerPlayer sp) {
				sp.serverLevel().playSound(null, stand.getX(), stand.getY(), stand.getZ(),
						ALSounds.LIGHTSABER_HIT.get(), SoundSource.PLAYERS, 0.9F, 1.1F);
				sp.serverLevel().sendParticles(ParticleTypes.ELECTRIC_SPARK, stand.getX(), stand.getY() + 1.0D,
						stand.getZ(), 8, 0.15, 0.15, 0.15, 0.1);

				long now = player.level().getGameTime();
				long lastTrain = player.getPersistentData().getLong("LastSaberTrainTick");
				if (now - lastTrain >= 30) {
					player.getPersistentData().putLong("LastSaberTrainTick", now);
					PlayerForceData data = PlayerForceData.get(sp);
					if (data != null) {
						data.recordSaberTraining(sp, 1);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onShieldBlock(LivingShieldBlockEvent event) {
		if (event.getDamageSource().getEntity() instanceof LivingEntity attacker) {
			ItemStack weapon = attacker.getMainHandItem();

			if (weapon.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(weapon)) {
				ItemStack shield = event.getEntity().getUseItem();

				if (!shield.isEmpty() && shield.isDamageableItem()) {
					EquipmentSlot slot = event.getEntity()
							.getUsedItemHand() == net.minecraft.world.InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND
									: EquipmentSlot.OFFHAND;

					float baseDamage = event.getBlockedDamage();
					int extraDamage = Math.max(5, (int) (baseDamage * 4.0F));

					shield.hurtAndBreak(extraDamage, event.getEntity(), slot);

					event.getEntity().level().playSound(null, event.getEntity().blockPosition(),
							ALSounds.LIGHTSABER_HIT.get(), SoundSource.PLAYERS, 1.0F, 0.8F);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityKilled(LivingDeathEvent event) {
		if (event.getSource().getEntity() instanceof ServerPlayer player) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null) {
				if (data.hasWrathOnKill()) {
					player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 80, 0));
					player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 0));
				}

				if (data.getSithRank() >= 5) {
					player.getFoodData().eat(4, 0.6F);
					player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1));
				}

				if (data.hasDeathExplosion() && !player.getPersistentData().getString("ForceChanneling").isEmpty()) {
					LivingEntity victim = event.getEntity();
					ServerLevel world = player.serverLevel();
					world.sendParticles(ParticleTypes.SONIC_BOOM, victim.getX(), victim.getY() + 1.0, victim.getZ(), 1,
							0, 0, 0, 0);
					world.playSound(null, victim.getX(), victim.getY(), victim.getZ(),
							SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.0F, 1.2F);
					doAoEDamage(world, player, victim, 4.0D, 6.0F);
				}

				if (player.getMainHandItem().getItem() instanceof ItemLightsaber) {
					boolean leveledUp = data.addLightsaberKill();
					if (leveledUp) {
						player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
					}
				}
				data.sync(player);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null) {
				if (com.fiskmods.pika.lightsabers.advancements.ALAdvancements.hasAdvancement(player, "chosen_one")) {
					data.setHasChosenOne(true);
				}
				data.sync(player);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null)
				data.sync(player);
		}
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		Entity entity = event.getEntity();
		if (entity.level().isClientSide())
			return;

		int stasisTicks = entity.getPersistentData().getInt("ForceStasisTicks");
		if (stasisTicks > 0) {
			entity.getPersistentData().putInt("ForceStasisTicks", stasisTicks - 1);

			double sx = entity.getPersistentData().getDouble("ForceStasisX");
			double sy = entity.getPersistentData().getDouble("ForceStasisY");
			double sz = entity.getPersistentData().getDouble("ForceStasisZ");
			float yRot = entity.getPersistentData().getFloat("ForceStasisYRot");
			float xRot = entity.getPersistentData().getFloat("ForceStasisXRot");

			if (entity instanceof ServerPlayer sp) {
				sp.teleportTo(sx, sy, sz);
				sp.setDeltaMovement(Vec3.ZERO);
				sp.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(sp));
			} else {
				entity.setPos(sx, sy, sz);
				entity.setYRot(yRot);
				entity.setXRot(xRot);
				entity.setDeltaMovement(Vec3.ZERO);
				entity.hurtMarked = true;
			}

			if (entity instanceof LivingEntity living) {
				living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 127, false, false));
				living.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 10, 10, false, false));
				living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10, 10, false, false));
			}

			if (entity.level() instanceof ServerLevel sl && entity.tickCount % 4 == 0) {
				sl.sendParticles(ParticleTypes.INSTANT_EFFECT, sx, sy + entity.getBbHeight() * 0.5D, sz, 3,
						entity.getBbWidth() * 0.4D, entity.getBbHeight() * 0.4D, entity.getBbWidth() * 0.4D, 0.02D);
			}

			if (stasisTicks - 1 <= 0) {
				entity.getPersistentData().remove("ForceStasisTicks");
				entity.getPersistentData().remove("ForceStasisX");
				entity.getPersistentData().remove("ForceStasisY");
				entity.getPersistentData().remove("ForceStasisZ");
				entity.getPersistentData().remove("ForceStasisYRot");
				entity.getPersistentData().remove("ForceStasisXRot");
			}
		}
	}

	@SubscribeEvent
	public static void onLivingFall(LivingFallEvent event) {
		if (event.getEntity() instanceof AbstractHorse horse) {
			if (horse.isVehicle() && horse.getFirstPassenger() instanceof Player rider) {
				PlayerForceData rData = PlayerForceData.get(rider);
				if (rData != null && rData.getJediRank() >= 3) {
					event.setDistance(0.0F);
					event.setCanceled(true);
					return;
				}
			}
		}

		if (event.getEntity() instanceof Player player) {
			PlayerForceData data = PlayerForceData.get(player);

			if (player.getPersistentData().getBoolean("ForceJumpNoFall")) {
				event.setDistance(0.0F);
				event.setCanceled(true);
				player.getPersistentData().putBoolean("ForceJumpNoFall", false);
				return;
			}

			if (data != null && data.isForceSensitive()) {
				float mult = data.getFallDamageMultiplier();
				if (mult <= 0.0f) {
					event.setDistance(0.0F);
					event.setCanceled(true);
				} else if (mult < 1.0f) {
					event.setDamageMultiplier(mult);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
		if (event.getEntity() instanceof Player player) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null && data.getJediRank() >= 1) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onCropBreak(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		if (player != null && !player.level().isClientSide()) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null && data.getJediRank() >= 1) {
				BlockState state = event.getState();
				if (state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state)) {
					if (player.getRandom().nextFloat() < 0.20F && event.getLevel() instanceof ServerLevel sl) {
						List<ItemStack> drops = Block.getDrops(state, sl, event.getPos(), null, player, player.getMainHandItem());
						for (ItemStack drop : drops) {
							Block.popResource(sl, event.getPos(), drop.copy());
						}
						sl.sendParticles(ParticleTypes.HAPPY_VILLAGER, event.getPos().getX() + 0.5D,
								event.getPos().getY() + 0.5D, event.getPos().getZ() + 0.5D, 6, 0.3, 0.3, 0.3, 0.05);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		Player player = event.getPlayer();
		if (player != null) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null && "LIBRARIAN".equals(data.getJediSpecialization())) {
				long cost = event.getCost();
				if (cost > 0) {
					event.setCost(Math.max(1L, Math.round(cost * 0.60F)));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onXpPickup(PlayerXpEvent.PickupXp event) {
		Player player = event.getEntity();
		if (player instanceof ServerPlayer sp) {
			PlayerForceData data = PlayerForceData.get(sp);
			if (data != null && "LIBRARIAN".equals(data.getJediSpecialization())) {
				data.addForceEnergy(0.5F);
				data.sync(sp);
			}
		}
	}

	@SubscribeEvent
	public static void onContainerInteract(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		if (player instanceof ServerPlayer sp && event.getLevel() instanceof ServerLevel sl) {
			PlayerForceData data = PlayerForceData.get(sp);
			if (data != null && "LIBRARIAN".equals(data.getJediSpecialization())) {
				BlockPos pos = event.getPos();
				BlockState state = sl.getBlockState(pos);
				if (state.getBlock() instanceof ChestBlock || state.getBlock() instanceof BarrelBlock || sl.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity) {
					BlockEntity be = sl.getBlockEntity(pos);
					if (be != null && !be.getPersistentData().getBoolean("AL_ArchivistLootChecked")) {
						be.getPersistentData().putBoolean("AL_ArchivistLootChecked", true);
						if (sp.getRandom().nextFloat() < 0.25F) {
							Item kyber = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "kyber_crystal"));
							if (kyber != null) {
								Block.popResource(sl, pos.above(), new ItemStack(kyber));
								sl.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.5F);
								sl.sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5D, pos.getY() + 1.2D,
										pos.getZ() + 0.5D, 15, 0.3, 0.3, 0.3, 0.1);
								sp.displayClientMessage(
										Component.translatable("message.lightsabersfork.archivist_bonus_loot")
												.withStyle(ChatFormatting.AQUA),
										true);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onVanillaGameEvent(VanillaGameEvent event) {
		if (event.getCause() instanceof Player player) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null && "ASSASSIN".equals(data.getSithSpecialization())) {
				if (event.getVanillaEvent().is(GameEvent.STEP) || event.getVanillaEvent().is(GameEvent.HIT_GROUND)) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			ItemStack crafted = event.getCrafting();
			if (crafted.getItem() instanceof ItemLightsaber) {
				LightsaberData lsData = crafted.get(ALDataComponents.LIGHTSABER_DATA.get());
				if (lsData != null && lsData.isDoubleBladed()) {
					com.fiskmods.pika.lightsabers.advancements.ALAdvancements.grant(player, "double_saber");
				}
			}
		}
	}
}