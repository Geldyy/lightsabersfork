package com.fiskmods.pika.lightsabers.client.render.force;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillNode;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class MeditationUIRenderer {

    // Registriert den Font-Pfad zu assets/lightsabersfork/font/aurebesh.json
    private static final ResourceLocation AUREBESH_FONT = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "aurebesh");

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        // GEÄNDERT: Wir brechen nicht mehr in Phase 2 & 3 ab, sondern rendern das Hauptmenü weiter (aber transparent)!
        if (!MeditationStateManager.isMeditating()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        GuiGraphics graphics = event.getGuiGraphics();
        Font font = mc.font;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int yPos = 40; 

        TreeCategory hovered = MeditationStateManager.getActiveCategory();

        int centerX = screenWidth / 2;
        // Abstand erhöht (von 90 auf 120), da der Text jetzt deutlich breiter in den Raum ragt!
        int offset = 120; 

        // NEU: Transparenz-Steuerung für das Top-Menü
        boolean isCategorySelection = MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.CATEGORY_SELECTION;
        float topAlpha = isCategorySelection ? 1.0F : 0.4F;

        drawCategory(graphics, font, TreeCategory.LIGHTSABER, centerX - offset, yPos, hovered == TreeCategory.LIGHTSABER, topAlpha);
        drawCategory(graphics, font, TreeCategory.FORCE, centerX + offset, yPos, hovered == TreeCategory.FORCE, topAlpha);

        // NEU: SKILL DETAILS RENDERN (Nur in Phase 3 aktiv)
        if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.NODE_SELECTION) {
            ResourceLocation selectedId = MeditationStateManager.getSelectedNodeId();
            if (selectedId != null) {
                SkillNode node = SkillTreeRegistry.getNode(selectedId);
                if (node != null) {
                    renderNodeDetails(graphics, font, node, screenWidth, screenHeight);
                }
            }
        }
    }

    // GEÄNDERT: Nimmt nun globalAlpha für das sanfte Ausblenden an
    private static void drawCategory(GuiGraphics graphics, Font font, TreeCategory category, int x, int y, boolean isSelected, float globalAlpha) {
        String key = category == TreeCategory.LIGHTSABER ? "gui.lightsabersfork.category.lightsaber" : "gui.lightsabersfork.category.force";
        Component text = Component.translatable(key);
        
        // Farben basierend auf Auswahl-Status & dynamischer Transparenz
        int aMain = (int)((isSelected ? 255 : 136) * globalAlpha) << 24;
        int aAure = (int)((isSelected ? 85 : 34) * globalAlpha) << 24;
        int aLine = (int)((isSelected ? 255 : 68) * globalAlpha) << 24;
        int aSub  = (int)((isSelected ? 255 : 102) * globalAlpha) << 24;
        
        int colorMain = aMain | (isSelected ? 0xF5F5DC : 0xAAAAAA);
        int colorAurebesh = aAure | (isSelected ? 0xFFFFFF : 0xAAAAAA);
        int colorLine = aLine | (isSelected ? 0xFFFFFF : 0xAAAAAA);
        int colorSub = aSub | (isSelected ? 0xCCCCCC : 0x888888);
        
        int width = font.width(text);
        
        // 1. Aurebesh Text (nutzt direkt die Aurebesh-Font-Definition)
        Component aurebeshText = Component.translatable(key).withStyle(Style.EMPTY.withFont(AUREBESH_FONT));
        
        float aurebeshScale = 0.8F;
        graphics.pose().pushPose();
        graphics.pose().scale(aurebeshScale, aurebeshScale, aurebeshScale);
        
        int aurebeshWidth = font.width(aurebeshText);
        float scaledX = (x - (aurebeshWidth * aurebeshScale) / 2) / aurebeshScale;
        // Da der Haupttext nun viel massiver ist, schieben wir das Aurebesh etwas höher
        float scaledY = (y - 12) / aurebeshScale; 
        
        graphics.drawString(font, aurebeshText, (int)scaledX, (int)scaledY, colorAurebesh, false);
        graphics.pose().popPose();

        // 2. Haupttext (Klartext) - DEUTLICH GRÖSSER SKALIERT!
        float mainScale = 1.8F; // 1.8-fache Größe für den extremen Film-Look
        graphics.pose().pushPose();
        graphics.pose().scale(mainScale, mainScale, mainScale);
        
        // Wir müssen die Start-X-Koordinate der Skalierung anpassen, damit es weiterhin perfekt zentriert ist
        float mainScaledX = (x - (width * mainScale) / 2) / mainScale;
        float mainScaledY = y / mainScale;
        
        graphics.drawString(font, text, (int)mainScaledX, (int)mainScaledY, colorMain, true);
        graphics.pose().popPose();

        // 3. Die Linie unter dem Text
        // Die Linienbreite richtet sich nun dynamisch nach der massiven Größe des skalierten Textes
        int halfWidth = (int)((width * mainScale) / 2) + 15; 
        int lineY = y + 20; // Die Linie rückt weiter nach unten, da der Text jetzt höher ist

        if (isSelected) {
            // Ausgewählt: Geteilte Linie mit Leuchtpunkt in der Mitte
            graphics.fill(x - halfWidth, lineY, x - 4, lineY + 1, colorLine);
            graphics.fill(x + 4, lineY, x + halfWidth, lineY + 1, colorLine);
            graphics.fill(x - 2, lineY - 1, x + 2, lineY + 2, colorLine);
        } else {
            // Inaktiv: Durchgehende, dunklere Linie ohne Punkt
            graphics.fill(x - halfWidth, lineY, x + halfWidth, lineY + 1, colorLine);
        }

        // 4. Subtext ("X FÄHIGKEITEN VERFÜGBAR")
        int availableSkills = category == TreeCategory.LIGHTSABER ? 1 : 0; // Platzhalter-Wert
        
        String translatableKey = availableSkills == 1 ? "gui.lightsabersfork.category.available.singular" : "gui.lightsabersfork.category.available.plural";
        Component subtext = Component.translatable(translatableKey, availableSkills);
        
        float subScale = 0.7F;
        graphics.pose().pushPose();
        graphics.pose().scale(subScale, subScale, subScale);
        
        int subWidth = font.width(subtext);
        float subScaledX = (x - (subWidth * subScale) / 2) / subScale;
        // Der Subtext orientiert sich dynamisch an der neuen, tieferen Position der Linie
        float subScaledY = (lineY + 6) / subScale; 
        
        graphics.drawString(font, subtext, (int)subScaledX, (int)subScaledY, colorSub, true);
        graphics.pose().popPose();
    }

    // =========================================================================
    // NEU: JEDI SURVIVOR UI FÜR FÄHIGKEITEN
    // =========================================================================
    private static void renderNodeDetails(GuiGraphics graphics, Font font, SkillNode node, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        PlayerForceData data = mc.player != null ? PlayerForceData.get(mc.player) : null;
        
        // NEU: Dynamische Punkte-Auslese je nach aktiver Kategorie
        TreeCategory activeCategory = MeditationStateManager.getActiveCategory();
        int availablePoints = 0;
        Component pointsLabel;
        
        if (activeCategory == TreeCategory.FORCE) {
            availablePoints = data != null ? data.getForcePoints() : 0;
            pointsLabel = Component.translatable("gui.lightsabersfork.skilltree.force_points_available"); // "MACHT-PUNKTE VERFÜGBAR"
        } else {
            availablePoints = data != null ? data.getAbilityPoints() : 0;
            pointsLabel = Component.translatable("gui.lightsabersfork.skilltree.ability_points_available"); // "FÄHIGKEITEN-PUNKTE VERFÜGBAR"
        }
        
        String pointsStr = String.valueOf(availablePoints);

        int leftX = 50;
        int bottomY = screenHeight - 60;
        
        // ... (Rest der Methode bleibt gleich: Zeichnen von Zahl, Text, Skill-Name, Kosten etc.)

        // Große Zahl (Verfügbare Punkte)
        graphics.pose().pushPose();
        graphics.pose().scale(2.5F, 2.5F, 2.5F);
        graphics.drawString(font, pointsStr, (int)(leftX / 2.5F), (int)((bottomY - 10) / 2.5F), 0xFFFFFFFF, true);
        graphics.pose().popPose();

        // Kleiner Subtext (Verfügbare Punkte)
        graphics.pose().pushPose();
        graphics.pose().scale(0.7F, 0.7F, 0.7F);
        graphics.drawString(font, pointsLabel, (int)(leftX / 0.7F), (int)((bottomY + 18) / 0.7F), 0xFFAAAAAA, true);
        graphics.pose().popPose();

        // --- 2. SKILL DETAILS (Unten Mitte/Rechts) ---
        int centerX = screenWidth / 2 + 30;
        
        // Skill Name (Groß, Weiß)
        Component title = node.getTitle();
        graphics.pose().pushPose();
        graphics.pose().scale(1.5F, 1.5F, 1.5F);
        graphics.drawString(font, title, (int)(centerX / 1.5F), (int)((bottomY - 15) / 1.5F), 0xFFFFFFFF, true);
        graphics.pose().popPose();

        // Skill Beschreibung (Klein, Grau)
        Component desc = node.getDescription();
        graphics.pose().pushPose();
        graphics.pose().scale(0.8F, 0.8F, 0.8F);
        graphics.drawString(font, desc, (int)(centerX / 0.8F), (int)((bottomY + 5) / 0.8F), 0xFFAAAAAA, true);
        graphics.pose().popPose();

        // --- NEU: KOSTEN- UND SPERR-PRÜFUNG ---
        boolean isUnlocked = data != null && data.hasSkill(node.getId());
        boolean canUnlock = data != null && node.canUnlock(mc.player, data);
        boolean canAfford = availablePoints >= node.getPointCost();

        // Wenn man ihn schon hat -> Gold/Weiß ("Erlernt")
        // Wenn man ihn lernen kann -> Weiß
        // Wenn man ihn nicht lernen kann (zu teuer oder gesperrt) -> Rot
        int costColor = isUnlocked ? 0xFFFFD700 : (canUnlock && canAfford ? 0xFFCCCCCC : 0xFFFF5555);
        Component costLabel = isUnlocked ? Component.literal("ERLERNT") : Component.translatable("gui.lightsabersfork.skilltree.point_cost", node.getPointCost());
        
        int costWidth = font.width(costLabel);
        
        graphics.pose().pushPose();
        graphics.pose().scale(0.8F, 0.8F, 0.8F);
        float costX = (centerX - 15) / 0.8F - costWidth;
        float costY = (bottomY - 8) / 0.8F;
        graphics.drawString(font, costLabel, (int)costX, (int)costY, costColor, true);
        graphics.pose().popPose();

        // Subtile vertikale Deko-Linie
        graphics.fill(centerX - 10, bottomY - 15, centerX - 9, bottomY + 12, 0x44FFFFFF);
    }
}