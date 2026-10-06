package com.fiskmods.pika.lightsabers.client.logic;

// KEIN @EventBusSubscriber mehr hier!
public class MeditationCameraHandler {

    // Diese Hilfsmethode bleibt, da dein Renderer sie abfragt
    public static float getInterpolatedProgress(float partialTick) {
        return MeditationStateManager.getTransitionProgress(partialTick);
    }
    
}