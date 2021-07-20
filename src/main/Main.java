package main;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import engine.map.MapLevel;
import net.ServerHandler;
import net.ServerTick;

import java.util.Timer;

public class Main {

    private static Thread consoleThread;
    private static ConsoleReader consoleReader;
    private static boolean running = true;

    private static Timer tickTimer;
    private static ServerTick serverTick;
    private static final float DELTA_TIME = 2.0f / 1000.0f;

    public static void main(String[] args) {
        System.out.println("[INFO] Starting server...");

        consoleReader = new ConsoleReader();

        consoleThread = new Thread(consoleReader);
        consoleThread.start();

        init();
    }

    public static void start() {
        tickTimer = new Timer();
        serverTick = new ServerTick();
        tickTimer.scheduleAtFixedRate(serverTick, 0, 2);
    }

    public static void stop() {
        tickTimer.cancel();
        tickTimer.purge();
    }

    private static void init() {
        Log.set(Log.LEVEL_TRACE);

        ServerHandler.start();
        MapLevel.loadMap();

        start();

        System.out.println("[INFO] Server started!");
    }

    public static Thread getConsoleThread() {
        return consoleThread;
    }

    public ConsoleReader getConsoleReader() {
        return consoleReader;
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Main.running = running;
    }

    public static float getDeltaTime() {
        return DELTA_TIME;
    }

}
