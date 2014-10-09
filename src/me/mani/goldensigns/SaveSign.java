package me.mani.goldensigns;

import java.util.HashMap;
import java.util.Map;

import me.mani.goldensigns.config.ConfigManager;
import me.mani.goldensigns.config.ServerData;
import me.mani.goldensigns.ping.ServerInfo;
import me.mani.goldensigns.ping.ServerPing;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

@SuppressWarnings("deprecation")
public class SaveSign implements ConfigurationSerializable {

	private String serverName;
	
	private String worldName;
	private int x;
	private int y;
	private int z;
	
	private byte data;
	
	public SaveSign(ServerSign sign) {
		this(
			sign.getInfo().getServerName(),
			sign.getSign().getLocation().getWorld().getName(),
			sign.getSign().getLocation().getBlockX(),
			sign.getSign().getLocation().getBlockY(),
			sign.getSign().getLocation().getBlockZ(),
			sign.getSign().getRawData()
		);
	}
	
	public SaveSign(String serverName, String worldName, int x, int y, int z, byte data) {
		this.serverName = serverName;
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
	}
	
	public ServerSign toServerSign() {
		World world = Bukkit.getWorld(worldName);
		Location loc = new Location(world, x, y, z);
		Block b = world.getBlockAt(x, y, z);
		b.setType(Material.WALL_SIGN);
		Sign sign = (Sign) b.getState();
		sign.setRawData(data);
		
		ServerData serverData = ConfigManager.getData(serverName);
		
		if (serverData == null)
			return null;
		
		return new ServerSign(sign, new ServerInfo(new ServerPing(serverData), serverName));
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> saveMap = new HashMap<>();
		
		saveMap.put("serverName", serverName);
		saveMap.put("worldName", worldName);
		saveMap.put("x", x);
		saveMap.put("y", y);
		saveMap.put("z", z);
		saveMap.put("data", data);
		
		return saveMap;
	}
	
	public static SaveSign deserialize(Map<String, Object> saveMap) {
		return new SaveSign(
			String.valueOf(saveMap.get("serverName")),
			String.valueOf(saveMap.get("worldName")),
			NumberConversions.toInt(saveMap.get("x")),
			NumberConversions.toInt(saveMap.get("y")),
			NumberConversions.toInt(saveMap.get("z")),
			NumberConversions.toByte(saveMap.get("data"))
		);
	}
	
}
