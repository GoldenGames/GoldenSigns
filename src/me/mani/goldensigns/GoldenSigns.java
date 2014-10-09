package me.mani.goldensigns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.mani.goldensigns.config.ConfigManager;
import me.mani.goldensigns.listener.SignClickListener;
import me.mani.goldensigns.listener.SignCreateListener;
import me.mani.goldensigns.listener.SignDestroyListener;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldenSigns extends JavaPlugin {
	
	private static GoldenSigns instance;
	
	private List<ServerSign> allServerSigns = new ArrayList<>();

	@Override
	public void onEnable() {
		
		// Config
		
		ConfigManager cfgMng = new ConfigManager(new File("plugins/ServerFrames/config.yml"));
		
		ConfigurationSerialization.registerClass(SaveSign.class);
		
		// BungeeCord
		
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		// Load Frames
		
		this.allServerSigns = ConfigManager.loadAll(new File("plugins/ServerFrames/frames.yml"));
		
		// Listener
		
		Bukkit.getPluginManager().registerEvents(new SignCreateListener(this), this);	
		Bukkit.getPluginManager().registerEvents(new SignDestroyListener(this), this);
		Bukkit.getPluginManager().registerEvents(new SignClickListener(this), this);
		
		// Starte Updating Scheduler
		
		UpdatingScheduler updater = new UpdatingScheduler(this);
		updater.start();
		
		// Static Zugriff
		
		instance = this;
		
	}
	
	@Override
	public void onDisable() {
		
		// Save Frames
		
		if (allServerSigns.size() >= 1)
			ConfigManager.saveAll(new File("plugins/ServerFrames/frames.yml"), allServerSigns);
		
	}
	
	public static GoldenSigns getInstance() {
		return instance;
	}
	
	public void addServerSign(ServerSign sign) {
		this.allServerSigns.add(sign);
		
	}
	
	public void removeServerSign(ServerSign sign) {
		if (this.allServerSigns.contains(sign))
			this.allServerSigns.remove(sign);
	}
	
	public boolean isServerSign(Sign sign) {
		return sign.hasMetadata("ServerSign");
	}
	
	public ServerSign getServerSign(Sign sign) {
		for (ServerSign serverSign : allServerSigns) {
			if (serverSign.getSign().equals(sign))
				return serverSign;
		}
		return null;
	}

	public List<ServerSign> getAllServerSigns() {
		return allServerSigns;
	}
}
