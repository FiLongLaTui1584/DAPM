package com.example.dapm.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.dapm.Activity.DangNhapActivity;
import com.example.dapm.Activity.DanhSachChatActivity;
import com.example.dapm.Activity.DetailActivity;
import com.example.dapm.Activity.SearchResultsActivity;
import com.example.dapm.Adapter.CategoryAdapter;
import com.example.dapm.Adapter.DiscountAdapter;
import com.example.dapm.Adapter.ProductAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Category;
import com.example.dapm.model.Discount;
import com.example.dapm.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerViewCategories, recyclerViewProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<Category> categoryList;
    private List<Product> productList;
    private List<Product> allProducts;
    private ImageButton imgChat;
    private FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addControl(view);
        addIntent();

        setupCategoriesRecyclerView(view);
        /*setupDiscountsRecyclerView(view);*/
        setupProductRecyclerView(view);

        return view;
    }

    private void setupCategoriesRecyclerView(View view) {
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        StaggeredGridLayoutManager staggeredGridLayoutManagerCategories = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.HORIZONTAL
        );
        recyclerViewCategories.setLayoutManager(staggeredGridLayoutManagerCategories);

        db = FirebaseFirestore.getInstance();
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerViewCategories.setAdapter(categoryAdapter);

        db.collection("categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    categoryList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String categoryName = document.getString("categoryName");
                        String categoryImageURL = document.getString("categoryImageURL");
                        String categoryID = document.getId(); // Lấy ID của category

                        Category category = new Category(categoryName, categoryImageURL, categoryID);
                        categoryList.add(category);
                    }
                    categoryAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("HomeFragment", "Error loading categories", e));

    }


    private void setupProductRecyclerView(View view) {
        db = FirebaseFirestore.getInstance();

        // Khởi tạo RecyclerView cho Products
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(gridLayoutManager);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);

        loadProductsFromFirestore(); // Tải dữ liệu từ Firestore
        setupSearchFunctionality(view);
    }

    private void loadProductsFromFirestore() {
        db.collection("products")
                .whereEqualTo("isApproved", "approved") // Chỉ lấy sản phẩm đã được duyệt
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allProducts = new ArrayList<>();
                    productList.clear();

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String productID = document.getId(); // Lấy productID từ DocumentSnapshot
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

                        // Chỉ thêm sản phẩm vào danh sách nếu đã được duyệt
                        if ("approved".equals(isApproved)) {
                            Product product = new Product(
                                    productID, productImage1, productImage2, productImage3, title, price, location,
                                    productDescription, productTinhTrang, productBaoHanh, productXuatXu,
                                    productHDSD, sellerID, categoryID, isApproved
                            );
                            allProducts.add(product);
                            productList.add(product);
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("HomeFragment", "Error loading products", e));
    }

    private void addControl(View view) {
        imgChat = view.findViewById(R.id.HomeChat);
    }

    private void addIntent() {
        imgChat.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DanhSachChatActivity.class);
            startActivity(intent);
        });
    }

    // Thêm phương thức cho nút tìm kiếm
    private void setupSearchFunctionality(View view) {
        EditText editSearch = view.findViewById(R.id.edit_search);
        ImageView searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            String query = editSearch.getText().toString().toLowerCase();
            List<Product> filteredProducts = new ArrayList<>();

            for (Product product : allProducts) {
                if (product.getProductTitle().toLowerCase().contains(query)) {
                    filteredProducts.add(product);
                }
            }

            // Chuyển đến SearchResultsActivity với danh sách sản phẩm đã lọc
            Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
            intent.putExtra("filteredProducts", (ArrayList<Product>) filteredProducts); // Gửi danh sách đã lọc
            startActivity(intent);
        });
    }
}
