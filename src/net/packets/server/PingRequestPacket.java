package net.packets.server;

import net.packets.Packet;

public class PingRequestPacket extends Packet {


    public PingRequestPacket() {
        super(11);
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Ping!");
    }
}
