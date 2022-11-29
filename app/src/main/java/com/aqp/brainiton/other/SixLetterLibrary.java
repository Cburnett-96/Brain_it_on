package com.aqp.brainiton.other;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SixLetterLibrary {
    public static Map<String,String> getQuestionsStage1(){
        HashMap<String,String> questions = new HashMap<>();

        questions.put("I’m covered with colorful feathers I can whistle, fly and talk.","Parrot");
        questions.put("People walk over me but boats go under me.","Bridge");
        questions.put("I’m soft and hairy, I always stay on the floor.","Carpet");
        questions.put("I blow hot air; you dry your hair with me.","Blower");
        questions.put("I walk and swim, I always carry my house.","Turtle");
        questions.put("I can be red or green, I’m dried to make raisins or squeezed to make wine.","Grapes");
        questions.put("I have two big ears; my favorite food is carrots.","Rabbit");
        questions.put("I am vegetable, when you eat me, your sight will be clear.","Squash");
        questions.put("I’m a yellow fruit and I grow on trees, monkeys love me.","Banana");
        questions.put("I’m in your yard, I always wake you up in the morning.","Rooster");
        questions.put("I have a scent; you use me every day.","Perfume");
        questions.put("I’m a green veggie that looks like a tree.","Broccoli");
        questions.put("I need your hands to use me, I can cut hair even paper.","Scissors");
        questions.put("I’m an insect with wings, I love flowers.","Butterfly");

        return questions;
    }

    public static Map<String,String> getQuestionsStage2(){
        HashMap<String,String> questions = new HashMap<>();

        questions.put("Pointing North, South, East, and West \n" +
                "It saves the lost and helps the rest. What is it?.","Compass");
        questions.put("I am full of holes but still hold water.","Sponge");
        questions.put("I’m always in your ceiling, I eat insects.","Lizard");
        questions.put("I’m in the kitchen, I am noisy, and I can make a milkshake.","Blender");
        questions.put("I am colorful up in the sky, you can see me after the rain.","Rainbow");
        questions.put("Every kid wants me, I’m always at your birthday party.","Balloon");
        questions.put("I’m a transport, I only have two wheels Pedals and a handlebar.","Bicycle");
        questions.put("I came from a palm tree; I am brown and hairy outside and white on the inside.","Coconut");
        questions.put("I’m a gray animal you might love, I have a long trunk.","Elephant");
        questions.put("I’m sweet cold and delicious, you eat me every summer.","IceCream");
        questions.put("My color is black, I use it to cook food.","Charcoal");
        questions.put("I am a paper; I have a lot of news.","Newspaper");

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

    public static Map<String,String> getStage2Questions(Context context, String subject, int SIZE){
        HashMap<String,String> questionsMap = new HashMap<>();
        Map<String,String> originalQuestion;

        originalQuestion = getQuestionsStage2();

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
