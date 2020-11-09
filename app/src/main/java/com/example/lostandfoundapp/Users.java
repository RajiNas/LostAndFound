package com.example.lostandfoundapp;

public class Users {
    String username;
    String email;
    Long phone;
    int password;
//    int confPassword;

    public Users() { }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

//    public int getConfPassword() {
//        return confPassword;
//    }
//
//    public void setConfPassword(int confPassword) {
//        this.confPassword = confPassword;
//    }
}