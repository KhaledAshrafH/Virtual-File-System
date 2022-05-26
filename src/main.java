import AllocationMethods.ContiguousAllocation;
import AllocationMethods.IndexedAllocation;
import AllocationMethods.LinkedAllocation;
import FileSystemStructure.Directory;
import SystemControl.CommandsConverter;
import SystemControl.DiskDataControl;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
    private static final Scanner input = new Scanner(System.in);

    static Directory root ;
    public static void main(String[] args) throws Exception {
        int choiceAlloc=0;
        File f;
        ArrayList<String> lines;

        System.out.println("Enter Number of The Allocation Method:");
        System.out.println("1- Contiguous Allocation\n2- Linked Allocation\n3- Indexed Allocation");

        choiceAlloc=input.nextInt();
        switch (choiceAlloc){
            case 1:{
                f=new File("C:\\Users\\Eng Khaled\\VFS\\DataBase\\ContiguousAllocation.vfs");
                lines= DiskDataControl.FRead(f);
                root=new Directory("","root",new ContiguousAllocation(100));
                if(lines.size()>0){
                    String command,operation;
                    int sizeOfFile=0,startBlock;
                    if(f.exists()) f.delete();
                    f.createNewFile();
                    for(int i=0;i< lines.size();i++){
                        String str=lines.get(i);
                        String [] splittedLine = str.split(" ");
                        str=splittedLine[0];
                        if(splittedLine.length>1) sizeOfFile=Integer.parseInt(splittedLine[2]);
                        if(str.contains(".")){
                            root.CreateFile(str,root,sizeOfFile,f,false);
                        }
                        else{
                            root.CreateFolder(str,root,f,false);
                        }

                    }
                }
                System.out.println("File System with Contiguous Allocation Method");
                System.out.println("---------------------------------------------");
                break;
            }
            case 2:{
                f=new File("C:\\Users\\Eng Khaled\\VFS\\DataBase\\LinkedAllocation.vfs");
                lines= DiskDataControl.FRead(f);
                root=new Directory("","root",new LinkedAllocation(100));
                if(lines.size()>0){
                    String command,operation;
                    int sizeOfFile=0,startBlock,endBlock,tempBlock;
//                    System.out.println(lines);
                    if(f.exists()) f.delete();
                    f.createNewFile();

                    for(int i=0;i< lines.size();i++){
                        ArrayList<Integer> spaces=new ArrayList<Integer>();
                        String str=lines.get(i);
                        String [] splittedLine = str.split(" ");
                        str=splittedLine[0];
                        if(str.contains(".") && str.contains("root")){
                            startBlock=Integer.parseInt(splittedLine[1]);
                            endBlock=Integer.parseInt(splittedLine[2]);
                            spaces.add(startBlock);
                            i++;
                            sizeOfFile++;
                            String str2=lines.get(i);
                            String [] splittedNums= str2.split(" ");
                            tempBlock=Integer.parseInt(splittedLine[1]);
                            while(true){
                                if(i>1){
                                    str2=lines.get(i);
                                    String [] splittedNums2= str2.split(" ");
                                    tempBlock=Integer.parseInt(splittedNums2[1]);
                                }
                                if(tempBlock==endBlock) break;
                                spaces.add(tempBlock);
                                sizeOfFile++;
                                i++;
                            }
                            spaces.add(endBlock);
                            sizeOfFile++;
                            i++;
                            root.CreateFile(str,root,sizeOfFile,f,false,spaces);
                            sizeOfFile=0;
                        }

                        else if(str.contains("root") && !str.contains(".")){
                            root.CreateFolder(str,root,f,false);
                        }

                    }
                }
                System.out.println("File System with Linked Allocation");
                System.out.println("----------------------------------");
                break;
            }
            case 3:{
                f=new File("C:\\Users\\Eng Khaled\\VFS\\DataBase\\IndexedAllocation.vfs");
                lines= DiskDataControl.FRead(f);
                root=new Directory("","root",new IndexedAllocation(100));
                if(lines.size()>0){
                    String command,operation;
                    int sizeOfFile=0,startBlock,endBlock,tempBlock;
                    if(f.exists()) f.delete();
                    f.createNewFile();

                    for(int i=0;i< lines.size();i++){
                        ArrayList<Integer> spaces=new ArrayList<Integer>();
                        String str=lines.get(i);
                        String [] splittedLine = str.split(" ");
                        str=splittedLine[0];
                        if(str.contains(".") && str.contains("root")){
                            startBlock=Integer.parseInt(splittedLine[1]);
                            spaces.add(startBlock);
                            i++;
                            String str2=lines.get(i);
                            String [] splittedNums= str2.split(" ");
                            for (String splittedNum : splittedNums) {
                                tempBlock = Integer.parseInt(splittedNum);
                                spaces.add(tempBlock);
                            }
                            sizeOfFile=spaces.size();
                            root.CreateFile(str,root,sizeOfFile,f,false,spaces);
                        }

                        else if(str.contains("root") && !str.contains(".")){
                            root.CreateFolder(str,root,f,false);
                        }

                    }
                }
                System.out.println("File System with Indexed Allocation Method");
                System.out.println("------------------------------------------");
                break;
            }
            default:{
                return;
            }
        }

        int temp=0;
        while(true){
            System.out.print(">");
            String command,operation;
            int numOfParts;
            if(temp==0) {
                temp++;
                input.nextLine();
            }
            command= input.nextLine();
            CommandsConverter commandsConverter=new CommandsConverter(command);
            numOfParts=commandsConverter.getNumOfParts();
            operation =commandsConverter.getOperation();
            if(operation.equalsIgnoreCase("CreateFile") && numOfParts==3){
                root.CreateFile(commandsConverter.getPath(),root,commandsConverter.getSize(),f,true);
            }
            else if(operation.equalsIgnoreCase("CreateFolder") && numOfParts==2){
                root.CreateFolder(commandsConverter.getPath(),root,f,true);
            }
            else if(operation.equalsIgnoreCase("DeleteFile") && numOfParts==2){
                root.deleteFile(commandsConverter.getPath(),root,f);
            }
            else if(operation.equalsIgnoreCase("DeleteFolder") && numOfParts==2){
                root.deleteFolder(commandsConverter.getPath(),root,f);
            }
            else if(operation.equalsIgnoreCase("DisplayDiskStatus") && numOfParts==1){
                root.displayDiskStatus();
            }
            else if(operation.equalsIgnoreCase("DisplayDiskStructure") && numOfParts==1){
                root.printDirectoryStructure(root,0);
            }
            else if(operation.equalsIgnoreCase("exit")){
                String exitCheck="no";
                System.out.println(">You want to close the program?(yes/no)");
                exitCheck=input.next();
                if(exitCheck.equalsIgnoreCase("yes")) {
                    System.out.println("Program Terminated Successfully");
                    break;
                }
            }
            else if(operation.equalsIgnoreCase("help")){
                System.out.println(">CreateFile PathOfFile SizeOfFile");
                System.out.println(" CreateFolder PathOfFolder");
                System.out.println(" DeleteFile PathOfFile");
                System.out.println(" DeleteFolder PathOfFolder");
                System.out.println(" DisplayDiskStructure");
                System.out.println(" DisplayDiskStatus");
            }
            else {
                if(operation.equalsIgnoreCase("CreateFile") && numOfParts==2){
                    System.out.println(">Please, Enter Size of the File!");
                }
                else System.out.println(">You Enter command incorrect, you can use command 'help'");
            }

        }

    }
}
