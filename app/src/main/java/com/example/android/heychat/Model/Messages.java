package com.example.android.heychat.Model;

/**
 * Created by kush_1 on 6/23/2017.
 */

public class Messages {
    public  String text;
    public  String name;
    public  String image;

    public Messages() {
    }

    public Messages(String text,String name) {
        this.text=text;
        this.name=name;


    }

    public String getText(){return text;}
    public void setText(String text){this.text=text;}

    public String getName(){return name;}
    public void setName(String name ){this.name=name;}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}




