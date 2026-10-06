package com.fiskmods.pika.lightsabers.world.inventory;

import com.fiskmods.pika.lightsabers.ALMenuTypes;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class DisassemblyStationMenu extends AbstractContainerMenu {

    private final IItemHandler inventory;
    private final ContainerData data;

    // Client-Konstruktor für IMenuTypeExtension (RegistryFriendlyByteBuf)
    public DisassemblyStationMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, new ItemStackHandler(17), new SimpleContainerData(3));
    }

    // Server-Konstruktor
    public DisassemblyStationMenu(int containerId, Inventory playerInventory, IItemHandler inventory, ContainerData data) {
        super(ALMenuTypes.DISASSEMBLY_STATION.get(), containerId);
        this.inventory = inventory;
        this.data = data;

        // Slot 0: Input (Lichtschwert)
        this.addSlot(new SlotItemHandler(inventory, 0, 16, 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof ItemLightsaber;
            }
        });

        // Slot 1: Fuel (Brennstoff)
        this.addSlot(new SlotItemHandler(inventory, 1, 16, 54) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getBurnTime(RecipeType.SMELTING) > 0;
            }
        });

        // Slots 2..16: 15 Ausgabeslots (5 Spalten x 3 Reihen)
        for (int i = 0; i < 15; i++) {
            int slotX = 72 + (i % 5) * 18;
            int slotY = 18 + (i / 5) * 18;
            this.addSlot(new SlotItemHandler(inventory, i + 2, slotX, slotY) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        }

        // Spieler-Inventar (Y: 86) & Hotbar (Y: 144)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 86 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 144));
        }

        this.addDataSlots(data);
    }

    public int getBurnProgress() {
        int max = this.data.get(2);
        if (max == 0) max = 200;
        return this.data.get(1) * 13 / max;
    }

    public int getCookProgress() {
        return this.data.get(0) * 24 / 200;
    }

    public boolean isBurning() {
        return this.data.get(1) > 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            if (index >= 2 && index <= 16) {
                if (!this.moveItemStackTo(stackInSlot, 17, 53, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stackInSlot, itemstack);
            } else if (index != 0 && index != 1) {
                if (stackInSlot.getItem() instanceof ItemLightsaber) {
                    if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) return ItemStack.EMPTY;
                } else if (stackInSlot.getBurnTime(RecipeType.SMELTING) > 0) {
                    if (!this.moveItemStackTo(stackInSlot, 1, 2, false)) return ItemStack.EMPTY;
                } else if (index >= 17 && index < 44) {
                    if (!this.moveItemStackTo(stackInSlot, 44, 53, false)) return ItemStack.EMPTY;
                } else if (index >= 44 && index < 53 && !this.moveItemStackTo(stackInSlot, 17, 44, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 17, 53, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}