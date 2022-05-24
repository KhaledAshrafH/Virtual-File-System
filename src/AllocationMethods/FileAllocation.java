package AllocationMethods;

import FileSystemStructure.File;

public interface FileAllocation {
    boolean allocateFile(File file);
    void deAllocateFile(File file);
    public int getNumOfFreeBlocks();
    public int getNumOfAllocatedBlocks();
    public String getFreeAndAllocatedBlocks();
}
