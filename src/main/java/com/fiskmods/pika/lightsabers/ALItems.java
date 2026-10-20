package com.fiskmods.pika.lightsabers;

import com.fiskmods.pika.lightsabers.item.ItemCrystalPouch;
import com.fiskmods.pika.lightsabers.item.crystals.CrystalColor;
import com.fiskmods.pika.lightsabers.item.crystals.FocusingCrystalType;
import com.fiskmods.pika.lightsabers.item.crystals.ItemColorCrystal;
import com.fiskmods.pika.lightsabers.item.crystals.ItemFocusingCrystal;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaberPart;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ALItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AdvancedLightsabers.MODID);

	public static final DeferredItem<Item> FOCUSING_CRYSTAL_COMPRESSED = ITEMS.registerItem(
			"focusing_crystal_compressed", props -> new ItemFocusingCrystal(FocusingCrystalType.COMPRESSED, props));

	public static final DeferredItem<Item> FOCUSING_CRYSTAL_CRACKED = ITEMS.registerItem("focusing_crystal_cracked",
			props -> new ItemFocusingCrystal(FocusingCrystalType.CRACKED, props));

	public static final DeferredItem<Item> FOCUSING_CRYSTAL_INVERTING = ITEMS.registerItem("focusing_crystal_inverting",
			props -> new ItemFocusingCrystal(FocusingCrystalType.INVERTING, props));

	public static final DeferredItem<Item> FOCUSING_CRYSTAL_FINE_CUT = ITEMS.registerItem("focusing_crystal_fine_cut",
			props -> new ItemFocusingCrystal(FocusingCrystalType.FINE_CUT, props));

	public static final DeferredItem<Item> FOCUSING_CRYSTAL_PRISMATIC = ITEMS.registerItem("focusing_crystal_prismatic",
			props -> new ItemFocusingCrystal(FocusingCrystalType.PRISMATIC, props));

	public static final DeferredItem<Item> FOCUSING_CRYSTAL_SHOTO = ITEMS.registerItem("focusing_crystal_shoto",
			props -> new ItemFocusingCrystal(FocusingCrystalType.SHOTO, props));

	public static final DeferredItem<Item> FOCUSING_CRYSTAL_EXTENDED = ITEMS.registerItem("focusing_crystal_extended",
			props -> new ItemFocusingCrystal(FocusingCrystalType.EXTENDED, props));

	public static final DeferredItem<Item> FOCUSING_CRYSTAL_UNSTABLE = ITEMS.registerItem("focusing_crystal_unstable",
			props -> new ItemFocusingCrystal(FocusingCrystalType.UNSTABLE, props));

	public static final DeferredItem<Item> FOCUSING_CRYSTAL_PULSE = ITEMS.registerItem("focusing_crystal_pulse",
			props -> new ItemFocusingCrystal(FocusingCrystalType.PULSE, props));

	public static final DeferredItem<BlockItem> JEDI_HOLOCRON = ITEMS.register("jedi_holocron",
			() -> new BlockItem(ALBlocks.JEDI_HOLOCRON.get(), new Item.Properties()));

	public static final DeferredItem<BlockItem> SITH_HOLOCRON = ITEMS.register("sith_holocron",
			() -> new BlockItem(ALBlocks.SITH_HOLOCRON.get(), new Item.Properties()));

	public static final DeferredItem<Item> CIRCUITRY = ITEMS.registerSimpleItem("lightsaber_circuitry",
			new Item.Properties());

	public static final DeferredItem<Item> DEEP_BLUE_CRYSTAL = ITEMS.registerItem("deep_blue_crystal",
			props -> new ItemColorCrystal(CrystalColor.DEEP_BLUE, props));
	public static final DeferredItem<Item> MEDIUM_BLUE_CRYSTAL = ITEMS.registerItem("medium_blue_crystal",
			props -> new ItemColorCrystal(CrystalColor.MEDIUM_BLUE, props));
	public static final DeferredItem<Item> LIGHT_BLUE_CRYSTAL = ITEMS.registerItem("light_blue_crystal",
			props -> new ItemColorCrystal(CrystalColor.LIGHT_BLUE, props));
	public static final DeferredItem<Item> ARCTIC_BLUE_CRYSTAL = ITEMS.registerItem("arctic_blue_crystal",
			props -> new ItemColorCrystal(CrystalColor.ARCTIC_BLUE, props));
	public static final DeferredItem<Item> WHITE_CRYSTAL = ITEMS.registerItem("white_crystal",
			props -> new ItemColorCrystal(CrystalColor.WHITE, props));
	public static final DeferredItem<Item> INDIGO_CRYSTAL = ITEMS.registerItem("indigo_crystal",
			props -> new ItemColorCrystal(CrystalColor.INDIGO, props));
	public static final DeferredItem<Item> PURPLE_CRYSTAL = ITEMS.registerItem("purple_crystal",
			props -> new ItemColorCrystal(CrystalColor.PURPLE, props));
	public static final DeferredItem<Item> MAGENTA_CRYSTAL = ITEMS.registerItem("magenta_crystal",
			props -> new ItemColorCrystal(CrystalColor.MAGENTA, props));
	public static final DeferredItem<Item> PINK_CRYSTAL = ITEMS.registerItem("pink_crystal",
			props -> new ItemColorCrystal(CrystalColor.PINK, props));
	public static final DeferredItem<Item> RED_CRYSTAL = ITEMS.registerItem("red_crystal",
			props -> new ItemColorCrystal(CrystalColor.RED, props));
	public static final DeferredItem<Item> BLOOD_ORANGE_CRYSTAL = ITEMS.registerItem("blood_orange_crystal",
			props -> new ItemColorCrystal(CrystalColor.BLOOD_ORANGE, props));
	public static final DeferredItem<Item> AMBER_CRYSTAL = ITEMS.registerItem("amber_crystal",
			props -> new ItemColorCrystal(CrystalColor.AMBER, props));
	public static final DeferredItem<Item> YELLOW_CRYSTAL = ITEMS.registerItem("yellow_crystal",
			props -> new ItemColorCrystal(CrystalColor.YELLOW, props));
	public static final DeferredItem<Item> GOLD_CRYSTAL = ITEMS.registerItem("gold_crystal",
			props -> new ItemColorCrystal(CrystalColor.GOLD, props));
	public static final DeferredItem<Item> LIME_GREEN_CRYSTAL = ITEMS.registerItem("lime_green_crystal",
			props -> new ItemColorCrystal(CrystalColor.LIME_GREEN, props));
	public static final DeferredItem<Item> GREEN_CRYSTAL = ITEMS.registerItem("green_crystal",
			props -> new ItemColorCrystal(CrystalColor.GREEN, props));
	public static final DeferredItem<Item> MINT_GREEN_CRYSTAL = ITEMS.registerItem("mint_green_crystal",
			props -> new ItemColorCrystal(CrystalColor.MINT_GREEN, props));
	public static final DeferredItem<Item> CYAN_CRYSTAL = ITEMS.registerItem("cyan_crystal",
			props -> new ItemColorCrystal(CrystalColor.CYAN, props));

	public static final DeferredItem<Item> LIGHTSABER = ITEMS.registerItem("lightsaber",
			props -> new com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber(props.stacksTo(1)));

	public static final DeferredItem<Item> GRAFLEX_EMITTER = ITEMS.registerItem("graflex_emitter",
			props -> new ItemLightsaberPart("graflex", PartType.EMITTER, props));
	public static final DeferredItem<Item> GRAFLEX_SWITCH_SECTION = ITEMS.registerItem("graflex_switch_module",
			props -> new ItemLightsaberPart("graflex", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> GRAFLEX_GRIP = ITEMS.registerItem("graflex_grip",
			props -> new ItemLightsaberPart("graflex", PartType.BODY, props));
	public static final DeferredItem<Item> GRAFLEX_POMMEL = ITEMS.registerItem("graflex_pommel",
			props -> new ItemLightsaberPart("graflex", PartType.POMMEL, props));

	public static final DeferredItem<Item> PRODIGAL_SON_EMITTER = ITEMS.registerItem("prodigal_son_emitter",
			props -> new ItemLightsaberPart("prodigal_son", PartType.EMITTER, props));
	public static final DeferredItem<Item> PRODIGAL_SON_SWITCH_SECTION = ITEMS.registerItem(
			"prodigal_son_switch_module",
			props -> new ItemLightsaberPart("prodigal_son", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> PRODIGAL_SON_GRIP = ITEMS.registerItem("prodigal_son_grip",
			props -> new ItemLightsaberPart("prodigal_son", PartType.BODY, props));
	public static final DeferredItem<Item> PRODIGAL_SON_POMMEL = ITEMS.registerItem("prodigal_son_pommel",
			props -> new ItemLightsaberPart("prodigal_son", PartType.POMMEL, props));

	public static final DeferredItem<Item> VAID_ANCIENT_EMITTER = ITEMS.registerItem("vaid_ancient_emitter",
			props -> new ItemLightsaberPart("vaid_ancient", PartType.EMITTER, props));
	public static final DeferredItem<Item> VAID_ANCIENT_SWITCH_SECTION = ITEMS.registerItem(
			"vaid_ancient_switch_module",
			props -> new ItemLightsaberPart("vaid_ancient", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> VAID_ANCIENT_GRIP = ITEMS.registerItem("vaid_ancient_grip",
			props -> new ItemLightsaberPart("vaid_ancient", PartType.BODY, props));
	public static final DeferredItem<Item> VAID_ANCIENT_POMMEL = ITEMS.registerItem("vaid_ancient_pommel",
			props -> new ItemLightsaberPart("vaid_ancient", PartType.POMMEL, props));

	public static final DeferredItem<Item> VAID_MODERN_EMITTER = ITEMS.registerItem("vaid_modern_emitter",
			props -> new ItemLightsaberPart("vaid_modern", PartType.EMITTER, props));
	public static final DeferredItem<Item> VAID_MODERN_SWITCH_SECTION = ITEMS.registerItem("vaid_modern_switch_module",
			props -> new ItemLightsaberPart("vaid_modern", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> VAID_MODERN_GRIP = ITEMS.registerItem("vaid_modern_grip",
			props -> new ItemLightsaberPart("vaid_modern", PartType.BODY, props));
	public static final DeferredItem<Item> VAID_MODERN_POMMEL = ITEMS.registerItem("vaid_modern_pommel",
			props -> new ItemLightsaberPart("vaid_modern", PartType.POMMEL, props));

	public static final DeferredItem<Item> REDEEMER_EMITTER = ITEMS.registerItem("redeemer_emitter",
			props -> new ItemLightsaberPart("redeemer", PartType.EMITTER, props));
	public static final DeferredItem<Item> REDEEMER_SWITCH_SECTION = ITEMS.registerItem("redeemer_switch_module",
			props -> new ItemLightsaberPart("redeemer", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> REDEEMER_GRIP = ITEMS.registerItem("redeemer_grip",
			props -> new ItemLightsaberPart("redeemer", PartType.BODY, props));
	public static final DeferredItem<Item> REDEEMER_POMMEL = ITEMS.registerItem("redeemer_pommel",
			props -> new ItemLightsaberPart("redeemer", PartType.POMMEL, props));

	public static final DeferredItem<Item> REBORN_EMITTER = ITEMS.registerItem("reborn_emitter",
			props -> new ItemLightsaberPart("reborn", PartType.EMITTER, props));
	public static final DeferredItem<Item> REBORN_SWITCH_SECTION = ITEMS.registerItem("reborn_switch_module",
			props -> new ItemLightsaberPart("reborn", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> REBORN_GRIP = ITEMS.registerItem("reborn_grip",
			props -> new ItemLightsaberPart("reborn", PartType.BODY, props));
	public static final DeferredItem<Item> REBORN_POMMEL = ITEMS.registerItem("reborn_pommel",
			props -> new ItemLightsaberPart("reborn", PartType.POMMEL, props));

	public static final DeferredItem<Item> REBEL_EMITTER = ITEMS.registerItem("rebel_emitter",
			props -> new ItemLightsaberPart("rebel", PartType.EMITTER, props));
	public static final DeferredItem<Item> REBEL_SWITCH_SECTION = ITEMS.registerItem("rebel_switch_module",
			props -> new ItemLightsaberPart("rebel", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> REBEL_GRIP = ITEMS.registerItem("rebel_grip",
			props -> new ItemLightsaberPart("rebel", PartType.BODY, props));
	public static final DeferredItem<Item> REBEL_POMMEL = ITEMS.registerItem("rebel_pommel",
			props -> new ItemLightsaberPart("rebel", PartType.POMMEL, props));

	public static final DeferredItem<Item> MECHANICAL_EMITTER = ITEMS.registerItem("mechanical_emitter",
			props -> new ItemLightsaberPart("mechanical", PartType.EMITTER, props));
	public static final DeferredItem<Item> MECHANICAL_SWITCH_SECTION = ITEMS.registerItem("mechanical_switch_module",
			props -> new ItemLightsaberPart("mechanical", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> MECHANICAL_GRIP = ITEMS.registerItem("mechanical_grip",
			props -> new ItemLightsaberPart("mechanical", PartType.BODY, props));
	public static final DeferredItem<Item> MECHANICAL_POMMEL = ITEMS.registerItem("mechanical_pommel",
			props -> new ItemLightsaberPart("mechanical", PartType.POMMEL, props));

	public static final DeferredItem<Item> MAULER_EMITTER = ITEMS.registerItem("mauler_emitter",
			props -> new ItemLightsaberPart("mauler", PartType.EMITTER, props));
	public static final DeferredItem<Item> MAULER_SWITCH_SECTION = ITEMS.registerItem("mauler_switch_module",
			props -> new ItemLightsaberPart("mauler", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> MAULER_GRIP = ITEMS.registerItem("mauler_grip",
			props -> new ItemLightsaberPart("mauler", PartType.BODY, props));
	public static final DeferredItem<Item> MAULER_POMMEL = ITEMS.registerItem("mauler_pommel",
			props -> new ItemLightsaberPart("mauler", PartType.POMMEL, props));

	public static final DeferredItem<Item> MANDALORIAN_EMITTER = ITEMS.registerItem("mandalorian_emitter",
			props -> new ItemLightsaberPart("mandalorian", PartType.EMITTER, props));
	public static final DeferredItem<Item> MANDALORIAN_SWITCH_SECTION = ITEMS.registerItem("mandalorian_switch_module",
			props -> new ItemLightsaberPart("mandalorian", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> MANDALORIAN_GRIP = ITEMS.registerItem("mandalorian_grip",
			props -> new ItemLightsaberPart("mandalorian", PartType.BODY, props));
	public static final DeferredItem<Item> MANDALORIAN_POMMEL = ITEMS.registerItem("mandalorian_pommel",
			props -> new ItemLightsaberPart("mandalorian", PartType.POMMEL, props));

	public static final DeferredItem<Item> KNIGHTED_EMITTER = ITEMS.registerItem("knighted_emitter",
			props -> new ItemLightsaberPart("knighted", PartType.EMITTER, props));
	public static final DeferredItem<Item> KNIGHTED_SWITCH_SECTION = ITEMS.registerItem("knighted_switch_module",
			props -> new ItemLightsaberPart("knighted", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> KNIGHTED_GRIP = ITEMS.registerItem("knighted_grip",
			props -> new ItemLightsaberPart("knighted", PartType.BODY, props));
	public static final DeferredItem<Item> KNIGHTED_POMMEL = ITEMS.registerItem("knighted_pommel",
			props -> new ItemLightsaberPart("knighted", PartType.POMMEL, props));

	public static final DeferredItem<Item> JUGGERNAUT_EMITTER = ITEMS.registerItem("juggernaut_emitter",
			props -> new ItemLightsaberPart("juggernaut", PartType.EMITTER, props));
	public static final DeferredItem<Item> JUGGERNAUT_SWITCH_SECTION = ITEMS.registerItem("juggernaut_switch_module",
			props -> new ItemLightsaberPart("juggernaut", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> JUGGERNAUT_GRIP = ITEMS.registerItem("juggernaut_grip",
			props -> new ItemLightsaberPart("juggernaut", PartType.BODY, props));
	public static final DeferredItem<Item> JUGGERNAUT_POMMEL = ITEMS.registerItem("juggernaut_pommel",
			props -> new ItemLightsaberPart("juggernaut", PartType.POMMEL, props));

	public static final DeferredItem<Item> IMPERIAL_EMITTER = ITEMS.registerItem("imperial_emitter",
			props -> new ItemLightsaberPart("imperial", PartType.EMITTER, props));
	public static final DeferredItem<Item> IMPERIAL_SWITCH_SECTION = ITEMS.registerItem("imperial_switch_module",
			props -> new ItemLightsaberPart("imperial", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> IMPERIAL_GRIP = ITEMS.registerItem("imperial_grip",
			props -> new ItemLightsaberPart("imperial", PartType.BODY, props));
	public static final DeferredItem<Item> IMPERIAL_POMMEL = ITEMS.registerItem("imperial_pommel",
			props -> new ItemLightsaberPart("imperial", PartType.POMMEL, props));

	public static final DeferredItem<Item> FURY_EMITTER = ITEMS.registerItem("fury_emitter",
			props -> new ItemLightsaberPart("fury", PartType.EMITTER, props));
	public static final DeferredItem<Item> FURY_SWITCH_SECTION = ITEMS.registerItem("fury_switch_module",
			props -> new ItemLightsaberPart("fury", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> FURY_GRIP = ITEMS.registerItem("fury_grip",
			props -> new ItemLightsaberPart("fury", PartType.BODY, props));
	public static final DeferredItem<Item> FURY_POMMEL = ITEMS.registerItem("fury_pommel",
			props -> new ItemLightsaberPart("fury", PartType.POMMEL, props));

	public static final DeferredItem<Item> FULCRUM_EMITTER = ITEMS.registerItem("fulcrum_emitter",
			props -> new ItemLightsaberPart("fulcrum", PartType.EMITTER, props));
	public static final DeferredItem<Item> FULCRUM_SWITCH_SECTION = ITEMS.registerItem("fulcrum_switch_module",
			props -> new ItemLightsaberPart("fulcrum", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> FULCRUM_GRIP = ITEMS.registerItem("fulcrum_grip",
			props -> new ItemLightsaberPart("fulcrum", PartType.BODY, props));
	public static final DeferredItem<Item> FULCRUM_POMMEL = ITEMS.registerItem("fulcrum_pommel",
			props -> new ItemLightsaberPart("fulcrum", PartType.POMMEL, props));

	public static final DeferredItem<Item> DROIDEKA_EMITTER = ITEMS.registerItem("droideka_emitter",
			props -> new ItemLightsaberPart("droideka", PartType.EMITTER, props));
	public static final DeferredItem<Item> DROIDEKA_SWITCH_SECTION = ITEMS.registerItem("droideka_switch_module",
			props -> new ItemLightsaberPart("droideka", PartType.SWITCH_SECTION, props));
	public static final DeferredItem<Item> DROIDEKA_GRIP = ITEMS.registerItem("droideka_grip",
			props -> new ItemLightsaberPart("droideka", PartType.BODY, props));
	public static final DeferredItem<Item> DROIDEKA_POMMEL = ITEMS.registerItem("droideka_pommel",
			props -> new ItemLightsaberPart("droideka", PartType.POMMEL, props));

	public static final DeferredItem<Item> NETHER_INFUSED_ELIXIR = ITEMS.registerItem("nether_infused_elixir",
			props -> new Item(props.stacksTo(16).craftRemainder(net.minecraft.world.item.Items.GLASS_BOTTLE)));

	public static final DeferredItem<Item> PURIFICATION_ELIXIR = ITEMS.registerItem("purification_elixir",
			props -> new com.fiskmods.pika.lightsabers.item.ItemPurificationElixir(false, props));

	public static final DeferredItem<Item> PRISTINE_PURIFICATION_ELIXIR = ITEMS.registerItem(
			"pristine_purification_elixir",
			props -> new com.fiskmods.pika.lightsabers.item.ItemPurificationElixir(true, props));
	public static final DeferredItem<Item> CRYSTAL_POUCH = ITEMS.register("crystal_pouch",
			() -> new ItemCrystalPouch(new Item.Properties()));

	public static final DeferredItem<BlockItem> LIGHT_FORCESTONE = ITEMS
			.registerSimpleBlockItem(ALBlocks.LIGHT_FORCESTONE);
	public static final DeferredItem<BlockItem> LIGHT_FORCESTONE_INSCRIBED = ITEMS
			.registerSimpleBlockItem(ALBlocks.LIGHT_FORCESTONE_INSCRIBED);
	public static final DeferredItem<BlockItem> LIGHT_FORCESTONE_CRACKED = ITEMS
			.registerSimpleBlockItem(ALBlocks.LIGHT_FORCESTONE_CRACKED);
	public static final DeferredItem<BlockItem> LIGHT_FORCESTONE_MOSSY = ITEMS
			.registerSimpleBlockItem(ALBlocks.LIGHT_FORCESTONE_MOSSY);
	public static final DeferredItem<BlockItem> LIGHT_FORCESTONE_PILLAR = ITEMS
			.registerSimpleBlockItem(ALBlocks.LIGHT_FORCESTONE_PILLAR);
	public static final DeferredItem<BlockItem> LIGHT_ACTIVATED_FORCESTONE_PILLAR = ITEMS
			.registerSimpleBlockItem(ALBlocks.LIGHT_ACTIVATED_FORCESTONE_PILLAR);
	public static final DeferredItem<BlockItem> LIGHT_FORCESTONE_STAIRS = ITEMS
			.registerSimpleBlockItem(ALBlocks.LIGHT_FORCESTONE_STAIRS);
	public static final DeferredItem<BlockItem> LIGHT_FORCESTONE_SLAB = ITEMS
			.registerSimpleBlockItem(ALBlocks.LIGHT_FORCESTONE_SLAB);

	public static final DeferredItem<BlockItem> DARK_FORCESTONE = ITEMS
			.registerSimpleBlockItem(ALBlocks.DARK_FORCESTONE);
	public static final DeferredItem<BlockItem> DARK_FORCESTONE_INSCRIBED = ITEMS
			.registerSimpleBlockItem(ALBlocks.DARK_FORCESTONE_INSCRIBED);
	public static final DeferredItem<BlockItem> DARK_FORCESTONE_CRACKED = ITEMS
			.registerSimpleBlockItem(ALBlocks.DARK_FORCESTONE_CRACKED);
	public static final DeferredItem<BlockItem> DARK_FORCESTONE_MOSSY = ITEMS
			.registerSimpleBlockItem(ALBlocks.DARK_FORCESTONE_MOSSY);
	public static final DeferredItem<BlockItem> DARK_FORCESTONE_PILLAR = ITEMS
			.registerSimpleBlockItem(ALBlocks.DARK_FORCESTONE_PILLAR);
	public static final DeferredItem<BlockItem> DARK_ACTIVATED_FORCESTONE_PILLAR = ITEMS
			.registerSimpleBlockItem(ALBlocks.DARK_ACTIVATED_FORCESTONE_PILLAR);
	public static final DeferredItem<BlockItem> DARK_FORCESTONE_STAIRS = ITEMS
			.registerSimpleBlockItem(ALBlocks.DARK_FORCESTONE_STAIRS);
	public static final DeferredItem<BlockItem> DARK_FORCESTONE_SLAB = ITEMS
			.registerSimpleBlockItem(ALBlocks.DARK_FORCESTONE_SLAB);

	public static final DeferredItem<BlockItem> BLACK_FORCESTONE = ITEMS
			.registerSimpleBlockItem(ALBlocks.BLACK_FORCESTONE);
	public static final DeferredItem<BlockItem> BLACK_FORCESTONE_INSCRIBED = ITEMS
			.registerSimpleBlockItem(ALBlocks.BLACK_FORCESTONE_INSCRIBED);
	public static final DeferredItem<BlockItem> BLACK_FORCESTONE_CRACKED = ITEMS
			.registerSimpleBlockItem(ALBlocks.BLACK_FORCESTONE_CRACKED);
	public static final DeferredItem<BlockItem> BLACK_FORCESTONE_MOSSY = ITEMS
			.registerSimpleBlockItem(ALBlocks.BLACK_FORCESTONE_MOSSY);
	public static final DeferredItem<BlockItem> BLACK_FORCESTONE_PILLAR = ITEMS
			.registerSimpleBlockItem(ALBlocks.BLACK_FORCESTONE_PILLAR);
	public static final DeferredItem<BlockItem> BLACK_ACTIVATED_FORCESTONE_PILLAR = ITEMS
			.registerSimpleBlockItem(ALBlocks.BLACK_ACTIVATED_FORCESTONE_PILLAR);
	public static final DeferredItem<BlockItem> BLACK_FORCESTONE_STAIRS = ITEMS
			.registerSimpleBlockItem(ALBlocks.BLACK_FORCESTONE_STAIRS);
	public static final DeferredItem<BlockItem> BLACK_FORCESTONE_SLAB = ITEMS
			.registerSimpleBlockItem(ALBlocks.BLACK_FORCESTONE_SLAB);

	public static final DeferredItem<BlockItem> SITH_COFFIN = ITEMS.registerSimpleBlockItem(ALBlocks.SITH_COFFIN);
	public static final DeferredItem<BlockItem> SITH_STONE_COFFIN = ITEMS
			.registerSimpleBlockItem(ALBlocks.SITH_STONE_COFFIN);
	public static final DeferredItem<BlockItem> LIGHTSABER_STAND = ITEMS
			.registerSimpleBlockItem(ALBlocks.LIGHTSABER_STAND);
	public static final DeferredItem<BlockItem> LIGHTSABER_FORGE_LIGHT_ITEM = ITEMS
			.registerSimpleBlockItem("lightsaber_forge_light", ALBlocks.LIGHTSABER_FORGE_LIGHT);
	public static final DeferredItem<BlockItem> DISASSEMBLY_STATION = ITEMS
			.registerSimpleBlockItem("disassembly_station", ALBlocks.DISASSEMBLY_STATION);

	public static final DeferredItem<BlockItem> SOLAR_LENS = ITEMS
			.registerSimpleBlockItem(ALBlocks.SOLAR_LENS);
	public static final DeferredItem<BlockItem> GLACIER_GATE = ITEMS
			.registerSimpleBlockItem(ALBlocks.GLACIER_GATE);
	public static final DeferredItem<BlockItem> DORMANT_KYBER_CLUSTER = ITEMS
			.registerSimpleBlockItem(ALBlocks.DORMANT_KYBER_CLUSTER);
	public static final DeferredItem<BlockItem> RESONANT_KYBER_CLUSTER = ITEMS
			.registerSimpleBlockItem(ALBlocks.RESONANT_KYBER_CLUSTER);
	public static final DeferredItem<BlockItem> KYBER_PEDESTAL = ITEMS
			.registerSimpleBlockItem(ALBlocks.KYBER_PEDESTAL);
}