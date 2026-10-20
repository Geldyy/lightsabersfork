package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.block.LightsaberForgeBlock;
import com.fiskmods.pika.lightsabers.block.entity.LightsaberForgeBlockEntity;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelLightsaberForge;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class LightsaberForgeRenderer implements BlockEntityRenderer<LightsaberForgeBlockEntity> {

    private static final ResourceLocation TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/models/lightsaber_forge_light.png");

    private final ModelLightsaberForge model;

    public LightsaberForgeRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ModelLightsaberForge(context.bakeLayer(ModelLightsaberForge.LAYER_LOCATION));
    }

    @Override
    public void render(LightsaberForgeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction facing = blockEntity.getBlockState().getOptionalValue(LightsaberForgeBlock.FACING).orElse(Direction.NORTH);

        poseStack.pushPose();

        poseStack.translate(0.5D, 1.5D, 0.5D);

        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
    }
}