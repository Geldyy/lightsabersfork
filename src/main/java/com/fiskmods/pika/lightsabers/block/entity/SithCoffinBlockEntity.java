package com.fiskmods.pika.lightsabers.block.entity;

import com.fiskmods.pika.lightsabers.ALBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SithCoffinBlockEntity extends BlockEntity {
    public boolean isOpen = false;
    public float openTimer = 0.0F;
    public float prevOpenTimer = 0.0F;

    public SithCoffinBlockEntity(BlockPos pos, BlockState state) {
        super(ALBlockEntities.SITH_COFFIN.get(), pos, state);
    }

    public void toggleLid() {
        this.isOpen = !this.isOpen;
        setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SithCoffinBlockEntity be) {
        be.prevOpenTimer = be.openTimer;

        if (be.isOpen && be.openTimer < 1.0F) {
            be.openTimer = Math.min(1.0F, be.openTimer + 0.033F); // ~30 Ticks für sanfte Öffnung
        } else if (!be.isOpen && be.openTimer > 0.0F) {
            be.openTimer = Math.max(0.0F, be.openTimer - 0.033F);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("IsOpen", this.isOpen);
        tag.putFloat("OpenTimer", this.openTimer);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.isOpen = tag.getBoolean("IsOpen");
        this.openTimer = tag.getFloat("OpenTimer");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}