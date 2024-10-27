package com.example.dapm.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dapm.Adapter.CartAdapter;
import com.example.dapm.R;
import com.example.dapm.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    RecyclerView recyclerViewCart;
    CartAdapter cartAdapter;
    ArrayList<CartItem> cartItemList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container,false);
        addControls(view);
        loadData();
        return view;
    }

    private void loadData() {
        cartItemList.add(new CartItem("quan que",R.drawable.def,"L","5","5000000"));
    }

    private void addControls(View view) {
        recyclerViewCart = view.findViewById(R.id.RecycleViewCart);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartItemList = new ArrayList<>();

        cartAdapter = new CartAdapter(getActivity(),cartItemList);
        recyclerViewCart.setAdapter(cartAdapter);
    }

}