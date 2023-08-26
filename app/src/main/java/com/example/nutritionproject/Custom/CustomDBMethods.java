package com.example.nutritionproject.Custom;

import android.util.Log;

import com.example.nutritionproject.Model.UserModel;
import com.example.nutritionproject.Retrofit.ApiClient;
import com.example.nutritionproject.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomDBMethods {

    private ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    public static UserProfile CurrentProfile = new UserProfile();

    public int minPasswordLength = 6;

    //region Callback Events
    public Event onLoginSuccess = new Event();
    public Event onLoginFailure = new Event();

    public Event onRegisterSuccess = new Event();

    public Event onRegisterFailure = new Event();

    public Event onGoalsChangedSuccess = new Event();
    public Event onGoalsChangedFailure = new Event();

    public Event onConnectionFailure = new Event();
    //endregion

    public void resetPassword(String email) {
        //TODO: (DB METHODS) check if user has account and send a recovery email to the email
        // Send email to users email with a code
        // For the user to reset the code they have to enter the code along with their new password

    }

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

    //region Reusable Methods
    private void setUserProfile(UserModel user) {
        CurrentProfile.id = user.getId();
        CurrentProfile.email = user.getEmail();
        CurrentProfile.goals = new UserGoals(user.getCalorie(), user.getProtein(), user.getCarb(), user.getFat());
    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPasswordValid(CharSequence password) {
        return (password.length() >= minPasswordLength);
    }
    //endregion
}
