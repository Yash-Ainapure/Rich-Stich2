package com.example.rich_stich;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ComfirmPayment extends AppCompatActivity {

    TextView totalPriceTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_payment);


        Intent intent = getIntent();
        String totalPrice=(String) intent.getSerializableExtra("totalPrice").toString();
        totalPriceTextView=findViewById(R.id.TotalPrice);
        totalPriceTextView.setText("Total Price : "+totalPrice);
    }
}