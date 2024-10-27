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
import com.example.dapm.model.CartItem;
import com.example.dapm.model.TinDang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    Activity context;

    ArrayList<CartItem> cartitemlist;

    public CartAdapter(Activity context, ArrayList<CartItem> cartItemslist){
        this.context=context;
        this.cartitemlist=cartItemslist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewTinDang= layoutInflater.inflate(R.layout.item_cart,parent,false);
        ViewHolder viewHoldercart =new ViewHolder(viewTinDang);
        return viewHoldercart;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem ct=cartitemlist.get(position);
        holder.ivHinh.setImageResource(ct.getImageRes());
        holder.txtTieuDe.setText(ct.getName());
        holder.txtGia.setText(ct.getPrice());
        holder.txtSoluong.setText(ct.getQuantity());
    }

    @Override
    public int getItemCount() {
        return cartitemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivHinh;
        TextView  txtGia,txtTieuDe,txtKichthuoc,txtSoluong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHinh=itemView.findViewById(R.id.imageViewCart);
            txtGia=itemView.findViewById(R.id.txtGiaCart);
            txtTieuDe=itemView.findViewById(R.id.txtTenspCart);
            txtKichthuoc= itemView.findViewById(R.id.txtKichthuocspCart);
            txtSoluong=itemView.findViewById(R.id.soluongCart);

        }
    }
}

