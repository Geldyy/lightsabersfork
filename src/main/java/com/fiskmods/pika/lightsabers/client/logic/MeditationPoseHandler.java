package com.fiskmods.pika.lightsabers.client.logic;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class MeditationPoseHandler {

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        if (MeditationStateManager.isMeditating() && player.equals(Minecraft.getInstance().player)) {
            event.getPoseStack().pushPose();
            
            // Ein leichtes, atmosphärisches Schweben (Sinus-Welle) zusätzlich zur physischen Fixierung
            float floatTime = player.tickCount + event.getPartialTick();
            double hoverAnimation = Math.sin(floatTime * 0.05D) * 0.05D;
            
            event.getPoseStack().translate(0.0D, hoverAnimation, 0.0D);
        }
    }

    // NEU: Wichtig, damit das Spiel weiß, dass unsere Render-Verschiebungen für diesen Frame abgeschlossen sind
    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        if (MeditationStateManager.isMeditating() && player.equals(Minecraft.getInstance().player)) {
            event.getPoseStack().popPose();
        }
    }
}