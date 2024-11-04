package com.example.dapm.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.ProductAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> filteredProducts;
    ImageView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> finish());



        recyclerView = findViewById(R.id.recyclerViewSearchResults);
        filteredProducts = (ArrayList<Product>) getIntent().getSerializableExtra("filteredProducts");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        productAdapter = new ProductAdapter(filteredProducts);
        recyclerView.setAdapter(productAdapter);
    }
}