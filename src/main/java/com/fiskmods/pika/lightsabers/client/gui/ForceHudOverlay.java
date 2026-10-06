package com.fiskmods.pika.lightsabers.client.gui;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class ForceHudOverlay {

    // Speichert den Zeitpunkt der letzten Interaktion in Millisekunden
    private static long lastActiveTime = 0;

    // Wird von den Inputs aufgerufen, um das HUD aufzuwecken
    public static void wakeUp() {
        lastActiveTime = System.currentTimeMillis();
    }

    // Hilfsmethode, um Transparenz (Alpha) auf einen Hex-Farbcode anzuwenden
    private static int applyAlpha(int color, float alphaMult) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        a = (int) (a * alphaMult);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || mc.options.hideGui) return;

        PlayerForceData data = PlayerForceData.get(player);
        
        // Render den HUD nur, wenn der Spieler Macht-Sensitiv ist
        if (data != null && data.isForceSensitive()) {
            
            // --- TIMER LOGIK FÜR FADE-OUT ---
            long timeSinceActive = System.currentTimeMillis() - lastActiveTime;
            
            // Nach 5 Sekunden komplett unsichtbar -> gar nicht erst rendern
            if (timeSinceActive > 5000) return;

            // Transparenz berechnen: Nach 4 Sekunden beginnt das Ausblenden
            float alphaMult = 1.0f;
            if (timeSinceActive > 4000) {
                alphaMult = 1.0f - ((timeSinceActive - 4000) / 1000.0f);
            }
            
            GuiGraphics graphics = event.getGuiGraphics();
            Font font = mc.font;
            
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            // 1. Energie-Balken Zeichnen
            int barWidth = 100;
            int barHeight = 6;
            int x = screenWidth - barWidth - 10;
            int y = screenHeight - 20;

            float currentEnergy = data.getForceEnergy();
            float maxEnergy = data.getMaxForceEnergy();
            if (maxEnergy <= 0) maxEnergy = 100.0f;
            
            float fillRatio = currentEnergy / maxEnergy;
            int fillWidth = (int) (barWidth * fillRatio);

            // Farben mit dynamischem Fade-Out-Alpha
            int bgColor = applyAlpha(0xAA000000, alphaMult);
            int fillColor = applyAlpha(0xFF00BFFF, alphaMult);
            int outlineColor = applyAlpha(0xFF888888, alphaMult);

            graphics.fill(x, y, x + barWidth, y + barHeight, bgColor);
            graphics.fill(x, y, x + fillWidth, y + barHeight, fillColor);
            graphics.renderOutline(x - 1, y - 1, barWidth + 2, barHeight + 2, outlineColor);

            // 2. Aktive Kraft anzeigen
            ResourceLocation selectedPower = data.getSelectedPower();
            if (selectedPower != null) {
                String powerName = selectedPower.getPath().replace("_", " ").toUpperCase();
                int textWidth = font.width(powerName);
                int textColor = applyAlpha(0xFFFFFFFF, alphaMult);
                
                // Da drawString den Alpha-Wert des Colors respektiert, fadet auch der Text weich aus
                graphics.drawString(font, powerName, x + (barWidth / 2) - (textWidth / 2), y - 10, textColor, true);
            } else {
                String empty = "NO POWER SELECTED";
                int textWidth = font.width(empty);
                int textColor = applyAlpha(0xFFAAAAAA, alphaMult);
                graphics.drawString(font, empty, x + (barWidth / 2) - (textWidth / 2), y - 10, textColor, true);
            }
        }
    }
}