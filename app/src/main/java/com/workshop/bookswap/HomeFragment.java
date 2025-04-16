//package com.workshop.bookswap;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.firestore.DocumentChange;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.workshop.bookswap.adapters.BookAdapter;
//import com.workshop.bookswap.models.Book;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HomeFragment extends Fragment {
//
//    private RecyclerView recyclerFeaturedBooks, recyclerRecentBooks, recyclerAuctions;
//    private BookAdapter featuredBookAdapter, recentBookAdapter, auctionBookAdapter;
//    private FirebaseFirestore db;
//    private List<Book> featuredBooksList = new ArrayList<>();
//    private List<Book> recentBooksList = new ArrayList<>();
//    private List<Book> auctionBooksList = new ArrayList<>();
//
//    public HomeFragment() {
//        // Required empty constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        // Initialize Firestore
//        db = FirebaseFirestore.getInstance();
//
//        // Initialize RecyclerViews
//        recyclerFeaturedBooks = view.findViewById(R.id.recycler_featured_books);
//        recyclerRecentBooks = view.findViewById(R.id.recycler_recent_books);
//        recyclerAuctions = view.findViewById(R.id.recycler_auctions);
//
//        // Setup Adapters
//        featuredBookAdapter = new BookAdapter(getContext(), featuredBooksList);
//        recentBookAdapter = new BookAdapter(getContext(), recentBooksList);
//        auctionBookAdapter = new BookAdapter(getContext(), auctionBooksList);
//
//        // Setup RecyclerViews
//        setupRecyclerView(recyclerFeaturedBooks, featuredBookAdapter);
//        setupRecyclerView(recyclerRecentBooks, recentBookAdapter);
//        setupRecyclerView(recyclerAuctions, auctionBookAdapter);
//
//        // Fetch Data from Firestore
//        fetchFeaturedBooks();
//        fetchRecentListings();
//        fetchAuctions();
//
//        return view;
//    }
//
//    private void setupRecyclerView(RecyclerView recyclerView, BookAdapter adapter) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void fetchFeaturedBooks() {
//        db.collection("books")
//                .orderBy("popularity", Query.Direction.DESCENDING)
//                .limit(5)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) return;
//                        updateBookList(value, featuredBooksList, featuredBookAdapter);
//                    }
//                });
//    }
//
//    private void fetchRecentListings() {
//        db.collection("books")
//                .orderBy("timestamp", Query.Direction.DESCENDING)
//                .limit(10)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) return;
//                        updateBookList(value, recentBooksList, recentBookAdapter);
//                    }
//                });
//    }
//
//    private void fetchAuctions() {
//        db.collection("books")
//                .whereEqualTo("isAuction", true)
//                .orderBy("auctionEndTime", Query.Direction.ASCENDING)
//                .limit(5)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) return;
//                        updateBookList(value, auctionBooksList, auctionBookAdapter);
//                    }
//                });
//    }
//
//    private void updateBookList(QuerySnapshot value, List<Book> bookList, BookAdapter adapter) {
//        for (DocumentChange dc : value.getDocumentChanges()) {
//            Book book = dc.getDocument().toObject(Book.class);
//            if (book == null || book.getId() == null) continue;  // defensive null check
//
//            switch (dc.getType()) {
//                case ADDED:
//                    boolean exists = false;
//                    for (Book b : bookList) {
//                        if (b != null && b.getId() != null && b.getId().equals(book.getId())) {
//                            exists = true;
//                            break;
//                        }
//                    }
//                    if (!exists) {
//                        bookList.add(0, book);
//                        adapter.notifyItemInserted(0);
//                    }
//                    break;
//                case MODIFIED:
//                    for (int i = 0; i < bookList.size(); i++) {
//                        Book b = bookList.get(i);
//                        if (b != null && b.getId() != null && b.getId().equals(book.getId())) {
//                            bookList.set(i, book);
//                            adapter.notifyItemChanged(i);
//                            break;
//                        }
//                    }
//                    break;
//                case REMOVED:
//                    for (int i = 0; i < bookList.size(); i++) {
//                        Book b = bookList.get(i);
//                        if (b != null && b.getId() != null && b.getId().equals(book.getId())) {
//                            bookList.remove(i);
//                            adapter.notifyItemRemoved(i);
//                            break;
//                        }
//                    }
//                    break;
//            }
//        }
//    }
//}


