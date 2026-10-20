package com.fiskmods.pika.lightsabers.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.client.ALKeyBinds;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;
import com.fiskmods.pika.lightsabers.network.payload.SelectPowerPayload;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.network.PacketDistributor;

public class ForceRadialScreen extends Screen {

	private static final ResourceLocation AUREBESH_FONT = ResourceLocation
			.fromNamespaceAndPath(AdvancedLightsabers.MODID, "aurebesh");

	public enum Alignment {
		NEUTRAL("gui.lightsabersfork.radial.alignment.neutral", 0xFFE0E0E0),
		JEDI("gui.lightsabersfork.radial.alignment.jedi", 0xFF00E5FF),
		SITH("gui.lightsabersfork.radial.alignment.sith", 0xFFFF2222);

		public final String translationKey;
		public final int color;

		Alignment(String translationKey, int color) {
			this.translationKey = translationKey;
			this.color = color;
		}

		public Component getDisplayName() {
			return Component.translatable(this.translationKey);
		}
	}

	private enum MenuPhase {
		CATEGORY_SELECT, POWER_SELECT
	}

	private MenuPhase currentPhase = MenuPhase.CATEGORY_SELECT;
	private Alignment selectedAlignment = Alignment.NEUTRAL;

	private final List<Alignment> availableCategories = new ArrayList<>();
	private final Map<Alignment, List<ResourceLocation>> categorizedPowers = new HashMap<>();
	private final Map<ResourceLocation, Integer> localPowerLevels = new HashMap<>();

	private int hoveredIndex = -1;

	public ForceRadialScreen(List<ResourceLocation> powers) {
		super(Component.translatable("gui.lightsabersfork.select.title"));
		categorizePowers(powers);
	}

	private void categorizePowers(List<ResourceLocation> powers) {
		categorizedPowers.put(Alignment.NEUTRAL, new ArrayList<>());
		categorizedPowers.put(Alignment.JEDI, new ArrayList<>());
		categorizedPowers.put(Alignment.SITH, new ArrayList<>());

		for (ResourceLocation p : powers) {
			Alignment align = getAlignmentForPower(p);
			categorizedPowers.get(align).add(p);
		}

		for (Alignment align : Alignment.values()) {
			if (!categorizedPowers.get(align).isEmpty()) {
				availableCategories.add(align);
			}
		}

		if (availableCategories.size() <= 1) {
			this.selectedAlignment = availableCategories.isEmpty() ? Alignment.NEUTRAL : availableCategories.get(0);
			this.currentPhase = MenuPhase.POWER_SELECT;
		} else {
			this.currentPhase = MenuPhase.CATEGORY_SELECT;
		}
	}

	private static Alignment getAlignmentForPower(ResourceLocation powerId) {
		String path = powerId.getPath();
		return switch (path) {
		case "force_heal", "electric_judgement", "force_barrier", "force_fortify" -> Alignment.JEDI;
		case "force_lightning", "force_choke", "force_drain" -> Alignment.SITH;
		default -> Alignment.NEUTRAL;
		};
	}

	@Override
	protected void init() {
		super.init();
		if (this.minecraft != null && this.minecraft.player != null) {
			PlayerForceData data = PlayerForceData.get(this.minecraft.player);
			if (data != null) {
				for (List<ResourceLocation> list : categorizedPowers.values()) {
					for (ResourceLocation power : list) {
						localPowerLevels.put(power, data.getPowerLevel(power));
					}
				}
			}
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);

		if (this.minecraft == null || this.minecraft.player == null) {
			this.onClose();
			return;
		}

		PlayerForceData data = PlayerForceData.get(this.minecraft.player);
		if (data == null)
			return;

		int centerX = this.width / 2;
		int centerY = this.height / 2 - 20;

		if (currentPhase == MenuPhase.CATEGORY_SELECT) {
			renderCategorySelectWheel(graphics, mouseX, mouseY, centerX, centerY);
		} else {
			renderPowerOrbitWheel(graphics, data, mouseX, mouseY, centerX, centerY);
			renderBottomDetailsBar(graphics, data);
		}
	}

