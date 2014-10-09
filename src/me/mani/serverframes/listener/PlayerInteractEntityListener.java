package me.mani.serverframes.listener;

import me.mani.serverframes.ServerFrame;
import me.mani.serverframes.ServerFrames;
import me.mani.serverframes.config.ConfigManager;
import me.mani.serverframes.config.ServerData;
import me.mani.serverframes.ping.ServerInfo;
import me.mani.serverframes.ping.ServerPing;

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
