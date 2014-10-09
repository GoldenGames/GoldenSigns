package me.mani.goldensigns.listener;

import me.mani.goldensigns.ServerFrame;
import me.mani.goldensigns.ServerFrames;
import me.mani.goldensigns.config.ConfigManager;
import me.mani.goldensigns.config.ServerData;
import me.mani.goldensigns.ping.ServerInfo;
import me.mani.goldensigns.ping.ServerPing;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityListener implements Listener {
	
	private ServerFrames pl;

	public PlayerInteractEntityListener(ServerFrames pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent ev) {
		Player p = ev.getPlayer();
		Entity e = ev.getRightClicked();
		if (e instanceof ItemFrame) {
			ItemFrame i = (ItemFrame) e;
			if (i.getItem().getType() != Material.STONE)
				return;
			if (!i.getItem().hasItemMeta())
				return;
			if (!i.getItem().getItemMeta().hasDisplayName())
				return;
			String rawInput = i.getItem().getItemMeta().getDisplayName();
			if (!rawInput.startsWith("[] "))
				return;
			String serverName = rawInput.substring(3);			
			ServerData data = ConfigManager.getData(serverName);
			
			if (data == null) {
				p.sendMessage("§cCannot create Frame! No Server with this name in config!");
				return;
			}
			
			ServerFrame frame = new ServerFrame(serverName, i, new ServerInfo(new ServerPing(data)));
			frame.update();
			pl.addFrame(frame);
		}
	}

}
