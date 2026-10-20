package com.fiskmods.pika.lightsabers.events;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID)
public class LightsaberCrystalEvents {

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (!player.level().isClientSide()) {
			return;
		}

		ItemStack mainHand = player.getMainHandItem();
		if (mainHand.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(mainHand)) {
			LightsaberData data = mainHand.get(ALDataComponents.LIGHTSABER_DATA.get());

			if (data != null && data.hasFocusCrystal(ALItems.FOCUSING_CRYSTAL_PULSE.get())) {
				spawnPulseBladeParticles(player);
			}
		}
	}

	private static void spawnPulseBladeParticles(Player player) {
		Minecraft mc = Minecraft.getInstance();
		boolean isFirstPerson = mc.options.getCameraType().isFirstPerson() && player == mc.player;

		if (isFirstPerson) {
			Vec3 eyePos = player.getEyePosition();
			Vec3 lookVec = player.getLookAngle();

			Vec3 rightVec = Vec3.directionFromRotation(0.0F, player.getYRot() + 90.0F);
			Vec3 upVec = rightVec.cross(lookVec).normalize();

			Vec3 handPos = eyePos.add(rightVec.scale(0.38D)).add(upVec.scale(-0.22D)).add(lookVec.scale(0.55D));

			double bladeProgress = player.getRandom().nextDouble() * 0.85D;
			Vec3 particlePos = handPos.add(upVec.scale(bladeProgress * 0.7D)).add(lookVec.scale(bladeProgress * 0.25D));

			if (player.getRandom().nextFloat() < 0.4F) {
				player.level().addParticle(ParticleTypes.ELECTRIC_SPARK,
						particlePos.x + (player.getRandom().nextDouble() - 0.5D) * 0.05D,
						particlePos.y + (player.getRandom().nextDouble() - 0.5D) * 0.05D,
						particlePos.z + (player.getRandom().nextDouble() - 0.5D) * 0.05D, 0.0D, 0.02D, 0.0D);
			}
		} else {

			Vec3 handPos = player.position().add(0.0D, player.getBbHeight() * 0.6D, 0.0D);
			if (player.getRandom().nextFloat() < 0.3F) {
				player.level().addParticle(ParticleTypes.ELECTRIC_SPARK,
						handPos.x + (player.getRandom().nextDouble() - 0.5D) * 0.4D,
						handPos.y + (player.getRandom().nextDouble() - 0.5D) * 0.4D,
						handPos.z + (player.getRandom().nextDouble() - 0.5D) * 0.4D, 0.0D, 0.05D, 0.0D);
			}
		}
	}
}