package info.malignantshadow.api.bukkit.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import info.malignantshadow.api.commands.Command;
import info.malignantshadow.api.commands.HelpListing;
import info.malignantshadow.api.util.ListUtil;
import info.malignantshadow.api.util.pagination.Paginator;

/**
 * A colored version of {@link HelpListing} for Bukkit plugin use.
 * 
 * @author MalignantShadow (Caleb Downs)
 *
 */
public class BukkitHelpListing extends HelpListing {
	
	/**
	 * The default colors used in a {@link BukkitHelpListing}
	 * 
	 * @author MalignantShadow (Caleb Downs)
	 *
	 */
	public static class DefaultColors implements BukkitHelpColors {
		
		@Override
		public ChatColor getSeparatorColor() {
			return ChatColor.AQUA;
		}
		
		@Override
		public ChatColor getArgsColor() {
			return ChatColor.AQUA;
		}
		
		@Override
		public ChatColor getAliasColor() {
			return ChatColor.DARK_AQUA;
		}
		
		@Override
		public ChatColor getDescriptionColor() {
			return ChatColor.DARK_AQUA;
		}
		
	}
	
	private BukkitHelpColors _colors;
	
	/**
	 * Construct a new {@link BukkitHelpListing} using the default colors.
	 * 
	 * @param fullCmd
	 *            The full command String use when activate the help message
	 * @param commands
	 *            A possible commands to show
	 */
	public BukkitHelpListing(String fullCmd, List<Command> commands) {
		this(fullCmd, commands, new DefaultColors());
	}
	
	/**
	 * Construct a new {@link BukkitHelpListing} using the give colors. If <code>colors</code> is <code>null</code> then the default colors will be used.
	 * 
	 * @param fullCmd
	 *            The full command String use when activate the help message
	 * @param commands
	 *            A possible commands to show
	 * @param colors
	 *            The colors to use
	 */
	public BukkitHelpListing(String fullCmd, List<Command> commands, BukkitHelpColors colors) {
		super(fullCmd, commands);
		_colors = colors == null ? new DefaultColors() : colors;
	}
	
	@Override
	public String formatFullCommand(String full) {
		return _colors.getAliasColor() + full;
	}
	
	@Override
	public String formatDescription(String desc) {
		return _colors.getDescriptionColor() + super.formatDescription(desc);
	}
	
	@Override
	public String formatArg(String arg, boolean required) {
		return _colors.getArgsColor() + super.formatArg(arg, required);
	}
	
	@Override
	public String formatAliases(List<String> aliases) {
		return _colors.getAliasColor() + ListUtil.join(aliases, _colors.getSeparatorColor() + "/" + _colors.getAliasColor());
	}
	
	@Override
	public List<String> getHelp() {
		return getHelp(1);
	}
	
	@Override
	public List<String> getHelp(int page) {
		List<String> help = new ArrayList<String>();
		
		for (Command c : getCommands())
			help.add("  " + getCommandHelp(c));
		
		Paginator<String> paginated = new Paginator<String>(7);
		paginated.getElements().addAll(help);
		if (!paginated.hasPage(page))
			return null;
		
		List<String> shownHelp = new ArrayList<String>(paginated.getPage(page));
		String alias = _colors.getAliasColor().toString();
		String separator = _colors.getSeparatorColor().toString();
		String desc = _colors.getDescriptionColor().toString();
		String header = String.format("%sCommand Help: %s/%s <command>", desc, separator, alias + getFullCmd());
		if (paginated.hasPage(2))
			header += String.format("%sPage %s/%s", desc, page + alias + separator, paginated.pages());
		shownHelp.add(0, header);
		shownHelp.add(1, desc + "Commands:");
		
		return shownHelp;
	}
}
