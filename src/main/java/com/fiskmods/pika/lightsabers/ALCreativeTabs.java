package com.fiskmods.pika.lightsabers;

import com.fiskmods.pika.lightsabers.item.ItemCrystalPouch;
import com.fiskmods.pika.lightsabers.item.crystals.CrystalColor;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ALCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AdvancedLightsabers.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LIGHTSABERS_TAB = TABS.register("lightsabers_tab", 
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.lightsabersfork"))
            .icon(() -> new ItemStack(ALItems.RED_CRYSTAL.get()))
            .displayItems((parameters, output) -> {
                // ==========================================
                // 1. BLÖCKE & DEKORATION
                // ==========================================
                
                // Light Forcestone
                output.accept(ALItems.LIGHT_FORCESTONE.get());
                output.accept(ALItems.LIGHT_FORCESTONE_INSCRIBED.get());
                output.accept(ALItems.LIGHT_FORCESTONE_CRACKED.get());
                output.accept(ALItems.LIGHT_FORCESTONE_MOSSY.get());
                output.accept(ALItems.LIGHT_FORCESTONE_PILLAR.get());
                output.accept(ALItems.LIGHT_ACTIVATED_FORCESTONE_PILLAR.get());
                output.accept(ALItems.LIGHT_FORCESTONE_STAIRS.get());
                output.accept(ALItems.LIGHT_FORCESTONE_SLAB.get());

                // Dark Forcestone
                output.accept(ALItems.DARK_FORCESTONE.get());
                output.accept(ALItems.DARK_FORCESTONE_INSCRIBED.get());
                output.accept(ALItems.DARK_FORCESTONE_CRACKED.get());
                output.accept(ALItems.DARK_FORCESTONE_MOSSY.get());
                output.accept(ALItems.DARK_FORCESTONE_PILLAR.get());
                output.accept(ALItems.DARK_ACTIVATED_FORCESTONE_PILLAR.get());
                output.accept(ALItems.DARK_FORCESTONE_STAIRS.get());
                output.accept(ALItems.DARK_FORCESTONE_SLAB.get());
                
                // Black Forcestone
                output.accept(ALItems.BLACK_FORCESTONE.get());
                output.accept(ALItems.BLACK_FORCESTONE_INSCRIBED.get());
                output.accept(ALItems.BLACK_FORCESTONE_CRACKED.get());
                output.accept(ALItems.BLACK_FORCESTONE_MOSSY.get());
                output.accept(ALItems.BLACK_FORCESTONE_PILLAR.get());
                output.accept(ALItems.BLACK_ACTIVATED_FORCESTONE_PILLAR.get());
                output.accept(ALItems.BLACK_FORCESTONE_STAIRS.get());
                output.accept(ALItems.BLACK_FORCESTONE_SLAB.get());

                // Dungeons & Decoration
                output.accept(ALItems.SITH_COFFIN.get());
                output.accept(ALItems.SITH_STONE_COFFIN.get());
                
                // ==========================================
                // 2. FORCE ITEMS
                // ==========================================
                
                output.accept(ALItems.JEDI_HOLOCRON.get());
                output.accept(ALItems.SITH_HOLOCRON.get());

                // ==========================================
                // 3. UTILITY, ELEKTRONIK & WERKZEUGE
                // ==========================================
                
                output.accept(ALItems.LIGHTSABER_FORGE_LIGHT_ITEM.get());
                output.accept(ALItems.DISASSEMBLY_STATION.get());
                output.accept(ALItems.LIGHTSABER_STAND.get());
                output.accept(ALItems.CIRCUITRY.get());

                // ==========================================
                // 4. KRISTALLE (Fokus & Farben)
                // ==========================================
                
                // Focusing Crystals
                output.accept(ALItems.FOCUSING_CRYSTAL_COMPRESSED.get());
                output.accept(ALItems.FOCUSING_CRYSTAL_CRACKED.get());
                output.accept(ALItems.FOCUSING_CRYSTAL_INVERTING.get());
                output.accept(ALItems.FOCUSING_CRYSTAL_FINE_CUT.get());
                output.accept(ALItems.FOCUSING_CRYSTAL_PRISMATIC.get());
                output.accept(ALItems.FOCUSING_CRYSTAL_SHOTO.get());
                output.accept(ALItems.FOCUSING_CRYSTAL_EXTENDED.get());
                output.accept(ALItems.FOCUSING_CRYSTAL_UNSTABLE.get());
                output.accept(ALItems.FOCUSING_CRYSTAL_PULSE.get());

                // Color Crystals (Regenbogen-Farbverlauf)
                output.accept(ALItems.RED_CRYSTAL.get());
                output.accept(ALItems.BLOOD_ORANGE_CRYSTAL.get());
                output.accept(ALItems.AMBER_CRYSTAL.get());
                output.accept(ALItems.YELLOW_CRYSTAL.get());
                output.accept(ALItems.GOLD_CRYSTAL.get());
                output.accept(ALItems.LIME_GREEN_CRYSTAL.get());
                output.accept(ALItems.GREEN_CRYSTAL.get());
                output.accept(ALItems.MINT_GREEN_CRYSTAL.get());
                output.accept(ALItems.CYAN_CRYSTAL.get());
                output.accept(ALItems.LIGHT_BLUE_CRYSTAL.get());
                output.accept(ALItems.MEDIUM_BLUE_CRYSTAL.get());
                output.accept(ALItems.DEEP_BLUE_CRYSTAL.get());
                output.accept(ALItems.ARCTIC_BLUE_CRYSTAL.get());
                output.accept(ALItems.INDIGO_CRYSTAL.get());
                output.accept(ALItems.PURPLE_CRYSTAL.get());
                output.accept(ALItems.MAGENTA_CRYSTAL.get());
                output.accept(ALItems.PINK_CRYSTAL.get());
                output.accept(ALItems.WHITE_CRYSTAL.get());
                
                for (CrystalColor color : CrystalColor.values()) {
                    output.accept(ItemCrystalPouch.createWithColor(color));
                }
                
                // ==========================================
                // 5. LICHTSCHWERTER
                // ==========================================
                
                // Vaid Ancient (Antike / Pre-Prequels)
                output.accept(ALItems.VAID_ANCIENT_EMITTER.get());
                output.accept(ALItems.VAID_ANCIENT_SWITCH_SECTION.get());
                output.accept(ALItems.VAID_ANCIENT_GRIP.get());
                output.accept(ALItems.VAID_ANCIENT_POMMEL.get());

                // Mauler (Darth Maul - Episode I)
                output.accept(ALItems.MAULER_EMITTER.get());
                output.accept(ALItems.MAULER_SWITCH_SECTION.get());
                output.accept(ALItems.MAULER_GRIP.get());
                output.accept(ALItems.MAULER_POMMEL.get());

                // Graflex (Anakin Ep. III / Luke Ep. IV-V / Rey)
                output.accept(ALItems.GRAFLEX_EMITTER.get());
                output.accept(ALItems.GRAFLEX_SWITCH_SECTION.get());
                output.accept(ALItems.GRAFLEX_GRIP.get());
                output.accept(ALItems.GRAFLEX_POMMEL.get());

                // Prodigal Son (Luke Skywalker - Episode VI)
                output.accept(ALItems.PRODIGAL_SON_EMITTER.get());
                output.accept(ALItems.PRODIGAL_SON_SWITCH_SECTION.get());
                output.accept(ALItems.PRODIGAL_SON_GRIP.get());
                output.accept(ALItems.PRODIGAL_SON_POMMEL.get());

                // Redeemer (Obi-Wan Kenobi - Episode III/IV Inspired)
                output.accept(ALItems.REDEEMER_EMITTER.get());
                output.accept(ALItems.REDEEMER_SWITCH_SECTION.get());
                output.accept(ALItems.REDEEMER_GRIP.get());
                output.accept(ALItems.REDEEMER_POMMEL.get());

                // Reborn (Revan / Post-Return of the Jedi EU)
                output.accept(ALItems.REBORN_EMITTER.get());
                output.accept(ALItems.REBORN_SWITCH_SECTION.get());
                output.accept(ALItems.REBORN_GRIP.get());
                output.accept(ALItems.REBORN_POMMEL.get());

                // Vaid Modern (Moderne Ära)
                output.accept(ALItems.VAID_MODERN_EMITTER.get());
                output.accept(ALItems.VAID_MODERN_SWITCH_SECTION.get());
                output.accept(ALItems.VAID_MODERN_GRIP.get());
                output.accept(ALItems.VAID_MODERN_POMMEL.get());

                // Mechanical (Generisch / Custom)
                output.accept(ALItems.MECHANICAL_EMITTER.get());
                output.accept(ALItems.MECHANICAL_SWITCH_SECTION.get());
                output.accept(ALItems.MECHANICAL_GRIP.get());
                output.accept(ALItems.MECHANICAL_POMMEL.get());
                
                output.accept(ALItems.MANDALORIAN_EMITTER.get());
                output.accept(ALItems.MANDALORIAN_SWITCH_SECTION.get());
                output.accept(ALItems.MANDALORIAN_GRIP.get());
                output.accept(ALItems.MANDALORIAN_POMMEL.get());
                
                // Knighted Bauteile
                output.accept(ALItems.KNIGHTED_EMITTER.get());
                output.accept(ALItems.KNIGHTED_SWITCH_SECTION.get());
                output.accept(ALItems.KNIGHTED_GRIP.get());
                output.accept(ALItems.KNIGHTED_POMMEL.get());
                
                // Juggernaut Bauteile
                output.accept(ALItems.JUGGERNAUT_EMITTER.get());
                output.accept(ALItems.JUGGERNAUT_SWITCH_SECTION.get());
                output.accept(ALItems.JUGGERNAUT_GRIP.get());
                output.accept(ALItems.JUGGERNAUT_POMMEL.get());
                
                output.accept(ALItems.IMPERIAL_EMITTER.get());
                output.accept(ALItems.IMPERIAL_SWITCH_SECTION.get());
                output.accept(ALItems.IMPERIAL_GRIP.get());
                output.accept(ALItems.IMPERIAL_POMMEL.get());
                
             // Im .displayItems((parameters, output) -> { ... }) Block:
                output.accept(ALItems.FURY_EMITTER.get());
                output.accept(ALItems.FURY_SWITCH_SECTION.get());
                output.accept(ALItems.FURY_GRIP.get());
                output.accept(ALItems.FURY_POMMEL.get());
                
             // Im .displayItems((parameters, output) -> { ... }) Block:
                output.accept(ALItems.FULCRUM_EMITTER.get());
                output.accept(ALItems.FULCRUM_SWITCH_SECTION.get());
                output.accept(ALItems.FULCRUM_GRIP.get());
                output.accept(ALItems.FULCRUM_POMMEL.get());
                
             // Im .displayItems((parameters, output) -> { ... }) Block:
                output.accept(ALItems.DROIDEKA_EMITTER.get());
                output.accept(ALItems.DROIDEKA_SWITCH_SECTION.get());
                output.accept(ALItems.DROIDEKA_GRIP.get());
                output.accept(ALItems.DROIDEKA_POMMEL.get());

                // Rebel Parts (Auskommentiert lassen bis Geometrie gefixt ist)
                output.accept(ALItems.REBEL_EMITTER.get());
                output.accept(ALItems.REBEL_SWITCH_SECTION.get());
                output.accept(ALItems.REBEL_GRIP.get());
                output.accept(ALItems.REBEL_POMMEL.get()); 
            })
            .build()
    );
}