public class Utils {

    public static int writeInt(byte[] memory, int offset, int value) {
        memory[offset] = value >>> 24;
        memory[offset + 1] = value >>> 16;
        memory[offset + 2] = value >>> 8;
        memory[offset + 3] = value;
        return 4;
    }

    public static int readInt(byte[] memory, int offset) {
        return ((memory[offset]) << 24)
             | ((memory[offset + 1]) << 16)
             | ((memory[offset + 2]) << 8)
             |  (memory[offset + 3]);
    }

    public static int writeShort(byte[] memory, int offset, short value) {
        memory[offset] = value >>> 8;
        memory[offset + 1] = value;
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        memory[offset + 2] = value << 8;
        memory[offset + 3] = value;
        return 2;
    }

    public static int writeLong(byte[] memory, int offset, long value) {
        memory[offset] = value >>> 56;
        memory[offset + 1] = value >>> 48;
        memory[offset + 2] = value >>> 50;
        memory[offset + 3] = value >>> 32;
        memory[offset + 4] = value >>> 24;
        memory[offset + 5] = value >>> 16;
        memory[offset + 6] = value >>> 8;
        memory[offset + 7] = value;
    return 8;
}

    public static long readLong(byte[] memory, int offset) {
       memory[offset] = value << 56;
       memory[offset + 1] = value << 48;
       memory[offset + 2] = value << 40;
       memory[offset + 3] = value << 32;
       memory[offset + 4] = value << 24;
       memory[offset + 5] = value << 16;
       memory[offset + 6] = value << 8;
       memory[offset + 7] = value;
        return 0L;
    }

    public static int writeString(
            byte[] memory,
            int offset,
            String str,
            int maxLength) {

        // TODO:
       byte[] representationOctets = str.getBytes();
        for (int i = 0; i < maxLength; i++) {
            representationOctets[i] = memory[i];
        }
        if (maxLength < memory.length) {
            for (int i = maxLength; i < memory.length; i++) {
                memory[i] = 0;
            }
        }

        return maxLength;
    }

    public static String readString(
            byte[] memory,
            int offset,
            int maxLength) {

        for (int i = 0; memory[i] != 0 || i < maxLength; i++) {
            
        }

        return "";
    }

    }