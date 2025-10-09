package com.example.petcareexpress;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.petcareexpress.objects.FoodFormula;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText inputPetWeightText, inputMealAmount;
    TextView petWeightText, petFoodBrandText, servingWeightOutput, mealWeightText, servingPerMealOutput;
    FoodFormula foodFormula;
    double servingWeight = 0;
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

        // Assign variables to equivalent in UI
        petWeightText = findViewById(R.id.pet_weight);
        petFoodBrandText = findViewById(R.id.pet_food_brand);
        mealWeightText = findViewById(R.id.meal_per_day_amount);
        // User-input guide UI elements
        inputPetWeightText = findViewById(R.id.input_pet_weight);
        inputMealAmount = findViewById(R.id.input_meal_amount);
        // Output-to-user UI elements
        servingWeightOutput = findViewById(R.id.serving_weight);
        servingPerMealOutput = findViewById(R.id.serving_per_meal);

        // Add TextChange Listeners
        inputPetWeightText.addTextChangedListener(inputPetWeightWatcher);
        inputMealAmount.addTextChangedListener(inputMealAmountWatcher);
        // Initialize classes
        foodFormula = new FoodFormula();
        // Initialize default values
        petFoodBrandText.setText(foodFormula.name);
        setServingWeightOutput(inputPetWeightText.getText().toString());
        setMealWeightText(inputMealAmount.getText().toString());
    }
    private final TextWatcher inputPetWeightWatcher = new TextWatcher() {
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
    };
    private final TextWatcher inputMealAmountWatcher = new TextWatcher() {
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
    };
    private void setMealWeightText(String textValue) {
        double doubleValue = Double.parseDouble(textValue);
        double mealWeight = servingWeight / doubleValue;

        servingPerMealOutput.setText(getString(R.string.grams_per_meal, mealWeight));
    }
    private void setServingWeightOutput(String textValue) {
        double doubleValue = Double.parseDouble(textValue);
        servingWeight = foodFormula.getRecommendedServing() * doubleValue;

        Log.d(TAG, "Received user-input for inputPetWeightText: " + doubleValue);

        servingWeightOutput.setText(getString(R.string.grams, servingWeight));
    }
}