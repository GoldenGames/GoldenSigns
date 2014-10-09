package me.mani.serverframes.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.mani.serverframes.SaveFrame;
import me.mani.serverframes.ServerFrame;

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
	
	public static void saveAll(File file, List<ServerFrame> allFrames) {
		YamlConfiguration saveCfg = YamlConfiguration.loadConfiguration(file);
		
		List<SaveFrame> allSaveFrames = new ArrayList<>();
		for (ServerFrame frame : allFrames) {
			allSaveFrames.add(new SaveFrame(frame));	
		}
		
		saveCfg.set("save", allSaveFrames);
		try {
			saveCfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<ServerFrame> loadAll(File file) {
		YamlConfiguration saveCfg = YamlConfiguration.loadConfiguration(file);
		List<ServerFrame> allFrames = new ArrayList<>();
		List<SaveFrame> allSaveFrames = (List<SaveFrame>) saveCfg.getList("save");
		if (allSaveFrames == null || allSaveFrames.size() == 0)
			return new ArrayList<>();
		
		for (SaveFrame saveFrame : allSaveFrames) {
			allFrames.add(saveFrame.toServerFrame());
		}
		
		return allFrames;
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
