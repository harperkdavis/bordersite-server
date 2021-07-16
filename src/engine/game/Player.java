package engine.game;

import java.util.UUID;

public class Player {

    private final int playerId;
    private final String uuid;

    private final String username;

    private int team = 0;
    private boolean registered = false;

    public Player(int playerId, String username) {
        this.playerId = playerId;
        this.uuid = UUID.randomUUID().toString();
        this.username = username;
    }

    public Player() {
        this.playerId = 0;
        this.uuid = "";
        this.username = "";
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
