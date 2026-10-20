package com.fiskmods.pika.lightsabers.loot;

import java.util.function.Supplier;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.mojang.serialization.MapCodec;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ALLootModifiers {

	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister
			.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AdvancedLightsabers.MODID);

	public static final Supplier<MapCodec<AddLootModifier>> ADD_LOOT = LOOT_MODIFIERS.register("add_loot",
			() -> AddLootModifier.CODEC);
}