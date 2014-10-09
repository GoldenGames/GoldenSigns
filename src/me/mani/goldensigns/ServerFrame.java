package me.mani.goldensigns;

import me.mani.goldensigns.ping.ServerInfo;

import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServerFrame {
	
	private String serverName;
	private ItemFrame frame;
	private ServerInfo info;	
	
	public ServerFrame(String name, ItemFrame frame, ServerInfo info) {
		this.serverName = name;
		this.frame = frame;
		this.info = info;
	}
	
	public String getServerName() {
		return this.serverName;
	}
	
	public void setServerName(String name) {
		this.serverName = name;
	}
	
	public ItemFrame getItemFrame() {
		return this.frame;
	}
	
	public void setItemFrame(ItemFrame frame) {
		this.frame = frame;
	}
	
	public ServerInfo getInfo() {
		return this.info;
	}
	
	public void setInfo(ServerInfo info) {
		this.info = info;
	}
	
	public void update() {
		info.update();
		this.frame.setItem(createItem(info));
	}
	
	private ItemStack createItem(ServerInfo info) {	
		short dur;
		String status;
		if (info.isOnline()) {
			dur = 5;
			status = "§aONLINE";
		}
		else {
			dur = 14;
			status = "§cOFFLINE";
		}
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE);
		item.setDurability(dur);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§7[" + status + "§7] §8- " + info.getMotd());
		item.setItemMeta(meta);
		return item;
	}
	
	public void send(Player p) {
		if (!this.getInfo().isOnline()) {
			p.sendMessage("§cDer Server ist offline!");
			return;
		}
		if (this.getInfo().getOnlinePlayers() == this.getInfo().getMaxPlayers()) {
			p.sendMessage("§cDer Server ist voll!");
			return;
		}
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
			out.writeUTF("Connect");
			out.writeUTF(this.getServerName());

			p.sendPluginMessage(ServerFrames.getInstance(), "BungeeCord", out.toByteArray());
		}
		catch (Exception e) {
			p.sendMessage("§cDu konntest nicht gemovet werden!");
		}
	}

}
