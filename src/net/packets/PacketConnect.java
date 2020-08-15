package net.packets;

import engine.math.ByteUtil;
import net.Server;

import java.net.InetAddress;

public class PacketConnect extends Packet {

    private int playerId;
    private InetAddress address;
    private int port;

    public PacketConnect(int playerId, InetAddress address, int port) {
        super(1);
        this.playerId = playerId;
        this.address = address;
        this.port = port;
    }

    @Override
    public byte[] getData() {
        byte[] bytes = new byte[4];
        byte[] playerIdBytes = ByteUtil.intToBytes(playerId);
        bytes[0] = 48;
        bytes[1] = 49;

        bytes[2] = playerIdBytes[2];
        bytes[3] = playerIdBytes[3];

        return bytes;
    }

    @Override
    public void writeData(Server server) {
        server.sendData(getData(), address, port);
    }

}
