package com.example.nutritionproject.Custom.java.FoodModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nutritionproject.Custom.java.Enums.FoodTag;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Set;

public class FoodProfile {
    @Nullable
    public String upcId;
    public String name;
    @Nullable
    public Set<FoodTag> tags;
    public String dateAdded;
    public boolean isCommon;
    @Nullable
    public String brandName;
    public boolean isVerified;
    @Nullable
    public FoodNutrition nutrition;

    //All Params
    public FoodProfile(@Nullable String upcId, String name, @Nullable Set<FoodTag> tags, String dateAdded, boolean isCommon, @Nullable String brandName, boolean isVerified, @Nullable FoodNutrition nutrition) {
        this.upcId = upcId;
        this.name = name;
        this.tags = tags;
        this.dateAdded = dateAdded;
        this.isCommon = isCommon;
        this.brandName = brandName;
        this.isVerified = isVerified;
        this.nutrition = nutrition;
    }

    @NonNull
    @Override
    public String toString() {
        HashMap<String, Object> profileMap = new HashMap<>();

        if (!upcId.isEmpty()) profileMap.put("UpcId", upcId);
        profileMap.put("Name", name);
        profileMap.put("DateAdded", dateAdded);
        profileMap.put("IsCommon", isCommon);
        if (!brandName.isEmpty()) profileMap.put("BrandName", brandName);
        profileMap.put("IsVerified", isVerified);

        String normalString = "";

        String tagString = "";

        for (Object obj : profileMap.keySet()) {
            normalString += (", " + obj.toString() + " : " + profileMap.get(obj).toString());
        }

        // Cut off first ", "
        normalString = normalString.substring(2);

        if (tags != null || !tags.isEmpty()) {
            // Add in ", " manually since it does not have a toString override
            String tagsPrefix = ", FoodTags : { ";
            String tagsString = "";
            String tagsSuffix = " }";

            for (FoodTag tag : tags) {
                tagsString += (", " + tag.name());
            }

            // Cut off first ", "
            tagsString = tagsString.substring(2);

            // Check if the list is empty, then skip
            tagString = !tagsString.isEmpty()? (tagsPrefix + tagsString + tagsSuffix) : "";
        }

        return String.format("FoodProfile : { %s%s%s }", normalString, tagString, (nutrition != null)? (", " + nutrition.toString()) : "");
    }
}
