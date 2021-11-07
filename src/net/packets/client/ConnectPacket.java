package net.packets.client;

import net.packets.Packet;

public class ConnectPacket extends Packet {

    private final String sessionID;

    public ConnectPacket(String sessionID) {
        super(1);
        this.sessionID = sessionID;
    }

    public ConnectPacket() {
        super(1);
        this.sessionID = "";
    }

    public String getSessionID() {
        return sessionID;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Username: " + sessionID);
    }

}
