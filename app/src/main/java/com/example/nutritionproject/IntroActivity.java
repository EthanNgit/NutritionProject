package com.example.nutritionproject;


import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.UserModel.UserProfileStaticRefOther;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.databinding.ActivityIntroBinding;

import org.joda.time.LocalDate;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener
{
    private final CustomDBMethods dbManager = new CustomDBMethods();
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.loginBtn.setOnClickListener(this);
        binding.registerBtn.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);

        String rememberMeString = preferences.getString("rememberMe", "");
        String skipEmail = preferences.getString("email", "");
        String skipPassword = preferences.getString("password", "");

        if (rememberMeString.equals("true") && !skipEmail.isEmpty() && !skipPassword.isEmpty())
        {
            dbManager.login(skipEmail, skipPassword, new EventCallback()
            {
                @Override
                public void onSuccess(@Nullable EventContext context)
                {
                    getFirstTimeData();
                }

                @Override
                public void onFailure(@Nullable EventContext context)
                {
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class));

                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == R.id.loginBtn)
        {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        }
        else if (id == R.id.registerBtn)
        {
            startActivity(new Intent(IntroActivity.this, SignupActivity.class));
        }
    }

    private void getFirstTimeData()
    {
        dbManager.updateNutrition(CurrentProfile.id, 0, 0, 0, 0,
                null, String.valueOf(new LocalDate()), null, new EventCallback()
                {
                    @Override
                    public void onSuccess(@Nullable EventContext context)
                    {
                        startActivity(new Intent(IntroActivity.this, DashboardHomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(@Nullable EventContext context)
                    {
                        startActivity(new Intent(IntroActivity.this, DashboardHomeActivity.class));
                        finish();
                    }
                });
    }

}
