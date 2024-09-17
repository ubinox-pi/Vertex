package com.vertex.io;

public class AirtableRecord {

    private Fields fields;

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

    public AirtableRecord(String uid, String name, String gmail, String number, String password, String referBy, String date, double coin, String url, String address) {
        this.Uid = uid;
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

    public static class Fields {
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

        public Fields(String uid, String name, String gmail, String number, String password, String referBy, String date, double coin, String url, String address) {
            Uid = uid;
            Name = name;
            Gmail = gmail;
            Number = number;
            Password = password;
            this.referBy = referBy;
            Date = date;
            Coin = coin;
            Url = url;
            Address = address;
        }
    }
}

