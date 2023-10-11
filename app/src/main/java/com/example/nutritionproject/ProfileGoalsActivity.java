package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;

import com.example.nutritionproject.databinding.ActivityProfileGoalsBinding;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileGoalsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener
{
    private ActivityProfileGoalsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileGoalsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
        updateGoals();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);
        TdeeActivity.onTDEEUserGoalUpdated.addListener(this, "updateGoals");

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.homeBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.tdeeBtn.setOnClickListener(this);
        binding.backBtn.setOnClickListener(this);
    }

    public void updateGoals()
    {
        binding.calorieResultLabelText.setText(String.valueOf(CurrentProfile.goals.calories) + " Calories");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        TdeeActivity.onTDEEUserGoalUpdated.removeListener(this, "updateCalories");
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.backBtn.getId())
        {
            finish();
        }
        else if (id == binding.tdeeBtn.getId())
        {
            startActivity(new Intent(ProfileGoalsActivity.this, TdeeActivity.class));
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        CustomUIMethods.setBottomNavBar(this, id, binding.bottomNavigationView, item);

        return false;
    }
}