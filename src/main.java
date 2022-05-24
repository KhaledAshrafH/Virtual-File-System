import AllocationMethods.ContiguousAllocation;
import AllocationMethods.FileAllocation;
import AllocationMethods.IndexedAllocation;
import AllocationMethods.LinkedAllocation;
import FileSystemStructure.Directory;
import FileSystemStructure.File;
import SystemControl.CommandsConverter;

import java.util.Scanner;

public class main {
    private static final Scanner input = new Scanner(System.in);
    static Directory root ;
    public static void main(String[] args){
        int choiceAlloc=0;
//        CommandsConverter commandsConverter=new CommandsConverter("CreateFile root/file.txt");
//        System.out.println(commandsConverter.getFileName());
//        System.out.println(commandsConverter.getFilePath());
//        System.out.println(commandsConverter.getOperation());
//        System.out.println(commandsConverter.getSize());
        System.out.println("Enter Number of The Allocation Method:");
        System.out.println("1- Contiguous Allocation\n2- Linked Allocation\n3- Indexed Allocation");
        choiceAlloc=input.nextInt();
        switch (choiceAlloc){
            case 1:{
                root=new Directory("","root",new ContiguousAllocation(100));
                System.out.println("File System with Contiguous Allocation Method");
                System.out.println("---------------------------------------------");
                break;
            }
            case 2:{
                root=new Directory("","root",new LinkedAllocation(100));
                System.out.println("File System with Linked Allocation");
                System.out.println("----------------------------------");
                break;
            }
            case 3:{
                root=new Directory("","root",new IndexedAllocation(100));
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
//            System.out.println(numOfParts);
//            System.out.println(command);
//            System.out.println(operation);
            if(operation.equalsIgnoreCase("CreateFile") && numOfParts==3){
                root.CreateFile(commandsConverter.getPath(),root,commandsConverter.getSize());
            }
            else if(operation.equalsIgnoreCase("CreateFolder") && numOfParts==2){
                root.CreateFolder(commandsConverter.getPath(),root);
            }
            else if(operation.equalsIgnoreCase("DeleteFile") && numOfParts==2){
                root.deleteFile();
            }
            else if(operation.equalsIgnoreCase("DeleteFolder") && numOfParts==2){
                root.deleteFolder(commandsConverter.getPath(),root);
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
                if(exitCheck.equalsIgnoreCase("yes")) break;
            }


        }
//        File f=new File("Khaled.txt","root/khaled.txt",50);
//        File f1=new File("Khaled1","root/Khaloda/Khaled1.pdf",20);
//        File f2=new File("Khaled2","root/Khaled2.txt",20);
//        File f3=new File("Khaled3","root/Khaled3.txt",20);
//
//        Directory dir =new Directory("root/Khaloda","Khaloda",root.getAllocationType());
//        Directory dir2 =new Directory("root/Khaloda/Khaled","Khaled",root.getAllocationType());
//        //Directory dir3 =new Directory("root/Khaloda/koky","koky",root.getAllocationType());
//        //Directory dir6 =new Directory("root/Khaloda/koky","koky",root.getAllocationType());
//        //Directory dir4 =new Directory("root/neymar","neymar",root.getAllocationType());
//        //Directory dir5 =new Directory("root/Khaloda/Khaled/Ali","ahmed",root.getAllocationType());
//        //Directory dir =new Directory("root/Khaloda","Khaloda",root.getAllocationType());
//        root.CreateFolder(dir.getDirectoryPath(),root);
//        root.CreateFolder(dir2.getDirectoryPath(),root);
////        root.CreateFolder(dir3.getDirectoryPath(),root);
////        root.CreateFolder(dir4.getDirectoryPath(),root);
////        root.CreateFolder(dir5.getDirectoryPath(),root);
////        root.CreateFolder(dir6.getDirectoryPath(),root);
//        root.CreateFile(f.getFilePath(),root,f.getFileSize());
//        root.CreateFile(f1.getFilePath(),root,f1.getFileSize());
//        root.CreateFile(f2.getFilePath(),root,f2.getFileSize());
//        root.CreateFile(f3.getFilePath(),root,f3.getFileSize());
//        root.printDirectoryStructure(root,0);
//        root.deleteFolder("root/Khaloda/Khaled",root);
//        root.printDirectoryStructure(root,0);

    }
}
