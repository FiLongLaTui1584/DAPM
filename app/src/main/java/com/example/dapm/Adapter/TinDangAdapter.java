package com.example.dapm.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dapm.R;
import com.example.dapm.model.TinDang;

import java.util.ArrayList;

public class TinDangAdapter extends RecyclerView.Adapter<TinDangAdapter.ViewHolder> {

    Activity context;
    ArrayList<TinDang> arr_TinDang;

    public TinDangAdapter(Activity context, ArrayList<TinDang> arr_TinDang) {
        this.context = context;
        this.arr_TinDang = arr_TinDang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewTinDang = layoutInflater.inflate(R.layout.item_tin, parent, false);
        return new ViewHolder(viewTinDang);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TinDang td = arr_TinDang.get(position);

        // Hiển thị hình ảnh đầu tiên
        Glide.with(context)
                .load(td.getProductImage1())
                .placeholder(R.drawable.def)
                .into(holder.ivHinh1);

        // Hiển thị hình ảnh thứ hai nếu có
        if (td.getProductImage2() != null && !td.getProductImage2().isEmpty()) {
            holder.ivHinh2.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(td.getProductImage2())
                    .placeholder(R.drawable.def)
                    .into(holder.ivHinh2);
        } else {
            holder.ivHinh2.setVisibility(View.GONE);
        }

        // Hiển thị hình ảnh thứ ba nếu có
        if (td.getProductImage3() != null && !td.getProductImage3().isEmpty()) {
            holder.ivHinh3.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(td.getProductImage3())
                    .placeholder(R.drawable.def)
                    .into(holder.ivHinh3);
        } else {
            holder.ivHinh3.setVisibility(View.GONE);
        }

        // Cập nhật tiêu đề và các thuộc tính khác
        holder.txtTieuDe.setText(td.getProductTitle());
        holder.txtGia.setText(String.valueOf(td.getProductPrice()));
        holder.txtTinhTrang.setText(td.getProductTinhTrang());
        holder.txtBaoHanh.setText(td.getProductBaoHanh());
        holder.txtXuatXu.setText(td.getProductXuatXu());
    }

    @Override
    public int getItemCount() {
        return arr_TinDang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHinh1, ivHinh2, ivHinh3;
        TextView txtGia, txtTieuDe, txtTinhTrang, txtBaoHanh, txtXuatXu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHinh1 = itemView.findViewById(R.id.ivHinh1);
            ivHinh2 = itemView.findViewById(R.id.ivHinh2);
            ivHinh3 = itemView.findViewById(R.id.ivHinh3);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtTieuDe = itemView.findViewById(R.id.txtTieuDe);
            txtTinhTrang = itemView.findViewById(R.id.txtTinhTrang);
            txtBaoHanh = itemView.findViewById(R.id.txtBaoHanh);
            txtXuatXu = itemView.findViewById(R.id.txtXuatXu);
        }
    }
}
