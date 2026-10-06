package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BroadcastLightningPayload(int entityId) implements CustomPacketPayload {

    public static final Type<BroadcastLightningPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "broadcast_lightning")
    );

    public static final StreamCodec<ByteBuf, BroadcastLightningPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, BroadcastLightningPayload::entityId,
            BroadcastLightningPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final BroadcastLightningPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
           // ForceLightningRenderer.trigger(payload.entityId());
        });
    }
}