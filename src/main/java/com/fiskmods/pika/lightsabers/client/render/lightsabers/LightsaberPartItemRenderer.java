package com.fiskmods.pika.lightsabers.client.render.lightsabers;

import com.fiskmods.pika.lightsabers.client.render.HiltManager;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.HiltSetModel;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaberPart;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LightsaberPartItemRenderer extends BlockEntityWithoutLevelRenderer {

	public static final LightsaberPartItemRenderer INSTANCE = new LightsaberPartItemRenderer();

	public LightsaberPartItemRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
			MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (!(stack.getItem() instanceof ItemLightsaberPart partItem))
			return;

		Item part = stack.getItem();
		PartType type = partItem.getPartType();
		HiltSetModel setModel = HiltManager.getSet(part);

		if (setModel == null)
			return;

		ModelPart model = setModel.getPartModel(type);
		if (model == null)
			return;

		poseStack.pushPose();

		float height = setModel.getHeight(type);

		switch (displayContext) {
		case GUI -> {
			poseStack.translate(0.5F, 0.5F, 0.5F);
			poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
			poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));

			float guiScale = (type == PartType.POMMEL && height <= 4.0F) ? 0.35F : 0.22F;
			poseStack.scale(guiScale, guiScale, guiScale);
			poseStack.translate(0.0F, (height * 0.0625F) / 2.0F, 0.0F);
		}
		case GROUND -> {
			poseStack.translate(0.5F, 0.2F, 0.5F);
			poseStack.scale(0.25F, 0.25F, 0.25F);
		}
		case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
			poseStack.translate(0.5F, 0.4F, 0.5F);
			poseStack.scale(0.3F, 0.3F, 0.3F);
		}
		case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
			poseStack.translate(0.5F, 0.4F, 0.5F);
			poseStack.scale(0.22F, 0.22F, 0.22F);
		}
		default -> {
			poseStack.translate(0.5F, 0.5F, 0.5F);
			poseStack.scale(0.25F, 0.25F, 0.25F);
		}
		}

		poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

		ResourceLocation texture = setModel.getTexture(type);

		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
		model.render(poseStack, consumer, packedLight, packedOverlay);

		poseStack.popPose();
	}
}