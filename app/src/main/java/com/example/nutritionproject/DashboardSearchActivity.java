package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardSearchActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    private BottomNavigationView bottomNavView;

    private final CustomUIMethods uiManager = new CustomUIMethods();
    private final CustomFitMethods fitManager = new CustomFitMethods();

    private TextView searchButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_search);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

        bottomNavView = findViewById(R.id.bottomNavigationView);

        searchButton = findViewById(R.id.searchBtn);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.searchBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);

        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == searchButton.getId()) {
            startActivity(new Intent(DashboardSearchActivity.this, SearchActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        uiManager.setBottomNavBar(this, id, bottomNavView, item);

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}