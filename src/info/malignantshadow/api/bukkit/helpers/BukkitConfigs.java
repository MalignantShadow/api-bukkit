package info.malignantshadow.api.bukkit.helpers;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

import info.malignantshadow.api.bukkit.commands.BukkitArgumentTypes;
import info.malignantshadow.api.config.ConfigPairing;
import info.malignantshadow.api.config.ConfigSection;
import info.malignantshadow.api.config.ConfigSequence;
import info.malignantshadow.api.config.Configs;

public class BukkitConfigs {
	
	public static final Color DEF_COLOR = Color.BLACK;
	public static final Location DEF_LOCATION = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
	
	private BukkitConfigs() {
	}
	
	/**
	 * TODO: get/put for
	 * 
	 * material/blocks+data e.g. ItemStack
	 * 
	 */
	
	public static Color getColor(ConfigPairing pair) {
		return getColor(DEF_COLOR, pair);
	}
	
	public static Color getColor(Color def, ConfigPairing pair) {
		return Configs.getComplex(def, pair, BukkitConfigs::getColor, BukkitConfigs::getColor, BukkitArgumentTypes.COLOR);
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
	
	public static Location getLocation(ConfigPairing pair) {
		return getLocation(DEF_LOCATION, pair);
	}
	
	public static Location getLocation(Location def, ConfigPairing pair) {
		return Configs.getComplex(def, pair, BukkitConfigs::getLocation, BukkitConfigs::getLocation, BukkitArgumentTypes.LOCATION);
	}
	
	public static Location getLocation(ConfigSection section) {
		return getLocation(DEF_LOCATION, section);
	}
	
	public static Location getLocation(Location def, ConfigSection section) {
		if (section == null)
			return null;
		
		World w = Bukkit.getWorld(section.getStringWithDefault("", "world"));
		double x = section.getNumber(0, "x").doubleValue();
		double y = section.getNumber(0, "y").doubleValue();
		double z = section.getNumber(0, "z").doubleValue();
		float yaw = section.getNumber(0, "yaw").floatValue();
		float pitch = section.getNumber(0, "pitch").floatValue();
		return new Location(w, x, y, z, yaw, pitch);
	}
	
	public static Location getLocation(ConfigSequence sequence) {
		return getLocation(DEF_LOCATION, sequence);
	}
	
	public static Location getLocation(Location def, ConfigSequence sequence) {
		if (sequence == null)
			return null;
		
		String input = "";
		for (int i = 0; i < sequence.size(); i++)
			input += sequence.getString("", i);
		
		return BukkitArgumentTypes.LOCATION.getValue(input);
	}
	
	public static String putLocationAsString(Location loc) {
		if (loc == null)
			return null;
		
		String str = "";
		World w = loc.getWorld();
		if (w != null)
			str += w.getName();
		return str + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
	}
	
	public static ConfigSection putLocationAsSection(Location loc) {
		ConfigSection section = new ConfigSection();
		World w = loc.getWorld();
		if (w != null)
			section.set(w.getName(), "world");
		section.set(loc.getX(), "x");
		section.set(loc.getY(), "y");
		section.set(loc.getZ(), "z");
		section.set(loc.getYaw(), "yaw");
		section.set(loc.getPitch(), "pitch");
		return section;
	}
	
	public static ConfigSequence putLocationAsSequence(Location loc) {
		ConfigSequence seq = new ConfigSequence();
		World w = loc.getWorld();
		if (w != null)
			seq.add(w.getName());
		seq.add(loc.getX());
		seq.add(loc.getY());
		seq.add(loc.getZ());
		seq.add(loc.getYaw());
		seq.add(loc.getPitch());
		return seq;
	}
	
}
