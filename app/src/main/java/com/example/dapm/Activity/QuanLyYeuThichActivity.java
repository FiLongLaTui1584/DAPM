package com.example.dapm.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.ProductAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Product;

import java.util.ArrayList;
import java.util.List;

public class QuanLyYeuThichActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_yeu_thich);

        // Initialize RecyclerView
        recyclerViewProducts = findViewById(R.id.recyclerViewSPYeuthich);

        // Use GridLayoutManager for 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewProducts.setLayoutManager(gridLayoutManager);

        // Sample data for Products
        productList = new ArrayList<>();/*
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 1", "9.999.999 đ", "30/2/2024", "Tp Hồ Chí Minh"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 2", "8.888.888 đ", "29/2/2024", "Hà Nội"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 3", "7.777.777 đ", "28/2/2024", "Đà Nẵng"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 4", "6.666.666 đ", "27/2/2024", "Cần Thơ"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 5", "5.555.555 đ", "26/2/2024", "Hải Phòng"));*/

        // Initialize ProductAdapter
        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);
    }
}
