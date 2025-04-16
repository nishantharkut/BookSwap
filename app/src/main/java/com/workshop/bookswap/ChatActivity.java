//package com.workshop.bookswap;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import com.workshop.bookswap.adapters.MessageAdapter;
//import com.workshop.bookswap.models.Message;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ChatActivity extends AppCompatActivity {
//
//    private TextView receiverNameTextView;
//    private RecyclerView chatRecyclerView;
//    private EditText messageEditText;
//    private ImageButton sendButton;
//    private MessageAdapter messageAdapter;
//    private List<Message> messageList;
//
//    private String senderId;
//    private String receiverId;
//    private DatabaseReference senderRoomRef;
//    private DatabaseReference receiverRoomRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        // Get extras from intent
//        String username = getIntent().getStringExtra("username");
//        receiverId = getIntent().getStringExtra("receiverId");
//        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Setup UI
//        chatRecyclerView = findViewById(R.id.chatRecyclerView);
//        messageEditText = findViewById(R.id.messageEditText);
//        sendButton = findViewById(R.id.sendButton);
//        receiverNameTextView = findViewById(R.id.chatUsername);
//        if (receiverNameTextView != null && username != null) {
//            receiverNameTextView.setText(username);
//        }
//
//        // Setup toolbar
//        Toolbar toolbar = findViewById(R.id.chatToolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setTitle(username);
//        }
//
//        toolbar.setNavigationOnClickListener(v -> {
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("navigateTo", "home");
//            setResult(RESULT_OK, resultIntent);
//            finish();
//        });
//
//        // Setup message adapter
//        messageList = new ArrayList<>();
//        messageAdapter = new MessageAdapter(this, messageList, senderId);
//        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        chatRecyclerView.setAdapter(messageAdapter);
//
//        // Firebase message paths (sender and receiver rooms)
//        String senderRoom = getChatId(senderId, receiverId);
//        String receiverRoom = getChatId(receiverId, senderId);
//        senderRoomRef = FirebaseDatabase.getInstance().getReference("messages").child(senderRoom);
//        receiverRoomRef = FirebaseDatabase.getInstance().getReference("messages").child(receiverRoom);
//
//        // Send message
//        sendButton.setOnClickListener(v -> {
//            String text = messageEditText.getText().toString().trim();
//            if (!text.isEmpty()) {
//                long timestamp = System.currentTimeMillis();
//                Message message = new Message(senderId, receiverId, text, timestamp);
//
//                // Push to sender's chat room
//                senderRoomRef.push().setValue(message).addOnSuccessListener(unused -> {
//                    // Then push to receiver's chat room
//                    receiverRoomRef.push().setValue(message);
//                });
//
//                messageEditText.setText("");
//            }
//        });
//
//        // Listen for new messages
//        senderRoomRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Message message = snapshot.getValue(Message.class);
//                if (message != null) {
//                    messageList.add(message);
//                    messageAdapter.notifyItemInserted(messageList.size() - 1);
//                    chatRecyclerView.scrollToPosition(messageList.size() - 1);
//                }
//            }
//
//            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
//            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
//            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
//            @Override public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
//
//    private String getChatId(String user1, String user2) {
//        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
//    }
//}

package com.workshop.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.workshop.bookswap.adapters.MessageAdapter;
import com.workshop.bookswap.models.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private TextView receiverNameTextView;
    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    private String senderId;
    private String receiverId;
    private String username;

    private DatabaseReference senderRoomRef;
    private DatabaseReference receiverRoomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get intent extras
        username = getIntent().getStringExtra("username");
        receiverId = getIntent().getStringExtra("receiverId");
        senderId = FirebaseAuth.getInstance().getUid();

        // Setup UI
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        receiverNameTextView = findViewById(R.id.chatUsername);
        if (receiverNameTextView != null && username != null) {
            receiverNameTextView.setText(username);
        }

        Toolbar toolbar = findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(username);
        }

        toolbar.setNavigationOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("navigateTo", "home");
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Setup chat data
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList, senderId);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);

        // Define chat rooms (same format for both users ensures consistency)
        String chatRoomId = getChatId(senderId, receiverId);
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("messages").child(chatRoomId);

        // Set up listener
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    chatRecyclerView.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });

        // Sending messages
        sendButton.setOnClickListener(v -> {
            String text = messageEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                long timestamp = System.currentTimeMillis();
                Message message = new Message(text, senderId, receiverId, timestamp);

                // Push message once to shared chat room
                chatRef.push().setValue(message);

                messageEditText.setText("");
            }
        });
    }

    private String getChatId(String user1, String user2) {
        // Ensures consistent ordering (lexicographical)
        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
    }
}