	private void renderCategorySelectWheel(GuiGraphics graphics, int mouseX, int mouseY, int cx, int cy) {
		int outerRadius = 80;
		int innerRadius = 18;

		double dX = mouseX - cx;
		double dY = mouseY - cy;
		double distSqr = (dX * dX) + (dY * dY);

		double angle = Math.toDegrees(Math.atan2(dY, dX));
		int catCount = availableCategories.size();

		if (distSqr <= outerRadius * outerRadius && distSqr >= innerRadius * innerRadius) {
			if (catCount == 3) {
				if (angle >= -150.0 && angle < -30.0) {
					this.hoveredIndex = availableCategories.indexOf(Alignment.NEUTRAL);
				} else if (angle >= -30.0 && angle < 90.0) {
					this.hoveredIndex = availableCategories.indexOf(Alignment.JEDI);
				} else {
					this.hoveredIndex = availableCategories.indexOf(Alignment.SITH);
				}
			} else if (catCount == 2) {
				this.hoveredIndex = (angle < 0.0) ? 0 : 1;
			}
		} else {
			this.hoveredIndex = -1;
		}

		drawFilledCircle(graphics, cx, cy, outerRadius, 0x55000000);

		if (this.hoveredIndex >= 0 && this.hoveredIndex < catCount) {
			Alignment hAlign = availableCategories.get(this.hoveredIndex);
			int glowColor = (hAlign.color & 0x00FFFFFF) | 0x2E000000;

			if (catCount == 3) {
				if (hAlign == Alignment.NEUTRAL)
					drawSector(graphics, cx, cy, innerRadius, outerRadius, 210, 330, glowColor);
				else if (hAlign == Alignment.JEDI)
					drawSector(graphics, cx, cy, innerRadius, outerRadius, 330, 90, glowColor);
				else if (hAlign == Alignment.SITH)
					drawSector(graphics, cx, cy, innerRadius, outerRadius, 90, 210, glowColor);
			} else if (catCount == 2) {
				if (this.hoveredIndex == 0)
					drawSector(graphics, cx, cy, innerRadius, outerRadius, 180, 360, glowColor);
				else
					drawSector(graphics, cx, cy, innerRadius, outerRadius, 0, 180, glowColor);
			}
		}

		drawCircleOutline(graphics, cx, cy, outerRadius, 0xFFFFFFFF);

		if (catCount == 3) {
			drawSpokeLine(graphics, cx, cy, innerRadius, outerRadius, -150);
			drawSpokeLine(graphics, cx, cy, innerRadius, outerRadius, -30);
			drawSpokeLine(graphics, cx, cy, innerRadius, outerRadius, 90);

			drawCategoryText(graphics, Alignment.NEUTRAL, cx, cy - 45);
			drawCategoryText(graphics, Alignment.JEDI, cx + 40, cy + 28);
			drawCategoryText(graphics, Alignment.SITH, cx - 40, cy + 28);
		} else if (catCount == 2) {
			drawSpokeLine(graphics, cx, cy, innerRadius, outerRadius, 180);
			drawSpokeLine(graphics, cx, cy, innerRadius, outerRadius, 0);

			drawCategoryText(graphics, availableCategories.get(0), cx, cy - 40);
			drawCategoryText(graphics, availableCategories.get(1), cx, cy + 30);
		}

		drawFilledCircle(graphics, cx, cy, innerRadius, 0xEE05070D);
		drawCircleOutline(graphics, cx, cy, innerRadius, 0xFFFFFFFF);
	}

	private void drawSpokeLine(GuiGraphics graphics, int cx, int cy, int rInner, int rOuter, double deg) {
		double rad = Math.toRadians(deg);
		int x1 = (int) Math.round(cx + Math.cos(rad) * rInner);
		int y1 = (int) Math.round(cy + Math.sin(rad) * rInner);
		int x2 = (int) Math.round(cx + Math.cos(rad) * rOuter);
		int y2 = (int) Math.round(cy + Math.sin(rad) * rOuter);
		drawLine(graphics, x1, y1, x2, y2, 0xFFFFFFFF);
	}

