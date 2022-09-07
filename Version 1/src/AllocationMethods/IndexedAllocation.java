package AllocationMethods;

import FileSystemStructure.VirtualFile;
import SystemControl.DiskDataControl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

class indexedNode{
    public boolean checkAllocate;
    public boolean isRoot;
    public ArrayList<Integer> pointersToBlocks;
    indexedNode(){
        checkAllocate=false;
        isRoot=false;
        pointersToBlocks=new ArrayList<>();
    }
}
public class IndexedAllocation implements FileAllocation{
    private static ArrayList<indexedNode> blocksOfFiles;
    private int totalSize=100;

    public IndexedAllocation(int totalSize) {
        this.totalSize = totalSize;
        blocksOfFiles=new ArrayList<>();
        for(int i=0;i<totalSize;i++){
            blocksOfFiles.add(new indexedNode());
        }
    }
    public IndexedAllocation() {
        blocksOfFiles=new ArrayList<>();
        for(int i=0;i<totalSize;i++){
            blocksOfFiles.add(new indexedNode());
        }
    }

    @Override
    public boolean allocateFile(VirtualFile file, File f,String path) throws Exception {
        String TempFile=(path+" ");
        if (getNumOfFreeBlocks() > 0) {
            ArrayList<Integer> freeSpaces = new ArrayList<>();
            for (int i = 0; i < totalSize; i++) {
                if (!blocksOfFiles.get(i).checkAllocate) freeSpaces.add(i);
            }
            Collections.shuffle(freeSpaces);
            if (freeSpaces.size() >= file.getFileSize()+1) {
                int startIndex=freeSpaces.get(0);
                int tempIndex;
                file.setStartBlock(startIndex);
                blocksOfFiles.get(startIndex).isRoot=true;
                blocksOfFiles.get(startIndex).checkAllocate=true;
                for(int i=1;i< file.getFileSize()+1;i++){
                    tempIndex=freeSpaces.get(i);
                    blocksOfFiles.get(startIndex).pointersToBlocks.add(tempIndex);
                    blocksOfFiles.get(tempIndex).checkAllocate=true;
                }
                int num,len=blocksOfFiles.get(startIndex).pointersToBlocks.size();
                TempFile+=String.valueOf(startIndex)+"\n";
                for(int i=0;i<len;i++){
                    num=blocksOfFiles.get(startIndex).pointersToBlocks.get(i);
                    if(i!=len-1) TempFile+=(String.valueOf(num) + " ");
                    else TempFile+=(String.valueOf(num));
                }
                DiskDataControl.FWrite(f,TempFile);
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean allocateFile(VirtualFile file, File f, String path, ArrayList<Integer> spaces) throws Exception {
        String TempFile=(path+" ");
        ArrayList<Integer> freeSpaces = spaces;
        int startIndex=freeSpaces.get(0);
        int tempIndex;
        file.setStartBlock(startIndex);
        blocksOfFiles.get(startIndex).isRoot=true;
        blocksOfFiles.get(startIndex).checkAllocate=true;
        TempFile+=String.valueOf(startIndex)+"\n";
        int len=spaces.size();
        for(int i=1;i< len;i++){
            tempIndex=freeSpaces.get(i);
            blocksOfFiles.get(startIndex).pointersToBlocks.add(tempIndex);
            blocksOfFiles.get(tempIndex).checkAllocate=true;
            if(i!=len-1) TempFile+=(String.valueOf(tempIndex) + " ");
            else TempFile+=(String.valueOf(tempIndex));
        }
        DiskDataControl.FWrite(f,TempFile);
        return true;
    }

    @Override
    public void deAllocateFile(VirtualFile file,File f,String path) throws IOException {
        indexedNode rootNode=blocksOfFiles.get(file.getStartBlock());
        int secondBlock=rootNode.pointersToBlocks.get(0);
        int thirdBlock=rootNode.pointersToBlocks.get(1);
        String str="";
        str=(String.valueOf(secondBlock) + " " + String.valueOf(thirdBlock));
        DiskDataControl.removeLine(f,path);
        DiskDataControl.removeLine(f,str);
        int lenOfList=rootNode.pointersToBlocks.size();
        int indexTemp;
        for(int i=0;i<lenOfList;i++){
            indexTemp=rootNode.pointersToBlocks.get(i);
            blocksOfFiles.get(indexTemp).checkAllocate=false;
        }
        rootNode.pointersToBlocks.removeAll(rootNode.pointersToBlocks);
        rootNode.isRoot=false;
        rootNode.checkAllocate=false;
    }

    @Override
    public int getNumOfAllocatedBlocks() {
        int cnt=0;
        for(int i=0;i<totalSize;i++){
            if(blocksOfFiles.get(i).checkAllocate) cnt++;
        }
        return cnt;
    }

    @Override
    public int getNumOfFreeBlocks() {
        int cnt=0;
        for(int i=0;i<totalSize;i++){
            if(!blocksOfFiles.get(i).checkAllocate) cnt++;
        }
        return cnt;
    }

    @Override
    public String getFreeAndAllocatedBlocks() {
        String result="";
        for(int i=0;i<totalSize;i++) {
            if(blocksOfFiles.get(i).checkAllocate) result+=('1');
            else result+=('0');
        }
        return result;
    }
}
