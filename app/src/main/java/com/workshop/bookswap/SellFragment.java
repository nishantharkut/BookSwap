//package com.workshop.bookswap;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class SellFragment extends Fragment {
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private Uri imageUri;
//    private ImageView imageBook;
//    private Button btnSelectImage, btnUploadImage, btnSellBook;
//    private EditText editBookTitle, editBookPrice;
//    private Spinner spinnerCategory, spinnerCondition;
//    private String uploadedImageUrl;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_sell, container, false);
//
//        // Initialize UI components
//        imageBook = view.findViewById(R.id.image_book);
//        btnSelectImage = view.findViewById(R.id.btn_select_image);
//        btnUploadImage = view.findViewById(R.id.btn_upload_image);
//        btnSellBook = view.findViewById(R.id.btn_sell_book);
//        editBookTitle = view.findViewById(R.id.edit_book_title);
//        editBookPrice = view.findViewById(R.id.edit_book_price);
//        spinnerCategory = view.findViewById(R.id.spinner_category);
//        spinnerCondition = view.findViewById(R.id.spinner_condition);
//
//        // Select Image
//        btnSelectImage.setOnClickListener(v -> openFileChooser());
//
//        // Upload Image
//        btnUploadImage.setOnClickListener(v -> {
//            if (imageUri != null) {
//                uploadImageToCloudinary();
//            } else {
//                Toast.makeText(getActivity(), "Please select an image first!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Sell Book
//        btnSellBook.setOnClickListener(v -> {
//            if (uploadedImageUrl == null || uploadedImageUrl.isEmpty()) {
//                Toast.makeText(getActivity(), "Please upload an image first!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            saveBookToFirebase();
//        });
//
//        return view;
//    }
//
//    // Open Gallery to Select Image
//    private void openFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//
//    // Handle Selected Image
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            imageBook.setImageURI(imageUri); // Show preview
//        }
//    }
//
//    // Upload Image to Cloudinary
//    private void uploadImageToCloudinary() {
//        Toast.makeText(getActivity(), "Uploading image...", Toast.LENGTH_SHORT).show();
//
//        CloudinaryHelper.uploadImage(imageUri, getActivity(), imageUrl -> {
//            if (imageUrl != null) {
//                uploadedImageUrl = imageUrl;
//                Toast.makeText(getActivity(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getActivity(), "Image upload failed!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // Save Book to Firebase Firestore
//    private void saveBookToFirebase() {
//        String title = editBookTitle.getText().toString().trim();
//        String priceInput = editBookPrice.getText().toString().trim();
//        String category = spinnerCategory.getSelectedItem().toString();
//        String condition = spinnerCondition.getSelectedItem().toString();
//
//        if (title.isEmpty() || priceInput.isEmpty()) {
//            Toast.makeText(getActivity(), "Please fill in all fields!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        double price;
//        try {
//            price = Double.parseDouble(priceInput); // ✅ Convert to double
//        } catch (NumberFormatException e) {
//            Toast.makeText(getActivity(), "Invalid price format!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        // Check if user is logged in
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user == null) {
//            Toast.makeText(getActivity(), "You must be logged in to sell!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String sellerId = user.getUid();
//        String sellerName = user.getDisplayName();
//
//        // Create book map
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> book = new HashMap<>();
//        book.put("title", title);
//        book.put("isAuction", false);
//        book.put("price", price); // ✅ Store as number
//        book.put("category", category);
//        book.put("condition", condition);
//        book.put("imageUrl", uploadedImageUrl);
//        book.put("timestamp", FieldValue.serverTimestamp());
//        book.put("isFeatured", false);
//        book.put("popularity", 0);
//        book.put("sellerId", sellerId);          // ✅ Save seller ID
//        book.put("sellerName", sellerName);      // ✅ Save seller name
//
//        db.collection("books").add(book)
//                .addOnSuccessListener(documentReference -> {
//                    Toast.makeText(getActivity(), "Book listed successfully!", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(getActivity(), "Error listing book!", Toast.LENGTH_SHORT).show();
//                });
//    }
//}

package com.workshop.bookswap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SellFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageBook;
    private Button btnSelectImage, btnUploadImage, btnSellBook;
    private EditText editBookTitle, editBookPrice;
    private Spinner spinnerCategory, spinnerCondition;
    private String uploadedImageUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);

        // Initialize UI components
        imageBook = view.findViewById(R.id.image_book);
        btnSelectImage = view.findViewById(R.id.btn_select_image);
        btnUploadImage = view.findViewById(R.id.btn_upload_image);
        btnSellBook = view.findViewById(R.id.btn_sell_book);
        editBookTitle = view.findViewById(R.id.edit_book_title);
        editBookPrice = view.findViewById(R.id.edit_book_price);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        spinnerCondition = view.findViewById(R.id.spinner_condition);

        // Select Image
        btnSelectImage.setOnClickListener(v -> openFileChooser());

        // Upload Image
        btnUploadImage.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImageToCloudinary();
            } else {
                Toast.makeText(getActivity(), "Please select an image first!", Toast.LENGTH_SHORT).show();
            }
        });

        // Sell Book
        btnSellBook.setOnClickListener(v -> {
            if (uploadedImageUrl == null || uploadedImageUrl.isEmpty()) {
                Toast.makeText(getActivity(), "Please upload an image first!", Toast.LENGTH_SHORT).show();
                return;
            }
            saveBookToFirebase();
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageBook.setImageURI(imageUri); // Show preview
        }
    }

    private void uploadImageToCloudinary() {
        Toast.makeText(getActivity(), "Uploading image...", Toast.LENGTH_SHORT).show();

        CloudinaryHelper.uploadImage(imageUri, getActivity(), imageUrl -> {
            if (imageUrl != null) {
                uploadedImageUrl = imageUrl;
                Toast.makeText(getActivity(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Image upload failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBookToFirebase() {
        String title = editBookTitle.getText().toString().trim();
        String priceInput = editBookPrice.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String condition = spinnerCondition.getSelectedItem().toString();

        if (title.isEmpty() || priceInput.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Invalid price format!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getActivity(), "You must be logged in to sell!", Toast.LENGTH_SHORT).show();
            return;
        }

        String sellerId = user.getUid();

        // 🔥 Fetch sellerName from Realtime Database using UID
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(sellerId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sellerName = snapshot.child("name").getValue(String.class);

                if (sellerName == null) {
                    sellerName = "Unknown Seller";
                }

                // ✅ Now build the book data
                Map<String, Object> book = new HashMap<>();
                book.put("title", title);
                book.put("isAuction", false);
                book.put("price", price);
                book.put("category", category);
                book.put("condition", condition);
                book.put("imageUrl", uploadedImageUrl);
                book.put("timestamp", FieldValue.serverTimestamp());
                book.put("isFeatured", false);
                book.put("popularity", 0);
                book.put("sellerId", sellerId);
                book.put("sellerName", sellerName); // ✅ Added from Realtime DB

                // ✅ Upload to Firestore
                FirebaseFirestore.getInstance().collection("books").add(book)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getActivity(), "Book listed successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error listing book!", Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to fetch user info!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

