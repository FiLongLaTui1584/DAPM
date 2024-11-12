package com.example.dapm.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.CheckOutAdapter;
import com.example.dapm.R;
import com.example.dapm.model.OrderThanhToan;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private RecyclerView orderDetailView;
    private CheckOutAdapter orderDetailAdapter;
    private List<OrderThanhToan> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderDetailView = findViewById(R.id.OrderDetailView);
        orderDetailView.setLayoutManager(new LinearLayoutManager(this));

        // Example order data
        /*orderList = new ArrayList<>();
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 1", "123.456 VND", 1));
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 2", "789.012 VND", 2));
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 1", "123.456 VND", 1));
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 2", "789.012 VND", 2));
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 1", "123.456 VND", 1));
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 2", "789.012 VND", 2));*/

        orderDetailAdapter = new CheckOutAdapter(orderList, this);
        orderDetailView.setAdapter(orderDetailAdapter);
    }
}
