package com.fiskmods.pika.lightsabers.network;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillNode;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;
import com.fiskmods.pika.lightsabers.network.payload.BroadcastLightningPayload;
import com.fiskmods.pika.lightsabers.network.payload.CycleForcePayload;
import com.fiskmods.pika.lightsabers.network.payload.DoubleJumpPayload;
import com.fiskmods.pika.lightsabers.network.payload.HolocronUsagePayload;
import com.fiskmods.pika.lightsabers.network.payload.MeditationTogglePayload;
import com.fiskmods.pika.lightsabers.network.payload.SelectPowerPayload;
import com.fiskmods.pika.lightsabers.network.payload.SyncForceDataPayload;
import com.fiskmods.pika.lightsabers.network.payload.ThrowLightsaberPayload;
import com.fiskmods.pika.lightsabers.network.payload.ToggleLightsaberPayload;
import com.fiskmods.pika.lightsabers.network.payload.ToggleRangeBoostPayload;
import com.fiskmods.pika.lightsabers.network.payload.UnlockSkillPayload;
import com.fiskmods.pika.lightsabers.network.payload.UseForcePayload;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.MOD)
public class ALNetwork {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(AdvancedLightsabers.MODID).versioned("1.0");

        registrar.playToServer(
            ToggleLightsaberPayload.TYPE,
            ToggleLightsaberPayload.STREAM_CODEC,
            ToggleLightsaberPayload::handle
        );
        registrar.playToServer(
            ToggleRangeBoostPayload.TYPE,
            ToggleRangeBoostPayload.STREAM_CODEC,
            ToggleRangeBoostPayload::handle
        );

        registrar.playToServer(
                ThrowLightsaberPayload.TYPE,
                ThrowLightsaberPayload.CODEC,
                ThrowLightsaberPayload::handle
        );
        registrar.playToServer(
        	    DoubleJumpPayload.TYPE,
        	    DoubleJumpPayload.STREAM_CODEC,
        	    DoubleJumpPayload::handle
        	);
        registrar.playToClient(
        	    BroadcastLightningPayload.TYPE,
        	    BroadcastLightningPayload.STREAM_CODEC,
        	    BroadcastLightningPayload::handle
        	);
        registrar.playToServer(
        	    HolocronUsagePayload.TYPE,
        	    HolocronUsagePayload.STREAM_CODEC,
        	    HolocronUsagePayload::handle
        	);
        registrar.playBidirectional(
        	    MeditationTogglePayload.TYPE,
        	    MeditationTogglePayload.STREAM_CODEC,
        	    MeditationTogglePayload::handle
        	);
        registrar.playToServer(
            UnlockSkillPayload.TYPE, 
            UnlockSkillPayload.STREAM_CODEC, 
            ALNetwork::handleUnlockSkill
        );
        registrar.playToClient(
            SyncForceDataPayload.TYPE, 
            SyncForceDataPayload.STREAM_CODEC, 
            ALNetwork::handleSyncForceData
        );
        registrar.playToServer(
            CycleForcePayload.TYPE, 
            CycleForcePayload.STREAM_CODEC, 
            CycleForcePayload::handle
        );
        registrar.playToServer(
           UseForcePayload.TYPE, 
           UseForcePayload.STREAM_CODEC, 
           UseForcePayload::handle
        );
        registrar.playToServer(
        	    SelectPowerPayload.TYPE,
        	    SelectPowerPayload.STREAM_CODEC,
        	    SelectPowerPayload::handle
        	);
    }
    
    public static void handleUnlockSkill(UnlockSkillPayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!(player instanceof ServerPlayer serverPlayer)) return;

            PlayerForceData data = PlayerForceData.get(serverPlayer);
            if (data == null) return;

            SkillNode node = SkillTreeRegistry.getNode(payload.skillId());
            if (node == null || data.hasSkill(payload.skillId())) return;

            // =========================================================================
            // NEU: PRÜFEN, OB DIE ELTERN-SKILLS BEREITS FREIGESCHALTET WURDEN!
            // =========================================================================
            if (!node.canUnlock(serverPlayer, data)) {
                // Ausführen abbrechen, falls der Untere/Eltern-Node noch nicht gelernt wurde
                return; 
            }

            boolean success = false;

            // Prüfen, ob der Spieler sich den Skill leisten kann
            if (node.getCategory() == TreeCategory.FORCE) {
                success = data.consumeForcePoints(node.getPointCost());
            } else if (node.getCategory() == TreeCategory.LIGHTSABER) {
                success = data.consumeAbilityPoints(node.getPointCost());
            }

            // Wenn bezahlt, Skill freischalten und neues Savegame an Client schicken
            if (success) {
                data.unlockSkill(payload.skillId());
                data.sync(serverPlayer); 
            }
        });
    }

    // ====================================================================
    // CLIENT EMPFÄNGT: Server schickt neues Savegame (SyncForceDataPayload)
    // ====================================================================
    public static void handleSyncForceData(SyncForceDataPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            PlayerForceData data = PlayerForceData.get(player);
            if (data != null) {
                // Clientseitiges Daten-Update (UI springt sofort auf "ERLERNT" um)
                data.deserializeNBT(player.registryAccess(), payload.dataTag());
            }
        });
    }
}