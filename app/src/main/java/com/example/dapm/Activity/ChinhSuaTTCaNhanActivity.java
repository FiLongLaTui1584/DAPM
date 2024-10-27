package com.example.dapm.Activity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dapm.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ChinhSuaTTCaNhanActivity extends AppCompatActivity {

    private ImageView cancel;
    private EditText changeName, changePhone, changeAddress, changeDescription;
    private EditText changeOldPassword, changeNewPassword, changeNew2Password;
    private TextView savePassword;
    private Button saveButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
        saveButton = findViewById(R.id.save_button);
        cancel = findViewById(R.id.cancel);
        changeOldPassword = findViewById(R.id.change_old_password);
        changeNewPassword = findViewById(R.id.change_new_password);
        changeNew2Password = findViewById(R.id.change_new2_password);
        savePassword = findViewById(R.id.save_password);
    }

    private void addEvent() {
        cancel.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> {
            saveUserInfo();
        });

        savePassword.setOnClickListener(v -> {
            changePassword();
        });
    }

    private void saveUserInfo() {
        String name = changeName.getText().toString().trim();
        String phone = changePhone.getText().toString().trim();
        String address = changeAddress.getText().toString().trim();
        String description = changeDescription.getText().toString().trim();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("phone", phone);
        userMap.put("address", address);
        userMap.put("description", description);

        db.collection("users").document(userId)
                .update(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ChinhSuaTTCaNhanActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ChinhSuaTTCaNhanActivity.this, "Lỗi khi lưu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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

        // Đổi mật khẩu
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
