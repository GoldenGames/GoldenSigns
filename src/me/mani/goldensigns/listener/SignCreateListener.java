package me.mani.goldensigns.listener;

import me.mani.goldensigns.GoldenSigns;
import me.mani.goldensigns.ServerSign;
import me.mani.goldensigns.config.ConfigManager;
import me.mani.goldensigns.config.ServerData;
import me.mani.goldensigns.ping.ServerInfo;
import me.mani.goldensigns.ping.ServerPing;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class SignCreateListener implements Listener {
	
	private GoldenSigns pl;

	public SignCreateListener(GoldenSigns pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent ev) {
		Player p = ev.getPlayer();
		Sign sign = (Sign) ev.getBlock().getState();
		
		if (!ev.getLine(0).equals("[]"))
			return;
		
		if (ConfigManager.getData(ev.getLine(1)) == null)
			return;
		
		String serverName = ev.getLine(1);
		ServerData serverData = ConfigManager.getData(serverName);
		
		ServerPing ping = new ServerPing(serverData);
		ServerInfo info = new ServerInfo(ping, serverName);
		
		ServerSign serverSign = new ServerSign(sign, info);
		serverSign.update();
		
		pl.addServerSign(serverSign);
		sign.setMetadata("ServerSign", new FixedMetadataValue(pl, true));
		
		p.sendMessage("§aServerSign created!");
	}
}
