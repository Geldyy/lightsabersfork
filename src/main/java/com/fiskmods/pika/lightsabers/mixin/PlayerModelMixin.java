package com.fiskmods.pika.lightsabers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fiskmods.pika.lightsabers.client.logic.MeditationClientTracker;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Mixin(PlayerModel.class)
public class PlayerModelMixin {

	@Inject(method = "setupAnim", at = @At("TAIL"))
	private void onSetupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (entity instanceof Player player && MeditationClientTracker.isPlayerMeditating(player)) {
			PlayerModel<?> model = (PlayerModel<?>) (Object) this;

			model.rightLeg.xRot = -1.41f;
			model.rightLeg.yRot = 0.35f;
			model.rightLeg.zRot = 0.1f;

			model.leftLeg.xRot = -1.41f;
			model.leftLeg.yRot = -0.35f;
			model.leftLeg.zRot = -0.1f;

			model.rightArm.xRot = -0.75f;
			model.rightArm.yRot = -0.2f;
			model.rightArm.zRot = 0.1f;

			model.leftArm.xRot = -0.75f;
			model.leftArm.yRot = 0.2f;
			model.leftArm.zRot = -0.1f;
		}
	}
}