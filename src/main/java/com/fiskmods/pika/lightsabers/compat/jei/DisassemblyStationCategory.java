package com.fiskmods.pika.lightsabers.compat.jei;

import java.util.List;

import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.compat.jei.ALJeiPlugin.DisassemblyJeiRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DisassemblyStationCategory implements IRecipeCategory<DisassemblyJeiRecipe> {

	public static final RecipeType<DisassemblyJeiRecipe> TYPE = RecipeType.create(AdvancedLightsabers.MODID,
			"disassembly_station", DisassemblyJeiRecipe.class);

	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
			"textures/gui/container/disassembly_station.png");

	private final IDrawable background;
	private final IDrawable icon;

	public DisassemblyStationCategory(IGuiHelper helper) {

		this.background = helper.createDrawable(TEXTURE, 10, 12, 156, 66);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
				new ItemStack(ALItems.DISASSEMBLY_STATION.get()));
	}

	@Override
	public RecipeType<DisassemblyJeiRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("container.lightsabersfork.disassembly_station");
	}

	@Override
	public int getWidth() {
		return 156;
	}

	@Override
	public int getHeight() {
		return 66;
	}

	@Override
	public void draw(DisassemblyJeiRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView,
			net.minecraft.client.gui.GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.background.draw(guiGraphics, 0, 0);
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DisassemblyJeiRecipe recipe, IFocusGroup focuses) {

		builder.addSlot(RecipeIngredientRole.INPUT, 16 - 10, 18 - 12).addItemStack(recipe.inputSaber());

		builder.addSlot(RecipeIngredientRole.CATALYST, 16 - 10, 54 - 12).addItemStacks(
				List.of(new ItemStack(Items.COAL), new ItemStack(Items.CHARCOAL), new ItemStack(Items.BLAZE_ROD)));

		List<ItemStack> outputs = recipe.outputs();
		for (int i = 0; i < outputs.size() && i < 15; i++) {
			int slotX = (72 - 10) + (i % 5) * 18;
			int slotY = (18 - 12) + (i / 5) * 18;
			builder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addItemStack(outputs.get(i));
		}
	}
}