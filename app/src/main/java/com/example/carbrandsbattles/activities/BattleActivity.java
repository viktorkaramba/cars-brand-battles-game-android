package com.example.carbrandsbattles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.fragments.BattleInfoFragment;
import com.example.carbrandsbattles.fragments.UserBattleInfoFragment;

public class BattleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        ImageButton backImageButton = findViewById(R.id.backImageButton);
        FragmentContainerView fragmentContainerView = findViewById(R.id.fragmentContainerView);
        backImageButton.setOnClickListener(view -> goBattleList());
        if (getDate(savedInstanceState,"info").equals("no")) {
            UserBattleInfoFragment userBattleInfoFragment = new UserBattleInfoFragment();
            userBattleInfoFragment.setArguments(savedInstanceState);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, userBattleInfoFragment);
            fragmentTransaction.commit();
        }
        else {
            BattleInfoFragment battleInfoFragment = new BattleInfoFragment();
            battleInfoFragment.setArguments(savedInstanceState);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, battleInfoFragment);
            fragmentTransaction.commit();
        }
    }

    public String getDate(Bundle savedInstanceState, String key){
        String result;
        if (savedInstanceState == null) {
            Bundle extras = this.getIntent().getExtras();
            if(extras == null) {
                result= null;
            } else {
                result= extras.getString(key);
            }
        } else {
            result= (String) savedInstanceState.getSerializable(key);
        }
        return result;
    }

    private void goBattleList() {
        Intent intent = new Intent(this, BattlesListActivity.class);
        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
    }

}