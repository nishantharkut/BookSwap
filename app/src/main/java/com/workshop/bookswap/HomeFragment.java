package com.workshop.bookswap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private RecyclerView featuredBooksRecycler, recentBooksRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Fix: Initialize RecyclerViews
        featuredBooksRecycler = view.findViewById(R.id.recycler_featured_books);
        recentBooksRecycler = view.findViewById(R.id.recycler_recent_books);

        // Set Layout Managers
        featuredBooksRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recentBooksRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
