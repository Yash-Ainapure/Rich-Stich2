package com.example.rich_stich;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MeasurementActivity extends AppCompatActivity {

    Button measurementsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);

        Intent intent = getIntent();
        String material=(String) intent.getSerializableExtra("selectedMaterial");
        GenderAndApparelSelection obj=(GenderAndApparelSelection) intent.getSerializableExtra("genderAndApparel");
        String[] measurements=obj.getMeasurements();

        LinearLayout layout = findViewById(R.id.linearLayout);

        ArrayList<String> hintsList = new ArrayList<>();
        ArrayList<String> measurementsList = new ArrayList<>();

        measurementsBtn = findViewById(R.id.getMeasurements);

        measurementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < measurements.length; i++) {
                    EditText editText = (EditText) layout.getChildAt(i); // Get the EditText directly from the layout

                    String measurementValue = editText.getText().toString();
                    hintsList.set(i, measurementValue);
                    measurementsList.set(i, measurementValue);

                    Log.d("MeasurementActivity", "Measurement: " + measurementsList.get(i));
                }
            }
        });


//        measurementsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                for (int i = 0; i < measurements.length; i++) {
//                    EditText editText = findViewById(layout.getChildAt(i).getId());
//                    String measurementValue = editText.getText().toString();
//                    hintsList.set(i,measurementValue);
//                    measurementsList.set(i, measurementValue);
//
//                    for (int j = 0; j <hintsList.size(); j++) {
//                        Log.d("MeasurementActivity", "Measurement: " + measurementsList.get(j));
//                    }
//                    for (int j = 0; j <measurementsList.size(); j++) {
//                        Log.d("MeasurementActivity", "Measurement: " + measurementsList.get(j));
//                    }
//                }
//            }
//        });



        //display edit texts of measurements
        for (String measurement : measurements) {
            EditText editText = new EditText(this);
            editText.setHint(measurement);
            editText.setId(View.generateViewId());

            // You can customize EditText properties here
            layout.addView(editText);
        }

    }
}