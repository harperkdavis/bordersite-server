package engine.game;

public class NetPlayer {

    private final String ipAddress;
    private final int port;

    private final int kryoId;

    private Player player;

    public NetPlayer(String ipAddress, int port, int playerId, int kryoId, String username) {
        player = new Player(playerId, username);
        this.kryoId = kryoId;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPlayerId() {
        return player.getPlayerId();
    }

    public int getKryoId() {
        return kryoId;
    }

    public String getUuid() {
        return player.getUuid();
    }

    public String getUsername() {
        return player.getUsername();
    }

    public boolean isRegistered() {
        return player.isRegistered();
    }

    public void setRegistered(boolean registered) {
        player.setRegistered(registered);
    }
}
