package com.aqp.brainiton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    String textAnswer, textDescription;
    TextView textScreen, textQuestion, textTitle;
    Animation smallbigforth;
    Button btnShuffle, btnReset, btnBack, btnCoin;
    EditText editText;
    LinearLayout linearLayout, layoutNextStage;

    private int currentQuestionIndex = 0;
    int max;
    private Map<String, String> getWordMap;
    private List<String> word, description;
    char randomizedCharacter1, randomizedCharacter2;
    Random rnd = new Random();
    String urlThesauras;

    TextView tvCoinsEarned, tvWord, tvSynonym;
    Button btnNextStage;

    SharedPreferences prefs;
    boolean isStage1Four, isStage1Six, isStage1Eight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.indigo));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mediumPurple));

        editText = findViewById(R.id.editText);
        linearLayout = findViewById(R.id.layoutParent);
        layoutNextStage = findViewById(R.id.layoutNextStage);
        btnReset = findViewById(R.id.resetButton);
        btnShuffle = findViewById(R.id.shuffleButton);
        btnBack = findViewById(R.id.backButton);
        btnCoin = findViewById(R.id.coinButton);
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

        Intent intentSubject = getIntent();
        subject = intentSubject.getStringExtra(Constants.SUBJECT);
        max  = intentSubject.getIntExtra("Riddle Size",0) - 1;
        textTitle.setText(subject);
        stageCoin = intentSubject.getIntExtra("Stage Coins",0);

        //Call
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
                            "\nYou have total earned on this stage: " +(stageCoin*currentQuestionIndex)+" coins")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setCancelable(false)
                    .setPositiveButton("Close", (dialog, which) -> {
                    });
            AlertDialog dialog = alertExit.create();
            dialog.show();
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

    private void GetStages(){
        if (subject.equals(getString(R.string.four_five_letters)+
                " "+getString(R.string.stage_01))){
            getWordMap = FourLetterLibrary.getStageQuestions(this, getString(R.string.stage_01), max);
        }
        if (subject.equals(getString(R.string.six_seven_letters)+
                " "+getString(R.string.stage_01))) {
            getWordMap = SixLetterLibrary.getStageQuestions(this, getString(R.string.stage_01), max);
        }
        if (subject.equals(getString(R.string.eight_ten_letters)+
                " "+getString(R.string.stage_01))) {
            getWordMap = EightLetterLibrary.getStageQuestions(this, getString(R.string.stage_01), max);
        }

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

        randomizedCharacter1 = (char) (rnd.nextInt(26) + 'A');
        randomizedCharacter2 = (char) (rnd.nextInt(26) + 'A');

        String withRandomChar = textAnswer + randomizedCharacter1 + randomizedCharacter2;

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

        tvWord.setText(answerWord);
        tvCoinsEarned.setText("You got "+ stageCoin +" coins");

        tvSynonym.setText(R.string.loading);

        requestApiButtonClickThesaurus();

        if (currentQuestionIndex == max) {
            btnNextStage.setText(getText(R.string.finish));
            tvCoinsEarned.setText("You got total of: "+ (stageCoin*currentQuestionIndex) +" coins earned!");

            int totalEarned = stageCoin*currentQuestionIndex;

            myRef.child("Coin").setValue(ServerValue.increment(totalEarned));
            myRef.child("TotalCoins").setValue(ServerValue.increment(totalEarned));
            myRef.child("Question").setValue(ServerValue.increment(currentQuestionIndex));

            if(isStage1Four){
                myRefStage.child("Stage1_Six").setValue(true);
                myRef.child("Stage").setValue(ServerValue.increment(1));
            }
            if (isStage1Six){
                myRefStage.child("Stage1_Eight").setValue(true);
                myRef.child("Stage").setValue(ServerValue.increment(1));
            }
        }

        btnNextStage.setOnClickListener(view1 -> {
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