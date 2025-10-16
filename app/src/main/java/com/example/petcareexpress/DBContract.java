package com.example.petcareexpress;

import android.provider.BaseColumns;

// Define schema of database.
public class DBContract {
    // Keep empty to prevent instantiation.
    private DBContract() {}
    public static final class FoodTable implements BaseColumns {
        public static final String TABLE_NAME = "food_inventory";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_BRAND_NAME = "brand_name";
        public static final String COLUMN_FOOD_TYPE = "food_type";
        public static final String COLUMN_MIN_PET_WEIGHT = "min_pet_weight";
        public static final String COLUMN_MIN_FEED_AMOUNT = "min_feed_amount";
        public static final String COLUMN_FOR_DELETION = "for_deletion";
    }
}
