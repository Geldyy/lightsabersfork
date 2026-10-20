package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class RedeemerSetModel extends HiltSetModel {

	private static final float[] OCTAGON_ANGLES = { 0.0F, 0.7853982F, 1.5707964F, 2.3561945F, 3.1415927F, -2.3561945F,
			-1.5707964F, -0.7853982F };

	private static final float[] BOOSTER_RIB_ANGLES = { 0.0F, 1.0471976F, 2.0943952F, 3.1415927F, -2.0943952F,
			-1.0471976F };

	private static final float[] GRIP_10_ANGLES = { 0.0F, 0.62831855F, 1.2566371F, 1.8849556F, 2.5132742F, 3.1415927F,
			-2.5132742F, -1.8849556F, -1.2566371F, -0.62831855F };

	private final ModelPart pommel;
	private final ModelPart body;
	private final ModelPart switchSection;
	private final ModelPart emitter;

	public RedeemerSetModel() {
		super("redeemer");

		this.pommel = createPommel().bakeRoot();
		ModelPart pommelTop = this.pommel.getChild("top_ring");
		pommelTop.xScale = 0.8F;
		pommelTop.yScale = 1.0F;
		pommelTop.zScale = 0.8F;

		this.body = createBody().bakeRoot();
		ModelPart bodyGrip = this.body.getChild("grip_group");
		bodyGrip.xScale = 0.9F;
		bodyGrip.yScale = 0.9F;
		bodyGrip.zScale = 0.9F;

		for (int i = 0; i < BOOSTER_RIB_ANGLES.length; i++) {
			ModelPart rib = this.body.getChild("booster_rib_" + i);
			rib.xScale = 1.0F;
			rib.yScale = 0.8F;
			rib.zScale = 1.0F;
		}

		this.switchSection = createSwitch().bakeRoot();
		ModelPart lowerBtn = this.switchSection.getChild("lower_button");
		lowerBtn.xScale = 0.2F;
		lowerBtn.yScale = 0.2F;
		lowerBtn.zScale = 0.2F;

		ModelPart upperBtn = this.switchSection.getChild("upper_button");
		upperBtn.xScale = 0.15F;
		upperBtn.yScale = 0.15F;
		upperBtn.zScale = 0.15F;

		this.emitter = createEmitter().bakeRoot();
		ModelPart emitterUpper1 = this.emitter.getChild("upper_emitter_1");
		emitterUpper1.xScale = 0.7F;
		emitterUpper1.yScale = 0.7F;
		emitterUpper1.zScale = 0.7F;

		ModelPart emitterMiddleUpper = this.emitter.getChild("middle_ring_upper");
		emitterMiddleUpper.xScale = 0.85F;
		emitterMiddleUpper.yScale = 0.85F;
		emitterMiddleUpper.zScale = 0.85F;

		ModelPart emitterRingBase = this.emitter.getChild("ring_base");
		emitterRingBase.xScale = 1.0F;
		emitterRingBase.yScale = 0.8F;
		emitterRingBase.zScale = 1.0F;

		ModelPart emitterLowerBody = this.emitter.getChild("lower_body");
		emitterLowerBody.xScale = 0.85F;
		emitterLowerBody.yScale = 1.0F;
		emitterLowerBody.zScale = 0.85F;

		ModelPart emitterMiddleLower = this.emitter.getChild("middle_ring_lower");
		emitterMiddleLower.xScale = 0.95F;
		emitterMiddleLower.yScale = 0.6F;
		emitterMiddleLower.zScale = 0.95F;

		ModelPart emitterUpper3 = this.emitter.getChild("upper_emitter_3");
		emitterUpper3.xScale = 1.1F;
		emitterUpper3.yScale = 0.7F;
		emitterUpper3.zScale = 1.1F;

		ModelPart emitterUpper4 = this.emitter.getChild("upper_emitter_4");
		emitterUpper4.xScale = 1.25F;
		emitterUpper4.yScale = 0.5F;
		emitterUpper4.zScale = 1.25F;

		ModelPart emitterUpperBody = this.emitter.getChild("upper_body");
		emitterUpperBody.xScale = 0.45F;
		emitterUpperBody.yScale = 0.6F;
		emitterUpperBody.zScale = 0.45F;
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
		case BODY -> 12.3F;
		case SWITCH_SECTION -> 8.0F;
		case EMITTER -> 30.0F;
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
		addOctagonalShell(bodyGroup, "body", 0, 0, -1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F);

		PartDefinition topRing = root.addOrReplaceChild("top_ring", CubeListBuilder.create(),
				PartPose.offset(0.0F, 1.0F, 0.0F));
		addOctagonalShell(topRing, "top", 10, 0, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createBody() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition bodyGroup = root.addOrReplaceChild("body_group", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(bodyGroup, "body", 8, 0, -1.5F, 0.0F, 2.62F, 3.0F, 6.0F, 1.0F);

		for (int i = 0; i < BOOSTER_RIB_ANGLES.length; i++) {
			PartDefinition rib = root.addOrReplaceChild("booster_rib_" + i,
					CubeListBuilder.create().texOffs(4, 8).addBox(-1.5F, -1.5F, 3.32F, 3.0F, 3.0F, 1.0F),
					PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.0F, BOOSTER_RIB_ANGLES[i], 0.0F));

			rib.addOrReplaceChild("rib_top_bevel_" + i,
					CubeListBuilder.create().texOffs(4, 12).addBox(-1.5F, -2.0F, -0.5F, 3.0F, 2.0F, 1.0F),
					PartPose.offsetAndRotation(0.0F, -1.18F, 3.94F, 0.6981317F, 0.0F, 0.0F));

			rib.addOrReplaceChild("rib_bot_bevel_" + i,
					CubeListBuilder.create().texOffs(4, 12).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 2.0F, 1.0F),
					PartPose.offsetAndRotation(0.0F, 1.18F, 3.94F, -0.6981317F, 0.0F, 0.0F));
		}

		PartDefinition gripGroup = root.addOrReplaceChild("grip_group", CubeListBuilder.create(),
				PartPose.offset(0.0F, 6.0F, 0.0F));
		addOctagonalShell(gripGroup, "grip", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 7.0F, 1.0F);

		for (int i = 0; i < GRIP_10_ANGLES.length; i++) {
			gripGroup.addOrReplaceChild("grip_bar_" + i,
					CubeListBuilder.create().texOffs(0, 8).addBox(-0.5F, 0.0F, -4.02F, 1.0F, 7.0F, 1.0F),
					PartPose.rotation(0.0F, GRIP_10_ANGLES[i], 0.0F));
		}

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createSwitch() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition switchMain = root.addOrReplaceChild("switch_main", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(switchMain, "body", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F);

		PartDefinition box7 = switchMain.getChild("body_6");
		box7.addOrReplaceChild("control_box",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -4.0F, 0.0F, 3.0F, 8.0F, 2.0F),
				PartPose.offset(0.0F, 4.0F, 3.62F));

		PartDefinition lowerBtn = root.addOrReplaceChild("lower_button", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-3.62F, 2.1F, 0.0F, 1.5707964F, 1.5707964F, 0.0F));
		addOctagonalShell(lowerBtn, "low_btn", 10, 9, -1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F);
		for (int i = 0; i < BOOSTER_RIB_ANGLES.length; i++) {
			lowerBtn.addOrReplaceChild("dial_tooth_" + i,
					CubeListBuilder.create().texOffs(22, 0).addBox(-1.5F, -1.5F, 2.7F, 3.0F, 3.0F, 2.0F),
					PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, BOOSTER_RIB_ANGLES[i], 0.0F));
		}

		PartDefinition upperBtn = root.addOrReplaceChild("upper_button", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-3.62F, 6.1F, 0.0F, 1.5707964F, 1.5707964F, 0.0F));
		addOctagonalShell(upperBtn, "up_btn", 8, 0, -1.5F, -5.0F, -0.38F, 3.0F, 5.0F, 4.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createEmitter() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition lowerBody = root.addOrReplaceChild("lower_body", CubeListBuilder.create(), PartPose.ZERO);
		addOctagonalShell(lowerBody, "lower_body", 0, 2, -1.5F, 0.0F, 1.62F, 3.0F, 16.0F, 2.0F);

		PartDefinition ringBase = root.addOrReplaceChild("ring_base", CubeListBuilder.create(), PartPose.ZERO);
		float[] ringYOffsets = { 0.0F, 2.0F, 4.0F, 6.0F, 8.0F, 10.0F, 12.0F, 14.0F, 16.0F, 18.0F };
		for (int r = 0; r < ringYOffsets.length; r++) {
			for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
				ringBase.addOrReplaceChild("ring_" + (r + 1) + "_" + i,
						CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F),
						PartPose.offsetAndRotation(0.0F, ringYOffsets[r], 0.0F, 0.0F, OCTAGON_ANGLES[i], 0.0F));
			}
		}

		PartDefinition upperBody = root.addOrReplaceChild("upper_body", CubeListBuilder.create(),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		addOctagonalShell(upperBody, "upper_body", 10, 0, -1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F);

		PartDefinition middleRingLower = root.addOrReplaceChild("middle_ring_lower", CubeListBuilder.create(),
				PartPose.offset(0.0F, 20.8F, 0.0F));
		addOctagonalShell(middleRingLower, "middle_ring_lower", 18, 0, -1.5F, -1.0F, 0.62F, 3.0F, 1.0F, 3.0F);

		PartDefinition middleRingUpper = root.addOrReplaceChild("middle_ring_upper", CubeListBuilder.create(),
				PartPose.offset(0.0F, 20.8F, 0.0F));
		addOctagonalShell(middleRingUpper, "middle_ring_upper", 18, 4, -1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F);

		PartDefinition upperEmitter1 = root.addOrReplaceChild("upper_emitter_1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 26.5F, 0.0F));
		addOctagonalShell(upperEmitter1, "upper_emitter_1", 18, 8, -1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F);

		PartDefinition upperEmitter2 = root.addOrReplaceChild("upper_emitter_2", CubeListBuilder.create(),
				PartPose.offset(0.0F, 26.5F, 0.0F));
		addOctagonalShell(upperEmitter2, "upper_emitter_2", 0, 20, -1.5F, 0.0F, 0.62F, 3.0F, 3.0F, 3.0F);

		PartDefinition upperEmitter3 = root.addOrReplaceChild("upper_emitter_3", CubeListBuilder.create(),
				PartPose.offset(0.0F, 29.0F, 0.0F));
		addOctagonalShell(upperEmitter3, "upper_emitter_3", 10, 17, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		PartDefinition upperEmitter4 = root.addOrReplaceChild("upper_emitter_4", CubeListBuilder.create(),
				PartPose.offset(0.0F, 30.0F, 0.0F));
		addOctagonalShell(upperEmitter4, "upper_emitter_4", 9, 19, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

		return LayerDefinition.create(mesh, 64, 32);
	}
}