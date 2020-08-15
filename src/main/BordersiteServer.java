package main;

import engine.math.ByteUtil;
import game.Player;
import net.Server;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BordersiteServer implements Runnable {

    private Server socketServer;
    private Thread server;

    public ConcurrentMap<InetAddress, Player> players = new ConcurrentHashMap<>();

    private int nextPID = 0;

    public static void main(String[] args) {
        new BordersiteServer().start();
    }

    public void start() {
        System.out.println("[INFO] Starting server...");
        socketServer = new Server(this);
        socketServer.start();
        server = new Thread(this, "server");
        server.start();
    }

    public void init() {
        System.out.println("[INFO] Server started successfully!");
    }

    public void run() {
        init();
        while (true) {
            socketServer.sendPlayerData();
        }
    }

    public int getNextPID() {
        nextPID++;
        return nextPID;
    }


}
