package com.example.petcareexpress.objects;

public class FoodFormula {
    public String name;
<<<<<<< HEAD
=======
    public String foodType;
>>>>>>> b67d0a2 (Extract sub-class foodFormula to own class file. Add measurement for per-meal feature.)
    public double minimumPetWeight;
    public double minimumFeedingAmount;
    public double recommendedServing;
    public FoodFormula() {
<<<<<<< HEAD
        this.name = "SmartHeart Cat Food";
=======
        this.name = "SmartHeart Cat Dry Food";
        this.foodType = "Wet";
>>>>>>> b67d0a2 (Extract sub-class foodFormula to own class file. Add measurement for per-meal feature.)
        this.minimumPetWeight = 2.0;
        this.minimumFeedingAmount = 40;
        this.recommendedServing = getRecommendedServing();
    }
<<<<<<< HEAD
    public FoodFormula(String name, double minimumPetWeight, double minimumFeedingAmount) {
        this.name = name;
=======
    public FoodFormula(String name, String foodType, double minimumPetWeight, double minimumFeedingAmount) {
        this.name = name;
        this.foodType = foodType;
>>>>>>> b67d0a2 (Extract sub-class foodFormula to own class file. Add measurement for per-meal feature.)
        this.minimumPetWeight = minimumPetWeight;
        this.minimumFeedingAmount = minimumFeedingAmount;
        this.recommendedServing = getRecommendedServing();
    }
    public double getRecommendedServing() {
        return minimumFeedingAmount / minimumPetWeight;
    }
}
