package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.databinding.ActivityAddFoodItemBinding;
import com.example.nutritionproject.databinding.ActivityProfileDetailsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileDetailsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener
{
    private ActivityProfileDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.homeBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.backBtn.getId())
        {
            finish();
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