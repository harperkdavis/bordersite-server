package net;

import com.esotericsoftware.kryonet.Connection;
import engine.game.NetPlayer;
import engine.game.Player;
import main.Main;
import net.packets.Packet;
import net.packets.client.ChatRequestPacket;
import net.packets.client.ConnectPacket;
import net.packets.client.TeamSelectPacket;
import net.packets.server.ChatPacket;
import net.packets.server.ConnectionReceivedPacket;
import net.packets.server.PlayerSpawnPacket;
import net.packets.server.WorldSpawnPacket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

public class PacketInterpreter {

    public static void interpret(Connection connection, Packet packet) {

        System.out.println("[PACKET] Incoming packet with type: " + packet.getPacketType());
        packet.printData();

        if (packet instanceof ConnectPacket) {
            ConnectPacket crp = (ConnectPacket) packet;
            InetSocketAddress address = connection.getRemoteAddressTCP();
            NetPlayer player = ServerHandler.addPlayer(address.getAddress().toString(), address.getPort(), connection.getID(), crp.getUsername());
            System.out.println(player.getUsername() + " has joined the server!");
            ServerHandler.broadcastMessage(player.getUsername() + " has joined the server", 1.0f, 1.0f, 0.8f);
            connection.sendTCP(new ConnectionReceivedPacket(player.getPlayerId()));
        } else if (packet instanceof ChatRequestPacket) {
            handleChatRequestPacket(connection, (ChatRequestPacket) packet);
        } else if (packet instanceof TeamSelectPacket) {
            handleTeamSelectPacket(connection, (TeamSelectPacket) packet);
        }

    }

    private static void handleChatRequestPacket(Connection connection, ChatRequestPacket packet) {
        if (packet.getMessage().equals("")) {
            return;
        }
        NetPlayer sender = ServerHandler.getPlayerKryoId(connection.getID());
        if (sender == null) {
            System.err.println("[ERROR] Unregistered connection attempted to send chat message! ");
            System.err.println("Ip: " + connection.getRemoteAddressTCP().toString());
            return;
        }
        if (sender.getPlayer().getTeam() == 0) {
            String chatMessage = "[TRIPOROV " + sender.getUsername() + "] " + packet.getMessage();
            ServerHandler.broadcastMessage(chatMessage, 1.0f, 0.5f, 0.45f);
        } else {
            String chatMessage = "[KISELEV " + sender.getUsername() + "] " + packet.getMessage();
            ServerHandler.broadcastMessage(chatMessage, 0.45f, 0.5f, 1.0f);
        }
    }

    private static void handleTeamSelectPacket(Connection connection, TeamSelectPacket packet) {
        int kryoId = connection.getID();
        NetPlayer sender = ServerHandler.getPlayerKryoId(kryoId);
        if (sender == null) {
            System.err.println("[ERROR] Unregistered connection attempted to join a team! ");
            System.err.println("Ip: " + connection.getRemoteAddressTCP().toString());
            return;
        }
        int team = packet.getTeam();
        sender.getPlayer().setTeam(team);

        ServerHandler.getServer().sendToAllExceptTCP(connection.getID(), new PlayerSpawnPacket(sender.getPlayerId(), sender.getUuid(), sender.getUsername(), team));
        connection.sendTCP(new WorldSpawnPacket(ServerHandler.getTick(), team, ServerHandler.getPlayerList()));

        if (team == 0) {
            ServerHandler.broadcastMessage(sender.getUsername() + " has joined the Triporov Federation (Red)", 0.8f, 0.05f, 0.05f);
        } else {
            ServerHandler.broadcastMessage(sender.getUsername() + " has joined the Kiselev Coalition (Blue)", 0.05f, 0.05f, 0.8f);
        }
        sender.setRegistered(true);
    }
}
