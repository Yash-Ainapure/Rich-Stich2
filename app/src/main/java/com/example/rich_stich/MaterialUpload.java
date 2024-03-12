package com.example.rich_stich;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MaterialUpload extends AppCompatActivity {

    Button b1,b2;
    ImageView iv;
    Uri imageuri;
    ProgressDialog pd;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    EditText nameedt,priceedt;
    String materialName;
    int materialPrice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_upload);

//        intent.putExtra("selectedMaterial", clickedImageKey);
//        intent.putExtra("genderAndApparel", getIntent().getSerializableExtra("genderAndApparel"));
//        intent.putExtra("materialPrice", price);

        b1=(Button) findViewById(R.id.select);
        b2=(Button) findViewById(R.id.upload);
        iv=(ImageView) findViewById(R.id.imageView3);
        nameedt=findViewById(R.id.materialName);
        priceedt=findViewById(R.id.materialPrice);

        mAuth = FirebaseAuth.getInstance();
        Intent intentx = getIntent();
        GenderAndApparelSelection obj= (GenderAndApparelSelection) intentx.getSerializableExtra("genderAndApparel");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                materialName=nameedt.getText().toString();
                String priceString = priceedt.getText().toString().trim();
                if (!priceString.isEmpty()) {
                    try {
                        materialPrice = Integer.parseInt(priceString);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MaterialUpload.this, "price field is facing some problems", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                }

                if(materialName.isEmpty() || materialPrice==0){
                    Toast.makeText(MaterialUpload.this, "name or price field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadImage();
            }
        });
    }

    private void uploadImage() {
        pd = new ProgressDialog(this);
        pd.setTitle("Uploading File....");
        pd.show();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        // Set the path in the storageReference
        storageReference = FirebaseStorage.getInstance().getReference("MaterialImages/" + userId + "/" + System.currentTimeMillis() + ".jpg");

        if(imageuri!=null) {
            storageReference.putFile(imageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    // Now you can store the imageUrl in the Realtime Database
                                    storeImageUrlInDatabase(imageUrl, materialPrice, materialName);

                                    iv.setImageURI(null);
                                    Toast.makeText(MaterialUpload.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                                    if (pd.isShowing()) {
                                        pd.dismiss();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MaterialUpload.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                    });
        }else{
            Toast.makeText(MaterialUpload.this, "select a image first", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
    }

    private void storeImageUrlInDatabase(String url,int price,String name) {
        // Assuming you have a DatabaseReference for your Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("imageCollection");

        // Assuming you have a model class named ImageModel with a field imageUrl
        MaterialModel imageModel = new MaterialModel(url,price,name);

        // Push the image model to the database
        //databaseReference.push().setValue(imageModel);

        databaseReference.push().setValue(imageModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent=new Intent(MaterialUpload.this,MaterialSelection.class);
                        intent.putExtra("genderAndApparel", getIntent().getSerializableExtra("genderAndApparel"));
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MaterialUpload.this, "Data upload failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && data!=null && data.getData() !=null){
            imageuri=data.getData();
            iv.setImageURI(imageuri);
        }
    }
}