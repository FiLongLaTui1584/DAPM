package com.example.dapm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Activity.ChatDetailActivity;
import com.example.dapm.R;
import com.example.dapm.model.ChatItem;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private List<ChatItem> chatItems;
    private Context context;

    public ChatListAdapter(List<ChatItem> chatItems, Context context) {
        this.chatItems = chatItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        ChatItem chatItem = chatItems.get(position);

        // Đặt tên người dùng, avatar và tin nhắn cuối cùng
        holder.otherUserName.setText(chatItem.getOtherUserName());
        holder.lastMessage.setText(chatItem.getLastMessage());

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        holder.lastMessageTime.setText(dateFormat.format(chatItem.getLastMessageTimestamp()));

        Picasso.get().load(chatItem.getOtherUserAvatar()).into(holder.otherUserAvatar);

        holder.itemView.setOnClickListener(v -> {
            String otherUserID = chatItem.getOtherUserID();
            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            String chatID;
            if (currentUserID.compareTo(otherUserID) < 0) {
                chatID = currentUserID + "_" + otherUserID;
            } else {
                chatID = otherUserID + "_" + currentUserID;
            }

            Intent intent = new Intent(context, ChatDetailActivity.class);
            intent.putExtra("chatID", chatID);
            intent.putExtra("senderID", currentUserID);
            intent.putExtra("sellerID", otherUserID);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    static class ChatListViewHolder extends RecyclerView.ViewHolder {
        ImageView otherUserAvatar;
        TextView otherUserName, lastMessage, lastMessageTime;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            otherUserAvatar = itemView.findViewById(R.id.otherUserAvatar);
            otherUserName = itemView.findViewById(R.id.otherUserName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            lastMessageTime = itemView.findViewById(R.id.lastMessageTime);
        }
    }
}
