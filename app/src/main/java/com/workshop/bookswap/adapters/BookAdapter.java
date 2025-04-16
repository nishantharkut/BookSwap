package com.workshop.bookswap.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.workshop.bookswap.BookDetailsActivity;
import com.workshop.bookswap.R;
import com.workshop.bookswap.models.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final Context context;
    private final List<Book> bookList;
    private final HashMap<String, CountDownTimer> timers = new HashMap<>();

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookCondition.setText(book.getCondition());
        holder.bookPrice.setText(String.format(Locale.getDefault(), "₹%.2f", book.getPrice()));

        // Load image
        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            Picasso.get().load(book.getImageUrl()).placeholder(R.drawable.sample_book).into(holder.bookImage);
        } else {
            holder.bookImage.setImageResource(R.drawable.sample_book);
        }

        // Cancel any existing timer for this book
        if (timers.containsKey(book.getId())) {
            timers.get(book.getId()).cancel();
            timers.remove(book.getId());
        }

        // Auction countdown
        if (book.isAuction()) {
            long millisLeft = book.getAuctionEndTime() - System.currentTimeMillis();

            if (millisLeft > 0) {
                holder.bookAuctionCountdown.setVisibility(View.VISIBLE);
                CountDownTimer timer = new CountDownTimer(millisLeft, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long seconds = millisUntilFinished / 1000;
                        long hours = seconds / 3600;
                        long minutes = (seconds % 3600) / 60;
                        long secs = seconds % 60;
                        String timeLeft = String.format(Locale.getDefault(),
                                "Ends in: %02dh %02dm %02ds", hours, minutes, secs);
                        holder.bookAuctionCountdown.setText(timeLeft);
                    }

                    @Override
                    public void onFinish() {
                        holder.bookAuctionCountdown.setText("Auction ended");
                    }
                };
                timer.start();
                timers.put(book.getId(), timer);
            } else {
                holder.bookAuctionCountdown.setVisibility(View.VISIBLE);
                holder.bookAuctionCountdown.setText("Auction ended");
            }
        } else {
            holder.bookAuctionCountdown.setVisibility(View.GONE);
        }

        // On click to open book details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailsActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public void onViewRecycled(@NonNull BookViewHolder holder) {
        super.onViewRecycled(holder);
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            Book book = bookList.get(position);
            if (timers.containsKey(book.getId())) {
                timers.get(book.getId()).cancel();
                timers.remove(book.getId());
            }
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView bookTitle, bookAuthor, bookPrice, bookCondition, bookAuctionCountdown;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_image);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookPrice = itemView.findViewById(R.id.book_price);
            bookCondition = itemView.findViewById(R.id.book_condition);
            bookAuctionCountdown = itemView.findViewById(R.id.book_auction_countdown);
        }
    }
}
