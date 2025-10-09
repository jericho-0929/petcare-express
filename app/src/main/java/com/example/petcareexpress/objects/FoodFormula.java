package com.example.petcareexpress.objects;

public class FoodFormula {
    public String name;
    public double minimumPetWeight;
    public double minimumFeedingAmount;
    public double recommendedServing;
    public FoodFormula() {
        this.name = "SmartHeart Cat Food";
        this.minimumPetWeight = 2.0;
        this.minimumFeedingAmount = 40;
        this.recommendedServing = getRecommendedServing();
    }
    public FoodFormula(String name, double minimumPetWeight, double minimumFeedingAmount) {
        this.name = name;
        this.minimumPetWeight = minimumPetWeight;
        this.minimumFeedingAmount = minimumFeedingAmount;
        this.recommendedServing = getRecommendedServing();
    }
    public double getRecommendedServing() {
        return minimumFeedingAmount / minimumPetWeight;
    }
}
