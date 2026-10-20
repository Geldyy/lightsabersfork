package com.fiskmods.pika.lightsabers.advancements;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID)
public class ALAdvancements {

	public static void grant(ServerPlayer player, String advancementName) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
				"lightsabers/" + advancementName);
		AdvancementHolder holder = player.server.getAdvancements().get(id);

		if (holder != null) {
			AdvancementProgress progress = player.getAdvancements().getOrStartProgress(holder);
			if (!progress.isDone()) {
				for (String criterion : progress.getRemainingCriteria()) {
					player.getAdvancements().award(holder, criterion);
				}
			}
		}
	}

	public static boolean hasAdvancement(ServerPlayer player, String advancementName) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
				"lightsabers/" + advancementName);
		AdvancementHolder holder = player.server.getAdvancements().get(id);
		if (holder != null) {
			return player.getAdvancements().getOrStartProgress(holder).isDone();
		}
		return false;
	}

	@SubscribeEvent
	public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}

		ResourceLocation id = event.getAdvancement().id();
		if (!id.getNamespace().equals(AdvancedLightsabers.MODID)) {
			return;
		}

		String path = id.getPath().replace("lightsabers/", "");
		int baseFp = 0;
		int baseAp = 0;

		switch (path) {
		case "combat_master" -> baseAp = 1;
		case "find_holocron" -> baseFp = 1;
		case "double_saber" -> baseAp = 2;
		case "jedi_first_step", "sith_first_step" -> baseFp = 1;
		case "jedi_all_skills", "sith_all_skills" -> baseFp = 2;
		case "jedi_grandmaster", "sith_emperor" -> {
			baseFp = 3;
			baseAp = 2;
		}
		case "chosen_one" -> {
			baseFp = 5;
			baseAp = 5;
			PlayerForceData d = PlayerForceData.get(player);
			if (d != null) {
				d.setHasChosenOne(true);
			}
		}
		}

		int actualFp = baseFp * 3;
		int actualAp = baseAp * 2;

		if (actualFp > 0 || actualAp > 0) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null) {
				if (actualFp > 0) {
					data.addForcePoints(actualFp);
				}
				if (actualAp > 0) {
					data.addAbilityPoints(actualAp);
				}
				data.sync(player);

				MutableComponent rewardComp = Component.translatable("message.lightsabersfork.advancement_bonus");
				if (actualFp > 0) {
					rewardComp.append(Component.translatable("message.lightsabersfork.advancement_bonus.fp", actualFp));
				}
				if (actualFp > 0 && actualAp > 0) {
					rewardComp.append(Component.translatable("message.lightsabersfork.advancement_bonus.and"));
				}
				if (actualAp > 0) {
					rewardComp.append(Component.translatable("message.lightsabersfork.advancement_bonus.ap", actualAp));
				}

				player.displayClientMessage(rewardComp, false);
				player.playNotifySound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 0.8F, 1.2F);
			}
		}
	}

	public static void checkForceAdvancements(ServerPlayer player, PlayerForceData data) {
		int jediRank = data.getJediRank();
		int sithRank = data.getSithRank();

		if (jediRank >= 1) {
			grant(player, "jedi_first_step");
		}
		if (jediRank >= 4) {
			grant(player, "jedi_all_skills");
		}
		if (jediRank >= 6) {
			grant(player, "jedi_grandmaster");
		}

		if (sithRank >= 1) {
			grant(player, "sith_first_step");
		}
		if (sithRank >= 4) {
			grant(player, "sith_all_skills");
		}
		if (sithRank >= 6) {
			grant(player, "sith_emperor");
		}

		ResourceLocation jediMax = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
				"lightsabers/jedi_grandmaster");
		ResourceLocation sithMax = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
				"lightsabers/sith_emperor");

		AdvancementHolder jediHolder = player.server.getAdvancements().get(jediMax);
		AdvancementHolder sithHolder = player.server.getAdvancements().get(sithMax);

		if (jediHolder != null && sithHolder != null) {
			boolean hasJedi = player.getAdvancements().getOrStartProgress(jediHolder).isDone();
			boolean hasSith = player.getAdvancements().getOrStartProgress(sithHolder).isDone();

			if (hasJedi && hasSith) {
				grant(player, "chosen_one");
			}
		}
	}
}