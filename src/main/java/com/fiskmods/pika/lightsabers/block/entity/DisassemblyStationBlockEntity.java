package com.fiskmods.pika.lightsabers.block.entity;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.ALDataComponents;
import com.fiskmods.pika.lightsabers.ALItems;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaber;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.world.inventory.DisassemblyStationMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class DisassemblyStationBlockEntity extends BlockEntity implements MenuProvider {

    public static final int TOTAL_SLOTS = 17;
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FUEL = 1;
    public static final int OUTPUT_START = 2;
    public static final int MAX_PROGRESS = 200;

    private final ItemStackHandler items = new ItemStackHandler(TOTAL_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private int progress = 0;
    private int fuelTicks = 0;
    private int maxFuelTicks = 0;

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> fuelTicks;
                case 2 -> maxFuelTicks;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> fuelTicks = value;
                case 2 -> maxFuelTicks = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public DisassemblyStationBlockEntity(BlockPos pos, BlockState blockState) {
        super(ALBlockEntities.DISASSEMBLY_STATION.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DisassemblyStationBlockEntity entity) {
        if (level.isClientSide()) return;

        boolean dirty = false;

        if (entity.fuelTicks > 0) {
            entity.fuelTicks--;
            dirty = true;
        }

        ItemStack inputStack = entity.items.getStackInSlot(SLOT_INPUT);
        boolean canProcess = entity.canDisassemble(inputStack);

        if (canProcess) {
            if (entity.fuelTicks == 0) {
                ItemStack fuelStack = entity.items.getStackInSlot(SLOT_FUEL);
                int burnTime = fuelStack.getBurnTime(RecipeType.SMELTING);
                if (burnTime > 0) {
                    entity.fuelTicks = burnTime;
                    entity.maxFuelTicks = burnTime;
                    fuelStack.shrink(1);
                    dirty = true;
                }
            }

            if (entity.fuelTicks > 0) {
                entity.progress++;
                if (entity.progress >= MAX_PROGRESS) {
                    entity.progress = 0;
                    entity.disassembleItem();
                    dirty = true;
                }
            } else if (entity.progress > 0) {
                entity.progress = Math.max(0, entity.progress - 2);
                dirty = true;
            }
        } else if (entity.progress > 0) {
            entity.progress = 0;
            dirty = true;
        }

        if (dirty) {
            entity.setChanged();
        }
    }

    public boolean canDisassemble(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemLightsaber)) {
            return false;
        }
        LightsaberData data = stack.get(ALDataComponents.LIGHTSABER_DATA.get());
        if (data == null) return false;

        int freeSlots = 0;
        for (int i = OUTPUT_START; i < TOTAL_SLOTS; i++) {
            if (items.getStackInSlot(i).isEmpty()) freeSlots++;
        }
        return freeSlots >= 5;
    }

    private void disassembleItem() {
        ItemStack input = items.getStackInSlot(SLOT_INPUT);
        if (!canDisassemble(input)) return;

        LightsaberData data = input.get(ALDataComponents.LIGHTSABER_DATA.get());
        if (data == null) return;

        List<ItemStack> extracted = new ArrayList<>();

        if (data.emitter() != null && data.emitter() != Items.AIR) extracted.add(new ItemStack(data.emitter()));
        if (data.switchSection() != null && data.switchSection() != Items.AIR) extracted.add(new ItemStack(data.switchSection()));
        if (data.grip() != null && data.grip() != Items.AIR) extracted.add(new ItemStack(data.grip()));
        if (data.pommel() != null && data.pommel() != Items.AIR) extracted.add(new ItemStack(data.pommel()));
        
     // Farbkristall
        if (data.colorCrystal() != null && data.colorCrystal() != Items.AIR) {
            extracted.add(new ItemStack(data.colorCrystal()));
        }

        // Alle Fokussierkristalle extrahieren
        if (data.focusCrystals() != null) {
            for (Item focusItem : data.focusCrystals()) {
                if (focusItem != null && focusItem != Items.AIR) {
                    extracted.add(new ItemStack(focusItem));
                }
            }
        }

        // Circuitry
        extracted.add(new ItemStack(ALItems.CIRCUITRY.get()));

        for (ItemStack out : extracted) {
            for (int i = OUTPUT_START; i < TOTAL_SLOTS; i++) {
                if (items.getStackInSlot(i).isEmpty()) {
                    items.setStackInSlot(i, out);
                    break;
                } else if (ItemStack.isSameItemSameComponents(items.getStackInSlot(i), out)
                        && items.getStackInSlot(i).getCount() + out.getCount() <= out.getMaxStackSize()) {
                    items.getStackInSlot(i).grow(out.getCount());
                    break;
                }
            }
        }

        input.shrink(1);
    }

    public ItemStackHandler getItemHandler() {
        return this.items;
    }

    public NonNullList<ItemStack> getInventoryAsList() {
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (!items.getStackInSlot(i).isEmpty()) {
                list.add(items.getStackInSlot(i));
            }
        }
        return list;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.lightsabersfork.disassembly_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new DisassemblyStationMenu(id, playerInventory, this.items, this.dataAccess);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", items.serializeNBT(registries));
        tag.putInt("Progress", progress);
        tag.putInt("FuelTicks", fuelTicks);
        tag.putInt("MaxFuelTicks", maxFuelTicks);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory")) items.deserializeNBT(registries, tag.getCompound("Inventory"));
        progress = tag.getInt("Progress");
        fuelTicks = tag.getInt("FuelTicks");
        maxFuelTicks = tag.getInt("MaxFuelTicks");
    }
}