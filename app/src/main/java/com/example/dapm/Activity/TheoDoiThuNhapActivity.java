package com.example.dapm.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.OrderAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Order;

import java.util.ArrayList;
import java.util.List;

public class TheoDoiThuNhapActivity extends AppCompatActivity {

    private ImageView cancel;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theo_doi_thu_nhap);

        addControl();
        addEvent();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = getSampleOrders(); // Replace with actual data
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);
    }

    private void addEvent() {
        cancel.setOnClickListener(v -> finish());
    }

    private void addControl() {
        recyclerView = findViewById(R.id.recyclerview_orders);
        cancel = findViewById(R.id.cancel);
    }


    // Sample data (replace with actual data source)
    private List<Order> getSampleOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("Order 1", R.drawable.sample_image, "+ 100.000 VND", "6/10/2024"));
        orders.add(new Order("Order 2", R.drawable.sample_image, "+ 200.000 VND", "7/10/2024"));
        orders.add(new Order("Order 3", R.drawable.sample_image, "+ 456.789 VND", "8/10/2024"));
        orders.add(new Order("Order 1", R.drawable.sample_image, "+ 100.000 VND", "6/10/2024"));
        orders.add(new Order("Order 2", R.drawable.sample_image, "+ 200.000 VND", "7/10/2024"));
        orders.add(new Order("Order 3", R.drawable.sample_image, "+ 456.789 VND", "8/10/2024"));
        orders.add(new Order("Order 1", R.drawable.sample_image, "+ 100.000 VND", "6/10/2024"));
        orders.add(new Order("Order 2", R.drawable.sample_image, "+ 200.000 VND", "7/10/2024"));
        orders.add(new Order("Order 3", R.drawable.sample_image, "+ 456.789 VND", "8/10/2024"));
        orders.add(new Order("Order 1", R.drawable.sample_image, "+ 100.000 VND", "6/10/2024"));
        orders.add(new Order("Order 2", R.drawable.sample_image, "+ 200.000 VND", "7/10/2024"));
        orders.add(new Order("Order 3", R.drawable.sample_image, "+ 456.789 VND", "8/10/2024"));
        return orders;
    }
}