	private void drawCategoryText(GuiGraphics graphics, Alignment align, int x, int y) {
		int idx = availableCategories.indexOf(align);
		boolean isHovered = (idx >= 0 && idx == this.hoveredIndex);
		int color = isHovered ? align.color : 0xFFAAAAAA;
		Component labelComp = align.getDisplayName();
		int w = this.font.width(labelComp);
		graphics.drawString(this.font, labelComp, x - (w / 2), y, color, true);
	}

	private void renderPowerOrbitWheel(GuiGraphics graphics, PlayerForceData data, int mouseX, int mouseY, int cx,
			int cy) {
		List<ResourceLocation> powers = categorizedPowers.get(this.selectedAlignment);
		if (powers.isEmpty())
			return;

		int circleRadius = 75;
		double dX = mouseX - cx;
		double dY = mouseY - cy;
		double distSqr = (dX * dX) + (dY * dY);
		double mouseAngle = (Math.toDegrees(Math.atan2(dY, dX)) + 360.0) % 360.0;

		int count = powers.size();
		double step = 360.0 / count;

		if (distSqr >= 25 * 25 && distSqr <= (circleRadius + 45) * (circleRadius + 45)) {
			this.hoveredIndex = (int) Math.floor(mouseAngle / step) % count;
		} else {
			this.hoveredIndex = -1;
		}

		drawFilledCircle(graphics, cx, cy, circleRadius + 18, 0x33000000);
		drawCircleOutline(graphics, cx, cy, circleRadius, 0x66FFFFFF);

		drawFilledCircle(graphics, cx, cy, 26, 0xCC05070D);
		drawCircleOutline(graphics, cx, cy, 26, 0xFF00BFFF);

		for (int i = 0; i < count; i++) {
			ResourceLocation powerId = powers.get(i);
			double angle = Math.toRadians((i + 0.5) * step);

			int iconX = (int) (cx + Math.cos(angle) * circleRadius);
			int iconY = (int) (cy + Math.sin(angle) * circleRadius);

			boolean isHovered = (i == this.hoveredIndex);
			int iconSize = isHovered ? 26 : 18;

			int boxX = iconX - (iconSize / 2);
			int boxY = iconY - (iconSize / 2);
			int borderCol = isHovered ? this.selectedAlignment.color : 0xFF555555;
			graphics.fill(boxX, boxY, boxX + iconSize, boxY + iconSize, isHovered ? 0xDD111827 : 0xAA000000);
			graphics.renderOutline(boxX, boxY, iconSize, iconSize, borderCol);

			renderPowerIcon(graphics, powerId, boxX + 2, boxY + 2, iconSize - 4);

			double outwardX = Math.cos(angle);
			double outwardY = Math.sin(angle);
			int textDist = (iconSize / 2) + 8;
			int aureX = (int) (iconX + outwardX * textDist);
			int aureY = (int) (iconY + outwardY * textDist);

			Component aureName = Component.translatable("power.lightsabersfork." + powerId.getPath())
					.withStyle(Style.EMPTY.withFont(AUREBESH_FONT));

			int aw = this.font.width(aureName);
			int ax = outwardX < -0.2 ? aureX - aw : (outwardX > 0.2 ? aureX : aureX - (aw / 2));
			int ay = aureY - 4;

			graphics.drawString(this.font, aureName, ax, ay, isHovered ? 0xFFFFFFFF : 0x88AAAAAA, false);
		}
	}

