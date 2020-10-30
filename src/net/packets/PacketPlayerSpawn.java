package net.packets;

import game.Player;
import net.Server;

public class PacketPlayerSpawn extends Packet {

    private Player player;

    public PacketPlayerSpawn(Player player) {
        super(04);
    }

    @Override
    public byte[] getData() {
        return player.getSpawnPacket();
    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData());
    }
}
