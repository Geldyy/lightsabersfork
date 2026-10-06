package com.fiskmods.pika.lightsabers.world.inventory;

import com.fiskmods.pika.lightsabers.ALMenuTypes;
import com.fiskmods.pika.lightsabers.item.ItemCrystalPouch;
import com.fiskmods.pika.lightsabers.item.crystals.ItemColorCrystal;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class CrystalPouchMenu extends AbstractContainerMenu {

    public static final int POUCH_SLOTS = 18;
    private final ItemStack pouchStack;
    private final int pouchSlotIndex;
    private final SimpleContainer pouchInventory;

    // Client-Konstruktor
    public CrystalPouchMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, buf.readVarInt());
    }

    private CrystalPouchMenu(int id, Inventory playerInv, int slotIndex) {
        this(id, playerInv, playerInv.getItem(slotIndex), slotIndex);
    }

    // Server-Konstruktor
    public CrystalPouchMenu(int id, Inventory playerInv, ItemStack pouchStack, int pouchSlotIndex) {
        super(ALMenuTypes.CRYSTAL_POUCH.get(), id);
        this.pouchStack = pouchStack;
        this.pouchSlotIndex = pouchSlotIndex;

        this.pouchInventory = new SimpleContainer(POUCH_SLOTS) {
            @Override
            public void setChanged() {
                super.setChanged();
                saveContentsToStack();
            }

            @Override
            public int getMaxStackSize() {
                return 16; // 1:1 Fisk Original-Limit
            }
        };

        loadContentsFromStack();

        // 1. Die 18 Kristall-Slots (2 Reihen à 9 Slots)
        for (int i = 0; i < POUCH_SLOTS; i++) {
            final int slotColorId = i;
            int x = 8 + (i % 9) * 18;
            int y = 18 + (i / 9) * 18;

            this.addSlot(new Slot(this.pouchInventory, i, x, y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    // Strikte Prüfung: Akzeptiert NUR den Farbkristall mit der passenden ID
                    if (stack.getItem() instanceof ItemColorCrystal crystal) {
                        return crystal.getColor().id == slotColorId;
                    }
                    return false;
                }
            });
        }

        // 2. Spieler-Inventar (3x9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int index = col + row * 9 + 9;
                int x = 8 + col * 18;
                int y = 68 + row * 18;
                this.addSlot(createPlayerSlot(playerInv, index, x, y));
            }
        }

        // 3. Spieler-Hotbar (1x9)
        for (int col = 0; col < 9; col++) {
            int x = 8 + col * 18;
            int y = 126;
            this.addSlot(createPlayerSlot(playerInv, col, x, y));
        }
    }

    private Slot createPlayerSlot(Inventory playerInv, int index, int x, int y) {
        return new Slot(playerInv, index, x, y) {
            @Override
            public boolean mayPickup(Player player) {
                return index != pouchSlotIndex;
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return !(stack.getItem() instanceof ItemCrystalPouch);
            }
        };
    }

    private void loadContentsFromStack() {
        ItemContainerContents contents = this.pouchStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        NonNullList<ItemStack> items = NonNullList.withSize(POUCH_SLOTS, ItemStack.EMPTY);
        contents.copyInto(items);
        for (int i = 0; i < POUCH_SLOTS; i++) {
            this.pouchInventory.setItem(i, items.get(i));
        }
    }

    private void saveContentsToStack() {
        NonNullList<ItemStack> items = NonNullList.withSize(POUCH_SLOTS, ItemStack.EMPTY);
        for (int i = 0; i < POUCH_SLOTS; i++) {
            items.set(i, this.pouchInventory.getItem(i));
        }
        this.pouchStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            if (index < POUCH_SLOTS) {
                // Aus der Tasche ins Spieler-Inventar
                if (!this.moveItemStackTo(stackInSlot, POUCH_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (stackInSlot.getItem() instanceof ItemColorCrystal crystal) {
                // Aus dem Spieler-Inventar zielgenau in den Slot dieser Kristallfarbe
                int targetSlot = crystal.getColor().id;
                if (targetSlot >= 0 && targetSlot < POUCH_SLOTS) {
                    if (!this.moveItemStackTo(stackInSlot, targetSlot, targetSlot + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return !this.pouchStack.isEmpty() && this.pouchStack.getItem() instanceof ItemCrystalPouch;
    }
}