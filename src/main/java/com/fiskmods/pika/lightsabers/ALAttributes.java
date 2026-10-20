package com.fiskmods.pika.lightsabers;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ALAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE,
			AdvancedLightsabers.MODID);

	public static final DeferredHolder<Attribute, Attribute> LIGHTSABER_IMMUNITY = ATTRIBUTES.register(
			"lightsaber_immunity",
			() -> new RangedAttribute("attribute.lightsabersfork.lightsaber_immunity", 0.0D, 0.0D, 1.0D)
					.setSyncable(true));
}