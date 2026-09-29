public class Utils {

    public static void writeInt(byte[] memory, int offset, int value) {
        memory[offset]     = (byte) (value >>> 24);
        memory[offset + 1] = (byte) (value >>> 16);
        memory[offset + 2] = (byte) (value >>> 8);
        memory[offset + 3] = (byte) value;
    }

    public static short readShort(byte[] memory, int offset) {
        return (short) (
            ((memory[offset] & 0xFF) << 8) |
            (memory[offset + 1] & 0xFF)
        );
    }

    public static void writeShort(byte[] memory, int offset, short value) {
        memory[offset]     = (byte) (value >>> 8);
        memory[offset + 1] = (byte) value;
    }

    public static void writeLong(byte[] memory, int offset, long value) {
        memory[offset]     = (byte) (value >>> 56);
        memory[offset + 1] = (byte) (value >>> 48);
        memory[offset + 2] = (byte) (value >>> 40);
        memory[offset + 3] = (byte) (value >>> 32);
        memory[offset + 4] = (byte) (value >>> 24);
        memory[offset + 5] = (byte) (value >>> 16);
        memory[offset + 6] = (byte) (value >>> 8);
        memory[offset + 7] = (byte) value;
    }

    public static long readLong(byte[] memory, int offset) {
        return ((long) (memory[offset] & 0xFF) << 56)
             | ((long) (memory[offset + 1] & 0xFF) << 48)
             | ((long) (memory[offset + 2] & 0xFF) << 40)
             | ((long) (memory[offset + 3] & 0xFF) << 32)
             | ((long) (memory[offset + 4] & 0xFF) << 24)
             | ((long) (memory[offset + 5] & 0xFF) << 16)
             | ((long) (memory[offset + 6] & 0xFF) << 8)
             | ((long) (memory[offset + 7] & 0xFF));
    }

   public static int writeString(
        byte[] memory,
        int offset,
        String str,
        int maxLength) {

        byte[] representationOctets = str.getBytes();

        int length = Math.min(representationOctets.length, maxLength);

        // Écrit la chaîne dans memory
        for (int i = 0; i < length; i++) {
            memory[offset + i] = representationOctets[i];
        }

        // Complète le reste avec des zéros
        for (int i = length; i < maxLength; i++) {
            memory[offset + i] = 0;
        }

        return length;
    }

   public static String readString(
        byte[] memory,
        int offset,
        int maxLength) {

        int length = 0;

        while (length < maxLength && memory[offset + length] != 0) {
            length++;
        }

        return new String(memory, offset, length);
}

}


    