package info.malignantshadow.api.bukkit.plugin;

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
	
}
