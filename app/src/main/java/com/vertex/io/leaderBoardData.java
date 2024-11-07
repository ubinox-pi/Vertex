package com.vertex.io;

public class leaderBoardData {
    private String name;
    private double coin;
    private String Url;

    public leaderBoardData() {
        //firebase
    }

    public leaderBoardData(String name, double coin, String Url) {
        this.name = name;
        this.coin = coin;
        this.Url = Url;
    }

    public String getName() {
        return name;
    }

    public double getCoin() {
        return coin;
    }

    public String getUrl() {
        return Url;
    }
}
