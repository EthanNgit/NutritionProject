package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.joda.time.LocalDate;

import me.bastanfar.semicirclearcprogressbar.SemiCircleArcProgressBar;

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
    private SemiCircleArcProgressBar calProgressBar;
    private TextView currentCalorieText;
    private TextView currentGoalCalorieText;

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
        TdeeActivity.onTDEEUserGoalUpdated.addListener(this, "checkCalorieProgress");

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

        setProfileButton();
        setHomeWidgets();

        dbManager.updateNutrition(CurrentProfile.id, 0, 0, 0, 0, String.valueOf(new LocalDate()), new EventCallback() {
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
        //Calorie progress
        checkCalorieProgress();

        //Recent meal history
        updateMealHistory();

    }

    private void updateMealHistory() {
        //TODO: implement meal history searching and saving
        emptyMealHistoryLayout.setVisibility(View.VISIBLE);
        fullMealHistoryLayout.setVisibility(View.GONE);
    }

    private void checkCalorieProgress() {
        //TODO: Implement simple round progress bar ui
        if (CurrentProfile.goals.calories == 0) {
            firstTimeCalCard.setVisibility(View.VISIBLE);
            dayCalorieProgressCard.setVisibility(View.GONE);
        } else {
            firstTimeCalCard.setVisibility(View.GONE);
            dayCalorieProgressCard.setVisibility(View.VISIBLE);

            double calPercent = ((double) CurrentProfile.currentMacros.calories / (double) CurrentProfile.goals.calories) * 100.0;
            calProgressBar.setPercent((int)calPercent);

            SpannableStringBuilder curCalBuilder = new SpannableStringBuilder();
            String kcalAmtString = String.valueOf(CurrentProfile.currentMacros.calories);
            String kcalString = " kcal";
            SpannableString kcalAmtSpannable = new SpannableString(kcalAmtString);
            kcalAmtSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkTheme_Brand)), 0, kcalAmtString.length(), 0);
            SpannableString kcalSpannable = new SpannableString(kcalString);
            kcalSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkTheme_WhiteFull)), 0, kcalString.length(), 0);
            curCalBuilder.append(kcalAmtSpannable);
            curCalBuilder.append(kcalSpannable);

            currentCalorieText.setText(curCalBuilder, TextView.BufferType.SPANNABLE);
            currentGoalCalorieText.setText("of " + String.valueOf(CurrentProfile.goals.calories) + " kcal");
        }

    }

    private void setProfileButton() {
        CustomUIMethods.setProfileButton(this, profileButton, CurrentProfile.userColorHex, profileButtonText, CurrentProfile.email);
    }



    //endregion
}