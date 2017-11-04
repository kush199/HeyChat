package com.example.android.heychat.Model;

/**
 * Created by kush_1 on 6/23/2017.
 */

public class Users_Info {
    private String Email;
    private String FirstName;
    private String LastName;
    private String Username;
    private String Password;
    private String device_token;
    private String image;


    public Users_Info()
    {}

    public Users_Info(String Email,String Username)
    {
        this.Email = Email;
        this.Username = Username;
    }



    public String getEmail(){return  Email;}
    public void setEmail(String Email){this.Email=Email;}

    public String getUsername(){return  Username;}
    public void setUsername(String Username){this.Username=Username;}

    public String getFirstName(){return  FirstName;}
    public void setFirstName(String Firstname){this.FirstName=Firstname;}

    public String getLastName(){return  LastName;}
    public void setLastName(String LastName){this.LastName=LastName;}


    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Users_Info(String Email, String Username, String FirstName, String LastName, String Password, String device_token, String image){
        this.Email=Email;
        this.Username=Username;
        this.FirstName=FirstName;
        this.LastName=LastName;
        this.Password = Password;
        this.device_token = device_token;
        this.image = image;
    }
    public Users_Info(String Email,String Username, String FirstName,String LastName,String Password)
    {
        this.Email=Email;
        this.Username=Username;
        this.FirstName=FirstName;
        this.LastName=LastName;
        this.Password = Password;

    }

}
