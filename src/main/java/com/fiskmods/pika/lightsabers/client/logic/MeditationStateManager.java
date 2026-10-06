package com.fiskmods.pika.lightsabers.client.logic;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillNode;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;
import com.fiskmods.pika.lightsabers.network.payload.UnlockSkillPayload;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class MeditationStateManager {

    public enum MeditationPhase {
        CATEGORY_SELECTION, 
        TREE_VIEW,          
        NODE_SELECTION      
    }

    private static boolean isMeditating = false;
    private static Vec3 holocronPos = Vec3.ZERO;
    private static Vec3 playerHoverPos = null;
    private static float lockedYaw = 0.0f;
    private static MeditationPhase currentPhase = MeditationPhase.CATEGORY_SELECTION;
    private static boolean isSithHolocron = false;
    
    private static float stelesRevealProgress = 0.0f;
    private static float prevStelesRevealProgress = 0.0f;
    private static float targetStelesReveal = 0.0f;

    private static float nodeFocusProgress = 0.0f;
    private static float prevNodeFocusProgress = 0.0f;
    private static float targetNodeFocus = 0.0f;

    private static TreeCategory currentCategory = TreeCategory.FORCE;
    private static int currentSteleIndex = 0;

    // NEU: Node Steuerung & 2-Sekunden-Hold Mechanik
    private static ResourceLocation selectedNodeId = null;
    private static boolean isHoldingUnlock = false;
    private static int unlockHoldTicks = 0;

    private static float currentCarouselAngle = 0.0f;
    private static float prevCarouselAngle = 0.0f;
    private static float targetCarouselAngle = 0.0f;

    private static float transitionProgress = 0.0f;
    private static float prevTransitionProgress = 0.0f;
    private static CameraType previousCameraType = null;

    public static void startMeditation(Player player, BlockPos clickedHolocronPos, boolean isSith) {
        if (!isMeditating) {
            Minecraft mc = Minecraft.getInstance();
            previousCameraType = mc.options.getCameraType();
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
        
        isMeditating = true;
        isSithHolocron = isSith;
        currentPhase = MeditationPhase.CATEGORY_SELECTION;
        
        holocronPos = (clickedHolocronPos != null) ? clickedHolocronPos.getCenter() : player.position();
        playerHoverPos = new Vec3(player.getX(), holocronPos.y, player.getZ());
        
        double dX = holocronPos.x - playerHoverPos.x;
        double dZ = holocronPos.z - playerHoverPos.z;
        lockedYaw = (float) (Mth.atan2(dZ, dX) * Mth.RAD_TO_DEG) - 90.0F; 
        
        currentSteleIndex = 0;
        currentCarouselAngle = 0.0f;
        prevCarouselAngle = 0.0f;
        targetCarouselAngle = 0.0f;
        
        stelesRevealProgress = 0.0f;
        prevStelesRevealProgress = 0.0f;
        targetStelesReveal = 0.0f;
        
        nodeFocusProgress = 0.0f;
        prevNodeFocusProgress = 0.0f;
        targetNodeFocus = 0.0f;
        
        selectedNodeId = null;
        isHoldingUnlock = false;
        unlockHoldTicks = 0;
    }

    public static void startMeditation(Player player) {
        startMeditation(player, player.blockPosition(), false);
    }

    public static void stopMeditation() {
        if (isMeditating) {
            isMeditating = false;
            restoreCamera();
        }
    }

    private static void restoreCamera() {
        Minecraft mc = Minecraft.getInstance();
        if (previousCameraType != null) {
            mc.options.setCameraType(previousCameraType);
            previousCameraType = null; 
        } else {
            mc.options.setCameraType(CameraType.FIRST_PERSON); 
        }
    }

    public static void selectCategory(TreeCategory category) {
        setActiveCategory(category);
        currentPhase = MeditationPhase.TREE_VIEW;
        targetStelesReveal = 1.0f;
        
        int count = getSteleCount(category);
        currentSteleIndex = count / 2;
        
        float targetAngle = calculateTargetAngle(currentSteleIndex, count);
        currentCarouselAngle = targetAngle;
        prevCarouselAngle = targetAngle;
        targetCarouselAngle = targetAngle;
    }

    public static void selectMonolith() {
        if (currentPhase == MeditationPhase.TREE_VIEW) {
            currentPhase = MeditationPhase.NODE_SELECTION;
            targetNodeFocus = 1.0f; 
            
            // Sucht den untersten Skill (Startpunkt)
            java.util.List<com.fiskmods.pika.lightsabers.force.skilltree.SkillNode> nodes = 
                com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.getNodesForStele(currentCategory, currentSteleIndex);
            
            if (!nodes.isEmpty()) {
                com.fiskmods.pika.lightsabers.force.skilltree.SkillNode root = nodes.get(0);
                for (com.fiskmods.pika.lightsabers.force.skilltree.SkillNode n : nodes) {
                    if (n.getOffsetY() > root.getOffsetY()) root = n; 
                }
                selectedNodeId = root.getId();
            }
        }
    }

    public static void deselectMonolith() {
        if (currentPhase == MeditationPhase.NODE_SELECTION) {
            currentPhase = MeditationPhase.TREE_VIEW;
            targetNodeFocus = 0.0f; 
            isHoldingUnlock = false;
            unlockHoldTicks = 0;
            selectedNodeId = null;
        }
    }

    public static void deselectCategory() {
        if (currentPhase == MeditationPhase.TREE_VIEW) {
            currentPhase = MeditationPhase.CATEGORY_SELECTION;
            targetStelesReveal = 0.0f; 
        }
    }

 // --- NEU: RÄUMLICHE NAVIGATION (WASD) MIT OVERRIDES ---
    public static void navigateNode(float dirX, float dirY) {
        if (selectedNodeId == null) return;
        
        com.fiskmods.pika.lightsabers.force.skilltree.SkillNode current = 
            com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.getNode(selectedNodeId);
        if (current == null) return;

        // 1. Zuerst prüfen: Gibt es einen harten Override vom Designer?
        ResourceLocation explicitTarget = null;
        if (dirY == -1.0F) explicitTarget = current.getNavUp();         // W gedrückt
        else if (dirY == 1.0F) explicitTarget = current.getNavDown();   // S gedrückt
        else if (dirX == -1.0F) explicitTarget = current.getNavLeft();  // A gedrückt
        else if (dirX == 1.0F) explicitTarget = current.getNavRight();  // D gedrückt

        if (explicitTarget != null && com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.getNode(explicitTarget) != null) {
            selectedNodeId = explicitTarget;
            isHoldingUnlock = false;
            unlockHoldTicks = 0;
            return; // Wir sind fertig, der Designer hat das Ziel diktiert!
        }

        // 2. FALLBACK: Die bisherige Mathematik, falls du keinen Override gesetzt hast
        java.util.List<com.fiskmods.pika.lightsabers.force.skilltree.SkillNode> nodes = 
            com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.getNodesForStele(currentCategory, currentSteleIndex);

        com.fiskmods.pika.lightsabers.force.skilltree.SkillNode bestTarget = null;
        float bestDist = Float.MAX_VALUE;

        for (com.fiskmods.pika.lightsabers.force.skilltree.SkillNode n : nodes) {
            if (n.getId().equals(selectedNodeId)) continue;
            
            float dx = n.getOffsetX() - current.getOffsetX();
            float dy = n.getOffsetY() - current.getOffsetY();
            
            float dotProduct = (dx * dirX) + (dy * dirY);
            
            if (dotProduct > 1.0f) { 
                float dist = (dx * dx) + (dy * dy);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestTarget = n;
                }
            }
        }
        
        if (bestTarget != null) {
            selectedNodeId = bestTarget.getId();
            isHoldingUnlock = false; 
            unlockHoldTicks = 0;
        }
    }

    public static void setHoldingUnlock(boolean holding) {
        if (currentPhase == MeditationPhase.NODE_SELECTION && selectedNodeId != null) {
            isHoldingUnlock = holding;
            if (!holding) unlockHoldTicks = 0;
        }
    }

    public static float calculateTargetAngle(int index, int count) {
        float spreadAngle = 120.0F;
        float stepAngle = count > 1 ? spreadAngle / (count - 1) : 0;
        float offsetFromCenter = index - (count - 1) / 2.0f;
        return -offsetFromCenter * stepAngle;
    }

    public static int getSteleCount(TreeCategory category) {
        if (category == null) return 3;
        String name = category.name();
        if (name.contains("LIGHTSABER")) return 3; 
        if (name.contains("FORCE")) return 3;
        return 3;
    }

    public static void setMeditating(boolean meditating, Vec3 centerPos) {
        if (isMeditating && !meditating) {
            restoreCamera();
        }
        
        isMeditating = meditating;
        holocronPos = (centerPos != null) ? centerPos : Vec3.ZERO;
        
        if (!meditating) {
            transitionProgress = 0.0f;
            prevTransitionProgress = 0.0f;
            stelesRevealProgress = 0.0f;
            prevStelesRevealProgress = 0.0f;
            nodeFocusProgress = 0.0f;
            prevNodeFocusProgress = 0.0f;
            playerHoverPos = null;
        }
    }

    public static boolean isMeditating() { return isMeditating; }
    public static Vec3 getMeditationCenter() { return holocronPos; }
    public static Vec3 getMeditationCenterPos() { return holocronPos; }
    public static float getLockedYaw() { return lockedYaw; }
    public static TreeCategory getCurrentCategory() { return currentCategory; }
    public static TreeCategory getActiveCategory() { return currentCategory; }
    public static int getSelectedSteleIndex() { return currentSteleIndex; }
    public static void setSelectedSteleIndex(int index) { currentSteleIndex = index; }
    public static void setTargetCarouselAngle(float angle) { targetCarouselAngle = angle; }
    public static MeditationPhase getCurrentPhase() { return currentPhase; }
    public static boolean isSith() { return isSithHolocron; }
    public static Vec3 getPlayerHoverPos() { return playerHoverPos; }
    public static ResourceLocation getSelectedNodeId() { return selectedNodeId; }
    public static float getUnlockProgress() { return Math.min(1.0F, unlockHoldTicks / 40.0F); }

    public static void setActiveCategory(TreeCategory category) {
        currentCategory = category;
        currentSteleIndex = 0;
    }

    public static float getCarouselAngle(float partialTick) {
        return Mth.lerp(partialTick, prevCarouselAngle, currentCarouselAngle);
    }
    public static float getTransitionProgress(float partialTick) {
        return Mth.lerp(partialTick, prevTransitionProgress, transitionProgress);
    }
    public static float getStelesRevealProgress(float partialTick) {
        return Mth.lerp(partialTick, prevStelesRevealProgress, stelesRevealProgress);
    }
    public static float getNodeFocusProgress(float partialTick) {
        return Mth.lerp(partialTick, prevNodeFocusProgress, nodeFocusProgress);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        
        if (isMeditating && mc.player != null && playerHoverPos != null) {
            mc.player.setDeltaMovement(0, 0, 0); 
            mc.player.setPos(playerHoverPos.x, playerHoverPos.y + 0.25D, playerHoverPos.z);
            
            mc.player.setYRot(lockedYaw);
            mc.player.setXRot(10.0F); 
            mc.player.yHeadRot = lockedYaw;
            mc.player.yBodyRot = lockedYaw;
        }

        prevTransitionProgress = transitionProgress;
        prevStelesRevealProgress = stelesRevealProgress;
        prevNodeFocusProgress = nodeFocusProgress;
        
        if (isMeditating) {
            if (transitionProgress < 1.0f) transitionProgress = Math.min(1.0f, transitionProgress + 0.06f);
            if (stelesRevealProgress != targetStelesReveal) stelesRevealProgress = Mth.lerp(0.1f, stelesRevealProgress, targetStelesReveal);
            if (nodeFocusProgress != targetNodeFocus) nodeFocusProgress = Mth.lerp(0.15f, nodeFocusProgress, targetNodeFocus);
            
            if (isHoldingUnlock && currentPhase == MeditationPhase.NODE_SELECTION && selectedNodeId != null) {
                SkillNode node = SkillTreeRegistry.getNode(selectedNodeId);
                PlayerForceData data = PlayerForceData.get(mc.player);

                // Nur hochzählen, wenn der Node auch wirklich freigeschaltet werden DARF!
                if (node != null && data != null && node.canUnlock(mc.player, data)) {
                    unlockHoldTicks++;
                    if (unlockHoldTicks >= 40) { // 2 Sekunden
                        PacketDistributor.sendToServer(new UnlockSkillPayload(selectedNodeId));
                        isHoldingUnlock = false;
                        unlockHoldTicks = 0;
                    }
                } else {
                    // Falls der Node gesperrt ist, Ladevorgang sofort abbrechen
                    unlockHoldTicks = 0;
                    isHoldingUnlock = false;
                }
            }
        } else {
            if (transitionProgress > 0.0f) transitionProgress = Math.max(0.0f, transitionProgress - 0.06f);
        }

        prevCarouselAngle = currentCarouselAngle;
        if (isMeditating && currentCarouselAngle != targetCarouselAngle) {
            currentCarouselAngle = Mth.lerp(0.15f, currentCarouselAngle, targetCarouselAngle);
            if (Math.abs(targetCarouselAngle - currentCarouselAngle) < 0.01f) {
                currentCarouselAngle = targetCarouselAngle;
            }
        }
    }
}