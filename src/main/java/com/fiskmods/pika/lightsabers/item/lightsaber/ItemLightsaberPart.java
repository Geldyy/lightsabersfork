package com.fiskmods.pika.lightsabers.item.lightsaber;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import java.util.function.Consumer;

import com.fiskmods.pika.lightsabers.client.render.lightsabers.LightsaberPartItemRenderer;


@SuppressWarnings("unused")
public class ItemLightsaberPart extends Item {
    private final String hiltName;
    private final PartType partType;

    public ItemLightsaberPart(String hiltName, PartType partType, Item.Properties properties) {
        super(properties);
        this.hiltName = hiltName;
        this.partType = partType;
    }
    
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return LightsaberPartItemRenderer.INSTANCE;
            }
        });
    }

    // Diese Methoden rufen wir später in der Schmiede ab!
    public String getHiltName() {
        return hiltName;
    }

    public PartType getPartType() {
        return partType;
    }
    
}