	private void renderBottomDetailsBar(GuiGraphics graphics, PlayerForceData data) {
		List<ResourceLocation> powers = categorizedPowers.get(this.selectedAlignment);
		if (powers.isEmpty() || this.hoveredIndex < 0 || this.hoveredIndex >= powers.size()) {
			return;
		}

		ResourceLocation activePower = powers.get(this.hoveredIndex);

		int barW = 340;
		int barH = 58;
		int barX = (this.width - barW) / 2;
		int barY = this.height - barH - 10;

		graphics.fill(barX, barY, barX + barW, barY + barH, 0xEE090D16);
		graphics.renderOutline(barX, barY, barW, barH, 0xFFFFFFFF);

		int curLvl = localPowerLevels.getOrDefault(activePower, 1);
		int maxLvl = data.getMaxUnlockedPowerLevel(activePower);
		int maxPossible = data.getMaxTierForPower(activePower);

		int lvlX = barX + 16;
		for (int l = maxPossible; l >= 1; l--) {
			int ly = barY + 6 + (maxPossible - l) * (maxPossible == 4 ? 12 : 15);
			boolean isCur = (l == curLvl);
			boolean isUnlocked = (l <= maxLvl);

			float scale = isCur ? 1.4F : 0.85F;
			int col = isCur ? 0xFFFFD700 : (isUnlocked ? 0xFF888888 : 0xFF333333);

			graphics.pose().pushPose();
			graphics.pose().scale(scale, scale, scale);
			graphics.drawString(this.font, String.valueOf(l), (int) (lvlX / scale), (int) (ly / scale), col, true);
			graphics.pose().popPose();
		}

		int infoX = barX + 38;
		Component title = Component.translatable("power.lightsabersfork." + activePower.getPath());
		graphics.drawString(this.font, title, infoX, barY + 8, 0xFFFFFFFF, true);

		int titleW = this.font.width(title);
		int infoLevel = data.getInfoUnlockLevel(activePower);

		Component statsComp = getPowerStatsComponent(data, activePower, curLvl, infoLevel);
		graphics.drawString(this.font, statsComp, infoX + titleW + 8, barY + 8, 0xFF00FF7F, false);

		Component descComp = getPureDescription(data, activePower, infoLevel);
		int descMaxW = (barX + barW - 95) - infoX;
		List<FormattedCharSequence> descLines = this.font.split(descComp, descMaxW);
		int descY = barY + 22;
		for (int lineIdx = 0; lineIdx < descLines.size() && lineIdx < 3; lineIdx++) {
			graphics.drawString(this.font, descLines.get(lineIdx), infoX, descY, 0xFFAAAAAA, false);
			descY += 10;
		}

		int alignX = barX + barW - 90;
		Alignment powerAlign = getAlignmentForPower(activePower);

		renderAlignmentLabel(graphics, Alignment.JEDI.getDisplayName(), alignX, barY + 8, powerAlign == Alignment.JEDI,
				Alignment.JEDI.color);
		renderAlignmentLabel(graphics, Alignment.NEUTRAL.getDisplayName(), alignX, barY + 23,
				powerAlign == Alignment.NEUTRAL, Alignment.NEUTRAL.color);
		renderAlignmentLabel(graphics, Alignment.SITH.getDisplayName(), alignX, barY + 38, powerAlign == Alignment.SITH,
				Alignment.SITH.color);

		int bigIconX = barX + barW - 46;
		int bigIconY = barY + 11;
		int bigBoxSize = 36;
		graphics.fill(bigIconX, bigIconY, bigIconX + bigBoxSize, bigIconY + bigBoxSize, 0xAA000000);
		graphics.renderOutline(bigIconX, bigIconY, bigBoxSize, bigBoxSize, powerAlign.color);
		renderPowerIcon(graphics, activePower, bigIconX + 3, bigIconY + 3, bigBoxSize - 6);
	}

