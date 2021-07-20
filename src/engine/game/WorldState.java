package engine.game;

import net.ServerHandler;

import java.util.ArrayList;

public class WorldState {

    private ArrayList<Player> players;
    private long timestamp;

    public WorldState() {
        players = new ArrayList<>();
        this.timestamp = System.currentTimeMillis();
    }

    public static WorldState createWorldState() {
        WorldState state = new WorldState();
        for (Player player : ServerHandler.getPlayerList()) {
            state.players.add(player.copy());
        }
        return state;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
