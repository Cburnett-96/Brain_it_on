package com.aqp.brainiton.other;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.aqp.brainiton.R;

public class WordLibrary {

    public static Map<String,String> getQuestionsStage1(){
        HashMap<String,String> questions = new HashMap<>();
        questions.put("a container made of metal and used for cooking food. \nSentence: The pan is very hot.","Pan");
        questions.put("a container, used for storage or cooking. \nSentence: There is tea in the pot.","Pot");
        questions.put("a group of houses and buildings. \nSentence: Life in the village is very peaceful.","Village");
        questions.put("a fruit consisting of a hard shell around an edible kernel. \nSentence: I have a nut.","Nut");
        questions.put("soil, loam, silt, or clay mixed with water. \nSentence: The pig plays in the mud.","Mud");
        questions.put("a thick fabric for covering part of a floor. \nSentence: He is sitting on a rug.","Rug");
        questions.put("to go quickly by moving the legs. \nSentence: He can run the fastest in the class.","Run");
        questions.put("pushing upward with one's legs and feet. \nSentence: I can jump on the rock.","Jump");
        questions.put("a body of water smaller than a lake. \nSentence: A frog swam in the pond.","Pond");
        questions.put("a round or oval flat dish that is used to hold food. \nSentence: I had a plate of spaghetti.","Plate");
        questions.put("feeling fear or anxiety; frightened. \nSentence: She was afraid of snakes.","Afraid");
        questions.put("a bed for a small baby. \nSentence: Ana put the baby in his crib.","Crib");
        questions.put("a device to catch and retain animals. \nSentence: Mark set a trap to catch the mouse.","Trap");
        questions.put("a simple meal that is quick to cook and eat. \nSentence: I have fruit for a snack.","Snack");
        questions.put("an insect or small animal which damages crops or food supplies. \nSentence: Black bugs are a pest for farmers.","Pest");
        questions.put("a barrier between two areas of land, made of wood or wire. \nSentence: They painted the fence white.","Fence");
        questions.put("sit on and control (a bicycle, motorcycle, or animal). \nSentence: I like to ride horses.","Ride");
        questions.put("food made from flour, water, and yeast. \nSentence: I bake some bread.","Bread");
        questions.put("consists of unwanted things or waste material. \nSentence: The garbage man collected the trash this morning.","Trash");
        questions.put("a large bag made of rough woven material. \nSentence: I want a sack of rice.","Sack");
        questions.put("an animal with a long tail that lives in hot countries and climbs trees. \nSentence: The monkey was swinging in the tree.","Monkey");
        questions.put("a large body of water surrounded by land. \nSentence: There is a small lake in front of my house.","Lake");
        questions.put("consists of meat, vegetables, or fruit baked in pastry. \nSentence: Anne's apple pie looks awfully good.","Pie");
        questions.put("a group of musicians playing together. \nSentence: Troy was a drummer in a rock band.","Band");
        questions.put("the hard parts inside your body which together form your skeleton. \nSentence: Boyet broke a bone in his left arm.","Bone");
        questions.put("a ball-shaped object with a map of the world on it. \nSentence: Mrs. Sarah has a globe in her office.","Globe");
        questions.put("an object that children play with. \nSentence: My favorite toy is a car.","Toy");
        questions.put("the soil and rock on the earth's surface. \nSentence: An apple fell to the ground.","Ground");
        questions.put("a wide way leading from one place to another. \nSentence: My house is at the end of the road.","Road");
        questions.put("birds hard curved or pointed part of its mouth. \nSentence: The bird had a worm in its beak.","Beak");
        questions.put("the flesh of an animal. \nSentence: Red meat is full of iron.","Meat");
        questions.put("a sharp-pointed woody projection from a stem or leaf. \nSentence: A thorn stuck in her finger.","Thorn");
        questions.put("a person who receives hospitality at someone else's home. \nSentence: He is a regular guest at the hotel.","Guest");
        questions.put("having a high degree of heat or a high temperature. \nSentence: It is hot in the summer and cold in the winter.","Hot");
        questions.put("a stretch of shoreline that curves inwards. \nSentence: The ships in the bay present a beautiful sight.","Bay");
        questions.put("a small vessel propelled on water by oars, sails, or an engine. \nSentence: The boy tied his boat to the dock.","Boat");
        questions.put("a place or structure in which birds or other animals lay eggs or give birth to young. \nSentence: The bird built a nest out of small twigs.","Nest");
        questions.put("is a place for the people accused or convicted of a crime. \nSentence: He is in jail for theft.","Jail");
        questions.put("a member of a religious community of women. \nSentence: The nun has dedicated her life to prayer and good deeds.","Nun");
        questions.put("wheat or any other cereal crop used as food. \nSentence: The machine grinds grain into flour.","Grain");

        return questions;
    }

