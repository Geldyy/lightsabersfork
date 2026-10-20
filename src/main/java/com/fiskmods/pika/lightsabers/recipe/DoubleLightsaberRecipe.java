package com.fiskmods.pika.lightsabers.recipe;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DoubleLightsaberRecipe extends CustomRecipe {

	public DoubleLightsaberRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack top = ItemStack.EMPTY;
		ItemStack bottom = ItemStack.EMPTY;
		int count = 0;

		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (!stack.isEmpty()) {
				if (!(stack.getItem() instanceof ItemLightsaber))
					return false;
				count++;

				int row = i / input.width();
				int col = i % input.width();

				int belowIndex = (row + 1) * input.width() + col;
				if (belowIndex < input.size()) {
					ItemStack belowStack = input.getItem(belowIndex);
					if (belowStack.getItem() instanceof ItemLightsaber) {
						top = stack;
						bottom = belowStack;
					}
				}
			}
		}

		return count == 2 && !top.isEmpty() && !bottom.isEmpty();
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack top = ItemStack.EMPTY;
		ItemStack bottom = ItemStack.EMPTY;

		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemLightsaber) {
				int row = i / input.width();
				int col = i % input.width();
				int belowIndex = (row + 1) * input.width() + col;
				if (belowIndex < input.size()) {
					ItemStack belowStack = input.getItem(belowIndex);
					if (belowStack.getItem() instanceof ItemLightsaber) {
						top = stack;
						bottom = belowStack;
						break;
					}
				}
			}
		}

		if (top.isEmpty() || bottom.isEmpty())
			return ItemStack.EMPTY;

		ItemStack doubleSaber = new ItemStack(ALItems.LIGHTSABER.get());
		LightsaberData topData = top.get(ALDataComponents.LIGHTSABER_DATA.get());
		LightsaberData bottomData = bottom.get(ALDataComponents.LIGHTSABER_DATA.get());

		if (topData != null && bottomData != null) {
			LightsaberData doubleData = new LightsaberData(topData.colorCrystal(), topData.focusCrystals(),
					topData.emitter(), topData.switchSection(), topData.grip(), bottomData.emitter(),
					bottomData.switchSection(), bottomData.grip(), bottomData.colorCrystal(),
					bottomData.focusCrystals());
			doubleSaber.set(ALDataComponents.LIGHTSABER_DATA.get(), doubleData);
		}

		return doubleSaber;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 1 && height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ALRecipeSerializers.DOUBLE_LIGHTSABER.get();
	}
}