package info.malignantshadow.api.bukkit.toys.teleporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import info.malignantshadow.api.bukkit.helpers.BukkitMessages;
import info.malignantshadow.api.bukkit.helpers.BukkitWorld;
import info.malignantshadow.api.config.ConfigPairing;
import info.malignantshadow.api.config.ConfigSection;
import info.malignantshadow.api.config.ConfigSequence;
import info.malignantshadow.api.config.ConfigSerializable;
import info.malignantshadow.api.util.ListUtil;
import info.malignantshadow.api.util.aliases.Nameable;

public class TeleporterNetwork implements Listener, Nameable, ConfigSerializable {
	
	private String _name;
	private List<Teleporter> _teleporters;
	private List<Player> _allowedPlayers;
	
	public TeleporterNetwork(String name) {
		Validate.notNull(name);
		_name = name;
		_teleporters = new ArrayList<Teleporter>();
		_allowedPlayers = new ArrayList<Player>();
	}
	
	@Override
	public String getName() {
		return _name;
	}
	
	public void allowPlayer(Player player) {
		if (player == null)
			return;
		
		_allowedPlayers.add(player);
	}
	
	public boolean disallowPlayer(Player player) {
		return _allowedPlayers.remove(player);
	}
	
	public boolean isAllowed(Player player) {
		return player != null && _allowedPlayers.contains(player);
	}
	
	public void push(Teleporter teleporter) {
		if (teleporter == null)
			return;
		
		_teleporters.add(teleporter);
	}
	
	public List<Teleporter> getTeleporters() {
		return _teleporters;
	}
	
	public Teleporter getTeleporter(Location location) {
		return ListUtil.find(_teleporters, t -> t.locationMatches(location));
	}
	
	public List<Teleporter> getTeleporters(String channel) {
		return ListUtil.slice(_teleporters, t -> t.getChannel().equals(channel));
	}
	
	public List<Teleporter> getReceivers(String channel) {
		return ListUtil.slice(_teleporters, t -> t.getChannel().equals(channel) && t.isReceiver());
	}
	
	public List<Teleporter> getTransmitters(String channel) {
		return ListUtil.slice(_teleporters, t -> t.getChannel().equals(channel) && t.isTransmitter());
	}
	
	@Override
	public ConfigSection serializeAsConfig() {
		ConfigSection root = new ConfigSection();
		root.set(_name, "name");
		root.set(serializeTeleporters(), "teleporeters");
		return root;
	}
	
	public ConfigSequence serializeTeleporters() {
		ConfigSequence teleporters = new ConfigSequence();
		for (Teleporter t : _teleporters) {
			if (t == null)
				continue;
			
			teleporters.add(t.serializeAsConfig());
		}
		return teleporters;
	}
	
	public static TeleporterNetwork fromConfig(String name, ConfigSection section) {
		TeleporterNetwork network = new TeleporterNetwork(name);
		ConfigPairing teleporterPair = section.get("teleporters");
		if (teleporterPair == null || !teleporterPair.isSequence())
			return network;
		
		for (Object o : teleporterPair.asSequence()) {
			if (!(o instanceof ConfigSection))
				continue;
			
			Teleporter tp = Teleporter.fromConfig((ConfigSection) o);
			if (tp != null)
				network.push(tp);
		}
		
		return network;
	}
	
	public void listen(JavaPlugin managingPlugin) {
		managingPlugin.getServer().getPluginManager().registerEvents(this, managingPlugin);
	}
	
	public void stopListening() {
		PlayerMoveEvent.getHandlerList().unregister(this);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Teleporter teleporter;
		if (!BukkitWorld.differentBlocks(event.getFrom(), event.getTo()) || !isAllowed(player) || (teleporter = getTeleporter(event.getTo())) == null)
			return;
		
		List<Teleporter> receivers = getReceivers(teleporter.getChannel());
		receivers.remove(teleporter);
		if (receivers.isEmpty()) {
			BukkitMessages.sendMessage(player, "&cOh snap! This teleporter is broken. Better contact &4The Admins&c.");
			return;
		}
		
		Teleporter receiver = receivers.size() > 1 ? receivers.get(new Random().nextInt(receivers.size())) : receivers.get(0);
		teleporter.teleport(player, receiver);
	}
	
}
