package com.fiskmods.pika.lightsabers.events;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.ALEntities;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.ALMenuTypes;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.ALKeyBinds;
import com.fiskmods.pika.lightsabers.client.gui.CrystalPouchScreen;
import com.fiskmods.pika.lightsabers.client.gui.DisassemblyStationScreen;
import com.fiskmods.pika.lightsabers.client.gui.LightsaberForgeScreen;
import com.fiskmods.pika.lightsabers.client.render.blocks.CrystalItemRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.DisassemblyStationItemRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.DisassemblyStationRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.HolocronItemRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.HolocronRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.LightsaberForgeItemRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.LightsaberForgeRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.LightsaberStandItemRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.LightsaberStandRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.SithCoffinItemRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.SithCoffinRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.SithStoneCoffinItemRenderer;
import com.fiskmods.pika.lightsabers.client.render.blocks.SithStoneCoffinRenderer;
import com.fiskmods.pika.lightsabers.client.render.lightsabers.LightsaberItemRenderer;
import com.fiskmods.pika.lightsabers.client.render.lightsabers.RenderLightsaberEntity;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelCrystal;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelDisassemblyStation;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelLightsaberForge;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelLightsaberStand;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelMonolithInner;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelMonolithOuter;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelSithCoffin;
import com.fiskmods.pika.lightsabers.client.render.models.blocks.ModelSithStoneCoffin;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
	
	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
	    event.register((stack, tintIndex) -> {
	        if (tintIndex == 1) {
	            int color = DyedItemColor.getOrDefault(stack, -1);
	            // Stellt sicher, dass der Alpha-Kanal immer voll auf 0xFF gesetzt ist
	            return color == -1 ? -1 : (0xFF000000 | color);
	        }
	        // layer0 (Basistasche): -1 entspricht 0xFFFFFFFF (voll deckendes Weiß / kein Farbfilter)
	        return -1;
	    }, ALItems.CRYSTAL_POUCH.get());
	}

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ALMenuTypes.LIGHTSABER_FORGE.get(), LightsaberForgeScreen::new);
        event.register(ALMenuTypes.DISASSEMBLY_STATION.get(), DisassemblyStationScreen::new);
        event.register(ALMenuTypes.CRYSTAL_POUCH.get(), CrystalPouchScreen::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelLightsaberStand.LAYER_LOCATION, ModelLightsaberStand::createBodyLayer);
        event.registerLayerDefinition(ModelDisassemblyStation.LAYER_LOCATION, ModelDisassemblyStation::createBodyLayer);
        event.registerLayerDefinition(ModelLightsaberForge.LAYER_LOCATION, ModelLightsaberForge::createBodyLayer);
        event.registerLayerDefinition(ModelCrystal.LAYER_LOCATION, ModelCrystal::createBodyLayer);
        event.registerLayerDefinition(ModelSithCoffin.LAYER_LOCATION, ModelSithCoffin::createBodyLayer);
        event.registerLayerDefinition(ModelSithStoneCoffin.LAYER_LOCATION, ModelSithStoneCoffin::createBodyLayer);
        event.registerLayerDefinition(ModelMonolithOuter.LAYER_LOCATION, ModelMonolithOuter::createBodyLayer);
        event.registerLayerDefinition(ModelMonolithInner.LAYER_LOCATION, ModelMonolithInner::createBodyLayer);
    
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ALEntities.LIGHTSABER.get(), RenderLightsaberEntity::new);
        event.registerBlockEntityRenderer(ALBlockEntities.LIGHTSABER_STAND.get(), LightsaberStandRenderer::new);
        event.registerBlockEntityRenderer(ALBlockEntities.DISASSEMBLY_STATION.get(), DisassemblyStationRenderer::new);
        event.registerBlockEntityRenderer(ALBlockEntities.LIGHTSABER_FORGE.get(), LightsaberForgeRenderer::new);
        event.registerBlockEntityRenderer(ALBlockEntities.HOLOCRON.get(), HolocronRenderer::new);
        event.registerBlockEntityRenderer(ALBlockEntities.SITH_COFFIN.get(), SithCoffinRenderer::new);
        event.registerBlockEntityRenderer(ALBlockEntities.SITH_STONE_COFFIN.get(), SithStoneCoffinRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        // 1. Lichtschwert-BEWLR
        event.registerItem(new IClientItemExtensions() {
            private LightsaberItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new LightsaberItemRenderer();
                }
                return this.renderer;
            }
        }, ALItems.LIGHTSABER.get());

        // 2. 3D-Lichtschwertständer-BEWLR
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return LightsaberStandItemRenderer.INSTANCE;
            }
        }, ALItems.LIGHTSABER_STAND.get());
        
     // 3D-Sarkophage BEWLR
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return SithCoffinItemRenderer.INSTANCE;
            }
        }, ALItems.SITH_COFFIN.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return SithStoneCoffinItemRenderer.INSTANCE;
            }
        }, ALItems.SITH_STONE_COFFIN.get());

        // 3. 3D-Demontagestation-BEWLR
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return DisassemblyStationItemRenderer.INSTANCE;
            }
        }, ALItems.DISASSEMBLY_STATION.get());

        // 4. 3D-Lichtschwert-Schmiede-BEWLR
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return LightsaberForgeItemRenderer.INSTANCE;
            }
        }, ALItems.LIGHTSABER_FORGE_LIGHT_ITEM.get());

        // 5. 3D-Holocron-BEWLR (Jedi & Sith)
        IClientItemExtensions holocronExtension = new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return HolocronItemRenderer.INSTANCE;
            }
        };
        event.registerItem(holocronExtension, ALItems.JEDI_HOLOCRON.get(), ALItems.SITH_HOLOCRON.get());

        // 6. 3D-Kristall-BEWLR für alle 18 Farben
        IClientItemExtensions crystalExtension = new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return CrystalItemRenderer.INSTANCE;
            }
        };

        event.registerItem(crystalExtension,
                ALItems.DEEP_BLUE_CRYSTAL.get(),
                ALItems.MEDIUM_BLUE_CRYSTAL.get(),
                ALItems.LIGHT_BLUE_CRYSTAL.get(),
                ALItems.ARCTIC_BLUE_CRYSTAL.get(),
                ALItems.WHITE_CRYSTAL.get(),
                ALItems.INDIGO_CRYSTAL.get(),
                ALItems.PURPLE_CRYSTAL.get(),
                ALItems.MAGENTA_CRYSTAL.get(),
                ALItems.PINK_CRYSTAL.get(),
                ALItems.RED_CRYSTAL.get(),
                ALItems.BLOOD_ORANGE_CRYSTAL.get(),
                ALItems.AMBER_CRYSTAL.get(),
                ALItems.YELLOW_CRYSTAL.get(),
                ALItems.GOLD_CRYSTAL.get(),
                ALItems.LIME_GREEN_CRYSTAL.get(),
                ALItems.GREEN_CRYSTAL.get(),
                ALItems.MINT_GREEN_CRYSTAL.get(),
                ALItems.CYAN_CRYSTAL.get()
        );
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        ALKeyBinds.register(event);
    }

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
    }
}