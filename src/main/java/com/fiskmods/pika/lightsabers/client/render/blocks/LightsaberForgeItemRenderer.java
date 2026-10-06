package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelLightsaberForge;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LightsaberForgeItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final LightsaberForgeItemRenderer INSTANCE = new LightsaberForgeItemRenderer();

    private static final ResourceLocation TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/models/lightsaber_forge_light.png");

    private ModelLightsaberForge model;

    public LightsaberForgeItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (this.model == null) {
            this.model = new ModelLightsaberForge(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLightsaberForge.LAYER_LOCATION));
        }

        poseStack.pushPose();

        switch (displayContext) {
            case GUI -> {
                poseStack.translate(0.5F, 0.5F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(30.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(-0.25F, -0.6F, 0.0F);
                poseStack.scale(0.38F, 0.38F, 0.38F);
            }
            case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0.65F, 0.35F, 0.45F);
                poseStack.mulPose(Axis.YP.rotationDegrees(35.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(-0.15F, -0.65F, 0.0F);
                poseStack.scale(0.22F, 0.22F, 0.22F);
            }
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5F, 0.25F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(-0.15F, -0.6F, 0.0F);
                poseStack.scale(0.20F, 0.20F, 0.20F);
            }
            case GROUND -> {
                poseStack.translate(0.5F, 0.35F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(-0.15F, -0.6F, 0.0F);
                poseStack.scale(0.25F, 0.25F, 0.25F);
            }
            default -> { // FIXED
                poseStack.translate(0.5F, 0.5F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(-0.15F, -0.6F, 0.0F);
                poseStack.scale(0.35F, 0.35F, 0.35F);
            }
        }

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
    }
}