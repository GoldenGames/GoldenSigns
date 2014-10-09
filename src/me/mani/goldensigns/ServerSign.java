package me.mani.goldensigns;

import me.mani.goldensigns.ping.ServerInfo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServerSign {
	
	private Sign sign;
	private ServerInfo info;	
	
	public ServerSign(Sign sign, ServerInfo info) {
		this.sign = sign;
		this.info = info;
	}
	
	public Sign getSign() {
		return this.sign;
	}
	
	public void setSign(Sign sign) {
		this.sign = sign;
	}
	
	public ServerInfo getInfo() {
		return this.info;
	}
	
	public void setInfo(ServerInfo info) {
		this.info = info;
	}
	
	public void update() {
		updateData();
		updateSign();
	}
	
	public void updateData() {
		info.update();
	}
	
	public void updateSign() {
		updateSign(info);
	}
	
	private void updateSign(ServerInfo info) {	
		String status = "---";
		if (info.isOnline() && info.isFull())
			status = "§e[FULL]";
		else if (info.isOnline())
			status = "§a[ONLINE]";
		else
			status = "§c[OFFLINE]";
				
		String motd = info.getMotd().substring(0, 15);
		
		String color = status.substring(0, 2);
		String playerStatus = color + info.getOnlinePlayers() + " §7/ " + color + info.getMaxPlayers();
		
		// Sign Format
		
		/* 
		 * [ %status% ]
		 * 
		 * %motd%
		 * %onlinePlayers% / %maxPlayers%
		 */
		
		sign.setLine(0, status);
		sign.setLine(1, "");
		sign.setLine(2, motd);
		sign.setLine(3, playerStatus);
		
		sign.update(true);		
	}
}
