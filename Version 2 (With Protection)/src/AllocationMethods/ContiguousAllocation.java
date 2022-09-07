package AllocationMethods;

import FileSystemStructure.VirtualFile;
import SystemControl.DiskDataControl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ContiguousAllocation implements FileAllocation{
    private static ArrayList<Integer> blocksOfFiles;
    private int totalSize=100;

    public ContiguousAllocation(int totalSize) {
        this.totalSize = totalSize;
        blocksOfFiles = new ArrayList<>();
        for (int i = 0; i < totalSize; i++) {
            blocksOfFiles.add(0);
        }
    }

    public ContiguousAllocation() {
        blocksOfFiles = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            blocksOfFiles.add(0);
        }
    }

    @Override
    public boolean allocateFile(VirtualFile file, File f,String path) throws Exception {
        int sizeOfFile =file.getFileSize();
        int freeBlocks=getNumOfFreeBlocks();
        String TempFile="";
        if(sizeOfFile>freeBlocks) return false;
        else{
            int checkSize=0;
            int start=blocksOfFiles.indexOf(0),end=0;
            for(int i=start;i<totalSize;i++){
                if(blocksOfFiles.get(i)==0){
                    start=i;
                    i++;
                    checkSize++;
                    for(int j=start+1;j<sizeOfFile+start;j++){
                        if(blocksOfFiles.get(j)==0) {
                            checkSize++;
                            i++;
                            end=j;
                        }
                        else{
                            checkSize=0;
                            break;
                        }
                    }
                }
                if(checkSize==sizeOfFile) {
                    file.setType("Contiguous");
                    file.setStartBlock(start);
                    for(int k=start;k<=end;k++) {
                        blocksOfFiles.set(k,1);
                    }
                    file.setEndBlock(end);
                    TempFile+=path+" "+(String.valueOf(file.getStartBlock())+" ")+(String.valueOf(file.getFileSize()));
                    DiskDataControl.FWrite(f,TempFile);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean allocateFile(VirtualFile file, File f, String path, ArrayList<Integer> spaces) throws Exception {
        return false;
    }

    @Override
    public void deAllocateFile(VirtualFile file,File f,String path) throws IOException {
        for(int i=file.getStartBlock();i<=file.getEndBlock();i++){
            blocksOfFiles.set(i,0);
        }
        DiskDataControl.removeLine(f,path);
    }

    public int getNumOfFreeBlocks(){
        int cnt=0;
        for(int i=0;i<totalSize;i++){
            if(blocksOfFiles.get(i)==0) cnt++;
        }
        return cnt;
    }

    public int getNumOfAllocatedBlocks(){
        int cnt=0;
        for(int i=0;i<totalSize;i++){
            if(blocksOfFiles.get(i)==1) cnt++;
        }
        return cnt;
    }
    public String getFreeAndAllocatedBlocks(){
        String result="";
        for(int i=0;i<totalSize;i++) {
            result+=(char)(blocksOfFiles.get(i) + '0');
        }
        return result;
    }


}
