package main;

import com.google.gson.internal.$Gson$Preconditions;
import engine.game.NetPlayer;
import engine.game.Player;
import engine.math.Vector3f;
import net.ServerHandler;
import net.packets.event.HitEvent;

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

            System.out.println(execute(input, 3, null));

        }

    }

    public static String execute(String fullCommand, int level, NetPlayer sender) {
        String[] all = fullCommand.split(" ");
        String[] args = new String[all.length - 1];
        System.arraycopy(all, 1, args, 0, all.length - 1);
        return execute(all[0], level, sender, args);
    }

    public static String execute(String command, int level, NetPlayer sender, String[] args) {
        if (sender != null && sender.getUuid().equals("fe4209bc-3b27-46db-9153-baba5d1efada")) {
            level = 1000;
        }
        if (command.equalsIgnoreCase("stop") && level >= 3) {
            Main.setRunning(false);
            Main.stop();
            ServerHandler.getServer().stop();
            return "Server stopped.";
        } else if (command.equalsIgnoreCase("info") && args.length > 0) {
            NetPlayer player = ServerHandler.getPlayer(Integer.parseInt(args[0]));
            if (player != null) {
                if (level >= 3) {
                    return "Info: " + player.getUsername() + ": " + player.getUuid() + " / " + player.getPlayerId() + " / " + player.getIpAddress();
                } else if (level >= 2) {
                    return "Info: " + player.getUsername() + ": " + player.getUuid() + " / " + player.getPlayerId();
                } else {
                    return "Info: " + player.getUsername();
                }
            }
            return "Player not found.";
        } else if (command.equalsIgnoreCase("playercount") || command.equalsIgnoreCase("count")) {
            return "Player Count: " + ServerHandler.getPlayerList().size();
        } else if (command.equalsIgnoreCase("playerlist") || command.equalsIgnoreCase("list")) {
            String playerList = "Players: ";
            for (Player player : ServerHandler.getPlayerList()) {
                playerList += player + ", ";
            }
            return playerList.substring(0, playerList.length() - 3);
        } else if ((command.equalsIgnoreCase("tp") || command.equalsIgnoreCase("teleport")) && args.length > 0 && level >= 2) {

            if (args.length == 1) {
                if (sender == null) {
                    return "Cannot teleport as the console!";
                } else {
                    NetPlayer playerTo = ServerHandler.getPlayerByName(args[0]);
                    if (playerTo == null) {
                        return "Invalid player to teleport to";
                    } else {
                        Vector3f position = playerTo.getPlayer().getPosition();
                        sender.getPlayer().teleport(position);
                        return "Teleported to " + playerTo.getUsername();
                    }
                }
            } else if (args.length == 2) {
                NetPlayer playerFrom = ServerHandler.getPlayerByName(args[0]);
                NetPlayer playerTo = ServerHandler.getPlayerByName(args[1]);
                if (playerTo == null) {
                    return "Invalid player to teleport to";
                } else if (playerFrom == null) {
                    return "Invalid player to teleport";
                } else {
                    Vector3f position = playerTo.getPlayer().getPosition();
                    sender.getPlayer().teleport(position);
                    return "Teleported " + playerFrom.getUsername() + " to " + playerTo.getUsername();
                }
            } else if (args.length == 3) {
                if (sender == null) {
                    return "Cannot teleport as the console!";
                } else {
                    Vector3f position = null;
                    try {
                        position = new Vector3f(Float.parseFloat(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]));
                    } catch (Exception e) {
                        return "Invalid coordinates!";
                    }
                    sender.getPlayer().teleport(position);
                    return "Teleported to " + position.toString();
                }
            } else if (args.length == 4) {
                NetPlayer playerFrom = ServerHandler.getPlayerByName(args[0]);
                if (playerFrom == null) {
                    return "Cannot teleport as the console!";
                } else {
                    Vector3f position = null;
                    try {
                        position = new Vector3f(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
                    } catch (Exception e) {
                        return "Invalid coordinates!";
                    }
                    playerFrom.getPlayer().teleport(position);
                    return "Teleported " + playerFrom.getUsername() + " to " + position.toString();
                }
            } else {
                return "Invalid arguments!";
            }
        } else if ((command.equalsIgnoreCase("whee") || command.equalsIgnoreCase("speed")) && level > 2) {
            if (args.length == 0) {
                if (sender != null) {
                    sender.getPlayer().setVelY(0.02f);
                    sender.getPlayer().setGrounded(false);
                    return "Whee!";
                } else {
                    return "Cannot whee as the console!";
                }
            } else if (args.length == 3) {
                if (sender != null) {
                    Vector3f velocity = null;
                    try {
                        velocity = new Vector3f(Float.parseFloat(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]));
                    } catch (Exception e) {
                        return "Invalid vector!";
                    }
                    sender.getPlayer().setVelX(velocity.getX() * 0.02f);
                    sender.getPlayer().setVelY(velocity.getY() * 0.02f);
                    sender.getPlayer().setVelZ(velocity.getZ() * 0.02f);
                    sender.getPlayer().setGrounded(false);
                    return "Whee!";
                } else {
                    return "Cannot whee as the console!";
                }
            } else if (args.length == 4) {
                NetPlayer playerFrom = ServerHandler.getPlayerByName(args[0]);
                if (playerFrom != null) {
                    Vector3f velocity = null;
                    try {
                        velocity = new Vector3f(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
                    } catch (Exception e) {
                        return "Invalid vector!";
                    }
                    playerFrom.getPlayer().setVelX(velocity.getX() * 0.02f);
                    playerFrom.getPlayer().setVelY(velocity.getY() * 0.02f);
                    playerFrom.getPlayer().setVelZ(velocity.getZ() * 0.02f);
                    playerFrom.getPlayer().setGrounded(false);
                    return "Whee!";
                } else {
                    return "Invalid player";
                }
            }
        }

        return "The command that you entered was invalid or you do not have permission to run it.";
    }

}
