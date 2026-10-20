package com.fiskmods.pika.lightsabers.network;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillNode;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;
import com.fiskmods.pika.lightsabers.network.payload.BroadcastLightningPayload;
import com.fiskmods.pika.lightsabers.network.payload.CycleForcePayload;
import com.fiskmods.pika.lightsabers.network.payload.DoubleJumpPayload;
import com.fiskmods.pika.lightsabers.network.payload.HolocronUsagePayload;
import com.fiskmods.pika.lightsabers.network.payload.MeditationTogglePayload;
import com.fiskmods.pika.lightsabers.network.payload.SelectPowerPayload;
import com.fiskmods.pika.lightsabers.network.payload.SyncForceDataPayload;
import com.fiskmods.pika.lightsabers.network.payload.ThrowLightsaberPayload;
import com.fiskmods.pika.lightsabers.network.payload.ToggleLightsaberPayload;
import com.fiskmods.pika.lightsabers.network.payload.ToggleRangeBoostPayload;
import com.fiskmods.pika.lightsabers.network.payload.UnlockSkillPayload;
import com.fiskmods.pika.lightsabers.network.payload.UseForcePayload;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID)
public class ALNetwork {

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(AdvancedLightsabers.MODID).versioned("1.0");

		registrar.playToServer(ToggleLightsaberPayload.TYPE, ToggleLightsaberPayload.STREAM_CODEC,
				ToggleLightsaberPayload::handle);
		registrar.playToServer(ToggleRangeBoostPayload.TYPE, ToggleRangeBoostPayload.STREAM_CODEC,
				ToggleRangeBoostPayload::handle);

		registrar.playToServer(ThrowLightsaberPayload.TYPE, ThrowLightsaberPayload.CODEC,
				ThrowLightsaberPayload::handle);
		registrar.playToServer(DoubleJumpPayload.TYPE, DoubleJumpPayload.STREAM_CODEC, DoubleJumpPayload::handle);
		registrar.playToServer(com.fiskmods.pika.lightsabers.network.payload.ChargedJumpPayload.TYPE,
				com.fiskmods.pika.lightsabers.network.payload.ChargedJumpPayload.STREAM_CODEC,
				com.fiskmods.pika.lightsabers.network.payload.ChargedJumpPayload::handle);
		registrar.playToClient(BroadcastLightningPayload.TYPE, BroadcastLightningPayload.STREAM_CODEC,
				BroadcastLightningPayload::handle);
		registrar.playToServer(HolocronUsagePayload.TYPE, HolocronUsagePayload.STREAM_CODEC,
				HolocronUsagePayload::handle);
		registrar.playBidirectional(MeditationTogglePayload.TYPE, MeditationTogglePayload.STREAM_CODEC,
				MeditationTogglePayload::handle);
		registrar.playToServer(UnlockSkillPayload.TYPE, UnlockSkillPayload.STREAM_CODEC, ALNetwork::handleUnlockSkill);
		registrar.playToClient(SyncForceDataPayload.TYPE, SyncForceDataPayload.STREAM_CODEC,
				ALNetwork::handleSyncForceData);
		registrar.playToServer(CycleForcePayload.TYPE, CycleForcePayload.STREAM_CODEC, CycleForcePayload::handle);
		registrar.playToServer(UseForcePayload.TYPE, UseForcePayload.STREAM_CODEC, UseForcePayload::handle);
		registrar.playToServer(SelectPowerPayload.TYPE, SelectPowerPayload.STREAM_CODEC, SelectPowerPayload::handle);
		registrar.playToClient(com.fiskmods.pika.lightsabers.network.payload.SyncBarrierPayload.TYPE,
				com.fiskmods.pika.lightsabers.network.payload.SyncBarrierPayload.STREAM_CODEC,
				com.fiskmods.pika.lightsabers.network.payload.SyncBarrierPayload::handle);
		registrar.playToClient(com.fiskmods.pika.lightsabers.network.payload.SyncMeditationPayload.TYPE,
				com.fiskmods.pika.lightsabers.network.payload.SyncMeditationPayload.STREAM_CODEC,
				com.fiskmods.pika.lightsabers.network.payload.SyncMeditationPayload::handle);
		registrar.playToServer(com.fiskmods.pika.lightsabers.network.payload.TelekinesisScrollPayload.TYPE,
				com.fiskmods.pika.lightsabers.network.payload.TelekinesisScrollPayload.STREAM_CODEC,
				com.fiskmods.pika.lightsabers.network.payload.TelekinesisScrollPayload::handle);
	}

	public static void handleUnlockSkill(UnlockSkillPayload payload,
			net.neoforged.neoforge.network.handling.IPayloadContext context) {
		context.enqueueWork(() -> {
			Player player = context.player();
			if (!(player instanceof ServerPlayer serverPlayer))
				return;

			PlayerForceData data = PlayerForceData.get(serverPlayer);
			if (data == null)
				return;

			SkillNode node = SkillTreeRegistry.getNode(payload.skillId());
			if (node == null)
				return;

			if (data.hasSkill(payload.skillId())) {
				if (data.canUpgradePowerTier(payload.skillId())) {
					int cost = data.getUpgradePointCost(payload.skillId());
					if (data.consumeForcePoints(cost)) {
						data.upgradePowerTier(payload.skillId());
						serverPlayer.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.2F);
						data.sync(serverPlayer);
					}
				}
				return;
			}

			if (!node.canUnlock(serverPlayer, data)) {
				return;
			}

			boolean success = false;
			if (node.getCategory() == TreeCategory.FORCE) {
				int cost;
				if (data.isUnifiedTreeUnlocked()) {
					cost = SkillTreeRegistry.getUnifiedPairCost(payload.skillId(), data);
				} else {
					boolean isSithHolocron = serverPlayer.getPersistentData().getBoolean("AtSithHolocron");
					cost = node.getPointCost();

					if (node.isJediSkill() && isSithHolocron) {
						cost = Math.round(cost * 2.0F);
					} else if (node.isSithSkill() && !isSithHolocron) {
						cost = Math.round(cost * 2.5F);
					}
				}

				success = data.consumeForcePoints(cost);
			} else if (node.getCategory() == TreeCategory.LIGHTSABER) {
				success = data.consumeAbilityPoints(node.getPointCost());
			}

			if (success) {
				data.unlockSkill(payload.skillId());
				if (data.isUnifiedTreeUnlocked() && node.getCategory() == TreeCategory.FORCE) {
					ResourceLocation counterpart = SkillTreeRegistry.getUnifiedCounterpart(payload.skillId());
					if (counterpart != null) {
						data.unlockSkill(counterpart);
					}
				}
				serverPlayer.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
				data.sync(serverPlayer);
				com.fiskmods.pika.lightsabers.advancements.ALAdvancements.checkForceAdvancements(serverPlayer, data);
			}
		});
	}

	public static void handleSyncForceData(SyncForceDataPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			Player player = context.player();
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null) {
				data.deserializeNBT(player.registryAccess(), payload.dataTag());
				com.fiskmods.pika.lightsabers.client.gui.ForceHudOverlay.onDataSynced(data);

				if (com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager.isMeditating()) {
					ResourceLocation currentSelected = com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager
							.getSelectedNodeId();
					if (SkillTreeRegistry.isNeutralRankNode(currentSelected)) {
						com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager
								.setSelectedNodeId(SkillTreeRegistry.getActiveNeutralRankNode(data));
					}
				}
			}
		});
	}
}