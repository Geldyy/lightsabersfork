package com.fiskmods.pika.lightsabers;

import java.util.function.Supplier;

import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ALDataComponents {

	public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister
			.createDataComponents(Registries.DATA_COMPONENT_TYPE, AdvancedLightsabers.MODID);

	public static final Supplier<DataComponentType<LightsaberData>> LIGHTSABER_DATA = COMPONENTS.registerComponentType(
			"lightsaber_data",
			builder -> builder.persistent(LightsaberData.CODEC).networkSynchronized(LightsaberData.STREAM_CODEC));

	public static final Supplier<DataComponentType<Boolean>> IS_ACTIVE = COMPONENTS.registerComponentType("is_active",
			builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

	public static final Supplier<DataComponentType<Boolean>> BLADE_MODE = COMPONENTS.registerComponentType("blade_mode",
			builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
}