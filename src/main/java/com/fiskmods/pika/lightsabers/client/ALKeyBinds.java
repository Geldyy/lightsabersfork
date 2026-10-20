package com.fiskmods.pika.lightsabers.client;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class ALKeyBinds {

	public static final KeyMapping TOGGLE_LIGHTSABER = new KeyMapping("key.lightsabersfork.toggle",
			KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, "key.categories.lightsabersfork");

	public static final KeyMapping THROW_LIGHTSABER = new KeyMapping("key.lightsabersfork.throw_lightsaber",
			com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM, org.lwjgl.glfw.GLFW.GLFW_KEY_V,
			"key.categories.lightsabersfork");

	public static final KeyMapping SINGLE_SWITCH = new KeyMapping("key.lightsabersfork.single_switch",
			KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.categories.lightsabersfork");

	public static final KeyMapping CYCLE_FORCE = new KeyMapping("key.lightsabersfork.cycle_force",
			KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U, "key.categories.lightsabersfork");

	public static final KeyMapping USE_FORCE = new KeyMapping("key.lightsabersfork.use_force",
			KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.categories.lightsabersfork");

	public static final KeyMapping MEDITATE = new KeyMapping("key.lightsabersfork.meditate",
			KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.categories.lightsabersfork");

	public static void register(RegisterKeyMappingsEvent event) {
		event.register(TOGGLE_LIGHTSABER);
		event.register(THROW_LIGHTSABER);
		event.register(SINGLE_SWITCH);
		event.register(CYCLE_FORCE);
		event.register(USE_FORCE);
		event.register(MEDITATE);
	}
}