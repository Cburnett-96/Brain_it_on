package com.aqp.brainiton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aqp.brainiton.other.SoundPoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.Objects;

public class AvatarActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, uidRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    SharedPreferences prefs;
    String editAvatarName;

    HorizontalScrollView horizontalScrollView_Avatar;
    ImageView changeAvatar, avatar1, avatar2, avatar3, avatar4, avatar5, avatar6,
            avatar7, avatar8;
    boolean isAvatar1, isAvatar2, isAvatar3, isAvatar4, isAvatar5, isAvatar6,
            isAvatar7, isAvatar8;
    Button btnSave, btnClose;
    TextView avatarName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_purple));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.dark_purple));

        horizontalScrollView_Avatar = findViewById(R.id.scrollView);
        btnSave = findViewById(R.id.saveButton);
        btnClose = findViewById(R.id.closeButton);
        changeAvatar = findViewById(R.id.changeAvatar);

        avatar1 = findViewById(R.id.avatar1);
        avatar2 = findViewById(R.id.avatar2);
        avatar3 = findViewById(R.id.avatar3);
        avatar4 = findViewById(R.id.avatar4);
        avatar5 = findViewById(R.id.avatar5);
        avatar6 = findViewById(R.id.avatar6);
        avatar7 = findViewById(R.id.avatar7);
        avatar8 = findViewById(R.id.avatar8);
        avatarName = findViewById(R.id.avatarName);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isAvatar1 = prefs.getBoolean("isAvatar1", false);
        isAvatar2 = prefs.getBoolean("isAvatar2", false);
        isAvatar3 = prefs.getBoolean("isAvatar3", false);
        isAvatar4 = prefs.getBoolean("isAvatar4", false);
        isAvatar5 = prefs.getBoolean("isAvatar5", false);
        isAvatar6 = prefs.getBoolean("isAvatar6", false);
        isAvatar7 = prefs.getBoolean("isAvatar7", false);
        isAvatar8 = prefs.getBoolean("isAvatar8", false);

        FirebaseAuth();

        editAvatarName = "default";

        avatar1.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_1);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_1";
            avatarName.setText("Avatar 1");

            if (isAvatar1) {
                btnSave.setText("Save");
                btnSave.setEnabled(true);
                btnSave.setTextColor(Color.BLACK);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(216,216,216)));
            }else{
                btnSave.setEnabled(false);
                btnSave.setTextColor(Color.WHITE);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                btnSave.setText("Purchase first at the avatar shop!");
            }

        });
        avatar2.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_2);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_2";
            avatarName.setText("Avatar 2");
            if (isAvatar2) {
                btnSave.setText("Save");
                btnSave.setEnabled(true);
                btnSave.setTextColor(Color.BLACK);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(216,216,216)));
            }else{
                btnSave.setEnabled(false);
                btnSave.setTextColor(Color.WHITE);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                btnSave.setText("Purchase first at the avatar shop!");
            }
        });
        avatar3.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_3);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_3";
            avatarName.setText("Avatar 3");
            if (isAvatar3) {
                btnSave.setText("Save");
                btnSave.setEnabled(true);
                btnSave.setTextColor(Color.BLACK);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(216,216,216)));
            }else{
                btnSave.setEnabled(false);
                btnSave.setTextColor(Color.WHITE);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                btnSave.setText("Purchase first at the avatar shop!");
            }
        });
        avatar4.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_4);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_4";
            avatarName.setText("Avatar 4");
            if (isAvatar4) {
                btnSave.setText("Save");
                btnSave.setEnabled(true);
                btnSave.setTextColor(Color.BLACK);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(216,216,216)));
            }else{
                btnSave.setEnabled(false);
                btnSave.setTextColor(Color.WHITE);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                btnSave.setText("Purchase first at the avatar shop!");
            }
        });
        avatar5.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_5);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_5";
            avatarName.setText("Avatar 5");
            if (isAvatar5) {
                btnSave.setText("Save");
                btnSave.setEnabled(true);
                btnSave.setTextColor(Color.BLACK);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(216,216,216)));
            }else{
                btnSave.setEnabled(false);
                btnSave.setTextColor(Color.WHITE);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                btnSave.setText("Purchase first at the avatar shop!");
            }
        });
        avatar6.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_6);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_6";
            avatarName.setText("Avatar 6");
            if (isAvatar6) {
                btnSave.setText("Save");
                btnSave.setEnabled(true);
                btnSave.setTextColor(Color.BLACK);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(216,216,216)));
            }else{
                btnSave.setEnabled(false);
                btnSave.setTextColor(Color.WHITE);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                btnSave.setText("Purchase first at the avatar shop!");
            }
        });
        avatar7.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_7);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_7";
            avatarName.setText("Avatar 7");
            if (isAvatar7) {
                btnSave.setText("Save");
                btnSave.setEnabled(true);
                btnSave.setTextColor(Color.BLACK);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(216,216,216)));
            }else{
                btnSave.setEnabled(false);
                btnSave.setTextColor(Color.WHITE);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                btnSave.setText("Purchase first at the avatar shop!");
            }
        });
        avatar8.setOnClickListener(v -> {
            InputStream imageStream = getResources().openRawResource(R.raw.avatar_8);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            changeAvatar.setImageBitmap(bitmap);
            editAvatarName = "avatar_8";
            avatarName.setText("Avatar 8");
            if (isAvatar8) {
                btnSave.setText("Save");
                btnSave.setEnabled(true);
                btnSave.setTextColor(Color.BLACK);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(216,216,216)));
            }else{
                btnSave.setEnabled(false);
                btnSave.setTextColor(Color.WHITE);
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                btnSave.setText("Purchase first at the avatar shop!");
            }
        });
        btnSave.setOnClickListener(v -> {
            SoundPoolManager.playSound(1);
            uidRef.child("Avatar").setValue(editAvatarName);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        btnClose.setOnClickListener(v -> {
            SoundPoolManager.playSound(0);
            finish();
        });
    }

    private void FirebaseAuth() {
        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uidRef = databaseReference.child("UserData").child(uid);
        user = mAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {

    }
}