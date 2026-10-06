package com.fiskmods.pika.lightsabers.client.render.models.lightsabers;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.item.lightsaber.LightsaberData;
import com.fiskmods.pika.lightsabers.item.lightsaber.PartType;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;

public abstract class HiltSetModel {

    private final String id;

    public HiltSetModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    // Liefert das gebackene 3D-Modell für das jeweilige Bauteil
    public abstract ModelPart getPartModel(PartType type);

    // Überladene Methode mit Zugriff auf die aktuellen Schwertdaten (für kontextuelle Anpassungen wie Fulcrum)
    public ModelPart getPartModel(PartType type, LightsaberData data) {
        return getPartModel(type);
    }

    // Liefert die originalen Techne-Höhen von Fisk (in 1/16 Blöcken)
    public abstract float getHeight(PartType type);

    // Ob das Set eine einzelne Textur (wie Reborn) oder 4 getrennte Texturen nutzt
    public boolean isSingleTexture() {
        return false;
    }

    // Ermittelt dynamisch den passenden Textur-Pfad
    public ResourceLocation getTexture(PartType type) {
        if (isSingleTexture()) {
            return ResourceLocation.fromNamespaceAndPath(
                    AdvancedLightsabers.MODID, 
                    "textures/models/lightsaber/" + this.id + ".png"
            );
        }
        
        String prefix = switch (type) {
            case EMITTER -> "emitter";
            case SWITCH_SECTION -> "switch_section";
            case BODY -> "body";
            case POMMEL -> "pommel";
        };
        
        return ResourceLocation.fromNamespaceAndPath(
                AdvancedLightsabers.MODID, 
                "textures/models/lightsaber/" + prefix + "_" + this.id + ".png"
        );
    }

    // Crossguard-Offsets für Emitter (wie bei Kylo Rens Schwert / Knighted)
    public float[] getCrossguard() {
        return new float[] { 0.0F, 0.0F, 0.0F };
    }

    public float[] getCrossguard(LightsaberData data) {
        return getCrossguard();
    }

    public boolean hasCrossguard() {
        return getCrossguard()[2] > 0.0F;
    }
}