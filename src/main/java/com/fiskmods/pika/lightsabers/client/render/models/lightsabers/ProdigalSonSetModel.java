package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ProdigalSonSetModel extends HiltSetModel {

	private static final float[] OCTAGON_ANGLES = { 0.0F, 0.7853982F, 1.5707964F, 2.3561945F, 3.1415927F, -2.3561945F,
			-1.5707964F, -0.7853982F };

	private static final float[] GRENADE_RIB_ANGLES = { 0.0F, 1.0471976F, 2.0943952F, 3.1415927F, -2.0943952F,
			-1.0471976F };

	private final ModelPart pommel;
	private final ModelPart body;
	private final ModelPart switchSection;
	private final ModelPart emitter;

	public ProdigalSonSetModel() {
		super("prodigal_son");

		this.pommel = createPommel().bakeRoot();
		ModelPart pommelTop = this.pommel.getChild("top_group");
		pommelTop.xScale = 0.8F;
		pommelTop.yScale = 1.0F;
		pommelTop.zScale = 0.8F;

		this.body = createBody().bakeRoot();
		ModelPart bodyRing = this.body.getChild("ring_group");
		bodyRing.xScale = 1.1F;
		bodyRing.yScale = 1.0F;
		bodyRing.zScale = 1.1F;

		ModelPart bodyGrip = this.body.getChild("grip_group");
		bodyGrip.xScale = 1.05F;
		bodyGrip.yScale = 1.05F;
		bodyGrip.zScale = 1.05F;

		this.switchSection = createSwitch().bakeRoot();
		ModelPart switchMain = this.switchSection.getChild("switch_main");
		switchMain.xScale = 1.1F;
		switchMain.yScale = 1.1F;
		switchMain.zScale = 1.1F;

		this.emitter = createEmitter().bakeRoot();

		ModelPart emitterRingBase = this.emitter.getChild("ring_base");
		emitterRingBase.xScale = 1.0F;
		emitterRingBase.yScale = 0.8F;
		emitterRingBase.zScale = 1.0F;

		ModelPart emitterLowerBody = this.emitter.getChild("lower_body");
		emitterLowerBody.xScale = 0.85F;
		emitterLowerBody.yScale = 1.0F;
		emitterLowerBody.zScale = 0.85F;

		ModelPart emitterUpperBody = this.emitter.getChild("upper_body");
		emitterUpperBody.xScale = 0.45F;
		emitterUpperBody.yScale = 0.6F;
		emitterUpperBody.zScale = 0.45F;

		ModelPart emitterMiddleRingLower = this.emitter.getChild("middle_ring_lower");
		emitterMiddleRingLower.xScale = 0.95F;
		emitterMiddleRingLower.yScale = 0.6F;
		emitterMiddleRingLower.zScale = 0.95F;

		ModelPart emitterMiddleRingUpper = this.emitter.getChild("middle_ring_upper");
		emitterMiddleRingUpper.xScale = 0.85F;
		emitterMiddleRingUpper.yScale = 0.85F;
		emitterMiddleRingUpper.zScale = 0.85F;

		ModelPart emitterUpper1 = this.emitter.getChild("upper_emitter_1");
		emitterUpper1.xScale = 0.7F;
		emitterUpper1.yScale = 0.7F;
		emitterUpper1.zScale = 0.7F;

		ModelPart emitterUpper3 = this.emitter.getChild("upper_emitter_3");
		emitterUpper3.xScale = 1.1F;
		emitterUpper3.yScale = 0.7F;
		emitterUpper3.zScale = 1.1F;

		ModelPart emitterUpper4 = this.emitter.getChild("upper_emitter_4");
		emitterUpper4.xScale = 1.25F;
		emitterUpper4.yScale = 0.5F;
		emitterUpper4.zScale = 1.25F;

		ModelPart emitterUpper5 = this.emitter.getChild("upper_emitter_5");
		emitterUpper5.xScale = 0.6F;
		emitterUpper5.yScale = 1.0F;
		emitterUpper5.zScale = 0.6F;
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
		case POMMEL -> 2.0F;
		case BODY -> 12.0F;
		case SWITCH_SECTION -> 8.8F;
		case EMITTER -> 31.0F;
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

		PartDefinition bodyGroup = root.addOrReplaceChild("body_group", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(bodyGroup, "body", 0, 0, -1.5F, 0.0F, -0.38F, 3.0F, 2.0F, 4.0F);

		PartDefinition topGroup = root.addOrReplaceChild("top_group", CubeListBuilder.create(),
				PartPose.offset(0.0F, 2.0F, 0.0F));
		addOctagonalShell(topGroup, "top", 10, 0, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createBody() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition bodyGroup = root.addOrReplaceChild("body_group", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(bodyGroup, "body", 8, 0, -1.5F, 0.0F, 2.62F, 3.0F, 6.0F, 1.0F);

		PartDefinition ringGroup = root.addOrReplaceChild("ring_group", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(ringGroup, "ring_inner", 0, 11, -1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F);

		for (int i = 0; i < GRENADE_RIB_ANGLES.length; i++) {
			PartDefinition rib = ringGroup.addOrReplaceChild("rib_" + i,
					CubeListBuilder.create().texOffs(0, 7).addBox(-1.5F, -1.5F, 3.43F, 3.0F, 3.0F, 1.0F),
					PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, GRENADE_RIB_ANGLES[i], 0.0F));

			rib.addOrReplaceChild("rib_top_bevel_" + i,
					CubeListBuilder.create().texOffs(8, 8).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 2.0F, 1.0F),
					PartPose.offsetAndRotation(0.0F, -1.5F, 4.43F, 0.83775806F, 0.0F, 0.0F));

			rib.addOrReplaceChild("rib_bot_bevel_" + i,
					CubeListBuilder.create().texOffs(8, 8).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 2.0F, 1.0F),
					PartPose.offsetAndRotation(0.0F, 1.5F, 4.43F, -0.83775806F, 0.0F, 0.0F));
		}

		PartDefinition gripGroup = root.addOrReplaceChild("grip_group", CubeListBuilder.create(),
				PartPose.offset(0.0F, 5.7F, 0.0F));
		addOctagonalShell(gripGroup, "grip", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 6.0F, 1.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createSwitch() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition switchMain = root.addOrReplaceChild("switch_main", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(switchMain, "body", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F);

		PartDefinition box7 = switchMain.getChild("body_6");
		PartDefinition controlBox = box7.addOrReplaceChild("control_box",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -4.0F, 0.0F, 3.0F, 8.0F, 2.0F),
				PartPose.offset(0.0F, 4.0F, 3.62F));

		controlBox.addOrReplaceChild("switch_lever",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F),
				PartPose.offset(-1.5F, 1.75F, 0.75F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createEmitter() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition lowerBody = root.addOrReplaceChild("lower_body", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(lowerBody, "lower_body", 0, 2, -1.5F, 0.0F, 1.62F, 3.0F, 17.0F, 2.0F);

		PartDefinition ringBase = root.addOrReplaceChild("ring_base", CubeListBuilder.create(), PartPose.ZERO);
		float[] ringYOffsets = { 0.0F, 2.0F, 4.0F, 6.0F, 8.0F, 10.0F, 12.0F, 14.0F, 16.0F, 18.0F };
		for (int r = 0; r < ringYOffsets.length; r++) {
			int u = (r == 0) ? 10 : 0;
			int v = (r == 0) ? 22 : 0;
			float h = (r == 0) ? 2.0F : 1.0F;

			for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
				ringBase.addOrReplaceChild("ring_" + (r + 1) + "_" + i,
						CubeListBuilder.create().texOffs(u, v).addBox(-1.5F, 0.0F, 2.62F, 3.0F, h, 1.0F),
						PartPose.offsetAndRotation(0.0F, ringYOffsets[r], 0.0F, 0.0F, OCTAGON_ANGLES[i], 0.0F));
			}
		}

		PartDefinition upperBody = root.addOrReplaceChild("upper_body", CubeListBuilder.create(),
				PartPose.offset(0.0F, 17.0F, 0.0F));
		addOctagonalShell(upperBody, "upper_body", 10, 0, -1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F);

		PartDefinition middleRingLower = root.addOrReplaceChild("middle_ring_lower", CubeListBuilder.create(),
				PartPose.offset(0.0F, 21.3F, 0.0F));
		addOctagonalShell(middleRingLower, "middle_ring_lower", 18, 0, -1.5F, -1.0F, 0.62F, 3.0F, 1.0F, 3.0F);

		PartDefinition middleRingUpper = root.addOrReplaceChild("middle_ring_upper", CubeListBuilder.create(),
				PartPose.offset(0.0F, 21.3F, 0.0F));
		addOctagonalShell(middleRingUpper, "middle_ring_upper", 18, 4, -1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F);

		PartDefinition upperEmitter1 = root.addOrReplaceChild("upper_emitter_1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 27.0F, 0.0F));
		addOctagonalShell(upperEmitter1, "upper_emitter_1", 18, 8, -1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F);

		PartDefinition upperEmitter2 = root.addOrReplaceChild("upper_emitter_2", CubeListBuilder.create(),
				PartPose.offset(0.0F, 27.0F, 0.0F));
		addOctagonalShell(upperEmitter2, "upper_emitter_2", 0, 21, -1.5F, 0.0F, 1.62F, 3.0F, 3.0F, 2.0F);

		PartDefinition upperEmitter3 = root.addOrReplaceChild("upper_emitter_3", CubeListBuilder.create(),
				PartPose.offset(0.0F, 29.5F, 0.0F));
		addOctagonalShell(upperEmitter3, "upper_emitter_3", 10, 17, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition upperEmitter4 = root.addOrReplaceChild("upper_emitter_4", CubeListBuilder.create(),
				PartPose.offset(0.0F, 30.5F, 0.0F));
		addOctagonalShell(upperEmitter4, "upper_emitter_4", 10, 19, -1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F);

		PartDefinition upperEmitter5 = root.addOrReplaceChild("upper_emitter_5", CubeListBuilder.create(),
				PartPose.offset(0.0F, 30.0F, 0.0F));
		addOctagonalShell(upperEmitter5, "upper_emitter_5", 10, 25, -1.5F, 0.0F, 2.62F, 3.0F, 2.0F, 1.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}
}