package me.mani.serverframes.config;

public class ServerData {
	
	private String ip;
	private int port;
	
	public ServerData(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public String getIP() {
		return this.ip;
	}
	
	public int getPort() {
		return this.port;
	}

}
