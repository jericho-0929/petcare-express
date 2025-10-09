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
    TextView petWeightText, petFoodBrandText, servingWeightText, mealWeightText;
    FoodFormula foodFormula;
    private final TextWatcher inputPetWeightWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.v(TAG, "inputPetWeightWatcher stand-by.");
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        public void afterTextChanged(Editable s) {
            String textValue = s.toString();
            try {
                setServingWeightText(textValue);

            } catch (NumberFormatException e) {
                Log.e(TAG, "NumberFormatException: Input is not string.");
            }
        }
    };
    private void setMealWeightText(String textValue) {
        double doubleValue = Double.parseDouble(textValue);
        double mealWeight = Double.parseDouble(servingWeightText.getText().toString()) / doubleValue;

        mealWeightText.setText();
    }
    private void setServingWeightText(String textValue) {
        double doubleValue = Double.parseDouble(textValue);
        double returnValue = foodFormula.getRecommendedServing() * doubleValue;

        Log.d(TAG, "Received user-input for inputPetWeightText: " + doubleValue);

        servingWeightText.setText(getString(R.string.grams, returnValue));
    }
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

        // Variables
        inputPetWeightText = findViewById(R.id.input_pet_weight);
        inputMealAmount = findViewById(R.id.input_meal_amount);

        petWeightText = findViewById(R.id.pet_weight);
        petFoodBrandText = findViewById(R.id.pet_food_brand);
        servingWeightText = findViewById(R.id.serving_weight);
        mealWeightText = findViewById(R.id.meal_per_day_amount);

        foodFormula = new FoodFormula();

        // Set TextChange Listener
        inputPetWeightText.addTextChangedListener(inputPetWeightWatcher);

        // Initialize default values
        petFoodBrandText.setText(foodFormula.name);
        setServingWeightText(inputPetWeightText.getText().toString());
    }
}