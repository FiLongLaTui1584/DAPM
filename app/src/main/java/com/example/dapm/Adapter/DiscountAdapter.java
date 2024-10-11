package com.example.dapm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.R;
import com.example.dapm.model.Discount;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder> {
    private List<Discount> discountList;

    // Constructor đúng
    public DiscountAdapter(List<Discount> discountList) {
        this.discountList = discountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Discount discount = discountList.get(position);
        holder.discountImage.setImageResource(discount.getImageResId());
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView discountImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            discountImage = itemView.findViewById(R.id.discountimage);
        }
    }
}
