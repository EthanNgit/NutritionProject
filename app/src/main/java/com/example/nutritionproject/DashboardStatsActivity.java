package com.example.nutritionproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Custom.SingleRowCalendar;
import com.example.nutritionproject.Custom.java.Custom.UI.RecyclerViewInterface;
import com.example.nutritionproject.Custom.java.Custom.UI.Widget.WidgetObject;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.databinding.ActivityDashboardStatsBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class DashboardStatsActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, RecyclerViewInterface
{
    private SingleRowCalendar calendar;
    private ActivityDashboardStatsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardStatsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.statsBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.settingsBtn.setOnClickListener(this);

        calendar = new SingleRowCalendar(binding.mainSingleRowCalendar, binding.monthLabel, binding.yearLabel, binding.leftBtn, binding.rightBtn, new EventCallback()
        {
            @Override
            public void onSuccess(@Nullable EventContext context)
            {
                Date date = (Date) context.getData();

                // TODO: use date to set the data
            }

            @Override
            public void onFailure(@Nullable EventContext context)
            {
                //
            }
        });

        handleWidget();
    }

    private void handleWidget()
    {
        // Create default layout
        // Check preferences for a saved layout
        // receive callbacks from the widget edit activity for changes to the order and widgets used
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->
            {
                Gson gson = new Gson();
                String jsonString = result.getData().getStringExtra("widgets");
                Type listType = new TypeToken<ArrayList<WidgetObject>>() {}.getType();
                if (jsonString != null)
                {
                    ArrayList<WidgetObject> newWidgets = gson.fromJson(jsonString, listType);

                    // Update widgets

                }
            });

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        CustomUIMethods.setBottomNavBar(this, null, binding.bottomNavigationView, item);

        return false;
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.settingsBtn.getId())
        {
            Intent intent = new Intent(DashboardStatsActivity.this, StatsEditWidgetActivity.class);

            activityResultLauncher.launch(intent);
        }
    }

    @Override
    public void onItemClick(int clickId, int position)
    {

    }
}