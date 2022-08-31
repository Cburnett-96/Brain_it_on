package com.aqp.brainiton.other;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SixLetterLibrary {
    public static Map<String,String> getQuestionsStage1(){
        HashMap<String,String> questions = new HashMap<>();
        questions.put("I have a handle, spout and a lid.\n" +
                "I am used to boiled water\n" +
                "I have no mouth but once I’m done you can hear my loud whistle.","Kettle");
        questions.put("I go up and down at the same time.\n" +
                "Up towards to sky and down towards the ground.","Seesaw");
        questions.put("Pointing North, South, East, and West \n" +
                "It saves the lost and helps the rest. What is it?.","Compass");

        return questions;
    }

    public static Map<String,String> getStageQuestions(Context context, String subject, int SIZE){
        HashMap<String,String> questionsMap = new HashMap<>();
        Map<String,String> originalQuestion;

        originalQuestion = getQuestionsStage1();

        int originalSize =  originalQuestion.size();
        ArrayList<String> keyList = new ArrayList<>(originalQuestion.keySet());

        while (questionsMap.size()<=SIZE){
            Random random = new Random();
            int randomNumber = random.nextInt(originalSize);
            String question = keyList.get(randomNumber);
            if (!questionsMap.containsKey(question)){
                questionsMap.put(question,originalQuestion.get(question));
            }
        }
        return questionsMap;
    }
}
