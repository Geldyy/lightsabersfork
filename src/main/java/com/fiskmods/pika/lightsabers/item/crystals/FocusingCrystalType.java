package com.fiskmods.pika.lightsabers.item.crystals;

public enum FocusingCrystalType {
    COMPRESSED("compressed"),
    CRACKED("cracked"),
    INVERTING("inverting"),
    FINE_CUT("fine_cut"),
    PRISMATIC("prismatic"),
	SHOTO("shoto"),
    EXTENDED("extended"),
    PULSE("pulse"),
    UNSTABLE("unstable");   

    private final String name;

    FocusingCrystalType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}