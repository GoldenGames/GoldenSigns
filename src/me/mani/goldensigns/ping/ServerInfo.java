package me.mani.goldensigns.ping;

public class ServerInfo {
	
	private ServerPing ping;
	
	private String serverName;
	private String motd;
	private int onlinePlayers;
	private int maxPlayers;
	private boolean isOnline;
	
	public ServerInfo(ServerPing ping, String serverName) {
		this.ping = ping;
		
		this.serverName = serverName;
		this.motd = "-1";
		this.onlinePlayers = -1;
		this.maxPlayers = -1;
		this.isOnline = false;
	}
	
	public ServerInfo update() {
		boolean isOnline = ping.fetchData();
		
		this.motd = ping.getMotd();
		this.onlinePlayers = ping.getPlayersOnline();
		this.maxPlayers = ping.getMaxPlayers();
		this.isOnline = isOnline;
		
		return this;
	}
	
	public String getServerName() {
		return this.serverName;
	}
	
	public void setServerName(String name) {
		this.serverName = name;
	}
	
	public String getMotd() {
		return this.motd;
	}
	
	public int getOnlinePlayers() {
		return this.onlinePlayers;
	}
	
	public int getMaxPlayers() {
		return this.maxPlayers;
	}
	
	public boolean isOnline() {
		return this.isOnline;
	}
	
	public boolean isFull() {
		return getMaxPlayers() == getOnlinePlayers();
	}
	
	public boolean equals(ServerInfo info) {
		if (!this.motd.equals(info.getMotd()))
			return false;
		if (this.onlinePlayers != info.getOnlinePlayers())
			return false;
		if (this.maxPlayers != info.getMaxPlayers())
			return false;
		if (this.isOnline != info.isOnline)
			return false;
		return true;
	}

}
