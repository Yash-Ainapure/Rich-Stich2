package com.example.rich_stich;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        String material=(String) intent.getSerializableExtra("selectedMaterial");
        GenderAndApparelSelection obj=(GenderAndApparelSelection) intent.getSerializableExtra("genderAndApparel");
        ArrayList<String> measurementsList = (ArrayList<String>) intent.getSerializableExtra("measurements");
        ArrayList<String> hintsList = (ArrayList<String>) intent.getSerializableExtra("hints");

        Log.d("MyActivity", "Material: " + material);

        Log.d("MyActivity", "Gender : " + obj.getGender());
        Log.d("MyActivity", "Apparel : " + obj.getApparel());

        Log.d("MyActivity", "Measurements List:");
        for (String measurement : measurementsList) {
            Log.d("MyActivity", "   " + measurement);
        }

        Log.d("MyActivity", "Hints List:");
        for (String hint : hintsList) {
            Log.d("MyActivity", "   " + hint);
        }

    }
}