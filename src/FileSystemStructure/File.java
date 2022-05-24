package FileSystemStructure;

import java.util.ArrayList;

public class File {
    private String fileName;
    private String filePath;
    private String type;
    private ArrayList<Integer> allocatedBlocks;
    private boolean deleted=false;
    private int fileSize;
    private int startBlock;

    public File(String name,String path, int size) {
        this.fileName = name;
        this.filePath =path;
        this.fileSize = size;
        allocatedBlocks=new ArrayList<>();
    }

    public File(String fileName, String filePath, String type, ArrayList<Integer> allocatedBlocks, boolean deleted, int fileSize) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.type = type;
        this.allocatedBlocks = allocatedBlocks;
        this.deleted = deleted;
        this.fileSize = fileSize;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAllocatedBlocks(ArrayList<Integer> allocatedBlocks) {
        this.allocatedBlocks = allocatedBlocks;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Integer> getAllocatedBlocks() {
        return allocatedBlocks;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getStartBlock() {
        return startBlock;
    }

    public void setStartBlock(int startBlock) {
        this.startBlock = startBlock;
    }
}
