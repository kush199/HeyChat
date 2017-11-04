package com.example.android.heychat.Model;

/**
 * Created by CHALLIDO on 7/20/2017.
 */

public class UserNameWithPic {
    private String Username;
    private String UserName;
    private String image;

    public UserNameWithPic(){}

    public UserNameWithPic(String userName, String image) {
        UserName = userName;
       this.image = image;
    }

    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setUrl(String image) {
        this.image = image;
    }
}
