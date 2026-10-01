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

}