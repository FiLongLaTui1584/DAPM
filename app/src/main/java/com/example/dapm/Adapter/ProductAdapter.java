package com.example.dapm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.R;
import com.example.dapm.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;

    // Constructor
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set product image
        holder.productImage.setImageResource(product.getImageResId());

        // Set product title
        holder.productTitle.setText(product.getTitle());

        // Set product price
        holder.productPrice.setText(product.getPrice());

        // Set product date and location
        String infoText = "Ngày đăng: " + product.getDate() + "\nĐịa điểm: " + product.getLocation();
        holder.productInfo.setText(infoText);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitle, productPrice, productInfo;
        ImageView favoriteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            productInfo = itemView.findViewById(R.id.productInfo);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
        }
    }
}
