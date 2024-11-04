package com.example.dapm.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.ProductAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryProductsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private TextView categoryTitle;
    private ImageView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        cancel=findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> finish());

        categoryTitle = findViewById(R.id.categoryTitle);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        db = FirebaseFirestore.getInstance();
        String categoryID = getIntent().getStringExtra("categoryID");

        loadCategoryName(categoryID); // Gọi hàm lấy tên danh mục
        loadProductsByCategory(categoryID);
    }

    private void loadCategoryName(String categoryID) {
        db.collection("categories").document(categoryID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String categoryName = documentSnapshot.getString("categoryName");
                        categoryTitle.setText(categoryName); // Đặt tên danh mục vào TextView
                    }
                })
                .addOnFailureListener(e -> Log.e("CategoryProductsActivity", "Error loading category name", e));
    }

    private void loadProductsByCategory(String categoryID) {
        db.collection("products")
                .whereEqualTo("categoryID", categoryID)
                .whereEqualTo("isApproved", "approved")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("CategoryProductsActivity", "Error loading products", e));
    }
}
