package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.nutritionproject.Custom.java.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardStatsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavView;

    private final CustomFitMethods fitManager = new CustomFitMethods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_stats);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        bottomNavView = findViewById(R.id.bottomNavigationView);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.statsBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        CustomUIMethods.setBottomNavBar(this, id, bottomNavView, item);

        return false;
    }
}