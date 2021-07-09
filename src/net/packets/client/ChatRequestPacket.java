package net.packets.client;

import net.packets.Packet;

public class ChatRequestPacket extends Packet {

    private final String message;

    public ChatRequestPacket(String message) {
        super(3);
        this.message = message;
    }

    public ChatRequestPacket() {
        super(3);
        this.message = "";
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Message: " + message);
    }

    public String getMessage() {
        return message;
    }

}
