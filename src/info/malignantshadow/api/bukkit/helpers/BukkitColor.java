package info.malignantshadow.api.bukkit.helpers;

import org.bukkit.Color;
import org.bukkit.DyeColor;

import info.malignantshadow.api.util.aliases.Aliasable;

public enum BukkitColor implements Aliasable {
	
	AQUA(DyeColor.LIGHT_BLUE, Color.AQUA, "Aqua", "light-blue", "light_blue"),
	BLACK(DyeColor.BLACK, Color.BLACK, "Black", "shadow"),
	BLUE(DyeColor.BLUE, Color.BLUE, "Blue", "bleu", "bloo"),
	BROWN(DyeColor.BROWN, Color.OLIVE, "Brown", "olive"),
	CYAN(DyeColor.CYAN, Color.TEAL, "Cyan", "teal"),
	GRAY(DyeColor.GRAY, Color.GRAY, "Gray", "grey"),
	GREEN(DyeColor.GREEN, Color.GREEN, "Green"),
	LIME(DyeColor.LIME, Color.LIME, "Lime"), //in the coconut, shake it all up
	MAGENTA(DyeColor.MAGENTA, Color.FUCHSIA, "Magenta", "fuschia", "dark-pink", "dark_pink"),
	NAVY(DyeColor.BLUE, Color.NAVY, "Navy"),
	ORANGE(DyeColor.ORANGE, Color.ORANGE, "Orange"),
	PINK(DyeColor.PINK, Color.FUCHSIA, "Pink", "fuchsia"),
	PURPLE(DyeColor.PURPLE, Color.PURPLE, "Purple"),
	RED(DyeColor.RED, Color.MAROON, "Red", "maroon"),
	SILVER(DyeColor.SILVER, Color.SILVER, "Silver", "light-gray", "light-grey", "light_gray", "light_grey"),
	WHITE(DyeColor.WHITE, Color.WHITE, "White", "shine"),
	YELLOW(DyeColor.YELLOW, Color.YELLOW, "yellow");
	
	private DyeColor _wool;
	private Color _color;
	private String _name;
	private String[] _aliases;
	
	private BukkitColor(DyeColor wool, Color color, String name, String... aliases) {
		_wool = wool;
		_color = color;
		_name = name;
		_aliases = aliases;
	}
	
	public DyeColor getWoolColor() {
		return _wool;
	}
	
	public Color getArmorColor() {
		return _wool.getColor();
	}
	
	public Color getFireworkColor() {
		return _wool.getFireworkColor();
	}
	
	public Color getColor() {
		return _color;
	}
	
	@Override
	public String getName() {
		return _name;
	}
	
	@Override
	public String[] getAliases() {
		return _aliases;
	}
	
}
