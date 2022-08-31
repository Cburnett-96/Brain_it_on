package com.aqp.brainiton.other;

import android.content.Context;

import com.aqp.brainiton.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FourLetterLibrary {

    public static Map<String,String> getQuestionsStage1(){
        HashMap<String,String> questions = new HashMap<>();
        questions.put("If you need to see in the darkness of the night, turn me on and I'll give you light.","Lamp");
        questions.put("This is something in your kitchen,\n" +
                "It is where you’d wash your dishes\n" +
                "And there’s a faucet above me.","Sink");
        questions.put("In the classroom I am seen\n" +
                "I have four legs, with a surface for writing.","Desk");
        questions.put("I have four legs, arms and back \n" +
                "You can rest and sit on me\n" +
                "I’m always at the park.","Bench");
        questions.put("In the living room I am seen\n" +
                "When you sit on the sofa\n" +
                "You can watch things on my widescreen.","Telly");
        questions.put("I have a tail, and I have a head \n" +
                "But has no legs what I am?.”","Penny");
        questions.put("I’m always at your kitchen\n" +
                "I have long arms and hands, \n" +
                "I use to hold food what I am?.","Tongs");
        questions.put("I’m always at your pocket, I love carrying coins.","Purse");
        questions.put("I like to twirl my body but keep my head up high.\n" +
                "After I go in, everything becomes tight.","Screw");
        questions.put("What flares up and does a lot of good, \n" +
                "And when it dies is just a piece of wood?.","Match");

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
