package com.fiskmods.pika.lightsabers;

import com.fiskmods.pika.lightsabers.block.entity.DisassemblyStationBlockEntity;
import com.fiskmods.pika.lightsabers.block.entity.GlacierGateBlockEntity;
import com.fiskmods.pika.lightsabers.block.entity.HolocronBlockEntity;
import com.fiskmods.pika.lightsabers.block.entity.KyberPedestalBlockEntity;
import com.fiskmods.pika.lightsabers.block.entity.LightsaberForgeBlockEntity;
import com.fiskmods.pika.lightsabers.block.entity.LightsaberStandBlockEntity;
import com.fiskmods.pika.lightsabers.block.entity.ResonantKyberBlockEntity;
import com.fiskmods.pika.lightsabers.block.entity.SithCoffinBlockEntity;
import com.fiskmods.pika.lightsabers.block.entity.SithStoneCoffinBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ALBlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
			.create(Registries.BLOCK_ENTITY_TYPE, AdvancedLightsabers.MODID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LightsaberForgeBlockEntity>> LIGHTSABER_FORGE = BLOCK_ENTITIES
			.register("lightsaber_forge", () -> BlockEntityType.Builder
					.of(LightsaberForgeBlockEntity::new, ALBlocks.LIGHTSABER_FORGE_LIGHT.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DisassemblyStationBlockEntity>> DISASSEMBLY_STATION = BLOCK_ENTITIES
			.register("disassembly_station", () -> BlockEntityType.Builder
					.of(DisassemblyStationBlockEntity::new, ALBlocks.DISASSEMBLY_STATION.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LightsaberStandBlockEntity>> LIGHTSABER_STAND = BLOCK_ENTITIES
			.register("lightsaber_stand", () -> BlockEntityType.Builder
					.of(LightsaberStandBlockEntity::new, ALBlocks.LIGHTSABER_STAND.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HolocronBlockEntity>> HOLOCRON = BLOCK_ENTITIES
			.register("holocron",
					() -> BlockEntityType.Builder
							.of(HolocronBlockEntity::new, ALBlocks.JEDI_HOLOCRON.get(), ALBlocks.SITH_HOLOCRON.get())
							.build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SithCoffinBlockEntity>> SITH_COFFIN = BLOCK_ENTITIES
			.register("sith_coffin", () -> BlockEntityType.Builder
					.of(SithCoffinBlockEntity::new, ALBlocks.SITH_COFFIN.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SithStoneCoffinBlockEntity>> SITH_STONE_COFFIN = BLOCK_ENTITIES
			.register("sith_stone_coffin", () -> BlockEntityType.Builder
					.of(SithStoneCoffinBlockEntity::new, ALBlocks.SITH_STONE_COFFIN.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GlacierGateBlockEntity>> GLACIER_GATE = BLOCK_ENTITIES
			.register("glacier_gate", () -> BlockEntityType.Builder
					.of(GlacierGateBlockEntity::new, ALBlocks.GLACIER_GATE.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ResonantKyberBlockEntity>> RESONANT_KYBER = BLOCK_ENTITIES
			.register("resonant_kyber", () -> BlockEntityType.Builder
					.of(ResonantKyberBlockEntity::new, ALBlocks.RESONANT_KYBER_CLUSTER.get()).build(null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KyberPedestalBlockEntity>> KYBER_PEDESTAL = BLOCK_ENTITIES
			.register("kyber_pedestal", () -> BlockEntityType.Builder
					.of(KyberPedestalBlockEntity::new, ALBlocks.KYBER_PEDESTAL.get()).build(null));
}