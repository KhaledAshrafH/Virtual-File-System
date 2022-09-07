import AllocationMethods.ContiguousAllocation;
import AllocationMethods.IndexedAllocation;
import AllocationMethods.LinkedAllocation;
import FileSystemStructure.Directory;
import SystemControl.CommandsConverter;
import SystemControl.DiskDataControl;
import SystemControl.Protection;
import SystemControl.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class main {
    private static final Scanner input = new Scanner(System.in);

    static Directory root ;
    public static void main(String[] args) throws Exception {
        int choiceAlloc=0;
        File f;
        ArrayList<String> lines;
        User admin=new User("admin","admin");
        Protection protection=new Protection(admin);
        protection.getUsers().add(admin);
        User currentUser=admin;
        //protection get users data from file
        f=new File("C:\\Users\\Eng Khaled\\VFS_V2\\DataBase\\user.txt");
        lines =DiskDataControl.FRead(f);
        if(lines.size()>0){
            for(int i=1;i< lines.size();i++){
                String []splitStr=lines.get(i).split(" , ");
                User user=new User(splitStr[0],splitStr[1]);
                protection.getUsers().add(user);
            }
        }
        //protection get capabilities data from file
        f=new File("C:\\Users\\Eng Khaled\\VFS_V2\\DataBase\\capabilities.txt");
        lines =DiskDataControl.FRead(f);
        if(lines.size()>0){
            for(int i=0;i< lines.size();i++){
                String str=lines.get(i);
                String []splitStr=str.split(",");
                //System.out.println(lines.get(i));
                //System.out.println(Arrays.toString(splitStr));
                String path=splitStr[0];
                for(int j=1;j<splitStr.length;j++){
                    if(j%2==1) {
                        int index=0;
                        for(int k=0;k<protection.getUsers().size();k++){
                            if(protection.getUsers().get(k).getUsername().equals(splitStr[j])){
                                index=k;
                            }
                        }
                        protection.getUsers().get(index).getCapabilities().put(path,splitStr[j+1]);
                        //System.out.println(path+" "+splitStr[j+1]+" "+protection.getUsers().get(index).getUsername());
                    }
                }
            }
        }
//        for(User user : protection.getUsers()){
//            System.out.println("User: "+ user.getUsername() + " "+user.getPassword());
//            for (String str: user.getCapabilities().keySet()) {
//                String key = str.toString();
//                String value = user.getCapabilities().get(str).toString();
//                System.out.println(key + " " + value);
//            }
//        }

        System.out.println("Enter Number of The Allocation Method:");
        System.out.println("1- Contiguous Allocation\n2- Linked Allocation\n3- Indexed Allocation");

        choiceAlloc=input.nextInt();
        switch (choiceAlloc){
            case 1:{
                f=new File("C:\\Users\\Eng Khaled\\VFS_V2\\DataBase\\ContiguousAllocation.vfs");
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
                f=new File("C:\\Users\\Eng Khaled\\VFS_V2\\DataBase\\LinkedAllocation.vfs");
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
                f=new File("C:\\Users\\Eng Khaled\\VFS_V2\\DataBase\\IndexedAllocation.vfs");
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
            String []str=command.split(" ");
            if(operation.equalsIgnoreCase("CreateFile") && numOfParts==3){
                if(currentUser==admin){
                    root.CreateFile(commandsConverter.getPath(),root,commandsConverter.getSize(),f,true);
                }
                else if(currentUser.getCapabilities().containsKey(commandsConverter.getPathDir())){
                    String access=currentUser.getCapabilities().get(commandsConverter.getPathDir());
                    if(access.equals("10") || access.equals("11")) root.CreateFile(commandsConverter.getPath(),root,commandsConverter.getSize(),f,true);
                    else System.out.println("You don't allow do this");
                }
                else System.out.println("You don't allow do this");
            }
            else if(operation.equalsIgnoreCase("CreateFolder") && numOfParts==2){
                if(currentUser==admin){
                    root.CreateFolder(commandsConverter.getPath(),root,f,true);
                }
                else if(currentUser.getCapabilities().containsKey(commandsConverter.getPathDir())){
                    String access=currentUser.getCapabilities().get(commandsConverter.getPathDir());
                    if(access.equals("10") || access.equals("11")) root.CreateFolder(commandsConverter.getPath(),root,f,true);
                    else System.out.println("You don't allow do this");
                }
                else {
//                    boolean check =false;
//                    String k="";
//                    for ( String key : currentUser.getCapabilities().keySet() ) {
//                        if(commandsConverter.getPathDir().contains(key)) {
//                            check =true;
//                            k=key;
//                            break;
//                        }
//                    }
//                    if(check){
//                        String access=currentUser.getCapabilities().get(k);
//                        if(access.equals("10") || access.equals("11")) root.CreateFolder(commandsConverter.getPath(),root,f,true);
//                        else System.out.println("You don't allow do this");
//                    }
                    System.out.println("You don't allow do this");
                }
            }
            else if(operation.equalsIgnoreCase("DeleteFile") && numOfParts==2){
                if(currentUser==admin){
                    root.deleteFile(commandsConverter.getPath(),root,f);
                }
                else if(currentUser.getCapabilities().containsKey(commandsConverter.getPathDir())){
                    String access=currentUser.getCapabilities().get(commandsConverter.getPathDir());
                    if(access.equals("01") || access.equals("11")) root.deleteFile(commandsConverter.getPath(),root,f);
                    else System.out.println("You don't allow do this");
                }
                else System.out.println("You don't allow do this");
            }
            else if(operation.equalsIgnoreCase("DeleteFolder") && numOfParts==2){
                if(currentUser==admin){
                    root.deleteFolder(commandsConverter.getPath(),root,f);
                }
                else if(currentUser.getCapabilities().containsKey(commandsConverter.getPathDir())){
                    String access=currentUser.getCapabilities().get(commandsConverter.getPathDir());
                    if(access.equals("01") || access.equals("11")) root.deleteFolder(commandsConverter.getPath(),root,f);
                    else System.out.println("You don't allow do this");
                }
                else System.out.println("You don't allow do this");
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
                    //protection.setCapabilitiesFile();
                    break;
                }
            }
            else if(operation.equalsIgnoreCase("help")){
                System.out.println(">CreateFile PathOfFile SizeOfFile");
                System.out.println(" CreateFolder PathOfFolder");
                System.out.println(" CUser username password 'for admin only'");
                System.out.println(" DeleteFile PathOfFile");
                System.out.println(" DeleteFolder PathOfFolder");
                System.out.println(" DisplayDiskStructure");
                System.out.println(" DisplayDiskStatus");
                System.out.println(" Grant username pathFolder Access 'for admin only'");
                System.out.println(" login username password");
                System.out.println(" TellUser");
            }
            else if(operation.equalsIgnoreCase("TellUser") && numOfParts==1){
                System.out.println(">"+currentUser.getUsername());
            }
            else if(operation.equalsIgnoreCase("CUser") && numOfParts==3){
                String [] arr=command.split(" ");
                protection.createUser(arr[1],arr[2],currentUser);
            }
            else if(operation.equalsIgnoreCase("Grant") && numOfParts==4){
                String [] arr=command.split(" ");
                protection.grantUser(arr[1],arr[2],arr[3],currentUser,root);
                protection.setCapabilitiesFile();
            }
            else if(operation.equalsIgnoreCase("Login") && numOfParts==3){
                String [] arr=command.split(" ");
                int idx= protection.login(arr[1],arr[2]);
                if(idx!=-1) currentUser=protection.getUsers().get(idx);
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
