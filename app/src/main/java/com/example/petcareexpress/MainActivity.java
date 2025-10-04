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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText inputPetWeightText;
    TextView petWeightText, petFoodBrandText, servingWeightText;
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
                double doubleValue = Double.parseDouble(textValue);
                double returnValue = foodFormula.recommendedServing * doubleValue;

                Log.d(TAG, "Received user-input for inputPetWeightText: " + doubleValue);

                servingWeightText.setText(getString(R.string.grams, returnValue));

            } catch (NumberFormatException e) {
                Log.e(TAG, "NumberFormatException: Input is not string.");
            }
        }
    };

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
        petWeightText = findViewById(R.id.pet_weight);
        petFoodBrandText = findViewById(R.id.pet_food_brand);
        servingWeightText = findViewById(R.id.serving_weight);
        foodFormula = new FoodFormula();

        // Set TextChange Listener
        inputPetWeightText.addTextChangedListener(inputPetWeightWatcher);
    }
}

class FoodFormula {
    String name;
    double minimumPetWeight;
    double minimumFeedingAmount;
    double recommendedServing;
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