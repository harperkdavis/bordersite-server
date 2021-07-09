package net;

import com.esotericsoftware.kryonet.Server;
import engine.game.Player;
import main.Main;
import net.packets.server.ChatPacket;

import java.util.HashMap;
import java.util.Map;

public class ServerHandler {

    private final static Map<Integer, Player> players = new HashMap<>();
    private static int nextPlayerId = 0;

    public static Player addPlayer(String ip, int port, int kryoId, String username) {
        Player newPlayer = new Player(nextPlayerId, kryoId, ip, port, username);
        players.put(nextPlayerId, newPlayer);
        nextPlayerId ++;
        return newPlayer;
    }

    public static Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    public static Player getPlayer(String uuid) {
        for (Player p : players.values()) {
            if (p.getUuid().equals(uuid)) {
                return p;
            }
        }
        return null;
    }

    public static Player getPlayerKryoId(int kryoId) {
        for (Player p : players.values()) {
            if (p.getKryoId() == kryoId) {
                return p;
            }
        }
        return null;
    }

    public static Player getPlayer(String ip, int port) {
        for (Player p : players.values()) {
            if (p.getIpAddress().equals(ip)) {
                return p;
            }
        }
        return null;
    }

    public static void broadcastMessage(String message, float r, float g, float b) {
        Main.getServer().sendToAllTCP(new ChatPacket(message, r, g, b));
    }

}
