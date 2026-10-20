package com.fiskmods.pika.lightsabers.client.logic;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.entity.player.Player;

public class MeditationClientTracker {

	private static final Map<Integer, Boolean> MEDITATING_PLAYERS = new HashMap<>();

	public static void setMeditating(int entityId, boolean isMeditating, boolean isFloating) {
		if (isMeditating) {
			MEDITATING_PLAYERS.put(entityId, isFloating);
		} else {
			MEDITATING_PLAYERS.remove(entityId);
		}
	}

	public static boolean isPlayerMeditating(Player player) {
		if (player == null)
			return false;
		if (player.isLocalPlayer()) {
			return MeditationStateManager.isMeditating();
		}
		return MEDITATING_PLAYERS.containsKey(player.getId());
	}

	public static boolean isPlayerFloating(Player player) {
		if (player == null)
			return false;
		if (player.isLocalPlayer()) {
			return MeditationStateManager.isPlayerFloating();
		}
		return Boolean.TRUE.equals(MEDITATING_PLAYERS.get(player.getId()));
	}

	public static void clear() {
		MEDITATING_PLAYERS.clear();
	}
}
