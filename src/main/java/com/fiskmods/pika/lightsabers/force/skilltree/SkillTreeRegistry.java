package com.fiskmods.pika.lightsabers.force.skilltree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SkillTreeRegistry {

    public enum TreeCategory {
        SURVIVAL,
        LIGHTSABER,
        FORCE
    }

    private static final Map<ResourceLocation, SkillNode> NODES = new HashMap<>();

    public static void register(SkillNode node) {
        NODES.put(node.getId(), node);
    }

    public static List<SkillNode> getNodesForStele(TreeCategory category, int steleIndex) {
        List<SkillNode> list = new ArrayList<>();
        for (SkillNode node : NODES.values()) {
            if (node.getCategory() == category && node.getSteleIndex() == steleIndex) {
                list.add(node);
            }
        }
        return list;
    }

    public static SkillNode getNode(ResourceLocation id) {
        return NODES.get(id);
    }

    public static void init() {
    	// ==========================================================
        // STELE 0: LICHTSCHWERT LINKS (SINGLE BLADED)
        // Dreizack-Muster, das oben zusammenläuft
        // ==========================================================
        
        SkillNode sb_adv_speed = new SkillNode(loc("sb_adv_speed"), TreeCategory.LIGHTSABER, 0, 
            -17.0F, -25.0F, Component.translatable("skill.lightsabersfork.sb_adv_speed.title"), 
            Component.translatable("skill.lightsabersfork.sb_adv_speed.desc"), 2, true)
            .setNavUp(loc("sb_better_throw"))
            .setNavRight(loc("sb_better_form"));

        SkillNode sb_better_form = new SkillNode(loc("sb_better_form"), TreeCategory.LIGHTSABER, 0, 
            17.0F, -25.0F, Component.translatable("skill.lightsabersfork.sb_better_form.title"), 
            Component.translatable("skill.lightsabersfork.sb_better_form.desc"), 2, false)
            .setNavUp(loc("sb_better_throw"))
            .setNavLeft(loc("sb_adv_speed"));

        SkillNode sb_better_throw = new SkillNode(loc("sb_better_throw"), TreeCategory.LIGHTSABER, 0, 
            0.0F, -45.0F, Component.translatable("skill.lightsabersfork.sb_better_throw.title"), 
            Component.translatable("skill.lightsabersfork.sb_better_throw.desc"), 2, true)
            .addParent(sb_adv_speed.getId()).addParent(sb_better_form.getId())
            .setNavUp(loc("sb_ult_speed"))
            .setNavDown(loc("sb_better_form")) // Pfeil S geht schräg nach unten links
            .setNavLeft(loc("sb_better_swerve"))
            .setNavRight(loc("sb_adv_throw"));

        SkillNode sb_better_swerve = new SkillNode(loc("sb_better_swerve"), TreeCategory.LIGHTSABER, 0, 
            -17.0F, -50.0F, Component.translatable("skill.lightsabersfork.sb_better_swerve.title"), 
            Component.translatable("skill.lightsabersfork.sb_better_swerve.desc"), 2, false)
            .addParent(sb_adv_speed.getId())
            .setNavUp(loc("sb_ult_speed"))
            .setNavDown(loc("sb_better_throw"))
            .setNavRight(loc("sb_adv_throw"));

        SkillNode sb_adv_throw = new SkillNode(loc("sb_adv_throw"), TreeCategory.LIGHTSABER, 0, 
            17.0F, -55.0F, Component.translatable("skill.lightsabersfork.sb_adv_throw.title"), 
            Component.translatable("skill.lightsabersfork.sb_adv_throw.desc"), 3, true)
            .addParent(sb_better_form.getId())
            .setNavUp(loc("sb_ult_speed"))
            .setNavDown(loc("sb_better_throw"))
            .setNavLeft(loc("sb_better_swerve"));

        SkillNode sb_ult_speed = new SkillNode(loc("sb_ult_speed"), TreeCategory.LIGHTSABER, 0, 
            0.0F, -80.0F, Component.translatable("skill.lightsabersfork.sb_ult_speed.title"), 
            Component.translatable("skill.lightsabersfork.sb_ult_speed.desc"), 4, true)
            .addParent(sb_better_swerve.getId()).addParent(sb_better_throw.getId()).addParent(sb_adv_throw.getId())
            .setNavDown(loc("sb_better_throw"))
            .setNavLeft(loc("sb_better_swerve"))
            .setNavRight(loc("sb_adv_throw"));

        registerAll(sb_adv_speed, sb_better_form, sb_better_swerve, sb_better_throw, sb_adv_throw, sb_ult_speed);


        // ==========================================================
        // STELE 1: LICHTSCHWERT MITTE (GRUNDLAGEN)
        // Asymmetrischer Baum, "Throw" ist eine Sackgasse!
        // ==========================================================
        
        SkillNode ls_block = new SkillNode(loc("ls_block"), TreeCategory.LIGHTSABER, 1, 
                0.0F, -15.0F, Component.translatable("skill.lightsabersfork.ls_block.title"), 
                Component.translatable("skill.lightsabersfork.ls_block.desc"), 1, false)
                .setNavUp(loc("ls_adv_block"))
                .setNavLeft(loc("ls_better_block"))
                .setNavRight(loc("ls_throw"));
            
        SkillNode ls_better_block = new SkillNode(loc("ls_better_block"), TreeCategory.LIGHTSABER, 1, 
            -17.0F, -30.0F, Component.translatable("skill.lightsabersfork.ls_better_block.title"), 
            Component.translatable("skill.lightsabersfork.ls_better_block.desc"), 1, false)
            .addParent(ls_block.getId())
            .setNavUp(loc("ls_adv_block"))
            .setNavDown(loc("ls_block"))
            .setNavRight(loc("ls_throw"));

        SkillNode ls_throw = new SkillNode(loc("ls_throw"), TreeCategory.LIGHTSABER, 1, 
            17.0F, -40.0F, Component.translatable("skill.lightsabersfork.ls_throw.title"), 
            Component.translatable("skill.lightsabersfork.ls_throw.desc"), 1, true)
            .addParent(ls_block.getId())
            .setNavUp(loc("ls_adv_block"))
            .setNavDown(loc("ls_block"))
            .setNavLeft(loc("ls_better_block"));

        SkillNode ls_adv_block = new SkillNode(loc("ls_adv_block"), TreeCategory.LIGHTSABER, 1, 
            0.0F, -50.0F, Component.translatable("skill.lightsabersfork.ls_adv_block.title"), 
            Component.translatable("skill.lightsabersfork.ls_adv_block.desc"), 2, false)
            .addParent(ls_better_block.getId())
            .setNavUp(loc("ls_lethal_precision"))
            .setNavDown(loc("ls_block"))
            .setNavLeft(loc("ls_focusing"))
            .setNavRight(loc("ls_kyber_resonance"));

        SkillNode ls_focusing = new SkillNode(loc("ls_focusing"), TreeCategory.LIGHTSABER, 1, 
            -17.0F, -60.0F, Component.translatable("skill.lightsabersfork.ls_focusing.title"), 
            Component.translatable("skill.lightsabersfork.ls_focusing.desc"), 2, true)
            .addParent(ls_adv_block.getId())
            .setNavUp(loc("ls_lethal_precision"))
            .setNavDown(loc("ls_adv_block"))
            .setNavRight(loc("ls_kyber_resonance"));

        SkillNode ls_dmg1 = new SkillNode(loc("ls_kyber_resonance"), TreeCategory.LIGHTSABER, 1, 
            17.0F, -70.0F, Component.translatable("skill.lightsabersfork.ls_kyber_resonance.title"), 
            Component.translatable("skill.lightsabersfork.ls_kyber_resonance.desc"), 2, false)
            .addParent(ls_adv_block.getId())
            .setNavUp(loc("ls_lethal_precision"))
            .setNavDown(loc("ls_adv_block"))
            .setNavLeft(loc("ls_focusing"));

        SkillNode ls_dmg3 = new SkillNode(loc("ls_lethal_precision"), TreeCategory.LIGHTSABER, 1, 
            0.0F, -85.0F, Component.translatable("skill.lightsabersfork.ls_lethal_precision.title"), 
            Component.translatable("skill.lightsabersfork.ls_lethal_precision.desc"), 3, false)
            .addParent(ls_focusing.getId()).addParent(ls_dmg1.getId())
            .setNavDown(loc("ls_adv_block"))
            .setNavLeft(loc("ls_focusing"))
            .setNavRight(loc("ls_kyber_resonance"));

        registerAll(ls_block, ls_better_block, ls_throw, ls_adv_block, ls_focusing, ls_dmg1, ls_dmg3);


     // ==========================================================
        // STELE 2: LICHTSCHWERT RECHTS (DOUBLE BLADED)
        // ==========================================================
        
        SkillNode db_better_form = new SkillNode(loc("db_better_form"), TreeCategory.LIGHTSABER, 2, 
            0.0F, -20.0F, Component.translatable("skill.lightsabersfork.db_better_form.title"), 
            Component.translatable("skill.lightsabersfork.db_better_form.desc"), 2, false)
            .setNavUp(loc("db_adv_form"))
            .setNavLeft(loc("db_single_switch"))
            .setNavRight(loc("db_adv_swerve"));

        SkillNode db_single_switch = new SkillNode(loc("db_single_switch"), TreeCategory.LIGHTSABER, 2, 
            -17.0F, -40.0F, Component.translatable("skill.lightsabersfork.db_single_switch.title"), 
            Component.translatable("skill.lightsabersfork.db_single_switch.desc"), 2, false)
            .addParent(db_better_form.getId())
            .setNavUp(loc("db_adv_formk"))
            .setNavRight(loc("db_adv_swerve"))
            .setNavDown(loc("db_better_form"));

        SkillNode db_adv_swerve = new SkillNode(loc("db_adv_swerve"), TreeCategory.LIGHTSABER, 2, 
            17.0F, -45.0F, Component.translatable("skill.lightsabersfork.db_adv_swerve.title"), 
            Component.translatable("skill.lightsabersfork.db_adv_swerve.desc"), 2, false)
            .addParent(db_better_form.getId())
            .setNavUp(loc("db_adv_form"))
            .setNavLeft(loc("db_single_switch"))
            .setNavDown(loc("db_better_form"));

        SkillNode db_adv_form = new SkillNode(loc("db_adv_form"), TreeCategory.LIGHTSABER, 2, 
            0.0F, -60.0F, Component.translatable("skill.lightsabersfork.db_adv_form.title"), 
            Component.translatable("skill.lightsabersfork.db_adv_form.desc"), 3, false)
            .addParent(db_single_switch.getId()).addParent(db_adv_swerve.getId())
            .setNavUp(loc("db_ult_block"))
            .setNavDown(loc("db_better_form"))
            .setNavLeft(loc("db_single_switch"))
            .setNavRight(loc("db_adv_swerve"));

        SkillNode db_ult_block = new SkillNode(loc("db_ult_block"), TreeCategory.LIGHTSABER, 2, 
            0.0F, -80.0F, Component.translatable("skill.lightsabersfork.db_ult_block.title"), 
            Component.translatable("skill.lightsabersfork.db_ult_block.desc"), 4, true)
            .addParent(db_adv_form.getId())
            .setNavDown(loc("db_adv_form"));

        registerAll(db_better_form, db_single_switch, db_adv_swerve, db_adv_form, db_ult_block);
        
    //    --------------- FORCE ABILITYS DOWN HERE --------------------
        
        // ==========================================================
        // STELE 3: MACHT GRUNDLAGEN (NEUTRAL)
        // ==========================================================
        SkillNode force_sensitivity = new SkillNode(loc("force_sensitivity"), TreeCategory.FORCE, 3, 
            0.0F, -10.0F, Component.translatable("skill.lightsabersfork.force_sensitivity.title"), Component.empty(), 1, true);
        SkillNode force_adept = new SkillNode(loc("force_adept"), TreeCategory.FORCE, 3, 
            0.0F, -25.0F, Component.translatable("skill.lightsabersfork.force_adept.title"), Component.empty(), 2, false).addParent(force_sensitivity.getId());
        SkillNode force_channeler = new SkillNode(loc("force_channeler"), TreeCategory.FORCE, 3, 
            0.0F, -40.0F, Component.translatable("skill.lightsabersfork.force_channeler.title"), Component.empty(), 3, false).addParent(force_adept.getId());
        SkillNode force_veteran = new SkillNode(loc("force_veteran"), TreeCategory.FORCE, 3, 
            0.0F, -55.0F, Component.translatable("skill.lightsabersfork.force_veteran.title"), Component.empty(), 4, false).addParent(force_channeler.getId());
        SkillNode force_mastery = new SkillNode(loc("force_mastery"), TreeCategory.FORCE, 3, 
            0.0F, -70.0F, Component.translatable("skill.lightsabersfork.force_mastery.title"), Component.empty(), 5, true).addParent(force_veteran.getId());

        registerAll(force_sensitivity, force_adept, force_channeler, force_veteran, force_mastery);

        // ==========================================================
        // STELE 4: JEDI AUSBILDUNG
        // ==========================================================
        SkillNode jedi_initiate = new SkillNode(loc("jedi_initiate"), TreeCategory.FORCE, 4, 
            0.0F, -10.0F, Component.translatable("skill.lightsabersfork.jedi_initiate.title"), Component.empty(), 1, false).addParent(loc("force_sensitivity"));
        SkillNode jedi_padawan = new SkillNode(loc("jedi_padawan"), TreeCategory.FORCE, 4, 
            0.0F, -25.0F, Component.translatable("skill.lightsabersfork.jedi_padawan.title"), Component.empty(), 2, false).addParent(jedi_initiate.getId());
        SkillNode jedi_knight = new SkillNode(loc("jedi_knight"), TreeCategory.FORCE, 4, 
            0.0F, -40.0F, Component.translatable("skill.lightsabersfork.jedi_knight.title"), Component.empty(), 3, true).addParent(jedi_padawan.getId());
        
        // Verzweigungen
        SkillNode jedi_librarian = new SkillNode(loc("jedi_librarian"), TreeCategory.FORCE, 4, 
            -15.0F, -55.0F, Component.translatable("skill.lightsabersfork.jedi_librarian.title"), Component.empty(), 3, false).addParent(jedi_knight.getId());
        SkillNode jedi_guard = new SkillNode(loc("jedi_guard"), TreeCategory.FORCE, 4, 
            15.0F, -55.0F, Component.translatable("skill.lightsabersfork.jedi_guard.title"), Component.empty(), 3, false).addParent(jedi_knight.getId());
        
        SkillNode jedi_master = new SkillNode(loc("jedi_master"), TreeCategory.FORCE, 4, 
            0.0F, -70.0F, Component.translatable("skill.lightsabersfork.jedi_master.title"), Component.empty(), 4, false).addParent(jedi_librarian.getId()).addParent(jedi_guard.getId());
        SkillNode jedi_council = new SkillNode(loc("jedi_council"), TreeCategory.FORCE, 4, 
            0.0F, -85.0F, Component.translatable("skill.lightsabersfork.jedi_council.title"), Component.empty(), 5, false).addParent(jedi_master.getId());
        SkillNode jedi_grandmaster = new SkillNode(loc("jedi_grandmaster"), TreeCategory.FORCE, 4, 
            0.0F, -100.0F, Component.translatable("skill.lightsabersfork.jedi_grandmaster.title"), Component.empty(), 6, true).addParent(jedi_council.getId());

        registerAll(jedi_initiate, jedi_padawan, jedi_knight, jedi_librarian, jedi_guard, jedi_master, jedi_council, jedi_grandmaster);

        // ==========================================================
        // STELE 5: SITH AUSBILDUNG
        // ==========================================================
        SkillNode sith_hopeful = new SkillNode(loc("sith_hopeful"), TreeCategory.FORCE, 5, 
            0.0F, -10.0F, Component.translatable("skill.lightsabersfork.sith_hopeful.title"), Component.empty(), 1, false).addParent(loc("force_sensitivity"));
        SkillNode sith_acolyte = new SkillNode(loc("sith_acolyte"), TreeCategory.FORCE, 5, 
            0.0F, -25.0F, Component.translatable("skill.lightsabersfork.sith_acolyte.title"), Component.empty(), 2, false).addParent(sith_hopeful.getId());
        SkillNode sith_warrior = new SkillNode(loc("sith_warrior"), TreeCategory.FORCE, 5, 
            0.0F, -40.0F, Component.translatable("skill.lightsabersfork.sith_warrior.title"), Component.empty(), 3, true).addParent(sith_acolyte.getId());
        
        // Verzweigungen
        SkillNode sith_inquisitor = new SkillNode(loc("sith_inquisitor"), TreeCategory.FORCE, 5, 
            -15.0F, -55.0F, Component.translatable("skill.lightsabersfork.sith_inquisitor.title"), Component.empty(), 3, false).addParent(sith_warrior.getId());
        SkillNode sith_assassin = new SkillNode(loc("sith_assassin"), TreeCategory.FORCE, 5, 
            15.0F, -55.0F, Component.translatable("skill.lightsabersfork.sith_assassin.title"), Component.empty(), 3, false).addParent(sith_warrior.getId());
        
        SkillNode sith_master = new SkillNode(loc("sith_master"), TreeCategory.FORCE, 5, 
            0.0F, -70.0F, Component.translatable("skill.lightsabersfork.sith_master.title"), Component.empty(), 4, false).addParent(sith_inquisitor.getId()).addParent(sith_assassin.getId());
        SkillNode sith_council = new SkillNode(loc("sith_council"), TreeCategory.FORCE, 5, 
            0.0F, -85.0F, Component.translatable("skill.lightsabersfork.sith_council.title"), Component.empty(), 5, false).addParent(sith_master.getId());
        SkillNode sith_emperor = new SkillNode(loc("sith_emperor"), TreeCategory.FORCE, 5, 
            0.0F, -100.0F, Component.translatable("skill.lightsabersfork.sith_emperor.title"), Component.empty(), 6, true).addParent(sith_council.getId());

        registerAll(sith_hopeful, sith_acolyte, sith_warrior, sith_inquisitor, sith_assassin, sith_master, sith_council, sith_emperor);
    }

    private static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID, path);
    }
    public static Collection<SkillNode> getAllNodes() {
        return NODES.values();
    }
    private static void registerAll(SkillNode... nodes) {
        for (SkillNode node : nodes) {
            register(node);
        }
    }
}