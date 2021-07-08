package net.packets.client;

import net.packets.Packet;

public class ConnectPacket extends Packet {

    private final String username;

    public ConnectPacket(String username) {
        super(1);
        this.username = username;
    }

    public ConnectPacket() {
        super(1);
        this.username = "";
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Username: " + username);
    }
}
