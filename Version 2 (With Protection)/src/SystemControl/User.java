package SystemControl;

import java.util.HashMap;

public class User {
    String username,password;
    HashMap<String,String> capabilities; //path,access

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        capabilities=new HashMap<>();
    }

    public User() {
        capabilities=new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<String, String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(HashMap<String, String> capabilities) {
        this.capabilities = capabilities;
    }
}
