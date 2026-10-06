package com.fiskmods.pika.lightsabers.force.skilltree;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SkillNode {
    private final ResourceLocation id;
    private final SkillTreeRegistry.TreeCategory category;
    private final int steleIndex; // z.B. 0-4 für die 5 Lichtschwert-Stile

    // 3D-Position relativ zur Mitte der Stele
    private final float offsetX;
    private final float offsetY;

    private final Component title;
    private final Component description;
    
    private final int pointCost;
    private final boolean usesForce; // Tag "NUTZT MACHT"

    private final List<ResourceLocation> parents = new ArrayList<>();
    private Predicate<Player> unlockCondition = player -> true;
    
    private ResourceLocation navUp = null;
    private ResourceLocation navDown = null;
    private ResourceLocation navLeft = null;
    private ResourceLocation navRight = null;

    public SkillNode(ResourceLocation id, SkillTreeRegistry.TreeCategory category, int steleIndex, float offsetX, float offsetY, Component title, Component description, int pointCost, boolean usesForce) {
        this.id = id;
        this.category = category;
        this.steleIndex = steleIndex;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.title = title;
        this.description = description;
        this.pointCost = pointCost;
        this.usesForce = usesForce;
    }

    public SkillNode addParent(ResourceLocation parentId) {
        this.parents.add(parentId);
        return this;
    }

    public SkillNode setCondition(Predicate<Player> condition) {
        this.unlockCondition = condition;
        return this;
    }
    
 // NEU: Setter-Methoden für die Richtungen (Builder-Pattern)
    public SkillNode setNavUp(ResourceLocation id) { this.navUp = id; return this; }
    public SkillNode setNavDown(ResourceLocation id) { this.navDown = id; return this; }
    public SkillNode setNavLeft(ResourceLocation id) { this.navLeft = id; return this; }
    public SkillNode setNavRight(ResourceLocation id) { this.navRight = id; return this; }

    // NEU: Getter-Methoden
    public ResourceLocation getNavUp() { return navUp; }
    public ResourceLocation getNavDown() { return navDown; }
    public ResourceLocation getNavLeft() { return navLeft; }
    public ResourceLocation getNavRight() { return navRight; }

    public ResourceLocation getId() { return id; }
    public SkillTreeRegistry.TreeCategory getCategory() { return category; }
    public int getSteleIndex() { return steleIndex; }
    public float getOffsetX() { return offsetX; }
    public float getOffsetY() { return offsetY; }
    public Component getTitle() { return title; }
    public Component getDescription() { return description; }
    public int getPointCost() { return pointCost; }
    public boolean usesForce() { return usesForce; }
    public List<ResourceLocation> getParents() { return parents; }
    
    public boolean canUnlock(Player player, PlayerForceData data) {
        if (data == null || data.hasSkill(this.id)) {
            return false; // Bereits freigeschaltet oder keine Daten
        }

        // Wenn der Knoten keine Eltern besitzt, ist er der Start-Knoten (Wurzel) der Stele!
        if (this.parents == null || this.parents.isEmpty()) {
            return true;
        }

        // Prüfen, ob mindestens einer der Eltern-Knoten freigeschaltet ist
        for (ResourceLocation parentId : this.parents) {
            if (data.hasSkill(parentId)) {
                return true; // Voraussetzung erfüllt!
            }
        }

        return false; // Kein Eltern-Knoten freigeschaltet -> Gesperrt!
    }
}