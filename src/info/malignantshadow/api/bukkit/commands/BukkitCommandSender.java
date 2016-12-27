package info.malignantshadow.api.bukkit.commands;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import info.malignantshadow.api.bukkit.helpers.BukkitMessages;
import info.malignantshadow.api.bukkit.helpers.BukkitPlayer;
import info.malignantshadow.api.commands.CommandSender;

public class BukkitCommandSender extends CommandSender {
	
	/**
	 * The Player sender type.
	 */
	public static final int PLAYER = 1;
	
	/**
	 * The console sender type.
	 */
	public static final int CONSOLE = 1 << 1;
	
	/**
	 * The block sender type. (e.g. Command Block)
	 */
	public static final int BLOCK = 1 << 2;
	
	/**
	 * The Minecart sender type
	 */
	public static final int MINECART = 1 << 3;
	
	/**
	 * A sender that isn't a player, console, block, or minecart.
	 */
	public static final int OTHER = 1 << 4;
	
	public static int getType(org.bukkit.command.CommandSender sender) {
		if (sender instanceof Player)
			return PLAYER;
		else if (sender instanceof ConsoleCommandSender)
			return CONSOLE;
		else if (sender instanceof BlockCommandSender)
			return BLOCK;
		else if (sender instanceof CommandMinecart)
			return MINECART;
		else
			return OTHER;
	}
	
	private org.bukkit.command.CommandSender _handle;
	
	/**
	 * Create a new {@link BukkitCommandSender} with the given handle
	 * 
	 * @param handle
	 *            The handle
	 */
	public BukkitCommandSender(org.bukkit.command.CommandSender handle) {
		_handle = handle;
	}
	
	/**
	 * Get this sender's Bukkit handle.
	 * 
	 * @return The handle
	 */
	public org.bukkit.command.CommandSender getHandle() {
		return _handle;
	}
	
	@Override
	public void print(String message) {
		print("&a", message);
	}
	
	/**
	 * Print a message to this sender. <code>resetString</code> will prefix the message as well as appended to the 'reset' color code. This is useful
	 * if the message should be one color but certain words need to be highlighted.
	 * 
	 * @param resetString
	 *            The reset string
	 * @param message
	 *            The message
	 */
	public void print(String resetString, String message) {
		String reset = BukkitMessages.DEFAULT_COLOR_CHAR + "r";
		BukkitMessages.sendMessage(_handle, resetString + message.replace(reset, reset + resetString));
	}
	
	@Override
	public void printErr(String error) {
		print("&c", error);
	}
	
	public boolean hasPermissions(String perm) {
		if (_handle == null)
			return false;
		
		return _handle.hasPermission(perm);
	}
	
	/**
	 * Is this sender a player?
	 * 
	 * @return <code>true</code> if this sender is a player.
	 */
	public boolean isPlayer() {
		return _handle instanceof Player;
	}
	
	/**
	 * Get the sender as a Bukkit {@link Player}
	 * 
	 * @return The sender cast to a player.
	 */
	public Player asPlayer() {
		return (Player) _handle;
	}
	
	/**
	 * Wrap the player in a {@link BukkitPlayer} object.
	 * 
	 * @return The BukkitPlayer
	 */
	public BukkitPlayer asBukkitPlayer() {
		return new BukkitPlayer(asPlayer());
	}
	
	/**
	 * Is this sender a console?
	 * 
	 * @return <code>true</code> if this sender is a console.
	 */
	public boolean isConsole() {
		return _handle instanceof ConsoleCommandSender;
	}
	
	/**
	 * Cast this sender's handle to {@link ConsoleCommandSender}
	 * 
	 * @return The console
	 */
	public ConsoleCommandSender asConsole() {
		return (ConsoleCommandSender) _handle;
	}
	
	/**
	 * Is this sender a minecart?
	 * 
	 * @return <code>true</code> if this sender is a minecart.
	 */
	public boolean isMinecart() {
		return _handle instanceof CommandMinecart;
	}
	
	/**
	 * Cast this sender's handle to {@link CommandMinecart}
	 * 
	 * @return The minecart
	 */
	public CommandMinecart asMinecart() {
		return (CommandMinecart) _handle;
	}
	
	/**
	 * Is this sender a block?
	 * 
	 * @return <code>true</code> if this sender is a block.
	 */
	public boolean isBlock() {
		return _handle instanceof BlockCommandSender;
	}
	
	/**
	 * Cast this sender's handle to {@link BlockCommandSender}
	 * 
	 * @return The block (sender)
	 */
	public BlockCommandSender asBlockSender() {
		return (BlockCommandSender) _handle;
	}
	
	/**
	 * Cast this sender's handle to {@link BlockCommandSender} and get the block.
	 * 
	 * @return The block
	 */
	public Block asBlock() {
		return asBlockSender().getBlock();
	}
	
	/**
	 * Get this command sender's location, or null if it is the console.
	 * 
	 * @return The sender's location
	 */
	public Location getLocation() {
		if (isConsole())
			return null;
		
		//every other command sender is an entity
		return ((Entity) _handle).getLocation();
	}
}
