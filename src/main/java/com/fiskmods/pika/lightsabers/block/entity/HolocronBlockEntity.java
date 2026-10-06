package com.fiskmods.pika.lightsabers.block.entity;

import com.fiskmods.pika.lightsabers.ALBlockEntities;
import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HolocronBlockEntity extends BlockEntity {

    private final Set<UUID> playersUsing = new HashSet<>();
    private int clientUsersCount = 0; // WICHTIG: Speichert den Status auf dem Client

    public float openTimer = 0.0F;
    public float prevOpenTimer = 0.0F;
    public int openTicks = 0;
    public int prevOpenTicks = 0;

    public HolocronBlockEntity(BlockPos pos, BlockState state) {
        super(ALBlockEntities.HOLOCRON.get(), pos, state);
    }

    public void setPlayerUsing(Player player, boolean isUsing) {
        if (isUsing) {
            playersUsing.add(player.getUUID());
        } else {
            playersUsing.remove(player.getUUID());
        }
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean isBeingUsed() {
        if (level != null && level.isClientSide()) {
            return clientUsersCount > 0;
        }
        return !playersUsing.isEmpty();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HolocronBlockEntity tile) {
        tile.prevOpenTimer = tile.openTimer;
        tile.prevOpenTicks = tile.openTicks;

        boolean isBeingUsed = tile.isBeingUsed();

        // Fallback: Prüft physische Distanz, falls das Netzwerk-Paket kurz hängt
        if (level.isClientSide() && !isBeingUsed) {
            if (MeditationStateManager.isMeditating()) {
                Vec3 center = MeditationStateManager.getMeditationCenterPos();
                if (center != null && pos.closerToCenterThan(center, 9.5D)) {
                    isBeingUsed = true;
                }
            }
        }

        if (!isBeingUsed) {
            tile.openTimer *= 0.85F;
        } else if (tile.openTimer < 1.0F) {
            tile.openTimer += 0.05F;
            tile.openTimer *= 1.05F;
        }

        if (tile.openTimer < 1.0E-6F) {
            tile.openTimer = 0.0F;
        }

        if (tile.openTimer == 0.0F) {
            tile.openTicks = 0;
        } else if (tile.openTimer >= 1.0F) {
            tile.openTicks++;
        }

        tile.openTimer = Mth.clamp(tile.openTimer, 0.0F, 1.0F);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("UsersCount", playersUsing.size());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        // NEU: Zieht die Information auf den Client!
        if (tag.contains("UsersCount")) {
            this.clientUsersCount = tag.getInt("UsersCount");
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putInt("UsersCount", playersUsing.size());
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}