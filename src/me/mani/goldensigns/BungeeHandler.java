package me.mani.goldensigns;

import me.mani.goldensigns.ping.ServerInfo;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeHandler {
	
	private static void sendBungeeMessage(Player p, String... args) throws Exception {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		for (String s : args)
			out.writeUTF(s);

		p.sendPluginMessage(GoldenSigns.getInstance(), "BungeeCord", out.toByteArray());
	}
	
	public static void send(Player p, ServerInfo info) {
		if (!info.isOnline()) {
			p.sendMessage("§cDer Server ist offline!");
			return;
		}
		if (info.isFull()) {
			p.sendMessage("§cDer Server ist voll!");
			return;
		}
		try {
			sendBungeeMessage(p, "Connect", info.getServerName());
		}
		catch (Exception e) {
			p.sendMessage("§cDu konntest nicht gemovet werden!");
		}
	}

}
