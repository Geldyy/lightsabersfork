package com.fiskmods.pika.lightsabers.compat.jei;

import java.util.ArrayList;
import java.util.List;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.gui.DisassemblyStationScreen;
import com.fiskmods.pika.lightsabers.client.gui.LightsaberForgeScreen;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class ALJeiPlugin implements IModPlugin {

	public static final ResourceLocation PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
			"jei_plugin");

	public record ForgeJeiRecipe(List<ItemStack> emitters, List<ItemStack> switches, List<ItemStack> grips,
			List<ItemStack> pommels, ItemStack circuitry, ItemStack crystal, List<ItemStack> focusCrystals,
			ItemStack output) {
	}

	public record DisassemblyJeiRecipe(ItemStack inputSaber, List<ItemStack> outputs) {
	}

	@Override
	public ResourceLocation getPluginUid() {
		return PLUGIN_UID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ALItems.LIGHTSABER.get(),
				new mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter<ItemStack>() {
					@Override
					public Object getSubtypeData(ItemStack stack,
							mezz.jei.api.ingredients.subtypes.UidContext context) {
						LightsaberData data = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
						return data != null ? data.colorCrystal().toString() + "_" + data.emitter().toString() : "";
					}

					@Override
					public String getLegacyStringSubtypeInfo(ItemStack stack,
							mezz.jei.api.ingredients.subtypes.UidContext context) {
						LightsaberData data = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
						return data != null ? data.colorCrystal().toString() + "_" + data.emitter().toString() : "";
					}
				});
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		var guiHelper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(new LightsaberForgeCategory(guiHelper),
				new DisassemblyStationCategory(guiHelper));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ALItems.LIGHTSABER_FORGE_LIGHT_ITEM.get()),
				LightsaberForgeCategory.TYPE);
		registration.addRecipeCatalyst(new ItemStack(ALItems.DISASSEMBLY_STATION.get()),
				DisassemblyStationCategory.TYPE);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(LightsaberForgeScreen.class, 108, 87, 22, 16, LightsaberForgeCategory.TYPE);
		registration.addRecipeClickArea(DisassemblyStationScreen.class, 42, 35, 24, 17,
				DisassemblyStationCategory.TYPE);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		List<ItemStack> allEmitters = List.of(new ItemStack(ALItems.GRAFLEX_EMITTER.get()),
				new ItemStack(ALItems.REDEEMER_EMITTER.get()), new ItemStack(ALItems.MAULER_EMITTER.get()),
				new ItemStack(ALItems.PRODIGAL_SON_EMITTER.get()), new ItemStack(ALItems.KNIGHTED_EMITTER.get()),
				new ItemStack(ALItems.DROIDEKA_EMITTER.get()), new ItemStack(ALItems.FULCRUM_EMITTER.get()),
				new ItemStack(ALItems.JUGGERNAUT_EMITTER.get()), new ItemStack(ALItems.MECHANICAL_EMITTER.get()),
				new ItemStack(ALItems.MANDALORIAN_EMITTER.get()), new ItemStack(ALItems.FURY_EMITTER.get()),
				new ItemStack(ALItems.REBEL_EMITTER.get()), new ItemStack(ALItems.IMPERIAL_EMITTER.get()),
				new ItemStack(ALItems.REBORN_EMITTER.get()), new ItemStack(ALItems.VAID_MODERN_EMITTER.get()),
				new ItemStack(ALItems.VAID_ANCIENT_EMITTER.get()));

		List<ItemStack> allSwitches = List.of(new ItemStack(ALItems.GRAFLEX_SWITCH_SECTION.get()),
				new ItemStack(ALItems.REDEEMER_SWITCH_SECTION.get()),
				new ItemStack(ALItems.MAULER_SWITCH_SECTION.get()),
				new ItemStack(ALItems.PRODIGAL_SON_SWITCH_SECTION.get()),
				new ItemStack(ALItems.KNIGHTED_SWITCH_SECTION.get()),
				new ItemStack(ALItems.DROIDEKA_SWITCH_SECTION.get()),
				new ItemStack(ALItems.FULCRUM_SWITCH_SECTION.get()),
				new ItemStack(ALItems.JUGGERNAUT_SWITCH_SECTION.get()),
				new ItemStack(ALItems.MECHANICAL_SWITCH_SECTION.get()),
				new ItemStack(ALItems.MANDALORIAN_SWITCH_SECTION.get()),
				new ItemStack(ALItems.FURY_SWITCH_SECTION.get()), new ItemStack(ALItems.REBEL_SWITCH_SECTION.get()),
				new ItemStack(ALItems.IMPERIAL_SWITCH_SECTION.get()),
				new ItemStack(ALItems.REBORN_SWITCH_SECTION.get()),
				new ItemStack(ALItems.VAID_MODERN_SWITCH_SECTION.get()),
				new ItemStack(ALItems.VAID_ANCIENT_SWITCH_SECTION.get()));

		List<ItemStack> allGrips = List.of(new ItemStack(ALItems.GRAFLEX_GRIP.get()),
				new ItemStack(ALItems.REDEEMER_GRIP.get()), new ItemStack(ALItems.MAULER_GRIP.get()),
				new ItemStack(ALItems.PRODIGAL_SON_GRIP.get()), new ItemStack(ALItems.KNIGHTED_GRIP.get()),
				new ItemStack(ALItems.DROIDEKA_GRIP.get()), new ItemStack(ALItems.FULCRUM_GRIP.get()),
				new ItemStack(ALItems.JUGGERNAUT_GRIP.get()), new ItemStack(ALItems.MECHANICAL_GRIP.get()),
				new ItemStack(ALItems.MANDALORIAN_GRIP.get()), new ItemStack(ALItems.FURY_GRIP.get()),
				new ItemStack(ALItems.REBEL_GRIP.get()), new ItemStack(ALItems.IMPERIAL_GRIP.get()),
				new ItemStack(ALItems.REBORN_GRIP.get()), new ItemStack(ALItems.VAID_MODERN_GRIP.get()),
				new ItemStack(ALItems.VAID_ANCIENT_GRIP.get()));

		List<ItemStack> allPommels = new ArrayList<>(List.of(new ItemStack(ALItems.GRAFLEX_POMMEL.get()),
				new ItemStack(ALItems.REDEEMER_POMMEL.get()), new ItemStack(ALItems.MAULER_POMMEL.get()),
				new ItemStack(ALItems.PRODIGAL_SON_POMMEL.get()), new ItemStack(ALItems.KNIGHTED_POMMEL.get()),
				new ItemStack(ALItems.DROIDEKA_POMMEL.get()), new ItemStack(ALItems.FULCRUM_POMMEL.get()),
				new ItemStack(ALItems.JUGGERNAUT_POMMEL.get()), new ItemStack(ALItems.MECHANICAL_POMMEL.get()),
				new ItemStack(ALItems.MANDALORIAN_POMMEL.get()), new ItemStack(ALItems.FURY_POMMEL.get()),
				new ItemStack(ALItems.REBEL_POMMEL.get()), new ItemStack(ALItems.IMPERIAL_POMMEL.get()),
				new ItemStack(ALItems.REBORN_POMMEL.get()), new ItemStack(ALItems.VAID_MODERN_POMMEL.get()),
				new ItemStack(ALItems.VAID_ANCIENT_POMMEL.get())));

		List<ItemStack> focusCrystals = List.of(new ItemStack(ALItems.FOCUSING_CRYSTAL_COMPRESSED.get()),
				new ItemStack(ALItems.FOCUSING_CRYSTAL_CRACKED.get()),
				new ItemStack(ALItems.FOCUSING_CRYSTAL_INVERTING.get()),
				new ItemStack(ALItems.FOCUSING_CRYSTAL_FINE_CUT.get()),
				new ItemStack(ALItems.FOCUSING_CRYSTAL_PRISMATIC.get()),
				new ItemStack(ALItems.FOCUSING_CRYSTAL_SHOTO.get()),
				new ItemStack(ALItems.FOCUSING_CRYSTAL_EXTENDED.get()),
				new ItemStack(ALItems.FOCUSING_CRYSTAL_PULSE.get()),
				new ItemStack(ALItems.FOCUSING_CRYSTAL_UNSTABLE.get()));

		List<Item> crystals = List.of(ALItems.RED_CRYSTAL.get(), ALItems.BLOOD_ORANGE_CRYSTAL.get(),
				ALItems.AMBER_CRYSTAL.get(), ALItems.YELLOW_CRYSTAL.get(), ALItems.GOLD_CRYSTAL.get(),
				ALItems.LIME_GREEN_CRYSTAL.get(), ALItems.GREEN_CRYSTAL.get(), ALItems.MINT_GREEN_CRYSTAL.get(),
				ALItems.CYAN_CRYSTAL.get(), ALItems.LIGHT_BLUE_CRYSTAL.get(), ALItems.MEDIUM_BLUE_CRYSTAL.get(),
				ALItems.DEEP_BLUE_CRYSTAL.get(), ALItems.ARCTIC_BLUE_CRYSTAL.get(), ALItems.INDIGO_CRYSTAL.get(),
				ALItems.PURPLE_CRYSTAL.get(), ALItems.MAGENTA_CRYSTAL.get(), ALItems.PINK_CRYSTAL.get(),
				ALItems.WHITE_CRYSTAL.get());

		List<ForgeJeiRecipe> forgeRecipes = new ArrayList<>();
		List<DisassemblyJeiRecipe> disassemblyRecipes = new ArrayList<>();

		for (Item crystal : crystals) {
			ItemStack saber = new ItemStack(ALItems.LIGHTSABER.get());
			LightsaberData data = new LightsaberData(crystal, List.of(), ALItems.GRAFLEX_EMITTER.get(),
					ALItems.GRAFLEX_SWITCH_SECTION.get(), ALItems.GRAFLEX_GRIP.get(), ALItems.GRAFLEX_POMMEL.get());
			saber.set(ALDataComponents.LIGHTSABER_DATA.get(), data);

			forgeRecipes.add(new ForgeJeiRecipe(allEmitters, allSwitches, allGrips, allPommels,
					new ItemStack(ALItems.CIRCUITRY.get()), new ItemStack(crystal), focusCrystals, saber));

			disassemblyRecipes.add(new DisassemblyJeiRecipe(saber,
					List.of(new ItemStack(ALItems.GRAFLEX_EMITTER.get()),
							new ItemStack(ALItems.GRAFLEX_SWITCH_SECTION.get()),
							new ItemStack(ALItems.GRAFLEX_GRIP.get()), new ItemStack(ALItems.GRAFLEX_POMMEL.get()),
							new ItemStack(ALItems.CIRCUITRY.get()), new ItemStack(crystal))));
		}

		registration.addRecipes(LightsaberForgeCategory.TYPE, forgeRecipes);
		registration.addRecipes(DisassemblyStationCategory.TYPE, disassemblyRecipes);
	}
}