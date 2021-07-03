package main;

import org.json.simple.JSONObject;

import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BordersiteServer implements Runnable {

    private Thread server;

    public static void main(String[] args) {
        new BordersiteServer().start();
    }

    public void start() {
        System.out.println("[INFO] Starting server...");

        server = new Thread(this, "server");
        server.start();

    }

    @Override
    public void run() {

    }
}
