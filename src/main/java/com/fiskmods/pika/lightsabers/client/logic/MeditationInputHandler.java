package com.fiskmods.pika.lightsabers.client.logic;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillNode;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;
import com.fiskmods.pika.lightsabers.network.payload.MeditationTogglePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
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
            if (MeditationStateManager.isFreeMeditation()) {
                if (event.getInput().up || event.getInput().down || event.getInput().left || event.getInput().right
                        || event.getInput().jumping || event.getInput().shiftKeyDown) {
                    exitMeditation();
                    return;
                }
            }
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

    public static void playClickSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public static void handleEscOrBack() {
        if (MeditationStateManager.isFreeMeditation()) {
            exitMeditation();
            return;
        }
        if (MeditationStateManager.isConfirmationDialogOpen()) {
            MeditationStateManager.cancelSpecialization();
        } else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.NODE_SELECTION) {
            MeditationStateManager.deselectMonolith();
        } else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.TREE_VIEW) {
            MeditationStateManager.deselectCategory();
        } else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.CATEGORY_SELECTION) {
            exitMeditation();
        }
    }

    public static void exitMeditation() {
        MeditationStateManager.stopMeditation();
        PacketDistributor.sendToServer(new MeditationTogglePayload(false, BlockPos.ZERO, false));
    }

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        if (MeditationStateManager.isMeditating()) {
            if (event.getScreen() instanceof PauseScreen) {
                event.setCanceled(true); 
                handleEscOrBack();
            } else if (event.getScreen() instanceof InventoryScreen) {
                event.setCanceled(true);
                exitMeditation();
            }
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !MeditationStateManager.isMeditating()) return;

        int key = event.getKey();

        if (MeditationStateManager.isFreeMeditation()) {
            if (event.getAction() == GLFW.GLFW_PRESS) {
                exitMeditation();
            }
            return;
        }

        if (MeditationStateManager.isConfirmationDialogOpen()) {
            if (event.getAction() == GLFW.GLFW_PRESS) {
                if (key == GLFW.GLFW_KEY_ENTER || key == mc.options.keyJump.getKey().getValue()) {
                    MeditationStateManager.confirmSpecialization();
                } else if (key == GLFW.GLFW_KEY_ESCAPE || key == mc.options.keyShift.getKey().getValue() || key == GLFW.GLFW_KEY_BACKSPACE) {
                    MeditationStateManager.cancelSpecialization();
                }
            }
            return;
        }

        int invKey = mc.options.keyInventory.getKey().getValue();
        if (event.getAction() == GLFW.GLFW_PRESS && (key == GLFW.GLFW_KEY_E || key == invKey)) {
            exitMeditation();
            return;
        }

        if (event.getAction() == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_ESCAPE) {
            handleEscOrBack();
            return;
        }

        if (event.getAction() == GLFW.GLFW_RELEASE) {
            if (key == mc.options.keyJump.getKey().getValue()) {
                MeditationStateManager.setHoldingUnlock(false);
            }
            return;
        }

        if (event.getAction() == GLFW.GLFW_PRESS) {
            if (key == mc.options.keyJump.getKey().getValue()) {
                if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.CATEGORY_SELECTION) {
                    MeditationStateManager.selectCategory(MeditationStateManager.getActiveCategory());
                } else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.TREE_VIEW) {
                    MeditationStateManager.selectMonolith();
                } else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.NODE_SELECTION) {
                    MeditationStateManager.setHoldingUnlock(true);
                }
            } else if (key == mc.options.keyShift.getKey().getValue()) {
                handleEscOrBack();
            } else {
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
                } else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.TREE_VIEW) {
                        if (pressLeft || pressRight) {
                            TreeCategory category = MeditationStateManager.getActiveCategory();
                            int numSteles = MeditationStateManager.getSteleCount(category);
                            int currentIndex = MeditationStateManager.getSelectedSteleIndex();

                            if (pressLeft) {
                                currentIndex = (currentIndex + 1) % numSteles;
                            } else if (pressRight) {
                                currentIndex = (currentIndex - 1 + numSteles) % numSteles;
                            }

                            MeditationStateManager.setSelectedSteleIndex(currentIndex);
                            
                            float targetAngle = MeditationStateManager.calculateTargetAngle(currentIndex, numSteles);
                            MeditationStateManager.setTargetCarouselAngle(targetAngle);
                        }
                    } else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.NODE_SELECTION) {
                    if (pressLeft)  MeditationStateManager.navigateNode(-1, 0);
                    if (pressRight) MeditationStateManager.navigateNode(1, 0);
                    if (pressUp)    MeditationStateManager.navigateNode(0, -1);
                    if (pressDown)  MeditationStateManager.navigateNode(0, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseButton(InputEvent.MouseButton.Pre event) {
        if (!MeditationStateManager.isMeditating()) return;

        Minecraft mc = Minecraft.getInstance();
        event.setCanceled(true);

        int button = event.getButton();
        int action = event.getAction();

        if (MeditationStateManager.isFreeMeditation()) {
            if (action == GLFW.GLFW_PRESS) {
                exitMeditation();
            }
            return;
        }

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        double mouseX = mc.mouseHandler.xpos() * (double) screenWidth / (double) mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * (double) screenHeight / (double) mc.getWindow().getScreenHeight();

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS) {
            handleEscOrBack();
            return;
        }
        
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (MeditationStateManager.isConfirmationDialogOpen()) {
                if (action == GLFW.GLFW_PRESS) {
                    int dialogW = 320;
                    int dialogH = 140;
                    int dialogX = (screenWidth - dialogW) / 2;
                    int dialogY = (screenHeight - dialogH) / 2;

                    if (mouseX >= dialogX + 15 && mouseX <= dialogX + 130 && mouseY >= dialogY + dialogH - 30 && mouseY <= dialogY + dialogH - 8) {
                        MeditationStateManager.confirmSpecialization();
                        playClickSound();
                    } else if (mouseX >= dialogX + dialogW - 130 && mouseX <= dialogX + dialogW - 15 && mouseY >= dialogY + dialogH - 30 && mouseY <= dialogY + dialogH - 8) {
                        MeditationStateManager.cancelSpecialization();
                        playClickSound();
                    }
                }
                return;
            }

            int centerX = screenWidth / 2;
            if (mouseY >= 15 && mouseY <= 75) {
                if (action == GLFW.GLFW_PRESS) {
                    if (mouseX < centerX - 10) {
                        MeditationStateManager.selectCategory(TreeCategory.LIGHTSABER);
                        playClickSound();
                        return;
                    } else if (mouseX > centerX + 10) {
                        MeditationStateManager.selectCategory(TreeCategory.FORCE);
                        playClickSound();
                        return;
                    }
                }
            }

            if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.CATEGORY_SELECTION) {
                if (action == GLFW.GLFW_PRESS) {
                    if (mouseX < centerX) {
                        MeditationStateManager.selectCategory(TreeCategory.LIGHTSABER);
                    } else {
                        MeditationStateManager.selectCategory(TreeCategory.FORCE);
                    }
                    playClickSound();
                }
            }
            else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.TREE_VIEW) {
                if (action == GLFW.GLFW_PRESS) {
                    if (mouseX < screenWidth * 0.30) {
                        MeditationStateManager.cycleStele(-1);
                        playClickSound();
                    } else if (mouseX > screenWidth * 0.70) {
                        MeditationStateManager.cycleStele(1);
                        playClickSound();
                    } else {
                        MeditationStateManager.selectMonolith();
                        playClickSound();
                    }
                }
            }
            else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.NODE_SELECTION) {
                if (action == GLFW.GLFW_PRESS) {
                    SkillNode clickedNode = MeditationStateManager.findNodeAtScreenPos(mouseX, mouseY, screenWidth, screenHeight);
                    if (clickedNode != null) {
                        ResourceLocation currentSel = MeditationStateManager.getSelectedNodeId();
                        if (currentSel != null && currentSel.equals(clickedNode.getId())) {
                            MeditationStateManager.setHoldingUnlock(true);
                        } else {
                            MeditationStateManager.setSelectedNodeId(clickedNode.getId());
                            MeditationStateManager.setHoldingUnlock(false);
                            playClickSound();
                        }
                        return;
                    }

                    if (mouseY >= screenHeight - 75 && mouseY <= screenHeight - 10) {
                        MeditationStateManager.setHoldingUnlock(true);
                        return;
                    }
                } else if (action == GLFW.GLFW_RELEASE) {
                    MeditationStateManager.setHoldingUnlock(false);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (!MeditationStateManager.isMeditating()) return;
        event.setCanceled(true);

        double deltaY = event.getScrollDeltaY();

        if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.TREE_VIEW) {
            if (deltaY > 0) {
                MeditationStateManager.cycleStele(-1);
            } else if (deltaY < 0) {
                MeditationStateManager.cycleStele(1);
            }
        } else if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.CATEGORY_SELECTION) {
            TreeCategory current = MeditationStateManager.getActiveCategory();
            MeditationStateManager.setActiveCategory(current == TreeCategory.LIGHTSABER ? TreeCategory.FORCE : TreeCategory.LIGHTSABER);
        }
    }
}