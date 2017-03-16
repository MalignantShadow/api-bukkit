package info.malignantshadow.api.bukkit.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import info.malignantshadow.api.util.ListUtil;

public class BukkitPlayerList {

	private List<BukkitPlayer> _players;

	public BukkitPlayerList() {
		_players = new ArrayList<BukkitPlayer>();
	}

	public BukkitPlayerList(int initialCapacity) {
		_players = new ArrayList<BukkitPlayer>(initialCapacity);
	}

	public int size() {
		return _players.size();
	}

	public boolean isEmpty() {
		return _players.isEmpty();
	}

	public boolean add(UUID uuid) {
		OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
		return add(p);
	}

	public boolean add(OfflinePlayer p) {
		if (p == null)
			return false;

		_players.add(new BukkitPlayer(p));
		return true;
	}

	public boolean add(BukkitPlayer p) {
		if (p == null)
			return false;

		_players.add(p);
		return true;
	}

	public boolean contains(OfflinePlayer p) {
		if (p == null)
			return false;

		return contains(p.getUniqueId());
	}

	public boolean contains(UUID uuid) {
		if (uuid == null)
			return false;

		return ListUtil.contains(_players, (p) -> p.getUniqueId().equals(uuid));
	}

	public boolean remove(UUID uuid) {
		return _players.removeIf((p) -> p != null && p.getUniqueId().equals(uuid));
	}

	public boolean remove(OfflinePlayer player) {
		return player != null && remove(player.getUniqueId());
	}

	//TODO: utilities, such as teleportation, etc.

}
