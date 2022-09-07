package AllocationMethods;

import FileSystemStructure.VirtualFile;
import SystemControl.DiskDataControl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

class Node{
    public int data;
    public int index;
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
    public boolean allocateFile(VirtualFile file, File f,String path) throws Exception {
        String TempFile="";
        if(getNumOfFreeBlocks()>0){
            ArrayList<Integer> freeSpaces= new ArrayList<>();
            for(int i=0;i<totalSize;i++){
                if(!blocksOfFiles.get(i).checkAllocate) freeSpaces.add(i);
            }
            Collections.shuffle(freeSpaces);

            if(freeSpaces.size()>= file.getFileSize()){
                int i;
                file.setStartBlock(freeSpaces.get(0));
                blocksOfFiles.get(freeSpaces.get(0)).data=1;
                blocksOfFiles.get(freeSpaces.get(0)).index=freeSpaces.get(0);
                blocksOfFiles.get(freeSpaces.get(0)).next=blocksOfFiles.get(freeSpaces.get(1));
                blocksOfFiles.get(freeSpaces.get(0)).checkAllocate=true;
                for(i=1;i< file.getFileSize()-1;i++){
                    blocksOfFiles.get(freeSpaces.get(i)).data=1;
                    blocksOfFiles.get(freeSpaces.get(i)).index=freeSpaces.get(i);
                    blocksOfFiles.get(freeSpaces.get(i)).next=blocksOfFiles.get(freeSpaces.get(i+1));
                    blocksOfFiles.get(freeSpaces.get(i)).checkAllocate=true;
                }
                blocksOfFiles.get(freeSpaces.get(i)).data=1;
                blocksOfFiles.get(freeSpaces.get(i)).index=freeSpaces.get(i);
                blocksOfFiles.get(freeSpaces.get(i)).next=null;
                blocksOfFiles.get(freeSpaces.get(i)).checkAllocate=true;
                file.setEndBlock(freeSpaces.get(i));
                TempFile+=(path + " " + String.valueOf(file.getStartBlock())+" "+(String.valueOf(file.getEndBlock()))+"\n");
                Node current=blocksOfFiles.get(file.getStartBlock());
                int index;
                while(current.next!=null){
                    Node Temp=current.next;
                    TempFile+=( String.valueOf(current.index)+" "+String.valueOf(Temp.index))+"\n";
                    current=current.next;
                }
                TempFile+=(String.valueOf(file.getEndBlock())) + " nil";
                DiskDataControl.FWrite(f,TempFile);
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    public boolean allocateFile(VirtualFile file, File f,String path,ArrayList<Integer> spaces) throws Exception {
        String TempFile="";
        if(getNumOfFreeBlocks()>0){
            ArrayList<Integer> freeSpaces= spaces;
            for(int i=0;i<totalSize;i++){
                if(!blocksOfFiles.get(i).checkAllocate) freeSpaces.add(i);
            }
            //Collections.shuffle(freeSpaces);

            if(freeSpaces.size()>= file.getFileSize()){
                int i;
                file.setStartBlock(freeSpaces.get(0));
                blocksOfFiles.get(freeSpaces.get(0)).data=1;
                blocksOfFiles.get(freeSpaces.get(0)).index=freeSpaces.get(0);
                blocksOfFiles.get(freeSpaces.get(0)).next=blocksOfFiles.get(freeSpaces.get(1));
                blocksOfFiles.get(freeSpaces.get(0)).checkAllocate=true;
                for(i=1;i< file.getFileSize()-1;i++){
                    blocksOfFiles.get(freeSpaces.get(i)).data=1;
                    blocksOfFiles.get(freeSpaces.get(i)).index=freeSpaces.get(i);
                    blocksOfFiles.get(freeSpaces.get(i)).next=blocksOfFiles.get(freeSpaces.get(i+1));
                    blocksOfFiles.get(freeSpaces.get(i)).checkAllocate=true;
                }
                blocksOfFiles.get(freeSpaces.get(i)).data=1;
                blocksOfFiles.get(freeSpaces.get(i)).index=freeSpaces.get(i);
                blocksOfFiles.get(freeSpaces.get(i)).next=null;
                blocksOfFiles.get(freeSpaces.get(i)).checkAllocate=true;
                file.setEndBlock(freeSpaces.get(i));
                TempFile+=(path + " " + String.valueOf(file.getStartBlock())+" "+(String.valueOf(file.getEndBlock()))+"\n");
                Node current=blocksOfFiles.get(file.getStartBlock());
                int index;
                while(current.next!=null){
                    Node Temp=current.next;
                    TempFile+=( String.valueOf(current.index)+" "+String.valueOf(Temp.index))+"\n";
                    current=current.next;
                }
                TempFile+=(String.valueOf(file.getEndBlock())) + " nil";
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
    public void deAllocateFile(VirtualFile file,File f,String path) throws Exception {
        Node current=blocksOfFiles.get(file.getStartBlock());
        Node current2=blocksOfFiles.get(file.getStartBlock());
        ArrayList<String> Data=DiskDataControl.FRead(f);
        String firstLine=path+" "+String.valueOf(file.getStartBlock())+" "+String.valueOf(file.getEndBlock());
        int startIdx=Data.indexOf(firstLine);
        int count=0;
        while(current2!=null){
            current2=current2.next;
            count++;
        }
        DiskDataControl.removeLines(f.getPath(),startIdx,count+2);
        int index;
        while(current!=null){
            Node Temp=current.next;
            index=blocksOfFiles.indexOf(current);
            blocksOfFiles.get(index).checkAllocate=false;
            blocksOfFiles.get(index).data=0;
            blocksOfFiles.get(index).next=null;
            current=Temp;
        }
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
