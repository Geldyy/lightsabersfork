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

public class ModelMonolithOuter extends Model {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "monolith_outer"), "main");

	private final ModelPart main;

	public ModelMonolithOuter(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition mainPart = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.ZERO);

		float pi = (float) Math.PI;

		PartDefinition frag_red = mainPart.addOrReplaceChild("frag_red", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-3.0F, -3.0F, -0.5F, -0.08F, 0.0F, -0.15F));
		frag_red.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(148, 0).addBox(-3.5F, -7.0F, -6.0F, 7.0F, 14.0F, 12.0F),
				PartPose.offsetAndRotation(-36.5F, -90.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_yellow = mainPart.addOrReplaceChild("frag_yellow", CubeListBuilder.create(),
				PartPose.offset(-1.0F, 0.0F, 0.0F));
		frag_yellow.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(96, 42).addBox(-10.5F, -7.0F, -6.0F, 21.0F, 14.0F, 12.0F),
				PartPose.offsetAndRotation(-20.5F, -90.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_lime = mainPart.addOrReplaceChild("frag_lime", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-2.0F, -4.0F, 0.8F, 0.1F, -0.05F, -0.12F));
		frag_lime.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(162, 52).addBox(-5.0F, -1.0F, -6.0F, 10.0F, 2.0F, 12.0F),
				PartPose.offsetAndRotation(-20.0F, -99.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_green = mainPart.addOrReplaceChild("frag_green", CubeListBuilder.create(),
				PartPose.offset(0.0F, -2.0F, 0.0F));
		frag_green.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(136, 68).addBox(-10.0F, -8.0F, -6.0F, 20.0F, 16.0F, 12.0F),
				PartPose.offsetAndRotation(0.0F, -92.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_cyan = mainPart.addOrReplaceChild("frag_cyan", CubeListBuilder.create(),
				PartPose.offset(1.0F, 0.0F, 0.0F));
		frag_cyan.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(136, 96).addBox(-10.5F, -7.0F, -6.0F, 21.0F, 14.0F, 12.0F),
				PartPose.offsetAndRotation(20.5F, -90.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_teal = mainPart.addOrReplaceChild("frag_teal", CubeListBuilder.create(),
				PartPose.offsetAndRotation(2.0F, -4.0F, 0.8F, 0.1F, 0.05F, 0.12F));
		frag_teal.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(52, 170).addBox(-5.0F, -1.0F, -6.0F, 10.0F, 2.0F, 12.0F),
				PartPose.offsetAndRotation(20.0F, -99.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_blue = mainPart.addOrReplaceChild("frag_blue", CubeListBuilder.create(),
				PartPose.offsetAndRotation(3.0F, -3.0F, -0.5F, -0.08F, 0.0F, 0.15F));
		frag_blue.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(162, 26).addBox(-3.5F, -7.0F, -6.0F, 7.0F, 14.0F, 12.0F),
				PartPose.offsetAndRotation(36.5F, -90.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_magenta = mainPart.addOrReplaceChild("frag_magenta", CubeListBuilder.create(),
				PartPose.ZERO);
		frag_magenta.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(0, 76).addBox(-10.0F, -20.0F, -7.0F, 20.0F, 40.0F, 14.0F),
				PartPose.offsetAndRotation(0.0F, -73.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_pink = mainPart.addOrReplaceChild("frag_pink", CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, -1.0F));
		frag_pink.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(68, 76).addBox(-10.0F, -19.0F, -7.0F, 20.0F, 38.0F, 14.0F),
				PartPose.offsetAndRotation(0.0F, -30.0F, -1.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_orange = mainPart.addOrReplaceChild("frag_orange", CubeListBuilder.create(),
				PartPose.offset(-1.0F, 0.0F, 0.0F));
		frag_orange.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(96, 0).addBox(-7.0F, -15.0F, -6.0F, 14.0F, 30.0F, 12.0F),
				PartPose.offsetAndRotation(-34.0F, -68.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_brown = mainPart.addOrReplaceChild("frag_brown", CubeListBuilder.create(),
				PartPose.offset(-2.0F, 2.0F, 0.0F));
		frag_brown.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(68, 128).addBox(-7.0F, -15.0F, -6.0F, 14.0F, 30.0F, 12.0F),
				PartPose.offsetAndRotation(-34.0F, -37.0F, 0.5F, pi, 0.0F, 0.0F));

		PartDefinition frag_lblue = mainPart.addOrReplaceChild("frag_lblue", CubeListBuilder.create(),
				PartPose.offset(1.0F, 0.0F, 0.0F));
		frag_lblue.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(120, 128).addBox(-7.0F, -15.0F, -6.0F, 14.0F, 30.0F, 12.0F),
				PartPose.offsetAndRotation(34.0F, -68.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_dblue = mainPart.addOrReplaceChild("frag_dblue", CubeListBuilder.create(),
				PartPose.offset(2.0F, 2.0F, 0.0F));
		frag_dblue.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(0, 130).addBox(-7.0F, -15.0F, -6.0F, 14.0F, 30.0F, 12.0F),
				PartPose.offsetAndRotation(34.0F, -37.0F, 0.5F, pi, 0.0F, 0.0F));

		PartDefinition frag_purple = mainPart.addOrReplaceChild("frag_purple", CubeListBuilder.create(),
				PartPose.offset(-1.0F, 0.0F, 0.0F));
		frag_purple.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -34.0F, -4.0F, 16.0F, 68.0F, 8.0F),
				PartPose.offsetAndRotation(-19.0F, -52.0F, 1.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_violet = mainPart.addOrReplaceChild("frag_violet", CubeListBuilder.create(),
				PartPose.offset(1.0F, 0.0F, 0.0F));
		frag_violet.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(48, 0).addBox(-8.0F, -34.0F, -4.0F, 16.0F, 68.0F, 8.0F),
				PartPose.offsetAndRotation(19.0F, -52.0F, 1.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_tiny_mid_l = mainPart.addOrReplaceChild("frag_tiny_mid_l", CubeListBuilder.create(),
				PartPose.offset(-1.0F, 1.0F, 0.0F));
		frag_tiny_mid_l.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(96, 170).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 4.0F, 10.0F),
				PartPose.offsetAndRotation(-18.0F, -84.0F, 1.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_tiny_mid_r = mainPart.addOrReplaceChild("frag_tiny_mid_r", CubeListBuilder.create(),
				PartPose.offset(1.0F, 1.0F, 0.0F));
		frag_tiny_mid_r.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(128, 170).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 4.0F, 10.0F),
				PartPose.offsetAndRotation(18.0F, -84.0F, 1.0F, pi, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			int color) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}