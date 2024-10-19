package com.example.dapm.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.ChatDetailAdapter;
import com.example.dapm.model.ChatMessage;
import com.example.dapm.R;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private ChatDetailAdapter chatDetailAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        // Khởi tạo RecyclerView và Adapter
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));

        // Giả lập dữ liệu tin nhắn
        chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage("Khoẻ ko?", true));  // Tin nhắn gửi
        chatMessages.add(new ChatMessage("ko", false));  // Tin nhắn nhận
        chatMessages.add(new ChatMessage("phế", true));  // Tin nhắn gửi

        // Thiết lập Adapter
        chatDetailAdapter = new ChatDetailAdapter(chatMessages);
        recyclerViewChat.setAdapter(chatDetailAdapter);
    }
}
