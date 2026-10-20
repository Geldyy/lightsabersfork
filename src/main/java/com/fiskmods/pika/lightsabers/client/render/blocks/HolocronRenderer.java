package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.block.entity.HolocronBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class HolocronRenderer implements BlockEntityRenderer<HolocronBlockEntity> {

    public static final ResourceLocation JEDI_SIDE = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/block/jedi_holocron_side.png");
    public static final ResourceLocation JEDI_CORNER = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/block/jedi_holocron_corner.png");
    public static final ResourceLocation JEDI_CORNER_BOTTOM = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/block/jedi_holocron_corner_bottom.png");
    public static final ResourceLocation JEDI_CORNER_SIDE = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/block/jedi_holocron_corner_side.png");

    public static final ResourceLocation SITH_BOTTOM = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/block/sith_holocron_bottom.png");
    public static final ResourceLocation SITH_SIDE = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/block/sith_holocron_side.png");

    public HolocronRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(HolocronBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(tile.getBlockState().getBlock());
        boolean isSith = blockId != null && blockId.getPath().contains("sith");

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.0D, 0.5D);

        float t = Mth.lerp(partialTicks, tile.prevOpenTicks, tile.openTicks);
        float f = Mth.lerp(partialTicks, tile.prevOpenTimer, tile.openTimer);

        if (isSith) {
            renderSithHolocron(poseStack, bufferSource, packedLight, packedOverlay, f, t);
        } else {
            renderJediHolocron(poseStack, bufferSource, packedLight, packedOverlay, f, t);
        }

        poseStack.popPose();
    }

    public static void renderJediHolocron(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, float f, float t) {
        int glowLight = LightTexture.FULL_BRIGHT;

        float size = 0.5F;
        float width = 0.707F * size;
        float width1 = Mth.sqrt(width * width - (width / 2.0F) * (width / 2.0F));
        float height = size * 0.2875F;

        float f1 = Mth.sin(t / 10.0F) / 20.0F;
        float offset = size * (0.5775F + f * (0.3F + f1));
        float rot = 180.0F * f;

        poseStack.pushPose();
        poseStack.translate(0.0F, size / 2.0F + (size / 2.0F + f1) * f, 0.0F);

        VertexConsumer sideConsumer = bufferSource.getBuffer(RenderType.entityCutout(JEDI_SIDE));
        for (int i = 0; i < 6; i++) {
            poseStack.pushPose();
            if (i < 4) {
                poseStack.mulPose(Axis.YP.rotationDegrees(i * 90.0F));
            } else {
                poseStack.mulPose(Axis.XP.rotationDegrees((i - 4) * 180.0F + 90.0F));
            }

            addQuad(poseStack, sideConsumer, glowLight, overlay,
                    -size / 2.0F, 0.0F, size / 2.0F, 1.0F, 1.0F,
                    0.0F, -size / 2.0F, size / 2.0F, 1.0F, 0.0F,
                    size / 2.0F, 0.0F, size / 2.0F, 0.0F, 0.0F,
                    0.0F, size / 2.0F, size / 2.0F, 0.0F, 1.0F);

            poseStack.popPose();
        }

        VertexConsumer cornerConsumer = bufferSource.getBuffer(RenderType.entityCutout(JEDI_CORNER));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(j * 90.0F));
                if (i == 1) poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

                addTriangle(poseStack, cornerConsumer, glowLight, overlay,
                        -size / 4.0F, size / 2.0F, size / 4.0F, 0.5F, 0.0F,
                        -size / 2.0F, size / 2.0F, 0.0F, 0.0F, 0.0F,
                        -size / 2.0F, 0.0F, size / 2.0F, 0.0F, 1.0F);

                addTriangle(poseStack, cornerConsumer, glowLight, overlay,
                        -size / 2.0F, 0.0F, size / 2.0F, 1.0F, 1.0F,
                        0.0F, size / 2.0F, size / 2.0F, 1.0F, 0.0F,
                        -size / 4.0F, size / 2.0F, size / 4.0F, 0.5F, 0.0F);

                poseStack.popPose();
            }
        }

        VertexConsumer cornerBottomConsumer = bufferSource.getBuffer(RenderType.entityCutout(JEDI_CORNER_BOTTOM));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F + j * 90.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(35.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(rot));
                if (i == 1) poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, 0.0F, offset);

                addTriangle(poseStack, cornerBottomConsumer, glowLight, overlay,
                        0.0F, (width1 + width1) / 3.0F, 0.0F, 0.0F, 1.0F,
                        width / 2.0F, -width1 / 3.0F, 0.0F, 0.0F, 0.0F,
                        0.0F, -width1 / 3.0F, 0.0F, 0.5F, 0.0F);

                addTriangle(poseStack, cornerBottomConsumer, glowLight, overlay,
                        0.0F, -width1 / 3.0F, 0.0F, 0.5F, 0.0F,
                        -width / 2.0F, -width1 / 3.0F, 0.0F, 1.0F, 0.0F,
                        0.0F, (width1 + width1) / 3.0F, 0.0F, 1.0F, 1.0F);

                poseStack.popPose();
            }
        }

        VertexConsumer cornerSideConsumer = bufferSource.getBuffer(RenderType.entityCutout(JEDI_CORNER_SIDE));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F + j * 90.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(35.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(rot));
                if (i == 1) poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, 0.0F, offset);

                for (int k = 0; k < 3; k++) {
                    addTriangle(poseStack, cornerSideConsumer, glowLight, overlay,
                            0.0F, -width1 / 3.0F, 0.0F, 0.5F, 1.0F,
                            0.0F, 0.0F, height, 1.0F, 0.0F,
                            -width / 2.0F, -width1 / 3.0F, 0.0F, 1.0F, 1.0F);

                    
                    addTriangle(poseStack, cornerSideConsumer, glowLight, overlay,
                            0.0F, -width1 / 3.0F, 0.0F, 0.5F, 1.0F,
                            width / 2.0F, -width1 / 3.0F, 0.0F, 0.0F, 1.0F,
                            0.0F, 0.0F, height, 0.0F, 0.0F);

                    poseStack.mulPose(Axis.ZP.rotationDegrees(120.0F));
                }
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

    public static void renderSithHolocron(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, float f, float t) {
        int glowLight = LightTexture.FULL_BRIGHT;
        float size = 0.5F;
        float f1 = Mth.sin(t / 10.0F) / 20.0F;

        poseStack.pushPose();
        poseStack.translate(0.0F, size / 2.0F + (size / 2.0F + f1) * f + 0.0001F, 0.0F);

        VertexConsumer bottomConsumer = bufferSource.getBuffer(RenderType.entityCutout(SITH_BOTTOM));
        addQuad(poseStack, bottomConsumer, glowLight, overlay,
                size / 2.0F, -size / 2.0F, -size / 2.0F, 1.0F, 0.0F,
                size / 2.0F, -size / 2.0F, size / 2.0F, 1.0F, 1.0F,
                -size / 2.0F, -size / 2.0F, size / 2.0F, 0.0F, 1.0F,
                -size / 2.0F, -size / 2.0F, -size / 2.0F, 0.0F, 0.0F);

        VertexConsumer sideConsumer = bufferSource.getBuffer(RenderType.entityCutout(SITH_SIDE));
        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(i * 90.0F));

            addTriangle(poseStack, sideConsumer, glowLight, overlay,
                    size / 2.0F, -size / 2.0F, -size / 2.0F, 0.0F, 1.0F,
                    0.0F, -size / 2.0F, -size / 2.0F, 0.5F, 1.0F,
                    0.0F, size / 2.0F, 0.0F, 0.0F, 0.0F);

            addTriangle(poseStack, sideConsumer, glowLight, overlay,
                    0.0F, -size / 2.0F, -size / 2.0F, 0.5F, 1.0F,
                    -size / 2.0F, -size / 2.0F, -size / 2.0F, 1.0F, 1.0F,
                    0.0F, size / 2.0F, 0.0F, 1.0F, 0.0F);

            
            addTriangle(poseStack, sideConsumer, glowLight, overlay,
                    0.0F, size / 2.0F, 0.0F, 0.0F, 0.0F,
                    0.0F, -size / 2.0F, -size / 2.0F, 0.5F, 1.0F,
                    size / 2.0F, -size / 2.0F, -size / 2.0F, 0.0F, 1.0F);

            addTriangle(poseStack, sideConsumer, glowLight, overlay,
                    0.0F, size / 2.0F, 0.0F, 1.0F, 0.0F,
                    -size / 2.0F, -size / 2.0F, -size / 2.0F, 1.0F, 1.0F,
                    0.0F, -size / 2.0F, -size / 2.0F, 0.5F, 1.0F);

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private static void addQuad(PoseStack poseStack, VertexConsumer consumer, int light, int overlay,
                                float x1, float y1, float z1, float u1, float v1,
                                float x2, float y2, float z2, float u2, float v2,
                                float x3, float y3, float z3, float u3, float v3,
                                float x4, float y4, float z4, float u4, float v4) {
        Matrix4f matrix = poseStack.last().pose();
        consumer.addVertex(matrix, x1, y1, z1).setColor(255, 255, 255, 255).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(0, 1, 0);
        consumer.addVertex(matrix, x2, y2, z2).setColor(255, 255, 255, 255).setUv(u2, v2).setOverlay(overlay).setLight(light).setNormal(0, 1, 0);
        consumer.addVertex(matrix, x3, y3, z3).setColor(255, 255, 255, 255).setUv(u3, v3).setOverlay(overlay).setLight(light).setNormal(0, 1, 0);
        consumer.addVertex(matrix, x4, y4, z4).setColor(255, 255, 255, 255).setUv(u4, v4).setOverlay(overlay).setLight(light).setNormal(0, 1, 0);
    }

    private static void addTriangle(PoseStack poseStack, VertexConsumer consumer, int light, int overlay,
                                    float x1, float y1, float z1, float u1, float v1,
                                    float x2, float y2, float z2, float u2, float v2,
                                    float x3, float y3, float z3, float u3, float v3) {
        addQuad(poseStack, consumer, light, overlay, x1, y1, z1, u1, v1, x2, y2, z2, u2, v2, x3, y3, z3, u3, v3, x3, y3, z3, u3, v3);
    }
}