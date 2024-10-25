package com.example.dapm.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dapm.R;
import com.google.firebase.auth.FirebaseAuth;

public class DangKyActivity extends AppCompatActivity {

    private Button tickButton;
    private boolean isChecked = false;
    private EditText sigup_name, sigup_phone, sigup_Email, sigup_Pass;
    private Button sigup_button;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);

        addControl();
        addEvent();
    }

    private void addEvent() {
        // Thiết lập sự kiện cho nút tick
        tickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
                if (isChecked) {
                    tickButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                } else {
                    tickButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        // Thiết lập sự kiện cho nút đăng ký
        sigup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void addControl() {
        auth = FirebaseAuth.getInstance();
        sigup_name = findViewById(R.id.sigup_name);
        sigup_phone = findViewById(R.id.sigup_phone);
        sigup_Email = findViewById(R.id.sigup_Email);
        sigup_Pass = findViewById(R.id.sigup_Pass);
        sigup_button = findViewById(R.id.sigup_button);
        tickButton = findViewById(R.id.tickButton);
    }

    private void registerUser() {
        String name = sigup_name.getText().toString().trim();
        String phone = sigup_phone.getText().toString().trim();
        String email = sigup_Email.getText().toString().trim();
        String password = sigup_Pass.getText().toString().trim();

        // Kiểm tra các trường không được để trống
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(DangKyActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra người dùng đã đồng ý điều khoản chưa
        if (!isChecked) {
            Toast.makeText(DangKyActivity.this, "Vui lòng đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng ký tài khoản với Firebase
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Có thể lưu thông tin người dùng vào cơ sở dữ liệu Firestore nếu cần
                        Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        // Chuyển đến màn hình chính hoặc đăng nhập
                    } else {
                        Toast.makeText(DangKyActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
