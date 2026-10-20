package com.fiskmods.pika.lightsabers.client.render.entity;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.entity.ForcePhantomEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ForcePhantomRenderer extends HumanoidMobRenderer<ForcePhantomEntity, HumanoidModel<ForcePhantomEntity>> {

	private static final ResourceLocation PHANTOM_TEXTURE = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
			"textures/entity/force_phantom.png");

	public ForcePhantomRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(ForcePhantomEntity entity) {
		return PHANTOM_TEXTURE;
	}
}
