package com.example.dapm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dapm.Adapter.ImageAdapter;
import com.example.dapm.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    private ImageView cancel, detailSellerAvatar;
    private String sellerID;
    private LinearLayout detailViewStoreButton;
    private FirebaseFirestore db;


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

        db = FirebaseFirestore.getInstance();
    }

    private void displayProductDetails() {
        sellerID = getIntent().getStringExtra("sellerID");

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
        if (sellerID != null) {
            db.collection("users").document(sellerID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String sellerName = documentSnapshot.getString("name");
                                String sellerAvatar = documentSnapshot.getString("avatar");

                                // Cập nhật UI
                                detailSellerName.setText(sellerName);
                                Picasso.get().load(sellerAvatar).into(detailSellerAvatar);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }

    private void showReportBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivity.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bao_cao_sp, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetView.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
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
}
