package com.example.carbrandsbattles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.services.UserInfoService;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        UserInfoService.init(WelcomeActivity.this);
        String userAccessToken = UserInfoService.getProperty("access_token");
        if(userAccessToken == null){
            goLoginActivity();
        }
        else {
            goBattleListActivity();
        }
    }

    private void goBattleListActivity() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(WelcomeActivity.this, BattlesListActivity.class);
            startActivity(intent);
        }, 3000);
    }

    private void goLoginActivity() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }, 3000);
    }
}