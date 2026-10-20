package com.fiskmods.pika.lightsabers.client.render.force;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.network.payload.SyncBarrierPayload;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class ForceBarrierRenderer {

    private static final ResourceLocation SHIELD_TEX = ResourceLocation.fromNamespaceAndPath(
            AdvancedLightsabers.MODID, "textures/gui/white_pixel.png");

    public record ActiveBarrier(
            UUID casterUUID,
            int targetEntityId,
            boolean hasDome,
            BlockPos domeCenter,
            int level
    ) {}

    private static final Map<UUID, ActiveBarrier> ACTIVE_BARRIERS = new HashMap<>();

    public static void handleBarrierSync(SyncBarrierPayload payload) {
        if (!payload.active()) {
            ACTIVE_BARRIERS.remove(payload.casterUUID());
        } else {
            ACTIVE_BARRIERS.put(payload.casterUUID(), new ActiveBarrier(
                    payload.casterUUID(),
                    payload.targetEntityId(),
                    payload.hasDome(),
                    payload.domeCenter(),
                    payload.level()
            ));
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        if (ACTIVE_BARRIERS.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        Vec3 camPos = camera.getPosition();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        float gameTime = mc.level.getGameTime() + event.getPartialTick().getGameTimeDeltaTicks();

        for (ActiveBarrier barrier : ACTIVE_BARRIERS.values()) {
            if (barrier.hasDome() && barrier.domeCenter() != null) {
                BlockPos pos = barrier.domeCenter();
                double x = pos.getX() + 0.5D - camPos.x;
                double y = pos.getY() - camPos.y;
                double z = pos.getZ() + 0.5D - camPos.z;

                poseStack.pushPose();
                poseStack.translate(x, y, z);
                float radius = 7.0F;
                try {
                    if (com.fiskmods.pika.lightsabers.ALForceAbilitiesConfig.BARRIER_RADIUS_T3 != null) {
                        radius = com.fiskmods.pika.lightsabers.ALForceAbilitiesConfig.BARRIER_RADIUS_T3.get().floatValue();
                    }
                } catch (Exception ignored) {}
                renderEnergyDome(poseStack, bufferSource, radius, gameTime, mc.level, pos);
                poseStack.popPose();
            }
            else {
                Entity target = mc.level.getEntity(barrier.targetEntityId());
                if (target != null && target.isAlive()) {
                    double x = Mth.lerp(event.getPartialTick().getGameTimeDeltaTicks(), target.xOld, target.getX()) - camPos.x;
                    double y = Mth.lerp(event.getPartialTick().getGameTimeDeltaTicks(), target.yOld, target.getY()) + (target.getBbHeight() / 2.0D) - camPos.y;
                    double z = Mth.lerp(event.getPartialTick().getGameTimeDeltaTicks(), target.zOld, target.getZ()) - camPos.z;

                    float radius = (barrier.level() >= 2) ? 2.2F : 1.6F;

                    poseStack.pushPose();
                    poseStack.translate(x, y, z);
                    renderEnergySphere(poseStack, bufferSource, radius, gameTime);
                    poseStack.popPose();
                }
            }
        }

        bufferSource.endBatch(RenderType.entityTranslucentEmissive(SHIELD_TEX));
    }

    private static void renderEnergySphere(PoseStack poseStack, MultiBufferSource bufferSource, float radius, float time) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(SHIELD_TEX));
        Matrix4f matrix = poseStack.last().pose();

        int rings = 16;
        int sectors = 24;

        float pulse = 0.5F + 0.15F * Mth.sin(time * 0.15F);
        float r = 0.1F;
        float g = 0.8F;
        float b = 1.0F;
        float alpha = pulse * 0.45F;

        for (int i = 0; i < rings; i++) {
            float phi1 = (float) Math.PI * i / rings;
            float phi2 = (float) Math.PI * (i + 1) / rings;

            for (int j = 0; j < sectors; j++) {
                float theta1 = (float) (2 * Math.PI) * j / sectors;
                float theta2 = (float) (2 * Math.PI) * (j + 1) / sectors;

                addSphereQuad(consumer, matrix, radius, phi1, phi2, theta1, theta2, r, g, b, alpha);
            }
        }
    }

    private static void renderEnergyDome(PoseStack poseStack, MultiBufferSource bufferSource, float radius, float time, net.minecraft.world.level.Level level, BlockPos centerPos) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(SHIELD_TEX));
        Matrix4f matrix = poseStack.last().pose();

        int rings = 12;
        int sectors = 32;

        float pulse = 0.4F + 0.1F * Mth.sin(time * 0.1F);
        float r = 0.05F;
        float g = 0.7F;
        float b = 0.95F;
        float alpha = pulse * 0.35F;

        // 1. Upper hemisphere dome
        for (int i = 0; i < rings; i++) {
            float phi1 = (float) (Math.PI * 0.5D) * i / rings;
            float phi2 = (float) (Math.PI * 0.5D) * (i + 1) / rings;

            for (int j = 0; j < sectors; j++) {
                float theta1 = (float) (2 * Math.PI) * j / sectors;
                float theta2 = (float) (2 * Math.PI) * (j + 1) / sectors;

                addSphereQuad(consumer, matrix, radius, phi1, phi2, theta1, theta2, r, g, b, alpha);
            }
        }

        // 2. Cylindrical walls extending down to ground level when applied in the air
        if (level != null && centerPos != null) {
            float[] sectorGroundOffsets = new float[sectors + 1];
            int startY = centerPos.getY();
            int minBuild = level.getMinBuildHeight();
            int maxScan = Math.max(minBuild, startY - 64);
            BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

            for (int j = 0; j <= sectors; j++) {
                float theta = (float) (2 * Math.PI) * (j % sectors) / sectors;
                double worldX = centerPos.getX() + 0.5D + radius * Mth.cos(theta);
                double worldZ = centerPos.getZ() + 0.5D + radius * Mth.sin(theta);

                mpos.set((int) Math.floor(worldX), startY, (int) Math.floor(worldZ));
                while (mpos.getY() > maxScan) {
                    net.minecraft.world.level.block.state.BlockState st = level.getBlockState(mpos);
                    if (!st.isAir() && (st.blocksMotion() || !st.getFluidState().isEmpty())) {
                        break;
                    }
                    mpos.move(net.minecraft.core.Direction.DOWN);
                }
                double groundY = mpos.getY() + 1.0D;
                float diff = (float) (startY - groundY);
                sectorGroundOffsets[j] = Math.max(0.0F, diff);
            }

            for (int j = 0; j < sectors; j++) {
                float theta1 = (float) (2 * Math.PI) * j / sectors;
                float theta2 = (float) (2 * Math.PI) * (j + 1) / sectors;

                float h1 = sectorGroundOffsets[j];
                float h2 = sectorGroundOffsets[j + 1];

                if (h1 <= 0.05F && h2 <= 0.05F) {
                    continue;
                }

                float maxH = Math.max(h1, h2);
                int vSteps = Math.max(1, (int) Math.ceil(maxH / 1.5F));

                float x1 = radius * Mth.cos(theta1);
                float z1 = radius * Mth.sin(theta1);
                float x2 = radius * Mth.cos(theta2);
                float z2 = radius * Mth.sin(theta2);

                for (int s = 0; s < vSteps; s++) {
                    float frac1 = (float) s / vSteps;
                    float frac2 = (float) (s + 1) / vSteps;

                    float yTop1 = -h1 * frac1;
                    float yTop2 = -h2 * frac1;
                    float yBot1 = -h1 * frac2;
                    float yBot2 = -h2 * frac2;

                    consumer.addVertex(matrix, x1, yTop1, z1).setColor(r, g, b, alpha).setUv(0, 0)
                            .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
                    consumer.addVertex(matrix, x1, yBot1, z1).setColor(r, g, b, alpha).setUv(0, 1)
                            .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
                    consumer.addVertex(matrix, x2, yBot2, z2).setColor(r, g, b, alpha).setUv(1, 1)
                            .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
                    consumer.addVertex(matrix, x2, yTop2, z2).setColor(r, g, b, alpha).setUv(1, 0)
                            .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
                }
            }
        }
    }

    private static void addSphereQuad(VertexConsumer consumer, Matrix4f mat, float radius, float phi1, float phi2, float theta1, float theta2, float r, float g, float b, float a) {
        float x1 = radius * Mth.sin(phi1) * Mth.cos(theta1);
        float y1 = radius * Mth.cos(phi1);
        float z1 = radius * Mth.sin(phi1) * Mth.sin(theta1);

        float x2 = radius * Mth.sin(phi2) * Mth.cos(theta1);
        float y2 = radius * Mth.cos(phi2);
        float z2 = radius * Mth.sin(phi2) * Mth.sin(theta1);

        float x3 = radius * Mth.sin(phi2) * Mth.cos(theta2);
        float y3 = radius * Mth.cos(phi2);
        float z3 = radius * Mth.sin(phi2) * Mth.sin(theta2);

        float x4 = radius * Mth.sin(phi1) * Mth.cos(theta2);
        float y4 = radius * Mth.cos(phi1);
        float z4 = radius * Mth.sin(phi1) * Mth.sin(theta2);

        consumer.addVertex(mat, x1, y1, z1).setColor(r, g, b, a).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
        consumer.addVertex(mat, x2, y2, z2).setColor(r, g, b, a).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
        consumer.addVertex(mat, x3, y3, z3).setColor(r, g, b, a).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
        consumer.addVertex(mat, x4, y4, z4).setColor(r, g, b, a).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
    }
}