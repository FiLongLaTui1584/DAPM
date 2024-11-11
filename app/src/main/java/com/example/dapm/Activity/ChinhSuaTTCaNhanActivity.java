package com.example.dapm.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dapm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ChinhSuaTTCaNhanActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView cancel, changeBankImage;
    private EditText changeName, changePhone, changeAddress, changeDescription;
    private EditText changeOldPassword, changeNewPassword, changeNew2Password;
    private EditText changeBankName, changeBankNumber;
    private TextView savePassword;
    private Button saveButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userId;
    private String bankImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_ttca_nhan);

        addControl();
        addEvent();
    }

    private void addControl() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        changeName = findViewById(R.id.change_name);
        changePhone = findViewById(R.id.change_phone);
        changeAddress = findViewById(R.id.change_address);
        changeDescription = findViewById(R.id.change_description);
        changeBankName = findViewById(R.id.change_bankName);
        changeBankNumber = findViewById(R.id.change_bankNumber);
        changeBankImage = findViewById(R.id.change_bankImage);
        saveButton = findViewById(R.id.save_button);
        cancel = findViewById(R.id.cancel);
        changeOldPassword = findViewById(R.id.change_old_password);
        changeNewPassword = findViewById(R.id.change_new_password);
        changeNew2Password = findViewById(R.id.change_new2_password);
        savePassword = findViewById(R.id.save_password);
    }

    private void addEvent() {
        cancel.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> saveUserInfo());

        savePassword.setOnClickListener(v -> changePassword());

        changeBankImage.setOnClickListener(v -> openImagePicker());
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadBankImage(imageUri);
        }
    }

    private void uploadBankImage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("users/" + userId + "/bankImage.jpg");
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            bankImageUrl = uri.toString();
                            updateBankImageInFirestore(bankImageUrl);

                            // Hiển thị ảnh ngay lập tức vào changeBankImage bằng Picasso
                            Picasso.get()
                                    .load(bankImageUrl)
                                    .into(changeBankImage);
                        }))
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void updateBankImageInFirestore(String imageUrl) {
        db.collection("users").document(userId)
                .update("bankImage", imageUrl)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Đã cập nhật ảnh ngân hàng", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi lưu ảnh ngân hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveUserInfo() {
        String name = changeName.getText().toString().trim();
        String phone = changePhone.getText().toString().trim();
        String address = changeAddress.getText().toString().trim();
        String description = changeDescription.getText().toString().trim();
        String bankName = changeBankName.getText().toString().trim();
        String bankNumber = changeBankNumber.getText().toString().trim();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("phone", phone);
        userMap.put("address", address);
        userMap.put("description", description);
        userMap.put("bankName", bankName);
        userMap.put("bankNumber", bankNumber);
        if (bankImageUrl != null) {
            userMap.put("bankImage", bankImageUrl); // Thêm URL ảnh ngân hàng nếu có
        }

        db.collection("users").document(userId)
                .update(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ChinhSuaTTCaNhanActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(ChinhSuaTTCaNhanActivity.this, "Lỗi khi lưu: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void changePassword() {
        String oldPassword = changeOldPassword.getText().toString().trim();
        String newPassword = changeNewPassword.getText().toString().trim();
        String confirmPassword = changeNew2Password.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(auth.getCurrentUser().getEmail(), oldPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        auth.getCurrentUser().updatePassword(newPassword)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "Lỗi khi đổi mật khẩu: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
