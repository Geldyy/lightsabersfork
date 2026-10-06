package com.fiskmods.pika.lightsabers.item.crystals;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.world.item.Item;

public class ItemColorCrystal extends Item {

    private final CrystalColor color;
    private static final Map<CrystalColor, ItemColorCrystal> BY_COLOR = new EnumMap<>(CrystalColor.class);

    public ItemColorCrystal(CrystalColor color, Properties properties) {
        super(properties);
        this.color = color;
        BY_COLOR.put(color, this); // Registriert den Kristall automatisch in der Lookup-Map
    }

    public CrystalColor getColor() {
        return this.color;
    }

    public static ItemColorCrystal getByColor(CrystalColor color) {
        return BY_COLOR.get(color);
    }
}