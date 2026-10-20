package com.fiskmods.pika.lightsabers.client.gui;

import java.util.Locale;
import java.util.Objects;

import com.fiskmods.pika.lightsabers.ALConfig;
import com.fiskmods.pika.lightsabers.AdvancedLightsabers;
import com.fiskmods.pika.lightsabers.force.skilltree.PlayerForceData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = AdvancedLightsabers.MODID, value = Dist.CLIENT)
public class ForceHudOverlay {

	private static long lastActiveTime = 0;
	private static float lastKnownEnergy = -1.0f;
	private static ResourceLocation lastKnownPower = null;
	private static int lastKnownPowerLevel = -1;
	private static int lastKnownCooldown = 0;
	private static boolean wasBelowMaxEnergy = false;
	private static boolean hadActiveCooldown = false;

	public static void wakeUp() {
		lastActiveTime = System.currentTimeMillis();
	}

	public static void onDataSynced(PlayerForceData data) {
		if (data == null || !data.isForceSensitive())
			return;

		float currentEnergy = data.getForceEnergy();
		float maxEnergy = data.getMaxForceEnergy();
		if (maxEnergy <= 0)
			maxEnergy = 100.0f;

		ResourceLocation selectedPower = data.getSelectedPower();
		int currentLevel = data.getCurrentSelectedPowerLevel();
		int currentCooldown = (selectedPower != null) ? data.getPowerCooldown(selectedPower) : 0;
		boolean hasAnyCooldowns = data.hasActiveCooldowns();

		if (lastKnownEnergy >= 0.0f && currentEnergy < lastKnownEnergy - 0.05f) {
			wakeUp();
		}
		if (!Objects.equals(selectedPower, lastKnownPower) || currentLevel != lastKnownPowerLevel) {
			wakeUp();
			lastKnownPower = selectedPower;
			lastKnownPowerLevel = currentLevel;
		}
		if (currentCooldown != lastKnownCooldown) {
			wakeUp();
			lastKnownCooldown = currentCooldown;
		}

		boolean isBelowMax = currentEnergy < maxEnergy - 0.05f;
		boolean hasCooldownActive = hasAnyCooldowns || currentCooldown > 0;

		if (isBelowMax || hasCooldownActive) {
			wakeUp();
		} else if (wasBelowMaxEnergy && !isBelowMax) {
			wakeUp();
		} else if (hadActiveCooldown && !hasCooldownActive) {
			wakeUp();
		}

		wasBelowMaxEnergy = isBelowMax;
		hadActiveCooldown = hasCooldownActive;
		lastKnownEnergy = currentEnergy;
	}

