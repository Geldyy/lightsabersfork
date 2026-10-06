package com.fiskmods.pika.lightsabers;

import org.slf4j.Logger;

import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry;
import com.fiskmods.pika.lightsabers.recipe.ALRecipeSerializers;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(AdvancedLightsabers.MODID)
public class AdvancedLightsabers {
    
    public static final String MODID = "lightsabersfork";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AdvancedLightsabers(IEventBus modEventBus, ModContainer modContainer) {
        
        // Das Setup-Event an den Mod-Event-Bus binden
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, ALConfig.SPEC);

        // Registriert unsere Items und den Creative Tab beim Start des Spiels
        ALItems.ITEMS.register(modEventBus);
        ALCreativeTabs.TABS.register(modEventBus);
        ALBlocks.BLOCKS.register(modEventBus);
        ALBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ALMenuTypes.MENUS.register(modEventBus);
        ALDataComponents.COMPONENTS.register(modEventBus);
        ALEntities.ENTITIES.register(modEventBus);
        ALAttributes.ATTRIBUTES.register(modEventBus);
        ALAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
        ALSounds.SOUNDS.register(modEventBus);
        ALRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        	SkillTreeRegistry.init();
        });
    }

    
}