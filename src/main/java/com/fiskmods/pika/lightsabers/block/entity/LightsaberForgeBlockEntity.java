package com.fiskmods.pika.lightsabers.block.entity;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.item.crystals.ItemColorCrystal;
import com.fiskmods.pika.lightsabers.item.crystals.ItemFocusingCrystal;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaberPart;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;
import com.fiskmods.pika.lightsabers.world.inventory.LightsaberForgeMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class LightsaberForgeBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (slot < 8 && level != null && !level.isClientSide()) {
                updateResult();
            }
        }
    };

    public LightsaberForgeBlockEntity(BlockPos pos, BlockState state) {
        super(ALBlockEntities.LIGHTSABER_FORGE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public NonNullList<ItemStack> getInventoryAsList() {
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                list.add(stack);
            }
        }
        return list;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", this.inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory")) {
            this.inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
    }

    private void updateResult() {
        Item colorCrystal = null;
        List<Item> focusCrystals = new ArrayList<>();
        Item circuitry = null;
        Item emitter = null;
        Item switchSection = null;
        Item grip = null;
        Item pommel = null;

        for (int i = 0; i <= 5; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (i == 0 && stack.getItem() instanceof ItemLightsaberPart part && part.getPartType() == PartType.EMITTER) {
                emitter = stack.getItem();
            } else if (i == 1 && stack.getItem() instanceof ItemLightsaberPart part && part.getPartType() == PartType.SWITCH_SECTION) {
                switchSection = stack.getItem();
            } else if (i == 2 && stack.getItem() instanceof ItemLightsaberPart part && part.getPartType() == PartType.BODY) {
                grip = stack.getItem();
            } else if (i == 3 && stack.getItem() instanceof ItemLightsaberPart) {
                pommel = stack.getItem();
            } else if (i == 4 && stack.getItem() == ALItems.CIRCUITRY.get()) {
                circuitry = stack.getItem();
            } else if (i == 5 && stack.getItem() instanceof ItemColorCrystal) {
                colorCrystal = stack.getItem();
            }
        }

        ItemStack slot6 = inventory.getStackInSlot(6);
        if (!slot6.isEmpty() && slot6.getItem() instanceof ItemFocusingCrystal) {
            focusCrystals.add(slot6.getItem());
        }

        ItemStack slot7 = inventory.getStackInSlot(7);
        if (!slot7.isEmpty() && slot7.getItem() instanceof ItemFocusingCrystal) {
            focusCrystals.add(slot7.getItem());
        }

        if (emitter != null && switchSection != null && grip != null && pommel != null && circuitry != null && colorCrystal != null) {
            ItemStack lightsaber = new ItemStack(ALItems.LIGHTSABER.get());
            LightsaberData data = new LightsaberData(colorCrystal, focusCrystals, emitter, switchSection, grip, pommel);
            lightsaber.set(ALDataComponents.LIGHTSABER_DATA.get(), data);
            inventory.setStackInSlot(8, lightsaber);
        } else {
            inventory.setStackInSlot(8, ItemStack.EMPTY);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.lightsabersfork.lightsaber_forge");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new LightsaberForgeMenu(containerId, playerInventory, this);
    }
}