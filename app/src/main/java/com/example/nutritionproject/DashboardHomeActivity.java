package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Custom.UI.FoodListAdapter;
import com.example.nutritionproject.Custom.java.Custom.UI.MealListAdapter;
import com.example.nutritionproject.Custom.java.Custom.UI.RecyclerViewInterface;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.UserModel.UserProfile;
import com.example.nutritionproject.Custom.java.UserModel.UserProfileStaticRefOther;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.bastanfar.semicirclearcprogressbar.SemiCircleArcProgressBar;

public class DashboardHomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, RecyclerViewInterface {

    //region References
    private BottomNavigationView bottomNavView;

    private final CustomDBMethods dbManager = new CustomDBMethods();
    private final CustomFitMethods fitManager = new CustomFitMethods();

    private CardView profileButton;
    private TextView profileButtonText;

    private Button getTdeeButton;

    private CardView firstTimeCalCard;
    private CardView dayCalorieProgressCard;
    private SemiCircleArcProgressBar calProgressBar;
    private TextView currentCalorieText;
    private TextView currentGoalCalorieText;
    private TextView currentProteinText;
    private TextView currentProteinGoalText;
    private TextView currentCarbText;
    private TextView currentCarbGoalText;
    private TextView currentFatText;
    private TextView currentFatGoalText;


    private RecyclerView fullMealHistoryLayout;
    private LinearLayout emptyMealHistoryLayout;
    private Button addMealButton;
    private Button emptyAddMealButton;


