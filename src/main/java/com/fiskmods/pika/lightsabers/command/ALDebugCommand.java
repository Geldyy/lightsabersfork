package com.fiskmods.pika.lightsabers.command;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALItems;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ALDebugCommand {

    private record HiltParts(
            Supplier<Item> emitter,
            Supplier<Item> switchSection,
            Supplier<Item> grip,
            Supplier<Item> pommel
    ) {}

    private static final Map<String, HiltParts> HILT_SETS = new LinkedHashMap<>();

    static {
        registerHilt("graflex", ALItems.GRAFLEX_EMITTER, ALItems.GRAFLEX_SWITCH_SECTION, ALItems.GRAFLEX_GRIP, ALItems.GRAFLEX_POMMEL);
        registerHilt("redeemer", ALItems.REDEEMER_EMITTER, ALItems.REDEEMER_SWITCH_SECTION, ALItems.REDEEMER_GRIP, ALItems.REDEEMER_POMMEL);
        registerHilt("mauler", ALItems.MAULER_EMITTER, ALItems.MAULER_SWITCH_SECTION, ALItems.MAULER_GRIP, ALItems.MAULER_POMMEL);
        registerHilt("prodigal_son", ALItems.PRODIGAL_SON_EMITTER, ALItems.PRODIGAL_SON_SWITCH_SECTION, ALItems.PRODIGAL_SON_GRIP, ALItems.PRODIGAL_SON_POMMEL);
        registerHilt("knighted", ALItems.KNIGHTED_EMITTER, ALItems.KNIGHTED_SWITCH_SECTION, ALItems.KNIGHTED_GRIP, ALItems.KNIGHTED_POMMEL);
        registerHilt("droideka", ALItems.DROIDEKA_EMITTER, ALItems.DROIDEKA_SWITCH_SECTION, ALItems.DROIDEKA_GRIP, ALItems.DROIDEKA_POMMEL);
        registerHilt("fulcrum", ALItems.FULCRUM_EMITTER, ALItems.FULCRUM_SWITCH_SECTION, ALItems.FULCRUM_GRIP, ALItems.FULCRUM_POMMEL);
        registerHilt("juggernaut", ALItems.JUGGERNAUT_EMITTER, ALItems.JUGGERNAUT_SWITCH_SECTION, ALItems.JUGGERNAUT_GRIP, ALItems.JUGGERNAUT_POMMEL);
        registerHilt("mechanical", ALItems.MECHANICAL_EMITTER, ALItems.MECHANICAL_SWITCH_SECTION, ALItems.MECHANICAL_GRIP, ALItems.MECHANICAL_POMMEL);
        registerHilt("mandalorian", ALItems.MANDALORIAN_EMITTER, ALItems.MANDALORIAN_SWITCH_SECTION, ALItems.MANDALORIAN_GRIP, ALItems.MANDALORIAN_POMMEL);
        registerHilt("fury", ALItems.FURY_EMITTER, ALItems.FURY_SWITCH_SECTION, ALItems.FURY_GRIP, ALItems.FURY_POMMEL);
        registerHilt("rebel", ALItems.REBEL_EMITTER, ALItems.REBEL_SWITCH_SECTION, ALItems.REBEL_GRIP, ALItems.REBEL_POMMEL);
        registerHilt("imperial", ALItems.IMPERIAL_EMITTER, ALItems.IMPERIAL_SWITCH_SECTION, ALItems.IMPERIAL_GRIP, ALItems.IMPERIAL_POMMEL);
        registerHilt("reborn", ALItems.REBORN_EMITTER, ALItems.REBORN_SWITCH_SECTION, ALItems.REBORN_GRIP, ALItems.REBORN_POMMEL);
        registerHilt("vaid_modern", ALItems.VAID_MODERN_EMITTER, ALItems.VAID_MODERN_SWITCH_SECTION, ALItems.VAID_MODERN_GRIP, ALItems.VAID_MODERN_POMMEL);
        registerHilt("vaid_ancient", ALItems.VAID_ANCIENT_EMITTER, ALItems.VAID_ANCIENT_SWITCH_SECTION, ALItems.VAID_ANCIENT_GRIP, ALItems.VAID_ANCIENT_POMMEL);
    }

    private static void registerHilt(String name, Supplier<Item> emitter, Supplier<Item> switchSection, Supplier<Item> grip, Supplier<Item> pommel) {
        HILT_SETS.put(name.toLowerCase(Locale.ROOT), new HiltParts(emitter, switchSection, grip, pommel));
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("alpikadebug")
                .requires(source -> source.hasPermission(2))
                
                // --- ALTES SABER SYSTEM ---
                .then(Commands.literal("saber")
                    .then(Commands.literal("give")
                        .then(Commands.literal("all").executes(ALDebugCommand::executeSaberGiveAll))
                        .then(Commands.argument("set", StringArgumentType.word())
                            .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(HILT_SETS.keySet(), builder))
                            .executes(ctx -> executeSaberGive(ctx, StringArgumentType.getString(ctx, "set"), false))
                        )
                    )
                    .then(Commands.literal("double")
                        .then(Commands.literal("all").executes(ALDebugCommand::executeSaberDoubleAll))
                        .then(Commands.argument("set", StringArgumentType.word())
                            .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(HILT_SETS.keySet(), builder))
                            .executes(ctx -> executeSaberGive(ctx, StringArgumentType.getString(ctx, "set"), true))
                        )
                    )
                )

                // --- NEUES FORCE SYSTEM ---
                .then(Commands.literal("force")
                    .then(Commands.literal("addXp")
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0))
                            .executes(ctx -> executeForceData(ctx, "addForceXp", FloatArgumentType.getFloat(ctx, "amount")))
                        )
                    )
                    .then(Commands.literal("addPoints")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                            .executes(ctx -> executeForceData(ctx, "addForcePoints", IntegerArgumentType.getInteger(ctx, "amount")))
                        )
                    )
                    .then(Commands.literal("reset")
                        .executes(ctx -> executeForceData(ctx, "reset", 0))
                    )
                    // NEU: stats Befehl
                    .then(Commands.literal("stats")
                        .executes(ctx -> executeForceData(ctx, "stats", 0))
                    )
                )

                // --- NEUES ABILITY SYSTEM (Lightsaber Skills) ---
                .then(Commands.literal("abilities")
                    .then(Commands.literal("addPoints")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                            .executes(ctx -> executeForceData(ctx, "addAbilityPoints", IntegerArgumentType.getInteger(ctx, "amount")))
                        )
                    )
                    .then(Commands.literal("addKills")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                            .executes(ctx -> executeForceData(ctx, "addKills", IntegerArgumentType.getInteger(ctx, "amount")))
                        )
                    )
                    // NEU: unlockAll Befehl
                    .then(Commands.literal("unlockAll")
                        .executes(ctx -> executeForceData(ctx, "unlockAll", 0))
                    )
                )
        );
    }

    // ==========================================
    // DELEGATE METHODE FÜR ALLE SKILLTREE DATEN
    // ==========================================
    private static int executeForceData(CommandContext<CommandSourceStack> context, String action, float value) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            PlayerForceData data = PlayerForceData.get(player);
            
            if (data == null) {
                context.getSource().sendFailure(Component.literal("§c[Fehler] PlayerForceData konnte nicht geladen werden."));
                return 0;
            }

            switch (action) {
                case "addForceXp" -> {
                    data.addForceXp(value);
                    context.getSource().sendSuccess(() -> Component.literal("§b[Macht] §7" + value + " XP hinzugefügt. (Aktuell: " + data.getForceXp() + " XP, " + data.getForcePoints() + " Punkte)"), true);
                }
                case "addForcePoints" -> {
                    // Temporär direkt auf die Variable zugreifen oder Setter bauen.
                    // Da wir keinen Setter für Force Points in der Klasse hatten, machen wir es per Schleife über XP
                    float xpNeeded = value * 2000.0f; // FORCE_XP_PER_POINT
                    data.addForceXp(xpNeeded);
                    context.getSource().sendSuccess(() -> Component.literal("§b[Macht] §7" + (int)value + " Punkte hinzugefügt."), true);
                }
                case "addAbilityPoints" -> {
                    // Da wir keinen direkten add-Befehl für Ability Points haben, machen wir es per Kill-Simulation
                    // Wir addieren einfach X Punkte. (Dafür fügen wir gleich noch einen kleinen public Setter in PlayerForceData ein).
                    int currentPoints = data.getAbilityPoints();
                    data.consumeAbilityPoints(currentPoints); // Leer machen
                    data.addAbilityPoints(currentPoints + (int)value);
                    context.getSource().sendSuccess(() -> Component.literal("§c[Kampf] §7" + (int)value + " Fähigkeiten-Punkte hinzugefügt."), true);
                }
                case "addKills" -> {
                    int kills = (int)value;
                    for (int i = 0; i < kills; i++) {
                        data.addLightsaberKill();
                    }
                    context.getSource().sendSuccess(() -> Component.literal("§c[Kampf] §7" + kills + " Lichtschwert-Kills simuliert."), true);
                }
                case "reset" -> {
                    data.resetAll();
                    context.getSource().sendSuccess(() -> Component.literal("§e[Skilltree] §7Alle Punkte, XP und Kills zurückgesetzt."), true);
                }
                // NEU: stats Fall hinzugefügt
                case "stats" -> {
                    context.getSource().sendSuccess(() -> Component.literal("§6--- Spieler Stats ---"), false);
                    context.getSource().sendSuccess(() -> Component.literal("§bMacht XP: §7" + data.getForceXp() + " / 2000.0"), false);
                    context.getSource().sendSuccess(() -> Component.literal("§bMacht Punkte: §7" + data.getForcePoints()), false);
                    context.getSource().sendSuccess(() -> Component.literal("§cLichtschwert Kills: §7" + data.getLightsaberKills() + " / " + data.getNextKillThreshold()), false);
                    context.getSource().sendSuccess(() -> Component.literal("§cLichtschwert Punkte: §7" + data.getAbilityPoints()), false);
                    context.getSource().sendSuccess(() -> Component.literal("§eFreigeschaltete Skills: §7" + data.getUnlockedSkills().size()), false);
                    context.getSource().sendSuccess(() -> Component.literal("§6---------------------"), false);
                    return 1; // Wir wollen bei "stats" eigentlich nicht direkt synchronisieren, returnen also hier
                }
                // NEU: unlockAll Fall hinzugefügt
                case "unlockAll" -> {
                    data.unlockAllSkills();
                    context.getSource().sendSuccess(() -> Component.literal("§a[Skilltree] §7Alle verfügbaren Fähigkeiten wurden freigeschaltet!"), true);
                }
            }

            // Neues Savegame direkt an den Client schicken, damit sich das UI aktualisiert!
            data.sync(player);
            return 1;
            
        } catch (Exception e) {
            // NEU: Das zwingt Eclipse, den Fehler rot und detailliert in die Konsole zu schreiben!
            e.printStackTrace(); 
            
            context.getSource().sendFailure(Component.literal("§c[Fehler] Befehl fehlgeschlagen: " + e.getMessage()));
            return 0;
        }
    }

    // ==========================================
    // ALTE SABER METHODEN (Unverändert)
    // ==========================================
    private static ItemStack createLightsaberStack(HiltParts parts, boolean isDouble) {
        ItemStack saber = new ItemStack(ALItems.LIGHTSABER.get());
        LightsaberData data = new LightsaberData(
                ALItems.GREEN_CRYSTAL.get(),
                List.of(),
                parts.emitter().get(),
                parts.switchSection().get(),
                parts.grip().get(),
                isDouble ? parts.emitter().get() : parts.pommel().get()
        );
        saber.set(ALDataComponents.LIGHTSABER_DATA.get(), data);
        return saber;
    }

    private static int executeSaberGive(CommandContext<CommandSourceStack> context, String setName, boolean isDouble) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            HiltParts parts = HILT_SETS.get(setName.toLowerCase(Locale.ROOT));

            if (parts == null) {
                context.getSource().sendFailure(Component.literal("§cUnbekanntes Hilt-Set: '" + setName + "'!"));
                return 0;
            }

            ItemStack saber = createLightsaberStack(parts, isDouble);
            player.getInventory().add(saber);

            String saberType = isDouble ? "Doppellichtschwert" : "Einzellichtschwert";
            context.getSource().sendSuccess(() -> Component.literal("§a[Debug] " + setName.toUpperCase() + "-" + saberType + " gegeben!"), true);
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
            context.getSource().sendSuccess(() -> Component.literal("§a[Debug] Alle " + HILT_SETS.size() + " Lichtschwert-Sets gegeben!"), true);
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
            context.getSource().sendSuccess(() -> Component.literal("§a[Debug] Alle " + HILT_SETS.size() + " Doppellichtschwert-Sets gegeben!"), true);
            return HILT_SETS.size();
        } catch (Exception e) {
            return 0;
        }
    }
}