package com.fiskmods.pika.lightsabers.command;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.events.LightsaberCombatEvents;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID)
public class ALDebugCommand {

	private record HiltParts(Supplier<Item> emitter, Supplier<Item> switchSection, Supplier<Item> grip,
			Supplier<Item> pommel) {
	}

	private static final Map<String, HiltParts> HILT_SETS = new LinkedHashMap<>();

	static {
		registerHilt("graflex", ALItems.GRAFLEX_EMITTER, ALItems.GRAFLEX_SWITCH_SECTION, ALItems.GRAFLEX_GRIP,
				ALItems.GRAFLEX_POMMEL);
		registerHilt("redeemer", ALItems.REDEEMER_EMITTER, ALItems.REDEEMER_SWITCH_SECTION, ALItems.REDEEMER_GRIP,
				ALItems.REDEEMER_POMMEL);
		registerHilt("mauler", ALItems.MAULER_EMITTER, ALItems.MAULER_SWITCH_SECTION, ALItems.MAULER_GRIP,
				ALItems.MAULER_POMMEL);
		registerHilt("prodigal_son", ALItems.PRODIGAL_SON_EMITTER, ALItems.PRODIGAL_SON_SWITCH_SECTION,
				ALItems.PRODIGAL_SON_GRIP, ALItems.PRODIGAL_SON_POMMEL);
		registerHilt("knighted", ALItems.KNIGHTED_EMITTER, ALItems.KNIGHTED_SWITCH_SECTION, ALItems.KNIGHTED_GRIP,
				ALItems.KNIGHTED_POMMEL);
		registerHilt("droideka", ALItems.DROIDEKA_EMITTER, ALItems.DROIDEKA_SWITCH_SECTION, ALItems.DROIDEKA_GRIP,
				ALItems.DROIDEKA_POMMEL);
		registerHilt("fulcrum", ALItems.FULCRUM_EMITTER, ALItems.FULCRUM_SWITCH_SECTION, ALItems.FULCRUM_GRIP,
				ALItems.FULCRUM_POMMEL);
		registerHilt("juggernaut", ALItems.JUGGERNAUT_EMITTER, ALItems.JUGGERNAUT_SWITCH_SECTION,
				ALItems.JUGGERNAUT_GRIP, ALItems.JUGGERNAUT_POMMEL);
		registerHilt("mechanical", ALItems.MECHANICAL_EMITTER, ALItems.MECHANICAL_SWITCH_SECTION,
				ALItems.MECHANICAL_GRIP, ALItems.MECHANICAL_POMMEL);
		registerHilt("mandalorian", ALItems.MANDALORIAN_EMITTER, ALItems.MANDALORIAN_SWITCH_SECTION,
				ALItems.MANDALORIAN_GRIP, ALItems.MANDALORIAN_POMMEL);
		registerHilt("fury", ALItems.FURY_EMITTER, ALItems.FURY_SWITCH_SECTION, ALItems.FURY_GRIP, ALItems.FURY_POMMEL);
		registerHilt("rebel", ALItems.REBEL_EMITTER, ALItems.REBEL_SWITCH_SECTION, ALItems.REBEL_GRIP,
				ALItems.REBEL_POMMEL);
		registerHilt("imperial", ALItems.IMPERIAL_EMITTER, ALItems.IMPERIAL_SWITCH_SECTION, ALItems.IMPERIAL_GRIP,
				ALItems.IMPERIAL_POMMEL);
		registerHilt("reborn", ALItems.REBORN_EMITTER, ALItems.REBORN_SWITCH_SECTION, ALItems.REBORN_GRIP,
				ALItems.REBORN_POMMEL);
		registerHilt("vaid_modern", ALItems.VAID_MODERN_EMITTER, ALItems.VAID_MODERN_SWITCH_SECTION,
				ALItems.VAID_MODERN_GRIP, ALItems.VAID_MODERN_POMMEL);
		registerHilt("vaid_ancient", ALItems.VAID_ANCIENT_EMITTER, ALItems.VAID_ANCIENT_SWITCH_SECTION,
				ALItems.VAID_ANCIENT_GRIP, ALItems.VAID_ANCIENT_POMMEL);
	}

