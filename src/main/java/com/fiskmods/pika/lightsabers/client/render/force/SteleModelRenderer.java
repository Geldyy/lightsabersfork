package com.fiskmods.pika.lightsabers.client.render.force;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelMonolithInner;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelMonolithOuter;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillNode;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SteleModelRenderer {

	private static final ResourceLocation TEXTURE_OUTER = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/block/uv_outer.png");
	private static final ResourceLocation TEXTURE_INNER = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/block/uv_inner.png");

	private static final ResourceLocation TEXTURE_NODE = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/gui/skill_node.png");
	private static final ResourceLocation TEXTURE_LINE = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/gui/white_pixel.png");
	private static final java.util.Map<ResourceLocation, ResourceLocation> ICON_CACHE = new java.util.HashMap<>();

	private static final java.util.Map<ResourceLocation, Boolean> ICON_EXISTS_CACHE = new java.util.HashMap<>();

	private static ResourceLocation getSkillIconTexture(ResourceLocation skillId) {
		ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
				"textures/gui/skills/" + skillId.getPath() + ".png");

		boolean exists = ICON_EXISTS_CACHE.computeIfAbsent(skillId,
				id -> Minecraft.getInstance().getResourceManager().getResource(loc).isPresent());

		return exists ? loc : null;
	}

	private static ModelMonolithOuter outerModel;
	private static ModelMonolithInner innerModel;

	public static boolean forceFallback = false;

	public static void renderStele(PoseStack poseStack, MultiBufferSource bufferSource, TreeCategory category,
			int steleIndex, boolean isSelected, boolean isOuterNode, float partialTick) {
		Minecraft mc = Minecraft.getInstance();
		poseStack.pushPose();

		if (isSelected) {
			poseStack.translate(0, 0, -1.0);
			poseStack.scale(1.1f, 1.1f, 1.1f);
		}

		try {
			if (forceFallback) {
				renderProceduralFallback(poseStack, bufferSource, category, mc);
			} else {
				if (outerModel == null)
					outerModel = new ModelMonolithOuter(
							mc.getEntityModels().bakeLayer(ModelMonolithOuter.LAYER_LOCATION));
				if (innerModel == null)
					innerModel = new ModelMonolithInner(
							mc.getEntityModels().bakeLayer(ModelMonolithInner.LAYER_LOCATION));

				poseStack.pushPose();
				poseStack.scale(1.0F, -1.0F, -1.0F);

				if (isOuterNode) {
					VertexConsumer vertexConsumer = bufferSource.getBuffer(outerModel.renderType(TEXTURE_OUTER));
					outerModel.renderToBuffer(poseStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY,
							0xFFFFFFFF);
				} else {
					VertexConsumer vertexConsumer = bufferSource.getBuffer(innerModel.renderType(TEXTURE_INNER));
					innerModel.renderToBuffer(poseStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY,
							0xFFFFFFFF);
				}

				renderSkillNodes(poseStack, bufferSource, category, steleIndex);

				poseStack.popPose();
			}
		} catch (Exception e) {
			System.err.println("Fehler beim Rendern des Monolith-Modells. Nutze Fallback!");
			renderProceduralFallback(poseStack, bufferSource, category, mc);
		}

		poseStack.popPose();
	}

	private static void renderSkillNodes(PoseStack poseStack, MultiBufferSource bufferSource, TreeCategory category,
			int steleIndex) {
		poseStack.pushPose();

		float px = 0.0625F;
		poseStack.scale(px, px, px);
		poseStack.translate(0.0F, 0.0F, -9.5F);

		int packedLight = 15728880;

		Minecraft mc = Minecraft.getInstance();
		PlayerForceData data = mc.player != null ? PlayerForceData.get(mc.player) : null;

		List<SkillNode> allNodes = SkillTreeRegistry.getNodesForStele(category, steleIndex, data);

		ResourceLocation activeRankId = (category == TreeCategory.FORCE && steleIndex == 1)
				? SkillTreeRegistry.getActiveNeutralRankNode(data)
				: null;

		List<SkillNode> nodes = new ArrayList<>();
		for (SkillNode n : allNodes) {
			String path = n.getId().getPath();
			if (category == TreeCategory.FORCE && steleIndex == 1
					&& (path.equals("force_adept") || path.equals("force_channeler") || path.equals("force_veteran")
							|| path.equals("force_mastery"))) {
				if (n.getId().equals(activeRankId)) {
					nodes.add(n);
				}
			} else {
				nodes.add(n);
			}
		}

		float lineR = 1.00F;
		float lineG = 0.82F;
		float lineB = 0.20F;
		float lineA = 1.00F;

		for (SkillNode node : nodes) {
			float x1 = node.getOffsetX();
			float y1 = node.getOffsetY();

			for (ResourceLocation parentId : node.getParents()) {
				SkillNode parent = SkillTreeRegistry.getNode(parentId);
				if (parent != null) {
					float x2 = parent.getOffsetX();
					float y2 = parent.getOffsetY();

					if (Math.abs(x1 - x2) < 0.1f && Math.abs(y1 - y2) < 0.1f)
						continue;

				}
			}
		}

		ResourceLocation selectedId = com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager
				.getSelectedNodeId();
		float unlockProgress = com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager.getUnlockProgress();

		for (SkillNode node : nodes) {
			float x = node.getOffsetX();
			float y = node.getOffsetY();

			boolean isNodeUnlocked = isSkillUnlocked(node.getId());
			ResourceLocation counterpartId = (data != null && data.isUnifiedTreeUnlocked() && category == TreeCategory.FORCE)
					? SkillTreeRegistry.getUnifiedCounterpart(node.getId())
					: null;
			boolean isFocused = node.getId().equals(selectedId)
					|| (counterpartId != null && counterpartId.equals(selectedId));
			boolean isPermanentlyLocked = data != null && node.isPermanentlyLocked(data);

			float nodeR, nodeG, nodeB, nodeA;
			float iconR, iconG, iconB, iconA;

			if (isPermanentlyLocked) {
				nodeR = isFocused ? 1.0F : 0.7F;
				nodeG = isFocused ? 0.2F : 0.1F;
				nodeB = isFocused ? 0.2F : 0.1F;
				nodeA = isFocused ? 1.0F : 0.65F;

				iconR = 0.95F;
				iconG = 0.95F;
				iconB = 0.95F;
				iconA = 0.90F;
			} else if (isFocused) {
				nodeR = 0.2F;
				nodeG = 0.8F;
				nodeB = 1.0F;
				nodeA = 1.0F;

				iconR = 0.06F;
				iconG = 0.06F;
				iconB = 0.08F;
				iconA = 1.0F;
			} else if (isNodeUnlocked) {
				nodeR = 1.0F;
				nodeG = 1.0F;
				nodeB = 1.0F;
				nodeA = 1.0F;

				iconR = 0.06F;
				iconG = 0.06F;
				iconB = 0.08F;
				iconA = 1.0F;
			} else {
				nodeR = 0.70F;
				nodeG = 0.70F;
				nodeB = 0.70F;
				nodeA = 0.85F;

				iconR = 0.22F;
				iconG = 0.22F;
				iconB = 0.25F;
				iconA = 0.95F;
			}

			float size = isFocused ? (7.75F + (unlockProgress * 1.5F)) : 7.0F;

			drawTexturedQuad(poseStack, bufferSource, TEXTURE_NODE, x, y, -1.0F, size, packedLight, nodeR, nodeG, nodeB,
					nodeA, false);

			ResourceLocation iconTex = getSkillIconTexture(node.getId());
			if (iconTex != null) {
				drawTexturedQuad(poseStack, bufferSource, iconTex, x, y, -1.25F, size * 0.68F, packedLight, iconR,
						iconG, iconB, iconA, true);
			}
		}

		poseStack.popPose();

	}

	private static void drawTexturedQuad(PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation texture,
			float x, float y, float z, float size, int light, float r, float g, float b, float a, boolean isIcon) {
		poseStack.pushPose();
		poseStack.translate(x, y, z);

		Matrix4f matrix = poseStack.last().pose();
		RenderType renderType = isIcon ? RenderType.entityTranslucent(texture)
				: RenderType.entityTranslucentEmissive(texture);
		VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

		float half = size / 2.0F;
		vertexConsumer.addVertex(matrix, -half, -half, 0).setColor(r, g, b, a).setUv(0.0F, 0.0F)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
		vertexConsumer.addVertex(matrix, -half, half, 0).setColor(r, g, b, a).setUv(0.0F, 1.0F)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
		vertexConsumer.addVertex(matrix, half, half, 0).setColor(r, g, b, a).setUv(1.0F, 1.0F)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
		vertexConsumer.addVertex(matrix, half, -half, 0).setColor(r, g, b, a).setUv(1.0F, 0.0F)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);

		poseStack.popPose();
	}

	private static void renderProceduralFallback(PoseStack poseStack, MultiBufferSource bufferSource,
			TreeCategory category, Minecraft mc) {
		Block base = BuiltInRegistries.BLOCK
				.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone"));
		Block cracked = BuiltInRegistries.BLOCK
				.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_cracked"));
		Block mossy = BuiltInRegistries.BLOCK
				.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_mossy"));
		Block inscribed = BuiltInRegistries.BLOCK
				.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_inscribed"));
		Block slab = BuiltInRegistries.BLOCK
				.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_slab"));
		Block pillar = BuiltInRegistries.BLOCK
				.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_pillar"));

		if (base == Blocks.AIR)
			base = Blocks.DEEPSLATE_BRICKS;
		if (cracked == Blocks.AIR)
			cracked = Blocks.CRACKED_DEEPSLATE_BRICKS;
		if (mossy == Blocks.AIR)
			mossy = Blocks.DEEPSLATE_BRICKS;
		if (inscribed == Blocks.AIR)
			inscribed = Blocks.CHISELED_DEEPSLATE;
		if (slab == Blocks.AIR)
			slab = Blocks.DEEPSLATE_BRICK_SLAB;
		if (pillar == Blocks.AIR)
			pillar = Blocks.DEEPSLATE_TILES;

		int seed = category != null ? category.name().hashCode() : 42;
		RandomSource random = RandomSource.create(seed);

		int height = 5;

		for (int y = 0; y <= height; y++) {
			for (int x = -1; x <= 1; x++) {
				poseStack.pushPose();
				poseStack.translate(x, y, 0);
				poseStack.translate(-0.5, 0, -0.5);

				BlockState state = base.defaultBlockState();
				float rand = random.nextFloat();

				if (y == height) {
					state = slab.defaultBlockState();
				} else {
					if (rand > 0.85f)
						state = mossy.defaultBlockState();
					else if (rand > 0.65f)
						state = cracked.defaultBlockState();
					else if (rand > 0.45f)
						state = inscribed.defaultBlockState();
				}

				mc.getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource, 15728880,
						OverlayTexture.NO_OVERLAY);
				poseStack.popPose();
			}

			for (int side : new int[] { -2, 2 }) {
				if (y < height) {
					poseStack.pushPose();
					poseStack.translate(side, y, 0);
					poseStack.translate(-0.5, 0, -0.5);
					mc.getBlockRenderer().renderSingleBlock(pillar.defaultBlockState(), poseStack, bufferSource,
							15728880, OverlayTexture.NO_OVERLAY);
					poseStack.popPose();
				} else if (y == height) {
					poseStack.pushPose();
					poseStack.translate(side, y - 1, 0);
					poseStack.translate(-0.5, 1.0, -0.5);
					mc.getBlockRenderer().renderSingleBlock(slab.defaultBlockState(), poseStack, bufferSource, 15728880,
							OverlayTexture.NO_OVERLAY);
					poseStack.popPose();
				}
			}
		}
	}

	private static boolean isSkillUnlocked(ResourceLocation skillId) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null)
			return false;
		PlayerForceData data = PlayerForceData.get(mc.player);
		return data != null && data.hasSkill(skillId);
	}
}