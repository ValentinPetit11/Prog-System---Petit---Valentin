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
			pointers[i] = Utils.readInt(memory, inodeOffset + 28 + (i * 4));
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

		byte[] memory =
				memoryManager.getFilesystemMemory();

		int offset = getInodeOffset();

		// TODO:
		// 1. Numéro d'inode
		// 2. Type
		// 3. Taille
		// 4. Création
		// 5. Modification
		// 6. 10 pointeurs directs
		// 7. Pointeur indirect
		// 8. Permissions
		// 9. Nombre de liens
	}
	
	public static void main(String[] args) {
		testStep6();
	}
}