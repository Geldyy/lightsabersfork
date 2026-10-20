package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.util.ServerRangeStates;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleRangeBoostPayload(boolean disabled) implements CustomPacketPayload {

	public static final Type<ToggleRangeBoostPayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "toggle_range_boost"));

	public static final StreamCodec<ByteBuf, ToggleRangeBoostPayload> STREAM_CODEC = StreamCodec
			.composite(ByteBufCodecs.BOOL, ToggleRangeBoostPayload::disabled, ToggleRangeBoostPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(ToggleRangeBoostPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				ServerRangeStates.setDisabled(player, payload.disabled());

				AdvancedLightsabers.LOGGER.info("Range boost for {} set to: Disabled = {}",
						player.getName().getString(), payload.disabled());
			}
		});
	}
}