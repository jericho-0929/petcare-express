package com.example.petcareexpress;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.petcareexpress.objects.FoodData;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityResultLauncher<Intent> getFoodItemActivity;
    private DBHelper dbHelper;
    EditText inputPetWeightText, inputMealAmount;
    TextView petWeightText, petFoodBrandText, petFoodTypeText,
            servingWeightOutput, mealWeightText, servingPerMealOutput;
    Button selectFoodButton, addItemButton;
    FoodData foodData;
    double servingWeight = 0;
    Cursor dbCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Activity Initialization
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize DBHelper
        dbHelper = new DBHelper(this);
        // Assign variables to equivalent in UI
        petWeightText = findViewById(R.id.pet_weight);
        petFoodBrandText = findViewById(R.id.pet_food_brand);
        petFoodTypeText = findViewById(R.id.pet_food_type);
        mealWeightText = findViewById(R.id.meal_per_day_amount);
        // User-input guide UI elements
        inputPetWeightText = findViewById(R.id.input_pet_weight);
        inputMealAmount = findViewById(R.id.input_meal_amount);
        // Output-to-user UI elements
        servingWeightOutput = findViewById(R.id.serving_weight);
        servingPerMealOutput = findViewById(R.id.serving_per_meal);
        // Buttons
        selectFoodButton = findViewById(R.id.select_food_button);
        addItemButton = findViewById(R.id.add_item_button);

        // Initialize ActivityResultLaunchers
        getFoodItemActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == MainActivity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;

                        foodData = data.getParcelableExtra("selectedFoodData");
                        assert foodData != null;

                        setFoodDataDisplayText(foodData.name, foodData.foodType);
                        setServingWeightOutput(inputPetWeightText.getText().toString());
                        setMealWeightText(inputMealAmount.getText().toString());
                    } else if (result.getResultCode() == MainActivity.RESULT_CANCELED) {
                        showShortToast("No item selected.");
                    }
                });

        // Add TextChange Listeners
        inputPetWeightText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.v(TAG, "inputPetWeightWatcher stand-by.");
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v(TAG, "inputPetWeightWatcher receiving input.");
            }

            public void afterTextChanged(Editable s) {
                String textValue = s.toString();
                try {
                    setServingWeightOutput(textValue);
                    setMealWeightText(inputMealAmount.getText().toString());

                } catch (NumberFormatException e) {
                    Log.e(TAG, "NumberFormatException: Input is not string.");
                }
            }
        });
        inputMealAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    String textValue = s.toString();
                    try {
                        setMealWeightText(textValue);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "NumberFormatException: Input is not string.");
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.v(TAG, "inputMealAmountWatcher stand-by.");
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.v(TAG, "inputMealAmountWatcher receiving input.");
                }
            });
        // Add onClick Listeners
        selectFoodButton.setOnClickListener(v -> {
            // showShortToast("Feature: 'Select Food' not implemented yet.");

            Intent intent = new Intent(MainActivity.this, SelectItemActivity.class);
            getFoodItemActivity.launch(intent);
        });
        addItemButton.setOnClickListener(v -> {
            new AddItemDialog().show(getSupportFragmentManager(), "ADD_ITEM");
        });

        // Initialize default values
        foodData = constructFoodFormula();
        setFoodDataDisplayText(foodData.name, foodData.foodType);
        setServingWeightOutput(inputPetWeightText.getText().toString());
        setMealWeightText(inputMealAmount.getText().toString());

        Log.d(TAG, "onCreate() finished.");
    }
    public void showShortToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
    private void setFoodDataDisplayText(String foodName, String foodType) {
        petFoodBrandText.setText(foodName);
        petFoodTypeText.setText(foodType);
    }
    private void setMealWeightText(String textValue) {
        double doubleValue = Double.parseDouble(textValue);
        double mealWeight = servingWeight / doubleValue;

        servingPerMealOutput.setText(getString(R.string.grams_per_meal, mealWeight));
    }
    private void setServingWeightOutput(String textValue) {
        double doubleValue = Double.parseDouble(textValue);
        servingWeight = foodData.getRecommendedServing() * doubleValue;

        Log.d(TAG, "Received user-input for inputPetWeightText: " + doubleValue);

        servingWeightOutput.setText(getString(R.string.grams, servingWeight));
    }
    private FoodData constructFoodFormula() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DBContract.FoodTable.TABLE_NAME,
                null,
                DBContract.FoodTable.COLUMN_ID + " = ?",
                new String[] {String.valueOf(1)},
                null,
                null,
                null
        );
        // Get first entry in foodDatabase.
        cursor.moveToFirst();
        // Initialize classes
        FoodData foodData = new FoodData(
                cursor.getString(cursor.getColumnIndexOrThrow(DBContract.FoodTable.COLUMN_BRAND_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBContract.FoodTable.COLUMN_FOOD_TYPE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DBContract.FoodTable.COLUMN_MIN_PET_WEIGHT)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DBContract.FoodTable.COLUMN_MIN_FEED_AMOUNT))
        );
        cursor.close();
        return foodData;
    }
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
