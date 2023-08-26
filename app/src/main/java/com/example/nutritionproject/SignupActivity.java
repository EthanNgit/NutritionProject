package com.example.nutritionproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nutritionproject.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.CustomUIMethods;

import java.lang.reflect.Method;

public class SignupActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {

    //region References
    private CustomUIMethods uiManager = new CustomUIMethods();
    private CustomDBMethods dbManager = new CustomDBMethods();

    private EditText emailField;
    private EditText passwordField;

    private TextView emailErrorView;
    private TextView passwordErrorView;
    private TextView errorView;

    private Button signUpButton;
    private Button loginButton;

    //endregion

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

        emailField = findViewById(R.id.emailTextField);
        passwordField = findViewById(R.id.passwordTextField);

        emailErrorView = findViewById(R.id.emailErrorText);
        passwordErrorView = findViewById(R.id.passwordErrorText);
        errorView = findViewById(R.id.signUpErrorText);

        signUpButton = findViewById(R.id.signUpButton);
        loginButton = findViewById(R.id.loginButton);

        emailField.setOnFocusChangeListener(this);
        passwordField.setOnFocusChangeListener(this);

        signUpButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        addListeners();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int id = view.getId();

        uiManager.setTextFieldBackgrounds(this, new EditText[]{emailField, passwordField}, R.drawable.rounded_midground_background);
        uiManager.setPopupMessage(this, emailErrorView, R.color.darkTheme_Transparent, "");
        uiManager.setPopupMessage(this, passwordErrorView, R.color.darkTheme_Transparent, "");

        if (id == emailField.getId()) {
            uiManager.setTextFieldBackgrounds(this, new EditText[] {emailField}, R.drawable.rounded_midground_background_highlighted);
        } else if (id == passwordField.getId()) {
            uiManager.setTextFieldBackgrounds(this, new EditText[] {passwordField}, R.drawable.rounded_midground_background_highlighted);
        }

        if (emailField.getText().length() != 0 && !dbManager.isEmailValid(emailField.getText().toString().trim())) {
            uiManager.setTextFieldBackgrounds(this, new EditText[] {emailField}, R.drawable.rounded_midground_background_error);
            uiManager.setPopupMessage(this, emailErrorView, R.color.darkTheme_Transparent, "Invalid email");
        }
        if (passwordField.getText().length() != 0 && !dbManager.isPasswordValid(passwordField.getText().toString().trim())) {
            uiManager.setTextFieldBackgrounds(this, new EditText[] {passwordField}, R.drawable.rounded_midground_background_error);
            uiManager.setPopupMessage(this, passwordErrorView, R.color.darkTheme_Transparent, "Invalid password");
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == signUpButton.getId()) {
            validateRegister();

        } else if (id == loginButton.getId()) {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
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
        dbManager.onRegisterSuccess.addListener(this, "registerSuccessCallback");
        dbManager.onRegisterFailure.addListener(this, "registerFailureCallback");
        dbManager.onConnectionFailure.addListener(this, "connectionFailureCallback");
    }

    private void removeListeners() {
        dbManager.onRegisterSuccess.removeListener(this, "registerSuccessCallback");
        dbManager.onRegisterFailure.removeListener(this, "registerFailureCallback");
        dbManager.onConnectionFailure.removeListener(this, "connectionFailureCallback");
    }

    private void validateRegister() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (dbManager.isEmailValid(email) && dbManager.isPasswordValid(password)) {
            dbManager.register(email, password);
        }
    }

    //endregion

    //region Callback Methods
    public Method registerSuccessCallback() {
        Log.d("NORTH_SIGNUP", "Register Success Event Fired");

        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();

        return null;
    }

    public Method registerFailureCallback() {
        Log.d("NORTH_SIGNUP", "Register Failure Event Fired");

        uiManager.setPopupMessage(this, errorView, R.color.darkTheme_Error, "User already exists.");

        return null;
    }

    public Method connectionFailureCallback() {
        Log.d("NORTH_SIGNUP", "Connection Failure Event Fired");

        uiManager.setPopupMessage(this, errorView, R.color.darkTheme_Error, "No Internet Connection.");

        return null;
    }
    //endregion
}