package info.malignantshadow.api.bukkit.toys.teleporter;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;

public class Teleporter {
	
	private Location _loc;
	private double _enterRotation, _exitRotation;
	private String _channel;
	private boolean _transmitter, _receiver;
	
	public Teleporter(Location location, double enter, double exit, String channel, boolean transmit, boolean receive) {
		_loc = location;
		_enterRotation = enter;
		_exitRotation = exit;
		_channel = channel;
		_transmitter = transmit;
		_receiver = receive;
	}
	
	public Location getLocation() {
		return _loc.clone();
	}
	
	public double getEnterRotation() {
		return _enterRotation;
	}
	
	public double getExitRotation() {
		return _exitRotation;
	}
	
	public String getChannel() {
		return _channel;
	}
	
	public boolean isTransmitter() {
		return _transmitter;
	}
	
	public boolean isReceiver() {
		return _receiver;
	}
	
	public boolean locationMatches(Location location) {
		if (location == null)
			return false;
		
		return location.getBlockX() == _loc.getBlockX() &&
			location.getBlockY() == _loc.getBlockY() &&
			location.getBlockZ() == _loc.getBlockZ();
	}
	
	public void teleport(Entity entity, Teleporter teleporter) {
		if (entity == null || teleporter == null)
			return;
		
		Location el = entity.getLocation();
		double r = teleporter.getExitRotation() + (_enterRotation - el.getYaw());
		Location loc = teleporter.getLocation();
		loc.setPitch(el.getPitch());
		loc.setY(r);
		entity.teleport(loc);
		loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMEN_TELEPORT, 1, new Random().nextFloat() * 2);
	}
	
}
