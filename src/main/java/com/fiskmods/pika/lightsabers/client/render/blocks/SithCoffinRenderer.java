package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.block.SithCoffinBlock;
import com.fiskmods.pika.lightsabers.block.entity.SithCoffinBlockEntity;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelSithCoffin;
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
import net.minecraft.util.Mth;

public class SithCoffinRenderer implements BlockEntityRenderer<SithCoffinBlockEntity> {
    public static final ResourceLocation TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/models/sith_coffin.png");

    private final ModelSithCoffin model;

    public SithCoffinRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ModelSithCoffin(context.bakeLayer(ModelSithCoffin.LAYER_LOCATION));
    }

    @Override
    public void render(SithCoffinBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction facing = be.getBlockState().getOptionalValue(SithCoffinBlock.FACING).orElse(Direction.NORTH);
        float progress = Mth.lerp(partialTick, be.prevOpenTimer, be.openTimer);

        poseStack.pushPose();

        // 1. Zentrierung am Basis-Block (FOOT)
        poseStack.translate(0.5D, 1.5D, 0.5D);

        // 2. 1:1 Fisk Ausrichtung entlang des 2-Block-Verbunds
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot() + 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        // 3. Originalanimation anwenden
        this.model.setupAnim(progress);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
    }
}