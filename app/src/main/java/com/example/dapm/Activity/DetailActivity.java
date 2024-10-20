package com.example.dapm.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dapm.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        // Tìm reportButton
        ImageButton reportButton = findViewById(R.id.reportButton);

        // Thiết lập sự kiện khi nhấn vào reportButton
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị BottomSheetDialog
                showReportBottomSheet();
            }
        });
    }

    private void showReportBottomSheet() {
        // Tạo BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivity.this);

        // Set layout cho BottomSheet
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bao_cao_sp, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Tìm cancelButton trong BottomSheet và thiết lập sự kiện
        bottomSheetView.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng BottomSheet khi nhấn nút Cancel
                bottomSheetDialog.dismiss();
            }
        });

        // Hiển thị BottomSheet
        bottomSheetDialog.show();
    }
}
