package com.fiskmods.pika.lightsabers.command;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID)
public class ALCommandEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ALDebugCommand.register(event.getDispatcher());
    }
}