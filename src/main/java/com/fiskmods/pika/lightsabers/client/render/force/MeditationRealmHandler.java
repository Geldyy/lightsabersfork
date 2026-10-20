package com.fiskmods.pika.lightsabers.client.render.force;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.ViewportEvent.RenderFog;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class MeditationRealmHandler {

	@SubscribeEvent
	public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
		if (!MeditationStateManager.isMeditating())
			return;

		float progress = MeditationStateManager.getTransitionProgress(1.0F);

		if (progress > 0.01F) {
			event.setRed(Mth.lerp(progress, event.getRed(), 0.06F));
			event.setGreen(Mth.lerp(progress, event.getGreen(), 0.03F));
			event.setBlue(Mth.lerp(progress, event.getBlue(), 0.15F));
		}
	}

	@SubscribeEvent
	public static void onRenderFog(RenderFog event) {
		if (!MeditationStateManager.isMeditating())
			return;

		float progress = MeditationStateManager.getTransitionProgress(1.0F);

		if (progress > 0.005F) {
			float targetNear = 15.0F;
			float targetFar = 24.0F;

			float near = Mth.lerp(progress, event.getNearPlaneDistance(), targetNear);
			float far = Mth.lerp(progress, event.getFarPlaneDistance(), targetFar);

			event.setNearPlaneDistance(near);
			event.setFarPlaneDistance(far);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
		if (!MeditationStateManager.isMeditating())
			return;

		LivingEntity entity = event.getEntity();
		Minecraft mc = Minecraft.getInstance();
		if (entity == mc.player)
			return;

		Vec3 center = MeditationStateManager.getMeditationCenter();
		if (center == null || center.equals(Vec3.ZERO))
			return;

		double dx = entity.getX() - center.x;
		double dz = entity.getZ() - center.z;
		double distSq = dx * dx + dz * dz;

		if (distSq > 27.04D) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
		if (!MeditationStateManager.isMeditating())
			return;

		Player player = event.getEntity();
		if (player.isLocalPlayer())
			return;

		Vec3 center = MeditationStateManager.getMeditationCenter();
		if (center == null || center.equals(Vec3.ZERO))
			return;

		double dx = player.getX() - center.x;
		double dz = player.getZ() - center.z;

		if (dx * dx + dz * dz > 27.04D) {
			event.setCanceled(true);
		}
	}
}