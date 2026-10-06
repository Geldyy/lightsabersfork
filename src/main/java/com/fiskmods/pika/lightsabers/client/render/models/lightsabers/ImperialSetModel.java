package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ImperialSetModel extends HiltSetModel {

    private static final float[] OCTAGON_ANGLES = {
        0.0F, 0.7853982F, 1.5707964F, 2.3561945F,
        3.1415927F, -2.3561945F, -1.5707964F, -0.7853982F
    };

    private final ModelPart pommel;
    private final ModelPart body;
    private final ModelPart switchSection;
    private final ModelPart emitter;

    public ImperialSetModel() {
        super("imperial");

        // 1. POMMEL BAKING & SKALIERUNGEN (1:1 ModelPommelImperial)
        this.pommel = createPommel().bakeRoot();
        if (this.pommel.hasChild("main")) {
            ModelPart main = this.pommel.getChild("main");
            if (main.hasChild("innerRing1")) {
                ModelPart p = main.getChild("innerRing1");
                p.xScale = 0.41F; p.yScale = 1.41F; p.zScale = 0.41F;
            }
            if (main.hasChild("body1")) {
                ModelPart p = main.getChild("body1");
                p.xScale = 1.03F; p.yScale = 1.03F; p.zScale = 1.03F;
            }
            if (main.hasChild("shell1")) {
                ModelPart p = main.getChild("shell1");
                p.xScale = 1.03F; p.yScale = 0.73F; p.zScale = 1.03F;
            }
            if (main.hasChild("topRing1_1")) {
                ModelPart p = main.getChild("topRing1_1");
                p.xScale = 0.3F; p.yScale = 0.3F; p.zScale = 0.3F;
            }
            if (main.hasChild("topRing1")) {
                ModelPart p = main.getChild("topRing1");
                p.xScale = 0.9F; p.yScale = 0.9F; p.zScale = 0.9F;
            }
        }

        // 2. BODY BAKING & SKALIERUNGEN (1:1 ModelBodyImperial)
        this.body = createBody().bakeRoot();
        if (this.body.hasChild("main")) {
            ModelPart main = this.body.getChild("main");
            if (main.hasChild("shell1")) {
                ModelPart p = main.getChild("shell1");
                p.xScale = 1.0F; p.yScale = 0.4F; p.zScale = 1.0F;
            }
            if (main.hasChild("frontButton1_1")) {
                ModelPart p = main.getChild("frontButton1_1");
                p.xScale = 0.15F; p.yScale = 0.15F; p.zScale = 0.15F;
            }
            if (main.hasChild("lineLeft1")) {
                ModelPart p = main.getChild("lineLeft1");
                p.xScale = 1.0F; p.yScale = 1.0F; p.zScale = 0.8F;
            }
            if (main.hasChild("shell1_1")) {
                ModelPart p = main.getChild("shell1_1");
                p.xScale = 1.0F; p.yScale = 0.4F; p.zScale = 1.0F;
            }
            if (main.hasChild("lineLeft1_1")) {
                ModelPart p = main.getChild("lineLeft1_1");
                p.xScale = 1.0F; p.yScale = 1.0F; p.zScale = 0.8F;
            }
            if (main.hasChild("frontButton1")) {
                ModelPart p = main.getChild("frontButton1");
                p.xScale = 0.15F; p.yScale = 0.15F; p.zScale = 0.15F;
            }
            if (main.hasChild("body1")) {
                ModelPart p = main.getChild("body1");
                p.xScale = 0.9F; p.yScale = 1.0F; p.zScale = 0.9F;
            }
        }

        // 3. SWITCH SECTION BAKING & SKALIERUNGEN (1:1 ModelSwitchSectionImperial)
        this.switchSection = createSwitch().bakeRoot();
        if (this.switchSection.hasChild("main")) {
            ModelPart main = this.switchSection.getChild("main");
            if (main.hasChild("ring1")) {
                ModelPart p = main.getChild("ring1");
                p.xScale = 1.0F; p.yScale = 0.3F; p.zScale = 1.0F;
            }
            if (main.hasChild("body1")) {
                ModelPart p = main.getChild("body1");
                p.xScale = 0.9F; p.yScale = 0.9F; p.zScale = 0.9F;
            }
            if (main.hasChild("body1_1")) {
                ModelPart p = main.getChild("body1_1");
                p.xScale = 1.0F; p.yScale = 0.8F; p.zScale = 1.0F;
            }
        }

        // 4. EMITTER BAKING & SKALIERUNGEN (1:1 ModelEmitterImperial)
        this.emitter = createEmitter().bakeRoot();
        if (this.emitter.hasChild("main")) {
            ModelPart main = this.emitter.getChild("main");
            if (main.hasChild("topRing1")) {
                ModelPart p = main.getChild("topRing1");
                p.xScale = 0.9F; p.yScale = 0.6F; p.zScale = 0.9F;
            }
            if (main.hasChild("topRing1_1")) {
                ModelPart p = main.getChild("topRing1_1");
                p.xScale = 0.9F; p.yScale = 0.9F; p.zScale = 0.9F;
            }
            if (main.hasChild("emitterRing1")) {
                ModelPart p = main.getChild("emitterRing1");
                p.xScale = 0.45F; p.yScale = 0.65F; p.zScale = 0.45F;
            }
            if (main.hasChild("upperRing1")) {
                ModelPart p = main.getChild("upperRing1");
                p.xScale = 0.5F; p.yScale = 0.8F; p.zScale = 0.5F;
            }
            if (main.hasChild("innerRing1")) {
                ModelPart p = main.getChild("innerRing1");
                p.xScale = 0.81F; p.yScale = 0.9F; p.zScale = 0.81F;
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
            case POMMEL -> 6.5F;
            case BODY -> 15.8F;
            case SWITCH_SECTION -> 3.7F;
            case EMITTER -> 7.6F;
        };
    }

    // =========================================================================
    // 1. POMMEL
    // =========================================================================
    private static LayerDefinition createPommel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 6.5F, 0.0F, (float)Math.PI, 0.0F, 0.0F));

        // innerRing1
        PartDefinition innerRing1 = main.addOrReplaceChild("innerRing1", CubeListBuilder.create(),
                PartPose.offsetAndRotation(1.0F, 0.06F, 0.0F, 0.0F, 0.0F, (float)Math.PI));
        for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
            innerRing1.addOrReplaceChild("innerRing" + (i + 2), CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F),
                    PartPose.rotation(0.55850536F, OCTAGON_ANGLES[i], 0.0F));
        }

        // body1 (Basis)
        PartDefinition body1 = main.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(16, 0)
                .addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F), PartPose.ZERO);
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            body1.addOrReplaceChild("body" + (i + 1), CubeListBuilder.create().texOffs(16, 0)
                    .addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // shell1 (Außenmantel & Krallen)
        PartDefinition shell1 = main.addOrReplaceChild("shell1", CubeListBuilder.create().texOffs(8, 8)
                .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.offset(0.0F, 1.03F, 0.0F));

        PartDefinition shell2 = shell1.addOrReplaceChild("shell2", CubeListBuilder.create().texOffs(16, 9)
                .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        PartDefinition shell3 = shell2.addOrReplaceChild("shell3", CubeListBuilder.create().texOffs(16, 0)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-1.0F, 1.0F, 3.12F));
        shell3.addOrReplaceChild("shell4", CubeListBuilder.create().texOffs(8, 12)
                .addBox(-3.0F, -1.0F, -2.5F, 3.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.62831855F));
        shell3.addOrReplaceChild("shell5", CubeListBuilder.create().texOffs(16, 0)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.5F, 0.0F, 0.0F));

        shell1.addOrReplaceChild("shell6", CubeListBuilder.create().texOffs(16, 9)
                .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));

        PartDefinition shell7 = shell1.addOrReplaceChild("shell7", CubeListBuilder.create().texOffs(16, 9)
                .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        PartDefinition shell8 = shell7.addOrReplaceChild("shell8", CubeListBuilder.create().mirror().texOffs(16, 0)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(1.0F, 1.0F, 3.12F));
        shell8.addOrReplaceChild("shell9", CubeListBuilder.create().mirror().texOffs(8, 12)
                .addBox(0.0F, -1.0F, -2.5F, 3.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.62831855F));
        shell8.addOrReplaceChild("shell10", CubeListBuilder.create().mirror().texOffs(16, 0)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, 0.0F, 0.0F));

        PartDefinition shell11 = shell1.addOrReplaceChild("shell11", CubeListBuilder.create().texOffs(8, 8)
                .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        PartDefinition shell12 = shell11.addOrReplaceChild("shell12", CubeListBuilder.create().mirror().texOffs(16, 0)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(1.0F, 3.0F, 3.12F));
        shell12.addOrReplaceChild("shell13", CubeListBuilder.create().mirror().texOffs(8, 12)
                .addBox(0.0F, -1.0F, -3.5F, 3.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.62831855F));
        shell12.addOrReplaceChild("shell14", CubeListBuilder.create().mirror().texOffs(16, 0)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, 0.0F, 0.0F));

        PartDefinition shell15 = shell1.addOrReplaceChild("shell15", CubeListBuilder.create().texOffs(8, 2)
                .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        shell15.addOrReplaceChild("shell16", CubeListBuilder.create().mirror().texOffs(8, 12)
                .addBox(0.0F, -1.0F, -2.5F, 3.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-1.5F, 5.0F, 3.12F, 0.0F, 0.0F, 0.3403392F));

        shell1.addOrReplaceChild("shell17", CubeListBuilder.create().texOffs(0, 9)
                .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        PartDefinition shell18 = shell1.addOrReplaceChild("shell18", CubeListBuilder.create().texOffs(8, 2)
                .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));
        shell18.addOrReplaceChild("shell19", CubeListBuilder.create().texOffs(8, 12)
                .addBox(-3.0F, -1.0F, -2.5F, 3.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(1.5F, 5.0F, 3.12F, 0.0F, 0.0F, -0.3403392F));

        PartDefinition shell20 = shell1.addOrReplaceChild("shell20", CubeListBuilder.create().texOffs(16, 0)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-1.0F, 3.0F, 3.12F));
        shell20.addOrReplaceChild("shell21", CubeListBuilder.create().texOffs(8, 12)
                .addBox(-3.0F, -1.0F, -3.5F, 3.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.62831855F));
        shell20.addOrReplaceChild("shell22", CubeListBuilder.create().texOffs(16, 0)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.5F, 0.0F, 0.0F));

        // topRing1_1
        PartDefinition topRing1_1 = main.addOrReplaceChild("topRing1_1", CubeListBuilder.create().texOffs(0, 17)
                .addBox(-1.5F, -1.0F, -0.4F, 3.0F, 3.0F, 4.0F), PartPose.offset(1.0F, 6.6F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            topRing1_1.addOrReplaceChild("topRing" + (i + 1) + "_1", CubeListBuilder.create().texOffs(0, 17)
                    .addBox(-1.5F, -1.0F, -0.38F, 3.0F, 3.0F, 4.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // topRing1
        PartDefinition topRing1 = main.addOrReplaceChild("topRing1", CubeListBuilder.create().texOffs(0, 17)
                .addBox(-1.6F, -1.0F, 0.62F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, -0.5235988F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            topRing1.addOrReplaceChild("topRing" + (i + 1), CubeListBuilder.create().texOffs(0, 17)
                    .addBox(-1.5F, -1.0F, 0.62F, 3.0F, 3.0F, 3.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        return LayerDefinition.create(mesh, 32, 32);
    }

    // =========================================================================
    // 2. BODY
    // =========================================================================
    private static LayerDefinition createBody() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 15.8F, 0.0F, (float)Math.PI, 0.0F, 0.0F));

        // body1 (Scale 0.9, 1.0, 0.9)
        PartDefinition body1 = main.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.ZERO);
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            body1.addOrReplaceChild("body" + (i + 1), CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 16.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // shell1 (Scale 1.0, 0.4, 1.0)
        PartDefinition shell1 = main.addOrReplaceChild("shell1", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F),
                PartPose.rotation(0.0F, 1.5707964F, (float)Math.PI));
        shell1.addOrReplaceChild("shell17", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));

        PartDefinition shell18 = shell1.addOrReplaceChild("shell18", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        PartDefinition shell19 = shell18.addOrReplaceChild("shell19", CubeListBuilder.create().texOffs(16, 10)
                .addBox(-1.0F, -5.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offset(0.5F, -3.0F, 3.12F));
        shell19.addOrReplaceChild("shell20", CubeListBuilder.create().texOffs(16, 16)
                .addBox(0.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.5235988F));
        PartDefinition shell21 = shell19.addOrReplaceChild("shell21", CubeListBuilder.create().texOffs(22, 10)
                .addBox(0.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.27925268F));
        shell21.addOrReplaceChild("shell22", CubeListBuilder.create().texOffs(22, 13)
                .addBox(0.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.17453292F));

        shell1.addOrReplaceChild("shell23", CubeListBuilder.create().texOffs(16, 0)
                .addBox(-1.5F, -9.0F, 2.62F, 3.0F, 9.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        PartDefinition shell24 = shell1.addOrReplaceChild("shell24", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));
        PartDefinition shell25 = shell24.addOrReplaceChild("shell25", CubeListBuilder.create().mirror().texOffs(16, 10)
                .addBox(-1.0F, -5.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offset(-0.5F, -3.0F, 3.12F));
        shell25.addOrReplaceChild("shell26", CubeListBuilder.create().mirror().texOffs(16, 16)
                .addBox(-2.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.5235988F));
        PartDefinition shell27 = shell25.addOrReplaceChild("shell27", CubeListBuilder.create().mirror().texOffs(22, 10)
                .addBox(-1.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.27925268F));
        shell27.addOrReplaceChild("shell28", CubeListBuilder.create().mirror().texOffs(22, 13)
                .addBox(-1.0F, -0.8F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 2.2F, 0.0F, 0.0F, 0.0F, -0.2617994F));

        // shell1_1 (Scale 1.0, 0.4, 1.0)
        PartDefinition shell1_1 = main.addOrReplaceChild("shell1_1", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F),
                PartPose.rotation(0.0F, -1.5707964F, (float)Math.PI));
        shell1_1.addOrReplaceChild("shell17_1", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));

        PartDefinition shell18_1 = shell1_1.addOrReplaceChild("shell18_1", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        PartDefinition shell19_1 = shell18_1.addOrReplaceChild("shell19_1", CubeListBuilder.create().texOffs(16, 10)
                .addBox(-1.0F, -5.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offset(0.5F, -3.0F, 3.12F));
        shell19_1.addOrReplaceChild("shell20_1", CubeListBuilder.create().texOffs(16, 16)
                .addBox(0.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.5235988F));
        PartDefinition shell21_1 = shell19_1.addOrReplaceChild("shell21_1", CubeListBuilder.create().texOffs(22, 10)
                .addBox(0.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.27925268F));
        shell21_1.addOrReplaceChild("shell22_1", CubeListBuilder.create().texOffs(22, 13)
                .addBox(0.25F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 1.4F, 0.0F, 0.0F, 0.0F, 0.27925268F));

        shell1_1.addOrReplaceChild("shell23_1", CubeListBuilder.create().texOffs(16, 0)
                .addBox(-1.5F, -9.0F, 2.62F, 3.0F, 9.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));

        PartDefinition shell24_1 = shell1_1.addOrReplaceChild("shell24_1", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));
        PartDefinition shell25_1 = shell24_1.addOrReplaceChild("shell25_1", CubeListBuilder.create().mirror().texOffs(16, 10)
                .addBox(-1.0F, -5.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offset(-0.5F, -3.0F, 3.12F));
        shell25_1.addOrReplaceChild("shell26_1", CubeListBuilder.create().mirror().texOffs(16, 16)
                .addBox(-2.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.5235988F));
        PartDefinition shell27_1 = shell25_1.addOrReplaceChild("shell27_1", CubeListBuilder.create().mirror().texOffs(22, 10)
                .addBox(-1.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.27925268F));
        shell27_1.addOrReplaceChild("shell28_1", CubeListBuilder.create().mirror().texOffs(22, 13)
                .addBox(-1.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.17453292F));

        // lineLeft1
        PartDefinition lineLeft1 = main.addOrReplaceChild("lineLeft1", CubeListBuilder.create().texOffs(9, 0)
                .addBox(-1.5F, 0.0F, 2.62F, 2.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 2.6F, 0.7F, 0.0F, 0.0F, 0.06981317F));
        lineLeft1.addOrReplaceChild("lineLeft2", CubeListBuilder.create().texOffs(9, 0)
                .addBox(0.0F, 0.0F, 0.0F, 1.0F, 9.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 4.5F, 2.62F, 0.0F, 0.0F, 0.17453292F));
        lineLeft1.addOrReplaceChild("lineLeft4", CubeListBuilder.create().texOffs(9, 0)
                .addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-0.45F, 4.68F, 2.62F, 0.0F, 0.0F, 0.31241393F));
        lineLeft1.addOrReplaceChild("lineLeft5", CubeListBuilder.create().texOffs(9, 0)
                .addBox(0.0F, 0.0F, 0.0F, 1.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(-2.42F, 1.0F, 2.62F, 0.0F, 0.0F, -0.17627825F));

        // lineLeft1_1
        PartDefinition lineLeft1_1 = main.addOrReplaceChild("lineLeft1_1", CubeListBuilder.create().texOffs(9, 0)
                .addBox(-1.5F, 0.0F, 2.62F, 2.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 2.6F, -5.7F, 0.0F, 0.0F, 0.06981317F));
        lineLeft1_1.addOrReplaceChild("lineLeft2_1", CubeListBuilder.create().texOffs(9, 0)
                .addBox(0.0F, 0.0F, 0.0F, 1.0F, 9.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 4.5F, 2.62F, 0.0F, 0.0F, 0.17453292F));
        lineLeft1_1.addOrReplaceChild("lineLeft4_1", CubeListBuilder.create().texOffs(9, 0)
                .addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-0.45F, 4.68F, 2.62F, 0.0F, 0.0F, 0.31241393F));
        lineLeft1_1.addOrReplaceChild("lineLeft5_1", CubeListBuilder.create().texOffs(9, 0)
                .addBox(0.0F, 0.0F, 0.0F, 1.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(-2.42F, 1.0F, 2.62F, 0.0F, 0.0F, -0.17627825F));

        // frontButton1
        PartDefinition frontButton1 = main.addOrReplaceChild("frontButton1", CubeListBuilder.create().texOffs(0, 17)
                .addBox(-1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(2.3F, 1.4F, -2.3F, 1.5707964F, -0.7853982F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            frontButton1.addOrReplaceChild("frontButton" + (i + 1), CubeListBuilder.create().texOffs(0, 17)
                    .addBox(-1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // frontButton1_1
        PartDefinition frontButton1_1 = main.addOrReplaceChild("frontButton1_1", CubeListBuilder.create().texOffs(13, 18)
                .addBox(-1.5F, -3.9F, -0.38F, 3.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(2.4F, 1.2F, 2.3F, 1.5707964F, -2.3561945F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            frontButton1_1.addOrReplaceChild("frontButton" + (i + 1) + "_1", CubeListBuilder.create().texOffs(13, 18)
                    .addBox(-1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        return LayerDefinition.create(mesh, 32, 32);
    }

    // =========================================================================
    // 3. SWITCH SECTION
    // =========================================================================
    private static LayerDefinition createSwitch() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
                PartPose.rotation((float)Math.PI, 0.0F, 0.0F));

        // ring1 (4 Ring-Ebenen geschachtelt)
        PartDefinition ring1 = main.addOrReplaceChild("ring1", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -1.6F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            ring1.addOrReplaceChild("ring" + (i + 1), CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        PartDefinition ring9 = ring1.addOrReplaceChild("ring9", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -2.0F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            ring9.addOrReplaceChild("ring" + (i + 9), CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        PartDefinition ring17 = ring9.addOrReplaceChild("ring17", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -2.0F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            ring17.addOrReplaceChild("ring" + (i + 17), CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        PartDefinition ring25 = ring17.addOrReplaceChild("ring25", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -2.0F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            ring25.addOrReplaceChild("ring" + (i + 25), CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // body1 (Basis-Körper)
        PartDefinition body1 = main.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 2)
                .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.offset(0.0F, 1.8F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            body1.addOrReplaceChild("body" + (i + 1), CubeListBuilder.create().texOffs(0, 2)
                    .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // body1_1 (Oberer Körper)
        PartDefinition body1_1 = main.addOrReplaceChild("body1_1", CubeListBuilder.create().texOffs(0, 2)
                .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 3.2F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            body1_1.addOrReplaceChild("body" + (i + 1) + "_1", CubeListBuilder.create().texOffs(0, 2)
                    .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        return LayerDefinition.create(mesh, 8, 8);
    }

    // =========================================================================
    // 4. EMITTER
    // =========================================================================
    private static LayerDefinition createEmitter() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(),
                PartPose.rotation((float)Math.PI, 0.0F, 0.0F));

        // topRing1 (Basis-Ring)
        PartDefinition topRing1 = main.addOrReplaceChild("topRing1", CubeListBuilder.create().texOffs(0, 8)
                .addBox(-1.5F, -1.0F, 0.62F, 3.0F, 1.0F, 3.0F), PartPose.ZERO);
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            topRing1.addOrReplaceChild("topRing" + (i + 1), CubeListBuilder.create().texOffs(0, 8)
                    .addBox(-1.5F, -1.0F, 0.62F, 3.0F, 1.0F, 3.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // topRing1_1 (Oberer Ring)
        PartDefinition topRing1_1 = main.addOrReplaceChild("topRing1_1", CubeListBuilder.create().texOffs(0, 4)
                .addBox(-1.5F, -1.0F, 0.62F, 3.0F, 1.0F, 3.0F), PartPose.offset(0.0F, -3.6F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            topRing1_1.addOrReplaceChild("topRing" + (i + 1) + "_1", CubeListBuilder.create().texOffs(0, 4)
                    .addBox(-1.5F, -1.0F, 0.62F, 3.0F, 1.0F, 3.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // emitterRing1
        PartDefinition emitterRing1 = main.addOrReplaceChild("emitterRing1", CubeListBuilder.create().texOffs(2, 4)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            emitterRing1.addOrReplaceChild("emitterRing" + (i + 1), CubeListBuilder.create().texOffs(2, 4)
                    .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // upperRing1
        PartDefinition upperRing1 = main.addOrReplaceChild("upperRing1", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -2.0F, 2.64F, 3.0F, 2.0F, 1.0F), PartPose.offset(0.0F, -2.56F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            upperRing1.addOrReplaceChild("upperRing" + (i + 1), CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-1.5F, -2.0F, 2.64F, 3.0F, 2.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // innerRing1
        PartDefinition innerRing1 = main.addOrReplaceChild("innerRing1", CubeListBuilder.create(),
                PartPose.offset(0.0F, 1.66F, 0.0F));
        for (int i = 0; i < OCTAGON_ANGLES.length; i++) {
            innerRing1.addOrReplaceChild("innerRing" + (i + 2), CubeListBuilder.create().texOffs(8, 0)
                    .addBox(-1.5F, -3.0F, 3.67F, 3.0F, 3.0F, 1.0F),
                    PartPose.rotation(0.55850536F, OCTAGON_ANGLES[i], 0.0F));
        }

        return LayerDefinition.create(mesh, 16, 16);
    }
}