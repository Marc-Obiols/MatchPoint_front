package com.example.loginlayout;

public class Chat {
    private String mName;

    public Chat() {}  // Needed for Firebase

    public Chat(String name) {
        mName = name;
    }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }
}
