package info.malignantshadow.api.bukkit.helpers;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class BukkitMessages {
	
	public static final char DEFAULT_COLOR_CHAR = '&';
	
	public static String format(String message) {
		return format(DEFAULT_COLOR_CHAR, message);
	}
	
	public static String format(char colorChar, String message) {
		return ChatColor.translateAlternateColorCodes(colorChar, message);
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		if (sender != null)
			sender.sendMessage(format(message));
	}
	
}
