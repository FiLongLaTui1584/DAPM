package com.example.dapm.Activity.ADMIN.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.ProductAdapter;
import com.example.dapm.model.Product;
import com.example.dapm.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DaDuyetFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_da_duyet, container, false);
        addControl(view);
        setupRecyclerView();
        loadApprovedProductsFromFirestore();
        return view;
    }

    private void addControl(View view) {
        recyclerView = view.findViewById(R.id.recyclerDaDuyet);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
    }

    private void loadApprovedProductsFromFirestore() {
        db = FirebaseFirestore.getInstance();
        db.collection("products")
                .whereEqualTo("isApproved", "approved")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String productID = document.getId();
                        String productImage1 = document.getString("productImage1");
                        String productImage2 = document.getString("productImage2");
                        String productImage3 = document.getString("productImage3");
                        String title = document.getString("productTitle");
                        int price = document.contains("productPrice") ? document.getLong("productPrice").intValue() : 0;
                        String location = document.getString("productLocation");
                        String productDescription = document.getString("productDescription");
                        String productTinhTrang = document.getString("productTinhTrang");
                        String productBaoHanh = document.getString("productBaoHanh");
                        String productXuatXu = document.getString("productXuatXu");
                        String productHDSD = document.getString("productHDSD");
                        String sellerID = document.getString("sellerID");
                        String isApproved = document.getString("isApproved");

                        Product product = new Product(
                                productID, productImage1, productImage2, productImage3, title, price, location,
                                productDescription, productTinhTrang, productBaoHanh, productXuatXu,
                                productHDSD, sellerID, isApproved
                        );

                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("DaDuyetFragment", "Error loading approved products", e));
    }
}
