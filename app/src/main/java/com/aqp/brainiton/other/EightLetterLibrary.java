package com.aqp.brainiton.other;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EightLetterLibrary {
    public static Map<String,String> getQuestionsStage1(){
        HashMap<String,String> questions = new HashMap<>();
        questions.put("I can speak without a tongue and listen without ears\n" +
                "You answer me, but I never ask you a question. What am I?.","Telephone");
        questions.put("I have an eye, strong winds and rain. \n" +
                "Once i came, people leave their homes because I’m dangerous.","Hurricane");
        questions.put("I wear clothes but I’m not a human\n" +
                "I work in a field but I’m not a farmer\n" +
                "My job is to protect the grain and\n" +
                "Keeping away the birds.","Scarecrow");
        questions.put("One simple click, one simple flash. \n" +
                "Preserving a memory, for years I will last.","Photograph");

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
