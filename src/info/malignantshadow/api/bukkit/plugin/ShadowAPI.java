package info.malignantshadow.api.bukkit.plugin;

public class ShadowAPI extends ShadowPlugin {
	
	private static ShadowAPI _instance;
	
	public static final ShadowAPI getInstance() {
		return _instance;
	}
	
	@Override
	public void onStart() {
		_instance = this;
	}
	
	@Override
	public void onEnd() {
		
	}
	
}
