package com.example.nutritionproject.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FoodModel {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("upcId")
    private String upcId;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("tags")
    private String tags;

    @Expose
    @SerializedName("dateAdded")
    private String dateAdded;

    @Expose
    @SerializedName("isCommon")
    private int isCommon;

    @Expose
    @SerializedName("brandName")
    private String brandName;

    @Expose
    @SerializedName("isVerified")
    private int isVerified;

    @Expose
    @SerializedName("nutrition")
    private String nutrition;

    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("message")
    private String message;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String  getUpcId() {
        return upcId;
    }

    public void setUpcId(String upcId) {
        this.upcId = upcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getIsCommon() {
        return isCommon;
    }

    public void setCommon(int common) {
        isCommon = common;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public void setVerified(int verified) {
        isVerified = verified;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
