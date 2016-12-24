package info.malignantshadow.api.bukkit.commands;

import org.bukkit.entity.Player;

import info.malignantshadow.api.commands.Command;
import info.malignantshadow.api.commands.CommandSender;

/**
 * Represents a Bukkit command.
 * 
 * @author MalignantShadow (Caleb Downs)
 *
 */
public class BukkitCommand extends Command {
	
	private int _acceptedSenders;
	private String[] _perms;
	private boolean _needAllPerms;
	
	/**
	 * Construct a new {@link BukkitCommand}. By default, this command has no required permissions and can only be sent by {@link Player}s.
	 * 
	 * @param name
	 *            The name of the command
	 * @param desc
	 *            The command's description
	 * @param aliases
	 *            The aliases for the command
	 * @see Command#Command(String, String, String...) Command(String, String, String...)
	 */
	public BukkitCommand(String name, String desc, String... aliases) {
		super(name, desc, aliases);
		_acceptedSenders = BukkitCommandSender.PLAYER;
		_perms = new String[0];
		_needAllPerms = false;
	}
	
	/**
	 * Set the accepted sender types for this command. The passed integer should be the result of bitwise OR'ing the sender types together.
	 * For instance:
	 * 
	 * <pre>
	 * command.setAcceptedSenders(BukkitCommandSender.PLAYER | BukkitCommandSender.CONSOLE)
	 * </pre>
	 * 
	 * Would allow this command to only be sent by the console or a player.
	 * 
	 * @param senderTypes
	 *            The sender types
	 * @return this
	 */
	public BukkitCommand thatCanBeSentBy(int senderTypes) {
		_acceptedSenders = senderTypes;
		return this;
	}
	
	/**
	 * Can this command be sent by the specified sender type?
	 * 
	 * @param senderType
	 *            The sender type
	 * @return <code>true</code> if this command be sent by the sender type(s) given, <code>false</code> otherwise.
	 */
	public boolean canBeSentBy(int senderType) {
		return (_acceptedSenders & senderType) > 0;
	}
	
	/**
	 * Can this command be sent by the given {@link CommandSender} instance?
	 * 
	 * @param sender
	 *            The sender
	 * @return <code>true</code> if this command be sent by the sender, <code>false</code> otherwise.
	 */
	public boolean canBeSentBy(CommandSender sender) {
		if (sender instanceof BukkitCommandSender)
			return canBeSentBy((BukkitCommandSender) sender);
		return canBeSentBy(BukkitCommandSender.OTHER);
	}
	
	/**
	 * Can this command be sent by the given {@link BukkitCommandSender} instance?
	 * 
	 * @param sender
	 *            The sender
	 * @return <code>true</code> if this command be sent by the sender, <code>false</code> otherwise.
	 */
	public boolean canBeSentBy(BukkitCommandSender sender) {
		if (sender == null)
			return false;
		
		return canBeSentBy(sender.getHandle());
	}
	
	/**
	 * Can this command be sent by the given Bukkit {@link org.bukkit.command.CommandSender CommandSender} instance?
	 * 
	 * @param sender
	 *            The sender
	 * @return <code>true</code> if this command be sent by the sender, <code>false</code> otherwise.
	 */
	public boolean canBeSentBy(org.bukkit.command.CommandSender sender) {
		return canBeSentBy(BukkitCommandSender.getType(sender)) && hasPermission(sender);
	}
	
	/**
	 * Set the permissions required for this command. The sender only needs one to successfully send the command.
	 * 
	 * @param permissions
	 *            The permissions
	 * @return this
	 */
	public BukkitCommand withRequiredPermissions(String... permissions) {
		return withRequiredPermissions(false, permissions);
		
	}
	
	/**
	 * Set the permissions required for this command. If <code>all</code> is <code>true</code> then the sender needs all of the permissions
	 * in order to send the command. Otherwise, the sender only needs to have one.
	 * 
	 * @param all
	 *            <code>true</code> if the sender needs all of the permissions given
	 * @param permissions
	 *            THe permissions
	 */
	public BukkitCommand withRequiredPermissions(boolean all, String... permissions) {
		_needAllPerms = all;
		_perms = permissions;
		return this;
	}
	
	/**
	 * Does the sender have permission to run this command?
	 * 
	 * @param sender
	 *            The sender
	 * @return <code>true</code> if the sender can send this command
	 */
	public boolean hasPermission(BukkitCommandSender sender) {
		return hasPermission(sender.getHandle());
	}
	
	/**
	 * Does the sender have permission to run this command?
	 * 
	 * @param sender
	 *            The sender
	 * @return <code>true</code> if the sender can send this command
	 */
	public boolean hasPermission(org.bukkit.command.CommandSender sender) {
		if (_perms.length == 0)
			return true;
		
		if (sender == null)
			return false;
		
		boolean hadPrevious = true;
		for (String p : _perms) {
			if (_needAllPerms) {
				hadPrevious = hadPrevious && sender.hasPermission(p);
				if (!hadPrevious)
					return false;
			} else if (sender.hasPermission(p))
				return true;
		}
		
		/*
		 * If we are here then we either:
		 * - Need all permissions (and have all of them)
		 * - Need one permission (and have none of them)
		 * Luckily, the two conditions above match the value of _needAllPerms, so return that value
		 */
		return _needAllPerms;
	}
	
}
