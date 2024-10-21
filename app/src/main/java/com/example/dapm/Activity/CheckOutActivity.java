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

public class CheckOutActivity extends AppCompatActivity {

    private RecyclerView checkOutView;
    private CheckOutAdapter checkOutAdapter;
    private List<OrderThanhToan> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        checkOutView = findViewById(R.id.CheckOutView);
        checkOutView.setLayoutManager(new LinearLayoutManager(this));

        // Example order data
        orderList = new ArrayList<>();
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 1", "123.456 VND", 1));
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 2", "789.012 VND", 2));
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 1", "123.456 VND", 1));
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 2", "789.012 VND", 2));
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 1", "123.456 VND", 1));
        orderList.add(new OrderThanhToan(R.drawable.sample_image, "Product 2", "789.012 VND", 2));

        checkOutAdapter = new CheckOutAdapter(orderList, this);
        checkOutView.setAdapter(checkOutAdapter);
    }
}
