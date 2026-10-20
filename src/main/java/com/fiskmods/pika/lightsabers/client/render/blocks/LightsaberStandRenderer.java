package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.block.LightsaberStandBlock;
import com.fiskmods.pika.lightsabers.block.entity.LightsaberStandBlockEntity;
import com.fiskmods.pika.lightsabers.client.render.HiltManager;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelLightsaberStand;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.HiltSetModel;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class LightsaberStandRenderer implements BlockEntityRenderer<LightsaberStandBlockEntity> {

    private static final ResourceLocation TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/models/lightsaber_stand.png");

    private final ModelLightsaberStand model;

    public LightsaberStandRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ModelLightsaberStand(context.bakeLayer(ModelLightsaberStand.LAYER_LOCATION));
    }

    @Override
    public void render(LightsaberStandBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction facing = blockEntity.getBlockState().getValue(LightsaberStandBlock.FACING);

        poseStack.pushPose();
        
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        ItemStack saberStack = blockEntity.getItem();
        boolean hasSaber = !saberStack.isEmpty();

        float standOffset = 0.0F;
        float centerOffsetWorld = 0.0F;

        if (hasSaber) {
            LightsaberData data = saberStack.get(ALDataComponents.LIGHTSABER_DATA.get());
            if (data != null) {
                boolean isDoubleBladed = data.isDoubleBladed();
                
                if (isDoubleBladed) {
                    float upperGripH = getPartHeight(data.grip(), PartType.BODY, 16.0F);
                    float upperSwitchH = getPartHeight(data.switchSection(), PartType.SWITCH_SECTION, 8.8F);
                    float upperEmitterH = getPartHeight(data.emitter(), PartType.EMITTER, 16.0F);
                    float upperH = upperGripH + upperSwitchH + upperEmitterH;

                    float lowerGripH = getPartHeight(data.getSecondaryGrip(), PartType.BODY, 16.0F);
                    float lowerSwitchH = getPartHeight(data.getSecondarySwitch(), PartType.SWITCH_SECTION, 8.8F);
                    float lowerEmitterH = getPartHeight(data.pommel(), PartType.EMITTER, 16.0F);
                    float lowerH = lowerGripH + lowerSwitchH + lowerEmitterH;

                    standOffset = 0.0F;

                    centerOffsetWorld = (upperH - lowerH) * 0.0625F * 0.185F * 0.5F;
                } else {
                    float pommelH = getPartHeight(data.pommel(), PartType.POMMEL, 1.0F);
                    float gripH = getPartHeight(data.grip(), PartType.BODY, 16.0F);
                    float switchH = getPartHeight(data.switchSection(), PartType.SWITCH_SECTION, 8.8F);
                    float emitterH = getPartHeight(data.emitter(), PartType.EMITTER, 16.0F);
                    float totalH = pommelH + gripH + switchH + emitterH;

                    float lengthCm = totalH * 0.575F;
                    float f = Math.min(lengthCm - 25.0F, 0.0F) * 0.2F;
                    standOffset = Math.max(f, -2.5F);

                    centerOffsetWorld = totalH * 0.0625F * 0.185F * 0.5F;
                }
            }
        }

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderStand(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, standOffset);

        if (hasSaber) {
            poseStack.pushPose();
            
            poseStack.translate(0.0D, 1.365D, 0.0D);
            
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));

            poseStack.translate(0.0D, -centerOffsetWorld, 0.0D);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    saberStack,
                    ItemDisplayContext.FIXED,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    0
            );
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private float getPartHeight(Item item, PartType type, float defaultVal) {
        if (item == null || item == Items.AIR) return defaultVal;
        HiltSetModel set = HiltManager.getSet(item);
        return set != null ? set.getHeight(type) : defaultVal;
    }
}