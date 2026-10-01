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

        public boolean writeFile(
                int inodeNum,
                byte[] data) {

        int blocksNeeded = // nombre minimal de blocs nécessaires
                (data.length
                + MemoryManager.BLOCK_SIZE - 1)
                / MemoryManager.BLOCK_SIZE;

        if (blocksNeeded > Inode.DIRECT_POINTERS) {
                return false;
        }

        int[] blockPointers =
                new int[Inode.DIRECT_POINTERS];

        for (int i = 0; i < blocksNeeded; i++) {
                        blockPointers[i] = memoryManager.allocateBlock();
                }

        byte[] memory =
                memoryManager.getFilesystemMemory();

        int bytesRemaining =
                data.length;

        int dataSrcOffset = 0;
                
                int quantiteACopier;
                
                int numeroBlock;
                
                

        for (int i = 0; i < blocksNeeded; i++) {
                        quantiteACopier = Math.min(bytesRemaining,MemoryManager.BLOCK_SIZE);
                        numeroBlock = memoryManager.DATA_OFFSET + quantiteACopier;
                        
                        int blockOffset =
                        MemoryManager.DATA_OFFSET
                        + blockPointers[i] * MemoryManager.BLOCK_SIZE;

                System.arraycopy(
                        data,
                        dataSrcOffset,
                        memory,
                        blockOffset,
                        quantiteACopier);

                dataSrcOffset += quantiteACopier;
                bytesRemaining -= quantiteACopier;
                }
                
                Inode inode =
                                new Inode(memoryManager, inodeNum);

                int fileType =
                                inode.getFileType();

                long creationTime =
                        Utils.readLong(
                                 memory,
                                inode.getInodeOffset() + 12);

                long modificationTime =
                        Utils.readLong(
                                        memory,
                                        inode.getInodeOffset() + 20);

                int indirectPointer =
                        Utils.readInt(
                                memory,
                                inode.getInodeOffset() + 68);

                 short permissions =
                        Utils.readShort(
                                memory,
                                inode.getInodeOffset() + 72);

                int linkCount =
                        Utils.readInt(
                                memory,
                                inode.getInodeOffset() + 74);

                inode.writeToMemory(
                        fileType,
                        data.length,
                        creationTime,
                        modificationTime,
                        blockPointers,
                        indirectPointer,
                        permissions,
                        linkCount)
                ;

        return true;
        }
        public byte[] readFile(int inodeNum) {

                if (inodeNum < 0 ||
                        inodeNum >= MemoryManager.MAX_INODES) {
                        return null;
                }

                Inode inode =
                        new Inode(memoryManager, inodeNum);

                int fileSize =
                        inode.getFileSize();

                if (fileSize <= 0) {
                        return new byte[0];
                }

                int[] blockPointers =
                        inode.getDirectPointers();

                byte[] memory =
                        memoryManager.getFilesystemMemory();

                byte[] data =
                        new byte[fileSize];

                int bytesRemaining = fileSize;
                int dataDstOffset = 0;

                for (int i = 0;
                        i < Inode.DIRECT_POINTERS &&
                        bytesRemaining > 0;
                        i++) {

                        int blockNumber =
                                blockPointers[i];

                        if (blockNumber < 129) {
                        return null;
                        }

                        int bytesToCopy =
                                Math.min(
                                        bytesRemaining,
                                        MemoryManager.BLOCK_SIZE);

                        int blockOffset =
                                MemoryManager.DATA_OFFSET
                                + blockNumber * MemoryManager.BLOCK_SIZE;

                        System.arraycopy(
                                memory,
                                blockOffset,
                                data,
                                dataDstOffset,
                                bytesToCopy);

                        dataDstOffset += bytesToCopy;
                        bytesRemaining -= bytesToCopy;
                }

                if (bytesRemaining != 0) {
                        return null;
                }

                return data;
        }


        public boolean deleteFile(int inodeNum) {
                if (inodeNum < 0 ||
                        inodeNum >= MemoryManager.MAX_INODES) {
                        return false;
                }

                Inode inode =
                        new Inode(memoryManager, inodeNum);

                int[] blockPointers =
                        inode.getDirectPointers();

                for (int i = 0;
                        i < Inode.DIRECT_POINTERS;
                        i++) {

                        int blockNumber =
                                blockPointers[i];

                       
                        if (blockNumber > 0) {
                        memoryManager.setBlockUsed(
                                blockNumber,
                                false);
                        }
                }

               
                byte[] memory =
                        memoryManager.getFilesystemMemory();

                int inodeOffset =
                        inode.getInodeOffset();

                for (int i = 0;
                        i < Inode.INODE_SIZE;
                        i++) {

                        memory[inodeOffset + i] = 0;
                }

                return true;
        }

        
}


