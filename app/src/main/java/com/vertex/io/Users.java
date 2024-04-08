package com.vertex.io;

public class Users {

    String Email, Phone, Password, Name,link, Date,ReferBy;
    int Coin = 0;

    public Users() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getReferBy() {
        return ReferBy;
    }

    public void setReferBy(String referBy) {
        ReferBy = referBy;
    }

    public Users(String referBy) {
        ReferBy = referBy;
    }

    public Users(String email, String phone, String password , String referBy, String name) {
        Email = email;
        Phone = phone;
        Password = password;
        Coin = 0;
        Name = name;
        link = "";
        Date = "";
        ReferBy = referBy;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public int getCoin() {
        return Coin;
    }

    public void setCoin(int coin) {
        Coin = coin;
    }




    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
