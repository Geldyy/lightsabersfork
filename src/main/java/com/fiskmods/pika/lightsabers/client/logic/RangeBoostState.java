package com.fiskmods.pika.lightsabers.client.logic;

public class RangeBoostState {
    private static boolean clientDisabled = false;

    public static boolean isDisabled() {
        return clientDisabled;
    }

    public static void toggle() {
        clientDisabled = !clientDisabled;
    }
    
}