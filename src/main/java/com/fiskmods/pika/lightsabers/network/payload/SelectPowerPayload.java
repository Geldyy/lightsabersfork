package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SelectPowerPayload(ResourceLocation powerId, int level) implements CustomPacketPayload {

	public static final Type<SelectPowerPayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "select_power"));

	public static final StreamCodec<ByteBuf, SelectPowerPayload> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, SelectPowerPayload::powerId, ByteBufCodecs.VAR_INT,
			SelectPowerPayload::level, SelectPowerPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SelectPowerPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				PlayerForceData data = PlayerForceData.get(player);
				if (data != null && data.isForceSensitive()) {

					if (data.getAvailableActivePowers().contains(payload.powerId())) {
						data.setSelectedPower(payload.powerId());
						data.setPowerLevel(payload.powerId(), payload.level());
						data.sync(player);
					}
				}
			}
		});
	}
}