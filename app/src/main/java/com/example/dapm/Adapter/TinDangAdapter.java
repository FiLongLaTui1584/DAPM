package com.example.dapm.Adapter;

import android.app.Activity;
import android.app.Notification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.R;
import com.example.dapm.model.TinDang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;

public class TinDangAdapter extends RecyclerView.Adapter<TinDangAdapter.ViewHolder>{

    Activity context;

    ArrayList<TinDang> arr_TinDang;

    public TinDangAdapter(Activity context,ArrayList<TinDang> arr_TinDang){
        this.context=context;
        this.arr_TinDang=arr_TinDang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewTinDang= layoutInflater.inflate(R.layout.itemtin,parent,false);
        ViewHolder viewHolderTD =new ViewHolder(viewTinDang);
        return viewHolderTD;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TinDang td=arr_TinDang.get(position);
        holder.ivHinh.setImageResource(td.getHinhanh());
        holder.txtTieuDe.setText(td.getTieuDeTinDang());
        holder.txtGia.setText(td.getGia());

    }

    @Override
    public int getItemCount() {
        return arr_TinDang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivHinh;
        TextView  txtGia,txtTieuDe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHinh=itemView.findViewById(R.id.ivHinh);
            txtGia=itemView.findViewById(R.id.txtGia);
            txtTieuDe=itemView.findViewById(R.id.txtTieuDe);
        }
    }
}

