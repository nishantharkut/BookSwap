package com.workshop.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private TextInputEditText inputEmail, inputPassword, inputName, inputRollNumber;
    private Button btnSignup, btnLogin, btnGuest;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputName = findViewById(R.id.name);
        inputRollNumber = findViewById(R.id.roll_number);
        btnSignup = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);
        btnGuest = findViewById(R.id.btn_guest);
        progressBar = findViewById(R.id.progressBar);

        btnSignup.setOnClickListener(v -> registerUser());
        btnLogin.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
        btnGuest.setOnClickListener(v -> continueAsGuest());
    }

    private void continueAsGuest() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        intent.putExtra("isGuest", true);
        startActivity(intent);
        finish();
    }

    private void registerUser() {
        String email = Objects.requireNonNull(inputEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(inputPassword.getText()).toString().trim();
        String name = Objects.requireNonNull(inputName.getText()).toString().trim();
        String rollNumber = Objects.requireNonNull(inputRollNumber.getText()).toString().trim();

        if (!isValidInput(email, password, name, rollNumber)) return;

        progressBar.setVisibility(View.VISIBLE);
        btnSignup.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveUserToFirestore(user.getUid(), name, email, rollNumber);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        btnSignup.setEnabled(true);
                        showToast("Signup Failed: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    private boolean isValidInput(String email, String password, String name, String rollNumber) {
        if (name.isEmpty()) {
            showToast("Name is required");
            return false;
        }
        if (rollNumber.isEmpty()) {
            showToast("Roll Number is required");
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter a valid email");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            showToast("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    private void saveUserToFirestore(String userId, String name, String email, String rollNumber) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("rollNumber", rollNumber);

        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    btnSignup.setEnabled(true);
                    showToast("Registration Successful!");
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnSignup.setEnabled(true);
                    showToast("Error: " + e.getMessage());
                });
    }

    private void showToast(String message) {
        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
