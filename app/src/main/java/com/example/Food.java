package com.example;

import java.io.Serializable;

public class Food implements Serializable {

    private String foodName, recordDate;
    private String calories, foodId;
    private static final long serialVersionUID = 10L;


    public Food() {
    }

    public Food(String foodName, String calories) {
        this.foodName = foodName;
        this.calories = calories;
    }

    public Food(String foodName, String recordDate, String calories, String foodId) {
        this.foodName = foodName;
        this.recordDate = recordDate;
        this.calories = calories;
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public static long getSerialVersionID() {
        return serialVersionUID;
    }
}