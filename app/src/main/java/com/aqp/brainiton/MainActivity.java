package com.aqp.brainiton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aqp.brainiton.adapter.RankingTopOneAdapter;
import com.aqp.brainiton.adapter.RankingTopTenAdapter;
import com.aqp.brainiton.adapter.RankingTopTwoThreeAdapter;
import com.aqp.brainiton.model.UserRankingOne;
import com.aqp.brainiton.model.UserRankingTen;
import com.aqp.brainiton.model.UserRankingTwoThree;
import com.aqp.brainiton.other.BackgroundSoundService;
import com.aqp.brainiton.other.SoundPoolManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private DatabaseReference databaseReference, uidRefAvatar, uidRanking, uidRefStages;
    private FirebaseUser currentUser;
    private TextView loginMessage;
    private SharedPreferences prefs;
    nl.joery.animatedbottombar.AnimatedBottomBar animatedBottomBar;
    RelativeLayout layoutRanking, layoutProfile, layoutShop, layoutSetting;
    LinearLayout layoutMain;
    Button btnPlay, btnBadge, btnWordDay;
    String username, avatar, totalCoins, stage, questions, letterStage;
    String avatarName;
    int coin;

    //Profile
    TextView tv_Username, tv_CurrentStage, tv_TotalQuestionAnswered, tv_CurrentCoin, tv_TotalCoins;
    Button btnEditAvatar, btnEditUsername, btnSaveUsername;
    EditText edtUsername;
    ImageView avatarImage;

    //Setting
    String music, soundEffect;
    Intent musicIntent;
    SwitchCompat switchMusic, switchSound;

    //Shop
    boolean isAvatar1, isAvatar2, isAvatar3, isAvatar4, isAvatar5, isAvatar6, isAvatar7, isAvatar8, isAvatar9,
            isAvatar10, isAvatar11, isAvatar12;
    MaterialButton btnAvatar1, btnAvatar2, btnAvatar3, btnAvatar4, btnAvatar5, btnAvatar6, btnAvatar7, btnAvatar8, btnAvatar9,
            btnAvatar10, btnAvatar11, btnAvatar12;
    TextView tvCoins;

    //Ranking
    RecyclerView recyclerViewTopOne, recyclerViewTop23, recyclerViewTopTen;
    private RankingTopOneAdapter adapterTop1;
    private RankingTopTwoThreeAdapter adapterTop23;
    private RankingTopTenAdapter adapterTopTen;
    private List<UserRankingOne> userList1;
    private List<UserRankingTwoThree> userList23;
    private List<UserRankingTen> userListTen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.indigo));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mediumPurple));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //initialization
        loginMessage = findViewById(R.id.loginMessage);
        animatedBottomBar = findViewById(R.id.bottom_bar);
        animatedBottomBar.selectTabById(R.id.tab_play, true);
        layoutMain = findViewById(R.id.layoutMain);
        layoutRanking = findViewById(R.id.layoutRanking);
        layoutProfile = findViewById(R.id.layoutProfile);
        layoutSetting = findViewById(R.id.layoutSetting);
        layoutShop = findViewById(R.id.layoutShop);
        tvCoins = findViewById(R.id.textViewCoins);
        btnPlay = findViewById(R.id.btn_play);
        btnBadge = findViewById(R.id.btn_badges);
        btnWordDay = findViewById(R.id.btn_wordOfTheDay);

        //saving and getting state on shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Methods of user auth and setting
        FirebaseAuth();
        UserSetting();

        btnPlay.setOnClickListener(view -> {
            SoundPoolManager.playSound(1);
            PlayPopup playPopup = new PlayPopup();
            playPopup.showPopupWindow(view);
        });
        btnBadge.setOnClickListener(view -> {
            SoundPoolManager.playSound(1);
            Intent intent = new Intent(MainActivity.this, BadgesActivity.class);
            startActivity(intent);
        });

        btnWordDay.setOnClickListener(view -> {
            SoundPoolManager.playSound(1);
            Intent intent = new Intent(MainActivity.this, WordDayActivity.class);
            startActivity(intent);
        });

        //Bottom Navigation on click function
        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                switch (animatedBottomBar.getSelectedIndex()){
                    case 0:
                        SoundPoolManager.playSound(1);
                        UserProfile();
                        GetAvatarInfo();
                        layoutProfile.setVisibility(View.VISIBLE);
                        layoutRanking.setVisibility(View.GONE);
                        layoutMain.setVisibility(View.GONE);
                        layoutShop.setVisibility(View.GONE);
                        layoutSetting.setVisibility(View.GONE);
                        break;
                    case 1:
                        UserRanking();
                        SoundPoolManager.playSound(1);
                        layoutRanking.setVisibility(View.VISIBLE);
                        layoutMain.setVisibility(View.GONE);
                        layoutProfile.setVisibility(View.GONE);
                        layoutShop.setVisibility(View.GONE);
                        layoutSetting.setVisibility(View.GONE);
                        break;
                    case 2:
                        SoundPoolManager.playSound(1);
                        layoutMain.setVisibility(View.VISIBLE);
                        layoutShop.setVisibility(View.GONE);
                        layoutSetting.setVisibility(View.GONE);
                        layoutRanking.setVisibility(View.GONE);
                        layoutProfile.setVisibility(View.GONE);
                        break;
                    case 3:
                        UserShop();
                        SoundPoolManager.playSound(1);
                        layoutShop.setVisibility(View.VISIBLE);
                        layoutSetting.setVisibility(View.GONE);
                        layoutRanking.setVisibility(View.GONE);
                        layoutProfile.setVisibility(View.GONE);
                        break;
                    case 4:
                        SoundPoolManager.playSound(1);
                        layoutSetting.setVisibility(View.VISIBLE);
                        layoutShop.setVisibility(View.GONE);
                        layoutRanking.setVisibility(View.GONE);
                        layoutProfile.setVisibility(View.GONE);
                        break;
                    default:
                        FirebaseAuth();
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });

    }

    //Get and Set Firebase auth instance and database value
    private void FirebaseAuth(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserData").child(currentUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String getUsername = Objects.requireNonNull(snapshot.child("Username").getValue()).toString();
                String getAvatar = Objects.requireNonNull(snapshot.child("Avatar").getValue()).toString();
                String getCoin = Objects.requireNonNull(snapshot.child("Coin").getValue()).toString();
                String getTotalCoins = Objects.requireNonNull(snapshot.child("TotalCoins").getValue()).toString();
                String getStage = Objects.requireNonNull(snapshot.child("Stage").getValue()).toString();
                String getQuestions = Objects.requireNonNull(snapshot.child("Question").getValue()).toString();
                String getLetterStage = Objects.requireNonNull(snapshot.child("LetterStage").getValue()).toString();

                username = getUsername;
                avatar = getAvatar;
                coin = Integer.parseInt(getCoin);
                totalCoins = getTotalCoins;
                stage = getStage;
                questions = getQuestions;
                letterStage = getLetterStage;

                loginMessage.setText("Welcome! "+getUsername);
                tvCoins.setText(String.valueOf(coin));

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username", getUsername);
                editor.putString("avatar", getAvatar);
                editor.putString("coin", getCoin);
                editor.putString("totalCoins", getTotalCoins);
                editor.putString("stage", getStage);
                editor.putString("questions", getQuestions);
                editor.apply();

                uidRanking = FirebaseDatabase.getInstance().getReference().child("UserRanking").child(currentUser.getUid());
                uidRanking.child("Username").setValue(getUsername);
                uidRanking.child("Riddle").setValue(Integer.parseInt(getQuestions));
                uidRanking.child("Avatar").setValue(getAvatar);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });

        uidRefAvatar = FirebaseDatabase.getInstance().getReference().child("UserAvatar").child(currentUser.getUid());
        uidRefAvatar.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean Avatar1 = (boolean) Objects.requireNonNull(snapshot.child("Avatar1").getValue());
                boolean Avatar2 = (boolean) Objects.requireNonNull(snapshot.child("Avatar2").getValue());
                boolean Avatar3 = (boolean) Objects.requireNonNull(snapshot.child("Avatar3").getValue());
                boolean Avatar4 = (boolean) Objects.requireNonNull(snapshot.child("Avatar4").getValue());
                boolean Avatar5 = (boolean) Objects.requireNonNull(snapshot.child("Avatar5").getValue());
                boolean Avatar6 = (boolean) Objects.requireNonNull(snapshot.child("Avatar6").getValue());
                boolean Avatar7 = (boolean) Objects.requireNonNull(snapshot.child("Avatar7").getValue());
                boolean Avatar8 = (boolean) Objects.requireNonNull(snapshot.child("Avatar8").getValue());
                boolean Avatar9 = (boolean) Objects.requireNonNull(snapshot.child("Avatar9").getValue());
                boolean Avatar10 = (boolean) Objects.requireNonNull(snapshot.child("Avatar10").getValue());
                boolean Avatar11 = (boolean) Objects.requireNonNull(snapshot.child("Avatar11").getValue());
                boolean Avatar12 = (boolean) Objects.requireNonNull(snapshot.child("Avatar12").getValue());

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isAvatar1", Avatar1);
                editor.putBoolean("isAvatar2", Avatar2);
                editor.putBoolean("isAvatar3", Avatar3);
                editor.putBoolean("isAvatar4", Avatar4);
                editor.putBoolean("isAvatar5", Avatar5);
                editor.putBoolean("isAvatar6", Avatar6);
                editor.putBoolean("isAvatar7", Avatar7);
                editor.putBoolean("isAvatar8", Avatar8);
                editor.putBoolean("isAvatar9", Avatar9);
                editor.putBoolean("isAvatar10", Avatar10);
                editor.putBoolean("isAvatar11", Avatar11);
                editor.putBoolean("isAvatar12", Avatar12);
                editor.apply();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });

        uidRefStages = FirebaseDatabase.getInstance().getReference().child("UserStages").child(currentUser.getUid());
        uidRefStages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean Stage1Four = (boolean) Objects.requireNonNull(snapshot.child("Stage1_Four").getValue());
                boolean Stage1Six = (boolean) Objects.requireNonNull(snapshot.child("Stage1_Six").getValue());
                boolean Stage1Eight = (boolean) Objects.requireNonNull(snapshot.child("Stage1_Eight").getValue());

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isStage1Four", Stage1Four);
                editor.putBoolean("isStage1Six", Stage1Six);
                editor.putBoolean("isStage1Eight", Stage1Eight);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }

    //User Profile Layout activity and methods
    @SuppressLint("SetTextI18n")
    private void UserProfile () {
        tv_CurrentCoin = findViewById(R.id.tvCoins);
        tv_CurrentStage = findViewById(R.id.tvStage);
        tv_Username = findViewById(R.id.tvUsername);
        tv_TotalCoins = findViewById(R.id.tvTotalCoins);
        tv_TotalQuestionAnswered = findViewById(R.id.tvTotalQuestion);
        btnEditAvatar = findViewById(R.id.buttonEditAvatar);
        btnEditUsername = findViewById(R.id.buttonEditUsername);
        btnSaveUsername = findViewById(R.id.buttonSaveUsername);
        avatarImage = findViewById(R.id.imageViewAvatar);
        edtUsername = findViewById(R.id.editTextUsername);

        avatarName = prefs.getString("avatar",null);

        tv_Username.setText(username);
        tv_CurrentStage.setText(stage+" ["+letterStage+"]");
        tv_TotalQuestionAnswered.setText(questions);
        tv_CurrentCoin.setText(String.valueOf(coin));
        tv_TotalCoins.setText(totalCoins);
        edtUsername.setHint(username);

        btnEditAvatar.setOnClickListener(view -> {
            SoundPoolManager.playSound(1);
            Intent intent = new Intent(MainActivity.this, AvatarActivity.class);
            startActivity(intent);
        });

        btnEditUsername.setOnClickListener(view -> {
            SoundPoolManager.playSound(1);
            btnEditUsername.setVisibility(View.GONE);
            tv_Username.setVisibility(View.GONE);
            edtUsername.setVisibility(View.VISIBLE);
            btnSaveUsername.setVisibility(View.VISIBLE);
        });

        btnSaveUsername.setOnClickListener(view -> {
            String getUsername = edtUsername.getText().toString().trim();

            if (TextUtils.isEmpty(getUsername)) {
                Toast.makeText(this, "Input Username", Toast.LENGTH_SHORT).show();
            } else {
                SoundPoolManager.playSound(0);
                btnEditUsername.setVisibility(View.VISIBLE);
                tv_Username.setVisibility(View.VISIBLE);
                edtUsername.setVisibility(View.GONE);
                btnSaveUsername.setVisibility(View.GONE);

                databaseReference.child("Username").setValue(getUsername);
                tv_Username.setText(getUsername);
                edtUsername.setHint(getUsername);

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtUsername.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
            edtUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);
        });
    }

    //Getting Avatar info and Apply to system
    private void GetAvatarInfo(){
        switch (avatarName) {
            case "avatar_1": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_1);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_2": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_2);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_3": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_3);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_4": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_4);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_5": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_5);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_6": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_6);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_7": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_7);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_8": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_8);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_9": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_9);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_10": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_10);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_11": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_11);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            case "avatar_12": {
                InputStream imageStream = getResources().openRawResource(R.raw.avatar_12);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
            default: {
                InputStream imageStream = getResources().openRawResource(R.raw.profile);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
        }
    }

    //User Setting Layout activity and methods
    private void UserSetting () {
        switchMusic = findViewById(R.id.switchMusic);
        switchSound = findViewById(R.id.switchSound);

        musicIntent = new Intent(this, BackgroundSoundService.class);

        music = prefs.getString("music setting",null);
        if (music.equals("on")){
            startService(musicIntent);
            switchMusic.setChecked(prefs.getBoolean("music value",true));
        }
        else if (music.equals("off")){
            stopService(musicIntent);
            switchMusic.setChecked(prefs.getBoolean("music value",false));
        }

        soundEffect = prefs.getString("sound setting",null);
        if (soundEffect.equals("on")){
            SoundPoolManager.instantiate(this);
            switchSound.setChecked(prefs.getBoolean("sound value",true));
        } else {
            switchSound.setChecked(prefs.getBoolean("sound value",false));
        }

        switchMusic.setOnClickListener(view -> {
            if (switchMusic.isChecked()) {
                SoundPoolManager.playSound(1);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("music value",true);
                editor.putString("music setting","on");
                editor.apply();
                switchMusic.setChecked(true);
                startService(musicIntent);
            } else {
                SoundPoolManager.playSound(0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("music value",false);
                editor.putString("music setting","off");
                editor.apply();
                switchMusic.setChecked(false);
                stopService(musicIntent);
            }
        });

        switchSound.setOnClickListener(view -> {
            if (switchSound.isChecked()) {
                SoundPoolManager.instantiate(getApplicationContext());
                SoundPoolManager.playSound(1);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("sound value",true);
                editor.putString("sound setting","on");
                editor.apply();
                switchSound.setChecked(true);
            } else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("sound value",false);
                editor.putString("sound setting","off");
                editor.apply();
                SoundPoolManager.stop();
                switchSound.setChecked(false);
            }
        });
    }

    //User Shop Layout activity and methods
    private void UserShop () {
        btnAvatar1 = findViewById(R.id.btn_buyAvatar1);
        btnAvatar2 = findViewById(R.id.btn_buyAvatar2);
        btnAvatar3 = findViewById(R.id.btn_buyAvatar3);
        btnAvatar4 = findViewById(R.id.btn_buyAvatar4);
        btnAvatar5 = findViewById(R.id.btn_buyAvatar5);
        btnAvatar6 = findViewById(R.id.btn_buyAvatar6);
        btnAvatar7 = findViewById(R.id.btn_buyAvatar7);
        btnAvatar8 = findViewById(R.id.btn_buyAvatar8);
        btnAvatar9 = findViewById(R.id.btn_buyAvatar9);
        btnAvatar10 = findViewById(R.id.btn_buyAvatar10);
        btnAvatar11 = findViewById(R.id.btn_buyAvatar11);
        btnAvatar12 = findViewById(R.id.btn_buyAvatar12);

        isAvatar1 = prefs.getBoolean("isAvatar1", false);
        isAvatar2 = prefs.getBoolean("isAvatar2", false);
        isAvatar3 = prefs.getBoolean("isAvatar3", false);
        isAvatar4 = prefs.getBoolean("isAvatar4", false);
        isAvatar5 = prefs.getBoolean("isAvatar5", false);
        isAvatar6 = prefs.getBoolean("isAvatar6", false);
        isAvatar7 = prefs.getBoolean("isAvatar7", false);
        isAvatar8 = prefs.getBoolean("isAvatar8", false);
        isAvatar9 = prefs.getBoolean("isAvatar9", false);
        isAvatar10 = prefs.getBoolean("isAvatar10", false);
        isAvatar11 = prefs.getBoolean("isAvatar11", false);
        isAvatar12 = prefs.getBoolean("isAvatar12", false);

        if(isAvatar1){
            btnAvatar1.setText(R.string.apply);
            btnAvatar1.setIconResource(R.drawable.ic_check);
            btnAvatar1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar1.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_1");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar1.setOnClickListener(view -> {
                if (isAvatar1) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_1");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar1").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar1.setText(R.string.apply);
                        btnAvatar1.setIconResource(R.drawable.ic_check);
                        isAvatar1 = true;
                        btnAvatar1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(isAvatar2){
            btnAvatar2.setText(R.string.apply);
            btnAvatar2.setIconResource(R.drawable.ic_check);
            btnAvatar2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar2.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_2");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar2.setOnClickListener(view -> {
                if (isAvatar2) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_2");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar2").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar2.setText(R.string.apply);
                        btnAvatar2.setIconResource(R.drawable.ic_check);
                        isAvatar2 = true;
                        btnAvatar2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAvatar3){
            btnAvatar3.setText(R.string.apply);
            btnAvatar3.setIconResource(R.drawable.ic_check);
            btnAvatar3.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar3.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_3");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar3.setOnClickListener(view -> {
                if (isAvatar3) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_3");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar3").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar3.setText(R.string.apply);
                        btnAvatar3.setIconResource(R.drawable.ic_check);
                        isAvatar3 = true;
                        btnAvatar3.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAvatar4){
            btnAvatar4.setText(R.string.apply);
            btnAvatar4.setIconResource(R.drawable.ic_check);
            btnAvatar4.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar4.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_4");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar4.setOnClickListener(view -> {
                if (isAvatar4) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_4");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar4").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar4.setText(R.string.apply);
                        btnAvatar4.setIconResource(R.drawable.ic_check);
                        isAvatar4 = true;
                        btnAvatar4.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAvatar5){
            btnAvatar5.setText(R.string.apply);
            btnAvatar5.setIconResource(R.drawable.ic_check);
            btnAvatar5.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar5.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_5");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar5.setOnClickListener(view -> {
                if (isAvatar5) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_5");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar5").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar5.setText(R.string.apply);
                        btnAvatar5.setIconResource(R.drawable.ic_check);
                        isAvatar5 = true;
                        btnAvatar5.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAvatar6){
            btnAvatar6.setText(R.string.apply);
            btnAvatar6.setIconResource(R.drawable.ic_check);
            btnAvatar6.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar6.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_6");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar6.setOnClickListener(view -> {
                if (isAvatar6) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_6");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar6").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar6.setText(R.string.apply);
                        btnAvatar6.setIconResource(R.drawable.ic_check);
                        isAvatar6 = true;
                        btnAvatar6.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAvatar7){
            btnAvatar7.setText(R.string.apply);
            btnAvatar7.setIconResource(R.drawable.ic_check);
            btnAvatar7.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar7.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_7");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar7.setOnClickListener(view -> {
                if (isAvatar7) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_7");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar7").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar7.setText(R.string.apply);
                        btnAvatar7.setIconResource(R.drawable.ic_check);
                        isAvatar7 = true;
                        btnAvatar7.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAvatar8){
            btnAvatar8.setText(R.string.apply);
            btnAvatar8.setIconResource(R.drawable.ic_check);
            btnAvatar8.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar8.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_8");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar8.setOnClickListener(view -> {
                if (isAvatar8) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_8");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar8").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar8.setText(R.string.apply);
                        btnAvatar8.setIconResource(R.drawable.ic_check);
                        isAvatar8 = true;
                        btnAvatar8.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAvatar9){
            btnAvatar9.setText(R.string.apply);
            btnAvatar9.setIconResource(R.drawable.ic_check);
            btnAvatar9.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar9.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_9");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar9.setOnClickListener(view -> {
                if (isAvatar9) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_9");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar9").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar9.setText(R.string.apply);
                        btnAvatar9.setIconResource(R.drawable.ic_check);
                        isAvatar9 = true;
                        btnAvatar9.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAvatar10){
            btnAvatar10.setText(R.string.apply);
            btnAvatar10.setIconResource(R.drawable.ic_check);
            btnAvatar10.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar10.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_10");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar10.setOnClickListener(view -> {
                if (isAvatar10) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_10");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar10").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar10.setText(R.string.apply);
                        btnAvatar10.setIconResource(R.drawable.ic_check);
                        isAvatar10 = true;
                        btnAvatar10.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAvatar11){
            btnAvatar11.setText(R.string.apply);
            btnAvatar11.setIconResource(R.drawable.ic_check);
            btnAvatar11.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar11.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_11");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar11.setOnClickListener(view -> {
                if (isAvatar11) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_11");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar11").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar11.setText(R.string.apply);
                        btnAvatar11.setIconResource(R.drawable.ic_check);
                        isAvatar11 = true;
                        btnAvatar11.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(isAvatar12){
            btnAvatar12.setText(R.string.apply);
            btnAvatar12.setIconResource(R.drawable.ic_check);
            btnAvatar12.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            btnAvatar12.setOnClickListener(view -> {
                SoundPoolManager.playSound(1);
                databaseReference.child("Avatar").setValue("avatar_12");
                Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnAvatar12.setOnClickListener(view -> {
                if (isAvatar12) {
                    SoundPoolManager.playSound(1);
                    databaseReference.child("Avatar").setValue("avatar_12");
                    Toast.makeText(this, "Successfully Applied!", Toast.LENGTH_SHORT).show();
                } else {
                    if (5000 <= coin) {
                        SoundPoolManager.playSound(1);
                        uidRefAvatar.child("Avatar12").setValue(true);
                        databaseReference.child("Coin").setValue(coin - 5000);
                        btnAvatar12.setText(R.string.apply);
                        btnAvatar12.setIconResource(R.drawable.ic_check);
                        isAvatar12 = true;
                        btnAvatar12.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
                    } else {
                        SoundPoolManager.playSound(0);
                        Toast.makeText(this, "You don't have enough coins to purchase this!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    //User Ranking Layout activity and methods
    private void UserRanking (){
        recyclerViewTopOne = findViewById(R.id.recyclerViewTopOne);
        recyclerViewTop23 = findViewById(R.id.recyclerViewTopTwoThree);
        recyclerViewTopTen = findViewById(R.id.recyclerViewTopTen);

        recyclerViewTopOne.setLayoutManager(new LinearLayoutManager(this));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerViewTop23.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewTopTen.setLayoutManager(new LinearLayoutManager(this));

        userList1 = new ArrayList<>();
        userList23 = new ArrayList<>();
        userListTen = new ArrayList<>();
        adapterTop1 = new RankingTopOneAdapter(this,userList1);
        adapterTop23 = new RankingTopTwoThreeAdapter(this, userList23);
        adapterTopTen = new RankingTopTenAdapter(this, userListTen);

        recyclerViewTopOne.setAdapter(adapterTop1);
        recyclerViewTop23.setAdapter(adapterTop23);
        recyclerViewTopTen.setAdapter(adapterTopTen);

        Query query = FirebaseDatabase.getInstance().getReference("UserRanking")
                .orderByChild("Riddle");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList1.clear();
                userList23.clear();
                userListTen.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserRankingOne user1 = snapshot.getValue(UserRankingOne.class);
                        UserRankingTwoThree user23 = snapshot.getValue(UserRankingTwoThree.class);
                        UserRankingTen userTen = snapshot.getValue(UserRankingTen.class);
                        userList1.add(user1);
                        userList23.add(user23);
                        userListTen.add(userTen);
                    }
                    adapterTop1.notifyDataSetChanged();
                    adapterTop23.notifyDataSetChanged();
                    adapterTopTen.notifyDataSetChanged();
                    Collections.reverse(userList1);
                    Collections.reverse(userList23);
                    Collections.reverse(userListTen);
                    adapterTop23.removeItem(0);
                    adapterTopTen.removeItem(0);
                    adapterTopTen.removeItem(0);
                    adapterTopTen.removeItem(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    //Double back to exit
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            finishAffinity();
            System.exit(0);
            super.onBackPressed();
        }
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}