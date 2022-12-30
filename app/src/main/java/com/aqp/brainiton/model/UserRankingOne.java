package com.aqp.brainiton.model;

public class UserRankingOne {
    public String Username;
    public String Avatar;
    int Points;

    public UserRankingOne(String username, String avatar, int points) {
        Username = username;
        Avatar = avatar;
        Points = points;
    }

    public UserRankingOne() {
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }
}
