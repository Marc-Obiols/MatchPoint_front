package com.example.loginlayout;

public class holder_event_card_simple {

    private String title;
    private String data;

    public holder_event_card_simple(String textTitle, String date){
        title = textTitle;
        data = date;
    }

    //Getter
    public String getEventTitle(){
        return title;
    }

    public String getEventDate(){
        return data;
    }


    //Setter

    public void setEventTitle(String input){
        title = input;
    }

    public void setEventDate(String input){
        data = input;
    }
}
