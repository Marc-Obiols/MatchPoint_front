package com.example.loginlayout;

public class holder_event_card_simple {

    private String title;
    private String data;
    private String creatorId;
    private String eventId;

    public holder_event_card_simple(String textTitle, String date, String creador, String evId){
        title = textTitle;
        data = date;
        creatorId = creador;
        eventId = evId;
    }

    //Getter
    public String getEventTitle(){
        return title;
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

    public void setEventTitle(String input){
        title = input;
    }

    public void setEventDate(String input){
        data = input;
    }
}