package com.aqp.brainiton;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.aqp.brainiton.other.Constants;
import com.aqp.brainiton.other.EightLetterLibrary;
import com.aqp.brainiton.other.FourLetterLibrary;
import com.aqp.brainiton.other.SixLetterLibrary;
import com.aqp.brainiton.other.SoundPoolManager;
import com.aqp.brainiton.other.SynonymOnly;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    DatabaseReference myRef, myRefBadge, myRefStage;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private int presCounter = 0;
    private int maxPresCounter;
    String[] keys = null;
    String answerWord, subject;
    int stageCoin;
    int totalCoins = 0;
    String textAnswer, textDescription;
    TextView textScreen, textQuestion, textTitle;
    Animation smallbigforth;
    Button btnShuffle, btnReset, btnBack, btnCoin, btnPronoun, btnClue;
    EditText editText;
    LinearLayout linearLayout, layoutNextStage;

    private int currentQuestionIndex = 0;
    int max;
    private Map<String, String> getWordMap;
    private List<String> word, description;
    //char randomizedCharacter1, randomizedCharacter2;
    Random rnd = new Random();
    String urlThesauras;
    TextToSpeech textToSpeech;

    TextView tvCoinsEarned, tvWord, tvSynonym;
    Button btnNextStage;

    SharedPreferences prefs;
    boolean isStage1Four, isStage1Six, isStage1Eight;
    boolean isBadge1, isBadge2, isBadge3, isBadge4, isBadge5, isBadge6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_purple));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.dark_purple));

        editText = findViewById(R.id.editText);
        linearLayout = findViewById(R.id.layoutParent);
        layoutNextStage = findViewById(R.id.layoutNextStage);
        btnReset = findViewById(R.id.resetButton);
        btnShuffle = findViewById(R.id.shuffleButton);
        btnBack = findViewById(R.id.backButton);
        btnCoin = findViewById(R.id.coinButton);
        btnPronoun = findViewById(R.id.btn_Pronoun);
        btnClue = findViewById(R.id.clueButton);
        textScreen = findViewById(R.id.textScreen);
        textTitle = findViewById(R.id.textTitle);
        smallbigforth = AnimationUtils.loadAnimation(this, R.anim.smallbigforth);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isStage1Four = prefs.getBoolean("isStage1Four",false);
        isStage1Six  = prefs.getBoolean("isStage1Six",false);
        isStage1Eight = prefs.getBoolean("isStage1Eight",false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("UserData").child(currentUser.getUid());
        myRefStage = FirebaseDatabase.getInstance().getReference("UserStages").child(currentUser.getUid());
        myRefBadge = FirebaseDatabase.getInstance().getReference("UserBadge").child(currentUser.getUid());

        Intent intentSubject = getIntent();
        subject = intentSubject.getStringExtra(Constants.SUBJECT);
        max  = intentSubject.getIntExtra("Riddle Size",0) - 1;
        textTitle.setText(subject);

        //Call
        GetBadges();
        GetStages();
        NextStage();

        btnShuffle.setOnClickListener(view -> {
            SoundPoolManager.playSound(0);
            doShuffle();
        });
        btnReset.setOnClickListener(view -> {
            SoundPoolManager.playSound(0);
            doReset();
        });
        btnBack.setOnClickListener(view -> {
            SoundPoolManager.playSound(0);
            AlertDialog.Builder alertExit = new AlertDialog.Builder(this);
            alertExit.setTitle("Exit Current Riddle")
                    .setMessage("Are you certain want to leave? \n" +
                            "\nNote: The game will not be saved!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        //Back to Menu Activity
                        overridePendingTransition(0, 0);
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                    });
            AlertDialog dialog = alertExit.create();
            dialog.show();
        });

        btnCoin.setOnClickListener(view -> {
            SoundPoolManager.playSound(0);
            AlertDialog.Builder alertExit = new AlertDialog.Builder(this);
            alertExit.setTitle("Coins!")
                    .setMessage("You can get some coins by solving this riddle!!\n " +
                            "\nYou have total earned on this stage: " +totalCoins+" coins")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setCancelable(false)
                    .setPositiveButton("Close", (dialog, which) -> {
                    });
            AlertDialog dialog = alertExit.create();
            dialog.show();
        });

        btnClue.setOnClickListener(view -> {
            SoundPoolManager.playSound(0);
            Toast.makeText(view.getContext(), "Under-development!", Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent(view.getContext(), ImageProcessActivity.class);
            view.getContext().startActivity(intent);*/
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if(status == TextToSpeech.SUCCESS){

                int result = textToSpeech.setLanguage(Locale.ENGLISH);

                if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED ){
                    Log.e("TTS","Language Not Supported");
                }
            }
        });

        btnPronoun.setOnClickListener(view -> {
            String text = answerWord;
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
            textToSpeech.setSpeechRate(0.5f);
        });
    }

    public int getRandomNumber(int max) {
        return (int) ((Math.random() * (max)) + 0);
    }

    @SuppressLint("SetTextI18n")
    private void addView(LinearLayout viewParent, final String text, final EditText editText) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        linearLayoutParams.rightMargin = 15;
        linearLayoutParams.leftMargin = 15;
        linearLayoutParams.width = 85;

        final TextView textView = new TextView(this);

        textView.setTypeface(ResourcesCompat.getFont(this, R.font.fredoka_one));
        textView.setLayoutParams(linearLayoutParams);
        textView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.text_background, getTheme()));
        textView.setTextColor(this.getResources().getColor(R.color.mediumPurple));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setElevation(6);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(32);

        textQuestion = findViewById(R.id.textQuestion);
        textScreen = findViewById(R.id.textScreen);

        textQuestion.setText(textDescription);

        textView.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if (presCounter < maxPresCounter) {
                if (presCounter == 0)
                    editText.setText("");

                editText.setText(editText.getText().toString() + text);
                textView.startAnimation(smallbigforth);
                textView.animate().alpha(0).setDuration(300);
                presCounter++;

                if (presCounter == maxPresCounter)
                    doValidate();
            }
        });
        viewParent.addView(textView);
    }

    private void GetBadges(){
        myRefBadge.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isBadge1 = (boolean) Objects.requireNonNull(snapshot.child("Badge1").getValue());
                isBadge2 = (boolean) Objects.requireNonNull(snapshot.child("Badge2").getValue());
                isBadge3 = (boolean) Objects.requireNonNull(snapshot.child("Badge3").getValue());
                isBadge4 = (boolean) Objects.requireNonNull(snapshot.child("Badge4").getValue());
                isBadge5 = (boolean) Objects.requireNonNull(snapshot.child("Badge5").getValue());
                isBadge6 = (boolean) Objects.requireNonNull(snapshot.child("Badge6").getValue());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }

    private void GetStages(){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        if (subject.equals(getString(R.string.four_five_letters)+
                " "+getString(R.string.stage_01))){
            getWordMap = FourLetterLibrary.getStageQuestions(this, getString(R.string.stage_01), max);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setLayoutParams(layoutParams);
        }
        if (subject.equals(getString(R.string.four_five_letters)+
                " "+getString(R.string.stage_02))){
            getWordMap = FourLetterLibrary.getStage2Questions(this, getString(R.string.stage_02), max);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setLayoutParams(layoutParams);
        }
        if (subject.equals(getString(R.string.six_seven_letters)+
                " "+getString(R.string.stage_01))) {
            getWordMap = SixLetterLibrary.getStageQuestions(this, getString(R.string.stage_01), max);
        }
        if (subject.equals(getString(R.string.six_seven_letters)+
                " "+getString(R.string.stage_02))) {
            getWordMap = SixLetterLibrary.getStage2Questions(this, getString(R.string.stage_02), max);
        }
        /*if (subject.equals(getString(R.string.eight_ten_letters)+
                " "+getString(R.string.stage_01))) {
            getWordMap = EightLetterLibrary.getStageQuestions(this, getString(R.string.stage_01), max);
        }*/

        description = new ArrayList<>(getWordMap.keySet());
        word = new ArrayList<>(getWordMap.values());

        long seed = System.nanoTime();
        Collections.shuffle(description, new Random(seed));
        Collections.shuffle(word, new Random(seed));

        for (Object o : description) {
            // Access keys/values in a random order
            getWordMap.get(o);
        }
        for (Object o : word) {
            // Access keys/values in a random order
            getWordMap.get(o);
        }
    }

    @SuppressLint("SetTextI18n")
    private void NextStage() {
        textAnswer = word.get(currentQuestionIndex);

        //randomizedCharacter1 = (char) (rnd.nextInt(26) + 'A');
        //randomizedCharacter2 = (char) (rnd.nextInt(26) + 'A');

        //String withRandomChar = textAnswer + randomizedCharacter1 + randomizedCharacter2;
        String withRandomChar = textAnswer;

        keys = withRandomChar.trim().toUpperCase().split("");
        textDescription = description.get(currentQuestionIndex);

        Collections.shuffle(Arrays.asList(keys));

        for (String key : keys) {
            addView(findViewById(R.id.layoutParent), key, findViewById(R.id.editText));
        }

        maxPresCounter = textAnswer.length();

        textScreen.setText("Question: " + (currentQuestionIndex + 1) + " / "+max);
    }

    private void doValidate() {
        presCounter = 0;

        if (editText.getText().toString().equals(textAnswer.trim().toUpperCase())) {
            currentQuestionIndex++;
            //Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();

            SoundPoolManager.playSound(2);

            answerWord = editText.getText().toString();
            editText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.goldenrod)));
            editText.setTextColor(this.getResources().getColor(R.color.white));

            new Handler().postDelayed(() -> {
                editText.setText("");
                editText.setBackgroundTintList(null);
                editText.setTextColor(this.getResources().getColor(R.color.mediumPurple));

                layoutNextStage.setVisibility(View.VISIBLE);
                showPopupWindow();

                linearLayout.setVisibility(View.GONE);
                btnCoin.setEnabled(false);
                btnReset.setEnabled(false);
                btnShuffle.setEnabled(false);
                btnBack.setEnabled(false);
            }, 1300);

        } else {
            editText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_red)));
            editText.setTextColor(this.getResources().getColor(R.color.white_smoke));

            //Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            SoundPoolManager.playSound(3);

            linearLayout.setVisibility(View.GONE);

            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            editText.startAnimation(shake);

            Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibe.vibrate(300);

            new Handler().postDelayed(() -> {
                editText.setText("");
                editText.setBackgroundTintList(null);
                editText.setTextColor(this.getResources().getColor(R.color.mediumPurple));
                linearLayout.removeAllViews();
                Collections.shuffle(Arrays.asList(keys));
                linearLayout.setVisibility(View.VISIBLE);
                for (String key : keys) {
                    addView(linearLayout, key, editText);
                }
            }, 1300);
        }
    }

    private void doShuffle() {
        presCounter = 0;
        editText.setText("");

        Collections.shuffle(Arrays.asList(keys));

        linearLayout.removeAllViews();
        for (String key : keys) {
            addView(linearLayout, key, editText);
        }

    }

    private void doReset() {
        presCounter = 0;
        editText.setText("");

        linearLayout.removeAllViews();
        for (String key : keys) {
            addView(linearLayout, key, editText);
        }

    }

    //Popup Window display method of word synonyms and next stage
    //@SuppressLint({"ClickableViewAccessibility", "InflateParams", "SetTextI18n"})
    @SuppressLint("SetTextI18n")
    private void showPopupWindow() {
        tvCoinsEarned = findViewById(R.id.textViewCoinsEarned);
        tvWord = findViewById(R.id.textViewCorrectWord);
        tvSynonym = findViewById(R.id.detail_translate_card_synonym);
        btnNextStage = findViewById(R.id.btn_nextStage);

        if (textAnswer.length() == 3){
            //stageCoin = intentSubject.getIntExtra("Stage Coins",0);
            stageCoin = 5;
            totalCoins += stageCoin;
        }
        if (textAnswer.length() == 4){
            stageCoin = 10;
            totalCoins += stageCoin;
        }
        if (textAnswer.length() == 5){
            stageCoin = 15;
            totalCoins += stageCoin;
        }
        if (textAnswer.length() == 6 | textAnswer.length() == 7){
            stageCoin = 25;
            totalCoins += stageCoin;
        }
        if (textAnswer.length() == 8 | textAnswer.length() == 9){
            stageCoin = 30;
            totalCoins += stageCoin;
        }

        tvWord.setText(answerWord);
        tvCoinsEarned.setText("You got "+ stageCoin +" coins");

        tvSynonym.setText(R.string.loading);

        requestApiButtonClickThesaurus();

        //Earn Badge
        EarnBadges();

        if (currentQuestionIndex == max) {
            btnNextStage.setText(getText(R.string.finish));
            tvCoinsEarned.setText("You got total of: "+ totalCoins +" coins earned!");

            int totalEarned = totalCoins;

            myRef.child("Coin").setValue(ServerValue.increment(totalEarned));
            myRef.child("TotalCoins").setValue(ServerValue.increment(totalEarned));
            myRef.child("Question").setValue(ServerValue.increment(currentQuestionIndex));

            if(isStage1Four & subject.equals(getString(R.string.four_five_letters)+
                    " "+getString(R.string.stage_01))){
                myRef.child("Stage").setValue(1);
            }
            if(isStage1Four & subject.equals(getString(R.string.four_five_letters)+
                    " "+getString(R.string.stage_02))){
                myRefStage.child("Stage1_Six").setValue(true);
                myRef.child("Stage").setValue(2);
            }

            if (isStage1Six & subject.equals(getString(R.string.six_seven_letters)+
                    " "+getString(R.string.stage_01))){
                myRef.child("Stage").setValue(1);
                myRef.child("LetterStage").setValue(getString(R.string.six_seven_letters));
            }
            if (isStage1Six & subject.equals(getString(R.string.six_seven_letters)+
                    " "+getString(R.string.stage_01))){
                myRefStage.child("Stage1_Eight").setValue(true);
                myRef.child("Stage").setValue(2);
            }
        }

        btnNextStage.setOnClickListener(view1 -> {
            SoundPoolManager.playSound(1);
            if (btnNextStage.getText().equals(getString(R.string.next))) {
                linearLayout.removeAllViews();
                NextStage();
                layoutNextStage.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                btnCoin.setEnabled(true);
                btnReset.setEnabled(true);
                btnShuffle.setEnabled(true);
                btnBack.setEnabled(true);
            } else {
                finish();
            }
        });
    }

    private void EarnBadges(){
        if (currentQuestionIndex == 5 & subject.equals(getString(R.string.four_five_letters)+
                " "+getString(R.string.stage_01)) & !isBadge3){
            myRefBadge.child("Badge3").setValue(true);
            myRef.child("Coin").setValue(ServerValue.increment(50));
            myRef.child("TotalCoins").setValue(ServerValue.increment(50));
        }
        if (currentQuestionIndex == 7 & subject.equals(getString(R.string.four_five_letters)+
                " "+getString(R.string.stage_02)) & !isBadge5){
            myRefBadge.child("Badge5").setValue(true);
            myRef.child("Coin").setValue(ServerValue.increment(50));
            myRef.child("TotalCoins").setValue(ServerValue.increment(50));
        }
        if (currentQuestionIndex == max & subject.equals(getString(R.string.four_five_letters)+
                " "+getString(R.string.stage_02)) & !isBadge4){
            myRefBadge.child("Badge4").setValue(true);
            myRef.child("Coin").setValue(ServerValue.increment(50));
            myRef.child("TotalCoins").setValue(ServerValue.increment(50));
        }

        if (currentQuestionIndex == 5 & subject.equals(getString(R.string.six_seven_letters)+
                " "+getString(R.string.stage_01)) & !isBadge6){
            myRefBadge.child("Badge6").setValue(true);
            myRef.child("Coin").setValue(ServerValue.increment(50));
            myRef.child("TotalCoins").setValue(ServerValue.increment(50));
        }
        if (currentQuestionIndex == 7 & subject.equals(getString(R.string.six_seven_letters)+
                " "+getString(R.string.stage_02)) & !isBadge1){
            myRefBadge.child("Badge1").setValue(true);
            myRef.child("Coin").setValue(ServerValue.increment(50));
            myRef.child("TotalCoins").setValue(ServerValue.increment(50));
        }
        if (currentQuestionIndex == max & subject.equals(getString(R.string.six_seven_letters)+
                " "+getString(R.string.stage_02)) & !isBadge2){
            myRefBadge.child("Badge2").setValue(true);
            myRef.child("Coin").setValue(ServerValue.increment(50));
            myRef.child("TotalCoins").setValue(ServerValue.increment(50));
        }

    }

    private String Synonyms() {
        final String word = answerWord;
        final String word_id = word.toLowerCase();
        return "https://words.bighugelabs.com/api/2/880d230daf6c17502d4d9bbd352019a3/" + word_id + "/json";
    }

    public void requestApiButtonClickThesaurus() {
        SynonymOnly synClass = new SynonymOnly(this, tvSynonym);
        urlThesauras = Synonyms();
        synClass.execute(urlThesauras);
    }

    @Override
    public void onBackPressed() {

    }
}