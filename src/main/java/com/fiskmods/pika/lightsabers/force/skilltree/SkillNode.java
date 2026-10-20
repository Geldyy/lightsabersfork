package com.fiskmods.pika.lightsabers.force.skilltree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class SkillNode {
	private final ResourceLocation id;
	private final SkillTreeRegistry.TreeCategory category;
	private final int steleIndex;

	private final float offsetX;
	private final float offsetY;

	private final Component title;
	private final Component description;

	private final int pointCost;
	private final boolean usesForce;

	private final List<ResourceLocation> parents = new ArrayList<>();
	private Predicate<Player> unlockCondition = player -> true;

	private ResourceLocation navUp = null;
	private ResourceLocation navDown = null;
	private ResourceLocation navLeft = null;
	private ResourceLocation navRight = null;

	public SkillNode(ResourceLocation id, SkillTreeRegistry.TreeCategory category, int steleIndex, float offsetX,
			float offsetY, Component title, Component description, int pointCost, boolean usesForce) {
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

	public SkillNode setNavUp(ResourceLocation id) {
		this.navUp = id;
		return this;
	}

	public SkillNode setNavDown(ResourceLocation id) {
		this.navDown = id;
		return this;
	}

	public SkillNode setNavLeft(ResourceLocation id) {
		this.navLeft = id;
		return this;
	}

	public SkillNode setNavRight(ResourceLocation id) {
		this.navRight = id;
		return this;
	}

	public ResourceLocation getNavUp() {
		return navUp;
	}

	public ResourceLocation getNavDown() {
		return navDown;
	}

	public ResourceLocation getNavLeft() {
		return navLeft;
	}

	public ResourceLocation getNavRight() {
		return navRight;
	}

	public ResourceLocation getId() {
		return id;
	}

	public SkillTreeRegistry.TreeCategory getCategory() {
		return category;
	}

	public int getSteleIndex() {
		return steleIndex;
	}

	public static boolean clientUnifiedActive = false;

	public static void setClientUnifiedActive(boolean active) {
		clientUnifiedActive = active;
	}

	public float getOffsetX() {
		if (clientUnifiedActive) {
			Float ux = SkillTreeRegistry.getUnifiedX(id);
			if (ux != null)
				return ux;
		}
		return offsetX;
	}

	public float getOffsetY() {
		if (clientUnifiedActive) {
			Float uy = SkillTreeRegistry.getUnifiedY(id);
			if (uy != null)
				return uy;
		}
		return offsetY;
	}

	public Component getTitle() {
		return title;
	}

	public Component getDescription() {
		return description;
	}

	public int getPointCost() {
		return pointCost;
	}

	public boolean usesForce() {
		return usesForce;
	}

	public List<ResourceLocation> getParents() {
		if (clientUnifiedActive) {
			List<ResourceLocation> up = SkillTreeRegistry.getUnifiedParents(id);
			if (up != null)
				return up;
		}
		return parents;
	}

	public boolean isSpecialization() {
		String path = id.getPath();
		return path.equals("jedi_librarian") || path.equals("jedi_guard") || path.equals("sith_inquisitor")
				|| path.equals("sith_assassin");
	}

	public boolean isJediSkill() {
		return this.category == SkillTreeRegistry.TreeCategory.FORCE && this.steleIndex == 0;
	}

	public boolean isSithSkill() {
		return this.category == SkillTreeRegistry.TreeCategory.FORCE && this.steleIndex == 2;
	}

	public boolean isJediMasterOrHigher() {
		String p = this.id.getPath();
		return p.equals("jedi_master") || p.equals("jedi_council") || p.equals("jedi_grandmaster");
	}

	public boolean isSithMasterOrHigher() {
		String p = this.id.getPath();
		return p.equals("sith_master") || p.equals("sith_council") || p.equals("sith_emperor");
	}

	public boolean isPermanentlyLocked(PlayerForceData data) {
		if (data == null)
			return false;
		if (data.isUnifiedTreeUnlocked())
			return false;
		String path = this.id.getPath();

		if (path.equals("jedi_librarian") && "GUARD".equals(data.getJediSpecialization()))
			return true;
		if (path.equals("jedi_guard") && "LIBRARIAN".equals(data.getJediSpecialization()))
			return true;
		if (path.equals("sith_inquisitor") && "ASSASSIN".equals(data.getSithSpecialization()))
			return true;
		if (path.equals("sith_assassin") && "INQUISITOR".equals(data.getSithSpecialization()))
			return true;

		if (isSithSkill() && data.getJediRank() >= 4)
			return true;

		if (isJediSkill() && data.getSithRank() >= 4)
			return true;

		if (isJediMasterOrHigher() && data.hasAnySithSkill())
			return true;

		if (isSithMasterOrHigher() && data.hasAnyJediSkill())
			return true;

		return false;
	}

	public boolean canUnlock(Player player, PlayerForceData data) {
		if (data == null) {
			return false;
		}

		if (data.isUnifiedTreeUnlocked()) {
			return SkillTreeRegistry.canUnlockUnified(this.id, data);
		}

		if (data.hasSkill(this.id)) {
			return false;
		}

		if (isPermanentlyLocked(data)) {
			return false;
		}

		if (this.unlockCondition != null && !this.unlockCondition.test(player)) {
			return false;
		}

		if (this.parents == null || this.parents.isEmpty()) {
			return true;
		}

		for (ResourceLocation parentId : this.parents) {
			if (data.hasSkill(parentId)) {
				return true;
			}
		}

		return false;
	}
}