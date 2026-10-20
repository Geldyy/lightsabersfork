package com.fiskmods.pika.lightsabers.network.payload;

import java.util.UUID;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.render.force.ForceBarrierRenderer;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncBarrierPayload(UUID casterUUID, int targetEntityId, boolean hasDome, BlockPos domeCenter, int level,
		boolean active) implements CustomPacketPayload {

	public static final Type<SyncBarrierPayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "sync_barrier"));

	public static final StreamCodec<ByteBuf, SyncBarrierPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, p -> p.casterUUID().toString(), ByteBufCodecs.VAR_INT,
			SyncBarrierPayload::targetEntityId, ByteBufCodecs.BOOL, SyncBarrierPayload::hasDome, BlockPos.STREAM_CODEC,
			SyncBarrierPayload::domeCenter, ByteBufCodecs.VAR_INT, SyncBarrierPayload::level, ByteBufCodecs.BOOL,
			SyncBarrierPayload::active,
			(uuidStr, targetId, hasDome, domePos, lvl, active) -> new SyncBarrierPayload(UUID.fromString(uuidStr),
					targetId, hasDome, domePos, lvl, active));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SyncBarrierPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			ForceBarrierRenderer.handleBarrierSync(payload);
		});
	}
}