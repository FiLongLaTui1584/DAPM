package com.example.dapm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.ProductAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class QuanLyYeuThichActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_yeu_thich);

        recyclerViewProducts = findViewById(R.id.recyclerViewSPYeuthich);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewProducts.setLayoutManager(gridLayoutManager);
        ImageView goBackButton = findViewById(R.id.goback);
        // Sample data for Products
        productList = new ArrayList<>();/*
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 1", "9.999.999 đ", "30/2/2024", "Tp Hồ Chí Minh"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 2", "8.888.888 đ", "29/2/2024", "Hà Nội"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 3", "7.777.777 đ", "28/2/2024", "Đà Nẵng"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 4", "6.666.666 đ", "27/2/2024", "Cần Thơ"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 5", "5.555.555 đ", "26/2/2024", "Hải Phòng"));*/
        goBackButton.setOnClickListener(v -> onBackPressed());
        // Initialize ProductAdapter
        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);
        loadFavoriteProducts();
    }

    private void loadFavoriteProducts() {
            String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
            if (userId != null) {
                db.collection("favorites")
                        .document(userId)
                        .collection("userFavorites")
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                Product product = document.toObject(Product.class);
                                productList.add(product);
                            }
                            productAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Error loading favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                // Not logged in, prompt login
                Toast.makeText(this, "Please log in to view your favorites.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
    }
}
