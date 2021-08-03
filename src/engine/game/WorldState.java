package engine.game;

import net.ServerHandler;
import net.ServerTick;
import net.packets.event.Event;

import java.util.ArrayList;

public class WorldState {

    private ArrayList<Player> players;
    private ArrayList<Event> stateEvents;
    private int redScore, blueScore;
    private long timestamp;

    public WorldState() {
        players = new ArrayList<>();
        this.timestamp = System.currentTimeMillis();
    }

    public static WorldState createWorldState() {
        WorldState state = new WorldState();
        state.stateEvents = new ArrayList<>(ServerHandler.getStateEvents());
        state.redScore = GameHandler.redScore;
        state.blueScore = GameHandler.blueScore;
        for (Player player : ServerHandler.getPlayerList()) {
            state.players.add(player.copy());
        }
        for (Player player : ServerHandler.getPlayerList()) {
            if (player.isForceMove()) {
                player.decreaseForceMoveTicks();
            }
        }
        return state;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Event> getStateEvents() {
        return stateEvents;
    }

    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
