package com.aqp.brainiton;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aqp.brainiton.other.SoundPoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.Objects;

public class BadgesActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference uidBadges;
    FirebaseUser currentUser;
    SharedPreferences prefs;
    String username, avatarName, badgeName, badgeDescription, badgeImage;
    Button btnBack;
    TextView txtUsername, txtBadgeName, txtDescription;
    ImageView avatarImage, imageViewBadge, imageViewBadge1, imageViewBadge2, imageViewBadge3,
            imageViewBadge4, imageViewBadge5, imageViewBadge6;
    boolean isBadge1, isBadge2, isBadge3, isBadge4, isBadge5, isBadge6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_purple));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.dark_purple));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        btnBack = findViewById(R.id.backButton);
        txtUsername = findViewById(R.id.textViewUsername);
        avatarImage = findViewById(R.id.imageViewProfile);
        imageViewBadge1 = findViewById(R.id.imageViewBadge1);
        imageViewBadge2 = findViewById(R.id.imageViewBadge2);
        imageViewBadge3 = findViewById(R.id.imageViewBadge3);
        imageViewBadge4 = findViewById(R.id.imageViewBadge4);
        imageViewBadge5 = findViewById(R.id.imageViewBadge5);
        imageViewBadge6 = findViewById(R.id.imageViewBadge6);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = prefs.getString("username",null);
        avatarName = prefs.getString("avatar",null);

        txtUsername.setText(username);
        GetAvatarInfo();
        GetBadges();
        BadgeInfo();

        btnBack.setOnClickListener(view -> {
            SoundPoolManager.playSound(0);
            finish();
        });
    }

    private void GetBadges(){
        uidBadges = FirebaseDatabase.getInstance().getReference().child("UserBadge").child(currentUser.getUid());
        uidBadges.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isBadge1 = (boolean) Objects.requireNonNull(snapshot.child("Badge1").getValue());
                isBadge2 = (boolean) Objects.requireNonNull(snapshot.child("Badge2").getValue());
                isBadge3 = (boolean) Objects.requireNonNull(snapshot.child("Badge3").getValue());
                isBadge4 = (boolean) Objects.requireNonNull(snapshot.child("Badge4").getValue());
                isBadge5 = (boolean) Objects.requireNonNull(snapshot.child("Badge5").getValue());
                isBadge6 = (boolean) Objects.requireNonNull(snapshot.child("Badge6").getValue());

                if (isBadge1) {
                    imageViewBadge1.setImageTintList(null);
                }
                if (isBadge2) {
                    imageViewBadge2.setImageTintList(null);
                }
                if (isBadge3) {
                    imageViewBadge3.setImageTintList(null);
                }
                if (isBadge4) {
                    imageViewBadge4.setImageTintList(null);
                }
                if (isBadge5) {
                    imageViewBadge5.setImageTintList(null);
                }
                if (isBadge6) {
                    imageViewBadge6.setImageTintList(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // [START_EXCLUDE]
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }

    //Getting Badge Info and Apply to system
    private void BadgeInfo(){
        imageViewBadge1.setOnClickListener(view -> {
            badgeName = "Proficient";
            badgeDescription = "You can unlock this by answering at least 7 riddles on stage two (6-9 letters)";
            badgeImage = "Badge1";
            showPopupWindow(view);
        });
        imageViewBadge2.setOnClickListener(view -> {
            badgeName = "Expert";
            badgeDescription = "You can unlock this by completing stage two (6-9 letters)";
            badgeImage = "Badge2";
            showPopupWindow(view);
        });
        imageViewBadge3.setOnClickListener(view -> {
            badgeName = "Beginner";
            badgeDescription = "You can unlock this by answering first 5 riddles at stage one.";
            badgeImage = "Badge3";
            showPopupWindow(view);
        });
        imageViewBadge4.setOnClickListener(view -> {
            badgeName = "Master";
            badgeDescription = "You can unlock this by completing stage two (3-5 letters)";
            badgeImage = "Badge4";
            showPopupWindow(view);
        });
        imageViewBadge5.setOnClickListener(view -> {
            badgeName = "Elite";
            badgeDescription = "You can unlock this by answering at least 7 riddles on stage two (3-5 letters)";
            badgeImage = "Badge5";
            showPopupWindow(view);
        });
        imageViewBadge6.setOnClickListener(view -> {
            //Toast.makeText(BadgesActivity.this, "Badge 6!", Toast.LENGTH_SHORT).show();
            badgeName = "Competent";
            badgeDescription = "You can unlock this by answering at least 5 riddles on stage one (6-9 letters)";
            badgeImage = "Badge6";
            showPopupWindow(view);
        });
    }
    //Popup Window display method of badge info
    @SuppressLint({"ClickableViewAccessibility", "InflateParams"})
    public void showPopupWindow(final View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_badge, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        imageViewBadge = popupView.findViewById(R.id.imageViewBadge);
        txtBadgeName = popupView.findViewById(R.id.textViewBadgeName);
        txtDescription  = popupView.findViewById(R.id.textViewDescription);

        SoundPoolManager.playSound(1);

        if(badgeImage.equals("Badge1")){
            InputStream imageStream = getResources().openRawResource(R.raw.badge_1);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageViewBadge.setImageBitmap(bitmap);
            txtBadgeName.setText(badgeName);
            txtDescription.setText(badgeDescription);
            if (isBadge1){
                imageViewBadge.setImageTintList(null);
            }
        }
        if(badgeImage.equals("Badge2")){
            InputStream imageStream = getResources().openRawResource(R.raw.badge_2);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageViewBadge.setImageBitmap(bitmap);
            txtBadgeName.setText(badgeName);
            txtDescription.setText(badgeDescription);
            if (isBadge2){
                imageViewBadge.setImageTintList(null);
            }
        }
        if(badgeImage.equals("Badge3")){
            InputStream imageStream = getResources().openRawResource(R.raw.badge_3);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageViewBadge.setImageBitmap(bitmap);
            txtBadgeName.setText(badgeName);
            txtDescription.setText(badgeDescription);
            if (isBadge3){
                imageViewBadge.setImageTintList(null);
            }
        }
        if(badgeImage.equals("Badge4")){
            InputStream imageStream = getResources().openRawResource(R.raw.badge_4);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageViewBadge.setImageBitmap(bitmap);
            txtBadgeName.setText(badgeName);
            txtDescription.setText(badgeDescription);
            if (isBadge4){
                imageViewBadge.setImageTintList(null);
            }
        }
        if(badgeImage.equals("Badge5")){
            InputStream imageStream = getResources().openRawResource(R.raw.badge_5);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageViewBadge.setImageBitmap(bitmap);
            txtBadgeName.setText(badgeName);
            txtDescription.setText(badgeDescription);
            if (isBadge5){
                imageViewBadge.setImageTintList(null);
            }
        }
        if(badgeImage.equals("Badge6")){
            InputStream imageStream = getResources().openRawResource(R.raw.badge_6);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageViewBadge.setImageBitmap(bitmap);
            txtBadgeName.setText(badgeName);
            txtDescription.setText(badgeDescription);
            if (isBadge6){
                imageViewBadge.setImageTintList(null);
            }
        }

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener((v, event) -> {
            //Close the window when clicked
            popupWindow.dismiss();
            return true;
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
            default: {
                InputStream imageStream = getResources().openRawResource(R.raw.profile);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(bitmap);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}