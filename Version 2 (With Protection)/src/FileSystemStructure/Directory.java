package FileSystemStructure;

import AllocationMethods.FileAllocation;
import SystemControl.DiskDataControl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Directory {
    private String directoryPath;
    private String directoryName;
    private ArrayList<VirtualFile> files;
    private ArrayList<Directory> subDirectories;
    private boolean deleted = false;
    private FileAllocation allocationType;


    public Directory(String directoryPath, String directoryName, FileAllocation allocationType) {
        this.directoryPath = directoryPath;
        this.directoryName = directoryName;
        this.allocationType = allocationType;
        files=new ArrayList<>();
        subDirectories=new ArrayList<>();
    }

    public Directory(String directoryPath, String directoryName, ArrayList<VirtualFile> files, ArrayList<Directory> subDirectories, boolean deleted, FileAllocation allocationType) {
        this.directoryPath = directoryPath;
        this.directoryName = directoryName;
        this.files = files;
        this.subDirectories = subDirectories;
        this.deleted = deleted;
        this.allocationType = allocationType;
    }


    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public ArrayList<VirtualFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<VirtualFile> files) {
        this.files = files;
    }

    public ArrayList<Directory> getSubDirectories() {
        return subDirectories;
    }

    public void setSubDirectories(ArrayList<Directory> subDirectories) {
        this.subDirectories = subDirectories;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public FileAllocation getAllocationType() {
        return allocationType;
    }

    public void setAllocationType(FileAllocation allocationType) {
        this.allocationType = allocationType;
    }

    public void CreateFolder(String path,Directory rootDir,File f,boolean check) throws Exception {  // root/folder1
        String directoryName="",directoryPath="";
        int temp = 0;
        int index=0;
        if(path.contains("/")) {index=path.lastIndexOf("/");} //   / => 4
        if(!path.contains("root")) {
            System.out.println(">Please Enter path correctly!");
            return;
        }
        for(int i=index+1;i<path.length();i++){
            directoryName += path.charAt(i);
        }

        for(int i=0;i<index;i++){
            directoryPath += path.charAt(i);
        }
        String Temp =directoryPath;
        Directory Dir=checkPathFolder(Temp,rootDir);
        if(Dir==null) {
            System.out.println(">Please Enter path correctly!");
            return;
        }
        if(Dir!=null && !Dir.checkFolderExist(directoryName,temp)) {
            Directory folder=new Directory(directoryPath,directoryName,Dir.allocationType);
            Dir.getSubDirectories().add(folder);
            DiskDataControl.FWrite(f,path);
            if(check) System.out.println(">Folder Created Successfully");
        }
        else{
            System.out.println(">Failed, Folder Already Exist");
        }
    }

    public void CreateFile(String path, Directory rootDir, int sizeFile, File f,boolean check) throws Exception {
        String fileName="",filePath="";
        int temp = 0,index=0;
        if(!path.contains(".")) {
            System.out.println(">Please Enter the Extension of the file!");
            return;
        }
        if(!path.contains("root")) {
            System.out.println(">Please Enter path correctly!");
            return;
        }
        if(path.contains("/")) { index=path.lastIndexOf("/");} //   / => 4

        for(int i=index+1;i<path.length();i++){
            fileName += path.charAt(i);
        }

        for(int i=0;i<index;i++){
            filePath += path.charAt(i);
        }
        String Temp=filePath;
        Directory Dir=checkPathFolder(filePath,rootDir);
        if(Dir==null) {
            System.out.println(">Please Enter path correctly!");
            return;
        }
        if(Dir!=null && !Dir.checkFileExist(fileName,temp)) {
            VirtualFile file=new VirtualFile(fileName,Temp,sizeFile);
            if(Dir.getAllocationType().allocateFile(file,f,path)){
                Dir.getFiles().add(file);
                if(check) System.out.println(">File Created Successfully");
            }
            else {
                System.out.println(">Not Found Space Enough");
            }
        }
        else{
            System.out.println(">Failed, File Already Exist");
        }
    }

    public void CreateFile(String path, Directory rootDir, int sizeFile, File f,boolean check,ArrayList<Integer> spaces) throws Exception {
        String fileName="",filePath="";
        int temp = 0,index=0;
        if(!path.contains(".")) {
            System.out.println(">Please Enter the Extension of the file!");
            return;
        }
        if(!path.contains("root")) {
            System.out.println(">Please Enter path correctly!");
            return;
        }
        if(path.contains("/")) { index=path.lastIndexOf("/");} //   / => 4

        for(int i=index+1;i<path.length();i++){
            fileName += path.charAt(i);
        }

        for(int i=0;i<index;i++){
            filePath += path.charAt(i);
        }
        String Temp=filePath;
        Directory Dir=checkPathFolder(filePath,rootDir);
        if(Dir==null) {
            System.out.println(">Please Enter path correctly!");
            return;
        }
        if(Dir!=null && !Dir.checkFileExist(fileName,temp)) {
            VirtualFile file=new VirtualFile(fileName,Temp,sizeFile);
            if(Dir.getAllocationType().allocateFile(file,f,path,spaces)){
                Dir.getFiles().add(file);
                if(check) System.out.println(">File Created Successfully");
            }
            else {
                System.out.println(">Not Found Space Enough");
            }
        }
        else{
            System.out.println(">Failed, File Already Exist");
        }
    }

    public void deleteFile(String path,Directory rootDir,File f) throws Exception {
        String fileName="",filePath="";
        int temp = 0,index=0;

        if(path.contains("/")) { index=path.lastIndexOf("/");} //   / => 4

        for(int i=index+1;i<path.length();i++){
            fileName += path.charAt(i);
        }

        for(int i=0;i<index;i++){
            filePath += path.charAt(i);
        }
        String Temp=filePath;
        Directory Dir=checkPathFolder(Temp,rootDir);
        index=Dir.checkPathFileUtility(fileName);
        if(index!=-1) {
            Dir.getAllocationType().deAllocateFile(Dir.getFiles().get(index),f,path);
            Dir.getFiles().remove(index);
            System.out.println(">File Deleted Successfully");
        }
        else System.out.println(">File not Found");
    }
    public void deleteFolder (String path,Directory rootDir,File f) throws Exception {
        String folderName="",folderPath="";
        int temp = 0,index=0;
        if(path.contains("/")) { index=path.lastIndexOf("/");} //   / => 4

        for(int i=index+1;i<path.length();i++){
            folderName += path.charAt(i);
        }

        for(int i=0;i<index;i++){
            folderPath += path.charAt(i);
        }
        String Temp=folderPath;
        Directory Dir=checkPathFolder(Temp,rootDir);
        index=Dir.checkPathFolderUtility(folderName);
        Directory currentDir=Dir.getSubDirectories().get(index);
        if(index !=-1){
            for(int i=0;i<currentDir.getFiles().size();i++){
                String str="";
                str+=(currentDir.getFiles().get(i).getFilePath()+"/"+currentDir.getFiles().get(i).getFileName());
                currentDir.getAllocationType().deAllocateFile(currentDir.getFiles().get(i),f,str);
            }
            for(int i=0;i<currentDir.getSubDirectories().size();i++){
                deleteAllFiles(currentDir.getSubDirectories().get(i),f);
            }
            Dir.getSubDirectories().remove(index);
            DiskDataControl.removeLine(f,path);
            System.out.println(">Folder Deleted Successfully");
        }
        else System.out.println(">Folder not Found");
    }

    public boolean checkFolderExist(String folderName,int index){ //true if exist ...
        boolean check=false;
        for(int i=0;i<subDirectories.size();i++){
            if(folderName.equalsIgnoreCase(subDirectories.get(i).getDirectoryName())) {
                check=true;
                index =i;
                break;
            }
        }
        return check;
    }
    public boolean checkFileExist(String fileName,int index){ //true if exist ...
        boolean check=false;
        for(int i=0;i<files.size();i++){
            if(fileName.equalsIgnoreCase(files.get(i).getFileName())) {
                check=true;
                index =i;
                break;
            }
        }
        return check;
    }
    public void deleteAllFiles(Directory dir,File f) throws Exception {
        for(int i=0;i<dir.getFiles().size();i++){
            String str="";
            str+=(dir.getFiles().get(i).getFilePath()+"/"+dir.getFiles().get(i).getFileName());
            dir.getAllocationType().deAllocateFile(dir.getFiles().get(i),f,str);
        }
        for(int i=0;i<dir.getSubDirectories().size();i++){
            deleteAllFiles(dir.getSubDirectories().get(i),f);
        }

    }
    public Directory checkPathFolder(String pathName,Directory rootDir){
        Directory d=rootDir;
        String Temp="";
        String[] FolderParents=pathName.split("/");
        if(pathName.contains("/")) {
            //System.out.println(Arrays.toString(FolderParents));
            int index=0,j=1;
            for(int i=1;i< FolderParents.length ;i++){
                index=d.checkPathFolderUtility(FolderParents[j]);
                if(index!=-1){
                    d = d.getSubDirectories().get(index);
                    j++;
                }
                else return null;
            }
            return d;
        }
        else if(pathName.equals("root")) {
            return d;
        }
        return null;
    }


    public int checkPathFolderUtility(String folderName){
        int index=-1;
        for(int i=0;i<subDirectories.size();i++){
            if(subDirectories.get(i).getDirectoryName().equals(folderName)) {
                index = i;
                return index;
            }
        }
        return index;
    }
    public int checkPathFileUtility(String fileName){
        int index=-1;
        for(int i=0;i<files.size();i++){

            if(files.get(i).getFileName().equals(fileName)) {
                index = i;
                return index;
            }
        }
        return index;
    }

    public void printDirectoryStructure(Directory rootDir,int level){
        int dirsLength=rootDir.subDirectories.size();
        int filesLength=rootDir.files.size();
        String str="",str2="";
        int space=((level+1)*4);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < space; i++) {
            sb.append(' ');
        }
        str += sb.toString();
        if(level==0) System.out.println(">-" +rootDir.getDirectoryName());

        for(int i=0;i<filesLength;i++){
            System.out.println(str+'-'+files.get(i).getFileName());
        }

        for(int i=0;i<dirsLength;i++){
            System.out.println(str+"-"+subDirectories.get(i).getDirectoryName());
            Directory d=subDirectories.get(i);
            if(d.subDirectories.size()>0 || d.files.size()>0) {
                d.printDirectoryStructure(d,level+1);
            }
        }

    }
    public void displayDiskStatus(){
        ArrayList<Integer> freeSpaces=new ArrayList<>();
        ArrayList<Integer> allocatedSpaces=new ArrayList<>();
        System.out.print(">Empty space: "+this.getAllocationType().getNumOfFreeBlocks()+"\n");
        System.out.print(">Allocated space: "+this.getAllocationType().getNumOfAllocatedBlocks()+"\n");
        String AllBlocks=getAllocationType().getFreeAndAllocatedBlocks();
        for(int i=0;i<AllBlocks.length();i++){
            if(AllBlocks.charAt(i)=='0') freeSpaces.add(i);
            else allocatedSpaces.add(i);
        }

        System.out.println(">Empty Blocks in the Disk");
        System.out.println("-------------------------");
        System.out.print("");
        for(int i=0;i<freeSpaces.size();i++){
            if(i!=freeSpaces.size()-1) System.out.print("|"+freeSpaces.get(i)+"|Free|, ");
            else System.out.print("|"+freeSpaces.get(i)+"|Free|");
        }
        System.out.println("\n\n>Allocated  Blocks in the Disk");
        System.out.println("--------------------------------");
        System.out.print("");
        for(int i=0;i<allocatedSpaces.size();i++){
            if(i!=allocatedSpaces.size()-1) System.out.print("|"+allocatedSpaces.get(i)+"|Allocated|, ");
            else System.out.print("|"+allocatedSpaces.get(i)+"|Allocated|\n");
        }
        System.out.println("\n>");

    }

}
