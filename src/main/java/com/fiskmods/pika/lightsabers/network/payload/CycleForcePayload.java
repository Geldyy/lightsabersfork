package com.fiskmods.pika.lightsabers.network.payload;

import java.util.List;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CycleForcePayload(boolean next) implements CustomPacketPayload {
    public static final Type<CycleForcePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "cycle_force"));
    
    public static final StreamCodec<ByteBuf, CycleForcePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, CycleForcePayload::next,
            CycleForcePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CycleForcePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                PlayerForceData data = PlayerForceData.get(player);
                if (data != null && data.isForceSensitive()) {
                    List<ResourceLocation> powers = data.getAvailableActivePowers();
                    if (powers.isEmpty()) return;
                    
                    ResourceLocation current = data.getSelectedPower();
                    int index = current != null ? powers.indexOf(current) : -1;
                    
                    if (payload.next()) {
                        index = (index + 1) % powers.size();
                    } else {
                        index = (index - 1 < 0) ? powers.size() - 1 : index - 1;
                    }
                    
                    data.setSelectedPower(powers.get(index));
                    data.sync(player);
                }
            }
        });
    }
}