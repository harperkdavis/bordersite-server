package net.packets;

import net.Server;

public abstract class Packet {

    public static enum PacketType {
        INVALID(-1), CONNECT(0), DISCONNECT(1), PLAYERDATA(2), INPUTS(3), PLAYERSPAWN(4), PLAYERDELETE(5);

        private int packetId;

        private PacketType(int packetId) {
            this.packetId = packetId;
        }

        public int getId() {
            return packetId;
        }
    }

    public byte packetId;

    public Packet(int packetId) {
        this.packetId = (byte) packetId;
    }

    public abstract byte[] getData();

    public abstract void writeData(Server server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);
    }

    public static PacketType lookupPacket(String packetId) {
        try {
            return lookupPacket(Integer.parseInt(packetId));
        } catch (NumberFormatException e) {
            return PacketType.INVALID;
        }
    }

    public static PacketType lookupPacket(int id) {
        for (PacketType p : PacketType.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return PacketType.INVALID;
    }

}
