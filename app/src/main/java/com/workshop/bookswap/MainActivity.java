package com.workshop.bookswap;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load Default Home Page
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        getSupportActionBar().setTitle("Home");

        // Navigation Click Listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                String title = "BookSwap";

                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                    title = "Home";
                } else if (item.getItemId() == R.id.nav_sell) {
                    selectedFragment = new SellFragment();
                    title = "Sell a Book";
                } else if (item.getItemId() == R.id.nav_auction) {
                    selectedFragment = new AuctionFragment();
                    title = "Auction";
                } else if (item.getItemId() == R.id.nav_messages) {
                    selectedFragment = new MessagesFragment();
                    title = "Messages";
                } else if (item.getItemId() == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                    title = "Profile";
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    getSupportActionBar().setTitle(title);
                }

                return true;
            }
        });
    }
}
