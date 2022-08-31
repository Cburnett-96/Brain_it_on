package com.aqp.brainiton.model;

public class UserRankingTen {
    public String Username;
    public String Avatar;
    int Riddle;

    public UserRankingTen(String username, String avatar, int riddle) {
        Username = username;
        Avatar = avatar;
        Riddle = riddle;
    }

    public UserRankingTen() {
    }

    public int getRiddle() {
        return Riddle;
    }

    public void setRiddle(int riddle) {
        Riddle = riddle;
    }
}
