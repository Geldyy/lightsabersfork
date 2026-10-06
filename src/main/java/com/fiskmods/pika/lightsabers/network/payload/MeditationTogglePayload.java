package com.fiskmods.pika.lightsabers.network.payload;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// NEU: Wir senden die BlockPos des Holocrons und ob es ein Sith-Holocron ist mit!
public record MeditationTogglePayload(boolean isMeditating, BlockPos holocronPos, boolean isSith) implements CustomPacketPayload {

    public static final Type<MeditationTogglePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, "meditation_toggle")
    );

    public static final StreamCodec<ByteBuf, MeditationTogglePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, MeditationTogglePayload::isMeditating,
            BlockPos.STREAM_CODEC, MeditationTogglePayload::holocronPos,
            ByteBufCodecs.BOOL, MeditationTogglePayload::isSith,
            MeditationTogglePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final MeditationTogglePayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.flow().isServerbound()) {
                if (context.player() instanceof ServerPlayer player) {
                    // Server sendet es an alle in Reichweite zurück
                    context.reply(new MeditationTogglePayload(payload.isMeditating(), payload.holocronPos(), payload.isSith()));
                }
            } else {
                if (payload.isMeditating()) {
                    // Wir übergeben jetzt die exakte Position und den Typ
                    MeditationStateManager.startMeditation(context.player(), payload.holocronPos(), payload.isSith());
                } else {
                    MeditationStateManager.stopMeditation();
                }
            }
        });
    }
}