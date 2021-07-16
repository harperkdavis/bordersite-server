package net.packets.server;

import net.packets.Packet;

public class PlayerSpawnPacket extends Packet {

    private final String playerUUID, username;
    private final int playerId, team;

    public PlayerSpawnPacket(int playerId, String uuid, String username, int team) {
        super(5);
        this.playerId = playerId;
        this.playerUUID = uuid;
        this.username = username;
        this.team = team;
    }

    public PlayerSpawnPacket() {
        super(5);
        this.playerId = 0;
        this.playerUUID = "";
        this.username = "";
        this.team = 0;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("PlayerId: " + playerId);
        System.out.println("Player UUID: " + playerUUID);
        System.out.println("Username: " + username);
        System.out.println("Team: " + (team == 0 ? "Red" : "Blue"));
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public String getUsername() {
        return username;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getTeam() {
        return team;
    }
}
