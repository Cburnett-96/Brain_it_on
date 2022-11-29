package com.aqp.brainiton;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aqp.brainiton.other.SoundPoolManager;
import com.aqp.brainiton.other.Synonym;
import com.aqp.brainiton.other.WordLibrary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WordDayActivity extends AppCompatActivity {
    Button title, copy, speech, desc, btnBack;
    TextView synonym, antonym;

    String data1, data2;
    String urlThesauras;
    int max = 39;
    Map<String, String> getWordMap;
    List<String> word, description;

    TextToSpeech textToSpeech;

    SharedPreferences prefs;
    String todayString;
    Calendar calendar;
    int year, month, day;
    boolean currentday = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_day);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_purple));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.dark_purple));

        title = findViewById(R.id.detail_translate_title);
        desc = findViewById(R.id.detail_translate_desc);
        synonym = findViewById(R.id.detail_translate_card_synonym);
        antonym = findViewById(R.id.detail_translate_card_antonym);
        copy = findViewById(R.id.detail_translate_copy);
        speech = findViewById(R.id.detail_translate_speech);
        btnBack = findViewById(R.id.detail_back_button);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        todayString = year + "" + month + "" + day + "";
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        currentday = prefs.getBoolean(todayString, false);

        if (!currentday && isZoneAutomatic(this) && isTimeAutomatic(this)) {
            getDataFromSetWord();
        } else {
            getDataFromSavedDayPref();
        }
        setData();

        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if(status == TextToSpeech.SUCCESS){

                int result = textToSpeech.setLanguage(Locale.ENGLISH);

                if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED ){
                    Log.e("TTS","Language Not Supported");
                }else{
                    speech.setEnabled(true);
                }
            }
        });

        btnBack.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            finish();
        });

        desc.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("desc",desc.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(WordDayActivity.this,"Copied",Toast.LENGTH_SHORT).show();
        });

        speech.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            String text = title.getText().toString();
            String des = desc.getText().toString();
            textToSpeech.speak(text+". "+des, TextToSpeech.QUEUE_FLUSH,null);
            textToSpeech.setSpeechRate(0.7f);
        });

        copy.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("title",title.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(WordDayActivity.this,"Copied",Toast.LENGTH_SHORT).show();
        });

        title.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("title",title.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(WordDayActivity.this,"Copied",Toast.LENGTH_SHORT).show();
        });
        requestApiButtonClickThesaurus();
    }

    private void getDataFromSetWord(){
        getWordMap = WordLibrary.getRandomQuestions(max);

        description = new ArrayList<>(getWordMap.keySet());
        word = new ArrayList<>(getWordMap.values());

        int ranNum = getRandomNumber(max);
        data1 = word.get(ranNum);
        //data1 = "weak";
        data2 = description.get(ranNum);

        SharedPreferences.Editor edt = prefs.edit();
        edt.putBoolean(todayString, true);
        edt.putString("word",data1);
        edt.putString("desc",data2);
        edt.apply();
    }

    private void getDataFromSavedDayPref(){
        String word = prefs.getString("word", null);
        String desc = prefs.getString("desc", null);
        data1 = word;
        data2 = desc;
    }

    public int getRandomNumber(int max) {
        return (int) ((Math.random() * (max)) + 0);
    }

    private void setData(){
        title.setText(data1);
        desc.setText(data2);
        synonym.setText(R.string.loading);
        antonym.setText(R.string.loading);
    }

    private String dictionaryEntriesThe() {
        final String word = title.getText().toString();
        final String word_id = word.toLowerCase();
        return "https://words.bighugelabs.com/api/2/880d230daf6c17502d4d9bbd352019a3/"+word_id+"/json" ;
    }

    public void requestApiButtonClickThesaurus(){
        Synonym synClass = new Synonym(this,synonym,antonym);
        urlThesauras = dictionaryEntriesThe();
        synClass.execute(urlThesauras);
    }

    public static boolean isTimeAutomatic(Context c) {
        return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
    }

    public static boolean isZoneAutomatic(Context c) {
        return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) == 1;
    }

    @Override
    public void onBackPressed() {

    }
}