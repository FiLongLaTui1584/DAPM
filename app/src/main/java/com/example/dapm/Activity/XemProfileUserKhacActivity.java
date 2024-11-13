package com.example.dapm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.dapm.Adapter.ProfilePagerAdapter;
import com.example.dapm.Fragment.FragmentProfile.DangBanFragment;
import com.example.dapm.Fragment.FragmentProfile.DanhGiaFragment;
import com.example.dapm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class XemProfileUserKhacActivity extends AppCompatActivity {
    private ImageButton reportButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private LinearLayout chatButton;
    private ImageView userAvatar, cancel;
    private TextView userName;
    private TextView userDescription;

    private String sellerID;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_profile_user_khac);

        db = FirebaseFirestore.getInstance();
        sellerID = getIntent().getStringExtra("sellerID");

        addControl();
        addEvent();
        setupViewPager_TabLayout(sellerID);
        loadUserProfile();
    }

    private void setupViewPager_TabLayout(String sellerID) {
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getSupportFragmentManager());

        // Sử dụng phương thức newInstance để truyền sellerID dưới dạng tham số
        DangBanFragment dangBanFragment = DangBanFragment.newInstance(sellerID);
        DanhGiaFragment danhGiaFragment = DanhGiaFragment.newInstance(sellerID);

        adapter.addFragment(dangBanFragment, "Đang Bán");
        adapter.addFragment(danhGiaFragment, "Đánh Giá");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addEvent() {
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportBottomSheet();
            }
        });

        cancel.setOnClickListener(v -> finish());

        chatButton.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String chatID;

            //Check xem đăng nhập chưa
            if (auth.getCurrentUser() == null) {
                Intent loginIntent = new Intent(XemProfileUserKhacActivity.this, DangNhapActivity.class);
                startActivity(loginIntent);
            } else {
                String currentUserID = auth.getCurrentUser().getUid();

                //Tạo chatID
                if (currentUserID.compareTo(sellerID) < 0) {
                    chatID = currentUserID + "_" + sellerID;
                } else {
                    chatID = sellerID + "_" + currentUserID;
                }

                //Lấy ID user đang đăng nhập -> Chặn việc tự nhắn chính mình
                if (sellerID.equals(currentUserID)) {
                    Toast.makeText(this, "Không thể tự nhắn chính mình", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(XemProfileUserKhacActivity.this, ChatDetailActivity.class);
                    intent.putExtra("sellerID", sellerID);
                    intent.putExtra("senderID", currentUserID);
                    intent.putExtra("chatID", chatID);
                    startActivity(intent);
                }
            }
        });

    }

    private void addControl() {
        reportButton = findViewById(R.id.reportButton1);
        chatButton = findViewById(R.id.chatButton);
        tabLayout = findViewById(R.id.tab_layout_profile);
        viewPager = findViewById(R.id.view_pager_profile);
        userAvatar = findViewById(R.id.userAvatar);
        userName = findViewById(R.id.userName);
        userDescription = findViewById(R.id.userDescription);
        cancel = findViewById(R.id.cancel);
    }

    private void showReportBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(XemProfileUserKhacActivity.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bao_cao_user, null);
        bottomSheetDialog.setContentView(bottomSheetView);


        bottomSheetView.findViewById(R.id.cancelButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.btnSubmitReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport(bottomSheetView, bottomSheetDialog);
            }
        });

        bottomSheetDialog.show();
    }

    private void submitReport(View bottomSheetView, BottomSheetDialog bottomSheetDialog) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để gửi báo cáo", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(XemProfileUserKhacActivity.this, DangNhapActivity.class);
            startActivity(loginIntent);
            return; // Thoát khỏi phương thức nếu chưa đăng nhập
        }

        // Lấy thông tin từ các trường trong layout
        EditText editTextPhoneNumber = bottomSheetView.findViewById(R.id.editTextPhoneNumber);
        EditText editTextEmail = bottomSheetView.findViewById(R.id.editTextEmail);
        RadioGroup radioGroupReasons = bottomSheetView.findViewById(R.id.radioGroupReasons);

        // Lấy nội dung từ các trường
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String email = editTextEmail.getText().toString();

        // Lấy lý do từ RadioButton đã chọn
        int selectedReasonId = radioGroupReasons.getCheckedRadioButtonId();
        String reportReason = "";
        if (selectedReasonId != -1) {
            RadioButton selectedRadioButton = bottomSheetView.findViewById(selectedReasonId);
            reportReason = selectedRadioButton.getText().toString();
        }

        // Kiểm tra xem các trường đã được điền đầy đủ chưa
        if (phoneNumber.isEmpty() || email.isEmpty() || reportReason.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuẩn bị dữ liệu để lưu
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("phoneNumber", phoneNumber);
        reportData.put("email", email);
        reportData.put("reason", reportReason);
        reportData.put("reportedUserID", sellerID);  // Lưu ID của người bị báo cáo
        reportData.put("reporterUserID", auth.getCurrentUser().getUid());  // Lưu ID của người báo cáo
        reportData.put("isClosed", false); // Thêm field TinhTrangDuyet dạng boolean, mặc định là false

        // Lưu dữ liệu vào Firestore
        FirebaseFirestore.getInstance().collection("reports").add(reportData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(XemProfileUserKhacActivity.this, "Báo cáo đã được gửi", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                        } else {
                            Toast.makeText(XemProfileUserKhacActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }






    private void loadUserProfile() {
        db.collection("users").document(sellerID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String avatarUrl = document.getString("avatar");
                                String description = document.getString("description");

                                userName.setText(name);
                                userDescription.setText(description);
                                Picasso.get().load(avatarUrl).into(userAvatar);
                            }
                        }
                    }
                });
    }
}
