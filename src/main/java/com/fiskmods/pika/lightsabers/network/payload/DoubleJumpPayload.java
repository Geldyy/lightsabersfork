package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DoubleJumpPayload() implements CustomPacketPayload {

	public static final Type<DoubleJumpPayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "double_jump"));

	public static final StreamCodec<ByteBuf, DoubleJumpPayload> STREAM_CODEC = StreamCodec
			.unit(new DoubleJumpPayload());

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(final DoubleJumpPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				if (player.isCreative() || player.isSpectator())
					return;

				PlayerForceData data = PlayerForceData.get(player);
				if (data != null && data.isForceSensitive() && data.consumeForceEnergy(10.0f)) {
					player.fallDistance = 0.0F;
					player.getPersistentData().putBoolean("ForceJumpNoFall", true);

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
					player.hurtMarked = true;

					ServerLevel world = player.serverLevel();
					Vec3 pos = player.position();
					world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BREEZE_JUMP, SoundSource.PLAYERS, 0.8F,
							1.3F);
					world.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 0.1D, pos.z, 6, 0.25D, 0.05D, 0.25D, 0.03D);

					data.sync(player);
				}
			}
		});
	}
}