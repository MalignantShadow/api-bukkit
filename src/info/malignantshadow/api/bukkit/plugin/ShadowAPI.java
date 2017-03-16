package info.malignantshadow.api.bukkit.plugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import info.malignantshadow.api.bukkit.toys.inventory.VirtualInventoryListener;

public class ShadowAPI extends ShadowPlugin {

	private static ShadowAPI _instance;

	public static final ShadowAPI getInstance() {
		return _instance;
	}

	@Override
	public void onStart() {
		_instance = this;
		registerEvents(new VirtualInventoryListener());
	}

	@Override
	public void onEnd() {

	}

	public static void registerEvents(Plugin plugin, Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}

}
