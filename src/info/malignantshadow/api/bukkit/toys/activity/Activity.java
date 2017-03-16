package info.malignantshadow.api.bukkit.toys.activity;

import org.bukkit.OfflinePlayer;

import info.malignantshadow.api.bukkit.helpers.BukkitPlayer;
import info.malignantshadow.api.bukkit.helpers.BukkitPlayerList;

public abstract class Activity {

	private BukkitPlayerList _spectators;
	private BukkitPlayerList _participants;

	public Activity() {
		_spectators = new BukkitPlayerList();
		_participants = new BukkitPlayerList();
	}

	public void addParticipant(BukkitPlayer player) {
		_participants.add(player);
	}

	public boolean isParticipant(OfflinePlayer player) {
		return _participants.contains(player);
	}

	public void addSpectator(BukkitPlayer player) {
		_spectators.add(player);
	}

	public boolean isSpectator(OfflinePlayer player) {
		return _spectators.contains(player);
	}

	public BukkitPlayerList getParticipants() {
		return _participants;
	}

	public BukkitPlayerList getSpectators() {
		return _spectators;
	}

	public abstract void start();

	public abstract void end();

}
