package com.example.mrg20.menuing_android.other_classes;

import android.media.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private String pathToPicture;
    private List<String> ingredientList;
    private int rating;

    private int proteins;
    private int calories;

    public Recipe(){
        name = "Test";
        pathToPicture = null;
        ingredientList = new ArrayList<>();
        rating = 0;

        proteins = 0;
        calories = 0;
    }

    public Recipe(String n, String pic, List<String> list, int r){
        name = n;
        pathToPicture = pic;
        ingredientList = list;
        rating = r;

        proteins = 0;
        calories = 0;
    }

    public Recipe(String n, String pic, List<String> list, int r, int p, int c){
        name = n;
        pathToPicture = pic;
        ingredientList = list;
        rating = r;
        proteins = p;
        calories = c;
    }

    public Recipe(JSONObject j) {
        try {
            name = j.getString("name");
            pathToPicture = j.getString("ptp");
            ingredientList = (List<String>) j.getJSONObject("ingList");
            rating = j.getInt("rating");
            proteins = j.getInt("proteins");
            calories = j.getInt("calories");
        } catch (JSONException e) {
            System.out.println("ERROR WHEN CREATING RECIPE" + e);
        }
    }

    public JSONObject getJSONObject(){
        JSONObject j = new JSONObject();
        try {
            j.put("name", name);
            j.put("ptp", pathToPicture);
            j.put("ingList", ingredientList);
            j.put("rating", rating);
            j.put("proteins", proteins);
            j.put("calories", calories);
        } catch (JSONException e) {
            System.out.println("ERROR WHEN CONVERTING RECIPE TO JSON" + e);
        }
        return j;
    }

    public void setRating(int i){
        rating = i;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return pathToPicture;
    }

    public List<String> getIngredientList() {
        return ingredientList;
    }

    public int getRating() {
        return rating;
    }

    public int getProteins() {
        return proteins;
    }

    public int getCalories() {
        return calories;
    }
}
