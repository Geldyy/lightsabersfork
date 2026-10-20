package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class RebelSetModel extends HiltSetModel {
	private final ModelPart pommel;
	private final ModelPart body;
	private final ModelPart switchSection;
	private final ModelPart emitter;

	public RebelSetModel() {
		super("rebel");
		this.pommel = createPommelLayer().bakeRoot();
		this.body = createBodyLayer().bakeRoot();
		this.switchSection = createSwitchSectionLayer().bakeRoot();
		this.emitter = createEmitterLayer().bakeRoot();

		ModelPart pommelMain = this.pommel.getChild("main");
		pommelMain.getChild("bottom1").xScale = 0.4F;
		pommelMain.getChild("bottom1").yScale = 0.7F;
		pommelMain.getChild("bottom1").zScale = 0.7F;

		pommelMain.getChild("body1").xScale = 1.2F;
		pommelMain.getChild("body1").yScale = 0.8F;
		pommelMain.getChild("body1").zScale = 1.2F;

		pommelMain.getChild("innerRing1").xScale = 0.91F;
		pommelMain.getChild("innerRing1").yScale = 1.0F;
		pommelMain.getChild("innerRing1").zScale = 0.91F;

		pommelMain.getChild("extrusion1").xScale = 1.0F;
		pommelMain.getChild("extrusion1").yScale = 0.5F;
		pommelMain.getChild("extrusion1").zScale = 1.0F;

		pommelMain.getChild("body1_1").xScale = 1.0F;
		pommelMain.getChild("body1_1").yScale = 1.05F;
		pommelMain.getChild("body1_1").zScale = 1.0F;

		ModelPart bodyMain = this.body.getChild("main");
		bodyMain.getChild("body9").xScale = 0.9F;
		bodyMain.getChild("body9").yScale = 1.34F;
		bodyMain.getChild("body9").zScale = 1.0F;

		ModelPart switchMain = this.switchSection.getChild("main");
		switchMain.getChild("innerBody1").xScale = 0.8F;
		switchMain.getChild("innerBody1").yScale = 0.6F;
		switchMain.getChild("innerBody1").zScale = 0.8F;

		switchMain.getChild("lowerSideButton1").xScale = 0.2F;
		switchMain.getChild("lowerSideButton1").yScale = 0.2F;
		switchMain.getChild("lowerSideButton1").zScale = 0.2F;

		ModelPart emitterMain = this.emitter.getChild("main");
		emitterMain.getChild("body1").xScale = 1.2F;
		emitterMain.getChild("body1").yScale = 1.0F;
		emitterMain.getChild("body1").zScale = 1.2F;

		emitterMain.getChild("shell1").xScale = 1.2F;
		emitterMain.getChild("shell1").yScale = 1.2F;
		emitterMain.getChild("shell1").zScale = 1.2F;

		emitterMain.getChild("innerRing1").xScale = 0.9F;
		emitterMain.getChild("innerRing1").yScale = 0.9F;
		emitterMain.getChild("innerRing1").zScale = 0.9F;

		emitterMain.getChild("ring3_1").xScale = 2.3F;
		emitterMain.getChild("ring3_1").yScale = 1.0F;
		emitterMain.getChild("ring3_1").zScale = 2.3F;
	}

	@Override
	public ModelPart getPartModel(PartType type) {
		return getPartModel(type, null);
	}

	@Override
	public ModelPart getPartModel(PartType type, LightsaberData data) {
		return switch (type) {
		case POMMEL -> this.pommel;
		case BODY -> this.body;
		case SWITCH_SECTION -> this.switchSection;
		case EMITTER -> this.emitter;
		};
	}

	@Override
	public float getHeight(PartType type) {
		return switch (type) {
		case POMMEL -> 6.0F;
		case BODY -> 20.0F;
		case SWITCH_SECTION -> 4.0F;
		case EMITTER -> 11.0F;
		};
	}

	public static LayerDefinition createPommelLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, (float) Math.PI, 0.0F, 0.0F));

		PartDefinition bottom1 = main.addOrReplaceChild("bottom1", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 5.8F, 0.0F, 0.0F, 0.0F, 1.0821041F));

		PartDefinition bottom2 = bottom1.addOrReplaceChild("bottom2",
				CubeListBuilder.create().mirror().texOffs(7, 19).addBox(-0.5F, -0.5F, -4.5F, 1.0F, 1.0F, 9.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4712389F));

		bottom2.addOrReplaceChild("bottom9",
				CubeListBuilder.create().mirror().texOffs(17, 1).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F),
				PartPose.offset(6.35F, 0.0F, 0.0F));

		PartDefinition bottom3 = bottom2.addOrReplaceChild("bottom3",
				CubeListBuilder.create().mirror().texOffs(10, 2).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
				PartPose.offset(0.5F, 0.0F, -4.0F));

		PartDefinition bottom4 = bottom3.addOrReplaceChild("bottom4",
				CubeListBuilder.create().mirror().texOffs(0, 8).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(3.68F, 0.0F, -0.12F, 0.0F, -0.6981317F, 0.0F));

		bottom4.addOrReplaceChild("bottom5",
				CubeListBuilder.create().mirror().texOffs(0, 10).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.78F, 0.0F, -0.05F, 0.0F, -0.4537856F, 0.0F));

		PartDefinition bottom6 = bottom2.addOrReplaceChild("bottom6",
				CubeListBuilder.create().mirror().texOffs(10, 2).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
				PartPose.offset(0.5F, 0.0F, 4.0F));

		PartDefinition bottom7 = bottom6.addOrReplaceChild("bottom7",
				CubeListBuilder.create().mirror().texOffs(0, 8).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(3.68F, 0.0F, 0.12F, 0.0F, 0.6981317F, 0.0F));

		bottom7.addOrReplaceChild("bottom8",
				CubeListBuilder.create().mirror().texOffs(0, 10).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.78F, 0.0F, 0.05F, 0.0F, 0.4537856F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.ZERO);

		addOctagonBox(body1, "body", 0, 10, -1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F);

		PartDefinition innerRing1 = main.addOrReplaceChild("innerRing1", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 1.86F, 0.0F, (float) Math.PI, 0.0F, 0.0F));

		addAngledOctagonRing(innerRing1, "innerRing", 16, 14, -1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F, 0.29496065F);

		PartDefinition extrusion1 = main.addOrReplaceChild("extrusion1", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 0.0F, -0.34906584F, 0.0F));

		addExtrusion(extrusion1, "extrusion2", "extrusion3", "extrusion4", 0.31415927F);
		addExtrusion(extrusion1, "extrusion8", "extrusion9", "extrusion10", 1.5707964F);
		addExtrusion(extrusion1, "extrusion14", "extrusion15", "extrusion16", 2.8274333F);
		addExtrusion(extrusion1, "extrusion20", "extrusion21", "extrusion22", -2.1991148F);
		addExtrusion(extrusion1, "extrusion26", "extrusion27", "extrusion28", 2.8274333F);
		addExtrusion(extrusion1, "extrusion29", "extrusion30", "extrusion31", -0.9424778F);

		PartDefinition body1_1 = main.addOrReplaceChild("body1_1",
				CubeListBuilder.create().texOffs(41, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
				PartPose.offset(0.0F, 5.0F, 0.0F));

		addOctagonBox(body1_1, "body", "_1", 41, 0, -1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static void addExtrusion(PartDefinition parent, String extName, String topName, String bottomName,
			float rotY) {
		PartDefinition ext = parent.addOrReplaceChild(extName,
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.5F, 4.0F, 1.0F, 3.0F, 1.0F),
				PartPose.rotation(0.0F, rotY, 0.0F));

		ext.addOrReplaceChild(topName,
				CubeListBuilder.create().texOffs(0, 7).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -1.5F, 5.0F, 0.61086524F, 0.0F, 0.0F));

		ext.addOrReplaceChild(bottomName,
				CubeListBuilder.create().texOffs(0, 4).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 1.5F, 5.0F, -0.4886922F, 0.0F, 0.0F));
	}

	public static LayerDefinition createSwitchSectionLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.rotation((float) Math.PI, 0.0F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));

		body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 4.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));

		body1.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 4.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));

		body1.addOrReplaceChild("body4",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 4.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));

		body1.addOrReplaceChild("body5",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 4.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 4.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));

		body1.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 4.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));

		body1.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 4.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition innerBody1 = main.addOrReplaceChild("innerBody1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -2.2F, 0.0F, 0.0F, -1.5707964F, 0.0F));

		innerBody1.addOrReplaceChild("body2_1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));

		innerBody1.addOrReplaceChild("body3_1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));

		innerBody1.addOrReplaceChild("body4_1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));

		innerBody1.addOrReplaceChild("body5_1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		innerBody1.addOrReplaceChild("body6_1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));

		innerBody1.addOrReplaceChild("body7_1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));

		innerBody1.addOrReplaceChild("body9_1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition lowerSideButton1 = main.addOrReplaceChild("lowerSideButton1",
				CubeListBuilder.create().texOffs(10, 9).addBox(-1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F),
				PartPose.offsetAndRotation(-3.62F, -2.1F, 0.0F, 1.5707964F, 1.5707964F, 0.0F));

		addOctagonBox(lowerSideButton1, "lowerSideButton", 10, 9, -1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, (float) Math.PI, 0.0F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F), PartPose.ZERO);

		body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));

		body1.addOrReplaceChild("body5",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));

		body1.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(31, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));

		body1.addOrReplaceChild("body11",
				CubeListBuilder.create().texOffs(13, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		body1.addOrReplaceChild("body14",
				CubeListBuilder.create().texOffs(22, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));

		body1.addOrReplaceChild("body17",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));

		body1.addOrReplaceChild("body20",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		main.addOrReplaceChild("switchPart",
				CubeListBuilder.create().texOffs(0, 23).addBox(0.0F, 0.0F, 0.0F, 2.0F, 6.0F, 1.0F),
				PartPose.offset(-1.0F, 3.3F, -4.5F));

		PartDefinition body9 = main.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -0.7F, 0.0F, 0.0F, -1.2217305F, 0.0F));

		PartDefinition body10 = body9.addOrReplaceChild("body10",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.22F, 1.0F, 15.0F, 1.0F),
				PartPose.rotation(0.0F, 0.62831855F, 0.0F));

		PartDefinition body10_1 = body10.addOrReplaceChild("body10_1",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 2.82F, 1.0F, 15.0F, 1.0F),
				PartPose.rotation(0.0F, 0.62831855F, 0.0F));

		PartDefinition body10_2 = body10_1.addOrReplaceChild("body10_2",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.22F, 1.0F, 15.0F, 1.0F),
				PartPose.rotation(0.0F, 0.62831855F, 0.0F));

		body10_2.addOrReplaceChild("body10_3",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
				PartPose.rotation(0.0F, 0.62831855F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	public static LayerDefinition createEmitterLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.rotation((float) Math.PI, 0.0F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(32, 0).addBox(-1.5F, -20.0F, 2.62F, 3.0F, 11.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().mirror().texOffs(24, 0).addBox(-1.5F, -18.0F, 2.62F, 3.0F, 9.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));

		PartDefinition body3 = body2.addOrReplaceChild("body3",
				CubeListBuilder.create().mirror().texOffs(4, 23).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(-1.0F, -18.0F, 3.12F));

		PartDefinition body4 = body3.addOrReplaceChild("body4",
				CubeListBuilder.create().mirror().texOffs(0, 24).addBox(0.0F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F),
				PartPose.offsetAndRotation(0.13F, -0.94F, 0.0F, 0.0F, 0.0F, -0.7679449F));

		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().mirror().texOffs(8, 21).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition body6 = body1.addOrReplaceChild("body6",
				CubeListBuilder.create().mirror().texOffs(16, 0).addBox(-1.5F, -16.0F, 2.62F, 3.0F, 7.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));

		PartDefinition body7 = body6.addOrReplaceChild("body7",
				CubeListBuilder.create().mirror().texOffs(8, 19).addBox(-3.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.91F, -15.19F, 3.12F, 0.0F, 0.0F, 0.62831855F));

		PartDefinition body8 = body6.addOrReplaceChild("body8",
				CubeListBuilder.create().mirror().texOffs(0, 21).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(-1.0F, -15.9F, 3.12F));

		body8.addOrReplaceChild("body9",
				CubeListBuilder.create().mirror().texOffs(4, 21).addBox(-0.6F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(0.8F, -0.2F, 0.0F));

		body1.addOrReplaceChild("body10",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(-1.5F, -16.0F, 2.62F, 3.0F, 7.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));

		body1.addOrReplaceChild("body12",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, -16.0F, 2.62F, 3.0F, 7.0F, 2.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		body1.addOrReplaceChild("body13",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, -16.0F, 2.62F, 3.0F, 7.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));

		PartDefinition body15 = body1.addOrReplaceChild("body15",
				CubeListBuilder.create().texOffs(16, 0).addBox(-1.5F, -16.0F, 2.62F, 3.0F, 7.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));

		body15.addOrReplaceChild("body16",
				CubeListBuilder.create().texOffs(8, 19).addBox(0.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-0.91F, -15.19F, 3.12F, 0.0F, 0.0F, -0.62831855F));

		PartDefinition body17 = body15.addOrReplaceChild("body17",
				CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(1.0F, -15.9F, 3.12F));

		body17.addOrReplaceChild("body18",
				CubeListBuilder.create().texOffs(4, 21).addBox(-0.4F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(-0.8F, -0.2F, 0.0F));

		PartDefinition body19 = body1.addOrReplaceChild("body19",
				CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, -18.0F, 2.62F, 3.0F, 9.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition body20 = body19.addOrReplaceChild("body20",
				CubeListBuilder.create().texOffs(4, 23).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(1.0F, -18.0F, 3.12F));

		PartDefinition body21 = body20.addOrReplaceChild("body21",
				CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F),
				PartPose.offsetAndRotation(-0.13F, -0.94F, 0.0F, 0.0F, 0.0F, 0.7679449F));

		body21.addOrReplaceChild("body22",
				CubeListBuilder.create().texOffs(8, 21).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition shell1 = main.addOrReplaceChild("shell1", CubeListBuilder.create(),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition shell2 = shell1.addOrReplaceChild("shell2",
				CubeListBuilder.create().texOffs(8, 15).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));

		PartDefinition shell3 = shell2.addOrReplaceChild("shell3",
				CubeListBuilder.create().mirror().texOffs(24, 0).addBox(-1.0F, -9.0F, -0.5F, 2.0F, 9.0F, 1.0F),
				PartPose.offset(0.5F, -3.0F, 3.12F));

		PartDefinition shell4 = shell3.addOrReplaceChild("shell4",
				CubeListBuilder.create().mirror().texOffs(15, 18).addBox(0.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, -9.0F, 0.0F, 0.0F, 0.0F, -0.9075712F));

		shell4.addOrReplaceChild("shell5",
				CubeListBuilder.create().mirror().texOffs(21, 18).addBox(0.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(1.3F, 0.0F, 0.0F));

		PartDefinition shell6 = shell3.addOrReplaceChild("shell6",
				CubeListBuilder.create().mirror().texOffs(30, 0).addBox(0.0F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, -9.0F, 0.0F, 0.0F, 0.0F, 0.17453292F));

		shell6.addOrReplaceChild("shell7",
				CubeListBuilder.create().mirror().texOffs(30, 5).addBox(0.0F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.09599311F));

		shell3.addOrReplaceChild("shell8",
				CubeListBuilder.create().mirror().texOffs(26, 10).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(0.5F, -9.0F, 0.0F));

		shell1.addOrReplaceChild("shell9",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, -14.0F, 2.62F, 3.0F, 14.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));

		PartDefinition shell10 = shell1.addOrReplaceChild("shell10",
				CubeListBuilder.create().texOffs(8, 15).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));

		PartDefinition shell11 = shell10.addOrReplaceChild("shell11",
				CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -9.0F, -0.5F, 2.0F, 9.0F, 1.0F),
				PartPose.offset(-0.5F, -3.0F, 3.12F));

		PartDefinition shell12 = shell11.addOrReplaceChild("shell12",
				CubeListBuilder.create().texOffs(15, 18).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, -9.0F, 0.0F, 0.0F, 0.0F, 0.9075712F));

		shell12.addOrReplaceChild("shell13",
				CubeListBuilder.create().texOffs(21, 18).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(-1.3F, 0.0F, 0.0F));

		PartDefinition shell14 = shell11.addOrReplaceChild("shell14",
				CubeListBuilder.create().texOffs(30, 0).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, -9.0F, 0.0F, 0.0F, 0.0F, -0.17453292F));

		shell14.addOrReplaceChild("shell15",
				CubeListBuilder.create().texOffs(30, 5).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.1308997F));

		shell11.addOrReplaceChild("shell16",
				CubeListBuilder.create().texOffs(26, 10).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(-0.5F, -9.0F, 0.0F));

		PartDefinition innerRing1 = main.addOrReplaceChild("innerRing1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -11.0F, 2.62F, 3.0F, 11.0F, 1.0F),
				PartPose.offset(0.0F, -3.0F, 0.0F));

		addOctagonBox(innerRing1, "innerRing", 0, 0, -1.5F, -11.0F, 2.62F, 3.0F, 11.0F, 1.0F);

		PartDefinition ring3_1 = main.addOrReplaceChild("ring3_1",
				CubeListBuilder.create().texOffs(0, 18).addBox(-1.5F, -1.0F, 0.62F, 3.0F, 1.0F, 3.0F), PartPose.ZERO);

		addOctagonBox(ring3_1, "ring3_", 0, 18, -1.5F, -1.0F, 0.62F, 3.0F, 1.0F, 3.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static void addOctagonBox(PartDefinition parent, String prefix, int texU, int texV, float x, float y,
			float z, float w, float h, float d) {
		addOctagonBox(parent, prefix, "", texU, texV, x, y, z, w, h, d);
	}

	private static void addOctagonBox(PartDefinition parent, String prefix, String suffix, int texU, int texV, float x,
			float y, float z, float w, float h, float d) {
		parent.addOrReplaceChild(prefix + "2" + suffix,
				CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));

		parent.addOrReplaceChild(prefix + "3" + suffix,
				CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));

		parent.addOrReplaceChild(prefix + "4" + suffix,
				CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));

		parent.addOrReplaceChild(prefix + "5" + suffix,
				CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		parent.addOrReplaceChild(prefix + "6" + suffix,
				CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));

		parent.addOrReplaceChild(prefix + "7" + suffix,
				CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));

		parent.addOrReplaceChild(prefix + (prefix.equals("body") && suffix.isEmpty() ? "9" : "8") + suffix,
				CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));
	}

	private static void addAngledOctagonRing(PartDefinition parent, String prefix, int texU, int texV, float x, float y,
			float z, float w, float h, float d, float pitch) {
		parent.addOrReplaceChild(prefix + "2", CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(pitch, 0.0F, 0.0F));

		parent.addOrReplaceChild(prefix + "3", CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(pitch, 0.7853982F, 0.0F));

		parent.addOrReplaceChild(prefix + "4", CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(pitch, 1.5707964F, 0.0F));

		parent.addOrReplaceChild(prefix + "5", CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(pitch, 2.3561945F, 0.0F));

		parent.addOrReplaceChild(prefix + "6", CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(pitch, 3.1415927F, 0.0F));

		parent.addOrReplaceChild(prefix + "7", CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(pitch, -2.3561945F, 0.0F));

		parent.addOrReplaceChild(prefix + "8", CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(pitch, -1.5707964F, 0.0F));

		parent.addOrReplaceChild(prefix + "9", CubeListBuilder.create().texOffs(texU, texV).addBox(x, y, z, w, h, d),
				PartPose.rotation(pitch, -0.7853982F, 0.0F));
	}
}