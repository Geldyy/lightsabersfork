package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class FurySetModel extends HiltSetModel {
    private static final float POMMEL_HEIGHT = 8.3F;
    private static final float BODY_HEIGHT = 16.0F;
    private static final float SWITCH_HEIGHT = 4.8F; // Angepasst von 6.0F auf 4.8F (entspricht 6.0F * 0.8F)
    private static final float EMITTER_HEIGHT = 18.0F;

    private final ModelPart pommel;
    private final ModelPart body;
    private final ModelPart switchSection;
    private final ModelPart emitter;

    public FurySetModel() {
        super("fury");

        // --- POMMEL ---
        this.pommel = createPommelLayer().bakeRoot();
        ModelPart pommelMain = this.pommel.getChild("main");

        ModelPart cap1 = pommelMain.getChild("cap1");
        cap1.xScale = 0.4F;
        cap1.yScale = 0.4F;
        cap1.zScale = 0.4F;

        ModelPart topRing1 = pommelMain.getChild("topRing1");
        topRing1.xScale = 1.05F;
        topRing1.yScale = 0.7F;
        topRing1.zScale = 1.05F;

        ModelPart side2_1 = pommelMain.getChild("side2_1");
        side2_1.xScale = 0.25F;
        side2_1.yScale = 0.25F;
        side2_1.zScale = 0.25F;

        ModelPart side4_1 = pommelMain.getChild("side4_1");
        side4_1.xScale = 0.23F;
        side4_1.yScale = 0.23F;
        side4_1.zScale = 0.23F;

        ModelPart side1_1 = pommelMain.getChild("side1_1");
        side1_1.xScale = 0.3F;
        side1_1.yScale = 0.3F;
        side1_1.zScale = 0.3F;

        ModelPart bottom1 = pommelMain.getChild("bottom1");
        bottom1.xScale = 0.7F;
        bottom1.yScale = 0.7F;
        bottom1.zScale = 0.7F;

        ModelPart angledRing1 = pommelMain.getChild("angledRing1");
        angledRing1.xScale = 0.85F;
        angledRing1.yScale = 1.4F;
        angledRing1.zScale = 0.85F;

        ModelPart lowerRing1 = pommelMain.getChild("lowerRing1");
        lowerRing1.xScale = 0.9F;
        lowerRing1.yScale = 0.7F;
        lowerRing1.zScale = 0.9F;

        ModelPart side3_1 = pommelMain.getChild("side3_1");
        side3_1.xScale = 0.3F;
        side3_1.yScale = 0.3F;
        side3_1.zScale = 0.3F;

        ModelPart middleRing1 = pommelMain.getChild("middleRing1");
        middleRing1.xScale = 0.85F;
        middleRing1.yScale = 0.85F;
        middleRing1.zScale = 0.85F;

        // --- BODY ---
        this.body = createBodyLayer().bakeRoot();

        // --- SWITCH SECTION ---
        this.switchSection = createSwitchSectionLayer().bakeRoot();
        ModelPart switchMain = this.switchSection.getChild("main");

        ModelPart switchBody1 = switchMain.getChild("body1");
        switchBody1.xScale = 0.9F;
        switchBody1.yScale = 0.8F; // Bündig mit den Ringen (0.8F statt 0.9F)
        switchBody1.zScale = 0.9F;

        ModelPart switchRing1 = switchMain.getChild("ring1");
        switchRing1.xScale = 1.0F;
        switchRing1.yScale = 0.8F;
        switchRing1.zScale = 1.0F;

        // --- EMITTER ---
        this.emitter = createEmitterLayer().bakeRoot();
        ModelPart emitterMain = this.emitter.getChild("main");

        ModelPart backPlate1 = emitterMain.getChild("backPlate1");
        backPlate1.xScale = 0.82F;
        backPlate1.yScale = 0.82F;
        backPlate1.zScale = 0.7F;

        ModelPart innerRing1 = emitterMain.getChild("innerRing1");
        innerRing1.xScale = 0.81F;
        innerRing1.yScale = 1.0F;
        innerRing1.zScale = 0.81F;

        ModelPart topRingEmitter1 = emitterMain.getChild("topRing1");
        topRingEmitter1.xScale = 0.9F;
        topRingEmitter1.yScale = 0.9F;
        topRingEmitter1.zScale = 0.9F;

        ModelPart darkInnerRing1 = emitterMain.getChild("darkInnerRing1");
        darkInnerRing1.xScale = 0.7F;
        darkInnerRing1.yScale = 0.7F;
        darkInnerRing1.zScale = 0.7F;

        ModelPart topDetail1 = emitterMain.getChild("topDetail1");
        topDetail1.xScale = 0.77F;
        topDetail1.yScale = 0.5F;
        topDetail1.zScale = 0.7F;

        ModelPart emitterRing1 = emitterMain.getChild("emitterRing1");
        emitterRing1.xScale = 0.65F;
        emitterRing1.yScale = 0.65F;
        emitterRing1.zScale = 0.65F;

        ModelPart upperRing1 = emitterMain.getChild("upperRing1");
        upperRing1.xScale = 0.8F;
        upperRing1.yScale = 0.8F;
        upperRing1.zScale = 0.8F;
    }

    @Override
    public ModelPart getPartModel(PartType type) {
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
            case POMMEL -> POMMEL_HEIGHT;
            case BODY -> BODY_HEIGHT;
            case SWITCH_SECTION -> SWITCH_HEIGHT;
            case EMITTER -> EMITTER_HEIGHT;
        };
    }

    // =========================================================================
    // LAYER DEFINITIONS
    // =========================================================================

    public static LayerDefinition createPommelLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, POMMEL_HEIGHT, 0.0F, (float) Math.PI, 0.0F, 0.0F));

        // cap1 (Base + 7 Children)
        PartDefinition cap1 = main.addOrReplaceChild("cap1",
                CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 7.5F, 0.0F));
        cap1.addOrReplaceChild("cap2", CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 2.0F, 4.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        cap1.addOrReplaceChild("cap3", CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 2.0F, 4.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        cap1.addOrReplaceChild("cap4", CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 2.0F, 4.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        cap1.addOrReplaceChild("cap5", CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 2.0F, 4.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        cap1.addOrReplaceChild("cap6", CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 2.0F, 4.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        cap1.addOrReplaceChild("cap7", CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 2.0F, 4.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        cap1.addOrReplaceChild("cap8", CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, -0.38F, 3.0F, 2.0F, 4.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // topRing1 (Base + 7 Children)
        PartDefinition topRing1 = main.addOrReplaceChild("topRing1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        topRing1.addOrReplaceChild("topRing2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        topRing1.addOrReplaceChild("topRing3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        topRing1.addOrReplaceChild("topRing4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        topRing1.addOrReplaceChild("topRing5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        topRing1.addOrReplaceChild("topRing6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        topRing1.addOrReplaceChild("topRing7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        topRing1.addOrReplaceChild("topRing8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // side2_1 (Base + 7 Children)
        PartDefinition side2_1 = main.addOrReplaceChild("side2_1",
                CubeListBuilder.create().texOffs(8, 9).addBox(-1.5F, -2.0F, 2.6F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(3.8F, 1.5F, 0.0F, 1.5707964F, -1.5707964F, 0.0F));
        side2_1.addOrReplaceChild("side2_2", CubeListBuilder.create().texOffs(8, 9).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        side2_1.addOrReplaceChild("side2_3", CubeListBuilder.create().texOffs(8, 9).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        side2_1.addOrReplaceChild("side2_4", CubeListBuilder.create().texOffs(8, 9).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        side2_1.addOrReplaceChild("side2_5", CubeListBuilder.create().texOffs(8, 9).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        side2_1.addOrReplaceChild("side2_6", CubeListBuilder.create().texOffs(8, 9).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        side2_1.addOrReplaceChild("side2_7", CubeListBuilder.create().texOffs(8, 9).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        side2_1.addOrReplaceChild("side2_8", CubeListBuilder.create().texOffs(8, 9).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // side4_1 (Base + 7 Children)
        PartDefinition side4_1 = main.addOrReplaceChild("side4_1",
                CubeListBuilder.create().texOffs(8, 12).addBox(-1.5F, -2.0F, 2.6F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(4.4F, 1.5F, 0.0F, 1.5707964F, -1.5707964F, 0.0F));
        side4_1.addOrReplaceChild("side4_2", CubeListBuilder.create().texOffs(8, 12).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        side4_1.addOrReplaceChild("side4_3", CubeListBuilder.create().texOffs(8, 12).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        side4_1.addOrReplaceChild("side4_4", CubeListBuilder.create().texOffs(8, 12).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        side4_1.addOrReplaceChild("side4_5", CubeListBuilder.create().texOffs(8, 12).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        side4_1.addOrReplaceChild("side4_6", CubeListBuilder.create().texOffs(8, 12).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        side4_1.addOrReplaceChild("side4_7", CubeListBuilder.create().texOffs(8, 12).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        side4_1.addOrReplaceChild("side4_8", CubeListBuilder.create().texOffs(8, 12).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // side1_1 (Base + 7 Children)
        PartDefinition side1_1 = main.addOrReplaceChild("side1_1",
                CubeListBuilder.create().texOffs(8, 6).addBox(-1.5F, -2.0F, 2.6F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(3.3F, 1.5F, 0.0F, 1.5707964F, -1.5707964F, 0.0F));
        side1_1.addOrReplaceChild("side1_2", CubeListBuilder.create().texOffs(8, 6).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        side1_1.addOrReplaceChild("side1_3", CubeListBuilder.create().texOffs(8, 6).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        side1_1.addOrReplaceChild("side1_4", CubeListBuilder.create().texOffs(8, 6).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        side1_1.addOrReplaceChild("side1_5", CubeListBuilder.create().texOffs(8, 6).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        side1_1.addOrReplaceChild("side1_6", CubeListBuilder.create().texOffs(8, 6).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        side1_1.addOrReplaceChild("side1_7", CubeListBuilder.create().texOffs(8, 6).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        side1_1.addOrReplaceChild("side1_8", CubeListBuilder.create().texOffs(8, 6).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // bottom1 (Base + 7 Children)
        PartDefinition bottom1 = main.addOrReplaceChild("bottom1",
                CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 6.1F, 0.0F));
        bottom1.addOrReplaceChild("bottom2", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        bottom1.addOrReplaceChild("bottom3", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        bottom1.addOrReplaceChild("bottom4", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        bottom1.addOrReplaceChild("bottom5", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        bottom1.addOrReplaceChild("bottom6", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        bottom1.addOrReplaceChild("bottom7", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        bottom1.addOrReplaceChild("bottom8", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, 0.0F, 1.62F, 3.0F, 2.0F, 2.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // secondaryTopRing1 (Base + 7 Children)
        PartDefinition secondaryTopRing1 = main.addOrReplaceChild("secondaryTopRing1",
                CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 0.7F, 0.0F));
        secondaryTopRing1.addOrReplaceChild("secondaryTopRing2", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        secondaryTopRing1.addOrReplaceChild("secondaryTopRing3", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        secondaryTopRing1.addOrReplaceChild("secondaryTopRing4", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        secondaryTopRing1.addOrReplaceChild("secondaryTopRing5", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        secondaryTopRing1.addOrReplaceChild("secondaryTopRing6", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        secondaryTopRing1.addOrReplaceChild("secondaryTopRing7", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        secondaryTopRing1.addOrReplaceChild("secondaryTopRing8", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // angledRing1 (Parent für 8 geneigte Ringsegmente)
        PartDefinition angledRing1 = main.addOrReplaceChild("angledRing1",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -3.03F, 0.0F));
        angledRing1.addOrReplaceChild("angledRing2", CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, 0.0F, 4.43F, 3.0F, 1.0F, 1.0F), PartPose.rotation(-0.6702064F, 0.0F, 0.0F));
        angledRing1.addOrReplaceChild("angledRing3", CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, 0.0F, 4.43F, 3.0F, 1.0F, 1.0F), PartPose.rotation(-0.6702064F, 0.7853982F, 0.0F));
        angledRing1.addOrReplaceChild("angledRing4", CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, 0.0F, 4.43F, 3.0F, 1.0F, 1.0F), PartPose.rotation(-0.6702064F, 1.5707964F, 0.0F));
        angledRing1.addOrReplaceChild("angledRing5", CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, 0.0F, 4.43F, 3.0F, 1.0F, 1.0F), PartPose.rotation(-0.6702064F, 2.3561945F, 0.0F));
        angledRing1.addOrReplaceChild("angledRing6", CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, 0.0F, 4.43F, 3.0F, 1.0F, 1.0F), PartPose.rotation(-0.6702064F, 3.1415927F, 0.0F));
        angledRing1.addOrReplaceChild("angledRing7", CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, 0.0F, 4.43F, 3.0F, 1.0F, 1.0F), PartPose.rotation(-0.6702064F, -2.3561945F, 0.0F));
        angledRing1.addOrReplaceChild("angledRing8", CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, 0.0F, 4.43F, 3.0F, 1.0F, 1.0F), PartPose.rotation(-0.6702064F, -1.5707964F, 0.0F));
        angledRing1.addOrReplaceChild("angledRing9", CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, 0.0F, 4.43F, 3.0F, 1.0F, 1.0F), PartPose.rotation(-0.6702064F, -0.7853982F, 0.0F));

        // lowerRing1 (Base + 7 Children)
        PartDefinition lowerRing1 = main.addOrReplaceChild("lowerRing1",
                CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 5.6F, 0.0F));
        lowerRing1.addOrReplaceChild("lowerRing2", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        lowerRing1.addOrReplaceChild("lowerRing3", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        lowerRing1.addOrReplaceChild("lowerRing4", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        lowerRing1.addOrReplaceChild("lowerRing5", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        lowerRing1.addOrReplaceChild("lowerRing6", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        lowerRing1.addOrReplaceChild("lowerRing7", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        lowerRing1.addOrReplaceChild("lowerRing8", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // side3_1 (Base + 7 Children)
        PartDefinition side3_1 = main.addOrReplaceChild("side3_1",
                CubeListBuilder.create().texOffs(0, 22).addBox(-1.5F, -2.0F, -1.4F, 3.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(4.2F, 1.5F, 0.0F, 1.5707964F, -1.5707964F, 0.0F));
        side3_1.addOrReplaceChild("side3_2", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        side3_1.addOrReplaceChild("side3_3", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        side3_1.addOrReplaceChild("side3_4", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        side3_1.addOrReplaceChild("side3_5", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        side3_1.addOrReplaceChild("side3_6", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        side3_1.addOrReplaceChild("side3_7", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        side3_1.addOrReplaceChild("side3_8", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -2.0F, 0.62F, 3.0F, 2.0F, 3.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // middleRing1 (Base + 7 Children)
        PartDefinition middleRing1 = main.addOrReplaceChild("middleRing1",
                CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F),
                PartPose.offset(0.0F, 2.48F, 0.0F));
        middleRing1.addOrReplaceChild("middleRing2", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        middleRing1.addOrReplaceChild("middleRing3", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        middleRing1.addOrReplaceChild("middleRing4", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        middleRing1.addOrReplaceChild("middleRing5", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        middleRing1.addOrReplaceChild("middleRing6", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        middleRing1.addOrReplaceChild("middleRing7", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        middleRing1.addOrReplaceChild("middleRing8", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, BODY_HEIGHT, 0.0F, (float) Math.PI, 0.0F, 0.0F));

        // 8-Kant-Basis
        PartDefinition body1 = main.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        body1.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        body1.addOrReplaceChild("body5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        body1.addOrReplaceChild("body8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        body1.addOrReplaceChild("body11", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        body1.addOrReplaceChild("body14", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        body1.addOrReplaceChild("body17", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        body1.addOrReplaceChild("body20", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // 10-teilige Rippen-Kette
        PartDefinition body9 = body1.addOrReplaceChild("body9",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.4F, 3.52F, 1.0F, 15.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition body10 = body9.addOrReplaceChild("body10",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
                PartPose.rotation(0.0F, 0.62831855F, 0.0F));
        PartDefinition body10_1 = body10.addOrReplaceChild("body10_1",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
                PartPose.rotation(0.0F, 0.62831855F, 0.0F));
        PartDefinition body10_2 = body10_1.addOrReplaceChild("body10_2",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
                PartPose.rotation(0.0F, 0.62831855F, 0.0F));
        PartDefinition body10_3 = body10_2.addOrReplaceChild("body10_3",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
                PartPose.rotation(0.0F, 0.62831855F, 0.0F));
        PartDefinition body10_4 = body10_3.addOrReplaceChild("body10_4",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
                PartPose.rotation(0.0F, 0.62831855F, 0.0F));
        PartDefinition body10_5 = body10_4.addOrReplaceChild("body10_5",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
                PartPose.rotation(0.0F, 0.62831855F, 0.0F));
        PartDefinition body10_6 = body10_5.addOrReplaceChild("body10_6",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
                PartPose.rotation(0.0F, 0.62831855F, 0.0F));
        PartDefinition body10_7 = body10_6.addOrReplaceChild("body10_7",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
                PartPose.rotation(0.0F, 0.62831855F, 0.0F));
        body10_7.addOrReplaceChild("body10_8",
                CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, 0.5F, 3.52F, 1.0F, 15.0F, 1.0F),
                PartPose.rotation(0.0F, 0.62831855F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition createSwitchSectionLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main",
                CubeListBuilder.create(),
                PartPose.rotation((float) Math.PI, 0.0F, 0.0F));

        // 8-Kant-Körper
        PartDefinition body1 = main.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F),
                PartPose.offset(0.0F, -0.1F, 0.0F));
        body1.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        body1.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        body1.addOrReplaceChild("body4", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        body1.addOrReplaceChild("body5", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        body1.addOrReplaceChild("body6", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, -6.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        body1.addOrReplaceChild("body7", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        body1.addOrReplaceChild("body8", CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // 4-Stufen-Ring-Hierarchie
        PartDefinition ring1 = main.addOrReplaceChild("ring1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        ring1.addOrReplaceChild("ring2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        ring1.addOrReplaceChild("ring3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        ring1.addOrReplaceChild("ring4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        ring1.addOrReplaceChild("ring5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        ring1.addOrReplaceChild("ring6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        ring1.addOrReplaceChild("ring7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        ring1.addOrReplaceChild("ring8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        PartDefinition ring9 = ring1.addOrReplaceChild("ring9",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F));
        ring9.addOrReplaceChild("ring10", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        ring9.addOrReplaceChild("ring11", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        ring9.addOrReplaceChild("ring12", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        ring9.addOrReplaceChild("ring13", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        ring9.addOrReplaceChild("ring14", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        ring9.addOrReplaceChild("ring15", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        ring9.addOrReplaceChild("ring16", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        PartDefinition ring17 = ring9.addOrReplaceChild("ring17",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F));
        ring17.addOrReplaceChild("ring18", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        ring17.addOrReplaceChild("ring19", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        ring17.addOrReplaceChild("ring20", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        ring17.addOrReplaceChild("ring21", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        ring17.addOrReplaceChild("ring22", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        ring17.addOrReplaceChild("ring23", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        ring17.addOrReplaceChild("ring24", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        PartDefinition ring25 = ring17.addOrReplaceChild("ring25",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F));
        ring25.addOrReplaceChild("ring26", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        ring25.addOrReplaceChild("ring27", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        ring25.addOrReplaceChild("ring28", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        ring25.addOrReplaceChild("ring29", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        ring25.addOrReplaceChild("ring30", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        ring25.addOrReplaceChild("ring31", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        ring25.addOrReplaceChild("ring32", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition createEmitterLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main",
                CubeListBuilder.create(),
                PartPose.rotation((float) Math.PI, 0.0F, 0.0F));

        // backPlate1
        PartDefinition backPlate1 = main.addOrReplaceChild("backPlate1",
                CubeListBuilder.create().texOffs(8, 14).addBox(-1.5F, -12.0F, 2.62F, 3.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.45F, 0.0F, -3.142989F, 0.0F));
        PartDefinition backPlate2 = backPlate1.addOrReplaceChild("backPlate2",
                CubeListBuilder.create().texOffs(0, 14).addBox(-1.5F, -12.0F, 2.62F, 3.0F, 10.0F, 1.0F),
                PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        backPlate2.addOrReplaceChild("backPlate3",
                CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 6.0F, 1.0F),
                PartPose.offset(-0.3F, -5.7F, 3.22F));
        PartDefinition backPlate4 = backPlate1.addOrReplaceChild("backPlate4",
                CubeListBuilder.create().texOffs(0, 14).addBox(-1.5F, -12.0F, 2.62F, 3.0F, 10.0F, 1.0F),
                PartPose.rotation(0.0F, -0.7853982F, 0.0F));
        backPlate4.addOrReplaceChild("backPlate5",
                CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 6.0F, 1.0F),
                PartPose.offset(0.3F, -5.7F, 3.22F));
        backPlate1.addOrReplaceChild("backPlate6",
                CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 6.0F, 1.0F),
                PartPose.offset(0.0F, -5.7F, 3.22F));

        // body1 (Haupt-Emitter-Körper)
        PartDefinition body1 = main.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -13.0F, 2.62F, 3.0F, 13.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        body1.addOrReplaceChild("body2", CubeListBuilder.create().mirror(true).texOffs(8, 0).addBox(-1.5F, -13.0F, 2.62F, 3.0F, 13.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        body1.addOrReplaceChild("body3", CubeListBuilder.create().mirror(true).texOffs(16, 0).addBox(-1.5F, -13.0F, 2.62F, 3.0F, 13.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));

        PartDefinition body4 = body1.addOrReplaceChild("body4",
                CubeListBuilder.create().mirror(true).texOffs(24, 3).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        body4.addOrReplaceChild("body5",
                CubeListBuilder.create().texOffs(24, 8).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, 3.62F, 0.61086524F, 0.0F, 0.0F));

        PartDefinition body6 = body1.addOrReplaceChild("body6",
                CubeListBuilder.create().texOffs(32, 3).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        body6.addOrReplaceChild("body7",
                CubeListBuilder.create().texOffs(32, 8).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, 3.62F, 0.61086524F, 0.0F, 0.0F));

        PartDefinition body8 = body1.addOrReplaceChild("body8",
                CubeListBuilder.create().texOffs(24, 3).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        body8.addOrReplaceChild("body9",
                CubeListBuilder.create().texOffs(24, 8).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, 3.62F, 0.61086524F, 0.0F, 0.0F));

        body1.addOrReplaceChild("body10", CubeListBuilder.create().texOffs(16, 0).addBox(-1.5F, -13.0F, 2.62F, 3.0F, 13.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        body1.addOrReplaceChild("body11", CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, -13.0F, 2.62F, 3.0F, 13.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        PartDefinition body12 = body1.addOrReplaceChild("body12",
                CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, -13.0F, 2.62F, 3.0F, 2.0F, 1.0F),
                PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        body12.addOrReplaceChild("body13",
                CubeListBuilder.create().texOffs(24, 5).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 3.62F, -0.33161256F, 0.0F, 0.0F));

        PartDefinition body14 = body1.addOrReplaceChild("body14",
                CubeListBuilder.create().texOffs(32, 0).addBox(-1.5F, -13.0F, 2.62F, 3.0F, 2.0F, 1.0F),
                PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        body14.addOrReplaceChild("body15",
                CubeListBuilder.create().texOffs(32, 5).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 3.62F, -0.33161256F, 0.0F, 0.0F));

        PartDefinition body16 = body1.addOrReplaceChild("body16",
                CubeListBuilder.create().mirror(true).texOffs(24, 0).addBox(-1.5F, -13.0F, 2.62F, 3.0F, 2.0F, 1.0F),
                PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        body16.addOrReplaceChild("body17",
                CubeListBuilder.create().texOffs(24, 5).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 3.62F, -0.33161256F, 0.0F, 0.0F));

        // innerRing1
        PartDefinition innerRing1 = main.addOrReplaceChild("innerRing1",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -11.64F, 0.0F));
        innerRing1.addOrReplaceChild("innerRing2", CubeListBuilder.create().texOffs(16, 14).addBox(-1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.29496065F, 0.0F, 0.0F));
        innerRing1.addOrReplaceChild("innerRing3", CubeListBuilder.create().texOffs(16, 14).addBox(-1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.29496065F, 0.7853982F, 0.0F));
        innerRing1.addOrReplaceChild("innerRing4", CubeListBuilder.create().texOffs(16, 14).addBox(-1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.29496065F, 1.5707964F, 0.0F));
        innerRing1.addOrReplaceChild("innerRing5", CubeListBuilder.create().texOffs(16, 14).addBox(-1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.29496065F, 2.3561945F, 0.0F));
        innerRing1.addOrReplaceChild("innerRing6", CubeListBuilder.create().texOffs(16, 14).addBox(-1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.29496065F, 3.1415927F, 0.0F));
        innerRing1.addOrReplaceChild("innerRing7", CubeListBuilder.create().texOffs(16, 14).addBox(-1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.29496065F, -2.3561945F, 0.0F));
        innerRing1.addOrReplaceChild("innerRing8", CubeListBuilder.create().texOffs(16, 14).addBox(-1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.29496065F, -1.5707964F, 0.0F));
        innerRing1.addOrReplaceChild("innerRing9", CubeListBuilder.create().texOffs(16, 14).addBox(-1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.29496065F, -0.7853982F, 0.0F));

        // topRing1
        PartDefinition topRing1 = main.addOrReplaceChild("topRing1",
                CubeListBuilder.create().texOffs(16, 21).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -16.9F, 0.0F));
        topRing1.addOrReplaceChild("topRing2", CubeListBuilder.create().texOffs(16, 21).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        topRing1.addOrReplaceChild("topRing3", CubeListBuilder.create().texOffs(16, 21).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        topRing1.addOrReplaceChild("topRing4", CubeListBuilder.create().texOffs(16, 21).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        topRing1.addOrReplaceChild("topRing5", CubeListBuilder.create().texOffs(16, 21).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        topRing1.addOrReplaceChild("topRing6", CubeListBuilder.create().texOffs(16, 21).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        topRing1.addOrReplaceChild("topRing7", CubeListBuilder.create().texOffs(16, 21).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        topRing1.addOrReplaceChild("topRing8", CubeListBuilder.create().texOffs(16, 21).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // darkInnerRing1
        PartDefinition darkInnerRing1 = main.addOrReplaceChild("darkInnerRing1",
                CubeListBuilder.create().texOffs(16, 18).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, -15.7F, 0.0F));
        darkInnerRing1.addOrReplaceChild("darkInnerRing2", CubeListBuilder.create().texOffs(16, 18).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        darkInnerRing1.addOrReplaceChild("darkInnerRing3", CubeListBuilder.create().texOffs(16, 18).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        darkInnerRing1.addOrReplaceChild("darkInnerRing4", CubeListBuilder.create().texOffs(16, 18).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        darkInnerRing1.addOrReplaceChild("darkInnerRing5", CubeListBuilder.create().texOffs(16, 18).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        darkInnerRing1.addOrReplaceChild("darkInnerRing6", CubeListBuilder.create().texOffs(16, 18).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        darkInnerRing1.addOrReplaceChild("darkInnerRing7", CubeListBuilder.create().texOffs(16, 18).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        darkInnerRing1.addOrReplaceChild("darkInnerRing8", CubeListBuilder.create().texOffs(16, 18).addBox(-1.5F, -2.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // topDetail1
        PartDefinition topDetail1 = main.addOrReplaceChild("topDetail1",
                CubeListBuilder.create().texOffs(32, 10).addBox(-1.5F, -4.0F, 0.0F, 3.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -8.3F, -2.7F, -0.17453292F, -3.1415927F, 0.0017453292F));
        topDetail1.addOrReplaceChild("topDetail2",
                CubeListBuilder.create().texOffs(24, 13).addBox(0.0F, -2.5F, -1.0F, 3.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(1.5F, -1.5F, 1.0F, 0.0F, 0.7853982F, 0.0F));
        topDetail1.addOrReplaceChild("topDetail3",
                CubeListBuilder.create().mirror(true).texOffs(24, 13).addBox(-3.0F, -2.5F, -1.0F, 3.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-1.5F, -1.5F, 1.0F, 0.0F, -0.7853982F, 0.0F));

        // emitterRing1
        PartDefinition emitterRing1 = main.addOrReplaceChild("emitterRing1",
                CubeListBuilder.create().texOffs(16, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -17.5F, 0.0F));
        emitterRing1.addOrReplaceChild("emitterRing2", CubeListBuilder.create().texOffs(16, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        emitterRing1.addOrReplaceChild("emitterRing3", CubeListBuilder.create().texOffs(16, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        emitterRing1.addOrReplaceChild("emitterRing4", CubeListBuilder.create().texOffs(16, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        emitterRing1.addOrReplaceChild("emitterRing5", CubeListBuilder.create().texOffs(16, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        emitterRing1.addOrReplaceChild("emitterRing6", CubeListBuilder.create().texOffs(16, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        emitterRing1.addOrReplaceChild("emitterRing7", CubeListBuilder.create().texOffs(16, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        emitterRing1.addOrReplaceChild("emitterRing8", CubeListBuilder.create().texOffs(16, 23).addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // upperRing1
        PartDefinition upperRing1 = main.addOrReplaceChild("upperRing1",
                CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -2.0F, 2.64F, 2.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, -15.86F, 0.0F));
        upperRing1.addOrReplaceChild("upperRing2", CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -2.0F, 2.64F, 2.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        upperRing1.addOrReplaceChild("upperRing3", CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -2.0F, 2.64F, 2.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        upperRing1.addOrReplaceChild("upperRing4", CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -2.0F, 2.64F, 2.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        upperRing1.addOrReplaceChild("upperRing5", CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -2.0F, 2.64F, 2.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        upperRing1.addOrReplaceChild("upperRing6", CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -2.0F, 2.64F, 2.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        upperRing1.addOrReplaceChild("upperRing7", CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -2.0F, 2.64F, 2.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        upperRing1.addOrReplaceChild("upperRing8", CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -2.0F, 2.64F, 2.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }
}