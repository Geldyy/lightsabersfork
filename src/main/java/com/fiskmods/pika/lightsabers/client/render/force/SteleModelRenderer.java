package com.fiskmods.pika.lightsabers.client.render.force;

import org.joml.Matrix4f;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelMonolithInner;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelMonolithOuter;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

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

public class SteleModelRenderer {

    private static final ResourceLocation TEXTURE_OUTER = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/block/uv_outer.png");
    private static final ResourceLocation TEXTURE_INNER = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/block/uv_inner.png");
    
    // Texturen für die Skill-Knoten und Linien
    private static final ResourceLocation TEXTURE_NODE = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/gui/skill_node.png");
    private static final ResourceLocation TEXTURE_LINE = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/gui/white_pixel.png");

    private static ModelMonolithOuter outerModel;
    private static ModelMonolithInner innerModel;
    
    public static boolean forceFallback = false; 

    public static void renderStele(PoseStack poseStack, MultiBufferSource bufferSource, TreeCategory category, int steleIndex, boolean isSelected, boolean isOuterNode, float partialTick) {
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
                if (outerModel == null) outerModel = new ModelMonolithOuter(mc.getEntityModels().bakeLayer(ModelMonolithOuter.LAYER_LOCATION));
                if (innerModel == null) innerModel = new ModelMonolithInner(mc.getEntityModels().bakeLayer(ModelMonolithInner.LAYER_LOCATION));

                poseStack.pushPose();
                poseStack.scale(1.0F, -1.0F, -1.0F); // Modell richtig herum drehen
                
                if (isOuterNode) {
                    VertexConsumer vertexConsumer = bufferSource.getBuffer(outerModel.renderType(TEXTURE_OUTER));
                    outerModel.renderToBuffer(poseStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
                } else {
                    VertexConsumer vertexConsumer = bufferSource.getBuffer(innerModel.renderType(TEXTURE_INNER));
                    innerModel.renderToBuffer(poseStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
                }
                
                // ==========================================================
                // SCHRITT 3.4: RENDER SKILL NODES (Auf der Leinwand)
                // ==========================================================
                renderSkillNodes(poseStack, bufferSource, category, steleIndex);
                
                poseStack.popPose();
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Rendern des Monolith-Modells. Nutze Fallback!");
            renderProceduralFallback(poseStack, bufferSource, category, mc);
        }

        poseStack.popPose();
    }

    private static void renderSkillNodes(PoseStack poseStack, MultiBufferSource bufferSource, TreeCategory category, int steleIndex) {
        poseStack.pushPose();
        
        float px = 0.0625F; 
        poseStack.scale(px, px, px);
        poseStack.translate(0.0F, 0.0F, -9.5F);
        
        int packedLight = 15728880; 

        java.util.List<com.fiskmods.pika.lightsabers.force.skilltree.SkillNode> nodes = 
            com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.getNodesForStele(category, steleIndex);

        // --- LINIEN ZEICHNEN ---
        for (com.fiskmods.pika.lightsabers.force.skilltree.SkillNode node : nodes) {
            float x1 = node.getOffsetX();
            float y1 = node.getOffsetY();
            
            boolean isNodeUnlocked = isSkillUnlocked(node.getId());

            for (ResourceLocation parentId : node.getParents()) {
                com.fiskmods.pika.lightsabers.force.skilltree.SkillNode parent = com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.getNode(parentId);
                if (parent != null) {
                    float x2 = parent.getOffsetX();
                    float y2 = parent.getOffsetY();
                    
                    float lineR = isNodeUnlocked ? 1.0F : 0.3F;
                    float lineG = isNodeUnlocked ? 0.8F : 0.3F;
                    float lineB = isNodeUnlocked ? 0.2F : 0.3F;
                    float lineA = isNodeUnlocked ? 0.8F : 0.2F;

                    drawGlowingLine(poseStack, bufferSource, x2, y2, x1, y1, 1.5F, packedLight, lineR, lineG, lineB, lineA);
                }
            }
        }

        // --- KNOTEN ZEICHNEN ---
        ResourceLocation selectedId = com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager.getSelectedNodeId();
        float unlockProgress = com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager.getUnlockProgress();

        for (com.fiskmods.pika.lightsabers.force.skilltree.SkillNode node : nodes) {
            float x = node.getOffsetX();
            float y = node.getOffsetY();
            
            boolean isNodeUnlocked = isSkillUnlocked(node.getId());
            boolean isFocused = node.getId().equals(selectedId);
            
            float nodeR = isNodeUnlocked ? 1.0F : 0.4F;
            float nodeG = isNodeUnlocked ? 1.0F : 0.4F;
            float nodeB = isNodeUnlocked ? 1.0F : 0.4F;
            float nodeA = isNodeUnlocked ? 1.0F : 0.5F;
            float size = 8.0F;

            // NEU: Feedback für den anvisierten Node
            if (isFocused) {
                nodeR = 0.2F; // Strahlendes Cyan
                nodeG = 0.8F; 
                nodeB = 1.0F; 
                nodeA = 1.0F;
                
                // Während der Spieler die Leertaste hält, wächst der Node dramatisch an
                size = 9.0F + (unlockProgress * 5.0F); 
            }
            
            drawRuneQuad(poseStack, bufferSource, x, y, -1.0F, size, packedLight, nodeR, nodeG, nodeB, nodeA);
        }

        poseStack.popPose();
    }

    // Die Linien-Methode bleibt unverändert...
    private static void drawGlowingLine(PoseStack poseStack, MultiBufferSource bufferSource, float x1, float y1, float x2, float y2, float thickness, int light, float r, float g, float b, float a) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float length = Mth.sqrt(dx * dx + dy * dy);
        float angle = (float) Math.atan2(dy, dx); 

        poseStack.pushPose();
        poseStack.translate(x1, y1, 0); 
        poseStack.mulPose(Axis.ZP.rotation(angle)); 

        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LINE));

        float halfThick = thickness / 2.0F;

        vertexConsumer.addVertex(matrix, 0, -halfThick, 0).setColor(r, g, b, a).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
        vertexConsumer.addVertex(matrix, 0, halfThick, 0).setColor(r, g, b, a).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
        vertexConsumer.addVertex(matrix, length, halfThick, 0).setColor(r, g, b, a).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
        vertexConsumer.addVertex(matrix, length, -halfThick, 0).setColor(r, g, b, a).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);

        poseStack.popPose();
    }

 // NEU: Nimmt jetzt float z als Parameter an
    private static void drawRuneQuad(PoseStack poseStack, MultiBufferSource bufferSource, float x, float y, float z, float size, int light, float r, float g, float b, float a) {
        poseStack.pushPose();
        
        // HIER GEFIXT: Wir nutzen den übergebenen Z-Wert
        poseStack.translate(x, y, z); 
        
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_NODE));

        float half = size / 2.0F;

        vertexConsumer.addVertex(matrix, -half, -half, 0).setColor(r, g, b, a).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
        vertexConsumer.addVertex(matrix, -half, half, 0).setColor(r, g, b, a).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
        vertexConsumer.addVertex(matrix, half, half, 0).setColor(r, g, b, a).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
        vertexConsumer.addVertex(matrix, half, -half, 0).setColor(r, g, b, a).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);

        poseStack.popPose();
    }

    // Procedural Fallback Logik
    private static void renderProceduralFallback(PoseStack poseStack, MultiBufferSource bufferSource, TreeCategory category, Minecraft mc) {
        Block base = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone"));
        Block cracked = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_cracked"));
        Block mossy = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_mossy"));
        Block inscribed = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_inscribed"));
        Block slab = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_slab"));
        Block pillar = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_pillar"));

        if (base == Blocks.AIR) base = Blocks.DEEPSLATE_BRICKS;
        if (cracked == Blocks.AIR) cracked = Blocks.CRACKED_DEEPSLATE_BRICKS;
        if (mossy == Blocks.AIR) mossy = Blocks.DEEPSLATE_BRICKS;
        if (inscribed == Blocks.AIR) inscribed = Blocks.CHISELED_DEEPSLATE;
        if (slab == Blocks.AIR) slab = Blocks.DEEPSLATE_BRICK_SLAB;
        if (pillar == Blocks.AIR) pillar = Blocks.DEEPSLATE_TILES;

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
                    if (rand > 0.85f) state = mossy.defaultBlockState();
                    else if (rand > 0.65f) state = cracked.defaultBlockState();
                    else if (rand > 0.45f) state = inscribed.defaultBlockState();
                }

                mc.getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
                poseStack.popPose();
            }
            
            for (int side : new int[]{-2, 2}) {
                if (y < height) { 
                    poseStack.pushPose();
                    poseStack.translate(side, y, 0);
                    poseStack.translate(-0.5, 0, -0.5);
                    mc.getBlockRenderer().renderSingleBlock(pillar.defaultBlockState(), poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
                    poseStack.popPose();
                } else if (y == height) {
                    poseStack.pushPose();
                    poseStack.translate(side, y - 1, 0);
                    poseStack.translate(-0.5, 1.0, -0.5);
                    mc.getBlockRenderer().renderSingleBlock(slab.defaultBlockState(), poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
                    poseStack.popPose();
                }
                
                
            }
        }
    }
    
 // =========================================================================
    // ECHTE SPIELER-DATEN LOGIK
    // =========================================================================
    private static boolean isSkillUnlocked(ResourceLocation skillId) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;

        // Tausche ".get()" durch deine echte Abfrage aus, falls sie anders heißt!
        PlayerForceData data = PlayerForceData.get(mc.player); 
        return data != null && data.hasSkill(skillId);
    }
}