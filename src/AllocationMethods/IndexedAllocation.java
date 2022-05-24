package AllocationMethods;

import FileSystemStructure.File;

import java.util.ArrayList;

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
    public boolean allocateFile(File file) {

        if (getNumOfFreeBlocks() > 0) {
            ArrayList<Integer> freeSpaces = new ArrayList<Integer>();
            for (int i = 0; i < totalSize; i++) {
                if (!blocksOfFiles.get(i).checkAllocate) freeSpaces.add(i);
            }

            if (freeSpaces.size() >= file.getFileSize()+1) {
                int startIndex=freeSpaces.get(0);
                int tempIndex=0;
                file.setStartBlock(startIndex);
                blocksOfFiles.get(startIndex).isRoot=true;
                blocksOfFiles.get(startIndex).checkAllocate=true;
                for(int i=1;i< file.getFileSize()+1;i++){
                    tempIndex=freeSpaces.get(i);
                    blocksOfFiles.get(startIndex).pointersToBlocks.add(tempIndex);
                    blocksOfFiles.get(tempIndex).checkAllocate=true;
                }
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void deAllocateFile(File file) {
        indexedNode rootNode=blocksOfFiles.get(file.getStartBlock());
        int lenOfList=rootNode.pointersToBlocks.size();
        int indexTemp=0;
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
