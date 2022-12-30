package com.aqp.brainiton.model;

public class UserRankingTen {
    public String Username;
    public String Avatar;
    int Points;

    public UserRankingTen(String username, String avatar, int points) {
        Username = username;
        Avatar = avatar;
        Points = points;
    }

    public UserRankingTen() {
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }
}
