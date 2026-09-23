public class Utils {

    public static int writeInt(byte[] memory, int offset, int value) {
        memory[offset]     = (byte) (value >>> 24);
        memory[offset + 1] = (byte) (value >>> 16);
        memory[offset + 2] = (byte) (value >>> 8);
        memory[offset + 3] = (byte) (value);
        return 4;
    }

    public static int readInt(byte[] memory, int offset) {
        return ((memory[offset]) << 24)
             | ((memory[offset + 1]) << 16)
             | ((memory[offset + 2]) << 8)
             |  (memory[offset + 3]);
    }

    public static int writeShort(byte[] memory, int offset, short value) {
        memory[offset] = (byte) (value >>> 8);
        memory[offset + 1] = (byte) (value);
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        
    }
}