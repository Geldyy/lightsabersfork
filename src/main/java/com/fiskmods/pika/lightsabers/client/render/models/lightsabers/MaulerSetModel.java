package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class MaulerSetModel extends HiltSetModel {

	private static final float[] OCTAGON_ANGLES = { 0.7853982F, 1.5707964F, 2.3561945F, 3.1415927F, -2.3561945F,
			-1.5707964F, -0.7853982F };

	private static final float[] BUTTON_ANGLES = { 0.7853982F, 1.5707964F, 2.3561945F, 3.1415927F, -2.3561945F,
			-1.5707964F, -0.7853982F };

	private static final float[] SIDE_PART_ANGLES = { 0.0F, 0.62831855F, 1.2566371F, 1.8849556F, 2.5132742F, 3.1415927F,
			-2.5132742F, -1.8849556F, -1.2566371F, -0.62831855F };

	private final ModelPart pommel;
	private final ModelPart body;
	private final ModelPart switchSection;
	private final ModelPart emitter;

	public MaulerSetModel() {
		super("mauler");

		this.pommel = createPommel().bakeRoot();
		if (this.pommel.hasChild("outerRing1")) {
			this.pommel.getChild("outerRing1").yScale = 0.25F;
		}
		if (this.pommel.hasChild("top1")) {
			ModelPart p = this.pommel.getChild("top1");
			p.xScale = 0.8F;
			p.zScale = 0.8F;
		}
		if (this.pommel.hasChild("innerRing1")) {
			ModelPart p = this.pommel.getChild("innerRing1");
			p.xScale = 0.65F;
			p.yScale = 1.3F;
			p.zScale = 0.65F;
		}

		this.body = createBody().bakeRoot();
		if (this.body.hasChild("upperCylinder1")) {
			ModelPart p = this.body.getChild("upperCylinder1");
			p.xScale = 1.1F;
			p.yScale = 1.1F;
			p.zScale = 1.1F;
		}
		if (this.body.hasChild("cylinder1")) {
			ModelPart p = this.body.getChild("cylinder1");
			p.xScale = 1.1F;
			p.yScale = 1.0F;
			p.zScale = 1.1F;
		}
		String[] rings = { "ring1_1", "ring2_1", "ring3_1", "ring4_1", "ring5_1" };
		for (String ringName : rings) {
			if (this.body.hasChild(ringName)) {
				ModelPart p = this.body.getChild(ringName);
				p.xScale = 1.2F;
				p.yScale = 0.8F;
				p.zScale = 1.2F;
			}
		}
		if (this.body.hasChild("button1_1")) {
			ModelPart p = this.body.getChild("button1_1");
			p.xScale = 0.3F;
			p.yScale = 0.3F;
			p.zScale = 0.3F;
		}
		if (this.body.hasChild("button2_1")) {
			ModelPart p = this.body.getChild("button2_1");
			p.xScale = 0.3F;
			p.yScale = 0.3F;
			p.zScale = 0.3F;
		}

		this.switchSection = createSwitch().bakeRoot();
		if (this.switchSection.hasChild("bottomConnector1")) {
			ModelPart p = this.switchSection.getChild("bottomConnector1");
			p.xScale = 0.9F;
			p.yScale = 0.9F;
			p.zScale = 0.9F;
		}
		if (this.switchSection.hasChild("topConnector1")) {
			ModelPart p = this.switchSection.getChild("topConnector1");
			p.xScale = 0.9F;
			p.yScale = 0.9F;
			p.zScale = 0.9F;
		}
		if (this.switchSection.hasChild("button1_1")) {
			ModelPart p = this.switchSection.getChild("button1_1");
			p.xScale = 0.3F;
			p.yScale = 0.3F;
			p.zScale = 0.3F;
		}
		if (this.switchSection.hasChild("button2_1")) {
			ModelPart p = this.switchSection.getChild("button2_1");
			p.xScale = 0.3F;
			p.yScale = 0.3F;
			p.zScale = 0.3F;
		}

		this.emitter = createEmitter().bakeRoot();
		if (this.emitter.hasChild("topRing1_1")) {
			ModelPart p = this.emitter.getChild("topRing1_1");
			p.xScale = 1.1F;
			p.yScale = 0.3F;
			p.zScale = 1.1F;
		}
		if (this.emitter.hasChild("topRing2_1")) {
			ModelPart p = this.emitter.getChild("topRing2_1");
			p.xScale = 1.1F;
			p.yScale = 0.3F;
			p.zScale = 1.1F;
		}
		if (this.emitter.hasChild("topRing_31")) {
			ModelPart p = this.emitter.getChild("topRing_31");
			p.xScale = 1.3F;
			p.yScale = 0.3F;
			p.zScale = 1.3F;
		}
		if (this.emitter.hasChild("topRing4_1")) {
			ModelPart p = this.emitter.getChild("topRing4_1");
			p.xScale = 1.3F;
			p.yScale = 0.3F;
			p.zScale = 1.3F;
		}
		if (this.emitter.hasChild("topRing5_1")) {
			ModelPart p = this.emitter.getChild("topRing5_1");
			p.xScale = 1.3F;
			p.yScale = 0.3F;
			p.zScale = 1.3F;
		}
		if (this.emitter.hasChild("innerRing1_1")) {
			ModelPart p = this.emitter.getChild("innerRing1_1");
			p.xScale = 0.98F;
			p.yScale = 1.0F;
			p.zScale = 0.98F;
		}
		if (this.emitter.hasChild("innerRing2_1")) {
			ModelPart p = this.emitter.getChild("innerRing2_1");
			p.xScale = 0.81F;
			p.yScale = 0.8F;
			p.zScale = 0.81F;
		}
		if (this.emitter.hasChild("innerCone1")) {
			ModelPart p = this.emitter.getChild("innerCone1");
			p.xScale = 0.75F;
			p.yScale = 0.8F;
			p.zScale = 0.75F;
		}
		if (this.emitter.hasChild("sidePart1")) {
			ModelPart p = this.emitter.getChild("sidePart1");
			p.xScale = 0.6F;
			p.yScale = 0.6F;
			p.zScale = 0.6F;
		}
		if (this.emitter.hasChild("innerCylinder1_1")) {
			ModelPart p = this.emitter.getChild("innerCylinder1_1");
			p.xScale = 0.95F;
			p.yScale = 1.7F;
			p.zScale = 0.95F;
		}
		if (this.emitter.hasChild("innerCylinder2_1")) {
			ModelPart p = this.emitter.getChild("innerCylinder2_1");
			p.xScale = 1.1F;
			p.yScale = 1.0F;
			p.zScale = 1.1F;
		}
		if (this.emitter.hasChild("emitterTop1")) {
			ModelPart p = this.emitter.getChild("emitterTop1");
			p.xScale = 1.2F;
			p.yScale = 0.7F;
			p.zScale = 1.2F;
		}
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
		case POMMEL -> 1.0F;
		case BODY -> 21.6F;
		case SWITCH_SECTION -> 12.0F;
		case EMITTER -> 17.9F;
		};
	}

	private static void addOctagonalShell(PartDefinition parent, String prefix, int u, int v, float minX, float minY,
			float minZ, float sizeX, float sizeY, float sizeZ) {
		for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
			parent.addOrReplaceChild(prefix + "_" + i,
					CubeListBuilder.create().texOffs(u, v).addBox(minX, minY, minZ, sizeX, sizeY, sizeZ),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}
	}

	private static void addOctagonalButton(PartDefinition buttonRoot, int u, int v) {
		for (int i = 0; i < BUTTON_ANGLES.length; i++) {
			buttonRoot.addOrReplaceChild("btn_" + i,
					CubeListBuilder.create().texOffs(u, v).addBox(-1.5F, -3.0F, -0.38F, 3.0F, 3.0F, 4.0F),
					PartPose.rotation(0.0F, BUTTON_ANGLES[i], 0.0F));
		}
	}

	private static LayerDefinition createPommel() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition outerRing1 = root.addOrReplaceChild("outerRing1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.ZERO);
		addOctagonalShell(outerRing1, "outerRing", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition top1 = root.addOrReplaceChild("top1",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.ZERO);
		addOctagonalShell(top1, "top", 8, 0, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition innerRing1 = root.addOrReplaceChild("innerRing1",
				CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.ZERO);
		addOctagonalShell(innerRing1, "innerRing", 0, 2, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createBody() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 12.0F, 1.0F), PartPose.ZERO);
		addOctagonalShell(body1, "body", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 12.0F, 1.0F);

		PartDefinition button1_1 = root.addOrReplaceChild("button1_1",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, -3.0F, -0.38F, 3.0F, 3.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, 4.5F, -3.62F, 1.5707964F, 0.0F, 0.0F));
		addOctagonalButton(button1_1, 8, 0);

		PartDefinition button2_1 = root.addOrReplaceChild("button2_1",
				CubeListBuilder.create().texOffs(8, 7).addBox(-1.5F, -3.0F, -0.38F, 3.0F, 3.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, 9.0F, -3.62F, 1.5707964F, 0.0F, 0.0F));
		addOctagonalButton(button2_1, 8, 7);

		PartDefinition cylinder1 = root.addOrReplaceChild("cylinder1",
				CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.offset(0.0F, 12.0F, 0.0F));
		addOctagonalShell(cylinder1, "cylinder", 0, 13, -1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F);

		PartDefinition upperCylinder1 = root.addOrReplaceChild("upperCylinder1",
				CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 6.0F, 1.0F),
				PartPose.offset(0.0F, 15.0F, 0.0F));
		addOctagonalShell(upperCylinder1, "upperCylinder", 0, 17, -1.5F, 0.0F, 2.62F, 3.0F, 6.0F, 1.0F);

		float[] ringYPositions = { 15.2F, 16.6F, 18.0F, 19.4F, 20.8F };
		for (int i = 0; i < ringYPositions.length; i++) {
			PartDefinition ring = root.addOrReplaceChild("ring" + (i + 1) + "_1",
					CubeListBuilder.create().texOffs(8, 14).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F),
					PartPose.offset(0.0F, ringYPositions[i], 0.0F));
			addOctagonalShell(ring, "ring" + (i + 1), 8, 14, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);
		}

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createSwitch() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 3).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 12.0F, 1.0F), PartPose.ZERO);
		addOctagonalShell(body1, "body", 0, 3, -1.5F, 0.0F, 2.62F, 3.0F, 12.0F, 1.0F);

		PartDefinition bottomConnector1 = root.addOrReplaceChild("bottomConnector1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.ZERO);
		addOctagonalShell(bottomConnector1, "bottomConnector", 0, 0, -1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F);

		PartDefinition topConnector1 = root.addOrReplaceChild("topConnector1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, 12.0F, 0.0F));
		addOctagonalShell(topConnector1, "topConnector", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 2.0F, 1.0F);

		PartDefinition button1_1 = root.addOrReplaceChild("button1_1",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, -3.0F, -0.38F, 3.0F, 3.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, 2.5F, -3.62F, 1.5707964F, 0.0F, 0.0F));
		addOctagonalButton(button1_1, 8, 0);

		PartDefinition button2_1 = root.addOrReplaceChild("button2_1",
				CubeListBuilder.create().texOffs(8, 7).addBox(-1.5F, -3.0F, -0.38F, 3.0F, 3.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, 7.0F, -3.62F, 1.5707964F, 0.0F, 0.0F));
		addOctagonalButton(button2_1, 8, 7);

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createEmitter() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.ZERO);
		addOctagonalShell(body1, "body", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F);

		PartDefinition bodyCurve1 = root.addOrReplaceChild("bodyCurve1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 4.6F, 0.0F));
		bodyCurve1.addOrReplaceChild("bodyCurve2",
				CubeListBuilder.create().texOffs(4, 20).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 2.0F, 1.0F),
				PartPose.rotation(0.34906584F, 0.0F, 0.0F));
		for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
			bodyCurve1.addOrReplaceChild("bodyCurve_" + i,
					CubeListBuilder.create().texOffs(4, 20).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 2.0F, 1.0F),
					PartPose.rotation(0.34906584F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition innerRing1_1 = root.addOrReplaceChild("innerRing1_1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 4.51F, 0.0F));
		for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
			innerRing1_1.addOrReplaceChild("innerRing1_" + i,
					CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, 3.24F, 3.0F, 3.0F, 1.0F),
					PartPose.rotation(-0.17453292F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition innerCone1 = root.addOrReplaceChild("innerCone1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 7.0F, 0.0F));
		for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
			innerCone1.addOrReplaceChild("innerCone_" + i,
					CubeListBuilder.create().texOffs(4, 11).addBox(-1.5F, 0.0F, 3.24F, 3.0F, 8.0F, 1.0F),
					PartPose.rotation(-0.17453292F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition innerRing2_1 = root.addOrReplaceChild("innerRing2_1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 8.51F, 0.0F));
		for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
			innerRing2_1.addOrReplaceChild("innerRing2_" + i,
					CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, 3.24F, 3.0F, 3.0F, 1.0F),
					PartPose.rotation(-0.17453292F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition sidePart1 = root.addOrReplaceChild("sidePart1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 4.8F, 0.0F));
		for (int i = 0; i < SIDE_PART_ANGLES.length; i++) {
			PartDefinition fin = sidePart1.addOrReplaceChild("fin_" + i,
					CubeListBuilder.create().texOffs(0, 5).addBox(-0.5F, 0.0F, -7.9F, 1.0F, 15.0F, 1.0F),
					PartPose.rotation(0.17453292F, SIDE_PART_ANGLES[i], 0.0F));
			fin.addOrReplaceChild("fin_tip_" + i,
					CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 3.0F, 1.0F),
					PartPose.offsetAndRotation(0.0F, 0.2F, -7.25F, 0.87266463F, 0.0F, 0.0F));
		}

		PartDefinition innerCylinder1_1 = root.addOrReplaceChild("innerCylinder1_1",
				CubeListBuilder.create().texOffs(16, 0).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, 13.5F, 0.0F));
		addOctagonalShell(innerCylinder1_1, "innerCyl1", 16, 0, -1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F);

		PartDefinition innerCylinder2_1 = root.addOrReplaceChild("innerCylinder2_1",
				CubeListBuilder.create().texOffs(4, 7).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F),
				PartPose.offset(0.0F, 14.9F, 0.0F));
		addOctagonalShell(innerCylinder2_1, "innerCyl2", 4, 7, -1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F);

		float[] topRingY = { 13.5F, 14.2F, 15.2F, 15.9F, 16.6F };
		String[] topRingNames = { "topRing1_1", "topRing2_1", "topRing_31", "topRing4_1", "topRing5_1" };
		for (int i = 0; i < topRingY.length; i++) {
			PartDefinition r = root.addOrReplaceChild(topRingNames[i],
					CubeListBuilder.create().texOffs(4, 5).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F),
					PartPose.offset(0.0F, topRingY[i], 0.0F));
			addOctagonalShell(r, "topRing_" + i, 4, 5, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);
		}

		PartDefinition emitterTop1 = root.addOrReplaceChild("emitterTop1",
				CubeListBuilder.create().texOffs(12, 4).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, 16.9F, 0.0F));
		addOctagonalShell(emitterTop1, "emitterTop", 12, 4, -1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}
}