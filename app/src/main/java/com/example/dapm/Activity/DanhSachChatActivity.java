package com.example.dapm.Activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.DSChatAdapter;
import com.example.dapm.model.DSChat;
import com.example.dapm.R;
import java.util.ArrayList;
import java.util.List;

public class DanhSachChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DSChatAdapter chatAdapter;
    private List<DSChat> chatList;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_danh_sach_chat);

        addControl();
        addIntent();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewDSChat);

        // Initialize data
        chatList = new ArrayList<>();
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));
        chatList.add(new DSChat("Chó", "Gâu gâu gâu gâu", R.drawable.sample_image));

        // Set up adapter
        chatAdapter = new DSChatAdapter(chatList);

        // Set LayoutManager and Adapter to RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);
    }

    private void addIntent() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void addControl() {
        btnBack = findViewById(R.id.btnBack);
    }
}
