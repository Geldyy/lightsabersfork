package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class MandalorianSetModel extends HiltSetModel {

	private final ModelPart pommel;
	private final ModelPart body;
	private final ModelPart switchSection;
	private final ModelPart emitter;

	public MandalorianSetModel() {
		super("mandalorian");

		this.pommel = createPommel().bakeRoot();

		this.body = createBody().bakeRoot();

		this.switchSection = createSwitch().bakeRoot();
		if (this.switchSection.hasChild("top1")) {
			ModelPart p = this.switchSection.getChild("top1");
			p.xScale = 0.92F;
			p.yScale = 1.0F;
			p.zScale = 0.9F;
		}

		this.emitter = createEmitter().bakeRoot();
		if (this.emitter.hasChild("peg1")) {
			ModelPart p = this.emitter.getChild("peg1");
			p.xScale = 0.9F;
			p.yScale = 0.5F;
			p.zScale = 1.0F;
		}
		if (this.emitter.hasChild("body1")) {
			ModelPart p = this.emitter.getChild("body1");
			p.xScale = 0.92F;
			p.yScale = 0.85F;
			p.zScale = 0.9F;
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
		case POMMEL -> 7.45F;
		case BODY -> 26.0F;
		case SWITCH_SECTION -> 1.0F;
		case EMITTER -> 13.0F;
		};
	}

	private static LayerDefinition createPommel() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 4.0F, 1.0F),
				PartPose.offset(0.0F, 3.45F, 0.0F));

		PartDefinition body8 = body1.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 4.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		body1.addOrReplaceChild("body7",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(1.98F, 0.0F, -2.5F, 1.0F, 4.0F, 5.0F),
				PartPose.ZERO);

		body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(0, 0).addBox(-2.98F, 0.0F, -2.5F, 1.0F, 4.0F, 5.0F), PartPose.ZERO);

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.0F, 3.76F, 0.0F, -0.46949357F, 0.0F));
		body2.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body4 = body1.addOrReplaceChild("body4",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(0.0F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.0F, 3.76F, 0.0F, 0.46949357F, 0.0F));
		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(0.0F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body9 = body8.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.0F, 3.76F, 0.0F, -0.46949357F, 0.0F));
		body9.addOrReplaceChild("body10",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body11 = body8.addOrReplaceChild("body11",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(0.0F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.0F, 3.76F, 0.0F, 0.46949357F, 0.0F));
		body11.addOrReplaceChild("body12",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(0.0F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body13 = root.addOrReplaceChild("body13",
				CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, 0.0F, 1.76F, 3.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, 2.45F, 0.0F));

		body13.addOrReplaceChild("body18",
				CubeListBuilder.create().texOffs(0, 14).addBox(-2.98F, 0.0F, -2.5F, 3.0F, 1.0F, 5.0F), PartPose.ZERO);
		body13.addOrReplaceChild("body19",
				CubeListBuilder.create().mirror().texOffs(0, 14).addBox(-0.02F, 0.0F, -2.5F, 3.0F, 1.0F, 5.0F),
				PartPose.ZERO);

		PartDefinition body20 = body13.addOrReplaceChild("body20",
				CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, 0.0F, 1.76F, 3.0F, 1.0F, 2.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body14 = body13.addOrReplaceChild("body14",
				CubeListBuilder.create().texOffs(11, 12).addBox(-1.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));
		body14.addOrReplaceChild("body15",
				CubeListBuilder.create().texOffs(11, 12).addBox(-1.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body16 = body13.addOrReplaceChild("body16",
				CubeListBuilder.create().mirror().texOffs(11, 12).addBox(0.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));
		body16.addOrReplaceChild("body17",
				CubeListBuilder.create().mirror().texOffs(11, 12).addBox(0.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body21 = body20.addOrReplaceChild("body21",
				CubeListBuilder.create().texOffs(11, 12).addBox(-1.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(-1.5F, 0.5F, 3.76F, 0.0F, -0.46949357F, 0.0F));
		body21.addOrReplaceChild("body22",
				CubeListBuilder.create().texOffs(11, 12).addBox(-1.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body23 = body20.addOrReplaceChild("body23",
				CubeListBuilder.create().mirror().texOffs(11, 12).addBox(0.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(1.5F, 0.5F, 3.76F, 0.0F, 0.46949357F, 0.0F));
		body23.addOrReplaceChild("body24",
				CubeListBuilder.create().mirror().texOffs(11, 12).addBox(0.0F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body25 = root.addOrReplaceChild("body25",
				CubeListBuilder.create().texOffs(8, 5).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 4.0F),
				PartPose.offset(0.0F, 0.0F, -0.75F));
		body25.addOrReplaceChild("body26",
				CubeListBuilder.create().texOffs(7, 0).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 2.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, 3.0F, 2.0F, 0.9599311F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createBody() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 26.0F, 1.0F), PartPose.ZERO);

		body1.addOrReplaceChild("body8",
				CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 26.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		body1.addOrReplaceChild("body7",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(1.98F, 0.0F, -2.5F, 1.0F, 26.0F, 5.0F),
				PartPose.ZERO);

		body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(0, 0).addBox(-2.98F, 0.0F, -2.5F, 1.0F, 26.0F, 5.0F), PartPose.ZERO);

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 26.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.0F, 3.76F, 0.0F, -0.46949357F, 0.0F));
		body2.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 26.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body4 = body1.addOrReplaceChild("body4",
				CubeListBuilder.create().mirror().texOffs(20, 0).addBox(0.0F, 0.0F, -1.0F, 1.0F, 26.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.0F, 3.76F, 0.0F, 0.46949357F, 0.0F));
		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().mirror().texOffs(20, 0).addBox(0.0F, 0.0F, -1.0F, 1.0F, 26.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body8 = body1.getChild("body8");
		PartDefinition body9 = body8.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 26.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.0F, 3.76F, 0.0F, -0.46949357F, 0.0F));
		body9.addOrReplaceChild("body10",
				CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 26.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body11 = body8.addOrReplaceChild("body11",
				CubeListBuilder.create().mirror().texOffs(20, 0).addBox(0.0F, 0.0F, -1.0F, 1.0F, 26.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.0F, 3.76F, 0.0F, 0.46949357F, 0.0F));
		body11.addOrReplaceChild("body12",
				CubeListBuilder.create().mirror().texOffs(20, 0).addBox(0.0F, 0.0F, -1.0F, 1.0F, 26.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createSwitch() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(18, 9).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 1.0F, 1.0F), PartPose.ZERO);

		PartDefinition body10 = body1.addOrReplaceChild("body10",
				CubeListBuilder.create().texOffs(14, 6).addBox(-1.5F, 0.0F, 2.76F, 3.0F, 2.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body6 = body1.addOrReplaceChild("body6",
				CubeListBuilder.create().texOffs(0, 11).addBox(-2.98F, 0.0F, -2.5F, 1.0F, 2.0F, 2.0F), PartPose.ZERO);
		body6.addOrReplaceChild("body7",
				CubeListBuilder.create().texOffs(1, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 3.0F),
				PartPose.offset(-2.48F, 0.0F, -0.5F));

		PartDefinition body8 = body1.addOrReplaceChild("body8",
				CubeListBuilder.create().mirror().texOffs(0, 11).addBox(1.98F, 0.0F, -2.5F, 1.0F, 2.0F, 2.0F),
				PartPose.ZERO);
		body8.addOrReplaceChild("body9",
				CubeListBuilder.create().mirror().texOffs(1, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 3.0F),
				PartPose.offset(2.48F, 0.0F, -0.5F));

		PartDefinition body2 = body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.0F, 3.76F, 0.0F, -0.46949357F, 0.0F));
		body2.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body4 = body1.addOrReplaceChild("body4",
				CubeListBuilder.create().mirror().texOffs(0, 19).addBox(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.0F, 3.76F, 0.0F, 0.46949357F, 0.0F));
		body4.addOrReplaceChild("body5",
				CubeListBuilder.create().mirror().texOffs(0, 19).addBox(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body11 = body10.addOrReplaceChild("body11",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.5F, 0.0F, 3.76F, 0.0F, -0.46949357F, 0.0F));
		body11.addOrReplaceChild("body12",
				CubeListBuilder.create().texOffs(0, 15).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -0.47211155F, 0.0F));

		PartDefinition body13 = body10.addOrReplaceChild("body13",
				CubeListBuilder.create().mirror().texOffs(0, 15).addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.5F, 0.0F, 3.76F, 0.0F, 0.46949357F, 0.0F));
		body13.addOrReplaceChild("body14",
				CubeListBuilder.create().mirror().texOffs(0, 15).addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.47211155F, 0.0F));

		PartDefinition body15 = body1.addOrReplaceChild("body15",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, 0.0F, -7.5F, 9.0F, 1.0F, 5.0F),
				PartPose.offsetAndRotation(0.0F, 0.9F, 0.0F, -0.2617994F, 0.0F, 0.0F));
		body15.addOrReplaceChild("body16",
				CubeListBuilder.create().texOffs(0, 22).addBox(-4.5F, 0.0F, 0.0F, 9.0F, 3.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -7.5F, 0.2617994F, 0.0F, 0.0F));
		body15.addOrReplaceChild("body17",
				CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, 0.0F, 5.5F, 5.0F, 1.0F, 4.0F),
				PartPose.offset(0.0F, 0.5F, -2.5F));
		body15.addOrReplaceChild("body18",
				CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 1.0F, 10.0F),
				PartPose.offset(3.5F, 0.5F, -2.5F));
		body15.addOrReplaceChild("body19",
				CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 1.0F, 10.0F),
				PartPose.offset(-3.5F, 0.5F, -2.5F));

		PartDefinition top1 = root.addOrReplaceChild("top1",
				CubeListBuilder.create().texOffs(14, 11).addBox(-3.0F, 0.0F, 3.0F, 6.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, 0.87F, 0.2F));

		top1.addOrReplaceChild("top2",
				CubeListBuilder.create().texOffs(14, 14).addBox(-3.0F, 0.0F, 3.0F, 6.0F, 1.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition top3 = top1.addOrReplaceChild("top3",
				CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, 0.0F, -6.0F, 1.0F, 1.0F, 6.0F),
				PartPose.offset(-2.0F, 0.0F, 3.0F));
		top3.addOrReplaceChild("top4",
				CubeListBuilder.create().texOffs(14, 16).addBox(-0.4F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		top3.addOrReplaceChild("top5",
				CubeListBuilder.create().texOffs(25, 16).addBox(-0.4F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -5.6F, 0.0F, -0.7853982F, 0.0F));
		top3.addOrReplaceChild("top6",
				CubeListBuilder.create().texOffs(20, 16).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 1.0F, 3.0F), PartPose.ZERO);

		PartDefinition top7 = top1.addOrReplaceChild("top7",
				CubeListBuilder.create().mirror().texOffs(22, 0).addBox(0.0F, 0.0F, -6.0F, 1.0F, 1.0F, 6.0F),
				PartPose.offset(2.0F, 0.0F, 3.0F));
		top7.addOrReplaceChild("top8",
				CubeListBuilder.create().mirror().texOffs(14, 16).addBox(-0.6F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));
		top7.addOrReplaceChild("top9",
				CubeListBuilder.create().mirror().texOffs(25, 16).addBox(-0.6F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -5.6F, 0.0F, 0.7853982F, 0.0F));
		top7.addOrReplaceChild("top10",
				CubeListBuilder.create().mirror().texOffs(20, 16).addBox(0.0F, 0.0F, -3.0F, 1.0F, 1.0F, 3.0F),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static LayerDefinition createEmitter() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition peg1 = root.addOrReplaceChild("peg1",
				CubeListBuilder.create().texOffs(14, 15).addBox(-3.5F, 0.0F, -3.0F, 7.0F, 1.0F, 3.0F),
				PartPose.offset(0.0F, 0.93F, -1.1F));

		PartDefinition prevPeg = peg1;
		for (int i = 2; i <= 10; i++) {
			prevPeg = prevPeg.addOrReplaceChild("peg" + i,
					CubeListBuilder.create().texOffs(14, 15).addBox(-3.5F, 0.0F, -3.0F, 7.0F, 1.0F, 3.0F),
					PartPose.offset(0.0F, 2.0F, 0.0F));
		}

		PartDefinition body1 = root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, 0.0F, 3.0F, 6.0F, 13.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 0.2F));

		body1.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, 0.0F, 3.0F, 6.0F, 13.0F, 1.0F),
				PartPose.rotation(0.0F, 3.1415927F, 0.0F));

		PartDefinition body3 = body1.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -6.0F, 1.0F, 13.0F, 6.0F),
				PartPose.offset(-2.0F, 0.0F, 3.0F));
		body3.addOrReplaceChild("body4",
				CubeListBuilder.create().texOffs(14, 0).addBox(-0.4F, 0.0F, -1.0F, 1.0F, 13.0F, 2.0F),
				PartPose.rotation(0.0F, 0.7853982F, 0.0F));
		body3.addOrReplaceChild("body5",
				CubeListBuilder.create().texOffs(14, 0).addBox(-0.4F, 0.0F, -1.0F, 1.0F, 13.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, -0.7853982F, 0.0F));

		PartDefinition body6 = body1.addOrReplaceChild("body6",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(0.0F, 0.0F, -6.0F, 1.0F, 13.0F, 6.0F),
				PartPose.offset(2.0F, 0.0F, 3.0F));
		body6.addOrReplaceChild("body7",
				CubeListBuilder.create().mirror().texOffs(14, 0).addBox(-0.6F, 0.0F, -1.0F, 1.0F, 13.0F, 2.0F),
				PartPose.rotation(0.0F, -0.7853982F, 0.0F));
		body6.addOrReplaceChild("body8",
				CubeListBuilder.create().mirror().texOffs(14, 0).addBox(-0.6F, 0.0F, -1.0F, 1.0F, 13.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 0.7853982F, 0.0F));

		PartDefinition body9 = body1.addOrReplaceChild("body9",
				CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 3.0F, 2.0F),
				PartPose.offset(-2.5F, 12.72F, 3.5F));
		body9.addOrReplaceChild("body13",
				CubeListBuilder.create().texOffs(8, 2).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition body10 = body9.addOrReplaceChild("body10",
				CubeListBuilder.create().texOffs(12, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 3.0F, 0.5F, -2.016728F, 0.0F, 0.0F));
		PartDefinition body11 = body10.addOrReplaceChild("body11",
				CubeListBuilder.create().texOffs(0, 19).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 2.0F),
				PartPose.offset(0.0F, 1.0F, -0.5F));
		body11.addOrReplaceChild("body12",
				CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 6.39F, 0.0F));

		PartDefinition body14 = body1.addOrReplaceChild("body14",
				CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 3.0F, 2.0F),
				PartPose.offset(2.5F, 12.72F, 3.5F));
		body14.addOrReplaceChild("body18",
				CubeListBuilder.create().mirror().texOffs(8, 2).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition body15 = body14.addOrReplaceChild("body15",
				CubeListBuilder.create().mirror().texOffs(12, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 3.0F, 0.5F, -2.016728F, 0.0F, 0.0F));
		PartDefinition body16 = body15.addOrReplaceChild("body16",
				CubeListBuilder.create().mirror().texOffs(0, 19).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 2.0F),
				PartPose.offset(0.0F, 1.0F, -0.5F));
		body16.addOrReplaceChild("body17",
				CubeListBuilder.create().mirror().texOffs(8, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 6.39F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}