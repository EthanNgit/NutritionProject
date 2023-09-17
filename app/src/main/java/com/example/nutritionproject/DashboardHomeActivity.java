package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Enums.FoodTag;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.FoodModel.FoodNutrition;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardHomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    //region References
    private BottomNavigationView bottomNavView;

    private final CustomDBMethods dbManager = new CustomDBMethods();
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

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

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


        dbManager.updateNutrition(CurrentProfile.id, 15, 1, 1, 1, String.valueOf(new LocalDate()), new EventCallback() {
            @Override
            public void onSuccess(@Nullable EventContext context) {
                Log.d("NORTH_", "Updated");
            }

            @Override
            public void onFailure(@Nullable EventContext context) {
                Log.d("NORTH_", "did not update");
            }
        });

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

        CustomUIMethods.setBottomNavBar(this, id, bottomNavView, item);

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
        CustomUIMethods.setProfileButton(this, profileButton, profileButtonText, CurrentProfile.email);
    }

    //endregion
}