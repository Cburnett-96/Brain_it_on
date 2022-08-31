package com.aqp.brainiton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aqp.brainiton.model.UserAvatar;
import com.aqp.brainiton.model.UserBadge;
import com.aqp.brainiton.model.UserData;
import com.aqp.brainiton.model.UserStages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef, myRefAvatar, myRefBadge, myRefStages;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private ProgressBar progressBar;
    Button btnContinue;
    private EditText edtUsername;

    String avatar, letterStage;
    int coin, totalCoins, question, stage;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.indigo));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mediumPurple));

        //initialization
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        progressBar = findViewById(R.id.progressBar);
        btnContinue = findViewById(R.id.btn_continue);
        edtUsername = findViewById(R.id.editTextUsername);

        //starting items of the new user
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        question = 0;
        stage = 0;
        coin = 0;
        totalCoins = 0;
        avatar = "default";
        letterStage = "4-5 letters";

        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnContinue.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String username = edtUsername.getText().toString().trim();
            UserData userData = new UserData(username, avatar, letterStage, stage, question, coin, totalCoins);
            UserAvatar userAvatar = new UserAvatar(false, false, false, false, false, false, false, false, false, false, false, false);
            UserBadge userBadge = new UserBadge(false, false, false, false, false, false);
            UserStages userStages = new UserStages(true, false, false, false, false, false, false, false, false, false, false, false,false, false, false);
            if (!TextUtils.isEmpty(username)){
                mAuth.signInAnonymously().
                        addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);

                        myRef = database.getReference("UserData").child(Objects.requireNonNull(mAuth.getUid()));
                        myRef.setValue(userData);

                        myRefAvatar = database.getReference("UserAvatar").child(Objects.requireNonNull(mAuth.getUid()));
                        myRefAvatar.setValue(userAvatar);

                        myRefBadge = database.getReference("UserBadge").child(Objects.requireNonNull(mAuth.getUid()));
                        myRefBadge.setValue(userBadge);

                        myRefStages = database.getReference("UserStages").child(Objects.requireNonNull(mAuth.getUid()));
                        myRefStages.setValue(userStages);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("music setting", "on");
                        editor.putString("sound setting", "on");
                        editor.apply();

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                        .addOnFailureListener(e -> {
                            Log.e("TAG",e.getMessage());            //return error in logs
                        });
            } else {
                progressBar.setVisibility(View.GONE);          //invisible the progress bar
                Toast.makeText(this, "Please enter valid Username", Toast.LENGTH_SHORT).show();
            }

        });
    }
}