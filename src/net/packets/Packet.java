package net.packets;

import java.util.UUID;

public abstract class Packet {

    private final String uuid;
    private final long timestamp;
    private final int packetType;

    public Packet(int packetType) {
        uuid = UUID.randomUUID().toString();
        timestamp = System.currentTimeMillis();
        this.packetType = packetType;
    }

    public abstract void printData();

    public String getUUID() {
        return uuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getPacketType() {
        return packetType;
    }
}
