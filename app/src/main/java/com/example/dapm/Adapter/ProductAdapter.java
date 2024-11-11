package com.example.dapm.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Activity.DetailActivity;
import com.example.dapm.R;
import com.example.dapm.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;

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

        // Hiển thị productImage1
        Picasso.get().load(product.getProductImage1()).into(holder.productImage1);
        holder.productTitle.setText(product.getProductTitle());
        holder.productPrice.setText(String.format("%,d VNĐ", product.getProductPrice()));
        holder.productLocation.setText("Địa điểm: " + product.getProductLocation());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            intent.putExtra("productID", product.getProductID());
            intent.putExtra("productImage1", product.getProductImage1());
            intent.putExtra("productImage2", product.getProductImage2());
            intent.putExtra("productImage3", product.getProductImage3());
            intent.putExtra("productTitle", product.getProductTitle());
            intent.putExtra("productPrice", product.getProductPrice());
            intent.putExtra("productLocation", product.getProductLocation());
            intent.putExtra("productDescription", product.getProductDescription());
            intent.putExtra("productTinhTrang", product.getProductTinhTrang());
            intent.putExtra("productBaoHanh", product.getProductBaoHanh());
            intent.putExtra("productXuatXu", product.getProductXuatXu());
            intent.putExtra("productHDSD", product.getProductHDSD());
            intent.putExtra("sellerID", product.getSellerID());
            intent.putExtra("productQuantity", product.getProductQuantity());
            view.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage1, productImage2, productImage3;
        TextView productTitle, productPrice, productLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage1 = itemView.findViewById(R.id.productImage1);
            productImage2 = itemView.findViewById(R.id.productImage2);
            productImage3 = itemView.findViewById(R.id.productImage3);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            productLocation = itemView.findViewById(R.id.productLocation);
        }
    }
}
