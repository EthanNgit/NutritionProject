package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.CustomDBMethods.CurrentProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.CustomFitMethods;
import com.example.nutritionproject.Custom.java.CustomUIMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardHomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    //region References
    private BottomNavigationView bottomNavView;

    private final CustomUIMethods uiManager = new CustomUIMethods();
    private final CustomFitMethods fitManager = new CustomFitMethods();

    private CardView profileButton;
    private TextView profileButtonText;

    private Button getTdeeButton;

    private CardView firstTimeCalCard;
    private CardView dayCalorieProgressCard;


    //endregion

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_home);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

        profileButton = findViewById(R.id.profileButton);
        profileButtonText = findViewById(R.id.profileButtonText);

        bottomNavView = findViewById(R.id.bottomNavigationView);
        getTdeeButton = findViewById(R.id.takeCalorieEstimateButton);

        firstTimeCalCard = findViewById(R.id.firstTimeCalSetCard);
        dayCalorieProgressCard = findViewById(R.id.calProgressChartCard);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.homeBtn).setChecked(true);

        profileButton.setOnClickListener(this);

        bottomNavView.setOnItemSelectedListener(this);
        getTdeeButton.setOnClickListener(this);

        setProfileButton();
        showCalorieGraph();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == profileButton.getId()) {
            startActivity(new Intent(DashboardHomeActivity.this, ProfileActivity.class));
        }

        if (id == getTdeeButton.getId()) {
            startActivity(new Intent(DashboardHomeActivity.this, TdeeActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        uiManager.setBottomNavBar(this, id, bottomNavView, item);

        return false;

    }

    //endregion

    //region reusable Methods
    private void showCalorieGraph() {
        /*
        if (CurrentProfile.goals.calorieGoal == 0) {
            firstTimeCalCard.setVisibility(View.VISIBLE);
            dayCalorieProgressCard.setVisibility(View.GONE);
        } else {
            firstTimeCalCard.setVisibility(View.GONE);
            dayCalorieProgressCard.setVisibility(View.VISIBLE);
        }
        */

        firstTimeCalCard.setVisibility(View.VISIBLE);
        dayCalorieProgressCard.setVisibility(View.GONE);

        //TODO: Implement simple round progress bar ui
    }

    private void setProfileButton() {
        if (CurrentProfile.userColorHex == null) {
            CurrentProfile.userColorHex = CustomUIMethods.getRandomLightColorHex();
        }

        uiManager.setProfileButton(this, profileButton, profileButtonText);
    }

    //endregion
}