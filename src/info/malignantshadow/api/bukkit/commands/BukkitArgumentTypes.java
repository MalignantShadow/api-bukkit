package info.malignantshadow.api.bukkit.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import info.malignantshadow.api.bukkit.helpers.BukkitColor;
import info.malignantshadow.api.util.ListUtil;
import info.malignantshadow.api.util.arguments.Argument;
import info.malignantshadow.api.util.arguments.ArgumentList;
import info.malignantshadow.api.util.arguments.ArgumentTypes;
import info.malignantshadow.api.util.arguments.ParsedArguments;

/**
 * Utility class holding {@link Argument.Type}s relating to Bukkit objects.
 * 
 * @author MalignantShadow (Caleb Downs)
 * @see ArgumentTypes
 *
 */
public final class BukkitArgumentTypes {
	
	private BukkitArgumentTypes() {
	}
	
	// [world] <x> <y> <z> [yaw] [pitch]
	private static final ArgumentList LOCATION_ARG_LIST = new ArgumentList();
	
	static {
		LOCATION_ARG_LIST.add(new Argument("world", "", false)
			.withAcceptedTypes(Bukkit::getWorld)
			.withDefault(Bukkit.getWorlds().get(0)));
		LOCATION_ARG_LIST.add(new Argument("x", "", true)
			.withAcceptedTypes(ArgumentTypes.DOUBLE));
		LOCATION_ARG_LIST.add(new Argument("y", "", true)
			.withAcceptedTypes(ArgumentTypes.DOUBLE));
		LOCATION_ARG_LIST.add(new Argument("z", "", true)
			.withAcceptedTypes(ArgumentTypes.DOUBLE));
		LOCATION_ARG_LIST.add(new Argument("yaw", "", false)
			.withAcceptedTypes(ArgumentTypes.DOUBLE)
			.withDefault(0));
		LOCATION_ARG_LIST.add(new Argument("pitch", "", false)
			.withAcceptedTypes(ArgumentTypes.DOUBLE)
			.withDefault(0));
	}
	
	public static final Argument.Type<Player> ONLINE_PLAYER = (input) -> {
		if (input == null || input.isEmpty())
			return null;
		
		if (input.length() > 16) { //the input might be a uuid (player usernames are max 16 characters)
			UUID uuid = UUID.fromString(input);
			if (uuid == null)
				return null;
			
			return Bukkit.getPlayer(uuid);
		}
		
		return ListUtil.find(new ArrayList<Player>(Bukkit.getOnlinePlayers()), (p) -> p != null && p.getName().startsWith(input));
	};
	
	/**
	 * Parse the argument as a {@link BukkitColor} object. Only names of colors are allowed. For uses with hex strings, use {@link #COLOR}
	 */
	public static final Argument.Type<BukkitColor> BUKKIT_COLOR = ArgumentTypes.enumValue(BukkitColor.values());
	
	/**
	 * Parse the argument as a {@link Color} object. The input can be a hex string starting with '#' or a name of a color.
	 */
	public static final Argument.Type<Color> COLOR = (input) -> {
		if (input == null || input.isEmpty())
			return null;
		
		if (input.startsWith("#")) {
			if (input.length() == 1) // "#"
				return Color.BLACK;
			
			input = input.substring(1, input.length());
			if (input.length() == 3) { // "#ABC" -> "#AABBCC"
				char r = input.charAt(0);
				char g = input.charAt(1);
				char b = input.charAt(2);
				input = "" + r + r + g + g + b + b;
			}
			
			try {
				return Color.fromRGB(Integer.parseInt(input, 16));
			} catch (NumberFormatException e) {
				// cannot parse as hex
				return null;
			}
		} else {
			BukkitColor c = BUKKIT_COLOR.getValue(input);
			if (c == null)
				return null;
			
			return c.getColor();
		}
	};
	
	public static final Argument.Type<Location> LOCATION = (input) -> {
		String[] split = input.split("\\s+");
		if (split.length < LOCATION_ARG_LIST.getMinimum())
			return null;
		
		ParsedArguments args = new ParsedArguments(LOCATION_ARG_LIST, split);
		World w = (World) args.get("world");
		double x = (Double) args.get("x");
		double y = (Double) args.get("y");
		double z = (Double) args.get("z");
		double yaw = (Double) args.get("yaw");
		double pitch = (Double) args.get("pitch");
		return new Location(w, x, y, z, (float) yaw, (float) pitch);
	};
	
	/**
	 * Parse the argument as an {@link EntityType}.
	 */
	public static final Argument.Type<EntityType> ENTITY_TYPE = ArgumentTypes.enumValue(EntityType.values());
	
	/**
	 * Parse the argument as a {@link Horse.Color}.
	 */
	public static final Argument.Type<Horse.Color> HORSE_COLOR = ArgumentTypes.enumValue(Horse.Color.values());
	
	/**
	 * Parse the argument as a {@link Horse.Style}.
	 */
	public static final Argument.Type<Horse.Style> HORSE_STYLE = ArgumentTypes.enumValue(Horse.Style.values());
	
	/**
	 * Parse the argument as a {@link Horse.Variant}.
	 */
	public static final Argument.Type<Horse.Variant> HORSE_VARIANT = ArgumentTypes.enumValue(Horse.Variant.values());
	
	/**
	 * Parse the argument as a {@link Material}.
	 */
	public static final Argument.Type<Material> MATERIAL = ArgumentTypes.enumValue(Material.values());
	
	/**
	 * Parse the argument as a {@link Material}, but return <code>null</code> if the parsed Material is not a placeable block.
	 */
	public static final Argument.Type<Material> MATERIAL_BLOCK = (input) -> {
		Material m = MATERIAL.getValue(input);
		if (m == null || !m.isBlock())
			return null;
		
		return m;
	};
	
	/**
	 * Parse the argument as a {@link Material}, but return <code>null</code> if the parsed Material is a placeable block.
	 */
	public static final Argument.Type<Material> MATERIAL_ITEM = (input) -> {
		Material m = MATERIAL.getValue(input);
		if (m == null || m.isBlock())
			return null;
		
		return m;
	};
	
	/**
	 * Parse the argument as a {@link PotionEffectType}.
	 */
	public static final Argument.Type<PotionEffectType> POTION_EFFECT = (input) -> {
		for (PotionEffectType p : PotionEffectType.values())
			if (p.getName().replaceAll("\\s+", "_").equalsIgnoreCase(input))
				return p;
			
		return null;
	};
	
}
