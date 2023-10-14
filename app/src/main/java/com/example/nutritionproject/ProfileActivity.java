package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.Intent;
import android.view.MenuItem;
import android.os.Bundle;

import android.view.View;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.databinding.ActivityProfileBinding;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener
{
    private final CustomDBMethods dbManager = new CustomDBMethods();
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);
        TdeeActivity.onTDEEUserGoalUpdated.addListener(this, "updateCalories");

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.homeBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.backBtn.setOnClickListener(this);
        binding.goalsBtn.setOnClickListener(this);
        binding.logoutBtn.setOnClickListener(this);
        binding.detailsBtn.setOnClickListener(this);


        binding.emailTextLabel.setText(CurrentProfile.email);
        updateCalories();

        PackageManager manager = getPackageManager();
        PackageInfo info = null;

        try
        {
            info = manager.getPackageInfo(
                    getPackageName(), 0);

            String version = info.versionName;
            binding.versionTextLabel.setText(version);
        }
        catch (PackageManager.NameNotFoundException e) {
            binding.versionTextLabel.setText("1.0");
        }

        binding.logoutTextLabel.setText("You are currently logged in as " + CurrentProfile.email);
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

        if (id == binding.detailsBtn.getId())
        {
            startActivity(new Intent(ProfileActivity.this, ProfileDetailsActivity.class));
        }
        else if (id == binding.goalsBtn.getId())
        {
            startActivity(new Intent(ProfileActivity.this, ProfileGoalsActivity.class));
        }
        else if (id == binding.logoutBtn.getId())
        {
            dbManager.logout(this);

            startActivity(new Intent(ProfileActivity.this, IntroActivity.class));

            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        CustomUIMethods.setBottomNavBar(this, null, binding.bottomNavigationView, item);

        return false;
    }

    public void updateCalories()
    {
        binding.goalsTextLabel.setText(CurrentProfile.goals.calories + " Calories");
    }

}