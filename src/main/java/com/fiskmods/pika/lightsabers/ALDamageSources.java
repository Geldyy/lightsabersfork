package com.fiskmods.pika.lightsabers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ALDamageSources {

	public static final ResourceKey<DamageType> LIGHTSABER = ResourceKey.create(Registries.DAMAGE_TYPE,
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "lightsaber"));

	public static DamageSource causeLightsaberDamage(Level level, Entity attacker) {
		return new DamageSource(
				level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(LIGHTSABER), attacker);
	}
}