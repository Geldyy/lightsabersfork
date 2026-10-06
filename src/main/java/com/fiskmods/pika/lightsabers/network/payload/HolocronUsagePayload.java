package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.block.entity.HolocronBlockEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record HolocronUsagePayload(BlockPos pos, boolean isOpening) implements CustomPacketPayload {
    public static final Type<HolocronUsagePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "holocron_usage"));

    public static final StreamCodec<ByteBuf, HolocronUsagePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HolocronUsagePayload::pos,
            ByteBufCodecs.BOOL,
            HolocronUsagePayload::isOpening,
            HolocronUsagePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(HolocronUsagePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (player != null && player.level().isLoaded(payload.pos())) {
                BlockEntity be = player.level().getBlockEntity(payload.pos());
                if (be instanceof HolocronBlockEntity holocron) {
                    holocron.setPlayerUsing(player, payload.isOpening());
                }
            }
        });
    }
}