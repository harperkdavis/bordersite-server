package engine.math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;

public class ByteUtil {

    public static byte[] intToBytes(int a) {
        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(a).array();
    }

    public static int byteToInt(byte[] a) {
        if (a.length == 4) {
            return ByteBuffer.wrap(a).getInt();
        } else {
            System.err.println("ByteUtil: Invalid array size, returning 0.");
            return 0;
        }
    }

    public static boolean[] byteToBits(byte a) {
        BitSet bitSet = BitSet.valueOf(new byte[]{a});
        boolean[] bitArray = new boolean[8];
        for (int i = 0; i < 8; i++) {
            bitArray[i] = bitSet.get(i);
        }
        return bitArray;
    }

    public static byte bitsToByte(boolean[] bits) {
        if (bits.length == 8) {
            return (byte)((bits[0]?1<<7:0) + (bits[1]?1<<6:0) + (bits[2]?1<<5:0) +
                    (bits[3]?1<<4:0) + (bits[4]?1<<3:0) + (bits[5]?1<<2:0) +
                    (bits[6]?1<<1:0) + (bits[7]?1:0));
        } else {
            System.err.println("ByteUtil: Invalid array size, returning 0.");
            return 0;
        }
    }

}
