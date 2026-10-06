package com.fiskmods.pika.lightsabers.client.logic;

import java.util.List;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.ALKeyBinds;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.network.payload.DoubleJumpPayload;
import com.fiskmods.pika.lightsabers.network.payload.ThrowLightsaberPayload;
import com.fiskmods.pika.lightsabers.network.payload.ToggleLightsaberPayload;
import com.fiskmods.pika.lightsabers.network.payload.UseForcePayload;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class ClientInputEvents {

    private static boolean wasJumpKeyDown = false;
    private static int jumpsDone = 0;
    private static boolean jumpKeyReleasedInAir = false;

    private static boolean wasUseForceDown = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        // 1. Lichtschwert ein-/ausschalten
        while (ALKeyBinds.TOGGLE_LIGHTSABER.consumeClick()) {
            if (player.getMainHandItem().getItem() instanceof ItemLightsaber || player.getOffhandItem().getItem() instanceof ItemLightsaber) {
                PacketDistributor.sendToServer(new ToggleLightsaberPayload(false));
            }
        }
        
        // 2. Single-Switch Modus umschalten
        while (ALKeyBinds.SINGLE_SWITCH.consumeClick()) {
             if (player.getMainHandItem().getItem() instanceof ItemLightsaber || player.getOffhandItem().getItem() instanceof ItemLightsaber) {
                 PacketDistributor.sendToServer(new ToggleLightsaberPayload(true));
             }
        }

        // 3. Lichtschwert werfen
        while (ALKeyBinds.THROW_LIGHTSABER.consumeClick()) {
            if (player.getMainHandItem().getItem() instanceof ItemLightsaber || player.getOffhandItem().getItem() instanceof ItemLightsaber) {
                PacketDistributor.sendToServer(new ThrowLightsaberPayload());
            }
        }

        // ==========================================
        // MACHT-FÄHIGKEITEN STEUERUNG
        // ==========================================

        // 4. Macht-Auswahlrad öffnen
        while (ALKeyBinds.CYCLE_FORCE.consumeClick()) {
            PlayerForceData data = PlayerForceData.get(player);
            if (data != null && data.isForceSensitive()) {
                List<ResourceLocation> powers = data.getAvailableActivePowers();
                if (!powers.isEmpty()) {
                    mc.setScreen(new com.fiskmods.pika.lightsabers.client.gui.ForceRadialScreen(powers));
                    com.fiskmods.pika.lightsabers.client.gui.ForceHudOverlay.wakeUp();
                }
            }
        }

        // 5. Macht benutzen (Use)
        boolean isUseForceDown = ALKeyBinds.USE_FORCE.isDown();
        
        if (isUseForceDown) {
            com.fiskmods.pika.lightsabers.client.gui.ForceHudOverlay.wakeUp(); 
        }
        
        if (isUseForceDown != wasUseForceDown) {
            PacketDistributor.sendToServer(new UseForcePayload(isUseForceDown));
            
            // CLIENT-SIDE PREDICTION: Für Instant-Bewegungen wie DASH
            if (isUseForceDown) {
                PlayerForceData data = PlayerForceData.get(player);
                if (data != null && data.isForceSensitive() && data.getSelectedPower() != null) {
                    if (data.getSelectedPower().getPath().equals("force_dash") && data.getForceEnergy() >= 15.0f) {
                        data.consumeForceEnergy(15.0f); 
                        Vec3 look = player.getLookAngle();
                        player.setDeltaMovement(look.x * 2.5, look.y * 1.0, look.z * 2.5);
                        player.hasImpulse = true; // Zwingt Minecraft, die Bewegung zu akzeptieren
                    }
                }
            }
            
            wasUseForceDown = isUseForceDown;
        }

        // ==========================================
        // 6. MACHT-SPRUNG (STRIKT NUR SURVIVAL/ADVENTURE)
        // ==========================================
        if (mc.screen == null && !player.isCreative() && !player.isSpectator()) {
            boolean isJumpDown = mc.options.keyJump.isDown();

            // Status zurücksetzen, wenn man Boden, Wasser, Leiter oder Elytra berührt
            if (player.onGround() || player.isInWater() || player.isFallFlying() || player.onClimbable()) {
                jumpsDone = 0;
                jumpKeyReleasedInAir = false;
            } else if (!isJumpDown) {
                // Spieler ist in der Luft und hat die Taste komplett losgelassen
                jumpKeyReleasedInAir = true; 
            }

            // Exakte Flankenerkennung: Taste wurde genau in diesem Tick frisch gedrückt
            if (isJumpDown && !wasJumpKeyDown) {
                
                // Darf der Sprung auslösen?
                if (jumpKeyReleasedInAir && jumpsDone == 0) {
                    PlayerForceData data = PlayerForceData.get(player);
                    
                    if (data != null && data.isForceSensitive() && data.getForceEnergy() >= 10.0f) {
                        jumpsDone++;
                        data.consumeForceEnergy(10.0f); // UI Energie abziehen
                        
                        // Schwung nach oben anwenden
                        Vec3 motion = player.getDeltaMovement();
                        // Wir setzen die Y-Velocity stark nach oben (0.65 ist fast ein 4-Block Sprung)
                        player.setDeltaMovement(motion.x, 0.65D, motion.z); 
                        player.hasImpulse = true; // <--- Das fehlende Puzzleteil!
                        
                        PacketDistributor.sendToServer(new DoubleJumpPayload());
                    }
                }
            }
            wasJumpKeyDown = isJumpDown;
        }
    }
}