package com.fiskmods.pika.lightsabers.client.gui;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.item.crystals.CrystalColor;
import com.fiskmods.pika.lightsabers.item.crystals.ItemColorCrystal;
import com.fiskmods.pika.lightsabers.world.inventory.CrystalPouchMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CrystalPouchScreen extends AbstractContainerScreen<CrystalPouchMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            AdvancedLightsabers.MODID, "textures/gui/container/crystal_pouch.png"
    );

    public CrystalPouchScreen(CrystalPouchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 150;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // 1. Hintergrund-Textur der Tasche
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // 2. Geister-Vorschau für alle leeren Kristall-Slots
        for (int i = 0; i < CrystalPouchMenu.POUCH_SLOTS; i++) {
            if (!this.menu.getSlot(i).hasItem()) {
                int slotX = x + 8 + (i % 9) * 18;
                int slotY = y + 18 + (i / 9) * 18;

                if (i < CrystalColor.values().length) {
                    CrystalColor color = CrystalColor.values()[i];
                    ItemColorCrystal crystalItem = ItemColorCrystal.getByColor(color);

                    if (crystalItem != null) {
                        // 25% Deckkraft (0.25F Alpha) wie im 1.7.10-Original
                        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.25F);
                        guiGraphics.renderFakeItem(new ItemStack(crystalItem), slotX, slotY);
                        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                    }
                }
            }
        }
    }
}