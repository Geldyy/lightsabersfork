package com.fiskmods.pika.lightsabers.client.render.force;

import org.joml.Matrix4f;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.logic.MeditationCameraHandler;
import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class MeditationWorldRenderer {

	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
			return;

		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.level == null)
			return;

		float partialTick = event.getPartialTick().getGameTimeDeltaTicks();
		float progress = MeditationCameraHandler.getInterpolatedProgress(partialTick);

		if (!MeditationStateManager.isMeditating() && progress <= 0.001F)
			return;

		float renderScale = MeditationStateManager.isMeditating() ? Math.max(progress, 0.05F) : progress;

		PoseStack poseStack = event.getPoseStack();
		Camera camera = event.getCamera();
		Vec3 camPos = camera.getPosition();

		Vec3 centerPos = MeditationStateManager.getMeditationCenter();
		if (centerPos == null || centerPos.equals(Vec3.ZERO))
			centerPos = mc.player.position();

		poseStack.pushPose();

		double groundY = MeditationStateManager.isFreeMeditation() ? centerPos.y : centerPos.y - 0.5;
		poseStack.translate(centerPos.x - camPos.x, groundY - camPos.y + 0.01, centerPos.z - camPos.z);
		poseStack.scale(renderScale, renderScale, renderScale);

		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

		renderPlatform(poseStack, bufferSource, centerPos);

		renderVoidAbyss(poseStack, bufferSource);

		float reveal = MeditationStateManager.getStelesRevealProgress(partialTick);
		if (reveal > 0.01F) {
			poseStack.pushPose();
			float yOffset = Mth.lerp(reveal, -10.0F, 0.0F);
			poseStack.translate(0, yOffset, 0);

			renderSteles(poseStack, bufferSource, partialTick);

			poseStack.popPose();
		}

		bufferSource.endBatch(RenderType.solid());
		bufferSource.endBatch(RenderType.cutout());
		bufferSource.endBatch(RenderType.cutoutMipped());
		bufferSource.endBatch(RenderType.endPortal());
		bufferSource.endBatch();

		poseStack.popPose();
	}

	private static void renderPlatform(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 centerPos) {
		Minecraft mc = Minecraft.getInstance();

		Block forcestone = BuiltInRegistries.BLOCK
				.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone"));
		Block mossy = BuiltInRegistries.BLOCK
				.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_mossy"));
		Block inscribed = BuiltInRegistries.BLOCK
				.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_inscribed"));

		if (forcestone == Blocks.AIR)
			forcestone = Blocks.SMOOTH_STONE;
		if (mossy == Blocks.AIR)
			mossy = Blocks.MOSSY_COBBLESTONE;
		if (inscribed == Blocks.AIR)
			inscribed = Blocks.CHISELED_STONE_BRICKS;

		poseStack.pushPose();
		int radius = 5;
		RandomSource random = RandomSource.create((long) (centerPos.x * 1337 + centerPos.z * 42));

		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				if (x * x + z * z <= radius * radius) {
					poseStack.pushPose();
					poseStack.translate(x - 0.5, -1.0, z - 0.5);

					BlockState stateToRender = forcestone.defaultBlockState();
					float rand = random.nextFloat();
					if (rand > 0.85f)
						stateToRender = mossy.defaultBlockState();
					else if (rand > 0.7f)
						stateToRender = inscribed.defaultBlockState();

					mc.getBlockRenderer().renderSingleBlock(stateToRender, poseStack, bufferSource, 15728880,
							OverlayTexture.NO_OVERLAY);
					poseStack.popPose();
				}
			}
		}
		poseStack.popPose();
	}

	private static void renderVoidAbyss(PoseStack poseStack, MultiBufferSource bufferSource) {
		poseStack.pushPose();
		poseStack.translate(0.0D, -0.05D, 0.0D);

		Matrix4f mat = poseStack.last().pose();
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.endPortal());

		int segments = 32;
		float innerRadius = 5.0F;
		float outerRadius = 26.0F;

		for (int i = 0; i < segments; i++) {
			float angle1 = (float) (i * 2.0 * Math.PI / segments);
			float angle2 = (float) ((i + 1) * 2.0 * Math.PI / segments);

			float cos1 = Mth.cos(angle1);
			float sin1 = Mth.sin(angle1);
			float cos2 = Mth.cos(angle2);
			float sin2 = Mth.sin(angle2);

			consumer.addVertex(mat, cos1 * innerRadius, 0.0F, sin1 * innerRadius);
			consumer.addVertex(mat, cos1 * outerRadius, 0.0F, sin1 * outerRadius);
			consumer.addVertex(mat, cos2 * outerRadius, 0.0F, sin2 * outerRadius);
			consumer.addVertex(mat, cos2 * innerRadius, 0.0F, sin2 * innerRadius);
		}

		poseStack.popPose();
	}

	private static void renderSteles(PoseStack poseStack, MultiBufferSource bufferSource, float partialTick) {
		TreeCategory activeCategory = MeditationStateManager.getActiveCategory();
		int numSteles = MeditationStateManager.getSteleCount(activeCategory);
		float carouselAngle = MeditationStateManager.getCarouselAngle(partialTick);

		float radius = 12.0F;
		float spreadAngle = 120.0F;
		float stepAngle = numSteles > 1 ? spreadAngle / (numSteles - 1) : 0;
		float baseYaw = MeditationStateManager.getLockedYaw();

		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(-baseYaw));

		for (int i = 0; i < numSteles; i++) {
			poseStack.pushPose();

			float baseAngle = (i - (numSteles - 1) / 2.0f) * stepAngle;
			float steleAngle = baseAngle + carouselAngle;
			float rad = steleAngle * Mth.DEG_TO_RAD;

			float x = Mth.sin(rad) * radius;
			float z = Mth.cos(rad) * radius;

			poseStack.translate(x, 1.0F, z);

			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - steleAngle));
			poseStack.mulPose(Axis.XP.rotationDegrees(10.0F));

			boolean isSelected = (i == MeditationStateManager.getSelectedSteleIndex());
			boolean isOuterNode = (numSteles == 1) || (i == 0 || i == numSteles - 1);

			SteleModelRenderer.renderStele(poseStack, bufferSource, activeCategory, i, isSelected, isOuterNode,
					partialTick);

			poseStack.popPose();
		}

		poseStack.popPose();
	}
}