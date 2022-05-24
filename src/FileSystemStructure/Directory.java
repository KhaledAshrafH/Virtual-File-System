package FileSystemStructure;

import AllocationMethods.FileAllocation;
import com.sun.deploy.util.StringUtils;

import java.io.FileFilter;
import java.security.DomainLoadStoreParameter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;

public class Directory {
    private String directoryPath;
    private String directoryName;
    private ArrayList<File> files;
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

    public Directory(String directoryPath, String directoryName, ArrayList<File> files, ArrayList<Directory> subDirectories, boolean deleted, FileAllocation allocationType) {
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

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
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

    public void CreateFolder(String path,Directory rootDir){  // root/folder1
        String directoryName="",directoryPath="";
        int temp = 0;
        int index=0;
        if(path.contains("/")) {index=path.lastIndexOf("/");} //   / => 4
        for(int i=index+1;i<path.length();i++){
            directoryName += path.charAt(i);
        }

        for(int i=0;i<index;i++){
            directoryPath += path.charAt(i);
        }
        String  Temp =directoryPath;
        Directory Dir=checkPathFolder(Temp,rootDir);
        if(Dir!=null && !Dir.checkFolderExist(directoryName,temp)) {
            Directory folder=new Directory(directoryPath,directoryName,Dir.allocationType);
            Dir.getSubDirectories().add(folder);
            System.out.println(">Folder Created Successfully");
        }
        else{
            System.out.println(">Failed, Folder Already Exist");
        }
    }

    public void CreateFile(String path,Directory rootDir,int sizeFile){
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
        Directory Dir=checkPathFolder(filePath,rootDir);
        
        if(Dir!=null && !Dir.checkFolderExist(fileName,temp)) {
            File file=new File(fileName,filePath,sizeFile);
            if(Dir.getAllocationType().allocateFile(file)){
                Dir.getFiles().add(file);
                System.out.println(">File Created Successfully");
            }
            else {
                System.out.println(">Not Found Space Enough");
            }
        }
        else{
            System.out.println(">Failed, File Already Exist");
        }
    }

    public void deleteFile(){

    }
    public void deleteFolder (String path,Directory rootDir){
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
        Dir.getSubDirectories().remove(index);
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
        for(int i=0;i<subDirectories.size();i++){
            if(fileName.equalsIgnoreCase(files.get(i).getFileName())) {
                check=true;
                index =i;
                break;
            }
        }
        return check;
    }

    public Directory checkPathFolder(String pathName,Directory rootDir){
        Directory d=rootDir;
        String Temp="";
        String[] FolderParents=pathName.split("/");
        if(pathName.contains("/")) {

            //System.out.println(Arrays.toString(FolderParents));
            int index=0,j=1;
            for(int i=1;i< FolderParents.length ;i++){
                if(d.checkPathFolderUtility(FolderParents[j])!=-1){
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
    public Directory checkPathFile(String pathName,Directory rootDir){
        Directory d=rootDir;
        String Temp="";
        String[] FolderParents=pathName.split("/");
        if(pathName.contains("/")) {
            System.out.println(Arrays.toString(FolderParents));
            int index=0,j=1;
            for(int i=1;i< FolderParents.length ;i++){
                //if(d.checkPathFileUtility(FolderParents[j])!=-1){
                    d = d.getSubDirectories().get(index);
                    j++;
                //}
                //else return null;
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
            index = i;
            if(subDirectories.get(i).getDirectoryName().equalsIgnoreCase(folderName)) return index;
        }
        return index;
    }
//    public int checkPathFileUtility(String fileName,int index){
//        for(int i=0;i<files.size();i++){
//            index = i;
//            if(subDirectories.get(i).getDirectoryName().equalsIgnoreCase(fileName)) return true;
//        }
//        return false;
//    }

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
