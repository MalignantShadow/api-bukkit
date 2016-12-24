package info.malignantshadow.api.bukkit.commands;

import org.bukkit.ChatColor;

/**
 * Interface for defining the colors in a {@link info.malignantshadow.api.bukkit.commands.BukkitHelpListing BukkitHelpListing}
 * 
 * @author MalignantShadow (Caleb Downs)
 *
 */
public interface BukkitHelpColors {
	
	/**
	 * Get the separator color. Separators are used to separate the aliases of a command.
	 * 
	 * @return The separator color
	 */
	public ChatColor getSeparatorColor();
	
	/**
	 * Get the color used for argument.
	 * 
	 * @return The argument color.
	 */
	public ChatColor getArgsColor();
	
	/**
	 * Get the color used for aliases.
	 * 
	 * @return The alias color.
	 */
	public ChatColor getAliasColor();
	
	/**
	 * Get the color used for descriptions.
	 * 
	 * @return The description color.
	 */
	public ChatColor getDescriptionColor();
	
}