	private static void registerHilt(String name, Supplier<Item> emitter, Supplier<Item> switchSection,
			Supplier<Item> grip, Supplier<Item> pommel) {
		HILT_SETS.put(name.toLowerCase(Locale.ROOT), new HiltParts(emitter, switchSection, grip, pommel));
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		var rootNode = dispatcher.register(Commands.literal("alpikadebug").requires(source -> source.hasPermission(2))

				.then(Commands.literal("saber").then(Commands.literal("give")
						.then(Commands.literal("all").executes(ALDebugCommand::executeSaberGiveAll))
						.then(Commands.argument("set", StringArgumentType.word())
								.suggests(
										(ctx, builder) -> SharedSuggestionProvider.suggest(HILT_SETS.keySet(), builder))
								.executes(
										ctx -> executeSaberGive(ctx, StringArgumentType.getString(ctx, "set"), false))))
						.then(Commands.literal("double")
								.then(Commands.literal("all").executes(ALDebugCommand::executeSaberDoubleAll))
								.then(Commands.argument("set", StringArgumentType.word()).suggests(
										(ctx, builder) -> SharedSuggestionProvider.suggest(HILT_SETS.keySet(), builder))
										.executes(ctx -> executeSaberGive(ctx, StringArgumentType.getString(ctx, "set"),
												true)))))

				.then(Commands.literal("force").then(Commands.literal("addXp")
						.then(Commands.argument("amount", FloatArgumentType.floatArg(0)).executes(
								ctx -> executeForceData(ctx, "addForceXp", FloatArgumentType.getFloat(ctx, "amount")))))
						.then(Commands.literal("addPoints")
								.then(Commands.argument("amount", IntegerArgumentType.integer(1))
										.executes(ctx -> executeForceData(ctx, "addForcePoints",
												IntegerArgumentType.getInteger(ctx, "amount")))))

						.then(Commands.literal("regen")
								.then(Commands.argument("multiplier", FloatArgumentType.floatArg(0))
										.executes(ctx -> executeForceData(ctx, "setRegen",
												FloatArgumentType.getFloat(ctx, "multiplier"))))
								.executes(ctx -> executeForceData(ctx, "toggleRegen", 0)))

						.then(Commands.literal("fill").executes(ctx -> executeForceData(ctx, "fill", 0)))
						.then(Commands.literal("reset").executes(ctx -> executeForceData(ctx, "reset", 0)))
						.then(Commands.literal("stats").executes(ctx -> executeForceData(ctx, "stats", 0)))
						.then(Commands.literal("unlockAllTiers").executes(ALDebugCommand::executeUnlockAllTiers))
						.then(Commands.literal("setTier").then(Commands.argument("power", StringArgumentType.word())
								.suggests((ctx,
										builder) -> SharedSuggestionProvider.suggest(PlayerForceData.ALL_ACTIVE_POWERS
												.stream().map(ResourceLocation::getPath), builder))
								.then(Commands.argument("tier", IntegerArgumentType.integer(1, 3))
										.executes(ctx -> executeSetTier(ctx, StringArgumentType.getString(ctx, "power"),
												IntegerArgumentType.getInteger(ctx, "tier")))))))

				.then(Commands.literal("abilities")
						.then(Commands.literal("addPoints")
								.then(Commands.argument("amount", IntegerArgumentType.integer(1))
										.executes(ctx -> executeForceData(ctx, "addAbilityPoints",
												IntegerArgumentType.getInteger(ctx, "amount")))))
						.then(Commands.literal("addKills")
								.then(Commands.argument("amount", IntegerArgumentType.integer(1))
										.executes(ctx -> executeForceData(ctx, "addKills",
												IntegerArgumentType.getInteger(ctx, "amount")))))
						.then(Commands.literal("unlockAll").executes(ctx -> executeForceData(ctx, "unlockAll", 0)))));

		dispatcher.register(Commands.literal("al").requires(source -> source.hasPermission(2)).redirect(rootNode));
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide())
			return;

