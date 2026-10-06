package com.fiskmods.pika.lightsabers;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ALConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue ACTIVE_DAMAGE;
    public static final ModConfigSpec.DoubleValue ACTIVE_SPEED;
    public static final ModConfigSpec.DoubleValue RANGE_BOOST_PERCENTAGE;

    static {
        BUILDER.push("Lightsaber Combat Stats");

        ACTIVE_DAMAGE = BUILDER.comment("Angriffsschaden, wenn das Lichtschwert ausgefahren ist. (Standard: 12.0)")
                .defineInRange("active_damage", 12.0, 0.0, 100.0);

        ACTIVE_SPEED = BUILDER.comment("Angriffsgeschwindigkeit (Cooldown), wenn ausgefahren. (Standard: -2.4 = normales Vanilla-Schwert)")
                .defineInRange("active_speed", -2.4, -4.0, 0.0);

        BUILDER.pop();
        
        BUILDER.push("Lightsaber Utility");
        RANGE_BOOST_PERCENTAGE = BUILDER.comment("Prozentualer Reichweiten-Boost durch ein Lichtschwert im Inventar (z.B. 10.0 = 10% mehr Reichweite). Setze auf 0.0 zum Deaktivieren.")
                .defineInRange("range_boost_percentage", 10.0, 0.0, 100.0);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}