package game;

import engine.math.ByteUtil;
import engine.math.Vector3f;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Player {

    public InetAddress ipAddress;
    public int port;
    public String username;

    public Vector3f position;
    public Vector3f rotation;

    public boolean[] inputs;

    public int playerId;

    public Player(InetAddress ipAddress, int port, int pid) {
        this.ipAddress = ipAddress;
        this.port = port;
        position = new Vector3f((float) Math.random() * 10, (float) Math.random() * 10, (float) Math.random() * 10);
        rotation = new Vector3f((float) Math.random() * 10, (float) Math.random() * 10, (float) Math.random() * 10);
        playerId = pid;
        System.out.println(position.getX());
    }

    public void update() {

    }

    public byte[] toBytes() {
        byte[] playerBytes = new byte[1024];

        playerBytes[0] = (byte) 48; // 0
        playerBytes[1] = (byte) 50; // 2

        byte[] playerIdBytes = ByteUtil.intToBytes(playerId);

        playerBytes[2] = playerIdBytes[2];
        playerBytes[3] = playerIdBytes[3];

        byte[] xPosBytes = ByteUtil.intToBytes(Math.round(position.getX() * 1000));
        byte[] yPosBytes = ByteUtil.intToBytes(Math.round(position.getY() * 1000));
        byte[] zPosBytes = ByteUtil.intToBytes(Math.round(position.getZ() * 1000));

        byte[] xRotBytes = ByteUtil.intToBytes(Math.round(position.getX() * 1000));
        byte[] yRotBytes = ByteUtil.intToBytes(Math.round(position.getY() * 1000));
        byte[] zRotBytes = ByteUtil.intToBytes(Math.round(position.getZ() * 1000));

        for(int i = 0; i < 4; i++) {
            playerBytes[4 + i] = xPosBytes[i];
            playerBytes[8 + i] = yPosBytes[i];
            playerBytes[12 + i] = zPosBytes[i];
            playerBytes[16 + i] = xRotBytes[i];
            playerBytes[20 + i] = yRotBytes[i];
            playerBytes[24 + i] = zRotBytes[i];
        }

        return playerBytes;
    }

}
