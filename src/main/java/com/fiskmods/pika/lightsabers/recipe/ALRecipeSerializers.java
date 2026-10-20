package com.fiskmods.pika.lightsabers.recipe;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ALRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
			.create(Registries.RECIPE_SERIALIZER, AdvancedLightsabers.MODID);

	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<DoubleLightsaberRecipe>> DOUBLE_LIGHTSABER = RECIPE_SERIALIZERS
			.register("crafting_special_double_lightsaber",
					() -> new SimpleCraftingRecipeSerializer<>(DoubleLightsaberRecipe::new));
}