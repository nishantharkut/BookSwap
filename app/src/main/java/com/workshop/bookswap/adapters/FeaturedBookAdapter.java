package com.workshop.bookswap.adapters;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;
import java.util.Locale;

public class FeaturedBookAdapter extends RecyclerView.Adapter<FeaturedBookAdapter.FeaturedBookViewHolder> {

    private final Context context;
    private final List<Book> featuredBooks;

    public FeaturedBookAdapter(Context context, List<Book> featuredBooks) {
        this.context = context;
        this.featuredBooks = featuredBooks;
    }

    @NonNull
    @Override
    public FeaturedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_featured_book, parent, false);

        // Set width to 50% of screen for 2 items per screen
        int screenWidth = parent.getResources().getDisplayMetrics().widthPixels;
        view.getLayoutParams().width = screenWidth / 2;

        return new FeaturedBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedBookViewHolder holder, int position) {
        Book book = featuredBooks.get(position);

        holder.title.setText(book.getTitle());
        holder.price.setText(String.format(Locale.getDefault(), "₹%.2f", book.getPrice()));

        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            Picasso.get().load(book.getImageUrl()).placeholder(R.drawable.sample_book).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.sample_book);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailsActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return featuredBooks.size();
    }

    static class FeaturedBookViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, price;

        public FeaturedBookViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageViewFeaturedBook);
            title = itemView.findViewById(R.id.textViewFeaturedTitle);
            price = itemView.findViewById(R.id.textViewFeaturedPrice);
        }
    }
}
