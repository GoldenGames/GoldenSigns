package me.mani.goldensigns.listener;

import me.mani.goldensigns.BungeeHandler;
import me.mani.goldensigns.GoldenSigns;
import me.mani.goldensigns.ServerSign;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignClickListener implements Listener {
	
	private GoldenSigns pl;

	public SignClickListener(GoldenSigns pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent ev) {
		Player p = ev.getPlayer();
		
		if (ev.getClickedBlock() == null || ev.getClickedBlock().getType() != Material.WALL_SIGN)
			return;
		
		Sign sign = (Sign) ev.getClickedBlock().getState();
		
		if (!pl.isServerSign(sign))
			return;
		
		ServerSign serverSign = pl.getServerSign(sign);
		
		if (p.isSneaking()) {
			String motd = serverSign.getInfo().getMotd();
			if (motd.contains("\n")) {
				String[] splittedMotd = motd.split("\n");
				p.sendMessage(splittedMotd[0]);
				p.sendMessage(splittedMotd[1]);
			}
			else
				p.sendMessage(motd);
		}
		else 
			BungeeHandler.send(p, serverSign.getInfo());
	}

}
