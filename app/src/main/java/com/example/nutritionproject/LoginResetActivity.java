package com.example.nutritionproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;

public class LoginResetActivity extends AppCompatActivity implements View.OnClickListener {
    private final CustomDBMethods dbManager = new CustomDBMethods();

    private ImageView backBtn;

    private CardView otpCard;

    private EditText otpField;
    private TextView otpErrorText;
    private Button otpVerifyButton;

    private CardView passCard;

    private EditText passwordField;
    private TextView passwordErrorText;
    private Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reset);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        backBtn = findViewById(R.id.backButton);

        otpCard = findViewById(R.id.otpCard);
        otpField = findViewById(R.id.otpTextField);
        otpErrorText = findViewById(R.id.otpErrorText);
        otpVerifyButton = findViewById(R.id.verifyOTPButton);

        passCard = findViewById(R.id.passwordCard);

        passCard = findViewById(R.id.passwordCard);
        passwordField = findViewById(R.id.passwordTextField);
        passwordErrorText = findViewById(R.id.passwordErrorText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        backBtn.setOnClickListener(this);
        otpVerifyButton.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            finish();
        }

        if (id == otpVerifyButton.getId()) {
            verifyOTP(otpField.getText().toString().trim());
        } else if (id == resetPasswordButton.getId()) {
            verifyPassword(passwordField.getText().toString().trim());
        }
    }


    private void verifyOTP(String otpCode) {
        if (otpCode.equals(CustomDBMethods.currentOTPValue)) {
            otpCard.setVisibility(View.GONE);
            passCard.setVisibility(View.VISIBLE);
        } else {
            CustomUIMethods.setPopupMessage(this, otpErrorText, R.color.darkTheme_Transparent, "Invalid OTP");
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[]{otpField}, R.drawable.bg_gray_6dp_stroke_red);
        }
    }

    private void verifyPassword(String newPassword) {
        if (CustomDBMethods.isPasswordValid(newPassword)) {
            dbManager.setPassword(LoginActivity.otpEmail, newPassword, null);
            finish();
        } else {
            CustomUIMethods.setPopupMessage(this, passwordErrorText, R.color.darkTheme_Transparent, "Invalid Password");
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[]{passwordField}, R.drawable.bg_gray_6dp_stroke_red);
        }
    }
}