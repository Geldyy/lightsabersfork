package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class JuggernautSetModel extends HiltSetModel {

	private static final float[] OCTAGON_ANGLES = { 0.0F, 0.7853982F, 1.5707964F, 2.3561945F, 3.1415927F, -2.3561945F,
			-1.5707964F, -0.7853982F };

	private final ModelPart pommel;
	private final ModelPart body;
	private final ModelPart switchSection;
	private final ModelPart emitter;

	public JuggernautSetModel() {
		super("juggernaut");

		this.pommel = createPommel().bakeRoot();

		this.body = createBody().bakeRoot();
		if (this.body.hasChild("main")) {
			ModelPart main = this.body.getChild("main");
			if (main.hasChild("body1")) {
				ModelPart p = main.getChild("body1");
				p.xScale = 0.8F;
				p.yScale = 1.0F;
				p.zScale = 0.8F;
			}
		}

		this.switchSection = createSwitch().bakeRoot();
		if (this.switchSection.hasChild("main")) {
			ModelPart main = this.switchSection.getChild("main");
			if (main.hasChild("body1")) {
				ModelPart p = main.getChild("body1");
				p.xScale = 1.1F;
				p.yScale = 1.1F;
				p.zScale = 1.1F;
			}
			if (main.hasChild("upperBody1")) {
				ModelPart p = main.getChild("upperBody1");
				p.xScale = 0.6F;
				p.yScale = 0.6F;
				p.zScale = 0.6F;
			}
			if (main.hasChild("top1")) {
				ModelPart p = main.getChild("top1");
				p.xScale = 1.1F;
				p.yScale = 1.1F;
				p.zScale = 1.1F;
			}
			if (main.hasChild("switchButton1")) {
				ModelPart p = main.getChild("switchButton1");
				p.xScale = 1.1F;
				p.yScale = 0.9F;
				p.zScale = 0.8F;
			}
		}

		this.emitter = createEmitter().bakeRoot();
		if (this.emitter.hasChild("main")) {
			ModelPart main = this.emitter.getChild("main");
			if (main.hasChild("frontButton1")) {
				ModelPart p = main.getChild("frontButton1");
				p.xScale = 0.3F;
				p.yScale = 0.3F;
				p.zScale = 0.3F;
			}
			if (main.hasChild("frontButtonRing1")) {
				ModelPart p = main.getChild("frontButtonRing1");
				p.xScale = 0.38F;
				p.yScale = 0.38F;
				p.zScale = 0.38F;
			}
			if (main.hasChild("ring1_1")) {
				ModelPart p = main.getChild("ring1_1");
				p.xScale = 0.8F;
				p.yScale = 0.8F;
				p.zScale = 0.73F;
			}
			if (main.hasChild("ring2_1")) {
				ModelPart p = main.getChild("ring2_1");
				p.xScale = 0.9F;
				p.yScale = 0.9F;
				p.zScale = 1.0F;
			}
			if (main.hasChild("ring3_1")) {
				ModelPart p = main.getChild("ring3_1");
				p.xScale = 0.8F;
				p.yScale = 0.8F;
				p.zScale = 0.73F;
			}
			if (main.hasChild("ring4_1")) {
				ModelPart p = main.getChild("ring4_1");
				p.xScale = 0.6F;
				p.yScale = 0.6F;
				p.zScale = 0.6F;
			}
			if (main.hasChild("leftGuardEdge1")) {
				ModelPart p = main.getChild("leftGuardEdge1");
				p.xScale = 1.0F;
				p.yScale = 1.0F;
				p.zScale = 1.2F;
			}
			if (main.hasChild("rightGuardEdge1")) {
				ModelPart p = main.getChild("rightGuardEdge1");
				p.xScale = 1.0F;
				p.yScale = 1.0F;
				p.zScale = 1.2F;
			}
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
		case POMMEL -> 5.0F;
		case BODY -> 16.0F;
		case SWITCH_SECTION -> 10.0F;
		case EMITTER -> 14.5F;
		};
	}

	private static LayerDefinition createPommel() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, (float) Math.PI, 1.5707964F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 5.0F, 4.0F), PartPose.ZERO);

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 5.0F, 4.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		body2.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 5.0F, 3.62F, -1.0471976F, 0.0F, 0.0F));

		PartDefinition body4 = body1.addOrReplaceChild("body4",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 5.0F, 4.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 5.0F, 3.62F, -1.0471976F, 0.0F, 0.0F));
		PartDefinition body6 = body4.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(14, 0).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 6.0F, 1.0F),
				PartPose.offset(0.0F, 3.0F, 3.12F));
		body6.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(14, 7).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 6.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 0.5F));

		PartDefinition body8 = body1.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 5.0F, 4.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		body8.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 5.0F, 3.62F, -1.0471976F, 0.0F, 0.0F));

		PartDefinition body10 = body1.addOrReplaceChild("body10",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 5.0F, 4.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		body10.addOrReplaceChild("body11",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 5.0F, 3.62F, -1.0471976F, 0.0F, 0.0F));
		PartDefinition body12 = body10.addOrReplaceChild("body12",
				CubeListBuilder.create().texOffs(14, 0).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 6.0F, 1.0F),
				PartPose.offset(0.0F, 3.0F, 3.12F));
		body12.addOrReplaceChild("body13",
				CubeListBuilder.create().texOffs(14, 7).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 6.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 0.5F));

		PartDefinition body14 = body1.addOrReplaceChild("body14",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 5.0F, 4.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		body14.addOrReplaceChild("body15",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 5.0F, 3.62F, -1.0471976F, 0.0F, 0.0F));

		PartDefinition body16 = body1.addOrReplaceChild("body16",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 5.0F, 4.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		body16.addOrReplaceChild("body17",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 5.0F, 3.62F, -1.0471976F, 0.0F, 0.0F));
		PartDefinition body18 = body16.addOrReplaceChild("body18",
				CubeListBuilder.create().texOffs(14, 0).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 6.0F, 1.0F),
				PartPose.offset(0.0F, 3.0F, 3.12F));
		body18.addOrReplaceChild("body19",
				CubeListBuilder.create().texOffs(14, 7).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 6.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 0.5F));

		PartDefinition body20 = body1.addOrReplaceChild("body20",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 5.0F, 4.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));
		body20.addOrReplaceChild("body21",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 5.0F, 3.62F, -1.0471976F, 0.0F, 0.0F));

		body1.addOrReplaceChild("body22",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 5.0F, 3.62F, -1.0471976F, 0.0F, 0.0F));
		PartDefinition body23 = body1.addOrReplaceChild("body23",
				CubeListBuilder.create().texOffs(14, 0).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 6.0F, 1.0F),
				PartPose.offset(0.0F, 3.0F, 3.12F));
		body23.addOrReplaceChild("body24",
				CubeListBuilder.create().texOffs(14, 7).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 6.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 0.5F));

		PartDefinition cap1 = main.addOrReplaceChild("cap1",
				CubeListBuilder.create().texOffs(0, 14).addBox(-3.5F, 0.0F, -1.0F, 7.0F, 2.0F, 2.0F),
				PartPose.offset(0.0F, 4.7F, 0.0F));
		cap1.addOrReplaceChild("cap2",
				CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, 0.0F, -0.5F, 8.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 1.3F, 0.0F));
		PartDefinition cap3 = cap1.addOrReplaceChild("cap3",
				CubeListBuilder.create().texOffs(0, 14).addBox(-3.5F, 0.0F, -1.0F, 7.0F, 2.0F, 2.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		cap3.addOrReplaceChild("cap4",
				CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, 0.0F, -0.5F, 8.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 1.3F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createBody() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, (float) Math.PI, 1.5707964F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.ZERO);
		body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		body1.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		body1.addOrReplaceChild("body4",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		body1.addOrReplaceChild("body5",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		body1.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		body1.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition body9 = body1.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.0F, 3.52F, 1.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 0.8976228F, 0.0F));
		PartDefinition body10 = body9.addOrReplaceChild("body10",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.0F, 3.52F, 1.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 0.8976228F, 0.0F));
		PartDefinition body11 = body10.addOrReplaceChild("body11",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.0F, 3.52F, 1.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 0.8976228F, 0.0F));
		PartDefinition body12 = body11.addOrReplaceChild("body12",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.0F, 3.52F, 1.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 0.8976228F, 0.0F));
		PartDefinition body13 = body12.addOrReplaceChild("body13",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.0F, 3.52F, 1.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 0.8976228F, 0.0F));
		PartDefinition body14 = body13.addOrReplaceChild("body14",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.0F, 3.52F, 1.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 0.8976228F, 0.0F));
		body14.addOrReplaceChild("body15",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.0F, 3.52F, 1.0F, 16.0F, 1.0F),
				PartPose.rotation(0.0F, 0.8976228F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createSwitch() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.rotation((float) Math.PI, 1.5707964F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 4.0F, 2.0F), PartPose.ZERO);
		body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 4.0F, 2.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		body1.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 4.0F, 2.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		body1.addOrReplaceChild("body4",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 4.0F, 2.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		body1.addOrReplaceChild("body5",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 4.0F, 2.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 4.0F, 2.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		body1.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 4.0F, 2.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		body1.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 4.0F, 2.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition upperBody1 = main.addOrReplaceChild("upperBody1",
				CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.offset(0.0F, -4.4F, 0.0F));
		upperBody1.addOrReplaceChild("upperBody2",
				CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		upperBody1.addOrReplaceChild("upperBody3",
				CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		upperBody1.addOrReplaceChild("upperBody4",
				CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		upperBody1.addOrReplaceChild("upperBody5",
				CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		upperBody1.addOrReplaceChild("upperBody6",
				CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		upperBody1.addOrReplaceChild("upperBody7",
				CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		upperBody1.addOrReplaceChild("upperBody8",
				CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));

		PartDefinition ring1 = main.addOrReplaceChild("ring1",
				CubeListBuilder.create().texOffs(0, 12).addBox(-1.5F, -1.0F, 1.62F, 3.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, -6.9F, 0.0F));
		for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
			ring1.addOrReplaceChild("ring" + (i + 1),
					CubeListBuilder.create().texOffs(0, 12).addBox(-1.5F, -1.0F, 1.62F, 3.0F, 1.0F, 2.0F),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition top1 = main.addOrReplaceChild("top1",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 2.0F, 2.0F),
				PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition top2 = top1.addOrReplaceChild("top2",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 2.0F, 2.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		top2.addOrReplaceChild("top3",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 3.62F, 0.7853982F, 0.0F, 0.0F));

		PartDefinition top4 = top1.addOrReplaceChild("top4",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 2.0F, 2.0F),
				PartPose.rotation(0.0F, 1.5707964F, 0.0F));
		top4.addOrReplaceChild("top5",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 3.62F, 0.7853982F, 0.0F, 0.0F));

		PartDefinition top6 = top1.addOrReplaceChild("top6",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 2.0F, 2.0F),
				PartPose.rotation(0.0F, 2.3561945F, 0.0F));
		top6.addOrReplaceChild("top7",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 3.62F, 0.7853982F, 0.0F, 0.0F));

		PartDefinition top8 = top1.addOrReplaceChild("top8",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 2.0F, 2.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));
		top8.addOrReplaceChild("top9",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 3.62F, 0.7853982F, 0.0F, 0.0F));

		PartDefinition top10 = top1.addOrReplaceChild("top10",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 2.0F, 2.0F),
				PartPose.rotation(0.0F, -2.3561945F, 0.0F));
		top10.addOrReplaceChild("top11",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 3.62F, 0.7853982F, 0.0F, 0.0F));

		PartDefinition top12 = top1.addOrReplaceChild("top12",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 2.0F, 2.0F),
				PartPose.rotation(0.0F, -1.5707964F, 0.0F));
		top12.addOrReplaceChild("top13",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 3.62F, 0.7853982F, 0.0F, 0.0F));

		PartDefinition top14 = top1.addOrReplaceChild("top14",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -4.0F, 1.62F, 3.0F, 2.0F, 2.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));
		top14.addOrReplaceChild("top15",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 3.62F, 0.7853982F, 0.0F, 0.0F));

		top1.addOrReplaceChild("top16",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 3.62F, 0.7853982F, 0.0F, 0.0F));

		main.addOrReplaceChild("switchPlate",
				CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(3.98F, -2.2F, 0.0F, 0.0F, 1.5707964F, 0.0F));

		PartDefinition switchButton1 = main.addOrReplaceChild("switchButton1",
				CubeListBuilder.create().texOffs(9, 5).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(4.18F, -3.6F, 0.0F, 0.0F, 1.5707964F, 0.0F));
		PartDefinition switchButton2 = switchButton1.addOrReplaceChild("switchButton2",
				CubeListBuilder.create().texOffs(9, 5).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 1.2F, 0.0F));
		PartDefinition switchButton3 = switchButton2.addOrReplaceChild("switchButton3",
				CubeListBuilder.create().texOffs(9, 5).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 1.2F, 0.0F));
		switchButton3.addOrReplaceChild("switchButton4",
				CubeListBuilder.create().texOffs(9, 5).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 1.2F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createEmitter() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.rotation((float) Math.PI, 1.5707964F, 0.0F));

		PartDefinition body1 = main.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -9.0F, 2.62F, 3.0F, 9.0F, 1.0F), PartPose.ZERO);
		for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
			body1.addOrReplaceChild("body" + (i + 1),
					CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -9.0F, 2.62F, 3.0F, 9.0F, 1.0F),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition ring1_1 = main.addOrReplaceChild("ring1_1",
				CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, -9.0F, 0.0F));
		for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
			ring1_1.addOrReplaceChild("ring1_" + (i + 1),
					CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition ring2_1 = main.addOrReplaceChild("ring2_1",
				CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, -10.2F, 0.0F));
		for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
			ring2_1.addOrReplaceChild("ring2_" + (i + 1),
					CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition ring3_1 = main.addOrReplaceChild("ring3_1",
				CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, -11.9F, 0.0F));
		for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
			ring3_1.addOrReplaceChild("ring3_" + (i + 1),
					CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition ring4_1 = main.addOrReplaceChild("ring4_1",
				CubeListBuilder.create().texOffs(0, 18).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, -12.5F, 0.0F));
		for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
			ring4_1.addOrReplaceChild("ring4_" + (i + 1),
					CubeListBuilder.create().texOffs(0, 18).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition leftGuard1 = main.addOrReplaceChild("leftGuard1",
				CubeListBuilder.create().mirror().texOffs(12, 0).addBox(0.0F, -23.0F, -0.5F, 1.0F, 23.0F, 1.0F),
				PartPose.offset(3.62F, -4.4F, 0.0F));
		PartDefinition leftGuard2 = leftGuard1.addOrReplaceChild("leftGuard2",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(0.0F, -20.0F, -0.5F, 1.0F, 20.0F, 1.0F),
				PartPose.offset(1.0F, -2.6F, 0.0F));
		PartDefinition leftGuard3 = leftGuard2.addOrReplaceChild("leftGuard3",
				CubeListBuilder.create().mirror().texOffs(16, 0).addBox(0.0F, -16.0F, -0.5F, 2.0F, 16.0F, 1.0F),
				PartPose.offset(1.0F, -1.75F, 0.0F));
		leftGuard3.addOrReplaceChild("leftGuard4",
				CubeListBuilder.create().mirror().texOffs(22, 0).addBox(0.0F, -14.0F, -0.5F, 1.0F, 14.0F, 1.0F),
				PartPose.offset(2.0F, -0.58F, 0.0F));
		leftGuard3.addOrReplaceChild("leftGuard5",
				CubeListBuilder.create().mirror().texOffs(20, 21).addBox(0.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -17.3F, 0.0F, 0.0F, 0.0F, 0.715585F));

		PartDefinition leftGuardEdge1 = main.addOrReplaceChild("leftGuardEdge1",
				CubeListBuilder.create().mirror().texOffs(20, 23).addBox(-1.0F, -7.0F, -0.5F, 1.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(3.62F, -1.5F, 0.0F, 0.0F, 0.0F, 0.34906584F));
		PartDefinition leftGuardEdge2 = leftGuardEdge1.addOrReplaceChild("leftGuardEdge2",
				CubeListBuilder.create().mirror().texOffs(24, 16).addBox(0.0F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, -7.0F, 0.0F, 0.0F, 0.0F, 0.6981317F));
		PartDefinition leftGuardEdge3 = leftGuardEdge2.addOrReplaceChild("leftGuardEdge3",
				CubeListBuilder.create().mirror().texOffs(16, 17).addBox(-1.0F, -13.0F, -0.5F, 1.0F, 13.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, -4.0F, 0.0F, 0.0F, 0.0F, -1.0471976F));
		PartDefinition leftGuardEdge4 = leftGuardEdge3.addOrReplaceChild("leftGuardEdge4",
				CubeListBuilder.create().mirror().texOffs(20, 17).addBox(-1.0F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.0F, 0.0F, -0.40142572F));
		PartDefinition leftGuardEdge5 = leftGuardEdge4.addOrReplaceChild("leftGuardEdge5",
				CubeListBuilder.create().mirror().texOffs(24, 23).addBox(-1.0F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.41887903F));
		PartDefinition leftGuardEdge6 = leftGuardEdge5.addOrReplaceChild("leftGuardEdge6",
				CubeListBuilder.create().mirror().texOffs(24, 26).addBox(-1.0F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.33161256F));
		leftGuardEdge6.addOrReplaceChild("leftGuardEdge7",
				CubeListBuilder.create().mirror().texOffs(24, 29).addBox(-1.0F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.41887903F));

		PartDefinition rightGuard1 = main.addOrReplaceChild("rightGuard1",
				CubeListBuilder.create().texOffs(12, 0).addBox(-1.0F, -23.0F, -0.5F, 1.0F, 23.0F, 1.0F),
				PartPose.offset(-3.62F, -4.4F, 0.0F));
		PartDefinition rightGuard2 = rightGuard1.addOrReplaceChild("rightGuard2",
				CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -20.0F, -0.5F, 1.0F, 20.0F, 1.0F),
				PartPose.offset(-1.0F, -2.6F, 0.0F));
		PartDefinition rightGuard3 = rightGuard2.addOrReplaceChild("rightGuard3",
				CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -16.0F, -0.5F, 2.0F, 16.0F, 1.0F),
				PartPose.offset(-1.0F, -1.75F, 0.0F));
		rightGuard3.addOrReplaceChild("rightGuard4",
				CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -14.0F, -0.5F, 1.0F, 14.0F, 1.0F),
				PartPose.offset(-2.0F, -0.58F, 0.0F));
		rightGuard3.addOrReplaceChild("rightGuard5",
				CubeListBuilder.create().texOffs(20, 21).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -17.3F, 0.0F, 0.0F, 0.0F, -0.715585F));

		PartDefinition rightGuardEdge1 = main.addOrReplaceChild("rightGuardEdge1",
				CubeListBuilder.create().texOffs(20, 23).addBox(0.0F, -7.0F, -0.5F, 1.0F, 7.0F, 1.0F),
				PartPose.offsetAndRotation(-3.62F, -1.5F, 0.0F, 0.0F, 0.0F, -0.34906584F));
		PartDefinition rightGuardEdge2 = rightGuardEdge1.addOrReplaceChild("rightGuardEdge2",
				CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, -7.0F, 0.0F, 0.0F, 0.0F, -0.6981317F));
		PartDefinition rightGuardEdge3 = rightGuardEdge2.addOrReplaceChild("rightGuardEdge3",
				CubeListBuilder.create().texOffs(16, 17).addBox(0.0F, -13.0F, -0.5F, 1.0F, 13.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, -4.0F, 0.0F, 0.0F, 0.0F, 1.0471976F));
		PartDefinition rightGuardEdge4 = rightGuardEdge3.addOrReplaceChild("rightGuardEdge4",
				CubeListBuilder.create().texOffs(20, 17).addBox(0.0F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.40142572F));
		PartDefinition rightGuardEdge5 = rightGuardEdge4.addOrReplaceChild("rightGuardEdge5",
				CubeListBuilder.create().texOffs(24, 23).addBox(0.0F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.41887903F));
		PartDefinition rightGuardEdge6 = rightGuardEdge5.addOrReplaceChild("rightGuardEdge6",
				CubeListBuilder.create().texOffs(24, 26).addBox(0.0F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.33161256F));
		rightGuardEdge6.addOrReplaceChild("rightGuardEdge7",
				CubeListBuilder.create().texOffs(24, 29).addBox(0.0F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.41887903F));

		PartDefinition frontButton1 = main.addOrReplaceChild("frontButton1",
				CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, -3.5F, -3.2F, 1.5707964F, 0.0F, 0.0F));
		for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
			frontButton1.addOrReplaceChild("frontButton" + (i + 1),
					CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}

		PartDefinition frontButtonRing1 = main.addOrReplaceChild("frontButtonRing1",
				CubeListBuilder.create().texOffs(26, 0).addBox(-1.5F, -3.0F, -0.38F, 3.0F, 3.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, -3.5F, -2.9F, 1.5707964F, 0.0F, 0.0F));
		for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
			frontButtonRing1.addOrReplaceChild("frontButtonRing" + (i + 1),
					CubeListBuilder.create().texOffs(26, 0).addBox(-1.5F, -3.0F, -0.38F, 3.0F, 3.0F, 4.0F),
					PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
		}

		return LayerDefinition.create(mesh, 64, 32);
	}
}