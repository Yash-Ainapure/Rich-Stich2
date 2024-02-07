package com.example.rich_stich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MaterialSelection extends AppCompatActivity {

    TextView selectedMaterial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_selection);

        selectedMaterial = findViewById(R.id.selectedMaterial);

        Intent intent = getIntent();
        GenderAndApparelSelection obj= (GenderAndApparelSelection) intent.getSerializableExtra("genderAndApparel");

        //displaying image in the image view
        DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference("imageCollection");
        imagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> imageUrls = new ArrayList<>();
                List<String> imageKeys = new ArrayList<>();
                List<String> prices = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageKey = snapshot.getKey(); // Get the key of the image
                    String imageUrl = snapshot.child("url").getValue(String.class);
                    String priceValue = snapshot.child("price").getValue(Integer.class).toString();

                    prices.add(priceValue);
                    imageKeys.add(imageKey);
                    imageUrls.add(imageUrl);
                }
                displayImages(imageUrls, imageKeys, prices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

    }


    private void displayImages(List<String> imageUrls, final List<String> imageKeys,List<String> prices) {
        LinearLayout linearLayout = findViewById(R.id.imageContainerLayout);

        for (int i = 0; i < imageUrls.size(); i++) {
            final int position = i;
            ImageView imageView = new ImageView(this);
            TextView priceTextView = new EditText(this);


            priceTextView.setText("Price: "+prices.get(position));
            Picasso.get().load(imageUrls.get(position)).into(imageView);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(10, 10, 10, 10);
            imageView.setLayoutParams(layoutParams);
            priceTextView.setLayoutParams(layoutParams);

            linearLayout.addView(priceTextView);
            linearLayout.addView(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String clickedImageKey = imageKeys.get(position);
                    String price = prices.get(position);
                    selectedMaterial.setText("Selected Material : "+clickedImageKey);
                    Toast.makeText(MaterialSelection.this, "Clicked Image Key: " + clickedImageKey, Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(MaterialSelection.this, MeasurementActivity.class);
                    intent.putExtra("selectedMaterial", clickedImageKey);
                    intent.putExtra("genderAndApparel", getIntent().getSerializableExtra("genderAndApparel"));
                    intent.putExtra("materialPrice", price);
                    startActivity(intent);

                }
            });
        }
    }

}