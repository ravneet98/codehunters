package com.codehunters.usher;
//CLASS TO HOLD CHAT DATA
public class ChatMessage {
    public String messageText;
    public String uid;

    public String Time;

    public ChatMessage(String messageText, String uid, String Time) {
        this.messageText = messageText;
        this.uid = uid;

        this.Time=Time;
    }

    public ChatMessage() {
    }
}
