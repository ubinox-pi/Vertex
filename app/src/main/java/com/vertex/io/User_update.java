package com.vertex.io;

public class User_update {
    private String Uid;
    private String Name;
    private String Gmail;
    private String Number;
    private String Password;
    private String referBy;
    private String Date;
    private double Coin;
    private String Url;
    private String Address;

    public User_update(String name, String gmail, String number, String password, String referBy, String date, double coin, String url, String address) {
        this.Name = name;
        this.Gmail = gmail;
        this.Number = number;
        this.Password = password;
        this.referBy = referBy;
        this.Date = date;
        this.Coin = coin;
        this.Url = url;
        this.Address = address;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGmail() {
        return Gmail;
    }

    public void setGmail(String gmail) {
        Gmail = gmail;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getReferBy() {
        return referBy;
    }

    public void setReferBy(String referBy) {
        this.referBy = referBy;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public double getCoin() {
        return Coin;
    }

    public void setCoin(double coin) {
        Coin = coin;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
