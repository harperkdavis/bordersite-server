package net;

import com.esotericsoftware.kryonet.Connection;
import engine.game.InputState;
import engine.game.NetPlayer;
import engine.game.PlayerHandler;
import engine.math.Vector2f;
import engine.math.Vector3f;
import main.ConsoleReader;
import java.util.List;
import net.packets.Packet;
import net.packets.client.*;
import net.packets.server.*;

import java.net.InetSocketAddress;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class PacketInterpreter {

    public static void interpret(Connection connection, Packet packet) {

        if (packet instanceof ConnectPacket) {
            System.out.println("[PACKET] Incoming packet with type: " + packet.getPacketType());
            packet.printData();
            ServerHandler.VerificationResult verification = null;
            try {
                verification = ServerHandler.verify(((ConnectPacket) packet).getSessionID());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (verification == null) {
                connection.sendTCP(new ConnectionDeniedPacket("Unknown error during verification. The authentication servers may be down."));
            } else if (verification.isVerified()) {
                if (ServerHandler.getPlayer(verification.getUuid()) != null) {
                    connection.sendTCP(new ConnectionDeniedPacket("Player is already on server!"));
                } else {
                    InetSocketAddress address = connection.getRemoteAddressTCP();
                    NetPlayer player = ServerHandler.addPlayer(address.getAddress().toString(), address.getPort(), connection.getID(), verification.getUsername(), verification.getUuid());
                    System.out.println("[INFO] " + player.getUsername() + " has joined the server!");
                    ServerHandler.broadcastMessage(player.getUsername() + " has joined the server", 1.0f, 1.0f, 0.9f);
                    connection.sendUDP(new PingRequestPacket());
                    connection.sendTCP(new ConnectionAcceptedPacket(player.getPlayerId()));
                }
            } else {
                connection.sendTCP(new ConnectionDeniedPacket(verification.getReason()));
            }
        } else if (packet instanceof ChatRequestPacket) {
            System.out.println("[PACKET] Incoming packet with type: " + packet.getPacketType());
            packet.printData();
            handleChatRequestPacket(connection, (ChatRequestPacket) packet);
        } else if (packet instanceof TeamSelectPacket) {
            System.out.println("[PACKET] Incoming packet with type: " + packet.getPacketType());
            packet.printData();
            handleTeamSelectPacket(connection, (TeamSelectPacket) packet);
        } else if (packet instanceof UserInputPacket) {
            handleUserInputPacket(connection, (UserInputPacket) packet);
        } else if (packet instanceof PongPacket) {
            handlePongPacket(connection, (PongPacket) packet);
        }

    }

    private static void handleChatRequestPacket(Connection connection, ChatRequestPacket packet) {
        if (packet.getMessage().length() == 0) {
            return;
        }
        NetPlayer sender = ServerHandler.getPlayerKryoId(connection.getID());
        if (sender == null) {
            System.err.println("[ERROR] Unregistered connection attempted to send chat message! ");
            System.err.println("Ip: " + connection.getRemoteAddressTCP().toString());
            return;
        }
        if (packet.getMessage().charAt(0) == '/') {
            if (packet.getMessage().length() == 1) {
                return;
            }
            int level = 1;
            if (((List<String>) ServerHandler.getServerInfo().get("admins")).contains(sender.getUsername())) {
                level = 3;
            } else if (((List<String>) ServerHandler.getServerInfo().get("mods")).contains(sender.getUsername())) {
                level = 2;
            }
            String response = ConsoleReader.execute(packet.getMessage().substring(1), level, sender);
            connection.sendTCP(new ChatPacket(response, 0.9f, 0.9f, 0.9f));
        } else {
            if (sender.getPlayer().getTeam() == 0) {
                String chatMessage = "[TRIPOROV " + sender.getUsername() + "] " + packet.getMessage();
                ServerHandler.broadcastMessage(chatMessage, ServerHandler.RED_COLOR);
            } else {
                String chatMessage = "[KISELEV " + sender.getUsername() + "] " + packet.getMessage();
                ServerHandler.broadcastMessage(chatMessage, ServerHandler.BLUE_COLOR);
            }
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

        PlayerHandler.respawn(sender, ServerTick.getSubtick());

        ServerHandler.getServer().sendToAllExceptTCP(connection.getID(), new PlayerSpawnPacket(sender.getPlayerId(), sender.getUuid(), sender.getUsername(), team));
        connection.sendTCP(new WorldSpawnPacket(ServerTick.getTick(), team, ServerHandler.getPlayerList()));

        if (team == 0) {
            ServerHandler.broadcastMessage(sender.getUsername() + " has joined the Triporov Federation (Red)", ServerHandler.DARK_RED_COLOR);
        } else {
            ServerHandler.broadcastMessage(sender.getUsername() + " has joined the Kiselev Coalition (Blue)", ServerHandler.DARK_BLUE_COLOR);
        }
        sender.setRegistered(true);
    }

    private static void handleUserInputPacket(Connection connection, UserInputPacket packet) {
        int kryoId = connection.getID();
        NetPlayer sender = ServerHandler.getPlayerKryoId(kryoId);
        if (sender == null) {
            System.err.println("[ERROR] Unregistered connection attempted to send an input! ");
            System.err.println("Ip: " + connection.getRemoteAddressTCP().toString());
            return;
        }
        sender.getPlayer().setInputSequence(packet.getInputSequence());
        for (int i = 0; i < 10; i++) {
            InputState inputs = sender.getMostRecentSnapshot();
            for (InputSnapshot snapshot : packet.getInputs()) {
                if (i == snapshot.getTimestamp()) {
                    inputs = new InputState(snapshot.getInputs(), snapshot.getRotation(), snapshot.getForward());
                }
            }
            if (inputs == null) {
                continue;
            }
            sender.setOrAddInput(ServerTick.getSubtick() + i, inputs);
            sender.setMostRecentSnapshot(inputs);
        }
    }

    private static void handlePongPacket(Connection connection, PongPacket packet) {
        int kryoId = connection.getID();
        NetPlayer sender = ServerHandler.getPlayerKryoId(kryoId);
        if (sender == null) {
            System.err.println("[ERROR] Unregistered connection attempted to send pong packet! ");
            System.err.println("Ip: " + connection.getRemoteAddressTCP().toString());
            return;
        }
        sender.getPlayer().setPing(System.currentTimeMillis() - packet.getSentTimestamp());
    }
}
