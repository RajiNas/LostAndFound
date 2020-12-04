package com.example.lostandfoundapp;

import java.util.Date;

public class Message {
    private String messageText;
    private String currentUser;
    private String contact;
    private long messageTime;

    public Message(String messageText, String messageUser,String contact)
    {

        this.messageText = messageText;
        this.currentUser = messageUser;
        this.contact = contact;

        messageTime = new Date().getTime();
    }

    public Message()
    {
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
