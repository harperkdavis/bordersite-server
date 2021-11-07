package net.packets.server;

import net.packets.Packet;

public class ConnectionDeniedPacket extends Packet {

    private final String reason;

    public ConnectionDeniedPacket(String reason) {
        super(13);
        this.reason = reason;
    }


    public ConnectionDeniedPacket() {
        super(13);
        this.reason = "No reason given.";
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Reason: " + reason);
    }
}
