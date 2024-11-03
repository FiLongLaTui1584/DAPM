package com.example.dapm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dapm.Adapter.ImageAdapter;
import com.example.dapm.R;
import com.example.dapm.model.ReportSP;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerViewImages;
    private TextView detailTitle, detailPrice, detailLocation,
            detailDescription, detailTinhTrang, detailBaoHanh, detailXuatXu, detailHDSD, detailSellerName;
    private ImageButton reportButton;
    private ImageView cancel, detailSellerAvatar, detailEdit, detailDelete, detailFavButton;
    private String sellerID, productID;
    private LinearLayout detailViewStoreButton, detailChatButton, detailSellerInfo,approveButton, rejectButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        addControl();
        addEvent();
        displayProductDetails();
        getSellerDetails();
    }

    private void addEvent() {
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportBottomSheet();
            }
        });

        cancel.setOnClickListener(v -> finish());

        detailViewStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewStoreButton();            }
        });

        detailChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatDetail();
            }
        });

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductApprovalStatus("approved");
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductApprovalStatus("rejected");
            }
        });
    }

    private void addControl() {
        recyclerViewImages = findViewById(R.id.imageRecyclerView);
        detailTitle = findViewById(R.id.detailTitle);
        detailPrice = findViewById(R.id.detailPrice);
        detailLocation = findViewById(R.id.detailLocation);
        detailDescription = findViewById(R.id.detailDescription);
        detailTinhTrang = findViewById(R.id.detailTinhTrang);
        detailBaoHanh = findViewById(R.id.detailBaoHanh);
        detailXuatXu = findViewById(R.id.detailXuatXu);
        detailHDSD = findViewById(R.id.detailHDSD);
        detailSellerAvatar = findViewById(R.id.detailSellerAvatar);
        detailSellerName = findViewById(R.id.detailSellerName);
        reportButton = findViewById(R.id.reportButton);
        cancel = findViewById(R.id.cancel);
        detailViewStoreButton = findViewById(R.id.detailViewStoreButton);
        detailChatButton = findViewById(R.id.detailChatButton);
        detailDelete= findViewById(R.id.detailDelete);
        detailEdit = findViewById(R.id.detailEdit);
        detailSellerInfo= findViewById(R.id.detailSellerInfo);
        detailFavButton= findViewById(R.id.detailFavButton);
        approveButton = findViewById(R.id.approveButton);
        rejectButton = findViewById(R.id.rejectButton);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private void displayProductDetails() {
        sellerID = getIntent().getStringExtra("sellerID");
        productID = getIntent().getStringExtra("productID");

        String productImage1 = getIntent().getStringExtra("productImage1");
        String productImage2 = getIntent().getStringExtra("productImage2");
        String productImage3 = getIntent().getStringExtra("productImage3");
        String title = getIntent().getStringExtra("productTitle");
        int price = getIntent().getIntExtra("productPrice", 0);
        String location = getIntent().getStringExtra("productLocation");
        String description = getIntent().getStringExtra("productDescription");
        String tinhTrang = getIntent().getStringExtra("productTinhTrang");
        String baoHanh = getIntent().getStringExtra("productBaoHanh");
        String xuatXu = getIntent().getStringExtra("productXuatXu");
        String hdsd = getIntent().getStringExtra("productHDSD");


        List<String> imageUrls = new ArrayList<>();
        imageUrls.add(productImage1);
        imageUrls.add(productImage2);
        imageUrls.add(productImage3);

        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ImageAdapter imageAdapter = new ImageAdapter(this, imageUrls);
        recyclerViewImages.setAdapter(imageAdapter);

        detailTitle.setText(title);
        detailPrice.setText(String.format("%,d VNĐ", price));
        detailLocation.setText("Địa điểm: " + location);
        detailDescription.setText(description);
        detailTinhTrang.setText("Tình trạng: " + tinhTrang);
        detailBaoHanh.setText("Chính sách bảo hành: " + baoHanh);
        detailXuatXu.setText("Xuất xứ: " + xuatXu);
        detailHDSD.setText("Hướng dẫn sử dụng: " + hdsd);
    }

    private void getSellerDetails() {
        sellerID = getIntent().getStringExtra("sellerID");

        if (sellerID != null) {
            db.collection("users").document(sellerID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String sellerName = documentSnapshot.getString("name");
                                String sellerAvatar = documentSnapshot.getString("avatar");

                                // Cập nhật giao diện chi tiết người bán
                                detailSellerName.setText(sellerName);
                                Picasso.get().load(sellerAvatar).into(detailSellerAvatar);

                                // Kiểm tra người dùng đăng nhập
                                if (auth.getCurrentUser() != null) {
                                    String currentUserID = auth.getCurrentUser().getUid();

                                    // Gọi Firestore để lấy thông tin người dùng hiện tại
                                    db.collection("users").document(currentUserID)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot userDocumentSnapshot) {
                                                    if (userDocumentSnapshot.exists()) {
                                                        // Lấy isAdmin của người dùng hiện tại
                                                        int isAdmin = userDocumentSnapshot.getLong("IsAdmin") != null ? userDocumentSnapshot.getLong("IsAdmin").intValue() : 0;

                                                        // Điều chỉnh giao diện dựa trên isAdmin
                                                        if (isAdmin == 1) {
                                                            detailEdit.setVisibility(View.GONE);
                                                            detailDelete.setVisibility(View.GONE);
                                                            detailFavButton.setVisibility(View.GONE);
                                                            reportButton.setVisibility(View.GONE);
                                                            detailSellerInfo.setVisibility(View.VISIBLE);
                                                            approveButton.setVisibility(View.VISIBLE);
                                                            rejectButton.setVisibility(View.VISIBLE);
                                                        } else if (sellerID.equals(currentUserID)) {
                                                            // Điều chỉnh giao diện nếu seller xem sản phẩm của mình
                                                            detailEdit.setVisibility(View.VISIBLE);
                                                            detailDelete.setVisibility(View.VISIBLE);
                                                            detailFavButton.setVisibility(View.GONE);
                                                            reportButton.setVisibility(View.GONE);
                                                            detailSellerInfo.setVisibility(View.GONE);
                                                            approveButton.setVisibility(View.GONE);
                                                            rejectButton.setVisibility(View.GONE);
                                                        } else {
                                                            // Điều chỉnh giao diện cho user thường
                                                            detailFavButton.setVisibility(View.VISIBLE);
                                                            reportButton.setVisibility(View.VISIBLE);
                                                            detailSellerInfo.setVisibility(View.VISIBLE);
                                                            detailEdit.setVisibility(View.GONE);
                                                            detailDelete.setVisibility(View.GONE);
                                                            approveButton.setVisibility(View.GONE);
                                                            rejectButton.setVisibility(View.GONE);
                                                        }
                                                    }
                                                }
                                            });
                                } else {
                                    // Khách vãng lai
                                    reportButton.setVisibility(View.VISIBLE);
                                    detailSellerInfo.setVisibility(View.VISIBLE);
                                    detailFavButton.setVisibility(View.GONE);
                                    detailEdit.setVisibility(View.GONE);
                                    detailDelete.setVisibility(View.GONE);
                                    approveButton.setVisibility(View.GONE);
                                    rejectButton.setVisibility(View.GONE);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xử lý lỗi
                        }
                    });
        }
    }

    private void showReportBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivity.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bao_cao_sp, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        RadioGroup radioGroupReasons = bottomSheetView.findViewById(R.id.radioGroupReasons);
        EditText editTextPhoneNumber = bottomSheetView.findViewById(R.id.editTextPhoneNumber);
        EditText editTextEmail = bottomSheetView.findViewById(R.id.editTextEmail);
        Button btnSubmitReport = bottomSheetView.findViewById(R.id.btnSubmitReport);

        btnSubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedReasonId = radioGroupReasons.getCheckedRadioButtonId();
                RadioButton selectedReason = bottomSheetView.findViewById(selectedReasonId);
                String reason = selectedReason != null ? selectedReason.getText().toString() : "";
                int userPhone = Integer.parseInt(editTextPhoneNumber.getText().toString().trim());
                String userEmail = editTextEmail.getText().toString().trim();

                // Tạo một đối tượng ReportSP
                ReportSP report = new ReportSP(productID, reason, userPhone, userEmail);

                // Gọi phương thức lưu báo cáo
                submitReport(report);

                // Đóng bottom sheet sau khi gửi
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.cancelButton).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    private void submitReport(ReportSP report) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reports_sp")
                .add(report)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(DetailActivity.this, "Báo cáo đã được gửi thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(DetailActivity.this, "Gửi báo cáo thất bại", Toast.LENGTH_SHORT).show());
    }

    private void setupViewStoreButton() {
        detailViewStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, XemProfileUserKhacActivity.class);
                intent.putExtra("sellerID", sellerID);
                startActivity(intent);
            }
        });
    }

    private void goToChatDetail() {
        //Check xem đăng nhập chưa
        if (auth.getCurrentUser() == null) {
            Intent loginIntent = new Intent(DetailActivity.this, DangNhapActivity.class);
            startActivity(loginIntent);
        }
        else {
            //Lấy ID user đang đăng nhập -> Chặn việc tự nhắn chính mình
            String currentUserID = auth.getCurrentUser().getUid();
            String chatID;

            if (sellerID.equals(currentUserID)) {
                Toast.makeText(this, "Không thể tự nhắn chính mình", Toast.LENGTH_SHORT).show();
            } else {

                //Tạo chatID
                if (currentUserID.compareTo(sellerID) < 0) {
                    chatID = currentUserID + "_" + sellerID;
                } else {
                    chatID = sellerID + "_" + currentUserID;
                }

                Intent intent = new Intent(DetailActivity.this, ChatDetailActivity.class);
                intent.putExtra("sellerID", sellerID);
                intent.putExtra("senderID", currentUserID);
                intent.putExtra("productID", productID);
                intent.putExtra("chatID", chatID);
                startActivity(intent);
            }
        }
    }

    //Hàm duyệt sản phẩm của admin
    private void updateProductApprovalStatus(String status) {
        if (productID != null) {
            db.collection("products").document(productID)
                    .update("isApproved", status)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DetailActivity.this, "Trạng thái đã được " + status, Toast.LENGTH_SHORT).show();
                            // Optionally, finish the activity or update the UI accordingly
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailActivity.this, "Trạng thái đã lỗi", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
