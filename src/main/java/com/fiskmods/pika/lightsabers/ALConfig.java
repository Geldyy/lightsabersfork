package com.fiskmods.pika.lightsabers;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ALConfig {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;

	public static final ModConfigSpec.DoubleValue ACTIVE_DAMAGE;
	public static final ModConfigSpec.DoubleValue ACTIVE_SPEED;
	public static final ModConfigSpec.DoubleValue RANGE_BOOST_PERCENTAGE;
	public static final ModConfigSpec.BooleanValue ALWAYS_SHOW_FORCE_HUD;

	public static final ModConfigSpec.IntValue INITIAL_POWER_USE_THRESHOLD;
	public static final ModConfigSpec.IntValue POWER_USE_STEP_INCREMENT;

	static {
		BUILDER.comment("Lightsaber Combat Stats")
				.translation("lightsabersfork.configuration.combat_stats")
				.push("Lightsaber Combat Stats");

		ACTIVE_DAMAGE = BUILDER.comment("Attack damage when the lightsaber is ignited. (Default: 12.0)")
				.translation("lightsabersfork.configuration.active_damage")
				.defineInRange("active_damage", 12.0, 0.0, 100.0);

		ACTIVE_SPEED = BUILDER.comment(
				"Attack speed (cooldown) when ignited. (Default: -2.4 = normal vanilla sword)")
				.translation("lightsabersfork.configuration.active_speed")
				.defineInRange("active_speed", -2.4, -4.0, 0.0);

		BUILDER.pop();

		BUILDER.comment("Lightsaber Utility")
				.translation("lightsabersfork.configuration.utility")
				.push("Lightsaber Utility");

		RANGE_BOOST_PERCENTAGE = BUILDER.comment(
				"Percentage range boost granted by having a lightsaber in inventory (e.g. 10.0 = 10% more range). Set to 0.0 to disable.")
				.translation("lightsabersfork.configuration.range_boost_percentage")
				.defineInRange("range_boost_percentage", 10.0, 0.0, 100.0);

		ALWAYS_SHOW_FORCE_HUD = BUILDER.comment(
				"When enabled (true), the Force HUD is permanently shown instead of fading out after 5 seconds.")
				.translation("lightsabersfork.configuration.always_show_force_hud")
				.define("alwaysShowForceHud", false);
		BUILDER.pop();

		BUILDER.comment("Force Power Progression")
				.translation("lightsabersfork.configuration.force_progression")
				.push("Force Power Progression");

		INITIAL_POWER_USE_THRESHOLD = BUILDER.comment(
				"Required power uses for the first bonus Force Point (Default: 25)")
				.translation("lightsabersfork.configuration.initial_power_use_threshold")
				.defineInRange("initialPowerUseThreshold", 25, 1, 10000);

		POWER_USE_STEP_INCREMENT = BUILDER.comment(
				"Increase of required power uses for each subsequent Force Point (stacking, e.g. 25 -> 50 more -> 75 more...) (Default: 25)")
				.translation("lightsabersfork.configuration.power_use_step_increment")
				.defineInRange("powerUseStepIncrement", 25, 1, 10000);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}
}