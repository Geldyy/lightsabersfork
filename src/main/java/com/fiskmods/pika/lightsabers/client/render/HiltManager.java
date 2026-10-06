package com.fiskmods.pika.lightsabers.client.render;

import java.util.HashMap;
import java.util.Map;

import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.DroidekaSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.FulcrumSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.FurySetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.GraflexSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.HiltSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.ImperialSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.JuggernautSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.KnightedSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.MandalorianSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.MaulerSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.MechanicalSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.ProdigalSonSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.RebelSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.RebornSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.RedeemerSetModel;
import com.fiskmods.pika.lightsabers.client.render.models.lightsabers.VaidSetModel;
import com.fiskmods.pika.lightsabers.item.lightsaber.ItemLightsaberPart;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class HiltManager {

    private static final Map<String, HiltSetModel> HILT_SETS = new HashMap<>();
    private static final HiltSetModel DEFAULT_SET = new GraflexSetModel();

    static {
        register(DEFAULT_SET);
        register(new ProdigalSonSetModel());
        register(new VaidSetModel("vaid_ancient"));
        register(new VaidSetModel("vaid_modern"));
        register(new RedeemerSetModel());
        register(new RebornSetModel());
        register(new MechanicalSetModel());
        register(new RebelSetModel());
        register(new MaulerSetModel());
        register(new MandalorianSetModel());
        register(new KnightedSetModel());
        register(new JuggernautSetModel());
        register(new ImperialSetModel());
        register(new FurySetModel());
        register(new FulcrumSetModel());
        register(new DroidekaSetModel());
    }

    public static void register(HiltSetModel setModel) {
        HILT_SETS.put(setModel.getId(), setModel);
    }

    /**
     * Retrieves the hilt model set by identifier.
     */
    public static HiltSetModel getSet(String setId) {
        if (setId == null) return DEFAULT_SET;
        return HILT_SETS.getOrDefault(setId.toLowerCase(), DEFAULT_SET);
    }

    public static HiltSetModel getSet(Item item) {
        if (item == null) return DEFAULT_SET;

        if (item instanceof ItemLightsaberPart part) {
            try {
                String hiltName = part.getHiltName();
                if (hiltName != null && HILT_SETS.containsKey(hiltName.toLowerCase())) {
                    return HILT_SETS.get(hiltName.toLowerCase());
                }
            } catch (Exception ignored) {
                // Fallback
            }
        }

        String path = BuiltInRegistries.ITEM.getKey(item).getPath().toLowerCase();
        for (Map.Entry<String, HiltSetModel> entry : HILT_SETS.entrySet()) {
            if (path.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return DEFAULT_SET;
    }
}