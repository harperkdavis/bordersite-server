package net;

import com.esotericsoftware.kryonet.Connection;
import engine.game.Player;
import main.Main;
import net.packets.Packet;
import net.packets.client.ChatRequestPacket;
import net.packets.client.ConnectPacket;
import net.packets.server.ChatPacket;
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
            Player player = ServerHandler.addPlayer(address.getAddress().toString(), address.getPort(), connection.getID(), crp.getUsername());
            System.out.println(player.getUsername() + " has joined the server!");
            ServerHandler.broadcastMessage(player.getUsername() + " has joined the server", 0.8f, 0.8f, 1.0f);
            connection.sendTCP(new ConnectionReceivedPacket(player.getPlayerId()));
        } else if (packet instanceof ChatRequestPacket) {
            handleChatRequestPacket(connection, (ChatRequestPacket) packet);
        }

    }

    private static void handleChatRequestPacket(Connection connection, ChatRequestPacket packet) {
        if (packet.getMessage().equals("")) {
            return;
        }
        Player sender = ServerHandler.getPlayerKryoId(connection.getID());
        if (sender == null) {
            System.err.println("[ERROR] Unregistered connection attempted to send chat message! ");
            System.err.println("Ip: " + connection.getRemoteAddressTCP().toString());
            return;
        }
        String chatMessage = "[" + sender.getUsername() + "] " + packet.getMessage();
        ServerHandler.broadcastMessage(chatMessage, 1, 1, 1);
    }
}
