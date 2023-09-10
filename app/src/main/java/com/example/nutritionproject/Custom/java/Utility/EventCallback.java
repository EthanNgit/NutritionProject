package com.example.nutritionproject.Custom.java.Utility;

import androidx.annotation.Nullable;

public interface EventCallback {
    public void onSuccess(@Nullable EventContext context);
    public void onFailure(@Nullable EventContext context);

}
