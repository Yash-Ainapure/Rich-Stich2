package com.example.rich_stich;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity {

    EditText etEmail, etPassword,etPhone, etConfirmPassword,etName;
    Button btnRegister;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        etName=findViewById(R.id.editTextText);
        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etPhone = findViewById(R.id.editTextPhone);
        etPassword = findViewById(R.id.editTextTextPassword1);
        etConfirmPassword = findViewById(R.id.editTextTextPassword2);
        btnRegister = findViewById(R.id.button);

        btnRegister.setOnClickListener(v -> {
            String name=etName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            if (email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
                etEmail.setError("Please fill in the required fields");
                etPhone.setError("Please fill in the required fields");
                etPassword.setError("Please fill in the required fields");
                etConfirmPassword.setError("Please fill in the required fields");
                etName.setError("Please fill in the required fields");
            } else if (!password.equals(confirmPassword)) {
                etPassword.setError("Passwords do not match");
                etConfirmPassword.setError("Passwords do not match");
            } else {
                // Register the user using firebase authentication

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterUser.this, task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegisterUser.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                    else{

                        saveAdditionalUserInfo(task.getResult().getUser().getUid(),name,phone,email);

                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                        Toast.makeText(this, "registered successfully,now you can login with the credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void saveAdditionalUserInfo(String userId, String name, String phoneNumber, String email) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("userInfo");
        UserInfo userInfo = new UserInfo(userId,name,phoneNumber,email);
        databaseReference.setValue(userInfo);

    }
}