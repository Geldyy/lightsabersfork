package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.block.SithStoneCoffinBlock;
import com.fiskmods.pika.lightsabers.block.entity.SithStoneCoffinBlockEntity;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelSithStoneCoffin;
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

public class SithStoneCoffinRenderer implements BlockEntityRenderer<SithStoneCoffinBlockEntity> {
    public static final ResourceLocation TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/models/sith_stone_coffin.png");

    private final ModelSithStoneCoffin model;

    public SithStoneCoffinRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ModelSithStoneCoffin(context.bakeLayer(ModelSithStoneCoffin.LAYER_LOCATION));
    }

    @Override
    public void render(SithStoneCoffinBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction facing = be.getBlockState().getOptionalValue(SithStoneCoffinBlock.FACING).orElse(Direction.NORTH);

        poseStack.pushPose();
        
        poseStack.translate(0.5D, 1.5D, 0.5D);

        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, be.baseplateOnly);

        poseStack.popPose();
    }
}