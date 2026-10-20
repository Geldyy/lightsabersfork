package com.fiskmods.pika.lightsabers;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = AdvancedLightsabers.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class AdvancedLightsabersClient {

	public AdvancedLightsabersClient(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		AdvancedLightsabers.LOGGER.info("Advanced Lightsabers Client-Setup erfolgreich!");
	}
}