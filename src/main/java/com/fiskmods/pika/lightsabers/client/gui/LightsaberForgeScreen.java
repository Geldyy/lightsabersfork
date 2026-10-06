package com.fiskmods.pika.lightsabers.client.gui;

import java.util.List;
import java.util.Locale;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.item.crystals.FocusingCrystalType;
import com.fiskmods.pika.lightsabers.item.crystals.ItemFocusingCrystal;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaberPart;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;
import com.fiskmods.pika.lightsabers.world.inventory.LightsaberForgeMenu;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LightsaberForgeScreen extends AbstractContainerScreen<LightsaberForgeMenu> {

    private static final ResourceLocation TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "textures/gui/container/lightsaber_forge.png");

    private float spinAngle = 0.0F;

    // Listen für wechselnde Ghost-Vorschau
    private static final List<Item> EMITTERS = List.of(
            ALItems.GRAFLEX_EMITTER.get(), ALItems.REDEEMER_EMITTER.get(), ALItems.MAULER_EMITTER.get(),
            ALItems.PRODIGAL_SON_EMITTER.get(), ALItems.KNIGHTED_EMITTER.get(), ALItems.DROIDEKA_EMITTER.get(),
            ALItems.FULCRUM_EMITTER.get(), ALItems.JUGGERNAUT_EMITTER.get(), ALItems.MECHANICAL_EMITTER.get(),
            ALItems.MANDALORIAN_EMITTER.get(), ALItems.FURY_EMITTER.get(), ALItems.REBEL_EMITTER.get(),
            ALItems.IMPERIAL_EMITTER.get(), ALItems.REBORN_EMITTER.get(), ALItems.VAID_MODERN_EMITTER.get(),
            ALItems.VAID_ANCIENT_EMITTER.get()
    );

    private static final List<Item> SWITCHES = List.of(
            ALItems.GRAFLEX_SWITCH_SECTION.get(), ALItems.REDEEMER_SWITCH_SECTION.get(), ALItems.MAULER_SWITCH_SECTION.get(),
            ALItems.PRODIGAL_SON_SWITCH_SECTION.get(), ALItems.KNIGHTED_SWITCH_SECTION.get(), ALItems.DROIDEKA_SWITCH_SECTION.get(),
            ALItems.FULCRUM_SWITCH_SECTION.get(), ALItems.JUGGERNAUT_SWITCH_SECTION.get(), ALItems.MECHANICAL_SWITCH_SECTION.get(),
            ALItems.MANDALORIAN_SWITCH_SECTION.get(), ALItems.FURY_SWITCH_SECTION.get(), ALItems.REBEL_SWITCH_SECTION.get(),
            ALItems.IMPERIAL_SWITCH_SECTION.get(), ALItems.REBORN_SWITCH_SECTION.get(), ALItems.VAID_MODERN_SWITCH_SECTION.get(),
            ALItems.VAID_ANCIENT_SWITCH_SECTION.get()
    );

    private static final List<Item> GRIPS = List.of(
            ALItems.GRAFLEX_GRIP.get(), ALItems.REDEEMER_GRIP.get(), ALItems.MAULER_GRIP.get(),
            ALItems.PRODIGAL_SON_GRIP.get(), ALItems.KNIGHTED_GRIP.get(), ALItems.DROIDEKA_GRIP.get(),
            ALItems.FULCRUM_GRIP.get(), ALItems.JUGGERNAUT_GRIP.get(), ALItems.MECHANICAL_GRIP.get(),
            ALItems.MANDALORIAN_GRIP.get(), ALItems.FURY_GRIP.get(), ALItems.REBEL_GRIP.get(),
            ALItems.IMPERIAL_GRIP.get(), ALItems.REBORN_GRIP.get(), ALItems.VAID_MODERN_GRIP.get(),
            ALItems.VAID_ANCIENT_GRIP.get()
    );

    private static final List<Item> POMMELS = List.of(
            ALItems.GRAFLEX_POMMEL.get(), ALItems.REDEEMER_POMMEL.get(), ALItems.MAULER_POMMEL.get(),
            ALItems.PRODIGAL_SON_POMMEL.get(), ALItems.KNIGHTED_POMMEL.get(), ALItems.DROIDEKA_POMMEL.get(),
            ALItems.FULCRUM_POMMEL.get(), ALItems.JUGGERNAUT_POMMEL.get(), ALItems.MECHANICAL_POMMEL.get(),
            ALItems.MANDALORIAN_POMMEL.get(), ALItems.FURY_POMMEL.get(), ALItems.REBEL_POMMEL.get(),
            ALItems.IMPERIAL_POMMEL.get(), ALItems.REBORN_POMMEL.get(), ALItems.VAID_MODERN_POMMEL.get(),
            ALItems.VAID_ANCIENT_POMMEL.get(), ALItems.GRAFLEX_EMITTER.get() // Emitter für Doppelklingen
    );

    private static final List<Item> COLOR_CRYSTALS = List.of(
            ALItems.DEEP_BLUE_CRYSTAL.get(), ALItems.MEDIUM_BLUE_CRYSTAL.get(), ALItems.CYAN_CRYSTAL.get(),
            ALItems.LIGHT_BLUE_CRYSTAL.get(), ALItems.GREEN_CRYSTAL.get(), ALItems.LIME_GREEN_CRYSTAL.get(),
            ALItems.MINT_GREEN_CRYSTAL.get(), ALItems.YELLOW_CRYSTAL.get(), ALItems.GOLD_CRYSTAL.get(),
            ALItems.RED_CRYSTAL.get(), ALItems.BLOOD_ORANGE_CRYSTAL.get(), ALItems.AMBER_CRYSTAL.get(),
            ALItems.PURPLE_CRYSTAL.get(), ALItems.MAGENTA_CRYSTAL.get(), ALItems.PINK_CRYSTAL.get(),
            ALItems.INDIGO_CRYSTAL.get(), ALItems.WHITE_CRYSTAL.get()
    );

    private static final List<Item> FOCUS_CRYSTALS = List.of(
            ALItems.FOCUSING_CRYSTAL_COMPRESSED.get(), ALItems.FOCUSING_CRYSTAL_CRACKED.get(),
            ALItems.FOCUSING_CRYSTAL_INVERTING.get(), ALItems.FOCUSING_CRYSTAL_FINE_CUT.get(),
            ALItems.FOCUSING_CRYSTAL_PRISMATIC.get(), ALItems.FOCUSING_CRYSTAL_SHOTO.get(),
            ALItems.FOCUSING_CRYSTAL_EXTENDED.get(), ALItems.FOCUSING_CRYSTAL_PULSE.get(),
            ALItems.FOCUSING_CRYSTAL_UNSTABLE.get()
    );

    public LightsaberForgeScreen(LightsaberForgeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 196;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // 1. GUI-Hintergrundtextur zeichnen
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // 2. Geister-Vorschau (Ghost Items) mit dynamischem Wechsel alle 1.5 Sekunden
        long timeIndex = System.currentTimeMillis() / 1500;
        for (int i = 0; i < 8; i++) {
            if (i < this.menu.slots.size()) {
                Slot slot = this.menu.getSlot(i);
                if (!slot.hasItem()) {
                    ItemStack ghost = getGhostItemForSlot(i, timeIndex);
                    if (!ghost.isEmpty()) {
                        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.25F);
                        guiGraphics.renderFakeItem(ghost, x + slot.x, y + slot.y);
                        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                    }
                }
            }
        }

        // 3. Horizontale 3D-Lichtschwert-Vorschau rendern
        renderLightsaberPreview(guiGraphics, x, y, partialTick);
    }

    private ItemStack getGhostItemForSlot(int slotIndex, long timeIndex) {
        return switch (slotIndex) {
            case 0 -> new ItemStack(EMITTERS.get((int) (timeIndex % EMITTERS.size())));
            case 1 -> new ItemStack(SWITCHES.get((int) (timeIndex % SWITCHES.size())));
            case 2 -> new ItemStack(GRIPS.get((int) (timeIndex % GRIPS.size())));
            case 3 -> new ItemStack(POMMELS.get((int) (timeIndex % POMMELS.size())));
            case 4 -> new ItemStack(ALItems.CIRCUITRY.get()); // Slot 4: Schaltkreis
            case 5 -> new ItemStack(COLOR_CRYSTALS.get((int) (timeIndex % COLOR_CRYSTALS.size()))); // Slot 5: Farbkristall
            case 6 -> new ItemStack(FOCUS_CRYSTALS.get((int) (timeIndex % FOCUS_CRYSTALS.size()))); // Slot 6: Fokus-Kristall 1
            case 7 -> new ItemStack(FOCUS_CRYSTALS.get((int) ((timeIndex + 1) % FOCUS_CRYSTALS.size()))); // Slot 7: Fokus-Kristall 2
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int titleWidth = this.font.width(this.title);
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - titleWidth / 2, 6, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.inventoryLabelY, 0x404040, false);

        ItemStack resultStack = this.menu.getSlot(8).getItem();
        if (!resultStack.isEmpty() && resultStack.getItem() instanceof ItemLightsaber) {
            LightsaberData data = resultStack.get(ALDataComponents.LIGHTSABER_DATA.get());
            if (data != null) {
                float cmLength = calculateDynamicLengthCm(data);
                String lengthString = String.format(Locale.GERMANY, "%,.3f cm", cmLength);
                guiGraphics.drawString(this.font, Component.literal(lengthString), 45, 55, 0xFFFFFF, false);
            }
        }
    }

    private float calculateDynamicLengthCm(LightsaberData data) {
        float totalHeight = 0.0F;
        totalHeight += getPartHeight(data.emitter(), PartType.EMITTER, 11.2F);
        totalHeight += getPartHeight(data.switchSection(), PartType.SWITCH_SECTION, 9.6F);
        totalHeight += getPartHeight(data.grip(), PartType.BODY, 15.8F);

        if (data.pommel() instanceof ItemLightsaberPart part && part.getPartType() == PartType.EMITTER) {
            totalHeight += getPartHeight(data.pommel(), PartType.EMITTER, 11.2F) + 21.0F;
        } else {
            totalHeight += getPartHeight(data.pommel(), PartType.POMMEL, 5.2F);
        }

        float modifier = 1.0F;
        if (data.focusCrystals() != null) {
            for (Item item : data.focusCrystals()) {
                if (item instanceof ItemFocusingCrystal crystal) {
                    FocusingCrystalType type = crystal.getCrystalType();
                    if (type == FocusingCrystalType.SHOTO) {
                        modifier *= 0.72F;
                    } else if (type == FocusingCrystalType.EXTENDED) {
                        modifier *= 1.38F;
                    } else if (type == FocusingCrystalType.FINE_CUT) {
                        modifier *= 1.08F;
                    }
                }
            }
        }

        return (totalHeight * modifier) * 0.575F;
    }

    private float getPartHeight(Item item, PartType expectedType, float defaultVal) {
        return defaultVal;
    }

    private void renderLightsaberPreview(GuiGraphics guiGraphics, int left, int top, float partialTick) {
        ItemStack resultStack = this.menu.getSlot(8).getItem();
        if (resultStack.isEmpty() || !(resultStack.getItem() instanceof ItemLightsaber)) {
            return;
        }

        ItemStack previewStack = resultStack.copy();
        previewStack.set(ALDataComponents.IS_ACTIVE.get(), true);

        LightsaberData data = previewStack.get(ALDataComponents.LIGHTSABER_DATA.get());
        boolean isDoubleBladed = data != null && data.pommel() instanceof ItemLightsaberPart part && part.getPartType() == PartType.EMITTER;

        this.spinAngle += partialTick * 0.3F;
        if (this.spinAngle >= 360.0F) {
            this.spinAngle -= 360.0F;
        }

        guiGraphics.enableScissor(left + 43, top + 22, left + 156, top + 69);

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        int posX = isDoubleBladed ? (left + 100) : (left + 144);
        int posY = top + 42;
        poseStack.translate(posX, posY, 150.0F);

        float scale = isDoubleBladed ? 68.0F : 115.0F;
        poseStack.scale(scale, -scale, scale);

        poseStack.mulPose(Axis.XP.rotationDegrees(10.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(this.spinAngle));

        Lighting.setupFor3DItems();

        Minecraft.getInstance().getItemRenderer().renderStatic(
                previewStack,
                ItemDisplayContext.FIXED,
                0xF000F0,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                guiGraphics.bufferSource(),
                Minecraft.getInstance().level,
                0
        );

        guiGraphics.flush();
        Lighting.setupForFlatItems();
        poseStack.popPose();

        guiGraphics.disableScissor();
    }
}