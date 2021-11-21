package net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import engine.game.*;
import engine.math.Vector2f;
import engine.math.Vector3f;
import net.http.MotdHandler;
import net.http.RootHandler;
import net.packets.Packet;
import net.packets.client.*;
import net.packets.event.*;
import net.packets.server.*;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler {

    private final static Map<Integer, NetPlayer> players = new ConcurrentHashMap<>();
    public static Map<Integer, WorldState> stateHistory = new ConcurrentHashMap<>();
    private static List<Event> stateEvents = new ArrayList<>();

    private static Server server;
    private static int nextPlayerId = 0;

    private static HttpServer httpServer;
    private static HttpClient httpClient;
    private static Map<String, ?> serverInfo = new HashMap<>();

    public static final Vector3f DARK_RED_COLOR = new Vector3f(175.0f / 256.0f, 12.0f / 256.0f, 21.0f / 256.0f);
    public static final Vector3f DARK_BLUE_COLOR = new Vector3f(13.0f / 256.0f, 53.0f / 256.0f, 120.0f / 256.0f);
    public static final Vector3f RED_COLOR = new Vector3f(210.0f / 256.0f, 41.0f / 256.0f, 45.0f / 256.0f);
    public static final Vector3f BLUE_COLOR = new Vector3f(23.0f / 256.0f, 97.0f / 256.0f, 176.0f / 256.0f);

    @SuppressWarnings("unchecked")
    public static void start() {
        Log.set(Log.LEVEL_TRACE);

        try {
            httpServer = HttpServer.create(new InetSocketAddress(27333), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpServer.createContext("/", new RootHandler());
        httpServer.createContext("/motd", new MotdHandler());
        httpServer.setExecutor(null);
        httpServer.start();

        httpClient = HttpClient.newHttpClient();

        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get("data/server.json"));
            serverInfo = (Map<String, ?>) gson.fromJson(reader, Map.class);
        } catch(IOException e) {
            e.printStackTrace();
        }

        server = new Server();
        server.start();

        Kryo kryo = server.getKryo();

        kryo.register(List.class);
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);

        kryo.register(Vector3f.class);
        kryo.register(Vector2f.class);

        kryo.register(Player.class);

        kryo.register(Event.class);
        kryo.register(HitEvent.class);
        kryo.register(DeathEvent.class);
        kryo.register(RespawnEvent.class);
        kryo.register(ShootEvent.class);

        kryo.register(Packet.class);
        kryo.register(ConnectPacket.class);
        kryo.register(ConnectionAcceptedPacket.class);
        kryo.register(ConnectionDeniedPacket.class);

        kryo.register(PingRequestPacket.class);
        kryo.register(PongPacket.class);

        kryo.register(ChatRequestPacket.class);
        kryo.register(ChatPacket.class);

        kryo.register(TeamSelectPacket.class);
        kryo.register(PlayerRemovePacket.class);
        kryo.register(PlayerSpawnPacket.class);
        kryo.register(WorldSpawnPacket.class);

        kryo.register(InputSnapshot.class);
        kryo.register(UserInputPacket.class);

        kryo.register(WorldState.class);
        kryo.register(WorldStatePacket.class);

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

    public static class VerificationResult {
        private final boolean isVerified;
        private final String reason;

        private final String username, uuid;

        public VerificationResult(boolean isVerified, String reason) {
            this(isVerified, reason, null, null);
        }

        public VerificationResult(boolean isVerified, String reason, String username, String uuid) {
            this.isVerified = isVerified;
            this.reason = reason;
            this.username = username;
            this.uuid = uuid;
        }

        public boolean isVerified() {
            return isVerified;
        }

        public String getReason() {
            return reason;
        }

        public String getUsername() {
            return username;
        }

        public String getUuid() {
            return uuid;
        }
    }

    public static VerificationResult verify(String sessionID) throws IOException, InterruptedException {
        String body = "{ \"sessionID\": \"" + sessionID + "\" }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:7777/verify"))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        System.out.println(response.body());
        Map<?, ?> map = gson.fromJson(response.body(), Map.class);

        if (map.get("result").equals("success")) {
            return new VerificationResult(true, "Successful.", (String) map.get("username"), (String) map.get("uuid"));
        } else if (map.get("result").equals("invalid")) {
            return new VerificationResult(false, "Invalid Session ID. Try relaunching your game.");
        } else if (map.get("result").equals("expired")) {
            return new VerificationResult(false, "Session ID Expired. Try relaunching your game.");
        } else if (map.get("result").equals("banned")) {
            return new VerificationResult(false, "Your account has been banned.");
        }
        return new VerificationResult(false, "Unknown error during verification. The authentication servers may be down.");
    }

    public static NetPlayer addPlayer(String ip, int port, int kryoId, String username, String uuid) {
        NetPlayer newPlayer = new NetPlayer(ip, port, nextPlayerId, kryoId, username, uuid);
        players.put(nextPlayerId, newPlayer);
        nextPlayerId ++;
        return newPlayer;
    }

    public static void executeSubtick(int subtick) {
        for (NetPlayer netPlayer : players.values()) {
            int inputSize = netPlayer.getInputHistory().size();
            if (inputSize <= subtick) {
                for (int i = inputSize; i <= subtick; i++) {
                    netPlayer.setOrAddInput(i, netPlayer.getMostRecentSnapshot());
                }
            }
        }
    }

    public static void executeTick(int tick) {

        if (tick % 50 == 0) {
            server.sendToAllUDP(new PingRequestPacket());
        }
        HashMap<Integer, WorldState> newStateHistory = new HashMap<>(stateHistory);
        for (Integer worldTick : stateHistory.keySet()) {
            if (worldTick < tick - 64) {
                newStateHistory.remove(worldTick);
            }
        }
        stateHistory = newStateHistory;

        // Inputs
        for (NetPlayer netPlayer : players.values()) {
            netPlayer.addTickInputs();
        }
        // Players
        for (int s = 0; s < 10; s++) { // Subtick Step
            for (NetPlayer netPlayer : players.values()) {
                if (netPlayer.isInput("shoot", s)) {
                    PlayerHandler.shoot(netPlayer, s);
                }
                PlayerHandler.updateMovement(netPlayer, s);
            }
        }

        WorldState worldState = WorldState.createWorldState();
        WorldStatePacket worldStatePacket = new WorldStatePacket(worldState);
        stateHistory.put(tick, worldState);
        for (NetPlayer player : players.values()) {
            if (player.isRegistered()) {
                player.sendPacketUDP(worldStatePacket);
            }
        }

        stateEvents.clear();
    }

    public static List<Event> getStateEvents() {
        return stateEvents;
    }

    public static void addEvent(Event event) {
        stateEvents.add(event);
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

    public static NetPlayer getPlayerByName(String name) {
        for (NetPlayer p : players.values()) {
            if (p.getUsername().equals(name)) {
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

    public static void broadcastMessage(String message, Vector3f color) {
        server.sendToAllTCP(new ChatPacket(message, color.getX(), color.getY(), color.getZ()));
    }

    public static void broadcastMessage(String message, float r, float g, float b) {
        server.sendToAllTCP(new ChatPacket(message, r, g, b));
    }

    public static Server getServer() {
        return server;
    }

    public static Map<Integer, NetPlayer> getPlayerMap() {
        return players;
    }

    public static Map<String, ?> getServerInfo() {
        return serverInfo;
    }

    public static void parseHttpQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
        if (query != null) {
            String[] pairs = query.split("[&]");
            for (String pair : pairs) {
                String[] param = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}
