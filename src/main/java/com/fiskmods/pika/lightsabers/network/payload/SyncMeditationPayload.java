package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.logic.MeditationClientTracker;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncMeditationPayload(int entityId, boolean isMeditating, boolean isFloating)
		implements CustomPacketPayload {

	public static final Type<SyncMeditationPayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "sync_meditation"));

	public static final StreamCodec<ByteBuf, SyncMeditationPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SyncMeditationPayload::entityId,
			ByteBufCodecs.BOOL, SyncMeditationPayload::isMeditating,
			ByteBufCodecs.BOOL, SyncMeditationPayload::isFloating,
			SyncMeditationPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SyncMeditationPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			MeditationClientTracker.setMeditating(payload.entityId(), payload.isMeditating(), payload.isFloating());
		});
	}
}
