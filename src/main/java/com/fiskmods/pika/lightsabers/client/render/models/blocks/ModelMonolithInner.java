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

public class ModelMonolithInner extends Model {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "monolith_inner"), "main");

	private final ModelPart main;

	public ModelMonolithInner(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition mainPart = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.ZERO);

		float pi = (float) Math.PI;

		PartDefinition frag_red = mainPart.addOrReplaceChild("frag_red", CubeListBuilder.create(),
				PartPose.offset(-2.0F, 0.0F, 0.0F));
		frag_red.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(38, 154).addBox(-3.5F, -7.0F, -6.0F, 7.0F, 14.0F, 12.0F),
				PartPose.offsetAndRotation(-39.5F, -88.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_yellow = mainPart.addOrReplaceChild("frag_yellow", CubeListBuilder.create(),
				PartPose.offset(-1.0F, 0.0F, 0.0F));
		frag_yellow.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(116, 118).addBox(-13.0F, -8.0F, -6.0F, 26.0F, 16.0F, 12.0F),
				PartPose.offsetAndRotation(-23.0F, -89.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_lime = mainPart.addOrReplaceChild("frag_lime", CubeListBuilder.create(),
				PartPose.offset(-1.0F, 0.0F, 0.0F));
		frag_lime.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(136, 56).addBox(-5.0F, -2.0F, -6.0F, 10.0F, 4.0F, 12.0F),
				PartPose.offsetAndRotation(-23.0F, -99.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_green = mainPart.addOrReplaceChild("frag_green", CubeListBuilder.create(), PartPose.ZERO);
		frag_green.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(136, 28).addBox(-10.0F, -8.0F, -6.0F, 20.0F, 16.0F, 12.0F),
				PartPose.offsetAndRotation(0.0F, -91.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_cyan = mainPart.addOrReplaceChild("frag_cyan", CubeListBuilder.create(),
				PartPose.offset(1.0F, 0.0F, 0.0F));
		frag_cyan.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(136, 0).addBox(-13.0F, -8.0F, -6.0F, 26.0F, 16.0F, 12.0F),
				PartPose.offsetAndRotation(23.0F, -89.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_teal = mainPart.addOrReplaceChild("frag_teal", CubeListBuilder.create(),
				PartPose.offset(1.0F, 0.0F, 0.0F));
		frag_teal.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(0, 180).addBox(-5.0F, -2.0F, -6.0F, 10.0F, 4.0F, 12.0F),
				PartPose.offsetAndRotation(23.0F, -99.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_blue = mainPart.addOrReplaceChild("frag_blue", CubeListBuilder.create(),
				PartPose.offset(2.0F, 0.0F, 0.0F));
		frag_blue.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(76, 154).addBox(-3.5F, -7.0F, -6.0F, 7.0F, 14.0F, 12.0F),
				PartPose.offsetAndRotation(39.5F, -88.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_magenta = mainPart.addOrReplaceChild("frag_magenta", CubeListBuilder.create(),
				PartPose.ZERO);
		frag_magenta.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(0, 82).addBox(-10.0F, -21.0F, -7.0F, 20.0F, 42.0F, 14.0F),
				PartPose.offsetAndRotation(0.0F, -62.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_pink = mainPart.addOrReplaceChild("frag_pink", CubeListBuilder.create(),
				PartPose.offset(0.0F, 2.0F, -1.0F));
		frag_pink.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(116, 72).addBox(-10.0F, -16.0F, -7.0F, 20.0F, 32.0F, 14.0F),
				PartPose.offsetAndRotation(0.0F, -25.0F, -1.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_brown = mainPart.addOrReplaceChild("frag_brown", CubeListBuilder.create(),
				PartPose.offset(-1.0F, 1.0F, 0.0F));
		frag_brown.addOrReplaceChild("cube1",
				CubeListBuilder.create().texOffs(68, 82).addBox(-6.0F, -30.0F, -6.0F, 12.0F, 60.0F, 12.0F),
				PartPose.offsetAndRotation(-30.0F, -51.0F, 0.0F, pi, 0.0F, 0.0F));
		frag_brown.addOrReplaceChild("cube2",
				CubeListBuilder.create().texOffs(154, 146).addBox(-4.0F, -6.0F, -6.0F, 8.0F, 12.0F, 12.0F),
				PartPose.offsetAndRotation(-28.0F, -15.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_darkblue = mainPart.addOrReplaceChild("frag_darkblue", CubeListBuilder.create(),
				PartPose.offset(1.0F, 1.0F, 0.0F));
		frag_darkblue.addOrReplaceChild("cube1",
				CubeListBuilder.create().texOffs(88, 0).addBox(-6.0F, -30.0F, -6.0F, 12.0F, 60.0F, 12.0F),
				PartPose.offsetAndRotation(30.0F, -51.0F, 0.0F, pi, 0.0F, 0.0F));
		frag_darkblue.addOrReplaceChild("cube2",
				CubeListBuilder.create().texOffs(154, 170).addBox(-4.0F, -6.0F, -6.0F, 8.0F, 12.0F, 12.0F),
				PartPose.offsetAndRotation(28.0F, -15.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_orange = mainPart.addOrReplaceChild("frag_orange", CubeListBuilder.create(),
				PartPose.offset(-2.0F, 1.0F, 0.0F));
		frag_orange.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(0, 138).addBox(-3.5F, -8.0F, -6.0F, 7.0F, 16.0F, 12.0F),
				PartPose.offsetAndRotation(-39.5F, -71.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_lblue = mainPart.addOrReplaceChild("frag_lblue", CubeListBuilder.create(),
				PartPose.offset(2.0F, 1.0F, 0.0F));
		frag_lblue.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(116, 146).addBox(-3.5F, -8.0F, -6.0F, 7.0F, 16.0F, 12.0F),
				PartPose.offsetAndRotation(39.5F, -71.0F, 0.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_purple = mainPart.addOrReplaceChild("frag_purple", CubeListBuilder.create(),
				PartPose.offset(-0.5F, 1.0F, 0.0F));
		frag_purple.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -37.0F, -4.0F, 14.0F, 74.0F, 8.0F),
				PartPose.offsetAndRotation(-17.0F, -46.0F, 1.0F, pi, 0.0F, 0.0F));

		PartDefinition frag_violet = mainPart.addOrReplaceChild("frag_violet", CubeListBuilder.create(),
				PartPose.offset(0.5F, 1.0F, 0.0F));
		frag_violet.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(44, 0).addBox(-7.0F, -37.0F, -4.0F, 14.0F, 74.0F, 8.0F),
				PartPose.offsetAndRotation(17.0F, -46.0F, 1.0F, pi, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			int color) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}