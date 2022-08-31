package com.aqp.brainiton.model;

public class UserRankingOne {
    public String Username;
    public String Avatar;
    int Riddle;

    public UserRankingOne(String username, String avatar, int riddle) {
        Username = username;
        Avatar = avatar;
        Riddle = riddle;
    }

    public UserRankingOne() {
    }

    public int getRiddle() {
        return Riddle;
    }

    public void setRiddle(int riddle) {
        Riddle = riddle;
    }
}