//package com.workshop.bookswap;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.firestore.DocumentChange;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.workshop.bookswap.adapters.BookAdapter;
//import com.workshop.bookswap.models.Book;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HomeFragment extends Fragment {
//
//    private RecyclerView recyclerFeaturedBooks, recyclerRecentBooks, recyclerAuctions;
//    private BookAdapter featuredBookAdapter, recentBookAdapter, auctionBookAdapter;
//    private FirebaseFirestore db;
//    private final List<Book> featuredBooksList = new ArrayList<>();
//    private final List<Book> recentBooksList = new ArrayList<>();
//    private final List<Book> auctionBooksList = new ArrayList<>();
//
//    private TextView emptyFeatured, emptyRecent, emptyAuction;
//
//    public HomeFragment() {
//        // Required empty constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        db = FirebaseFirestore.getInstance();
//
//        // Initialize Views
//        recyclerFeaturedBooks = view.findViewById(R.id.recycler_featured_books);
//        recyclerRecentBooks = view.findViewById(R.id.recycler_recent_books);
//        recyclerAuctions = view.findViewById(R.id.recycler_auctions);
//
//        emptyFeatured = view.findViewById(R.id.empty_featured);
//        emptyRecent = view.findViewById(R.id.empty_recent);
//        emptyAuction = view.findViewById(R.id.empty_auction);
//
//        // Setup Adapters
//        featuredBookAdapter = new BookAdapter(getContext(), featuredBooksList);
//        recentBookAdapter = new BookAdapter(getContext(), recentBooksList);
//        auctionBookAdapter = new BookAdapter(getContext(), auctionBooksList);
//
//        setupRecyclerView(recyclerFeaturedBooks, featuredBookAdapter);
//        setupRecyclerView(recyclerRecentBooks, recentBookAdapter);
//        setupRecyclerView(recyclerAuctions, auctionBookAdapter);
//
//        // Fetch Firestore Data
//        fetchFeaturedBooks();
//        fetchRecentListings();
//        fetchAuctions();
//
//        return view;
//    }
//
//    private void setupRecyclerView(RecyclerView recyclerView, BookAdapter adapter) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void fetchFeaturedBooks() {
//        db.collection("books")
//                .orderBy("popularity", Query.Direction.DESCENDING)
//                .limit(5)
//                .addSnapshotListener(getBookEventListener(featuredBooksList, featuredBookAdapter, recyclerFeaturedBooks, emptyFeatured));
//    }
//
//    private void fetchRecentListings() {
//        db.collection("books")
//                .orderBy("timestamp", Query.Direction.DESCENDING)
//                .limit(10)
//                .addSnapshotListener(getBookEventListener(recentBooksList, recentBookAdapter, recyclerRecentBooks, emptyRecent));
//    }
//
//    private void fetchAuctions() {
//        db.collection("books")
//                .whereEqualTo("isAuction", true)
//                .orderBy("auctionEndTime", Query.Direction.ASCENDING)
//                .limit(10)
//                .addSnapshotListener(getBookEventListener(auctionBooksList, auctionBookAdapter, recyclerAuctions, emptyAuction));
//    }
//
//    private EventListener<QuerySnapshot> getBookEventListener(List<Book> bookList, BookAdapter adapter,
//                                                              RecyclerView recyclerView, TextView emptyView) {
//        return (value, error) -> {
//            if (error != null || value == null) return;
//
//            for (DocumentChange dc : value.getDocumentChanges()) {
//                Book book = dc.getDocument().toObject(Book.class);
//                if (book == null || book.getId() == null) continue;
//
//                switch (dc.getType()) {
//                    case ADDED:
//                        handleAddedBook(book, bookList, adapter);
//                        break;
//                    case MODIFIED:
//                        handleModifiedBook(book, bookList, adapter);
//                        break;
//                    case REMOVED:
//                        handleRemovedBook(book, bookList, adapter);
//                        break;
//                }
//            }
//
//            updateEmptyState(bookList, recyclerView, emptyView);
//        };
//    }
//
//    private void handleAddedBook(Book book, List<Book> bookList, BookAdapter adapter) {
//        for (Book b : bookList) {
//            if (b != null && book.getId().equals(b.getId())) return;
//        }
//        bookList.add(book); // adds to the end to avoid visual jump
//        adapter.notifyItemInserted(bookList.size() - 1);
//    }
//
//    private void handleModifiedBook(Book book, List<Book> bookList, BookAdapter adapter) {
//        for (int i = 0; i < bookList.size(); i++) {
//            Book b = bookList.get(i);
//            if (b != null && book.getId().equals(b.getId())) {
//                bookList.set(i, book);
//                adapter.notifyItemChanged(i);
//                return;
//            }
//        }
//    }
//
//    private void handleRemovedBook(Book book, List<Book> bookList, BookAdapter adapter) {
//        for (int i = 0; i < bookList.size(); i++) {
//            Book b = bookList.get(i);
//            if (b != null && book.getId().equals(b.getId())) {
//                bookList.remove(i);
//                adapter.notifyItemRemoved(i);
//                return;
//            }
//        }
//    }
//
//    private void updateEmptyState(List<Book> bookList, RecyclerView recyclerView, TextView emptyView) {
//        if (bookList.isEmpty()) {
//            recyclerView.setVisibility(View.GONE);
//            emptyView.setVisibility(View.VISIBLE);
//        } else {
//            recyclerView.setVisibility(View.VISIBLE);
//            emptyView.setVisibility(View.GONE);
//        }
//    }
//}

