package com.fiskmods.pika.lightsabers.client.render.blocks;

import com.fiskmods.pika.lightsabers.ALItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HolocronItemRenderer extends BlockEntityWithoutLevelRenderer {

    public static final HolocronItemRenderer INSTANCE = new HolocronItemRenderer();

    public HolocronItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        boolean isSith = stack.is(ALItems.SITH_HOLOCRON.get()) || stack.getItem().getDescriptionId().contains("sith");

        poseStack.pushPose();

        switch (displayContext) {
            case GUI -> {
                poseStack.translate(0.5F, 0.35F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(25.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.scale(0.85F, 0.85F, 0.85F);
            }
            case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0.55F, 0.35F, 0.5F);
                poseStack.scale(0.55F, 0.55F, 0.55F);
            }
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5F, 0.30F, 0.5F);
                poseStack.scale(0.45F, 0.45F, 0.45F);
            }
            case GROUND -> {
                poseStack.translate(0.5F, 0.35F, 0.5F);
                poseStack.scale(0.5F, 0.5F, 0.5F);
            }
            default -> {
                poseStack.translate(0.5F, 0.35F, 0.5F);
                poseStack.scale(0.6F, 0.6F, 0.6F);
            }
        }

        int overlay = packedOverlay != 0 ? packedOverlay : OverlayTexture.NO_OVERLAY;

        if (isSith) {
            HolocronRenderer.renderSithHolocron(poseStack, buffer, packedLight, overlay, 0.0F, 0.0F);
        } else {
            HolocronRenderer.renderJediHolocron(poseStack, buffer, packedLight, overlay, 0.0F, 0.0F);
        }

        poseStack.popPose();
    }
}