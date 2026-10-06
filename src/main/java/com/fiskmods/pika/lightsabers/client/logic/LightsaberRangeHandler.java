package com.fiskmods.pika.lightsabers.client.logic;

import com.fiskmods.pika.lightsabers.ALConfig;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.util.ServerRangeStates;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LightsaberRangeHandler {

    private static final ResourceLocation RANGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "lightsaber_range_boost");

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player == null || player.level().isClientSide()) return;

        double boostPercent = ALConfig.RANGE_BOOST_PERCENTAGE.get();
        boolean serverAllowed = !ServerRangeStates.isDisabled(player);

        boolean hasLightsaberInInventory = false;
        if (boostPercent > 0.0 && serverAllowed) {
            for (ItemStack stack : player.getInventory().items) {
                if (stack.getItem() instanceof ItemLightsaber) {
                    hasLightsaberInInventory = true;
                    break;
                }
            }
        }

        AttributeInstance blockRangeAttr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
        AttributeInstance entityRangeAttr = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);

        applyOrRemoveModifier(blockRangeAttr, hasLightsaberInInventory, boostPercent);
        applyOrRemoveModifier(entityRangeAttr, hasLightsaberInInventory, boostPercent);
    }

    private static void applyOrRemoveModifier(AttributeInstance attribute, boolean apply, double percent) {
        if (attribute == null) return;

        // Prüfen, ob der Modifikator aktuell bereits auf dem Attribut liegt
        boolean hasModifier = attribute.getModifier(RANGE_MODIFIER_ID) != null;

        if (apply && percent > 0.0) {
            // Nur hinzufügen, wenn er noch NICHT da ist
            if (!hasModifier) {
                double modifierValue = percent / 100.0;
                attribute.addTransientModifier(new AttributeModifier(
                    RANGE_MODIFIER_ID, 
                    modifierValue, 
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ));
            }
        } else {
            // Nur entfernen, wenn er aktuell vorhanden ist
            if (hasModifier) {
                attribute.removeModifier(RANGE_MODIFIER_ID);
            }
        }
    }
}