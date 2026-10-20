package com.fiskmods.pika.lightsabers;

import java.util.function.Supplier;

import com.fiskmods.pika.lightsabers.entity.EntityLightsaber;
import com.fiskmods.pika.lightsabers.entity.TelekineticBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ALEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE,
			AdvancedLightsabers.MODID);

	public static final Supplier<EntityType<EntityLightsaber>> LIGHTSABER = ENTITIES.register("lightsaber",
			() -> EntityType.Builder.<EntityLightsaber>of(EntityLightsaber::new, MobCategory.MISC).sized(0.5F, 0.5F)
					.clientTrackingRange(4).updateInterval(1).build("lightsaber"));

	public static final Supplier<EntityType<TelekineticBlockEntity>> TELEKINETIC_BLOCK = ENTITIES.register(
			"telekinetic_block",
			() -> EntityType.Builder.<TelekineticBlockEntity>of(TelekineticBlockEntity::new, MobCategory.MISC)
					.sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(1).build("telekinetic_block"));

	public static final Supplier<EntityType<com.fiskmods.pika.lightsabers.entity.ForcePhantomEntity>> FORCE_PHANTOM = ENTITIES
			.register("force_phantom", () -> EntityType.Builder
					.<com.fiskmods.pika.lightsabers.entity.ForcePhantomEntity>of(
							com.fiskmods.pika.lightsabers.entity.ForcePhantomEntity::new, MobCategory.MONSTER)
					.sized(0.6F, 1.95F).clientTrackingRange(8).updateInterval(1).build("force_phantom"));
}