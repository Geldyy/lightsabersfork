package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncForceDataPayload(CompoundTag dataTag) implements CustomPacketPayload {
    public static final Type<SyncForceDataPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "sync_force_data"));

    public static final StreamCodec<ByteBuf, SyncForceDataPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, SyncForceDataPayload::dataTag,
            SyncForceDataPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}