package com.aqp.brainiton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.aqp.brainiton.other.Constants;
import com.aqp.brainiton.other.SoundPoolManager;

public class PlayPopup {
    SharedPreferences prefs;
    boolean isStage1Four, isStage1Six, isStage1Eight;
    boolean isStage1Three, isStage2Three, isStage1six, isStage2six;

    LinearLayout layoutMode, layoutChoiceLetter, layout4_5ChoiceStage, layout6_7ChoiceStage, layout8_10ChoiceStage;
    Button btnFourFiveLetters, btnSixSevenLetters, btnEightTenLetters, btnStage1_4_5, btnStage2_4_5, buttonClose,
            btnStage1_6_7, btnStage2_6_7, btnStage1_8_10, btnStage2_8_10, btnRiddle, btnLearning;

    //PopupWindow display method
    public void showPopupWindow(final View view) {

        //Create a View object yourself through inflater
        view.getContext();
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup_play, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        isStage1Four = prefs.getBoolean("isStage1Four", false);
        isStage1Six = prefs.getBoolean("isStage1Six", false);
        isStage1Eight = prefs.getBoolean("isStage1Eight", false);

        isStage1Three = prefs.getBoolean(view.getContext().getString(R.string.four_five_letters) +
                " " + view.getContext().getString(R.string.stage_01), false);
        isStage2Three = prefs.getBoolean(view.getContext().getString(R.string.four_five_letters) +
                " " + view.getContext().getString(R.string.stage_02), false);
        isStage1six = prefs.getBoolean(view.getContext().getString(R.string.six_seven_letters) +
                " " + view.getContext().getString(R.string.stage_01), false);
        isStage2six = prefs.getBoolean(view.getContext().getString(R.string.six_seven_letters) +
                " " + view.getContext().getString(R.string.stage_02), false);

        layoutMode = popupView.findViewById(R.id.layoutChoiceMode);
        layoutChoiceLetter = popupView.findViewById(R.id.layoutChoiceLetter);
        layout4_5ChoiceStage = popupView.findViewById(R.id.layout4_5ChoiceStage);
        layout6_7ChoiceStage = popupView.findViewById(R.id.layout6_7ChoiceStage);
        layout8_10ChoiceStage = popupView.findViewById(R.id.layout8_10ChoiceStage);
        btnFourFiveLetters = popupView.findViewById(R.id.btn_FourFiveLetters);
        btnSixSevenLetters = popupView.findViewById(R.id.btn_SixSevenLetters);
        btnEightTenLetters = popupView.findViewById(R.id.btn_EightTenLetters);
        btnStage1_4_5 = popupView.findViewById(R.id.btn_Stage1_4_5);
        btnStage2_4_5 = popupView.findViewById(R.id.btn_Stage2_4_5);
        btnStage1_6_7 = popupView.findViewById(R.id.btn_Stage1_6_7);
        btnStage2_6_7 = popupView.findViewById(R.id.btn_Stage2_6_7);
        btnStage1_8_10 = popupView.findViewById(R.id.btn_Stage1_8_10);
        btnStage2_8_10 = popupView.findViewById(R.id.btn_Stage2_8_10);
        btnRiddle = popupView.findViewById(R.id.btn_Riddle);
        btnLearning = popupView.findViewById(R.id.btn_Learning);
        buttonClose = popupView.findViewById(R.id.closeButton);

        btnRiddle.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            layoutChoiceLetter.setVisibility(View.VISIBLE);
            layoutMode.setVisibility(View.GONE);
        });

        btnLearning.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            //Toast.makeText(view.getContext(), "Under-development!", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
            Intent intent = new Intent(v.getContext(), ImageProcessActivity.class);
            v.getContext().startActivity(intent);
        });

        btnFourFiveLetters.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            layoutChoiceLetter.setVisibility(View.GONE);
            layout4_5ChoiceStage.setVisibility(View.VISIBLE);
        });

        btnSixSevenLetters.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if (isStage1Six) {
                layoutChoiceLetter.setVisibility(View.GONE);
                layout6_7ChoiceStage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(view.getContext(), "You need to finish the riddle in 3 - 5 Letters Stage 2!", Toast.LENGTH_SHORT).show();
            }
        });
        btnEightTenLetters.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if (isStage1Eight) {
                layoutChoiceLetter.setVisibility(View.GONE);
                layout8_10ChoiceStage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(view.getContext(), "You need to finish the riddle in 6 - 9 Letters Stage 2!", Toast.LENGTH_SHORT).show();
            }
        });

        //THREE to FIVE LETTERS
        btnStage1_4_5.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if (isStage1Three){
                Toast.makeText(view.getContext(), "You Completed this Stage!", Toast.LENGTH_SHORT).show();
            } else {
                popupWindow.dismiss();
                Intent intent = new Intent(v.getContext(), PlayActivity.class);
                intent.putExtra(Constants.SUBJECT, v.getContext().getString(R.string.four_five_letters) +
                        " " + v.getContext().getString(R.string.stage_01));
                intent.putExtra("Riddle Size", 14);
                v.getContext().startActivity(intent);
            }
        });
        btnStage2_4_5.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if (!isStage1Three) {
                Toast.makeText(view.getContext(), "You Need to Complete Stage 1!", Toast.LENGTH_SHORT).show();
            } else {
                if (isStage2Three){
                    Toast.makeText(view.getContext(), "You Completed this Stage!", Toast.LENGTH_SHORT).show();
                } else {
                    popupWindow.dismiss();
                    Intent intent = new Intent(v.getContext(), PlayActivity.class);
                    intent.putExtra(Constants.SUBJECT, v.getContext().getString(R.string.four_five_letters) +
                            " " + v.getContext().getString(R.string.stage_02));
                    intent.putExtra("Riddle Size", 12);
                    v.getContext().startActivity(intent);
                }
            }
        });

        //SIX to SEVEN LETTERS
        btnStage1_6_7.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if (isStage1six){
                Toast.makeText(view.getContext(), "You Completed this Stage!", Toast.LENGTH_SHORT).show();
            } else {
                popupWindow.dismiss();
                Intent intent = new Intent(v.getContext(), PlayActivity.class);
                intent.putExtra(Constants.SUBJECT, v.getContext().getString(R.string.six_seven_letters) +
                        " " + v.getContext().getString(R.string.stage_01));
                intent.putExtra("Riddle Size", 14);
                v.getContext().startActivity(intent);
            }
        });
        btnStage2_6_7.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            if (!isStage1six) {
                Toast.makeText(view.getContext(), "You Need to Complete Stage 1!", Toast.LENGTH_SHORT).show();
            } else {
                if (isStage2six){
                    Toast.makeText(view.getContext(), "You Completed this Stage!", Toast.LENGTH_SHORT).show();
                } else {
                    popupWindow.dismiss();
                    Intent intent = new Intent(v.getContext(), PlayActivity.class);
                    intent.putExtra(Constants.SUBJECT, v.getContext().getString(R.string.six_seven_letters) +
                            " " + v.getContext().getString(R.string.stage_02));
                    intent.putExtra("Riddle Size", 12);
                    v.getContext().startActivity(intent);
                }
            }
        });

        //EIGHT to TEN LETTERS
        btnStage1_8_10.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            popupWindow.dismiss();
            Intent intent = new Intent(v.getContext(), PlayActivity.class);
            intent.putExtra(Constants.SUBJECT, v.getContext().getString(R.string.eight_ten_letters) +
                    " " + v.getContext().getString(R.string.stage_01));
            intent.putExtra("Riddle Size", 4);
            v.getContext().startActivity(intent);
        });
        btnStage2_8_10.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            Toast.makeText(view.getContext(), "Under-development!", Toast.LENGTH_SHORT).show();
        });

        buttonClose.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            popupWindow.dismiss();
        });
    }
}
