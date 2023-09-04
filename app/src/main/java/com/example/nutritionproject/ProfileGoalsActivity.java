package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileGoalsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    private BottomNavigationView bottomNavView;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_goals);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        backBtn = findViewById(R.id.backButton);

        bottomNavView = findViewById(R.id.bottomNavigationView);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.homeBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);

        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        CustomUIMethods.setBottomNavBar(this, id, bottomNavView, item);

        return false;
    }

}