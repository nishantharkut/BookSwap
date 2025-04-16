//package com.workshop.bookswap;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//public class ProfileFragment extends Fragment {
//
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//    private TextView fullNameText, rollNumberText, emailText, universityText, usernameText;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        fullNameText = view.findViewById(R.id.text_full_name);
//        rollNumberText = view.findViewById(R.id.text_roll_number);
//        emailText = view.findViewById(R.id.text_email);
//        universityText = view.findViewById(R.id.text_university);
//        usernameText = view.findViewById(R.id.text_username);
//        Button logoutButton = view.findViewById(R.id.btn_logout);
//
//        loadUserData();
//
//        logoutButton.setOnClickListener(v -> {
//            mAuth.signOut();
//            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//            requireActivity().finish();
//        });
//
//        return view;
//    }
//
//    private void loadUserData() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            String uid = user.getUid();
//            db.collection("users").document(uid)
//                    .get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//                            String name = documentSnapshot.getString("fullName");
//                            String roll = documentSnapshot.getString("rollNumber");
//                            String email = documentSnapshot.getString("email");
//                            String university = documentSnapshot.getString("university");
//
//                            fullNameText.setText("Full Name: " + name);
//                            rollNumberText.setText("Roll Number: " + roll);
//                            emailText.setText("Email: " + email);
//                            universityText.setText("University: " + university);
//                            usernameText.setText(name);
//                        }
//                    })
//                    .addOnFailureListener(e ->
//                            Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show()
//                    );
//        }
//    }
//}

package com.workshop.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class ProfileFragment extends Fragment {

    private TextView profileName, profileEmail, profileRoll, profileUniversity;
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        profileRoll = view.findViewById(R.id.profile_roll);
        profileUniversity = view.findViewById(R.id.profile_university);
        btnLogout = view.findViewById(R.id.btn_logout);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HelperClass userData = snapshot.getValue(HelperClass.class);
                    if (userData != null) {
                        profileName.setText("Full Name: "+userData.getName());
                        profileEmail.setText("Email-Id: "+userData.getEmail());
                        profileRoll.setText("Roll Number: "+userData.getRoll_number());
                        profileUniversity.setText("University: ABV-IIITM Gwalior");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}


