package net.packets.client;

import net.InputSnapshot;
import net.packets.Packet;

import java.util.ArrayList;

public class UserInputPacket extends Packet {

    private final ArrayList<InputSnapshot> inputs;
    private final int inputSequence;

    public UserInputPacket(ArrayList<InputSnapshot> inputs, int inputSequence) {
        super(9);
        this.inputs = inputs;
        this.inputSequence = inputSequence;
    }

    public UserInputPacket() {
        super(9);
        inputs = null;
        this.inputSequence = 0;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Keys: " + inputs);
    }

    public ArrayList<InputSnapshot> getInputs() {
        return inputs;
    }
}


