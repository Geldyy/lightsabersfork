package com.fiskmods.pika.lightsabers.client.render.force;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.logic.MeditationCameraHandler;
import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;
import com.mojang.blaze3d.vertex.PoseStack;
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
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        float partialTick = event.getPartialTick().getGameTimeDeltaTicks();
        float progress = MeditationCameraHandler.getInterpolatedProgress(partialTick);

        if (!MeditationStateManager.isMeditating() && progress <= 0.001F) return;

        float renderScale = MeditationStateManager.isMeditating() ? Math.max(progress, 0.05F) : progress;

        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        Vec3 camPos = camera.getPosition();

        // Das Zentrum ist jetzt exakt die Mitte des Holocron-Blocks
        Vec3 centerPos = MeditationStateManager.getMeditationCenter();
        if (centerPos == null || centerPos.equals(Vec3.ZERO)) centerPos = mc.player.position();

        poseStack.pushPose();

        double groundY = centerPos.y - 0.5;
        poseStack.translate(centerPos.x - camPos.x, groundY - camPos.y + 0.01, centerPos.z - camPos.z);
        poseStack.scale(renderScale, renderScale, renderScale);

        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        
        // 1. Plattform rendern (Zentriert um das Holocron)
        renderPlatform(poseStack, bufferSource, centerPos);

        // 2. Monolithen im weiten Halbkreis dahinter hochfahren
        float reveal = MeditationStateManager.getStelesRevealProgress(partialTick);
        if (reveal > 0.01F) {
            poseStack.pushPose();
            // Starten tiefer (-10.0), da sie weiter weg sind
            float yOffset = Mth.lerp(reveal, -10.0F, 0.0F);
            poseStack.translate(0, yOffset, 0);
            
            renderSteles(poseStack, bufferSource, partialTick);
            
            poseStack.popPose();
        }

        bufferSource.endBatch(RenderType.solid());
        bufferSource.endBatch(RenderType.cutout());
        bufferSource.endBatch(RenderType.cutoutMipped());
        bufferSource.endBatch();

        poseStack.popPose();
    }

    private static void renderPlatform(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 centerPos) {
        Minecraft mc = Minecraft.getInstance();
        
        Block forcestone = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone"));
        Block mossy = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_mossy"));
        Block inscribed = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "black_forcestone_inscribed"));
        
        if (forcestone == Blocks.AIR) forcestone = Blocks.SMOOTH_STONE;
        if (mossy == Blocks.AIR) mossy = Blocks.MOSSY_COBBLESTONE;
        if (inscribed == Blocks.AIR) inscribed = Blocks.CHISELED_STONE_BRICKS;

        poseStack.pushPose();
        int radius = 5; // Plattform minimal größer machen, da der Spieler nun am Rand sitzt
        RandomSource random = RandomSource.create((long) (centerPos.x * 1337 + centerPos.z * 42));

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    poseStack.pushPose();
                    poseStack.translate(x - 0.5, -1.0, z - 0.5);

                    BlockState stateToRender = forcestone.defaultBlockState();
                    float rand = random.nextFloat();
                    if (rand > 0.85f) stateToRender = mossy.defaultBlockState();
                    else if (rand > 0.7f) stateToRender = inscribed.defaultBlockState();

                    mc.getBlockRenderer().renderSingleBlock(stateToRender, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
                    poseStack.popPose();
                }
            }
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

        for (int i = 0; i < numSteles; i++) {
            poseStack.pushPose();

            // 1. Position der Stele auf dem Halbkreis berechnen
            float baseAngle = (i - (numSteles - 1) / 2.0f) * stepAngle;
            float currentAngle = baseAngle + carouselAngle;
            float angleRad = (baseYaw + currentAngle + 90.0F) * Mth.DEG_TO_RAD;

            float x = Mth.cos(angleRad) * radius;
            float z = Mth.sin(angleRad) * radius;

            poseStack.translate(x, 1.0F, z);

            // 2. Zur Kreismitte drehen
            float rotYaw = (float)(Mth.atan2(-z, -x) * Mth.RAD_TO_DEG) - 90.0F;
            poseStack.mulPose(Axis.YP.rotationDegrees(rotYaw));
            
            // Die leichte Neigung nach hinten bleibt, damit sie imposant über dir ragen
            poseStack.mulPose(Axis.XP.rotationDegrees(10.0F));

            boolean isSelected = (i == MeditationStateManager.getSelectedSteleIndex());
            
         // BUGFIX 2: Ist das die erste (0) oder die letzte (numSteles - 1) Stele? Dann ist es ein "Bookend" (Outer Node)!
            boolean isOuterNode = (i == 0 || i == numSteles - 1);

            // NEU: Wir übergeben 'i' als steleIndex!
            SteleModelRenderer.renderStele(poseStack, bufferSource, activeCategory, i, isSelected, isOuterNode, partialTick);

            poseStack.popPose();
        }
    }
}