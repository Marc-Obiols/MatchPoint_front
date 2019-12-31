package com.example.loginlayout;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class holder_event_card {
    private CircleImageView image;
    private String title;
    private String data;
    private String user;

    public holder_event_card(String textTitle, String textUser, String date){
        //this.image = image;
        title = textTitle;
        user = textUser;
        data = date;
    }

    public CircleImageView getEventImage(){
        return image;
    }

    public String getEventTitle(){
        return title;
    }

    public String getEventUser(){
        return user;
    }

    public String getEventDate(){
        return data;
    }

}
