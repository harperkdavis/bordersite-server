package engine.game;

import engine.math.Vector3f;
import net.InputSnapshot;
import net.ServerHandler;
import net.packets.Packet;

import java.util.ArrayList;
import java.util.List;

public class NetPlayer {

    private final String ipAddress;
    private final int port;

    private final int kryoId;

    private Player player;
    private final List<InputState> inputHistory = new ArrayList<>();
    private List<InputState> tickInputs = new ArrayList<>();
    private InputState prevTickInput = new InputState();
    private InputState mostRecentSnapshot = new InputState();

    public NetPlayer(String ipAddress, int port, int playerId, int kryoId, String username, String uuid) {
        player = new Player(playerId, uuid, username);
        this.kryoId = kryoId;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void sendPacketTCP(Packet packet) {
        ServerHandler.getServer().sendToTCP(kryoId, packet);
    }

    public void sendPacketUDP(Packet packet) {
        ServerHandler.getServer().sendToUDP(kryoId, packet);
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

    public List<InputState> getInputHistory() {
        return inputHistory;
    }

    public InputState getMostRecentSnapshot() {
        return mostRecentSnapshot;
    }

    public void setMostRecentSnapshot(InputState mostRecentSnapshot) {
        if (mostRecentSnapshot != null) {
            this.mostRecentSnapshot = mostRecentSnapshot;
        }
    }

    public void setOrAddInput(int index, InputState inputs) {
        if (index >= inputHistory.size()) {
            inputHistory.add(inputs);
        } else {
            inputHistory.set(index, inputs);
        }
    }

    public List<InputState> getTickInputs() {
        return tickInputs;
    }

    public void addTickInputs() {
        if (!tickInputs.isEmpty()) {
            prevTickInput = tickInputs.get(tickInputs.size() - 1);
        } else {
            prevTickInput = new InputState();
        }
        tickInputs.clear();
        for (int i = 0; i < 10; i++) {
            if (inputHistory.size() > 0) {
                tickInputs.add(inputHistory.remove(0));
            }
        }
    }

    public boolean isInput(String input, int subtick) {
        if (subtick < tickInputs.size()) {
            if (tickInputs.get(subtick) == null || tickInputs.get(subtick).getKeys() == null) {
                return mostRecentSnapshot.getKeys().contains(input);
            }
            return tickInputs.get(subtick).getKeys().contains(input);
        } else {
            return mostRecentSnapshot.getKeys().contains(input);
        }
    }

    public boolean isInputDown(String input, int subtick) {
        if (subtick < tickInputs.size()) {
            if ((tickInputs.get(subtick) == null || tickInputs.get(subtick).getKeys() == null)) {
                return false;
            }
            if (subtick == 0) {
                return !prevTickInput.getKeys().contains(input) && tickInputs.get(subtick).getKeys().contains(input);
            } else {
                if ((tickInputs.get(subtick - 1) == null || tickInputs.get(subtick - 1).getKeys() == null)) {
                    return false;
                }
                return !tickInputs.get(subtick - 1).getKeys().contains(input) && tickInputs.get(subtick).getKeys().contains(input);
            }
        } else {
            return mostRecentSnapshot.getKeys().contains(input);
        }
    }

    public boolean isInputUp(String input, int subtick) {
        if (subtick < tickInputs.size()) {
            if (tickInputs.get(subtick) == null || tickInputs.get(subtick).getKeys() == null) {
                return false;
            }
            if (subtick == 0) {
                return prevTickInput.getKeys().contains(input) && !tickInputs.get(subtick).getKeys().contains(input);
            } else {
                if ((tickInputs.get(subtick - 1) == null || tickInputs.get(subtick - 1).getKeys() == null)) {
                    return false;
                }
                return tickInputs.get(subtick - 1).getKeys().contains(input) && !tickInputs.get(subtick).getKeys().contains(input);
            }
        } else {
            return false;
        }
    }

    public Vector3f getCameraRotation(int subtick) {
        if (subtick < tickInputs.size()) {
            if (tickInputs.get(subtick) == null || tickInputs.get(subtick).getKeys() == null) {
                return mostRecentSnapshot.getRotation();
            }
            return tickInputs.get(subtick).getRotation();
        } else {
            return mostRecentSnapshot.getRotation();
        }
    }

    public Vector3f getForward(int subtick) {
        if (subtick < tickInputs.size()) {
            if (tickInputs.get(subtick) == null || tickInputs.get(subtick).getKeys() == null) {
                return mostRecentSnapshot.getForward();
            }
            return tickInputs.get(subtick).getForward();
        } else {
            return mostRecentSnapshot.getForward();
        }
    }
}
