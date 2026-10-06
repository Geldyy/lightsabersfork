package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelLightsaberStand;
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

public class LightsaberStandItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final LightsaberStandItemRenderer INSTANCE = new LightsaberStandItemRenderer();
    private static final ResourceLocation TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/models/lightsaber_stand.png");
    
    private ModelLightsaberStand model;

    public LightsaberStandItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (this.model == null) {
            this.model = new ModelLightsaberStand(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLightsaberStand.LAYER_LOCATION));
        }

        poseStack.pushPose();

        // Skalierung und zentrierte Ausrichtung für GUI, Hand und Drop
        switch (displayContext) {
            case GUI -> {
                poseStack.translate(0.5F, 0.5F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(30.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, -1.25F, 0.0F);
                poseStack.scale(0.85F, 0.85F, 0.85F);
            }
            case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5F, 0.4F, 0.5F);
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, -1.25F, 0.0F);
                poseStack.scale(0.7F, 0.7F, 0.7F);
            }
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5F, 0.35F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, -1.25F, 0.0F);
                poseStack.scale(0.6F, 0.6F, 0.6F);
            }
            case FIXED -> {
                poseStack.translate(0.5F, 0.5F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, -1.25F, 0.0F);
                poseStack.scale(0.8F, 0.8F, 0.8F);
            }
            default -> { // Ground / Drop
                poseStack.translate(0.5F, 0.5F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0F, -1.25F, 0.0F);
                poseStack.scale(0.7F, 0.7F, 0.7F);
            }
        }

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderStand(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0.0F);

        poseStack.popPose();
    }
}