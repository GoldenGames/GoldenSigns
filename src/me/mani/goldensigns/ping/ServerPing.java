package me.mani.goldensigns.ping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;

import me.mani.goldensigns.config.ServerData;

import org.bukkit.Bukkit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerPing {

	private InetSocketAddress address;
	private int timeout = 1000;

	private int pingVersion = -1;
	private int protocolVersion = -1;
	private String gameVersion;
	private String motd = "-1";
	private int playersOnline = -1;
	private int maxPlayers = -1;

	public ServerPing(ServerData data) {
		this(data.getIP(), data.getPort());
	}
	
	public ServerPing(String ip, int port) {
		InetSocketAddress inet = new InetSocketAddress(ip, port);
		this.setAddress(inet);
		this.fetchData();
	}

	public boolean fetchData() {
        Socket socket = new Socket();
        try {

            socket.setSoTimeout(this.timeout);

            socket.connect(this.address, getTimeout());

            try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
                    try (ByteArrayOutputStream frame = new ByteArrayOutputStream()) {
                        try (DataOutputStream frameOut = new DataOutputStream(frame)) {

                            // Handshake
                            writeVarInt(0x00, frameOut);
                            writeVarInt(4, frameOut);
                            writeString(this.address.getHostString(), frameOut);
                            frameOut.writeShort(this.address.getPort());
                            writeVarInt(1, frameOut);
                            // Write handshake
                            writeVarInt(frame.size(), out);
                            frame.writeTo(out);
                            frame.reset();

                            // Ping
                            writeVarInt(0x00, frameOut);
                            // Write ping
                            writeVarInt(frame.size(), out);
                            frame.writeTo(out);
                            frame.reset();

                            int len = readVarInt(in);
                            byte[] packet = new byte[len];
                            in.readFully(packet);

                            try (ByteArrayInputStream inPacket = new ByteArrayInputStream(packet)) {
                                try (DataInputStream inFrame = new DataInputStream(inPacket)) {
                                    int id = readVarInt(inFrame);
                                    if (id != 0x00) {
                                        Bukkit.getLogger().log(Level.SEVERE, "Error[0] fetching data from server " + this.address.toString());
                                        return false;
                                    }

                                    JsonParser parser = new JsonParser();
                                    String json = readString(inFrame);
                                    System.out.println(json);
                                    JsonObject jsonObject = parser.parse(json).getAsJsonObject();
                                    JsonObject jsonPlayers = jsonObject.get("players").getAsJsonObject();

                                    playersOnline = Integer.parseInt(jsonPlayers.get("online").toString());
                                    maxPlayers = Integer.parseInt(jsonPlayers.get("max").toString());
                                    motd = jsonObject.get("description").toString().replaceAll("\"", "").trim();
                                }
                            }
                        }
                    }
                }
            }


        } catch (Exception exception) {
            if (!(exception instanceof ConnectException))
                Bukkit.getLogger().log(Level.SEVERE, "Error[1] fetching data from server " + this.address.toString());
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return true;
    }

	public static void writeString(String s, DataOutput out) throws IOException {
		byte[] b = s.getBytes("UTF-8");
		writeVarInt(b.length, out);
		out.write(b);
	}

	public static String readString(DataInput in) throws IOException {
		int len = readVarInt(in);
		byte[] b = new byte[len];
		in.readFully(b);

		return new String(b, "UTF-8");
	}

	public static int readVarInt(DataInput input) throws IOException {
		int out = 0;
		int bytes = 0;
		byte in;
		while (true) {
			in = input.readByte();

			out |= (in & 0x7F) << (bytes++ * 7);

			if (bytes > 32) {
				throw new RuntimeException("VarInt too big");
			}

			if ((in & 0x80) != 0x80) {
				break;
			}
		}

		return out;
	}

	public static void writeVarInt(int value, DataOutput output)
			throws IOException {
		int part;
		while (true) {
			part = value & 0x7F;

			value >>>= 7;
			if (value != 0) {
				part |= 0x80;
			}

			output.writeByte(part);

			if (value == 0) {
				break;
			}
		}
	}

	public InetSocketAddress getAddress() {
		return this.address;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public int getPingVersion() {
		return this.pingVersion;
	}

	public int getProtocolVersion() {
		return this.protocolVersion;
	}

	public String getGameVersion() {
		return this.gameVersion;
	}

	public String getMotd() {
		return this.motd;
	}

	public int getPlayersOnline() {
		return this.playersOnline;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}