package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class DroidekaSetModel extends HiltSetModel {
    private final ModelPart pommel;
    private final ModelPart body;
    private final ModelPart switchSection;
    private final ModelPart emitter;

    public DroidekaSetModel() {
        super("droideka");
        this.pommel = createPommelLayer().bakeRoot();
        this.body = createBodyLayer().bakeRoot();
        this.switchSection = createSwitchSectionLayer().bakeRoot();
        this.emitter = createEmitterLayer().bakeRoot();

        // Pommel GL11 Skalierungen
        ModelPart pommelMain = this.pommel.getChild("main");
        pommelMain.getChild("top1_1").xScale = 1.2F;
        pommelMain.getChild("top1_1").yScale = 1.2F;
        pommelMain.getChild("top1_1").zScale = 1.2F;

        pommelMain.getChild("body1").xScale = 1.2F;
        pommelMain.getChild("body1").yScale = 1.2F;
        pommelMain.getChild("body1").zScale = 1.2F;

        pommelMain.getChild("top1").xScale = 0.8F;
        pommelMain.getChild("top1").yScale = 1.0F;
        pommelMain.getChild("top1").zScale = 0.8F;

        // Switch Section GL11 Skalierungen
        ModelPart switchMain = this.switchSection.getChild("main");
        switchMain.getChild("button1").xScale = 0.4F;
        switchMain.getChild("button1").yScale = 0.4F;
        switchMain.getChild("button1").zScale = 0.4F;

        switchMain.getChild("lowerConnector1").xScale = 0.37F;
        switchMain.getChild("lowerConnector1").yScale = 0.37F;
        switchMain.getChild("lowerConnector1").zScale = 0.37F;

        switchMain.getChild("connector1").xScale = 0.6F;
        switchMain.getChild("connector1").yScale = 0.6F;
        switchMain.getChild("connector1").zScale = 0.6F;

        switchMain.getChild("ring1").xScale = 0.8F;
        switchMain.getChild("ring1").yScale = 0.8F;
        switchMain.getChild("ring1").zScale = 0.8F;

        // Body GL11 Skalierungen
        ModelPart bodyMain = this.body.getChild("main");
        bodyMain.getChild("upper2_1").xScale = 0.7F;
        bodyMain.getChild("upper2_1").yScale = 0.7F;
        bodyMain.getChild("upper2_1").zScale = 0.7F;

        bodyMain.getChild("upper6_1").xScale = 0.92F;
        bodyMain.getChild("upper6_1").yScale = 0.5F;
        bodyMain.getChild("upper6_1").zScale = 0.92F;

        bodyMain.getChild("upper3_1").xScale = 1.0F;
        bodyMain.getChild("upper3_1").yScale = 0.5F;
        bodyMain.getChild("upper3_1").zScale = 1.0F;

        bodyMain.getChild("upper5_1").xScale = 0.84F;
        bodyMain.getChild("upper5_1").yScale = 0.5F;
        bodyMain.getChild("upper5_1").zScale = 0.84F;

        bodyMain.getChild("upper1_1").xScale = 0.77F;
        bodyMain.getChild("upper1_1").yScale = 0.5F;
        bodyMain.getChild("upper1_1").zScale = 0.77F;

        bodyMain.getChild("connector1").xScale = 0.6F;
        bodyMain.getChild("connector1").yScale = 0.6F;
        bodyMain.getChild("connector1").zScale = 0.6F;

        bodyMain.getChild("upper4_1").xScale = 1.0F;
        bodyMain.getChild("upper4_1").yScale = 0.5F;
        bodyMain.getChild("upper4_1").zScale = 1.0F;

        // Emitter GL11 Skalierungen
        ModelPart emitterMain = this.emitter.getChild("main");
        emitterMain.getChild("ring1").xScale = 1.2F;
        emitterMain.getChild("ring1").yScale = 1.2F;
        emitterMain.getChild("ring1").zScale = 1.2F;
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
            case POMMEL -> 6.5F;
            case BODY -> 29.4F;
            case SWITCH_SECTION -> 9.6F;
            case EMITTER -> 5.3F;
        };
    }

    public static LayerDefinition createPommelLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 6.5F, 0.0F, (float) Math.PI, 0.0F, 0.0F));

        // top1_1
        PartDefinition top1_1 = main.addOrReplaceChild("top1_1",
                CubeListBuilder.create()
                        .texOffs(4, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.ZERO);

        top1_1.addOrReplaceChild("top2_1",
                CubeListBuilder.create()
                        .texOffs(4, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, 0.7853982F, 0.0F));

        top1_1.addOrReplaceChild("top3_1",
                CubeListBuilder.create()
                        .texOffs(4, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, 1.5707964F, 0.0F));

        top1_1.addOrReplaceChild("top4_1",
                CubeListBuilder.create()
                        .texOffs(4, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, 2.3561945F, 0.0F));

        top1_1.addOrReplaceChild("top5_1",
                CubeListBuilder.create()
                        .texOffs(4, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, 3.1415927F, 0.0F));

        top1_1.addOrReplaceChild("top6_1",
                CubeListBuilder.create()
                        .texOffs(4, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, -2.3561945F, 0.0F));

        top1_1.addOrReplaceChild("top7_1",
                CubeListBuilder.create()
                        .texOffs(4, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        top1_1.addOrReplaceChild("top8_1",
                CubeListBuilder.create()
                        .texOffs(4, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // extrusion1
        PartDefinition extrusion1 = main.addOrReplaceChild("extrusion1",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 2.7F, 0.0F));

        addExtrusion(extrusion1, "extrusion2", "extrusion3", "extrusion4", 0.31415927F);
        addExtrusion(extrusion1, "extrusion5", "extrusion6", "extrusion7", 0.9424778F);
        addExtrusion(extrusion1, "extrusion8", "extrusion9", "extrusion10", 1.5707964F);
        addExtrusion(extrusion1, "extrusion11", "extrusion12", "extrusion13", 2.1991148F);
        addExtrusion(extrusion1, "extrusion14", "extrusion15", "extrusion16", 2.8274333F);
        addExtrusion(extrusion1, "extrusion17", "extrusion18", "extrusion19", 3.455752F);
        addExtrusion(extrusion1, "extrusion20", "extrusion21", "extrusion22", -2.1991148F);
        addExtrusion(extrusion1, "extrusion23", "extrusion24", "extrusion25", -1.5707964F);
        addExtrusion(extrusion1, "extrusion26", "extrusion27", "extrusion28", 2.8274333F);
        addExtrusion(extrusion1, "extrusion29", "extrusion30", "extrusion31", -0.9424778F);
        addExtrusion(extrusion1, "extrusion32", "extrusion33", "extrusion34", -0.31415927F);

        // bottom1
        PartDefinition bottom1 = main.addOrReplaceChild("bottom1",
                CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 5.5F, 0.0F));

        bottom1.addOrReplaceChild("bottom2",
                CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, 0.7853982F, 0.0F));

        bottom1.addOrReplaceChild("bottom3",
                CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, 1.5707964F, 0.0F));

        bottom1.addOrReplaceChild("bottom4",
                CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, 2.3561945F, 0.0F));

        bottom1.addOrReplaceChild("bottom5",
                CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, 3.1415927F, 0.0F));

        bottom1.addOrReplaceChild("bottom6",
                CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, -2.3561945F, 0.0F));

        bottom1.addOrReplaceChild("bottom7",
                CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        bottom1.addOrReplaceChild("bottom8",
                CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F),
                PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // body1
        PartDefinition body1 = main.addOrReplaceChild("body1",
                CubeListBuilder.create()
                        .texOffs(0, 10).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F),
                PartPose.offset(0.0F, 1.2F, 0.0F));

        body1.addOrReplaceChild("body2",
                CubeListBuilder.create()
                        .texOffs(0, 10).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F),
                PartPose.rotation(0.0F, 0.7853982F, 0.0F));

        body1.addOrReplaceChild("body3",
                CubeListBuilder.create()
                        .texOffs(0, 10).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F),
                PartPose.rotation(0.0F, 1.5707964F, 0.0F));

        body1.addOrReplaceChild("body4",
                CubeListBuilder.create()
                        .texOffs(0, 10).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F),
                PartPose.rotation(0.0F, 2.3561945F, 0.0F));

        body1.addOrReplaceChild("body5",
                CubeListBuilder.create()
                        .texOffs(0, 10).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F),
                PartPose.rotation(0.0F, 3.1415927F, 0.0F));

        body1.addOrReplaceChild("body6",
                CubeListBuilder.create()
                        .texOffs(0, 10).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F),
                PartPose.rotation(0.0F, -2.3561945F, 0.0F));

        body1.addOrReplaceChild("body7",
                CubeListBuilder.create()
                        .texOffs(0, 10).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F),
                PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        body1.addOrReplaceChild("body8",
                CubeListBuilder.create()
                        .texOffs(0, 10).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F),
                PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // top1
        PartDefinition top1 = main.addOrReplaceChild("top1",
                CubeListBuilder.create()
                        .texOffs(10, 5).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.ZERO);

        top1.addOrReplaceChild("top2",
                CubeListBuilder.create()
                        .texOffs(10, 5).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.rotation(0.0F, 0.7853982F, 0.0F));

        top1.addOrReplaceChild("top3",
                CubeListBuilder.create()
                        .texOffs(10, 5).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.rotation(0.0F, 1.5707964F, 0.0F));

        top1.addOrReplaceChild("top4",
                CubeListBuilder.create()
                        .texOffs(10, 5).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.rotation(0.0F, 2.3561945F, 0.0F));

        top1.addOrReplaceChild("top5",
                CubeListBuilder.create()
                        .texOffs(10, 5).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.rotation(0.0F, 3.1415927F, 0.0F));

        top1.addOrReplaceChild("top6",
                CubeListBuilder.create()
                        .texOffs(10, 5).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.rotation(0.0F, -2.3561945F, 0.0F));

        top1.addOrReplaceChild("top7",
                CubeListBuilder.create()
                        .texOffs(10, 5).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        top1.addOrReplaceChild("top8",
                CubeListBuilder.create()
                        .texOffs(10, 5).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    private static void addExtrusion(PartDefinition parent, String extName, String topName, String bottomName, float rotY) {
        PartDefinition ext = parent.addOrReplaceChild(extName,
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-0.5F, -1.5F, 4.0F, 1.0F, 3.0F, 1.0F),
                PartPose.rotation(0.0F, rotY, 0.0F));

        ext.addOrReplaceChild(topName,
                CubeListBuilder.create()
                        .texOffs(0, 7).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -1.5F, 5.0F, 0.61086524F, 0.0F, 0.0F));

        ext.addOrReplaceChild(bottomName,
                CubeListBuilder.create()
                        .texOffs(0, 4).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 1.5F, 5.0F, -0.4886922F, 0.0F, 0.0F));
    }

    public static LayerDefinition createSwitchSectionLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main",
                CubeListBuilder.create(),
                PartPose.rotation((float) Math.PI, 0.0F, 0.0F));

        // upper1
        PartDefinition upper1 = main.addOrReplaceChild("upper1",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.5F, -3.0F, 1.62F, 3.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, -6.6F, 0.0F));

        addOctagonBox(upper1, "upper", 0, 0, -1.5F, -3.0F, 1.62F, 3.0F, 3.0F, 2.0F);

        // lower1
        PartDefinition lower1 = main.addOrReplaceChild("lower1",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.5F, -3.0F, 1.62F, 3.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, -2.2F, 0.0F));

        addOctagonBox(lower1, "lower", 0, 0, -1.5F, -3.0F, 1.62F, 3.0F, 3.0F, 2.0F);

        // button1
        PartDefinition button1 = main.addOrReplaceChild("button1",
                CubeListBuilder.create()
                        .texOffs(8, 4).addBox(-1.5F, -5.0F, -0.38F, 3.0F, 5.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -3.3F, -2.6F, 1.5707964F, 0.0F, 0.0F));

        addOctagonBox(button1, "button", 8, 4, -1.5F, -5.0F, -0.38F, 3.0F, 5.0F, 4.0F);

        // lowerConnector1
        PartDefinition lowerConnector1 = main.addOrReplaceChild("lowerConnector1",
                CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 7.0F, 1.0F),
                PartPose.offset(0.0F, -1.4F, 0.0F));

        addOctagonBox(lowerConnector1, "lowerConnector", 0, 5, -1.5F, 0.0F, 2.62F, 3.0F, 7.0F, 1.0F);

        // connector1
        PartDefinition connector1 = main.addOrReplaceChild("connector1",
                CubeListBuilder.create()
                        .texOffs(10, 0).addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, -5.2F, 0.0F));

        addOctagonBox(connector1, "connector", 10, 0, -1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F);

        // ring1
        PartDefinition ring1 = main.addOrReplaceChild("ring1",
                CubeListBuilder.create()
                        .texOffs(0, 13).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, -2.2F, 0.0F));

        addOctagonBox(ring1, "ring", 0, 13, -1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F);

        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 29.4F, 0.0F, (float) Math.PI, 0.0F, 0.0F));

        // upper2_1
        PartDefinition upper2_1 = main.addOrReplaceChild("upper2_1",
                CubeListBuilder.create()
                        .texOffs(36, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 7.0F, 1.0F),
                PartPose.offset(0.0F, 0.5F, 0.0F));
        addOctagonBox(upper2_1, "upper2_", 36, 0, -1.5F, 0.0F, 2.62F, 3.0F, 7.0F, 1.0F);

        // upper1_1
        PartDefinition upper1_1 = main.addOrReplaceChild("upper1_1",
                CubeListBuilder.create()
                        .texOffs(6, 26).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.ZERO);
        addOctagonBox(upper1_1, "upper1_", 6, 26, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

        // upper3_1
        PartDefinition upper3_1 = main.addOrReplaceChild("upper3_1",
                CubeListBuilder.create()
                        .texOffs(18, 25).addBox(-1.5F, 0.0F, 0.62F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 1.0F, 0.0F));
        addOctagonBox(upper3_1, "upper3_", 18, 25, -1.5F, 0.0F, 0.62F, 3.0F, 1.0F, 3.0F);

        // upper4_1
        PartDefinition upper4_1 = main.addOrReplaceChild("upper4_1",
                CubeListBuilder.create()
                        .texOffs(8, 29).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 5.2F, 0.0F));
        addOctagonBox(upper4_1, "upper4_", 8, 29, -1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F);

        // upper5_1
        PartDefinition upper5_1 = main.addOrReplaceChild("upper5_1",
                CubeListBuilder.create()
                        .texOffs(8, 29).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 5.7F, 0.0F));
        addOctagonBox(upper5_1, "upper5_", 8, 29, -1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F);

        // upper6_1
        PartDefinition upper6_1 = main.addOrReplaceChild("upper6_1",
                CubeListBuilder.create()
                        .texOffs(8, 29).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 6.2F, 0.0F));
        addOctagonBox(upper6_1, "upper6_", 8, 29, -1.5F, 0.0F, 1.62F, 3.0F, 1.0F, 2.0F);

        // connector1
        PartDefinition connector1 = main.addOrReplaceChild("connector1",
                CubeListBuilder.create()
                        .texOffs(36, 8).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, 6.7F, 0.0F));
        addOctagonBox(connector1, "connector", 36, 8, -1.5F, 0.0F, 2.62F, 3.0F, 5.0F, 1.0F);

        // upperBody1
        PartDefinition upperBody1 = main.addOrReplaceChild("upperBody1",
                CubeListBuilder.create()
                        .texOffs(24, 16).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, 9.4F, 0.0F));
        addOctagonBox(upperBody1, "upperBody", 24, 16, -1.5F, 0.0F, 1.62F, 3.0F, 3.0F, 2.0F);

        // middle1
        PartDefinition middle1 = main.addOrReplaceChild("middle1",
                CubeListBuilder.create()
                        .texOffs(24, 0).addBox(-2.0F, 0.0F, 1.62F, 4.0F, 14.0F, 2.0F),
                PartPose.offset(0.0F, 12.4F, 0.0F));

        middle1.addOrReplaceChild("middle2",
                CubeListBuilder.create()
                        .texOffs(24, 0).addBox(-2.0F, 0.0F, 1.62F, 4.0F, 14.0F, 2.0F),
                PartPose.rotation(0.0F, 1.5707964F, 0.0F));

        middle1.addOrReplaceChild("middle3",
                CubeListBuilder.create()
                        .texOffs(24, 0).addBox(-2.0F, 0.0F, 1.62F, 4.0F, 14.0F, 2.0F),
                PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        middle1.addOrReplaceChild("middle4",
                CubeListBuilder.create()
                        .texOffs(24, 0).addBox(-2.0F, 0.0F, 1.62F, 4.0F, 14.0F, 2.0F),
                PartPose.rotation(0.0F, 3.1415927F, 0.0F));

        // lowerBody1
        PartDefinition lowerBody1 = main.addOrReplaceChild("lowerBody1",
                CubeListBuilder.create()
                        .texOffs(24, 21).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, 26.4F, 0.0F));
        addOctagonBox(lowerBody1, "lowerBody", 24, 21, -1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F);

        // frontGrip1
        PartDefinition frontGrip1 = main.addOrReplaceChild("frontGrip1",
                CubeListBuilder.create()
                        .texOffs(18, 0).addBox(-1.0F, -11.5F, -1.0F, 2.0F, 23.0F, 1.0F),
                PartPose.offset(0.0F, 17.5F, -3.62F));

        PartDefinition frontGrip2 = frontGrip1.addOrReplaceChild("frontGrip2",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.0F, -13.0F, -1.0F, 2.0F, 27.0F, 1.0F),
                PartPose.offset(0.0F, -1.5F, -0.5F));

        // frontGrip Links
        PartDefinition frontGrip3 = frontGrip2.addOrReplaceChild("frontGrip3",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-0.7F, 2.0F, -0.5F, 0.0F, 0.57595867F, 0.0F));

        PartDefinition frontGrip4 = frontGrip3.addOrReplaceChild("frontGrip4",
                CubeListBuilder.create()
                        .texOffs(14, 17).addBox(0.0F, -11.0F, -0.5F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 12.0F, 0.0F, 0.0F, 0.0F, -0.19198622F));

        frontGrip4.addOrReplaceChild("frontGrip5",
                CubeListBuilder.create()
                        .texOffs(14, 17).addBox(-0.5F, -11.0F, -0.5F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.06981317F));

        frontGrip4.addOrReplaceChild("frontGrip6",
                CubeListBuilder.create()
                        .texOffs(14, 17).addBox(-0.5F, -11.0F, -0.5F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.13962634F));

        frontGrip4.addOrReplaceChild("frontGrip7",
                CubeListBuilder.create()
                        .texOffs(14, 17).addBox(-0.5F, -11.0F, -0.5F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.19198622F));

        PartDefinition frontGrip8 = frontGrip4.addOrReplaceChild("frontGrip8",
                CubeListBuilder.create()
                        .texOffs(14, 0).addBox(0.0F, -16.0F, -0.5F, 1.0F, 16.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.0F, 0.0F, 0.29670596F));

        frontGrip8.addOrReplaceChild("frontGrip9",
                CubeListBuilder.create()
                        .texOffs(14, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, -16.0F, 0.0F, 0.0F, 0.0F, -0.03141593F));

        frontGrip8.addOrReplaceChild("frontGrip10",
                CubeListBuilder.create()
                        .texOffs(14, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, -16.0F, 0.0F, 0.0F, 0.0F, -0.09599311F));

        frontGrip8.addOrReplaceChild("frontGrip11",
                CubeListBuilder.create()
                        .texOffs(14, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, -16.0F, 0.0F, 0.0F, 0.0F, -0.03141593F));

        frontGrip8.addOrReplaceChild("frontGrip12",
                CubeListBuilder.create()
                        .texOffs(14, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, -16.0F, 0.0F, 0.0F, 0.0F, -0.099483766F));

        // frontGrip Rechts (Mirror)
        PartDefinition frontGrip13 = frontGrip2.addOrReplaceChild("frontGrip13",
                CubeListBuilder.create().mirror(),
                PartPose.offsetAndRotation(0.7F, 2.0F, -0.5F, 0.0F, -0.57595867F, 0.0F));

        PartDefinition frontGrip14 = frontGrip13.addOrReplaceChild("frontGrip14",
                CubeListBuilder.create().mirror()
                        .texOffs(14, 17).addBox(-1.0F, -11.0F, -0.5F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.19198622F));

        frontGrip14.addOrReplaceChild("frontGrip15",
                CubeListBuilder.create().mirror()
                        .texOffs(14, 17).addBox(-0.5F, -11.0F, -0.5F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.06981317F));

        frontGrip14.addOrReplaceChild("frontGrip16",
                CubeListBuilder.create().mirror()
                        .texOffs(14, 17).addBox(-0.5F, -11.0F, -0.5F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.13962634F));

        frontGrip14.addOrReplaceChild("frontGrip17",
                CubeListBuilder.create().mirror()
                        .texOffs(14, 17).addBox(-0.5F, -11.0F, -0.5F, 1.0F, 11.0F, 1.0F),
                PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.19198622F));

        PartDefinition frontGrip18 = frontGrip14.addOrReplaceChild("frontGrip18",
                CubeListBuilder.create().mirror()
                        .texOffs(14, 0).addBox(-1.0F, -16.0F, -0.5F, 1.0F, 16.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.0F, 0.0F, -0.29670596F));

        frontGrip18.addOrReplaceChild("frontGrip19",
                CubeListBuilder.create().mirror()
                        .texOffs(14, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -16.0F, 0.0F, 0.0F, 0.0F, 0.03141593F));

        frontGrip18.addOrReplaceChild("frontGrip20",
                CubeListBuilder.create().mirror()
                        .texOffs(14, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -16.0F, 0.0F, 0.0F, 0.0F, 0.09599311F));

        frontGrip18.addOrReplaceChild("frontGrip21",
                CubeListBuilder.create().mirror()
                        .texOffs(14, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -16.0F, 0.0F, 0.0F, 0.0F, 0.03141593F));

        frontGrip18.addOrReplaceChild("frontGrip22",
                CubeListBuilder.create().mirror()
                        .texOffs(14, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -16.0F, 0.0F, 0.0F, 0.0F, 0.099483766F));

        // backGrip
        PartDefinition backGrip1 = main.addOrReplaceChild("backGrip1",
                CubeListBuilder.create()
                        .texOffs(6, 0).addBox(-1.5F, -12.0F, 0.0F, 3.0F, 25.0F, 1.0F),
                PartPose.offset(0.0F, 16.9F, 3.62F));

        PartDefinition backGrip2 = backGrip1.addOrReplaceChild("backGrip2",
                CubeListBuilder.create()
                        .texOffs(6, 0).addBox(-3.0F, -12.0F, -1.0F, 3.0F, 25.0F, 1.0F),
                PartPose.offsetAndRotation(-1.5F, 0.0F, 1.0F, 0.0F, -0.61086524F, 0.0F));

        backGrip2.addOrReplaceChild("backGrip3",
                CubeListBuilder.create()
                        .texOffs(0, 28).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -12.0F, 0.0F, 0.5061455F, 0.0F, 0.0F));

        PartDefinition backGrip4 = backGrip1.addOrReplaceChild("backGrip4",
                CubeListBuilder.create().mirror()
                        .texOffs(6, 0).addBox(0.0F, -12.0F, -1.0F, 3.0F, 25.0F, 1.0F),
                PartPose.offsetAndRotation(1.5F, 0.0F, 1.0F, 0.0F, 0.61086524F, 0.0F));

        backGrip4.addOrReplaceChild("backGrip5",
                CubeListBuilder.create()
                        .texOffs(0, 28).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, -12.0F, 0.0F, 0.5061455F, 0.0F, 0.0F));

        backGrip1.addOrReplaceChild("backGrip6",
                CubeListBuilder.create()
                        .texOffs(0, 28).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -12.0F, 1.0F, 0.41887903F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition createEmitterLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main",
                CubeListBuilder.create(),
                PartPose.rotation((float) Math.PI, 0.0F, 0.0F));

        // body1
        PartDefinition body1 = main.addOrReplaceChild("body1",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.5F, -3.0F, 1.62F, 3.0F, 3.0F, 2.0F),
                PartPose.ZERO);
        addOctagonBox(body1, "body", 0, 0, -1.5F, -3.0F, 1.62F, 3.0F, 3.0F, 2.0F);

        // ring1
        PartDefinition ring1 = main.addOrReplaceChild("ring1",
                CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, -0.3F, 0.0F));
        addOctagonBox(ring1, "ring", 0, 5, -1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F);

        return LayerDefinition.create(mesh, 64, 32);
    }

    private static void addOctagonBox(PartDefinition parent, String prefix, int texU, int texV, float x, float y, float z, float w, float h, float d) {
        parent.addOrReplaceChild(prefix + "2",
                CubeListBuilder.create()
                        .texOffs(texU, texV).addBox(x, y, z, w, h, d),
                PartPose.rotation(0.0F, 0.7853982F, 0.0F));

        parent.addOrReplaceChild(prefix + "3",
                CubeListBuilder.create()
                        .texOffs(texU, texV).addBox(x, y, z, w, h, d),
                PartPose.rotation(0.0F, 1.5707964F, 0.0F));

        parent.addOrReplaceChild(prefix + "4",
                CubeListBuilder.create()
                        .texOffs(texU, texV).addBox(x, y, z, w, h, d),
                PartPose.rotation(0.0F, 2.3561945F, 0.0F));

        parent.addOrReplaceChild(prefix + "5",
                CubeListBuilder.create()
                        .texOffs(texU, texV).addBox(x, y, z, w, h, d),
                PartPose.rotation(0.0F, 3.1415927F, 0.0F));

        parent.addOrReplaceChild(prefix + "6",
                CubeListBuilder.create()
                        .texOffs(texU, texV).addBox(x, y, z, w, h, d),
                PartPose.rotation(0.0F, -2.3561945F, 0.0F));

        parent.addOrReplaceChild(prefix + "7",
                CubeListBuilder.create()
                        .texOffs(texU, texV).addBox(x, y, z, w, h, d),
                PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        parent.addOrReplaceChild(prefix + "8",
                CubeListBuilder.create()
                        .texOffs(texU, texV).addBox(x, y, z, w, h, d),
                PartPose.rotation(0.0F, -0.7853982F, 0.0F));
    }
}