		float mult = player.getPersistentData().getFloat("DebugForceRegen");
		if (mult > 0.0F && player instanceof ServerPlayer serverPlayer) {
			PlayerForceData data = PlayerForceData.get(serverPlayer);
			if (data != null && data.isForceSensitive()) {
				if (data.getForceEnergy() < data.getMaxForceEnergy()) {

					data.addForceEnergy(mult * 0.5F);
					if (player.tickCount % 4 == 0) {
						data.sync(serverPlayer);
					}
				}
			}
		}
	}

	private static int executeUnlockAllTiers(CommandContext<CommandSourceStack> context) {
		try {
			ServerPlayer player = context.getSource().getPlayerOrException();
			PlayerForceData data = PlayerForceData.get(player);
			if (data == null)
				return 0;

			data.maxAllPowers();
			data.sync(player);

			context.getSource().sendSuccess(() -> Component.literal(
					"§a[Force] §7All 11 Force powers learned and upgraded to §6Tier 3 (Mastery)§7!"),
					true);
			return 1;
		} catch (Exception e) {
			context.getSource().sendFailure(Component.literal("§c[Error] " + e.getMessage()));
			return 0;
		}
	}

	private static int executeSetTier(CommandContext<CommandSourceStack> context, String powerName, int tier) {
		try {
			ServerPlayer player = context.getSource().getPlayerOrException();
			PlayerForceData data = PlayerForceData.get(player);
			if (data == null)
				return 0;

			ResourceLocation powerId = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, powerName);
			if (!PlayerForceData.ALL_ACTIVE_POWERS.contains(powerId)) {
				context.getSource()
						.sendFailure(Component.literal("§c[Error] Unknown power: '" + powerName + "'!"));
				return 0;
			}

			data.setForceSensitivity(true);
			data.unlockSkill(powerId);
			data.setPowerTier(powerId, tier);
			data.sync(player);

			context.getSource()
					.sendSuccess(() -> Component.literal(
							"§a[Force] §7Power §b" + powerName + "§7 set to §eTier " + tier + "§7!"),
							true);
			return 1;
		} catch (Exception e) {
			context.getSource().sendFailure(Component.literal("§c[Error] " + e.getMessage()));
			return 0;
		}
	}

	private static int executeForceData(CommandContext<CommandSourceStack> context, String action, float value) {
		try {
			ServerPlayer player = context.getSource().getPlayerOrException();
			PlayerForceData data = PlayerForceData.get(player);

			if (data == null) {
				context.getSource().sendFailure(Component.literal("§c[Error] PlayerForceData could not be loaded."));
				return 0;
			}

			switch (action) {
			case "addForceXp" -> {
				data.addForceXp(value);
				context.getSource().sendSuccess(() -> Component.literal("§b[Force] §7Added " + value + " XP. (Current: "
						+ data.getForceXp() + " XP, " + data.getForcePoints() + " Points)"), true);
			}
			case "addForcePoints" -> {
				float xpNeeded = value * 2000.0f;
				data.addForceXp(xpNeeded);
				context.getSource()
						.sendSuccess(() -> Component.literal("§b[Force] §7Added " + (int) value + " Points."), true);
			}
			case "setRegen" -> {
				player.getPersistentData().putFloat("DebugForceRegen", value);
				if (value > 0.0F) {
					context.getSource().sendSuccess(
							() -> Component
									.literal("§b[Force] §7Regeneration multiplier set to §e" + value + "x§7."),
							true);
				} else {
					context.getSource()
							.sendSuccess(() -> Component.literal("§b[Force] §7Turbo regeneration disabled."), true);
				}
				return 1;
			}
			case "toggleRegen" -> {
				float current = player.getPersistentData().getFloat("DebugForceRegen");
				float next = (current > 0.0F) ? 0.0F : 10.0F;
				player.getPersistentData().putFloat("DebugForceRegen", next);
				if (next > 0.0F) {
					context.getSource().sendSuccess(
							() -> Component.literal("§b[Force] §7Turbo regeneration §aenabled §7(" + next + "x)."),
							true);
				} else {
					context.getSource().sendSuccess(
							() -> Component.literal("§b[Force] §7Turbo regeneration §cdisabled §7(Normal)."), true);
				}
				return 1;
			}
			case "fill" -> {
				data.addForceEnergy(data.getMaxForceEnergy());
				data.sync(player);
				context.getSource()
						.sendSuccess(() -> Component.literal("§b[Force] §7Force energy fully restored ("
								+ (int) data.getForceEnergy() + " / " + (int) data.getMaxForceEnergy() + " FP)."),
								true);
				return 1;
			}
			case "addAbilityPoints" -> {
				int currentPoints = data.getAbilityPoints();
				data.consumeAbilityPoints(currentPoints);
				data.addAbilityPoints(currentPoints + (int) value);
				context.getSource().sendSuccess(
						() -> Component.literal("§c[Combat] §7Added " + (int) value + " Ability Points."), true);
			}
			case "addKills" -> {
				int kills = (int) value;
				for (int i = 0; i < kills; i++) {
					data.addLightsaberKill();
				}
				context.getSource().sendSuccess(
						() -> Component.literal("§c[Combat] §7Simulated " + kills + " Lightsaber Kills."), true);
			}
			case "reset" -> {
				data.resetAll();
				player.getPersistentData().putFloat("DebugForceRegen", 0.0F);
				context.getSource().sendSuccess(
						() -> Component.literal("§e[Skilltree] §7All points, XP, kills and debug regen reset."), true);
			}
			case "stats" -> {
				float debugRegen = player.getPersistentData().getFloat("DebugForceRegen");
				long gameTime = player.level().getGameTime();

				context.getSource().sendSuccess(() -> Component.literal("§6========== [ Advanced Lightsabers - Alle Stats ] =========="), false);

				// 1. Core Force & Ränge
				context.getSource().sendSuccess(() -> Component.literal("§e--- Macht-Grundwerte & Ränge ---"), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§bMachtsensitiv: §7" + (data.isForceSensitive() ? "§aJa" : "§cNein")
						+ " §8| §bChosen One: §7" + (data.hasChosenOne() ? "§aJa" : "§7Nein")), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§bMachtenergie: §7" + (int) data.getForceEnergy() + " / " + (int) data.getMaxForceEnergy() + " aF"
						+ (debugRegen > 0 ? " §e[Turbo " + debugRegen + "x]" : "")), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§bForce Points (FP): §7" + data.getForcePoints()
						+ " §8| §bForce XP: §7" + String.format(Locale.ROOT, "%.1f / 2000.0 (x%.2f XP-Mult)", data.getForceXp(), data.getMeditationXpMultiplier())), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§bMacht-Ränge: §7Neutral: " + data.getForceRank()
						+ " §8| §9Jedi: " + data.getJediRank() + " (" + data.getJediSpecialization() + ")"
						+ " §8| §cSith: " + data.getSithRank() + " (" + data.getSithSpecialization() + ")"), false);

				// 2. Meditation
				int medTicks = data.getMeditationTicks();
				int medSec = medTicks / 20;
				int medMin = medSec / 60;
				int medSecRem = medSec % 60;
				int nextBonusTicks = 1800 - (medTicks % 1800);
				context.getSource().sendSuccess(() -> Component.literal("§e--- Meditation & Kapazität ---"), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§bMeditations-Zeit: §7" + String.format("%02d:%02d", medMin, medSecRem) + " / 45:00 min (" + medTicks + " Ticks)"), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§bMeditations-Bonus: §e+" + data.getMeditationBonusEnergy() + " / 150 aF"
						+ (data.getMeditationBonusEnergy() < 150 ? " §8(Nächste +5 aF in " + (nextBonusTicks / 20) + "s)" : " §a[MAX]")), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§bMeditations-FP: §7" + data.getMeditationFpProgressTicks() + " / " + data.getRequiredMeditationTicksForNextFp()
						+ " Ticks §8(Bereits verdient: " + data.getMeditationFpEarned() + " FP)"), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§bSaber-Meditation (Kata AP): §7" + data.getSaberMeditationTicks() + " / 600 Ticks"
						+ " §8(Verdient: " + data.getSaberMeditationPointsEarned() + " / " + PlayerForceData.MAX_SABER_MEDITATION_POINTS + " AP)"), false);

				// 3. Kampf & Lichtschwert
				context.getSource().sendSuccess(() -> Component.literal("§e--- Kampf & Lichtschwert ---"), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§cAbility Points (AP): §7" + data.getAbilityPoints()), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§cLs Kills: §7" + data.getLightsaberKills() + " / " + data.getNextKillThreshold()
						+ " §8| §cBlocks: §7" + data.getLightsaberBlocks() + " / " + data.getNextBlockThreshold()), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§cTraining Progress: §7" + data.getSaberTrainingProgress() + " Ticks"
						+ " §8(Verdient: " + data.getSaberTrainingPointsEarned() + " / " + PlayerForceData.MAX_TRAINING_POINTS + " AP)"), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§cMeilensteine: §7Erstes LS: " + (data.hasCraftedFirstLightsaber() ? "§a✔" : "§c✘")
						+ " §8| §7Doppelklinge: " + (data.hasCraftedFirstDoubleSaber() ? "§a✔" : "§c✘")
						+ " §8| §7Fokussiert: " + (data.hasCraftedFirstFocusedSaber() ? "§a✔" : "§c✘")), false);

				// 4. Mächte & Telekinese
				context.getSource().sendSuccess(() -> Component.literal("§e--- Mächte, Status & Telekinese ---"), false);
				ResourceLocation sel = data.getSelectedPower();
				context.getSource().sendSuccess(() -> Component.literal(
						"§dAusgewählt: §7" + (sel != null ? sel.getPath() + " (Lvl " + data.getCurrentSelectedPowerLevel() + ")" : "Keine")
						+ " §8| §dGesamt-Machteinsätze: §7" + data.getTotalPowerUses() + " / " + data.getNextPowerUseThreshold()
						+ " §8(Schritt: " + data.getPowerUseStep() + ")"), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§dTK Entity ID: §7" + (data.getTelekineticEntityId() == -1 ? "Keines" : data.getTelekineticEntityId())
						+ " §8| §dTK Distanz: §7" + String.format(Locale.ROOT, "%.1fm", data.getTelekineticDistance())
						+ " §8| §dTK Hold: §7" + data.getTelekineticHoldTicks() + " Ticks"), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§dMachtstille: §7" + data.getForceSilenceTicks() + " Ticks"
						+ " §8| §dDeath Protection: §7" + (gameTime - data.getLastDeathProtectionTime() < 6000 ? (6000 - (gameTime - data.getLastDeathProtectionTime())) / 20 + "s CD" : "§aBereit")
						+ " §8| §dBloodrush: §7" + (gameTime - data.getLastBloodrushTime() < 1200 ? (1200 - (gameTime - data.getLastBloodrushTime())) / 20 + "s CD" : "§aBereit")), false);

				// 5. Player Persistent Tags (Kampf-Status, Meditation, Channeling)
				context.getSource().sendSuccess(() -> Component.literal("§e--- Player State (NBT) ---"), false);
				boolean inCombat = LightsaberCombatEvents.isInCombatCooldown(player);
				int combatCdRem = LightsaberCombatEvents.getRemainingCombatCooldownSeconds(player);
				boolean isMed = player.getPersistentData().getBoolean("IsMeditating");
				boolean isFree = player.getPersistentData().getBoolean("IsFreeMeditation");
				boolean atSith = player.getPersistentData().getBoolean("AtSithHolocron");
				String channel = player.getPersistentData().getString("ForceChanneling");
				float barrierDmg = player.getPersistentData().getFloat("BarrierAbsorbedDmg");
				context.getSource().sendSuccess(() -> Component.literal(
						"§aKampf-Status: §7" + (inCombat ? "§cIm Kampf (" + combatCdRem + "s Cooldown)" : "§aFriedlich")
						+ " §8| §aMeditiert: §7" + (isMed ? (isFree ? "§eFrei" : (atSith ? "§cSith-Holocron" : "§9Jedi-Holocron")) : "§7Nein")), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§aChanneling: §7" + (channel.isEmpty() ? "Keines" : channel)
						+ " §8| §aBarriere Absorbiert: §7" + String.format(Locale.ROOT, "%.1f", barrierDmg)
						+ " §8| §aJump NoFall: §7" + player.getPersistentData().getBoolean("ForceJumpNoFall")), false);

				// 6. Skills
				context.getSource().sendSuccess(() -> Component.literal("§e--- Freigeschaltete Skills (" + data.getUnlockedSkills().size() + ") ---"), false);
				context.getSource().sendSuccess(() -> Component.literal(
						"§7" + String.join(", ", data.getUnlockedSkills().stream().map(ResourceLocation::getPath).sorted().toList())), false);

				// 7. Kräfte Details
				context.getSource().sendSuccess(() -> Component.literal("§e--- Aktive Mächte Details (Tier | Lvl | Uses | Mastery | AmfP | AmfI | CD) ---"), false);
				for (ResourceLocation pId : PlayerForceData.ALL_ACTIVE_POWERS) {
					if (data.hasSkill(pId)) {
						int tier = data.getMaxUnlockedPowerLevel(pId);
						int lvl = data.getPowerLevel(pId);
						int uses = data.getPowerUsageCount(pId);
						int mastery = data.getMasteryTicks(pId);
						float amfp = data.getAmfP(pId);
						float amfi = data.getAmfI(pId);
						int cd = data.getPowerCooldown(pId);
						context.getSource().sendSuccess(() -> Component.literal(
								"§b" + pId.getPath() + ": §7T" + tier + " L" + lvl
								+ " §8| §7Uses: " + uses
								+ " §8| §7Mast: " + mastery + "/72k"
								+ " §8| §7AmfP: " + String.format(Locale.ROOT, "%.1f", amfp)
								+ " §8| §7AmfI: " + String.format(Locale.ROOT, "%.1f", amfi)
								+ (cd > 0 ? " §8| §cCD: " + String.format(Locale.ROOT, "%.1fs", cd / 20.0f) : "")), false);
					}
				}

				context.getSource().sendSuccess(() -> Component.literal("§6================================================"), false);
				return 1;
			}
			case "unlockAll" -> {
				data.unlockAllSkills();
				context.getSource()
						.sendSuccess(() -> Component.literal("§a[Skilltree] §7All available skills unlocked!"), true);
			}
			}

			data.sync(player);
			return 1;

		} catch (Exception e) {
			e.printStackTrace();
			context.getSource().sendFailure(Component.literal("§c[Error] Command failed: " + e.getMessage()));
			return 0;
		}
	}

	private static ItemStack createLightsaberStack(HiltParts parts, boolean isDouble) {
		ItemStack saber = new ItemStack(ALItems.LIGHTSABER.get());
		LightsaberData data = new LightsaberData(ALItems.GREEN_CRYSTAL.get(), List.of(), parts.emitter().get(),
				parts.switchSection().get(), parts.grip().get(),
				isDouble ? parts.emitter().get() : parts.pommel().get());
		saber.set(ALDataComponents.LIGHTSABER_DATA.get(), data);
		return saber;
	}

	private static int executeSaberGive(CommandContext<CommandSourceStack> context, String setName, boolean isDouble) {
		try {
			ServerPlayer player = context.getSource().getPlayerOrException();
			HiltParts parts = HILT_SETS.get(setName.toLowerCase(Locale.ROOT));

			if (parts == null) {
				context.getSource().sendFailure(Component.literal("§cUnknown Hilt Set: '" + setName + "'!"));
				return 0;
			}

			ItemStack saber = createLightsaberStack(parts, isDouble);
			player.getInventory().add(saber);

			String saberType = isDouble ? "Double-Bladed Lightsaber" : "Single-Bladed Lightsaber";
			context.getSource().sendSuccess(
					() -> Component.literal("§a[Debug] Granted " + setName.toUpperCase() + " (" + saberType + ")!"),
					true);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	private static int executeSaberGiveAll(CommandContext<CommandSourceStack> context) {
		try {
			ServerPlayer player = context.getSource().getPlayerOrException();
			for (Map.Entry<String, HiltParts> entry : HILT_SETS.entrySet()) {
				ItemStack saber = createLightsaberStack(entry.getValue(), false);
				player.getInventory().add(saber);
			}
			context.getSource().sendSuccess(
					() -> Component
							.literal("§a[Debug] Granted all " + HILT_SETS.size() + " Single-Bladed Lightsaber Sets!"),
					true);
			return HILT_SETS.size();
		} catch (Exception e) {
			return 0;
		}
	}

	private static int executeSaberDoubleAll(CommandContext<CommandSourceStack> context) {
		try {
			ServerPlayer player = context.getSource().getPlayerOrException();
			for (Map.Entry<String, HiltParts> entry : HILT_SETS.entrySet()) {
				ItemStack saber = createLightsaberStack(entry.getValue(), true);
				player.getInventory().add(saber);
			}
			context.getSource().sendSuccess(
					() -> Component
							.literal("§a[Debug] Granted all " + HILT_SETS.size() + " Double-Bladed Lightsaber Sets!"),
					true);
			return HILT_SETS.size();
		} catch (Exception e) {
			return 0;
		}
	}
}