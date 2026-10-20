package com.fiskmods.pika.lightsabers.compat.jei;

import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.compat.jei.ALJeiPlugin.ForgeJeiRecipe;

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

public class LightsaberForgeCategory implements IRecipeCategory<ForgeJeiRecipe> {

	public static final RecipeType<ForgeJeiRecipe> TYPE = RecipeType.create(AdvancedLightsabers.MODID,
			"lightsaber_forge", ForgeJeiRecipe.class);

	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
			"textures/gui/container/lightsaber_forge.png");

	private final IDrawable background;
	private final IDrawable icon;

	public LightsaberForgeCategory(IGuiHelper helper) {

		this.background = helper.createDrawable(TEXTURE, 12, 12, 152, 98);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
				new ItemStack(ALItems.LIGHTSABER_FORGE_LIGHT_ITEM.get()));
	}

	@Override
	public RecipeType<ForgeJeiRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("container.lightsabersfork.lightsaber_forge");
	}

	@Override
	public int getWidth() {
		return 152;
	}

	@Override
	public int getHeight() {
		return 98;
	}

	@Override
	public void draw(ForgeJeiRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView,
			net.minecraft.client.gui.GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.background.draw(guiGraphics, 0, 0);
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ForgeJeiRecipe recipe, IFocusGroup focuses) {

		builder.addSlot(RecipeIngredientRole.INPUT, 20 - 12, 17 - 12).addItemStacks(recipe.emitters());
		builder.addSlot(RecipeIngredientRole.INPUT, 20 - 12, 35 - 12).addItemStacks(recipe.switches());
		builder.addSlot(RecipeIngredientRole.INPUT, 20 - 12, 53 - 12).addItemStacks(recipe.grips());
		builder.addSlot(RecipeIngredientRole.INPUT, 20 - 12, 71 - 12).addItemStacks(recipe.pommels());

		builder.addSlot(RecipeIngredientRole.INPUT, 43 - 12, 71 - 12).addItemStack(recipe.circuitry());
		builder.addSlot(RecipeIngredientRole.INPUT, 66 - 12, 71 - 12).addItemStack(recipe.crystal());

		builder.addSlot(RecipeIngredientRole.INPUT, 89 - 12, 71 - 12).addItemStacks(recipe.focusCrystals());
		builder.addSlot(RecipeIngredientRole.INPUT, 107 - 12, 71 - 12).addItemStacks(recipe.focusCrystals());

		builder.addSlot(RecipeIngredientRole.OUTPUT, 136 - 12, 87 - 12).addItemStack(recipe.output());
	}
}