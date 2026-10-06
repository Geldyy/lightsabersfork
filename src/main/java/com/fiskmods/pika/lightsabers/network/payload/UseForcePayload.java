package com.fiskmods.pika.lightsabers.network.payload;

import java.util.List;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UseForcePayload(boolean isKeyDown) implements CustomPacketPayload {
    public static final Type<UseForcePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "use_force"));
    
    public static final StreamCodec<ByteBuf, UseForcePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, UseForcePayload::isKeyDown,
            UseForcePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(UseForcePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                PlayerForceData data = PlayerForceData.get(player);
                if (data != null && data.isForceSensitive() && data.getSelectedPower() != null) {
                    
                    String power = data.getSelectedPower().getPath();
                    ServerLevel world = player.serverLevel();
                    
                    if (payload.isKeyDown()) {
                        // ==========================================
                        // 1. MACHT-TELEKINESE (Blöcke greifen & werfen)
                        // ==========================================
                        if (power.equals("force_telekinesis")) {
                            String storedBlock = player.getPersistentData().getString("TK_Block");
                            
                            // A) Block greifen
                            if (storedBlock.isEmpty() && data.consumeForceEnergy(15.0f)) {
                                Vec3 start = player.getEyePosition();
                                Vec3 look = player.getLookAngle();
                                double reach = data.hasSkill("lightsabersfork", "force_telekinesis_3") ? 16.0 : (data.hasSkill("lightsabersfork", "force_telekinesis_2") ? 10.0 : 5.0);
                                Vec3 end = start.add(look.x * reach, look.y * reach, look.z * reach);
                                
                                BlockHitResult hit = world.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
                                
                                if (hit.getType() == HitResult.Type.BLOCK) {
                                    BlockPos pos = hit.getBlockPos();
                                    BlockState state = world.getBlockState(pos);
                                    
                                    player.getPersistentData().putString("TK_Block", BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString());
                                    world.destroyBlock(pos, false);
                                    world.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 0.5f);
                                }
                            } 
                            // B) Block werfen
                            else if (!storedBlock.isEmpty()) {
                                Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(storedBlock));
                                BlockPos spawnPos = player.blockPosition().above();
                                BlockState state = block.defaultBlockState();
                                
                                // Korrekter Aufruf mit (Level, BlockPos, BlockState) für 1.21.1
                                FallingBlockEntity falling = FallingBlockEntity.fall(world, spawnPos, state);
                                if (falling != null) {
                                    Vec3 look = player.getLookAngle();
                                    double force = data.hasSkill("lightsabersfork", "force_telekinesis_3") ? 3.5 : 2.0;
                                    
                                    falling.setPos(player.getX(), player.getEyeY() + 0.2, player.getZ());
                                    falling.setDeltaMovement(look.scale(force));
                                    falling.hurtMarked = true;
                                }
                                
                                player.getPersistentData().putString("TK_Block", "");
                                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WIND_CHARGE_THROW, SoundSource.PLAYERS, 1.0f, 1.0f);
                            }
                            data.sync(player);
                        }
                        // ==========================================
                        // 2. MACHT-SPRINT (Dash)
                        // ==========================================
                        else if (power.equals("force_dash")) {
                            if (data.consumeForceEnergy(15.0f)) {
                                Vec3 look = player.getLookAngle();
                                player.setDeltaMovement(look.x * 2.5, look.y * 1.0, look.z * 2.5);
                                player.hurtMarked = true;
                                player.fallDistance = 0.0f;
                                player.getPersistentData().putBoolean("ForceJumpNoFall", true); 
                                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 2.0F);
                                world.sendParticles(ParticleTypes.POOF, player.getX(), player.getY() + 1.0, player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                                data.sync(player);
                            }
                        }
                        // ==========================================
                        // 3. MACHT-STOSS (Push) - MIT 3 STUFEN
                        // ==========================================
                        else if (power.equals("force_push")) {
                            if (data.consumeForceEnergy(20.0f)) {
                                boolean t2 = data.hasSkill("lightsabersfork", "force_push_2");
                                boolean t3 = data.hasSkill("lightsabersfork", "force_push_3");
                                
                                double radius = t2 ? 12.0D : 8.0D;
                                double strength = t3 ? 4.0 : (t2 ? 3.0 : 2.5);
                                
                                Vec3 look = player.getLookAngle();
                                AABB box = player.getBoundingBox().inflate(radius);
                                List<Entity> entities = world.getEntities(player, box);
                                
                                for (Entity e : entities) {
                                    Vec3 dir = e.position().subtract(player.position()).normalize();
                                    if (look.dot(dir) > 0.4) {
                                        if (e instanceof Projectile proj && t2) {
                                            proj.setDeltaMovement(proj.getDeltaMovement().scale(-2.0));
                                            proj.hurtMarked = true;
                                        } else if (e instanceof LivingEntity) {
                                            e.setDeltaMovement(dir.x * strength, 0.6, dir.z * strength);
                                            e.hurtMarked = true;
                                            if (t3) e.hurt(world.damageSources().magic(), 4.0f);
                                        }
                                    }
                                }
                                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 1.0F, 0.5F);
                                world.sendParticles(ParticleTypes.CLOUD, player.getX() + look.x, player.getEyeY(), player.getZ() + look.z, 30, 1.0, 1.0, 1.0, 0.2);
                                data.sync(player);
                            }
                        }
                        // ==========================================
                        // 4. MACHT-SOG (Pull) - MIT 3 STUFEN
                        // ==========================================
                        else if (power.equals("force_pull")) {
                            if (data.consumeForceEnergy(20.0f)) {
                                boolean t2 = data.hasSkill("lightsabersfork", "force_pull_2");
                                boolean t3 = data.hasSkill("lightsabersfork", "force_pull_3");
                                
                                Vec3 look = player.getLookAngle();
                                AABB box = player.getBoundingBox().inflate(t2 ? 15.0D : 8.0D);
                                List<Entity> entities = world.getEntities(player, box);
                                
                                for (Entity e : entities) {
                                    Vec3 dir = player.position().subtract(e.position()).normalize();
                                    if (look.dot(e.position().subtract(player.position()).normalize()) > 0.4) {
                                        if (e instanceof ItemEntity item && t3) {
                                            item.setPos(player.getX(), player.getY(), player.getZ());
                                        } else if (e instanceof LivingEntity) {
                                            e.setDeltaMovement(dir.x * 2.0, 0.3, dir.z * 2.0);
                                            e.hurtMarked = true;
                                            ((LivingEntity)e).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 4));
                                        }
                                    }
                                }
                                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_PREPARE_BLINDNESS, SoundSource.PLAYERS, 1.0F, 1.5F);
                                data.sync(player);
                            }
                        }
                        // ==========================================
                        // 5. MACHT-HEILUNG (Heal) - MIT 3 STUFEN
                        // ==========================================
                        else if (power.equals("force_heal")) {
                            if (data.consumeForceEnergy(35.0f)) {
                                boolean t2 = data.hasSkill("lightsabersfork", "force_heal_2");
                                boolean t3 = data.hasSkill("lightsabersfork", "force_heal_3");
                                
                                float healAmount = t3 ? 16.0f : (t2 ? 12.0f : 8.0f);
                                player.heal(healAmount); 
                                
                                if (t2) player.removeAllEffects();
                                
                                if (t3) {
                                    List<LivingEntity> allies = world.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(8.0D));
                                    for (LivingEntity ally : allies) {
                                        if (ally != player && (ally instanceof Player || ally.isAlliedTo(player))) {
                                            ally.heal(8.0f);
                                            world.sendParticles(ParticleTypes.HAPPY_VILLAGER, ally.getX(), ally.getY() + 1.0, ally.getZ(), 10, 0.5, 0.5, 0.5, 0.1);
                                        }
                                    }
                                }
                                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                                world.sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.0, player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                                data.sync(player);
                            }
                        }
                        else if (power.equals("force_repulse")) {
                            if (data.consumeForceEnergy(40.0f)) {
                                AABB box = player.getBoundingBox().inflate(6.0D);
                                List<LivingEntity> targets = world.getEntitiesOfClass(LivingEntity.class, box);
                                for (LivingEntity target : targets) {
                                    if (target != player) {
                                        Vec3 dir = target.position().subtract(player.position()).normalize();
                                        target.setDeltaMovement(dir.x * 2.0, 0.8, dir.z * 2.0);
                                        target.hurtMarked = true;
                                        target.hurt(world.damageSources().magic(), 6.0f);
                                    }
                                }
                                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                                world.sendParticles(ParticleTypes.SONIC_BOOM, player.getX(), player.getY() + 1.0, player.getZ(), 1, 0, 0, 0, 0);
                                data.sync(player);
                            }
                        }
                        else {
                            // KANALISIERTE FÄHIGKEITEN STARTEN (Lightning, Choke, Fortify)
                            player.getPersistentData().putString("ForceChanneling", power);
                        }
                    } else {
                        // Kanalisierung beenden
                        player.getPersistentData().putString("ForceChanneling", "");
                    }
                } else if (!payload.isKeyDown()) {
                    player.getPersistentData().putString("ForceChanneling", "");
                }
            }
        });
    }
}