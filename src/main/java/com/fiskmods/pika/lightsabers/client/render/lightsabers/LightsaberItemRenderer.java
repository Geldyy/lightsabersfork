package com.fiskmods.pika.lightsabers.client.render.lightsabers;

import java.util.List;

import org.joml.Matrix4f;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.client.render.HiltManager;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.HiltSetModel;
import com.fiskmods.pika.lightsabers.item.crystals.FocusingCrystalType;
import com.fiskmods.pika.lightsabers.item.crystals.ItemColorCrystal;
import com.fiskmods.pika.lightsabers.item.crystals.ItemFocusingCrystal;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class LightsaberItemRenderer extends BlockEntityWithoutLevelRenderer {

	private static final ResourceLocation WHITE_TEXTURE = ResourceLocation
			.withDefaultNamespace("textures/misc/white.png");

	private static final float[] COS = new float[8];
	private static final float[] SIN = new float[8];

	static {
		for (int i = 0; i < 8; i++) {
			float angle = (float) (i * Math.PI / 4.0);
			COS[i] = Mth.cos(angle);
			SIN[i] = Mth.sin(angle);
		}
	}

	public LightsaberItemRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
			MultiBufferSource buffer, int packedLight, int packedOverlay) {
		LightsaberData data = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
		if (data == null) {
			data = LightsaberData.createDefault();
		}

		boolean isDoubleBladed = data.isDoubleBladed();

		boolean isSingleMode = stack.getOrDefault(ALDataComponents.BLADE_MODE.get(), false);

		poseStack.pushPose();
		float scale = 0.185F;

		switch (displayContext) {
		case GUI -> {
			poseStack.translate(0.5F, 0.5F, 0.5F);
			poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
			if (isDoubleBladed) {
				poseStack.scale(0.12F, 0.12F, 0.12F);
				poseStack.translate(0.0F, 0.0F, 0.0F);
			} else {
				poseStack.scale(0.20F, 0.20F, 0.20F);
				poseStack.translate(0.0F, -1.3F, 0.0F);
			}
		}
		case GROUND -> {
			poseStack.translate(0.5F, 0.28F, 0.5F);
			poseStack.scale(scale, scale, scale);
		}
		case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
			float fpY = isDoubleBladed ? 0.38F : 0.15F;
			poseStack.translate(0.5F, fpY, 0.5F);
			poseStack.scale(scale, scale, scale);

			if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isUsingItem()) {
				ItemStack useItem = Minecraft.getInstance().player.getUseItem();
				if (useItem.getItem() instanceof ItemLightsaber) {
					poseStack.translate(-0.35F, 0.15F, -0.3F);
					poseStack.mulPose(Axis.YP.rotationDegrees(-60.0F));
					poseStack.mulPose(Axis.XP.rotationDegrees(-40.0F));
				}
			}
		}
		case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
			float tpY = isDoubleBladed ? 0.38F : 0.12F;
			poseStack.translate(0.5F, tpY, 0.5F);
			poseStack.scale(scale, scale, scale);
		}
		case FIXED, NONE -> {
			poseStack.translate(0.5F, 0.5F, 0.5F);
			poseStack.scale(scale, scale, scale);
		}
		}

		float currentY = 0.0F;
		float lowerSectionHeight = 0.0F;
		float lowerGripH = 0.0F;
		float lowerSwitchH = 0.0F;
		float lowerEmitterH = 0.0F;

		HiltSetModel emitterSet = null;
		HiltSetModel lowerEmitterSet = null;
		float emitterBaseY = 0.0F;

		if (isDoubleBladed) {
			Item secGrip = data.getSecondaryGrip();
			Item secSwitch = data.getSecondarySwitch();
			Item secEmitter = data.pommel();

			poseStack.pushPose();
			poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

			if (secGrip != null && secGrip != Items.AIR) {
				lowerGripH = renderHiltPart(secGrip, PartType.BODY, data, poseStack, buffer, packedLight,
						packedOverlay);
				poseStack.translate(0.0F, lowerGripH * 0.0625F, 0.0F);
			}

			if (secSwitch != null && secSwitch != Items.AIR) {
				lowerSwitchH = renderHiltPart(secSwitch, PartType.SWITCH_SECTION, data, poseStack, buffer, packedLight,
						packedOverlay);
				poseStack.translate(0.0F, lowerSwitchH * 0.0625F, 0.0F);
			}

			if (secEmitter != null && secEmitter != Items.AIR) {
				lowerEmitterSet = HiltManager.getSet(secEmitter);
				lowerEmitterH = renderHiltPart(secEmitter, PartType.EMITTER, data, poseStack, buffer, packedLight,
						packedOverlay);
			}
			poseStack.popPose();

			lowerSectionHeight = (lowerGripH + lowerSwitchH + lowerEmitterH) * 0.0625F;

			if (data.grip() != null && data.grip() != Items.AIR) {
				poseStack.pushPose();
				poseStack.translate(0.0F, currentY, 0.0F);
				float height = renderHiltPart(data.grip(), PartType.BODY, data, poseStack, buffer, packedLight,
						packedOverlay);
				poseStack.popPose();
				currentY += height * 0.0625F;
			}

			if (data.switchSection() != null && data.switchSection() != Items.AIR) {
				poseStack.pushPose();
				poseStack.translate(0.0F, currentY, 0.0F);
				float height = renderHiltPart(data.switchSection(), PartType.SWITCH_SECTION, data, poseStack, buffer,
						packedLight, packedOverlay);
				poseStack.popPose();
				currentY += height * 0.0625F;
			}

			emitterBaseY = currentY;
			if (data.emitter() != null && data.emitter() != Items.AIR) {
				emitterSet = HiltManager.getSet(data.emitter());
				poseStack.pushPose();
				poseStack.translate(0.0F, currentY, 0.0F);
				float height = renderHiltPart(data.emitter(), PartType.EMITTER, data, poseStack, buffer, packedLight,
						packedOverlay);
				poseStack.popPose();
				currentY += height * 0.0625F;
			}

		} else {
			if (data.pommel() != null && data.pommel() != Items.AIR) {
				poseStack.pushPose();
				poseStack.translate(0.0F, currentY, 0.0F);
				float height = renderHiltPart(data.pommel(), PartType.POMMEL, data, poseStack, buffer, packedLight,
						packedOverlay);
				poseStack.popPose();
				currentY += height * 0.0625F;
			}

			if (data.grip() != null && data.grip() != Items.AIR) {
				poseStack.pushPose();
				poseStack.translate(0.0F, currentY, 0.0F);
				float height = renderHiltPart(data.grip(), PartType.BODY, data, poseStack, buffer, packedLight,
						packedOverlay);
				poseStack.popPose();
				currentY += height * 0.0625F;
			}

			if (data.switchSection() != null && data.switchSection() != Items.AIR) {
				poseStack.pushPose();
				poseStack.translate(0.0F, currentY, 0.0F);
				float height = renderHiltPart(data.switchSection(), PartType.SWITCH_SECTION, data, poseStack, buffer,
						packedLight, packedOverlay);
				poseStack.popPose();
				currentY += height * 0.0625F;
			}

			emitterBaseY = currentY;
			if (data.emitter() != null && data.emitter() != Items.AIR) {
				emitterSet = HiltManager.getSet(data.emitter());
				poseStack.pushPose();
				poseStack.translate(0.0F, currentY, 0.0F);
				float height = renderHiltPart(data.emitter(), PartType.EMITTER, data, poseStack, buffer, packedLight,
						packedOverlay);
				poseStack.popPose();
				currentY += height * 0.0625F;
			}
		}

		boolean isActive = stack.getOrDefault(ALDataComponents.IS_ACTIVE.get(), false);
		if (isActive && displayContext != ItemDisplayContext.GUI
				&& data.colorCrystal() instanceof ItemColorCrystal colorCrystal) {
			float bladeEmitterOffset = 0.22F;

			poseStack.pushPose();
			poseStack.translate(0.0F, currentY - bladeEmitterOffset, 0.0F);
			renderBlade(poseStack, buffer, colorCrystal, data.focusCrystals(), displayContext, 1.0F,
					bladeEmitterOffset);
			poseStack.popPose();

			if (emitterSet != null && emitterSet.hasCrossguard()) {
				float[] cg = emitterSet.getCrossguard(data);
				float cgDist = cg[0] * 0.0625F;
				float cgY = emitterBaseY + (cg[1] * 0.0625F);
				float cgScale = cg.length > 2 ? cg[2] : 0.3F;

				poseStack.pushPose();
				poseStack.translate(0.0F, cgY, cgDist);
				poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
				renderBlade(poseStack, buffer, colorCrystal, data.focusCrystals(), displayContext, cgScale, 0.05F);
				poseStack.popPose();

				poseStack.pushPose();
				poseStack.translate(0.0F, cgY, -cgDist);
				poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
				renderBlade(poseStack, buffer, colorCrystal, data.focusCrystals(), displayContext, cgScale, 0.05F);
				poseStack.popPose();
			}

			if (isDoubleBladed && !isSingleMode
					&& data.getSecondaryColorCrystal() instanceof ItemColorCrystal secColorCrystal) {
				List<Item> secFocus = data.getSecondaryFocusCrystals();

				poseStack.pushPose();
				poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
				float lowerY = lowerSectionHeight - bladeEmitterOffset;
				poseStack.translate(0.0F, lowerY, 0.0F);
				renderBlade(poseStack, buffer, secColorCrystal, secFocus, displayContext, 1.0F, bladeEmitterOffset);
				poseStack.popPose();

				if (lowerEmitterSet != null && lowerEmitterSet.hasCrossguard()) {
					float[] cg = lowerEmitterSet.getCrossguard(data);
					float cgDist = cg[0] * 0.0625F;
					float cgY = ((lowerGripH + lowerSwitchH) * 0.0625F) + (cg[1] * 0.0625F);
					float cgScale = cg.length > 2 ? cg[2] : 0.3F;

					poseStack.pushPose();
					poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

					poseStack.pushPose();
					poseStack.translate(0.0F, cgY, cgDist);
					poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
					renderBlade(poseStack, buffer, secColorCrystal, secFocus, displayContext, cgScale, 0.05F);
					poseStack.popPose();

					poseStack.pushPose();
					poseStack.translate(0.0F, cgY, -cgDist);
					poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
					renderBlade(poseStack, buffer, secColorCrystal, secFocus, displayContext, cgScale, 0.05F);
					poseStack.popPose();

					poseStack.popPose();
				}
			}
		}

		poseStack.popPose();
	}

	private float renderHiltPart(Item part, PartType type, LightsaberData data, PoseStack poseStack,
			MultiBufferSource buffer, int light, int overlay) {
		if (part == null || part == Items.AIR)
			return 0.0F;

		HiltSetModel set = HiltManager.getSet(part);
		if (set == null)
			return 16.0F;

		ModelPart model = set.getPartModel(type, data);
		float height = set.getHeight(type);

		if (model != null) {
			ResourceLocation texture = set.getTexture(type);
			VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
			model.render(poseStack, consumer, light, overlay);
		}

		return height;
	}

	private void renderBlade(PoseStack poseStack, MultiBufferSource buffer, ItemColorCrystal colorCrystal,
			List<Item> focusCrystals, ItemDisplayContext displayContext, float lengthScale, float emitterOffset) {
		poseStack.pushPose();

		boolean isFirstPerson = displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
				|| displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;

		float baseBladeLength = 5.8F * lengthScale;
		float tipLength = (isFirstPerson ? 0.60F : 0.50F) * Math.min(1.0F, lengthScale);

		float coreRadius = isFirstPerson ? 0.105F : 0.072F;
		float maxAuraRadius = isFirstPerson ? 0.480F : 0.320F;

		if (lengthScale < 0.5F) {
			coreRadius *= 0.8F;
			maxAuraRadius *= 0.8F;
		}

		float r = 1.0F, g = 1.0F, b = 1.0F;
		if (colorCrystal.getColor() != null) {
			float[] rgb = colorCrystal.getColor().getRGB();
			r = rgb[0];
			g = rgb[1];
			b = rgb[2];
		}

		float coreR = 1.0F, coreG = 1.0F, coreB = 1.0F;
		boolean invertColors = false;

		float time = Minecraft.getInstance().level != null ? (float) Minecraft.getInstance().level.getGameTime() : 0F;

		if (focusCrystals != null) {
			for (Item item : focusCrystals) {
				if (!(item instanceof ItemFocusingCrystal focusCrystal))
					continue;
				FocusingCrystalType type = focusCrystal.getCrystalType();
				switch (type) {
				case COMPRESSED -> {
					coreRadius *= 0.75F;
					maxAuraRadius *= 0.78F;
				}
				case FINE_CUT -> poseStack.scale(0.25F, 1.0F, 1.0F);
				case INVERTING -> invertColors = true;
				case PRISMATIC -> {
					float hue = (time * 0.005F) % 1.0F;
					java.awt.Color rgb = java.awt.Color.getHSBColor(hue, 0.9F, 1.0F);
					r = rgb.getRed() / 255.0F;
					g = rgb.getGreen() / 255.0F;
					b = rgb.getBlue() / 255.0F;
				}
				case CRACKED -> poseStack.translate((Math.random() - 0.5) * 0.025, 0, (Math.random() - 0.5) * 0.025);
				case SHOTO -> {
					baseBladeLength *= 0.65F;
					tipLength *= 0.65F;
				}
				case EXTENDED -> {
					baseBladeLength *= 1.42F;
					tipLength *= 1.20F;
				}
				case PULSE -> {
					float pulse = (float) (Math.sin(time * 0.15F) + Math.sin(time * 0.4F));
					maxAuraRadius += (0.020F * pulse);

					if (!isFirstPerson && Math.random() < 0.25) {
						float h = (float) Math.random() * (baseBladeLength + emitterOffset);
						float branchLength = 0.8F + (float) Math.random() * 1.2F;

						poseStack.pushPose();
						poseStack.translate(0, h, 0);
						poseStack.mulPose(Axis.YP.rotationDegrees((float) Math.random() * 360));
						poseStack.mulPose(Axis.ZP.rotationDegrees((float) Math.random() * 60 + 30));

						Matrix4f sparkMatrix = poseStack.last().pose();
						VertexConsumer sparkBuffer = buffer.getBuffer(RenderType.lightning());
						drawPointedOctagon(sparkMatrix, sparkBuffer, 0.015F, branchLength, 0.08F, 0.7F, 0.85F, 1.0F,
								0.9F, 15728880);
						poseStack.popPose();
					}
				}
				case UNSTABLE -> poseStack.translate((Math.random() - 0.5) * 0.035, 0, (Math.random() - 0.5) * 0.035);
				}
			}
		}

		if (invertColors) {
			coreR = 0.0F;
			coreG = 0.0F;
			coreB = 0.0F;
			r = Math.min(1.0F, r + 0.2F);
			g = Math.min(1.0F, g + 0.2F);
			b = Math.min(1.0F, b + 0.2F);
		}

		float bladeLength = baseBladeLength + emitterOffset;
		Matrix4f matrix = poseStack.last().pose();
		int glowLight = 15728880;

		VertexConsumer auraConsumer = buffer.getBuffer(RenderType.entityTranslucent(WHITE_TEXTURE));
		int layers = 85;
		float startRadius = coreRadius * 1.05F;

		for (int i = 0; i < layers; i++) {
			float progress = (float) i / (float) (layers - 1);
			float layerRadius = Mth.lerp(progress, startRadius, maxAuraRadius);
			float layerTip = tipLength * (1.0F + progress * 0.30F);
			float layerAlpha = (0.62F / layers) * (1.0F - (float) Math.pow(progress, 1.15));

			drawPointedOctagon(matrix, auraConsumer, layerRadius, bladeLength, layerTip, r, g, b, layerAlpha,
					glowLight);
		}

		drawPointedOctagon(matrix, auraConsumer, coreRadius, bladeLength - 0.03F, tipLength * 0.90F, coreR, coreG,
				coreB, 1.0F, glowLight);

		poseStack.popPose();
	}

	private void drawPointedOctagon(Matrix4f matrix, VertexConsumer builder, float radius, float length,
			float tipLength, float r, float g, float b, float a, int light) {
		int overlay = OverlayTexture.NO_OVERLAY;
		float shaftLength = Math.max(0.1F, length - tipLength);

		for (int i = 0; i < 8; i++) {
			int next = (i + 1) % 8;
			float x1 = COS[i] * radius;
			float z1 = SIN[i] * radius;
			float x2 = COS[next] * radius;
			float z2 = SIN[next] * radius;

			builder.addVertex(matrix, x1, 0.0F, z1).setColor(r, g, b, a).setUv(0, 0).setOverlay(overlay).setLight(light)
					.setNormal(0, 1, 0);
			builder.addVertex(matrix, x2, 0.0F, z2).setColor(r, g, b, a).setUv(1, 0).setOverlay(overlay).setLight(light)
					.setNormal(0, 1, 0);
			builder.addVertex(matrix, x2, shaftLength, z2).setColor(r, g, b, a).setUv(1, 1).setOverlay(overlay)
					.setLight(light).setNormal(0, 1, 0);
			builder.addVertex(matrix, x1, shaftLength, z1).setColor(r, g, b, a).setUv(0, 1).setOverlay(overlay)
					.setLight(light).setNormal(0, 1, 0);
		}

		for (int i = 0; i < 8; i++) {
			int next = (i + 1) % 8;
			float x1 = COS[i] * radius;
			float z1 = SIN[i] * radius;
			float x2 = COS[next] * radius;
			float z2 = SIN[next] * radius;

			builder.addVertex(matrix, x1, shaftLength, z1).setColor(r, g, b, a).setUv(0, 0).setOverlay(overlay)
					.setLight(light).setNormal(0, 1, 0);
			builder.addVertex(matrix, x2, shaftLength, z2).setColor(r, g, b, a).setUv(1, 0).setOverlay(overlay)
					.setLight(light).setNormal(0, 1, 0);
			builder.addVertex(matrix, 0.0F, length, 0.0F).setColor(r, g, b, a).setUv(0.5F, 1.0F).setOverlay(overlay)
					.setLight(light).setNormal(0, 1, 0);
			builder.addVertex(matrix, 0.0F, length, 0.0F).setColor(r, g, b, a).setUv(0.5F, 1.0F).setOverlay(overlay)
					.setLight(light).setNormal(0, 1, 0);
		}
	}
}