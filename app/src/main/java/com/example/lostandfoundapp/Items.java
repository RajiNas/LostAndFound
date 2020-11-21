package com.example.lostandfoundapp;

import com.google.firebase.firestore.Exclude;

public class Items
{
    private String username;
    private String title;
    private String description;
    private String category;
    private String address;
    private String date;
    private String status;
    private String image;
    private String mKey;

    public Items() {
    }

    public Items(String username, String title, String address, String description, String category, String date, String status, String image) {
        this.username = username;
        this.title = title;
        this.address = address;
        this.description = description;
        this.category = category;
        this.date = date;
        this.status = status;
        this.image = image;
    }

// the @Exclude with exclude the value from the database
    @Exclude
    public String getmKey() {
        return mKey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
