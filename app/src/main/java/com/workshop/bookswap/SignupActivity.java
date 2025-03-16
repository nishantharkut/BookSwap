package com.workshop.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signup_name, signup_roll_number, signup_email, signup_password;
    TextView btn_login;
    Button signup_button, btn_guest;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_roll_number = findViewById(R.id.signup_roll_number);
        signup_name = findViewById(R.id.signup_name);
        btn_login = findViewById(R.id.btn_login);
        signup_button = findViewById(R.id.signup_button);
        btn_guest = findViewById(R.id.btn_guest);
        progressBar = findViewById(R.id.progressBar);
        // Guest login click listener
        btn_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guestIntent = new Intent(SignupActivity.this, MainActivity.class);
                guestIntent.putExtra("guest", true); // Pass guest flag
                startActivity(guestIntent);
                finish(); // Finish login activity
            }
        });
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = signup_name.getText().toString().trim();
                String roll_number = signup_roll_number.getText().toString().trim();
                String email = signup_email.getText().toString().trim();
                String password = signup_password.getText().toString().trim();

                if (name.isEmpty() || roll_number.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    String userID = user.getUid();
                                    database = FirebaseDatabase.getInstance();
                                    reference = database.getReference("users");

                                    // Create user object
                                    HelperClass helperClass = new HelperClass(name, roll_number, email, password);

                                    // Store user data under their UID
                                    reference.child(userID).setValue(helperClass)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(SignupActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignupActivity.this, "Failed to store user data!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                if (task.getException() != null) {
                                    Toast.makeText(SignupActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SignupActivity.this, "Registration Failed!", Toast.LENGTH_LONG).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        });
            }
        });


        btn_login.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
    }
}