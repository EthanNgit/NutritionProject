package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.nutritionproject.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.CustomUIMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardHomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    //region References
    private BottomNavigationView bottomNavView;

    private final CustomUIMethods uiManager = new CustomUIMethods();
    private final CustomFitMethods fitManager = new CustomFitMethods();

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

        bottomNavView = findViewById(R.id.bottomNavigationView);
        getTdeeButton = findViewById(R.id.takeCalorieEstimateButton);

        firstTimeCalCard = findViewById(R.id.firstTimeCalSetCard);
        dayCalorieProgressCard = findViewById(R.id.calProgressChartCard);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.homeBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);
        getTdeeButton.setOnClickListener(this);

        /*
        if (CurrentProfile.goals.calorieGoal == 0) {
            //TODO: Extract to separate Method???

            firstTimeCalCard.setVisibility(View.VISIBLE);
            dayCalorieProgressCard.setVisibility(View.GONE);
        } else {
            showCalorieGraph();
        }
        */
        firstTimeCalCard.setVisibility(View.VISIBLE);
        dayCalorieProgressCard.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.takeCalorieEstimateButton) {
            startActivity(new Intent(DashboardHomeActivity.this, TdeeActivity.class));
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
            uiManager.checkNavItem(item);
        } else if (id == R.id.searchBtn) {
            Log.d("NORTH_DASHBOARD", "Search button pressed "+ item.isChecked());

            startActivity(new Intent(DashboardHomeActivity.this, DashboardSearchActivity.class));
            finish();

            uiManager.checkNavItem(item);
        } else if (id == R.id.statsBtn) {
            Log.d("NORTH_DASHBOARD", "Stats button pressed " + item.isChecked());

            startActivity(new Intent(DashboardHomeActivity.this, DashboardStatsActivity.class));
            finish();

            uiManager.checkNavItem(item);
        }
        return false;

    }

    //endregion

    //region reusable Methods
    private void showCalorieGraph() {
        firstTimeCalCard.setVisibility(View.GONE);
        dayCalorieProgressCard.setVisibility(View.VISIBLE);

        //TODO: Implement simple round progress bar ui
    }
    //endregion
}