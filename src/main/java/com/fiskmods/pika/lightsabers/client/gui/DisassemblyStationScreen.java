package com.fiskmods.pika.lightsabers.client.gui;

import java.util.List;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.world.inventory.DisassemblyStationMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DisassemblyStationScreen extends AbstractContainerScreen<DisassemblyStationMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            AdvancedLightsabers.MODID, "textures/gui/container/disassembly_station.png"
    );

    // Brennstoff-Rotation für Slot 1
    private static final List<Item> FUELS = List.of(
            Items.COAL, Items.CHARCOAL, Items.BLAZE_ROD, Items.LAVA_BUCKET, Items.COAL_BLOCK
    );

    public DisassemblyStationScreen(DisassemblyStationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 168;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // 1. Hintergrund-GUI
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // 2. Flammen-Animation (Brennstoff)
        if (this.menu.isBurning()) {
            int burn = this.menu.getBurnProgress();
            guiGraphics.blit(TEXTURE, x + 17, y + 37 + 12 - burn, 176, 12 - burn, 14, burn + 2);
        }

        // 3. Pfeil-Animation (Zerlegung)
        int cook = this.menu.getCookProgress();
        guiGraphics.blit(TEXTURE, x + 39, y + 36, 176, 14, cook + 1, 16);

        long timeIndex = System.currentTimeMillis() / 1500;

        // 4. Geister-Vorschau: Slot 0 (Lichtschwert mit vollständigen Modelldaten)
        if (this.menu.slots.size() > 0) {
            Slot inputSlot = this.menu.getSlot(0);
            if (!inputSlot.hasItem()) {
                ItemStack ghostSaber = createGhostLightsaber(timeIndex);
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.25F);
                guiGraphics.renderFakeItem(ghostSaber, x + inputSlot.x, y + inputSlot.y);
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        // 5. Geister-Vorschau: Slot 1 (Brennstoff)
        if (this.menu.slots.size() > 1) {
            Slot fuelSlot = this.menu.getSlot(1);
            if (!fuelSlot.hasItem()) {
                ItemStack ghostFuel = new ItemStack(FUELS.get((int) (timeIndex % FUELS.size())));
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.25F);
                guiGraphics.renderFakeItem(ghostFuel, x + fuelSlot.x, y + fuelSlot.y);
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    private ItemStack createGhostLightsaber(long timeIndex) {
        ItemStack saber = new ItemStack(ALItems.LIGHTSABER.get());
        Item color = (timeIndex % 2 == 0) ? ALItems.GREEN_CRYSTAL.get() : ALItems.RED_CRYSTAL.get();
        Item emitter = (timeIndex % 2 == 0) ? ALItems.GRAFLEX_EMITTER.get() : ALItems.PRODIGAL_SON_EMITTER.get();
        Item switchPart = (timeIndex % 2 == 0) ? ALItems.GRAFLEX_SWITCH_SECTION.get() : ALItems.PRODIGAL_SON_SWITCH_SECTION.get();
        Item grip = (timeIndex % 2 == 0) ? ALItems.GRAFLEX_GRIP.get() : ALItems.PRODIGAL_SON_GRIP.get();
        Item pommel = (timeIndex % 2 == 0) ? ALItems.GRAFLEX_POMMEL.get() : ALItems.PRODIGAL_SON_POMMEL.get();

        LightsaberData data = new LightsaberData(color, List.of(), emitter, switchPart, grip, pommel);
        saber.set(ALDataComponents.LIGHTSABER_DATA.get(), data);
        return saber;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.inventoryLabelY, 0x404040, false);
    }
}