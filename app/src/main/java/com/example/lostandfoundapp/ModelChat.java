package com.example.lostandfoundapp;

public class ModelChat {
    String message, receiver, sender, timestamp, titleOfMsg;
    boolean isSeen;

    public ModelChat() {
    }

    public ModelChat(String message, String receiver, String sender, String timestamp, String titleOfMsg, boolean isSeen) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.timestamp = timestamp;
        this.titleOfMsg = titleOfMsg;
        this.isSeen = isSeen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitleOfMsg() {
        return titleOfMsg;
    }

    public void setTitleOfMsg(String titleOfMsg) {
        this.titleOfMsg = titleOfMsg;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}