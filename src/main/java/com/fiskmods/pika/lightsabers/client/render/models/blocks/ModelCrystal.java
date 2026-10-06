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

public class ModelCrystal extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = 
        new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "crystal"), "main");

    private final ModelPart root;

    public ModelCrystal(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Shape 1 (Linke Nebensäule)
        root.addOrReplaceChild("shape1", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(-1.0F, 0.3F, 0.0F, -0.17453292F, 0.0F, -0.17453292F));

        // Shape 2 (Kleine vordere Shard)
        root.addOrReplaceChild("shape2", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-0.8F, 0.3F, -0.9F, 0.34906584F, 0.33161256F, -0.06981317F));

        // Shape 3 (Zentrale Hauptkristallsäule)
        root.addOrReplaceChild("shape3", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 0.3F, 0.0F, 0.08726646F, 0.0F, 0.10471976F));

        // Shape 4 (Hintere breite Flanke)
        root.addOrReplaceChild("shape4", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 0.3F, 1.0F, -0.2443461F, 1.1693707F, 0.34906584F));

        // Shape 5 (Mittlere Nebenshard)
        root.addOrReplaceChild("shape5", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 0.3F, 0.5F, -0.5235988F, -0.40142572F, 0.12217305F));

        // Shape 6 (Rechter dicker Kristallausläufer)
        root.addOrReplaceChild("shape6", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-0.5F, -3.0F, -0.5F, 2.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(0.5F, 0.0F, -1.5F, 0.20943952F, -0.82030475F, 0.12217305F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}