package com.fiskmods.pika.lightsabers;

import java.util.function.Supplier;

import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ALAttachmentTypes {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AdvancedLightsabers.MODID);

    public static final Supplier<AttachmentType<PlayerForceData>> FORCE_DATA = ATTACHMENT_TYPES.register(
            "force_data", () -> AttachmentType.serializable(PlayerForceData::new)
                    .copyOnDeath()
                    .build()
    );
}