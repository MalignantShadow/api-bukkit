package info.malignantshadow.api.bukkit.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import info.malignantshadow.api.util.ListUtil;
import info.malignantshadow.api.util.StringUtil;
import info.malignantshadow.api.util.arguments.Argument;
import info.malignantshadow.api.util.arguments.ArgumentTypes;

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
	 * Parse the argument as a {@link DyeColor}.
	 */
	public static final Argument.Type<DyeColor> DYE_COLOR = (input) -> {
		if (input == null || input.isEmpty())
			return null;
		
		if (StringUtil.eic(input, "black", "shadow"))
			return DyeColor.BLACK;
		else if (StringUtil.eic(input, "blue", "bleu", "bloo"))
			return DyeColor.BLUE;
		else if (StringUtil.eic(input, "brown"))
			return DyeColor.BROWN;
		else if (StringUtil.eic(input, "cyan", "aqua"))
			return DyeColor.CYAN;
		else if (StringUtil.eic(input, "gray", "grey"))
			return DyeColor.GRAY;
		else if (StringUtil.eic(input, "light_blue", "light_bleu", "light_bloo", "light-blue", "light-bleu", "light-bloo"))
			return DyeColor.LIGHT_BLUE;
		else if (StringUtil.eic(input, "lime", "light_green", "light-green"))
			return DyeColor.LIME;
		else if (StringUtil.eic(input, "magenta", "dark_pink", "dark-pink"))
			return DyeColor.MAGENTA;
		else if (StringUtil.eic(input, "orange"))
			return DyeColor.ORANGE;
		else if (StringUtil.eic(input, "pink"))
			return DyeColor.PINK;
		else if (StringUtil.eic(input, "purple"))
			return DyeColor.PURPLE;
		else if (StringUtil.eic(input, "red"))
			return DyeColor.RED;
		else if (StringUtil.eic(input, "silver", "light_gray", "light_grey", "light-gray", "light-grey"))
			return DyeColor.SILVER;
		else if (StringUtil.eic(input, "white", "shine"))
			return DyeColor.WHITE;
		
		return null;
	};
	
	/**
	 * Parse the argument as a Bukkit {@link Color} object.
	 */
	public static final Argument.Type<Color> BUKKIT_COLOR = (input) -> {
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
		}
		
		DyeColor dye = DYE_COLOR.getValue(input);
		if (dye != null)
			return dye.getColor();
		
		return null;
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
