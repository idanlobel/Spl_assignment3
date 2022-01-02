package bgu.spl.net.srv;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private String password;
    private List<String> followers;
    private String birthday;
    private boolean logged_in;

    public User(String username,String password,String birth)
    {
        this.username=username;
        this.password=password;
        this.birthday=birth;
        this.followers=new ArrayList<>();
        this.logged_in=false;
    }

    public void login()
    {
        this.logged_in=true;
    }

    public boolean isLogged_in()
    {
        return this.logged_in;
    }

    public List<String> getFollowers()
    {
        return this.followers;
    }
    public String getUsername()
    {
        return this.username;
    }




}
