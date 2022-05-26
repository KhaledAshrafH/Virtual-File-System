package AllocationMethods;

import FileSystemStructure.VirtualFile;

import java.io.File;
import java.util.ArrayList;

public interface FileAllocation {
    boolean allocateFile(VirtualFile file, File f,String path) throws Exception;
    boolean allocateFile(VirtualFile file, File f, String path, ArrayList<Integer> spaces) throws Exception;
    void deAllocateFile(VirtualFile file,File f,String path) throws Exception;
    int getNumOfFreeBlocks();
    int getNumOfAllocatedBlocks();
    String getFreeAndAllocatedBlocks();
}
