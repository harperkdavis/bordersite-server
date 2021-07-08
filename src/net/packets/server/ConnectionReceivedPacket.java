package net.packets.server;

import net.packets.Packet;

public class ConnectionReceivedPacket extends Packet {

    private final int playerId;

    public ConnectionReceivedPacket(int playerId) {
        super(2);
        this.playerId = playerId;
    }


    public ConnectionReceivedPacket() {
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
