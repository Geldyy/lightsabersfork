package com.fiskmods.pika.lightsabers.item.crystals;

import java.util.Locale;

public enum CrystalColor {
	DEEP_BLUE(0, 255), MEDIUM_BLUE(1, 27647), LIGHT_BLUE(2, 5880319), ARCTIC_BLUE(3, 14546687), WHITE(4, 16777215),
	INDIGO(5, 6095103), PURPLE(6, 11337901), MAGENTA(7, 16711935), PINK(8, 16748474), RED(9, 16711680),
	BLOOD_ORANGE(10, 16744448), AMBER(11, 16758272), YELLOW(12, 16776960), GOLD(13, 16777018), LIME_GREEN(14, 12582656),
	GREEN(15, 65280), MINT_GREEN(16, 65435), CYAN(17, 65535);

	public final int id;
	public final int hexColor;
	private final float[] rgb;

	CrystalColor(int id, int hexColor) {
		this.id = id;
		this.hexColor = hexColor;
		this.rgb = calculateRGB(hexColor);

	}

	private static float[] calculateRGB(int hex) {
		float r = ((hex >> 16) & 0xFF) / 255.0F;
		float g = ((hex >> 8) & 0xFF) / 255.0F;
		float b = (hex & 0xFF) / 255.0F;
		return new float[] { r, g, b };
	}

	public float getRed() {
		return rgb[0];
	}

	public float getGreen() {
		return rgb[1];
	}

	public float getBlue() {
		return rgb[2];
	}

	public float[] getRGB() {
		return rgb;
	}

	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public int getColor() {
		return this.hexColor;
	}
}