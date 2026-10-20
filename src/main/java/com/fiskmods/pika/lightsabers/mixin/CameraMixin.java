package com.fiskmods.pika.lightsabers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@Mixin(Camera.class)
public abstract class CameraMixin {

	@Shadow
	protected abstract void setPosition(double x, double y, double z);

	@Shadow
	protected abstract void setRotation(float yRot, float xRot);

	@Inject(method = "setup", at = @At("TAIL"))
	public void onSetup(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse,
			float partialTick, CallbackInfo ci) {
		if (!MeditationStateManager.isMeditating())
			return;

		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.level == null)
			return;

		float enterProgress = MeditationStateManager.getTransitionProgress(partialTick);
		float revealProgress = MeditationStateManager.getStelesRevealProgress(partialTick);

		float nodeFocus = MeditationStateManager.getNodeFocusProgress(partialTick);

		if (enterProgress <= 0.01F)
			return;

		if (MeditationStateManager.isFreeMeditation()) {
			Vec3 playerPos = mc.player.position();
			Vec3 focus = playerPos.add(0, 0.7, 0);

			float targetDistance = 3.8F;
			float targetPitch = 15.0F;
			float targetYaw = MeditationStateManager.getOrbitYaw(partialTick);

			float pitchRad = targetPitch * Mth.DEG_TO_RAD;
			float yawRad = targetYaw * Mth.DEG_TO_RAD;

			double lookX = -Mth.sin(yawRad) * Mth.cos(pitchRad);
			double lookY = -Mth.sin(pitchRad);
			double lookZ = Mth.cos(yawRad) * Mth.cos(pitchRad);

			double camX = focus.x - lookX * targetDistance;
			double camY = focus.y - lookY * targetDistance;
			double camZ = focus.z - lookZ * targetDistance;

			ClipContext context = new ClipContext(focus, new Vec3(camX, camY, camZ), ClipContext.Block.VISUAL,
					ClipContext.Fluid.NONE, mc.player);
			BlockHitResult hit = mc.level.clip(context);
			if (hit.getType() != HitResult.Type.MISS) {
				float safeDistance = (float) hit.getLocation().distanceTo(focus) - 0.2F;
				camX = focus.x - lookX * safeDistance;
				camY = focus.y - lookY * safeDistance;
				camZ = focus.z - lookZ * safeDistance;
			}

			Camera cam = (Camera) (Object) this;
			if (enterProgress < 1.0F) {
				float ease = (float) (1.0 - Math.pow(1.0 - enterProgress, 3));
				camX = Mth.lerp(ease, cam.getPosition().x, camX);
				camY = Mth.lerp(ease, cam.getPosition().y, camY);
				camZ = Mth.lerp(ease, cam.getPosition().z, camZ);
				targetYaw = Mth.lerp(ease, cam.getYRot(), targetYaw);
				targetPitch = Mth.lerp(ease, cam.getXRot(), targetPitch);
			}

			this.setPosition(camX, camY, camZ);
			this.setRotation(targetYaw, targetPitch);
			return;
		}

		float focusYOffset = Mth.lerp(nodeFocus, 1.5F, 0.0F);

		Vec3 focusStart = MeditationStateManager.getPlayerHoverPos().add(0, 1.0, 0);
		Vec3 focusEnd = MeditationStateManager.getMeditationCenterPos().add(0, focusYOffset, 0);

		Vec3 focus = new Vec3(Mth.lerp(revealProgress, focusStart.x, focusEnd.x),
				Mth.lerp(revealProgress, focusStart.y, focusEnd.y), Mth.lerp(revealProgress, focusStart.z, focusEnd.z));

		float targetDistance = Mth.lerp(revealProgress, 5.5F, -1.0F) + Mth.lerp(nodeFocus, 0.0F, -5.0F);

		float targetPitch = Mth.lerp(revealProgress, 20.0F, -5.0F) + Mth.lerp(nodeFocus, 0.0F, -12.0F);

		float startYaw = MeditationStateManager.getLockedYaw() - 40.0F;
		float endYaw = MeditationStateManager.getLockedYaw();
		float targetYaw = Mth.lerp(revealProgress, startYaw, endYaw);

		float pitchRad = targetPitch * Mth.DEG_TO_RAD;
		float yawRad = targetYaw * Mth.DEG_TO_RAD;

		double lookX = -Mth.sin(yawRad) * Mth.cos(pitchRad);
		double lookY = -Mth.sin(pitchRad);
		double lookZ = Mth.cos(yawRad) * Mth.cos(pitchRad);

		double camX = focus.x - lookX * targetDistance;
		double camY = focus.y - lookY * targetDistance;
		double camZ = focus.z - lookZ * targetDistance;

		ClipContext context = new ClipContext(focus, new Vec3(camX, camY, camZ), ClipContext.Block.VISUAL,
				ClipContext.Fluid.NONE, mc.player);
		BlockHitResult hit = mc.level.clip(context);
		if (hit.getType() != HitResult.Type.MISS) {
			float safeDistance = (float) hit.getLocation().distanceTo(focus) - 0.2F;
			camX = focus.x - lookX * safeDistance;
			camY = focus.y - lookY * safeDistance;
			camZ = focus.z - lookZ * safeDistance;
		}

		Camera cam = (Camera) (Object) this;

		if (enterProgress < 1.0F) {
			float ease = (float) (1.0 - Math.pow(1.0 - enterProgress, 3));
			camX = Mth.lerp(ease, cam.getPosition().x, camX);
			camY = Mth.lerp(ease, cam.getPosition().y, camY);
			camZ = Mth.lerp(ease, cam.getPosition().z, camZ);
			targetYaw = Mth.lerp(ease, cam.getYRot(), targetYaw);
			targetPitch = Mth.lerp(ease, cam.getXRot(), targetPitch);
		}

		this.setPosition(camX, camY, camZ);
		this.setRotation(targetYaw, targetPitch);
	}
}