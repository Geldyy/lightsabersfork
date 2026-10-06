package com.fiskmods.pika.lightsabers.util;

import net.minecraft.world.entity.player.Player;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ServerRangeStates {
    private static final Set<UUID> disabledPlayers = new HashSet<>();

    public static boolean isDisabled(Player player) {
        return disabledPlayers.contains(player.getUUID());
    }

    public static void setDisabled(Player player, boolean disabled) {
        if (disabled) {
            disabledPlayers.add(player.getUUID());
        } else {
            disabledPlayers.remove(player.getUUID());
        }
    }
}