package com.fiskmods.pika.lightsabers;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AdvancedLightsabersClient {

    public AdvancedLightsabersClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Hier registrieren wir später:
        // - BlockEntity Renderer (für die Forge)
        // - Item Renderer (für die Lichtschwerter)
        // - Client Keybinds
        AdvancedLightsabers.LOGGER.info("Advanced Lightsabers Client-Setup erfolgreich!");
    }
}