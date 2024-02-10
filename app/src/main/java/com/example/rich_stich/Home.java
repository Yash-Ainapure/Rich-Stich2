package com.example.rich_stich;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Home extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout newCustomer;
    ActionBarDrawerToggle actionBarDrawerTogg1e;
    TextView username;
    LinearLayout customClothing;
    private FirebaseAuth mAuth;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerTogg1e.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Finish the activity and exit the app
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        newCustomer=findViewById(R.id.linearLayout5);
        customClothing=findViewById(R.id.linearLayout2);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerTogg1e = new ActionBarDrawerToggle(this , drawerLayout , R.string.menu_open, R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerTogg1e);
        actionBarDrawerTogg1e.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Home");
        }
        View headerView = navigationView.getHeaderView(0);
        username=headerView.findViewById(R.id.username);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        username.setText(user.getEmail());

        //custom clothing click listener
        customClothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,CustomClothing.class));
            }
        });
        newCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,AddNewCustomer.class));
            }
        });

        //sidebar navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_account) {
                    startActivity(new Intent(Home.this,MyAccount.class));
                } else if (itemId == R.id.view_manage) {
                    Log.i("MENU_DRAWER_TAG", "View/Manage is clicked");
                    //startActivity(new Intent(Home.this,MyAccount.class));
                } else if (itemId == R.id.wishlist) {
                    Log.i("MENU_DRAWER_TAG", "Wishlist is clicked");
                    //startActivity(new Intent(Home.this,MyAccount.class));
                }
                else if (itemId == R.id.logout) {
                    mAuth=FirebaseAuth.getInstance();
                    mAuth.signOut();
                    startActivity(new Intent(Home.this,MainActivity.class));
                    Toast.makeText(Home.this, "user logged out", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });


        //set profile image
        ImageView profileImage=headerView.findViewById(R.id.imageView);
        StorageReference storageReference;
        FirebaseAuth mAuth2 = FirebaseAuth.getInstance();
        String Uid=mAuth2.getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference("UserProfileImages/"+Uid);
        try {
            File localfile= File.createTempFile("tempfile",".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    DisplayMetrics dm=new DisplayMetrics();
                    Bitmap bitmap= BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    profileImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Home.this, "failed to load profile image", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}