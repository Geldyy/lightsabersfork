package com.fiskmods.pika.lightsabers.client.render.force;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.logic.MeditationStateManager;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillNode;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry;
import com.fiskmods.pika.lightsabers.force.skilltree.SkillTreeRegistry.TreeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class MeditationUIRenderer {

	private static final ResourceLocation AUREBESH_FONT = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "aurebesh");

	private static int cachedLsAvailable = 0;
	private static int cachedForceAvailable = 0;
	private static int lastLsPoints = -1;
	private static int lastForcePoints = -1;
	private static int lastSkillSetHash = -1;
	private static boolean lastHolocronIsSith = false;

	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Post event) {
		if (!MeditationStateManager.isMeditating() || MeditationStateManager.isFreeMeditation()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		GuiGraphics graphics = event.getGuiGraphics();
		Font font = mc.font;

		int screenWidth = mc.getWindow().getGuiScaledWidth();
		int screenHeight = mc.getWindow().getGuiScaledHeight();
		int yPos = 40;

		double mouseX = mc.mouseHandler.xpos() * (double) screenWidth / (double) mc.getWindow().getScreenWidth();
		double mouseY = mc.mouseHandler.ypos() * (double) screenHeight / (double) mc.getWindow().getScreenHeight();

		int centerX = screenWidth / 2;
		int offset = 120;

		boolean isCategorySelection = MeditationStateManager
				.getCurrentPhase() == MeditationStateManager.MeditationPhase.CATEGORY_SELECTION;

		PlayerForceData data = mc.player != null ? PlayerForceData.get(mc.player) : null;
		boolean isHolocronLocked = (data != null && !data.canAccessHolocron());

		if (isCategorySelection && !MeditationStateManager.isConfirmationDialogOpen()) {
			if (mouseX < centerX && mouseY >= 15 && mouseY <= 75) {
				MeditationStateManager.setActiveCategory(TreeCategory.LIGHTSABER);
			} else if (mouseX > centerX && mouseY >= 15 && mouseY <= 75) {
				MeditationStateManager.setActiveCategory(TreeCategory.FORCE);
			}
		}

		TreeCategory hovered = MeditationStateManager.getActiveCategory();
		float topAlpha = isCategorySelection ? 1.0F : 0.4F;

		int availableLs = getDynamicAvailableSkills(mc.player, data, TreeCategory.LIGHTSABER);
		int availableForce = getDynamicAvailableSkills(mc.player, data, TreeCategory.FORCE);

		drawCategory(graphics, font, TreeCategory.LIGHTSABER, centerX - offset, yPos,
				hovered == TreeCategory.LIGHTSABER, topAlpha, availableLs, isHolocronLocked);
		drawCategory(graphics, font, TreeCategory.FORCE, centerX + offset, yPos, hovered == TreeCategory.FORCE,
				topAlpha, availableForce, isHolocronLocked);

		if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.NODE_SELECTION) {
			ResourceLocation selectedId = MeditationStateManager.getSelectedNodeId();
			if (selectedId != null) {
				SkillNode node = SkillTreeRegistry.getNode(selectedId);
				if (node != null) {
					renderNodeDetails(graphics, font, node, screenWidth, screenHeight);
				}
			}
		}

		if (MeditationStateManager.isConfirmationDialogOpen()) {
			renderSpecializationDialog(graphics, font, screenWidth, screenHeight, mouseX, mouseY);
		}

		boolean isHoveringInteractive = isHoveringElement(mouseX, mouseY, screenWidth, screenHeight);
		drawCustomCursor(graphics, (int) mouseX, (int) mouseY, isHoveringInteractive);
	}

	private static boolean isHoveringElement(double mouseX, double mouseY, int screenWidth, int screenHeight) {
		if (MeditationStateManager.isConfirmationDialogOpen())
			return true;
		if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.CATEGORY_SELECTION) {
			if (mouseY >= 15 && mouseY <= 75)
				return true;
		}
		if (MeditationStateManager.getCurrentPhase() == MeditationStateManager.MeditationPhase.NODE_SELECTION) {
			if (mouseY >= screenHeight - 75 && mouseY <= screenHeight - 10)
				return true;
			return MeditationStateManager.findNodeAtScreenPos(mouseX, mouseY, screenWidth, screenHeight) != null;
		}
		return false;
	}

	private static void drawCustomCursor(GuiGraphics graphics, int x, int y, boolean isHovering) {
		int alpha = isHovering ? 255 : Math.round(255 * 0.70F);
		int crossColor = (alpha << 24) | (isHovering ? 0xFFD700 : 0x00E5FF);
		int centerColor = (alpha << 24) | 0xFFFFFF;
		graphics.fill(x - 3, y, x + 4, y + 1, crossColor);
		graphics.fill(x, y - 3, x + 1, y + 4, crossColor);
		graphics.fill(x - 1, y - 1, x + 2, y + 2, centerColor);
	}

	private static void drawCategory(GuiGraphics graphics, Font font, TreeCategory category, int x, int y,
			boolean isSelected, float globalAlpha, int availableSkills, boolean isLocked) {
		Minecraft mc = Minecraft.getInstance();
		PlayerForceData data = mc.player != null ? PlayerForceData.get(mc.player) : null;
		String key;
		if (category == TreeCategory.FORCE && data != null && data.isUnifiedTreeUnlocked()) {
			key = "gui.lightsabersfork.category.force.unified";
		} else {
			key = category == TreeCategory.LIGHTSABER ? "gui.lightsabersfork.category.lightsaber"
					: "gui.lightsabersfork.category.force";
		}
		Component text = Component.translatable(key);
		Component aurebeshText = Component.translatable(key).withStyle(Style.EMPTY.withFont(AUREBESH_FONT));

		if (isLocked) {
			float aurebeshScale = 1.2F;
			int aAure = (int) ((isSelected ? 220 : 100) * globalAlpha) << 24;
			int colorAurebesh = aAure | (isSelected ? 0xF5F5DC : 0x888888);

			graphics.pose().pushPose();
			graphics.pose().scale(aurebeshScale, aurebeshScale, aurebeshScale);
			int aurebeshWidth = font.width(aurebeshText);
			float scaledX = (x - (aurebeshWidth * aurebeshScale) / 2) / aurebeshScale;
			float scaledY = y / aurebeshScale;
			graphics.drawString(font, aurebeshText, (int) scaledX, (int) scaledY, colorAurebesh, false);
			graphics.pose().popPose();
			return;
		}

		int aMain = (int) ((isSelected ? 255 : 136) * globalAlpha) << 24;
		int aAure = (int) ((isSelected ? 85 : 34) * globalAlpha) << 24;
		int aLine = (int) ((isSelected ? 255 : 68) * globalAlpha) << 24;
		int aSub = (int) ((isSelected ? 255 : 102) * globalAlpha) << 24;

		int colorMain = aMain | (isSelected ? 0xF5F5DC : 0xAAAAAA);
		int colorAurebesh = aAure | (isSelected ? 0xFFFFFF : 0xAAAAAA);
		int colorLine = aLine | (isSelected ? 0xFFFFFF : 0xAAAAAA);
		int colorSub = aSub | (isSelected ? 0xCCCCCC : 0x888888);

		int width = font.width(text);

		float aurebeshScale = 0.8F;
		graphics.pose().pushPose();
		graphics.pose().scale(aurebeshScale, aurebeshScale, aurebeshScale);
		int aurebeshWidth = font.width(aurebeshText);
		float scaledX = (x - (aurebeshWidth * aurebeshScale) / 2) / aurebeshScale;
		float scaledY = (y - 12) / aurebeshScale;
		graphics.drawString(font, aurebeshText, (int) scaledX, (int) scaledY, colorAurebesh, false);
		graphics.pose().popPose();

		float mainScale = 1.8F;
		graphics.pose().pushPose();
		graphics.pose().scale(mainScale, mainScale, mainScale);
		float mainScaledX = (x - (width * mainScale) / 2) / mainScale;
		float mainScaledY = y / mainScale;
		graphics.drawString(font, text, (int) mainScaledX, (int) mainScaledY, colorMain, true);
		graphics.pose().popPose();

		int halfWidth = (int) ((width * mainScale) / 2) + 15;
		int lineY = y + 20;

		if (isSelected) {
			graphics.fill(x - halfWidth, lineY, x - 4, lineY + 1, colorLine);
			graphics.fill(x + 4, lineY, x + halfWidth, lineY + 1, colorLine);
			graphics.fill(x - 2, lineY - 1, x + 2, lineY + 2, colorLine);
		} else {
			graphics.fill(x - halfWidth, lineY, x + halfWidth, lineY + 1, colorLine);
		}

		String translatableKey = availableSkills == 1 ? "gui.lightsabersfork.category.available.singular"
				: "gui.lightsabersfork.category.available.plural";
		Component subtext = Component.translatable(translatableKey, availableSkills);

		float subScale = 0.7F;
		graphics.pose().pushPose();
		graphics.pose().scale(subScale, subScale, subScale);
		int subWidth = font.width(subtext);
		float subScaledX = (x - (subWidth * subScale) / 2) / subScale;
		float subScaledY = (lineY + 6) / subScale;
		graphics.drawString(font, subtext, (int) subScaledX, (int) subScaledY, colorSub, true);
		graphics.pose().popPose();
	}

	private static void renderNodeDetails(GuiGraphics graphics, Font font, SkillNode node, int screenWidth,
			int screenHeight) {
		Minecraft mc = Minecraft.getInstance();
		PlayerForceData data = mc.player != null ? PlayerForceData.get(mc.player) : null;

		TreeCategory activeCategory = MeditationStateManager.getActiveCategory();
		int availablePoints = 0;
		Component pointsLabel;

		if (activeCategory == TreeCategory.FORCE) {
			availablePoints = data != null ? data.getForcePoints() : 0;
			pointsLabel = Component.translatable("gui.lightsabersfork.skilltree.force_points_available");
		} else {
			availablePoints = data != null ? data.getAbilityPoints() : 0;
			pointsLabel = Component.translatable("gui.lightsabersfork.skilltree.ability_points_available");
		}

		String pointsStr = String.valueOf(availablePoints);
		int leftX = 50;
		int bottomY = screenHeight - 60;

		graphics.pose().pushPose();
		graphics.pose().scale(2.5F, 2.5F, 2.5F);
		graphics.drawString(font, pointsStr, (int) (leftX / 2.5F), (int) ((bottomY - 10) / 2.5F), 0xFFFFFFFF, true);
		graphics.pose().popPose();

		graphics.pose().pushPose();
		graphics.pose().scale(0.7F, 0.7F, 0.7F);
		graphics.drawString(font, pointsLabel, (int) (leftX / 0.7F), (int) ((bottomY + 18) / 0.7F), 0xFFAAAAAA, true);
		graphics.pose().popPose();

		int centerX = screenWidth / 2 + 30;

		boolean isUnified = (data != null && data.isUnifiedTreeUnlocked() && node.getCategory() == TreeCategory.FORCE);
		ResourceLocation counterpartId = isUnified ? SkillTreeRegistry.getUnifiedCounterpart(node.getId()) : null;
		SkillNode counterpartNode = counterpartId != null ? SkillTreeRegistry.getNode(counterpartId) : null;

		Component title = node.getTitle();
		if (isUnified && counterpartNode != null) {
			title = Component.literal("").append(node.getTitle()).append(" §6& §f").append(counterpartNode.getTitle());
		}

		graphics.pose().pushPose();
		graphics.pose().scale(1.5F, 1.5F, 1.5F);
		graphics.drawString(font, title, (int) (centerX / 1.5F), (int) ((bottomY - 15) / 1.5F), 0xFFFFFFFF, true);
		graphics.pose().popPose();

		Component desc = node.getDescription();
		if (isUnified && counterpartNode != null) {
			desc = Component.translatable("gui.lightsabersfork.skilltree.balance_dual_pack_desc", node.getTitle(),
					counterpartNode.getTitle());
		}

		graphics.pose().pushPose();
		graphics.pose().scale(0.8F, 0.8F, 0.8F);
		graphics.drawString(font, desc, (int) (centerX / 0.8F), (int) ((bottomY + 5) / 0.8F), 0xFFAAAAAA, true);
		graphics.pose().popPose();

		boolean isUnlocked;
		if (isUnified && counterpartId != null) {
			isUnlocked = data != null && data.hasSkill(node.getId()) && data.hasSkill(counterpartId);
		} else {
			isUnlocked = data != null && data.hasSkill(node.getId());
		}

		boolean isPermanentlyLocked = node.isPermanentlyLocked(data);
		boolean canUnlock = data != null && node.canUnlock(mc.player, data);
		boolean isPower = PlayerForceData.ALL_ACTIVE_POWERS.contains(node.getId());

		boolean isSithHolocron = MeditationStateManager.isSith();
		float costMultiplier = 1.0F;
		if (node.getCategory() == TreeCategory.FORCE && !isUnified) {
			if (node.isJediSkill() && isSithHolocron) {
				costMultiplier = 2.0F;
			} else if (node.isSithSkill() && !isSithHolocron) {
				costMultiplier = 2.5F;
			}
		}
		int effectiveCost = isUnified
				? SkillTreeRegistry.getUnifiedPairCost(node.getId(), data)
				: Math.round(node.getPointCost() * costMultiplier);

		int costColor;
		Component costLabel;

		if (isUnlocked && isPower) {
			int currentTier = data.getMaxUnlockedPowerLevel(node.getId());
			int maxTier = data.getMaxTierForPower(node.getId());
			if (currentTier >= maxTier) {
				costColor = 0xFFFFD700;
				costLabel = (maxTier == 4) ? Component.translatable("gui.lightsabersfork.skilltree.tier_4_singularity")
						: Component.translatable("gui.lightsabersfork.skilltree.tier_3_master");
			} else {
				int nextTier = currentTier + 1;
				float curAmfP = data.getAmfP(node.getId());
				float reqAmfP = data.getRequiredAmfPForTier(node.getId(), nextTier);
				boolean hasProgress = curAmfP >= reqAmfP;
				boolean readyToUpgrade = data.canUpgradePowerTier(node.getId());
				int upgradeCost = data.getUpgradePointCost(node.getId());

				if (readyToUpgrade) {
					costColor = (availablePoints >= upgradeCost) ? 0xFF00FF7F : 0xFFFF5555;
					costLabel = Component.translatable("gui.lightsabersfork.skilltree.upgrade_tier",
							"I".repeat(nextTier), upgradeCost);
				} else {
					costColor = 0xFFAAAAAA;
					if (nextTier == 4 && !data.hasChosenOneAdvancement()) {
						costLabel = Component.translatable("gui.lightsabersfork.skilltree.tier_4_chosen_req",
								(int) curAmfP, (int) reqAmfP);
					} else {
						costLabel = Component.translatable("gui.lightsabersfork.skilltree.tier_progress_locked",
								"I".repeat(nextTier), (int) curAmfP, (int) reqAmfP, (hasProgress ? "§a✓" : "§c✗"));
					}
				}
			}
		} else if (isUnlocked) {
			costColor = 0xFFFFD700;
			costLabel = Component.translatable("gui.lightsabersfork.skilltree.unlocked");
		} else if (isPermanentlyLocked) {
			costColor = 0xFFFF2222;
			if (node.isSithSkill() && data != null && data.getJediRank() >= 4) {
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.locked_jedi_master");
			} else if (node.isJediSkill() && data != null && data.getSithRank() >= 4) {
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.locked_sith_master");
			} else if (node.isJediMasterOrHigher() && data != null && data.hasAnySithSkill()) {
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.locked_dark_touched");
			} else if (node.isSithMasterOrHigher() && data != null && data.hasAnyJediSkill()) {
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.locked_light_touched");
			} else {
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.locked");
			}
		} else if (canUnlock && availablePoints >= effectiveCost) {
			costColor = 0xFFCCCCCC;
			if (isUnified) {
				int surcharge = SkillTreeRegistry.getUnifiedPairSurcharge(node.getId());
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.balance_pack_cost", effectiveCost,
						surcharge);
			} else if (costMultiplier > 1.0F) {
				String multStr = (costMultiplier % 1.0F == 0) ? String.valueOf((int) costMultiplier)
						: String.valueOf(costMultiplier);
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.foreign_teaching", effectiveCost,
						multStr);
			} else {
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.point_cost", effectiveCost);
			}
		} else {
			costColor = 0xFFFF5555;
			if (isUnified) {
				int surcharge = SkillTreeRegistry.getUnifiedPairSurcharge(node.getId());
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.balance_pack_cost", effectiveCost,
						surcharge);
			} else if (costMultiplier > 1.0F) {
				String multStr = (costMultiplier % 1.0F == 0) ? String.valueOf((int) costMultiplier)
						: String.valueOf(costMultiplier);
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.foreign_teaching", effectiveCost,
						multStr);
			} else {
				costLabel = Component.translatable("gui.lightsabersfork.skilltree.point_cost", effectiveCost);
			}
		}

		int costWidth = font.width(costLabel);
		graphics.pose().pushPose();
		graphics.pose().scale(0.8F, 0.8F, 0.8F);
		float costX = (centerX - 15) / 0.8F - costWidth;
		float costY = (bottomY - 8) / 0.8F;
		graphics.drawString(font, costLabel, (int) costX, (int) costY, costColor, true);
		graphics.pose().popPose();

		graphics.fill(centerX - 10, bottomY - 15, centerX - 9, bottomY + 12, 0x44FFFFFF);
	}

	private static void renderSpecializationDialog(GuiGraphics graphics, Font font, int screenWidth, int screenHeight,
			double mouseX, double mouseY) {
		ResourceLocation pendingId = MeditationStateManager.getPendingSpecializationNode();
		if (pendingId == null)
			return;

		boolean isJedi = pendingId.getPath().startsWith("jedi");
		int dialogW = 320;
		int dialogH = 140;
		int dialogX = (screenWidth - dialogW) / 2;
		int dialogY = (screenHeight - dialogH) / 2;

		int borderColor = isJedi ? 0xFF00E5FF : 0xFFFF2222;

		graphics.fill(0, 0, screenWidth, screenHeight, 0x99000000);
		graphics.fill(dialogX, dialogY, dialogX + dialogW, dialogY + dialogH, 0xF50A0E18);
		graphics.renderOutline(dialogX, dialogY, dialogW, dialogH, borderColor);

		Component title = isJedi ? Component.translatable("gui.lightsabersfork.specialization.jedi.title")
				: Component.translatable("gui.lightsabersfork.specialization.sith.title");

		graphics.pose().pushPose();
		graphics.pose().scale(1.25F, 1.25F, 1.25F);
		int tw = font.width(title);
		graphics.drawString(font, title, (int) ((screenWidth / 2.0F - (tw * 1.25F) / 2.0F) / 1.25F),
				(int) ((dialogY + 14) / 1.25F), borderColor, true);
		graphics.pose().popPose();

		Component warning = isJedi ? Component.translatable("gui.lightsabersfork.specialization.jedi.warning")
				: Component.translatable("gui.lightsabersfork.specialization.sith.warning");

		List<FormattedCharSequence> lines = font.split(warning, dialogW - 32);
		int wy = dialogY + 38;
		for (FormattedCharSequence line : lines) {
			graphics.drawString(font, line, dialogX + 16, wy, 0xFFDDDDDD, false);
			wy += 11;
		}

		boolean hoverConfirm = mouseX >= dialogX + 15 && mouseX <= dialogX + 130 && mouseY >= dialogY + dialogH - 30
				&& mouseY <= dialogY + dialogH - 8;
		boolean hoverCancel = mouseX >= dialogX + dialogW - 130 && mouseX <= dialogX + dialogW - 15
				&& mouseY >= dialogY + dialogH - 30 && mouseY <= dialogY + dialogH - 8;

		Component confirmBtn = Component.literal(hoverConfirm ? "§a▶ [ENTER] §f" : "§a[ENTER] §f")
				.append(Component.translatable("gui.lightsabersfork.dialog.confirm"));
		Component cancelBtn = Component.literal(hoverCancel ? "§c▶ [ESC] §f" : "§c[ESC] §f")
				.append(Component.translatable("gui.lightsabersfork.dialog.cancel"));

		graphics.drawString(font, confirmBtn, dialogX + 24, dialogY + dialogH - 22,
				hoverConfirm ? 0xFFFFFFFF : 0xFF00FF7F, false);
		int cw = font.width(cancelBtn);
		graphics.drawString(font, cancelBtn, dialogX + dialogW - cw - 24, dialogY + dialogH - 22,
				hoverCancel ? 0xFFFFFFFF : 0xFFFF5555, false);
	}

	private static class SimState {
		final Set<ResourceLocation> unlocked;
		String jediSpec;
		String sithSpec;
		int forceRank;
		int jediRank;
		int sithRank;

		SimState(Set<ResourceLocation> unlocked, String jediSpec, String sithSpec, int forceRank, int jediRank,
				int sithRank) {
			this.unlocked = new HashSet<>(unlocked);
			this.jediSpec = jediSpec;
			this.sithSpec = sithSpec;
			this.forceRank = forceRank;
			this.jediRank = jediRank;
			this.sithRank = sithRank;
		}

		SimState copy() {
			return new SimState(this.unlocked, this.jediSpec, this.sithSpec, this.forceRank, this.jediRank,
					this.sithRank);
		}

		boolean hasSkill(ResourceLocation id) {
			return unlocked.contains(id);
		}

		boolean hasAnyJediSkill() {
			for (ResourceLocation rl : unlocked) {
				String p = rl.getPath();
				if (p.startsWith("jedi_") || p.equals("force_heal") || p.equals("force_barrier") || p.equals("force_fortify")
						|| p.equals("electric_judgement") || p.equals("electric_judgment")) {
					return true;
				}
			}
			return false;
		}

		boolean hasAnySithSkill() {
			for (ResourceLocation rl : unlocked) {
				String p = rl.getPath();
				if (p.startsWith("sith_") || p.equals("force_choke") || p.equals("force_lightning")
						|| p.equals("force_drain")) {
					return true;
				}
			}
			return false;
		}

		void unlock(SkillNode node) {
			unlocked.add(node.getId());
			String path = node.getId().getPath();

			if (path.equals("force_adept"))
				this.forceRank = Math.max(this.forceRank, 1);
			else if (path.equals("force_channeler"))
				this.forceRank = Math.max(this.forceRank, 2);
			else if (path.equals("force_veteran"))
				this.forceRank = Math.max(this.forceRank, 3);
			else if (path.equals("force_mastery"))
				this.forceRank = Math.max(this.forceRank, 4);

			else if (path.equals("jedi_initiate"))
				this.jediRank = Math.max(this.jediRank, 1);
			else if (path.equals("jedi_padawan"))
				this.jediRank = Math.max(this.jediRank, 2);
			else if (path.equals("jedi_knight"))
				this.jediRank = Math.max(this.jediRank, 3);
			else if (path.equals("jedi_librarian")) {
				this.jediRank = Math.max(this.jediRank, 3);
				this.jediSpec = "LIBRARIAN";
			} else if (path.equals("jedi_guard")) {
				this.jediRank = Math.max(this.jediRank, 3);
				this.jediSpec = "GUARD";
			} else if (path.equals("jedi_master"))
				this.jediRank = Math.max(this.jediRank, 4);
			else if (path.equals("jedi_council"))
				this.jediRank = Math.max(this.jediRank, 5);
			else if (path.equals("jedi_grandmaster"))
				this.jediRank = Math.max(this.jediRank, 6);

			else if (path.equals("sith_hopeful"))
				this.sithRank = Math.max(this.sithRank, 1);
			else if (path.equals("sith_acolyte"))
				this.sithRank = Math.max(this.sithRank, 2);
			else if (path.equals("sith_warrior"))
				this.sithRank = Math.max(this.sithRank, 3);
			else if (path.equals("sith_inquisitor")) {
				this.sithRank = Math.max(this.sithRank, 3);
				this.sithSpec = "INQUISITOR";
			} else if (path.equals("sith_assassin")) {
				this.sithRank = Math.max(this.sithRank, 3);
				this.sithSpec = "ASSASSIN";
			} else if (path.equals("sith_master"))
				this.sithRank = Math.max(this.sithRank, 4);
			else if (path.equals("sith_council"))
				this.sithRank = Math.max(this.sithRank, 5);
			else if (path.equals("sith_emperor"))
				this.sithRank = Math.max(this.sithRank, 6);
		}
	}

	private static int getDynamicAvailableSkills(Player player, PlayerForceData data, TreeCategory category) {
		if (player == null || data == null)
			return 0;

		int currentAp = data.getAbilityPoints();
		int currentFp = data.getForcePoints();
		int skillHash = data.getUnlockedSkills().hashCode();
		boolean isSith = MeditationStateManager.isSith();

		if (category == TreeCategory.LIGHTSABER) {
			if (currentAp == lastLsPoints && skillHash == lastSkillSetHash) {
				return cachedLsAvailable;
			}
		} else {
			if (currentFp == lastForcePoints && skillHash == lastSkillSetHash && isSith == lastHolocronIsSith) {
				return cachedForceAvailable;
			}
		}

		int points = (category == TreeCategory.FORCE) ? currentFp : currentAp;
		int result = 0;

		if (points > 0) {
			List<SkillNode> unowned = new ArrayList<>();
			for (SkillNode node : SkillTreeRegistry.getAllNodes()) {
				if (node.getCategory() == category && !data.hasSkill(node.getId())) {
					unowned.add(node);
				}
			}

			if (!unowned.isEmpty()) {
				SimState startState = new SimState(data.getUnlockedSkills(), data.getJediSpecialization(),
						data.getSithSpecialization(), data.getForceRank(), data.getJediRank(), data.getSithRank());
				Map<String, Integer> memo = new HashMap<>();
				result = dfsMaxSkills(player, startState, points, category, isSith, unowned, memo);
			}
		}

		if (category == TreeCategory.LIGHTSABER) {
			cachedLsAvailable = result;
			lastLsPoints = currentAp;
		} else {
			cachedForceAvailable = result;
			lastForcePoints = currentFp;
			lastHolocronIsSith = isSith;
		}
		lastSkillSetHash = skillHash;

		return result;
	}

	private static int dfsMaxSkills(Player player, SimState state, int points, TreeCategory category,
			boolean isSithHolocron, List<SkillNode> unowned, Map<String, Integer> memo) {
		if (points <= 0)
			return 0;

		String key = state.unlocked.size() + ":" + points + ":" + state.jediSpec + ":" + state.sithSpec + ":"
				+ state.jediRank + ":" + state.sithRank;
		if (memo.containsKey(key)) {
			return memo.get(key);
		}

		int best = 0;

		for (SkillNode candidate : unowned) {
			if (canUnlockSim(player, candidate, state)) {
				int cost = getNodeCost(candidate, category, isSithHolocron);
				if (points >= cost) {
					SimState nextState = state.copy();
					nextState.unlock(candidate);

					int result = 1
							+ dfsMaxSkills(player, nextState, points - cost, category, isSithHolocron, unowned, memo);
					if (result > best) {
						best = result;
					}
				}
			}
		}

		memo.put(key, best);
		return best;
	}

	private static boolean canUnlockSim(Player player, SkillNode node, SimState state) {
		if (state.hasSkill(node.getId()))
			return false;

		String path = node.getId().getPath();

		if (path.equals("jedi_librarian") && "GUARD".equals(state.jediSpec))
			return false;
		if (path.equals("jedi_guard") && "LIBRARIAN".equals(state.jediSpec))
			return false;
		if (path.equals("sith_inquisitor") && "ASSASSIN".equals(state.sithSpec))
			return false;
		if (path.equals("sith_assassin") && "INQUISITOR".equals(state.sithSpec))
			return false;

		if (node.isSithSkill() && state.jediRank >= 4)
			return false;
		if (node.isJediSkill() && state.sithRank >= 4)
			return false;
		if (node.isJediMasterOrHigher() && state.hasAnySithSkill())
			return false;
		if (node.isSithMasterOrHigher() && state.hasAnyJediSkill())
			return false;

		if (node.getParents() == null || node.getParents().isEmpty()) {
			return true;
		}
		for (ResourceLocation parentId : node.getParents()) {
			if (state.hasSkill(parentId)) {
				return true;
			}
		}
		return false;
	}

	private static int getNodeCost(SkillNode node, TreeCategory category, boolean isSithHolocron) {
		if (category != TreeCategory.FORCE) {
			return node.getPointCost();
		}
		float multiplier = 1.0F;
		if (node.isJediSkill() && isSithHolocron)
			multiplier = 2.0F;
		else if (node.isSithSkill() && !isSithHolocron)
			multiplier = 2.5F;
		return Math.round(node.getPointCost() * multiplier);
	}
}