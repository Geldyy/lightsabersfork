package com.fiskmods.pika.lightsabers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ALSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = 
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, AdvancedLightsabers.MODID);

    // --- LICHTSCHWERT SOUNDS (Exakt aus 1.7.10) ---
    public static final DeferredHolder<SoundEvent, SoundEvent> LIGHTSABER_ON = register("player.lightsaber.on");
    public static final DeferredHolder<SoundEvent, SoundEvent> LIGHTSABER_OFF = register("player.lightsaber.off");
    public static final DeferredHolder<SoundEvent, SoundEvent> LIGHTSABER_HUM_MEDIUM = register("ambient.lightsaber.hum_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> LIGHTSABER_HUM_LIGHT = register("ambient.lightsaber.hum_light");
    public static final DeferredHolder<SoundEvent, SoundEvent> LIGHTSABER_HUM_HEAVY = register("ambient.lightsaber.hum_heavy");
    public static final DeferredHolder<SoundEvent, SoundEvent> LIGHTSABER_SWING = register("player.lightsaber.swing");
    public static final DeferredHolder<SoundEvent, SoundEvent> LIGHTSABER_HIT = register("player.lightsaber.hit");

    // --- MACHT SOUNDS ---
    public static final DeferredHolder<SoundEvent, SoundEvent> FORCE_CAST = register("player.force.cast");
    public static final DeferredHolder<SoundEvent, SoundEvent> FORCE_DARK = register("player.force.dark");
    public static final DeferredHolder<SoundEvent, SoundEvent> FORCE_FAIL = register("player.force.fail");
    public static final DeferredHolder<SoundEvent, SoundEvent> FORCE_LIGHTNING = register("player.force.lightning");
    public static final DeferredHolder<SoundEvent, SoundEvent> FORCE_HEAL = register("player.force.heal");
    public static final DeferredHolder<SoundEvent, SoundEvent> FORCE_STEALTH_ON = register("player.force.stealth.on");
    public static final DeferredHolder<SoundEvent, SoundEvent> FORCE_STEALTH_OFF = register("player.force.stealth.off");
    public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENT_STEALTH = register("ambient.stealth");
    public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENT_FORTIFY = register("ambient.fortify");

    // --- BLÖCKE ---
    public static final DeferredHolder<SoundEvent, SoundEvent> HOLOCRON_INVEST = register("block.holocron.invest");
    public static final DeferredHolder<SoundEvent, SoundEvent> HOLOCRON_UNLOCK = register("block.holocron.unlock");
    public static final DeferredHolder<SoundEvent, SoundEvent> SARCOPHAGUS_OPEN = register("block.sith_sarcophagus.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> SARCOPHAGUS_CLOSE = register("block.sith_sarcophagus.close");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, name)
        ));
    }
}