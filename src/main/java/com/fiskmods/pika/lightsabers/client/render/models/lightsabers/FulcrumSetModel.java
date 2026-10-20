package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class FulcrumSetModel extends HiltSetModel {
	private final ModelPart pommel;
	private final ModelPart body;
	private final ModelPart switchSection;
	private final ModelPart emitter;

	public FulcrumSetModel() {
		super("fulcrum");
		this.pommel = createPommelLayer().bakeRoot();
		this.body = createBodyLayer().bakeRoot();
		this.switchSection = createSwitchSectionLayer().bakeRoot();
		this.emitter = createEmitterLayer().bakeRoot();

		ModelPart pommelMain = this.pommel.getChild("main");
		pommelMain.getChild("side1").xScale = 0.45F;
		pommelMain.getChild("side1").yScale = 0.45F;
		pommelMain.getChild("side1").zScale = 0.45F;

		pommelMain.getChild("bottom1").xScale = 0.5F;
		pommelMain.getChild("bottom1").yScale = 0.5F;
		pommelMain.getChild("bottom1").zScale = 0.5F;

		pommelMain.getChild("ring1").xScale = 1.1F;
		pommelMain.getChild("ring1").yScale = 1.1F;
		pommelMain.getChild("ring1").zScale = 1.1F;

		ModelPart bodyMain = this.body.getChild("main");
		bodyMain.getChild("indent1").xScale = 0.9F;
		bodyMain.getChild("indent1").yScale = 0.9F;
		bodyMain.getChild("indent1").zScale = 0.9F;

		bodyMain.getChild("front1").xScale = 0.86F;
		bodyMain.getChild("front1").yScale = 0.86F;
		bodyMain.getChild("front1").zScale = 0.86F;

		ModelPart emitterMain = this.emitter.getChild("main");
		emitterMain.getChild("middleRing1").xScale = 1.1F;
		emitterMain.getChild("middleRing1").yScale = 0.6F;
		emitterMain.getChild("middleRing1").zScale = 1.1F;

		emitterMain.getChild("emitter1_1").xScale = 0.9F;
		emitterMain.getChild("emitter1_1").yScale = 0.9F;
		emitterMain.getChild("emitter1_1").zScale = 0.9F;
	}

	@Override
	public ModelPart getPartModel(PartType type) {
		return getPartModel(type, null);
	}

	@Override
	public ModelPart getPartModel(PartType type, LightsaberData data) {
		if (type == PartType.POMMEL) {
			ModelPart pommelMain = this.pommel.getChild("main");
			boolean isFulcrumBody = isFulcrumGrip(data);

			if (isFulcrumBody) {

				pommelMain.x = 0.0F;
				pommelMain.y = 7.0F;
				pommelMain.z = -1.13F;
				pommelMain.xRot = (float) Math.PI - 0.0872665F;
			} else {

				pommelMain.x = 0.0F;
				pommelMain.y = 7.0F;
				pommelMain.z = 0.0F;
				pommelMain.xRot = (float) Math.PI;
			}
			pommelMain.yRot = 0.0F;
			pommelMain.zRot = 0.0F;
			return this.pommel;
		}

		return switch (type) {
		case POMMEL -> this.pommel;
		case BODY -> this.body;
		case SWITCH_SECTION -> this.switchSection;
		case EMITTER -> this.emitter;
		};
	}

	private boolean isFulcrumGrip(LightsaberData data) {
		if (data == null || data.grip() == null) {
			return false;
		}
		ResourceLocation key = BuiltInRegistries.ITEM.getKey(data.grip());
		return key != null && key.getPath().contains("fulcrum");
	}

	@Override
	public float getHeight(PartType type) {
		return switch (type) {
		case POMMEL -> 7.0F;
		case BODY -> 30.5F;
		case SWITCH_SECTION -> 10.0F;
		case EMITTER -> 18.7F;
		};
	}

	public static LayerDefinition createPommelLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, (float) Math.PI, 0.0F, 0.0F));

		PartDefinition cap1 = main.addOrReplaceChild("cap1",
				CubeListBuilder.create().texOffs(43, 18).addBox(-1.5F, 0.0F, 1.76F, 3.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 6.24F, 0.24F, 0.034906585F, 0.0F, 0.0F));

		cap1.addOrReplaceChild("cap6",
				CubeListBuilder.create().texOffs(32, 20).addBox(-2.98F, 0.0F, -2.5F, 3.0F, 1.0F, 5.0F), PartPose.ZERO);

		cap1.addOrReplaceChild("cap7",
				CubeListBuilder.create().mirror().texOffs(32, 20).addBox(-0.02F, 0.0F, -2.5F, 3.0F, 1.0F, 5.0F),
				PartPose.ZERO);

		PartDefinition cap2 = cap1.addOrReplaceChild("cap2",
				CubeListBuilder.create().texOffs(30, 17).addBox(-1.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		cap2.addOrReplaceChild("cap3",
				CubeListBuilder.create().texOffs(30, 17).addBox(-1.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition cap4 = cap1.addOrReplaceChild("cap4",
				CubeListBuilder.create().mirror().texOffs(30, 17).addBox(0.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		cap4.addOrReplaceChild("cap5",
				CubeListBuilder.create().mirror().texOffs(30, 17).addBox(0.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition cap8 = cap1.addOrReplaceChild("cap8",
				CubeListBuilder.create().texOffs(43, 18).addBox(-1.5F, 0.0F, 1.76F, 3.0F, 1.0F, 2.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition cap9 = cap8.addOrReplaceChild("cap9",
				CubeListBuilder.create().texOffs(30, 17).addBox(-1.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		cap9.addOrReplaceChild("cap10",
				CubeListBuilder.create().texOffs(30, 17).addBox(-1.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition cap11 = cap8.addOrReplaceChild("cap11",
				CubeListBuilder.create().mirror().texOffs(30, 17).addBox(0.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		cap11.addOrReplaceChild("cap12",
				CubeListBuilder.create().mirror().texOffs(30, 17).addBox(0.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition side1 = main.addOrReplaceChild("side1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-14.5F, -1.0F, 1.62F, 16.0F, 12.0F, 2.0F),
				PartPose.offsetAndRotation(-3.0F, 1.1F, 0.04F, 0.0F, 0.034906585F, -1.5707964F));

		side1.addOrReplaceChild("side2",
				CubeListBuilder.create().texOffs(26, 14).addBox(-1.5F, -1.0F, 1.62F, 3.0F, 1.0F, 2.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));

		side1.addOrReplaceChild("side3",
				CubeListBuilder.create().texOffs(26, 14).addBox(-1.5F, -1.0F, 1.62F, 3.0F, 1.0F, 2.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));

		side1.addOrReplaceChild("side4",
				CubeListBuilder.create().texOffs(26, 14).addBox(-1.5F, -1.0F, 1.62F, 3.0F, 1.0F, 2.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));

		side1.addOrReplaceChild("side5",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-1.5F, -1.0F, 1.62F, 16.0F, 12.0F, 2.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		side1.addOrReplaceChild("side6",
				CubeListBuilder.create().texOffs(26, 14).addBox(-1.5F, -1.0F, 1.62F, 3.0F, 1.0F, 2.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));

		side1.addOrReplaceChild("side7",
				CubeListBuilder.create().texOffs(26, 14).addBox(-1.5F, -1.0F, 1.62F, 3.0F, 1.0F, 2.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));

		side1.addOrReplaceChild("side8",
				CubeListBuilder.create().texOffs(26, 14).addBox(-1.5F, -1.0F, 1.62F, 3.0F, 1.0F, 2.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		side1.addOrReplaceChild("side9",
				CubeListBuilder.create().texOffs(0, 14).addBox(-11.0F, -0.5F, -2.0F, 11.0F, 12.0F, 4.0F),
				PartPose.offset(-3.5F, -0.5F, 0.0F));

		PartDefinition bottom1 = main.addOrReplaceChild("bottom1",
				CubeListBuilder.create().texOffs(47, 15).addBox(0.0F, -1.0F, -3.0F, 2.0F, 2.0F, 6.0F),
				PartPose.offsetAndRotation(0.0F, 7.0F, 0.34F, 3.1415927F, -0.034906585F, 1.5707964F));

		PartDefinition bottom2 = bottom1.addOrReplaceChild("bottom2",
				CubeListBuilder.create().mirror().texOffs(21, 21).addBox(-0.5F, -0.5F, -4.5F, 1.0F, 1.0F, 9.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.34906584F));

		bottom2.addOrReplaceChild("bottom9",
				CubeListBuilder.create().mirror().texOffs(56, 2).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F),
				PartPose.offset(6.35F, 0.0F, 0.0F));

		PartDefinition bottom3 = bottom2.addOrReplaceChild("bottom3",
				CubeListBuilder.create().mirror().texOffs(51, 0).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
				PartPose.offset(0.5F, 0.0F, -4.0F));

		PartDefinition bottom4 = bottom3.addOrReplaceChild("bottom4",
				CubeListBuilder.create().mirror().texOffs(51, 2).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(3.68F, 0.0F, -0.12F, 0.0F, -0.6981317F, 0.0F));

		bottom4.addOrReplaceChild("bottom5",
				CubeListBuilder.create().mirror().texOffs(51, 2).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.78F, 0.0F, -0.05F, 0.0F, -0.4537856F, 0.0F));

		PartDefinition bottom6 = bottom2.addOrReplaceChild("bottom6",
				CubeListBuilder.create().mirror().texOffs(51, 0).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
				PartPose.offset(0.5F, 0.0F, 4.0F));

		PartDefinition bottom7 = bottom6.addOrReplaceChild("bottom7",
				CubeListBuilder.create().mirror().texOffs(51, 2).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(3.68F, 0.0F, 0.12F, 0.0F, 0.6981317F, 0.0F));

		bottom7.addOrReplaceChild("bottom8",
				CubeListBuilder.create().mirror().texOffs(51, 2).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.78F, 0.0F, 0.05F, 0.0F, 0.4537856F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(36, 4).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -0.69F, 0.0F, 0.034906585F, 0.0F, 0.0F));

		body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(44, 0).addBox(-2.98F, 0.0F, -2.5F, 1.0F, 7.0F, 5.0F), PartPose.ZERO);

		body1.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(44, 0).addBox(1.98F, 0.0F, -2.5F, 1.0F, 7.0F, 5.0F), PartPose.ZERO);

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(36, 12).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body2.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(36, 12).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body4 = body1.addOrReplaceChild("body4",
				CubeListBuilder.create().mirror().texOffs(36, 12).addBox(0.0F, -0.5F, -1.0F, 1.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().mirror().texOffs(36, 12).addBox(0.0F, -0.5F, -1.0F, 1.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body8 = body1.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(36, 4).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 7.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body9 = body8.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(36, 12).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body9.addOrReplaceChild("body10",
				CubeListBuilder.create().texOffs(36, 12).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body11 = body8.addOrReplaceChild("body11",
				CubeListBuilder.create().mirror().texOffs(36, 12).addBox(0.0F, -0.5F, -1.0F, 1.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body11.addOrReplaceChild("body12",
				CubeListBuilder.create().mirror().texOffs(36, 12).addBox(0.0F, -0.5F, -1.0F, 1.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition ring1 = main.addOrReplaceChild("ring1",
				CubeListBuilder.create().texOffs(34, 0).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 4.31F, 0.18F, 0.034906585F, 0.0F, 0.0F));

		ring1.addOrReplaceChild("ring6",
				CubeListBuilder.create().texOffs(40, 12).addBox(-2.98F, 0.0F, -2.5F, 1.0F, 1.0F, 5.0F), PartPose.ZERO);

		ring1.addOrReplaceChild("ring7",
				CubeListBuilder.create().mirror().texOffs(40, 12).addBox(1.98F, 0.0F, -2.5F, 1.0F, 1.0F, 5.0F),
				PartPose.ZERO);

		PartDefinition ring2 = ring1.addOrReplaceChild("ring2",
				CubeListBuilder.create().texOffs(36, 2).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		ring2.addOrReplaceChild("ring3",
				CubeListBuilder.create().texOffs(36, 2).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition ring4 = ring1.addOrReplaceChild("ring4",
				CubeListBuilder.create().mirror().texOffs(36, 2).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		ring4.addOrReplaceChild("ring5",
				CubeListBuilder.create().mirror().texOffs(36, 2).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition ring8 = ring1.addOrReplaceChild("ring8",
				CubeListBuilder.create().texOffs(34, 0).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition ring9 = ring8.addOrReplaceChild("ring9",
				CubeListBuilder.create().texOffs(36, 2).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		ring9.addOrReplaceChild("ring10",
				CubeListBuilder.create().texOffs(36, 2).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition ring11 = ring8.addOrReplaceChild("ring11",
				CubeListBuilder.create().mirror().texOffs(36, 2).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		ring11.addOrReplaceChild("ring12",
				CubeListBuilder.create().mirror().texOffs(36, 2).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	public static LayerDefinition createSwitchSectionLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.rotation((float) Math.PI, 0.0F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(4, 16).addBox(-1.5F, -10.0F, 2.76F, 3.0F, 10.0F, 1.0F), PartPose.ZERO);

		PartDefinition body6 = body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(0, 16).addBox(-2.98F, -10.0F, 1.5F, 1.0F, 10.0F, 1.0F), PartPose.ZERO);

		body6.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(0, 16).addBox(-2.98F, -10.0F, -2.5F, 1.0F, 10.0F, 1.0F),
				PartPose.ZERO);

		body6.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(0, 0).addBox(-2.58F, -10.0F, -3.0F, 1.0F, 10.0F, 6.0F), PartPose.ZERO);

		PartDefinition body9 = body1.addOrReplaceChild("body9",
				CubeListBuilder.create().mirror().texOffs(0, 16).addBox(1.98F, -10.0F, 1.5F, 1.0F, 10.0F, 1.0F),
				PartPose.ZERO);

		body9.addOrReplaceChild("body10",
				CubeListBuilder.create().mirror().texOffs(0, 16).addBox(1.98F, -10.0F, -2.5F, 1.0F, 10.0F, 1.0F),
				PartPose.ZERO);

		body9.addOrReplaceChild("body11",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(1.58F, -10.0F, -3.0F, 1.0F, 10.0F, 6.0F),
				PartPose.ZERO);

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -10.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body2.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -10.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body4 = body1.addOrReplaceChild("body4",
				CubeListBuilder.create().mirror().texOffs(0, 16).addBox(0.0F, -10.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().mirror().texOffs(0, 16).addBox(0.0F, -10.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body12 = body1.addOrReplaceChild("body12",
				CubeListBuilder.create().texOffs(4, 16).addBox(-1.5F, -10.0F, 2.76F, 3.0F, 10.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body13 = body12.addOrReplaceChild("body13",
				CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -10.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body13.addOrReplaceChild("body14",
				CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -10.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body15 = body12.addOrReplaceChild("body15",
				CubeListBuilder.create().mirror().texOffs(0, 16).addBox(0.0F, -10.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body15.addOrReplaceChild("body16",
				CubeListBuilder.create().mirror().texOffs(0, 16).addBox(0.0F, -10.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body17 = body1.addOrReplaceChild("body17", CubeListBuilder.create(),
				PartPose.offset(-2.09F, -7.6F, 0.0F));

		body17.addOrReplaceChild("body18",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.5F, 0.40840703F, 0.0F, 0.0F));

		body17.addOrReplaceChild("body19",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, -0.40840703F, 0.0F, 0.0F));

		body17.addOrReplaceChild("body20",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.5F, -0.40840703F, 0.0F, 0.0F));

		body17.addOrReplaceChild("body21",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.40840703F, 0.0F, 0.0F));

		body17.addOrReplaceChild("body22",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 1.83F, 0.0F, 0.7853982F, 0.0F, 0.0F));

		body17.addOrReplaceChild("body23",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -1.83F, 0.0F, 0.7853982F, 0.0F, 0.0F));

		body17.addOrReplaceChild("body24",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);

		PartDefinition body25 = body1.addOrReplaceChild("body25", CubeListBuilder.create(),
				PartPose.offset(-2.09F, -2.4F, 0.0F));

		body25.addOrReplaceChild("body26",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.5F, 0.40840703F, 0.0F, 0.0F));

		body25.addOrReplaceChild("body27",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, -0.40840703F, 0.0F, 0.0F));

		body25.addOrReplaceChild("body28",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.5F, -0.40840703F, 0.0F, 0.0F));

		body25.addOrReplaceChild("body29",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.40840703F, 0.0F, 0.0F));

		body25.addOrReplaceChild("body30",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 1.83F, 0.0F, 0.7853982F, 0.0F, 0.0F));

		body25.addOrReplaceChild("body31",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -1.83F, 0.0F, 0.7853982F, 0.0F, 0.0F));

		body25.addOrReplaceChild("body32",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);

		PartDefinition body33 = body1.addOrReplaceChild("body33", CubeListBuilder.create(),
				PartPose.offset(2.09F, -7.6F, 0.0F));

		body33.addOrReplaceChild("body34",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.5F, 0.40840703F, 0.0F, 0.0F));

		body33.addOrReplaceChild("body35",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, -0.40840703F, 0.0F, 0.0F));

		body33.addOrReplaceChild("body36",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.5F, -0.40840703F, 0.0F, 0.0F));

		body33.addOrReplaceChild("body37",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.40840703F, 0.0F, 0.0F));

		body33.addOrReplaceChild("body38",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 1.83F, 0.0F, 0.7853982F, 0.0F, 0.0F));

		body33.addOrReplaceChild("body39",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -1.83F, 0.0F, 0.7853982F, 0.0F, 0.0F));

		body33.addOrReplaceChild("body40",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.ZERO);

		PartDefinition body41 = body1.addOrReplaceChild("body41", CubeListBuilder.create(),
				PartPose.offset(2.09F, -2.4F, 0.0F));

		body41.addOrReplaceChild("body42",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.5F, 0.40840703F, 0.0F, 0.0F));

		body41.addOrReplaceChild("body43",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, -0.40840703F, 0.0F, 0.0F));

		body41.addOrReplaceChild("body44",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.5F, -0.40840703F, 0.0F, 0.0F));

		body41.addOrReplaceChild("body45",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.40840703F, 0.0F, 0.0F));

		body41.addOrReplaceChild("body46",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 1.83F, 0.0F, 0.7853982F, 0.0F, 0.0F));

		body41.addOrReplaceChild("body47",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -1.83F, 0.0F, 0.7853982F, 0.0F, 0.0F));

		body41.addOrReplaceChild("body48",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 30.5F, 0.0F, (float) Math.PI, 0.0F, 0.0F));

		PartDefinition indent1 = main.addOrReplaceChild("indent1",
				CubeListBuilder.create().texOffs(13, 0).addBox(-1.5F, -1.0F, -3.76F, 3.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 13.6F, 0.1F, 0.034906585F, 0.0F, 0.0F));

		PartDefinition indent2 = indent1.addOrReplaceChild("indent2",
				CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, -0.5F, -3.76F, 0.0F, 0.46949357F, 0.0F));

		indent2.addOrReplaceChild("indent3",
				CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition indent4 = indent1.addOrReplaceChild("indent4",
				CubeListBuilder.create().mirror().texOffs(16, 3).addBox(0.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, -0.5F, -3.76F, 0.0F, -0.46949357F, 0.0F));

		indent4.addOrReplaceChild("indent5",
				CubeListBuilder.create().mirror().texOffs(16, 3).addBox(0.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		indent1.addOrReplaceChild("indent6",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(1.98F, -1.0F, -2.5F, 1.0F, 2.0F, 3.0F),
				PartPose.ZERO);

		indent1.addOrReplaceChild("indent7",
				CubeListBuilder.create().texOffs(8, 0).addBox(-2.98F, -1.0F, -2.5F, 1.0F, 2.0F, 3.0F), PartPose.ZERO);

		PartDefinition indent8 = indent1.addOrReplaceChild("indent8",
				CubeListBuilder.create().texOffs(13, 0).addBox(-1.5F, -1.0F, -3.76F, 3.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, 4.3F, 0.0F));

		PartDefinition indent9 = indent8.addOrReplaceChild("indent9",
				CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, -0.5F, -3.76F, 0.0F, 0.46949357F, 0.0F));

		indent9.addOrReplaceChild("indent10",
				CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition indent11 = indent8.addOrReplaceChild("indent11",
				CubeListBuilder.create().mirror().texOffs(16, 3).addBox(0.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, -0.5F, -3.76F, 0.0F, -0.46949357F, 0.0F));

		indent11.addOrReplaceChild("indent12",
				CubeListBuilder.create().mirror().texOffs(16, 3).addBox(0.0F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		indent8.addOrReplaceChild("indent13",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(1.98F, -1.0F, -2.5F, 1.0F, 2.0F, 3.0F),
				PartPose.ZERO);

		indent8.addOrReplaceChild("indent14",
				CubeListBuilder.create().texOffs(8, 0).addBox(-2.98F, -1.0F, -2.5F, 1.0F, 2.0F, 3.0F), PartPose.ZERO);

		PartDefinition indent15 = indent1.addOrReplaceChild("indent15",
				CubeListBuilder.create().texOffs(13, 0).addBox(-1.5F, -1.0F, -3.76F, 3.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 8.5F, 0.05F, 0.02617994F, 0.0F, 0.0F));

		PartDefinition indent16 = indent15.addOrReplaceChild("indent16",
				CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, -0.5F, -3.76F, 0.0F, 0.46949357F, 0.0F));

		indent16.addOrReplaceChild("indent17",
				CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition indent18 = indent15.addOrReplaceChild("indent18",
				CubeListBuilder.create().mirror().texOffs(16, 3).addBox(0.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, -0.5F, -3.76F, 0.0F, -0.46949357F, 0.0F));

		indent18.addOrReplaceChild("indent19",
				CubeListBuilder.create().mirror().texOffs(16, 3).addBox(0.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		indent15.addOrReplaceChild("indent20",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(1.98F, -1.0F, -2.5F, 1.0F, 2.0F, 3.0F),
				PartPose.ZERO);

		indent15.addOrReplaceChild("indent21",
				CubeListBuilder.create().texOffs(8, 0).addBox(-2.98F, -1.0F, -2.5F, 1.0F, 2.0F, 3.0F), PartPose.ZERO);

		PartDefinition indent22 = indent1.addOrReplaceChild("indent22",
				CubeListBuilder.create().texOffs(13, 0).addBox(-1.5F, -1.0F, -3.76F, 3.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 12.9F, 0.23F, 0.05235988F, 0.0F, 0.0F));

		PartDefinition indent23 = indent22.addOrReplaceChild("indent23",
				CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, -0.5F, -3.76F, 0.0F, 0.46949357F, 0.0F));

		indent23.addOrReplaceChild("indent24",
				CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition indent25 = indent22.addOrReplaceChild("indent25",
				CubeListBuilder.create().mirror().texOffs(16, 3).addBox(0.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, -0.5F, -3.76F, 0.0F, -0.46949357F, 0.0F));

		indent25.addOrReplaceChild("indent26",
				CubeListBuilder.create().mirror().texOffs(16, 3).addBox(0.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		indent22.addOrReplaceChild("indent27",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(1.98F, -1.0F, -2.5F, 1.0F, 2.0F, 3.0F),
				PartPose.ZERO);

		indent22.addOrReplaceChild("indent28",
				CubeListBuilder.create().texOffs(8, 0).addBox(-2.98F, -1.0F, -2.5F, 1.0F, 2.0F, 3.0F), PartPose.ZERO);

		PartDefinition indent29 = indent1.addOrReplaceChild("indent29",
				CubeListBuilder.create().texOffs(13, 0).addBox(-1.5F, -1.0F, -3.76F, 3.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 17.2F, 0.45F, 0.05235988F, 0.0F, 0.0F));

		PartDefinition indent30 = indent29.addOrReplaceChild("indent30",
				CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, -0.5F, -3.76F, 0.0F, 0.46949357F, 0.0F));

		indent30.addOrReplaceChild("indent31",
				CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition indent32 = indent29.addOrReplaceChild("indent32",
				CubeListBuilder.create().mirror().texOffs(16, 3).addBox(0.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, -0.5F, -3.76F, 0.0F, -0.46949357F, 0.0F));

		indent32.addOrReplaceChild("indent33",
				CubeListBuilder.create().mirror().texOffs(16, 3).addBox(0.0F, -0.5F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		indent29.addOrReplaceChild("indent34",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(1.98F, -1.0F, -2.5F, 1.0F, 2.0F, 3.0F),
				PartPose.ZERO);

		indent29.addOrReplaceChild("indent35",
				CubeListBuilder.create().texOffs(8, 0).addBox(-2.98F, -1.0F, -2.5F, 1.0F, 2.0F, 3.0F), PartPose.ZERO);

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 18).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 12.0F, 1.0F), PartPose.ZERO);

		PartDefinition body6 = body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(8, 18).addBox(-2.98F, 0.0F, 1.5F, 1.0F, 12.0F, 1.0F), PartPose.ZERO);

		body6.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(8, 18).addBox(-2.98F, 0.0F, -2.5F, 1.0F, 12.0F, 1.0F), PartPose.ZERO);

		body6.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(0, 0).addBox(-2.58F, 0.0F, -3.0F, 1.0F, 12.0F, 6.0F), PartPose.ZERO);

		PartDefinition body9 = body1.addOrReplaceChild("body9",
				CubeListBuilder.create().mirror().texOffs(8, 18).addBox(1.98F, 0.0F, 1.5F, 1.0F, 12.0F, 1.0F),
				PartPose.ZERO);

		body9.addOrReplaceChild("body10",
				CubeListBuilder.create().mirror().texOffs(8, 18).addBox(1.98F, 0.0F, -2.5F, 1.0F, 12.0F, 1.0F),
				PartPose.ZERO);

		body9.addOrReplaceChild("body11",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(1.58F, 0.0F, -3.0F, 1.0F, 12.0F, 6.0F),
				PartPose.ZERO);

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(8, 18).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 12.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body2.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(8, 18).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 12.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body4 = body1.addOrReplaceChild("body4",
				CubeListBuilder.create().mirror().texOffs(8, 18).addBox(0.0F, -0.5F, -1.0F, 1.0F, 12.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().mirror().texOffs(8, 18).addBox(0.0F, -0.5F, -1.0F, 1.0F, 12.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body12 = body1.addOrReplaceChild("body12",
				CubeListBuilder.create().texOffs(0, 18).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 12.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body13 = body12.addOrReplaceChild("body13",
				CubeListBuilder.create().texOffs(8, 18).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 12.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body13.addOrReplaceChild("body14",
				CubeListBuilder.create().texOffs(8, 18).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 12.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body15 = body12.addOrReplaceChild("body15",
				CubeListBuilder.create().mirror().texOffs(8, 18).addBox(0.0F, -0.5F, -1.0F, 1.0F, 12.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body15.addOrReplaceChild("body16",
				CubeListBuilder.create().mirror().texOffs(8, 18).addBox(0.0F, -0.5F, -1.0F, 1.0F, 12.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body17 = body1.addOrReplaceChild("body17",
				CubeListBuilder.create().texOffs(20, 18).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 11.7F, 0.0F, 0.034906585F, 0.0F, 0.0F));

		PartDefinition body18 = body17.addOrReplaceChild("body18",
				CubeListBuilder.create().texOffs(28, 18).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body18.addOrReplaceChild("body19",
				CubeListBuilder.create().texOffs(28, 18).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body20 = body17.addOrReplaceChild("body20",
				CubeListBuilder.create().mirror().texOffs(28, 18).addBox(0.0F, -0.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body20.addOrReplaceChild("body21",
				CubeListBuilder.create().mirror().texOffs(28, 18).addBox(0.0F, -0.5F, -1.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body22 = body17.addOrReplaceChild("body22",
				CubeListBuilder.create().texOffs(12, 18).addBox(-2.98F, 0.0F, -0.5F, 1.0F, 10.0F, 3.0F), PartPose.ZERO);

		PartDefinition body23 = body22.addOrReplaceChild("body23",
				CubeListBuilder.create().texOffs(19, 5).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offset(-2.48F, 0.5F, -0.5F));

		body23.addOrReplaceChild("body24",
				CubeListBuilder.create().texOffs(21, 0).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 1.65F, 0.0F, 0.7853982F, 0.0F, 0.0F));

		body22.addOrReplaceChild("body25",
				CubeListBuilder.create().texOffs(20, 3).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-2.48F, 0.0F, 0.9F, 0.61086524F, 0.0F, 0.0F));

		body22.addOrReplaceChild("body26",
				CubeListBuilder.create().texOffs(20, 3).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-2.48F, 0.0F, -0.9F, 0.9599311F, 0.0F, 0.0F));

		PartDefinition body27 = body17.addOrReplaceChild("body27",
				CubeListBuilder.create().mirror().texOffs(12, 18).addBox(1.98F, 0.0F, -0.5F, 1.0F, 10.0F, 3.0F),
				PartPose.ZERO);

		PartDefinition body28 = body27.addOrReplaceChild("body28",
				CubeListBuilder.create().mirror().texOffs(19, 5).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offset(2.48F, 0.5F, -0.5F));

		body28.addOrReplaceChild("body29",
				CubeListBuilder.create().mirror().texOffs(21, 0).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 1.65F, 0.0F, 0.7853982F, 0.0F, 0.0F));

		body27.addOrReplaceChild("body30",
				CubeListBuilder.create().mirror().texOffs(20, 3).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(2.48F, 0.0F, 0.9F, 0.61086524F, 0.0F, 0.0F));

		body27.addOrReplaceChild("body31",
				CubeListBuilder.create().mirror().texOffs(20, 3).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(2.48F, 0.0F, -0.9F, 0.9599311F, 0.0F, 0.0F));

		PartDefinition body32 = body17.addOrReplaceChild("body32",
				CubeListBuilder.create().texOffs(25, 0).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body33 = body32.addOrReplaceChild("body33",
				CubeListBuilder.create().texOffs(24, 2).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body33.addOrReplaceChild("body34",
				CubeListBuilder.create().texOffs(24, 2).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body35 = body32.addOrReplaceChild("body35",
				CubeListBuilder.create().mirror().texOffs(24, 2).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body35.addOrReplaceChild("body36",
				CubeListBuilder.create().mirror().texOffs(24, 2).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body37 = body17.addOrReplaceChild("body37",
				CubeListBuilder.create().texOffs(22, 8).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 9.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 9.8F, 0.0F, 0.05235988F, 0.0F, 0.0F));

		PartDefinition body38 = body37.addOrReplaceChild("body38",
				CubeListBuilder.create().texOffs(30, 8).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 9.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body38.addOrReplaceChild("body39",
				CubeListBuilder.create().texOffs(30, 8).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 9.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body40 = body37.addOrReplaceChild("body40",
				CubeListBuilder.create().mirror().texOffs(30, 8).addBox(0.0F, -0.5F, -1.0F, 1.0F, 9.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body40.addOrReplaceChild("body41",
				CubeListBuilder.create().mirror().texOffs(30, 8).addBox(0.0F, -0.5F, -1.0F, 1.0F, 9.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body42 = body37.addOrReplaceChild("body42",
				CubeListBuilder.create().texOffs(14, 6).addBox(-2.98F, 0.0F, -0.5F, 1.0F, 9.0F, 3.0F), PartPose.ZERO);

		PartDefinition body43 = body42.addOrReplaceChild("body43",
				CubeListBuilder.create().texOffs(19, 5).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offset(-2.48F, 8.5F, -0.5F));

		body43.addOrReplaceChild("body44",
				CubeListBuilder.create().texOffs(21, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -1.65F, 0.0F, -0.7853982F, 0.0F, 0.0F));

		PartDefinition body45 = body37.addOrReplaceChild("body45",
				CubeListBuilder.create().mirror().texOffs(14, 6).addBox(1.98F, 0.0F, -0.5F, 1.0F, 9.0F, 3.0F),
				PartPose.ZERO);

		PartDefinition body46 = body45.addOrReplaceChild("body46",
				CubeListBuilder.create().mirror().texOffs(19, 5).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offset(2.48F, 8.5F, -0.5F));

		body46.addOrReplaceChild("body47",
				CubeListBuilder.create().mirror().texOffs(21, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -1.65F, 0.0F, -0.7853982F, 0.0F, 0.0F));

		PartDefinition body48 = body37.addOrReplaceChild("body48",
				CubeListBuilder.create().texOffs(25, 0).addBox(-1.5F, 8.0F, 2.76F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body49 = body48.addOrReplaceChild("body49",
				CubeListBuilder.create().texOffs(24, 2).addBox(-1.0F, 7.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body49.addOrReplaceChild("body50",
				CubeListBuilder.create().texOffs(24, 2).addBox(-1.0F, 7.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body51 = body48.addOrReplaceChild("body51",
				CubeListBuilder.create().mirror().texOffs(24, 2).addBox(0.0F, 7.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body51.addOrReplaceChild("body52",
				CubeListBuilder.create().mirror().texOffs(24, 2).addBox(0.0F, 7.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition front1 = main.addOrReplaceChild("front1",
				CubeListBuilder.create().texOffs(34, 6).addBox(-1.5F, 0.0F, -3.76F, 3.0F, 11.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 11.7F, 0.0F, 0.034906585F, 0.0F, 0.0F));

		PartDefinition front2 = front1.addOrReplaceChild("front2",
				CubeListBuilder.create().texOffs(42, 6).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 11.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, -3.76F, 0.0F, 0.46949357F, 0.0F));

		front2.addOrReplaceChild("front3",
				CubeListBuilder.create().texOffs(42, 6).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 11.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition front4 = front1.addOrReplaceChild("front4",
				CubeListBuilder.create().mirror().texOffs(42, 6).addBox(0.0F, -0.5F, 0.0F, 1.0F, 11.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, -3.76F, 0.0F, -0.46949357F, 0.0F));

		front4.addOrReplaceChild("front5",
				CubeListBuilder.create().mirror().texOffs(42, 6).addBox(0.0F, -0.5F, 0.0F, 1.0F, 11.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		front1.addOrReplaceChild("front6",
				CubeListBuilder.create().mirror().texOffs(46, 5).addBox(1.98F, 0.0F, -2.5F, 1.0F, 11.0F, 2.0F),
				PartPose.ZERO);

		front1.addOrReplaceChild("front7",
				CubeListBuilder.create().texOffs(46, 5).addBox(-2.98F, 0.0F, -2.5F, 1.0F, 11.0F, 2.0F), PartPose.ZERO);

		PartDefinition front8 = front1.addOrReplaceChild("front8",
				CubeListBuilder.create().texOffs(32, 18).addBox(-1.5F, 0.0F, -3.76F, 3.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 10.7F, 0.0F, 0.05235988F, 0.0F, 0.0F));

		PartDefinition front9 = front8.addOrReplaceChild("front9",
				CubeListBuilder.create().texOffs(40, 18).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, -3.76F, 0.0F, 0.46949357F, 0.0F));

		front9.addOrReplaceChild("front10",
				CubeListBuilder.create().texOffs(40, 18).addBox(-1.0F, -0.5F, 0.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition front11 = front8.addOrReplaceChild("front11",
				CubeListBuilder.create().mirror().texOffs(40, 18).addBox(0.0F, -0.5F, 0.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, -3.76F, 0.0F, -0.46949357F, 0.0F));

		front11.addOrReplaceChild("front12",
				CubeListBuilder.create().mirror().texOffs(40, 18).addBox(0.0F, -0.5F, 0.0F, 1.0F, 10.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		front8.addOrReplaceChild("front13",
				CubeListBuilder.create().mirror().texOffs(44, 18).addBox(1.98F, 0.0F, -2.5F, 1.0F, 10.0F, 2.0F),
				PartPose.ZERO);

		front8.addOrReplaceChild("front14",
				CubeListBuilder.create().texOffs(44, 18).addBox(-2.98F, 0.0F, -2.5F, 1.0F, 10.0F, 2.0F), PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}

	public static LayerDefinition createEmitterLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.rotation((float) Math.PI, 0.0F, 0.0F));

		PartDefinition middleRing1 = main.addOrReplaceChild("middleRing1",
				CubeListBuilder.create().texOffs(0, 23).addBox(-1.5F, -0.5F, 2.76F, 3.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, -13.5F, 0.0F));

		middleRing1.addOrReplaceChild("middleRing6",
				CubeListBuilder.create().texOffs(0, 17).addBox(-2.98F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F), PartPose.ZERO);

		middleRing1.addOrReplaceChild("middleRing7",
				CubeListBuilder.create().mirror().texOffs(0, 17).addBox(1.98F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F),
				PartPose.ZERO);

		PartDefinition middleRing2 = middleRing1.addOrReplaceChild("middleRing2",
				CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.0F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		middleRing2.addOrReplaceChild("middleRing3",
				CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition middleRing4 = middleRing1.addOrReplaceChild("middleRing4",
				CubeListBuilder.create().mirror().texOffs(0, 17).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.0F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		middleRing4.addOrReplaceChild("middleRing5",
				CubeListBuilder.create().mirror().texOffs(0, 17).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition middleRing8 = middleRing1.addOrReplaceChild("middleRing8",
				CubeListBuilder.create().texOffs(0, 23).addBox(-1.5F, -0.5F, 2.76F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition middleRing9 = middleRing8.addOrReplaceChild("middleRing9",
				CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.0F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		middleRing9.addOrReplaceChild("middleRing10",
				CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition middleRing11 = middleRing8.addOrReplaceChild("middleRing11",
				CubeListBuilder.create().mirror().texOffs(0, 17).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.0F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		middleRing11.addOrReplaceChild("middleRing12",
				CubeListBuilder.create().mirror().texOffs(0, 17).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition emitter2_1 = main.addOrReplaceChild("emitter2_1",
				CubeListBuilder.create().texOffs(8, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, -17.7F, 0.0F));

		emitter2_1.addOrReplaceChild("emitter2_2",
				CubeListBuilder.create().texOffs(8, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));

		emitter2_1.addOrReplaceChild("emitter2_3",
				CubeListBuilder.create().texOffs(8, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));

		emitter2_1.addOrReplaceChild("emitter2_4",
				CubeListBuilder.create().texOffs(8, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));

		emitter2_1.addOrReplaceChild("emitter2_5",
				CubeListBuilder.create().texOffs(8, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		emitter2_1.addOrReplaceChild("emitter2_6",
				CubeListBuilder.create().texOffs(8, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));

		emitter2_1.addOrReplaceChild("emitter2_7",
				CubeListBuilder.create().texOffs(8, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));

		emitter2_1.addOrReplaceChild("emitter2_8",
				CubeListBuilder.create().texOffs(8, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -16.0F, 2.76F, 3.0F, 16.0F, 1.0F), PartPose.ZERO);

		PartDefinition body6 = body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(8, 0).addBox(-2.98F, -16.0F, 1.5F, 1.0F, 16.0F, 1.0F), PartPose.ZERO);

		body6.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(8, 0).addBox(-2.98F, -16.0F, -2.5F, 1.0F, 16.0F, 1.0F), PartPose.ZERO);

		body6.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(12, 0).addBox(-2.58F, -13.0F, -3.0F, 1.0F, 13.0F, 6.0F),
				PartPose.ZERO);

		PartDefinition body9 = body6.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(0, 25).addBox(-0.5F, -3.5F, -3.0F, 1.0F, 4.0F, 3.0F),
				PartPose.offset(-2.48F, -12.5F, 1.5F));

		PartDefinition body10 = body9.addOrReplaceChild("body10",
				CubeListBuilder.create().texOffs(8, 25).addBox(-0.5F, -1.0F, -4.0F, 1.0F, 1.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.61086524F, 0.0F, 0.0F));

		body10.addOrReplaceChild("body11",
				CubeListBuilder.create().texOffs(6, 25).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, -0.8F, -2.0F));

		PartDefinition body12 = body1.addOrReplaceChild("body12",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(1.98F, -16.0F, 1.5F, 1.0F, 16.0F, 1.0F),
				PartPose.ZERO);

		body12.addOrReplaceChild("body13",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(1.98F, -16.0F, -2.5F, 1.0F, 16.0F, 1.0F),
				PartPose.ZERO);

		body12.addOrReplaceChild("body14",
				CubeListBuilder.create().mirror().texOffs(12, 0).addBox(1.58F, -13.0F, -3.0F, 1.0F, 13.0F, 6.0F),
				PartPose.ZERO);

		PartDefinition body15 = body12.addOrReplaceChild("body15",
				CubeListBuilder.create().mirror().texOffs(0, 25).addBox(-0.5F, -3.5F, -3.0F, 1.0F, 4.0F, 3.0F),
				PartPose.offset(2.48F, -12.5F, 1.5F));

		PartDefinition body16 = body15.addOrReplaceChild("body16",
				CubeListBuilder.create().mirror().texOffs(8, 25).addBox(-0.5F, -1.0F, -4.0F, 1.0F, 1.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.61086524F, 0.0F, 0.0F));

		body16.addOrReplaceChild("body17",
				CubeListBuilder.create().mirror().texOffs(6, 25).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, -0.8F, -2.0F));

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -16.5F, -1.0F, 1.0F, 16.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body2.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -16.5F, -1.0F, 1.0F, 16.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body4 = body1.addOrReplaceChild("body4",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(0.0F, -16.5F, -1.0F, 1.0F, 16.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(0.0F, -16.5F, -1.0F, 1.0F, 16.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body18 = body1.addOrReplaceChild("body18",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -16.0F, 2.76F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body19 = body18.addOrReplaceChild("body19",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -16.5F, -1.0F, 1.0F, 16.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));

		body19.addOrReplaceChild("body20",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -16.5F, -1.0F, 1.0F, 16.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body21 = body18.addOrReplaceChild("body21",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(0.0F, -16.5F, -1.0F, 1.0F, 16.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));

		body21.addOrReplaceChild("body22",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(0.0F, -16.5F, -1.0F, 1.0F, 16.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition emitter1_1 = main.addOrReplaceChild("emitter1_1",
				CubeListBuilder.create().texOffs(12, 19).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.offset(0.0F, -15.4F, 0.0F));

		emitter1_1.addOrReplaceChild("emitter1_2",
				CubeListBuilder.create().texOffs(12, 19).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));

		emitter1_1.addOrReplaceChild("emitter1_3",
				CubeListBuilder.create().texOffs(12, 19).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));

		emitter1_1.addOrReplaceChild("emitter1_4",
				CubeListBuilder.create().texOffs(12, 19).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));

		emitter1_1.addOrReplaceChild("emitter1_5",
				CubeListBuilder.create().texOffs(12, 19).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		emitter1_1.addOrReplaceChild("emitter1_6",
				CubeListBuilder.create().texOffs(12, 19).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));

		emitter1_1.addOrReplaceChild("emitter1_7",
				CubeListBuilder.create().texOffs(12, 19).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));

		emitter1_1.addOrReplaceChild("emitter1_8",
				CubeListBuilder.create().texOffs(12, 19).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}