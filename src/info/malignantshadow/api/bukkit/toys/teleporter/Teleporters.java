package info.malignantshadow.api.bukkit.toys.teleporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import info.malignantshadow.api.bukkit.plugin.ShadowAPI;
import info.malignantshadow.api.config.ConfigPairing;
import info.malignantshadow.api.config.ConfigSection;
import info.malignantshadow.api.config.ConfigSequence;
import info.malignantshadow.api.config.processor.extension.YamlConfigProcessor;
import info.malignantshadow.api.util.ListUtil;

public class Teleporters {
	
	//	public static final String GLOBAL_NETWORK_NAME = "__GLOBAL__";
	
	private static boolean HAS_INIT = false;
	private static YamlConfigProcessor YAML;
	private static File CONFIG_FILE;
	private static List<TeleporterNetwork> NETWORKS;
	
	private Teleporters() {
	}
	
	static {
		YAML = new YamlConfigProcessor();
		CONFIG_FILE = new File(ShadowAPI.getInstance().getDataFolder(), "toys/teleporters/networks.yml");
		NETWORKS = new ArrayList<TeleporterNetwork>();
	}
	
	public static void init() {
		if (HAS_INIT)
			return;
		
		ConfigSection config = YAML.getDocument(CONFIG_FILE);
		if (config == null)
			return;
		
		ConfigPairing pair = config.get("networks");
		if (pair == null || !pair.isSection())
			return;
		
		for (ConfigPairing p : pair.asSection()) {
			String name = p.getKey();
			if (!p.isSection())
				continue;
			
			TeleporterNetwork network = TeleporterNetwork.fromConfig(name, p.asSection());
			if (network == null)
				continue;
			
			NETWORKS.add(network);
		}
		HAS_INIT = true;
	}
	
	public static boolean save() {
		try {
			if (!CONFIG_FILE.exists()) {
				CONFIG_FILE.getParentFile().mkdirs();
				CONFIG_FILE.createNewFile();
			}
			
			ConfigSection root = new ConfigSection();
			ConfigSequence networks = new ConfigSequence();
			for (TeleporterNetwork n : NETWORKS)
				networks.add(n.serializeAsConfig());
			
			root.set(networks, "networks");
			YAML.putDocument(root, CONFIG_FILE);
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void add(String name, Teleporter tp) {
		Validate.notNull(tp);
		TeleporterNetwork network = getNetwork(name);
		network.push(tp);
	}
	
	public static TeleporterNetwork getNetwork(String name) {
		TeleporterNetwork network = ListUtil.find(NETWORKS, n -> n.getName().equals(name));
		if (network == null) {
			network = new TeleporterNetwork(name);
			NETWORKS.add(network);
		}
		
		return network;
	}
	
}
