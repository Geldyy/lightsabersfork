package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ChargedJumpPayload(float chargeRatio) implements CustomPacketPayload {

	public static final Type<ChargedJumpPayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "charged_jump"));

	public static final StreamCodec<ByteBuf, ChargedJumpPayload> STREAM_CODEC = StreamCodec
			.composite(ByteBufCodecs.FLOAT, ChargedJumpPayload::chargeRatio, ChargedJumpPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(final ChargedJumpPayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				if (player.isSpectator())
					return;

				PlayerForceData data = PlayerForceData.get(player);
				ResourceLocation jumpPower = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "force_jump");
				if (data == null || !data.isForceSensitive() || !data.hasSkill(jumpPower))
					return;

				float charge = Mth.clamp(payload.chargeRatio(), 0.15F, 1.0F);
				int level = data.getPowerLevel(jumpPower);
				float maxBlocks = (level >= 3) ? 60.0F : ((level >= 2) ? 40.0F : 20.0F);
				float blocksJumped = charge * maxBlocks;
				float cost = (level >= 3) ? (blocksJumped / 10.0F * 10.0F)
						: ((level >= 2) ? (blocksJumped / 6.0F * 7.0F) : (blocksJumped / 4.0F * 5.0F));
				cost = Math.max(5.0F, cost);

				if (data.consumeForceEnergy(cost)) {
					player.fallDistance = 0.0F;
					player.getPersistentData().putBoolean("ForceJumpNoFall", true);

					double maxYVel = (level >= 3) ? 3.15D : ((level >= 2) ? 2.55D : 1.8D);
					double yVel = 0.85D + (charge * (maxYVel - 0.85D));
					Vec3 look = player.getLookAngle();
					double forward = player.isSprinting() ? 0.7D : 0.4D;
					player.setDeltaMovement(look.x * forward * charge, yVel, look.z * forward * charge);
					player.hurtMarked = true;

					player.connection.send(new ClientboundSetEntityMotionPacket(player));

					ServerLevel sl = player.serverLevel();
					sl.playSound(null, player.getX(), player.getY(), player.getZ(),
							SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 1.2F, 0.7F + (charge * 0.5F));
					sl.playSound(null, player.getX(), player.getY(), player.getZ(),
							SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.5F);

					int count = Math.round(15 + charge * 25);
					sl.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(), count,
							0.4D, 0.1D, 0.4D, 0.1D);
					sl.sendParticles(ParticleTypes.POOF, player.getX(), player.getY(), player.getZ(), 10,
							0.3D, 0.1D, 0.3D, 0.05D);

					data.recordPowerUse(jumpPower, player);
					data.sync(player);
				}
			}
		});
	}
}
