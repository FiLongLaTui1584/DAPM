package com.example.dapm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.R;
import com.example.dapm.model.OrderThanhToan;

import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ViewHolder> {

    private List<OrderThanhToan> orderList;
    private Context context;

    public CheckOutAdapter(List<OrderThanhToan> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_mua_thanh_toan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderThanhToan order = orderList.get(position);
        holder.orderImage.setImageResource(order.getImageResource());
        holder.orderName.setText(order.getName());
        holder.orderPrice.setText(order.getPrice());
        holder.orderQuantity.setText("x " + order.getQuantity());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView orderImage;
        TextView orderName, orderPrice, orderQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.order_image);
            orderName = itemView.findViewById(R.id.order_name);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderQuantity = itemView.findViewById(R.id.order_quantity);
        }
    }
}

