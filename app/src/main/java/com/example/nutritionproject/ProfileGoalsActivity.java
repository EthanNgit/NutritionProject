package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileGoalsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    private BottomNavigationView bottomNavView;

    private ImageView backBtn;

    private Button tdeeButton;
    private TextView calorieLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_goals);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);
        TdeeActivity.onTDEEUserGoalUpdated.addListener(this, "updateGoals");

        backBtn = findViewById(R.id.backButton);

        bottomNavView = findViewById(R.id.bottomNavigationView);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.homeBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);

        backBtn.setOnClickListener(this);

        tdeeButton = findViewById(R.id.takeCalorieEstimateButton);
        calorieLabel = findViewById(R.id.calorieResultLabelText);

        tdeeButton.setOnClickListener(this);

        updateGoals();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TdeeActivity.onTDEEUserGoalUpdated.removeListener(this, "updateCalories");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            finish();
        } else if (id == tdeeButton.getId()) {
            startActivity(new Intent(ProfileGoalsActivity.this, TdeeActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        CustomUIMethods.setBottomNavBar(this, id, bottomNavView, item);

        return false;
    }

    public void updateGoals() {
        //set graph etc
        calorieLabel.setText(String.valueOf(CurrentProfile.goals.calories) + " Calories");
    }

}