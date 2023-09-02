package com.example.nutritionproject.Custom.java.Custom;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.example.nutritionproject.Custom.java.Utility.Event;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.UserModel.UserGoals;
import com.example.nutritionproject.Custom.java.UserModel.UserProfile;
import com.example.nutritionproject.Model.UserModel;
import com.example.nutritionproject.Retrofit.ApiClient;
import com.example.nutritionproject.Retrofit.ApiInterface;

import java.security.SecureRandom;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomDBMethods {

    private ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    public static UserProfile CurrentProfile = new UserProfile();

    public static String currentOTPValue;
    private boolean firstOTPCallToSkip = false;

    //region Callback Events
    public Event onLoginSuccess = new Event();
    public Event onLoginFailure = new Event();

    public Event onRegisterSuccess = new Event();
    public Event onRegisterFailure = new Event();

    public Event onRecoveryMailSentSuccess = new Event();
    public Event onRecoveryMailSentFailure = new Event();
    public Event onRecoveryOTPExpired = new Event();

    public Event onPasswordResetSuccess = new Event();
    public Event getOnPasswordResetFailure = new Event();
    public Event onGoalsChangedSuccess = new Event();
    public Event onGoalsChangedFailure = new Event();

    public Event onConnectionFailure = new Event();
    //endregion

    public void resetPassword(String email) {
        // Potential need for scalability - seems gmail has sending limits, in which if you function is overused
        // or gets abused, it will disable your sending account...

        try {
            String senderEmail = "ethannorthprojects@gmail.com";
            String senderPassword = "kfdhbdtmmctolsna";

            String senderHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", senderHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);

                }
            });

            currentOTPValue = generateOneTimePassword(5);
            firstOTPCallToSkip = true;

            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                if (!firstOTPCallToSkip) {
                                    currentOTPValue = null;
                                    onRecoveryOTPExpired.invoke();
                                }
                                firstOTPCallToSkip = false;
                            } catch (Exception e) {

                            }
                        }
                    });
                }
            };
            // 10 min = 600000
            timer.schedule(doAsynchronousTask, 0, 600000);

            MimeMessage otpEmailMessage = new MimeMessage(session);

            otpEmailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            otpEmailMessage.setSubject("North Nutrition Project | Reset Password Request");
            otpEmailMessage.setText(String.format("Your OTP is %s. \n\nThis OTP will expire in 10 minutes. \n\nIf you did not send this reset password request, reset your password.\n\n-North Project\n\n\nMessage id: %s", currentOTPValue, generateRandomID(12)));

            Thread emailThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(otpEmailMessage);

                    } catch (MessagingException e) {
                        throw new RuntimeException(e);

                    }
                }
            });

            emailThread.start();
            onRecoveryMailSentSuccess.invoke();

        } catch (AddressException e) {
            onRecoveryMailSentFailure.invoke();
            throw new RuntimeException(e);

        } catch (MessagingException e) {
            onRecoveryMailSentFailure.invoke();
            throw new RuntimeException(e);
        }

    }

    // C# > Java
    public void login(String email, String password) {
        Call<UserModel> userModelCall = apiInterface.login(email, password);

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response != null) {
                    UserModel userModel = response.body();

                    if (userModel.isSuccess()) {
                        Log.d("NORTH_DATABASE", "Logged in successfully");
                        setUserProfile(userModel);
                        onLoginSuccess.invoke();
                    } else {
                        Log.d("NORTH_DATABASE", "Failed to log in");
                        onLoginFailure.invoke();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("NORTH_DATABASE", "Failed connection," + t.getMessage());
                onConnectionFailure.invoke();
            }
        });

    }

    public void register(String email, String password) {
        Call<UserModel> userModelCall = apiInterface.register(email, password);

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel userModel = response.body();

                    if (userModel.isSuccess()) {
                        Log.d("NORTH_DATABASE", "Account successfully created");
                        onRegisterSuccess.invoke();
                    } else {
                        Log.d("NORTH_DATABASE", "Failed to register account");
                        onRegisterFailure.invoke();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("NORTH_DATABASE", "Failed connection," + t.getMessage());
                onConnectionFailure.invoke();
            }
        });

    }

    public void updateGoals(int userid, int calorie, int protein, int carb, int fat) {
        Call<UserModel> userModelCall = apiInterface.setGoals(userid, calorie, protein, carb, fat);

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel userModel = response.body();

                    if (userModel.isSuccess()) {
                        Log.d("NORTH_DATABASE", "Goals successfully updated");
                        onGoalsChangedSuccess.invoke();
                    } else {
                        Log.d("NORTH_DATABASE", "Goals failed to update");
                        onGoalsChangedFailure.invoke();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("NORTH_DATABASE", "Failed connection," + t.getMessage());
                onConnectionFailure.invoke();
            }
        });

    }

    public void setPassword(String email, String newPassword) {
        Call<UserModel> userModelCall = apiInterface.setPassword(email, newPassword);

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel userModel = response.body();

                    if (userModel.isSuccess()) {
                        Log.d("NORTH_DATABASE", "Password successfully reset");

                    } else {
                        Log.d("NORTH_DATABASE", "Password failed to reset");

                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("NORTH_DATABASE", "Failed connection," + t.getMessage());
                onConnectionFailure.invoke();
            }
        });

    }

    public void getUser(String email, EventCallback callback) {
        Call<UserModel> userModelCall = apiInterface.getUser(email);

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel userModel = response.body();

                    if (userModel.isSuccess()) {
                        Log.d("NORTH_DATABASE", "User exists");
                        callback.onSuccess();
                    } else {
                        Log.d("NORTH_DATABASE", "User does not Exist");
                        callback.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("NORTH_DATABASE", "Failed connection," + t.getMessage());
                onConnectionFailure.invoke();
            }
        });

    }

    //region Reusable Methods
    public void logout(Context context) {
        if (CurrentProfile.email != null) {
            CurrentProfile = new UserProfile();

            SharedPreferences preferences = context.getSharedPreferences("login", MODE_PRIVATE);
            preferences.edit().clear().apply();

            Log.d("NORTH_DATABASE", "Logged out successfully");
        }
    }

    private void setUserProfile(UserModel user) {
        CurrentProfile.id = user.getId();
        CurrentProfile.email = user.getEmail();
        CurrentProfile.goals = new UserGoals(user.getCalorie(), user.getProtein(), user.getCarb(), user.getFat());
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(CharSequence password) {
        int minPasswordLength = 6;
        return (password.length() >= minPasswordLength);
    }

    public static String formatEmail(String email) {
        return email.toString().trim().toLowerCase();
    }

    public String generateOneTimePassword(int length) {
        //TODO: To make more ux friendly create a "battle royale" between palindrome, repeated, and sequential otp codes
        if (length >= 1) {
            Random randomOTP = new Random();

            int baseNum = 1;
            int maxNum = 9;

            while (length != 1) {
                baseNum *= 10;
                maxNum *= 10;
                length -= 1;
            }


            int n = baseNum + randomOTP.nextInt(maxNum);

            return Integer.toString(n);
        }
        return "";
    }

    public String generateRandomID(int length) {
        final String allChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom secureRandom= new SecureRandom();

        StringBuilder stringBuilder = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            stringBuilder.append(allChars.charAt(secureRandom.nextInt(allChars.length())));
        return stringBuilder.toString();
    }


    //endregion
}
