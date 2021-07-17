package net.packets.client;

import net.InputSnapshot;
import net.packets.Packet;

import java.util.ArrayList;

public class UserInputPacket extends Packet {

    private final ArrayList<InputSnapshot> keybinds;

    public UserInputPacket(ArrayList<InputSnapshot> keybinds) {
        super(9);
        this.keybinds = keybinds;
    }

    public UserInputPacket() {
        super(9);
        keybinds = null;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Keys: " + keybinds);
    }

}
