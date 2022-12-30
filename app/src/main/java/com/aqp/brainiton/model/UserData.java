package com.aqp.brainiton.model;

public class UserData {
    public String Username,Avatar, LetterStage;
    public int Stage, Question, Coin, TotalCoins, Points;

    public UserData(String username, String avatar, String letterStage, int stage, int question, int coin, int totalCoins, int point) {
        Username = username;
        Avatar = avatar;
        LetterStage = letterStage;
        Stage = stage;
        Question = question;
        Coin = coin;
        TotalCoins = totalCoins;
        Points = point;
    }
}
