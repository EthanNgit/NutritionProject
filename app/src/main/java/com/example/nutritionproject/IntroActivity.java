package com.example.nutritionproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nutritionproject.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.CustomUIMethods;
import com.google.gson.Gson;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener{

    private CustomUIMethods uiManager = new CustomUIMethods();
    private CustomDBMethods dbManager = new CustomDBMethods();
    private Button loginBtn;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_intro);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

        loginBtn = findViewById(R.id.introLoginButton);
        signUpBtn = findViewById(R.id.introSignUpButton);

        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);

        //preferences.edit().clear().commit();

        String rememberMeString = preferences.getString("rememberMe", "");

        //TODO: potential bad practice, not sure if it matters since its on device pref only...
        String skipEmail = preferences.getString("email", "");
        String skipPassword = preferences.getString("password", "");

        if (rememberMeString.equals("true") && !skipEmail.isEmpty() && !skipPassword.isEmpty()) {
            //TODO: Update to saving userprofile and authenticating though there, when profiles are needed
            Gson gson = new Gson();

            dbManager.login(skipEmail, skipPassword);

            startActivity(new Intent(IntroActivity.this, DashboardHomeActivity.class));
            finish();
        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.introLoginButton) {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        } else if (id == R.id.introSignUpButton) {
            startActivity(new Intent(IntroActivity.this, SignupActivity.class));
        }
    }

}
