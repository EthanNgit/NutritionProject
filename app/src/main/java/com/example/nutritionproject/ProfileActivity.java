package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nutritionproject.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.CustomUIMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    private final CustomUIMethods uiManager = new CustomUIMethods();
    private final CustomFitMethods fitManager = new CustomFitMethods();
    private final CustomDBMethods dbManager = new CustomDBMethods();

    private BottomNavigationView bottomNavView;

    private ImageView backBtn;

    private CardView detailsButton;
    private TextView emailTextLabel;

    private CardView goalsButton;
    private TextView goalsTextLabel;

    private TextView versionTextLabel;

    private CardView logOutButton;
    private TextView logOutTextLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

        bottomNavView = findViewById(R.id.bottomNavigationView);

        backBtn = findViewById(R.id.backButton);

        detailsButton = findViewById(R.id.detailsButton);
        emailTextLabel = findViewById(R.id.emailTextLabel);

        goalsButton = findViewById(R.id.goalsButton);
        goalsTextLabel = findViewById(R.id.goalsTextLabel);

        versionTextLabel = findViewById(R.id.versionTextLabel);

        logOutButton = findViewById(R.id.logoutButton);
        logOutTextLabel = findViewById(R.id.logoutTextLabel);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.homeBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);

        backBtn.setOnClickListener(this);
        detailsButton.setOnClickListener(this);
        goalsButton.setOnClickListener(this);
        logOutButton.setOnClickListener(this);

        emailTextLabel.setText(CurrentProfile.email);
        goalsTextLabel.setText(CurrentProfile.goals.calorieGoal + " Calories");

        PackageManager manager = getPackageManager();
        PackageInfo info = null;

        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
            String version = info.versionName;
            versionTextLabel.setText(version);

        } catch (PackageManager.NameNotFoundException e) {
            versionTextLabel.setText("1.0");

        }

        logOutTextLabel.setText("You are currently logged in as " + CurrentProfile.email);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            finish();
        }

        if (id == detailsButton.getId()) {
            startActivity(new Intent(ProfileActivity.this, ProfileDetailsActivity.class));
        } else if (id == goalsButton.getId()) {
            startActivity(new Intent(ProfileActivity.this, ProfileGoalsActivity.class));
        } else if (id == logOutButton.getId()) {
            dbManager.logout(this);

            startActivity(new Intent(ProfileActivity.this, IntroActivity.class));
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        uiManager.uncheckAllNavItems(bottomNavView.getMenu());

        //TODO: Make easier to do this???

        if (id == R.id.homeBtn) {
            Log.d("NORTH_DASHBOARD", "Home button pressed " + item.isChecked());

            startActivity(new Intent(ProfileActivity.this, DashboardHomeActivity.class));
            finish();

            uiManager.checkNavItem(item);
        } else if (id == R.id.searchBtn) {
            Log.d("NORTH_DASHBOARD", "Search button pressed "+ item.isChecked());

            startActivity(new Intent(ProfileActivity.this, DashboardSearchActivity.class));
            finish();

            uiManager.checkNavItem(item);
        } else if (id == R.id.statsBtn) {
            Log.d("NORTH_DASHBOARD", "Stats button pressed " + item.isChecked());

            startActivity(new Intent(ProfileActivity.this, DashboardStatsActivity.class));
            finish();

            uiManager.checkNavItem(item);
        }

        return false;
    }


}