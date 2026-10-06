package com.fiskmods.pika.lightsabers.client.logic;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.sound.MovingSoundHum;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class ClientSoundEvents {

    private static final Set<UUID> PLAYING_HUM_PLAYERS = new HashSet<>();

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            PLAYING_HUM_PLAYERS.clear();
            return;
        }

        for (Player player : mc.level.players()) {
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();
            boolean active = (main.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(main)) ||
                             (off.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(off));

            UUID id = player.getUUID();
            if (active && !PLAYING_HUM_PLAYERS.contains(id)) {
                PLAYING_HUM_PLAYERS.add(id);
                mc.getSoundManager().play(new MovingSoundHum(player, () -> PLAYING_HUM_PLAYERS.remove(id)));
            }
        }
    }
}