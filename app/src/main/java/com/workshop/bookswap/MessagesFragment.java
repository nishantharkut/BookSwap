package com.workshop.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.workshop.bookswap.adapters.ChatAdapter;
import com.workshop.bookswap.models.ChatUser;
import com.workshop.bookswap.models.User;

import java.util.*;

public class MessagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatUser> chatUserList;
    private TextView noMessagesTextView; // TextView for empty state

    private DatabaseReference messagesRef, usersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        recyclerView = view.findViewById(R.id.messagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        noMessagesTextView = view.findViewById(R.id.noMessagesTextView); // Initialize the TextView

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        messagesRef = FirebaseDatabase.getInstance().getReference("messages");
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        chatUserList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getContext(), chatUserList);
        Log.d("MessagesFragment", "Current User ID: " + currentUserId);

        recyclerView.setAdapter(chatAdapter);

        // 👇 Add this block to test if RecyclerView and Adapter are working
        chatUserList.add(new ChatUser(
                "test123",
                "Test User",
                "Test Message",
                System.currentTimeMillis(),
                null
        ));
        chatAdapter.notifyDataSetChanged();

        chatAdapter.setOnItemClickListener(chatUser -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("receiverId", chatUser.getUid());
            intent.putExtra("username", chatUser.getUsername());
            startActivityForResult(intent, 1001);

        });

        fetchChats();

        return view;
    }

    private void fetchChats() {
        HashMap<String, ChatUser> latestMessagesMap = new HashMap<>();

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                latestMessagesMap.clear();

                for (DataSnapshot messageSnap : snapshot.getChildren()) {
                    String message = messageSnap.child("message").getValue(String.class);
                    String senderId = messageSnap.child("senderId").getValue(String.class);
                    String receiverId = messageSnap.child("receiverId").getValue(String.class);
                    Long timestamp = messageSnap.child("timestamp").getValue(Long.class);

                    if (senderId == null || receiverId == null || message == null || timestamp == null)
                        continue;

                    boolean isCurrentUserInvolved = senderId.equals(currentUserId) || receiverId.equals(currentUserId);
                    if (!isCurrentUserInvolved) continue;

                    String otherUserId = senderId.equals(currentUserId) ? receiverId : senderId;

                    ChatUser existing = latestMessagesMap.get(otherUserId);
                    if (existing == null || existing.getTimestamp() < timestamp) {
                        ChatUser newChat = new ChatUser(
                                otherUserId,
                                "Loading...", // Will be updated after fetching username
                                message,
                                timestamp,
                                null
                        );
                        latestMessagesMap.put(otherUserId, newChat);
                    }
                }

                fetchUserDetailsForChats(new ArrayList<>(latestMessagesMap.values()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load chats", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserDetailsForChats(List<ChatUser> rawChatUsers) {
        chatUserList.clear();
        if (rawChatUsers.isEmpty()) {
            noMessagesTextView.setVisibility(View.VISIBLE);
            chatAdapter.notifyDataSetChanged();
            return;
        }

        noMessagesTextView.setVisibility(View.GONE);

        for (ChatUser chatUser : rawChatUsers) {
            usersRef.child(chatUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        Log.d("MessagesFragment", "Fetched user: " + user.getName() + " (" + user.getId() + ")");
                        chatUserList.add(new ChatUser(
                                user.getId(),
                                user.getName(),
                                chatUser.getLastMessage(),
                                chatUser.getTimestamp(),
                                user.getProfileImageUrl()
                        ));
                    }

                    if (chatUserList.size() == rawChatUsers.size()) {
                        chatAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Ignore failed user fetch
                }
            });
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == getActivity().RESULT_OK && data != null) {
            String navigateTo = data.getStringExtra("navigateTo");
            if ("home".equals(navigateTo)) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToHome();
                }
            }
        }
    }

}
