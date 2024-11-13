package com.example.dapm.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Activity.DangNhapActivity;
import com.example.dapm.Activity.DetailActivity;
import com.example.dapm.R;
import com.example.dapm.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();


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


        if (product.isFavorite()) {
            holder.productFav.setImageResource(R.drawable.ic_love_full);
        } else {
            holder.productFav.setImageResource(R.drawable.ic_love);
        }


        holder.productFav.setOnClickListener(view -> {
            String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
            if (userId != null) {
                db.collection("favorites")
                        .document(userId)
                        .collection("userFavorites")
                        .document(product.getProductID())
                        .get()
                        .addOnSuccessListener(document -> {
                            if (document.exists()) {
                                // Nếu sản phẩm đã được yêu thích, xóa khỏi danh sách yêu thích
                                db.collection("favorites")
                                        .document(userId)
                                        .collection("userFavorites")
                                        .document(product.getProductID())
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            holder.productFav.setImageResource(R.drawable.ic_love);
                                            Toast.makeText(view.getContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(view.getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            } else {
                                // Nếu sản phẩm chưa được yêu thích, thêm vào danh sách yêu thích
                                db.collection("favorites")
                                        .document(userId)
                                        .collection("userFavorites")
                                        .document(product.getProductID())
                                        .set(product)
                                        .addOnSuccessListener(aVoid -> {
                                            holder.productFav.setImageResource(R.drawable.ic_love_full);
                                            Toast.makeText(view.getContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(view.getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                        });
            } else {
                // Nếu người dùng chưa đăng nhập, chuyển đến trang đăng nhập
                Intent intent = new Intent(view.getContext(), DangNhapActivity.class);
                view.getContext().startActivity(intent);
            }
        });
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
        ImageView productImage1, productImage2, productImage3, productFav;
        TextView productTitle, productPrice, productLocation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage1 = itemView.findViewById(R.id.productImage1);
            productImage2 = itemView.findViewById(R.id.productImage2);
            productImage3 = itemView.findViewById(R.id.productImage3);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            productLocation = itemView.findViewById(R.id.productLocation);
            productFav = itemView.findViewById(R.id.productFav);
        }
    }
}
