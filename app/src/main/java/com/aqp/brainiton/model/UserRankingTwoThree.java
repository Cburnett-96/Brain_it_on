package com.aqp.brainiton.model;

public class UserRankingTwoThree {
    public String Username;
    public String Avatar;
    int Points;

    public UserRankingTwoThree(String username, String avatar, int points) {
        Username = username;
        Avatar = avatar;
        Points = points;
    }

    public UserRankingTwoThree() {
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }
}
