package com.fiskmods.pika.lightsabers.item;

import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.item.crystals.CrystalColor;
import com.fiskmods.pika.lightsabers.world.inventory.CrystalPouchMenu;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

public class ItemCrystalPouch extends Item {

    public ItemCrystalPouch(Properties properties) {
        super(properties.stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)); //
    }

    public static ItemStack createWithColor(CrystalColor color) {
        ItemStack stack = new ItemStack(ALItems.CRYSTAL_POUCH.get());
        // Falls die Methode getColor() heißt:
        stack.<DyedItemColor>set(DataComponents.DYED_COLOR, new DyedItemColor(color.getColor(), true));
        return stack;
    }

    public static int getColor(ItemStack stack) {
        return DyedItemColor.getOrDefault(stack, 0xFFFFFF);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand); //

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) { //
            int slotIndex = (hand == InteractionHand.MAIN_HAND) ? player.getInventory().selected : 40; //

            serverPlayer.openMenu(new SimpleMenuProvider( //
                    (id, playerInv, p) -> new CrystalPouchMenu(id, playerInv, heldStack, slotIndex), //
                    Component.translatable("gui.lightsabersfork.crystal_pouch") //
            ), buf -> {
                buf.writeVarInt(slotIndex); //
            });
        }

        return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide()); //
    }
}