package com.fiskmods.pika.lightsabers.events;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALSounds;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID)
public class LightsaberCombatEvents {

    private static final ResourceLocation RANGE_MOD_ID = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "ls_range");
    private static final ResourceLocation DMG_MOD_ID = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "ls_damage");
    private static final ResourceLocation SPEED_MOD_ID = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "ls_speed");
    private static final ResourceLocation KB_MOD_ID = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "ls_knockback");

    private static boolean hasSkill(PlayerForceData data, String skillName) {
        if (data == null) return false;
        return data.hasSkill(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, skillName));
    }

    // --- HELPER FÜR MODUS-TRENUNG ---
    public static float getSbMult(ItemStack stack) {
        LightsaberData lsData = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
        boolean isDouble = lsData != null && lsData.isDoubleBladed();
        boolean isSingleMode = ItemLightsaber.isSingleBladeMode(stack);
        if (!isDouble) return 1.0f; 
        if (isSingleMode) return 0.5f; 
        return 0.0f; 
    }

    public static float getDbMult(ItemStack stack) {
        LightsaberData lsData = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
        boolean isDouble = lsData != null && lsData.isDoubleBladed();
        boolean isSingleMode = ItemLightsaber.isSingleBladeMode(stack);
        if (isDouble && !isSingleMode) return 1.0f; 
        return 0.0f; 
    }

    // Kombiniert primäre und (falls vorhanden) sekundäre Kristalle für die Auswertung
    private static List<Item> getAllFocusCrystals(LightsaberData data) {
        List<Item> allCrystals = new ArrayList<>();
        if (data.focusCrystals() != null) allCrystals.addAll(data.focusCrystals());
        if (data.isDoubleBladed() && data.secFocusCrystals() != null) allCrystals.addAll(data.secFocusCrystals());
        return allCrystals;
    }

    // ==============================================================
    // KANALISIERTE MACHT-FÄHIGKEITEN (Blitze, Würgen, Drain)
    // ==============================================================
    private static void handleChanneledPowers(Player player, PlayerForceData data) {
        String channeledPower = player.getPersistentData().getString("ForceChanneling");
        if (channeledPower == null || channeledPower.isEmpty()) return;

        ServerLevel world = (ServerLevel) player.level();
        Vec3 look = player.getLookAngle();
        AABB box = player.getBoundingBox().inflate(10.0D);
        List<LivingEntity> targets = world.getEntitiesOfClass(LivingEntity.class, box);

        switch (channeledPower) {
            case "force_lightning":
                if (!data.consumeForceEnergy(0.3f)) { // Kostet ca. 6 Energie pro Sekunde
                    player.getPersistentData().putString("ForceChanneling", ""); return;
                }
                
                // Sound und Partikel
                if (player.tickCount % 5 == 0) {
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.3F, 2.0F);
                }
                
                for (LivingEntity target : targets) {
                    if (target != player && look.dot(target.position().subtract(player.position()).normalize()) > 0.6) {
                        // Partikel-Kette zum Ziel
                        world.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + 1, target.getZ(), 2, 0.2, 0.2, 0.2, 0.05);
                        
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 2));
                        
                        if (player.tickCount % 10 == 0) { // Schaden nur alle halbe Sekunde
                            target.hurt(world.damageSources().lightningBolt(), 2.0f);
                            
                            // KORRUPTIONS-MECHANIK (Villager -> Zombie -> Skelett)
                            if (target.getType() == net.minecraft.world.entity.EntityType.VILLAGER) {
                                if (world.random.nextFloat() < 0.2f) { // 20% Chance pro Hit
                                    ((Mob) target).convertTo(net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER, true);
                                }
                            } else if (target.getType() == net.minecraft.world.entity.EntityType.ZOMBIE || target.getType() == net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER) {
                                if (world.random.nextFloat() < 0.1f) { 
                                    ((Mob) target).convertTo(net.minecraft.world.entity.EntityType.SKELETON, true);
                                }
                            }
                        }
                    }
                }
                break;
                
            case "electric_judgment":
                if (!data.consumeForceEnergy(0.3f)) {
                    player.getPersistentData().putString("ForceChanneling", ""); return;
                }
                
                for (LivingEntity target : targets) {
                    if (target != player && look.dot(target.position().subtract(player.position()).normalize()) > 0.7) {
                        // Gelbe Funken
                        world.sendParticles(ParticleTypes.WAX_ON, target.getX(), target.getY() + 1, target.getZ(), 5, 0.2, 0.2, 0.2, 0.05);
                        
                        // Betäubung (Slowness 255 = Bewegungsunfähig)
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 255, false, false));
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10, 255, false, false));
                        
                        if (player.tickCount % 10 == 0) {
                            // LÄUTERUNG
                            if (target.getType() == net.minecraft.world.entity.EntityType.ZOMBIE || target.getType() == net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER) {
                                if (world.random.nextFloat() < 0.15f) {
                                    ((Mob) target).convertTo(net.minecraft.world.entity.EntityType.VILLAGER, true);
                                }
                            }
                            // Schaden NUR an Sith-Spielern
                            if (target instanceof Player targetPlayer) {
                                PlayerForceData targetData = PlayerForceData.get(targetPlayer);
                                if (targetData != null && targetData.hasSkill("lightsabersfork", "sith_warrior")) {
                                    target.hurt(world.damageSources().magic(), 4.0f);
                                }
                            }
                        }
                    }
                }
                break;

            case "force_choke":
                if (!data.consumeForceEnergy(0.4f)) {
                    player.getPersistentData().putString("ForceChanneling", ""); return;
                }
                
                for (LivingEntity target : targets) {
                    // Strengerer Kegel für Würgegriff
                    if (target != player && look.dot(target.position().subtract(player.position()).normalize()) > 0.85) {
                        // Hebt das Ziel an
                        target.setDeltaMovement(0, 0.05, 0);
                        target.hurtMarked = true;
                        
                        world.sendParticles(ParticleTypes.SMOKE, target.getX(), target.getEyeY(), target.getZ(), 2, 0.1, 0.1, 0.1, 0.0);
                        
                        if (player.tickCount % 10 == 0) {
                            // Rüstungsdurchdringender Erstickungsschaden
                            target.hurt(world.damageSources().drown(), 2.0f); 
                            world.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_BREATH, SoundSource.PLAYERS, 1.0F, 0.5F);
                        }
                        break; // Würgen bricht nach dem ersten gefundenen Ziel ab (Fokus)
                    }
                }
                break;

            case "force_drain":
                if (!data.consumeForceEnergy(0.1f)) { // Kostet kaum was, weil man ja drainen soll!
                    player.getPersistentData().putString("ForceChanneling", ""); return;
                }
                
                for (LivingEntity target : targets) {
                    if (target != player && look.dot(target.position().subtract(player.position()).normalize()) > 0.8) {
                        
                        // Rote Vampir-Partikel
                        world.sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY() + 1, target.getZ(), 2, 0.2, 0.2, 0.2, 0.1);
                        world.sendParticles(ParticleTypes.HEART, player.getX(), player.getY() + 1, player.getZ(), 1, 0.2, 0.2, 0.2, 0.1);

                        if (player.tickCount % 10 == 0) {
                            target.hurt(world.damageSources().magic(), 1.0f);
                            player.heal(1.0f); // HP stehlen
                            
                            // Macht stehlen, wenn Ziel ein Spieler ist
                            if (target instanceof Player targetPlayer) {
                                PlayerForceData targetData = PlayerForceData.get(targetPlayer);
                                if (targetData != null && targetData.consumeForceEnergy(5.0f)) {
                                    data.addForceEnergy(5.0f);
                                    targetData.sync((ServerPlayer) targetPlayer);
                                }
                            }
                        }
                        break; // Auch hier Fokus auf 1 Ziel
                    }
                }
                break;
        }
        
        // Regelmäßiger Sync, wenn man kanalisiert, damit der Balken smooth abnimmt
        if (player.tickCount % 5 == 0) {
            data.sync((ServerPlayer) player);
        }
    }

    // ==========================================
    // 0. PASSIVE STATS & ENERGIE REGENERATION
    // ==========================================
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        PlayerForceData data = PlayerForceData.get(player);
        
        // NEU: Konstante Energie-Regeneration. (Wird Client & Server parallel ausgeführt, deshalb super weich!)
        if (data != null && data.isForceSensitive()) {
            data.regenerateEnergy();
            
            // NEU: HIER WIRD DIE KANALISIERUNG AUSGEFÜHRT (Nur auf dem Server)
            if (!player.level().isClientSide) {
                handleChanneledPowers(player, data);
            }
        }
        
        if (player.tickCount % 2 != 0) return;

        ItemStack handItem = player.getMainHandItem();
        AttributeInstance rangeAttr = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
        AttributeInstance dmgAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance speedAttr = player.getAttribute(Attributes.ATTACK_SPEED);
        AttributeInstance kbAttr = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);

        if (rangeAttr == null || dmgAttr == null || speedAttr == null || kbAttr == null) return;

        if (handItem.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(handItem)) {
            if (data == null) return;

            float sbMult = getSbMult(handItem);
            float dbMult = getDbMult(handItem); 
            
            double bonusRange = 0.0;
            double bonusDamage = 0.0;
            double bonusSpeed = 0.0;
            double bonusKbRes = 0.0;

            // --- CORE SKILLS (ls_ Mitte) IMMER 100% AKTIV ---
            if (hasSkill(data, "ls_kyber_resonance")) bonusDamage += 1.0;
            if (hasSkill(data, "ls_lethal_precision")) bonusDamage += 3.0;

            // --- SINGLE-BLADE SKILLS ---
            if (sbMult > 0) {
                if (hasSkill(data, "sb_better_form")) { bonusRange += (1.75 * sbMult); bonusKbRes += (0.15 * sbMult); }
                if (hasSkill(data, "sb_adv_form")) { bonusRange += (0.5 * sbMult); bonusDamage += (1.0 * sbMult); bonusKbRes += (0.15 * sbMult); }
                if (hasSkill(data, "sb_adv_speed")) bonusSpeed += (1.4 * sbMult); 
                if (hasSkill(data, "sb_ult_speed")) bonusSpeed += (100.0 * sbMult); 
            }

            // --- DOUBLE-BLADE SKILLS ---
            if (dbMult > 0) {
                if (hasSkill(data, "db_better_form")) { bonusRange += (1.75 * dbMult); bonusKbRes += (0.15 * dbMult); }
                if (hasSkill(data, "db_adv_form")) { bonusRange += (0.5 * dbMult); bonusDamage += (1.0 * dbMult); bonusKbRes += (0.15 * dbMult); }
            }

            if (hasSkill(data, "ls_focusing")) {
                LightsaberData lsData = handItem.get(ALDataComponents.LIGHTSABER_DATA.get());
                if (lsData != null) {
                    for (Item crystalItem : getAllFocusCrystals(lsData)) {
                        String crystalStr = BuiltInRegistries.ITEM.getKey(crystalItem).getPath().toLowerCase(); 
                        
                        if (crystalStr.contains("compressed") || crystalStr.contains("fine_cut")) bonusDamage += 1.0;
                        else if (crystalStr.contains("cracked") || crystalStr.contains("shoto")) bonusDamage += 2.0;
                        else if (crystalStr.contains("extended")) bonusDamage -= 2.0;
                        else if (crystalStr.contains("unstable") || crystalStr.contains("pulse")) bonusDamage += 4.0;

                        if (crystalStr.contains("shoto")) bonusRange -= 2.0;
                        if (crystalStr.contains("extended")) bonusRange += 2.0;
                    }
                }
            }

            applyModifier(rangeAttr, RANGE_MOD_ID, bonusRange, AttributeModifier.Operation.ADD_VALUE);
            applyModifier(dmgAttr, DMG_MOD_ID, bonusDamage, AttributeModifier.Operation.ADD_VALUE);
            applyModifier(speedAttr, SPEED_MOD_ID, bonusSpeed, AttributeModifier.Operation.ADD_VALUE);
            applyModifier(kbAttr, KB_MOD_ID, bonusKbRes, AttributeModifier.Operation.ADD_VALUE);

        } else {
            rangeAttr.removeModifier(RANGE_MOD_ID);
            dmgAttr.removeModifier(DMG_MOD_ID);
            speedAttr.removeModifier(SPEED_MOD_ID);
            kbAttr.removeModifier(KB_MOD_ID);
        }
    }

    private static void applyModifier(AttributeInstance instance, ResourceLocation id, double value, AttributeModifier.Operation op) {
        instance.removeModifier(id);
        if (value != 0) instance.addTransientModifier(new AttributeModifier(id, value, op));
    }

    // ==========================================
    // 1. ZIEL-SCHADEN & BLOCKEN 
    // ==========================================
    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();

        if (victim instanceof Player defender && defender.isUsingItem()) {
            ItemStack useItem = defender.getUseItem();
            if (useItem.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(useItem)) {
                LightsaberData lsData = useItem.get(ALDataComponents.LIGHTSABER_DATA.get());
                PlayerForceData forceData = PlayerForceData.get(defender);
                
                if (lsData != null && forceData != null) {
                    float projBlockChance = 0.0f;
                    float meleeBlockChance = 0.0f;

                    if (hasSkill(forceData, "ls_block")) { projBlockChance = 0.35f; meleeBlockChance = 0.50f; }
                    if (hasSkill(forceData, "ls_better_block")) { projBlockChance = 0.50f; meleeBlockChance = 0.70f; }
                    if (hasSkill(forceData, "ls_adv_block")) { projBlockChance = 0.70f; meleeBlockChance = 0.80f; }

                    boolean isEffectivelyDouble = lsData.isDoubleBladed() && !ItemLightsaber.isSingleBladeMode(useItem);
                    if (isEffectivelyDouble) {
                        projBlockChance += 0.20f; 
                        if (hasSkill(forceData, "db_ult_block")) { projBlockChance = 1.0f; meleeBlockChance = 0.90f; }
                    }

                    if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
                        if (projBlockChance > 0 && Math.random() <= projBlockChance) {
                            event.setCanceled(true);
                            defender.level().playSound(null, defender.blockPosition(), ALSounds.LIGHTSABER_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.4F + (float)(Math.random() * 0.2F));
                            if (defender.level() instanceof ServerLevel sl) sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, defender.getX(), defender.getEyeY(), defender.getZ(), 12, 0.2, 0.2, 0.2, 0.1);
                            if (event.getSource().getDirectEntity() instanceof Projectile projectile) projectile.discard();
                        }
                    } else if (event.getSource().getEntity() instanceof LivingEntity attacker) {
                        if (meleeBlockChance > 0 && Math.random() <= meleeBlockChance) {
                            event.setCanceled(true);
                            defender.level().playSound(null, defender.blockPosition(), ALSounds.LIGHTSABER_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.2F + (float)(Math.random() * 0.2F));
                            ItemStack attackerWeapon = attacker.getMainHandItem();
                            if (attackerWeapon.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(attackerWeapon)) {
                                if (defender.level() instanceof ServerLevel sl) sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, defender.getX(), defender.getEyeY(), defender.getZ(), 16, 0.2, 0.2, 0.2, 0.1);
                            } else if (!attackerWeapon.isEmpty() && attackerWeapon.isDamageableItem()) {
                                attackerWeapon.hurtAndBreak(25, attacker, EquipmentSlot.MAINHAND);
                                if (defender.level() instanceof ServerLevel sl) sl.sendParticles(ParticleTypes.LAVA, attacker.getX(), attacker.getEyeY(), attacker.getZ(), 6, 0.2, 0.2, 0.2, 0.1);
                            }
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // 2. SWEEPING & KRISTALL-EFFEKTE (AttackEntityEvent)
    // ==========================================
    
    // --- HILFSMETHODEN FÜR PHASE 6 (SWEEPING IN DER LUFT) ---
    public static double getSweepRadius(PlayerForceData forceData, ItemStack stack, boolean isEffectivelyDouble) {
        double inflateRadius = isEffectivelyDouble ? 3.0D : 1.5D;
        if (forceData != null) {
            float sbMult = getSbMult(stack);
            float dbMult = getDbMult(stack);
            
            float bestSbBonus = 0.0f;
            if (hasSkill(forceData, "sb_better_swerve")) bestSbBonus = 1.0F;
            if (hasSkill(forceData, "sb_adv_swerve")) bestSbBonus = 3.0F;
            
            float bestDbBonus = 0.0f;
            if (hasSkill(forceData, "db_adv_swerve")) bestDbBonus = 3.0F;

            inflateRadius += (bestSbBonus * sbMult) + (bestDbBonus * dbMult);
        }
        return inflateRadius;
    }

    public static float getSweepDamage(PlayerForceData forceData, ItemStack stack, float weaponTotalDmg, boolean isEffectivelyDouble) {
        float sweepMultiplier = 1.0F;
        if (forceData != null) {
            float sbMult = getSbMult(stack);
            float dbMult = getDbMult(stack);
            
            float bestSbBonus = 0.0f;
            if (hasSkill(forceData, "sb_better_swerve")) bestSbBonus = 0.40F; // +40%
            if (hasSkill(forceData, "sb_adv_swerve")) bestSbBonus = 0.75F; // +75%
            
            float bestDbBonus = 0.0f;
            if (hasSkill(forceData, "db_adv_swerve")) bestDbBonus = 0.75F;

            sweepMultiplier += (bestSbBonus * sbMult) + (bestDbBonus * dbMult);
        }
        float damageFactor = isEffectivelyDouble ? 0.50F : 0.80F;
        return (1.0F + weaponTotalDmg * damageFactor) * sweepMultiplier;
    }

    public static double getSweepMaxDistSqr(boolean isEffectivelyDouble) {
        return isEffectivelyDouble ? 16.0D : 6.25D; 
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof ItemLightsaber) || !ItemLightsaber.isActive(stack)) return;

        LightsaberData lsData = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
        if (lsData == null) return;

        PlayerForceData forceData = PlayerForceData.get(player);
        if (event.getTarget() instanceof LivingEntity target) {
            
            float weaponTotalDmg = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            boolean isSweep = player.getAttackStrengthScale(0.5F) > 0.9F && player.onGround() && !player.isSprinting() && player.walkDist == player.walkDistO;
            boolean isEffectivelyDouble = lsData.isDoubleBladed() && !ItemLightsaber.isSingleBladeMode(stack);

            if (isSweep) {
                // Nutze die neuen Hilfsmethoden für absolut konsistente Berechnungen
                double inflateRadius = getSweepRadius(forceData, stack, isEffectivelyDouble);
                float sweepDamage = getSweepDamage(forceData, stack, weaponTotalDmg, isEffectivelyDouble);
                double maxDistSqr = getSweepMaxDistSqr(isEffectivelyDouble);

                AABB sweepBox = target.getBoundingBox().inflate(inflateRadius, 0.5D, inflateRadius);
                List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, sweepBox);

                for (LivingEntity nearby : targets) {
                    if (nearby != player && nearby != target && !player.isAlliedTo(nearby) && !(nearby instanceof ArmorStand)) {
                        if (player.distanceToSqr(nearby) < maxDistSqr) {
                            nearby.invulnerableTime = 0;
                            nearby.knockback(0.4F, Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(player.getYRot() * ((float) Math.PI / 180F)));
                            nearby.hurt(player.damageSources().playerAttack(player), sweepDamage);
                        }
                    }
                }

                if (player.level() instanceof ServerLevel serverLevel) {
                    double px = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
                    double pz = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
                    double waveOffset = isEffectivelyDouble ? 1.5D : 1.0D;
                    serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + px * waveOffset, player.getY(0.5D), player.getZ() + pz * waveOffset, isEffectivelyDouble ? 3 : 1, 0.3, 0.1, 0.3, 0.0);
                }
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, isEffectivelyDouble ? 0.85F : 1.0F);
            }

            // --- KRISTALLE (Vollständig gefixt für Double Bladed, Undead, Armor Pierce, Pulse Chain & Unstable) ---
            if (hasSkill(forceData, "ls_focusing")) {
                boolean isUndead = target.isInvertedHealAndHarm(); // Der 1.21.1 Weg für Untote

                for (Item crystalItem : getAllFocusCrystals(lsData)) {
                    String crystalStr = BuiltInRegistries.ITEM.getKey(crystalItem).getPath().toLowerCase(); 
                    
                    if (crystalStr.contains("prismatic")) {
                        if (isUndead) { target.invulnerableTime = 0; target.hurt(player.damageSources().magic(), 4.0f); }
                    }
                    else if (crystalStr.contains("invert")) {
                        if (isUndead) doAoEDamage(player.level(), player, target, 3.0, weaponTotalDmg * 0.40f);
                    }
                    else if (crystalStr.contains("fine_cut")) {
                        target.invulnerableTime = 0; 
                        // Fine Cut: 3 Armor Pierce (Magic ignoriert Rüstung in Minecraft nativ)
                        target.hurt(player.damageSources().magic(), 3.0f);
                    }
                    else if (crystalStr.contains("unstable")) {
                        doAoEDamage(player.level(), player, target, 3.0, weaponTotalDmg * 0.35f);
                        if (player.level() instanceof ServerLevel sl) {
                            sl.sendParticles(ParticleTypes.LAVA, target.getX(), target.getY() + 1, target.getZ(), 10, 0.5, 0.5, 0.5, 0.1);
                        }
                        // Wahrscheinlichkeit auf 10% erhöht
                        if (player.getRandom().nextFloat() < 0.10f) { 
                            ItemLightsaber.setBladeActive(stack, false);
                            player.level().playSound(null, player.blockPosition(), ALSounds.LIGHTSABER_OFF.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (player.level() instanceof ServerLevel sl) sl.sendParticles(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY() + 1, player.getZ(), 15, 0.5, 0.5, 0.5, 0.05);
                            player.displayClientMessage(net.minecraft.network.chat.Component.literal("§cDein instabiles Lichtschwert hat sich überhitzt und deaktiviert!"), true);
                        }
                    }
                    else if (crystalStr.contains("pulse")) {
                        // KETTENREAKTION FIX: Sucht 5 Ziele, sortiert nach Distanz vom letzen Hit
                        List<LivingEntity> chainTargets = new ArrayList<>();
                        LivingEntity currentChainSource = target;

                        for (int bounce = 0; bounce < 5; bounce++) {
                            List<LivingEntity> possibleTargets = player.level().getEntitiesOfClass(LivingEntity.class, currentChainSource.getBoundingBox().inflate(3.0D));
                            
                            // Sortieren, um den Nächsten zu finden
                            final LivingEntity finalSource = currentChainSource;
                            possibleTargets.sort(Comparator.comparingDouble(e -> e.distanceToSqr(finalSource)));

                            boolean foundNext = false;
                            for (LivingEntity possible : possibleTargets) {
                                if (possible != player && possible != target && !chainTargets.contains(possible)) {
                                    chainTargets.add(possible);
                                    currentChainSource = possible; // Der nächste Blitz springt von diesem Ziel weiter!
                                    foundNext = true;
                                    
                                    possible.invulnerableTime = 0;
                                    possible.hurt(player.damageSources().lightningBolt(), weaponTotalDmg * 0.35f);
                                    if (player.level() instanceof ServerLevel sl) {
                                        sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, possible.getX(), possible.getY() + 1, possible.getZ(), 5, 0.2, 0.2, 0.2, 0.1);
                                    }
                                    break; // Nur einen pro Bounce hinzufügen
                                }
                            }
                            if (!foundNext) break; // Keine weiteren Ziele in Reichweite
                        }

                        if (player.getRandom().nextFloat() < 0.15f) { 
                            player.invulnerableTime = 0;
                            player.hurt(player.damageSources().magic(), weaponTotalDmg * 0.50f);
                        }
                    }
                }
            }
        }
    }

    private static void doAoEDamage(net.minecraft.world.level.Level level, Player attacker, LivingEntity center, double radius, float damage) {
        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, center.getBoundingBox().inflate(radius));
        for (LivingEntity aoeTarget : nearby) {
            if (aoeTarget != attacker && aoeTarget != center) {
                aoeTarget.invulnerableTime = 0; 
                aoeTarget.hurt(attacker.damageSources().playerAttack(attacker), damage);
            }
        }
    }

    // ==========================================
    // 3. SCHILDE SCHNELL ZERSTÖREN
    // ==========================================
    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        if (event.getDamageSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();

            if (weapon.getItem() instanceof ItemLightsaber && ItemLightsaber.isActive(weapon)) {
                ItemStack shield = event.getEntity().getUseItem();

                if (!shield.isEmpty() && shield.isDamageableItem()) {
                    EquipmentSlot slot = event.getEntity().getUsedItemHand() == net.minecraft.world.InteractionHand.MAIN_HAND 
                            ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

                    float baseDamage = event.getBlockedDamage();
                    int extraDamage = Math.max(5, (int) (baseDamage * 4.0F));

                    shield.hurtAndBreak(extraDamage, event.getEntity(), slot);

                    event.getEntity().level().playSound(null, event.getEntity().blockPosition(),
                            ALSounds.LIGHTSABER_HIT.get(), SoundSource.PLAYERS, 1.0F, 0.8F);
                }
            }
        }
    }

    // ==========================================
    // 4. SKILL-PUNKTE BEIM KILLEN FARMEN & SYNC
    // ==========================================
    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            if (player.getMainHandItem().getItem() instanceof ItemLightsaber) {
                PlayerForceData data = PlayerForceData.get(player);
                if (data != null) {
                    boolean leveledUp = data.addLightsaberKill();
                    if (leveledUp) {
                        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                    data.sync(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerForceData data = PlayerForceData.get(player);
            if (data != null) data.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerForceData data = PlayerForceData.get(player);
            if (data != null) data.sync(player);
        }
    }

    // ==========================================
    // 5. MACHT-SPRUNG / DASH FALLSCHADEN BLOCKEN
    // ==========================================
    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            // Hat der Spieler gerade einen Macht-Sprung oder Dash gemacht?
            if (player.getPersistentData().getBoolean("ForceJumpNoFall")) {
                // Setze Distanz auf 0, brich Event ab und entferne den Schutz (gilt nur 1x pro Sprung)
                event.setDistance(0.0F);
                event.setCanceled(true);
                player.getPersistentData().putBoolean("ForceJumpNoFall", false);
            }
        }
    }
}