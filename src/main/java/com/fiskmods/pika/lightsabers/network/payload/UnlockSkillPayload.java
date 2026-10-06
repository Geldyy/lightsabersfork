package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UnlockSkillPayload(ResourceLocation skillId) implements CustomPacketPayload {
    public static final Type<UnlockSkillPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "unlock_skill"));

    public static final StreamCodec<ByteBuf, UnlockSkillPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, UnlockSkillPayload::skillId,
            UnlockSkillPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}