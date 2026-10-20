package com.fiskmods.pika.lightsabers.client.render.entity;

import com.fiskmods.pika.lightsabers.entity.TelekineticBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class RenderTelekineticBlock extends EntityRenderer<TelekineticBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public RenderTelekineticBlock(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = context.getBlockRenderDispatcher();
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(TelekineticBlockEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        BlockState state = entity.getBlockState();
        if (state == null || state.isAir()) return;

        poseStack.pushPose();
        poseStack.translate(-0.5D, 0.0D, -0.5D);

        this.blockRenderer.renderSingleBlock(state, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TelekineticBlockEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}