package com.example.android.heychat.Model;

/**
 * Created by kush_1 on 6/26/2017.
 */

public class UserName {
    private String Username;
    public UserName()
    {

    }
    public UserName(String Username)
    {
        this.Username=Username;
    }

    public String getUsername(){ return  Username;}
    public void setUsername(String Username){this.Username=Username;}
}
