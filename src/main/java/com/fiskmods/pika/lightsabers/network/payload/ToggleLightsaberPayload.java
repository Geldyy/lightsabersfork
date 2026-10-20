package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALSounds;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleLightsaberPayload(boolean toggleBladeMode) implements CustomPacketPayload {

	public static final Type<ToggleLightsaberPayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "toggle_lightsaber"));

	public static final StreamCodec<ByteBuf, ToggleLightsaberPayload> STREAM_CODEC = StreamCodec
			.composite(ByteBufCodecs.BOOL, ToggleLightsaberPayload::toggleBladeMode, ToggleLightsaberPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(ToggleLightsaberPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				ItemStack stack = player.getMainHandItem();
				InteractionHand hand = InteractionHand.MAIN_HAND;

				if (!(stack.getItem() instanceof ItemLightsaber)) {
					stack = player.getOffhandItem();
					hand = InteractionHand.OFF_HAND;
				}

				if (stack.getItem() instanceof ItemLightsaber) {
					PlayerForceData data = PlayerForceData.get(player);

					if (payload.toggleBladeMode()) {

						if (data == null || !data.hasSkill(
								ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "db_single_switch"))) {
							return;
						}

						boolean currentMode = stack.getOrDefault(ALDataComponents.BLADE_MODE.get(), false);
						stack.set(ALDataComponents.BLADE_MODE.get(), !currentMode);

						boolean isActive = ItemLightsaber.isActive(stack);
						stack.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemLightsaber.createAttributes(stack, isActive));

						player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
								ALSounds.LIGHTSABER_SWING.get(), SoundSource.PLAYERS, 0.6F, 1.5F);
					}

					else {
						boolean currentlyActive = ItemLightsaber.isActive(stack);
						boolean newActive = !currentlyActive;

						ItemLightsaber.setBladeActive(stack, newActive);

						player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
								newActive ? ALSounds.LIGHTSABER_ON.get() : ALSounds.LIGHTSABER_OFF.get(),
								SoundSource.PLAYERS, 1.0F, 1.0F);
					}
				}
			}
		});
	}
}