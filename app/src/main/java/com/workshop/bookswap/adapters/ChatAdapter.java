package com.workshop.bookswap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.workshop.bookswap.R;
import com.workshop.bookswap.models.ChatUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private List<ChatUser> chatList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ChatUser chatUser);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ChatAdapter(Context context, List<ChatUser> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatUser chatUser = chatList.get(position);
        holder.receiverName.setText(chatUser.getUsername());
        holder.lastMessage.setText(chatUser.getLastMessage());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(chatUser);
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView receiverName, lastMessage;

        public ChatViewHolder(View itemView) {
            super(itemView);
            receiverName = itemView.findViewById(R.id.receiver_name);
            lastMessage = itemView.findViewById(R.id.last_message);
        }
    }
}
