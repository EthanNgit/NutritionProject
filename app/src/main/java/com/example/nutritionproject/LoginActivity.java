package com.example.nutritionproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.databinding.ActivityFoodItemViewBinding;
import com.example.nutritionproject.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

import java.lang.reflect.Method;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener
{
    private CustomDBMethods dbManager = new CustomDBMethods();
    public static String otpEmail;
    private boolean shouldRememberMe;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.emailTextField.setOnFocusChangeListener(this);
        binding.passwordTextField.setOnFocusChangeListener(this);

        binding.forgotPasswordBtn.setOnClickListener(this);
        binding.registerBtn.setOnClickListener(this);
        binding.loginBtn.setOnClickListener(this);

        binding.rememberMeSwitch.setOnCheckedChangeListener(this);

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

        if (id == binding.forgotPasswordBtn.getId())
        {
            forgotPassword();
        }
        else if (id == binding.loginBtn.getId())
        {
            dbManager.login(CustomDBMethods.formatEmail(binding.emailTextField.getText().toString()), binding.passwordTextField.getText().toString().trim(), null);
        }
        else if (id == binding.registerBtn.getId())
        {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));

            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
    {
        if (compoundButton.getId() == binding.rememberMeSwitch.getId())
        {
            shouldRememberMe = b;
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
        dbManager.onRecoveryMailSentSuccess.addListener(this, "passwordResetSuccessCallback");
        dbManager.onRecoveryMailSentFailure.addListener(this, "passwordResetFailureCallback");
        dbManager.onRecoveryOTPExpired.addListener(this, "recoveryOTPExpiredCallback");
        dbManager.onConnectionFailure.addListener(this, "connectionFailureCallback");
        dbManager.onLoginSuccess.addListener(this, "loginSuccessCallback");
        dbManager.onLoginFailure.addListener(this, "loginFailureCallback");
    }

    private void removeListeners()
    {
        dbManager.onRecoveryMailSentSuccess.removeListener(this, "passwordResetSuccessCallback");
        dbManager.onRecoveryMailSentFailure.removeListener(this, "passwordResetFailureCallback");
        dbManager.onRecoveryOTPExpired.removeListener(this, "recoveryOTPExpiredCallback");
        dbManager.onConnectionFailure.removeListener(this, "connectionFailureCallback");
        dbManager.onLoginSuccess.removeListener(this, "loginSuccessCallback");
        dbManager.onLoginFailure.removeListener(this, "loginFailureCallback");
    }

    private void setLoginPreferences()
    {
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("rememberMe", shouldRememberMe? "true" : "false");
        editor.putString("email", shouldRememberMe? CustomDBMethods.formatEmail(binding.emailTextField.getText().toString()) : "");
        editor.putString("password", shouldRememberMe? binding.passwordTextField.getText().toString().trim() : "");
        editor.apply();
    }

    private void forgotPassword()
    {
        if (binding.emailTextField.getText().toString().isEmpty())
        {
            CustomUIMethods.setPopupMessage(this, binding.emailErrorText, R.color.darkTheme_Transparent, "Invalid email");
            CustomUIMethods.setPopupMessage(this, binding.loginErrorText, R.color.darkTheme_Error, "Enter email to be sent a recovery mail");

            return;
        }

        Context outerContext = this;
        dbManager.getUser(CustomDBMethods.formatEmail(binding.emailTextField.getText().toString()), new EventCallback()
        {
            @Override
            public void onSuccess(EventContext context)
            {
                dbManager.resetPassword(CustomDBMethods.formatEmail(binding.emailTextField.getText().toString()), null);
            }

            @Override
            public void onFailure(EventContext context)
            {
                CustomUIMethods.setPopupMessage(outerContext, binding.loginErrorText, R.color.darkTheme_Error, "User has not registered an account");
            }
        });
    }

    public void loginSuccessCallback()
    {
        setLoginPreferences();
        startActivity(new Intent(LoginActivity.this, DashboardHomeActivity.class));

        finish();
    }

    public void loginFailureCallback()
    {
        CustomUIMethods.setPopupMessage(this, binding.loginErrorText, R.color.darkTheme_Error, "Email or password is incorrect.");
    }

    public void passwordResetSuccessCallback()
    {
        otpEmail = CustomDBMethods.formatEmail(binding.emailTextField.getText().toString());
        startActivity(new Intent(LoginActivity.this, LoginResetActivity.class));
    }

    public void passwordResetFailureCallback()
    {
        CustomUIMethods.setPopupMessage(this, binding.loginErrorText, R.color.darkTheme_Error, "Recovery mail failed to send.");
    }

    public void recoveryOTPExpiredCallback()
    {

    }

    public void connectionFailureCallback()
    {
        CustomUIMethods.setPopupMessage(this, binding.loginErrorText, R.color.darkTheme_Error, "No Internet Connection.");
    }
}