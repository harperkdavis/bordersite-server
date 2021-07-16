package net.packets.server;

import net.packets.Packet;

public class PlayerRemovePacket extends Packet {

    private final String playerUUID;
    private final int playerId;

    public PlayerRemovePacket(String uuid, int playerId) {
        super(6);
        this.playerUUID = uuid;
        this.playerId = playerId;
    }

    public PlayerRemovePacket() {
        super(6);
        this.playerUUID = "";
        this.playerId = 0;
    }

    @Override
    public void printData() {
        System.out.println("--- Packet " + getUUID() + " - Type " + getPacketType() + " - Unix Timestamp " + getTimestamp() + " ---");
        System.out.println("PlayerId: " + playerId);
        System.out.println("Player UUID: " + playerUUID);
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public int getPlayerId() {
        return playerId;
    }
}
