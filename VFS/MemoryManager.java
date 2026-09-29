import java.io.*;

/**
 * Simulation d'un disque dur 
 * @author V.Petit
 * @version 1.0
 */
public class MemoryManager {

    public static final int BLOCK_SIZE = 512; // Taille d'un bloc
    public static final int TOTAL_MEMORY = 1024 * 1024; // Taille de la mémoire 
    public static final int NUM_BLOCKS =
            TOTAL_MEMORY / BLOCK_SIZE; // Nombre de blocs

    public static final int SUPERBLOCK_OFFSET = 0; // offset de départ du super bloc
    public static final int BITMAP_OFFSET = BLOCK_SIZE; // offset de départ du bitmap
    public static final int INODE_TABLE_OFFSET =
            2 * BLOCK_SIZE; // offset de départ de l'inode
    public static final int DATA_OFFSET =
            129 * BLOCK_SIZE; // offset des données

    public static final int INODE_SIZE = 128; // taille en octets de l'inode 

    public static final int INODE_TABLE_SIZE =
            DATA_OFFSET - INODE_TABLE_OFFSET; 

    public static final int MAX_INODES =
            INODE_TABLE_SIZE / INODE_SIZE;

    private byte[] memory; // tableau java représentant la mémoire du disque dur

    /**
     * Constructeur sans paramètres qui initialise la mémoire
     */
    public MemoryManager() {
        this.memory = new byte[TOTAL_MEMORY];
        initializeFilesystem();
    }

    /**
     * Réinitialisation de la mémoire
     */
    private void initializeFilesystem() {
        writeSuperblock();

        // Pour chaque bloc qui n'est pas un bloc de données, le marquer comme utilisé
        for (int i = 0; i < 129; i++) {
            setBlockUsed(i, true);
        }
    }

    private void writeSuperblock() {
        // TODO:
        // Utiliser Utils pour écrire les métadonnées.

        Utils.writeString(
                memory,
                SUPERBLOCK_OFFSET,
                "MYFS1.0",
                16);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 16,
                BLOCK_SIZE);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 20,
                TOTAL_MEMORY);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 24,
                NUM_BLOCKS);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 28,
                MAX_INODES);
    }

    /** 
     * @return memory
     */
    public byte[] getFilesystemMemory() {
        return memory;
    }

    /**
     * Modifie la disponibilité du block
     * @param blockNumber le numéro du bloc
     * @param used boolean indiquant true si utilisé, false sinon
     */
    public boolean setBlockUsed(
        int blockNumber,
        boolean used) {

    if (blockNumber < 0 ||
        blockNumber >= NUM_BLOCKS) {
        return false;
    }

    int byteIndex = blockNumber / 8; // il y a 8 bytes donc pour trouver le byteIndex on divise le numéro du bloc par 8
    int bitPosition = blockNumber % 8; 
    int offset = BITMAP_OFFSET + byteIndex; // On part de l'offset du bitmap et on ajoute l'index de la valeur qu'on veut modifier

    if (used) {
        memory[BITMAP_OFFSET + byteIndex] = (byte) ((memory[BITMAP_OFFSET + byteIndex] & 0xFF) | 1 << bitPosition); // si utilisé on met le bitmap à 1 (utilisé)
    } else {
         memory[offset] = (byte) (memory[offset] & 0xFF) & ~(1 << bitPosition);// sinon on  le met à 0 (non utilisé)
    }

    return true;
}

    public int isBlockUsed(int blockNumber) {

        if (blockNumber < 0 ||
            blockNumber >= NUM_BLOCKS) {
            return -1;
        }

        int byteIndex = blockNumber / 8;
        int bitPosition = blockNumber % 8;
        int offset = BITMAP_OFFSET + byteIndex;
        if ((memory[offset] & 1 << bitPosition) != 0) {
            return 1;
        }else{
            return 0;
        }
    }

    public int allocateBlock() {

        for (int i = 129; i < NUM_BLOCKS; i++) {
            if (isBlockUsed(i) == 0) {
                setBlockUsed(i, true);
                return i;
            }
        }

        return -1;
    }
}