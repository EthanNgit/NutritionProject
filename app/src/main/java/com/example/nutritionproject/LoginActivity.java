package com.example.nutritionproject;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.nutritionproject.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.LoginResetActivity;
import com.google.gson.Gson;

import java.lang.reflect.Method;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //region References
    private CustomUIMethods uiManager = new CustomUIMethods();
    private CustomDBMethods dbManager = new CustomDBMethods();

    public static String otpEmail;

    private EditText emailField;
    private EditText passwordField;

    private TextView emailErrorView;
    private TextView passwordErrorView;
    private TextView errorView;

    private Switch rememberMeSwitch;
    private boolean shouldRememberMe;

    private Button signUpBtn;
    private Button forgotPasswordBtn;
    private Button loginBtn;

    //endregion

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

        emailField = findViewById(R.id.emailTextField);
        passwordField = findViewById(R.id.passwordTextField);

        emailErrorView = findViewById(R.id.emailErrorText);
        passwordErrorView = findViewById(R.id.passwordErrorText);
        errorView = findViewById(R.id.loginErrorText);

        rememberMeSwitch = findViewById(R.id.rememberMeSwitch);

        signUpBtn = findViewById(R.id.signUpButton);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordButton);
        loginBtn = findViewById(R.id.loginButton);

        emailField.setOnFocusChangeListener(this);
        passwordField.setOnFocusChangeListener(this);

        signUpBtn.setOnClickListener(this);
        forgotPasswordBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        rememberMeSwitch.setOnCheckedChangeListener(this);

        addListeners();

    }


    @Override
    public void onFocusChange(View view, boolean b) {
        int id = view.getId();

        uiManager.setTextFieldBackgrounds(this, new EditText[]{emailField, passwordField}, R.drawable.bg_gray_6dp_stroke_gray);
        uiManager.setPopupMessage(this, emailErrorView, R.color.darkTheme_Transparent, "");
        uiManager.setPopupMessage(this, passwordErrorView, R.color.darkTheme_Transparent, "");


        if (id == emailField.getId()) {
            uiManager.setTextFieldBackgrounds(this, new EditText[] {emailField}, R.drawable.bg_gray_6dp_stroke_white);
        } else if (id == passwordField.getId()) {
            uiManager.setTextFieldBackgrounds(this, new EditText[] {passwordField}, R.drawable.bg_gray_6dp_stroke_white);
        }

        if (emailField.getText().length() != 0 && !dbManager.isEmailValid(emailField.getText().toString().trim())) {
            uiManager.setTextFieldBackgrounds(this, new EditText[] {emailField}, R.drawable.bg_gray_6dp_stroke_red);
            uiManager.setPopupMessage(this, emailErrorView, R.color.darkTheme_Transparent, "Invalid email");
        }
        if (passwordField.getText().length() != 0 && !dbManager.isPasswordValid(passwordField.getText().toString().trim())) {
            uiManager.setTextFieldBackgrounds(this, new EditText[] {passwordField}, R.drawable.bg_gray_6dp_stroke_red);
            uiManager.setPopupMessage(this, passwordErrorView, R.color.darkTheme_Transparent, "Invalid password");
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == forgotPasswordBtn.getId()) {
            forgotPassword();
        } else if (id == loginBtn.getId()) {
            dbManager.login(emailField.getText().toString().trim(), passwordField.getText().toString().trim());

        } else if (id == signUpBtn.getId()) {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == rememberMeSwitch.getId()) {
            shouldRememberMe = b;
        }

    }

    @Override
    protected void onDestroy() {
        removeListeners();
        super.onDestroy();

    }

    //endregion

    //region Main Methods
    private void addListeners() {
        dbManager.onLoginSuccess.addListener(this, "loginSuccessCallback");
        dbManager.onLoginFailure.addListener(this, "loginFailureCallback");
        dbManager.onRecoveryMailSentSuccess.addListener(this, "passwordResetSuccessCallback");
        dbManager.onRecoveryMailSentFailure.addListener(this, "passwordResetFailureCallback");
        dbManager.onRecoveryOTPExpired.addListener(this, "recoveryOTPExpiredCallback");
        dbManager.onConnectionFailure.addListener(this, "connectionFailureCallback");

    }

    private void removeListeners() {
        dbManager.onLoginSuccess.removeListener(this, "loginSuccessCallback");
        dbManager.onLoginFailure.removeListener(this, "loginFailureCallback");
        dbManager.onRecoveryMailSentSuccess.removeListener(this, "passwordResetSuccessCallback");
        dbManager.onRecoveryMailSentFailure.removeListener(this, "passwordResetFailureCallback");
        dbManager.onRecoveryOTPExpired.removeListener(this, "recoveryOTPExpiredCallback");
        dbManager.onConnectionFailure.removeListener(this, "connectionFailureCallback");

    }

    private void setLoginPreferences() {
        Gson gson = new Gson();
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("rememberMe", shouldRememberMe? "true" : "false");
        editor.putString("email", shouldRememberMe? emailField.getText().toString().trim() : "");
        editor.putString("password", shouldRememberMe? passwordField.getText().toString().trim() : "");
        editor.apply();

    }

    private void forgotPassword() {
        if (emailField.getText().toString().isEmpty()) {
            //Give Error
            uiManager.setPopupMessage(this, emailErrorView, R.color.darkTheme_Transparent, "Invalid email");
            uiManager.setPopupMessage(this, errorView, R.color.darkTheme_Error, "Enter email to be sent a recovery mail");

            return;
        }

        dbManager.resetPassword(emailField.getText().toString().trim());

    }

    //endregion

    //region Callback Methods
    public Method loginSuccessCallback() {
        Log.d("NORTH_LOGIN", "Login Success Event Fired");

        setLoginPreferences();
        startActivity(new Intent(LoginActivity.this, DashboardHomeActivity.class));
        finish();

        return null;

    }

    public Method loginFailureCallback() {
        Log.d("NORTH_LOGIN", "Login Failure Event Fired");

        uiManager.setPopupMessage(this, errorView, R.color.darkTheme_Error, "Email or password is incorrect.");

        return null;

    }

    public Method passwordResetSuccessCallback() {
        //open OTP stuff

        otpEmail = emailField.getText().toString().trim();
        startActivity(new Intent(LoginActivity.this, LoginResetActivity.class));

        Log.d("NORTH_TEXT", "OTP PASSWORD IS: " + dbManager.currentOTPValue);

        return null;
    }

    public Method passwordResetFailureCallback() {

        uiManager.setPopupMessage(this, errorView, R.color.darkTheme_Error, "Recovery mail failed to send.");

        return null;
    }

    public Method recoveryOTPExpiredCallback() {
        Log.d("NORTH_TEXT", "OTP PASSWORD HAS EXPIRED");

        return null;
    }

    public Method connectionFailureCallback() {
        Log.d("NORTH_LOGIN", "Connection Failure Event Fired");

        uiManager.setPopupMessage(this, errorView, R.color.darkTheme_Error, "No Internet Connection.");

        return null;

    }

    //endregion
}