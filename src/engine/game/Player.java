package engine.game;

import java.util.UUID;

public class Player {

    private final int playerId;
    private final String uuid;
    private final String ipAddress;
    private final int port;

    private final String username;

    public Player(int playerId, String ipAddress, int port, String username) {
        this.playerId = playerId;
        this.ipAddress = ipAddress;
        this.port = port;
        this.uuid = UUID.randomUUID().toString();
        this.username = username;
    }


    public int getPlayerId() {
        return playerId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }
}
