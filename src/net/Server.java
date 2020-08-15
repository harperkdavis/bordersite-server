package net;

import engine.math.ByteUtil;
import game.Player;
import main.BordersiteServer;
import net.packets.Packet;
import net.packets.PacketConnect;
import net.packets.PacketPlayerData;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class Server extends Thread {

    private DatagramSocket socket;
    private BordersiteServer game;

    private long threadTime;



    public Server(BordersiteServer game) {
        System.out.println(Arrays.toString(ByteUtil.intToBytes(594)));
        this.game = game;
        try {
            this.socket = new DatagramSocket(7777);
        } catch (SocketException e) {
            System.err.println("[ERROR] Socket Exception: " + e);
        }
        threadTime = System.currentTimeMillis();
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.err.println("[ERROR] Error receiving packet: " + e);
            }
            parsePacket(data, packet.getAddress(), packet.getPort());
        }
    }

    public void sendPlayerData() {
        if (threadTime + 20 < System.currentTimeMillis()) {
            for (Player p : game.players.values()) {
                PacketPlayerData ppd = new PacketPlayerData(p);
                ppd.writeData(this);
            }
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketType type = Packet.lookupPacket(message.substring(0, 2));

        switch (type) {
            case INVALID:
                System.err.println("[ERROR] Invalid packet!");
                break;
            case CONNECT:
                playerConnect(data, address, port);
                break;
            case DISCONNECT:
                playerDisconnect(data, address, port);
                break;
        }
    }

    private void playerConnect(byte[] data, InetAddress address, int port) {
        String username = new String(data).trim().substring(2);

        Player newPlayer = new Player(address, port, game.getNextPID());
        newPlayer.username = username;

        game.players.put(address, newPlayer);

        PacketConnect pc = new PacketConnect(newPlayer.playerId, address, port);
        pc.writeData(this);

        System.out.println("[INFO] " + username + " has connected to the server (" + address.getHostAddress() + ":" + port + ")");
    }

    private void playerDisconnect(byte[] data, InetAddress address, int port) {
        Player removed = game.players.get(address);
        game.players.remove(address);

        System.out.println("[INFO] " + removed.username + " has disconnected from the server (" + address.getHostAddress() + ":" + port + ")");
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            System.err.println("[ERROR] Error sending packet: " + e);
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (Player p : game.players.values()) {
            sendData(data, p.ipAddress, p.port);
        }
    }


}
