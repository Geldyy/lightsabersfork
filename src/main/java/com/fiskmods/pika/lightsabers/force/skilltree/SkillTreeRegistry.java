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
		SURVIVAL, LIGHTSABER, FORCE
	}

	private static final Map<ResourceLocation, SkillNode> NODES = new HashMap<>();

	public static class UnifiedPair {
		public final ResourceLocation first;
		public final ResourceLocation second;
		public final int tier;
		public final int baseCost;
		public final int surcharge;

		public UnifiedPair(ResourceLocation first, ResourceLocation second, int tier, int baseCost, int surcharge) {
			this.first = first;
			this.second = second;
			this.tier = tier;
			this.baseCost = baseCost;
			this.surcharge = surcharge;
		}

		public int getTotalCost() {
			return baseCost + surcharge;
		}
	}

	public static final Map<ResourceLocation, UnifiedPair> UNIFIED_PAIRS = new HashMap<>();
	public static final Map<ResourceLocation, Float> UNIFIED_X = new HashMap<>();
	public static final Map<ResourceLocation, Float> UNIFIED_Y = new HashMap<>();
	public static final Map<ResourceLocation, List<ResourceLocation>> UNIFIED_PARENTS = new HashMap<>();

	private static void registerUnifiedPair(String firstPath, String secondPath, int tier, int baseCost, int surcharge) {
		ResourceLocation first = loc(firstPath);
		ResourceLocation second = loc(secondPath);
		UnifiedPair pair = new UnifiedPair(first, second, tier, baseCost, surcharge);
		UNIFIED_PAIRS.put(first, pair);
		UNIFIED_PAIRS.put(second, pair);
	}

	static {
		// Tier 1: Base 1+1 = 2 FP, +1 FP Surcharge = 3 FP
		registerUnifiedPair("jedi_initiate", "sith_hopeful", 1, 2, 1);

		// Tier 2: Base 2+2 = 4 FP, +2 FP Surcharge = 6 FP
		registerUnifiedPair("jedi_padawan", "sith_acolyte", 2, 4, 2);
		registerUnifiedPair("force_heal", "force_choke", 2, 4, 2);

		// Tier 3: Base 3+3 = 6 FP, +3 FP Surcharge = 9 FP
		registerUnifiedPair("jedi_knight", "sith_warrior", 3, 6, 3);
		registerUnifiedPair("jedi_guard", "sith_inquisitor", 3, 6, 3);
		registerUnifiedPair("jedi_librarian", "sith_assassin", 3, 6, 3);

		// Tier 4: Base 4+4 = 8 FP / 2+3 = 5 FP, +4 FP Surcharge = 12 FP / 9 FP
		registerUnifiedPair("jedi_master", "sith_master", 4, 8, 4);
		registerUnifiedPair("force_barrier", "force_drain", 4, 5, 4);

		// Tier 5: Base 5+5 = 10 FP / 3+2 = 5 FP, +5 FP Surcharge = 15 FP / 10 FP
		registerUnifiedPair("jedi_council", "sith_council", 5, 10, 5);
		registerUnifiedPair("electric_judgement", "force_lightning", 5, 5, 5);

		// Tier 6: Base 6+6 = 12 FP, +6 FP Surcharge = 18 FP
		registerUnifiedPair("jedi_grandmaster", "sith_emperor", 6, 12, 6);

		// Center column coordinates for Unified Monolith (Outer Stele):
		UNIFIED_X.put(loc("jedi_initiate"), -5.0F);
		UNIFIED_Y.put(loc("jedi_initiate"), -20.0F);

		UNIFIED_X.put(loc("sith_hopeful"), 5.0F);
		UNIFIED_Y.put(loc("sith_hopeful"), -20.0F);

		UNIFIED_X.put(loc("force_heal"), 0.0F);
		UNIFIED_Y.put(loc("force_heal"), -29.0F);

		UNIFIED_X.put(loc("force_choke"), 0.0F);
		UNIFIED_Y.put(loc("force_choke"), -39.0F);

		UNIFIED_X.put(loc("jedi_master"), -5.0F);
		UNIFIED_Y.put(loc("jedi_master"), -58.0F);

		UNIFIED_X.put(loc("sith_master"), 5.0F);
		UNIFIED_Y.put(loc("sith_master"), -58.0F);

		UNIFIED_X.put(loc("sith_council"), 0.0F);
		UNIFIED_Y.put(loc("sith_council"), -70.0F);

		UNIFIED_X.put(loc("jedi_council"), 0.0F);
		UNIFIED_Y.put(loc("jedi_council"), -81.0F);

		UNIFIED_X.put(loc("jedi_grandmaster"), -5.0F);
		UNIFIED_Y.put(loc("jedi_grandmaster"), -95.0F);

		UNIFIED_X.put(loc("sith_emperor"), 5.0F);
		UNIFIED_Y.put(loc("sith_emperor"), -95.0F);

		// Connection lines for Unified Tree:
		UNIFIED_PARENTS.put(loc("jedi_initiate"), java.util.Collections.emptyList());
		UNIFIED_PARENTS.put(loc("sith_hopeful"), java.util.Collections.emptyList());

		UNIFIED_PARENTS.put(loc("jedi_padawan"), List.of(loc("jedi_initiate")));
		UNIFIED_PARENTS.put(loc("sith_acolyte"), List.of(loc("sith_hopeful")));

		UNIFIED_PARENTS.put(loc("force_heal"), List.of(loc("jedi_initiate"), loc("sith_hopeful")));
		UNIFIED_PARENTS.put(loc("force_choke"), List.of(loc("force_heal")));

		UNIFIED_PARENTS.put(loc("jedi_knight"), List.of(loc("jedi_padawan")));
		UNIFIED_PARENTS.put(loc("sith_warrior"), List.of(loc("sith_acolyte")));

		UNIFIED_PARENTS.put(loc("jedi_guard"), List.of(loc("jedi_knight")));
		UNIFIED_PARENTS.put(loc("sith_inquisitor"), List.of(loc("sith_warrior")));

		UNIFIED_PARENTS.put(loc("jedi_librarian"), List.of(loc("jedi_guard")));
		UNIFIED_PARENTS.put(loc("sith_assassin"), List.of(loc("sith_inquisitor")));

		UNIFIED_PARENTS.put(loc("jedi_master"), List.of(loc("jedi_knight"), loc("force_choke")));
		UNIFIED_PARENTS.put(loc("sith_master"), List.of(loc("sith_warrior"), loc("force_choke")));

		UNIFIED_PARENTS.put(loc("force_barrier"), List.of(loc("jedi_master")));
		UNIFIED_PARENTS.put(loc("force_drain"), List.of(loc("sith_master")));

		UNIFIED_PARENTS.put(loc("sith_council"), List.of(loc("jedi_master"), loc("sith_master")));
		UNIFIED_PARENTS.put(loc("jedi_council"), List.of(loc("sith_council")));

		UNIFIED_PARENTS.put(loc("electric_judgement"), List.of(loc("force_barrier"), loc("jedi_council")));
		UNIFIED_PARENTS.put(loc("force_lightning"), List.of(loc("force_drain"), loc("sith_council")));

		UNIFIED_PARENTS.put(loc("jedi_grandmaster"), List.of(loc("jedi_council")));
		UNIFIED_PARENTS.put(loc("sith_emperor"), List.of(loc("jedi_council")));
	}

	public static UnifiedPair getUnifiedPair(ResourceLocation id) {
		return UNIFIED_PAIRS.get(id);
	}

	public static ResourceLocation getUnifiedCounterpart(ResourceLocation id) {
		UnifiedPair pair = getUnifiedPair(id);
		if (pair == null) return null;
		return pair.first.equals(id) ? pair.second : pair.first;
	}

	public static int getUnifiedPairCost(ResourceLocation id, PlayerForceData data) {
		UnifiedPair pair = getUnifiedPair(id);
		if (pair == null) {
			SkillNode node = getNode(id);
			return node != null ? node.getPointCost() : 1;
		}
		if (data != null) {
			boolean hasFirst = data.hasSkill(pair.first);
			boolean hasSecond = data.hasSkill(pair.second);
			if (hasFirst && !hasSecond) {
				return Math.round(pair.getTotalCost() / 2.0F);
			} else if (!hasFirst && hasSecond) {
				return Math.round(pair.getTotalCost() / 2.0F);
			}
		}
		return pair.getTotalCost();
	}

	public static int getUnifiedPairSurcharge(ResourceLocation id) {
		UnifiedPair pair = getUnifiedPair(id);
		return pair != null ? pair.surcharge : 0;
	}

	public static Float getUnifiedX(ResourceLocation id) {
		return UNIFIED_X.get(id);
	}

	public static Float getUnifiedY(ResourceLocation id) {
		return UNIFIED_Y.get(id);
	}

	public static List<ResourceLocation> getUnifiedParents(ResourceLocation id) {
		return UNIFIED_PARENTS.get(id);
	}

	public static boolean canUnlockUnified(ResourceLocation id, PlayerForceData data) {
		if (data == null)
			return false;
		UnifiedPair pair = getUnifiedPair(id);
		if (pair == null)
			return false;

		if (data.hasSkill(pair.first) && data.hasSkill(pair.second)) {
			return false;
		}

		switch (pair.tier) {
		case 1:
			return true;
		case 2:
			if (pair.first.getPath().equals("jedi_padawan")) {
				return data.hasSkill(loc("jedi_initiate")) && data.hasSkill(loc("sith_hopeful"));
			} else if (pair.first.getPath().equals("force_heal")) {
				return data.hasSkill(loc("jedi_padawan")) && data.hasSkill(loc("sith_acolyte"));
			}
			return false;
		case 3:
			if (pair.first.getPath().equals("jedi_knight")) {
				return data.hasSkill(loc("force_heal")) && data.hasSkill(loc("force_choke"));
			} else {
				return data.hasSkill(loc("jedi_knight")) && data.hasSkill(loc("sith_warrior"));
			}
		case 4:
			if (pair.first.getPath().equals("jedi_master")) {
				return data.hasSkill(loc("jedi_knight")) && data.hasSkill(loc("sith_warrior"));
			} else {
				return data.hasSkill(loc("jedi_master")) && data.hasSkill(loc("sith_master"));
			}
		case 5:
			if (pair.first.getPath().equals("jedi_council")) {
				return data.hasSkill(loc("force_barrier")) && data.hasSkill(loc("force_drain"));
			} else {
				return data.hasSkill(loc("jedi_council")) && data.hasSkill(loc("sith_council"));
			}
		case 6:
			return data.hasSkill(loc("electric_judgement")) && data.hasSkill(loc("force_lightning"));
		default:
			return false;
		}
	}

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

	public static List<SkillNode> getNodesForStele(TreeCategory category, int steleIndex, PlayerForceData data) {
		List<SkillNode> list = new ArrayList<>();

		if (category == TreeCategory.FORCE && data != null && data.isUnifiedTreeUnlocked()) {
			if (steleIndex == 0) {
				for (SkillNode node : NODES.values()) {
					if (node.getCategory() == TreeCategory.FORCE
							&& (node.getSteleIndex() == 0 || node.getSteleIndex() == 2)) {
						list.add(node);
					}
				}
			}
			return list;
		}

		ResourceLocation activeRank = getActiveNeutralRankNode(data);

		for (SkillNode node : NODES.values()) {
			if (node.getCategory() == category && node.getSteleIndex() == steleIndex) {

				if (category == TreeCategory.FORCE && steleIndex == 1 && isNeutralRankNode(node.getId())) {
					if (!node.getId().equals(activeRank)) {
						continue;
					}
				}
				list.add(node);
			}
		}
		return list;
	}

	public static SkillNode getNode(ResourceLocation id) {
		return NODES.get(id);
	}

	public static boolean isNeutralRankNode(ResourceLocation id) {
		if (id == null)
			return false;
		String p = id.getPath();
		return p.equals("force_adept") || p.equals("force_channeler") || p.equals("force_veteran")
				|| p.equals("force_mastery");
	}

	public static ResourceLocation getActiveNeutralRankNode(PlayerForceData data) {
		if (data == null)
			return loc("force_adept");
		if (!data.hasSkill("lightsabersfork", "force_adept"))
			return loc("force_adept");
		if (!data.hasSkill("lightsabersfork", "force_channeler"))
			return loc("force_channeler");
		if (!data.hasSkill("lightsabersfork", "force_veteran"))
			return loc("force_veteran");
		return loc("force_mastery");
	}

	public static void init() {

		SkillNode sb_adv_speed = new SkillNode(loc("sb_adv_speed"), TreeCategory.LIGHTSABER, 0, -17.0F, -25.0F,
				Component.translatable("skill.lightsabersfork.sb_adv_speed.title"),
				Component.translatable("skill.lightsabersfork.sb_adv_speed.desc"), 2, true)
				.setNavUp(loc("sb_better_throw")).setNavRight(loc("sb_better_form"));

		SkillNode sb_better_form = new SkillNode(loc("sb_better_form"), TreeCategory.LIGHTSABER, 0, 17.0F, -25.0F,
				Component.translatable("skill.lightsabersfork.sb_better_form.title"),
				Component.translatable("skill.lightsabersfork.sb_better_form.desc"), 2, false)
				.setNavUp(loc("sb_better_throw")).setNavLeft(loc("sb_adv_speed"));

		SkillNode sb_better_throw = new SkillNode(loc("sb_better_throw"), TreeCategory.LIGHTSABER, 0, 0.0F, -45.0F,
				Component.translatable("skill.lightsabersfork.sb_better_throw.title"),
				Component.translatable("skill.lightsabersfork.sb_better_throw.desc"), 2, true)
				.addParent(sb_adv_speed.getId()).addParent(sb_better_form.getId()).setNavUp(loc("sb_ult_speed"))
				.setNavDown(loc("sb_better_form")).setNavLeft(loc("sb_better_swerve")).setNavRight(loc("sb_adv_throw"));

		SkillNode sb_better_swerve = new SkillNode(loc("sb_better_swerve"), TreeCategory.LIGHTSABER, 0, -17.0F, -50.0F,
				Component.translatable("skill.lightsabersfork.sb_better_swerve.title"),
				Component.translatable("skill.lightsabersfork.sb_better_swerve.desc"), 2, false)
				.addParent(sb_adv_speed.getId()).setNavUp(loc("sb_ult_speed")).setNavDown(loc("sb_better_throw"))
				.setNavRight(loc("sb_adv_throw"));

		SkillNode sb_adv_throw = new SkillNode(loc("sb_adv_throw"), TreeCategory.LIGHTSABER, 0, 17.0F, -55.0F,
				Component.translatable("skill.lightsabersfork.sb_adv_throw.title"),
				Component.translatable("skill.lightsabersfork.sb_adv_throw.desc"), 3, true)
				.addParent(sb_better_form.getId()).setNavUp(loc("sb_ult_speed")).setNavDown(loc("sb_better_throw"))
				.setNavLeft(loc("sb_better_swerve"));

		SkillNode sb_ult_speed = new SkillNode(loc("sb_ult_speed"), TreeCategory.LIGHTSABER, 0, 0.0F, -80.0F,
				Component.translatable("skill.lightsabersfork.sb_ult_speed.title"),
				Component.translatable("skill.lightsabersfork.sb_ult_speed.desc"), 4, true)
				.addParent(sb_better_swerve.getId()).addParent(sb_better_throw.getId()).addParent(sb_adv_throw.getId())
				.setNavDown(loc("sb_better_throw")).setNavLeft(loc("sb_better_swerve"))
				.setNavRight(loc("sb_adv_throw"));

		registerAll(sb_adv_speed, sb_better_form, sb_better_swerve, sb_better_throw, sb_adv_throw, sb_ult_speed);

		SkillNode ls_block = new SkillNode(loc("ls_block"), TreeCategory.LIGHTSABER, 1, 0.0F, -15.0F,
				Component.translatable("skill.lightsabersfork.ls_block.title"),
				Component.translatable("skill.lightsabersfork.ls_block.desc"), 1, false).setNavUp(loc("ls_adv_block"))
				.setNavLeft(loc("ls_better_block")).setNavRight(loc("ls_throw"));

		SkillNode ls_better_block = new SkillNode(loc("ls_better_block"), TreeCategory.LIGHTSABER, 1, -17.0F, -30.0F,
				Component.translatable("skill.lightsabersfork.ls_better_block.title"),
				Component.translatable("skill.lightsabersfork.ls_better_block.desc"), 1, false)
				.addParent(ls_block.getId()).setNavUp(loc("ls_adv_block")).setNavDown(loc("ls_block"))
				.setNavRight(loc("ls_throw"));

		SkillNode ls_throw = new SkillNode(loc("ls_throw"), TreeCategory.LIGHTSABER, 1, 17.0F, -40.0F,
				Component.translatable("skill.lightsabersfork.ls_throw.title"),
				Component.translatable("skill.lightsabersfork.ls_throw.desc"), 1, true).addParent(ls_block.getId())
				.setNavUp(loc("ls_adv_block")).setNavDown(loc("ls_block")).setNavLeft(loc("ls_better_block"));

		SkillNode ls_adv_block = new SkillNode(loc("ls_adv_block"), TreeCategory.LIGHTSABER, 1, 0.0F, -50.0F,
				Component.translatable("skill.lightsabersfork.ls_adv_block.title"),
				Component.translatable("skill.lightsabersfork.ls_adv_block.desc"), 2, false)
				.addParent(ls_better_block.getId()).setNavUp(loc("ls_lethal_precision")).setNavDown(loc("ls_block"))
				.setNavLeft(loc("ls_focusing")).setNavRight(loc("ls_kyber_resonance"));

		SkillNode ls_focusing = new SkillNode(loc("ls_focusing"), TreeCategory.LIGHTSABER, 1, -17.0F, -60.0F,
				Component.translatable("skill.lightsabersfork.ls_focusing.title"),
				Component.translatable("skill.lightsabersfork.ls_focusing.desc"), 2, true)
				.addParent(ls_adv_block.getId()).setNavUp(loc("ls_lethal_precision")).setNavDown(loc("ls_adv_block"))
				.setNavRight(loc("ls_kyber_resonance"));

		SkillNode ls_dmg1 = new SkillNode(loc("ls_kyber_resonance"), TreeCategory.LIGHTSABER, 1, 17.0F, -70.0F,
				Component.translatable("skill.lightsabersfork.ls_kyber_resonance.title"),
				Component.translatable("skill.lightsabersfork.ls_kyber_resonance.desc"), 2, false)
				.addParent(ls_adv_block.getId()).setNavUp(loc("ls_lethal_precision")).setNavDown(loc("ls_adv_block"))
				.setNavLeft(loc("ls_focusing"));

		SkillNode ls_dmg3 = new SkillNode(loc("ls_lethal_precision"), TreeCategory.LIGHTSABER, 1, 0.0F, -85.0F,
				Component.translatable("skill.lightsabersfork.ls_lethal_precision.title"),
				Component.translatable("skill.lightsabersfork.ls_lethal_precision.desc"), 3, false)
				.addParent(ls_focusing.getId()).addParent(ls_dmg1.getId()).setNavDown(loc("ls_adv_block"))
				.setNavLeft(loc("ls_focusing")).setNavRight(loc("ls_kyber_resonance"));

		registerAll(ls_block, ls_better_block, ls_throw, ls_adv_block, ls_focusing, ls_dmg1, ls_dmg3);

		SkillNode db_better_form = new SkillNode(loc("db_better_form"), TreeCategory.LIGHTSABER, 2, 0.0F, -20.0F,
				Component.translatable("skill.lightsabersfork.db_better_form.title"),
				Component.translatable("skill.lightsabersfork.db_better_form.desc"), 2, false)
				.setNavUp(loc("db_adv_form")).setNavLeft(loc("db_single_switch")).setNavRight(loc("db_adv_swerve"));

		SkillNode db_single_switch = new SkillNode(loc("db_single_switch"), TreeCategory.LIGHTSABER, 2, -17.0F, -40.0F,
				Component.translatable("skill.lightsabersfork.db_single_switch.title"),
				Component.translatable("skill.lightsabersfork.db_single_switch.desc"), 2, false)
				.addParent(db_better_form.getId()).setNavUp(loc("db_adv_form")).setNavRight(loc("db_adv_swerve"))
				.setNavDown(loc("db_better_form"));

		SkillNode db_adv_swerve = new SkillNode(loc("db_adv_swerve"), TreeCategory.LIGHTSABER, 2, 17.0F, -45.0F,
				Component.translatable("skill.lightsabersfork.db_adv_swerve.title"),
				Component.translatable("skill.lightsabersfork.db_adv_swerve.desc"), 2, false)
				.addParent(db_better_form.getId()).setNavUp(loc("db_adv_form")).setNavLeft(loc("db_single_switch"))
				.setNavDown(loc("db_better_form"));

		SkillNode db_adv_form = new SkillNode(loc("db_adv_form"), TreeCategory.LIGHTSABER, 2, 0.0F, -60.0F,
				Component.translatable("skill.lightsabersfork.db_adv_form.title"),
				Component.translatable("skill.lightsabersfork.db_adv_form.desc"), 3, false)
				.addParent(db_single_switch.getId()).addParent(db_adv_swerve.getId()).setNavUp(loc("db_ult_block"))
				.setNavDown(loc("db_better_form")).setNavLeft(loc("db_single_switch"))
				.setNavRight(loc("db_adv_swerve"));

		SkillNode db_ult_block = new SkillNode(loc("db_ult_block"), TreeCategory.LIGHTSABER, 2, 0.0F, -80.0F,
				Component.translatable("skill.lightsabersfork.db_ult_block.title"),
				Component.translatable("skill.lightsabersfork.db_ult_block.desc"), 4, true)
				.addParent(db_adv_form.getId()).setNavDown(loc("db_adv_form"));

		registerAll(db_better_form, db_single_switch, db_adv_swerve, db_adv_form, db_ult_block);

		SkillNode force_sensitivity = new SkillNode(loc("force_sensitivity"), TreeCategory.FORCE, 1, 0.0F, -15.0F,
				Component.translatable("skill.lightsabersfork.force_sensitivity.title"),
				Component.translatable("skill.lightsabersfork.force_sensitivity.desc"), 1, true)
				.setNavUp(loc("force_push")).setNavLeft(loc("force_push")).setNavRight(loc("force_pull"));

		SkillNode force_push = new SkillNode(loc("force_push"), TreeCategory.FORCE, 1, -14.0F, -32.0F,
				Component.translatable("power.lightsabersfork.force_push"),
				Component.translatable("power.lightsabersfork.force_push.desc"), 1, true)
				.addParent(force_sensitivity.getId()).setNavDown(loc("force_sensitivity")).setNavLeft(loc("force_dash"))
				.setNavRight(loc("force_pull")).setNavUp(loc("force_jump"));

		SkillNode force_dash = new SkillNode(loc("force_dash"), TreeCategory.FORCE, 1, -28.0F, -46.0F,
				Component.translatable("power.lightsabersfork.force_dash"),
				Component.translatable("power.lightsabersfork.force_dash.desc"), 2, true)
				.addParent(force_push.getId()).setNavDown(loc("force_push")).setNavRight(loc("force_jump"))
				.setNavUp(loc("force_repulse"));

		SkillNode force_jump = new SkillNode(loc("force_jump"), TreeCategory.FORCE, 1, -14.0F, -60.0F,
				Component.translatable("power.lightsabersfork.force_jump"),
				Component.translatable("power.lightsabersfork.force_jump.desc"), 2, true)
				.addParent(force_dash.getId()).setNavDown(loc("force_push")).setNavLeft(loc("force_dash"))
				.setNavUp(loc("force_repulse")).setNavRight(loc("force_stasis"));

		SkillNode force_repulse = new SkillNode(loc("force_repulse"), TreeCategory.FORCE, 1, -28.0F, -74.0F,
				Component.translatable("power.lightsabersfork.force_repulse"),
				Component.translatable("power.lightsabersfork.force_repulse.desc"), 3, true)
				.addParent(force_jump.getId()).setNavDown(loc("force_jump")).setNavRight(loc("force_adept"))
				.setNavUp(loc("force_adept"));

		SkillNode force_pull = new SkillNode(loc("force_pull"), TreeCategory.FORCE, 1, 14.0F, -32.0F,
				Component.translatable("power.lightsabersfork.force_pull"),
				Component.translatable("power.lightsabersfork.force_pull.desc"), 1, true)
				.addParent(force_sensitivity.getId()).setNavDown(loc("force_sensitivity")).setNavRight(loc("force_telekinesis"))
				.setNavLeft(loc("force_push")).setNavUp(loc("force_stasis"));

		SkillNode force_telekinesis = new SkillNode(loc("force_telekinesis"), TreeCategory.FORCE, 1, 28.0F, -46.0F,
				Component.translatable("power.lightsabersfork.force_telekinesis"),
				Component.translatable("power.lightsabersfork.force_telekinesis.desc"), 2, true)
				.addParent(force_pull.getId()).setNavDown(loc("force_pull")).setNavLeft(loc("force_stasis"))
				.setNavUp(loc("force_slam"));

		SkillNode force_stasis = new SkillNode(loc("force_stasis"), TreeCategory.FORCE, 1, 14.0F, -60.0F,
				Component.translatable("power.lightsabersfork.force_stasis"),
				Component.translatable("power.lightsabersfork.force_stasis.desc"), 2, true)
				.addParent(force_telekinesis.getId()).setNavDown(loc("force_pull")).setNavRight(loc("force_telekinesis"))
				.setNavUp(loc("force_slam")).setNavLeft(loc("force_jump"));

		SkillNode force_slam = new SkillNode(loc("force_slam"), TreeCategory.FORCE, 1, 28.0F, -74.0F,
				Component.translatable("power.lightsabersfork.force_slam"),
				Component.translatable("power.lightsabersfork.force_slam.desc"), 3, true)
				.addParent(force_stasis.getId()).setNavDown(loc("force_stasis")).setNavLeft(loc("force_adept"))
				.setNavUp(loc("force_adept"));

		SkillNode force_adept = new SkillNode(loc("force_adept"), TreeCategory.FORCE, 1, 0.0F, -86.0F,
				Component.translatable("skill.lightsabersfork.force_adept.title"),
				Component.translatable("skill.lightsabersfork.force_adept.desc"), 3, false)
				.addParent(force_repulse.getId()).addParent(force_slam.getId()).setNavDown(loc("force_jump"))
				.setNavLeft(loc("force_repulse")).setNavRight(loc("force_slam"));

		SkillNode force_channeler = new SkillNode(loc("force_channeler"), TreeCategory.FORCE, 1, 0.0F, -86.0F,
				Component.translatable("skill.lightsabersfork.force_channeler.title"),
				Component.translatable("skill.lightsabersfork.force_channeler.desc"), 4, false)
				.addParent(force_adept.getId()).setNavDown(loc("force_jump"))
				.setNavLeft(loc("force_repulse")).setNavRight(loc("force_slam"));

		SkillNode force_veteran = new SkillNode(loc("force_veteran"), TreeCategory.FORCE, 1, 0.0F, -86.0F,
				Component.translatable("skill.lightsabersfork.force_veteran.title"),
				Component.translatable("skill.lightsabersfork.force_veteran.desc"), 5, false)
				.addParent(force_channeler.getId()).setNavDown(loc("force_jump"))
				.setNavLeft(loc("force_repulse")).setNavRight(loc("force_slam"));

		SkillNode force_mastery = new SkillNode(loc("force_mastery"), TreeCategory.FORCE, 1, 0.0F, -86.0F,
				Component.translatable("skill.lightsabersfork.force_mastery.title"),
				Component.translatable("skill.lightsabersfork.force_mastery.desc"), 6, true)
				.addParent(force_veteran.getId()).setNavDown(loc("force_jump"))
				.setNavLeft(loc("force_repulse")).setNavRight(loc("force_slam"));

		registerAll(force_sensitivity, force_push, force_pull, force_dash, force_jump, force_stasis, force_telekinesis,
				force_repulse, force_slam, force_adept, force_channeler, force_veteran, force_mastery);

		SkillNode jedi_initiate = new SkillNode(loc("jedi_initiate"), TreeCategory.FORCE, 0, 0.0F, -20.0F,
				Component.translatable("skill.lightsabersfork.jedi_initiate.title"),
				Component.translatable("skill.lightsabersfork.jedi_initiate.desc"), 1, false)
				.addParent(loc("force_sensitivity")).setNavUp(loc("force_heal")).setNavLeft(loc("jedi_padawan"));

		SkillNode jedi_padawan = new SkillNode(loc("jedi_padawan"), TreeCategory.FORCE, 0, -17.0F, -26.0F,
				Component.translatable("skill.lightsabersfork.jedi_padawan.title"),
				Component.translatable("skill.lightsabersfork.jedi_padawan.desc"), 2, false)
				.addParent(jedi_initiate.getId()).setNavDown(loc("jedi_initiate")).setNavRight(loc("force_heal"))
				.setNavUp(loc("jedi_knight")).setNavLeft(loc("jedi_guard"));

		SkillNode force_heal = new SkillNode(loc("force_heal"), TreeCategory.FORCE, 0, 0.0F, -36.0F,
				Component.translatable("power.lightsabersfork.force_heal"),
				Component.translatable("power.lightsabersfork.force_heal.desc"), 2, true)
				.addParent(jedi_padawan.getId()).setNavDown(loc("jedi_initiate")).setNavLeft(loc("jedi_padawan"))
				.setNavUp(loc("jedi_master"));

		SkillNode jedi_knight = new SkillNode(loc("jedi_knight"), TreeCategory.FORCE, 0, -17.0F, -46.0F,
				Component.translatable("skill.lightsabersfork.jedi_knight.title"),
				Component.translatable("skill.lightsabersfork.jedi_knight.desc"), 3, false)
				.addParent(jedi_padawan.getId()).setNavDown(loc("jedi_padawan")).setNavLeft(loc("jedi_guard"))
				.setNavRight(loc("jedi_master")).setNavUp(loc("force_barrier"));

		SkillNode jedi_guard = new SkillNode(loc("jedi_guard"), TreeCategory.FORCE, 0, -34.0F, -36.0F,
				Component.translatable("skill.lightsabersfork.jedi_guard.title"),
				Component.translatable("skill.lightsabersfork.jedi_guard.desc"), 3, false)
				.addParent(jedi_knight.getId()).setNavUp(loc("jedi_librarian")).setNavRight(loc("jedi_knight"));

		SkillNode jedi_librarian = new SkillNode(loc("jedi_librarian"), TreeCategory.FORCE, 0, -34.0F, -64.0F,
				Component.translatable("skill.lightsabersfork.jedi_librarian.title"),
				Component.translatable("skill.lightsabersfork.jedi_librarian.desc"), 3, false)
				.addParent(jedi_knight.getId()).setNavDown(loc("jedi_guard")).setNavRight(loc("force_barrier"));

		SkillNode jedi_master = new SkillNode(loc("jedi_master"), TreeCategory.FORCE, 0, 0.0F, -58.0F,
				Component.translatable("skill.lightsabersfork.jedi_master.title"),
				Component.translatable("skill.lightsabersfork.jedi_master.desc"), 4, false)
				.addParent(jedi_knight.getId()).setNavDown(loc("force_heal")).setNavLeft(loc("force_barrier"))
				.setNavUp(loc("jedi_council"));

		SkillNode force_barrier = new SkillNode(loc("force_barrier"), TreeCategory.FORCE, 0, -17.0F, -70.0F,
				Component.translatable("power.lightsabersfork.force_barrier"),
				Component.translatable("power.lightsabersfork.force_barrier.desc"), 2, true)
				.addParent(jedi_master.getId()).setNavDown(loc("jedi_knight")).setNavLeft(loc("jedi_librarian"))
				.setNavRight(loc("jedi_council")).setNavUp(loc("electric_judgement"));

		SkillNode jedi_council = new SkillNode(loc("jedi_council"), TreeCategory.FORCE, 0, 0.0F, -78.0F,
				Component.translatable("skill.lightsabersfork.jedi_council.title"),
				Component.translatable("skill.lightsabersfork.jedi_council.desc"), 5, false)
				.addParent(jedi_master.getId()).setNavDown(loc("jedi_master")).setNavLeft(loc("electric_judgement"))
				.setNavUp(loc("jedi_grandmaster"));

		SkillNode electric_judgement = new SkillNode(loc("electric_judgement"), TreeCategory.FORCE, 0, -17.0F, -89.0F,
				Component.translatable("power.lightsabersfork.electric_judgement"),
				Component.translatable("power.lightsabersfork.electric_judgement.desc"), 3, true)
				.addParent(jedi_council.getId()).setNavDown(loc("force_barrier")).setNavRight(loc("jedi_council"))
				.setNavUp(loc("jedi_grandmaster"));

		SkillNode jedi_grandmaster = new SkillNode(loc("jedi_grandmaster"), TreeCategory.FORCE, 0, 0.0F, -95.0F,
				Component.translatable("skill.lightsabersfork.jedi_grandmaster.title"),
				Component.translatable("skill.lightsabersfork.jedi_grandmaster.desc"), 6, true)
				.addParent(jedi_council.getId()).setNavDown(loc("jedi_council")).setNavLeft(loc("electric_judgement"));

		registerAll(jedi_initiate, jedi_padawan, jedi_knight, jedi_guard, jedi_librarian, force_heal, force_barrier,
				jedi_master, jedi_council, electric_judgement, jedi_grandmaster);

		SkillNode sith_hopeful = new SkillNode(loc("sith_hopeful"), TreeCategory.FORCE, 2, 0.0F, -20.0F,
				Component.translatable("skill.lightsabersfork.sith_hopeful.title"),
				Component.translatable("skill.lightsabersfork.sith_hopeful.desc"), 1, false)
				.addParent(loc("force_sensitivity")).setNavUp(loc("force_choke")).setNavRight(loc("sith_acolyte"));

		SkillNode sith_acolyte = new SkillNode(loc("sith_acolyte"), TreeCategory.FORCE, 2, 17.0F, -26.0F,
				Component.translatable("skill.lightsabersfork.sith_acolyte.title"),
				Component.translatable("skill.lightsabersfork.sith_acolyte.desc"), 2, false)
				.addParent(sith_hopeful.getId()).setNavDown(loc("sith_hopeful")).setNavLeft(loc("force_choke"))
				.setNavUp(loc("sith_warrior")).setNavRight(loc("sith_inquisitor"));

		SkillNode force_choke = new SkillNode(loc("force_choke"), TreeCategory.FORCE, 2, 0.0F, -36.0F,
				Component.translatable("power.lightsabersfork.force_choke"),
				Component.translatable("power.lightsabersfork.force_choke.desc"), 2, true)
				.addParent(sith_acolyte.getId()).setNavDown(loc("sith_hopeful")).setNavRight(loc("sith_acolyte"))
				.setNavUp(loc("sith_master"));

		SkillNode sith_warrior = new SkillNode(loc("sith_warrior"), TreeCategory.FORCE, 2, 17.0F, -46.0F,
				Component.translatable("skill.lightsabersfork.sith_warrior.title"),
				Component.translatable("skill.lightsabersfork.sith_warrior.desc"), 3, false)
				.addParent(sith_acolyte.getId()).setNavDown(loc("sith_acolyte")).setNavRight(loc("sith_inquisitor"))
				.setNavLeft(loc("sith_master")).setNavUp(loc("force_drain"));

		SkillNode sith_inquisitor = new SkillNode(loc("sith_inquisitor"), TreeCategory.FORCE, 2, 34.0F, -36.0F,
				Component.translatable("skill.lightsabersfork.sith_inquisitor.title"),
				Component.translatable("skill.lightsabersfork.sith_inquisitor.desc"), 3, false)
				.addParent(sith_warrior.getId()).setNavUp(loc("sith_assassin")).setNavLeft(loc("sith_warrior"));

		SkillNode sith_assassin = new SkillNode(loc("sith_assassin"), TreeCategory.FORCE, 2, 34.0F, -64.0F,
				Component.translatable("skill.lightsabersfork.sith_assassin.title"),
				Component.translatable("skill.lightsabersfork.sith_assassin.desc"), 3, false)
				.addParent(sith_warrior.getId()).setNavDown(loc("sith_inquisitor")).setNavLeft(loc("force_drain"));

		SkillNode sith_master = new SkillNode(loc("sith_master"), TreeCategory.FORCE, 2, 0.0F, -58.0F,
				Component.translatable("skill.lightsabersfork.sith_master.title"),
				Component.translatable("skill.lightsabersfork.sith_master.desc"), 4, false)
				.addParent(sith_warrior.getId()).setNavDown(loc("force_choke")).setNavRight(loc("force_drain"))
				.setNavUp(loc("sith_council"));

		SkillNode force_drain = new SkillNode(loc("force_drain"), TreeCategory.FORCE, 2, 17.0F, -70.0F,
				Component.translatable("power.lightsabersfork.force_drain"),
				Component.translatable("power.lightsabersfork.force_drain.desc"), 3, true)
				.addParent(sith_master.getId()).setNavDown(loc("sith_warrior")).setNavRight(loc("sith_assassin"))
				.setNavLeft(loc("sith_council")).setNavUp(loc("force_lightning"));

		SkillNode sith_council = new SkillNode(loc("sith_council"), TreeCategory.FORCE, 2, 0.0F, -78.0F,
				Component.translatable("skill.lightsabersfork.sith_council.title"),
				Component.translatable("skill.lightsabersfork.sith_council.desc"), 5, false)
				.addParent(sith_master.getId()).setNavDown(loc("sith_master")).setNavRight(loc("force_lightning"))
				.setNavUp(loc("sith_emperor"));

		SkillNode force_lightning = new SkillNode(loc("force_lightning"), TreeCategory.FORCE, 2, 17.0F, -89.0F,
				Component.translatable("power.lightsabersfork.force_lightning"),
				Component.translatable("power.lightsabersfork.force_lightning.desc"), 2, true)
				.addParent(sith_council.getId()).setNavDown(loc("force_drain")).setNavLeft(loc("sith_council"))
				.setNavUp(loc("sith_emperor"));

		SkillNode sith_emperor = new SkillNode(loc("sith_emperor"), TreeCategory.FORCE, 2, 0.0F, -95.0F,
				Component.translatable("skill.lightsabersfork.sith_emperor.title"),
				Component.translatable("skill.lightsabersfork.sith_emperor.desc"), 6, true)
				.addParent(sith_council.getId()).setNavDown(loc("sith_council")).setNavRight(loc("force_lightning"));

		registerAll(sith_hopeful, sith_acolyte, force_choke, sith_warrior, sith_assassin, sith_inquisitor, sith_master,
				force_lightning, force_drain, sith_council, sith_emperor);
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