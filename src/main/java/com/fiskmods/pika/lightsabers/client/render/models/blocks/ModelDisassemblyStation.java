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

public class ModelDisassemblyStation extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "disassembly_station"), "main");

	private final ModelPart base;
	private final ModelPart tabletopThing;
	private final ModelPart theClaw;
	private final ModelPart ring1;
	private final ModelPart back;

	public ModelDisassemblyStation(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.base = root.getChild("base");
		this.tabletopThing = root.getChild("tabletop_thing");
		this.theClaw = root.getChild("the_claw");
		this.ring1 = root.getChild("ring1");
		this.back = root.getChild("back");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		root.addOrReplaceChild("base",
				CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, 10.0F, -8.0F, 32.0F, 14.0F, 16.0F),
				PartPose.ZERO);

		PartDefinition tabletopThing = root.addOrReplaceChild("tabletop_thing",
				CubeListBuilder.create().texOffs(90, 0).addBox(-19.0F, 10.0F, -8.0F, 12.0F, 3.0F, 7.0F),
				PartPose.offset(22.0F, -3.0F, 4.0F));

		tabletopThing.addOrReplaceChild("tabletop_thing_slant",
				CubeListBuilder.create().texOffs(94, 26).addBox(0.0F, 0.0F, 0.0F, 12.0F, 3.0F, 5.0F),
				PartPose.offsetAndRotation(-19.0F, 13.54F, -11.54F, 0.7853982F, 0.0F, 0.0F));

		PartDefinition theClaw = root.addOrReplaceChild("the_claw",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F),
				PartPose.offsetAndRotation(-18.0F, -2.0F, 4.0F, -0.9075712F, 0.13962634F, 0.0F));

		PartDefinition clawArm2 = theClaw.addOrReplaceChild("claw_arm_2",
				CubeListBuilder.create().texOffs(81, 0).addBox(0.0F, 0.6F, -0.01F, 2.0F, 6.0F, 2.0F),
				PartPose.offsetAndRotation(-0.4F, 9.6F, 0.0F, 0.0F, 0.0F, -0.87266463F));

		PartDefinition clawArm3 = clawArm2.addOrReplaceChild("claw_arm_3", CubeListBuilder.create(),
				PartPose.offsetAndRotation(4.5F, 6.6F, 0.0F, 0.0F, 0.0F, 1.5707964F));

		PartDefinition clawArm3Left = clawArm3.addOrReplaceChild("claw_arm_3_left",
				CubeListBuilder.create().texOffs(94, 10).addBox(0.0F, 0.6F, -0.01F, 2.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(1.84F, 5.61F, 0.0F, 0.0F, 0.0F, 2.3561945F));

		clawArm3Left.addOrReplaceChild("claw_arm_4_left",
				CubeListBuilder.create().texOffs(103, 11).addBox(0.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F),
				PartPose.offsetAndRotation(-3.4F, 0.1F, -0.02F, 0.0F, 0.0F, -1.0995574F));

		PartDefinition clawArm3Right = clawArm3.addOrReplaceChild("claw_arm_3_right",
				CubeListBuilder.create().texOffs(94, 10).mirror().addBox(3.2F, -0.8F, -0.01F, 2.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(1.84F, 5.61F, 0.0F, 0.0F, 0.0F, -2.3561945F));

		clawArm3Right.addOrReplaceChild("claw_arm_4_right",
				CubeListBuilder.create().texOffs(103, 11).addBox(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 2.0F),
				PartPose.offsetAndRotation(3.71F, 0.86F, -0.02F, 0.0F, 0.0F, 0.7330383F));

		PartDefinition ring1 = root.addOrReplaceChild("ring1",
				CubeListBuilder.create().texOffs(81, 10).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F),
				PartPose.offsetAndRotation(-15.0F, -1.1F, 4.0F, 1.5707964F, 1.5707964F, 0.0F));

		ring1.addOrReplaceChild("ring2",
				CubeListBuilder.create().texOffs(81, 10).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		ring1.addOrReplaceChild("ring3",
				CubeListBuilder.create().texOffs(81, 10).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		ring1.addOrReplaceChild("ring4",
				CubeListBuilder.create().texOffs(81, 10).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		ring1.addOrReplaceChild("ring5",
				CubeListBuilder.create().texOffs(81, 10).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		ring1.addOrReplaceChild("ring6",
				CubeListBuilder.create().texOffs(81, 10).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		ring1.addOrReplaceChild("ring7",
				CubeListBuilder.create().texOffs(81, 10).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F),
				PartPose.rotation(0.0F, -1.5810938F, 0.0F));
		ring1.addOrReplaceChild("ring8",
				CubeListBuilder.create().texOffs(81, 10).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition back = root.addOrReplaceChild("back",
				CubeListBuilder.create().texOffs(0, 30).addBox(-16.0F, 2.0F, -8.0F, 32.0F, 15.0F, 5.0F),
				PartPose.offset(0.0F, -7.0F, 11.0F));

		PartDefinition innerSlant = back.addOrReplaceChild("inner_slant",
				CubeListBuilder.create().texOffs(0, 50).addBox(-16.01F, 10.0F, -8.0F, 32.0F, 8.0F, 6.0F),
				PartPose.offsetAndRotation(0.0F, -4.0F, -16.0F, 0.87266463F, 0.0F, 0.0F));

		innerSlant.addOrReplaceChild("outer_slant",
				CubeListBuilder.create().texOffs(0, 72).addBox(-15.99F, 10.0F, -8.0F, 32.0F, 8.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, -13.47F, 1.0821041F, 0.0F, 0.0F));

		innerSlant.addOrReplaceChild("front_top",
				CubeListBuilder.create().texOffs(0, 64).addBox(0.0F, 0.0F, 0.0F, 32.0F, 4.0F, 4.0F),
				PartPose.offsetAndRotation(-16.0F, 6.22F, -6.52F, -0.87266463F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
			int color) {
		this.base.render(poseStack, buffer, packedLight, packedOverlay, color);
		this.tabletopThing.render(poseStack, buffer, packedLight, packedOverlay, color);
		this.theClaw.render(poseStack, buffer, packedLight, packedOverlay, color);

		poseStack.pushPose();
		this.ring1.render(poseStack, buffer, packedLight, packedOverlay, color);
		poseStack.popPose();

		this.back.render(poseStack, buffer, packedLight, packedOverlay, color);
	}
}