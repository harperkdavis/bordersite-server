package net.packets.server;

import engine.game.WorldState;
import net.packets.Packet;

public class WorldStatePacket extends Packet {

    private final WorldState worldState;

    public WorldStatePacket(WorldState worldState) {
        super(10);
        this.worldState = worldState;
    }

    public WorldStatePacket() {
        super(10);
        this.worldState = null;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("World State: " + worldState);
    }

    public WorldState getWorldState() {
        return worldState;
    }

}
