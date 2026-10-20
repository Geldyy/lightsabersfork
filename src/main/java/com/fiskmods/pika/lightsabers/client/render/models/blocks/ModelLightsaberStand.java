package com.fiskmods.pika.lightsabers.client.render.models.blocks;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ModelLightsaberStand extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "lightsaber_stand"), "main");

	private final ModelPart trimL;
	private final ModelPart base1;
	private final ModelPart rStandTip;
	private final ModelPart lStandTip;

	public ModelLightsaberStand(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.trimL = root.getChild("trim_l");
		this.base1 = root.getChild("base1");
		this.rStandTip = root.getChild("r_stand_tip");
		this.lStandTip = root.getChild("l_stand_tip");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		PartDefinition trimL = root.addOrReplaceChild("trim_l",
				CubeListBuilder.create().texOffs(0, 9).addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 8.0F),
				PartPose.offset(-5.2F, 22.8F, -2.8F));

		trimL.addOrReplaceChild("trim_btm",
				CubeListBuilder.create().texOffs(10, 9).addBox(0.0F, 0.0F, 0.0F, 15.0F, 3.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		trimL.addOrReplaceChild("trim_top",
				CubeListBuilder.create().texOffs(10, 9).addBox(0.0F, 0.0F, 0.0F, 15.0F, 3.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 7.0F));

		trimL.addOrReplaceChild("trim_r",
				CubeListBuilder.create().texOffs(0, 9).addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 8.0F),
				PartPose.offset(14.0F, 0.0F, 0.0F));

		root.addOrReplaceChild("base1",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 1.0F, 8.0F),
				PartPose.offset(-4.8F, 23.0F, -2.3F));

		PartDefinition rTip = root.addOrReplaceChild("r_stand_tip",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(2.5F, 22.3F, -0.5F));

		rTip.addOrReplaceChild("r_stand_angle",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.3F, 0.7F, 0.0F, 0.0F, 0.0F, -0.7853982F));

		PartDefinition lTip = root.addOrReplaceChild("l_stand_tip",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(-3.5F, 22.3F, -0.5F));

		lTip.addOrReplaceChild("l_stand_angle",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7853982F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	public void renderStand(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
			float standOffset) {
		poseStack.pushPose();
		poseStack.translate(-5.2F * 0.0625F, 22.8F * 0.0625F, -2.8F * 0.0625F);
		poseStack.scale(0.7F, 0.4F, 0.7F);
		poseStack.translate(5.2F * 0.0625F, -22.8F * 0.0625F, 2.8F * 0.0625F);
		this.trimL.render(poseStack, buffer, packedLight, packedOverlay);
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(-4.8F * 0.0625F, 23.0F * 0.0625F, -2.3F * 0.0625F);
		poseStack.scale(0.7F, 0.4F, 0.7F);
		poseStack.translate(4.8F * 0.0625F, -23.0F * 0.0625F, 2.3F * 0.0625F);
		this.base1.render(poseStack, buffer, packedLight, packedOverlay);
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(standOffset * 0.0625F, 0.0F, 0.0F);
		this.rStandTip.render(poseStack, buffer, packedLight, packedOverlay);
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(-standOffset * 0.0625F, 0.0F, 0.0F);
		this.lStandTip.render(poseStack, buffer, packedLight, packedOverlay);
		poseStack.popPose();
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
			int color) {
		renderStand(poseStack, buffer, packedLight, packedOverlay, 0.0F);
	}
}