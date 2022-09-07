package SystemControl;

import java.util.ArrayList;

public class CommandsConverter {
    String command;
    ArrayList<String> commandParts;
    int numOfParts;
    public CommandsConverter(String command){
        this.command=command;
        commandParts=new ArrayList<>();

        numOfParts=getNumOfSpaces(command)+1;
        String temp="";
        char c;
        for(int i=0;i<command.length();i++){
            c=command.charAt(i);
            if(c!=' ') {
                temp+=c;
            }
            else {
                commandParts.add(temp);
                temp="";
            }

        }
        commandParts.add(temp);
    }
    public String getOperation(){
        return commandParts.get(0);
    }
    public String getPath(){
        if(numOfParts>=2) return commandParts.get(1);
        return "null";
    }
    public String getPathDir(){
        String str="";int index=0;
        if(commandParts.get(1).contains("/")) index=commandParts.get(1).lastIndexOf("/");
        else return commandParts.get(1);
        for(int i=0;i<index;i++){
            str+=commandParts.get(1).charAt(i);
        }
        return str;
    }
    public String getName(){
        if(numOfParts>=2){
            String str=commandParts.get(1);
            String result="";
            int lastSlash=str.lastIndexOf("/");
            for(int i=lastSlash+1;i<str.length();i++){
                result+=str.charAt(i);
            }
            return result;
        }
        return "null";
    }
    public int getSize(){
        if(numOfParts>=3){
            return (Integer.parseInt(String.valueOf(Integer.parseInt(commandParts.get(2)))));
        }
        return -1;
    }

    public int getNumOfParts() {
        return numOfParts;
    }

    private int getNumOfSpaces(String str){
        int cnt=0;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)==' ') cnt++;
        }
        return cnt;
    }

}
