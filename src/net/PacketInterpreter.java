package net;

import com.esotericsoftware.kryonet.Connection;
import engine.game.Player;
import net.packets.Packet;
import net.packets.client.ConnectPacket;
import net.packets.server.ConnectionReceivedPacket;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class PacketInterpreter {

    public static void interpret(Connection connection, Packet packet) {

        // System.out.println("[PACKET] Incoming packet with type: " + packet.getPacketType());
        // packet.printData();

        if (packet instanceof ConnectPacket) {
            ConnectPacket crp = (ConnectPacket) packet;
            InetSocketAddress address = connection.getRemoteAddressTCP();
            Player player = ServerHandler.addPlayer(address.getAddress().toString(), address.getPort(), crp.getUsername());
            System.out.println(player.getUsername() + " has joined the server!");
            connection.sendTCP(new ConnectionReceivedPacket(player.getPlayerId()));
        }

    }
}
