package com.example.lostandfoundapp;

public class Users
{
    private String username;
    private String image;

    public Users(String username, String image) {
        this.username = username;
        this.image = image;
    }

    public Users ()
    {

    }


    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
