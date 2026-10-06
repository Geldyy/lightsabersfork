package com.fiskmods.pika.lightsabers.client.render.models.blocks;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ModelLightsaberForge extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = 
        new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "lightsaber_forge"), "main");

    private final ModelPart root;

    public ModelLightsaberForge(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;

        // Native Skalierungen der Werkzeuge & des Monitors
        ModelPart hammer = root.getChild("hammer");
        hammer.xScale = 0.7F; hammer.yScale = 0.7F; hammer.zScale = 0.7F;

        ModelPart wrench = root.getChild("wrench");
        wrench.xScale = 0.5F; wrench.yScale = 0.5F; wrench.zScale = 0.5F;

        ModelPart screwdriver = root.getChild("screwdriver");
        screwdriver.xScale = 0.5F; screwdriver.yScale = 0.5F; screwdriver.zScale = 0.5F;

        ModelPart welder = root.getChild("welder_main");
        welder.xScale = 0.7F; welder.yScale = 0.7F; welder.zScale = 0.7F;

        ModelPart monitor = root.getChild("monitor_main");
        monitor.xScale = 0.5F; monitor.yScale = 0.5F; monitor.zScale = 0.5F;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        // 1. Werkbank-Hauptkörper
        PartDefinition workbench = root.addOrReplaceChild("workbench",
                CubeListBuilder.create().texOffs(32, 0).addBox(0.0F, 0.0F, 0.0F, 32.0F, 1.0F, 16.0F),
                PartPose.offset(-8.0F, 23.0F, -8.0F));

        workbench.addOrReplaceChild("right_siding", CubeListBuilder.create().texOffs(0, 33).addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 11.0F), PartPose.offset(31.0F, -10.0F, 4.0F));
        workbench.addOrReplaceChild("left_siding", CubeListBuilder.create().texOffs(0, 33).addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 11.0F), PartPose.offset(0.0F, -10.0F, 4.0F));
        workbench.addOrReplaceChild("back_siding", CubeListBuilder.create().texOffs(51, 46).addBox(0.0F, 0.0F, 0.0F, 28.0F, 10.0F, 1.0F), PartPose.offset(2.0F, -10.0F, 15.0F));

        workbench.addOrReplaceChild("trim_left", CubeListBuilder.create().texOffs(0, 60).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 15.0F), PartPose.offset(-0.5F, -11.5F, 0.5F));
        workbench.addOrReplaceChild("trim_right", CubeListBuilder.create().texOffs(0, 60).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 15.0F), PartPose.offset(31.5F, -11.5F, 0.5F));
        workbench.addOrReplaceChild("trim_front", CubeListBuilder.create().texOffs(0, 58).addBox(0.0F, 0.0F, 0.0F, 33.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, -11.5F, -0.5F));
        workbench.addOrReplaceChild("trim_back", CubeListBuilder.create().texOffs(0, 58).addBox(0.0F, 0.0F, 0.0F, 33.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, -11.5F, 15.5F));

        workbench.addOrReplaceChild("workbench_top", CubeListBuilder.create().texOffs(32, 17).addBox(0.0F, 0.0F, 0.0F, 32.0F, 1.0F, 16.0F), PartPose.offset(0.0F, -11.0F, 0.0F));
        workbench.addOrReplaceChild("workbench_front_side_angle", CubeListBuilder.create().texOffs(38, 34).addBox(0.0F, 0.0F, 0.0F, 31.0F, 1.0F, 11.0F), PartPose.offsetAndRotation(0.5F, -10.5F, 1.0F, -1.2217305F, 0.0F, 0.0F));

        workbench.addOrReplaceChild("workbench_pillar_1", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 4.0F), PartPose.offset(0.0F, -10.0F, 0.0F));
        workbench.addOrReplaceChild("workbench_pillar_2", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 4.0F), PartPose.offset(30.0F, -10.0F, 0.0F));
        workbench.addOrReplaceChild("workbench_pillar_3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 1.0F), PartPose.offset(30.0F, -10.0F, 15.0F));
        workbench.addOrReplaceChild("workbench_pillar_4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 1.0F), PartPose.offset(0.0F, -10.0F, 15.0F));
        workbench.addOrReplaceChild("workbench_pillar_5", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 4.0F), PartPose.offset(20.0F, -10.0F, 0.0F));
        workbench.addOrReplaceChild("workbench_pillar_6", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 4.0F), PartPose.offset(10.0F, -10.0F, 0.0F));

        // 2. Werkzeugkiste (Toolbox)
        PartDefinition toolbox = root.addOrReplaceChild("toolbox",
                CubeListBuilder.create().texOffs(53, 61).addBox(0.0F, 0.0F, 0.0F, 7.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-6.0F, 9.0F, 2.0F, 0.0F, -0.2617994F, 0.0F));

        toolbox.addOrReplaceChild("toolbox_lock", CubeListBuilder.create().texOffs(7, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F), PartPose.offset(3.0F, 0.5F, -0.2F));
        PartDefinition handle1 = toolbox.addOrReplaceChild("toolbox_handle_1", CubeListBuilder.create().texOffs(8, 12).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F), PartPose.offset(1.5F, -1.0F, 1.0F));
        PartDefinition handle2 = handle1.addOrReplaceChild("toolbox_handle_2", CubeListBuilder.create().texOffs(8, 12).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F), PartPose.offset(3.0F, 0.0F, 0.0F));
        handle2.addOrReplaceChild("toolbox_handle_3", CubeListBuilder.create().texOffs(25, 37).addBox(0.0F, 0.0F, 0.0F, 4.0F, 1.0F, 1.0F), PartPose.offset(-3.0F, -0.7F, 0.0F));

        // 3. Lichtschwert-Ablageständer
        PartDefinition lightsaberStand = root.addOrReplaceChild("lightsaber_stand",
                CubeListBuilder.create().texOffs(73, 58).addBox(0.0F, 0.0F, 0.0F, 6.0F, 2.0F, 2.0F),
                PartPose.offset(5.5F, 10.5F, 0.0F));

        lightsaberStand.addOrReplaceChild("lightsaber_stand_right", CubeListBuilder.create().texOffs(73, 58).mirror().addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7853982F));
        lightsaberStand.addOrReplaceChild("lightsaber_stand_left", CubeListBuilder.create().texOffs(73, 58).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7853982F));

        // 4. Hammer
        PartDefinition hammer = root.addOrReplaceChild("hammer",
                CubeListBuilder.create().texOffs(0, 65).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(-4.0F, 11.3F, -3.0F, 0.0F, 0.6632251F, 0.0F));
        hammer.addOrReplaceChild("hammer_head", CubeListBuilder.create().texOffs(0, 62).addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F), PartPose.offset(-1.0F, 0.0F, 5.0F));

        // 5. Schraubenschlüssel (Wrench)
        PartDefinition wrench = root.addOrReplaceChild("wrench",
                CubeListBuilder.create().texOffs(41, 62).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offsetAndRotation(22.0F, 11.3F, 2.0F, 0.0F, -1.0995574F, 0.0F));
        PartDefinition wrenchR1 = wrench.addOrReplaceChild("wrench_right_1", CubeListBuilder.create().texOffs(0, 41).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, -0.7853982F, 0.0F));
        wrenchR1.addOrReplaceChild("wrench_right_2", CubeListBuilder.create().texOffs(0, 72).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, -0.7853982F, 0.0F));
        PartDefinition wrenchL1 = wrench.addOrReplaceChild("wrench_left_1", CubeListBuilder.create().texOffs(0, 41).mirror().addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 6.0F, 0.0F, 0.7853982F, 0.0F));
        wrenchL1.addOrReplaceChild("wrench_left_2", CubeListBuilder.create().texOffs(0, 72).mirror().addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.7853982F, 0.0F));

        // 6. Schraubenzieher (Screwdriver)
        root.addOrReplaceChild("screwdriver",
                CubeListBuilder.create().texOffs(19, 62).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 11.3F, -3.0F, 0.0F, 1.0995574F, 0.0F));

        // 7. Schweißgerät (Welder)
        PartDefinition welder = root.addOrReplaceChild("welder_main",
                CubeListBuilder.create().texOffs(26, 47).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(20.0F, 10.6F, -5.0F, 0.0F, -0.55850536F, 0.0F));
        PartDefinition pipe1 = welder.addOrReplaceChild("welder_pipe_1", CubeListBuilder.create().texOffs(0, 35).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F), PartPose.offset(0.6F, 1.0F, 5.0F));
        PartDefinition pipe2 = pipe1.addOrReplaceChild("welder_pipe_2", CubeListBuilder.create().texOffs(32, 64).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(0.25F, 0.0F, 3.3F, 0.0F, -0.75049156F, 0.0F));
        PartDefinition pipe = pipe2.addOrReplaceChild("welder_pipe", CubeListBuilder.create().texOffs(24, 19).addBox(0.0F, 0.0F, 0.0F, 7.0F, 1.0F, 1.0F), PartPose.offset(-6.0F, 0.0F, 4.0F));
        pipe.addOrReplaceChild("welder_tip", CubeListBuilder.create().texOffs(2, 12).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.0F, 0.0F, -1.0F));

        // 8. Monitor & Befestigung
        PartDefinition monitor = root.addOrReplaceChild("monitor_main",
                CubeListBuilder.create().texOffs(14, 0).addBox(1.0F, 0.0F, 0.0F, 12.0F, 9.0F, 1.0F),
                PartPose.offset(-3.0F, 2.2F, 7.0F));
        monitor.addOrReplaceChild("monitor_stand", CubeListBuilder.create().texOffs(15, 30).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F), PartPose.offset(1.0F, 9.7F, -1.0F));
        monitor.addOrReplaceChild("monitor_trim_top", CubeListBuilder.create().texOffs(15, 25).addBox(0.0F, 0.0F, 0.0F, 14.0F, 1.0F, 2.0F), PartPose.offset(0.0F, -1.0F, -1.0F));
        monitor.addOrReplaceChild("monitor_trim_bottom", CubeListBuilder.create().texOffs(15, 25).addBox(0.0F, 0.0F, 0.0F, 14.0F, 1.0F, 2.0F), PartPose.offset(0.0F, 9.0F, -1.0F));
        monitor.addOrReplaceChild("monitor_trim_left", CubeListBuilder.create().texOffs(15, 11).addBox(0.0F, 0.0F, 0.0F, 1.0F, 9.0F, 2.0F), PartPose.offset(0.0F, 0.0F, -1.0F));
        monitor.addOrReplaceChild("monitor_trim_right", CubeListBuilder.create().texOffs(15, 11).addBox(0.0F, 0.0F, 0.0F, 1.0F, 9.0F, 2.0F), PartPose.offset(13.0F, 0.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 128, 96);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}