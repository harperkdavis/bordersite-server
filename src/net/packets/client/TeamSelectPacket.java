package net.packets.client;

import net.packets.Packet;

public class TeamSelectPacket extends Packet {

    private final int team;

    public TeamSelectPacket(int team) {
        super(7);
        this.team = team;
    }

    public TeamSelectPacket() {
        super(7);
        this.team = 0;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Team: " + team);
    }

    public int getTeam() {
        return team;
    }
}
