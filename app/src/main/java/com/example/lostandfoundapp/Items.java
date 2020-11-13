package com.example.lostandfoundapp;

public class Items
{
    String UserName;
    String title;
    Float lon;
    Float lat;
    String description;
    String category;
    String date;

    public Items(String userName, String title, Float lon, Float lat, String description, String category, String date) {
        UserName = userName;
        this.title = title;
        this.lon = lon;
        this.lat = lat;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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
}
