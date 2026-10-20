package com.fiskmods.pika.lightsabers.force.skilltree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fiskmods.pika.lightsabers.ALAttachmentTypes;
import com.fiskmods.pika.lightsabers.ALConfig;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.network.payload.SyncForceDataPayload;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlayerForceData implements INBTSerializable<CompoundTag> {

	public static final int MASTERY_TIER_1_TICKS = 6000;
	public static final int MASTERY_TIER_2_TICKS = 18000;
	public static final int MASTERY_TIER_3_TICKS = 42000;
	public static final int MASTERY_MAX_TICKS = 72000;

	public static final int TIER_2_USAGE_REQ = 150;
	public static final int TIER_3_USAGE_REQ = 400;

	private final Set<ResourceLocation> unlockedSkills = new HashSet<>();

	private boolean forceSensitivity = false;
	private int forceRank = 0;
	private int jediRank = 0;
	private int sithRank = 0;
	private String jediSpecialization = "NONE";
	private String sithSpecialization = "NONE";

	private int forcePoints = 0;
	private float forceXp = 0.0f;
	private static final float FORCE_XP_PER_POINT = 2000.0f;
	private final Map<ResourceLocation, Integer> powerCooldowns = new HashMap<>();

	private boolean craftedFirstLightsaber = false;
	private int meditationFpEarned = 0;
	private int meditationFpProgressTicks = 0;
	private int totalPowerUses = 0;
	private int nextPowerUseThreshold = 25;
	private int powerUseStep = 25;

	private float forceEnergy = 0.0f;
	private ResourceLocation selectedPower = null;
	private int meditationTicks = 0;
	private int meditationBonusEnergy = 0;

	private int telekineticEntityId = -1;
	private float telekineticDistance = 5.0F;
	private int telekineticHoldTicks = 0;
	private int telekineticMaxTicks = -1;

	private int forceSilenceTicks = 0;

	private final Map<ResourceLocation, Integer> selectedPowerLevels = new HashMap<>();
	private final Map<ResourceLocation, Integer> unlockedPowerTiers = new HashMap<>();
	private final Map<ResourceLocation, Integer> powerUsageCounts = new HashMap<>();
	private final Map<ResourceLocation, Integer> powerMasteryTicks = new HashMap<>();
	private final Map<ResourceLocation, Float> powerAmfP = new HashMap<>();
	private final Map<ResourceLocation, Float> powerAmfI = new HashMap<>();
	private boolean hasChosenOne = false;

	private long lastDeathProtectionTime = 0;
	private long lastBloodrushTime = 0;

	private int abilityPoints = 0;
	private int lightsaberKills = 0;
	private int nextKillThreshold = 35;

	private int lightsaberBlocks = 0;
	private int nextBlockThreshold = 20;

	private int saberTrainingProgress = 0;
	private int saberTrainingPointsEarned = 0;
	public static final int MAX_TRAINING_POINTS = 5;

	private int saberMeditationTicks = 0;
	private int saberMeditationPointsEarned = 0;
	public static final int MAX_SABER_MEDITATION_POINTS = 3;

	private boolean craftedFirstDoubleSaber = false;
	private boolean craftedFirstFocusedSaber = false;

	public static final List<ResourceLocation> ALL_ACTIVE_POWERS = List.of(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_push"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_pull"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_dash"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_jump"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_heal"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_repulse"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_lightning"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "electric_judgement"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_choke"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_drain"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_telekinesis"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_stasis"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_slam"),
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_barrier"));

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null)
				data.sync(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null)
				data.sync(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (event.getEntity() instanceof ServerPlayer newPlayer) {
			PlayerForceData oldData = PlayerForceData.get(event.getOriginal());
			PlayerForceData newData = PlayerForceData.get(newPlayer);

			if (oldData != null && newData != null) {
				newData.deserializeNBT(newPlayer.registryAccess(),
						oldData.serializeNBT(event.getOriginal().registryAccess()));
				newData.forceEnergy = newData.getMaxForceEnergy();
				newData.sync(newPlayer);
			}
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof ServerPlayer trackedPlayer && event.getEntity() instanceof ServerPlayer observer) {
			if (trackedPlayer.getPersistentData().getBoolean("IsMeditating")) {
				PlayerForceData data = PlayerForceData.get(trackedPlayer);
				boolean isFloating = data != null && data.isForceSensitive();
				PacketDistributor.sendToPlayer(observer,
						new com.fiskmods.pika.lightsabers.network.payload.SyncMeditationPayload(
								trackedPlayer.getId(), true, isFloating));
			}
		}
	}

	public boolean hasSkill(ResourceLocation skillId) {
		return unlockedSkills.contains(skillId);
	}

	public boolean hasSkill(String namespace, String path) {
		return hasSkill(ResourceLocation.fromNamespaceAndPath(namespace, path));
	}

	public boolean canAccessHolocron() {
		return this.forcePoints > 0 || this.abilityPoints > 0 || !this.unlockedSkills.isEmpty()
				|| this.forceSensitivity;
	}

	public boolean hasCraftedFirstLightsaber() {
		return craftedFirstLightsaber;
	}

	public boolean hasCraftedFirstDoubleSaber() {
		return craftedFirstDoubleSaber;
	}

	public boolean hasCraftedFirstFocusedSaber() {
		return craftedFirstFocusedSaber;
	}

	public void rewardFirstLightsaber(ServerPlayer player) {
		if (!this.craftedFirstLightsaber) {
			this.craftedFirstLightsaber = true;
			this.forcePoints += 1;
			this.abilityPoints += 1;
			checkAutoUnlockSensitivity(player);
			player.displayClientMessage(Component.translatable("message.lightsabersfork.first_lightsaber_crafted"),
					false);
			player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
			this.sync(player);
		}
	}

	public void rewardFocusedLightsaber(ServerPlayer player) {
		if (!this.craftedFirstFocusedSaber) {
			this.craftedFirstFocusedSaber = true;
			this.abilityPoints += 1;
			player.displayClientMessage(Component.translatable("message.lightsabersfork.milestone_focused_saber"),
					false);
			player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.2F);
			this.sync(player);
		}
	}

	public void rewardDoubleLightsaber(ServerPlayer player) {
		if (!this.craftedFirstDoubleSaber) {
			this.craftedFirstDoubleSaber = true;
			this.abilityPoints += 1;
			player.displayClientMessage(Component.translatable("message.lightsabersfork.milestone_double_saber"),
					false);
			player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.2F);
			this.sync(player);
		}
	}

	public void recordLightsaberBlock(ServerPlayer player) {
		this.lightsaberBlocks++;
		if (this.lightsaberBlocks >= this.nextBlockThreshold) {
			this.abilityPoints += 1;
			int achieved = this.lightsaberBlocks;

			this.nextBlockThreshold += 20 + (this.lightsaberBlocks / 4);

			if (player != null) {
				player.displayClientMessage(Component.translatable("message.lightsabersfork.mastery_defense", achieved),
						true);
				player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.1F);
				this.sync(player);
			}
		}
	}

	public void recordSaberTraining(ServerPlayer player, int amount) {
		if (this.saberTrainingPointsEarned >= MAX_TRAINING_POINTS) {
			return;
		}
		this.saberTrainingProgress += amount;
		int threshold = 15 + (this.saberTrainingPointsEarned * 10);
		if (this.saberTrainingProgress >= threshold) {
			this.saberTrainingProgress = 0;
			this.saberTrainingPointsEarned++;
			this.abilityPoints += 1;

			player.displayClientMessage(Component.translatable("message.lightsabersfork.training_reward",
					this.saberTrainingPointsEarned, MAX_TRAINING_POINTS), true);
			player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.2F);
			this.sync(player);
		}
	}

	public void tickSaberMeditation(ServerPlayer player) {
		tickSaberMeditation(1, player);
	}

	public int getRequiredMeditationTicksForNextFp() {
		if (this.meditationFpEarned == 0) {
			return 1200;
		}

		return this.meditationFpEarned * 3000;
	}

	public void tickSaberMeditation(int ticks, ServerPlayer player) {
		if (this.jediRank < 2 && this.sithRank < 2) {
			return;
		}
		if (this.saberMeditationPointsEarned >= MAX_SABER_MEDITATION_POINTS) {
			return;
		}
		this.saberMeditationTicks += ticks;
		if (this.saberMeditationTicks >= 600) {
			this.saberMeditationTicks = 0;
			this.saberMeditationPointsEarned++;
			this.abilityPoints += 1;

			player.displayClientMessage(Component.translatable("message.lightsabersfork.kata_meditation_reward"), true);
			player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.0F);
			this.sync(player);
		}
	}

	public boolean tickMeditationFpProgress(int ticks, ServerPlayer player) {
		this.meditationFpProgressTicks += ticks;
		if (player != null) {
			boolean hasLightsaber = player.getMainHandItem().getItem() instanceof ItemLightsaber
					|| player.getOffhandItem().getItem() instanceof ItemLightsaber
					|| player.getInventory().items.stream().anyMatch(s -> s.getItem() instanceof ItemLightsaber);

			if (hasLightsaber && (this.jediRank >= 2 || this.sithRank >= 2)) {
				this.tickSaberMeditation(ticks, player);
			}
		}

		int required = getRequiredMeditationTicksForNextFp();
		if (this.meditationFpProgressTicks >= required) {
			this.meditationFpProgressTicks = 0;
			this.meditationFpEarned++;
			this.forcePoints++;

			boolean firstTimeSens = false;
			ResourceLocation sensNode = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_sensitivity");
			if (!this.forceSensitivity || !this.hasSkill(sensNode)) {
				this.consumeForcePoints(1);
				this.unlockSkill(sensNode);
				firstTimeSens = true;
			}

			if (player != null) {
				if (firstTimeSens) {
					player.displayClientMessage(Component.translatable("skill.lightsabersfork.force_sensitivity.title")
							.withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
					PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
							new com.fiskmods.pika.lightsabers.network.payload.SyncMeditationPayload(player.getId(), true, true));
				} else {
					player.displayClientMessage(Component.translatable("message.lightsabersfork.meditation_reward")
							.withStyle(ChatFormatting.AQUA), true);
				}
				player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
				this.sync(player);
			}
			return true;
		}
		return false;
	}

	public int getTotalPowerUses() {
		return totalPowerUses;
	}

	public int getNextPowerUseThreshold() {
		return nextPowerUseThreshold;
	}

	public int getPowerUseStep() {
		return powerUseStep;
	}

	public void recordPowerUse(ResourceLocation powerId, ServerPlayer player) {
		if (powerId == null || !forceSensitivity)
			return;

		int oldInfoLevel = getInfoUnlockLevel(powerId);

		int count = getPowerUsageCount(powerId) + 1;
		powerUsageCounts.put(powerId, count);

		if (!isChanneledPower(powerId)) {
			addAmfP(powerId, 10.0f);
			addAmfI(powerId, 5.0f);
		}

		addForceXp(15.0f);

		int newInfoLevel = getInfoUnlockLevel(powerId);
		if (newInfoLevel > oldInfoLevel && player != null) {
			Component powerName = Component.translatable("power.lightsabersfork." + powerId.getPath());
			player.displayClientMessage(Component.translatable("message.lightsabersfork.mastery_knowledge_unlocked",
					powerName, newInfoLevel), true);
			player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.3F);
		}

		this.totalPowerUses++;
		if (this.totalPowerUses >= this.nextPowerUseThreshold) {
			this.forcePoints++;
			int reached = this.nextPowerUseThreshold;
			int stepIncrement = (ALConfig.POWER_USE_STEP_INCREMENT != null) ? ALConfig.POWER_USE_STEP_INCREMENT.get() : 25;
			this.powerUseStep += stepIncrement;
			this.nextPowerUseThreshold += this.powerUseStep;

			if (player != null) {
				player.displayClientMessage(Component.translatable("message.lightsabersfork.mastery_practice", reached)
						.withStyle(ChatFormatting.AQUA), true);
				player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
		}

		if (player != null) {
			this.sync(player);
		}
	}

	public void recordPowerUse(ResourceLocation powerId) {
		recordPowerUse(powerId, null);
	}

	public boolean hasAnyJediSkill() {
		for (ResourceLocation skill : unlockedSkills) {
			SkillNode node = SkillTreeRegistry.getNode(skill);
			if (node != null && node.isJediSkill())
				return true;
			String p = skill.getPath();
			if (p.startsWith("jedi_") || p.equals("force_heal") || p.equals("force_barrier") || p.equals("force_fortify")
					|| p.equals("electric_judgement") || p.equals("electric_judgment")) {
				return true;
			}
		}
		return false;
	}

	public boolean hasAnySithSkill() {
		for (ResourceLocation skill : unlockedSkills) {
			SkillNode node = SkillTreeRegistry.getNode(skill);
			if (node != null && node.isSithSkill())
				return true;
			String p = skill.getPath();
			if (p.startsWith("sith_") || p.equals("force_choke") || p.equals("force_lightning")
					|| p.equals("force_drain")) {
				return true;
			}
		}
		return false;
	}

	public void unlockSkill(ResourceLocation skillId) {
		unlockedSkills.add(skillId);
		String path = skillId.getPath();

		if (ALL_ACTIVE_POWERS.contains(skillId)) {
			unlockedPowerTiers.putIfAbsent(skillId, 1);
		}

		if (path.equals("force_sensitivity")) {
			this.forceSensitivity = true;
			this.forceEnergy = getMaxForceEnergy();
		} else if (path.equals("force_adept"))
			this.forceRank = Math.max(this.forceRank, 1);
		else if (path.equals("force_channeler"))
			this.forceRank = Math.max(this.forceRank, 2);
		else if (path.equals("force_veteran"))
			this.forceRank = Math.max(this.forceRank, 3);
		else if (path.equals("force_mastery"))
			this.forceRank = Math.max(this.forceRank, 4);

		else if (path.equals("jedi_initiate"))
			this.jediRank = Math.max(this.jediRank, 1);
		else if (path.equals("jedi_padawan"))
			this.jediRank = Math.max(this.jediRank, 2);
		else if (path.equals("jedi_knight"))
			this.jediRank = Math.max(this.jediRank, 3);
		else if (path.equals("jedi_librarian")) {
			this.jediRank = Math.max(this.jediRank, 3);
			this.jediSpecialization = "LIBRARIAN";
		} else if (path.equals("jedi_guard")) {
			this.jediRank = Math.max(this.jediRank, 3);
			this.jediSpecialization = "GUARD";
		} else if (path.equals("jedi_master"))
			this.jediRank = Math.max(this.jediRank, 4);
		else if (path.equals("jedi_council"))
			this.jediRank = Math.max(this.jediRank, 5);
		else if (path.equals("jedi_grandmaster"))
			this.jediRank = Math.max(this.jediRank, 6);

		else if (path.equals("sith_hopeful"))
			this.sithRank = Math.max(this.sithRank, 1);
		else if (path.equals("sith_acolyte"))
			this.sithRank = Math.max(this.sithRank, 2);
		else if (path.equals("sith_warrior"))
			this.sithRank = Math.max(this.sithRank, 3);
		else if (path.equals("sith_inquisitor")) {
			this.sithRank = Math.max(this.sithRank, 3);
			this.sithSpecialization = "INQUISITOR";
		} else if (path.equals("sith_assassin")) {
			this.sithRank = Math.max(this.sithRank, 3);
			this.sithSpecialization = "ASSASSIN";
		} else if (path.equals("sith_master"))
			this.sithRank = Math.max(this.sithRank, 4);
		else if (path.equals("sith_council"))
			this.sithRank = Math.max(this.sithRank, 5);
		else if (path.equals("sith_emperor"))
			this.sithRank = Math.max(this.sithRank, 6);
	}

	public int resetFactionSkills() {
		int totalFactionSpent = 0;
		List<ResourceLocation> toRemove = new ArrayList<>();

		for (ResourceLocation skillId : this.unlockedSkills) {
			SkillNode node = SkillTreeRegistry.getNode(skillId);
			boolean isFaction = false;

			if (node != null) {
				isFaction = node.isJediSkill() || node.isSithSkill();
			} else {
				String p = skillId.getPath();
				isFaction = p.startsWith("jedi_") || p.startsWith("sith_") || p.equals("force_heal")
						|| p.equals("force_barrier") || p.equals("force_fortify") || p.equals("electric_judgement") || p.equals("electric_judgment")
						|| p.equals("force_choke") || p.equals("force_lightning") || p.equals("force_drain");
			}

			if (isFaction) {
				toRemove.add(skillId);
				int baseCost = node != null ? node.getPointCost() : 2;
				totalFactionSpent += baseCost;

				int tier = this.unlockedPowerTiers.getOrDefault(skillId, 1);
				if (tier >= 2)
					totalFactionSpent += 2;
				if (tier >= 3)
					totalFactionSpent += 3;
			}
		}

		this.unlockedSkills.removeAll(toRemove);
		for (ResourceLocation rem : toRemove) {
			this.unlockedPowerTiers.remove(rem);
			this.selectedPowerLevels.remove(rem);
		}

		this.jediRank = 0;
		this.sithRank = 0;
		this.jediSpecialization = "NONE";
		this.sithSpecialization = "NONE";

		if (this.selectedPower != null && toRemove.contains(this.selectedPower)) {
			this.selectedPower = null;
		}

		int refund = Math.round(totalFactionSpent * 0.50F);
		this.forcePoints += refund;
		return refund;
	}

	public int resetAllPowersPristine() {
		int totalSpent = 0;
		List<ResourceLocation> toRemove = new ArrayList<>();

		for (ResourceLocation skillId : this.unlockedSkills) {
			if (skillId.getPath().equals("force_sensitivity")) {
				continue;
			}

			SkillNode node = SkillTreeRegistry.getNode(skillId);
			if (node != null && node.getCategory() == SkillTreeRegistry.TreeCategory.FORCE) {
				toRemove.add(skillId);
				totalSpent += node.getPointCost();

				int tier = this.unlockedPowerTiers.getOrDefault(skillId, 1);
				if (tier >= 2)
					totalSpent += 2;
				if (tier >= 3)
					totalSpent += 3;
			} else if (ALL_ACTIVE_POWERS.contains(skillId)) {
				toRemove.add(skillId);
				totalSpent += (node != null ? node.getPointCost() : 1);

				int tier = this.unlockedPowerTiers.getOrDefault(skillId, 1);
				if (tier >= 2)
					totalSpent += 2;
				if (tier >= 3)
					totalSpent += 3;
			}
		}

		this.unlockedSkills.removeAll(toRemove);
		for (ResourceLocation rem : toRemove) {
			this.unlockedPowerTiers.remove(rem);
			this.selectedPowerLevels.remove(rem);
		}

		this.forceRank = 0;
		this.jediRank = 0;
		this.sithRank = 0;
		this.jediSpecialization = "NONE";
		this.sithSpecialization = "NONE";
		this.selectedPower = null;

		int refund = Math.round(totalSpent * 0.70F);
		this.forcePoints += refund;
		return refund;
	}

	public Set<ResourceLocation> getUnlockedSkills() {
		return this.unlockedSkills;
	}

	public int getTelekineticEntityId() {
		return telekineticEntityId;
	}

	public void setTelekineticEntityId(int id) {
		this.telekineticEntityId = id;
	}

	public float getTelekineticDistance() {
		return telekineticDistance;
	}

	public void setTelekineticDistance(float dist) {
		this.telekineticDistance = dist;
	}

	public int getTelekineticHoldTicks() {
		return telekineticHoldTicks;
	}

	public void setTelekineticHoldTicks(int ticks) {
		this.telekineticHoldTicks = ticks;
	}

	public void incrementTelekineticHoldTicks() {
		this.telekineticHoldTicks++;
	}

	public int getTelekineticMaxTicks() {
		return telekineticMaxTicks;
	}

	public void setTelekineticMaxTicks(int maxTicks) {
		this.telekineticMaxTicks = maxTicks;
	}

	public void clearTelekineticEntity() {
		this.telekineticEntityId = -1;
		this.telekineticHoldTicks = 0;
		this.telekineticMaxTicks = -1;
		this.telekineticDistance = 5.0F;
	}

	public boolean isForceSilenced() {
		return forceSilenceTicks > 0;
	}

	public int getForceSilenceTicks() {
		return forceSilenceTicks;
	}

	public void setForceSilenceTicks(int ticks) {
		this.forceSilenceTicks = ticks;
	}

	public int getPowerUsageCount(ResourceLocation powerId) {
		if (powerId == null)
			return 0;
		return powerUsageCounts.getOrDefault(powerId, 0);
	}

	public int getMaxUnlockedPowerLevel(ResourceLocation powerId) {
		if (powerId == null)
			return 1;
		return unlockedPowerTiers.getOrDefault(powerId, 1);
	}

	public float getAmfP(ResourceLocation powerId) {
		if (powerId == null)
			return 0.0f;
		return powerAmfP.getOrDefault(powerId, 0.0f);
	}

	public void addAmfP(ResourceLocation powerId, float amount) {
		if (powerId == null || !forceSensitivity)
			return;
		powerAmfP.put(powerId, getAmfP(powerId) + amount);
	}

	public float getAmfI(ResourceLocation powerId) {
		if (powerId == null)
			return 0.0f;
		return powerAmfI.getOrDefault(powerId, 0.0f);
	}

	public void addAmfI(ResourceLocation powerId, float amount) {
		if (powerId == null || !forceSensitivity)
			return;
		powerAmfI.put(powerId, getAmfI(powerId) + amount);
	}

	public int getInfoUnlockLevel(ResourceLocation powerId) {
		float amfi = getAmfI(powerId);
		if (amfi >= 5000.0f)
			return 4;
		if (amfi >= 3000.0f)
			return 3;
		if (amfi >= 2000.0f)
			return 2;
		if (amfi >= 1000.0f)
			return 1;
		return 0;
	}

	public boolean isChanneledPower(ResourceLocation powerId) {
		if (powerId == null)
			return false;
		String p = powerId.getPath();
		return p.equals("force_lightning") || p.equals("electric_judgement") || p.equals("force_choke")
				|| p.equals("force_drain") || p.equals("force_barrier") || p.equals("force_fortify") || p.equals("force_telekinesis");
	}

	public int getMaxTierForPower(ResourceLocation powerId) {
		if (powerId == null)
			return 3;
		String path = powerId.getPath();
		if (path.equals("force_push") || path.equals("electric_judgement") || path.equals("electric_judgment"))
			return 4;
		return 3;
	}

	public float getRequiredAmfPForTier(ResourceLocation powerId, int tier) {
		boolean channeled = isChanneledPower(powerId);
		if (tier == 2) {
			return channeled ? 200.0f : 250.0f;
		} else if (tier == 3) {
			return 600.0f;
		} else if (tier == 4) {
			return 1000.0f;
		}
		return 0.0f;
	}

	public boolean hasChosenOneAdvancement() {
		return this.hasChosenOne;
	}

	public void setHasChosenOne(boolean value) {
		this.hasChosenOne = value;
	}

	public boolean hasMaxedNeutralTree() {
		if (!hasSkill("lightsabersfork", "force_sensitivity"))
			return false;
		if (!hasSkill("lightsabersfork", "force_mastery"))
			return false;

		String[] neutralPowers = { "force_push", "force_pull", "force_dash", "force_jump", "force_telekinesis",
				"force_stasis", "force_repulse", "force_slam" };
		for (String power : neutralPowers) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath("lightsabersfork", power);
			if (!hasSkill(id))
				return false;
			if (getMaxUnlockedPowerLevel(id) < getMaxTierForPower(id))
				return false;
		}
		return true;
	}

	public boolean isUnifiedTreeUnlocked() {
		return this.hasChosenOne && hasMaxedNeutralTree();
	}

	public boolean canUpgradePowerTier(ResourceLocation powerId) {
		if (powerId == null || !hasSkill(powerId))
			return false;
		int currentTier = getMaxUnlockedPowerLevel(powerId);
		int maxTier = getMaxTierForPower(powerId);
		if (currentTier >= maxTier)
			return false;

		int nextTier = currentTier + 1;
		float amfp = getAmfP(powerId);
		float req = getRequiredAmfPForTier(powerId, nextTier);

		if (amfp < req)
			return false;

		if (nextTier == 4 && !this.hasChosenOne)
			return false;

		return true;
	}

	public int getUpgradePointCost(ResourceLocation powerId) {
		int currentTier = getMaxUnlockedPowerLevel(powerId);
		if (currentTier == 1)
			return 2;
		if (currentTier == 2)
			return 3;
		if (currentTier == 3)
			return 4;
		return 2;
	}

	public void upgradePowerTier(ResourceLocation powerId) {
		if (powerId == null)
			return;
		int current = getMaxUnlockedPowerLevel(powerId);
		int maxTier = getMaxTierForPower(powerId);
		if (current < maxTier) {
			unlockedPowerTiers.put(powerId, current + 1);
			setPowerLevel(powerId, current + 1);
		}
	}

	public boolean isForceSensitive() {
		return forceSensitivity;
	}

	public void setForceSensitivity(boolean sensitive) {
		this.forceSensitivity = sensitive;
	}

	public int getForceRank() {
		return forceRank;
	}

	public void setForceRank(int rank) {
		this.forceRank = rank;
	}

	public int getJediRank() {
		return jediRank;
	}

	public void setJediRank(int rank) {
		this.jediRank = rank;
	}

	public int getSithRank() {
		return sithRank;
	}

	public void setSithRank(int rank) {
		this.sithRank = rank;
	}

	public String getJediSpecialization() {
		return jediSpecialization;
	}

	public void setJediSpecialization(String spec) {
		this.jediSpecialization = spec;
	}

	public String getSithSpecialization() {
		return sithSpecialization;
	}

	public void setSithSpecialization(String spec) {
		this.sithSpecialization = spec;
	}

	public float getMaxForceEnergy() {
		if (!forceSensitivity)
			return 0.0f;

		int effectiveRank = Math.max(forceRank, Math.max(jediRank, sithRank));

		float max = 150.0f;
		if (effectiveRank >= 1)
			max = 220.0f;
		if (effectiveRank >= 2)
			max = 320.0f;
		if (effectiveRank >= 3)
			max = 450.0f;
		if (effectiveRank >= 4)
			max = 600.0f;
		if (effectiveRank >= 5)
			max = 800.0f;
		if (effectiveRank >= 6)
			max = 1000.0f;

		if (!"NONE".equals(jediSpecialization) || !"NONE".equals(sithSpecialization)) {
			max += 150.0f;
		}

		max += this.meditationBonusEnergy;
		return max;
	}

	public boolean regenerateEnergy() {
		return regenerateEnergy(null);
	}

	public boolean regenerateEnergy(Player player) {
		if (!forceSensitivity)
			return false;

		boolean cooldownFinished = this.tickCooldowns();

		if (this.forceSilenceTicks > 0) {
			this.forceSilenceTicks--;
		}

		int effectiveRank = Math.max(forceRank, Math.max(jediRank, sithRank));

		float baseRegen = 0.125f;
		if (effectiveRank >= 1)
			baseRegen = 0.200f;
		if (effectiveRank >= 2)
			baseRegen = 0.300f;
		if (effectiveRank >= 3)
			baseRegen = 0.450f;
		if (effectiveRank >= 4)
			baseRegen = 0.650f;
		if (effectiveRank >= 5)
			baseRegen = 0.900f;
		if (effectiveRank >= 6)
			baseRegen = 1.250f;

		if ("LIBRARIAN".equals(jediSpecialization)) {
			baseRegen *= 1.25f;
		}

		if (player != null && jediRank >= 1) {
			if (player.isCrouching() || (player.walkDist == player.walkDistO && player.onGround())) {
				baseRegen *= 1.15f;
			}
		}

		if (player != null && sithRank >= 3) {
			if (player.getHealth() <= player.getMaxHealth() * 0.5f) {
				baseRegen *= 1.25f;
			}
		}

		if (player != null && player.getPersistentData().getBoolean("IsMeditating")) {
			baseRegen *= 2.5f;
		}

		float prevEnergy = this.forceEnergy;
		float maxEnergy = getMaxForceEnergy();
		if (this.forceEnergy < maxEnergy) {
			this.forceEnergy = Math.min(this.forceEnergy + baseRegen, maxEnergy);
		}

		boolean justReachedMax = (prevEnergy < maxEnergy && this.forceEnergy >= maxEnergy);
		return cooldownFinished || justReachedMax;
	}

	public float getForceEnergy() {
		return forceEnergy;
	}

	public void setForceEnergy(float energy) {
		this.forceEnergy = Math.max(0.0f, Math.min(energy, getMaxForceEnergy()));
	}

	public boolean consumeForceEnergy(float amount) {
		if (!forceSensitivity)
			return false;
		if (this.forceEnergy >= amount) {
			this.forceEnergy -= amount;
			return true;
		}
		return false;
	}

	public void addForceEnergy(float amount) {
		if (!forceSensitivity)
			return;
		this.forceEnergy = Math.min(this.forceEnergy + amount, getMaxForceEnergy());
	}

	public int getMeditationTicks() {
		return meditationTicks;
	}

	public int getMeditationBonusEnergy() {
		return meditationBonusEnergy;
	}

	public void addMeditationTicks(int ticks) {
		addMeditationTicks(ticks, null);
	}

	public void addMeditationTicks(int ticks, ServerPlayer player) {
		this.meditationTicks += ticks;
		int calculatedBonus = Math.min(150, (this.meditationTicks / 1800) * 5);
		if (calculatedBonus > this.meditationBonusEnergy) {
			this.meditationBonusEnergy = calculatedBonus;
			if (player != null) {
				player.displayClientMessage(
						Component.translatable("message.lightsabersfork.meditation_energy_bonus", this.meditationBonusEnergy)
								.withStyle(ChatFormatting.AQUA), true);
				player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.2F);
				this.sync(player);
			}
		}
	}

	public ResourceLocation getSelectedPower() {
		return selectedPower;
	}

	public void setSelectedPower(ResourceLocation powerId) {
		this.selectedPower = powerId;
	}

	public int getPowerLevel(ResourceLocation powerId) {
		if (powerId == null)
			return 1;
		return selectedPowerLevels.getOrDefault(powerId, 1);
	}

	public void setPowerLevel(ResourceLocation powerId, int level) {
		if (powerId == null)
			return;
		int maxAllowed = getMaxUnlockedPowerLevel(powerId);
		int clamp = Math.max(1, Math.min(level, maxAllowed));
		selectedPowerLevels.put(powerId, clamp);
	}

	public int getCurrentSelectedPowerLevel() {
		return getPowerLevel(this.selectedPower);
	}

	public int getMasteryTicks(ResourceLocation powerId) {
		if (powerId == null)
			return 0;
		return powerMasteryTicks.getOrDefault(powerId, 0);
	}

	public void addMasteryTicks(ResourceLocation powerId, int ticks) {
		if (powerId == null || !forceSensitivity)
			return;
		int current = getMasteryTicks(powerId);
		powerMasteryTicks.put(powerId, Math.min(MASTERY_MAX_TICKS, current + ticks));
	}

	public int getMasteryTier(ResourceLocation powerId) {
		if (powerId == null)
			return 0;
		int ticks = getMasteryTicks(powerId);
		int uses = getPowerUsageCount(powerId);

		if (ticks >= MASTERY_TIER_3_TICKS || uses >= 100)
			return 3;
		if (ticks >= MASTERY_TIER_2_TICKS || uses >= 35)
			return 2;
		if (ticks >= MASTERY_TIER_1_TICKS || uses >= 10 || hasSkill(powerId))
			return 1;
		return 0;
	}

	public float getMasteryProgress(ResourceLocation powerId) {
		if (powerId == null)
			return 0.0f;
		float tickProg = (float) getMasteryTicks(powerId) / (float) MASTERY_TIER_3_TICKS;
		float useProg = (float) getPowerUsageCount(powerId) / 100.0f;
		return Math.min(1.0f, Math.max(tickProg, useProg));
	}

	public List<ResourceLocation> getAvailableActivePowers() {
		List<ResourceLocation> activePowers = new ArrayList<>();
		if (this.forceSensitivity) {
			for (ResourceLocation power : ALL_ACTIVE_POWERS) {
				if (hasSkill(power)) {
					activePowers.add(power);
				}
			}
		}
		return activePowers;
	}

	public float getFallDamageMultiplier() {
		if (forceRank >= 4)
			return 0.0f;
		if (forceRank >= 1)
			return 0.75f;
		return 1.0f;
	}

	public float getMobilityCostMultiplier() {
		return forceRank >= 1 ? 0.75f : 1.0f;
	}

	public float getPowerCostMultiplier(ResourceLocation powerId) {
		float mult = 1.0f;
		if (forceRank >= 2 && powerId != null) {
			String path = powerId.getPath();
			if (path.equals("force_push") || path.equals("force_pull")) {
				mult *= 0.80f;
			}
		}
		if (powerId != null && "GUARD".equals(jediSpecialization) && (powerId.getPath().equals("force_barrier") || powerId.getPath().equals("force_fortify"))) {
			mult *= 0.70f;
		}
		if (jediRank >= 6)
			mult *= 0.75f;
		return mult;
	}

	public float getPassiveSpeedBonus() {
		return forceRank >= 2 ? 0.10f : 0.0f;
	}

	public float getRangeMultiplier(ResourceLocation powerId) {
		return forceRank >= 3 ? 1.25f : 1.0f;
	}

	public float getCooldownMultiplier() {
		return forceRank >= 4 ? 0.70f : 1.0f;
	}

	public float getHungerMultiplier() {
		return jediRank >= 1 ? 0.80f : 1.0f;
	}

	public float getBlockStaminaMultiplier() {
		return jediRank >= 2 ? 0.75f : 1.0f;
	}

	public double getPassiveArmorBonus() {
		if ("GUARD".equals(jediSpecialization))
			return 3.0D;
		if (jediRank >= 2)
			return 1.0D;
		return 0.0D;
	}

	public double getPassiveKnockbackResistanceBonus() {
		return "GUARD".equals(jediSpecialization) ? 0.20D : 0.0D;
	}

	public float getMeditationXpMultiplier() {
		return "LIBRARIAN".equals(jediSpecialization) ? 2.0f : 1.0f;
	}

	public boolean hasLightAura() {
		return jediRank >= 4;
	}

	public boolean isImmuneToDarkness() {
		return jediRank >= 4 || sithRank >= 6;
	}

	public boolean isImmuneToWardenDarkness() {
		return jediRank >= 6 || sithRank >= 6;
	}

	public boolean canTriggerDeathProtection(long currentGameTime) {
		return jediRank >= 5 && (currentGameTime - lastDeathProtectionTime >= 6000);
	}

	public void triggerDeathProtection(long currentGameTime) {
		this.lastDeathProtectionTime = currentGameTime;
		this.forceEnergy = 0.0f;
	}

	public boolean canTriggerBloodrush(long currentGameTime) {
		return sithRank >= 3 && (currentGameTime - lastBloodrushTime >= 1200);
	}

	public void triggerBloodrush(long currentGameTime) {
		this.lastBloodrushTime = currentGameTime;
	}

	public boolean hasUndeadRepellingAura() {
		return jediRank >= 6;
	}

	public boolean hasWrathOnKill() {
		return sithRank >= 1;
	}

	public float getExecuteDamageMultiplier(float healthPercent) {
		if (healthPercent <= 0.35f) {
			if (sithRank >= 4)
				return 1.25f;
			if (sithRank >= 2)
				return 1.20f;
		}
		return 1.0f;
	}

	public float getDamageToManaReturnRatio() {
		return "INQUISITOR".equals(sithSpecialization) ? 0.05f : 0.0f;
	}

	public float getSneakCritMultiplier() {
		return "ASSASSIN".equals(sithSpecialization) ? 1.50f : 1.0f;
	}

	public boolean hasFearAura() {
		return sithRank >= 4;
	}

	public void onTakeDamageConvertAnger(float damage) {
		if (sithRank >= 4 && damage > 0.0f) {
			this.addForceEnergy(5.0f);
		}
	}

	public boolean hasDeathExplosion() {
		return sithRank >= 5;
	}

	public boolean hasArmorPiercingForce() {
		return sithRank >= 6;
	}

	public float getDrainMultiplier() {
		return sithRank >= 6 ? 2.0f : 1.0f;
	}

	public int getForcePoints() {
		return forcePoints;
	}

	public void addForcePoints(int amount) {
		this.forcePoints += amount;
		checkAutoUnlockSensitivity(null);
	}

	public void checkAutoUnlockSensitivity(ServerPlayer player) {
		ResourceLocation sensNode = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_sensitivity");
		if ((!this.forceSensitivity || !this.hasSkill(sensNode)) && this.forcePoints > 0) {
			this.consumeForcePoints(1);
			this.unlockSkill(sensNode);
			if (player != null) {
				player.displayClientMessage(Component.translatable("skill.lightsabersfork.force_sensitivity.title")
						.withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
				PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
						new com.fiskmods.pika.lightsabers.network.payload.SyncMeditationPayload(player.getId(), true, true));
				this.sync(player);
			}
		}
	}

	public boolean consumeForcePoints(int amount) {
		if (this.forcePoints >= amount) {
			this.forcePoints -= amount;
			return true;
		}
		return false;
	}

	public float getForceXp() {
		return forceXp;
	}

	public void addForceXp(float amount) {
		this.forceXp += amount;
		while (this.forceXp >= FORCE_XP_PER_POINT) {
			this.forceXp -= FORCE_XP_PER_POINT;
			this.forcePoints++;
			checkAutoUnlockSensitivity(null);
		}
	}

	public int getAbilityPoints() {
		return abilityPoints;
	}

	public void addAbilityPoints(int amount) {
		this.abilityPoints += amount;
	}

	public boolean consumeAbilityPoints(int amount) {
		if (this.abilityPoints >= amount) {
			this.abilityPoints -= amount;
			return true;
		}
		return false;
	}

	public int getLightsaberKills() {
		return lightsaberKills;
	}

	public int getNextKillThreshold() {
		return nextKillThreshold;
	}

	public int getLightsaberBlocks() {
		return lightsaberBlocks;
	}

	public int getNextBlockThreshold() {
		return nextBlockThreshold;
	}

	public int getSaberTrainingProgress() {
		return saberTrainingProgress;
	}

	public int getSaberTrainingPointsEarned() {
		return saberTrainingPointsEarned;
	}

	public int getSaberMeditationTicks() {
		return saberMeditationTicks;
	}

	public int getSaberMeditationPointsEarned() {
		return saberMeditationPointsEarned;
	}

	public int getMeditationFpEarned() {
		return meditationFpEarned;
	}

	public int getMeditationFpProgressTicks() {
		return meditationFpProgressTicks;
	}

	public boolean hasChosenOne() {
		return hasChosenOne;
	}

	public long getLastDeathProtectionTime() {
		return lastDeathProtectionTime;
	}

	public long getLastBloodrushTime() {
		return lastBloodrushTime;
	}

	public boolean addLightsaberKill() {
		this.lightsaberKills++;
		if (this.lightsaberKills >= this.nextKillThreshold) {
			this.abilityPoints++;
			this.lightsaberKills = 0;
			this.nextKillThreshold += 10;
			return true;
		}
		return false;
	}

	public void unlockAllSkills() {
		for (SkillNode node : SkillTreeRegistry.getAllNodes()) {
			this.unlockSkill(node.getId());
		}
	}

	public void resetAll() {
		unlockedSkills.clear();
		selectedPowerLevels.clear();
		unlockedPowerTiers.clear();
		powerUsageCounts.clear();
		powerMasteryTicks.clear();
		powerCooldowns.clear();
		forcePoints = 0;
		forceXp = 0.0f;
		abilityPoints = 0;
		lightsaberKills = 0;
		nextKillThreshold = 35;
		lightsaberBlocks = 0;
		nextBlockThreshold = 20;
		saberTrainingProgress = 0;
		saberTrainingPointsEarned = 0;
		saberMeditationTicks = 0;
		saberMeditationPointsEarned = 0;
		craftedFirstDoubleSaber = false;
		craftedFirstFocusedSaber = false;
		forceSensitivity = false;
		forceRank = 0;
		jediRank = 0;
		sithRank = 0;
		jediSpecialization = "NONE";
		sithSpecialization = "NONE";
		forceEnergy = 0.0f;
		selectedPower = null;
		meditationTicks = 0;
		meditationBonusEnergy = 0;
		lastDeathProtectionTime = 0;
		lastBloodrushTime = 0;
		forceSilenceTicks = 0;
		clearTelekineticEntity();
		powerAmfP.clear();
		powerAmfI.clear();
		hasChosenOne = false;

		craftedFirstLightsaber = false;
		meditationFpEarned = 0;
		meditationFpProgressTicks = 0;
		totalPowerUses = 0;
		int initialThreshold = (ALConfig.INITIAL_POWER_USE_THRESHOLD != null) ? ALConfig.INITIAL_POWER_USE_THRESHOLD.get() : 25;
		int initialStep = (ALConfig.POWER_USE_STEP_INCREMENT != null) ? ALConfig.POWER_USE_STEP_INCREMENT.get() : 25;
		nextPowerUseThreshold = initialThreshold;
		powerUseStep = initialStep;
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag tag = new CompoundTag();
		tag.putInt("ForcePoints", forcePoints);
		tag.putFloat("ForceXP", forceXp);
		tag.putInt("AbilityPoints", abilityPoints);
		tag.putInt("LightsaberKills", lightsaberKills);
		tag.putInt("NextKillThreshold", nextKillThreshold);

		tag.putInt("LightsaberBlocks", lightsaberBlocks);
		tag.putInt("NextBlockThreshold", nextBlockThreshold);
		tag.putInt("SaberTrainingProgress", saberTrainingProgress);
		tag.putInt("SaberTrainingPointsEarned", saberTrainingPointsEarned);
		tag.putInt("SaberMeditationTicks", saberMeditationTicks);
		tag.putInt("SaberMeditationPointsEarned", saberMeditationPointsEarned);
		tag.putBoolean("CraftedFirstDoubleSaber", craftedFirstDoubleSaber);
		tag.putBoolean("CraftedFirstFocusedSaber", craftedFirstFocusedSaber);

		tag.putBoolean("CraftedFirstLightsaber", craftedFirstLightsaber);
		tag.putInt("MeditationFpEarned", meditationFpEarned);
		tag.putInt("MeditationFpProgress", meditationFpProgressTicks);
		tag.putInt("TotalPowerUses", totalPowerUses);
		tag.putInt("NextPowerUseThreshold", nextPowerUseThreshold);
		tag.putInt("PowerUseStep", powerUseStep);

		tag.putBoolean("ForceSensitivity", forceSensitivity);
		tag.putInt("ForceRank", forceRank);
		tag.putInt("JediRank", jediRank);
		tag.putInt("SithRank", sithRank);
		tag.putString("JediSpec", jediSpecialization);
		tag.putString("SithSpec", sithSpecialization);
		tag.putFloat("ForceEnergy", forceEnergy);
		if (selectedPower != null)
			tag.putString("SelectedPower", selectedPower.toString());

		tag.putInt("MeditationTicks", meditationTicks);
		tag.putInt("MeditationBonus", meditationBonusEnergy);
		tag.putLong("LastDeathProtection", lastDeathProtectionTime);
		tag.putLong("LastBloodrush", lastBloodrushTime);

		tag.putInt("TK_EntityId", telekineticEntityId);
		tag.putFloat("TK_Dist", telekineticDistance);
		tag.putInt("TK_HoldTicks", telekineticHoldTicks);
		tag.putInt("TK_MaxTicks", telekineticMaxTicks);
		tag.putInt("ForceSilence", forceSilenceTicks);

		CompoundTag levelsTag = new CompoundTag();
		for (Map.Entry<ResourceLocation, Integer> entry : selectedPowerLevels.entrySet()) {
			levelsTag.putInt(entry.getKey().toString(), entry.getValue());
		}
		tag.put("SelectedLevels", levelsTag);

		CompoundTag tiersTag = new CompoundTag();
		for (Map.Entry<ResourceLocation, Integer> entry : unlockedPowerTiers.entrySet()) {
			tiersTag.putInt(entry.getKey().toString(), entry.getValue());
		}
		tag.put("UnlockedTiers", tiersTag);

		CompoundTag usageTag = new CompoundTag();
		for (Map.Entry<ResourceLocation, Integer> entry : powerUsageCounts.entrySet()) {
			usageTag.putInt(entry.getKey().toString(), entry.getValue());
		}
		tag.put("PowerUsages", usageTag);

		CompoundTag masteryTag = new CompoundTag();
		for (Map.Entry<ResourceLocation, Integer> entry : powerMasteryTicks.entrySet()) {
			masteryTag.putInt(entry.getKey().toString(), entry.getValue());
		}
		tag.put("PowerMastery", masteryTag);

		CompoundTag cdTag = new CompoundTag();
		for (Map.Entry<ResourceLocation, Integer> entry : powerCooldowns.entrySet()) {
			if (entry.getValue() > 0) {
				cdTag.putInt(entry.getKey().toString(), entry.getValue());
			}
		}
		tag.put("PowerCooldowns", cdTag);

		CompoundTag amfpTag = new CompoundTag();
		for (Map.Entry<ResourceLocation, Float> entry : powerAmfP.entrySet()) {
			amfpTag.putFloat(entry.getKey().toString(), entry.getValue());
		}
		tag.put("PowerAmfP", amfpTag);

		CompoundTag amfiTag = new CompoundTag();
		for (Map.Entry<ResourceLocation, Float> entry : powerAmfI.entrySet()) {
			amfiTag.putFloat(entry.getKey().toString(), entry.getValue());
		}
		tag.put("PowerAmfI", amfiTag);
		tag.putBoolean("HasChosenOne", hasChosenOne);

		ListTag list = new ListTag();
		for (ResourceLocation rl : unlockedSkills)
			list.add(StringTag.valueOf(rl.toString()));
		tag.put("UnlockedSkills", list);

		return tag;
	}

	public int getBasePowerCooldownTicks(ResourceLocation powerId) {
		return getBasePowerCooldownTicks(powerId, getPowerLevel(powerId));
	}

	public int getBasePowerCooldownTicks(ResourceLocation powerId, int level) {
		if (powerId == null)
			return 0;
		return switch (powerId.getPath()) {
		case "force_dash" -> (level == 2) ? 60 : 40;
		case "force_jump" -> (level == 3) ? 60 : 100;
		case "force_push" -> (level == 4) ? 300 : ((level == 2) ? 100 : ((level == 3) ? 80 : 60));
		case "force_pull" -> (level == 2) ? 100 : ((level == 3) ? 80 : 60);
		case "force_telekinesis" -> (level == 2) ? 80 : 40;
		case "force_slam" -> 160;
		case "force_stasis" -> (level == 3) ? 240 : 140;
		case "force_repulse" -> (level == 3) ? 200 : 100;
		case "force_heal" -> (level == 3) ? 280 : ((level == 2) ? 200 : 160);
		case "force_lightning", "electric_judgement", "force_choke", "force_drain", "force_barrier", "force_fortify" -> 40;

		default -> 0;
		};
	}

	public int getPowerCooldown(ResourceLocation powerId) {
		if (powerId == null)
			return 0;
		return powerCooldowns.getOrDefault(powerId, 0);
	}

	public boolean isPowerOnCooldown(ResourceLocation powerId) {
		return getPowerCooldown(powerId) > 0;
	}

	public void triggerPowerCooldown(ResourceLocation powerId) {
		if (powerId == null)
			return;
		int baseTicks = getBasePowerCooldownTicks(powerId);
		if (baseTicks > 0) {
			int finalTicks = Math.max(5, Math.round(baseTicks * getCooldownMultiplier()));
			powerCooldowns.put(powerId, finalTicks);
		}
	}

	public void setPowerCooldown(ResourceLocation powerId, int ticks) {
		if (powerId == null)
			return;
		if (ticks <= 0) {
			powerCooldowns.remove(powerId);
		} else {
			powerCooldowns.put(powerId, ticks);
		}
	}

	public boolean hasActiveCooldowns() {
		return !powerCooldowns.isEmpty();
	}

	public boolean tickCooldowns() {
		if (powerCooldowns.isEmpty())
			return false;
		boolean finished = false;
		var it = powerCooldowns.entrySet().iterator();
		while (it.hasNext()) {
			var entry = it.next();
			int newTicks = entry.getValue() - 1;
			if (newTicks <= 0) {
				it.remove();
				finished = true;
			} else {
				entry.setValue(newTicks);
			}
		}
		return finished;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
		forcePoints = tag.getInt("ForcePoints");
		forceXp = tag.getFloat("ForceXP");
		abilityPoints = tag.getInt("AbilityPoints");
		lightsaberKills = tag.getInt("LightsaberKills");
		nextKillThreshold = tag.contains("NextKillThreshold") ? tag.getInt("NextKillThreshold") : 35;

		lightsaberBlocks = tag.getInt("LightsaberBlocks");
		nextBlockThreshold = tag.contains("NextBlockThreshold") ? tag.getInt("NextBlockThreshold") : 20;
		saberTrainingProgress = tag.getInt("SaberTrainingProgress");
		saberTrainingPointsEarned = tag.getInt("SaberTrainingPointsEarned");
		saberMeditationTicks = tag.getInt("SaberMeditationTicks");
		saberMeditationPointsEarned = tag.getInt("SaberMeditationPointsEarned");
		craftedFirstDoubleSaber = tag.getBoolean("CraftedFirstDoubleSaber");
		craftedFirstFocusedSaber = tag.getBoolean("CraftedFirstFocusedSaber");

		craftedFirstLightsaber = tag.getBoolean("CraftedFirstLightsaber");
		meditationFpEarned = tag.getInt("MeditationFpEarned");
		meditationFpProgressTicks = tag.getInt("MeditationFpProgress");
		totalPowerUses = tag.getInt("TotalPowerUses");
		int defaultInitial = (ALConfig.INITIAL_POWER_USE_THRESHOLD != null) ? ALConfig.INITIAL_POWER_USE_THRESHOLD.get() : 25;
		int defaultStep = (ALConfig.POWER_USE_STEP_INCREMENT != null) ? ALConfig.POWER_USE_STEP_INCREMENT.get() : 25;
		nextPowerUseThreshold = tag.contains("NextPowerUseThreshold") ? tag.getInt("NextPowerUseThreshold") : defaultInitial;
		powerUseStep = tag.contains("PowerUseStep") ? tag.getInt("PowerUseStep") : defaultStep;

		forceSensitivity = tag.getBoolean("ForceSensitivity");
		forceRank = tag.getInt("ForceRank");
		jediRank = tag.getInt("JediRank");
		sithRank = tag.getInt("SithRank");
		jediSpecialization = tag.contains("JediSpec") ? tag.getString("JediSpec") : "NONE";
		sithSpecialization = tag.contains("SithSpec") ? tag.getString("SithSpec") : "NONE";
		forceEnergy = tag.contains("ForceEnergy") ? tag.getFloat("ForceEnergy") : 0.0f;
		selectedPower = tag.contains("SelectedPower") ? ResourceLocation.parse(tag.getString("SelectedPower")) : null;

		meditationTicks = tag.contains("MeditationTicks") ? tag.getInt("MeditationTicks") : 0;
		meditationBonusEnergy = tag.contains("MeditationBonus") ? tag.getInt("MeditationBonus") : 0;
		lastDeathProtectionTime = tag.contains("LastDeathProtection") ? tag.getLong("LastDeathProtection") : 0;
		lastBloodrushTime = tag.contains("LastBloodrush") ? tag.getLong("LastBloodrush") : 0;

		telekineticEntityId = tag.contains("TK_EntityId") ? tag.getInt("TK_EntityId") : -1;
		telekineticDistance = tag.contains("TK_Dist") ? tag.getFloat("TK_Dist") : 5.0F;
		telekineticHoldTicks = tag.contains("TK_HoldTicks") ? tag.getInt("TK_HoldTicks") : 0;
		telekineticMaxTicks = tag.contains("TK_MaxTicks") ? tag.getInt("TK_MaxTicks") : -1;
		forceSilenceTicks = tag.contains("ForceSilence") ? tag.getInt("ForceSilence") : 0;

		selectedPowerLevels.clear();
		if (tag.contains("SelectedLevels")) {
			CompoundTag levelsTag = tag.getCompound("SelectedLevels");
			for (String key : levelsTag.getAllKeys()) {
				ResourceLocation rl = ResourceLocation.parse(key);
				if ("force_fortify".equals(rl.getPath())) {
					rl = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), "force_barrier");
				}
				selectedPowerLevels.put(rl, levelsTag.getInt(key));
			}
		}

		unlockedPowerTiers.clear();
		if (tag.contains("UnlockedTiers")) {
			CompoundTag tiersTag = tag.getCompound("UnlockedTiers");
			for (String key : tiersTag.getAllKeys()) {
				ResourceLocation rl = ResourceLocation.parse(key);
				if ("force_fortify".equals(rl.getPath())) {
					rl = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), "force_barrier");
				}
				unlockedPowerTiers.put(rl, tiersTag.getInt(key));
			}
		}

		powerUsageCounts.clear();
		if (tag.contains("PowerUsages")) {
			CompoundTag usageTag = tag.getCompound("PowerUsages");
			for (String key : usageTag.getAllKeys()) {
				ResourceLocation rl = ResourceLocation.parse(key);
				if ("force_fortify".equals(rl.getPath())) {
					rl = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), "force_barrier");
				}
				powerUsageCounts.put(rl, usageTag.getInt(key));
			}
		}

		powerMasteryTicks.clear();
		if (tag.contains("PowerMastery")) {
			CompoundTag masteryTag = tag.getCompound("PowerMastery");
			for (String key : masteryTag.getAllKeys()) {
				ResourceLocation rl = ResourceLocation.parse(key);
				if ("force_fortify".equals(rl.getPath())) {
					rl = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), "force_barrier");
				}
				powerMasteryTicks.put(rl, masteryTag.getInt(key));
			}
		}

		powerCooldowns.clear();
		if (tag.contains("PowerCooldowns")) {
			CompoundTag cdTag = tag.getCompound("PowerCooldowns");
			for (String key : cdTag.getAllKeys()) {
				ResourceLocation rl = ResourceLocation.parse(key);
				if ("force_fortify".equals(rl.getPath())) {
					rl = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), "force_barrier");
				}
				powerCooldowns.put(rl, cdTag.getInt(key));
			}
		}

		powerAmfP.clear();
		if (tag.contains("PowerAmfP")) {
			CompoundTag amfpTag = tag.getCompound("PowerAmfP");
			for (String key : amfpTag.getAllKeys()) {
				ResourceLocation rl = ResourceLocation.parse(key);
				if ("force_fortify".equals(rl.getPath())) {
					rl = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), "force_barrier");
				}
				powerAmfP.put(rl, amfpTag.getFloat(key));
			}
		}

		powerAmfI.clear();
		if (tag.contains("PowerAmfI")) {
			CompoundTag amfiTag = tag.getCompound("PowerAmfI");
			for (String key : amfiTag.getAllKeys()) {
				ResourceLocation rl = ResourceLocation.parse(key);
				if ("force_fortify".equals(rl.getPath())) {
					rl = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), "force_barrier");
				}
				powerAmfI.put(rl, amfiTag.getFloat(key));
			}
		}
		hasChosenOne = tag.getBoolean("HasChosenOne");

		unlockedSkills.clear();
		ListTag list = tag.getList("UnlockedSkills", Tag.TAG_STRING);
		for (int i = 0; i < list.size(); i++) {
			ResourceLocation rl = ResourceLocation.parse(list.getString(i));
			if ("force_fortify".equals(rl.getPath())) {
				rl = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), "force_barrier");
			}
			unlockedSkills.add(rl);
		}
	}

	public void sync(ServerPlayer player) {
		CompoundTag tag = this.serializeNBT(player.registryAccess());
		PacketDistributor.sendToPlayer(player, new SyncForceDataPayload(tag));
	}

	public static PlayerForceData get(Player player) {
		if (player == null)
			return null;
		return player.getData(ALAttachmentTypes.FORCE_DATA);
	}

	public void setPowerTier(ResourceLocation powerId, int tier) {
		if (powerId == null)
			return;
		int maxAllowed = getMaxTierForPower(powerId);
		int clamped = Math.max(1, Math.min(tier, maxAllowed));
		unlockedPowerTiers.put(powerId, clamped);
		setPowerLevel(powerId, clamped);
		if (clamped == 2) {
			powerUsageCounts.put(powerId, Math.max(powerUsageCounts.getOrDefault(powerId, 0), TIER_2_USAGE_REQ));
		} else if (clamped == 3) {
			powerUsageCounts.put(powerId, Math.max(powerUsageCounts.getOrDefault(powerId, 0), TIER_3_USAGE_REQ));
		}
	}

	public void maxAllPowers() {
		this.forceSensitivity = true;
		this.forceRank = Math.max(this.forceRank, 4);
		for (ResourceLocation power : ALL_ACTIVE_POWERS) {
			unlockSkill(power);
			int maxT = getMaxTierForPower(power);
			unlockedPowerTiers.put(power, maxT);
			setPowerLevel(power, maxT);
			powerUsageCounts.put(power, 500);
			powerMasteryTicks.put(power, MASTERY_MAX_TICKS);
		}
		this.forceEnergy = getMaxForceEnergy();
	}
}