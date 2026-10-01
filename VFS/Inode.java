public class Inode {

    private MemoryManager memoryManager;
    private int inodeNumber;

    public static final int INODE_SIZE = 128;
    public static final int DIRECT_POINTERS = 10;

    public Inode(
            MemoryManager memoryManager,
            int inodeNumber) {

        this.memoryManager = memoryManager;
        this.inodeNumber = inodeNumber;
    }

	int offset;
    public int getInodeOffset() {
		offset = this.memoryManager.INODE_TABLE_OFFSET + (inodeNumber * INODE_SIZE);
        return offset;
    }

    public int getFileType() {
        return memoryManager.getFilesystemMemory()[getInodeOffset() + 4];
    }

    public int getFileSize() {
		return memoryManager.getFilesystemMemory()[getInodeOffset() + 8];
    }

    public int[] getDirectPointers() {

		byte[] memory = memoryManager.getFilesystemMemory();

		int[] pointers = new int[DIRECT_POINTERS];

		int inodeOffset = getInodeOffset();

		for (int i = 0; i < DIRECT_POINTERS; i++) {
			pointers[i] = Utils.readInt(memory, offset + 28 + (i * 4));
		}

		return pointers;
	}

	
	public static void testStep6() {
		System.out.println("=== TEST ÉTAPE 6 : Adressage Inode ===");

		MemoryManager mm = new MemoryManager();

		Inode inode = new Inode(mm, 4);

		int expectedOffset =
				MemoryManager.INODE_TABLE_OFFSET
				+ (4 * Inode.INODE_SIZE);

		assert inode.getInodeOffset() == expectedOffset :
				"Offset d'inode incorrect";

		System.out.println("[OK] Étape 6 validée !");
	}
	
	public void writeToMemory(
        int fileType,
        int fileSize,
        long creationTime,
        long modificationTime,
        int[] directPointers,
        int indirectPointer,
        short permissions,
        int linkCount) {

		byte[] memory = memoryManager.getFilesystemMemory();

		int offset = getInodeOffset();

		Utils.writeInt(memory, offset, inodeNumber);
		Utils.writeInt(memory, offset + 4, fileType);
		Utils.writeInt(memory, offset + 8, fileSize);

		Utils.writeLong(memory, offset + 12, creationTime);
		Utils.writeLong(memory, offset + 20, modificationTime);

		for (int i = 0; i < directPointers.length; i++) {
			Utils.writeInt(
				memory,
				offset + 28 + (i * 4),
				directPointers[i]
			);
		}

		Utils.writeInt(memory, offset + 68, indirectPointer);
		Utils.writeShort(memory, offset + 72, permissions);
		Utils.writeInt(memory, offset + 74, linkCount);
	}

	
	public static void testStep7() {
		System.out.println("=== TEST ÉTAPE 7 : Sérialisation Inode ===");

		MemoryManager mm = new MemoryManager();

		Inode inode = new Inode(mm, 2);

		int[] ptrs = new int[] {
			150, 151, 0, 0, 0,
			0, 0, 0, 0, 0
		};

		long creation = 0x0102030405060708L;
		long modification = 0x1112131415161718L;

		inode.writeToMemory(
				1,
				1024,
				creation,
				modification,
				ptrs,
				777,
				(short) 0644,
				3);

		byte[] memory =
				mm.getFilesystemMemory();

		int offset = inode.getInodeOffset();

		assert (memory[offset] & 0xFF) == 0x00;
		assert (memory[offset + 3] & 0xFF) == 0x02;

		assert Utils.readInt(memory, offset + 4) == 1;
		assert Utils.readInt(memory, offset + 8) == 1024;

		assert (memory[offset + 12] & 0xFF) == 0x01;
		assert (memory[offset + 13] & 0xFF) == 0x02;
		assert (memory[offset + 14] & 0xFF) == 0x03;
		assert (memory[offset + 15] & 0xFF) == 0x04;
		assert (memory[offset + 16] & 0xFF) == 0x05;
		assert (memory[offset + 17] & 0xFF) == 0x06;
		assert (memory[offset + 18] & 0xFF) == 0x07;
		assert (memory[offset + 19] & 0xFF) == 0x08;

		assert Utils.readLong(
				memory,
				offset + 12) == creation;

		assert Utils.readLong(
				memory,
				offset + 20) == modification;

		assert Utils.readInt(
				memory,
				offset + 28) == 150;

		assert Utils.readInt(
				memory,
				offset + 32) == 151;

		assert Utils.readInt(
				memory,
				offset + 68) == 777;

		assert Utils.readShort(
				memory,
				offset + 72) == (short) 0644;

		assert Utils.readInt(
				memory,
				offset + 74) == 3;

		assert inode.getFileType() == 1;
		assert inode.getFileSize() == 1024;

		int[] result =
				inode.getDirectPointers();

		assert result[0] == 150;
		assert result[1] == 151;

		System.out.println("[OK] Étape 7 validée !");
	}
	
	public static void main(String[] args) {
		testStep6();
	}
}