package com.fiskmods.pika.lightsabers;

import com.fiskmods.pika.lightsabers.world.inventory.CrystalPouchMenu;
import com.fiskmods.pika.lightsabers.world.inventory.DisassemblyStationMenu;
import com.fiskmods.pika.lightsabers.world.inventory.LightsaberForgeMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ALMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU,
			AdvancedLightsabers.MODID);

	public static final DeferredHolder<MenuType<?>, MenuType<LightsaberForgeMenu>> LIGHTSABER_FORGE = MENUS
			.register("lightsaber_forge", () -> IMenuTypeExtension.create(LightsaberForgeMenu::new));

	public static final DeferredHolder<MenuType<?>, MenuType<DisassemblyStationMenu>> DISASSEMBLY_STATION = MENUS
			.register("disassembly_station", () -> IMenuTypeExtension.create(DisassemblyStationMenu::new));

	public static final DeferredHolder<MenuType<?>, MenuType<CrystalPouchMenu>> CRYSTAL_POUCH = MENUS
			.register("crystal_pouch", () -> IMenuTypeExtension.create(CrystalPouchMenu::new));
}