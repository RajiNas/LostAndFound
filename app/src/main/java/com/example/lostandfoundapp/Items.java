package com.example.lostandfoundapp;

import com.google.firebase.firestore.Exclude;

import java.util.Comparator;

public class Items {
    private String username;
    private String userEmail;
    private String title;
    private String description;
    private String category;
    private String address;
    private String date;
    private String userEmail;
    private String status;
    private String image;
    private String mKey;


    public Items() {
    }

    public Items(String username, String userEmail, String title, String address, String description, String category, String date, String status, String image) {
        this.username = username;
        this.userEmail = userEmail;
        this.title = title;
        this.address = address;
        this.description = description;
        this.category = category;
        this.date = date;
        this.status = status;
        this.image = image;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    // add comparator to sort the data

    public static final Comparator<Items> titleComparator = new Comparator<Items>() {
        @Override
        public int compare(Items items, Items t1) {
            return items.getTitle().compareTo(t1.getTitle());
        }
    };

}


