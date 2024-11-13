package com.example.dapm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dapm.Activity.ADMIN.AdminActivity;
import com.example.dapm.Fragment.HomeFragment;
import com.example.dapm.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class DangNhapActivity extends AppCompatActivity {

    private EditText siginEmail, siginPass;
    private Button siginButton;
    private ImageView cancel;
    private TextView siginForgotPass, siginCreate;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);

        addControl();
        addEvent();
    }

    private void addEvent() {
        siginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        siginCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        siginForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = siginEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(DangNhapActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendPasswordResetEmail(email);
            }
        });

        cancel.setOnClickListener(v -> finish());


    }

    private void addControl() {
        mAuth = FirebaseAuth.getInstance();
        siginEmail = findViewById(R.id.sigin_Email);
        siginPass = findViewById(R.id.sigin_Pass);
        siginButton = findViewById(R.id.sigin_button);
        siginForgotPass = findViewById(R.id.sigin_ForgotPass);
        siginCreate = findViewById(R.id.sigin_Create);
        cancel = findViewById(R.id.cancel);
        db = FirebaseFirestore.getInstance();
    }

    private void loginUser() {
        String email = siginEmail.getText().toString().trim();
        String password = siginPass.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Bạn phải nhập đầy đủ các ô !", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        db.collection("users").document(uid).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    Boolean isLocked = documentSnapshot.getBoolean("isLocked");
                                    Timestamp lockExpiration = documentSnapshot.getTimestamp("lockExpiration");

                                    if (isLocked != null && isLocked) {
                                        Date now = new Date();
                                        if (lockExpiration == null || lockExpiration.toDate().after(now)) {
                                            showAccountLockedDialog(lockExpiration);
                                        } else {
                                            documentSnapshot.getReference().update("isLocked", false, "lockExpiration", null);
                                            checkUserAccessLevel(uid);
                                        }
                                    } else {
                                        checkUserAccessLevel(uid);
                                    }
                                });
                    } else {
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(DangNhapActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DangNhapActivity.this, "Failed to send reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showAccountLockedDialog(Timestamp lockExpiration) {
        String message = (lockExpiration == null) ? "Tài khoản của bạn đã bị khóa vĩnh viễn, vui lòng gửi email đến longadmin@gmail.com để được giúp đỡ."
                : "Tài khoản của bạn bị khóa đến: " + lockExpiration.toDate() +"vui lòng gửi email đến longadmin@gmail.com để được giúp đỡ";

        new AlertDialog.Builder(this)
                .setTitle("Tài khoản bị khóa")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }



    private void checkUserAccessLevel(String uid) {
        DocumentReference df = db.collection("users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());

                Long IsAdmin = documentSnapshot.getLong("IsAdmin");if (IsAdmin != null && IsAdmin == 1) {
                    Toast.makeText(DangNhapActivity.this, "Admin đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    finish();
                } else {
                    Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(DangNhapActivity.this, "Lỗi truy vấn quyền người dùng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }




}



