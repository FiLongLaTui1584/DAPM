package com.example.dapm.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.model.ReportSP;
import com.example.dapm.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private List<ReportSP> reportList;
    private OnBlockProductListener listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, ProductInfo> productInfoMap = new HashMap<>();

    public ReportAdapter(List<ReportSP> reportList, OnBlockProductListener listener) {
        this.reportList = reportList;
        this.listener = listener;
        loadProductInfos();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportSP report = reportList.get(position);
        ProductInfo productInfo = productInfoMap.get(report.getProductID());

        // Kiểm tra xem productInfo có tồn tại không
        if (productInfo != null) {
            holder.productName.setText(productInfo.getProductName());
            Picasso.get().load(productInfo.getProductImage()).into(holder.productImage);
        } else {
            // Nếu không có thông tin sản phẩm, có thể ẩn hoặc thiết lập giá trị mặc định
            holder.productName.setText("Tên sản phẩm không có");
            holder.productImage.setImageResource(R.drawable.sample_image); // Hình ảnh mặc định
        }

        holder.reason.setText("Lý do: " + report.getReportReason());
        holder.itemView.setOnClickListener(v -> listener.onBlockProduct(report));
    }


    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView reason;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            reason = itemView.findViewById(R.id.reason);
        }
    }

    public interface OnBlockProductListener {
        void onBlockProduct(ReportSP report);
    }

    public void loadProductInfos() {
        for (ReportSP report : reportList) {
            db.collection("products").document(report.getProductID()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String productName = document.getString("productTitle");
                                String productImage = document.getString("productImage1");
                                productInfoMap.put(report.getProductID(), new ProductInfo(productName, productImage));
                                notifyDataSetChanged(); // Cập nhật lại adapter
                            }
                        } else {
                            // Log lỗi nếu không thành công
                            Log.e("ReportAdapter", "Error getting product info: ", task.getException());
                        }
                    });
        }
    }




    private static class ProductInfo {
        private String productName;
        private String productImage;

        public ProductInfo(String productName, String productImage) {
            this.productName = productName;
            this.productImage = productImage;
        }

        public String getProductName() {
            return productName;
        }

        public String getProductImage() {
            return productImage;
        }
    }
}
