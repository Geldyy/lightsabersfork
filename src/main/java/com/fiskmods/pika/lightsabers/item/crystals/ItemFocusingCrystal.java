package com.fiskmods.pika.lightsabers.item.crystals;

import net.minecraft.world.item.Item;

public class ItemFocusingCrystal extends Item {
	private final FocusingCrystalType crystalType;

	public ItemFocusingCrystal(FocusingCrystalType crystalType, Properties properties) {
		super(properties);
		this.crystalType = crystalType;
	}

	public FocusingCrystalType getCrystalType() {
		return crystalType;
	}
}