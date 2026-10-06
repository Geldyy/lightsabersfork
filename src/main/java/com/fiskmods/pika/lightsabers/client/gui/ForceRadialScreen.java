package com.fiskmods.pika.lightsabers.client.gui;

import java.util.List;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.client.ALKeyBinds;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.network.payload.SelectPowerPayload;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;

public class ForceRadialScreen extends Screen {

    private final List<ResourceLocation> availablePowers;
    private int hoveredIndex = -1;

    public ForceRadialScreen(List<ResourceLocation> powers) {
        super(Component.translatable("gui.lightsabersfork.select.title"));
        this.availablePowers = powers;
    }

    @Override
    public boolean isPauseScreen() {
        return false; // Spiel läuft im Hintergrund weiter!
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        if (this.minecraft == null || this.minecraft.player == null || availablePowers.isEmpty()) {
            this.onClose();
            return;
        }

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int outerRadius = 80;
        int innerRadius = 30;

        double dX = mouseX - centerX;
        double dY = mouseY - centerY;
        double distSqr = (dX * dX) + (dY * dY);

        // Winkel der Maus berechnen (0 bis 360 Grad, 0 ist rechts)
        double mouseAngle = Math.toDegrees(Math.atan2(dY, dX));
        if (mouseAngle < 0) {
            mouseAngle += 360.0;
        }

        int count = availablePowers.size();
        double segmentAngle = 360.0 / count;

        // Bestimmen, Welches Segment die Maus berührt
        if (distSqr >= innerRadius * innerRadius && distSqr <= outerRadius * outerRadius * 2) {
            this.hoveredIndex = (int) Math.floor(mouseAngle / segmentAngle) % count;
        } else {
            this.hoveredIndex = -1;
        }

        // 1. Hintergrund-Kreis & Segmente rendern
        graphics.fill(centerX - outerRadius, centerY - outerRadius, centerX + outerRadius, centerY + outerRadius, 0x44000000);
        graphics.renderOutline(centerX - outerRadius - 1, centerY - outerRadius - 1, (outerRadius * 2) + 2, (outerRadius * 2) + 2, 0xFF555555);

        PlayerForceData data = PlayerForceData.get(this.minecraft.player);
        ResourceLocation currentlyEquipped = data != null ? data.getSelectedPower() : null;

        // 2. Die einzelnen Fähigkeits-Knoten im Kreis rendern
        for (int i = 0; i < count; i++) {
            ResourceLocation powerId = availablePowers.get(i);
            double midAngle = Math.toRadians((i + 0.5) * segmentAngle);

            int iconX = (int) (centerX + Math.cos(midAngle) * 55);
            int iconY = (int) (centerY + Math.sin(midAngle) * 55);

            boolean isHovered = (i == this.hoveredIndex);
            boolean isSelected = powerId.equals(currentlyEquipped);

            // Farben festlegen
            int boxColor = isHovered ? 0xFF00BFFF : (isSelected ? 0xFF00FF7F : 0x88222222);
            int textColor = isHovered ? 0xFFFFFFFF : (isSelected ? 0xFF00FF7F : 0xFFAAAAAA);

            String name = powerId.getPath().replace("_", " ").toUpperCase();
            int textWidth = this.font.width(name);

            // Kasten für die Fähigkeit
            int boxWidth = textWidth + 12;
            int boxHeight = 16;
            int bx = iconX - (boxWidth / 2);
            int by = iconY - (boxHeight / 2);

            graphics.fill(bx, by, bx + boxWidth, by + boxHeight, isHovered ? 0xAA003366 : 0xAA000000);
            graphics.renderOutline(bx, by, boxWidth, boxHeight, boxColor);

            // Text
            graphics.drawString(this.font, name, bx + 6, by + 4, textColor, true);
        }

        // 3. Zentrum-Kreis (Zeigt den Namen der anvisierten Kraft in der Mitte)
        graphics.fill(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius, 0xCC000000);
        graphics.renderOutline(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2, 0xFF00BFFF);

        if (this.hoveredIndex >= 0 && this.hoveredIndex < count) {
            String hoveredName = availablePowers.get(this.hoveredIndex).getPath().replace("_", " ").toUpperCase();
            int hWidth = this.font.width(hoveredName);
            graphics.drawString(this.font, hoveredName, centerX - (hWidth / 2), centerY - 4, 0xFF00BFFF, true);
        } else {
            graphics.drawString(this.font, "SELECT", centerX - (this.font.width("SELECT") / 2), centerY - 4, 0x88FFFFFF, true);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.hoveredIndex >= 0 && this.hoveredIndex < availablePowers.size()) {
            selectAndClose(availablePowers.get(this.hoveredIndex));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        // Wenn R (oder die zugewiesene Quick-Select-Taste) losgelassen wird, wähle das hovered Item und schließe das Rad
        if (ALKeyBinds.CYCLE_FORCE.matches(keyCode, scanCode)) {
            if (this.hoveredIndex >= 0 && this.hoveredIndex < availablePowers.size()) {
                selectAndClose(availablePowers.get(this.hoveredIndex));
            } else {
                this.onClose();
            }
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void selectAndClose(ResourceLocation chosenPower) {
        PacketDistributor.sendToServer(new SelectPowerPayload(chosenPower));
        ForceHudOverlay.wakeUp(); // HUD aktivieren
        this.onClose();
    }
}