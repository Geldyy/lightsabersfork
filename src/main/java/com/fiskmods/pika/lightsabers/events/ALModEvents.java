package com.fiskmods.pika.lightsabers.events;

import com.fiskmods.pika.lightsabers.ALAttributes;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

public class ALModEvents {

	@SubscribeEvent
	public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, ALAttributes.LIGHTSABER_IMMUNITY);
	}

	@SubscribeEvent
	public static void onEntityAttributeCreation(net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent event) {
		event.put(com.fiskmods.pika.lightsabers.ALEntities.FORCE_PHANTOM.get(),
				com.fiskmods.pika.lightsabers.entity.ForcePhantomEntity.createAttributes().build());
	}
}