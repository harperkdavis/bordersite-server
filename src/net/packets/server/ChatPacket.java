package net.packets.server;

import net.packets.Packet;

public class ChatPacket extends Packet {

    private final String message;
    private float red = 1, green = 1, blue = 1;

    public ChatPacket(String message, float red, float green, float blue) {
        super(4);
        this.message = message;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public ChatPacket(String message) {
        super(4);
        this.message = message;
    }

    public ChatPacket() {
        super(4);
        this.message = "";
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Message: " + message);
        System.out.println("Color: " + red * 255 + ", " + green * 255 + ", " + blue * 255);
    }

    public String getMessage() {
        return message;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

}
