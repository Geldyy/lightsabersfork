package com.fiskmods.pika.lightsabers.client.logic;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;
import com.fiskmods.pika.lightsabers.network.payload.MeditationTogglePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class MeditationInputHandler {

    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {
        if (MeditationStateManager.isMeditating()) {
            event.getInput().forwardImpulse = 0;
            event.getInput().leftImpulse = 0;
            event.getInput().up = false;
            event.getInput().down = false;
            event.getInput().left = false;
            event.getInput().right = false;
            event.getInput().jumping = false;
            event.getInput().shiftKeyDown = false;
        }
    }

    // Blockiert das Vanilla-Pausemenü und beendet stattdessen unsere UI!
    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        if (MeditationStateManager.isMeditating() && event.getScreen() instanceof PauseScreen) {
            event.setCanceled(true); 
            
            MeditationStateManager.stopMeditation();
            PacketDistributor.sendToServer(new MeditationTogglePayload(false, BlockPos.ZERO, false));
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !MeditationStateManager.isMeditating()) return;

        int key = event.getKey();

        // NEU: RELEASE-Erkennung für die Leertaste (Abbruch des 2-Sekunden Timers)
        if (event.getAction() == GLFW.GLFW_RELEASE) {
            if (key == mc.options.keyJump.getKey().getValue()) {
                MeditationStateManager.setHoldingUnlock(false);
            }
            return;
        }

        if (event.getAction() == GLFW.GLFW_PRESS) {
            
            // 1. LEERTASTE: Einen Schritt tiefer gehen oder Unlocken
            if (key == mc.options.keyJump.getKey().getValue()) {
                if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.CATEGORY_SELECTION) {
                    MeditationStateManager.selectCategory(MeditationStateManager.getActiveCategory());
                } 
                else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.TREE_VIEW) {
                    MeditationStateManager.selectMonolith();
                }
                else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.NODE_SELECTION) {
                    MeditationStateManager.setHoldingUnlock(true); // NEU: Startet den 2s Timer!
                }
            }
            
            // 2. SHIFT (Sneak): Einen Schritt zurückgehen
            else if (key == mc.options.keyShift.getKey().getValue()) {
                if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.NODE_SELECTION) {
                    MeditationStateManager.deselectMonolith();
                } 
                else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.TREE_VIEW) {
                    MeditationStateManager.deselectCategory();
                } 
                else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.CATEGORY_SELECTION) {
                    MeditationStateManager.stopMeditation();
                    PacketDistributor.sendToServer(new MeditationTogglePayload(false, BlockPos.ZERO, false));
                }
            }
            
            // 3. WASD Steuerung (Abhängig von der Phase)
            else {
                boolean pressLeft = key == mc.options.keyLeft.getKey().getValue() || key == GLFW.GLFW_KEY_A;
                boolean pressRight = key == mc.options.keyRight.getKey().getValue() || key == GLFW.GLFW_KEY_D;
                boolean pressUp = key == mc.options.keyUp.getKey().getValue() || key == GLFW.GLFW_KEY_W;
                boolean pressDown = key == mc.options.keyDown.getKey().getValue() || key == GLFW.GLFW_KEY_S;

                if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.CATEGORY_SELECTION) {
                    if (pressLeft || pressRight) {
                        TreeCategory current = MeditationStateManager.getActiveCategory();
                        TreeCategory next = (current == TreeCategory.LIGHTSABER) ? TreeCategory.FORCE : TreeCategory.LIGHTSABER;
                        MeditationStateManager.setActiveCategory(next);
                    }
                }
                else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.TREE_VIEW) {
                    if (pressLeft || pressRight) {
                        TreeCategory category = MeditationStateManager.getActiveCategory();
                        int numSteles = MeditationStateManager.getSteleCount(category);
                        int currentIndex = MeditationStateManager.getSelectedSteleIndex();

                        if (pressLeft) currentIndex = (currentIndex - 1 + numSteles) % numSteles;
                        else if (pressRight) currentIndex = (currentIndex + 1) % numSteles;

                        MeditationStateManager.setSelectedSteleIndex(currentIndex);
                        
                        float targetAngle = MeditationStateManager.calculateTargetAngle(currentIndex, numSteles);
                        MeditationStateManager.setTargetCarouselAngle(targetAngle);
                    }
                }
                else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.NODE_SELECTION) {
                    // NEU: Räumliche Navigation
                    if (pressLeft)  MeditationStateManager.navigateNode(-1, 0);
                    if (pressRight) MeditationStateManager.navigateNode(1, 0);
                    if (pressUp)    MeditationStateManager.navigateNode(0, -1); // Up ist Y-Negativ!
                    if (pressDown)  MeditationStateManager.navigateNode(0, 1);
                }
            }
        }
    }
}