package com.example.rich_stich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Payment extends AppCompatActivity {

    TextView fabricCost,WorkingCost,DeliveryCharges,TotalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        String material=(String) intent.getSerializableExtra("selectedMaterial");
        GenderAndApparelSelection obj=(GenderAndApparelSelection) intent.getSerializableExtra("genderAndApparel");
        ArrayList<String> measurementsList = (ArrayList<String>) intent.getSerializableExtra("measurements");
        ArrayList<String> hintsList = (ArrayList<String>) intent.getSerializableExtra("hints");
        String price=(String) intent.getSerializableExtra("materialPrice");


        LinearLayout layout = findViewById(R.id.linearLayout);
        ImageView imageView = findViewById(R.id.materialImageView);
        TextView apparelTextView = findViewById(R.id.apparelTextView);
        Button payButton = findViewById(R.id.payButton);
        fabricCost=findViewById(R.id.fabricCost);
        WorkingCost=findViewById(R.id.workingcost);
        DeliveryCharges=findViewById(R.id.DeleveryCharges);
        TotalPrice=findViewById(R.id.TotalPrice);

        //displaying image in the image view
        DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference("imageCollection").child(material);
        imagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.child("url").getValue(String.class);
                Picasso.get().load(imageUrl).into(imageView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        apparelTextView.setText("Apparel : "+obj.getApparel());
        fabricCost.setText("Fabric Cost : "+price);
        WorkingCost.setText("Working Cost : 1000");
        DeliveryCharges.setText("Delivery Charges : 100");
        TotalPrice.setText("Total Price : "+(Integer.parseInt(price)+1000+100));


        Log.d("MyActivity", "Material Price : " + price);
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

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, ComfirmPayment.class);
                intent.putExtra("totalPrice", (Integer.parseInt(price)+1000+100));
                startActivity(intent);
            }
        });


        for (int i = 0; i < measurementsList.size(); i++) {
            TextView tv = new TextView(this);
            tv.setText(hintsList.get(i)+" : "+measurementsList.get(i));
            tv.setGravity(Gravity.CENTER);

            // You can customize EditText properties here
            layout.addView(tv);
        }
    }
}