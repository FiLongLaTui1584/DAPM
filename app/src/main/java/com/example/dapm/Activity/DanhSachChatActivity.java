package com.example.dapm.Activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.ChatListAdapter;
import com.example.dapm.R;
import com.example.dapm.model.ChatItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DanhSachChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;
    private List<ChatItem> chatItems;
    private FirebaseFirestore db;
    private ImageButton cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_chat);

        cancel=findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewChatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatItems = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(chatItems, this);
        recyclerView.setAdapter(chatListAdapter);

        db = FirebaseFirestore.getInstance();

        loadChatList();
    }

    private void loadChatList() {
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("chats")
                .whereArrayContains("participants", currentUserID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    chatItems.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        List<String> participants = (List<String>) document.get("participants");
                        String otherUserID = participants.get(0).equals(currentUserID) ? participants.get(1) : participants.get(0);

                        String lastMessage = document.getString("lastMessage");
                        Date lastMessageTimestamp = document.getDate("lastMessageTimestamp");

                        // Lấy thông tin người dùng khác
                        db.collection("users").document(otherUserID).get()
                                .addOnSuccessListener(userDoc -> {
                                    if (userDoc.exists()) {
                                        String otherUserName = userDoc.getString("name");
                                        String otherUserAvatar = userDoc.getString("avatar");

                                        // Tạo ChatItem và thêm vào danh sách
                                        ChatItem chatItem = new ChatItem();
                                        chatItem.setOtherUserID(otherUserID);
                                        chatItem.setOtherUserName(otherUserName);
                                        chatItem.setOtherUserAvatar(otherUserAvatar);
                                        chatItem.setLastMessage(lastMessage);
                                        chatItem.setLastMessageTimestamp(lastMessageTimestamp);

                                        chatItems.add(chatItem);
                                        chatListAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                });
    }
}

