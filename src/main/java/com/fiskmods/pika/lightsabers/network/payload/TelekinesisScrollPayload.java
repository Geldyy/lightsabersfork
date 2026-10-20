package com.fiskmods.pika.lightsabers.network.payload;

import java.util.List;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.entity.TelekineticBlockEntity;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TelekinesisScrollPayload(float scrollDelta) implements CustomPacketPayload {
	public static final Type<TelekinesisScrollPayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "tk_scroll"));

	public static final StreamCodec<ByteBuf, TelekinesisScrollPayload> STREAM_CODEC = StreamCodec
			.composite(ByteBufCodecs.FLOAT, TelekinesisScrollPayload::scrollDelta, TelekinesisScrollPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(TelekinesisScrollPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				PlayerForceData data = PlayerForceData.get(player);

				if (data != null && data.getTelekineticEntityId() != -1) {
					float curDist = data.getTelekineticDistance();
					float newDist = Mth.clamp(curDist + (payload.scrollDelta() * 0.85F), 2.5F, 18.0F);
					data.setTelekineticDistance(newDist);
					data.sync(player);
					return;
				}

				List<TelekineticBlockEntity> list = player.serverLevel().getEntitiesOfClass(
						TelekineticBlockEntity.class, player.getBoundingBox().inflate(24.0D),
						e -> e.getOwnerId() == player.getId() && !e.isThrown());
				for (TelekineticBlockEntity entity : list) {
					entity.adjustDistance(payload.scrollDelta());
				}
			}
		});
	}
}