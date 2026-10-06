/*package com.fiskmods.pika.lightsabers.client.render.force;

import com.fiskmods.pika.lightsabers.ALAttachmentTypes;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.effects.logic.PlayerForceData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.*;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class ForceLightningRenderer {

    private static final Map<Integer, Long> ACTIVE_CASTERS = new HashMap<>();

    public static void trigger(int entityId) {
        ACTIVE_CASTERS.put(entityId, System.currentTimeMillis() + 200L);
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, Long>> it = ACTIVE_CASTERS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Long> entry = it.next();
            if (currentTime > entry.getValue()) {
                it.remove();
            }
        }

        if (ACTIVE_CASTERS.isEmpty()) return;

        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = event.getCamera().getPosition();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer builder = bufferSource.getBuffer(RenderType.lightning());

        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);

        for (int casterId : ACTIVE_CASTERS.keySet()) {
            if (mc.level.getEntity(casterId) instanceof Player player) {
                renderPlayerLightning(player, poseStack, builder, cameraPos, partialTick);
            }
        }

        bufferSource.endBatch(RenderType.lightning());
    }

    private static void renderPlayerLightning(Player player, PoseStack poseStack, VertexConsumer builder, Vec3 cameraPos, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        boolean isFirstPerson = (casterIsPlayer(player, mc) && mc.options.getCameraType().isFirstPerson());

        PlayerForceData data = player.getData(ALAttachmentTypes.FORCE_DATA.get());
        String selectedPower = data.getSelectedPower().toLowerCase();

        // 1. Farbabfrage: Electric Judgment (Jedi Gold-Gelb) vs. Sith Lightning (Cyan-Blau)
        boolean isElectricJudgment = selectedPower.contains("electric") || selectedPower.contains("judgement") || selectedPower.contains("judgment");
        
        int level = isElectricJudgment ? Math.max(1, data.getPowerLevel(data.getSelectedPower())) : Math.max(1, data.getPowerLevel("lightning"));

        Vec3 eyePos = player.getEyePosition(partialTick);
        Vec3 lookVec = player.getViewVector(partialTick);

        double reach = 8.0D;
        Vec3 dest = eyePos.add(lookVec.scale(reach));
        LivingEntity hitTarget = null;

        BlockHitResult blockHit = player.level().clip(new ClipContext(eyePos, dest, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 endTarget = (blockHit.getType() != HitResult.Type.MISS) ? blockHit.getLocation() : dest;

        AABB searchArea = player.getBoundingBox().expandTowards(lookVec.scale(reach)).inflate(1.2D);
        List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, searchArea, e -> e != player && e.isAlive());
        double closestDist = Double.MAX_VALUE;

        for (LivingEntity target : targets) {
            Vec3 toTarget = target.getEyePosition(partialTick).subtract(eyePos);
            if (lookVec.dot(toTarget.normalize()) > 0.65D) {
                double dist = eyePos.distanceTo(target.position());
                if (dist < closestDist) {
                    closestDist = dist;
                    hitTarget = target;
                }
            }
        }

        if (hitTarget != null) {
            endTarget = hitTarget.position().add(0.0D, hitTarget.getBbHeight() * 0.55D, 0.0D);
        }

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f matrix = poseStack.last().pose();

        // Zeitbasis mit Cosinus-Interpolation (110ms pro Zyklus)
        long stepDuration = 110L;
        long now = System.currentTimeMillis();
        long currentStep = now / stepDuration;
        float fraction = (float) (now % stepDuration) / (float) stepDuration;
        float morphProgress = (1.0F - Mth.cos(fraction * (float) Math.PI)) * 0.5F;

        int boltsPerHand = 1 + (level > 1 ? 1 : 0);
        int strandIndex = 0;

        for (int hand = 0; hand < 2; hand++) {
            boolean isRightHand = (hand == 0);
            for (int b = 0; b < boltsPerHand; b++) {
                renderSmoothMorphingLightning(matrix, builder, player, eyePos, endTarget, isRightHand, isFirstPerson, cameraPos, currentStep, morphProgress, strandIndex, isElectricJudgment, partialTick);
                strandIndex++;
            }
        }

        poseStack.popPose();
    }

    private static void renderSmoothMorphingLightning(Matrix4f mat, VertexConsumer consumer, Player player, Vec3 eyePos, Vec3 endTarget, boolean rightHand, boolean firstPerson, Vec3 cameraPos, long step, float morphProgress, int strandIndex, boolean isYellow, float partialTicks) {
        Vec3 look = player.getViewVector(partialTicks);
        Vec3 up = new Vec3(0, 1, 0);
        Vec3 right = look.cross(up).normalize();
        if (right.lengthSqr() < 1.0E-4D) right = new Vec3(1, 0, 0);
        Vec3 realUp = right.cross(look).normalize();

        double sideOffset = rightHand ? (firstPerson ? 0.30D : 0.38D) : (firstPerson ? -0.30D : -0.38D);
        double forwardOffset = firstPerson ? 0.45D : 0.65D;
        double downOffset = firstPerson ? -0.18D : -0.24D;

        Vec3 start = eyePos.add(right.scale(sideOffset)).add(realUp.scale(downOffset)).add(look.scale(forwardOffset));
        Vec3 target = endTarget;

        int segments = 12;
        Vec3[] points = new Vec3[segments + 1];
        points[0] = start;
        points[segments] = target;

        double totalDist = start.distanceTo(target);
        double maxJitter = Math.min(totalDist * 0.06D, 0.35D);

        Random randA = new Random(step * 31337L + (strandIndex * 1013L));
        Random randB = new Random((step + 1L) * 31337L + (strandIndex * 1013L));

        for (int i = 1; i < segments; i++) {
            float progress = (float) i / segments;
            Vec3 straightPoint = start.lerp(target, progress);

            double envelope = Math.sin(progress * Math.PI) * maxJitter;
            Vec3 jitterA = getRandomJitter(randA, envelope);
            Vec3 jitterB = getRandomJitter(randB, envelope);

            Vec3 smoothJitter = jitterA.lerp(jitterB, morphProgress);
            points[i] = straightPoint.add(smoothJitter);
        }

        // Farbdefinition: Gelb (Plo Koon) vs. Cyan-Blau (Sith)
        float glowR = isYellow ? 1.00F : 0.30F;
        float glowG = isYellow ? 0.80F : 0.75F;
        float glowB = isYellow ? 0.10F : 1.00F;

        float coreR = isYellow ? 1.00F : 1.00F;
        float coreG = isYellow ? 0.96F : 1.00F;
        float coreB = isYellow ? 0.70F : 1.00F;

        for (int i = 0; i < segments; i++) {
            Vec3 pA = points[i];
            Vec3 pB = points[i + 1];

            // 1. Zarter Glow-Mantel
            drawBillboardSegment(mat, consumer, pA, pB, cameraPos, 0.015F, glowR, glowG, glowB, 0.65F);

            // 2. Scharfer Blitzkern
            drawBillboardSegment(mat, consumer, pA, pB, cameraPos, 0.005F, coreR, coreG, coreB, 1.0F);

            // Verzweigungen
            if (i > 1 && i < segments - 2 && randA.nextFloat() < 0.30F) {
                renderSmoothBranch(mat, consumer, pA, look, cameraPos, randA, randB, morphProgress, maxJitter * 0.7D, isYellow);
            }
        }
    }

    private static void renderSmoothBranch(Matrix4f mat, VertexConsumer consumer, Vec3 start, Vec3 generalDir, Vec3 cameraPos, Random randA, Random randB, float morphProgress, double jitterScale, boolean isYellow) {
        Vec3 current = start;
        int branchSegments = 2 + (randA.nextInt(3));

        Vec3 dirA = generalDir.add((randA.nextDouble() - 0.5D), (randA.nextDouble() - 0.5D), (randA.nextDouble() - 0.5D)).normalize().scale(0.30D);
        Vec3 dirB = generalDir.add((randB.nextDouble() - 0.5D), (randB.nextDouble() - 0.5D), (randB.nextDouble() - 0.5D)).normalize().scale(0.30D);
        Vec3 branchDir = dirA.lerp(dirB, morphProgress);

        float glowR = isYellow ? 1.00F : 0.25F;
        float glowG = isYellow ? 0.75F : 0.60F;
        float glowB = isYellow ? 0.05F : 1.00F;

        float coreR = isYellow ? 1.00F : 1.00F;
        float coreG = isYellow ? 0.95F : 1.00F;
        float coreB = isYellow ? 0.60F : 1.00F;

        for (int b = 0; b < branchSegments; b++) {
            Vec3 jitA = new Vec3((randA.nextDouble() - 0.5D) * jitterScale, (randA.nextDouble() - 0.5D) * jitterScale, (randA.nextDouble() - 0.5D) * jitterScale);
            Vec3 jitB = new Vec3((randB.nextDouble() - 0.5D) * jitterScale, (randB.nextDouble() - 0.5D) * jitterScale, (randB.nextDouble() - 0.5D) * jitterScale);
            Vec3 next = current.add(branchDir).add(jitA.lerp(jitB, morphProgress));

            drawBillboardSegment(mat, consumer, current, next, cameraPos, 0.010F, glowR, glowG, glowB, 0.45F);
            drawBillboardSegment(mat, consumer, current, next, cameraPos, 0.003F, coreR, coreG, coreB, 0.90F);

            current = next;
        }
    }

    private static boolean casterIsPlayer(Player player, Minecraft mc) {
        return player == mc.player;
    }

    private static Vec3 getRandomJitter(Random rand, double scale) {
        return new Vec3(
                (rand.nextDouble() * 2.0D - 1.0D) * scale,
                (rand.nextDouble() * 2.0D - 1.0D) * scale,
                (rand.nextDouble() * 2.0D - 1.0D) * scale
        );
    }

    private static void drawBillboardSegment(Matrix4f mat, VertexConsumer consumer, Vec3 start, Vec3 end, Vec3 cameraPos, float width, float r, float g, float b, float a) {
        Vec3 mid = start.add(end).scale(0.5D);
        Vec3 toCam = cameraPos.subtract(mid);
        Vec3 dir = end.subtract(start);

        Vec3 normal = dir.cross(toCam).normalize().scale(width);
        if (normal.lengthSqr() < 1.0E-6D) {
            normal = new Vec3(0, width, 0);
        }

        Vec3 v1 = start.add(normal);
        Vec3 v2 = end.add(normal);
        Vec3 v3 = end.subtract(normal);
        Vec3 v4 = start.subtract(normal);

        consumer.addVertex(mat, (float) v1.x, (float) v1.y, (float) v1.z).setColor(r, g, b, a);
        consumer.addVertex(mat, (float) v2.x, (float) v2.y, (float) v2.z).setColor(r, g, b, a);
        consumer.addVertex(mat, (float) v3.x, (float) v3.y, (float) v3.z).setColor(r, g, b, a);
        consumer.addVertex(mat, (float) v4.x, (float) v4.y, (float) v4.z).setColor(r, g, b, a);
    }
}
*/