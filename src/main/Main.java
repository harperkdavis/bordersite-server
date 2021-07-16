package main;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import net.ServerHandler;

public class Main implements Runnable {

    private static Thread serverThread, consoleThread;
    private ConsoleReader consoleReader;
    private static boolean running = true;

    private static long updateMillisecond;
    int tick = 0;

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

        ServerHandler.start();

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
            ServerHandler.timeStep();
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

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Main.running = running;
    }

}