	private static int applyAlpha(int color, float alphaMult) {
		int a = (color >> 24) & 0xFF;
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;

		a = (int) (a * alphaMult);
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if (player == null || mc.options.hideGui)
			return;

		PlayerForceData data = PlayerForceData.get(player);

		if (data != null && data.isForceSensitive()) {
			float currentEnergy = data.getForceEnergy();
			float maxEnergy = data.getMaxForceEnergy();
			if (maxEnergy <= 0)
				maxEnergy = 100.0f;

			ResourceLocation selectedPower = data.getSelectedPower();
			int currentLevel = data.getCurrentSelectedPowerLevel();
			int currentCooldown = (selectedPower != null) ? data.getPowerCooldown(selectedPower) : 0;
			boolean hasAnyCooldowns = data.hasActiveCooldowns();

			if (lastKnownEnergy < 0.0f) {
				lastKnownEnergy = currentEnergy;
				lastKnownPower = selectedPower;
				lastKnownPowerLevel = currentLevel;
				lastKnownCooldown = currentCooldown;
				wasBelowMaxEnergy = currentEnergy < maxEnergy - 0.05f;
				hadActiveCooldown = hasAnyCooldowns || currentCooldown > 0;
			} else {
				if (currentEnergy < lastKnownEnergy - 0.05f) {
					wakeUp();
				}
				if (!Objects.equals(selectedPower, lastKnownPower) || currentLevel != lastKnownPowerLevel) {
					wakeUp();
					lastKnownPower = selectedPower;
					lastKnownPowerLevel = currentLevel;
				}
				if (currentCooldown != lastKnownCooldown) {
					wakeUp();
					lastKnownCooldown = currentCooldown;
				}

				boolean isBelowMax = currentEnergy < maxEnergy - 0.05f;
				boolean hasCooldownActive = hasAnyCooldowns || currentCooldown > 0;

				if (isBelowMax || hasCooldownActive) {
					wakeUp();
				} else if (wasBelowMaxEnergy && !isBelowMax) {
					wakeUp();
				} else if (hadActiveCooldown && !hasCooldownActive) {
					wakeUp();
				}

				wasBelowMaxEnergy = isBelowMax;
				hadActiveCooldown = hasCooldownActive;
				lastKnownEnergy = currentEnergy;
			}

			boolean alwaysShow = false;
			try {
				alwaysShow = ALConfig.ALWAYS_SHOW_FORCE_HUD.get();
			} catch (Exception ignored) {
			}

			float alphaMult = 1.0f;

			if (!alwaysShow) {
				long timeSinceActive = System.currentTimeMillis() - lastActiveTime;
				if (timeSinceActive > 5000)
					return;

				if (timeSinceActive > 4000) {
					alphaMult = 1.0f - ((timeSinceActive - 4000) / 1000.0f);
				}
			}

			GuiGraphics graphics = event.getGuiGraphics();
			Font font = mc.font;

			int screenWidth = mc.getWindow().getGuiScaledWidth();
			int screenHeight = mc.getWindow().getGuiScaledHeight();

			int barWidth = 104;
			int barHeight = 10;
			int x = screenWidth - barWidth - 10;
			int y = screenHeight - 24;

			float fillRatio = Math.max(0.0f, Math.min(1.0f, currentEnergy / maxEnergy));
			int fillWidth = (int) (barWidth * fillRatio);

			int bgColor = applyAlpha(0xAA000000, alphaMult);
			int fillColor = applyAlpha(0xFF0099EE, alphaMult);
			int outlineColor = applyAlpha(0xFF888888, alphaMult);

			graphics.fill(x, y, x + barWidth, y + barHeight, bgColor);
			graphics.fill(x, y, x + fillWidth, y + barHeight, fillColor);
			graphics.renderOutline(x - 1, y - 1, barWidth + 2, barHeight + 2, outlineColor);

			String energyNumbers = String.format("%d / %d", Math.round(currentEnergy), Math.round(maxEnergy));
			int numColor = applyAlpha(0xFFFFFFFF, alphaMult);

			graphics.pose().pushPose();
			float numScale = 0.75f;
			graphics.pose().scale(numScale, numScale, 1.0f);
			int scaledNumWidth = (int) (font.width(energyNumbers) * numScale);
			int numX = (int) ((x + (barWidth / 2.0f) - (scaledNumWidth / 2.0f)) / numScale);
			int numY = (int) ((y + 2.0f) / numScale);
			graphics.drawString(font, energyNumbers, numX, numY, numColor, true);
			graphics.pose().popPose();

			selectedPower = data.getSelectedPower();
			if (selectedPower != null) {
				int lvl = data.getCurrentSelectedPowerLevel();
				String roman = (lvl >= 4) ? " IV" : ((lvl == 3) ? " III" : ((lvl == 2) ? " II" : " I"));
				Component powerName = Component.translatable("power.lightsabersfork." + selectedPower.getPath())
						.append(Component.literal(roman));

				int cdTicks = data.getPowerCooldown(selectedPower);
				if (cdTicks > 0) {
					powerName = powerName.copy()
							.append(Component.translatable("gui.lightsabersfork.hud.cooldown",
									String.format(Locale.ROOT, "%.1f", cdTicks / 20.0f)));
				}

				int textWidth = font.width(powerName);
				int textColor = applyAlpha(0xFFFFFFFF, alphaMult);
				graphics.drawString(font, powerName, x + (barWidth / 2) - (textWidth / 2), y - 11, textColor, true);
			} else {
				Component empty = Component.translatable("gui.lightsabersfork.hud.no_power");
				int textWidth = font.width(empty);
				int textColor = applyAlpha(0xFFAAAAAA, alphaMult);
				graphics.drawString(font, empty, x + (barWidth / 2) - (textWidth / 2), y - 11, textColor, true);
			}
		}
	}
}