package me.mani.serverframes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import me.mani.serverframes.config.ConfigManager;
import me.mani.serverframes.config.ServerData;
import me.mani.serverframes.ping.ServerInfo;
import me.mani.serverframes.ping.ServerPing;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.util.NumberConversions;

public class SaveFrame implements ConfigurationSerializable {

	private String serverName;
	
	private String worldName;
	private int x;
	private int y;
	private int z;
	
	private String facing;
	
	public SaveFrame(ServerFrame frame) {
		
		// TODO Fix den Bug das der Code crasht wen die Entity spawnt! Ändere Location auf den Block an dem die Frame hängt!
		
		this(
			frame.getServerName(),
			frame.getItemFrame().getLocation().getWorld().getName(),
			frame.getItemFrame().getLocation().getBlockX(),
			frame.getItemFrame().getLocation().getBlockY(),
			frame.getItemFrame().getLocation().getBlockZ(),
			frame.getItemFrame().getFacing().name()
		);
	}
	
	public SaveFrame(String serverName, String worldName, int x, int y, int z, String facing) {
		this.serverName = serverName;
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.facing = facing;
	}
	
	public ServerFrame toServerFrame() {
		World world = Bukkit.getWorld(worldName);
		Location loc = new Location(world, x, y, z);
		ItemFrame frame = (ItemFrame) world.spawnEntity(loc, EntityType.ITEM_FRAME);
		frame.setFacingDirection(BlockFace.valueOf(facing));
		ServerData data = ConfigManager.getData(serverName);
		
		if (data == null)
			return null;
		
		return new ServerFrame(serverName, frame, new ServerInfo(new ServerPing(data)));
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> saveMap = new HashMap<>();
		
		saveMap.put("serverName", serverName);
		saveMap.put("worldName", worldName);
		saveMap.put("x", x);
		saveMap.put("y", y);
		saveMap.put("z", z);
		saveMap.put("facing", facing);
		
		return saveMap;
	}
	
	public static SaveFrame deserialize(Map<String, Object> saveMap) {
		return new SaveFrame(
			String.valueOf(saveMap.get("serverName")),
			String.valueOf(saveMap.get("worldName")),
			NumberConversions.toInt(saveMap.get("x")),
			NumberConversions.toInt(saveMap.get("y")),
			NumberConversions.toInt(saveMap.get("z")),
			String.valueOf(saveMap.get("facing"))
		);
	}
	
}
