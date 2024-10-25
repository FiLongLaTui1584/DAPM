package com.example.dapm.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.dapm.Activity.DanhSachChatActivity;
import com.example.dapm.Adapter.CategoryAdapter;
import com.example.dapm.Adapter.DiscountAdapter;
import com.example.dapm.Adapter.ProductAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Category;
import com.example.dapm.model.Discount;
import com.example.dapm.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewCategories, recyclerViewDiscounts, recyclerViewProducts;
    private CategoryAdapter categoryAdapter;
    private DiscountAdapter discountAdapter;
    private ProductAdapter productAdapter;
    private List<Category> categoryList;
    private List<Discount> discountList;
    private List<Product> productList;
    private ImageButton imgChat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addControl(view);
        addIntent();

        // Khởi tạo RecyclerView cho Categories
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        StaggeredGridLayoutManager staggeredGridLayoutManagerCategories = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.HORIZONTAL
        );
        recyclerViewCategories.setLayoutManager(staggeredGridLayoutManagerCategories);

        // Dữ liệu mẫu cho Categories
        categoryList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            categoryList.add(new Category("Danh mục nè", R.drawable.sample_image));
        }

        // Khởi tạo Adapter cho Categories
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerViewCategories.setAdapter(categoryAdapter);

        // Khởi tạo RecyclerView cho Discounts
        recyclerViewDiscounts = view.findViewById(R.id.recyclerViewDiscount);
        StaggeredGridLayoutManager staggeredGridLayoutManagerDiscounts = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.HORIZONTAL
        );
        recyclerViewDiscounts.setLayoutManager(staggeredGridLayoutManagerDiscounts);

        // Dữ liệu mẫu cho Discounts
        discountList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            discountList.add(new Discount(R.drawable.sample_image));
        }

        // Khởi tạo Adapter cho Discounts
        discountAdapter = new DiscountAdapter(discountList);
        recyclerViewDiscounts.setAdapter(discountAdapter);

        // Khởi tạo RecyclerView cho Products
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProduct);

        // Sử dụng GridLayoutManager để hiển thị 2 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(gridLayoutManager);

        // Dữ liệu mẫu cho Products
        productList = new ArrayList<>();
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 1", "9.999.999 đ", "30/2/2024", "Tp Hồ Chí Minh"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 2", "8.888.888 đ", "29/2/2024", "Hà Nội"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 3", "7.777.777 đ", "28/2/2024", "Đà Nẵng"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 4", "6.666.666 đ", "27/2/2024", "Cần Thơ"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 5", "5.555.555 đ", "26/2/2024", "Hải Phòng"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 1", "9.999.999 đ", "30/2/2024", "Tp Hồ Chí Minh"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 2", "8.888.888 đ", "29/2/2024", "Hà Nội"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 3", "7.777.777 đ", "28/2/2024", "Đà Nẵng"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 4", "6.666.666 đ", "27/2/2024", "Cần Thơ"));
        productList.add(new Product(R.drawable.sample_image, "Sản phẩm 5", "5.555.555 đ", "26/2/2024", "Hải Phòng"));

        // Khởi tạo Adapter cho Products
        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);

        return view;
    }

    private void addControl(View view) {
        imgChat = view.findViewById(R.id.HomeChat);
    }

    private void addIntent() {
        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachChatActivity.class);
                startActivity(intent);
            }
        });
    }
}
