package com.example.dapm.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dapm.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Calendar;
import java.util.Date;

public class ReportDetailActivity extends AppCompatActivity {

    private TextView rpUsername, rpReason1, rpReporter1, countUserRP;
    private ImageView userAvatar;
    private String reportedUserID, reporterUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        

        rpUsername = findViewById(R.id.rp_username);
        rpReason1 = findViewById(R.id.rp_reason1);
        rpReporter1 = findViewById(R.id.rp_reporter1);
        countUserRP = findViewById(R.id.countUserRP);
        userAvatar = findViewById(R.id.rp_useravatar);
        ImageView cancelButton = findViewById(R.id.cancel);
        LinearLayout blockButton = findViewById(R.id.BlockButton);
        LinearLayout noBlockButton = findViewById(R.id.NoBlockButton);

        reportedUserID = getIntent().getStringExtra("reportedUserID");
        reporterUserID = getIntent().getStringExtra("reporterUserID");

        String reason = getIntent().getStringExtra("reason");
        rpReason1.setText(reason);

        String userAvatarUrl = getIntent().getStringExtra("userAvatarUrl");
        loadUserAvatar(userAvatarUrl);

        loadUserInfo();
        countUserReports();

        cancelButton.setOnClickListener(v -> finish());
        blockButton.setOnClickListener(v -> showBlockDurationDialog());
        noBlockButton.setOnClickListener(v -> showUnlockConfirmationDialog());
    }

    private void showUnlockConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận mở khóa tài khoản")
                .setMessage("Bạn có chắc chắn muốn mở khóa tài khoản này không?")
                .setPositiveButton("Có", (dialog, which) -> unlockUserAccount())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void unlockUserAccount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(reportedUserID);

        // Cập nhật trạng thái isLocked thành false và thiết lập lockExpiration thành null
        userRef.update("isLocked", false, "lockExpiration", null)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Tài khoản đã được mở khóa", Toast.LENGTH_SHORT).show();
                    // Có thể thêm logic cập nhật báo cáo hoặc UI tại đây nếu cần
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi mở khóa tài khoản", Toast.LENGTH_SHORT).show();
                });
    }



    private void loadUserAvatar(String url) {
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.sample_image)
                .error(R.drawable.sample_image)
                .into(userAvatar);
    }

    private void loadUserInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(reportedUserID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        rpUsername.setText(name);
                    } else {
                        rpUsername.setText("Tên không tồn tại");
                    }
                });

        db.collection("users").document(reporterUserID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        rpReporter1.setText(name);
                    } else {
                        rpReporter1.setText("Tên không tồn tại");
                    }
                });
    }

    private void countUserReports() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reports")
                .whereEqualTo("reportedUserID", reportedUserID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = task.getResult().size();
                        countUserRP.setText(String.valueOf(count));
                    } else {
                        countUserRP.setText("0");
                    }
                });
    }

    private void showBlockDurationDialog() {
        String[] options = {"12 tiếng", "24 tiếng", "1 tuần", "Vĩnh viễn"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thời gian khóa tài khoản")
                .setItems(options, (dialog, which) -> {
                    int hours;
                    switch (which) {
                        case 0: hours = 12; break;
                        case 1: hours = 24; break;
                        case 2: hours = 168; break;
                        default: hours = -1; // Vĩnh viễn
                    }
                    blockUser(reportedUserID, hours);
                });
        builder.show();
    }

    private void blockUser(String userID, int hours) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userID);

        Date expirationDate = (hours == -1) ? null : new Date(System.currentTimeMillis() + hours * 3600 * 1000);

        userRef.update("isLocked", true, "lockExpiration", expirationDate)
                .addOnSuccessListener(aVoid -> closeRelatedReports(userID))
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi khóa tài khoản", Toast.LENGTH_SHORT).show());
    }

    private void closeRelatedReports(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reports")
                .whereEqualTo("reportedUserID", userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        WriteBatch batch = db.batch();
                        task.getResult().forEach(document -> batch.update(document.getReference(), "isClosed", true));
                        batch.commit()
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Tài khoản và báo cáo đã được cập nhật", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi đóng báo cáo", Toast.LENGTH_SHORT).show());
                    }
                });
    }
}