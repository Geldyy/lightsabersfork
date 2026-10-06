package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelSithCoffin;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SithCoffinItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final SithCoffinItemRenderer INSTANCE = new SithCoffinItemRenderer();

    private ModelSithCoffin model;

    public SithCoffinItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (this.model == null) {
            this.model = new ModelSithCoffin(Minecraft.getInstance().getEntityModels().bakeLayer(ModelSithCoffin.LAYER_LOCATION));
        }

        poseStack.pushPose();

        switch (displayContext) {
            case GUI -> {
                poseStack.translate(0.5F, 0.45F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(30.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, -0.2F, -0.25F);
                poseStack.scale(0.35F, 0.35F, 0.35F);
            }
            case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0.55F, 0.35F, 0.5F);
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, 0.0F, -0.25F);
                poseStack.scale(0.20F, 0.20F, 0.20F);
            }
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5F, 0.25F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, 0.0F, -0.25F);
                poseStack.scale(0.18F, 0.18F, 0.18F);
            }
            default -> {
                poseStack.translate(0.5F, 0.35F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, 0.0F, -0.25F);
                poseStack.scale(0.25F, 0.25F, 0.25F);
            }
        }

        this.model.setupAnim(0.0F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(SithCoffinRenderer.TEXTURE));
        this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
    }
}