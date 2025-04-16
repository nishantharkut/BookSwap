package com.workshop.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private boolean isGuest;  // Track guest mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Guest Mode Status
        isGuest = getIntent().getBooleanExtra("guest", false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // If not in guest mode, check if user is logged in
        if (!isGuest) {
            checkUserLogin();
        } else {
            Toast.makeText(this, "Guest Mode: Limited access", Toast.LENGTH_LONG).show();
        }

        // Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bottom Navigation Setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load Default Home Page
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Home");
            }
        }

        // Navigation Click Listener (Updated)
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String title = "BookSwap";

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                title = "Home";
            }

            else if (item.getItemId() == R.id.nav_sell) {
                if (isGuest) {
                    showGuestRestrictionToast();
                    return false;
                }
                selectedFragment = new SellFragment();
                title = "Sell Your Books";
            }

            else if (item.getItemId() == R.id.nav_auction) {
                if (isGuest){
                    showGuestRestrictionToast();
                    return false;
                }
                selectedFragment = new AuctionFragment();
                title = "Auction";
            }

            else if (item.getItemId() == R.id.nav_messages) {
                if (isGuest) {
                    showGuestRestrictionToast();
                    return false;
                }
                selectedFragment = new MessagesFragment();
                title = "Messages";
            }

            else if (item.getItemId() == R.id.nav_profile) {
                if(isGuest){
                    showGuestRestrictionToast();
                    return false;
                }
                selectedFragment = new ProfileFragment();
                title = "Profile";
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                }
            }
            return true;
        });
    }

    // Check if user is logged in, redirect to login if not
    private void checkUserLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    // Show a warning toast for guest users
    private void showGuestRestrictionToast() {
        Toast.makeText(this, "You must log in to access this feature", Toast.LENGTH_SHORT).show();
    }
    public void navigateToHome() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

}
