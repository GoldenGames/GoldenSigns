package me.mani.goldensigns.listener;

import me.mani.goldensigns.GoldenSigns;
import me.mani.goldensigns.ServerSign;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SignDestroyListener implements Listener {
	
	private GoldenSigns pl;

	public SignDestroyListener(GoldenSigns pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent ev) {
		Player p = ev.getPlayer();
		
		if (ev.getBlock().getType() != Material.WALL_SIGN)
			return;
		
		Sign sign = (Sign) ev.getBlock().getState();
		
		if (!pl.isServerSign(sign))
			return;
		
		ServerSign serverSign = pl.getServerSign(sign);
		
		pl.removeServerSign(serverSign);
		
		p.sendMessage("§aServerSign removed!");
	}

}
