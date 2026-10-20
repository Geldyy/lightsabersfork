package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.block.entity.HolocronBlockEntity;
import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MeditationTogglePayload(boolean isMeditating, BlockPos holocronPos, boolean isSith)
		implements CustomPacketPayload {

	public static final Type<MeditationTogglePayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "meditation_toggle"));

	public static final StreamCodec<ByteBuf, MeditationTogglePayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, MeditationTogglePayload::isMeditating, BlockPos.STREAM_CODEC,
			MeditationTogglePayload::holocronPos, ByteBufCodecs.BOOL, MeditationTogglePayload::isSith,
			MeditationTogglePayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(final MeditationTogglePayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.flow().isServerbound()) {
				if (context.player() instanceof ServerPlayer player) {
					com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData data = com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData.get(player);
					boolean isFloating = data != null && data.isForceSensitive();
					boolean isFree = payload.holocronPos() == null || payload.holocronPos().equals(BlockPos.ZERO);

					if (payload.isMeditating()) {
						if (isFree) {
							if (com.fiskmods.pika.lightsabers.events.LightsaberCombatEvents.isInCombatCooldown(player)) {
								int remaining = com.fiskmods.pika.lightsabers.events.LightsaberCombatEvents.getRemainingCombatCooldownSeconds(player);
								player.displayClientMessage(
										net.minecraft.network.chat.Component.translatable("message.lightsabersfork.meditation_combat_cooldown", remaining)
												.withStyle(net.minecraft.ChatFormatting.RED), true);
								player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
										net.minecraft.sounds.SoundEvents.VILLAGER_NO, net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 1.0F);
								return;
							}
							player.getPersistentData().putBoolean("IsFreeMeditation", true);
							player.getPersistentData().putBoolean("IsMeditating", true);

							net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
									new SyncMeditationPayload(player.getId(), true, false));
							net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
									new MeditationTogglePayload(true, BlockPos.ZERO, false));
						} else {
							player.getPersistentData().putBoolean("AtSithHolocron", payload.isSith());
							player.getPersistentData().putBoolean("IsMeditating", true);

							BlockPos hPos = payload.holocronPos();
							if (hPos != null && player.level().isLoaded(hPos)) {
								BlockEntity be = player.level().getBlockEntity(hPos);
								if (be instanceof HolocronBlockEntity holocron) {
									holocron.setPlayerUsing(player, true);
									player.getPersistentData().putLong("ActiveHolocronPos", hPos.asLong());
								}
							}
							net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
									new SyncMeditationPayload(player.getId(), true, isFloating));
						}
					} else {
						player.getPersistentData().remove("AtSithHolocron");
						player.getPersistentData().remove("IsMeditating");
						player.getPersistentData().remove("IsFreeMeditation");

						if (player.getPersistentData().contains("ActiveHolocronPos")) {
							BlockPos hPos = BlockPos.of(player.getPersistentData().getLong("ActiveHolocronPos"));
							if (player.level().isLoaded(hPos)) {
								BlockEntity be = player.level().getBlockEntity(hPos);
								if (be instanceof HolocronBlockEntity holocron) {
									holocron.setPlayerUsing(player, false);
								}
							}
							player.getPersistentData().remove("ActiveHolocronPos");
						}
						net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
								new SyncMeditationPayload(player.getId(), false, false));
					}
				}
			} else {
				if (payload.isMeditating()) {
					if (MeditationStateManager.canOpen()) {
						boolean isFree = payload.holocronPos() == null || payload.holocronPos().equals(BlockPos.ZERO);
						if (isFree) {
							MeditationStateManager.startFreeMeditation(context.player());
						} else {
							MeditationStateManager.startMeditation(context.player(), payload.holocronPos(),
									payload.isSith());
						}
					}
				} else {
					MeditationStateManager.stopMeditation();
				}
			}
		});
	}
}