    public static Map<String,String> getQuestionsStage2(){
        HashMap<String,String> questions = new HashMap<>();
        questions.put("find (something or someone) Elect- choose (someone) to hold public office or some other position by voting.","Discover");
        questions.put("grow or cause to grow and become more mature, advanced, or elaborate.","Develop");
        questions.put("come or go back to a place or person.","Return");
        questions.put("a small piece of paper, fabric, plastic, or similar material attached to an object and giving information about it.","Label");
        questions.put("being the same in quantity, size, degree, or value.","Equal");
        questions.put("a scientific procedure to make a discovery, test a or demonstrate a known fact.","Experiment");
        questions.put("go away from.","Leave");
        questions.put("a man who has people working for him, especially servants or slaves.","Master");
        questions.put("known about by many people.","Famous");
        questions.put("not making any sound.","Silent");
        questions.put("a detailed proposal for doing or achieving something.","Plan");

        return questions;
    }

    public static Map<String,String> getQuestionsStage3(){
        HashMap<String,String> questions = new HashMap<>();
        questions.put("is the scientific study of life.","Biology");
        questions.put("is a commercial advertisement.","Trailer");
        questions.put("is the practice of cultivating plants and livestock.","Agriculture");
        questions.put("talk about (something) with another person or group of people.","Discuss");
        questions.put("to give back or restore (especially money); repay.","Refund");
        questions.put("a hot glowing body of ignited gas that is generated by something on fire.","Flames");
        questions.put("a main division of a book, typically with a number or title.","Chapter");
        questions.put("firmly press (something soft or yielding).","Squeeze");
        questions.put("rub out or remove (writing or marks).","Erase");
        questions.put("an abundance of valuable possessions or money.","Wealth");
        questions.put("a verbal or written answer.","Response");

        return questions;
    }

    public static Map<String,String> getQuestionsStage4(){
        HashMap<String,String> questions = new HashMap<>();
        questions.put("the outside limit of an object, area, or surface.","Edge");
        questions.put("come into or be in contact with.","Touch");
        questions.put("a room, set of rooms, or building used as a place for commercial, professional, or bureaucratic work.","Office");
        questions.put("lacking consideration for others; response- a verbal or written answer.","Selfish");
        questions.put("have the same opinion about something.","Agree");
        questions.put("take (something) away or off from the position occupied.","Remove");
        questions.put("the short, fine, soft hair of certain animals.","Fur");
        questions.put("very thin.","Skinny");
        questions.put("an idea or opinion produced by thinking.","Thought");
        questions.put("a person who plays a musical instrument.","Musician");
        questions.put("cause (someone) to believe in the truth of something.","Convince");

        return questions;
    }

    public static Map<String,String> getRandomQuestions(int SIZE){
        HashMap<String,String> questionsMap = new HashMap<>();
        Map<String,String> originalQuestion = new HashMap<>();

        originalQuestion = getQuestionsStage1();
        //originalQuestion.putAll(getQuestionsStage2());
        //originalQuestion.putAll(getQuestionsStage3());
        //originalQuestion.putAll(getQuestionsStage4());

        int originalSize =  originalQuestion.size();
        System.out.println("SIZE: "+originalQuestion.size());
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
