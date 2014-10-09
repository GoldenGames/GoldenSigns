package me.mani.goldensigns.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.mani.goldensigns.SaveSign;
import me.mani.goldensigns.ServerSign;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	
	private static YamlConfiguration config;
	
	public ConfigManager(File file) {
		config = YamlConfiguration.loadConfiguration(file);
		addDefaults();
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveAll(File file, List<ServerSign> allSigns) {
		YamlConfiguration saveCfg = YamlConfiguration.loadConfiguration(file);
		
		List<SaveSign> allSaveSigns = new ArrayList<>();
		for (ServerSign sign : allSigns) {
			allSaveSigns.add(new SaveSign(sign));	
		}
		
		saveCfg.set("save", allSaveSigns);
		try {
			saveCfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<ServerSign> loadAll(File file) {
		YamlConfiguration saveCfg = YamlConfiguration.loadConfiguration(file);
		List<ServerSign> allSigns = new ArrayList<>();
		List<SaveSign> allSaveSigns = (List<SaveSign>) saveCfg.getList("save");
		if (allSaveSigns == null || allSaveSigns.size() == 0)
			return new ArrayList<>();
		
		for (SaveSign saveSign : allSaveSigns) {
			allSigns.add(saveSign.toServerSign());
		}
		
		return allSigns;
	}
	
	private static void addDefaults() {
		config.options().copyDefaults(true);
		
		config.addDefault("unlock", false);
		
		// Example
		
		if (!isUnlocked()) {
			config.set("server.example.ip", "0.0.0.0");
			config.set("server.example.port", 25565);
		}
	}
	
	public static ServerData getData(String name) {
		if (!config.contains("server." + name))
			return null;
		return new ServerData(config.getString("server." + name + ".ip"), config.getInt("server." + name + ".port"));
	}
	
	public static boolean isUnlocked() {
		return config.getBoolean("unlock");
	}

}
