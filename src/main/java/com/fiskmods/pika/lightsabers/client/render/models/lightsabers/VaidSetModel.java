package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class VaidSetModel extends HiltSetModel {

    private static final float[] OCTAGON_ANGLES = {
        0.0F,
        0.7853982F,
        1.5707964F,
        2.3561945F,
        3.1415927F,
        -2.3561945F,
        -1.5707964F,
        -0.7853982F
    };

    private static final float[] STUD_ANGLES = {
        0.0F,
        0.5235988F,
        1.0471976F,
        1.5707964F,
        2.0943952F,
        2.6179938F,
        3.1415927F,
        -2.6179938F,
        -2.0943952F,
        -1.5707964F,
        -1.0471976F,
        -0.5235988F
    };

    private final ModelPart pommel;
    private final ModelPart body;
    private final ModelPart switchSection;
    private final ModelPart emitter;

    public VaidSetModel() {
        this("vaid_ancient");
    }

    public VaidSetModel(String id) {
        super(id);

        // 1. POMMEL BAKING & SKALIERUNG
        this.pommel = createPommel().bakeRoot();
        ModelPart pommelInnerRing = this.pommel.getChild("inner_ring");
        pommelInnerRing.xScale = 0.7F;
        pommelInnerRing.yScale = 0.7F;
        pommelInnerRing.zScale = 0.7F;

        ModelPart pommelTop = this.pommel.getChild("top_ring");
        pommelTop.xScale = 0.8F;
        pommelTop.yScale = 1.0F;
        pommelTop.zScale = 0.8F;

        ModelPart pommelRing1 = this.pommel.getChild("ring1");
        pommelRing1.xScale = 0.8F;
        pommelRing1.yScale = 0.8F;
        pommelRing1.zScale = 0.8F;

        ModelPart pommelRing2 = this.pommel.getChild("ring2");
        pommelRing2.xScale = 0.8F;
        pommelRing2.yScale = 0.8F;
        pommelRing2.zScale = 0.8F;

        ModelPart pommelBody = this.pommel.getChild("body");
        pommelBody.xScale = 1.03F;
        pommelBody.yScale = 1.03F;
        pommelBody.zScale = 1.03F;

        ModelPart pommelShell = this.pommel.getChild("shell");
        pommelShell.xScale = 1.03F;
        pommelShell.yScale = 1.03F;
        pommelShell.zScale = 1.03F;

        // 2. BODY BAKING & SKALIERUNG
        this.body = createBody().bakeRoot();
        ModelPart bodyRing4 = this.body.getChild("ring4");
        bodyRing4.xScale = 1.25F;
        bodyRing4.yScale = 1.25F;
        bodyRing4.zScale = 1.25F;

        ModelPart bodyTopRing = this.body.getChild("top_ring");
        bodyTopRing.xScale = 1.15F;
        bodyTopRing.yScale = 1.15F;
        bodyTopRing.zScale = 1.15F;

        ModelPart bodyRing5 = this.body.getChild("ring5");
        bodyRing5.xScale = 1.15F;
        bodyRing5.yScale = 1.15F;
        bodyRing5.zScale = 1.15F;

        ModelPart bodyRing1 = this.body.getChild("ring1");
        bodyRing1.xScale = 1.15F;
        bodyRing1.yScale = 1.15F;
        bodyRing1.zScale = 1.15F;

        ModelPart bodyRing2 = this.body.getChild("ring2");
        bodyRing2.xScale = 1.25F;
        bodyRing2.yScale = 1.25F;
        bodyRing2.zScale = 1.25F;

        ModelPart bodyMain = this.body.getChild("body_main");
        bodyMain.xScale = 1.03F;
        bodyMain.yScale = 1.03F;
        bodyMain.zScale = 1.03F;

        ModelPart bodyRing3 = this.body.getChild("ring3");
        bodyRing3.xScale = 1.05F;
        bodyRing3.yScale = 1.05F;
        bodyRing3.zScale = 1.05F;

        // 3. SWITCH SECTION BAKING & SKALIERUNG
        this.switchSection = createSwitch().bakeRoot();
        ModelPart switchMain = this.switchSection.getChild("body_main");
        switchMain.xScale = 1.15F;
        switchMain.yScale = 1.15F;
        switchMain.zScale = 1.15F;

        ModelPart switchUpperBtn = this.switchSection.getChild("upper_button");
        switchUpperBtn.xScale = 0.3F;
        switchUpperBtn.yScale = 0.3F;
        switchUpperBtn.zScale = 0.3F;

        ModelPart switchLowerBtn = this.switchSection.getChild("lower_button");
        switchLowerBtn.xScale = 0.3F;
        switchLowerBtn.yScale = 0.3F;
        switchLowerBtn.zScale = 0.3F;

        // 4. EMITTER BAKING & SKALIERUNG
        this.emitter = createEmitter().bakeRoot();
        ModelPart emitterInnerRing = this.emitter.getChild("inner_ring");
        emitterInnerRing.xScale = 0.9F;
        emitterInnerRing.yScale = 0.9F;
        emitterInnerRing.zScale = 0.9F;

        ModelPart emitterRing2 = this.emitter.getChild("ring2");
        emitterRing2.xScale = 0.9F;
        emitterRing2.yScale = 1.0F;
        emitterRing2.zScale = 0.9F;

        ModelPart emitterShell = this.emitter.getChild("shell");
        emitterShell.xScale = 1.2F;
        emitterShell.yScale = 1.2F;
        emitterShell.zScale = 1.2F;
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
            case POMMEL -> 8.0F;
            case BODY -> 22.0F;
            case SWITCH_SECTION -> 8.0F;
            case EMITTER -> 19.3F;
        };
    }

    private static void addOctagonalShell(PartDefinition parent, String name, int u, int v, float minX, float minY, float minZ, float sizeX, float sizeY, float sizeZ) {
        for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
            parent.addOrReplaceChild(name + "_" + i, CubeListBuilder.create().texOffs(u, v)
                    .addBox(minX, minY, minZ, sizeX, sizeY, sizeZ),
                    PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }
    }

    // =========================================================================
    // 1. POMMEL (Höhe: 8.0F mit Klauen-Finnen)
    // =========================================================================
    private static LayerDefinition createPommel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Inner Core Tube (0.0F bis 8.0F, scaled 0.7)
        PartDefinition innerRing = root.addOrReplaceChild("inner_ring", CubeListBuilder.create(), PartPose.ZERO);
        addOctagonalShell(innerRing, "inner", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F);

        // Pommel End Ring 2 (scaled 0.8)
        PartDefinition ring2 = root.addOrReplaceChild("ring2", CubeListBuilder.create(), PartPose.offset(0.0F, 2.8F, 0.0F));
        addOctagonalShell(ring2, "ring2", 16, 7, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

        // Pommel Middle Ring 1 (scaled 0.8)
        PartDefinition ring1 = root.addOrReplaceChild("ring1", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));
        addOctagonalShell(ring1, "ring1", 16, 5, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

        // Hauptkörper-Basis (7.0F bis 8.0F, scaled 1.03)
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));
        addOctagonalShell(body, "body", 16, 0, -1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F);

        // Oberer Einsteck-Zapfen (8.0F bis 9.0F, scaled 0.8, 1.0, 0.8)
        PartDefinition topRing = root.addOrReplaceChild("top_ring", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));
        addOctagonalShell(topRing, "top", 8, 0, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

        // Klauen-Finnen Shell (scaled 1.03)
        PartDefinition shell = root.addOrReplaceChild("shell", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));
        shell.addOrReplaceChild("shell_0", CubeListBuilder.create().texOffs(8, 8).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.ZERO);

        PartDefinition shell45 = shell.addOrReplaceChild("shell_45", CubeListBuilder.create().texOffs(16, 9).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        PartDefinition s3 = shell45.addOrReplaceChild("s3", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-1.0F, 1.0F, 3.12F));
        s3.addOrReplaceChild("s4", CubeListBuilder.create().texOffs(8, 12).addBox(-3.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.62831855F));
        s3.addOrReplaceChild("s5", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.5F, 0.0F, 0.0F));

        shell.addOrReplaceChild("shell_90", CubeListBuilder.create().texOffs(16, 9).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));

        PartDefinition shell135 = shell.addOrReplaceChild("shell_135", CubeListBuilder.create().texOffs(16, 9).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        PartDefinition s8 = shell135.addOrReplaceChild("s8", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(1.0F, 1.0F, 3.12F));
        s8.addOrReplaceChild("s9", CubeListBuilder.create().texOffs(8, 12).addBox(0.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.62831855F));
        s8.addOrReplaceChild("s10", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, 0.0F, 0.0F));

        PartDefinition shell180 = shell.addOrReplaceChild("shell_180", CubeListBuilder.create().texOffs(8, 8).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        PartDefinition s12 = shell180.addOrReplaceChild("s12", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(1.0F, 3.0F, 3.12F));
        s12.addOrReplaceChild("s13", CubeListBuilder.create().texOffs(8, 12).addBox(0.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.62831855F));
        s12.addOrReplaceChild("s14", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, 0.0F, 0.0F));

        PartDefinition shellN135 = shell.addOrReplaceChild("shell_n135", CubeListBuilder.create().texOffs(8, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        shellN135.addOrReplaceChild("s16", CubeListBuilder.create().texOffs(8, 12).addBox(0.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-1.5F, 5.0F, 3.12F, 0.0F, 0.0F, 0.3403392F));

        shell.addOrReplaceChild("shell_n90", CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        PartDefinition shellN45 = shell.addOrReplaceChild("shell_n45", CubeListBuilder.create().texOffs(8, 2).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));
        shellN45.addOrReplaceChild("s19", CubeListBuilder.create().texOffs(8, 12).addBox(-3.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(1.5F, 5.0F, 3.12F, 0.0F, 0.0F, -0.3403392F));

        PartDefinition s20 = shell.addOrReplaceChild("s20", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-1.0F, 3.0F, 3.12F));
        s20.addOrReplaceChild("s21", CubeListBuilder.create().texOffs(8, 12).addBox(-3.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.62831855F));
        s20.addOrReplaceChild("s22", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.5F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    // =========================================================================
    // 2. BODY (Höhe: 22.0F mit 12 radialen Studs & Mehrfarb-Texturpaneelen)
    // =========================================================================
    private static LayerDefinition createBody() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // 1. Untere Stepped Rings (sitzen bündig am Pommel)
        PartDefinition ring5 = root.addOrReplaceChild("ring5", CubeListBuilder.create(), PartPose.ZERO);
        addOctagonalShell(ring5, "r5", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

        PartDefinition ring4 = root.addOrReplaceChild("ring4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.65F, 0.0F));
        addOctagonalShell(ring4, "r4", 0, 2, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

        // 2. Stud-Ring (12 radiale Studs)
        PartDefinition ring3 = root.addOrReplaceChild("ring3", CubeListBuilder.create(), PartPose.offset(0.0F, 1.6F, 0.0F));
        addOctagonalShell(ring3, "r3", 0, 4, -1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F);
        for (int i = 0; i < STUD_ANGLES.length; i++) {
            ring3.addOrReplaceChild("stud_" + i, CubeListBuilder.create().texOffs(0, 8)
                    .addBox(-1.0F, -1.0F, 3.3F, 2.0F, 2.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 0.0F, STUD_ANGLES[i], 0.0F));
        }

        PartDefinition ring2 = root.addOrReplaceChild("ring2", CubeListBuilder.create(), PartPose.offset(0.0F, 4.65F, 0.0F));
        addOctagonalShell(ring2, "r2", 0, 2, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

        PartDefinition ring1 = root.addOrReplaceChild("ring1", CubeListBuilder.create(), PartPose.offset(0.0F, 5.1F, 0.0F));
        addOctagonalShell(ring1, "r1", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

        // 3. Hauptkörper (Paneele mit individuellen Texture-Offsets)
        PartDefinition bodyMain = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));
        int[] texOffsetsU = {56, 48, 40, 32, 24, 16, 8, 0};
        for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
            bodyMain.addOrReplaceChild("panel_" + i, CubeListBuilder.create().texOffs(texOffsetsU[i], 16)
                    .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 15.0F, 1.0F),
                    PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // 4. Oberer Verbindungsring zur Switch Section
        PartDefinition topRing = root.addOrReplaceChild("top_ring", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 0.0F));
        addOctagonalShell(topRing, "top", 0, 11, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

        return LayerDefinition.create(mesh, 64, 32);
    }

    // =========================================================================
    // 3. SWITCH SECTION (Höhe: 8.0F mit 2 Mini-Buttons)
    // =========================================================================
    private static LayerDefinition createSwitch() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition bodyMain = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.ZERO);
        addOctagonalShell(bodyMain, "body", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 8.0F, 1.0F);

        // Oberer Schalter-Knopf (scaled 0.3)
        PartDefinition upperBtn = root.addOrReplaceChild("upper_button", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.8F, 6.2F, 0.0F, 0.0F, 0.0F, -1.5707964F));
        addOctagonalShell(upperBtn, "up_btn", 8, 0, -1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F);

        // Unterer Schalter-Knopf (scaled 0.3)
        PartDefinition lowerBtn = root.addOrReplaceChild("lower_button", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.8F, 3.0F, 0.0F, 0.0F, 0.0F, -1.5707964F));
        addOctagonalShell(lowerBtn, "low_btn", 0, 9, -1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F);

        return LayerDefinition.create(mesh, 64, 32);
    }

    // =========================================================================
    // 4. EMITTER (Höhe: 19.3F mit Krallen-Shroud & Ringen)
    // =========================================================================
    private static LayerDefinition createEmitter() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // 1. Basis-Ringe
        PartDefinition ring1 = root.addOrReplaceChild("ring1", CubeListBuilder.create(), PartPose.ZERO);
        addOctagonalShell(ring1, "r1", 0, 12, -1.5F, 0.0F, 2.62F, 3.0F, 2.0F, 1.0F);

        PartDefinition ring2 = root.addOrReplaceChild("ring2", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));
        addOctagonalShell(ring2, "r2", 0, 15, -1.5F, 0.0F, 2.62F, 3.0F, 2.0F, 1.0F);

        PartDefinition ring3 = root.addOrReplaceChild("ring3", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));
        addOctagonalShell(ring3, "r3", 0, 18, -1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F);

        // 2. Innerer Emitter-Zylinder (scaled 0.9)
        PartDefinition innerRing = root.addOrReplaceChild("inner_ring", CubeListBuilder.create(), PartPose.offset(0.0F, 8.3F, 0.0F));
        addOctagonalShell(innerRing, "inner", 0, 0, -1.5F, 0.0F, 2.62F, 3.0F, 11.0F, 1.0F);

        // 3. Äußere Shroud-Klauen (scaled 1.2)
        PartDefinition shell = root.addOrReplaceChild("shell", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));
        shell.addOrReplaceChild("s1", CubeListBuilder.create().texOffs(8, 15).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.ZERO);
        shell.addOrReplaceChild("s9", CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 14.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        shell.addOrReplaceChild("s17", CubeListBuilder.create().texOffs(8, 15).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        shell.addOrReplaceChild("s23", CubeListBuilder.create().texOffs(16, 0).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 9.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        // Shroud 45°
        PartDefinition s2 = shell.addOrReplaceChild("s2", CubeListBuilder.create().texOffs(8, 15).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        PartDefinition s3 = s2.addOrReplaceChild("s3", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 9.0F, 1.0F), PartPose.offset(0.5F, 0.0F, 3.12F));
        PartDefinition s4 = s3.addOrReplaceChild("s4", CubeListBuilder.create().texOffs(15, 18).addBox(0.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.9075712F));
        s4.addOrReplaceChild("s5", CubeListBuilder.create().texOffs(21, 18).addBox(0.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(1.3F, 0.0F, 0.0F));
        PartDefinition s6 = s3.addOrReplaceChild("s6", CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.17453292F));
        s6.addOrReplaceChild("s7", CubeListBuilder.create().texOffs(30, 5).addBox(0.0F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.1308997F));
        s3.addOrReplaceChild("s8", CubeListBuilder.create().texOffs(26, 10).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.5F, 0.0F, 0.0F));

        // Shroud 135°
        PartDefinition s10 = shell.addOrReplaceChild("s10", CubeListBuilder.create().texOffs(8, 15).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        PartDefinition s11 = s10.addOrReplaceChild("s11", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 9.0F, 1.0F), PartPose.offset(-0.5F, 0.0F, 3.12F));
        PartDefinition s12 = s11.addOrReplaceChild("s12", CubeListBuilder.create().texOffs(15, 18).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9075712F));
        s12.addOrReplaceChild("s13", CubeListBuilder.create().texOffs(21, 18).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-1.3F, 0.0F, 0.0F));
        PartDefinition s14 = s11.addOrReplaceChild("s14", CubeListBuilder.create().texOffs(30, 0).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.17453292F));
        s14.addOrReplaceChild("s15", CubeListBuilder.create().texOffs(30, 5).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.1308997F));
        s11.addOrReplaceChild("s16", CubeListBuilder.create().texOffs(26, 10).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, 0.0F, 0.0F));

        // Shroud -135°
        PartDefinition s18 = shell.addOrReplaceChild("s18", CubeListBuilder.create().texOffs(8, 15).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        PartDefinition s19 = s18.addOrReplaceChild("s19", CubeListBuilder.create().texOffs(16, 10).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offset(0.5F, 0.0F, 3.12F));
        s19.addOrReplaceChild("s20", CubeListBuilder.create().texOffs(16, 16).addBox(0.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5235988F));
        PartDefinition s21 = s19.addOrReplaceChild("s21", CubeListBuilder.create().texOffs(22, 10).addBox(0.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.27925268F));
        s21.addOrReplaceChild("s22", CubeListBuilder.create().texOffs(22, 13).addBox(0.0F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.17453292F));

        // Shroud -45°
        PartDefinition s24 = shell.addOrReplaceChild("s24", CubeListBuilder.create().texOffs(8, 15).addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));
        PartDefinition s25 = s24.addOrReplaceChild("s25", CubeListBuilder.create().texOffs(16, 10).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offset(-0.5F, 0.0F, 3.12F));
        s25.addOrReplaceChild("s26", CubeListBuilder.create().texOffs(16, 16).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5235988F));
        PartDefinition s27 = s25.addOrReplaceChild("s27", CubeListBuilder.create().texOffs(22, 10).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.27925268F));
        s27.addOrReplaceChild("s28", CubeListBuilder.create().texOffs(22, 13).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.17453292F));

        return LayerDefinition.create(mesh, 64, 32);
    }
}