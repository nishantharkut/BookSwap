package com.workshop.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import com.squareup.picasso.Picasso;
import com.workshop.bookswap.models.Book;

import java.util.Locale;

public class BookDetailsActivity extends AppCompatActivity {

    private ImageView bookImage;
    private TextView titleText, authorText, priceText, conditionText, categoryText, countdownText;
    private Button btnBuyNow, btnRent, btnChat;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String currentUserId = auth.getCurrentUser().getUid();
    private CountDownTimer auctionTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Initialize UI
        bookImage = findViewById(R.id.book_image);
        titleText = findViewById(R.id.book_title);
        authorText = findViewById(R.id.book_author);
        priceText = findViewById(R.id.book_price);
        conditionText = findViewById(R.id.book_condition);
        categoryText = findViewById(R.id.book_category);
        countdownText = findViewById(R.id.book_auction_countdown);

        btnBuyNow = findViewById(R.id.btn_buy_now);
        btnRent = findViewById(R.id.btn_rent);
        btnChat = findViewById(R.id.btn_chat);

        // Get book ID from intent
        String bookId = getIntent().getStringExtra("BOOK_ID");
        if (bookId == null) {
            Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadBookDetails(bookId);
    }

    private void loadBookDetails(String bookId) {
        FirebaseFirestore.getInstance().collection("books").document(bookId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Book book = snapshot.toObject(Book.class);
                        if (book != null) {
                            book.setId(snapshot.getId()); // Important!
                            displayBook(book);
                        }
                    } else {
                        Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load book details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void displayBook(Book book) {
        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        priceText.setText(String.format(Locale.getDefault(), "₹%.2f", book.getPrice()));
        conditionText.setText("Condition: " + book.getCondition());
        categoryText.setText("Category: " + book.getCategory());

        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            Picasso.get().load(book.getImageUrl()).placeholder(R.drawable.sample_book).into(bookImage);
        } else {
            bookImage.setImageResource(R.drawable.sample_book);
        }

        // Auction countdown
        if (book.isAuction()) {
            long millisLeft = book.getAuctionEndTime() - System.currentTimeMillis();
            if (millisLeft > 0) {
                countdownText.setVisibility(View.VISIBLE);
                auctionTimer = new CountDownTimer(millisLeft, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long seconds = millisUntilFinished / 1000;
                        long hours = seconds / 3600;
                        long minutes = (seconds % 3600) / 60;
                        long secs = seconds % 60;
                        String timeLeft = String.format(Locale.getDefault(),
                                "Ends in: %02dh %02dm %02ds", hours, minutes, secs);
                        countdownText.setText(timeLeft);
                    }

                    @Override
                    public void onFinish() {
                        countdownText.setText("Auction ended");
                    }
                }.start();
            } else {
                countdownText.setVisibility(View.VISIBLE);
                countdownText.setText("Auction ended");
            }
        } else {
            countdownText.setVisibility(View.GONE);
        }

        // TODO: Set up button actions
        btnBuyNow.setOnClickListener(v -> {
            DatabaseReference purchasesRef = FirebaseDatabase.getInstance()
                    .getReference("purchases")
                    .child(currentUserId)
                    .child(book.getId());

            purchasesRef.setValue(book).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Purchase successful!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to purchase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        btnRent.setOnClickListener(v -> {
            DatabaseReference rentRequestsRef = FirebaseDatabase.getInstance()
                    .getReference("rentRequests")
                    .child(book.getId())
                    .child(currentUserId);

            rentRequestsRef.setValue(true).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Rent request sent!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to send request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        btnChat.setOnClickListener(v -> {
            if (book.getSellerId() == null || book.getSellerId().equals(currentUserId)) {
                Toast.makeText(this, "You cannot buy the book you listed", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("receiverId", book.getSellerId());
            intent.putExtra("receiverName", book.getSellerName()); // Optional
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (auctionTimer != null) {
            auctionTimer.cancel();
        }
    }
}
