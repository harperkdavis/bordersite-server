package net.packets;

import game.Player;
import net.Server;

public class PacketPlayerData extends Packet {

    private Player player;

    public PacketPlayerData(Player player) {
        super(02);
        this.player = player;
    }

    @Override
    public byte[] getData() {
        return player.toBytes();
    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData());
    }
}
