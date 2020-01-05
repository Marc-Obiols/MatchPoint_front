package com.example.loginlayout;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class holder_event_card {
    private CircleImageView image;
    private String title;
    private String data;
    private String user;
    private String creatorId;
    private String eventId;

    public holder_event_card(String textTitle, String textUser, String date, String creador, String evId){
        title = textTitle;
        user = textUser;
        data = date;
        creatorId = creador;
        eventId = evId;
    }

    //Getter
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

    public String getEventId(){
        return eventId;
    }

    public String getEventCreatorId(){
        return creatorId;
    }


    //Setter
    public void setEventImage(CircleImageView input){
        image = input;
    }

    public void setEventTitle(String input){
        title = input;
    }

    public void setEventUser(String input){
        user = input;
    }

    public void setEventDate(String input){
        data = input;
    }

}
