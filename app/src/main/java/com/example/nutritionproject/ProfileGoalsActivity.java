package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomStatsMethods;
import com.example.nutritionproject.Custom.java.UserModel.UserProfile;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
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
        int calorieGoal = CurrentProfile.goals.calories;
        int proteinGoal = CurrentProfile.goals.proteins;
        int carbsGoal = CurrentProfile.goals.carbs;
        int fatsGoal = CurrentProfile.goals.fats;

        int calorieColor = R.color.darkTheme_Brand;
        int proteinColor = R.color.darkTheme_Protein;
        int carbsColor = R.color.darkTheme_Carb;
        int fatsColor = R.color.darkTheme_Fat;

        String[] labels = {"Protein", "Carbs", "Fats"};
        int[] values = {proteinGoal * 4, carbsGoal * 4, fatsGoal * 9};
        int[] colors = {proteinColor, carbsColor, fatsColor};

        CustomStatsMethods.fillPieChart(this, binding.macroPieChart, labels, values, colors);

        SpannableStringBuilder curCalBuilder = CustomUIMethods.getMultiColouredMacroText(this, calorieGoal, " Calories", R.color.darkTheme_Brand, R.color.darkTheme_WhiteMed);
        binding.calorieValue.setText(curCalBuilder, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder curProBuilder = CustomUIMethods.getMultiColouredMacroText(this, proteinGoal, "g Protein", R.color.darkTheme_Protein, R.color.darkTheme_WhiteMed);
        binding.proteinValue.setText(curProBuilder, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder curCarBuilder = CustomUIMethods.getMultiColouredMacroText(this, carbsGoal, "g Carbs", R.color.darkTheme_Carb, R.color.darkTheme_WhiteMed);
        binding.carbValue.setText(curCarBuilder, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder curFatBuilder = CustomUIMethods.getMultiColouredMacroText(this, fatsGoal, "g Fats", R.color.darkTheme_Fat, R.color.darkTheme_WhiteMed);
        binding.fatValue.setText(curFatBuilder, TextView.BufferType.SPANNABLE);
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
        CustomUIMethods.setBottomNavBar(this, null, binding.bottomNavigationView, item);

        return false;
    }
}