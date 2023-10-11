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
import com.example.nutritionproject.databinding.ActivityFoodItemViewBinding;
import com.example.nutritionproject.databinding.ActivityLoginResetBinding;

public class LoginResetActivity extends AppCompatActivity implements View.OnClickListener
{
    private final CustomDBMethods dbManager = new CustomDBMethods();
    private ActivityLoginResetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginResetBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.backBtn.setOnClickListener(this);
        binding.verifyOTPBtn.setOnClickListener(this);
        binding.resetPasswordBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.backBtn.getId())
        {
            finish();
        }

        if (id == binding.verifyOTPBtn.getId())
        {
            verifyOTP(binding.otpTextField.getText().toString().trim());
        }
        else if (id == binding.resetPasswordBtn.getId())
        {
            verifyPassword(binding.passwordTextField.getText().toString().trim());
        }
    }


    private void verifyOTP(String otpCode)
    {
        if (otpCode.equals(CustomDBMethods.currentOTPValue))
        {
            binding.passwordCard.setVisibility(View.VISIBLE);
            binding.otpCard.setVisibility(View.GONE);
        }
        else
        {
            CustomUIMethods.setPopupMessage(this, binding.otpErrorText, R.color.darkTheme_Transparent, "Invalid OTP");
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[]{binding.otpTextField}, R.drawable.bg_gray_6dp_stroke_red);
        }
    }

    private void verifyPassword(String newPassword)
    {
        if (CustomDBMethods.isPasswordValid(newPassword))
        {
            dbManager.setPassword(LoginActivity.otpEmail, newPassword, null);
            finish();
        }
        else
        {
            CustomUIMethods.setPopupMessage(this, binding.passwordErrorText, R.color.darkTheme_Transparent, "Invalid Password");
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[]{binding.passwordTextField}, R.drawable.bg_gray_6dp_stroke_red);
        }
    }
}