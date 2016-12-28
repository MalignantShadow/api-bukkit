package info.malignantshadow.api.bukkit.helpers;

import org.bukkit.Color;

import info.malignantshadow.api.bukkit.commands.BukkitArgumentTypes;
import info.malignantshadow.api.config.ConfigPairing;
import info.malignantshadow.api.config.ConfigSection;
import info.malignantshadow.api.config.ConfigSequence;

public class BukkitConfigs {
	
	public static final Color DEF_COLOR = Color.BLACK;
	
	private BukkitConfigs() {
	}
	
	/**
	 * TODO: get/put for
	 * 
	 * colors
	 * location
	 * material/blocks+data e.g. ItemStack
	 * 
	 */
	
	public static Color getColor(ConfigPairing pair) {
		return getColor(DEF_COLOR, pair);
	}
	
	public static Color getColor(Color def, ConfigPairing pair) {
		if (pair == null)
			return def;
		
		if (pair.isSection())
			return getColor(def, pair.asSection());
		if (pair.isSequence())
			return getColor(def, pair.asSequence());
		if (pair.isNumber())
			return Color.fromRGB(pair.asInt());
		if (pair.isString()) {
			Color color = BukkitArgumentTypes.COLOR.getValue(pair.asString());
			if (color != null)
				return color;
		}
		
		return def;
	}
	
	public static Color getColor(ConfigSequence seq) {
		return getColor(DEF_COLOR, seq);
	}
	
	public static Color getColor(Color def, ConfigSequence seq) {
		if (seq == null)
			return DEF_COLOR;
		
		int r = seq.getNumber(0, 0).intValue();
		int g = seq.getNumber(0, 1).intValue();
		int b = seq.getNumber(0, 2).intValue();
		return Color.fromRGB(r, g, b);
	}
	
	public static Color getColor(ConfigSection section) {
		return getColor(DEF_COLOR, section);
	}
	
	public static Color getColor(Color def, ConfigSection section) {
		if (section == null)
			return DEF_COLOR;
		
		int r = section.getNumber(0, "r").intValue();
		int g = section.getNumber(0, "g").intValue();
		int b = section.getNumber(0, "b").intValue();
		return Color.fromRGB(r, g, b);
	}
	
	public static String putColorAsString(Color color) {
		return putColorAsString(color, true);
	}
	
	public static String putColorAsString(Color color, boolean hex) {
		if (color == null)
			return null;
		
		if (!hex) { //try to use the name first
			if (color.equals(Color.AQUA))
				return "aqua";
			if (color.equals(Color.BLACK))
				return "black";
			if (color.equals(Color.BLUE))
				return "blue";
			if (color.equals(Color.FUCHSIA))
				return "fuchsia";
			if (color.equals(Color.GRAY))
				return "gray";
			if (color.equals(Color.GREEN))
				return "green";
			if (color.equals(Color.LIME))
				return "lime";
			if (color.equals(Color.MAROON))
				return "maroon";
			if (color.equals(Color.NAVY))
				return "navy";
			if (color.equals(Color.OLIVE))
				return "olive";
			if (color.equals(Color.ORANGE))
				return "orange";
			if (color.equals(Color.PURPLE))
				return "purple";
			if (color.equals(Color.RED))
				return "red";
			if (color.equals(Color.SILVER))
				return "silver";
			if (color.equals(Color.TEAL))
				return "teal";
			if (color.equals(Color.WHITE))
				return "white";
			if (color.equals(Color.YELLOW))
				return "yellow";
		}
		
		return "#"
			+ String.format("%02X", color.getRed())
			+ String.format("%02X", color.getBlue())
			+ String.format("%02X", color.getGreen());
	}
	
	public static ConfigSection putColorAsSection(Color color) {
		if (color == null)
			return null;
		
		ConfigSection section = new ConfigSection();
		section.set(color.getRed(), "r");
		section.set(color.getGreen(), "g");
		section.set(color.getBlue(), "b");
		return section;
	}
	
	public static ConfigSequence putColorAsSequence(Color color) {
		if (color == null)
			return null;
		
		ConfigSequence seq = new ConfigSequence();
		seq.add(color.getRed());
		seq.add(color.getGreen());
		seq.add(color.getBlue());
		return seq;
	}
	
}
