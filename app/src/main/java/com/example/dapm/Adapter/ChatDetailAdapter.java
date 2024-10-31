package com.example.dapm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dapm.model.ChatMessage;
import com.example.dapm.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.ChatViewHolder> {
    private List<ChatMessage> chatMessages;
    private String senderID;
    private Context context;

    public ChatDetailAdapter(List<ChatMessage> chatMessages, Context context, String senderID) {
        this.chatMessages = chatMessages;
        this.context = context;
        this.senderID = senderID;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);

        // Hiển thị nội dung tin nhắn
        holder.messageTextView.setText(message.getMessageContent());

        // Hiển thị thời gian
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        holder.timeTextView.setText(dateFormat.format(message.getTimestamp()));

        // Điều chỉnh hiển thị dựa trên người gửi
        if (message.getSenderID().equals(senderID)) {
            // Tin nhắn gửi
            holder.itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            holder.messageTextView.setBackgroundResource(R.drawable.style_button_yellow);
        } else {
            // Tin nhắn nhận
            holder.itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            holder.messageTextView.setBackgroundResource(R.drawable.style_button_grey);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timeTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}
