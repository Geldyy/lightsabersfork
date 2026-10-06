package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelCrystal;
import com.fiskmods.pika.lightsabers.item.crystals.CrystalColor;
import com.fiskmods.pika.lightsabers.item.crystals.ItemColorCrystal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CrystalItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final CrystalItemRenderer INSTANCE = new CrystalItemRenderer();
    private static final ResourceLocation WHITE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/misc/white.png");

    private ModelCrystal model;

    public CrystalItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (!(stack.getItem() instanceof ItemColorCrystal colorCrystal)) {
            return;
        }

        if (this.model == null) {
            this.model = new ModelCrystal(Minecraft.getInstance().getEntityModels().bakeLayer(ModelCrystal.LAYER_LOCATION));
        }

        CrystalColor color = colorCrystal.getColor();
        float r = color.getRed();
        float g = color.getGreen();
        float b = color.getBlue();

        poseStack.pushPose();

        // 1. Ausrichtung, Zentrierung und Original-Skalierungsfaktoren
        switch (displayContext) {
            case GUI -> {
                poseStack.translate(0.5F, 0.15F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(25.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(2.2F, 2.2F, 2.2F);
            }
            case FIXED -> { // Schmiede-Slots / Rahmen
                poseStack.translate(0.5F, 0.20F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(1.8F, 1.8F, 1.8F);
            }
            case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0.55F, 0.35F, 0.5F);
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(1.5F, 1.5F, 1.5F);
            }
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5F, 0.30F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(1.2F, 1.2F, 1.2F);
            }
            case GROUND -> { // Als Drop auf dem Boden
                poseStack.translate(0.5F, 0.12F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(1.8F, 1.8F, 1.8F);
            }
            default -> {
                poseStack.translate(0.5F, 0.25F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(1.6F, 1.6F, 1.6F);
            }
        }

        // 2. Transparenz (Alpha = 60 %) & Leuchtkraft
        int alpha = (int) (0.60F * 255.0F);
        int argb = FastColor.ARGB32.color(alpha, (int) (r * 255.0F), (int) (g * 255.0F), (int) (b * 255.0F));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(WHITE_TEXTURE));
        this.model.renderToBuffer(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, argb);

        poseStack.popPose();
    }
}