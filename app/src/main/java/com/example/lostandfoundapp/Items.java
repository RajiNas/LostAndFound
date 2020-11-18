package com.example.lostandfoundapp;

import com.google.firebase.firestore.Exclude;

public class Items
{
    private String username;
    private String title;
    private Float lon;
    private Float lat;
    private String description;
    private String category;
    private String date;
   private String status;
   private String image;
    private String mKey;



    public Items() {
    }

    public Items(String username, String title, Float lon, Float lat, String description, String category, String date, String status, String image) {
        this.username = username;
        this.title = title;
        this.lon = lon;
        this.lat = lat;
        this.description = description;
        this.category = category;
        this.date = date;
        this.status = status;
        this.image = image;
    }

// the @Eclude with exclude the value from the database
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

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) { username = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
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
