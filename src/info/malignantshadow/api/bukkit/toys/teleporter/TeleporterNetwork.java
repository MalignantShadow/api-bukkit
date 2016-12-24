package info.malignantshadow.api.bukkit.toys.teleporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import info.malignantshadow.api.bukkit.BukkitMessages;
import info.malignantshadow.api.bukkit.players.BukkitWorld;
import info.malignantshadow.api.util.ListUtil;

public class TeleporterNetwork implements Listener {
	
	private List<Teleporter> _teleporters;
	private List<Player> _allowedPlayers;
	
	public TeleporterNetwork() {
		_teleporters = new ArrayList<Teleporter>();
		_allowedPlayers = new ArrayList<Player>();
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
