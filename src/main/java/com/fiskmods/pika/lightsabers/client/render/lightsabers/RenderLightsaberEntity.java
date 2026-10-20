package com.fiskmods.pika.lightsabers.client.render.lightsabers;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.entity.EntityLightsaber;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaberPart;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderLightsaberEntity extends EntityRenderer<EntityLightsaber> {

	public RenderLightsaberEntity(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(EntityLightsaber entity, float entityYaw, float partialTicks, PoseStack poseStack,
			MultiBufferSource buffer, int packedLight) {
		ItemStack stack = entity.getItem();
		if (stack.isEmpty())
			return;

		poseStack.pushPose();

		poseStack.translate(0.0F, 0.25F, 0.0F);

		float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
		float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
		poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 90.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(pitch));

		poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));

		float spin = (entity.tickCount + partialTicks) * 75.0F;
		poseStack.mulPose(Axis.XP.rotationDegrees(spin));

		LightsaberData data = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
		boolean isDoubleBladed = data != null && data.pommel() instanceof ItemLightsaberPart p
				&& p.getPartType() == PartType.EMITTER;
		float yOffset = isDoubleBladed ? 0.18F : 0.35F;

		poseStack.translate(-0.5F, -yOffset, -0.5F);

		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, packedLight,
				net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(),
				entity.getId());

		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityLightsaber entity) {
		return net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS;
	}
}