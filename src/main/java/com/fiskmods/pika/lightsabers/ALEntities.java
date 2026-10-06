package com.fiskmods.pika.lightsabers;

import com.fiskmods.pika.lightsabers.entity.EntityLightsaber;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ALEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, AdvancedLightsabers.MODID);

    public static final Supplier<EntityType<EntityLightsaber>> LIGHTSABER = ENTITIES.register("lightsaber",
            () -> EntityType.Builder.<EntityLightsaber>of(EntityLightsaber::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build("lightsaber"));
}