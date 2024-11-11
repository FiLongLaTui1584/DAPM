package com.example.dapm.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dapm.Adapter.ImageAdapter;
import com.example.dapm.R;
import com.example.dapm.model.ReportSP;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    private RecyclerView recyclerViewImages;
    private TextView detailTitle, detailPrice, detailLocation, detailQuantity,
            detailDescription, detailTinhTrang, detailBaoHanh, detailXuatXu, detailHDSD, detailSellerName;
    private EditText editTitle, editPrice, editDescription, editLocation, editTinhTrang, editBaoHanh, editXuatXu, editHDSD, editQuantity;
    private ImageButton reportButton;
    private ImageView cancel, detailSellerAvatar, detailEdit, detailDelete, detailFavButton;
    private String sellerID, productID;
    private LinearLayout detailViewStoreButton, detailChatButton, detailSellerInfo,approveButton, rejectButton, detailCartButton;
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

    //Hàm thêm sự kiện
    private void addEvent() {
        productID = getIntent().getStringExtra("productID");

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

        detailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo AlertDialog để xác nhận xóa
                new AlertDialog.Builder(DetailActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Thực hiện xóa sản phẩm khi người dùng xác nhận
                                db.collection("products").document(productID)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(DetailActivity.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                                finish(); // Đóng DetailActivity sau khi xóa
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(DetailActivity.this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Hủy", null) // Đóng dialog nếu chọn Hủy
                        .show();
            }
        });


        detailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditBottomSheet();
            }
        });

        detailCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

    }

    //Hàm thêm control
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
        detailQuantity = findViewById(R.id.detailQuantity);
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
        detailCartButton = findViewById(R.id.detailCartButton);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    //Hàm hiển thị thông tin sản phẩm
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
        int productQuantity = getIntent().getIntExtra("productQuantity", 0);


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
        detailQuantity.setText("Còn " + String.valueOf(productQuantity) + " sản phẩm");
    }

    //Hàm lấy thông tin người bán
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

    //Hàm hiện báo cáo
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

                String phoneInput = editTextPhoneNumber.getText().toString().trim();
                String emailInput = editTextEmail.getText().toString().trim();

                // Kiểm tra xem số điện thoại và email có được nhập đầy đủ không
                if (phoneInput.isEmpty() || emailInput.isEmpty()) {
                    Toast.makeText(DetailActivity.this, "Vui lòng nhập đầy đủ số điện thoại và email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển đổi số điện thoại từ String sang int
                int userPhone = Integer.parseInt(phoneInput);

                // Tạo một đối tượng ReportSP
                ReportSP report = new ReportSP(productID, reason, userPhone, emailInput);

                // Gọi phương thức lưu báo cáo
                submitReport(report);

                // Đóng bottom sheet sau khi gửi
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.cancelButton).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    //Hàm gửi báo cáo
    private void submitReport(ReportSP report) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reports_sp")
                .add(report)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(DetailActivity.this, "Báo cáo đã được gửi thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(DetailActivity.this, "Gửi báo cáo thất bại", Toast.LENGTH_SHORT).show());
    }

    //Hàm mở cửa sổ xem profile
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

    //Hàm chuyển sang chat
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

    //Hàm hiện bottomsheet sửa sp
    private void showEditBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivity.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheet_edit, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Liên kết các trường EditText trong BottomSheet với mã
        editTitle = bottomSheetView.findViewById(R.id.editTitle);
        editPrice = bottomSheetView.findViewById(R.id.editPrice);
        editQuantity = bottomSheetView.findViewById(R.id.editQuantity);
        editDescription = bottomSheetView.findViewById(R.id.editDescription);
        editLocation = bottomSheetView.findViewById(R.id.editLocation);
        editTinhTrang = bottomSheetView.findViewById(R.id.editTinhTrang);
        editBaoHanh = bottomSheetView.findViewById(R.id.editBaoHanh);
        editXuatXu = bottomSheetView.findViewById(R.id.editXuatXu);
        editHDSD = bottomSheetView.findViewById(R.id.editHDSD);

        // Thiết lập giá trị hiện tại vào các trường
        editTitle.setText(detailTitle.getText().toString());
        editPrice.setText(detailPrice.getText().toString());
        editQuantity.setText(detailQuantity.getText().toString());
        editDescription.setText(detailDescription.getText().toString());
        editLocation.setText(detailLocation.getText().toString());
        editTinhTrang.setText(detailTinhTrang.getText().toString());
        editBaoHanh.setText(detailBaoHanh.getText().toString());
        editXuatXu.setText(detailXuatXu.getText().toString());
        editHDSD.setText(detailHDSD.getText().toString());

        // Xử lý nút lưu thay đổi
        AppCompatButton btnSave = bottomSheetView.findViewById(R.id.saveButton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức cập nhật sản phẩm lên Firestore
                updateProductDetails();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    // Phương thức cập nhật thông tin sản phẩm lên Firestore
    private void updateProductDetails() {
        // Lấy các giá trị đã chỉnh sửa
        String updatedTitle = editTitle.getText().toString().trim();
        int updatedPrice = Integer.parseInt(editPrice.getText().toString().trim().replaceAll("[^\\d]", ""));
        int updatedQuantity = Integer.parseInt(editQuantity.getText().toString().trim());
        String updatedDescription = editDescription.getText().toString().trim();
        String updatedLocation = editLocation.getText().toString().trim();
        String updatedTinhTrang = editTinhTrang.getText().toString().trim();
        String updatedBaoHanh = editBaoHanh.getText().toString().trim();
        String updatedXuatXu = editXuatXu.getText().toString().trim();
        String updatedHDSD = editHDSD.getText().toString().trim();

        // Tạo một map chứa các trường cần cập nhật
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("productTitle", updatedTitle);
        updatedFields.put("productPrice", updatedPrice);
        updatedFields.put("productQuantity", updatedQuantity);
        updatedFields.put("productDescription", updatedDescription);
        updatedFields.put("productLocation", updatedLocation);
        updatedFields.put("productTinhTrang", updatedTinhTrang);
        updatedFields.put("productBaoHanh", updatedBaoHanh);
        updatedFields.put("productXuatXu", updatedXuatXu);
        updatedFields.put("productHDSD", updatedHDSD);

        // Gửi cập nhật lên Firestore
        db.collection("products").document(productID)
                .update(updatedFields)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        displayProductDetails(); // Cập nhật giao diện
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Phương thức thêm sản phẩm vào giỏ hàng, số lượng mặc định 1
    private void addToCart() {
        //Check xem đăng nhập chưa
        if (auth.getCurrentUser() == null) {
            Intent loginIntent = new Intent(DetailActivity.this, DangNhapActivity.class);
            startActivity(loginIntent);
        }
        else {
            String currentUserID = auth.getCurrentUser().getUid();

            // Tạo đối tượng cartItem để thêm vào giỏ hàng
            Map<String, Object> cartItem = new HashMap<>();
            cartItem.put("productID", productID);  // ID sản phẩm
            cartItem.put("cartQuantity", 1);  // Số lượng mặc định là 1

            // Thêm sản phẩm vào giỏ hàng của người dùng
            db.collection("carts").document(currentUserID)
                    .collection("cartItems")
                    .document(productID)  // Dùng productID làm document ID trong cartItems
                    .set(cartItem)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DetailActivity.this, "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailActivity.this, "Không thể thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
