package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class MechanicalSetModel extends HiltSetModel {

	private final ModelPart pommel;
	private final ModelPart body;
	private final ModelPart switchSection;
	private final ModelPart emitter;

	public MechanicalSetModel() {
		super("mechanical");

		this.pommel = createPommel().bakeRoot();
		if (this.pommel.hasChild("body1")) {
			this.pommel.getChild("body1").yScale = 0.5F;
		}
		if (this.pommel.hasChild("body1_2")) {
			this.pommel.getChild("body1_2").yScale = 0.5F;
		}
		if (this.pommel.hasChild("body1_1")) {
			this.pommel.getChild("body1_1").yScale = 0.5F;
		}
		if (this.pommel.hasChild("top1_1")) {
			ModelPart p = this.pommel.getChild("top1_1");
			p.xScale = 0.8F;
			p.zScale = 0.8F;
		}
		if (this.pommel.hasChild("top1")) {
			ModelPart p = this.pommel.getChild("top1");
			p.xScale = 0.8F;
			p.zScale = 0.8F;
		}

		this.body = createBody().bakeRoot();
		if (this.body.hasChild("frontRing1")) {
			ModelPart p = this.body.getChild("frontRing1");
			p.xScale = 0.4F;
			p.yScale = 0.4F;
			p.zScale = 0.4F;
		}
		if (this.body.hasChild("frontRing2")) {
			ModelPart p = this.body.getChild("frontRing2");
			p.xScale = 0.3F;
			p.yScale = 0.3F;
			p.zScale = 0.3F;
		}
		if (this.body.hasChild("frontRing3")) {
			ModelPart p = this.body.getChild("frontRing3");
			p.xScale = 0.4F;
			p.yScale = 0.4F;
			p.zScale = 0.4F;
		}
		if (this.body.hasChild("frontRing4")) {
			ModelPart p = this.body.getChild("frontRing4");
			p.xScale = 0.2F;
			p.yScale = 0.2F;
			p.zScale = 0.2F;
		}

		this.switchSection = createSwitch().bakeRoot();
		if (this.switchSection.hasChild("body1")) {
			ModelPart p = this.switchSection.getChild("body1");
			p.xScale = 1.1F;
			p.yScale = 1.1F;
			p.zScale = 1.1F;
		}
		if (this.switchSection.hasChild("switchPanel")) {
			ModelPart p = this.switchSection.getChild("switchPanel");
			p.xScale = 1.2F;
			p.yScale = 1.1F;
			p.zScale = 1.4F;
		}
		if (this.switchSection.hasChild("switch1")) {
			ModelPart p = this.switchSection.getChild("switch1");
			p.xScale = 0.5F;
			p.yScale = 0.6F;
			p.zScale = 1.0F;
		}
		if (this.switchSection.hasChild("switch2")) {
			ModelPart p = this.switchSection.getChild("switch2");
			p.xScale = 0.5F;
			p.yScale = 0.6F;
			p.zScale = 1.0F;
		}

		this.emitter = createEmitter().bakeRoot();
		if (this.emitter.hasChild("guard3")) {
			this.emitter.getChild("guard3").xScale = 1.04F;
		}
		if (this.emitter.hasChild("frontButton1")) {
			ModelPart p = this.emitter.getChild("frontButton1");
			p.xScale = 0.3F;
			p.yScale = 0.3F;
			p.zScale = 0.3F;
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
		case POMMEL -> 2.5F;
		case BODY -> 16.0F;
		case SWITCH_SECTION -> 8.8F;
		case EMITTER -> 16.0F;
		};
	}

	private static LayerDefinition createPommel() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		float[] angles = { 0.7853982F, 1.5707964F, 2.3561945F, 3.1415927F, -2.3561945F, -1.5707964F, -0.7853982F };

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F), PartPose.ZERO);
		for (int i = 0; i < angles.length; i++) {
			body1.addOrReplaceChild("body" + (i + 2),
					CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
					PartPose.rotation(0.0F, angles[i], 0.0F));
		}

		PartDefinition body1_2 = root.addOrReplaceChild("body1_2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
				PartPose.offset(0.0F, 1.0F, 0.0F));
		for (int i = 0; i < angles.length; i++) {
			body1_2.addOrReplaceChild("body" + (i + 2) + "_2",
					CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
					PartPose.rotation(0.0F, angles[i], 0.0F));
		}

		PartDefinition body1_1 = root.addOrReplaceChild("body1_1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
				PartPose.offset(0.0F, 2.0F, 0.0F));
		for (int i = 0; i < angles.length; i++) {
			body1_1.addOrReplaceChild("body" + (i + 2) + "_1",
					CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
					PartPose.rotation(0.0F, angles[i], 0.0F));
		}

		PartDefinition top1_1 = root.addOrReplaceChild("top1_1",
				CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 1.0F, 0.0F));
		for (int i = 0; i < angles.length; i++) {
			top1_1.addOrReplaceChild("top" + (i + 2) + "_1",
					CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
					PartPose.rotation(0.0F, angles[i], 0.0F));
		}

		PartDefinition top1 = root.addOrReplaceChild("top1",
				CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 2.0F, 0.0F));
		for (int i = 0; i < angles.length; i++) {
			top1.addOrReplaceChild("top" + (i + 2),
					CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
					PartPose.rotation(0.0F, angles[i], 0.0F));
		}

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createBody() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.ZERO);
		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		PartDefinition body5 = body1.addOrReplaceChild("body5",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		PartDefinition body8 = body1.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		PartDefinition body11 = body1.addOrReplaceChild("body11",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		PartDefinition body14 = body1.addOrReplaceChild("body14",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		PartDefinition body17 = body1.addOrReplaceChild("body17",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		PartDefinition body20 = body1.addOrReplaceChild("body20",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition[] wallSegments = { body1, body2, body5, body8, body11, body14, body17, body20 };
		for (int i = 0; i < wallSegments.length; i++) {
			PartDefinition trackBase = wallSegments[i].addOrReplaceChild("track_base_" + i,
					CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -7.5F, 0.5F, 2.0F, 15.0F, 1.0F),
					PartPose.offset(0.0F, 8.3F, 2.6F));
			trackBase.addOrReplaceChild("track_ridge_" + i,
					CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -7.5F, 0.5F, 1.0F, 15.0F, 1.0F),
					PartPose.offset(0.0F, 0.0F, 0.6F));
		}

		float[] ringAngles = { 0.7853982F, 1.5707964F, 2.3561945F, 3.1415927F, -2.3561945F, -1.5707964F, -0.7853982F };

		PartDefinition frontRing1 = root.addOrReplaceChild("frontRing1",
				CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 2.6F, 3.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-2.5F, 13.0F, -2.5F, 1.5707964F, 0.7853982F, 0.0F));
		for (int i = 0; i < ringAngles.length; i++) {
			frontRing1.addOrReplaceChild("frontRing1_" + i,
					CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F),
					PartPose.rotation(0.0F, ringAngles[i], 0.0F));
		}

		PartDefinition frontRing2 = root.addOrReplaceChild("frontRing2",
				CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 2.6F, 3.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-3.0F, 13.0F, -3.0F, 1.5707964F, 0.7853982F, 0.0F));
		for (int i = 0; i < ringAngles.length; i++) {
			frontRing2.addOrReplaceChild("frontRing2_" + i,
					CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F),
					PartPose.rotation(0.0F, ringAngles[i], 0.0F));
		}

		PartDefinition frontRing3 = root.addOrReplaceChild("frontRing3",
				CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -2.0F, -0.4F, 3.0F, 2.0F, 4.0F),
				PartPose.offsetAndRotation(-3.4F, 13.0F, -3.4F, 1.5707964F, 0.7853982F, 0.0F));
		for (int i = 0; i < ringAngles.length; i++) {
			frontRing3.addOrReplaceChild("frontRing3_" + i,
					CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -2.0F, -0.38F, 3.0F, 2.0F, 4.0F),
					PartPose.rotation(0.0F, ringAngles[i], 0.0F));
		}

		PartDefinition frontRing4 = root.addOrReplaceChild("frontRing4",
				CubeListBuilder.create().texOffs(0, 26).addBox(-1.5F, -2.0F, 2.6F, 3.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-3.7F, 13.0F, -3.7F, 1.5707964F, 0.7853982F, 0.0F));
		for (int i = 0; i < ringAngles.length; i++) {
			frontRing4.addOrReplaceChild("frontRing4_" + i,
					CubeListBuilder.create().texOffs(0, 26).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F),
					PartPose.rotation(0.0F, ringAngles[i], 0.0F));
		}

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createSwitch() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F), PartPose.ZERO);
		body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		body1.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		body1.addOrReplaceChild("body4",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		body1.addOrReplaceChild("body5",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		PartDefinition body7 = body1.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		body1.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		body7.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -4.0F, 0.0F, 3.0F, 8.0F, 2.0F),
				PartPose.offset(0.0F, 4.0F, 3.0F));

		root.addOrReplaceChild("switchPanel",
				CubeListBuilder.create().texOffs(8, 20).addBox(-2.0F, -4.1F, 0.0F, 2.0F, 8.0F, 3.0F),
				PartPose.offset(-3.0F, 4.5F, -2.2F));

		root.addOrReplaceChild("switch1",
				CubeListBuilder.create().texOffs(10, 3).addBox(0.1F, -8.48F, -1.87F, 2.0F, 9.0F, 2.0F),
				PartPose.offsetAndRotation(-4.7F, 6.0F, -1.1F, -0.10471976F, 0.0F, 0.0F));

		root.addOrReplaceChild("switch2",
				CubeListBuilder.create().texOffs(10, 3).addBox(0.1F, -0.45F, -2.02F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(-4.7F, 5.7F, -1.1F, 0.61086524F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createEmitter() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(32, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F), PartPose.ZERO);

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().mirror().texOffs(24, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 18.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));

		PartDefinition body6 = body1.addOrReplaceChild("body6",
				CubeListBuilder.create().mirror().texOffs(16, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));

		PartDefinition body10 = body1.addOrReplaceChild("body10",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 15.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));

		body1.addOrReplaceChild("body12",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 15.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body13 = body1.addOrReplaceChild("body13",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 15.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));

		PartDefinition body15 = body1.addOrReplaceChild("body15",
				CubeListBuilder.create().texOffs(16, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));

		PartDefinition body19 = body1.addOrReplaceChild("body19",
				CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 18.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		body1.addOrReplaceChild("body28",
				CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -0.5F, -0.49F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-0.8F, 19.76F, 3.1F, -0.29670596F, -0.2617994F, 0.20943952F));

		body1.addOrReplaceChild("body29",
				CubeListBuilder.create().mirror().texOffs(0, 30).addBox(-1.0F, -0.5F, -0.49F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.8F, 19.76F, 3.1F, -0.29670596F, 0.2617994F, -0.20943952F));

		PartDefinition body3 = body2.addOrReplaceChild("body3",
				CubeListBuilder.create().mirror().texOffs(4, 23).addBox(-0.5F, 0.0F, -0.49F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(-1.0F, 18.0F, 3.12F));
		PartDefinition body4 = body3.addOrReplaceChild("body4",
				CubeListBuilder.create().mirror().texOffs(0, 24).addBox(0.0F, -2.5F, -0.49F, 1.0F, 3.0F, 1.0F),
				PartPose.offsetAndRotation(0.13F, 0.94F, 0.0F, 0.0F, 0.0F, 0.7679449F));
		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().mirror().texOffs(8, 21).addBox(-1.0F, -0.5F, -0.49F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition body20 = body19.addOrReplaceChild("body20",
				CubeListBuilder.create().texOffs(4, 23).addBox(-0.5F, 0.0F, -0.49F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(1.0F, 18.0F, 3.12F));
		PartDefinition body21 = body20.addOrReplaceChild("body21",
				CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -2.5F, -0.49F, 1.0F, 3.0F, 1.0F),
				PartPose.offsetAndRotation(-0.13F, 0.94F, 0.0F, 0.0F, 0.0F, -0.7679449F));
		body21.addOrReplaceChild("body22",
				CubeListBuilder.create().texOffs(8, 21).addBox(0.0F, -0.5F, -0.49F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		body6.addOrReplaceChild("body7",
				CubeListBuilder.create().mirror().texOffs(8, 19).addBox(-3.0F, 0.0F, -0.49F, 3.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.91F, 15.19F, 3.12F, 0.0F, 0.0F, -0.62831855F));
		PartDefinition body8 = body6.addOrReplaceChild("body8",
				CubeListBuilder.create().mirror().texOffs(0, 21).addBox(-0.5F, -0.5F, -0.49F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(-1.0F, 15.9F, 3.12F));
		body8.addOrReplaceChild("body9",
				CubeListBuilder.create().mirror().texOffs(4, 21).addBox(-0.6F, -0.5F, -0.49F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(0.8F, 0.2F, 0.0F));

		body15.addOrReplaceChild("body16",
				CubeListBuilder.create().texOffs(8, 19).addBox(0.0F, 0.0F, -0.49F, 3.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-0.91F, 15.19F, 3.12F, 0.0F, 0.0F, 0.62831855F));
		PartDefinition body17 = body15.addOrReplaceChild("body17",
				CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, -0.5F, -0.49F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(1.0F, 15.9F, 3.12F));
		body17.addOrReplaceChild("body18",
				CubeListBuilder.create().texOffs(4, 21).addBox(-0.4F, -0.5F, -0.49F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(-0.8F, 0.2F, 0.0F));

		body10.addOrReplaceChild("body11",
				CubeListBuilder.create().mirror().texOffs(0, 19).addBox(-3.0F, 0.0F, -0.49F, 3.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.16F, 14.06F, 3.12F, 0.0F, 0.0F, -0.33161256F));
		body13.addOrReplaceChild("body14",
				CubeListBuilder.create().texOffs(0, 19).addBox(0.0F, 0.0F, -0.49F, 3.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.16F, 14.06F, 3.12F, 0.0F, 0.0F, 0.33161256F));

		PartDefinition guard1 = root.addOrReplaceChild("guard1",
				CubeListBuilder.create().texOffs(34, 17).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 2.0F, 7.0F),
				PartPose.offset(0.0F, 12.0F, -2.5F));
		guard1.addOrReplaceChild("guard2",
				CubeListBuilder.create().texOffs(44, 21).addBox(0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.7958701F, 0.0F));

		root.addOrReplaceChild("guard3",
				CubeListBuilder.create().texOffs(44, 21).addBox(0.0F, -2.0F, 0.0F, 5.0F, 2.0F, 3.0F),
				PartPose.offset(-2.6F, 13.0F, -3.94F));
		root.addOrReplaceChild("guard4",
				CubeListBuilder.create().texOffs(44, 21).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 3.0F),
				PartPose.offsetAndRotation(2.61F, 13.0F, -3.94F, 0.0F, -0.80285144F, 0.0F));
		root.addOrReplaceChild("guard5",
				CubeListBuilder.create().texOffs(40, 10).addBox(0.0F, -3.0F, 0.0F, 8.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(-4.0F, 11.12F, 0.38F, -0.7853982F, 0.0F, 0.0F));
		root.addOrReplaceChild("guard6",
				CubeListBuilder.create().texOffs(40, 17).addBox(0.0F, -2.0F, 0.0F, 8.0F, 2.0F, 2.0F),
				PartPose.offset(-4.0F, 11.0F, 2.5F));

		root.addOrReplaceChild("panel1",
				CubeListBuilder.create().texOffs(26, 22).addBox(0.0F, -5.0F, 0.0F, 2.0F, 5.0F, 1.0F),
				PartPose.offset(-1.0F, 9.0F, 3.0F));
		root.addOrReplaceChild("panel2",
				CubeListBuilder.create().texOffs(26, 22).addBox(0.0F, -3.0F, 0.0F, 2.0F, 3.0F, 1.0F),
				PartPose.offset(-1.0F, 16.0F, 3.0F));

		PartDefinition frontButton1 = root.addOrReplaceChild("frontButton1",
				CubeListBuilder.create().texOffs(41, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 4.0F, 4.0F),
				PartPose.offsetAndRotation(1.8F, 6.0F, 2.0F, -1.5707964F, -2.3561945F, 0.0F));
		frontButton1.addOrReplaceChild("frontButton2",
				CubeListBuilder.create().texOffs(41, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 4.0F, 4.0F),
				PartPose.rotation(0.0F, 2.1816616F, 0.0F));
		frontButton1.addOrReplaceChild("frontButton3",
				CubeListBuilder.create().texOffs(41, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 4.0F, 4.0F),
				PartPose.rotation(0.0F, -2.1816616F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}