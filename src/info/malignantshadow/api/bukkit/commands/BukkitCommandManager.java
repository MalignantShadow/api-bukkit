package info.malignantshadow.api.bukkit.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import info.malignantshadow.api.commands.Command;
import info.malignantshadow.api.commands.CommandContext;
import info.malignantshadow.api.commands.CommandManager;
import info.malignantshadow.api.commands.CommandSender;

public class BukkitCommandManager extends CommandManager implements CommandExecutor {
	
	private JavaPlugin _plugin;
	private BukkitHelpColors _defColors;
	
	/**
	 * Create a new {@link BukkitCommandManager} without a plugin reference. If this is the root manager,
	 * {@link #BukkitCommandManager(JavaPlugin)} should be used instead.
	 */
	public BukkitCommandManager() {
		this(null);
	}
	
	/**
	 * Create a new {@link BukkitCommandManager}. The plugin reference is used for registering commands to Bukkit.
	 * 
	 * @param plugin
	 *            The owning plugin of this manager
	 */
	public BukkitCommandManager(JavaPlugin plugin) {
		_plugin = plugin;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Bukkit</b> - If <code>sender</code> is an instance of {@link BukkitCommandSender}, then commands will also be hidden if the sender cannot send the command
	 * (determined by {@link BukkitCommand#canBeSentBy(BukkitCommandSender)})
	 * </p>
	 */
	@Override
	public List<Command> getVisibleCommands(CommandSender sender) {
		List<Command> visible = new ArrayList<Command>();
		
		for (Command c : super.getVisibleCommands(sender))
			if (!(c instanceof BukkitCommand) || ((BukkitCommand) c).canBeSentBy(sender))
				visible.add(c);
			
		return visible;
	}
	
	private static CommandMap getCommandMap() {
		try {
			if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
				Field f = SimplePluginManager.class.getDeclaredField("commandMap");
				f.setAccessible(true);
				
				return (CommandMap) f.get(Bukkit.getPluginManager());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static PluginCommand makePluginCommand(String name, Plugin plugin) {
		try {
			Constructor<PluginCommand> con = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			con.setAccessible(true);
			return con.newInstance(name, plugin);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected boolean commandWillDispatch(Command cmd, CommandContext context) {
		if (!(cmd instanceof BukkitCommand) || !(context.getSender() instanceof BukkitCommandSender))
			return true;
		
		BukkitCommand bCmd = (BukkitCommand) cmd;
		
		// test the sender's permissions
		BukkitCommandSender sender = (BukkitCommandSender) context.getSender();
		if (!bCmd.hasPermission(sender)) {
			sender.printErr("You don't have permission to run this command");
			return false;
		}
		
		// test if the command accepts the sender type
		if (!bCmd.canBeSentBy(sender)) {
			String message = "This command cannot be run %s";
			if (sender.isPlayer())
				context.printErr(String.format(message, "by a player"));
			else if (sender.isConsole())
				context.printErr(String.format(message, "from the console"));
			else if (sender.isBlock())
				context.printErr(String.format(message, "by a command block"));
			else if (sender.isMinecart())
				context.printErr(String.format(message, "by a command minecart"));
			else
				context.printErr("Whatever you are, you cannot run this command");
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean onCommand(org.bukkit.command.CommandSender bukkitCmdSender, org.bukkit.command.Command bukkitCmd, String label, String[] args) {
		BukkitCommandSender sender = new BukkitCommandSender(bukkitCmdSender);
		dispatch(sender, label, args);
		
		//return true to prevent Bukkit from sending an (ambiguous) error message
		//all error messages are handled by this message (an an approprioate message is sent)
		return true;
	}
	
	@Override
	public BukkitCommandManager push(Command command) {
		return (BukkitCommandManager) super.push(command);
	}
	
	/**
	 * Set the default help listing colors for this command manager.
	 * 
	 * @param colors
	 *            The colors to use
	 * @return this
	 */
	public BukkitCommandManager withDefaultColors(BukkitHelpColors colors) {
		_defColors = colors;
		return this;
	}
	
	/**
	 * Get the default colors used in help listings for this command manager.
	 * 
	 * @return The default colors.
	 */
	public BukkitHelpColors getDefaultColors() {
		return _defColors;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Bukkit</b> - The messages are colored using the default colors.
	 * </p>
	 * 
	 * @see BukkitHelpListing.DefaultColors
	 */
	@Override
	public BukkitHelpListing getHelpListing(String fullCmd, CommandSender sender) {
		return getHelpListing(fullCmd, sender, _defColors);
	}
	
	/**
	 * Get a help listing object with all visible commands from this manager.
	 * 
	 * @param fullCmd
	 *            The full command (minus the sub-command and arguments) to be displayed
	 * @param sender
	 *            Who might send a command (used to determine the visible commands)
	 * @param colors
	 *            The colors to use
	 * @return The help listing
	 */
	public BukkitHelpListing getHelpListing(String fullCmd, CommandSender sender, BukkitHelpColors colors) {
		return new BukkitHelpListing(fullCmd, getVisibleCommands(sender), colors);
	}
	
	@Override
	public BukkitCommandManager withHelpCommand() {
		return (BukkitCommandManager) super.withHelpCommand();
	}
	
	@Override
	public BukkitCommandManager withHelpCommand(String name, String... aliases) {
		return (BukkitCommandManager) super.withHelpCommand(name, aliases);
	}
	
	/**
	 * Add all previously defined commands to Bukkit's command map. This method should be the last method called on this manager
	 * when creating it. This method will fail silently if the command map cannot be retrieved or this command manager was constructed
	 * without a plugin reference. Furthermore, if a Bukkit version of a command cannot be created, it will be skipped (not registered).
	 * 
	 * <p>
	 * This method is provided as a convenience due to possible inaccuracies that may occur from adding commands in a <code>plugin.yml</code> and
	 * adding commands to this manager. The names, description, and aliases of commands may not be the same, because they have to be defined in two separate places.
	 * </p>
	 * <p>
	 * To solve this problem, this method uses reflection to access Bukkit's internal command map and add commands that way.
	 * </p>
	 * 
	 * @return this
	 * @see <a href="http://bukkit.org/threads/tutorial-registering-commands-at-runtime.158461/">https://bukkit.org/threads/tutorial-registering-commands-at-runtime.158461/</a>
	 */
	public BukkitCommandManager registerSelf() {
		CommandMap cmds = getCommandMap();
		if (cmds == null || _plugin == null)
			return this;
		
		for (Command c : getCommands()) {
			PluginCommand bukkitCmd = makePluginCommand(c.getName(), _plugin);
			if (bukkitCmd == null)
				continue;
			bukkitCmd.setAliases(Arrays.asList(c.getAliases()));
			bukkitCmd.setExecutor(this);
			cmds.register(_plugin.getName(), bukkitCmd);
		}
		return this;
	}
	
}
