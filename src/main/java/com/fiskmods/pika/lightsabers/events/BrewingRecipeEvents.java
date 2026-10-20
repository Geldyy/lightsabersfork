package com.fiskmods.pika.lightsabers.events;

import java.util.List;

import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredItem;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID)
public class BrewingRecipeEvents {

	@SubscribeEvent
	public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
		PotionBrewing.Builder builder = event.getBuilder();

		builder.addRecipe(new BrewingRecipe(Ingredient.of(Items.POTION), Ingredient.of(Items.NETHER_STAR),
				new ItemStack(ALItems.NETHER_INFUSED_ELIXIR.get())));

		List<DeferredItem<Item>> allCrystals = List.of(ALItems.RED_CRYSTAL, ALItems.BLOOD_ORANGE_CRYSTAL,
				ALItems.AMBER_CRYSTAL, ALItems.YELLOW_CRYSTAL, ALItems.GOLD_CRYSTAL, ALItems.LIME_GREEN_CRYSTAL,
				ALItems.GREEN_CRYSTAL, ALItems.MINT_GREEN_CRYSTAL, ALItems.CYAN_CRYSTAL, ALItems.LIGHT_BLUE_CRYSTAL,
				ALItems.MEDIUM_BLUE_CRYSTAL, ALItems.DEEP_BLUE_CRYSTAL, ALItems.ARCTIC_BLUE_CRYSTAL,
				ALItems.INDIGO_CRYSTAL, ALItems.PURPLE_CRYSTAL, ALItems.MAGENTA_CRYSTAL, ALItems.PINK_CRYSTAL,
				ALItems.WHITE_CRYSTAL);

		for (DeferredItem<Item> crystal : allCrystals) {
			builder.addRecipe(new BrewingRecipe(Ingredient.of(ALItems.NETHER_INFUSED_ELIXIR.get()),
					Ingredient.of(crystal.get()), new ItemStack(ALItems.PURIFICATION_ELIXIR.get())));
		}

		builder.addRecipe(new BrewingRecipe(Ingredient.of(ALItems.PURIFICATION_ELIXIR.get()),
				Ingredient.of(ALItems.WHITE_CRYSTAL.get()), new ItemStack(ALItems.PRISTINE_PURIFICATION_ELIXIR.get())));
	}
}