	private Component getPowerStatsComponent(PlayerForceData data, ResourceLocation powerId, int level, int infoLevel) {
		float amfi = data != null ? data.getAmfI(powerId) : 0;
		if (infoLevel < 1) {
			return Component.translatable("gui.lightsabersfork.radial.stats.unknown", (int) amfi);
		}

		String path = powerId.getPath();
		boolean isChanneled = path.equals("force_lightning") || path.equals("electric_judgement")
				|| path.equals("force_choke") || path.equals("force_drain")
				|| path.equals("force_barrier") || path.equals("force_fortify");

		int cost = switch (path) {
		case "force_push" -> (level >= 4) ? 50 : ((level >= 3) ? 22 : ((level >= 2) ? 7 : 5));
		case "force_pull" -> (level >= 3) ? 22 : ((level >= 2) ? 7 : 5);
		case "force_dash" -> (level >= 3) ? 20 : ((level >= 2) ? 15 : 10);
		case "force_slam" -> (level >= 3) ? 45 : ((level >= 2) ? 30 : 20);
		case "force_jump" -> (level >= 3) ? 20 : ((level >= 2) ? 15 : 10);
		case "force_repulse" -> (level >= 3) ? 35 : ((level >= 2) ? 25 : 15);
		case "force_stasis" -> (level >= 3) ? 35 : ((level >= 2) ? 28 : 12);
		case "force_telekinesis" -> (level >= 3) ? 20 : ((level >= 2) ? 5 : 3);
		case "force_heal" -> 35;
		case "force_barrier", "force_fortify" -> (level >= 3) ? 1 : ((level == 2) ? 4 : 3);
		default -> 6;
		};

		Component costComp = Component.translatable(isChanneled ? "gui.lightsabersfork.radial.cost.channeled" : "gui.lightsabersfork.radial.cost.instant", cost);

		Component cdComp = switch (path) {
		case "force_push" -> Component.translatable("gui.lightsabersfork.radial.cooldown", ((level >= 4) ? "15.0" : ((level >= 2) ? "5.0" : ((level >= 3) ? "4.0" : "3.0"))));
		case "force_pull" -> Component.translatable("gui.lightsabersfork.radial.cooldown", ((level >= 2) ? "5.0" : ((level >= 3) ? "4.0" : "3.0")));
		case "force_dash" -> Component.translatable("gui.lightsabersfork.radial.cooldown", ((level >= 2) ? "3.0" : "2.0"));
		case "force_jump" -> Component.translatable("gui.lightsabersfork.radial.cooldown", ((level >= 3) ? "3.0" : "5.0"));
		case "force_slam" -> Component.translatable("gui.lightsabersfork.radial.cooldown", "8.0");
		case "force_repulse" -> Component.translatable("gui.lightsabersfork.radial.cooldown", ((level >= 3) ? "10.0" : "5.0"));
		case "force_stasis" -> Component.translatable("gui.lightsabersfork.radial.cooldown", ((level >= 3) ? "12.0" : "7.0"));
		case "force_telekinesis" -> Component.translatable("gui.lightsabersfork.radial.cooldown", ((level >= 2) ? "4.0" : "2.0"));
		case "force_heal" -> Component.translatable("gui.lightsabersfork.radial.cooldown", "15.0");
		default -> Component.translatable("gui.lightsabersfork.radial.channeled");
		};

		return Component.translatable("gui.lightsabersfork.radial.stats.format", costComp, cdComp);
	}

	private Component getPureDescription(PlayerForceData data, ResourceLocation powerId, int infoLevel) {
		String path = powerId.getPath();
		float amfi = data != null ? data.getAmfI(powerId) : 0;

		if (infoLevel < 1) {
			return Component.translatable("gui.lightsabersfork.radial.amfi.shrouded", (int) amfi);
		}

		MutableComponent desc = Component.translatable("power.lightsabersfork." + path + ".desc").copy();

		if (infoLevel == 1) {
			desc.append(Component.translatable("gui.lightsabersfork.radial.amfi.tier2", (int) amfi));
		} else if (infoLevel == 2) {
			desc.append(Component.translatable("gui.lightsabersfork.radial.amfi.tier3", (int) amfi));
		} else if (infoLevel == 3) {
			if (data != null && data.getMaxTierForPower(powerId) >= 4) {
				desc.append(Component.translatable("gui.lightsabersfork.radial.amfi.tier4_hint", (int) amfi));
			} else {
				desc.append(Component.translatable("gui.lightsabersfork.radial.amfi.mastery"));
			}
		} else if (infoLevel >= 4) {
			if (path.equals("force_push")) {
				desc.append(Component.translatable("gui.lightsabersfork.radial.amfi.singularity_crush"));
			}
		}

		return desc;
	}

	private void renderAlignmentLabel(GuiGraphics graphics, Component text, int x, int y, boolean isActive,
			int activeColor) {
		float scale = isActive ? 1.2F : 0.85F;
		int col = isActive ? activeColor : 0x55888888;

		graphics.pose().pushPose();
		graphics.pose().scale(scale, scale, scale);
		graphics.drawString(this.font, text, (int) (x / scale), (int) (y / scale), col, true);
		graphics.pose().popPose();
	}

