package net.packets.server;

import engine.game.Player;
import net.packets.Packet;

import java.util.ArrayList;
import java.util.List;

public class WorldSpawnPacket extends Packet {

    private final int tick, team;
    private final List<Player> playerList;

    public WorldSpawnPacket(int tick, int team, List<Player> playerList) {
        super(8);
        this.tick = tick;
        this.team = team;
        this.playerList = playerList;
    }

    public WorldSpawnPacket() {
        super(8);
        this.tick = 0;
        this.team = 0;
        this.playerList = new ArrayList<>();
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("Tick: " + tick);
        System.out.println("Team: " + team);
        System.out.println("Player List: ");
        for (Player p : playerList) {
            System.out.println(p.toString());
        }
    }

    public int getTick() {
        return tick;
    }

    public int getTeam() {
        return team;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}