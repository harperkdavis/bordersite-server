package net.packets.client;

import net.packets.Packet;

public class PongPacket extends Packet {

    private long sentTimestamp;

    public PongPacket(long sentTimestamp) {
        super(12);
        this.sentTimestamp = sentTimestamp;
    }

    public PongPacket() {
        super(12);
        this.sentTimestamp = 0;
    }

    public long getSentTimestamp() {
        return sentTimestamp;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Pong: " + sentTimestamp);
    }
}
