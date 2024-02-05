package com.example.rich_stich;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class CustomClothing extends AppCompatActivity {

    private String selectedGender,selectedApparel;
    Button getMeasurements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clothing);


        getMeasurements = findViewById(R.id.measurementsBtn);
        //spinner to select gender from user
        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        Spinner apparelSpinner = findViewById(R.id.apparelSpinner);
        Spinner materialSpinner = findViewById(R.id.materialSpinner);
        Spinner measurementsSpinner = findViewById(R.id.measurementsSpinner);


        //gender selector adapter
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        //apparel selector
        ArrayAdapter<CharSequence> apparelAdapter = ArrayAdapter.createFromResource(this,
                R.array.apparel_options, android.R.layout.simple_spinner_item);
        apparelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        apparelSpinner.setAdapter(apparelAdapter);

        //material selector
        ArrayAdapter<CharSequence> materialAdapter = ArrayAdapter.createFromResource(this,
                R.array.material_options, android.R.layout.simple_spinner_item);
        materialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(materialAdapter);

        // Set up measurements spinner initially
        final ArrayAdapter<CharSequence> measurementsAdapter = ArrayAdapter.createFromResource(this,
                R.array.measurements_shirt_male, android.R.layout.simple_spinner_item);
        measurementsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurementsSpinner.setAdapter(measurementsAdapter);


        //gender selector
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedGender = parentView.getItemAtPosition(position).toString();
                Toast.makeText(CustomClothing.this, "Selected Gender: " + selectedGender, Toast.LENGTH_SHORT).show();
                updateApparelOptions(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Apparel spinner item selection listener
        apparelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedApparel = parentView.getItemAtPosition(position).toString();
                updateMeasurementsOptions(selectedGender,selectedApparel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        getMeasurements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMeasurementsInputDialog(selectedGender,selectedApparel);
            }
        });
    }



    private void updateApparelOptions(String selectedGender) {
        // You can implement logic here to update the apparel options based on gender
        // For simplicity, let's assume that for Male, we only have Shirt, and for Female, we have Dress
        Spinner apparelSpinner = findViewById(R.id.apparelSpinner);
        ArrayAdapter<CharSequence> apparelAdapter = ArrayAdapter.createFromResource(this,
                    (selectedGender.equals("Male")) ? R.array.male_apparel_options : R.array.female_apparel_options,
                android.R.layout.simple_spinner_item);
        apparelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        apparelSpinner.setAdapter(apparelAdapter);
    }

    private void updateMeasurementsOptions(String selectedGender, String selectedApparel) {
        Spinner measurementsSpinner = findViewById(R.id.measurementsSpinner);
        int measurementsArrayId = 0; // Default value

        if (selectedGender.equals("Male")) {
            if (selectedApparel.equals("Shirt")) {
                measurementsArrayId = R.array.measurements_shirt_male;
            } else if (selectedApparel.equals("T-Shirt")) {
                measurementsArrayId = R.array.measurements_tshirt_male;
            } else if (selectedApparel.equals("Pant")) {
                measurementsArrayId = R.array.measurements_pant_male;
            } else if (selectedApparel.equals("Coat")) {
                measurementsArrayId = R.array.measurements_coat_male;
            } else if (selectedApparel.equals("Jeans")) {
                measurementsArrayId = R.array.measurements_jeans_male;
            } else if (selectedApparel.equals("Hoodie")) {
                measurementsArrayId = R.array.measurements_hoodie_male;
            } else if (selectedApparel.equals("Suit")) {
                measurementsArrayId = R.array.measurements_suit_male;
            }
        } else if (selectedGender.equals("Female")) {
            if (selectedApparel.equals("Dress")) {
                measurementsArrayId = R.array.measurements_dress_female;
            } else if (selectedApparel.equals("Skirt")) {
                measurementsArrayId = R.array.measurements_skirt_female;
            } else if (selectedApparel.equals("Leggings")) {
                measurementsArrayId = R.array.measurements_leggings_female;
            } else if (selectedApparel.equals("Coat")) {
                measurementsArrayId = R.array.measurements_coat_female;
            } else if (selectedApparel.equals("Blouse")) {
                measurementsArrayId = R.array.measurements_blouse_female;
            }
        }

        ArrayAdapter<CharSequence> measurementsAdapter = ArrayAdapter.createFromResource(this,
                measurementsArrayId, android.R.layout.simple_spinner_item);
        measurementsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurementsSpinner.setAdapter(measurementsAdapter);
    }

    private void showMeasurementsInputDialog(String selectedGender, String selectedApparel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Measurements");

        // Inflate the dynamic layout for measurements input
        //View dialogView = getLayoutInflater().inflate(R.layout.activity_custom_clothing, null);
        //LinearLayout measurementsLayout = dialogView.findViewById(R.id.measurementsLayout);

        // Inflate the dynamic layout for measurements input
        View dialogView = getLayoutInflater().inflate(R.layout.measurements_input_dialog, null);
        LinearLayout measurementsLayout = dialogView.findViewById(R.id.measurementsLayout);


        // Clear existing views in the layout
        measurementsLayout.removeAllViews();

        // Add input fields dynamically based on selected gender and apparel
        String[] measurementsArray = getMeasurementsArray(selectedGender, selectedApparel);
        for (String measurement : measurementsArray) {
            EditText editText = new EditText(this);
            editText.setHint(measurement);  // Set hint to display the type of measurement
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            editText.setLayoutParams(layoutParams);
            measurementsLayout.addView(editText);
        }

        builder.setView(dialogView);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve measurements from dynamically created input fields
                // ...

                // Perform validation and further processing here
                // You can save the measurements to variables, a database, or process them as needed
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private String[] getMeasurementsArray(String selectedGender, String selectedApparel) {

        if (selectedGender.equals("Male")) {
            if (selectedApparel.equals("Shirt")) {
                return getResources().getStringArray(R.array.measurements_shirt_male);
            } else if (selectedApparel.equals("T-Shirt")) {
                return getResources().getStringArray(R.array.measurements_tshirt_male);
            } else if (selectedApparel.equals("Pant")) {
                return getResources().getStringArray(R.array.measurements_pant_male);
            } else if (selectedApparel.equals("Coat")) {
                return getResources().getStringArray(R.array.measurements_coat_male);
            } else if (selectedApparel.equals("Jeans")) {
                return getResources().getStringArray(R.array.measurements_jeans_male);
            } else if (selectedApparel.equals("Hoodie")) {
                return getResources().getStringArray(R.array.measurements_hoodie_male);
            } else if (selectedApparel.equals("Suit")) {
                return getResources().getStringArray(R.array.measurements_suit_male);
            }
        } else if (selectedGender.equals("Female")) {
            if (selectedApparel.equals("Dress")) {
                return getResources().getStringArray( R.array.measurements_dress_female);
            } else if (selectedApparel.equals("Skirt")) {
                return getResources().getStringArray( R.array.measurements_skirt_female);
            } else if (selectedApparel.equals("Leggings")) {
                return getResources().getStringArray(R.array.measurements_leggings_female);
            } else if (selectedApparel.equals("Coat")) {
                return getResources().getStringArray(R.array.measurements_coat_female);
            } else if (selectedApparel.equals("Blouse")) {
                return getResources().getStringArray(R.array.measurements_blouse_female);
            }
        }

        // Default: return an empty array or handle other cases as needed
        return new String[0];
    }

}