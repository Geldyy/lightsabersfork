package com.fiskmods.pika.lightsabers;

import com.fiskmods.pika.lightsabers.block.DisassemblyStationBlock;
import com.fiskmods.pika.lightsabers.block.HolocronBlock;
import com.fiskmods.pika.lightsabers.block.LightsaberForgeBlock;
import com.fiskmods.pika.lightsabers.block.LightsaberStandBlock;
import com.fiskmods.pika.lightsabers.block.SithCoffinBlock;
import com.fiskmods.pika.lightsabers.block.SithStoneCoffinBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ALBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AdvancedLightsabers.MODID);

    // ==========================================
    // 1. WERKBÄNKE & HOLOCRONE
    // ==========================================
    public static final DeferredBlock<Block> LIGHTSABER_FORGE_LIGHT = BLOCKS.registerBlock(
            "lightsaber_forge_light",
            LightsaberForgeBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<Block> DISASSEMBLY_STATION = BLOCKS.registerBlock("disassembly_station",
            DisassemblyStationBlock::new,
            BlockBehaviour.Properties.of()
                    .strength(2.5F)
                    .sound(SoundType.METAL)
                    .noOcclusion());

    public static final DeferredBlock<Block> JEDI_HOLOCRON = BLOCKS.register("jedi_holocron", 
            () -> new HolocronBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion(), "jedi"));

    public static final DeferredBlock<Block> SITH_HOLOCRON = BLOCKS.register("sith_holocron", 
            () -> new HolocronBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion(), "sith"));

    // ==========================================
    // 2. HELLE MACHTSTEINE (LIGHT FORCESTONE)
    // ==========================================
    private static final BlockBehaviour.Properties LIGHT_PROPS = BlockBehaviour.Properties.of()
            .mapColor(MapColor.QUARTZ)
            .strength(2.0F, 6.0F)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops();

    public static final DeferredBlock<Block> LIGHT_FORCESTONE = BLOCKS.register("light_forcestone",
            () -> new Block(LIGHT_PROPS));

    public static final DeferredBlock<Block> LIGHT_FORCESTONE_INSCRIBED = BLOCKS.register("light_forcestone_inscribed",
            () -> new Block(LIGHT_PROPS));

    public static final DeferredBlock<Block> LIGHT_FORCESTONE_CRACKED = BLOCKS.register("light_forcestone_cracked",
            () -> new Block(LIGHT_PROPS));

    public static final DeferredBlock<Block> LIGHT_FORCESTONE_MOSSY = BLOCKS.register("light_forcestone_mossy",
            () -> new Block(LIGHT_PROPS));

    public static final DeferredBlock<RotatedPillarBlock> LIGHT_FORCESTONE_PILLAR = BLOCKS.register("light_forcestone_pillar",
            () -> new RotatedPillarBlock(LIGHT_PROPS));

    public static final DeferredBlock<RotatedPillarBlock> LIGHT_ACTIVATED_FORCESTONE_PILLAR = BLOCKS.register("light_activated_forcestone_pillar",
            () -> new RotatedPillarBlock(LIGHT_PROPS.lightLevel(state -> 12)));

    public static final DeferredBlock<StairBlock> LIGHT_FORCESTONE_STAIRS = BLOCKS.register("light_forcestone_stairs",
            () -> new StairBlock(LIGHT_FORCESTONE.get().defaultBlockState(), LIGHT_PROPS.lightLevel(state -> 0)));

    public static final DeferredBlock<SlabBlock> LIGHT_FORCESTONE_SLAB = BLOCKS.register("light_forcestone_slab",
            () -> new SlabBlock(LIGHT_PROPS.lightLevel(state -> 0)));

    // ==========================================
    // 3. DUNKLE MACHTSTEINE (DARK FORCESTONE)
    // ==========================================
    private static final BlockBehaviour.Properties DARK_PROPS = BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(3.0F, 9.0F)
            .sound(SoundType.DEEPSLATE)
            .requiresCorrectToolForDrops();

    public static final DeferredBlock<Block> DARK_FORCESTONE = BLOCKS.register("dark_forcestone",
            () -> new Block(DARK_PROPS));

    public static final DeferredBlock<Block> DARK_FORCESTONE_INSCRIBED = BLOCKS.register("dark_forcestone_inscribed",
            () -> new Block(DARK_PROPS));

    public static final DeferredBlock<Block> DARK_FORCESTONE_CRACKED = BLOCKS.register("dark_forcestone_cracked",
            () -> new Block(DARK_PROPS));

    public static final DeferredBlock<Block> DARK_FORCESTONE_MOSSY = BLOCKS.register("dark_forcestone_mossy",
            () -> new Block(DARK_PROPS));

    public static final DeferredBlock<RotatedPillarBlock> DARK_FORCESTONE_PILLAR = BLOCKS.register("dark_forcestone_pillar",
            () -> new RotatedPillarBlock(DARK_PROPS));

    public static final DeferredBlock<RotatedPillarBlock> DARK_ACTIVATED_FORCESTONE_PILLAR = BLOCKS.register("dark_activated_forcestone_pillar",
            () -> new RotatedPillarBlock(DARK_PROPS.lightLevel(state -> 15)));

    public static final DeferredBlock<StairBlock> DARK_FORCESTONE_STAIRS = BLOCKS.register("dark_forcestone_stairs",
            () -> new StairBlock(DARK_FORCESTONE.get().defaultBlockState(), DARK_PROPS.lightLevel(state -> 0)));

    public static final DeferredBlock<SlabBlock> DARK_FORCESTONE_SLAB = BLOCKS.register("dark_forcestone_slab",
            () -> new SlabBlock(DARK_PROPS.lightLevel(state -> 0)));
    
    // ==========================================
    // 3.5 SCHWARZE MACHTSTEINE (BLACK FORCESTONE)
    // ==========================================
    private static final BlockBehaviour.Properties BLACK_PROPS = BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(3.0F, 9.0F)
            .sound(SoundType.DEEPSLATE)
            .requiresCorrectToolForDrops();

    public static final DeferredBlock<Block> BLACK_FORCESTONE = BLOCKS.register("black_forcestone",
            () -> new Block(BLACK_PROPS));

    public static final DeferredBlock<Block> BLACK_FORCESTONE_INSCRIBED = BLOCKS.register("black_forcestone_inscribed",
            () -> new Block(BLACK_PROPS));

    public static final DeferredBlock<Block> BLACK_FORCESTONE_CRACKED = BLOCKS.register("black_forcestone_cracked",
            () -> new Block(BLACK_PROPS));

    public static final DeferredBlock<Block> BLACK_FORCESTONE_MOSSY = BLOCKS.register("black_forcestone_mossy",
            () -> new Block(BLACK_PROPS));

    public static final DeferredBlock<RotatedPillarBlock> BLACK_FORCESTONE_PILLAR = BLOCKS.register("black_forcestone_pillar",
            () -> new RotatedPillarBlock(BLACK_PROPS));

    public static final DeferredBlock<RotatedPillarBlock> BLACK_ACTIVATED_FORCESTONE_PILLAR = BLOCKS.register("black_activated_forcestone_pillar",
            () -> new RotatedPillarBlock(BLACK_PROPS.lightLevel(state -> 12)));

    public static final DeferredBlock<StairBlock> BLACK_FORCESTONE_STAIRS = BLOCKS.register("black_forcestone_stairs",
            () -> new StairBlock(BLACK_FORCESTONE.get().defaultBlockState(), BLACK_PROPS.lightLevel(state -> 0)));

    public static final DeferredBlock<SlabBlock> BLACK_FORCESTONE_SLAB = BLOCKS.register("black_forcestone_slab",
            () -> new SlabBlock(BLACK_PROPS.lightLevel(state -> 0)));

    // ==========================================
    // 4. DEKORATION & SARKOPHAGE
    // ==========================================
 // ==========================================
    // 4. DEKORATION & SARKOPHAGE
    // ==========================================
    public static final DeferredBlock<Block> SITH_COFFIN = BLOCKS.registerBlock(
            "sith_coffin",
            SithCoffinBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(50.0F, 2000.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion()
    );

    public static final DeferredBlock<Block> SITH_STONE_COFFIN = BLOCKS.registerBlock(
            "sith_stone_coffin",
            SithStoneCoffinBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(50.0F, 2000.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
    );

    public static final DeferredBlock<Block> LIGHTSABER_STAND = BLOCKS.registerBlock("lightsaber_stand",
            LightsaberStandBlock::new,
            BlockBehaviour.Properties.of()
                    .strength(1.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion());
}