package com.fiskmods.pika.lightsabers.client.logic;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillNode;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;
import com.fiskmods.pika.lightsabers.network.payload.UnlockSkillPayload;

import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
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
    private static boolean isFreeMeditation = false;
    private static float initialPlayerYaw = 0.0f;
    private static float initialPlayerPitch = 0.0f;
    private static float orbitAngle = 0.0f;
    private static float prevOrbitAngle = 0.0f;
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
    private static long lastCloseTime = 0L;
    public static boolean canOpen() {
        return System.currentTimeMillis() - lastCloseTime > 500L;
    }

    private static TreeCategory currentCategory = TreeCategory.FORCE;
    private static int currentSteleIndex = 0;

    
    private static ResourceLocation selectedNodeId = null;
    private static boolean isHoldingUnlock = false;
    private static int unlockHoldTicks = 0;

    
    private static boolean showConfirmationDialog = false;
    private static ResourceLocation pendingSpecializationNode = null;

    private static float currentCarouselAngle = 0.0f;
    private static float prevCarouselAngle = 0.0f;
    private static float targetCarouselAngle = 0.0f;

    private static float transitionProgress = 0.0f;
    private static float prevTransitionProgress = 0.0f;
    private static CameraType previousCameraType = null;

    public static void startFreeMeditation(Player player) {
        if (!canOpen()) return;

        Minecraft mc = Minecraft.getInstance();
        if (!isMeditating) {
            previousCameraType = mc.options.getCameraType();
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }

        KeyMapping.releaseAll();
        mc.options.keyUse.setDown(false);
        while (mc.options.keyUse.consumeClick()) {}

        isMeditating = true;
        isFreeMeditation = true;
        isSithHolocron = false;
        currentPhase = MeditationPhase.CATEGORY_SELECTION;

        initialPlayerYaw = player.getYRot();
        initialPlayerPitch = player.getXRot();
        orbitAngle = initialPlayerYaw;
        prevOrbitAngle = initialPlayerYaw;

        holocronPos = player.position();
        playerHoverPos = player.position();
        lockedYaw = initialPlayerYaw;

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
        showConfirmationDialog = false;
        pendingSpecializationNode = null;

        mc.mouseHandler.releaseMouse();
        GLFW.glfwSetInputMode(mc.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    public static void startMeditation(Player player, BlockPos clickedHolocronPos, boolean isSith) {
        if (!canOpen()) return;

        Minecraft mc = Minecraft.getInstance();
        if (!isMeditating) {
            previousCameraType = mc.options.getCameraType();
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
        
        KeyMapping.releaseAll();
        mc.options.keyUse.setDown(false);
        while (mc.options.keyUse.consumeClick()) {}

        if (!isMeditating) {
            previousCameraType = mc.options.getCameraType();
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
        
        isMeditating = true;
        isFreeMeditation = false;
        isSithHolocron = isSith;
        currentPhase = MeditationPhase.CATEGORY_SELECTION;

        initialPlayerYaw = player.getYRot();
        initialPlayerPitch = player.getXRot();
        orbitAngle = initialPlayerYaw;
        prevOrbitAngle = initialPlayerYaw;
        
        holocronPos = (clickedHolocronPos != null) ? clickedHolocronPos.getCenter() : player.position();
        
        double diffX = player.getX() - holocronPos.x;
        double diffZ = player.getZ() - holocronPos.z;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        if (dist < 0.1D) {
            diffX = 0.0D;
            diffZ = 2.5D;
            dist = 2.5D;
        }

        double normX = diffX / dist;
        double normZ = diffZ / dist;

        playerHoverPos = new Vec3(holocronPos.x + normX * 2.5D, player.getY(), holocronPos.z + normZ * 2.5D);

        double dirToHoloX = holocronPos.x - playerHoverPos.x;
        double dirToHoloZ = holocronPos.z - playerHoverPos.z;
        lockedYaw = (float) (Mth.atan2(dirToHoloZ, dirToHoloX) * Mth.RAD_TO_DEG) - 90.0F;
        
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
        showConfirmationDialog = false;
        pendingSpecializationNode = null;

        mc.mouseHandler.releaseMouse();
        GLFW.glfwSetInputMode(mc.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    public static void startMeditation(Player player) {
        startMeditation(player, player.blockPosition(), false);
    }

    public static void stopMeditation() {
        if (isMeditating) {
            isMeditating = false;
            isFreeMeditation = false;
            lastCloseTime = System.currentTimeMillis();
            cancelSpecialization();

            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.setYRot(initialPlayerYaw);
                mc.player.setXRot(initialPlayerPitch);
                mc.player.yHeadRot = initialPlayerYaw;
                mc.player.yBodyRot = initialPlayerYaw;
            }

            restoreCamera();
            
            KeyMapping.releaseAll();
            mc.options.keyUse.setDown(false);
            mc.options.keyAttack.setDown(false);
            while (mc.options.keyUse.consumeClick()) {}
            while (mc.options.keyAttack.consumeClick()) {}
            long window = mc.getWindow().getWindow();
            GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
            mc.mouseHandler.grabMouse();
        }
    }

    private static void restoreCamera() {
        Minecraft mc = Minecraft.getInstance();
        mc.options.setCameraType(CameraType.FIRST_PERSON); 
        previousCameraType = null;
    }

    public static void selectCategory(TreeCategory category) {
    	Minecraft mc = Minecraft.getInstance();
        PlayerForceData data = mc.player != null ? PlayerForceData.get(mc.player) : null;
        
        if (data != null && !data.canAccessHolocron()) {
            return;
        }

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
            
            List<SkillNode> nodes = 
                SkillTreeRegistry.getNodesForStele(currentCategory, currentSteleIndex);
            
            if (!nodes.isEmpty()) {
                SkillNode root = null;
                for (SkillNode n : nodes) {
                    if (n.getParents() == null || n.getParents().isEmpty()) {
                        if (root == null || n.getOffsetY() > root.getOffsetY()) {
                            root = n;
                        }
                    }
                }
                
                if (root == null) {
                    root = nodes.get(0);
                    for (SkillNode n : nodes) {
                        if (n.getOffsetY() > root.getOffsetY()) root = n; 
                    }
                }
                
                selectedNodeId = root.getId();
            }
        }
    }

    public static void deselectMonolith() {
        if (currentPhase == MeditationPhase.NODE_SELECTION) {
            if (showConfirmationDialog) {
                cancelSpecialization();
                return;
            }
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

    public static void cycleStele(int direction) {
        int numSteles = getSteleCount(currentCategory);
        currentSteleIndex = (currentSteleIndex - direction + numSteles) % numSteles;
        float targetAngle = calculateTargetAngle(currentSteleIndex, numSteles);
        setTargetCarouselAngle(targetAngle);
    }

    public static SkillNode findNodeAtScreenPos(double mouseX, double mouseY, int screenWidth, int screenHeight) {
        if (currentPhase != MeditationPhase.NODE_SELECTION) return null;

        List<SkillNode> nodes = SkillTreeRegistry.getNodesForStele(currentCategory, currentSteleIndex);
        if (nodes == null || nodes.isEmpty()) return null;

        Minecraft mc = Minecraft.getInstance();
        PlayerForceData data = mc.player != null ? PlayerForceData.get(mc.player) : null;
        ResourceLocation activeRank = SkillTreeRegistry.getActiveNeutralRankNode(data);

        SkillNode closest = null;
        double closestDistSq = Double.MAX_VALUE;

        float scaleX = screenHeight * 0.0075F;
        float scaleY = screenHeight * 0.0058F;
        float baseScreenX = screenWidth / 2.0F;
        float baseScreenY = screenHeight * 0.68F;

        for (SkillNode node : nodes) {
            if (currentCategory == TreeCategory.FORCE && currentSteleIndex == 1 && isNeutralRank(node.getId())) {
                if (!node.getId().equals(activeRank)) {
                    continue;
                }
            }

            double nodeScreenX = baseScreenX + (node.getOffsetX() * scaleX);
            double nodeScreenY = baseScreenY - ((-node.getOffsetY() - 10.0F) * scaleY);

            double dx = mouseX - nodeScreenX;
            double dy = mouseY - nodeScreenY;
            double distSq = dx * dx + dy * dy;

            if (distSq < 576.0D && distSq < closestDistSq) {
                closestDistSq = distSq;
                closest = node;
            }
        }

        return closest;
    }

    public static void navigateNode(float dirX, float dirY) {
        if (selectedNodeId == null || showConfirmationDialog) return;
        
        SkillNode current = SkillTreeRegistry.getNode(selectedNodeId);
        if (current == null) return;

        Minecraft mc = Minecraft.getInstance();
        PlayerForceData data = mc.player != null ? PlayerForceData.get(mc.player) : null;
        ResourceLocation activeRankNode = SkillTreeRegistry.getActiveNeutralRankNode(data);

        ResourceLocation explicitTarget = null;
        if (dirY == -1.0F) explicitTarget = current.getNavUp();
        else if (dirY == 1.0F) explicitTarget = current.getNavDown();
        else if (dirX == -1.0F) explicitTarget = current.getNavLeft();
        else if (dirX == 1.0F) explicitTarget = current.getNavRight();

        if (explicitTarget != null) {
            if (isNeutralRank(explicitTarget)) {
                explicitTarget = activeRankNode;
            }

            if (SkillTreeRegistry.getNode(explicitTarget) != null) {
                selectedNodeId = explicitTarget;
                isHoldingUnlock = false;
                unlockHoldTicks = 0;
                return;
            }
        }

        List<SkillNode> nodes = SkillTreeRegistry.getNodesForStele(currentCategory, currentSteleIndex);
        SkillNode bestTarget = null;
        float bestDist = Float.MAX_VALUE;

        for (SkillNode n : nodes) {
            if (n.getId().equals(selectedNodeId)) continue;

            if (currentCategory == TreeCategory.FORCE && currentSteleIndex == 1 && isNeutralRank(n.getId())) {
                if (!n.getId().equals(activeRankNode)) {
                    continue;
                }
            }
            
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

    public static boolean isNeutralRank(ResourceLocation id) {
        if (id == null) return false;
        String path = id.getPath();
        return path.equals("force_adept") || path.equals("force_channeler") 
            || path.equals("force_veteran") || path.equals("force_mastery");
    }

    public static void setSelectedNodeId(ResourceLocation id) {
        selectedNodeId = id;
    }
    public static void setHoldingUnlock(boolean holding) {
        if (showConfirmationDialog) return;
        if (currentPhase == MeditationPhase.NODE_SELECTION && selectedNodeId != null) {
            isHoldingUnlock = holding;
            if (!holding) unlockHoldTicks = 0;
        }
    }

    public static boolean isConfirmationDialogOpen() { return showConfirmationDialog; }
    public static ResourceLocation getPendingSpecializationNode() { return pendingSpecializationNode; }

    public static void confirmSpecialization() {
        if (showConfirmationDialog && pendingSpecializationNode != null) {
            PacketDistributor.sendToServer(new UnlockSkillPayload(pendingSpecializationNode));
            showConfirmationDialog = false;
            pendingSpecializationNode = null;
            isHoldingUnlock = false;
            unlockHoldTicks = 0;
        }
    }

    public static void cancelSpecialization() {
        showConfirmationDialog = false;
        pendingSpecializationNode = null;
        isHoldingUnlock = false;
        unlockHoldTicks = 0;
    }

    public static float calculateTargetAngle(int index, int count) {
        float spreadAngle = 120.0F;
        float stepAngle = count > 1 ? spreadAngle / (count - 1) : 0;
        float offsetFromCenter = index - (count - 1) / 2.0f;
        return -offsetFromCenter * stepAngle;
    }

    public static int getSteleCount(TreeCategory category) {
        if (category == TreeCategory.FORCE) {
            Minecraft mc = Minecraft.getInstance();
            PlayerForceData data = mc.player != null ? PlayerForceData.get(mc.player) : null;
            if (data != null && data.isUnifiedTreeUnlocked()) {
                return 1;
            }
        }
        return 3;
    }

    public static void setMeditating(boolean meditating, Vec3 centerPos) {
        if (isMeditating && !meditating) {
            restoreCamera();
        }
        
        isMeditating = meditating;
        holocronPos = (centerPos != null) ? centerPos : Vec3.ZERO;
        
        if (!meditating) {
            isFreeMeditation = false;
            transitionProgress = 0.0f;
            prevTransitionProgress = 0.0f;
            stelesRevealProgress = 0.0f;
            prevStelesRevealProgress = 0.0f;
            nodeFocusProgress = 0.0f;
            prevNodeFocusProgress = 0.0f;
            playerHoverPos = null;
            cancelSpecialization();
        }
    }

    public static boolean isMeditating() { return isMeditating; }
    public static boolean isFreeMeditation() { return isFreeMeditation; }
    public static float getOrbitYaw(float partialTick) { return Mth.lerp(partialTick, prevOrbitAngle, orbitAngle); }
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

    public static boolean isPlayerFloating() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        PlayerForceData data = PlayerForceData.get(mc.player);
        return data != null && data.isForceSensitive();
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerForceData clientData = mc.player != null ? PlayerForceData.get(mc.player) : null;
        SkillNode.setClientUnifiedActive(clientData != null && clientData.isUnifiedTreeUnlocked());
        
        if (isMeditating && mc.player != null && playerHoverPos != null) {
            
            mc.options.keyUse.setDown(false);
            while (mc.options.keyUse.consumeClick()) {}

            double yOffset = (!isFreeMeditation && isPlayerFloating()) ? 0.25D : 0.0D;
            mc.player.setDeltaMovement(0, 0, 0); 
            mc.player.setPos(playerHoverPos.x, playerHoverPos.y + yOffset, playerHoverPos.z);
            
            float faceYaw = isFreeMeditation ? initialPlayerYaw : lockedYaw;
            float facePitch = isFreeMeditation ? 0.0F : 10.0F;
            mc.player.setYRot(faceYaw);
            mc.player.setXRot(facePitch); 
            mc.player.yHeadRot = faceYaw;
            mc.player.yBodyRot = faceYaw;

            if (mc.mouseHandler.isMouseGrabbed()) {
                mc.mouseHandler.releaseMouse();
            }
            long window = mc.getWindow().getWindow();
            if (GLFW.glfwGetInputMode(window, GLFW.GLFW_CURSOR) != GLFW.GLFW_CURSOR_HIDDEN) {
                GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
            }
        }

        prevOrbitAngle = orbitAngle;
        if (isMeditating && isFreeMeditation) {
            orbitAngle += 0.4F;
            if (orbitAngle >= 360.0F) {
                orbitAngle -= 360.0F;
                prevOrbitAngle -= 360.0F;
            }
        }

        prevTransitionProgress = transitionProgress;
        prevStelesRevealProgress = stelesRevealProgress;
        prevNodeFocusProgress = nodeFocusProgress;
        
        if (isMeditating) {
            if (transitionProgress < 1.0f) transitionProgress = Math.min(1.0f, transitionProgress + 0.06f);
            if (stelesRevealProgress != targetStelesReveal) stelesRevealProgress = Mth.lerp(0.1f, stelesRevealProgress, targetStelesReveal);
            if (nodeFocusProgress != targetNodeFocus) nodeFocusProgress = Mth.lerp(0.15f, nodeFocusProgress, targetNodeFocus);
            
            if (isHoldingUnlock && currentPhase == MeditationPhase.NODE_SELECTION && selectedNodeId != null && !showConfirmationDialog) {
                SkillNode node = SkillTreeRegistry.getNode(selectedNodeId);
                PlayerForceData data = PlayerForceData.get(mc.player);

                boolean canLearnFirstTime = node != null && data != null && node.canUnlock(mc.player, data);
                boolean canUpgradeTier = node != null && data != null && data.canUpgradePowerTier(node.getId());

                if (canLearnFirstTime || canUpgradeTier) {
                    unlockHoldTicks++;
                    if (unlockHoldTicks >= 40) {
                        if (canLearnFirstTime && node.isSpecialization() && (data == null || !data.isUnifiedTreeUnlocked())) {
                            showConfirmationDialog = true;
                            pendingSpecializationNode = selectedNodeId;
                            isHoldingUnlock = false;
                            unlockHoldTicks = 0;
                        } else {
                            PacketDistributor.sendToServer(new UnlockSkillPayload(selectedNodeId));
                            isHoldingUnlock = false;
                            unlockHoldTicks = 0;
                        }
                    }
                } else {
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