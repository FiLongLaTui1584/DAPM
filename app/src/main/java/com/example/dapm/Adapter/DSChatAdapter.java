package com.example.dapm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.R;
import com.example.dapm.model.DSChat;

import java.util.List;

public class DSChatAdapter extends RecyclerView.Adapter<DSChatAdapter.DSChatViewHolder> {

    private List<DSChat> chatList;

    // Constructor to pass the data
    public DSChatAdapter(List<DSChat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public DSChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_danh_sach_tn, parent, false);
        return new DSChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DSChatViewHolder holder, int position) {
        DSChat chat = chatList.get(position);

        // Bind the data to the views
        holder.username.setText(chat.getUsername());
        holder.message.setText(chat.getMessage());
        holder.profileImage.setImageResource(chat.getProfileImage());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // ViewHolder class
    public static class DSChatViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView username, message;

        public DSChatViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.text_username);
            message = itemView.findViewById(R.id.text_message);
        }
    }
}
