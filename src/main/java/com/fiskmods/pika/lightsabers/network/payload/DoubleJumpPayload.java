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
            ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "double_jump")
    );

    public static final StreamCodec<ByteBuf, DoubleJumpPayload> STREAM_CODEC = StreamCodec.unit(new DoubleJumpPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final DoubleJumpPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                
                // STRIKT: Kein Doppelsprung im Creative oder Spectator!
                if (player.isCreative() || player.isSpectator()) {
                    return;
                }

                PlayerForceData data = PlayerForceData.get(player);
                
                // Sicherheit-Check & Energieverbrauch
                if (data != null && data.isForceSensitive() && data.consumeForceEnergy(10.0f)) {
                    
                    player.fallDistance = 0.0F; 
                    // NBT Tag für den CombatEvent (Fallschaden annulieren)
                    player.getPersistentData().putBoolean("ForceJumpNoFall", true);

                    // Sicherheits-Sync für den Server
                    Vec3 motion = player.getDeltaMovement();
                    player.setDeltaMovement(motion.x, 0.65D, motion.z);
                    player.hurtMarked = true;

                    ServerLevel world = player.serverLevel();
                    Vec3 pos = player.position();

                    world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BREEZE_JUMP, SoundSource.PLAYERS, 0.8F, 1.3F);
                    world.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 0.1D, pos.z, 6, 0.25D, 0.05D, 0.25D, 0.03D);
                    
                    data.sync(player);
                }
            }
        });
    }
}