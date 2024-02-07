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
import java.util.Arrays;

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
        String price=(String) intent.getSerializableExtra("materialPrice");

        LinearLayout layout = findViewById(R.id.linearLayout);

        ArrayList<String> hintsList = new ArrayList<>();
        ArrayList<String> measurementsList = new ArrayList<>();

        measurementsBtn = findViewById(R.id.getMeasurements);

        measurementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                measurementsList.clear();
                for (int i = 0; i < measurements.length; i++) {
                    EditText editText = findViewById(layout.getChildAt(i).getId());
                    String measurementValue = editText.getText().toString();
                    measurementsList.add(measurementValue);
                }

                Intent intent = new Intent(MeasurementActivity.this, Payment.class);
                intent.putExtra("selectedMaterial", material);
                intent.putExtra("genderAndApparel", obj);
                intent.putExtra("measurements", measurementsList);
                intent.putExtra("hints", hintsList);
                intent.putExtra("materialPrice", price);
                startActivity(intent);
            }
        });


        //display edit texts of measurements
        for (String measurement : measurements) {
            EditText editText = new EditText(this);
            hintsList.add(measurement);
            editText.setHint(measurement);
            editText.setId(View.generateViewId());

            // You can customize EditText properties here
            layout.addView(editText);
        }

    }
}