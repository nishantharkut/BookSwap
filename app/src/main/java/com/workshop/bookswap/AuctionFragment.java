//package com.workshop.bookswap;
//
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.*;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.workshop.bookswap.models.Book;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;
//import java.util.UUID;
//
//public class AuctionFragment extends Fragment {
//
//    private EditText etTitle, etBid;
//    private Spinner spinnerCondition;
//    private TextView tvEndTime;
//    private Button btnStartAuction;
//
//    private long selectedEndTime = 0L;
//    private boolean isAuction;
//    private long auctionEndTime;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_auction, container, false);
//
//        etTitle = view.findViewById(R.id.et_book_title);
//        etBid = view.findViewById(R.id.et_book_starting_bid);
//        spinnerCondition = view.findViewById(R.id.spinner_condition);
//        tvEndTime = view.findViewById(R.id.tv_selected_time);
//        btnStartAuction = view.findViewById(R.id.btn_start_auction);
//
//        // Date & Time Picker
//        tvEndTime.setOnClickListener(v -> showDateTimePicker());
//
//        btnStartAuction.setOnClickListener(v -> uploadAuctionBook());
//
//        return view;
//    }
//
//    private void showDateTimePicker() {
//        Calendar calendar = Calendar.getInstance();
//        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
//            calendar.set(Calendar.YEAR, year);
//            calendar.set(Calendar.MONTH, month);
//            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//            new TimePickerDialog(requireContext(), (view1, hourOfDay, minute) -> {
//                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                calendar.set(Calendar.MINUTE, minute);
//                calendar.set(Calendar.SECOND, 0);
//
//                selectedEndTime = calendar.getTimeInMillis();
//
//                String formatted = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//                        .format(calendar.getTime());
//                tvEndTime.setText("Ends on: " + formatted);
//
//            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
//
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//    }
//
//    private void uploadAuctionBook() {
//        String title = etTitle.getText().toString().trim();
//        String bid = etBid.getText().toString().trim();
//        String condition = spinnerCondition.getSelectedItem().toString();
//
//        if (title.isEmpty() || bid.isEmpty() || selectedEndTime == 0L) {
//            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Book book = new Book(
//                bookId,
//                title,
//                "",
//                Double.parseDouble(bid),
//                "https://via.placeholder.com/150",
//                "Auction",
//                condition,
//                true,
//                selectedEndTime,
//                currentTime,
//                false, // isFeatured
//                "TEMP_USER_ID",
//                "TEMP_USER_NAME",
//                false // isSold
//        );
//
//
//        FirebaseFirestore.getInstance()
//                .collection("books")
//                .document(book.getId())
//                .set(book)
//                .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Auction posted!", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//    }
//}


package com.workshop.bookswap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.firebase.Timestamp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.workshop.bookswap.models.Book;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class AuctionFragment extends Fragment {

    private EditText etTitle, etBid;
    private Spinner spinnerCondition;
    private TextView tvEndTime;
    private Button btnStartAuction;

    private long selectedEndTime = 0L;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_auction, container, false);

        etTitle = view.findViewById(R.id.et_book_title);
        etBid = view.findViewById(R.id.et_book_starting_bid);
        spinnerCondition = view.findViewById(R.id.spinner_condition);
        tvEndTime = view.findViewById(R.id.tv_selected_time);
        btnStartAuction = view.findViewById(R.id.btn_start_auction);

        // Date & Time Picker
        tvEndTime.setOnClickListener(v -> showDateTimePicker());

        btnStartAuction.setOnClickListener(v -> uploadAuctionBook());

        return view;
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(requireContext(), (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                selectedEndTime = calendar.getTimeInMillis();

                String formatted = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                        .format(calendar.getTime());
                tvEndTime.setText("Ends on: " + formatted);

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void uploadAuctionBook() {
        String title = etTitle.getText().toString().trim();
        String bid = etBid.getText().toString().trim();
        String condition = spinnerCondition.getSelectedItem().toString();

        if (title.isEmpty() || bid.isEmpty() || selectedEndTime == 0L) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current user info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String sellerId = user.getUid();
        String sellerName = user.getDisplayName() != null ? user.getDisplayName() : "Unknown Seller";

        String bookId = UUID.randomUUID().toString();
        Timestamp timestamp = Timestamp.now(); // Current server timestamp

        Book book = new Book(
                bookId,
                title,
                "", // Author not asked
                Double.parseDouble(bid),
                "https://via.placeholder.com/150",
                "Auction",
                condition,
                true,
                selectedEndTime,
                timestamp,       // Correct Timestamp object
                false,           // isFeatured
                false,
                sellerId,
                sellerName,
                false            // isSold
        );


        FirebaseFirestore.getInstance()
                .collection("books")
                .document(book.getId())
                .set(book)
                .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Auction posted!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
