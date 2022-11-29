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
        questions.put("I have big eyes; I sleep at day and awake at night.","Owl");
        questions.put("I am a stone but I have value, you can see me in jewelry.","Gem");
        questions.put("You put hot water in me, you drink coffee or tea in me.","Mug");
        questions.put("I live in a cave and I hate the light, I fly at night.","Bat");
        questions.put("I am a tiny insect, but hardworking.","Ant");
        questions.put("I live on a farm and I can give you milk, I like to eat grass for a snack.","Cow");
        questions.put("I am fat and pink; I like mud and dirt.","Pig");
        questions.put("I love catching mice, I say meow.","Cat");
        questions.put("I am an insect living in a hive, when I fly, I make a buzzing noise.","Bee");
        questions.put("I am young, I’m always in my mother’s pocket.","Joey");
        questions.put("I’m the strongest, I am the king of the jungle.","Lion");
        questions.put("I am powder white; I am used to making bread.","Flour");
        questions.put("I am green on the ground, in your yard, I can be seen.","Grass");

        return questions;
    }

    public static Map<String,String> getQuestionsStage2(){
        HashMap<String,String> questions = new HashMap<>();

        questions.put("I am a cube that has some spots, I am rolled to play a game.","Dice");
        questions.put("I am an instrument, something that you can beat with sticks.","Drum");
        questions.put("I eat an insect, I can jump, and I am green.","Frog");
        questions.put("I am round, I have a handle, and you can carry water in me.","Pail");
        questions.put("I have many teeth; I use to fix your hair.","Comb");
        questions.put("I’m an insect with wings, I’m attracted by light and flies at night.","Moth");
        questions.put("I’m not sun and moon, but I give light in the darkness.","Bulb");
        questions.put("I’m small and I love cheese, I’m afraid of cats.","Mice");
        questions.put("I’m a vegetable, people cry when cutting me.","Onion");
        questions.put("I can be red or green, I’m dried to make raisins or squeezed to make wine.","Grape");
        questions.put("I fly at the sky, I’m the king of birds.","Eagle");
        questions.put("I am a tall animal and good at running fast, so people ride me in a race.","Horse");

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
