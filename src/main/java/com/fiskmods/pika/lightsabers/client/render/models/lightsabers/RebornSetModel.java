package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class RebornSetModel extends HiltSetModel {

	private static final float[] OCTAGON_ANGLES = { 0.0F, 0.7853982F, 1.5707964F, 2.3561945F, 3.1415927F, -2.3561945F,
			-1.5707964F, -0.7853982F };

	private final ModelPart pommel;
	private final ModelPart body;
	private final ModelPart switchSection;
	private final ModelPart emitter;

	public RebornSetModel() {
		super("reborn");

		this.pommel = createPommel().bakeRoot();
		ModelPart pommelSide1 = this.pommel.getChild("side1_1");
		pommelSide1.xScale = 0.3F;
		pommelSide1.yScale = 0.3F;
		pommelSide1.zScale = 0.3F;

		ModelPart pommelSide2 = this.pommel.getChild("side2_1");
		pommelSide2.xScale = 0.25F;
		pommelSide2.yScale = 0.25F;
		pommelSide2.zScale = 0.25F;

		ModelPart pommelSide3 = this.pommel.getChild("side3_1");
		pommelSide3.xScale = 0.3F;
		pommelSide3.yScale = 0.3F;
		pommelSide3.zScale = 0.3F;

		ModelPart pommelSide4 = this.pommel.getChild("side4_1");
		pommelSide4.xScale = 0.23F;
		pommelSide4.yScale = 0.23F;
		pommelSide4.zScale = 0.23F;

		ModelPart pommelSideRing1 = this.pommel.getChild("side_ring_1");
		pommelSideRing1.xScale = 1.1F;
		pommelSideRing1.yScale = 1.5F;
		pommelSideRing1.zScale = 1.1F;

		this.body = createBody().bakeRoot();

		this.switchSection = createSwitch().bakeRoot();
		ModelPart switchRing1 = this.switchSection.getChild("ring1");
		switchRing1.xScale = 1.0F;
		switchRing1.yScale = 0.8F;
		switchRing1.zScale = 1.0F;

		ModelPart switchSideRing1 = this.switchSection.getChild("side_ring_1");
		switchSideRing1.xScale = 1.1F;
		switchSideRing1.yScale = 1.0F;
		switchSideRing1.zScale = 1.1F;

		ModelPart switchSideRing2 = this.switchSection.getChild("side_ring_2");
		switchSideRing2.xScale = 1.01F;
		switchSideRing2.yScale = 0.5F;
		switchSideRing2.zScale = 1.01F;

		ModelPart switchSideRing21 = this.switchSection.getChild("side_ring_2_1");
		switchSideRing21.xScale = 1.01F;
		switchSideRing21.yScale = 0.5F;
		switchSideRing21.zScale = 1.01F;

		ModelPart switchSideRing22 = this.switchSection.getChild("side_ring_2_2");
		switchSideRing22.xScale = 1.01F;
		switchSideRing22.yScale = 0.5F;
		switchSideRing22.zScale = 1.01F;

		ModelPart switchSideRing23 = this.switchSection.getChild("side_ring_2_3");
		switchSideRing23.xScale = 1.01F;
		switchSideRing23.yScale = 0.5F;
		switchSideRing23.zScale = 1.01F;

		ModelPart switchBtn1 = this.switchSection.getChild("button1");
		switchBtn1.xScale = 0.25F;
		switchBtn1.yScale = 0.25F;
		switchBtn1.zScale = 0.25F;

		ModelPart switchBtn2 = this.switchSection.getChild("button2");
		switchBtn2.xScale = 0.2F;
		switchBtn2.yScale = 0.2F;
		switchBtn2.zScale = 0.25F;

		ModelPart topRing9 = this.switchSection.getChild("top_ring_9");
		topRing9.xScale = 0.5F;
		topRing9.yScale = 1.0F;
		topRing9.zScale = 1.0F;

		ModelPart topRing10 = this.switchSection.getChild("top_ring_10");
		topRing10.xScale = 0.5F;
		topRing10.yScale = 1.0F;
		topRing10.zScale = 1.0F;

		ModelPart topRing11 = this.switchSection.getChild("top_ring_11");
		topRing11.xScale = 0.5F;
		topRing11.yScale = 0.5F;
		topRing11.zScale = 1.0F;

		ModelPart topRing12 = this.switchSection.getChild("top_ring_12");
		topRing12.xScale = 0.5F;
		topRing12.yScale = 0.5F;
		topRing12.zScale = 1.0F;

		ModelPart topRing121 = this.switchSection.getChild("top_ring_12_1");
		topRing121.xScale = 0.5F;
		topRing121.yScale = 0.5F;
		topRing121.zScale = 0.5F;

		ModelPart topRing122 = this.switchSection.getChild("top_ring_12_2");
		topRing122.xScale = 0.5F;
		topRing122.yScale = 0.5F;
		topRing122.zScale = 0.5F;

		this.emitter = createEmitter().bakeRoot();
		ModelPart emitterInnerRing = this.emitter.getChild("inner_ring");
		emitterInnerRing.xScale = 0.81F;
		emitterInnerRing.yScale = 1.0F;
		emitterInnerRing.zScale = 0.81F;

		ModelPart emitterSideRing1 = this.emitter.getChild("side_ring_1");
		emitterSideRing1.xScale = 1.1F;
		emitterSideRing1.yScale = 1.0F;
		emitterSideRing1.zScale = 1.1F;

		ModelPart emitterSideRing2 = this.emitter.getChild("side_ring_2");
		emitterSideRing2.xScale = 1.1F;
		emitterSideRing2.yScale = 2.0F;
		emitterSideRing2.zScale = 1.1F;

		ModelPart emitterSideRing3 = this.emitter.getChild("side_ring_3");
		emitterSideRing3.xScale = 1.1F;
		emitterSideRing3.yScale = 2.0F;
		emitterSideRing3.zScale = 1.1F;

		ModelPart emitterSideRing4 = this.emitter.getChild("side_ring_4");
		emitterSideRing4.xScale = 1.1F;
		emitterSideRing4.yScale = 2.0F;
		emitterSideRing4.zScale = 1.1F;

		ModelPart emitterTip = this.emitter.getChild("emitter_tip");
		emitterTip.xScale = 0.37F;
		emitterTip.yScale = 0.93F;
		emitterTip.zScale = 0.37F;

		ModelPart darkInnerRing = this.emitter.getChild("dark_inner_ring");
		darkInnerRing.xScale = 0.63F;
		darkInnerRing.yScale = 0.53F;
		darkInnerRing.zScale = 0.63F;
	}

	@Override
	public boolean isSingleTexture() {
		return true;
	}

	@Override
	public ModelPart getPartModel(PartType type) {
		return switch (type) {
		case EMITTER -> this.emitter;
		case SWITCH_SECTION -> this.switchSection;
		case BODY -> this.body;
		case POMMEL -> this.pommel;
		};
	}

	@Override
	public float getHeight(PartType type) {
		return switch (type) {
		case POMMEL -> 6.0F;
		case BODY -> 19.0F;
		case SWITCH_SECTION -> 8.0F;
		case EMITTER -> 18.0F;
		};
	}

	private static void addOctagonalShell(PartDefinition parent, String name, int u, int v, float minX, float minY,
			float minZ, float sizeX, float sizeY, float sizeZ) {
		for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
			parent.addOrReplaceChild(name + "_" + i,
					CubeListBuilder.create().texOffs(u, v).addBox(minX, minY, minZ, sizeX, sizeY, sizeZ),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}
	}

	private static LayerDefinition createPommel() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition secTopRing = root.addOrReplaceChild("secondary_top_ring", CubeListBuilder.create(),
				PartPose.ZERO);
		addOctagonalShell(secTopRing, "sec_top", 0, 11, -1.5F, 0.0F, -0.38F, 3.0F, 6.0F, 4.0F);

		PartDefinition sideRing1 = root.addOrReplaceChild("side_ring_1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 4.5F, 0.0F));
		addOctagonalShell(sideRing1, "top_ring", 16, 14, -1.5F, -1.0F, 2.62F, 3.0F, 2.0F, 1.0F);

		PartDefinition s1 = root.addOrReplaceChild("side1_1", CubeListBuilder.create(),
				PartPose.offsetAndRotation(3.7F, 4.6F, 0.0F, 1.5707964F, -1.5707964F, 0.0F));
		addOctagonalShell(s1, "s1", 8, 6, -1.5F, -2.0F, 2.6F, 3.0F, 2.0F, 1.0F);

		PartDefinition s2 = root.addOrReplaceChild("side2_1", CubeListBuilder.create(),
				PartPose.offsetAndRotation(4.0F, 4.6F, 0.0F, 1.5707964F, -1.5707964F, 0.0F));
		addOctagonalShell(s2, "s2", 8, 9, -1.5F, -2.0F, 2.6F, 3.0F, 2.0F, 1.0F);

		PartDefinition s3 = root.addOrReplaceChild("side3_1", CubeListBuilder.create(),
				PartPose.offsetAndRotation(4.4F, 4.6F, 0.0F, 1.5707964F, -1.5707964F, 0.0F));
		addOctagonalShell(s3, "s3", 0, 4, -1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F);

		PartDefinition s4 = root.addOrReplaceChild("side4_1", CubeListBuilder.create(),
				PartPose.offsetAndRotation(4.6F, 4.6F, 0.0F, 1.5707964F, -1.5707964F, 0.0F));
		addOctagonalShell(s4, "s4", 8, 12, -1.5F, -2.0F, 2.6F, 3.0F, 2.0F, 1.0F);

		return LayerDefinition.create(mesh, 32, 32);
	}

	private static LayerDefinition createBody() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition bodyMain = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.ZERO);
		int[] texOffsetsU = { 30, 25, 23, 30, 24, 30, 25, 23 };
		for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
			bodyMain.addOrReplaceChild("panel_" + i,
					CubeListBuilder.create().texOffs(texOffsetsU[i], 11).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 19.0F, 1.0F),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}

		float[][] bladePos = { { -2.8F, 0.0F, -2.2F, 2.3561945F }, { 2.2F, 0.0F, -2.8F, 0.7853982F },
				{ 0.5F, 0.0F, 3.6F, -1.5707964F } };

		for (int b = 0; b < bladePos.length; b++) {
			PartDefinition blade = root.addOrReplaceChild("side_blade_" + b,
					CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.0F, 0.0F, 1.0F, 17.0F, 1.0F),
					PartPose.offsetAndRotation(bladePos[b][0], bladePos[b][1], bladePos[b][2], 0.0F, bladePos[b][3],
							0.0F));

			blade.addOrReplaceChild("sub2_" + b,
					CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 5.0F, 0.0F, 1.0F, 1.0F, 1.0F),
					PartPose.offset(0.9F, -5.0F, 0.0F));

			blade.addOrReplaceChild("sub3_" + b,
					CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.3F, 0.0F, 1.0F, 5.0F, 1.0F),
					PartPose.offsetAndRotation(0.96F, 0.5F, 0.0F, 0.0F, 0.0F, 0.17453292F));

			blade.addOrReplaceChild("sub4_" + b,
					CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.3F, 0.0F, 1.0F, 5.0F, 1.0F),
					PartPose.offsetAndRotation(-0.04F, 11.4F, 0.0F, 0.0F, 0.0F, -0.17453292F));

			blade.addOrReplaceChild("sub5_" + b,
					CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.3F, 0.0F, 3.0F, 1.0F, 1.0F),
					PartPose.offsetAndRotation(-1.34F, 17.1F, 0.0F, 0.0F, 0.0F, -0.61086524F));
		}

		return LayerDefinition.create(mesh, 32, 32);
	}

	private static LayerDefinition createSwitch() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition ring1 = root.addOrReplaceChild("ring1", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(ring1, "ring1_shell", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 10.0F, 1.0F);

		PartDefinition sideRing1 = root.addOrReplaceChild("side_ring_1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 1.0F, 0.0F));
		addOctagonalShell(sideRing1, "sr1", 16, 14, -1.5F, -1.0F, 2.62F, 3.0F, 2.0F, 1.0F);

		PartDefinition sideRing2 = root.addOrReplaceChild("side_ring_2", CubeListBuilder.create(),
				PartPose.offset(0.0F, 5.3F, 0.0F));
		addOctagonalShell(sideRing2, "sr2", 16, 15, -1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition sideRing22 = root.addOrReplaceChild("side_ring_2_2", CubeListBuilder.create(),
				PartPose.offset(0.0F, 4.3F, 0.0F));
		addOctagonalShell(sideRing22, "sr2_2", 16, 15, -1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition sideRing23 = root.addOrReplaceChild("side_ring_2_3", CubeListBuilder.create(),
				PartPose.offset(0.0F, 8.0F, 0.0F));
		addOctagonalShell(sideRing23, "sr2_3", 16, 15, -1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition sideRing21 = root.addOrReplaceChild("side_ring_2_1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 9.0F, 0.0F));
		addOctagonalShell(sideRing21, "sr2_1", 16, 15, -1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition btn1 = root.addOrReplaceChild("button1", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 7.5F, -3.0F, 1.5707964F, 0.0F, 0.0F));
		addOctagonalShell(btn1, "b1", 2, 23, -1.5F, -5.0F, -0.38F, 3.0F, 5.0F, 4.0F);

		PartDefinition btn2 = root.addOrReplaceChild("button2", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 5.0F, -3.0F, 1.5707964F, 0.0F, 0.0F));
		addOctagonalShell(btn2, "b2", 8, 0, -1.5F, -5.0F, -0.38F, 3.0F, 5.0F, 4.0F);

		root.addOrReplaceChild("top_ring_9",
				CubeListBuilder.create().texOffs(16, 15).addBox(-5.0F, 2.0F, 2.65F, 4.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 9.2F, 0.0F, 0.0F, 3.1415927F, 1.5707964F));

		root.addOrReplaceChild("top_ring_10",
				CubeListBuilder.create().texOffs(16, 15).addBox(-5.0F, -3.0F, 2.65F, 4.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 9.2F, 0.0F, 0.0F, 3.1415927F, 1.5707964F));

		root.addOrReplaceChild("top_ring_11",
				CubeListBuilder.create().texOffs(16, 15).addBox(-5.0F, -3.0F, 2.65F, 4.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.67F, 5.55F, 0.0F, 0.0F, 3.1415927F, 2.268928F));

		root.addOrReplaceChild("top_ring_12",
				CubeListBuilder.create().texOffs(16, 15).addBox(1.0F, -3.0F, 2.65F, 4.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-0.66F, 5.54F, 0.0F, 0.0F, -3.1415927F, -2.268928F));

		root.addOrReplaceChild("top_ring_12_1",
				CubeListBuilder.create().texOffs(17, 19).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 3.0F, 1.0F),
				PartPose.offset(0.0F, 6.2F, -3.15F));

		root.addOrReplaceChild("top_ring_12_2",
				CubeListBuilder.create().texOffs(16, 15).addBox(1.3F, -5.5F, 2.63F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(-0.9F, 0.42F, -4.94F));

		root.addOrReplaceChild("blade_s1",
				CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(2.3F, 0.0F, -2.8F, 0.0F, 0.7853982F, 0.0F));

		root.addOrReplaceChild("blade_s2",
				CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(-3.6F, 0.0F, -4.3F, 0.0F, -0.7853982F, 0.0F));

		root.addOrReplaceChild("blade_s3",
				CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(0.5F, 0.0F, 3.6F, 0.0F, -1.5707964F, 0.0F));

		return LayerDefinition.create(mesh, 32, 32);
	}

	private static LayerDefinition createEmitter() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(body1, "b1", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 10.0F, 1.0F);

		PartDefinition sideRing4 = root.addOrReplaceChild("side_ring_4", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(sideRing4, "sr4", 16, 21, -1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition sideRing3 = root.addOrReplaceChild("side_ring_3", CubeListBuilder.create(),
				PartPose.offset(0.0F, 3.0F, 0.0F));
		addOctagonalShell(sideRing3, "sr3", 16, 21, -1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition sideRing2 = root.addOrReplaceChild("side_ring_2", CubeListBuilder.create(),
				PartPose.offset(0.0F, 6.1F, 0.0F));
		addOctagonalShell(sideRing2, "sr2", 16, 21, -1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition sideRing1 = root.addOrReplaceChild("side_ring_1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 10.0F, 0.0F));
		addOctagonalShell(sideRing1, "sr1", 16, 14, -1.5F, -1.0F, 2.62F, 3.0F, 2.0F, 1.0F);

		PartDefinition innerRing = root.addOrReplaceChild("inner_ring", CubeListBuilder.create(),
				PartPose.offset(0.0F, 8.74F, 0.0F));
		for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
			innerRing.addOrReplaceChild("in_" + i,
					CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 3.67F, 3.0F, 2.0F, 1.0F),
					PartPose.rotation(0.46949357F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition darkInnerRing = root.addOrReplaceChild("dark_inner_ring", CubeListBuilder.create(),
				PartPose.offset(0.0F, 12.1F, 0.0F));
		addOctagonalShell(darkInnerRing, "dark_in", 0, 0, -1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F);

		PartDefinition emitterTip = root.addOrReplaceChild("emitter_tip", CubeListBuilder.create(),
				PartPose.offset(0.0F, 13.0F, 0.0F));
		addOctagonalShell(emitterTip, "tip", 16, 25, -1.5F, 0.0F, 2.62F, 3.0F, 2.0F, 1.0F);

		root.addOrReplaceChild("claw_1",
				CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.0F, 0.0F, 1.0F, 18.0F, 1.0F),
				PartPose.offsetAndRotation(2.3F, 0.0F, -2.8F, 0.0F, 0.7853982F, 0.0F));

		root.addOrReplaceChild("claw_2",
				CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.0F, 0.0F, 1.0F, 18.0F, 1.0F),
				PartPose.offsetAndRotation(-2.9F, 0.0F, -2.3F, 0.0F, 2.3561945F, 0.0F));

		root.addOrReplaceChild("claw_3",
				CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.0F, 0.0F, 1.0F, 18.0F, 1.0F),
				PartPose.offsetAndRotation(0.5F, 0.0F, 3.6F, 0.0F, -1.5707964F, 0.0F));

		return LayerDefinition.create(mesh, 32, 32);
	}
}