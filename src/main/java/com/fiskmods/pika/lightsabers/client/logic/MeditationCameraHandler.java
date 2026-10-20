package com.fiskmods.pika.lightsabers.client.logic;


public class MeditationCameraHandler {

    public static float getInterpolatedProgress(float partialTick) {
        return MeditationStateManager.getTransitionProgress(partialTick);
    }
    
}