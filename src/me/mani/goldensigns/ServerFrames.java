package me.mani.goldensigns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import me.mani.goldensigns.config.ConfigManager;
import me.mani.goldensigns.listener.PlayerInteractEntityListener;
import me.mani.goldensigns.ping.ServerInfo;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerFrames extends JavaPlugin {
	
	private static ServerFrames instance;
	public Logger log = Logger.getLogger("Minecraft");
	private List<ServerFrame> allFrames = new ArrayList<>();

	@Override
	public void onEnable() {
		
		// Config
		
		ConfigManager cfgMng = new ConfigManager(new File("plugins/ServerFrames/config.yml"));
		ConfigurationSerialization.registerClass(SaveFrame.class);
		
		// BungeeCord
		
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		// Load Frames
		
		this.allFrames = ConfigManager.loadAll(new File("plugins/ServerFrames/frames.yml"));
		
		instance = this;
		Bukkit.getPluginManager().registerEvents(new PlayerInteractEntityListener(this), this);		
		
	}
	
	@Override
	public void onDisable() {
		
		// Save Frames
		
		if (allFrames.size() >= 1)
			ConfigManager.saveAll(new File("plugins/ServerFrames/frames.yml"), allFrames);
		
	}
	
	public static ServerFrames getInstance() {
		return instance;
	}
	
	public void addFrame(ServerFrame frame) {
		this.allFrames.add(frame);
		
	}
	
	public void removeFrame(ServerFrame frame) {
		if (this.allFrames.contains(frame))
			this.allFrames.remove(frame);
	}
	
	public ServerFrame getFrame(ServerInfo info) {
		for (ServerFrame frame : this.allFrames) {
			if (frame.getInfo().equals(info))
				return frame;
		}
		return null;
	}

}
