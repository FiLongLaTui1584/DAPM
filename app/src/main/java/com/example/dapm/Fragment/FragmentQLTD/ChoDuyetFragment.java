package com.example.dapm.Fragment.FragmentQLTD;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.ProductAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChoDuyetFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cho_duyet, container, false);
        addControl(view);
        setupRecyclerView();
        loadProductsFromFirestore();
        return view;
    }

    private void addControl(View view) {
        recyclerView = view.findViewById(R.id.recyclerChoDuyet);
        auth = FirebaseAuth.getInstance();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
    }

    private void loadProductsFromFirestore() {
        String currentUserID = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (currentUserID == null) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập để xem sản phẩm chờ duyệt", Toast.LENGTH_SHORT).show();
            return;
        }

        db = FirebaseFirestore.getInstance();
        db.collection("products")
                .whereEqualTo("isApproved", "pending") // Lấy sản phẩm chờ duyệt
                .whereEqualTo("sellerID", currentUserID) // Chỉ lấy sản phẩm của người dùng hiện tại
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
                        String categoryID = document.getString("categoryID");
                        String isApproved = document.getString("isApproved");
                        int productQuantity  = document.contains("productQuantity") ? document.getLong("productQuantity").intValue() : 0;

                        Product product = new Product(
                                productID, productImage1, productImage2, productImage3, title, price, location,
                                productDescription, productTinhTrang, productBaoHanh, productXuatXu,
                                productHDSD, sellerID, categoryID, isApproved, productQuantity
                        );

                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("ChoDuyetFragment", "Error loading products", e));
    }
}
