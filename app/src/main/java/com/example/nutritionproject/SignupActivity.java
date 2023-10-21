package com.example.nutritionproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.databinding.ActivityProfileGoalsBinding;
import com.example.nutritionproject.databinding.ActivitySignupBinding;

import java.lang.reflect.Method;

public class SignupActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener
{
    private CustomDBMethods dbManager = new CustomDBMethods();
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.passwordTextField.setOnFocusChangeListener(this);
        binding.emailTextField.setOnFocusChangeListener(this);

        binding.registerBtn.setOnClickListener(this);
        binding.loginBtn.setOnClickListener(this);

        addListeners();
    }

    @Override
    public void onFocusChange(View view, boolean b)
    {
        int id = view.getId();

        CustomUIMethods.setTextFieldBackgrounds(this, new EditText[]{binding.emailTextField, binding.passwordTextField}, R.drawable.bg_gray_6dp_stroke_gray);
        CustomUIMethods.setPopupMessage(this, binding.emailErrorText, R.color.darkTheme_Transparent, "");
        CustomUIMethods.setPopupMessage(this, binding.passwordErrorText, R.color.darkTheme_Transparent, "");

        if (id == binding.emailTextField.getId())
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.emailTextField}, R.drawable.bg_gray_6dp_stroke_white);
        }
        else if (id == binding.passwordTextField.getId())
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.passwordTextField}, R.drawable.bg_gray_6dp_stroke_white);
        }

        if (binding.emailTextField.getText().length() != 0 && !dbManager.isEmailValid(CustomDBMethods.formatEmail(binding.emailTextField.getText().toString())))
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.emailTextField}, R.drawable.bg_gray_6dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, binding.emailErrorText, R.color.darkTheme_Transparent, "Invalid email");
        }
        if (binding.passwordTextField.getText().length() != 0 && !dbManager.isPasswordValid(binding.passwordTextField.getText().toString().trim()))
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.passwordTextField}, R.drawable.bg_gray_6dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, binding.passwordErrorText, R.color.darkTheme_Transparent, "Invalid password");
        }

    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.registerBtn.getId())
        {
            validateRegister();
        }
        else if (id == binding.loginBtn.getId())
        {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));

            finish();
        }
    }

    @Override
    protected void onDestroy()
    {
        removeListeners();

        super.onDestroy();
    }

    private void addListeners()
    {
        dbManager.onRegisterSuccess.addListener(this, "registerSuccessCallback");
        dbManager.onRegisterFailure.addListener(this, "registerFailureCallback");
        dbManager.onConnectionFailure.addListener(this, "connectionFailureCallback");
    }

    private void removeListeners()
    {
        dbManager.onRegisterSuccess.removeListener(this, "registerSuccessCallback");
        dbManager.onRegisterFailure.removeListener(this, "registerFailureCallback");
        dbManager.onConnectionFailure.removeListener(this, "connectionFailureCallback");
    }

    private void validateRegister()
    {
        String email = CustomDBMethods.formatEmail(binding.emailTextField.getText().toString());
        String password = binding.passwordTextField.getText().toString().trim();

        if (dbManager.isEmailValid(email) && dbManager.isPasswordValid(password))
        {
            dbManager.register(email, password, null);
        }
    }

    public void registerSuccessCallback()
    {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));

        finish();
    }

    public void registerFailureCallback()
    {
        CustomUIMethods.setPopupMessage(this, binding.signUpErrorText, R.color.darkTheme_Error, "User already exists.");
    }

    public void connectionFailureCallback()
    {
        Log.d("NORTH_SIGNUP", "Connection Failure Event Fired");

        CustomUIMethods.setPopupMessage(this, binding.signUpErrorText, R.color.darkTheme_Error, "No Internet Connection.");
    }
}