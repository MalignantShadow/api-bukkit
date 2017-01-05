package info.malignantshadow.api.bukkit.toys.teleporter;

import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;

import info.malignantshadow.api.bukkit.helpers.BukkitConfigs;
import info.malignantshadow.api.config.ConfigPairing;
import info.malignantshadow.api.config.ConfigSection;
import info.malignantshadow.api.config.ConfigSerializable;

public class Teleporter implements ConfigSerializable {
	
	private Location _loc;
	private double _enterRotation, _exitRotation;
	private String _channel;
	private boolean _transmitter, _receiver;
	
	public static final boolean DEF_TRANSMIT_STATE = true;
	public static final boolean DEF_RECEIVE_STATE = false;
	
	public Teleporter(Location location) {
		this(location, "");
	}
	
	public Teleporter(Location location, String channel) {
		this(location, 0, 180, channel, DEF_TRANSMIT_STATE, DEF_RECEIVE_STATE);
	}
	
	public Teleporter(Location location, double enter, double exit, String channel, boolean transmit, boolean receive) {
		setLocation(location);
		_enterRotation = enter;
		_exitRotation = exit;
		setChannel(channel);
		_transmitter = transmit;
		_receiver = receive;
	}
	
	public Location getLocation() {
		return _loc.clone();
	}
	
	public void setLocation(Location location) {
		Validate.notNull(location);
		Validate.notNull(location.getWorld());
		_loc = location;
	}
	
	public double getEnterRotation() {
		return _enterRotation;
	}
	
	public void setEnterRotation(double rotation) {
		_enterRotation = rotation;
	}
	
	public double getExitRotation() {
		return _exitRotation;
	}
	
	public void setExitRotation(double rotation) {
		_exitRotation = rotation;
	}
	
	public String getChannel() {
		return _channel;
	}
	
	public void setChannel(String channel) {
		Validate.notNull(channel);
		_channel = channel;
	}
	
	public boolean isTransmitter() {
		return _transmitter;
	}
	
	public void setTransmitter(boolean canTransmit) {
		_transmitter = canTransmit;
	}
	
	public boolean isReceiver() {
		return _receiver;
	}
	
	public void setReceiver(boolean canReceive) {
		_receiver = canReceive;
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
	
	@Override
	public ConfigSection serializeAsConfig() {
		ConfigSection root = new ConfigSection();
		ConfigSection locSection = BukkitConfigs.putLocationAsSection(_loc);
		locSection.remove("yaw");
		locSection.remove("pitch");
		root.set(locSection, "location");
		root.set(_channel, "channel");
		root.set(_transmitter, "transmit");
		root.set(_receiver, "receive");
		root.set(_enterRotation, "enter_rotation");
		root.set(_enterRotation, "exit_rotation");
		return root;
	}
	
	public static Teleporter fromConfig(ConfigSection config) {
		if (config == null)
			return null;
		
		ConfigPairing locPair = config.get("location");
		Location location = BukkitConfigs.getLocation(null, locPair);
		if (location == null)
			return null;
		
		String channel = config.getString("channel");
		
		boolean transmit = config.getBoolean(DEF_TRANSMIT_STATE, "transmit");
		boolean receive = config.getBoolean(DEF_RECEIVE_STATE, "receive");
		
		double enterRotation = config.getNumber("enter_rotation").doubleValue();
		double exitRotation = config.getNumber(enterRotation - 180, "enter_rotation").doubleValue();
		
		return new Teleporter(location, enterRotation, exitRotation, channel, transmit, receive);
	}
	
}
