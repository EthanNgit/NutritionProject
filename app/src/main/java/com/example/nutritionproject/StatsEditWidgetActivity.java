package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Custom.UI.MealListAdapter;
import com.example.nutritionproject.Custom.java.Custom.UI.RecyclerViewInterface;
import com.example.nutritionproject.Custom.java.Custom.UI.Widget.Widget;
import com.example.nutritionproject.Custom.java.Custom.UI.Widget.WidgetObject;
import com.example.nutritionproject.Custom.java.Custom.UI.WidgetListAdapter;
import com.example.nutritionproject.Custom.java.Enums.ActivityResultCodes;
import com.example.nutritionproject.databinding.ActivityDashboardHomeBinding;
import com.example.nutritionproject.databinding.ActivityStatsEditWidgetBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StatsEditWidgetActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, RecyclerViewInterface
{
    private ArrayList<WidgetObject> widgets = new ArrayList<>();
    WidgetListAdapter adapter;
    private ActivityStatsEditWidgetBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsEditWidgetBinding.inflate(getLayoutInflater());
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

        binding.backBtn.setOnClickListener(this);

        handleWidgets();
    }

    private void handleWidgets()
    {
        String jsonString = getIntent().getStringExtra("widgets");
        Type listType = new TypeToken<ArrayList<WidgetObject>>() {}.getType();
        Gson gson = new Gson();

        if (jsonString == null) finish();

        widgets = gson.fromJson(jsonString, listType);

        updateWidgets();
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.backBtn.getId())
        {
            Intent intent = new Intent();
            Gson gson = new Gson();

            intent.putExtra("widgets", gson.toJson(widgets));
            setResult(ActivityResultCodes.StatsEditWidget.getCode(), intent);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        return false;
    }

    @Override
    public void onItemClick(int clickId, int position)
    {
        WidgetObject currentWidget = widgets.get(position);

        if (clickId == 0)
        {
            if (!currentWidget.isEnabled)
            {
                widgets.get(position).isEnabled = true;
            }
        }
        else if (clickId == 1)
        {
            if (currentWidget.isEnabled)
            {
                widgets.get(position).isEnabled = false;
            }
        }

        updateWidgets();
    }

    private void updateWidgets()
    {
        if (widgets.size() > 0)
        {
            RecyclerViewInterface recyclerViewInterface = this;
            adapter = new WidgetListAdapter(this, widgets, recyclerViewInterface);

            binding.widgetRecyclerView.setAdapter(adapter);
            binding.widgetRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}