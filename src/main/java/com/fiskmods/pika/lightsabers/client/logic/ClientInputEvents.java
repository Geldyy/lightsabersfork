package com.fiskmods.pika.lightsabers.client.logic;

import java.util.List;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.ALKeyBinds;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.network.payload.DoubleJumpPayload;
import com.fiskmods.pika.lightsabers.network.payload.ThrowLightsaberPayload;
import com.fiskmods.pika.lightsabers.network.payload.ToggleLightsaberPayload;
import com.fiskmods.pika.lightsabers.network.payload.UseForcePayload;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class ClientInputEvents {

	private static boolean wasJumpKeyDown = false;
	private static int jumpsDone = 0;
	private static boolean jumpKeyReleasedInAir = false;
	private static int jumpChargeTicks = 0;

	private static boolean wasUseForceDown = false;

	boolean isUseForceDown = ALKeyBinds.USE_FORCE.isDown();

	@SubscribeEvent
	public static void onMouseScroll(net.neoforged.neoforge.client.event.InputEvent.MouseScrollingEvent event) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null || mc.level == null)
			return;

		PlayerForceData data = PlayerForceData.get(player);
		boolean isHoldingEntity = data != null && data.getTelekineticEntityId() != -1;

		List<com.fiskmods.pika.lightsabers.entity.TelekineticBlockEntity> heldBlocks = mc.level.getEntitiesOfClass(
				com.fiskmods.pika.lightsabers.entity.TelekineticBlockEntity.class,
				player.getBoundingBox().inflate(24.0D), e -> e.getOwnerId() == player.getId() && !e.isThrown());

		if (isHoldingEntity || !heldBlocks.isEmpty()) {
			event.setCanceled(true);
			double delta = event.getScrollDeltaY();
			if (delta != 0) {
				PacketDistributor.sendToServer(
						new com.fiskmods.pika.lightsabers.network.payload.TelekinesisScrollPayload((float) delta));
			}
		}
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if (player == null)
			return;

		while (ALKeyBinds.TOGGLE_LIGHTSABER.consumeClick()) {
			if (player.getMainHandItem().getItem() instanceof ItemLightsaber
					|| player.getOffhandItem().getItem() instanceof ItemLightsaber) {
				PacketDistributor.sendToServer(new ToggleLightsaberPayload(false));
			}
		}

		while (ALKeyBinds.SINGLE_SWITCH.consumeClick()) {
			if (player.getMainHandItem().getItem() instanceof ItemLightsaber
					|| player.getOffhandItem().getItem() instanceof ItemLightsaber) {
				PacketDistributor.sendToServer(new ToggleLightsaberPayload(true));
			}
		}

		while (ALKeyBinds.THROW_LIGHTSABER.consumeClick()) {
			if (player.getMainHandItem().getItem() instanceof ItemLightsaber
					|| player.getOffhandItem().getItem() instanceof ItemLightsaber) {
				PacketDistributor.sendToServer(new ThrowLightsaberPayload());
			}
		}

		while (ALKeyBinds.CYCLE_FORCE.consumeClick()) {
			PlayerForceData data = PlayerForceData.get(player);
			if (data != null && data.isForceSensitive()) {
				List<ResourceLocation> powers = data.getAvailableActivePowers();
				if (!powers.isEmpty()) {
					mc.setScreen(new com.fiskmods.pika.lightsabers.client.gui.ForceRadialScreen(powers));
					com.fiskmods.pika.lightsabers.client.gui.ForceHudOverlay.wakeUp();
				}
			}
		}

		while (ALKeyBinds.MEDITATE.consumeClick()) {
			if (MeditationStateManager.isMeditating()) {
				MeditationInputHandler.exitMeditation();
			} else {
				PacketDistributor.sendToServer(
						new com.fiskmods.pika.lightsabers.network.payload.MeditationTogglePayload(true, net.minecraft.core.BlockPos.ZERO, false));
			}
		}

		boolean isUseForceDown = ALKeyBinds.USE_FORCE.isDown();

		if (isUseForceDown) {
			com.fiskmods.pika.lightsabers.client.gui.ForceHudOverlay.wakeUp();
		}

		if (isUseForceDown) {
			com.fiskmods.pika.lightsabers.client.gui.ForceHudOverlay.wakeUp();
		}

		if (isUseForceDown != wasUseForceDown) {
			PacketDistributor.sendToServer(new UseForcePayload(isUseForceDown));
			wasUseForceDown = isUseForceDown;
		}

		if (mc.screen == null && !player.isCreative() && !player.isSpectator()) {
			boolean isJumpDown = mc.options.keyJump.isDown();
			boolean isSneakDown = mc.options.keyShift.isDown();

			PlayerForceData data = PlayerForceData.get(player);
			boolean hasJumpSkill = data != null && data.isForceSensitive()
					&& data.hasSkill(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_jump"));

			if (hasJumpSkill && player.onGround() && isSneakDown && isJumpDown) {
				jumpChargeTicks++;
				if (jumpChargeTicks % 5 == 0) {
					float pitch = 0.8F + Math.min(1.0F, jumpChargeTicks / 30.0F) * 0.8F;
					player.playSound(net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP, 0.4F, pitch);
				}
			} else {
				if (jumpChargeTicks >= 5 && player.onGround()) {
					float charge = Math.min(1.0F, jumpChargeTicks / 30.0F);
					PacketDistributor.sendToServer(
							new com.fiskmods.pika.lightsabers.network.payload.ChargedJumpPayload(charge));

					double yVel = 0.85D + (charge * 1.35D);
					Vec3 look = player.getLookAngle();
					double forward = player.isSprinting() ? 0.65D : 0.35D;
					player.setDeltaMovement(look.x * forward * charge, yVel, look.z * forward * charge);
					player.hasImpulse = true;
				}
				jumpChargeTicks = 0;
			}

			if (player.onGround() || player.isInWater() || player.isFallFlying() || player.onClimbable()) {
				jumpsDone = 0;
				jumpKeyReleasedInAir = false;
			} else if (!isJumpDown) {
				jumpKeyReleasedInAir = true;
			}

			if (isJumpDown && !wasJumpKeyDown) {

				if (jumpKeyReleasedInAir && jumpsDone == 0) {
					if (data != null && data.isForceSensitive() && data.getForceEnergy() >= 10.0f) {
						jumpsDone++;
						data.consumeForceEnergy(10.0f);

						Vec3 motion = player.getDeltaMovement();
						Vec3 look = player.getLookAngle();
						Vec3 horizLook = new Vec3(look.x, 0, look.z);
						if (horizLook.lengthSqr() > 1.0E-4D) {
							horizLook = horizLook.normalize();
						} else {
							horizLook = Vec3.ZERO;
						}

						double horizBoost = player.isSprinting() ? 0.65D : 0.45D;
						player.setDeltaMovement(motion.x + horizLook.x * horizBoost, 0.65D,
								motion.z + horizLook.z * horizBoost);
						player.hasImpulse = true;

						PacketDistributor.sendToServer(new DoubleJumpPayload());
					}
				}
			}
			wasJumpKeyDown = isJumpDown;
		}
	}
}