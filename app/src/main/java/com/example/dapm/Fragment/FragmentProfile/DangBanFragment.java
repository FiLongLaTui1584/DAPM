package com.example.dapm.Fragment.FragmentProfile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.ProductAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DangBanFragment extends Fragment {
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private FirebaseFirestore db;
    private String sellerID;

    public static DangBanFragment newInstance(String sellerID) {
        DangBanFragment fragment = new DangBanFragment();
        Bundle args = new Bundle();
        args.putString("sellerID", sellerID);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_ban, container, false);

        if (getArguments() != null) {
            sellerID = getArguments().getString("sellerID");
        }

        recyclerViewProducts = view.findViewById(R.id.recycler_view_dang_ban);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(gridLayoutManager);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);

        db = FirebaseFirestore.getInstance();

        if (sellerID != null) {
            loadProductsBySellerID(sellerID);
        }

        return view;
    }

    private void loadProductsBySellerID(String sellerID) {
        db.collection("products")
                .whereEqualTo("sellerID", sellerID)
                .whereEqualTo("isApproved", "approved")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
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
                .addOnFailureListener(e -> Log.e("DangBanFragment", "Lỗi khi tải sản phẩm cho người bán", e));
    }
}
