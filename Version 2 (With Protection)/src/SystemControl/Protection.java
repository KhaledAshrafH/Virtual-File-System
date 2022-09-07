package SystemControl;

import FileSystemStructure.Directory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Protection {
    User admin; // admin to control the protection
    ArrayList<User> users; // to store list of users that admin create it

    public Protection(User admin) {
        this.admin = admin;
        users=new ArrayList<>();
    }

    // create new user by admin only
    public void createUser(String username,String password,User currUser) throws Exception {
        //check the current user that already login in the system now
        // if current user is admin..
        if(currUser==admin){
            // if username valid and not exist in the system
            if(checkUserExist(username)==-1){
                String userInfo=username + " , " + password;
                File f=new File("C:\\Users\\Eng Khaled\\VFS_V2\\DataBase\\user.txt");
                DiskDataControl.FWrite(f,userInfo);
                User newUser=new User(username,password);
                users.add(newUser);
                System.out.println(">User added Successfully");
            }
            // if username already exist
            else {
                System.out.println(">Not Valid Username");
            }
        }
        // if current user is user..
        else System.out.println(">You don't Allow to use this command");
    }

    // login user already exist in the system
    public int login(String username,String password){
        int index=checkUserExist(username);
        // if username found
        if(index!=-1){
            //check the password of the user
            if(users.get(index).getPassword().equals(password)){
                System.out.println(">You are successfully logged in.");
                return index;
            }
            else{
                System.out.println(">Password not correct");
            }
        }
        // if username not found
        else{
            System.out.println(">Username not found!");
        }
        return -1;
    }

    public void grantUser(String username, String path, String access, User currUser, Directory root){
        //check if current user is admin
        if(currUser==admin){
            // if path of directory
            if(!path.contains(".")){
                String Temp=path;
                Directory dir=root.checkPathFolder(Temp,root);
                // check if path is found
                if(dir!=null){
                    int index =checkUserExist(username);

                    // check user exist in the system ot not
                    if(index!=-1){
                        users.get(index).capabilities.put(path,access);
                        System.out.println("User Granted Successfully");
                    }
                    else {
                        System.out.println(">Username Not Found");
                    }
                }
                // if path not found
                else{
                    System.out.println(">Path Not Found");
                }
            }
            //if the path of file
            else {
                System.out.println(">This Command use for Folders only");
            }
        }
        // if current user is not admin
        else System.out.println(">You don't Allow to use this command");
    }

    // used to store Capabilities of users in the file (as database)
    public void setCapabilitiesFile() throws Exception {
        File f=new File("C:\\Users\\Eng Khaled\\VFS_V2\\DataBase\\capabilities.txt");
        if(f.exists()) {f.delete();
        f.createNewFile();}
        Set<String> paths=new LinkedHashSet<String>();
        String str="";
        for(int i=0;i<users.size();i++){
            for ( String key : users.get(i).getCapabilities().keySet()) {
                paths.add(key);
            }
        }
        for(String element : paths) {
            str+=element+",";
            for(User user : users){
                if(user.getCapabilities().containsKey(element)){
                    str+=(user.getUsername()+","+user.getCapabilities().get(element))+",";
                }
            }
            str=str.substring(0, str.length() - 1);
            DiskDataControl.FWrite(f,str);
            str="";
        }
    }

    public int checkUserExist(String username){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getUsername().equals(username)){
                return i;
            }
        }
        return -1;
    }
    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
