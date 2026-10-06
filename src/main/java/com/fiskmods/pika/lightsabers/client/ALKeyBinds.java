package com.fiskmods.pika.lightsabers.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ALKeyBinds {
    // Standard-Taste ist 'R' (kann im Steuerung-Menü beliebig geändert werden)
    public static final KeyMapping TOGGLE_LIGHTSABER = new KeyMapping(
        "key.lightsabersfork.toggle",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_H,
        "key.categories.lightsabersfork"
    );
    
    public static final KeyMapping THROW_LIGHTSABER = new KeyMapping(
            "key.lightsabersfork.throw_lightsaber", 
            com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM, 
            org.lwjgl.glfw.GLFW.GLFW_KEY_V, // Standardtaste: V
            "key.categories.lightsabersfork" // Fix: Name korrigiert
        );

    public static final KeyMapping SINGLE_SWITCH = new KeyMapping(
            "key.lightsabersfork.single_switch",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.lightsabersfork"
        );
    
    // --- NEU FÜR MACHT-FÄHIGKEITEN ---
    public static final KeyMapping CYCLE_FORCE = new KeyMapping(
            "key.lightsabersfork.cycle_force",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U, // Standardtaste: R (Cycle)
            "key.categories.lightsabersfork"
        );

    public static final KeyMapping USE_FORCE = new KeyMapping(
            "key.lightsabersfork.use_force",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z, // Standardtaste: F (Use)
            "key.categories.lightsabersfork"
        );
    
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_LIGHTSABER);
        event.register(THROW_LIGHTSABER);
        event.register(SINGLE_SWITCH);
        event.register(CYCLE_FORCE);
        event.register(USE_FORCE);
    }
}