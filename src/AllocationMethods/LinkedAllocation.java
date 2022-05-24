package AllocationMethods;

import FileSystemStructure.File;

import java.util.ArrayList;

class Node{
    public int data;
    public boolean checkAllocate;
    public Node next = null;
    public Node() {
        data=0;
        checkAllocate=false;
    }
}

public class LinkedAllocation implements FileAllocation{
    private static ArrayList<Node> blocksOfFiles;
    private int totalSize=100;

    public LinkedAllocation(int totalSize) {
        this.totalSize = totalSize;
        blocksOfFiles=new ArrayList<>();
        for(int i=0;i<totalSize;i++){
            blocksOfFiles.add(new Node());
        }

    }
    public LinkedAllocation() {
        blocksOfFiles=new ArrayList<>();
        for(int i=0;i<totalSize;i++){
            blocksOfFiles.add(new Node());
        }
    }
    @Override
    public boolean allocateFile(File file) {

        if(getNumOfFreeBlocks()>0){
            ArrayList<Integer> freeSpaces=new ArrayList<Integer>();
            for(int i=0;i<totalSize;i++){
                if(!blocksOfFiles.get(i).checkAllocate) freeSpaces.add(i);
            }
            if(freeSpaces.size()>= file.getFileSize()){
                int i;
                file.setStartBlock(freeSpaces.get(0));
                blocksOfFiles.get(freeSpaces.get(0)).data=1;
                blocksOfFiles.get(freeSpaces.get(0)).next=blocksOfFiles.get(freeSpaces.get(1));
                blocksOfFiles.get(freeSpaces.get(0)).checkAllocate=true;
                for(i=1;i< file.getFileSize()-1;i++){
                    blocksOfFiles.get(freeSpaces.get(i)).data=1;
                    blocksOfFiles.get(freeSpaces.get(i)).next=blocksOfFiles.get(freeSpaces.get(i+1));
                    blocksOfFiles.get(freeSpaces.get(i)).checkAllocate=true;
                }
                blocksOfFiles.get(freeSpaces.get(i)).data=1;
                blocksOfFiles.get(freeSpaces.get(i)).next=null;
                blocksOfFiles.get(freeSpaces.get(i)).checkAllocate=true;
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
        Node current=blocksOfFiles.get(file.getStartBlock());
        while(current.next!=null){
            Node Temp=current.next;
            current=new Node();
            current=Temp;
        }
        current=new Node();
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
            result+=(char)(blocksOfFiles.get(i).data + '0');
        }
        return result;
    }
}
