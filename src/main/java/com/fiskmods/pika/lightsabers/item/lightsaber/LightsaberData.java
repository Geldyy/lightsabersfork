package com.fiskmods.pika.lightsabers.item.lightsaber;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public record LightsaberData(Item colorCrystal, List<Item> focusCrystals, Item emitter, Item switchSection, Item grip,
		Item pommel, Item secSwitchSection, Item secGrip, Item secColorCrystal, List<Item> secFocusCrystals) {

	public LightsaberData(Item colorCrystal, List<Item> focusCrystals, Item emitter, Item switchSection, Item grip,
			Item pommel) {
		this(colorCrystal, focusCrystals, emitter, switchSection, grip, pommel, Items.AIR, Items.AIR, Items.AIR,
				List.of());
	}

	public static LightsaberData createDefault() {
		return new LightsaberData(
				com.fiskmods.pika.lightsabers.ALItems.LIGHT_BLUE_CRYSTAL.get(),
				List.of(),
				com.fiskmods.pika.lightsabers.ALItems.GRAFLEX_EMITTER.get(),
				com.fiskmods.pika.lightsabers.ALItems.GRAFLEX_SWITCH_SECTION.get(),
				com.fiskmods.pika.lightsabers.ALItems.GRAFLEX_GRIP.get(),
				com.fiskmods.pika.lightsabers.ALItems.GRAFLEX_POMMEL.get()
		);
	}

	public static final Codec<LightsaberData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("color_crystal").forGetter(LightsaberData::colorCrystal),
			BuiltInRegistries.ITEM.byNameCodec().listOf().optionalFieldOf("focus_crystals", List.of())
					.forGetter(LightsaberData::focusCrystals),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("emitter").forGetter(LightsaberData::emitter),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("switch_section").forGetter(LightsaberData::switchSection),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("grip").forGetter(LightsaberData::grip),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("pommel").forGetter(LightsaberData::pommel),
			BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("sec_switch_section", Items.AIR)
					.forGetter(LightsaberData::secSwitchSection),
			BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("sec_grip", Items.AIR)
					.forGetter(LightsaberData::secGrip),
			BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("sec_color_crystal", Items.AIR)
					.forGetter(LightsaberData::secColorCrystal),
			BuiltInRegistries.ITEM.byNameCodec().listOf().optionalFieldOf("sec_focus_crystals", List.of())
					.forGetter(LightsaberData::secFocusCrystals))
			.apply(instance, LightsaberData::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, LightsaberData> STREAM_CODEC = StreamCodec
			.of((buf, data) -> {
				ByteBufCodecs.registry(Registries.ITEM).encode(buf, data.colorCrystal());
				ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()).encode(buf, data.focusCrystals());
				ByteBufCodecs.registry(Registries.ITEM).encode(buf, data.emitter());
				ByteBufCodecs.registry(Registries.ITEM).encode(buf, data.switchSection());
				ByteBufCodecs.registry(Registries.ITEM).encode(buf, data.grip());
				ByteBufCodecs.registry(Registries.ITEM).encode(buf, data.pommel());
				ByteBufCodecs.registry(Registries.ITEM).encode(buf, data.secSwitchSection());
				ByteBufCodecs.registry(Registries.ITEM).encode(buf, data.secGrip());
				ByteBufCodecs.registry(Registries.ITEM).encode(buf, data.secColorCrystal());
				ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()).encode(buf,
						data.secFocusCrystals());
			}, buf -> new LightsaberData(ByteBufCodecs.registry(Registries.ITEM).decode(buf),
					ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()).decode(buf),
					ByteBufCodecs.registry(Registries.ITEM).decode(buf),
					ByteBufCodecs.registry(Registries.ITEM).decode(buf),
					ByteBufCodecs.registry(Registries.ITEM).decode(buf),
					ByteBufCodecs.registry(Registries.ITEM).decode(buf),
					ByteBufCodecs.registry(Registries.ITEM).decode(buf),
					ByteBufCodecs.registry(Registries.ITEM).decode(buf),
					ByteBufCodecs.registry(Registries.ITEM).decode(buf),
					ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()).decode(buf)));

	public boolean isDoubleBladed() {
		return pommel instanceof ItemLightsaberPart p && p.getPartType() == PartType.EMITTER;
	}

	public Item getSecondaryGrip() {
		return (secGrip != null && secGrip != Items.AIR) ? secGrip : grip;
	}

	public Item getSecondarySwitch() {
		return (secSwitchSection != null && secSwitchSection != Items.AIR) ? secSwitchSection : switchSection;
	}

	public Item getSecondaryColorCrystal() {
		return (secColorCrystal != null && secColorCrystal != Items.AIR) ? secColorCrystal : colorCrystal;
	}

	public List<Item> getSecondaryFocusCrystals() {
		return (secFocusCrystals != null && !secFocusCrystals.isEmpty()) ? secFocusCrystals : focusCrystals;
	}

	public boolean hasFocusCrystal(Item crystalItem) {
		return (focusCrystals != null && focusCrystals.contains(crystalItem))
				|| (secFocusCrystals != null && secFocusCrystals.contains(crystalItem));
	}
}