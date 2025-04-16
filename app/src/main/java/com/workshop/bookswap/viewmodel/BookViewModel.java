package com.workshop.bookswap.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.workshop.bookswap.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends ViewModel {
    private final FirebaseFirestore db;
    private final MutableLiveData<List<Book>> featuredBooks;
    private final MutableLiveData<List<Book>> recentBooks;
    private final MutableLiveData<List<Book>> auctionBooks;

    public BookViewModel() {
        db = FirebaseFirestore.getInstance();
        featuredBooks = new MutableLiveData<>(new ArrayList<>());
        recentBooks = new MutableLiveData<>(new ArrayList<>());
        auctionBooks = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Book>> getFeaturedBooks() {
        return featuredBooks;
    }

    public LiveData<List<Book>> getRecentBooks() {
        return recentBooks;
    }

    public LiveData<List<Book>> getAuctionBooks() {
        return auctionBooks;
    }

    public void fetchFeaturedBooks() {
        db.collection("books")
                .whereEqualTo("isFeatured", true)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;
                    List<Book> books = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        books.add(doc.toObject(Book.class));
                    }
                    featuredBooks.setValue(books);
                });
    }

    public void fetchRecentBooks() {
        db.collection("books")
                .orderBy("timestamp", Query.Direction.DESCENDING)  // Latest books first
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;
                    List<Book> books = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        books.add(doc.toObject(Book.class));
                    }
                    recentBooks.setValue(books);
                });
    }

    public void fetchAuctionBooks() {
        db.collection("auctions")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;
                    List<Book> books = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        books.add(doc.toObject(Book.class));
                    }
                    auctionBooks.setValue(books);
                });
    }
}
