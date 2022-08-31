package com.aqp.brainiton.model;

public class UserRankingTwoThree {
    public String Username;
    public String Avatar;
    int Riddle;

    public UserRankingTwoThree(String username, String avatar, int riddle) {
        Username = username;
        Avatar = avatar;
        Riddle = riddle;
    }

    public UserRankingTwoThree() {
    }

    public int getRiddle() {
        return Riddle;
    }

    public void setRiddle(int riddle) {
        Riddle = riddle;
    }
}
