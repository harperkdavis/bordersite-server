package main;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import net.PacketInterpreter;
import net.packets.Packet;
import net.packets.client.ConnectPacket;
import net.packets.server.ConnectionReceivedPacket;

import java.io.IOException;

public class Main implements Runnable {

    private Thread serverThread;
    private static Server server;

    public static void main(String[] args) {
        new Main().start();
    }

    public void start() {
        System.out.println("[INFO] Starting server...");

        serverThread = new Thread(this, "server");
        serverThread.start();

        init();
    }

    private void init() {
        Log.set(Log.LEVEL_TRACE);

        server = new Server();
        server.start();

        Kryo kryo = server.getKryo();
        kryo.register(Packet.class);
        kryo.register(ConnectPacket.class);
        kryo.register(ConnectionReceivedPacket.class);


        try {
            server.bind(54555, 54777);
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
        });

    }

    @Override
    public void run() {

    }

    public static Server getServer() {
        return server;
    }
}
