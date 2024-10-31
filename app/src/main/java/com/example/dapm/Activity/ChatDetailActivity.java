package com.example.dapm.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.ChatDetailAdapter;
import com.example.dapm.model.ChatMessage;
import com.example.dapm.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private ChatDetailAdapter chatDetailAdapter;
    private List<ChatMessage> chatMessages;
    private EditText chatInput;
    private ImageView chatSendButton;
    TextView sellerName, productName, productPrice;
    ImageView cancel, sellerAvatar, productImage;
    LinearLayout viewButton, viewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        // Nhận sellerID và productID từ Intent
        String sellerID = getIntent().getStringExtra("sellerID");
        String senderID = getIntent().getStringExtra("senderID");
        String productID = getIntent().getStringExtra("productID");
        String chatID = getIntent().getStringExtra("chatID");

        addControl();
        addEvent();

        if (senderID != null) {
            loadReceiverInfo(sellerID); // Gọi hàm để tải thông tin người bán
            viewProduct.setVisibility(View.GONE);
        }

        if (productID != null) {
            loadProductInfo(productID); // Gọi hàm để tải thông tin sản phẩm
            viewProduct.setVisibility(View.VISIBLE);
        }

        // Tải tin nhắn từ Firestore
        loadChatMessages(chatID);
    }

    private void addEvent() {
        cancel.setOnClickListener(v -> finish());
        viewButton.setOnClickListener(v -> finish());

        // Thêm sự kiện cho nút gửi tin nhắn
        chatSendButton.setOnClickListener(v -> {
            String messageContent = chatInput.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                sendMessage(messageContent); // Gọi hàm gửi tin nhắn
            }
        });
    }

    private void addControl() {
        sellerName = findViewById(R.id.sellerName);
        sellerAvatar = findViewById(R.id.sellerAvatar);
        productName = findViewById(R.id.productTitle);
        productPrice = findViewById(R.id.productPrice);
        productImage = findViewById(R.id.productImage);
        cancel = findViewById(R.id.cancel);
        viewButton = findViewById(R.id.viewButton);
        viewProduct = findViewById(R.id.viewProduct);
        chatInput = findViewById(R.id.chatInput);
        chatSendButton = findViewById(R.id.chatSendButton);

        // Khởi tạo RecyclerView và Adapter với senderID
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        chatMessages = new ArrayList<>();
        String senderID = getIntent().getStringExtra("senderID");
        chatDetailAdapter = new ChatDetailAdapter(chatMessages, this, senderID);
        recyclerViewChat.setAdapter(chatDetailAdapter);
    }

    private void loadReceiverInfo(String sellerID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(sellerID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String receiverName = document.getString("name");
                            String receiverAvatarUrl = document.getString("avatar");

                            // Hiển thị thông tin người nhận
                            sellerName.setText(receiverName);
                            Picasso.get().load(receiverAvatarUrl).into(sellerAvatar);
                        }
                    }
                });
    }

    private void loadProductInfo(String productID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").document(productID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String title = document.getString("productTitle");
                            int price = document.contains("productPrice") ? document.getLong("productPrice").intValue() : 0;
                            String imageUrl = document.getString("productImage1");

                            // Hiển thị thông tin sản phẩm
                            productName.setText(title);
                            productPrice.setText(String.format("%,d VNĐ", price));
                            Picasso.get().load(imageUrl).into(productImage);
                        }
                    }
                });
    }

    private void loadChatMessages(String chatID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats").document(chatID)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.e("ChatDetailActivity", "Lỗi khi truy vấn tin nhắn", e);
                        return;
                    }
                    chatMessages.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        ChatMessage message = document.toObject(ChatMessage.class);
                        chatMessages.add(message);
                    }
                    chatDetailAdapter.notifyDataSetChanged();
                    recyclerViewChat.scrollToPosition(chatMessages.size() - 1); // Cuộn xuống tin nhắn mới nhất
                });

    }


    private void sendMessage(String messageContent) {

        String sellerID = getIntent().getStringExtra("sellerID");
        String senderID = getIntent().getStringExtra("senderID");
        String chatID = getIntent().getStringExtra("chatID");

        // Tạo một ChatMessage mới
        ChatMessage chatMessage = new ChatMessage(senderID, sellerID, messageContent, new Date());

        // Thêm tin nhắn vào Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats").document(chatID).collection("messages").add(chatMessage)
                .addOnSuccessListener(documentReference -> {
                    // Xóa nội dung trong chatInput sau khi gửi thành công
                    chatInput.setText("");

                    // Cập nhật nội dung và thời gian của tin nhắn cuối cùng
                    Map<String, Object> lastMessageUpdate = new HashMap<>();
                    lastMessageUpdate.put("lastMessage", messageContent); // Nội dung tin nhắn cuối
                    lastMessageUpdate.put("lastMessageTimestamp", FieldValue.serverTimestamp()); // Thời gian của tin nhắn cuối

                    // Sử dụng `set` với `SetOptions.merge()` để đảm bảo các trường được tạo mới nếu chưa tồn tại
                    db.collection("chats").document(chatID)
                            .set(lastMessageUpdate, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> Log.d("Chat", "Last message updated"))
                            .addOnFailureListener(e -> Log.e("Chat", "Error updating last message", e));
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi gửi tin nhắn
                    Log.e("Chat", "Error sending message", e);
                });
    }


}
