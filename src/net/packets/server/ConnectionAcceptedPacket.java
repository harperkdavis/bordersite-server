package net.packets.server;

import net.packets.Packet;

public class ConnectionAcceptedPacket extends Packet {

    private final int playerId;

    public ConnectionAcceptedPacket(int playerId) {
        super(2);
        this.playerId = playerId;
    }


    public ConnectionAcceptedPacket() {
        super(2);
        this.playerId = 0;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("PlayerId: " + playerId);
    }
}
