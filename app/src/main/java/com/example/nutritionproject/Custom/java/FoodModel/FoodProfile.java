package com.example.nutritionproject.Custom.java.FoodModel;

import androidx.annotation.NonNull;

import com.example.nutritionproject.Custom.java.Enums.FoodTag;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Set;

public class FoodProfile {
    public String upcId;
    public String name;
    public Set<FoodTag> tags;
    public LocalDate dateAdded;
    public boolean isCommon;
    public String brandName;
    public boolean isVerified;
    public FoodNutrition nutrition;

    //All Params
    public FoodProfile(String upcId, String name, Set<FoodTag> tags, LocalDate dateAdded, boolean isCommon, String brandName, boolean isVerified, FoodNutrition nutrition) {
        this.upcId = upcId;
        this.name = name;
        this.tags = tags;
        this.dateAdded = dateAdded;
        this.isCommon = isCommon;
        this.brandName = brandName;
        this.isVerified = isVerified;
        this.nutrition = nutrition;
    }

    //Least Params
    public FoodProfile(String name, LocalDate dateAdded, boolean isCommon, String brandName, boolean isVerified, FoodNutrition nutrition) {
        this.name = name;
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

        for (Object obj : profileMap.keySet()) {
            normalString += (", " + obj.toString() + " : " + profileMap.get(obj).toString());
        }

        // Cut off first ", "
        normalString = normalString.substring(2);

        // Add in ", " manually since it doesnt have a toString override
        String tagsPrefix = ", FoodTags : { ";
        String tagsString = "";
        String tagsSuffix = " }";

        for (FoodTag tag : tags) {
            tagsString += (", " + tag.name());
        }

        // Cut off first ", "
        tagsString = tagsString.substring(2);

        // Check if the list is empty, then skip
        String finalTagsString = !tagsString.isEmpty()? (tagsPrefix + tagsString + tagsSuffix) : "";

        return String.format("FoodProfile : { %s%s%s}", normalString, finalTagsString, (", " + nutrition.toString()));
    }
}
