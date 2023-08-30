package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.nutritionproject.Custom.CustomUIMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileDetailsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    private final CustomUIMethods uiManager = new CustomUIMethods();

    private BottomNavigationView bottomNavView;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

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

        uiManager.uncheckAllNavItems(bottomNavView.getMenu());

        //TODO: Implement other Dashboard pages
        //TODO: Make easier to do this???

        if (id == R.id.homeBtn) {
            Log.d("NORTH_DASHBOARD", "Home button pressed " + item.isChecked());

            startActivity(new Intent(ProfileDetailsActivity.this, DashboardHomeActivity.class));
            finish();

            uiManager.checkNavItem(item);
        } else if (id == R.id.searchBtn) {
            Log.d("NORTH_DASHBOARD", "Search button pressed "+ item.isChecked());

            startActivity(new Intent(ProfileDetailsActivity.this, DashboardSearchActivity.class));
            finish();

            uiManager.checkNavItem(item);
        } else if (id == R.id.statsBtn) {
            Log.d("NORTH_DASHBOARD", "Stats button pressed " + item.isChecked());

            startActivity(new Intent(ProfileDetailsActivity.this, DashboardStatsActivity.class));
            finish();

            uiManager.checkNavItem(item);
        }

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}