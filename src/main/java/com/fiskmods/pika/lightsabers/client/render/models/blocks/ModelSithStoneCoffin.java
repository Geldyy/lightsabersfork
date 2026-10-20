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

public class ModelSithStoneCoffin extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "sith_stone_coffin"), "main");

	private final ModelPart base;
	private final ModelPart coffin;

	public ModelSithStoneCoffin(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.base = root.getChild("base");
		this.coffin = this.base.getChild("coffin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition base = root.addOrReplaceChild("base",
				CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 3.0F, 16.0F),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		base.addOrReplaceChild("coffin",
				CubeListBuilder.create().texOffs(0, 20).addBox(-6.5F, -4.5F, 0.0F, 13.0F, 9.0F, 28.0F),
				PartPose.offsetAndRotation(0.0F, -3.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, boolean baseplateOnly) {
		this.coffin.visible = !baseplateOnly;
		this.base.render(poseStack, consumer, light, overlay);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
			int color) {
		this.base.render(poseStack, buffer, packedLight, packedOverlay, color);
	}
}