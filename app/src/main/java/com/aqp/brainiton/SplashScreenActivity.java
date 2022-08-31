package com.aqp.brainiton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ImageView banner, loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.indigo));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mediumPurple));

        //  Get Firebase auth instance
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        banner = findViewById(R.id.banner);
        loading = findViewById(R.id.loading);

        FirstBootCheckInternetConnection();
    }

    private void FirstBootCheckInternetConnection() {
        Glide.with(this)
                .load(R.raw.splash_screen_loading)
                .into(loading);

        if (mAuth.getCurrentUser() != null) {
            new Handler().postDelayed(() -> {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }, 3000);
        } else {
            if (!isNetworkAvailable()) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Brain It ON! Need Internet Connection Access")
                        .setMessage("Please Check Your Internet Connection")
                        .setCancelable(false)
                        .setPositiveButton("Try again!", (dialogInterface, i) -> FirstBootCheckInternetConnection())
                        .setNegativeButton("Close", (dialog, which) -> {
                            finishAffinity();
                            System.exit(0);
                        })
                        .show();
            } else {
                new Handler().postDelayed(() -> {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }, 5000);
            }
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                    return true;
                } else return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            }
        }
        return false;
    }
    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}