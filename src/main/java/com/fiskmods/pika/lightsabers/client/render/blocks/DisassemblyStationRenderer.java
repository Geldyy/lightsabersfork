package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.block.DisassemblyStationBlock;
import com.fiskmods.pika.lightsabers.block.entity.DisassemblyStationBlockEntity;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelDisassemblyStation;
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

public class DisassemblyStationRenderer implements BlockEntityRenderer<DisassemblyStationBlockEntity> {

    private static final ResourceLocation TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/models/disassembly_station.png");
    private static final ResourceLocation TEXTURE_LIGHTS = 
        ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/models/disassembly_station_lights.png");

    private final ModelDisassemblyStation model;

    public DisassemblyStationRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ModelDisassemblyStation(context.bakeLayer(ModelDisassemblyStation.LAYER_LOCATION));
    }

    @Override
    public void render(DisassemblyStationBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction facing = blockEntity.getBlockState().getValue(DisassemblyStationBlock.FACING);

        poseStack.pushPose();

        poseStack.translate(0.5D, 1.5D, 0.5D);
        
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        poseStack.translate(0.5D, 0.0D, 0.0D);

        VertexConsumer baseConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, baseConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        VertexConsumer glowConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE_LIGHTS));
        this.model.renderToBuffer(poseStack, glowConsumer, 15728880, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
    }
}