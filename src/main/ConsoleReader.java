package main;

import com.google.gson.internal.$Gson$Preconditions;
import engine.game.NetPlayer;
import engine.game.Player;
import net.ServerHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader implements Runnable {

    @Override
    public void run() {

        while (Main.isRunning()) {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));

            String input = "INVALID INPUT";
            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] all = input.split(" ");;
            String[] args = new String[all.length - 1];
            System.arraycopy(all, 1, args, 0, all.length - 1);
            execute(all[0], args);

        }

    }

    public static void execute(String command, String[] args) {

        if (command.equalsIgnoreCase("stop")) {
            Main.setRunning(false);
            Main.stop();
            ServerHandler.getServer().stop();
            return;
        } else if (command.equalsIgnoreCase("info") && args.length > 0) {
            NetPlayer player = ServerHandler.getPlayer(Integer.parseInt(args[0]));
            if (player != null) {
                System.out.println("--- " + player.getUsername() + " --- UUID " + player.getUuid() + " --- PlayerId " + player.getPlayerId() + " ---");
                System.out.println("Ip: " + player.getIpAddress() + ":" + player.getPort());
            }
            return;
        }

        System.err.println("[ERROR] Invalid command.");
    }

}
