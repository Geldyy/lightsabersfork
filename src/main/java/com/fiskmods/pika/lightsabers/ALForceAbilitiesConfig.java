package com.fiskmods.pika.lightsabers;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ALForceAbilitiesConfig {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;

	// ==========================================
	// FORCE BARRIER (formerly Force Fortify)
	// ==========================================
	public static final ModConfigSpec.DoubleValue BARRIER_START_COST_T1;
	public static final ModConfigSpec.DoubleValue BARRIER_START_COST_T2;
	public static final ModConfigSpec.DoubleValue BARRIER_START_COST_T3;

	public static final ModConfigSpec.DoubleValue BARRIER_HOLD_COST_T1;
	public static final ModConfigSpec.DoubleValue BARRIER_HOLD_COST_T2;
	public static final ModConfigSpec.DoubleValue BARRIER_HOLD_COST_T3;

	public static final ModConfigSpec.DoubleValue BARRIER_CAPACITY_T1;
	public static final ModConfigSpec.DoubleValue BARRIER_CAPACITY_T2;
	public static final ModConfigSpec.DoubleValue BARRIER_CAPACITY_T3;

	public static final ModConfigSpec.DoubleValue BARRIER_ENERGY_PER_DAMAGE_T1;
	public static final ModConfigSpec.DoubleValue BARRIER_ENERGY_PER_DAMAGE_T2;
	public static final ModConfigSpec.DoubleValue BARRIER_ENERGY_PER_DAMAGE_T3;

	public static final ModConfigSpec.DoubleValue BARRIER_RADIUS_T1;
	public static final ModConfigSpec.DoubleValue BARRIER_RADIUS_T2;
	public static final ModConfigSpec.DoubleValue BARRIER_RADIUS_T3;

	// ==========================================
	// FORCE HEAL
	// ==========================================
	public static final ModConfigSpec.DoubleValue HEAL_COST_T1;
	public static final ModConfigSpec.DoubleValue HEAL_COST_T2;
	public static final ModConfigSpec.DoubleValue HEAL_COST_T3;

	public static final ModConfigSpec.IntValue HEAL_COOLDOWN_TICKS_T1;
	public static final ModConfigSpec.IntValue HEAL_COOLDOWN_TICKS_T2;
	public static final ModConfigSpec.IntValue HEAL_COOLDOWN_TICKS_T3;

	public static final ModConfigSpec.DoubleValue HEAL_AMOUNT_T1;
	public static final ModConfigSpec.DoubleValue HEAL_AMOUNT_T2;
	public static final ModConfigSpec.DoubleValue HEAL_AMOUNT_T3;

	public static final ModConfigSpec.DoubleValue HEAL_ALLY_AMOUNT_T2;
	public static final ModConfigSpec.DoubleValue HEAL_ALLY_AMOUNT_T3;
	public static final ModConfigSpec.DoubleValue HEAL_RADIUS_T2;
	public static final ModConfigSpec.DoubleValue HEAL_RADIUS_T3;

	// ==========================================
	// ELECTRIC JUDGEMENT
	// ==========================================
	public static final ModConfigSpec.DoubleValue EJ_HOLD_COST_T1;
	public static final ModConfigSpec.DoubleValue EJ_HOLD_COST_T2;
	public static final ModConfigSpec.DoubleValue EJ_HOLD_COST_T3;
	public static final ModConfigSpec.DoubleValue EJ_HOLD_COST_T4;

	public static final ModConfigSpec.DoubleValue EJ_REACH_T1;
	public static final ModConfigSpec.DoubleValue EJ_REACH_T2;
	public static final ModConfigSpec.DoubleValue EJ_REACH_T3;
	public static final ModConfigSpec.DoubleValue EJ_REACH_T4;

	public static final ModConfigSpec.DoubleValue EJ_CHAIN_RANGE_T2;
	public static final ModConfigSpec.DoubleValue EJ_CHAIN_RANGE_T3;
	public static final ModConfigSpec.DoubleValue EJ_CHAIN_RANGE_T4;

	public static final ModConfigSpec.IntValue EJ_MAX_TARGETS_T1;
	public static final ModConfigSpec.IntValue EJ_MAX_TARGETS_T2;
	public static final ModConfigSpec.IntValue EJ_MAX_TARGETS_T3;
	public static final ModConfigSpec.IntValue EJ_MAX_TARGETS_T4;

	public static final ModConfigSpec.DoubleValue EJ_DAMAGE_UNDEAD_T1;
	public static final ModConfigSpec.DoubleValue EJ_DAMAGE_UNDEAD_T2;
	public static final ModConfigSpec.DoubleValue EJ_DAMAGE_UNDEAD_T3;
	public static final ModConfigSpec.DoubleValue EJ_DAMAGE_UNDEAD_T4;

	public static final ModConfigSpec.DoubleValue EJ_DAMAGE_SITH_T1;
	public static final ModConfigSpec.DoubleValue EJ_DAMAGE_SITH_T2;
	public static final ModConfigSpec.DoubleValue EJ_DAMAGE_SITH_T3;
	public static final ModConfigSpec.DoubleValue EJ_DAMAGE_SITH_T4;

	public static final ModConfigSpec.DoubleValue EJ_CONVERT_CHANCE_T1;
	public static final ModConfigSpec.DoubleValue EJ_CONVERT_CHANCE_T2;
	public static final ModConfigSpec.DoubleValue EJ_CONVERT_CHANCE_T3;
	public static final ModConfigSpec.DoubleValue EJ_CONVERT_CHANCE_T4;

	public static final ModConfigSpec.BooleanValue EJ_CONVERT_WITCH;
	public static final ModConfigSpec.BooleanValue EJ_CONVERT_SKELETON;
	public static final ModConfigSpec.BooleanValue EJ_CONVERT_ZOMBIE;
	public static final ModConfigSpec.BooleanValue EJ_ENDERMAN_FAKEOUT;

	public static final ModConfigSpec.IntValue EJ_SILENCE_TICKS_T3;
	public static final ModConfigSpec.DoubleValue EJ_SITH_COOLDOWN_INCREASE_T2;

	// ==========================================
	// FORCE LIGHTNING (Sith)
	// ==========================================
	public static final ModConfigSpec.DoubleValue LIGHTNING_HOLD_COST_T1;
	public static final ModConfigSpec.DoubleValue LIGHTNING_HOLD_COST_T2;
	public static final ModConfigSpec.DoubleValue LIGHTNING_HOLD_COST_T3;

	public static final ModConfigSpec.DoubleValue LIGHTNING_REACH_T1;
	public static final ModConfigSpec.DoubleValue LIGHTNING_REACH_T2;
	public static final ModConfigSpec.DoubleValue LIGHTNING_REACH_T3;

	public static final ModConfigSpec.DoubleValue LIGHTNING_CHAIN_RANGE_T2;
	public static final ModConfigSpec.DoubleValue LIGHTNING_CHAIN_RANGE_T3;

	public static final ModConfigSpec.IntValue LIGHTNING_MAX_TARGETS_T1;
	public static final ModConfigSpec.IntValue LIGHTNING_MAX_TARGETS_T2;
	public static final ModConfigSpec.IntValue LIGHTNING_MAX_TARGETS_T3;

	public static final ModConfigSpec.DoubleValue LIGHTNING_DAMAGE_PRIMARY_T1;
	public static final ModConfigSpec.DoubleValue LIGHTNING_DAMAGE_PRIMARY_T2;
	public static final ModConfigSpec.DoubleValue LIGHTNING_DAMAGE_CHAIN_T2;
	public static final ModConfigSpec.DoubleValue LIGHTNING_DAMAGE_PRIMARY_T3;
	public static final ModConfigSpec.DoubleValue LIGHTNING_DAMAGE_CHAIN_T3;
	public static final ModConfigSpec.DoubleValue LIGHTNING_ARMOR_PENETRATION_T3;

	public static final ModConfigSpec.DoubleValue LIGHTNING_CORRUPT_CHANCE_T1;
	public static final ModConfigSpec.DoubleValue LIGHTNING_CORRUPT_CHANCE_T2;
	public static final ModConfigSpec.DoubleValue LIGHTNING_CORRUPT_CHANCE_T3;

	// ==========================================
	// FORCE CHOKE (Sith +80% default costs)
	// ==========================================
	public static final ModConfigSpec.DoubleValue CHOKE_HOLD_COST_T1;
	public static final ModConfigSpec.DoubleValue CHOKE_HOLD_COST_T2;
	public static final ModConfigSpec.DoubleValue CHOKE_HOLD_COST_T3;

	public static final ModConfigSpec.DoubleValue CHOKE_REACH_T1;
	public static final ModConfigSpec.DoubleValue CHOKE_REACH_T2;
	public static final ModConfigSpec.DoubleValue CHOKE_REACH_T3;

	public static final ModConfigSpec.IntValue CHOKE_MAX_TARGETS_T1;
	public static final ModConfigSpec.IntValue CHOKE_MAX_TARGETS_T2;
	public static final ModConfigSpec.IntValue CHOKE_MAX_TARGETS_T3;

	public static final ModConfigSpec.DoubleValue CHOKE_DAMAGE_T1;
	public static final ModConfigSpec.DoubleValue CHOKE_DAMAGE_T2;
	public static final ModConfigSpec.DoubleValue CHOKE_DAMAGE_T3;

	public static final ModConfigSpec.DoubleValue CHOKE_EXECUTE_THRESHOLD;

	// ==========================================
	// FORCE DRAIN (Sith +80% default costs)
	// ==========================================
	public static final ModConfigSpec.DoubleValue DRAIN_HOLD_COST_T1;
	public static final ModConfigSpec.DoubleValue DRAIN_HOLD_COST_T2;
	public static final ModConfigSpec.DoubleValue DRAIN_HOLD_COST_T3;

	public static final ModConfigSpec.DoubleValue DRAIN_REACH_T1;
	public static final ModConfigSpec.DoubleValue DRAIN_REACH_T2;
	public static final ModConfigSpec.DoubleValue DRAIN_REACH_T3;

	public static final ModConfigSpec.IntValue DRAIN_MAX_TARGETS_T1;
	public static final ModConfigSpec.IntValue DRAIN_MAX_TARGETS_T2;
	public static final ModConfigSpec.IntValue DRAIN_MAX_TARGETS_T3;

	public static final ModConfigSpec.DoubleValue DRAIN_HP_T1;
	public static final ModConfigSpec.DoubleValue DRAIN_HP_T2;
	public static final ModConfigSpec.DoubleValue DRAIN_HP_T3;

	public static final ModConfigSpec.DoubleValue DRAIN_ENERGY_T1;
	public static final ModConfigSpec.DoubleValue DRAIN_ENERGY_T2;
	public static final ModConfigSpec.DoubleValue DRAIN_ENERGY_T3;

	// ==========================================
	// FORCE PUSH
	// ==========================================
	public static final ModConfigSpec.DoubleValue PUSH_COST_T1;
	public static final ModConfigSpec.DoubleValue PUSH_COST_T2;
	public static final ModConfigSpec.DoubleValue PUSH_COST_T3;
	public static final ModConfigSpec.DoubleValue PUSH_COST_T4;

	public static final ModConfigSpec.DoubleValue PUSH_REACH_T1;
	public static final ModConfigSpec.DoubleValue PUSH_REACH_T2;
	public static final ModConfigSpec.DoubleValue PUSH_REACH_T3;
	public static final ModConfigSpec.DoubleValue PUSH_REACH_T4;

	public static final ModConfigSpec.DoubleValue PUSH_STRENGTH_T1;
	public static final ModConfigSpec.DoubleValue PUSH_STRENGTH_T2;
	public static final ModConfigSpec.DoubleValue PUSH_STRENGTH_T3;
	public static final ModConfigSpec.DoubleValue PUSH_STRENGTH_T4;

	// ==========================================
	// FORCE PULL
	// ==========================================
	public static final ModConfigSpec.DoubleValue PULL_COST_T1;
	public static final ModConfigSpec.DoubleValue PULL_COST_T2;
	public static final ModConfigSpec.DoubleValue PULL_COST_T3;

	public static final ModConfigSpec.DoubleValue PULL_REACH_T1;
	public static final ModConfigSpec.DoubleValue PULL_REACH_T2;
	public static final ModConfigSpec.DoubleValue PULL_REACH_T3;
	public static ModConfigSpec.DoubleValue PULL_RADIUS_T1;
	public static ModConfigSpec.DoubleValue PULL_RADIUS_T2;
	public static ModConfigSpec.DoubleValue PULL_RADIUS_T3;

	public static final ModConfigSpec.IntValue PULL_MAX_TARGETS_T1;
	public static final ModConfigSpec.IntValue PULL_MAX_TARGETS_T2;
	public static final ModConfigSpec.IntValue PULL_MAX_TARGETS_T3;

	public static final ModConfigSpec.DoubleValue PULL_STRENGTH_T1;
	public static final ModConfigSpec.DoubleValue PULL_STRENGTH_T2;
	public static final ModConfigSpec.DoubleValue PULL_STRENGTH_T3;

	// ==========================================
	// FORCE DASH
	// ==========================================
	public static final ModConfigSpec.DoubleValue DASH_COST_T1;
	public static final ModConfigSpec.DoubleValue DASH_COST_T2;
	public static final ModConfigSpec.DoubleValue DASH_COST_T3;

	public static final ModConfigSpec.DoubleValue DASH_SPEED_T1;
	public static final ModConfigSpec.DoubleValue DASH_SPEED_T2;
	public static final ModConfigSpec.DoubleValue DASH_SPEED_T3;

	// ==========================================
	// FORCE JUMP
	// ==========================================
	public static final ModConfigSpec.DoubleValue JUMP_COST_T1;
	public static final ModConfigSpec.DoubleValue JUMP_COST_T2;
	public static final ModConfigSpec.DoubleValue JUMP_COST_T3;

	public static final ModConfigSpec.DoubleValue JUMP_STRENGTH_T1;
	public static final ModConfigSpec.DoubleValue JUMP_STRENGTH_T2;
	public static final ModConfigSpec.DoubleValue JUMP_STRENGTH_T3;

	// ==========================================
	// FORCE REPULSE
	// ==========================================
	public static final ModConfigSpec.DoubleValue REPULSE_COST_T1;
	public static final ModConfigSpec.DoubleValue REPULSE_COST_T2;
	public static final ModConfigSpec.DoubleValue REPULSE_COST_T3;

	public static final ModConfigSpec.DoubleValue REPULSE_RADIUS_T1;
	public static final ModConfigSpec.DoubleValue REPULSE_RADIUS_T2;
	public static final ModConfigSpec.DoubleValue REPULSE_RADIUS_T3;

	public static final ModConfigSpec.DoubleValue REPULSE_DAMAGE_T1;
	public static final ModConfigSpec.DoubleValue REPULSE_DAMAGE_T2;
	public static final ModConfigSpec.DoubleValue REPULSE_DAMAGE_T3;

	// ==========================================
	// FORCE STASIS
	// ==========================================
	public static final ModConfigSpec.DoubleValue STASIS_COST_T1;
	public static final ModConfigSpec.DoubleValue STASIS_COST_T2;
	public static final ModConfigSpec.DoubleValue STASIS_COST_T3;

	public static final ModConfigSpec.DoubleValue STASIS_REACH_T1;
	public static final ModConfigSpec.DoubleValue STASIS_REACH_T2;
	public static final ModConfigSpec.DoubleValue STASIS_REACH_T3;

	public static final ModConfigSpec.IntValue STASIS_DURATION_TICKS_T1;
	public static final ModConfigSpec.IntValue STASIS_DURATION_TICKS_T2;
	public static final ModConfigSpec.IntValue STASIS_DURATION_TICKS_T3;

	// ==========================================
	// FORCE SLAM
	// ==========================================
	public static final ModConfigSpec.DoubleValue SLAM_COST_T1;
	public static final ModConfigSpec.DoubleValue SLAM_COST_T2;
	public static final ModConfigSpec.DoubleValue SLAM_COST_T3;

	public static final ModConfigSpec.DoubleValue SLAM_RADIUS_T1;
	public static final ModConfigSpec.DoubleValue SLAM_RADIUS_T2;
	public static final ModConfigSpec.DoubleValue SLAM_RADIUS_T3;

	public static final ModConfigSpec.DoubleValue SLAM_DAMAGE_T1;
	public static final ModConfigSpec.DoubleValue SLAM_DAMAGE_T2;
	public static final ModConfigSpec.DoubleValue SLAM_DAMAGE_T3;

	// ==========================================
	// FORCE TELEKINESIS
	// ==========================================
	public static final ModConfigSpec.DoubleValue TELEKINESIS_HOLD_COST_T1;
	public static final ModConfigSpec.DoubleValue TELEKINESIS_HOLD_COST_T2;
	public static final ModConfigSpec.DoubleValue TELEKINESIS_HOLD_COST_T3;

	public static final ModConfigSpec.DoubleValue TELEKINESIS_REACH_T1;
	public static final ModConfigSpec.DoubleValue TELEKINESIS_REACH_T2;
	public static final ModConfigSpec.DoubleValue TELEKINESIS_REACH_T3;

	public static final ModConfigSpec.DoubleValue TELEKINESIS_THROW_FORCE_T1;
	public static final ModConfigSpec.DoubleValue TELEKINESIS_THROW_FORCE_T2;
	public static final ModConfigSpec.DoubleValue TELEKINESIS_THROW_FORCE_T3;

	static {
		BUILDER.comment("=========================================================================",
				" Advanced Lightsabers - Force Abilities Configuration",
				" Here you can configure all stats, costs, ranges, and damages for all Force Abilities.",
				"=========================================================================");

		// ---------------------------------------------------------------------
		// FORCE BARRIER
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Barrier Settings").translation("lightsabersfork.configuration.force_barrier").push("force_barrier");
		BARRIER_START_COST_T1 = BUILDER.comment("Activation cost (aF) for Tier 1").translation("lightsabersfork.configuration.force_barrier.start_cost_t1").defineInRange("start_cost_t1", 15.0, 0.0, 1000.0);
		BARRIER_START_COST_T2 = BUILDER.comment("Activation cost (aF) for Tier 2").translation("lightsabersfork.configuration.force_barrier.start_cost_t2").defineInRange("start_cost_t2", 25.0, 0.0, 1000.0);
		BARRIER_START_COST_T3 = BUILDER.comment("Activation cost (aF) for Tier 3").translation("lightsabersfork.configuration.force_barrier.start_cost_t3").defineInRange("start_cost_t3", 40.0, 0.0, 1000.0);

		BARRIER_HOLD_COST_T1 = BUILDER.comment("Holding cost (aF/s) for Tier 1").translation("lightsabersfork.configuration.force_barrier.hold_cost_per_second_t1").defineInRange("hold_cost_per_second_t1", 2.5, 0.0, 500.0);
		BARRIER_HOLD_COST_T2 = BUILDER.comment("Holding cost (aF/s) for Tier 2").translation("lightsabersfork.configuration.force_barrier.hold_cost_per_second_t2").defineInRange("hold_cost_per_second_t2", 4.0, 0.0, 500.0);
		BARRIER_HOLD_COST_T3 = BUILDER.comment("Holding cost (aF/s) for Tier 3 (Default: 0.5 aF/s)").translation("lightsabersfork.configuration.force_barrier.hold_cost_per_second_t3").defineInRange("hold_cost_per_second_t3", 0.5, 0.0, 500.0);

		BARRIER_CAPACITY_T1 = BUILDER.comment("Max damage absorbed before shield shatters on Tier 1 (Default: 100 DMG)").translation("lightsabersfork.configuration.force_barrier.absorb_capacity_t1").defineInRange("absorb_capacity_t1", 100.0, 1.0, 100000.0);
		BARRIER_CAPACITY_T2 = BUILDER.comment("Max damage absorbed before shield shatters on Tier 2 (Default: 300 DMG)").translation("lightsabersfork.configuration.force_barrier.absorb_capacity_t2").defineInRange("absorb_capacity_t2", 300.0, 1.0, 100000.0);
		BARRIER_CAPACITY_T3 = BUILDER.comment("Max damage absorbed before shield shatters on Tier 3 (Default: 1300 DMG)").translation("lightsabersfork.configuration.force_barrier.absorb_capacity_t3").defineInRange("absorb_capacity_t3", 1300.0, 1.0, 100000.0);

		BARRIER_ENERGY_PER_DAMAGE_T1 = BUILDER.comment("Force energy consumed per 1 DMG absorbed on Tier 1").translation("lightsabersfork.configuration.force_barrier.energy_per_damage_t1").defineInRange("energy_per_damage_t1", 0.5, 0.0, 100.0);
		BARRIER_ENERGY_PER_DAMAGE_T2 = BUILDER.comment("Force energy consumed per 1 DMG absorbed on Tier 2").translation("lightsabersfork.configuration.force_barrier.energy_per_damage_t2").defineInRange("energy_per_damage_t2", 0.35, 0.0, 100.0);
		BARRIER_ENERGY_PER_DAMAGE_T3 = BUILDER.comment("Force energy consumed per 1 DMG absorbed on Tier 3 (Default: 0.25 aF per DMG, i.e. 0.5 aF per 2 DMG)").translation("lightsabersfork.configuration.force_barrier.energy_per_damage_t3").defineInRange("energy_per_damage_t3", 0.25, 0.0, 100.0);

		BARRIER_RADIUS_T1 = BUILDER.comment("Barrier radius (blocks) for Tier 1").translation("lightsabersfork.configuration.force_barrier.radius_t1").defineInRange("radius_t1", 2.0, 0.5, 50.0);
		BARRIER_RADIUS_T2 = BUILDER.comment("Barrier radius (blocks) for Tier 2").translation("lightsabersfork.configuration.force_barrier.radius_t2").defineInRange("radius_t2", 3.5, 0.5, 50.0);
		BARRIER_RADIUS_T3 = BUILDER.comment("Barrier radius (blocks) for Tier 3 Dome").translation("lightsabersfork.configuration.force_barrier.radius_t3").defineInRange("radius_t3", 7.0, 0.5, 50.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE HEAL
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Heal Settings").translation("lightsabersfork.configuration.force_heal").push("force_heal");
		HEAL_COST_T1 = BUILDER.comment("Force cost (aF) for Tier 1").translation("lightsabersfork.configuration.force_heal.cost_t1").defineInRange("cost_t1", 18.0, 0.0, 1000.0);
		HEAL_COST_T2 = BUILDER.comment("Force cost (aF) for Tier 2").translation("lightsabersfork.configuration.force_heal.cost_t2").defineInRange("cost_t2", 28.0, 0.0, 1000.0);
		HEAL_COST_T3 = BUILDER.comment("Force cost (aF) for Tier 3").translation("lightsabersfork.configuration.force_heal.cost_t3").defineInRange("cost_t3", 45.0, 0.0, 1000.0);

		HEAL_COOLDOWN_TICKS_T1 = BUILDER.comment("Cooldown ticks for Tier 1 (160 ticks = 8s)").translation("lightsabersfork.configuration.force_heal.cooldown_ticks_t1").defineInRange("cooldown_ticks_t1", 160, 0, 10000);
		HEAL_COOLDOWN_TICKS_T2 = BUILDER.comment("Cooldown ticks for Tier 2 (200 ticks = 10s)").translation("lightsabersfork.configuration.force_heal.cooldown_ticks_t2").defineInRange("cooldown_ticks_t2", 200, 0, 10000);
		HEAL_COOLDOWN_TICKS_T3 = BUILDER.comment("Cooldown ticks for Tier 3 (280 ticks = 14s)").translation("lightsabersfork.configuration.force_heal.cooldown_ticks_t3").defineInRange("cooldown_ticks_t3", 280, 0, 10000);

		HEAL_AMOUNT_T1 = BUILDER.comment("Self healing (HP) for Tier 1").translation("lightsabersfork.configuration.force_heal.self_heal_hp_t1").defineInRange("self_heal_hp_t1", 6.0, 0.0, 1000.0);
		HEAL_AMOUNT_T2 = BUILDER.comment("Self healing (HP) for Tier 2").translation("lightsabersfork.configuration.force_heal.self_heal_hp_t2").defineInRange("self_heal_hp_t2", 10.0, 0.0, 1000.0);
		HEAL_AMOUNT_T3 = BUILDER.comment("Self healing (HP) for Tier 3").translation("lightsabersfork.configuration.force_heal.self_heal_hp_t3").defineInRange("self_heal_hp_t3", 16.0, 0.0, 1000.0);

		HEAL_ALLY_AMOUNT_T2 = BUILDER.comment("Ally healing (HP) for Tier 2").translation("lightsabersfork.configuration.force_heal.ally_heal_hp_t2").defineInRange("ally_heal_hp_t2", 10.0, 0.0, 1000.0);
		HEAL_ALLY_AMOUNT_T3 = BUILDER.comment("Ally healing (HP) for Tier 3").translation("lightsabersfork.configuration.force_heal.ally_heal_hp_t3").defineInRange("ally_heal_hp_t3", 16.0, 0.0, 1000.0);

		HEAL_RADIUS_T2 = BUILDER.comment("Ally healing radius (blocks) for Tier 2").translation("lightsabersfork.configuration.force_heal.heal_radius_t2").defineInRange("heal_radius_t2", 8.0, 1.0, 100.0);
		HEAL_RADIUS_T3 = BUILDER.comment("Ally healing radius (blocks) for Tier 3").translation("lightsabersfork.configuration.force_heal.heal_radius_t3").defineInRange("heal_radius_t3", 14.0, 1.0, 100.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// ELECTRIC JUDGEMENT
		// ---------------------------------------------------------------------
		BUILDER.comment("Electric Judgment Settings").translation("lightsabersfork.configuration.electric_judgement").push("electric_judgement");
		EJ_HOLD_COST_T1 = BUILDER.comment("Holding cost (aF/s) for Tier 1 (Default: 30 aF/s = 1.5 aF/tick)").translation("lightsabersfork.configuration.electric_judgement.hold_cost_per_second_t1").defineInRange("hold_cost_per_second_t1", 30.0, 0.0, 500.0);
		EJ_HOLD_COST_T2 = BUILDER.comment("Holding cost (aF/s) for Tier 2 (Default: 50 aF/s = 2.5 aF/tick)").translation("lightsabersfork.configuration.electric_judgement.hold_cost_per_second_t2").defineInRange("hold_cost_per_second_t2", 50.0, 0.0, 500.0);
		EJ_HOLD_COST_T3 = BUILDER.comment("Holding cost (aF/s) for Tier 3 (Default: 90 aF/s = 4.5 aF/tick)").translation("lightsabersfork.configuration.electric_judgement.hold_cost_per_second_t3").defineInRange("hold_cost_per_second_t3", 90.0, 0.0, 500.0);
		EJ_HOLD_COST_T4 = BUILDER.comment("Holding cost (aF/s) for Tier 4 (Default: 50 aF/s = 2.5 aF/tick)").translation("lightsabersfork.configuration.electric_judgement.hold_cost_per_second_t4").defineInRange("hold_cost_per_second_t4", 50.0, 0.0, 500.0);

		EJ_REACH_T1 = BUILDER.comment("Reach (blocks) for Tier 1").translation("lightsabersfork.configuration.electric_judgement.reach_t1").defineInRange("reach_t1", 16.0, 1.0, 100.0);
		EJ_REACH_T2 = BUILDER.comment("Reach (blocks) for Tier 2").translation("lightsabersfork.configuration.electric_judgement.reach_t2").defineInRange("reach_t2", 24.0, 1.0, 100.0);
		EJ_REACH_T3 = BUILDER.comment("Reach (blocks) for Tier 3").translation("lightsabersfork.configuration.electric_judgement.reach_t3").defineInRange("reach_t3", 32.0, 1.0, 100.0);
		EJ_REACH_T4 = BUILDER.comment("Reach (blocks) for Tier 4").translation("lightsabersfork.configuration.electric_judgement.reach_t4").defineInRange("reach_t4", 24.0, 1.0, 100.0);

		EJ_CHAIN_RANGE_T2 = BUILDER.comment("Chain jump distance (blocks) for Tier 2 (Default: 8.0)").translation("lightsabersfork.configuration.electric_judgement.chain_range_t2").defineInRange("chain_range_t2", 8.0, 1.0, 50.0);
		EJ_CHAIN_RANGE_T3 = BUILDER.comment("Chain jump distance (blocks) for Tier 3 (Default: 10.0)").translation("lightsabersfork.configuration.electric_judgement.chain_range_t3").defineInRange("chain_range_t3", 10.0, 1.0, 50.0);
		EJ_CHAIN_RANGE_T4 = BUILDER.comment("Chain jump distance (blocks) for Tier 4 (Default: 8.0)").translation("lightsabersfork.configuration.electric_judgement.chain_range_t4").defineInRange("chain_range_t4", 8.0, 1.0, 50.0);

		EJ_MAX_TARGETS_T1 = BUILDER.comment("Max targets for Tier 1 (1 primary)").translation("lightsabersfork.configuration.electric_judgement.max_targets_t1").defineInRange("max_targets_t1", 1, 1, 50);
		EJ_MAX_TARGETS_T2 = BUILDER.comment("Max targets for Tier 2 (1 primary + 7 chains = 8)").translation("lightsabersfork.configuration.electric_judgement.max_targets_t2").defineInRange("max_targets_t2", 8, 1, 50);
		EJ_MAX_TARGETS_T3 = BUILDER.comment("Max targets for Tier 3 (1 primary + 10 chains + sub-chains)").translation("lightsabersfork.configuration.electric_judgement.max_targets_t3").defineInRange("max_targets_t3", 21, 1, 100);
		EJ_MAX_TARGETS_T4 = BUILDER.comment("Max targets for Tier 4 (1 primary + 7 chains = 8)").translation("lightsabersfork.configuration.electric_judgement.max_targets_t4").defineInRange("max_targets_t4", 8, 1, 50);

		EJ_DAMAGE_UNDEAD_T1 = BUILDER.comment("Damage vs Undead per 0.5s for Tier 1 (Default: 8.0 = 16 DPS)").translation("lightsabersfork.configuration.electric_judgement.damage_undead_per_half_second_t1").defineInRange("damage_undead_per_half_second_t1", 8.0, 0.0, 500.0);
		EJ_DAMAGE_UNDEAD_T2 = BUILDER.comment("Damage vs Undead per 0.5s for Tier 2 (Default: 10.0 = 20 DPS)").translation("lightsabersfork.configuration.electric_judgement.damage_undead_per_half_second_t2").defineInRange("damage_undead_per_half_second_t2", 10.0, 0.0, 500.0);
		EJ_DAMAGE_UNDEAD_T3 = BUILDER.comment("Damage vs Undead per 0.5s for Tier 3 (Default: 20.0 = 40 DPS)").translation("lightsabersfork.configuration.electric_judgement.damage_undead_per_half_second_t3").defineInRange("damage_undead_per_half_second_t3", 20.0, 0.0, 500.0);
		EJ_DAMAGE_UNDEAD_T4 = BUILDER.comment("Damage vs Undead per 0.5s for Tier 4 (Default: 10.0 = 20 DPS)").translation("lightsabersfork.configuration.electric_judgement.damage_undead_per_half_second_t4").defineInRange("damage_undead_per_half_second_t4", 10.0, 0.0, 500.0);

		EJ_DAMAGE_SITH_T1 = BUILDER.comment("Damage vs Sith/Monsters per 0.5s for Tier 1 (Default: 4.0 = 8 DPS)").translation("lightsabersfork.configuration.electric_judgement.damage_sith_per_half_second_t1").defineInRange("damage_sith_per_half_second_t1", 4.0, 0.0, 500.0);
		EJ_DAMAGE_SITH_T2 = BUILDER.comment("Damage vs Sith/Monsters per 0.5s for Tier 2 (Default: 6.0 = 12 DPS)").translation("lightsabersfork.configuration.electric_judgement.damage_sith_per_half_second_t2").defineInRange("damage_sith_per_half_second_t2", 6.0, 0.0, 500.0);
		EJ_DAMAGE_SITH_T3 = BUILDER.comment("Damage vs Sith/Monsters per 0.5s for Tier 3 (Default: 6.0 = 12 DPS)").translation("lightsabersfork.configuration.electric_judgement.damage_sith_per_half_second_t3").defineInRange("damage_sith_per_half_second_t3", 6.0, 0.0, 500.0);
		EJ_DAMAGE_SITH_T4 = BUILDER.comment("Damage vs Sith/Monsters per 0.5s for Tier 4 (Default: 6.0 = 12 DPS)").translation("lightsabersfork.configuration.electric_judgement.damage_sith_per_half_second_t4").defineInRange("damage_sith_per_half_second_t4", 6.0, 0.0, 500.0);

		EJ_CONVERT_CHANCE_T1 = BUILDER.comment("Conversion chance per second for Tier 1 (Default: 0.0)").translation("lightsabersfork.configuration.electric_judgement.convert_chance_t1").defineInRange("convert_chance_t1", 0.0, 0.0, 1.0);
		EJ_CONVERT_CHANCE_T2 = BUILDER.comment("Conversion chance per second for Tier 2 (Default: 0.15 = 15%)").translation("lightsabersfork.configuration.electric_judgement.convert_chance_t2").defineInRange("convert_chance_t2", 0.15, 0.0, 1.0);
		EJ_CONVERT_CHANCE_T3 = BUILDER.comment("Conversion chance per second for Tier 3 (Default: 0.30 = 30%)").translation("lightsabersfork.configuration.electric_judgement.convert_chance_t3").defineInRange("convert_chance_t3", 0.30, 0.0, 1.0);
		EJ_CONVERT_CHANCE_T4 = BUILDER.comment("Conversion chance per second for Tier 4 (Default: 0.80 = 80%)").translation("lightsabersfork.configuration.electric_judgement.convert_chance_t4").defineInRange("convert_chance_t4", 0.80, 0.0, 1.0);

		EJ_CONVERT_WITCH = BUILDER.comment("Whether witches can be purified into villagers").translation("lightsabersfork.configuration.electric_judgement.convert_witch_to_villager").define("convert_witch_to_villager", true);
		EJ_CONVERT_SKELETON = BUILDER.comment("Whether skeletons can be purified into zombies (and then to villagers)").translation("lightsabersfork.configuration.electric_judgement.convert_skeleton_to_zombie").define("convert_skeleton_to_zombie", true);
		EJ_CONVERT_ZOMBIE = BUILDER.comment("Whether zombies can be purified into villagers").translation("lightsabersfork.configuration.electric_judgement.convert_zombie_to_villager").define("convert_zombie_to_villager", true);
		EJ_ENDERMAN_FAKEOUT = BUILDER.comment("When true, Endermen play conversion smoke/portal particles and reveal an Enderman fakeout; when false, converts to villager").translation("lightsabersfork.configuration.electric_judgement.enderman_fakeout").define("enderman_fakeout", true);

		EJ_SILENCE_TICKS_T3 = BUILDER.comment("Force Silence duration (ticks) inflicted on Sith players on Tier 3").translation("lightsabersfork.configuration.electric_judgement.silence_ticks_t3").defineInRange("silence_ticks_t3", 120, 0, 12000);
		EJ_SITH_COOLDOWN_INCREASE_T2 = BUILDER.comment("Cooldown increase multiplier on Sith abilities from Tier 2").translation("lightsabersfork.configuration.electric_judgement.sith_cooldown_increase_t2").defineInRange("sith_cooldown_increase_t2", 0.10, 0.0, 5.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE LIGHTNING (Sith)
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Lightning Settings").translation("lightsabersfork.configuration.force_lightning").push("force_lightning");
		LIGHTNING_HOLD_COST_T1 = BUILDER.comment("Holding cost (aF/s) for Tier 1 (Default: 30 aF/s = 1.5 aF/tick)").translation("lightsabersfork.configuration.force_lightning.hold_cost_per_second_t1").defineInRange("hold_cost_per_second_t1", 30.0, 0.0, 500.0);
		LIGHTNING_HOLD_COST_T2 = BUILDER.comment("Holding cost (aF/s) for Tier 2 (Default: 50 aF/s = 2.5 aF/tick)").translation("lightsabersfork.configuration.force_lightning.hold_cost_per_second_t2").defineInRange("hold_cost_per_second_t2", 50.0, 0.0, 500.0);
		LIGHTNING_HOLD_COST_T3 = BUILDER.comment("Holding cost (aF/s) for Tier 3 (Default: 80 aF/s = 4.0 aF/tick)").translation("lightsabersfork.configuration.force_lightning.hold_cost_per_second_t3").defineInRange("hold_cost_per_second_t3", 80.0, 0.0, 500.0);

		LIGHTNING_REACH_T1 = BUILDER.comment("Reach (blocks) for Tier 1 (Default: 16)").translation("lightsabersfork.configuration.force_lightning.reach_t1").defineInRange("reach_t1", 16.0, 1.0, 100.0);
		LIGHTNING_REACH_T2 = BUILDER.comment("Reach (blocks) for Tier 2 (Default: 24)").translation("lightsabersfork.configuration.force_lightning.reach_t2").defineInRange("reach_t2", 24.0, 1.0, 100.0);
		LIGHTNING_REACH_T3 = BUILDER.comment("Reach (blocks) for Tier 3 (Default: 30)").translation("lightsabersfork.configuration.force_lightning.reach_t3").defineInRange("reach_t3", 30.0, 1.0, 100.0);

		LIGHTNING_CHAIN_RANGE_T2 = BUILDER.comment("Chain jump distance (blocks) for Tier 2 (Default: 5.0)").translation("lightsabersfork.configuration.force_lightning.chain_range_t2").defineInRange("chain_range_t2", 5.0, 1.0, 50.0);
		LIGHTNING_CHAIN_RANGE_T3 = BUILDER.comment("Chain jump distance (blocks) for Tier 3 (Default: 7.0)").translation("lightsabersfork.configuration.force_lightning.chain_range_t3").defineInRange("chain_range_t3", 7.0, 1.0, 50.0);

		LIGHTNING_MAX_TARGETS_T1 = BUILDER.comment("Max targets for Tier 1 (1 primary)").translation("lightsabersfork.configuration.force_lightning.max_targets_t1").defineInRange("max_targets_t1", 1, 1, 50);
		LIGHTNING_MAX_TARGETS_T2 = BUILDER.comment("Max targets for Tier 2 (1 primary + 2 chains = 3)").translation("lightsabersfork.configuration.force_lightning.max_targets_t2").defineInRange("max_targets_t2", 3, 1, 50);
		LIGHTNING_MAX_TARGETS_T3 = BUILDER.comment("Max targets for Tier 3 (1 primary + 5 chains = 6)").translation("lightsabersfork.configuration.force_lightning.max_targets_t3").defineInRange("max_targets_t3", 6, 1, 50);

		LIGHTNING_DAMAGE_PRIMARY_T1 = BUILDER.comment("Primary target damage per 0.5s for Tier 1 (Default: 6.0 = 12 DPS)").translation("lightsabersfork.configuration.force_lightning.damage_primary_per_half_second_t1").defineInRange("damage_primary_per_half_second_t1", 6.0, 0.0, 500.0);
		LIGHTNING_DAMAGE_PRIMARY_T2 = BUILDER.comment("Primary target damage per 0.5s for Tier 2 (Default: 9.0 = 18 DPS)").translation("lightsabersfork.configuration.force_lightning.damage_primary_per_half_second_t2").defineInRange("damage_primary_per_half_second_t2", 9.0, 0.0, 500.0);
		LIGHTNING_DAMAGE_CHAIN_T2 = BUILDER.comment("Chain target damage per 0.5s for Tier 2 (Default: 6.0 = 12 DPS)").translation("lightsabersfork.configuration.force_lightning.damage_chain_per_half_second_t2").defineInRange("damage_chain_per_half_second_t2", 6.0, 0.0, 500.0);
		LIGHTNING_DAMAGE_PRIMARY_T3 = BUILDER.comment("Primary target damage per 0.5s for Tier 3 (Default: 12.0 = 24 DPS)").translation("lightsabersfork.configuration.force_lightning.damage_primary_per_half_second_t3").defineInRange("damage_primary_per_half_second_t3", 12.0, 0.0, 500.0);
		LIGHTNING_DAMAGE_CHAIN_T3 = BUILDER.comment("Chain target damage per 0.5s for Tier 3 (Default: 7.0 = 14 DPS)").translation("lightsabersfork.configuration.force_lightning.damage_chain_per_half_second_t3").defineInRange("damage_chain_per_half_second_t3", 7.0, 0.0, 500.0);
		LIGHTNING_ARMOR_PENETRATION_T3 = BUILDER.comment("Armor penetration ratio for Tier 3 (Default: 0.20 = 20%)").translation("lightsabersfork.configuration.force_lightning.armor_penetration_t3").defineInRange("armor_penetration_t3", 0.20, 0.0, 1.0);

		LIGHTNING_CORRUPT_CHANCE_T1 = BUILDER.comment("Villager corruption chance per second for Tier 1").translation("lightsabersfork.configuration.force_lightning.corrupt_chance_t1").defineInRange("corrupt_chance_t1", 0.15, 0.0, 1.0);
		LIGHTNING_CORRUPT_CHANCE_T2 = BUILDER.comment("Villager corruption chance per second for Tier 2").translation("lightsabersfork.configuration.force_lightning.corrupt_chance_t2").defineInRange("corrupt_chance_t2", 0.20, 0.0, 1.0);
		LIGHTNING_CORRUPT_CHANCE_T3 = BUILDER.comment("Villager corruption chance per second for Tier 3").translation("lightsabersfork.configuration.force_lightning.corrupt_chance_t3").defineInRange("corrupt_chance_t3", 0.30, 0.0, 1.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE CHOKE (Sith +80% default costs)
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Choke Settings").translation("lightsabersfork.configuration.force_choke").push("force_choke");
		CHOKE_HOLD_COST_T1 = BUILDER.comment("Holding cost (aF/s) for Tier 1 (Default: 7.2 aF/s = +80% base)").translation("lightsabersfork.configuration.force_choke.hold_cost_per_second_t1").defineInRange("hold_cost_per_second_t1", 7.2, 0.0, 500.0);
		CHOKE_HOLD_COST_T2 = BUILDER.comment("Holding cost (aF/s) for Tier 2 (Default: 11.7 aF/s = +80% base)").translation("lightsabersfork.configuration.force_choke.hold_cost_per_second_t2").defineInRange("hold_cost_per_second_t2", 11.7, 0.0, 500.0);
		CHOKE_HOLD_COST_T3 = BUILDER.comment("Holding cost (aF/s) for Tier 3 (Default: 18.0 aF/s = +80% base)").translation("lightsabersfork.configuration.force_choke.hold_cost_per_second_t3").defineInRange("hold_cost_per_second_t3", 18.0, 0.0, 500.0);

		CHOKE_REACH_T1 = BUILDER.comment("Reach (blocks) for Tier 1").translation("lightsabersfork.configuration.force_choke.reach_t1").defineInRange("reach_t1", 12.0, 1.0, 100.0);
		CHOKE_REACH_T2 = BUILDER.comment("Reach (blocks) for Tier 2").translation("lightsabersfork.configuration.force_choke.reach_t2").defineInRange("reach_t2", 18.0, 1.0, 100.0);
		CHOKE_REACH_T3 = BUILDER.comment("Reach (blocks) for Tier 3").translation("lightsabersfork.configuration.force_choke.reach_t3").defineInRange("reach_t3", 24.0, 1.0, 100.0);

		CHOKE_MAX_TARGETS_T1 = BUILDER.comment("Max targets for Tier 1").translation("lightsabersfork.configuration.force_choke.max_targets_t1").defineInRange("max_targets_t1", 1, 1, 50);
		CHOKE_MAX_TARGETS_T2 = BUILDER.comment("Max targets for Tier 2").translation("lightsabersfork.configuration.force_choke.max_targets_t2").defineInRange("max_targets_t2", 2, 1, 50);
		CHOKE_MAX_TARGETS_T3 = BUILDER.comment("Max targets for Tier 3").translation("lightsabersfork.configuration.force_choke.max_targets_t3").defineInRange("max_targets_t3", 4, 1, 50);

		CHOKE_DAMAGE_T1 = BUILDER.comment("Damage per 0.5s for Tier 1").translation("lightsabersfork.configuration.force_choke.damage_per_half_second_t1").defineInRange("damage_per_half_second_t1", 2.0, 0.0, 500.0);
		CHOKE_DAMAGE_T2 = BUILDER.comment("Damage per 0.5s for Tier 2").translation("lightsabersfork.configuration.force_choke.damage_per_half_second_t2").defineInRange("damage_per_half_second_t2", 3.5, 0.0, 500.0);
		CHOKE_DAMAGE_T3 = BUILDER.comment("Damage per 0.5s for Tier 3").translation("lightsabersfork.configuration.force_choke.damage_per_half_second_t3").defineInRange("damage_per_half_second_t3", 5.5, 0.0, 500.0);

		CHOKE_EXECUTE_THRESHOLD = BUILDER.comment("Health percentage threshold for execute on Tier 3 (Default: 0.25 = 25%)").translation("lightsabersfork.configuration.force_choke.execute_hp_threshold").defineInRange("execute_hp_threshold", 0.25, 0.01, 1.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE DRAIN (Sith +80% default costs)
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Drain Settings").translation("lightsabersfork.configuration.force_drain").push("force_drain");
		DRAIN_HOLD_COST_T1 = BUILDER.comment("Holding cost (aF/s) for Tier 1 (Default: 6.3 aF/s = +80% base)").translation("lightsabersfork.configuration.force_drain.hold_cost_per_second_t1").defineInRange("hold_cost_per_second_t1", 6.3, 0.0, 500.0);
		DRAIN_HOLD_COST_T2 = BUILDER.comment("Holding cost (aF/s) for Tier 2 (Default: 9.9 aF/s = +80% base)").translation("lightsabersfork.configuration.force_drain.hold_cost_per_second_t2").defineInRange("hold_cost_per_second_t2", 9.9, 0.0, 500.0);
		DRAIN_HOLD_COST_T3 = BUILDER.comment("Holding cost (aF/s) for Tier 3 (Default: 14.4 aF/s = +80% base)").translation("lightsabersfork.configuration.force_drain.hold_cost_per_second_t3").defineInRange("hold_cost_per_second_t3", 14.4, 0.0, 500.0);

		DRAIN_REACH_T1 = BUILDER.comment("Reach (blocks) for Tier 1").translation("lightsabersfork.configuration.force_drain.reach_t1").defineInRange("reach_t1", 10.0, 1.0, 100.0);
		DRAIN_REACH_T2 = BUILDER.comment("Reach (blocks) for Tier 2").translation("lightsabersfork.configuration.force_drain.reach_t2").defineInRange("reach_t2", 15.0, 1.0, 100.0);
		DRAIN_REACH_T3 = BUILDER.comment("Reach (blocks) for Tier 3").translation("lightsabersfork.configuration.force_drain.reach_t3").defineInRange("reach_t3", 22.0, 1.0, 100.0);

		DRAIN_MAX_TARGETS_T1 = BUILDER.comment("Max targets for Tier 1").translation("lightsabersfork.configuration.force_drain.max_targets_t1").defineInRange("max_targets_t1", 1, 1, 50);
		DRAIN_MAX_TARGETS_T2 = BUILDER.comment("Max targets for Tier 2").translation("lightsabersfork.configuration.force_drain.max_targets_t2").defineInRange("max_targets_t2", 3, 1, 50);
		DRAIN_MAX_TARGETS_T3 = BUILDER.comment("Max targets for Tier 3").translation("lightsabersfork.configuration.force_drain.max_targets_t3").defineInRange("max_targets_t3", 6, 1, 50);

		DRAIN_HP_T1 = BUILDER.comment("HP drained per 0.5s for Tier 1").translation("lightsabersfork.configuration.force_drain.drain_hp_per_half_second_t1").defineInRange("drain_hp_per_half_second_t1", 2.0, 0.0, 500.0);
		DRAIN_HP_T2 = BUILDER.comment("HP drained per 0.5s for Tier 2").translation("lightsabersfork.configuration.force_drain.drain_hp_per_half_second_t2").defineInRange("drain_hp_per_half_second_t2", 3.0, 0.0, 500.0);
		DRAIN_HP_T3 = BUILDER.comment("HP drained per 0.5s for Tier 3").translation("lightsabersfork.configuration.force_drain.drain_hp_per_half_second_t3").defineInRange("drain_hp_per_half_second_t3", 4.5, 0.0, 500.0);

		DRAIN_ENERGY_T1 = BUILDER.comment("Energy siphoned per 0.5s for Tier 1").translation("lightsabersfork.configuration.force_drain.drain_energy_t1").defineInRange("drain_energy_t1", 3.0, 0.0, 500.0);
		DRAIN_ENERGY_T2 = BUILDER.comment("Energy siphoned per 0.5s for Tier 2").translation("lightsabersfork.configuration.force_drain.drain_energy_t2").defineInRange("drain_energy_t2", 6.0, 0.0, 500.0);
		DRAIN_ENERGY_T3 = BUILDER.comment("Energy siphoned per 0.5s for Tier 3").translation("lightsabersfork.configuration.force_drain.drain_energy_t3").defineInRange("drain_energy_t3", 10.0, 0.0, 500.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE PUSH
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Push Settings").translation("lightsabersfork.configuration.force_push").push("force_push");
		PUSH_COST_T1 = BUILDER.comment("Cost (aF) for Tier 1").translation("lightsabersfork.configuration.force_push.cost_t1").defineInRange("cost_t1", 12.0, 0.0, 1000.0);
		PUSH_COST_T2 = BUILDER.comment("Cost (aF) for Tier 2").translation("lightsabersfork.configuration.force_push.cost_t2").defineInRange("cost_t2", 18.0, 0.0, 1000.0);
		PUSH_COST_T3 = BUILDER.comment("Cost (aF) for Tier 3").translation("lightsabersfork.configuration.force_push.cost_t3").defineInRange("cost_t3", 28.0, 0.0, 1000.0);
		PUSH_COST_T4 = BUILDER.comment("Cost (aF) for Tier 4").translation("lightsabersfork.configuration.force_push.cost_t4").defineInRange("cost_t4", 40.0, 0.0, 1000.0);

		PUSH_REACH_T1 = BUILDER.comment("Reach (blocks) for Tier 1").translation("lightsabersfork.configuration.force_push.reach_t1").defineInRange("reach_t1", 6.0, 1.0, 100.0);
		PUSH_REACH_T2 = BUILDER.comment("Reach (blocks) for Tier 2").translation("lightsabersfork.configuration.force_push.reach_t2").defineInRange("reach_t2", 10.0, 1.0, 100.0);
		PUSH_REACH_T3 = BUILDER.comment("Reach (blocks) for Tier 3").translation("lightsabersfork.configuration.force_push.reach_t3").defineInRange("reach_t3", 16.0, 1.0, 100.0);
		PUSH_REACH_T4 = BUILDER.comment("Reach (blocks) for Tier 4").translation("lightsabersfork.configuration.force_push.reach_t4").defineInRange("reach_t4", 24.0, 1.0, 100.0);

		PUSH_STRENGTH_T1 = BUILDER.comment("Push strength for Tier 1").translation("lightsabersfork.configuration.force_push.strength_t1").defineInRange("strength_t1", 1.4, 0.1, 50.0);
		PUSH_STRENGTH_T2 = BUILDER.comment("Push strength for Tier 2").translation("lightsabersfork.configuration.force_push.strength_t2").defineInRange("strength_t2", 2.2, 0.1, 50.0);
		PUSH_STRENGTH_T3 = BUILDER.comment("Push strength for Tier 3").translation("lightsabersfork.configuration.force_push.strength_t3").defineInRange("strength_t3", 3.2, 0.1, 50.0);
		PUSH_STRENGTH_T4 = BUILDER.comment("Push strength for Tier 4").translation("lightsabersfork.configuration.force_push.strength_t4").defineInRange("strength_t4", 4.5, 0.1, 50.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE PULL
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Pull Settings").translation("lightsabersfork.configuration.force_pull").push("force_pull");
		PULL_COST_T1 = BUILDER.comment("Cost (aF) for Tier 1").translation("lightsabersfork.configuration.force_pull.cost_t1").defineInRange("cost_t1", 12.0, 0.0, 1000.0);
		PULL_COST_T2 = BUILDER.comment("Cost (aF) for Tier 2").translation("lightsabersfork.configuration.force_pull.cost_t2").defineInRange("cost_t2", 18.0, 0.0, 1000.0);
		PULL_COST_T3 = BUILDER.comment("Cost (aF) for Tier 3").translation("lightsabersfork.configuration.force_pull.cost_t3").defineInRange("cost_t3", 25.0, 0.0, 1000.0);

		PULL_REACH_T1 = BUILDER.comment("Reach / Radius (blocks) for Tier 1").translation("lightsabersfork.configuration.force_pull.reach_t1").defineInRange("reach_t1", 8.0, 1.0, 100.0);
		PULL_REACH_T2 = BUILDER.comment("Reach / Radius (blocks) for Tier 2").translation("lightsabersfork.configuration.force_pull.reach_t2").defineInRange("reach_t2", 14.0, 1.0, 100.0);
		PULL_REACH_T3 = BUILDER.comment("Reach / Radius (blocks) for Tier 3").translation("lightsabersfork.configuration.force_pull.reach_t3").defineInRange("reach_t3", 20.0, 1.0, 100.0);
		PULL_RADIUS_T1 = PULL_REACH_T1;
		PULL_RADIUS_T2 = PULL_REACH_T2;
		PULL_RADIUS_T3 = PULL_REACH_T3;

		PULL_MAX_TARGETS_T1 = BUILDER.comment("Max targets pulled for Tier 1").translation("lightsabersfork.configuration.force_pull.max_targets_t1").defineInRange("max_targets_t1", 3, 1, 100);
		PULL_MAX_TARGETS_T2 = BUILDER.comment("Max targets pulled for Tier 2").translation("lightsabersfork.configuration.force_pull.max_targets_t2").defineInRange("max_targets_t2", 6, 1, 100);
		PULL_MAX_TARGETS_T3 = BUILDER.comment("Max targets pulled for Tier 3").translation("lightsabersfork.configuration.force_pull.max_targets_t3").defineInRange("max_targets_t3", 12, 1, 100);

		PULL_STRENGTH_T1 = BUILDER.comment("Pull strength for Tier 1").translation("lightsabersfork.configuration.force_pull.strength_t1").defineInRange("strength_t1", 1.2, 0.1, 50.0);
		PULL_STRENGTH_T2 = BUILDER.comment("Pull strength for Tier 2").translation("lightsabersfork.configuration.force_pull.strength_t2").defineInRange("strength_t2", 2.0, 0.1, 50.0);
		PULL_STRENGTH_T3 = BUILDER.comment("Pull strength for Tier 3").translation("lightsabersfork.configuration.force_pull.strength_t3").defineInRange("strength_t3", 3.0, 0.1, 50.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE DASH
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Dash Settings").translation("lightsabersfork.configuration.force_dash").push("force_dash");
		DASH_COST_T1 = BUILDER.comment("Cost (aF) for Tier 1").translation("lightsabersfork.configuration.force_dash.cost_t1").defineInRange("cost_t1", 10.0, 0.0, 1000.0);
		DASH_COST_T2 = BUILDER.comment("Cost (aF) for Tier 2").translation("lightsabersfork.configuration.force_dash.cost_t2").defineInRange("cost_t2", 15.0, 0.0, 1000.0);
		DASH_COST_T3 = BUILDER.comment("Cost (aF) for Tier 3").translation("lightsabersfork.configuration.force_dash.cost_t3").defineInRange("cost_t3", 20.0, 0.0, 1000.0);

		DASH_SPEED_T1 = BUILDER.comment("Dash speed for Tier 1").translation("lightsabersfork.configuration.force_dash.speed_t1").defineInRange("speed_t1", 2.0, 0.1, 20.0);
		DASH_SPEED_T2 = BUILDER.comment("Dash speed for Tier 2").translation("lightsabersfork.configuration.force_dash.speed_t2").defineInRange("speed_t2", 2.8, 0.1, 20.0);
		DASH_SPEED_T3 = BUILDER.comment("Dash speed for Tier 3").translation("lightsabersfork.configuration.force_dash.speed_t3").defineInRange("speed_t3", 3.4, 0.1, 20.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE JUMP
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Jump Settings").translation("lightsabersfork.configuration.force_jump").push("force_jump");
		JUMP_COST_T1 = BUILDER.comment("Cost (aF) for Tier 1").translation("lightsabersfork.configuration.force_jump.cost_t1").defineInRange("cost_t1", 12.0, 0.0, 1000.0);
		JUMP_COST_T2 = BUILDER.comment("Cost (aF) for Tier 2").translation("lightsabersfork.configuration.force_jump.cost_t2").defineInRange("cost_t2", 18.0, 0.0, 1000.0);
		JUMP_COST_T3 = BUILDER.comment("Cost (aF) for Tier 3").translation("lightsabersfork.configuration.force_jump.cost_t3").defineInRange("cost_t3", 24.0, 0.0, 1000.0);

		JUMP_STRENGTH_T1 = BUILDER.comment("Jump upward velocity for Tier 1").translation("lightsabersfork.configuration.force_jump.strength_t1").defineInRange("strength_t1", 1.4, 0.1, 20.0);
		JUMP_STRENGTH_T2 = BUILDER.comment("Jump upward velocity for Tier 2").translation("lightsabersfork.configuration.force_jump.strength_t2").defineInRange("strength_t2", 2.0, 0.1, 20.0);
		JUMP_STRENGTH_T3 = BUILDER.comment("Jump upward velocity for Tier 3").translation("lightsabersfork.configuration.force_jump.strength_t3").defineInRange("strength_t3", 2.8, 0.1, 20.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE REPULSE
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Repulse Settings").translation("lightsabersfork.configuration.force_repulse").push("force_repulse");
		REPULSE_COST_T1 = BUILDER.comment("Cost (aF) for Tier 1").translation("lightsabersfork.configuration.force_repulse.cost_t1").defineInRange("cost_t1", 15.0, 0.0, 1000.0);
		REPULSE_COST_T2 = BUILDER.comment("Cost (aF) for Tier 2").translation("lightsabersfork.configuration.force_repulse.cost_t2").defineInRange("cost_t2", 25.0, 0.0, 1000.0);
		REPULSE_COST_T3 = BUILDER.comment("Cost (aF) for Tier 3").translation("lightsabersfork.configuration.force_repulse.cost_t3").defineInRange("cost_t3", 35.0, 0.0, 1000.0);

		REPULSE_RADIUS_T1 = BUILDER.comment("Radius (blocks) for Tier 1").translation("lightsabersfork.configuration.force_repulse.radius_t1").defineInRange("radius_t1", 10.0, 1.0, 100.0);
		REPULSE_RADIUS_T2 = BUILDER.comment("Radius (blocks) for Tier 2").translation("lightsabersfork.configuration.force_repulse.radius_t2").defineInRange("radius_t2", 15.0, 1.0, 100.0);
		REPULSE_RADIUS_T3 = BUILDER.comment("Radius (blocks) for Tier 3").translation("lightsabersfork.configuration.force_repulse.radius_t3").defineInRange("radius_t3", 30.0, 1.0, 100.0);

		REPULSE_DAMAGE_T1 = BUILDER.comment("Damage for Tier 1").translation("lightsabersfork.configuration.force_repulse.damage_t1").defineInRange("damage_t1", 6.0, 0.0, 500.0);
		REPULSE_DAMAGE_T2 = BUILDER.comment("Damage for Tier 2").translation("lightsabersfork.configuration.force_repulse.damage_t2").defineInRange("damage_t2", 8.0, 0.0, 500.0);
		REPULSE_DAMAGE_T3 = BUILDER.comment("Damage for Tier 3").translation("lightsabersfork.configuration.force_repulse.damage_t3").defineInRange("damage_t3", 10.0, 0.0, 500.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE STASIS
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Stasis Settings").translation("lightsabersfork.configuration.force_stasis").push("force_stasis");
		STASIS_COST_T1 = BUILDER.comment("Cost (aF) for Tier 1").translation("lightsabersfork.configuration.force_stasis.cost_t1").defineInRange("cost_t1", 12.0, 0.0, 1000.0);
		STASIS_COST_T2 = BUILDER.comment("Cost (aF) for Tier 2").translation("lightsabersfork.configuration.force_stasis.cost_t2").defineInRange("cost_t2", 28.0, 0.0, 1000.0);
		STASIS_COST_T3 = BUILDER.comment("Cost (aF) for Tier 3").translation("lightsabersfork.configuration.force_stasis.cost_t3").defineInRange("cost_t3", 35.0, 0.0, 1000.0);

		STASIS_REACH_T1 = BUILDER.comment("Reach (blocks) for Tier 1").translation("lightsabersfork.configuration.force_stasis.reach_t1").defineInRange("reach_t1", 7.0, 1.0, 100.0);
		STASIS_REACH_T2 = BUILDER.comment("Reach (blocks) for Tier 2").translation("lightsabersfork.configuration.force_stasis.reach_t2").defineInRange("reach_t2", 10.0, 1.0, 100.0);
		STASIS_REACH_T3 = BUILDER.comment("Reach (blocks) for Tier 3").translation("lightsabersfork.configuration.force_stasis.reach_t3").defineInRange("reach_t3", 16.0, 1.0, 100.0);

		STASIS_DURATION_TICKS_T1 = BUILDER.comment("Duration (ticks) for Tier 1 (140 ticks = 7s)").translation("lightsabersfork.configuration.force_stasis.duration_ticks_t1").defineInRange("duration_ticks_t1", 140, 10, 12000);
		STASIS_DURATION_TICKS_T2 = BUILDER.comment("Duration (ticks) for Tier 2 (140 ticks = 7s)").translation("lightsabersfork.configuration.force_stasis.duration_ticks_t2").defineInRange("duration_ticks_t2", 140, 10, 12000);
		STASIS_DURATION_TICKS_T3 = BUILDER.comment("Duration (ticks) for Tier 3 (240 ticks = 12s)").translation("lightsabersfork.configuration.force_stasis.duration_ticks_t3").defineInRange("duration_ticks_t3", 240, 10, 12000);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE SLAM
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Slam Settings").translation("lightsabersfork.configuration.force_slam").push("force_slam");
		SLAM_COST_T1 = BUILDER.comment("Cost (aF) for Tier 1").translation("lightsabersfork.configuration.force_slam.cost_t1").defineInRange("cost_t1", 20.0, 0.0, 1000.0);
		SLAM_COST_T2 = BUILDER.comment("Cost (aF) for Tier 2").translation("lightsabersfork.configuration.force_slam.cost_t2").defineInRange("cost_t2", 30.0, 0.0, 1000.0);
		SLAM_COST_T3 = BUILDER.comment("Cost (aF) for Tier 3").translation("lightsabersfork.configuration.force_slam.cost_t3").defineInRange("cost_t3", 45.0, 0.0, 1000.0);

		SLAM_RADIUS_T1 = BUILDER.comment("Radius (blocks) for Tier 1").translation("lightsabersfork.configuration.force_slam.radius_t1").defineInRange("radius_t1", 10.0, 1.0, 100.0);
		SLAM_RADIUS_T2 = BUILDER.comment("Radius (blocks) for Tier 2").translation("lightsabersfork.configuration.force_slam.radius_t2").defineInRange("radius_t2", 15.0, 1.0, 100.0);
		SLAM_RADIUS_T3 = BUILDER.comment("Radius (blocks) for Tier 3").translation("lightsabersfork.configuration.force_slam.radius_t3").defineInRange("radius_t3", 15.0, 1.0, 100.0);

		SLAM_DAMAGE_T1 = BUILDER.comment("Damage for Tier 1").translation("lightsabersfork.configuration.force_slam.damage_t1").defineInRange("damage_t1", 8.0, 0.0, 500.0);
		SLAM_DAMAGE_T2 = BUILDER.comment("Damage for Tier 2").translation("lightsabersfork.configuration.force_slam.damage_t2").defineInRange("damage_t2", 12.0, 0.0, 500.0);
		SLAM_DAMAGE_T3 = BUILDER.comment("Damage for Tier 3").translation("lightsabersfork.configuration.force_slam.damage_t3").defineInRange("damage_t3", 16.0, 0.0, 500.0);
		BUILDER.pop();

		// ---------------------------------------------------------------------
		// FORCE TELEKINESIS
		// ---------------------------------------------------------------------
		BUILDER.comment("Force Telekinesis Settings").translation("lightsabersfork.configuration.force_telekinesis").push("force_telekinesis");
		TELEKINESIS_HOLD_COST_T1 = BUILDER.comment("Holding cost (aF/s) for Tier 1").translation("lightsabersfork.configuration.force_telekinesis.hold_cost_per_second_t1").defineInRange("hold_cost_per_second_t1", 0.5, 0.0, 100.0);
		TELEKINESIS_HOLD_COST_T2 = BUILDER.comment("Holding cost (aF/s) for Tier 2").translation("lightsabersfork.configuration.force_telekinesis.hold_cost_per_second_t2").defineInRange("hold_cost_per_second_t2", 0.75, 0.0, 100.0);
		TELEKINESIS_HOLD_COST_T3 = BUILDER.comment("Holding cost (aF/s) for Tier 3 (Default: 0.0 = free at Tier 3)").translation("lightsabersfork.configuration.force_telekinesis.hold_cost_per_second_t3").defineInRange("hold_cost_per_second_t3", 0.0, 0.0, 100.0);

		TELEKINESIS_REACH_T1 = BUILDER.comment("Reach (blocks) for Tier 1").translation("lightsabersfork.configuration.force_telekinesis.reach_t1").defineInRange("reach_t1", 8.0, 1.0, 100.0);
		TELEKINESIS_REACH_T2 = BUILDER.comment("Reach (blocks) for Tier 2").translation("lightsabersfork.configuration.force_telekinesis.reach_t2").defineInRange("reach_t2", 16.0, 1.0, 100.0);
		TELEKINESIS_REACH_T3 = BUILDER.comment("Reach (blocks) for Tier 3").translation("lightsabersfork.configuration.force_telekinesis.reach_t3").defineInRange("reach_t3", 30.0, 1.0, 100.0);

		TELEKINESIS_THROW_FORCE_T1 = BUILDER.comment("Throw velocity for Tier 1").translation("lightsabersfork.configuration.force_telekinesis.throw_force_t1").defineInRange("throw_force_t1", 1.8, 0.1, 20.0);
		TELEKINESIS_THROW_FORCE_T2 = BUILDER.comment("Throw velocity for Tier 2").translation("lightsabersfork.configuration.force_telekinesis.throw_force_t2").defineInRange("throw_force_t2", 2.6, 0.1, 20.0);
		TELEKINESIS_THROW_FORCE_T3 = BUILDER.comment("Throw velocity for Tier 3").translation("lightsabersfork.configuration.force_telekinesis.throw_force_t3").defineInRange("throw_force_t3", 3.5, 0.1, 20.0);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}
}