package com.workshop.bookswap;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.workshop.bookswap.adapters.BookAdapter;
import com.workshop.bookswap.adapters.FeaturedBookAdapter;
import com.workshop.bookswap.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private RecyclerView featuredRecyclerView, recentRecyclerView, auctionRecyclerView;
    private TextView emptyFeatured, emptyRecent, emptyAuction;
    private SearchView searchView;
    private ChipGroup categoryChips;

    private ShimmerFrameLayout shimmerFeatured, shimmerRecent, shimmerAuction;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final List<Book> allBooks = new ArrayList<>();
    private final List<Book> featuredBooks = new ArrayList<>();
    private final List<Book> recentBooks = new ArrayList<>();
    private final List<Book> auctionBooks = new ArrayList<>();

    private FeaturedBookAdapter featuredAdapter;
    private BookAdapter recentAdapter, auctionAdapter;


    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // Initialize Views
        featuredRecyclerView = view.findViewById(R.id.featured_books_recycler_view);
        recentRecyclerView = view.findViewById(R.id.recent_books_recycler_view);
        auctionRecyclerView = view.findViewById(R.id.auction_books_recycler_view);

        emptyFeatured = view.findViewById(R.id.no_featured_books_text_view);
        emptyRecent = view.findViewById(R.id.no_recent_books_text_view);
        emptyAuction = view.findViewById(R.id.no_auction_books_text_view);

        shimmerFeatured = view.findViewById(R.id.shimmer_featured);
        shimmerRecent = view.findViewById(R.id.shimmer_recent);
        shimmerAuction = view.findViewById(R.id.shimmer_auction);

        searchView = view.findViewById(R.id.search_books);
        categoryChips = view.findViewById(R.id.category_chips);

        // Setup RecyclerViews
        setupRecyclerView(featuredRecyclerView);
        setupRecyclerView(recentRecyclerView);
        setupRecyclerView(auctionRecyclerView);

        featuredAdapter = new FeaturedBookAdapter(getContext(), featuredBooks);
        recentAdapter = new BookAdapter(getContext(), recentBooks);
        auctionAdapter = new BookAdapter(getContext(), auctionBooks);

        featuredRecyclerView.setAdapter(featuredAdapter);
        recentRecyclerView.setAdapter(recentAdapter);
        auctionRecyclerView.setAdapter(auctionAdapter);

        // Start shimmer loading
        shimmerFeatured.startShimmer();
        shimmerRecent.startShimmer();
        shimmerAuction.startShimmer();

        // Hide recyclers initially
        featuredRecyclerView.setVisibility(View.GONE);
        recentRecyclerView.setVisibility(View.GONE);
        auctionRecyclerView.setVisibility(View.GONE);

        loadBooks();
        setupSearch();
        setupCategoryFilter();

        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerView.setHasFixedSize(true);
    }

