package com.fiskmods.pika.lightsabers.force.skilltree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fiskmods.pika.lightsabers.ALAttachmentTypes; 
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.network.payload.SyncForceDataPayload;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlayerForceData implements INBTSerializable<CompoundTag> {

    private final Set<ResourceLocation> unlockedSkills = new HashSet<>();

    // --- MACHT RÄNGE & SPEZIALISIERUNGEN (NEU) ---
    private boolean forceSensitivity = false;
    private int forceRank = 0; // 0-4
    private int jediRank = 0;  // 0-6
    private int sithRank = 0;  // 0-6
    private String jediSpecialization = "NONE";
    private String sithSpecialization = "NONE";

    // --- MACHT SYSTEM (Punkte & XP) ---
    private int forcePoints = 0;
    private float forceXp = 0.0f;
    private static final float FORCE_XP_PER_POINT = 2000.0f;

    // --- MACHT ENERGIE (Mana-Pool) ---
    private float forceEnergy = 0.0f;
    private ResourceLocation selectedPower = null;

    // --- LICHTSCHWERT SYSTEM (Abilities) ---
    private int abilityPoints = 0;
    private int lightsaberKills = 0;
    private int nextKillThreshold = 35; 
    
    // ==========================================
    // EVENTS ZUR SYNCHRONISATION
    // ==========================================
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerForceData data = PlayerForceData.get(player);
            if (data != null) data.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerForceData data = PlayerForceData.get(player);
            if (data != null) data.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer newPlayer) {
            PlayerForceData oldData = PlayerForceData.get(event.getOriginal());
            PlayerForceData newData = PlayerForceData.get(newPlayer);
            
            if (oldData != null && newData != null) {
                newData.deserializeNBT(newPlayer.registryAccess(), oldData.serializeNBT(event.getOriginal().registryAccess()));
                // Energie beim Tod oder Dimensionswechsel auffüllen
                newData.forceEnergy = newData.getMaxForceEnergy();
                newData.sync(newPlayer);
            }
        }
    }

    // ==========================================
    // SKILL UNLOCKS & RANG-UPDATES
    // ==========================================
    public boolean hasSkill(ResourceLocation skillId) {
        return unlockedSkills.contains(skillId);
    }
    
    public boolean hasSkill(String namespace, String path) {
        return hasSkill(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public void unlockSkill(ResourceLocation skillId) {
        unlockedSkills.add(skillId);
        String path = skillId.getPath();

        // 1. Macht Ränge
        if (path.equals("force_sensitivity")) { this.forceSensitivity = true; this.forceEnergy = getMaxForceEnergy(); }
        else if (path.equals("force_adept")) this.forceRank = Math.max(this.forceRank, 1);
        else if (path.equals("force_channeler")) this.forceRank = Math.max(this.forceRank, 2);
        else if (path.equals("force_veteran")) this.forceRank = Math.max(this.forceRank, 3);
        else if (path.equals("force_mastery")) this.forceRank = Math.max(this.forceRank, 4);

        // 2. Jedi Ränge
        else if (path.equals("jedi_initiate")) this.jediRank = Math.max(this.jediRank, 1);
        else if (path.equals("jedi_padawan")) this.jediRank = Math.max(this.jediRank, 2);
        else if (path.equals("jedi_knight")) this.jediRank = Math.max(this.jediRank, 3);
        else if (path.equals("jedi_librarian")) { this.jediRank = Math.max(this.jediRank, 3); this.jediSpecialization = "LIBRARIAN"; }
        else if (path.equals("jedi_guard")) { this.jediRank = Math.max(this.jediRank, 3); this.jediSpecialization = "GUARD"; }
        else if (path.equals("jedi_master")) this.jediRank = Math.max(this.jediRank, 4);
        else if (path.equals("jedi_council")) this.jediRank = Math.max(this.jediRank, 5);
        else if (path.equals("jedi_grandmaster")) this.jediRank = Math.max(this.jediRank, 6);

        // 3. Sith Ränge
        else if (path.equals("sith_hopeful")) this.sithRank = Math.max(this.sithRank, 1);
        else if (path.equals("sith_acolyte")) this.sithRank = Math.max(this.sithRank, 2);
        else if (path.equals("sith_warrior")) this.sithRank = Math.max(this.sithRank, 3);
        else if (path.equals("sith_inquisitor")) { this.sithRank = Math.max(this.sithRank, 3); this.sithSpecialization = "INQUISITOR"; }
        else if (path.equals("sith_assassin")) { this.sithRank = Math.max(this.sithRank, 3); this.sithSpecialization = "ASSASSIN"; }
        else if (path.equals("sith_master")) this.sithRank = Math.max(this.sithRank, 4);
        else if (path.equals("sith_council")) this.sithRank = Math.max(this.sithRank, 5);
        else if (path.equals("sith_emperor")) this.sithRank = Math.max(this.sithRank, 6);
    }
    
    public Set<ResourceLocation> getUnlockedSkills() {
        return this.unlockedSkills;
    }

    // ==========================================
    // DYNAMISCHE MACHT ENERGIE LOGIK (Mana-System)
    // ==========================================
    public boolean isForceSensitive() {
        return forceSensitivity;
    }

    public float getMaxForceEnergy() {
        if (!forceSensitivity) return 0.0f;
        
        float max = 100.0f;
        if (forceRank >= 2) max = 150.0f;
        if (forceRank >= 3) max = 250.0f;
        if (forceRank >= 4) max = 400.0f;
        
        // Spezialisierungs-Boni
        if ("LIBRARIAN".equals(jediSpecialization) || "INQUISITOR".equals(sithSpecialization)) {
            max += 50.0f; // Hexer und Archivare haben tiefere Machtreserven
        }
        return max;
    }

    public void regenerateEnergy() {
        if (!forceSensitivity) return;
        
        float baseRegen = 0.5f; // 0.5 pro Tick = 10 / Sekunde
        if (forceRank >= 2) baseRegen = 0.75f; 
        if (forceRank >= 3) baseRegen = 1.0f;  
        if (forceRank >= 4) baseRegen = 1.5f;  
        
        if ("LIBRARIAN".equals(jediSpecialization)) baseRegen *= 1.25f; // 25% schnellere Regeneration
        
        if (this.forceEnergy < getMaxForceEnergy()) {
            this.forceEnergy = Math.min(this.forceEnergy + baseRegen, getMaxForceEnergy());
        }
    }

    public float getForceEnergy() {
        return forceEnergy;
    }

    public boolean consumeForceEnergy(float amount) {
        if (!forceSensitivity) return false;
        if (this.forceEnergy >= amount) {
            this.forceEnergy -= amount;
            return true;
        }
        return false;
    }

    public void addForceEnergy(float amount) {
        if (!forceSensitivity) return;
        this.forceEnergy = Math.min(this.forceEnergy + amount, getMaxForceEnergy());
    }

    public ResourceLocation getSelectedPower() {
        return selectedPower;
    }
    
    public void setSelectedPower(ResourceLocation powerId) {
        this.selectedPower = powerId;
    }

    public List<ResourceLocation> getAvailableActivePowers() {
        List<ResourceLocation> activePowers = new ArrayList<>();
        
        // FÜR DEN TEST: Alle Skills temporär ins Rad packen, wenn man machtsensitiv ist
        if (this.isForceSensitive()) {
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "force_push"));
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "force_dash"));
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "force_heal"));
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "force_lightning"));
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "force_repulse"));
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "force_pull"));
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "electric_judgement"));
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "force_choke"));
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "force_drain"));
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "force_telekinesis"));
            activePowers.add(ResourceLocation.fromNamespaceAndPath("lightsabersfork", "force_fortify"));
        }
        
        return activePowers;
    }

    // ==========================================
    // LICHTSCHWERT & MACHT-PUNKTE LOGIK
    // ==========================================
    public int getForcePoints() { return forcePoints; }
    public boolean consumeForcePoints(int amount) {
        if (this.forcePoints >= amount) { this.forcePoints -= amount; return true; }
        return false;
    }
    public float getForceXp() { return forceXp; }
    public void addForceXp(float amount) {
        this.forceXp += amount;
        while (this.forceXp >= FORCE_XP_PER_POINT) { this.forceXp -= FORCE_XP_PER_POINT; this.forcePoints++; }
    }
    public int getAbilityPoints() { return abilityPoints; }
    public void addAbilityPoints(int amount) { this.abilityPoints += amount; }
    public boolean consumeAbilityPoints(int amount) {
        if (this.abilityPoints >= amount) { this.abilityPoints -= amount; return true; }
        return false;
    }
    public int getLightsaberKills() { return lightsaberKills; }
    public int getNextKillThreshold() { return nextKillThreshold; }
    public boolean addLightsaberKill() {
        this.lightsaberKills++;
        if (this.lightsaberKills >= this.nextKillThreshold) {
            this.abilityPoints++;
            this.lightsaberKills = 0;
            this.nextKillThreshold += 10;
            return true; 
        }
        return false;
    }

    // ==========================================
    // DEBUG & SPEICHERN
    // ==========================================
    public void unlockAllSkills() {
        for (SkillNode node : SkillTreeRegistry.getAllNodes()) { this.unlockSkill(node.getId()); }
    }

    public void resetAll() {
        unlockedSkills.clear();
        forcePoints = 0; forceXp = 0.0f; abilityPoints = 0; lightsaberKills = 0; nextKillThreshold = 35;
        forceSensitivity = false; forceRank = 0; jediRank = 0; sithRank = 0;
        jediSpecialization = "NONE"; sithSpecialization = "NONE";
        forceEnergy = 0.0f; selectedPower = null;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("ForcePoints", forcePoints);
        tag.putFloat("ForceXP", forceXp);
        tag.putInt("AbilityPoints", abilityPoints);
        tag.putInt("LightsaberKills", lightsaberKills);
        tag.putInt("NextKillThreshold", nextKillThreshold);

        // Neue Macht & Ränge
        tag.putBoolean("ForceSensitivity", forceSensitivity);
        tag.putInt("ForceRank", forceRank);
        tag.putInt("JediRank", jediRank);
        tag.putInt("SithRank", sithRank);
        tag.putString("JediSpec", jediSpecialization);
        tag.putString("SithSpec", sithSpecialization);
        tag.putFloat("ForceEnergy", forceEnergy);
        if (selectedPower != null) tag.putString("SelectedPower", selectedPower.toString());

        ListTag list = new ListTag();
        for (ResourceLocation rl : unlockedSkills) list.add(StringTag.valueOf(rl.toString()));
        tag.put("UnlockedSkills", list);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        forcePoints = tag.getInt("ForcePoints");
        forceXp = tag.getFloat("ForceXP");
        abilityPoints = tag.getInt("AbilityPoints");
        lightsaberKills = tag.getInt("LightsaberKills");
        nextKillThreshold = tag.contains("NextKillThreshold") ? tag.getInt("NextKillThreshold") : 35;

        // Neue Macht & Ränge
        forceSensitivity = tag.getBoolean("ForceSensitivity");
        forceRank = tag.getInt("ForceRank");
        jediRank = tag.getInt("JediRank");
        sithRank = tag.getInt("SithRank");
        jediSpecialization = tag.contains("JediSpec") ? tag.getString("JediSpec") : "NONE";
        sithSpecialization = tag.contains("SithSpec") ? tag.getString("SithSpec") : "NONE";
        forceEnergy = tag.contains("ForceEnergy") ? tag.getFloat("ForceEnergy") : 0.0f;
        selectedPower = tag.contains("SelectedPower") ? ResourceLocation.parse(tag.getString("SelectedPower")) : null;

        unlockedSkills.clear();
        ListTag list = tag.getList("UnlockedSkills", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) unlockedSkills.add(ResourceLocation.parse(list.getString(i)));
    }

    public void sync(ServerPlayer player) {
        CompoundTag tag = this.serializeNBT(player.registryAccess());
        PacketDistributor.sendToPlayer(player, new SyncForceDataPayload(tag));
    }

    public static PlayerForceData get(Player player) {
        if (player == null) return null;
        return player.getData(ALAttachmentTypes.FORCE_DATA);
    }
}