package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelSithStoneCoffin;
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

public class SithStoneCoffinItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final SithStoneCoffinItemRenderer INSTANCE = new SithStoneCoffinItemRenderer();

    private ModelSithStoneCoffin model;

    public SithStoneCoffinItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (this.model == null) {
            this.model = new ModelSithStoneCoffin(Minecraft.getInstance().getEntityModels().bakeLayer(ModelSithStoneCoffin.LAYER_LOCATION));
        }

        poseStack.pushPose();

        switch (displayContext) {
            case GUI -> {
                poseStack.translate(0.5F, 0.40F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(20.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(0.40F, 0.40F, 0.40F);
            }
            case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0.55F, 0.4F, 0.5F);
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(0.25F, 0.25F, 0.25F);
            }
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5F, 0.3F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(0.20F, 0.20F, 0.20F);
            }
            case GROUND -> {
                poseStack.translate(0.5F, 0.3F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(0.30F, 0.30F, 0.30F);
            }
            default -> {
                poseStack.translate(0.5F, 0.35F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(0.35F, 0.35F, 0.35F);
            }
        }

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(SithStoneCoffinRenderer.TEXTURE));
        this.model.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, false);

        poseStack.popPose();
    }
}