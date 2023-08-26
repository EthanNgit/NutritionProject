package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.nutritionproject.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.CustomUIMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardSearchActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavView;

    private final CustomUIMethods uiManager = new CustomUIMethods();
    private final CustomFitMethods fitManager = new CustomFitMethods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_search);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

        bottomNavView = findViewById(R.id.bottomNavigationView);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.searchBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        uiManager.uncheckAllNavItems(bottomNavView.getMenu());

        //TODO: Implement other Dashboard pages
        //TODO: Make easier to do this???

        if (id == R.id.homeBtn) {
            Log.d("NORTH_DASHBOARD", "Home button pressed " + item.isChecked());

            startActivity(new Intent(DashboardSearchActivity.this, DashboardHomeActivity.class));
            finish();

            uiManager.checkNavItem(item);
        } else if (id == R.id.searchBtn) {
            Log.d("NORTH_DASHBOARD", "Search button pressed "+ item.isChecked());
            uiManager.checkNavItem(item);
        } else if (id == R.id.statsBtn) {
            Log.d("NORTH_DASHBOARD", "Stats button pressed " + item.isChecked());

            startActivity(new Intent(DashboardSearchActivity.this, DashboardStatsActivity.class));
            finish();

            uiManager.checkNavItem(item);
        }

        return false;
    }
}