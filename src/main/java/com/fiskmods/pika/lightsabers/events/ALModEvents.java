package com.fiskmods.pika.lightsabers.events;

import com.fiskmods.pika.lightsabers.ALAttributes;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ALModEvents {

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ALAttributes.LIGHTSABER_IMMUNITY);
    }
}