package com.example.nutritionproject.Custom.java.Custom;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.nutritionproject.Custom.java.Enums.FoodTag;
import com.example.nutritionproject.Custom.java.FoodModel.FoodNutrition;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.Utility.Event;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.UserModel.UserGoals;
import com.example.nutritionproject.Custom.java.UserModel.UserProfile;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.Custom.java.Utility.EventContextStrings;
import com.example.nutritionproject.Model.FoodModel;
import com.example.nutritionproject.Model.UserModel;
import com.example.nutritionproject.Retrofit.ApiClient;
import com.example.nutritionproject.Retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
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
    // I personally tried converting this class to static for convenience, but I noticed a performance decrease
    // so I will leave it as is for now. (If i were to take a guess its 2 reasons api and event calls that make it slow)
    private ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    public static UserProfile CurrentProfile = null;

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

    public Event onFoodItemAddedSuccess = new Event();
    public Event onFoodItemAddedFailure = new Event();

    public Event onFoodItemSearchSuccess = new Event();
    public Event onFoodItemSearchFailure = new Event();
    //endregion

    // C# > Java (at least for anything that has interfaces; optional params and built in events are great)

    //region User Methods

    /**
     * @apiNote Provide email to send recover email to and an optional callback
     */
    public void resetPassword(String email, @Nullable EventCallback callback) {
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
            if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Recovery mail sent successfully");
            if (callback != null) callback.onSuccess(new EventContext.Builder().withMessage(EventContextStrings.passwordRecoverMailSuccess).build());
            onRecoveryMailSentSuccess.invoke();

        } catch (AddressException e) {
            if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Recovery email failed to send: " + e.getMessage());
            if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.invalidInput).build());
            onRecoveryMailSentFailure.invoke();
            throw new RuntimeException(e);

        } catch (MessagingException e) {
            if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Recovery email failed to send: " + e.getMessage());
            if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.responseError).build());
            onRecoveryMailSentFailure.invoke();
            throw new RuntimeException(e);
        }

    }

    /**
     * @apiNote Provide email and password of an existing account to log into an account to and an optional callback
     */
    public void login(String email, String password, @Nullable EventCallback callback) {
        Call<UserModel> userModelCall = apiInterface.login(email, password);

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response != null) {
                    UserModel userModel = response.body();

                    if (userModel.isSuccess()) {
                        setUserProfile(userModel);

                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Logged in successfully");
                        if (callback != null) callback.onSuccess(new EventContext.Builder().withMessage(EventContextStrings.loginSuccess).build());
                        onLoginSuccess.invoke();
                    } else {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Failed to log in");
                        if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.responseError).build());
                        onLoginFailure.invoke();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Failed connection," + t.getMessage());
                if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.connectionError).build());
                onConnectionFailure.invoke();
            }
        });

    }

    /**
     * @apiNote Provide email and password to a new account to register an account and an optional callback
     */
    public void register(String email, String password, @Nullable EventCallback callback) {
        Call<UserModel> userModelCall = apiInterface.register(email, password);

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel userModel = response.body();

                    if (userModel.isSuccess()) {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Account successfully created");
                        if (callback != null) callback.onSuccess(new EventContext.Builder().withMessage(EventContextStrings.registerSuccess).build());
                        onRegisterSuccess.invoke();
                    } else {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Failed to register account");
                        if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.responseError).build());
                        onRegisterFailure.invoke();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Failed connection," + t.getMessage());
                if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.connectionError).build());
                onConnectionFailure.invoke();
            }
        });

    }

    /**
     * @apiNote Provide user id belonging to the account, along with the target goals and an optional callback
     */
    public void updateGoals(int userid, int calorie, int protein, int carb, int fat, @Nullable EventCallback callback) {
        Call<UserModel> userModelCall = apiInterface.setGoals(userid, calorie, protein, carb, fat);

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel userModel = response.body();

                    if (userModel.isSuccess()) {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Goals successfully updated");
                        if (callback != null) callback.onSuccess(new EventContext.Builder().withMessage(EventContextStrings.updateGoalSuccess).build());
                        onGoalsChangedSuccess.invoke();
                    } else {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Goals failed to update");
                        if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.responseError).build());
                        onGoalsChangedFailure.invoke();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Failed connection," + t.getMessage());
                if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.connectionError).build());
                onConnectionFailure.invoke();
            }
        });

    }

    /**
     * @apiNote Provide email and new password to an existing account to reset an account's password and an optional callback
     */
    public void setPassword(String email, String newPassword, @Nullable EventCallback callback) {
        Call<UserModel> userModelCall = apiInterface.setPassword(email, newPassword);

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel userModel = response.body();

                    if (userModel.isSuccess()) {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Password successfully reset");
                        if (callback != null) callback.onSuccess(new EventContext.Builder().withMessage(EventContextStrings.passwordResetSuccess).build());
                    } else {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Password failed to reset");
                        if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.responseError).build());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Failed connection," + t.getMessage());
                if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.connectionError).build());
                onConnectionFailure.invoke();
            }
        });

    }

    /**
     * @apiNote Provide email to an existing account to search for it in the database and an optional callback
     */
    public void getUser(String email, EventCallback callback) {
        Call<UserModel> userModelCall = apiInterface.getUser(email);

        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel userModel = response.body();

                    if (userModel.isSuccess()) {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "User exists");
                        if (callback != null) callback.onSuccess(new EventContext.Builder().withMessage(EventContextStrings.userSearchSuccess).build());
                    } else {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "User does not Exist");
                        if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.responseError).build());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Failed connection," + t.getMessage());
                if (callback != null) callback.onFailure(new EventContext.Builder().withMessage(EventContextStrings.connectionError).build());
                onConnectionFailure.invoke();
            }
        });

    }

    //endregion

    //region Food db Methods
    /**
     * @apiNote Provide the item you want to add and an optional callback
     */
    public void addFoodItem(FoodProfile item, @Nullable EventCallback callback) {
        @Nullable String upcId = item.upcId;
        String name = item.name;
        @Nullable Set<FoodTag> tags = item.tags;
        String dateAdded = item.dateAdded;
        boolean isCommon = item.isCommon;
        @Nullable String brandName = item.brandName;
        boolean isVerified = item.isVerified;
        @Nullable FoodNutrition nutrition = item.nutrition;

        Gson gson = new Gson();

        Call<FoodModel> foodModelCall = apiInterface.addFoodItem(upcId, name, gson.toJson(tags), dateAdded, isCommon, brandName, isVerified, gson.toJson(nutrition));

        foodModelCall.enqueue(new Callback<FoodModel>() {
            @Override
            public void onResponse(Call<FoodModel> call, Response<FoodModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FoodModel foodModel = response.body();

                    if (foodModel.isSuccess()) {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Food item added successfully");

                        if (callback != null) callback.onSuccess(new EventContext.Builder().withMessage(EventContextStrings.foodAddSuccess).build());
                        onFoodItemAddedSuccess.invoke();
                    } else {
                        if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Food item failed to create" + foodModel.getMessage());
                        if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.responseError).build());
                        onFoodItemAddedFailure.invoke();
                    }
                }
            }

            @Override
            public void onFailure(Call<FoodModel> call, Throwable t) {
                if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Failed connection, " + t.getMessage());
                if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.connectionError).build());
                onConnectionFailure.invoke();
            }
        });
    }

    /**
     * @apiNote Provide upcid or name of an existing item to search for it and an optional callback.
     *  (results will be sent to context.getData, make sure to type-cast the result)
     */
    public void searchFoodItem(@Nullable String upcId, @Nullable String name, @Nullable EventCallback callback) {
        if (upcId == null && name == null) {
            if (callback != null) callback.onFailure(new EventContext.Builder().withMessage(EventContextStrings.invalidInput).build());
            return;
        }

        Call<List<FoodModel>> foodModelCall = apiInterface.searchFoodItem(upcId, name);

        foodModelCall.enqueue(new Callback<List<FoodModel>>() {
            @Override
            public void onResponse(Call<List<FoodModel>> call, Response<List<FoodModel>> response) {
                List<FoodModel> foodModel = response.body();
                List<FoodProfile> profiles = new ArrayList<>();
                Gson gson = new Gson();
                Type setType = new TypeToken<HashSet<FoodTag>>(){}.getType();
                for(FoodModel model : foodModel) {
                    profiles.add(new FoodProfile(model.getUpcId(), model.getName(), gson.fromJson(model.getTags(), setType),
                            model.getDateAdded(), model.getIsCommon() != 0, model.getBrandName(),
                            model.getIsVerified() != 0, gson.fromJson(model.getNutrition(), FoodNutrition.class)));
                }

                if (profiles.size() > 0) {
                    if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", profiles.size() + " Search results found");
                    if (callback != null) callback.onSuccess(new EventContext.Builder().withMessage(EventContextStrings.foodSearchSuccess).withData(profiles).build());
                    onFoodItemSearchSuccess.invoke();
                } else {
                    if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", profiles.size() + "0 Search results found");
                    if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.responseError).build());
                    onFoodItemSearchFailure.invoke();
                }

            }

            @Override
            public void onFailure(Call<List<FoodModel>> call, Throwable t) {
                if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Failed connection, " + t.getMessage());
                if (callback != null) callback.onFailure(new EventContext.Builder().withError(EventContextStrings.connectionError).build());
                onConnectionFailure.invoke();
            }
        });
    }
    //endregion

    //region Reusable Methods
    public void logout(Context context) {
        if (CurrentProfile.email != null) {
            CurrentProfile = null;

            SharedPreferences preferences = context.getSharedPreferences("login", MODE_PRIVATE);
            preferences.edit().clear().apply();

            if (CustomUtilityMethods.shouldDebug(CustomDBMethods.class)) Log.d("NORTH_DATABASE", "Logged out successfully");
        }
    }

    private static void setUserProfile(UserModel user) {
        CurrentProfile = new UserProfile(user.getId(), user.getEmail(),
                new UserGoals(user.getCalorie(), user.getProtein(), user.getCarb(), user.getFat()), null);
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

    public static String generateOneTimePassword(int length) {
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

    public static String generateRandomID(int length) {
        final String allChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom secureRandom= new SecureRandom();

        StringBuilder stringBuilder = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            stringBuilder.append(allChars.charAt(secureRandom.nextInt(allChars.length())));
        return stringBuilder.toString();
    }
    //endregion
}