    //endregion

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_home);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);
        dbManager.onUserCurrentNutritionUpdateSuccess.addListener(this, "checkCalorieProgress");
        UserProfileStaticRefOther.onUserMealHistoryUpdate.addListener(this, "updateMealHistory");

        //region Main find
        profileButton = findViewById(R.id.profileButton);
        profileButtonText = findViewById(R.id.profileButtonText);

        bottomNavView = findViewById(R.id.bottomNavigationView);
        //endregion
        //region Calories find
        getTdeeButton = findViewById(R.id.takeCalorieEstimateButton);
        firstTimeCalCard = findViewById(R.id.firstTimeCalSetCard);
        dayCalorieProgressCard = findViewById(R.id.calProgressChartCard);
        calProgressBar = findViewById(R.id.calProgressBar);
        currentCalorieText = findViewById(R.id.currentCalorieLabel);
        currentGoalCalorieText = findViewById(R.id.currentGoalCalorieLabel);
        currentProteinText = findViewById(R.id.protein_value);
        currentProteinGoalText = findViewById(R.id.total_protein_value);
        currentCarbText = findViewById(R.id.carb_value);
        currentCarbGoalText = findViewById(R.id.total_carb_value);
        currentFatText = findViewById(R.id.fat_value);
        currentFatGoalText = findViewById(R.id.total_fat_value);
        //endregion
        //region Meals find
        fullMealHistoryLayout = findViewById(R.id.mealPlannerRecyclerView);
        emptyMealHistoryLayout = findViewById(R.id.emptyMealPlannerLayout);
        addMealButton = findViewById(R.id.addMealButton);
        emptyAddMealButton = findViewById(R.id.emptyAddMealButton);
        //endregion

        //region Main setters
        bottomNavView.setItemIconTintList(null);
        bottomNavView.getMenu().findItem(R.id.homeBtn).setChecked(true);
        bottomNavView.setOnItemSelectedListener(this);
        profileButton.setOnClickListener(this);
        //endregion
        //region Calories setters
        getTdeeButton.setOnClickListener(this);
        //endregion
        //region Meals setters
        addMealButton.setOnClickListener(this);
        emptyAddMealButton.setOnClickListener(this);
        //endregion

        setHomeWidgets();

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

        if (id == addMealButton.getId() || id == emptyAddMealButton.getId()) {
            //bring to add meal screen. Search screen or something.
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
    private void setHomeWidgets() {
        // Set Data
        getFirstTimeData();

        // Recent meal history
        updateMealHistory();

        // Calorie progress
        checkCalorieProgress();

        // Profile Button
        setProfileButton();
    }

    private void updateMealRecyclerView(ArrayList<MealProfile> meals) {
        if (meals.size() > 0) {
            RecyclerViewInterface recyclerViewInterface = this;
            MealListAdapter adapter = new MealListAdapter(this, meals, recyclerViewInterface);
            fullMealHistoryLayout.setAdapter(adapter);
            fullMealHistoryLayout.setLayoutManager(new LinearLayoutManager(this));
        }
    }


    private void getFirstTimeData() {

        dbManager.updateNutrition(CurrentProfile.id, 0,0,0,0, UserProfileStaticRefOther.userMealHistory, String.valueOf(new LocalDate()), null);
        dbManager.getMeals(1, new EventCallback() {
            @Override
            public void onSuccess(@Nullable EventContext context) {
                ArrayList<MealProfile> meals = (ArrayList<MealProfile>) context.getData();

                if (!meals.isEmpty()) {
                    fullMealHistoryLayout.setVisibility(View.VISIBLE);
                    emptyMealHistoryLayout.setVisibility(View.GONE);
                }

                updateMealRecyclerView(meals);
            }

            @Override
            public void onFailure(@Nullable EventContext context) {
                emptyMealHistoryLayout.setVisibility(View.VISIBLE);
                fullMealHistoryLayout.setVisibility(View.GONE);
            }
        });


    }

    public void updateMealHistory() {
        //TODO: implement meal history searching and saving
        emptyMealHistoryLayout.setVisibility(View.VISIBLE);
        fullMealHistoryLayout.setVisibility(View.GONE);

        updateMealRecyclerView(UserProfileStaticRefOther.userMealHistory);
    }

    public void checkCalorieProgress() {
        //TODO: Implement simple round progress bar ui
        if (CurrentProfile.goals.calories == 0) {
            firstTimeCalCard.setVisibility(View.VISIBLE);
            dayCalorieProgressCard.setVisibility(View.GONE);
        } else {
            firstTimeCalCard.setVisibility(View.GONE);
            dayCalorieProgressCard.setVisibility(View.VISIBLE);

            double calPercent = ((double) CurrentProfile.currentMacros.calories / (double) CurrentProfile.goals.calories) * 100.0;
            calProgressBar.setPercent((int)calPercent);

            SpannableStringBuilder curCalBuilder = CustomUIMethods.getMultiColouredMacroText(this, CurrentProfile.currentMacros.calories, R.color.darkTheme_Brand, R.color.darkTheme_WhiteMed);
            currentCalorieText.setText(curCalBuilder, TextView.BufferType.SPANNABLE);
            currentGoalCalorieText.setText("of " + String.valueOf(CurrentProfile.goals.calories) + " kcal");

            SpannableStringBuilder curProBuilder = CustomUIMethods.getMultiColouredMacroText(this, CurrentProfile.currentMacros.proteins, R.color.darkTheme_Protein, R.color.darkTheme_WhiteMed);
            currentProteinText.setText(curProBuilder, TextView.BufferType.SPANNABLE);
            currentProteinGoalText.setText("of " + String.valueOf(CurrentProfile.goals.proteins) + " protein");

            SpannableStringBuilder curCarBuilder = CustomUIMethods.getMultiColouredMacroText(this, CurrentProfile.currentMacros.carbs, R.color.darkTheme_Carb, R.color.darkTheme_WhiteMed);
            currentCarbText.setText(curCarBuilder, TextView.BufferType.SPANNABLE);
            currentCarbGoalText.setText("of " + String.valueOf(CurrentProfile.goals.carbs) + " carbs");

            SpannableStringBuilder curFatBuilder = CustomUIMethods.getMultiColouredMacroText(this, CurrentProfile.currentMacros.fats, R.color.darkTheme_Fat, R.color.darkTheme_WhiteMed);
            currentFatText.setText(curFatBuilder, TextView.BufferType.SPANNABLE);
            currentFatGoalText.setText("of " + String.valueOf(CurrentProfile.goals.fats) + " fats");

        }

    }

    private void setProfileButton() {
        CustomUIMethods.setProfileButton(this, profileButton, CurrentProfile.userColorHex, profileButtonText, CurrentProfile.email);
    }

    @Override
    public void onItemClick(int position) {

    }


    //endregion

}