//    private void loadBooks() {
//        db.collection("books").get().addOnSuccessListener(querySnapshot -> {
//            allBooks.clear();
//            featuredBooks.clear();
//            recentBooks.clear();
//            auctionBooks.clear();
//
//            for (QueryDocumentSnapshot doc : querySnapshot) {
//                Book book = doc.toObject(Book.class);
//                book.setId(doc.getId());
//                allBooks.add(book);
//
//                if (book.isFeatured()) {
//                    featuredBooks.add(book);
//                }
//                if (!book.isAuction()) {
//                    recentBooks.add(book);
//                } else if (book.getAuctionEndTime() > System.currentTimeMillis()) {
//                    auctionBooks.add(book);
//                }
//            }
//
//            toggleEmptyViews();
//
//            featuredAdapter.notifyDataSetChanged();
//            recentAdapter.notifyDataSetChanged();
//            auctionAdapter.notifyDataSetChanged();
//
//            // Stop shimmer and show content
//            shimmerFeatured.stopShimmer();
//            shimmerRecent.stopShimmer();
//            shimmerAuction.stopShimmer();
//
//            shimmerFeatured.setVisibility(View.GONE);
//            shimmerRecent.setVisibility(View.GONE);
//            shimmerAuction.setVisibility(View.GONE);
//
//            featuredRecyclerView.setVisibility(View.VISIBLE);
//            recentRecyclerView.setVisibility(View.VISIBLE);
//            auctionRecyclerView.setVisibility(View.VISIBLE);
//        });
//    }
private void loadBooks() {
    db.collection("books").get().addOnSuccessListener(querySnapshot -> {
        allBooks.clear();
        featuredBooks.clear();
        recentBooks.clear();
        auctionBooks.clear();

        for (QueryDocumentSnapshot doc : querySnapshot) {
            Book book = doc.toObject(Book.class);
            book.setId(doc.getId());

            // Debug: Log the values fetched from Firestore
            Log.d("DEBUG_BOOK", "Fetched book: " + book.getTitle()
                    + " | isFeatured: " + book.isFeatured()
                    + " | isAuction: " + book.isAuction());

            allBooks.add(book);

            if (book.isFeatured()) {
                Log.d("DEBUG_FEATURED", "Adding to featured: " + book.getTitle());
                featuredBooks.add(book);
            }

            if (!book.isAuction()) {
                Log.d("DEBUG_RECENT", "Adding to recent: " + book.getTitle());
                recentBooks.add(book);
            } else if (book.getAuctionEndTime() > System.currentTimeMillis()) {
                Log.d("DEBUG_AUCTION", "Adding to auction: " + book.getTitle());
                auctionBooks.add(book);
            }
        }

        toggleEmptyViews();

        featuredAdapter.notifyDataSetChanged();
        recentAdapter.notifyDataSetChanged();
        auctionAdapter.notifyDataSetChanged();

        // Stop shimmer and show content
        shimmerFeatured.stopShimmer();
        shimmerRecent.stopShimmer();
        shimmerAuction.stopShimmer();

        shimmerFeatured.setVisibility(View.GONE);
        shimmerRecent.setVisibility(View.GONE);
        shimmerAuction.setVisibility(View.GONE);

        featuredRecyclerView.setVisibility(View.VISIBLE);
        recentRecyclerView.setVisibility(View.VISIBLE);
        auctionRecyclerView.setVisibility(View.VISIBLE);
    }).addOnFailureListener(e -> {
        Log.e("DEBUG_BOOK", "Failed to fetch books: " + e.getMessage());
    });
}

    private void toggleEmptyViews() {
        emptyFeatured.setVisibility(featuredBooks.isEmpty() ? View.VISIBLE : View.GONE);
        emptyRecent.setVisibility(recentBooks.isEmpty() ? View.VISIBLE : View.GONE);
        emptyAuction.setVisibility(auctionBooks.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText.trim());
                return true;
            }
        });
    }

    private void filterBooks(String query) {
        if (TextUtils.isEmpty(query)) {
            resetFilters();
            return;
        }

        List<Book> filteredFeatured = new ArrayList<>();
        List<Book> filteredRecent = new ArrayList<>();
        List<Book> filteredAuction = new ArrayList<>();

        for (Book book : allBooks) {
            if (book.getTitle().toLowerCase(Locale.ROOT).contains(query.toLowerCase())) {
                if (book.isFeatured()) {
                    filteredFeatured.add(book);
                } else if (book.isAuction() && book.getAuctionEndTime() > System.currentTimeMillis()) {
                    filteredAuction.add(book);
                } else {
                    filteredRecent.add(book);
                }
            }
        }

        featuredBooks.clear();
        recentBooks.clear();
        auctionBooks.clear();

        featuredBooks.addAll(filteredFeatured);
        recentBooks.addAll(filteredRecent);
        auctionBooks.addAll(filteredAuction);

        toggleEmptyViews();
        featuredAdapter.notifyDataSetChanged();
        recentAdapter.notifyDataSetChanged();
        auctionAdapter.notifyDataSetChanged();
    }

    private void resetFilters() {
        featuredBooks.clear();
        recentBooks.clear();
        auctionBooks.clear();

        for (Book book : allBooks) {
            if (book.isFeatured()) {
                featuredBooks.add(book);
            } else if (book.isAuction() && book.getAuctionEndTime() > System.currentTimeMillis()) {
                auctionBooks.add(book);
            } else {
                recentBooks.add(book);
            }
        }

        toggleEmptyViews();
        featuredAdapter.notifyDataSetChanged();
        recentAdapter.notifyDataSetChanged();
        auctionAdapter.notifyDataSetChanged();
    }

    private void setupCategoryFilter() {
        categoryChips.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) {
                resetFilters();
                return;
            }

            Chip selectedChip = group.findViewById(checkedId);
            if (selectedChip == null) return;

            String category = selectedChip.getText().toString();

            List<Book> filteredFeatured = new ArrayList<>();
            List<Book> filteredRecent = new ArrayList<>();
            List<Book> filteredAuction = new ArrayList<>();

            for (Book book : allBooks) {
                if (book.getCategory().equalsIgnoreCase(category)) {
                    if (book.isFeatured()) {
                        filteredFeatured.add(book);
                    } else if (book.isAuction() && book.getAuctionEndTime() > System.currentTimeMillis()) {
                        filteredAuction.add(book);
                    } else {
                        filteredRecent.add(book);
                    }
                }
            }

            featuredBooks.clear();
            recentBooks.clear();
            auctionBooks.clear();

            featuredBooks.addAll(filteredFeatured);
            recentBooks.addAll(filteredRecent);
            auctionBooks.addAll(filteredAuction);

            toggleEmptyViews();
            featuredAdapter.notifyDataSetChanged();
            recentAdapter.notifyDataSetChanged();
            auctionAdapter.notifyDataSetChanged();
        });
    }

}

