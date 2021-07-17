package net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import engine.game.NetPlayer;
import engine.game.Player;
import main.Main;
import net.packets.Packet;
import net.packets.client.ChatRequestPacket;
import net.packets.client.ConnectPacket;
import net.packets.client.TeamSelectPacket;
import net.packets.client.UserInputPacket;
import net.packets.server.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ServerHandler {

    private final static Map<Integer, NetPlayer> players = new HashMap<>();
    private static Server server;
    private static int nextPlayerId = 0;
    private static int tick = 0;

    public static void start() {
        server = new Server();
        server.start();

        Kryo kryo = server.getKryo();

        kryo.register(List.class);
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);

        kryo.register(Player.class);

        kryo.register(Packet.class);
        kryo.register(ConnectPacket.class);
        kryo.register(ConnectionReceivedPacket.class);

        kryo.register(ChatRequestPacket.class);
        kryo.register(ChatPacket.class);

        kryo.register(TeamSelectPacket.class);
        kryo.register(PlayerRemovePacket.class);
        kryo.register(PlayerSpawnPacket.class);
        kryo.register(WorldSpawnPacket.class);

        kryo.register(InputSnapshot.class);
        kryo.register(UserInputPacket.class);

        try {
            server.bind(27555, 27777);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("[INFO] " + connection.getRemoteAddressTCP().toString() + " has connected.");
            }

            @Override
            public void received(Connection connection, Object packet) {
                if (packet instanceof Packet) {
                    PacketInterpreter.interpret(connection, (Packet) packet);
                }
            }

            @Override
            public void disconnected(Connection connection) {
                NetPlayer removing = getPlayerKryoId(connection.getID());
                if (removing != null) {
                    ServerHandler.broadcastMessage(removing.getUsername() + " has left the server", 1.0f, 1.0f, 0.8f);
                    server.sendToAllTCP(new PlayerRemovePacket(removing.getUuid(), removing.getPlayerId()));
                    players.remove(removing.getPlayerId());
                } else {
                    System.out.println("[INFO] Unregistered player disconnected: " + connection.getRemoteAddressTCP().toString());
                }
            }
        });
    }

    public static void timeStep() {
        tick++;
    }

    public static NetPlayer addPlayer(String ip, int port, int kryoId, String username) {
        NetPlayer newPlayer = new NetPlayer(ip, port, nextPlayerId, kryoId, username);
        players.put(nextPlayerId, newPlayer);
        nextPlayerId ++;
        return newPlayer;
    }

    public static NetPlayer getPlayer(int playerId) {
        return players.get(playerId);
    }

    public static NetPlayer getPlayer(String uuid) {
        for (NetPlayer p : players.values()) {
            if (p.getUuid().equals(uuid)) {
                return p;
            }
        }
        return null;
    }

    public static List<Player> getPlayerList() {
        List<Player> playerList = new ArrayList<>();
        for (NetPlayer np : players.values()) {
            if (np.isRegistered()) {
                playerList.add(np.getPlayer());
            }
        }
        return playerList;
    }

    public static NetPlayer getPlayerKryoId(int kryoId) {
        for (NetPlayer p : players.values()) {
            if (p.getKryoId() == kryoId) {
                return p;
            }
        }
        return null;
    }

    public static NetPlayer getPlayer(String ip, int port) {
        for (NetPlayer p : players.values()) {
            if (p.getIpAddress().equals(ip)) {
                return p;
            }
        }
        return null;
    }

    public static void broadcastMessage(String message) {
        server.sendToAllTCP(new ChatPacket(message, 1, 1, 1));
    }

    public static void broadcastMessage(String message, float r, float g, float b) {
        server.sendToAllTCP(new ChatPacket(message, r, g, b));
    }

    public static Server getServer() {
        return server;
    }

    public static int getTick() {
        return tick;
    }

}
