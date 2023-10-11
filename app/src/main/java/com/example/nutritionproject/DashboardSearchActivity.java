package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.databinding.ActivityDashboardHomeBinding;
import com.example.nutritionproject.databinding.ActivityDashboardSearchBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardSearchActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener
{
    private final CustomFitMethods fitManager = new CustomFitMethods();
    private ActivityDashboardSearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardSearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    public void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.searchBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.searchBtn.getId())
        {
            startActivity(new Intent(DashboardSearchActivity.this, SearchActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        CustomUIMethods.setBottomNavBar(this, id, binding.bottomNavigationView, item);

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        super.onPointerCaptureChanged(hasCapture);
    }
}