package com.example.dapm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.R;
import com.example.dapm.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orderList;

    // Constructor
    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each order (assuming 'item_order.xml' layout file)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get current order from the list
        Order order = orderList.get(position);

        // Set order image (local drawable resource)
        holder.orderImage.setImageResource(order.getImageResId());

        // Set order name
        holder.orderName.setText(order.getName());

        // Set order income
        holder.orderIncome.setText(order.getIncome());

        // Set order date
        holder.orderDate.setText(order.getDate());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // ViewHolder class to hold the views for each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView orderImage;
        TextView orderName, orderIncome, orderDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bind views to the ViewHolder using IDs from the 'item_order.xml'
            orderImage = itemView.findViewById(R.id.order_image);
            orderName = itemView.findViewById(R.id.order_name);
            orderIncome = itemView.findViewById(R.id.order_income);
            orderDate = itemView.findViewById(R.id.order_date);
        }
    }
}
