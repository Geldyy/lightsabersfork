package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.entity.EntityLightsaber;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ThrowLightsaberPayload() implements CustomPacketPayload {
	public static final Type<ThrowLightsaberPayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "throw_lightsaber"));
	public static final StreamCodec<FriendlyByteBuf, ThrowLightsaberPayload> CODEC = StreamCodec
			.unit(new ThrowLightsaberPayload());

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(ThrowLightsaberPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			Player player = context.player();
			PlayerForceData data = PlayerForceData.get(player);

			if (data == null
					|| !data.hasSkill(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "ls_throw"))) {
				return;
			}

			ItemStack mainHand = player.getMainHandItem();
			ItemStack offHand = player.getOffhandItem();

			boolean mainActive = mainHand.getItem() instanceof ItemLightsaber
					&& mainHand.getOrDefault(ALDataComponents.IS_ACTIVE.get(), false);
			boolean offActive = offHand.getItem() instanceof ItemLightsaber
					&& offHand.getOrDefault(ALDataComponents.IS_ACTIVE.get(), false);

			InteractionHand handToThrow = null;
			ItemStack stackToThrow = ItemStack.EMPTY;
			int slotIndex = -1;

			if (mainActive) {
				handToThrow = InteractionHand.MAIN_HAND;
				stackToThrow = mainHand;
				slotIndex = player.getInventory().selected;
			} else if (offActive) {
				handToThrow = InteractionHand.OFF_HAND;
				stackToThrow = offHand;
				slotIndex = -2;
			}

			if (handToThrow != null) {
				EntityLightsaber entity = new EntityLightsaber(player.level(), player, stackToThrow, slotIndex);
				entity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
				entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
				player.level().addFreshEntity(entity);

				player.setItemInHand(handToThrow, ItemStack.EMPTY);
			}
		});
	}
}