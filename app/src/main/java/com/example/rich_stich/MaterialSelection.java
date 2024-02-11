package com.example.rich_stich;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class MaterialSelection extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Handle the back button
            case android.R.id.home:
                onBackPressed(); // This will call the default back button behavior
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    TextView selectedMaterial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_selection);
        getSupportActionBar().setTitle("Material Selection");
        selectedMaterial = findViewById(R.id.selectedMaterial);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
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
                List<String> materialName = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageKey = snapshot.getKey(); // Get the key of the image
                    String imageUrl = snapshot.child("url").getValue(String.class);
                    String priceValue = snapshot.child("price").getValue(Integer.class).toString();
                    String materialname = snapshot.child("name").getValue(String.class).toString();

                    prices.add(priceValue);
                    imageKeys.add(imageKey);
                    imageUrls.add(imageUrl);
                    materialName.add(materialname);
                }
                displayImages(imageUrls, imageKeys, prices , materialName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

    }


    private void displayImages(List<String> imageUrls, final List<String> imageKeys, List<String> prices , List<String> materialName) {
        GridLayout gridLayout = findViewById(R.id.imageContainerLayout);

        gridLayout.setColumnCount(2); // Set the number of columns

        for (int i = 0; i < imageUrls.size(); i++) {
            final int position = i;

            // Inflate the material_item_layout for each material
            View materialItemView = LayoutInflater.from(this).inflate(R.layout.material_item_layout, null);
            ImageView imageView = materialItemView.findViewById(R.id.imageView2);
            TextView nameTextView = materialItemView.findViewById(R.id.textView6);
            TextView priceTextView = materialItemView.findViewById(R.id.textView10);

            // Set data for each material
            nameTextView.setText(materialName.get(position)); // Replace with your actual data
            priceTextView.setText("₹ " + prices.get(position));
            Picasso.get().load(imageUrls.get(position)).into(imageView);

            // Set onClickListener for each material
            materialItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String clickedImageKey = imageKeys.get(position);
                    String price = prices.get(position);
                    String material = materialName.get(position);
                    selectedMaterial.setText("Selected Material :" + material);

                    Toast.makeText(MaterialSelection.this, "Selected Material :" + material, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MaterialSelection.this, MeasurementActivity.class);
                    intent.putExtra("selectedMaterial", clickedImageKey);
                    intent.putExtra("genderAndApparel", getIntent().getSerializableExtra("genderAndApparel"));
                    intent.putExtra("materialPrice", price);
                    startActivity(intent);
                }
            });

            // Set layout parameters for evenly distributed columns
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = 0;
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.setMargins(20, 20, 20, 20);
            layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f);
            materialItemView.setLayoutParams(layoutParams);

            // Add the inflated material item to the GridLayout
            gridLayout.addView(materialItemView);
        }
    }




}