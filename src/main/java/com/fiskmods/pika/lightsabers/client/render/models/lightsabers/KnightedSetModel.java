package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.item.crystals.FocusingCrystalType;
import com.fiskmods.pika.lightsabers.item.crystals.ItemFocusingCrystal;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.item.Item;

public class KnightedSetModel extends HiltSetModel {

    private static final float[] OCTAGON_ANGLES = {
        0.0F, 0.7853982F, 1.5707964F, 2.3561945F,
        3.1415927F, -2.3561945F, -1.5707964F, -0.7853982F
    };

    private static final float[] DECAGON_ANGLES = {
        0.0F, 0.62831855F, 1.2566371F, 1.8849556F, 2.5132742F,
        3.1415927F, -2.5132742F, -1.8849556F, -1.2566371F, -0.62831855F
    };

    private static final float[] HEXAGON_ANGLES = {
        0.0F, 1.0471976F, 2.0943952F, 3.1415927F, -2.0943952F, -1.0471976F
    };

    private static final float[] QUAD_ANGLES = {
        0.0F, 1.5707964F, 3.1415927F, -1.5707964F
    };

    private final ModelPart pommel;
    private final ModelPart body;
    private final ModelPart switchSection;
    private final ModelPart emitter;

    public KnightedSetModel() {
        super("knighted");

        // 1. POMMEL BAKING & SKALIERUNG (1:1 ModelPommelKnighted)
        this.pommel = createPommel().bakeRoot();
        if (this.pommel.hasChild("main")) {
            ModelPart main = this.pommel.getChild("main");
            if (main.hasChild("top1")) {
                ModelPart p = main.getChild("top1");
                p.xScale = 0.8F; p.yScale = 1.0F; p.zScale = 0.8F;
            }
            if (main.hasChild("cap1")) {
                ModelPart p = main.getChild("cap1");
                p.xScale = 1.3F; p.yScale = 1.3F; p.zScale = 1.3F;
            }
        }

        // 2. BODY BAKING (1:1 ModelBodyKnighted)
        this.body = createBody().bakeRoot();

        // 3. SWITCH SECTION BAKING & SKALIERUNG (1:1 ModelSwitchSectionKnighted)
        this.switchSection = createSwitch().bakeRoot();
        if (this.switchSection.hasChild("main")) {
            ModelPart main = this.switchSection.getChild("main");
            if (main.hasChild("body1")) {
                ModelPart p = main.getChild("body1");
                p.xScale = 1.2F; p.yScale = 1.2F; p.zScale = 1.2F;
            }
            if (main.hasChild("button1")) {
                ModelPart p = main.getChild("button1");
                p.xScale = 0.25F; p.yScale = 0.13F; p.zScale = 0.13F;
            }
            if (main.hasChild("shell1")) {
                ModelPart p = main.getChild("shell1");
                p.xScale = 1.35F; p.yScale = 1.35F; p.zScale = 1.35F;
            }
        }

        // 4. EMITTER BAKING & SKALIERUNG (1:1 ModelEmitterKnighted)
        this.emitter = createEmitter().bakeRoot();
        if (this.emitter.hasChild("main")) {
            ModelPart main = this.emitter.getChild("main");
            if (main.hasChild("topEmitter1")) {
                ModelPart p = main.getChild("topEmitter1");
                p.xScale = 0.7F; p.yScale = 0.6F; p.zScale = 0.7F;
            }
            if (main.hasChild("sideEmitter1_1")) {
                ModelPart p = main.getChild("sideEmitter1_1");
                p.xScale = 0.7F; p.yScale = 0.7F; p.zScale = 0.7F;
            }
            if (main.hasChild("sideEmitter2_1")) {
                ModelPart p = main.getChild("sideEmitter2_1");
                p.xScale = 0.7F; p.yScale = 0.7F; p.zScale = 0.7F;
            }
            if (main.hasChild("shape236")) {
                ModelPart p = main.getChild("shape236");
                p.xScale = 1.2F; p.yScale = 0.8F; p.zScale = 1.2F;
            }
            if (main.hasChild("body1")) {
                ModelPart p = main.getChild("body1");
                p.xScale = 1.2F; p.yScale = 1.2F; p.zScale = 1.2F;
            }
            if (main.hasChild("shell1")) {
                ModelPart p = main.getChild("shell1");
                p.xScale = 1.35F; p.yScale = 1.35F; p.zScale = 1.35F;
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
            case POMMEL -> 13.3F;
            case BODY -> 20.0F;
            case SWITCH_SECTION -> 8.4F; 
            case EMITTER -> 12.6F;
        };
    }
    // [X-Distanz, Y-Höhe (ab Emitter-Basis), Klingenlängen-Skalierung]
    @Override
    public float[] getCrossguard() {
        return getCrossguard(null);
    }

    @Override
    public float[] getCrossguard(LightsaberData data) {
        float lengthModifier = 1.0F;

        if (data != null && data.focusCrystals() != null) {
            for (Item crystal : data.focusCrystals()) {
                if (crystal instanceof ItemFocusingCrystal fc) {
                    if (fc.getCrystalType() == FocusingCrystalType.SHOTO) {
                        lengthModifier *= 0.65F;
                    } else if (fc.getCrystalType() == FocusingCrystalType.EXTENDED) {
                        lengthModifier *= 1.45F;
                    }
                }
            }
        }

        return new float[] { 4.0F, 6.6F, 0.22F * lengthModifier };
    }

    // =========================================================================
    // 1. POMMEL
    // =========================================================================
    private static LayerDefinition createPommel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 13.3F, 0.0F, (float)Math.PI, 0.0F, 0.0F));

        PartDefinition body1 = main.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, 0.0F, 0.62F, 3.0F, 9.0F, 3.0F), PartPose.ZERO);
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            body1.addOrReplaceChild("body" + (i + 1), CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-1.5F, 0.0F, 0.62F, 3.0F, 9.0F, 3.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        for (int i = 0; i < HEXAGON_ANGLES.length; i++) {
            body1.addOrReplaceChild("fin_base_" + i, CubeListBuilder.create().texOffs(0, 24)
                    .addBox(-4.3F, -3.0F, -0.5F, 1.0F, 6.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, 4.5F, 0.0F, 0.0F, HEXAGON_ANGLES[i], 0.0F));

            PartDefinition finArm = body1.addOrReplaceChild("fin_arm_" + i, CubeListBuilder.create().texOffs(0, 12)
                    .addBox(-0.5F, -3.0F, 3.5F, 1.0F, 11.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, 4.5F, 0.0F, 0.0F, HEXAGON_ANGLES[i], 0.0F));

            finArm.addOrReplaceChild("fin_tip1_" + i, CubeListBuilder.create().texOffs(4, 26)
                    .addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, -3.0F, 4.5F, 0.55850536F, 0.0F, 0.0F));

            PartDefinition finSeg = finArm.addOrReplaceChild("fin_seg_" + i, CubeListBuilder.create().texOffs(4, 12)
                    .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 8.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, -3.0F, 4.5F, 0.05235988F, 0.0F, 0.0F));

            finSeg.addOrReplaceChild("fin_tip2_" + i, CubeListBuilder.create().texOffs(4, 21)
                    .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, 7.7F, 0.0F, 0.13962634F, 0.0F, 0.0F));
        }

        main.addOrReplaceChild("connector", CubeListBuilder.create().texOffs(12, 5)
                .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 4.0F), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition cap1 = main.addOrReplaceChild("cap1", CubeListBuilder.create().texOffs(12, 0)
                .addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F), PartPose.offset(0.0F, 12.0F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            cap1.addOrReplaceChild("cap" + (i + 1), CubeListBuilder.create().texOffs(12, 0)
                    .addBox(-1.5F, 0.0F, -0.38F, 3.0F, 1.0F, 4.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        PartDefinition top1 = main.addOrReplaceChild("top1", CubeListBuilder.create().texOffs(8, 12)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.ZERO);
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            top1.addOrReplaceChild("top" + (i + 1), CubeListBuilder.create().texOffs(8, 12)
                    .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        return LayerDefinition.create(mesh, 64, 32);
    }

    // =========================================================================
    // 2. BODY
    // =========================================================================
    private static LayerDefinition createBody() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, (float)Math.PI, 0.0F, 0.0F));

        PartDefinition body1 = main.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F), PartPose.ZERO);
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            body1.addOrReplaceChild("body" + (i + 1), CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-1.5F, 0.0F, 2.62F, 3.0F, 20.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        for (int i = 0; i < DECAGON_ANGLES.length; i++) {
            body1.addOrReplaceChild("rib_" + i, CubeListBuilder.create().texOffs(8, 0)
                    .addBox(-1.0F, -9.5F, 2.95F, 2.0F, 19.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, 9.6F, 0.0F, 0.0F, DECAGON_ANGLES[i], 0.0F));
        }

        for (int i = 0; i < QUAD_ANGLES.length; i++) {
            body1.addOrReplaceChild("knob_" + i, CubeListBuilder.create().texOffs(0, 21)
                    .addBox(-0.5F, -3.0F, 3.4F, 1.0F, 6.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, 2.9F, 0.0F, 0.0F, QUAD_ANGLES[i], 0.0F));
        }

        return LayerDefinition.create(mesh, 64, 32);
    }

    // =========================================================================
    // 3. SWITCH SECTION
    // =========================================================================
    private static LayerDefinition createSwitch() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.rotation((float)Math.PI, 0.0F, 0.0F));

        // 8-Kant Basis (Alle Wände hängen als Child an body1!)
        PartDefinition body1 = main.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -7.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.ZERO);
        
        body1.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -7.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        body1.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -7.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        body1.addOrReplaceChild("body4", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -7.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
        body1.addOrReplaceChild("body5", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -7.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        body1.addOrReplaceChild("body6", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -7.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        
        // Wand 7 trägt die Schaltbox!
        PartDefinition body7 = body1.addOrReplaceChild("body7", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -7.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        
        body1.addOrReplaceChild("body12", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -7.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        // Schaltbox-Aufbau an body7
        PartDefinition body8 = body7.addOrReplaceChild("body8", CubeListBuilder.create().texOffs(8, 0)
                .addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 1.0F), PartPose.offset(0.0F, -6.0F, 3.12F));
        body8.addOrReplaceChild("body9", CubeListBuilder.create().texOffs(8, 3)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offset(1.0F, 0.9F, 0.5F));
        body8.addOrReplaceChild("body10", CubeListBuilder.create().texOffs(8, 3)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offset(-1.0F, 0.9F, 0.5F));
        body8.addOrReplaceChild("body11", CubeListBuilder.create().texOffs(12, 3)
                .addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 1.0F), PartPose.offset(0.0F, 0.0F, 0.2F));

        // Schalter-Knopf
        PartDefinition button1 = main.addOrReplaceChild("button1", CubeListBuilder.create().texOffs(6, 12)
                .addBox(-1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(-4.35F, -4.9F, 0.0F, 1.5707964F, 1.5707964F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            button1.addOrReplaceChild("btn_oct_" + i, CubeListBuilder.create().texOffs(6, 12)
                    .addBox(-1.5F, -4.0F, -0.38F, 3.0F, 4.0F, 4.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }
        for (int i = 0; i < HEXAGON_ANGLES.length; i++) {
            button1.addOrReplaceChild("btn_hex_" + i, CubeListBuilder.create().texOffs(8, 6)
                    .addBox(-1.5F, -1.5F, 2.7F, 3.0F, 3.0F, 2.0F),
                    PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, HEXAGON_ANGLES[i], 0.0F));
        }

        // Schutz-Manschette (Shell)
        PartDefinition shell1 = main.addOrReplaceChild("shell1", CubeListBuilder.create().texOffs(0, 8)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -0.3F, 0.0F));
        shell1.addOrReplaceChild("shell2", CubeListBuilder.create().texOffs(0, 10)
                .addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        shell1.addOrReplaceChild("shell3", CubeListBuilder.create().texOffs(0, 10)
                .addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        shell1.addOrReplaceChild("shell4", CubeListBuilder.create().texOffs(0, 10)
                .addBox(-1.5F, -5.0F, 2.62F, 3.0F, 5.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));

        PartDefinition shell5 = shell1.addOrReplaceChild("shell5", CubeListBuilder.create().texOffs(0, 8)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        shell5.addOrReplaceChild("shell6", CubeListBuilder.create().mirror().texOffs(0, 16)
                .addBox(-1.5F, -4.0F, -0.5F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, -1.0F, 3.12F));

        shell1.addOrReplaceChild("shell7", CubeListBuilder.create().mirror().texOffs(0, 8)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        shell1.addOrReplaceChild("shell8", CubeListBuilder.create().mirror().texOffs(0, 8)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
        shell1.addOrReplaceChild("shell9", CubeListBuilder.create().mirror().texOffs(0, 8)
                .addBox(-1.5F, -1.0F, 2.62F, 3.0F, 1.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));

        shell1.addOrReplaceChild("shell10", CubeListBuilder.create().mirror().texOffs(0, 16)
                .addBox(-0.5F, -4.0F, -0.5F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, -1.0F, 3.12F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    // =========================================================================
    // 4. EMITTER
    // =========================================================================
    private static LayerDefinition createEmitter() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.rotation((float)Math.PI, 0.0F, 0.0F));

        // 1. Basis-Körper (Alle Wände an body1!)
        PartDefinition body1 = main.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(16, 15)
                .addBox(-1.5F, -4.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.ZERO);
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            body1.addOrReplaceChild("body" + (i + 1), CubeListBuilder.create().texOffs(16, 15)
                    .addBox(-1.5F, -4.0F, 2.62F, 3.0F, 4.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // 2. Innerer Zylinder (Alle Wände an body1_1!)
        PartDefinition body1_1 = main.addOrReplaceChild("body1_1", CubeListBuilder.create().texOffs(13, 20)
                .addBox(-1.5F, -4.0F, 0.62F, 3.0F, 4.0F, 3.0F), PartPose.offset(0.0F, -4.8F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            body1_1.addOrReplaceChild("body_inner_" + (i + 1), CubeListBuilder.create().texOffs(13, 20)
                    .addBox(-1.5F, -4.0F, 0.62F, 3.0F, 4.0F, 3.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // 3. Oberer Emitter-Ring (Alle Wände an topEmitter1!)
        PartDefinition topEmitter1 = main.addOrReplaceChild("topEmitter1", CubeListBuilder.create().texOffs(24, 14)
                .addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.offset(0.0F, -8.8F, 0.0F));
        for (int i = 1; i < OCTAGON_ANGLES.length; i++) {
            topEmitter1.addOrReplaceChild("topEmitter" + (i + 1), CubeListBuilder.create().texOffs(24, 14)
                    .addBox(-1.5F, -3.0F, 2.62F, 3.0F, 3.0F, 1.0F), PartPose.rotation(0.0F, OCTAGON_ANGLES[i], 0.0F));
        }

        // 4. Detailblock Shape 236
        main.addOrReplaceChild("shape236", CubeListBuilder.create().texOffs(0, 27)
                .addBox(-1.5F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-4.12F, 0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));

        // 5. Äußere Shell mit Kabeln
        PartDefinition shell1 = main.addOrReplaceChild("shell1", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.ZERO);
        shell1.addOrReplaceChild("shell2", CubeListBuilder.create().texOffs(0, 15)
                .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
        shell1.addOrReplaceChild("shell3", CubeListBuilder.create().texOffs(0, 15)
                .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
        shell1.addOrReplaceChild("shell4", CubeListBuilder.create().texOffs(0, 15)
                .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 7.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));

        PartDefinition shell5 = shell1.addOrReplaceChild("shell5", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
        PartDefinition shell6 = shell5.addOrReplaceChild("shell6", CubeListBuilder.create().mirror().texOffs(8, 26)
                .addBox(-1.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, 0.0F, 3.12F));
        shell6.addOrReplaceChild("shell7", CubeListBuilder.create().mirror().texOffs(9, 24)
                .addBox(0.0F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.34906584F));

        PartDefinition shell8 = shell1.addOrReplaceChild("shell8", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
        PartDefinition shell9 = shell8.addOrReplaceChild("shell9", CubeListBuilder.create().mirror().texOffs(8, 28)
                .addBox(0.0F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-1.5F, 0.7F, 3.12F, 0.0F, 0.0F, -0.34906584F));
        shell9.addOrReplaceChild("shell10", CubeListBuilder.create().mirror().texOffs(13, 27)
                .addBox(0.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.13962634F));

        PartDefinition shell11 = shell1.addOrReplaceChild("shell11", CubeListBuilder.create().texOffs(8, 15)
                .addBox(-1.5F, -6.0F, 2.62F, 3.0F, 6.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));
        PartDefinition shell12 = shell11.addOrReplaceChild("shell12", CubeListBuilder.create().texOffs(8, 28)
                .addBox(-1.0F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(1.5F, 0.7F, 3.12F, 0.0F, 0.0F, 0.34906584F));
        shell12.addOrReplaceChild("shell13", CubeListBuilder.create().texOffs(13, 27)
                .addBox(-2.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-0.9F, 0.0F, 0.0F, 0.0F, 0.0F, -0.13962634F));

        PartDefinition shell14 = shell1.addOrReplaceChild("shell14", CubeListBuilder.create().texOffs(8, 26)
                .addBox(-1.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offset(0.5F, 0.0F, 3.12F));
        shell14.addOrReplaceChild("shell15", CubeListBuilder.create().texOffs(9, 24)
                .addBox(-1.0F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.34906584F));

        // 6. Crossguard Side Emitter 1 (+Z)
        PartDefinition side1 = main.addOrReplaceChild("sideEmitter1_1", CubeListBuilder.create().texOffs(32, 0)
                .addBox(-1.5F, -14.0F, 2.62F, 3.0F, 14.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -6.6F, 4.0F, -1.5707964F, 0.0F, 0.0F));
        buildSideEmitterBranches(side1, false);

        // 7. Crossguard Side Emitter 2 (-Z)
        PartDefinition side2 = main.addOrReplaceChild("sideEmitter2_1", CubeListBuilder.create().texOffs(32, 0)
                .addBox(-1.5F, -14.0F, 2.62F, 3.0F, 14.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -6.6F, -4.0F, -1.5707964F, -3.1415927F, 0.0F));
        buildSideEmitterBranches(side2, true);

        return LayerDefinition.create(mesh, 64, 32);
    }

    private static void buildSideEmitterBranches(PartDefinition root, boolean isSecond) {
        if (!isSecond) {
            // Emitter 1 (+Z)
            PartDefinition p45 = root.addOrReplaceChild("quillon_45", CubeListBuilder.create().mirror().texOffs(24, 0)
                    .addBox(-1.5F, -13.0F, 2.62F, 3.0F, 13.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
            PartDefinition c1 = p45.addOrReplaceChild("clamp1", CubeListBuilder.create().mirror().texOffs(4, 23)
                    .addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-1.0F, -13.0F, 3.12F));
            c1.addOrReplaceChild("wire1", CubeListBuilder.create().mirror().texOffs(0, 23)
                    .addBox(-1.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                    PartPose.offsetAndRotation(0.5F, -1.0F, 0.0F, 0.0F, 0.0F, -1.0995574F));

            PartDefinition p90 = root.addOrReplaceChild("quillon_90", CubeListBuilder.create().mirror().texOffs(16, 0)
                    .addBox(-1.5F, -11.0F, 2.62F, 3.0F, 12.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
            PartDefinition c2 = p90.addOrReplaceChild("clamp2", CubeListBuilder.create().mirror().texOffs(0, 30)
                    .addBox(-1.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, -11.0F, 3.12F));
            c2.addOrReplaceChild("wire2", CubeListBuilder.create().mirror().texOffs(3, 25)
                    .addBox(0.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(-0.78F, -2.0F, 0.0F, 0.0F, 0.0F, 0.5235988F));

            root.addOrReplaceChild("quillon_135", CubeListBuilder.create().mirror().texOffs(8, 0)
                    .addBox(-1.5F, -11.0F, 2.62F, 3.0F, 14.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));
            root.addOrReplaceChild("quillon_180", CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-1.5F, -11.0F, 2.62F, 3.0F, 14.0F, 1.0F), PartPose.rotation(0.0F, 3.1415927F, 0.0F));
            root.addOrReplaceChild("quillon_n135", CubeListBuilder.create().texOffs(8, 0)
                    .addBox(-1.5F, -11.0F, 2.62F, 3.0F, 14.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));

            PartDefinition pn90 = root.addOrReplaceChild("quillon_n90", CubeListBuilder.create().texOffs(16, 0)
                    .addBox(-1.5F, -11.0F, 2.62F, 3.0F, 12.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
            PartDefinition c3 = pn90.addOrReplaceChild("clamp3", CubeListBuilder.create().texOffs(0, 30)
                    .addBox(-1.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offset(0.5F, -11.0F, 3.12F));
            c3.addOrReplaceChild("wire3", CubeListBuilder.create().texOffs(3, 25)
                    .addBox(-2.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.78F, -2.0F, 0.0F, 0.0F, 0.0F, -0.5235988F));

            PartDefinition pn45 = root.addOrReplaceChild("quillon_n45", CubeListBuilder.create().texOffs(24, 0)
                    .addBox(-1.5F, -13.0F, 2.62F, 3.0F, 13.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));
            PartDefinition c4 = pn45.addOrReplaceChild("clamp4", CubeListBuilder.create().texOffs(4, 23)
                    .addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(1.0F, -13.0F, 3.12F));
            c4.addOrReplaceChild("wire4", CubeListBuilder.create().texOffs(0, 23)
                    .addBox(0.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                    PartPose.offsetAndRotation(-0.5F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0995574F));

            root.addOrReplaceChild("tip_left", CubeListBuilder.create().texOffs(7, 22)
                    .addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(-0.8F, -13.76F, 3.1F, 0.29670596F, -0.2617994F, -0.20943952F));
            root.addOrReplaceChild("tip_right", CubeListBuilder.create().mirror().texOffs(7, 22)
                    .addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.8F, -13.76F, 3.1F, 0.29670596F, 0.2617994F, 0.20943952F));
        } else {
            // Emitter 2 (-Z)
            PartDefinition p45 = root.addOrReplaceChild("quillon_45", CubeListBuilder.create().texOffs(24, 0)
                    .addBox(-1.5F, -13.0F, 2.62F, 3.0F, 13.0F, 1.0F), PartPose.rotation(0.0F, -0.7853982F, 0.0F));
            PartDefinition c1 = p45.addOrReplaceChild("clamp1", CubeListBuilder.create().texOffs(4, 23)
                    .addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(1.0F, -13.0F, 3.12F));
            c1.addOrReplaceChild("wire1", CubeListBuilder.create().texOffs(0, 23)
                    .addBox(0.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                    PartPose.offsetAndRotation(-0.5F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0995574F));

            PartDefinition p90 = root.addOrReplaceChild("quillon_90", CubeListBuilder.create().texOffs(16, 0)
                    .addBox(-1.5F, -11.0F, 2.62F, 3.0F, 12.0F, 1.0F), PartPose.rotation(0.0F, -1.5707964F, 0.0F));
            PartDefinition c2 = p90.addOrReplaceChild("clamp2", CubeListBuilder.create().texOffs(0, 30)
                    .addBox(-1.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offset(0.5F, -11.0F, 3.12F));
            c2.addOrReplaceChild("wire2", CubeListBuilder.create().texOffs(16, 13)
                    .addBox(-2.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.78F, -2.0F, 0.0F, 0.0F, 0.0F, -0.5235988F));

            root.addOrReplaceChild("quillon_135", CubeListBuilder.create().texOffs(8, 0)
                    .addBox(-1.5F, -11.0F, 2.62F, 3.0F, 14.0F, 1.0F), PartPose.rotation(0.0F, -2.3561945F, 0.0F));
            root.addOrReplaceChild("quillon_180", CubeListBuilder.create().mirror().texOffs(0, 0)
                    .addBox(-1.5F, -11.0F, 2.62F, 3.0F, 14.0F, 1.0F), PartPose.rotation(0.0F, -3.1415927F, 0.0F));
            root.addOrReplaceChild("quillon_n135", CubeListBuilder.create().mirror().texOffs(8, 0)
                    .addBox(-1.5F, -11.0F, 2.62F, 3.0F, 14.0F, 1.0F), PartPose.rotation(0.0F, 2.3561945F, 0.0F));

            PartDefinition pn90 = root.addOrReplaceChild("quillon_n90", CubeListBuilder.create().mirror().texOffs(16, 0)
                    .addBox(-1.5F, -11.0F, 2.62F, 3.0F, 12.0F, 1.0F), PartPose.rotation(0.0F, 1.5707964F, 0.0F));
            PartDefinition c3 = pn90.addOrReplaceChild("clamp3", CubeListBuilder.create().mirror().texOffs(0, 30)
                    .addBox(-1.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, -11.0F, 3.12F));
            c3.addOrReplaceChild("wire3", CubeListBuilder.create().mirror().texOffs(16, 13)
                    .addBox(0.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(-0.78F, -2.0F, 0.0F, 0.0F, 0.0F, 0.5235988F));

            PartDefinition pn45 = root.addOrReplaceChild("quillon_n45", CubeListBuilder.create().mirror().texOffs(24, 0)
                    .addBox(-1.5F, -13.0F, 2.62F, 3.0F, 13.0F, 1.0F), PartPose.rotation(0.0F, 0.7853982F, 0.0F));
            PartDefinition c4 = pn45.addOrReplaceChild("clamp4", CubeListBuilder.create().mirror().texOffs(4, 23)
                    .addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(-1.0F, -13.0F, 3.12F));
            c4.addOrReplaceChild("wire4", CubeListBuilder.create().mirror().texOffs(0, 23)
                    .addBox(-1.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                    PartPose.offsetAndRotation(0.5F, -1.0F, 0.0F, 0.0F, 0.0F, -1.0995574F));

            root.addOrReplaceChild("tip_right", CubeListBuilder.create().mirror().texOffs(7, 22)
                    .addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(0.8F, -13.76F, 3.1F, 0.29670596F, 0.2617994F, 0.20943952F));
            root.addOrReplaceChild("tip_left", CubeListBuilder.create().texOffs(7, 22)
                    .addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(-0.8F, -13.76F, 3.1F, 0.29670596F, -0.2617994F, -0.20943952F));
        }
    }
}