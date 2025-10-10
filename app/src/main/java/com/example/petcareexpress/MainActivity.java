package com.example.petcareexpress;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.petcareexpress.objects.FoodFormula;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String BUTTON_TAG = "Buttons";
    EditText inputPetWeightText, inputMealAmount;
    TextView petWeightText, petFoodBrandText, servingWeightOutput, mealWeightText, servingPerMealOutput;
    Button selectFoodButton, addItemButton;
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
        // Buttons
        selectFoodButton = findViewById(R.id.select_food_button);
        addItemButton = findViewById(R.id.add_item_button);

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
            Log.d(BUTTON_TAG, "Select Food Button pressed");
            showShortToast("Feature: 'Select Food' not implemented yet.");
        });
        addItemButton.setOnClickListener(v -> {
            Log.d(BUTTON_TAG, "Add Item Button pressed");
            new AddItemDialog().show(getSupportFragmentManager(), "ADD_ITEM");
        });

        // Initialize classes
        foodFormula = new FoodFormula();
        // Initialize default values
        petFoodBrandText.setText(foodFormula.name);
        setServingWeightOutput(inputPetWeightText.getText().toString());
        setMealWeightText(inputMealAmount.getText().toString());
    }
    private void showShortToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
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
