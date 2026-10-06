package com.fiskmods.pika.lightsabers.block.entity;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SithStoneCoffinBlockEntity extends BlockEntity {
    public boolean baseplateOnly = false;

    public SithStoneCoffinBlockEntity(BlockPos pos, BlockState state) {
        super(ALBlockEntities.SITH_STONE_COFFIN.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("BaseplateOnly", this.baseplateOnly);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.baseplateOnly = tag.getBoolean("BaseplateOnly");
    }
}