	private static final Map<ResourceLocation, ResourceLocation> ICON_CACHE = new HashMap<>();

	private static ResourceLocation getPowerIcon(ResourceLocation powerId) {
		if (powerId == null) {
			return null;
		}
		return ICON_CACHE.computeIfAbsent(powerId, id -> {
			String path = id.getPath();
			if ("electric_judgment".equals(path)) {
				path = "electric_judgement";
			}
			ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
					"textures/gui/skills/" + path + ".png");

			Minecraft mc = Minecraft.getInstance();
			if (mc != null && mc.getResourceManager() != null) {
				if (mc.getResourceManager().getResource(loc).isPresent()) {
					return loc;
				}
				if ("force_fortify".equals(path)) {
					ResourceLocation alt = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
							"textures/gui/skills/force_barrier.png");
					if (mc.getResourceManager().getResource(alt).isPresent()) {
						return alt;
					}
				} else if ("force_barrier".equals(path)) {
					ResourceLocation alt = ResourceLocation.fromNamespaceAndPath(AdvancedLightsabers.MODID,
							"textures/gui/skills/force_fortify.png");
					if (mc.getResourceManager().getResource(alt).isPresent()) {
						return alt;
					}
				}
				return null;
			}
			return loc;
		});
	}

	private void renderPowerIcon(GuiGraphics graphics, ResourceLocation powerId, int x, int y, int size) {
		ResourceLocation iconTex = getPowerIcon(powerId);

		try {
			if (iconTex != null) {
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
				graphics.blit(iconTex, x, y, 0, 0, size, size, size, size);
				graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.disableBlend();
			} else {
				drawFallbackIcon(graphics, powerId, x, y, size);
			}
		} catch (Exception e) {
			drawFallbackIcon(graphics, powerId, x, y, size);
		}
	}

	private void drawFallbackIcon(GuiGraphics graphics, ResourceLocation powerId, int x, int y, int size) {
		String name = (powerId != null) ? powerId.getPath() : "?";
		String initial = name.substring(0, Math.min(2, name.length())).toUpperCase();
		graphics.fill(x, y, x + size, y + size, 0x44FFFFFF);
		int tw = this.font.width(initial);
		graphics.drawString(this.font, initial, x + (size - tw) / 2, y + (size - 8) / 2, 0xFFFFFFFF, false);
	}

	private void drawFilledCircle(GuiGraphics graphics, int cx, int cy, int radius, int color) {
		for (int dy = -radius; dy <= radius; dy++) {
			int dx = (int) Math.sqrt(radius * radius - dy * dy);
			graphics.fill(cx - dx, cy + dy, cx + dx + 1, cy + dy + 1, color);
		}
	}

	private void drawCircleOutline(GuiGraphics graphics, int cx, int cy, int radius, int color) {
		int segments = 80;
		double step = (2 * Math.PI) / segments;
		for (int i = 0; i < segments; i++) {
			double a1 = i * step;
			double a2 = (i + 1) * step;
			int x1 = (int) Math.round(cx + Math.cos(a1) * radius);
			int y1 = (int) Math.round(cy + Math.sin(a1) * radius);
			int x2 = (int) Math.round(cx + Math.cos(a2) * radius);
			int y2 = (int) Math.round(cy + Math.sin(a2) * radius);
			drawLine(graphics, x1, y1, x2, y2, color);
		}
	}

	private void drawSector(GuiGraphics graphics, int cx, int cy, int rIn, int rOut, double startDeg, double endDeg,
			int color) {
		for (int y = -rOut; y <= rOut; y++) {
			for (int x = -rOut; x <= rOut; x++) {
				int distSq = x * x + y * y;
				if (distSq >= rIn * rIn && distSq <= rOut * rOut) {
					double deg = (Math.toDegrees(Math.atan2(y, x)) + 360.0) % 360.0;
					boolean inSector;
					if (startDeg <= endDeg) {
						inSector = deg >= startDeg && deg < endDeg;
					} else {
						inSector = deg >= startDeg || deg < endDeg;
					}
					if (inSector) {
						graphics.fill(cx + x, cy + y, cx + x + 1, cy + y + 1, color);
					}
				}
			}
		}
	}

	private void drawLine(GuiGraphics graphics, int x1, int y1, int x2, int y2, int color) {
		int steps = (int) Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
		if (steps == 0) {
			graphics.fill(x1, y1, x1 + 1, y1 + 1, color);
			return;
		}
		for (int i = 0; i <= steps; i++) {
			float t = (float) i / steps;
			int px = (int) Math.round(x1 + t * (x2 - x1));
			int py = (int) Math.round(y1 + t * (y2 - y1));
			graphics.fill(px, py, px + 1, py + 1, color);
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (currentPhase == MenuPhase.POWER_SELECT && this.hoveredIndex >= 0) {
			List<ResourceLocation> powers = categorizedPowers.get(this.selectedAlignment);
			if (this.hoveredIndex < powers.size()) {
				ResourceLocation power = powers.get(this.hoveredIndex);
				PlayerForceData data = PlayerForceData.get(this.minecraft.player);
				if (data != null) {
					int maxLvl = data.getMaxUnlockedPowerLevel(power);
					int cur = localPowerLevels.getOrDefault(power, 1);
					if (scrollY > 0)
						cur = Math.min(maxLvl, cur + 1);
					else if (scrollY < 0)
						cur = Math.max(1, cur - 1);
					localPowerLevels.put(power, cur);
					playClickSound();
					return true;
				}
			}
		}
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (currentPhase == MenuPhase.CATEGORY_SELECT) {
			if (button == 0 && this.hoveredIndex >= 0 && this.hoveredIndex < availableCategories.size()) {
				this.selectedAlignment = availableCategories.get(this.hoveredIndex);
				this.currentPhase = MenuPhase.POWER_SELECT;
				this.hoveredIndex = -1;
				playClickSound();
				return true;
			}
		} else {
			List<ResourceLocation> powers = categorizedPowers.get(this.selectedAlignment);
			if (this.hoveredIndex >= 0 && this.hoveredIndex < powers.size()) {
				ResourceLocation power = powers.get(this.hoveredIndex);

				if (button == 0) {
					int lvl = localPowerLevels.getOrDefault(power, 1);
					selectAndClose(power, lvl);
					return true;
				} else if (button == 1) {
					PlayerForceData data = PlayerForceData.get(this.minecraft.player);
					if (data != null) {
						int maxLvl = data.getMaxUnlockedPowerLevel(power);
						int cur = localPowerLevels.getOrDefault(power, 1);
						int next = (cur >= maxLvl) ? 1 : cur + 1;
						localPowerLevels.put(power, next);
						playClickSound();
						return true;
					}
				}
			}
			if (button == 1 && availableCategories.size() > 1) {
				this.currentPhase = MenuPhase.CATEGORY_SELECT;
				this.hoveredIndex = -1;
				playClickSound();
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (ALKeyBinds.CYCLE_FORCE.matches(keyCode, scanCode)) {
			if (currentPhase == MenuPhase.POWER_SELECT && this.hoveredIndex >= 0) {
				List<ResourceLocation> powers = categorizedPowers.get(this.selectedAlignment);
				if (this.hoveredIndex < powers.size()) {
					ResourceLocation power = powers.get(this.hoveredIndex);
					int lvl = localPowerLevels.getOrDefault(power, 1);
					selectAndClose(power, lvl);
					return true;
				}
			}
			this.onClose();
			return true;
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	private void playClickSound() {
		if (this.minecraft != null && this.minecraft.player != null) {
			this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.4F, 1.4F);
		}
	}

	private void selectAndClose(ResourceLocation chosenPower, int level) {
		PacketDistributor.sendToServer(new SelectPowerPayload(chosenPower, level));
		if (this.minecraft != null && this.minecraft.player != null) {
			PlayerForceData data = PlayerForceData.get(this.minecraft.player);
			if (data != null) {
				data.setSelectedPower(chosenPower);
				data.setPowerLevel(chosenPower, level);
			}
		}
		ForceHudOverlay.wakeUp();

		if (this.minecraft != null) {
			this.minecraft.options.keyAttack.setDown(false);
		}
		this.onClose();
	}
}