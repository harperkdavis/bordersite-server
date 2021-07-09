package main;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import net.PacketInterpreter;
import net.packets.Packet;
import net.packets.client.ChatRequestPacket;
import net.packets.client.ConnectPacket;
import net.packets.server.ChatPacket;
import net.packets.server.ConnectionReceivedPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main implements Runnable {

    private static Thread serverThread, consoleThread;
    private ConsoleReader consoleReader;
    private static Server server;
    private static boolean running = true;

    private static long updateMillisecond;

    public static void main(String[] args) {
        new Main().start();
    }

    public void start() {
        System.out.println("[INFO] Starting server...");

        serverThread = new Thread(this);
        serverThread.start();

        consoleReader = new ConsoleReader();

        consoleThread = new Thread(consoleReader);
        consoleThread.start();

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

        kryo.register(ChatRequestPacket.class);
        kryo.register(ChatPacket.class);


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
        });

        updateMillisecond = System.currentTimeMillis();

        System.out.println("[INFO] Server started!");
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("[INFO] Thread interrupted, Stopping server...");
                running = false;
            }
            // update
        }
        System.out.println("[INFO] Server stopped");
    }

    public static Thread getServerThread() {
        return serverThread;
    }

    public static Thread getConsoleThread() {
        return consoleThread;
    }

    public ConsoleReader getConsoleReader() {
        return consoleReader;
    }

    public static Server getServer() {
        return server;
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Main.running = running;
    }

}
