package net;

import com.esotericsoftware.kryonet.Server;
import engine.game.Player;

import java.util.HashMap;
import java.util.Map;

public class ServerHandler {

    private final static Map<Integer, Player> players = new HashMap<>();
    private static int nextPlayerId = 0;

    public static Player addPlayer(String ip, int port, String username) {
        Player newPlayer = new Player(nextPlayerId, ip, port, username);
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

    public static Player getPlayer(String ip, int port) {
        for (Player p : players.values()) {
            if (p.getIpAddress().equals(ip)) {
                return p;
            }
        }
        return null;
    }

}
