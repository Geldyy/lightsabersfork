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

public class ModelSithCoffin extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = 
        new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "sith_coffin"), "main");

    private final ModelPart base1;
    private final ModelPart lid1;

    public ModelSithCoffin(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.base1 = root.getChild("base1");
        this.lid1 = root.getChild("lid1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // 1. Basis-Platte (32 Pixel lang = 2 Blöcke)[cite: 1]
        PartDefinition base1 = root.addOrReplaceChild("base1",
                CubeListBuilder.create().texOffs(160, 0).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 3.0F, 32.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        // 2. Sockel & Goldene Wanne (Kind von base1)[cite: 1]
        PartDefinition base2 = base1.addOrReplaceChild("base2",
                CubeListBuilder.create().texOffs(110, 0).addBox(-6.0F, -2.0F, -14.0F, 12.0F, 2.0F, 28.0F),
                PartPose.offset(0.0F, -3.0F, 8.0F));

        // Innenkorpus[cite: 1]
        base2.addOrReplaceChild("base3",
                CubeListBuilder.create().texOffs(0, 90).addBox(-5.5F, -6.0F, -12.0F, 11.0F, 6.0F, 24.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        // 3. Linke schräge Seitenwand (60 Grad Neigung) & Zierelemente[cite: 1]
        PartDefinition base4 = base2.addOrReplaceChild("base4",
                CubeListBuilder.create().texOffs(183, 36).addBox(-8.0F, -1.0F, -14.0F, 8.0F, 1.0F, 28.0F),
                PartPose.offsetAndRotation(-6.0F, -2.0F, 0.0F, 0.0F, 0.0F, 1.0471976F));

        base4.addOrReplaceChild("base5",
                CubeListBuilder.create().texOffs(172, 0).addBox(-3.0F, -2.0F, 0.0F, 3.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, -1.0F, -13.98F, 0.0F, 0.0F, 1.5707964F));

        base4.addOrReplaceChild("base6",
                CubeListBuilder.create().texOffs(172, 0).addBox(-3.0F, -2.0F, -1.0F, 3.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, -1.0F, 13.98F, 0.0F, 0.0F, 1.5707964F));

        // 4. Rechte schräge Seitenwand (-60 Grad Neigung) & Zierelemente[cite: 1]
        PartDefinition base7 = base2.addOrReplaceChild("base7",
                CubeListBuilder.create().texOffs(183, 36).mirror().addBox(0.0F, -1.0F, -14.0F, 8.0F, 1.0F, 28.0F),
                PartPose.offsetAndRotation(6.0F, -2.0F, 0.0F, 0.0F, 0.0F, -1.0471976F));

        base7.addOrReplaceChild("base8",
                CubeListBuilder.create().texOffs(172, 0).mirror().addBox(0.0F, -2.0F, 0.0F, 3.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, -1.0F, -13.98F, 0.0F, 0.0F, -1.5707964F));

        base7.addOrReplaceChild("base9",
                CubeListBuilder.create().texOffs(172, 0).mirror().addBox(0.0F, -2.0F, -1.0F, 3.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, -1.0F, 13.98F, 0.0F, 0.0F, -1.5707964F));

        // 5. Stirnwände & Trägerstangen-Aufhängung[cite: 1]
        PartDefinition base10 = base2.addOrReplaceChild("base10",
                CubeListBuilder.create().texOffs(107, 0).addBox(-6.0F, -7.0F, -0.5F, 12.0F, 7.0F, 1.0F),
                PartPose.offset(0.0F, -2.0F, -13.5F));

        base2.addOrReplaceChild("base15",
                CubeListBuilder.create().texOffs(107, 0).addBox(-6.0F, -7.0F, -0.5F, 12.0F, 7.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, 13.5F, 0.0F, 3.1415927F, 0.0F));

        // 6. Äußere Haltestangen (präzise an base10 verknüpft)[cite: 1]
        PartDefinition base11 = base10.addOrReplaceChild("base11",
                CubeListBuilder.create().texOffs(58, 0).addBox(-10.0F, -2.0F, -1.0F, 20.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, -5.5F, 0.0F));

        base11.addOrReplaceChild("base12",
                CubeListBuilder.create().texOffs(0, 30).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 25.0F),
                PartPose.offset(9.0F, -1.0F, 1.0F));

        base11.addOrReplaceChild("base13",
                CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 25.0F),
                PartPose.offset(-9.0F, -1.0F, 1.0F));

        base11.addOrReplaceChild("base14",
                CubeListBuilder.create().texOffs(58, 0).addBox(-10.0F, -2.0F, -1.0F, 20.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 27.0F, 0.0F, 3.1415927F, 0.0F));

        // 7. Deckel (Root 2)[cite: 1]
        PartDefinition lid1 = root.addOrReplaceChild("lid1",
                CubeListBuilder.create().texOffs(0, 60).addBox(1.5F, -2.3F, -13.0F, 15.0F, 1.0F, 26.0F),
                PartPose.offset(-9.0F, 11.5F, 8.0F));

        lid1.addOrReplaceChild("lid2",
                CubeListBuilder.create().texOffs(64, 28).mirror().addBox(-1.0F, 0.0F, -13.0F, 1.0F, 3.0F, 26.0F),
                PartPose.offsetAndRotation(16.5F, -2.3F, 0.0F, 0.0F, 0.0F, -0.6981317F));

        lid1.addOrReplaceChild("lid3",
                CubeListBuilder.create().texOffs(64, 28).addBox(0.0F, 0.0F, -13.0F, 1.0F, 3.0F, 26.0F),
                PartPose.offsetAndRotation(1.5F, -2.3F, 0.0F, 0.0F, 0.0F, 0.6981317F));

        lid1.addOrReplaceChild("lid4",
                CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F, -0.5F, 16.0F, 2.0F, 1.0F),
                PartPose.offset(9.0F, -1.3F, -12.5F));

        lid1.addOrReplaceChild("lid5",
                CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F, -0.5F, 16.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(9.0F, -1.3F, 12.5F, 0.0F, 3.1415927F, 0.0F));

        return LayerDefinition.create(mesh, 256, 128);
    }

    /**
     * 1:1 Fisk Original-Animation (Schieben in Phase 1, Anheben & Kippen in Phase 2)[cite: 1]
     */
    public void setupAnim(float f) {
        float f1 = (f > 0.5F) ? ((1.0F - f) * 2.0F) : 1.0F; //[cite: 1]
        float f2 = 1.0F - f1; //[cite: 1]
        this.lid1.x = -9.0F + 16.0F * f; //[cite: 1]
        this.lid1.y = 11.5F - 3.0F * f2; //[cite: 1]
        this.lid1.z = 8.0F; //[cite: 1]
        this.lid1.zRot = f2; //[cite: 1]
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.base1.render(poseStack, buffer, packedLight, packedOverlay, color); //[cite: 1]
        this.lid1.render(poseStack, buffer, packedLight, packedOverlay, color); //[cite: 1]
    }
}