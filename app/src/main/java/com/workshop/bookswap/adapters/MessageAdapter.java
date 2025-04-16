//package com.workshop.bookswap.adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.workshop.bookswap.R;
//import com.workshop.bookswap.models.Message;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
//
//    private List<Message> messageList;
//    private String currentUserId;
//    public MessageAdapter(Context context, List<Message> messageList, String currentUserId) {
//        this.messageList = messageList;
//        this.currentUserId = currentUserId;
//    }
//
//    @NonNull
//    @Override
//    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
//        return new MessageViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
//        Message message = messageList.get(position);
//        holder.messageTextView.setText(message.getMessage());
//
//        // Format the timestamp to a human-readable date format
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
//        String formattedDate = sdf.format(new Date(message.getTimestamp()));
//        holder.timestampTextView.setText(formattedDate);
//    }
//
//    @Override
//    public int getItemCount() {
//        return messageList.size();
//    }
//
//    public static class MessageViewHolder extends RecyclerView.ViewHolder {
//        TextView messageTextView, timestampTextView;
//
//        public MessageViewHolder(View itemView) {
//            super(itemView);
//            messageTextView = itemView.findViewById(R.id.messageTextView);
//            timestampTextView = itemView.findViewById(R.id.timestampTextView);
//        }
//    }
//
//    // Update the message list
//    public void updateMessages(List<Message> messages) {
//        this.messageList = messages;
//        notifyDataSetChanged();
//    }
//
//
//}

package com.workshop.bookswap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.workshop.bookswap.R;
import com.workshop.bookswap.models.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messageList;
    private String currentUserId;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public MessageAdapter(Context context, List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = sdf.format(new Date(message.getTimestamp()));

        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            SentMessageViewHolder viewHolder = (SentMessageViewHolder) holder;
            viewHolder.messageTextView.setText(message.getMessage());
            viewHolder.timestampTextView.setText(formattedDate);
        } else {
            ReceivedMessageViewHolder viewHolder = (ReceivedMessageViewHolder) holder;
            viewHolder.messageTextView.setText(message.getMessage());
            viewHolder.timestampTextView.setText(formattedDate);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView, timestampTextView;
        public SentMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView, timestampTextView;
        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }

    public void updateMessages(List<Message> messages) {
        this.messageList = messages;
        notifyDataSetChanged();
    }
}


