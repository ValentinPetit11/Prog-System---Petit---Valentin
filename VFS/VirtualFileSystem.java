import java.util.*;

public class VirtualFileSystem {

    private MemoryManager memoryManager;

    public VirtualFileSystem() {
        this.memoryManager = new MemoryManager();
    }

    private int allocateInode() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        for (int i = 0; i < this.memoryManager.MAX_INODES; i++) {
            Inode inode = new Inode(memoryManager, i);

            if (inode.getFileType() == 0) {
                return i;
            }
        }

        return -1;
    }

    public boolean createFile(
            String directory,
            String filename) {

        int inodeNum = allocateInode();

        if (inodeNum == -1) {
            return false;
        }

      Inode inode = new Inode(memoryManager, inodeNum);

        int[] directPointers = new int[Inode.DIRECT_POINTERS];

        long currentTime = System.currentTimeMillis();

        inode.writeToMemory(
                1,              
                0,              
                currentTime,    
                currentTime,    
                directPointers,
                0,              
                (short) 0644,  
                1               
        );

        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }
	
	public static void testStep8() {
		System.out.println("=== TEST ÉTAPE 8 : Création Fichier ===");

		VirtualFileSystem vfs =
				new VirtualFileSystem();

		boolean ok1 =
				vfs.createFile("/", "fichier1.txt");

		boolean ok2 =
				vfs.createFile("/", "fichier2.txt");

		assert ok1 :
				"La création du premier fichier a échoué";

		assert ok2 :
				"La création du second fichier a échoué";

		MemoryManager mm =
				vfs.getMemoryManager();

		Inode inode0 =
				new Inode(mm, 0);

		Inode inode1 =
				new Inode(mm, 1);

		assert inode0.getFileType() == 1 :
				"L'inode 0 doit représenter un fichier";

		assert inode1.getFileType() == 1 :
				"L'inode 1 doit représenter un fichier";

		assert inode0.getFileSize() == 0 :
				"Le premier fichier doit être vide";

		assert inode1.getFileSize() == 0 :
				"Le second fichier doit être vide";

		System.out.println("[OK] Étape 8 validée !");
	}

   public boolean writeFile(
        int inodeNum,
        byte[] data) {

        int blocksNeeded =
                (data.length
                + MemoryManager.BLOCK_SIZE - 1)
                / MemoryManager.BLOCK_SIZE;

        if (blocksNeeded > Inode.DIRECT_POINTERS) {
                return false;
        }

        int[] blockPointers =
                new int[Inode.DIRECT_POINTERS];

        for (int i = 0; i < blocksNeeded; i++) {
                int blockNumber = memoryManager.allocateBlock();

                if (blockNumber == -1) {
                for (int j = 0; j < i; j++) {
                        memoryManager.setBlockUsed(
                                blockPointers[j],
                                false
                        );
                }

                return false;
                }

                blockPointers[i] = blockNumber;
        }

        byte[] memory = memoryManager.getFilesystemMemory();

        int bytesRemaining = data.length;

        int dataSrcOffset = 0;

        for (int i = 0; i < blocksNeeded; i++) {

                int blockNumber = blockPointers[i];

                int blockOffset = blockNumber * MemoryManager.BLOCK_SIZE;

                int bytesACopier =
                        Math.min(
                                bytesRemaining,
                                MemoryManager.BLOCK_SIZE
                        );

                for (int j = 0; j < bytesACopier; j++) {
                memory[blockOffset + j] =
                        data[dataSrcOffset + j];
                }

                dataSrcOffset += bytesACopier;
                bytesRemaining -= bytesACopier;
        }

        
        Inode inode = new Inode(memoryManager, inodeNum);

        int fileSize = data.length;

        int[] oldPointers = inode.getDirectPointers();

        long currentTime = System.currentTimeMillis();

        inode.writeToMemory(
                inode.getFileType(),
                fileSize,
                currentTime,
                currentTime,
                blockPointers,
                0,
                (short) 0644,
                1
        );

        return true;
        }
        public byte[] readFile(int inodeNum) {

                Inode inode =
                        new Inode(memoryManager, inodeNum);

                int fileSize = inode.getFileSize();

                if (fileSize == 0) {
                        return new byte[0];
                }

                byte[] fileData = new byte[fileSize];

                byte[] memory = memoryManager.getFilesystemMemory();

                int[] blockPointers = inode.getDirectPointers();

                int bytesRemaining = fileSize;

                int fileDataOffset = 0;

                for (int i = 0; i < Inode.DIRECT_POINTERS && bytesRemaining > 0; i++) {

                        int blockNumber = blockPointers[i];

                        if (blockNumber == 0) {
                                break;
                        }

                        int blockOffset = blockNumber * MemoryManager.BLOCK_SIZE;

                        int bytesACopier = Math.min(bytesRemaining, MemoryManager.BLOCK_SIZE);

                        for (int j = 0; j < bytesToCopy; j++) {
                                fileData[fileDataOffset + j] = memory[blockOffset + j];
                        }

                        fileDataOffset += bytesACopier;
                        bytesRemaining -= bytesACopier;
                }

                return fileData;
        }
}


