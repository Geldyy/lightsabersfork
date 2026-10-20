package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class GraflexSetModel extends HiltSetModel {

	private final ModelPart pommel;
	private final ModelPart body;
	private final ModelPart switchSection;
	private final ModelPart emitter;

	public GraflexSetModel() {
		super("graflex");

		this.pommel = createPommel().bakeRoot();
		if (this.pommel.hasChild("top1")) {
			ModelPart p = this.pommel.getChild("top1");
			p.xScale = 0.8F;
			p.yScale = 1.0F;
			p.zScale = 0.8F;
		}
		if (this.pommel.hasChild("body1")) {
			ModelPart p = this.pommel.getChild("body1");
			p.xScale = 1.0F;
			p.yScale = 0.5F;
			p.zScale = 1.0F;
		}
		if (this.pommel.hasChild("bottom1")) {
			ModelPart p = this.pommel.getChild("bottom1");
			p.xScale = 0.4F;
			p.yScale = 0.4F;
			p.zScale = 0.4F;
		}

		this.body = createBody().bakeRoot();

		this.switchSection = createSwitch().bakeRoot();
		if (this.switchSection.hasChild("body1")) {
			ModelPart p = this.switchSection.getChild("body1");
			p.xScale = 1.1F;
			p.yScale = 1.1F;
			p.zScale = 1.1F;
		}
		if (this.switchSection.hasChild("switch1")) {
			ModelPart p = this.switchSection.getChild("switch1");
			p.xScale = 0.6F;
			p.yScale = 0.6F;
			p.zScale = 0.6F;
		}

		this.emitter = createEmitter().bakeRoot();
		this.emitter.yRot = 3.1415927F;
		if (this.emitter.hasChild("upperRing1")) {
			ModelPart p = this.emitter.getChild("upperRing1");
			p.xScale = 0.7F;
			p.yScale = 0.7F;
			p.zScale = 0.7F;
		}
		if (this.emitter.hasChild("frontButton1")) {
			ModelPart p = this.emitter.getChild("frontButton1");
			p.xScale = 0.3F;
			p.yScale = 0.3F;
			p.zScale = 0.3F;
		}
		if (this.emitter.hasChild("frontRing1")) {
			ModelPart p = this.emitter.getChild("frontRing1");
			p.xScale = 0.4F;
			p.yScale = 0.4F;
			p.zScale = 0.4F;
		}
		if (this.emitter.hasChild("backButton1")) {
			ModelPart p = this.emitter.getChild("backButton1");
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
		case POMMEL -> 1.0F;
		case BODY -> 16.0F;
		case SWITCH_SECTION -> 8.8F;
		case EMITTER -> 16.0F;
		};
	}

	private static LayerDefinition createPommel() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition top1 = root.addOrReplaceChild("top1",
				CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 1, 1), PartPose.ZERO);
		top1.addOrReplaceChild("top2", CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 1, 1),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		top1.addOrReplaceChild("top3", CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 1, 1),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		top1.addOrReplaceChild("top4", CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 1, 1),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		top1.addOrReplaceChild("top5", CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 1, 1),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		top1.addOrReplaceChild("top6", CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 1, 1),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		top1.addOrReplaceChild("top7", CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 1, 1),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		top1.addOrReplaceChild("top8", CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 1, 1),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3, 1, 4), PartPose.ZERO);
		body1.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3, 1, 4),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		body1.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3, 1, 4),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		body1.addOrReplaceChild("body4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3, 1, 4),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		body1.addOrReplaceChild("body5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3, 1, 4),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		body1.addOrReplaceChild("body6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3, 1, 4),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		body1.addOrReplaceChild("body7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3, 1, 4),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		body1.addOrReplaceChild("body8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3, 1, 4),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));
		body1.addOrReplaceChild("body9", CubeListBuilder.create().texOffs(0, 5).addBox(-2.5F, 0.0F, -1.0F, 5, 1, 2),
				PartPose.offsetAndRotation(-0.2F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0017453292F));

		PartDefinition bottom1 = root.addOrReplaceChild("bottom1",
				CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -1.0F, -3.0F, 2, 2, 6),
				PartPose.offset(2.3F, 0.0F, 0.0F));
		PartDefinition bottom2 = bottom1.addOrReplaceChild("bottom2",
				CubeListBuilder.create().mirror().texOffs(7, 7).addBox(-0.5F, -0.5F, -4.5F, 1, 1, 9),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4712389F));
		PartDefinition bottom3 = bottom2.addOrReplaceChild("bottom3",
				CubeListBuilder.create().mirror().texOffs(10, 2).addBox(0.0F, -0.5F, -0.5F, 4, 1, 1),
				PartPose.offset(0.5F, 0.0F, -4.0F));
		PartDefinition bottom4 = bottom3.addOrReplaceChild("bottom4",
				CubeListBuilder.create().mirror().texOffs(0, 8).addBox(0.0F, -0.5F, -0.5F, 2, 1, 1),
				PartPose.offsetAndRotation(3.68F, 0.0F, -0.12F, 0.0F, -0.6981317F, 0.0F));
		bottom4.addOrReplaceChild("bottom5",
				CubeListBuilder.create().mirror().texOffs(0, 10).addBox(0.0F, -0.5F, -0.5F, 2, 1, 1),
				PartPose.offsetAndRotation(1.78F, 0.0F, -0.05F, 0.0F, -0.4537856F, 0.0F));

		PartDefinition bottom6 = bottom2.addOrReplaceChild("bottom6",
				CubeListBuilder.create().mirror().texOffs(10, 2).addBox(0.0F, -0.5F, -0.5F, 4, 1, 1),
				PartPose.offset(0.5F, 0.0F, 4.0F));
		PartDefinition bottom7 = bottom6.addOrReplaceChild("bottom7",
				CubeListBuilder.create().mirror().texOffs(0, 8).addBox(0.0F, -0.5F, -0.5F, 2, 1, 1),
				PartPose.offsetAndRotation(3.68F, 0.0F, 0.12F, 0.0F, 0.6981317F, 0.0F));
		bottom7.addOrReplaceChild("bottom8",
				CubeListBuilder.create().mirror().texOffs(0, 10).addBox(0.0F, -0.5F, -0.5F, 2, 1, 1),
				PartPose.offsetAndRotation(1.78F, 0.0F, 0.05F, 0.0F, 0.4537856F, 0.0F));

		bottom2.addOrReplaceChild("bottom9",
				CubeListBuilder.create().mirror().texOffs(17, 1).addBox(-0.5F, -0.5F, -1.5F, 1, 1, 3),
				PartPose.offset(6.35F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createBody() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 16, 1), PartPose.ZERO);

		PartDefinition body23 = body1.addOrReplaceChild("body23",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -7.5F, 0.5F, 2, 15, 1),
				PartPose.offset(0.0F, 8.3F, 2.6F));
		body23.addOrReplaceChild("body24", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -7.5F, 0.5F, 1, 15, 1),
				PartPose.offset(0.0F, 0.0F, 0.6F));

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 16, 1),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		PartDefinition body3 = body2.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -7.5F, 0.5F, 2, 15, 1),
				PartPose.offset(0.0F, 8.3F, 2.6F));
		body3.addOrReplaceChild("body4", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -7.5F, 0.5F, 1, 15, 1),
				PartPose.offset(0.0F, 0.0F, 0.6F));

		PartDefinition body5 = body1.addOrReplaceChild("body5",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 16, 1),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		PartDefinition body6 = body5.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -7.5F, 0.5F, 2, 15, 1),
				PartPose.offset(0.0F, 8.3F, 2.6F));
		body6.addOrReplaceChild("body7", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -7.5F, 0.5F, 1, 15, 1),
				PartPose.offset(0.0F, 0.0F, 0.6F));

		PartDefinition body8 = body1.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 16, 1),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		PartDefinition body9 = body8.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -7.5F, 0.5F, 2, 15, 1),
				PartPose.offset(0.0F, 8.3F, 2.6F));
		body9.addOrReplaceChild("body10", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -7.5F, 0.5F, 1, 15, 1),
				PartPose.offset(0.0F, 0.0F, 0.6F));

		PartDefinition body11 = body1.addOrReplaceChild("body11",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 16, 1),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		PartDefinition body12 = body11.addOrReplaceChild("body12",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -7.5F, 0.5F, 2, 15, 1),
				PartPose.offset(0.0F, 8.3F, 2.6F));
		body12.addOrReplaceChild("body13", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -7.5F, 0.5F, 1, 15, 1),
				PartPose.offset(0.0F, 0.0F, 0.6F));

		PartDefinition body14 = body1.addOrReplaceChild("body14",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 16, 1),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		PartDefinition body15 = body14.addOrReplaceChild("body15",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -7.5F, 0.5F, 2, 15, 1),
				PartPose.offset(0.0F, 8.3F, 2.6F));
		body15.addOrReplaceChild("body16", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -7.5F, 0.5F, 1, 15, 1),
				PartPose.offset(0.0F, 0.0F, 0.6F));

		PartDefinition body17 = body1.addOrReplaceChild("body17",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 16, 1),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		PartDefinition body18 = body17.addOrReplaceChild("body18",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -7.5F, 0.5F, 2, 15, 1),
				PartPose.offset(0.0F, 8.3F, 2.6F));
		body18.addOrReplaceChild("body19", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -7.5F, 0.5F, 1, 15, 1),
				PartPose.offset(0.0F, 0.0F, 0.6F));

		PartDefinition body20 = body1.addOrReplaceChild("body20",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 16, 1),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));
		PartDefinition body21 = body20.addOrReplaceChild("body21",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -7.5F, 0.5F, 2, 15, 1),
				PartPose.offset(0.0F, 8.3F, 2.6F));
		body21.addOrReplaceChild("body22", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -7.5F, 0.5F, 1, 15, 1),
				PartPose.offset(0.0F, 0.0F, 0.6F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createSwitch() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 8, 1), PartPose.ZERO);
		body1.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 8, 1),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		body1.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 8, 1),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		body1.addOrReplaceChild("body4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 8, 1),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		body1.addOrReplaceChild("body5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 8, 1),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		body1.addOrReplaceChild("body6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 8, 1),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));

		PartDefinition body7 = body1.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 8, 1),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		body7.addOrReplaceChild("body8", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -4.0F, 0.0F, 3, 8, 2),
				PartPose.offset(0.0F, 4.0F, 3.0F));

		body1.addOrReplaceChild("body9", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 8, 1),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition switch1 = root.addOrReplaceChild("switch1",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -1.0F, -1.0F, 2, 2, 1),
				PartPose.offset(-4.7F, 6.0F, -1.4F));
		switch1.addOrReplaceChild("switch2",
				CubeListBuilder.create().texOffs(8, 3).addBox(-1.0F, -7.5F, -1.0F, 2, 9, 1),
				PartPose.offsetAndRotation(0.0F, 0.0F, -0.8F, -0.10471976F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createEmitter() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(32, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 20, 1), PartPose.ZERO);

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().mirror().texOffs(24, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 18, 1),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		PartDefinition body3 = body2.addOrReplaceChild("body3",
				CubeListBuilder.create().mirror().texOffs(4, 23).addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1),
				PartPose.offset(-1.0F, 18.0F, 3.12F));
		PartDefinition body4 = body3.addOrReplaceChild("body4",
				CubeListBuilder.create().mirror().texOffs(0, 24).addBox(0.0F, -0.5F, -0.5F, 1, 3, 1),
				PartPose.offsetAndRotation(0.13F, -0.94F, 0.0F, 0.0F, 0.0F, -0.7679449F));
		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().mirror().texOffs(8, 21).addBox(-1.0F, -0.5F, -0.5F, 1, 1, 1),
				PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition body6 = body1.addOrReplaceChild("body6",
				CubeListBuilder.create().mirror().texOffs(16, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 16, 1),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		body6.addOrReplaceChild("body7",
				CubeListBuilder.create().mirror().texOffs(8, 19).addBox(-3.0F, -1.0F, -0.5F, 3, 1, 1),
				PartPose.offsetAndRotation(0.91F, 15.19F, 3.12F, 0.0F, 0.0F, 0.62831855F));
		PartDefinition body8 = body6.addOrReplaceChild("body8",
				CubeListBuilder.create().mirror().texOffs(0, 21).addBox(-0.5F, -1.5F, -0.5F, 1, 2, 1),
				PartPose.offset(-1.0F, 15.9F, 3.12F));
		body8.addOrReplaceChild("body9",
				CubeListBuilder.create().mirror().texOffs(4, 21).addBox(-0.6F, -0.5F, -0.5F, 1, 1, 1),
				PartPose.offset(0.8F, -0.2F, 0.0F));

		PartDefinition body10 = body1.addOrReplaceChild("body10",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 15, 1),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		body10.addOrReplaceChild("body11",
				CubeListBuilder.create().mirror().texOffs(0, 19).addBox(-3.0F, -1.0F, -0.5F, 3, 1, 1),
				PartPose.offsetAndRotation(1.16F, 14.06F, 3.12F, 0.0F, 0.0F, 0.33161256F));

		body1.addOrReplaceChild("body12", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 15, 1),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body13 = body1.addOrReplaceChild("body13",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 15, 1),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		body13.addOrReplaceChild("body14", CubeListBuilder.create().texOffs(0, 19).addBox(0.0F, -1.0F, -0.5F, 3, 1, 1),
				PartPose.offsetAndRotation(-1.16F, 14.06F, 3.12F, 0.0F, 0.0F, -0.33161256F));

		PartDefinition body15 = body1.addOrReplaceChild("body15",
				CubeListBuilder.create().texOffs(16, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 16, 1),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		body15.addOrReplaceChild("body16", CubeListBuilder.create().texOffs(8, 19).addBox(0.0F, -1.0F, -0.5F, 3, 1, 1),
				PartPose.offsetAndRotation(-0.91F, 15.19F, 3.12F, 0.0F, 0.0F, -0.62831855F));
		PartDefinition body17 = body15.addOrReplaceChild("body17",
				CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, -1.5F, -0.5F, 1, 2, 1),
				PartPose.offset(1.0F, 15.9F, 3.12F));
		body17.addOrReplaceChild("body18", CubeListBuilder.create().texOffs(4, 21).addBox(-0.4F, -0.5F, -0.5F, 1, 1, 1),
				PartPose.offset(-0.8F, -0.2F, 0.0F));

		PartDefinition body19 = body1.addOrReplaceChild("body19",
				CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, 0.0F, 2.62F, 3, 18, 1),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));
		PartDefinition body20 = body19.addOrReplaceChild("body20",
				CubeListBuilder.create().texOffs(4, 23).addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1),
				PartPose.offset(1.0F, 18.0F, 3.12F));
		PartDefinition body21 = body20.addOrReplaceChild("body21",
				CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -0.5F, -0.5F, 1, 3, 1),
				PartPose.offsetAndRotation(-0.13F, -0.94F, 0.0F, 0.0F, 0.0F, 0.7679449F));
		body21.addOrReplaceChild("body22", CubeListBuilder.create().texOffs(8, 21).addBox(0.0F, -0.5F, -0.5F, 1, 1, 1),
				PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition body23 = body1.addOrReplaceChild("body23",
				CubeListBuilder.create().texOffs(6, 24).addBox(-1.0F, -1.5F, -1.0F, 2, 3, 2),
				PartPose.offset(0.0F, 17.0F, 3.2F));
		PartDefinition body24 = body23.addOrReplaceChild("body24",
				CubeListBuilder.create().texOffs(11, 27).addBox(-0.5F, -1.0F, 0.0F, 1, 2, 3),
				PartPose.offset(0.7F, -0.3F, 0.43F));
		body24.addOrReplaceChild("body25", CubeListBuilder.create().texOffs(1, 26).addBox(-0.5F, -0.5F, 0.0F, 1, 1, 3),
				PartPose.offsetAndRotation(0.0F, 1.13F, -0.04F, 0.20943952F, 0.0F, 0.0F));
		PartDefinition body26 = body23.addOrReplaceChild("body26",
				CubeListBuilder.create().texOffs(11, 27).addBox(-0.5F, -1.0F, 0.0F, 1, 2, 3),
				PartPose.offset(-0.7F, -0.3F, 0.43F));
		body26.addOrReplaceChild("body27", CubeListBuilder.create().texOffs(1, 26).addBox(-0.5F, -0.5F, 0.0F, 1, 1, 3),
				PartPose.offsetAndRotation(0.0F, 1.13F, -0.04F, 0.20943952F, 0.0F, 0.0F));

		body1.addOrReplaceChild("body28", CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1),
				PartPose.offsetAndRotation(-0.8F, 19.76F, 3.1F, 0.29670596F, -0.2617994F, -0.20943952F));
		body1.addOrReplaceChild("body29",
				CubeListBuilder.create().mirror().texOffs(0, 30).addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1),
				PartPose.offsetAndRotation(0.8F, 19.76F, 3.1F, 0.29670596F, 0.2617994F, 0.20943952F));

		PartDefinition upperRing1 = root.addOrReplaceChild("upperRing1",
				CubeListBuilder.create().texOffs(19, 27).addBox(-1.5F, 0.0F, 2.62F, 3, 4, 1),
				PartPose.offset(0.0F, 15.7F, 0.0F));
		upperRing1.addOrReplaceChild("upperRing2",
				CubeListBuilder.create().texOffs(19, 27).addBox(-1.5F, 0.0F, 2.62F, 3, 4, 1),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		upperRing1.addOrReplaceChild("upperRing3",
				CubeListBuilder.create().texOffs(19, 27).addBox(-1.5F, 0.0F, 2.62F, 3, 4, 1),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		upperRing1.addOrReplaceChild("upperRing4",
				CubeListBuilder.create().texOffs(19, 27).addBox(-1.5F, 0.0F, 2.62F, 3, 4, 1),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		upperRing1.addOrReplaceChild("upperRing5",
				CubeListBuilder.create().texOffs(19, 27).addBox(-1.5F, 0.0F, 2.62F, 3, 4, 1),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		upperRing1.addOrReplaceChild("upperRing6",
				CubeListBuilder.create().texOffs(19, 27).addBox(-1.5F, 0.0F, 2.62F, 3, 4, 1),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		upperRing1.addOrReplaceChild("upperRing7",
				CubeListBuilder.create().texOffs(19, 27).addBox(-1.5F, 0.0F, 2.62F, 3, 4, 1),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		upperRing1.addOrReplaceChild("upperRing8",
				CubeListBuilder.create().texOffs(19, 27).addBox(-1.5F, 0.0F, 2.62F, 3, 4, 1),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition frontRing1 = root.addOrReplaceChild("frontRing1",
				CubeListBuilder.create().texOffs(27, 29).addBox(-1.5F, -2.0F, 2.62F, 3, 2, 1),
				PartPose.offsetAndRotation(0.0F, 11.0F, -3.4F, 1.5707964F, 0.0F, 0.0F));
		frontRing1.addOrReplaceChild("frontRing2",
				CubeListBuilder.create().texOffs(27, 29).addBox(-1.5F, -2.0F, 2.62F, 3, 2, 1),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		frontRing1.addOrReplaceChild("frontRing3",
				CubeListBuilder.create().texOffs(27, 29).addBox(-1.5F, -2.0F, 2.62F, 3, 2, 1),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		frontRing1.addOrReplaceChild("frontRing4",
				CubeListBuilder.create().texOffs(27, 29).addBox(-1.5F, -2.0F, 2.62F, 3, 2, 1),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		frontRing1.addOrReplaceChild("frontRing5",
				CubeListBuilder.create().texOffs(27, 29).addBox(-1.5F, -2.0F, 2.62F, 3, 2, 1),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		frontRing1.addOrReplaceChild("frontRing6",
				CubeListBuilder.create().texOffs(27, 29).addBox(-1.5F, -2.0F, 2.62F, 3, 2, 1),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		frontRing1.addOrReplaceChild("frontRing7",
				CubeListBuilder.create().texOffs(27, 29).addBox(-1.5F, -2.0F, 2.62F, 3, 2, 1),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		frontRing1.addOrReplaceChild("frontRing8",
				CubeListBuilder.create().texOffs(27, 29).addBox(-1.5F, -2.0F, 2.62F, 3, 2, 1),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition frontButton1 = root.addOrReplaceChild("frontButton1",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.offsetAndRotation(0.0F, 11.0F, -3.2F, 1.5707964F, 0.0F, 0.0F));
		frontButton1.addOrReplaceChild("frontButton2",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		frontButton1.addOrReplaceChild("frontButton3",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		frontButton1.addOrReplaceChild("frontButton4",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		frontButton1.addOrReplaceChild("frontButton5",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		frontButton1.addOrReplaceChild("frontButton6",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		frontButton1.addOrReplaceChild("frontButton7",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		frontButton1.addOrReplaceChild("frontButton8",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition backButton1 = root.addOrReplaceChild("backButton1",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.offsetAndRotation(0.0F, 13.2F, 3.6F, -1.5707964F, 0.0F, 0.0F));
		backButton1.addOrReplaceChild("backButton2",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		backButton1.addOrReplaceChild("backButton3",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		backButton1.addOrReplaceChild("backButton4",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		backButton1.addOrReplaceChild("backButton5",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		backButton1.addOrReplaceChild("backButton6",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		backButton1.addOrReplaceChild("backButton7",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		backButton1.addOrReplaceChild("backButton8",
				CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -4.0F, -0.38F, 3, 4, 4),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}