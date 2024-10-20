package com.example.dapm.Fragment.FragmentProfile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dapm.Adapter.CategoryAdapter;
import com.example.dapm.Adapter.DiscountAdapter;
import com.example.dapm.Adapter.ProductAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Category;
import com.example.dapm.model.Discount;
import com.example.dapm.model.Product;

import java.util.ArrayList;
import java.util.List;

public class DangBanFragment extends Fragment {
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_ban, container, false);

        recyclerViewProducts = view.findViewById(R.id.recycler_view_dang_ban);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(gridLayoutManager);

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

        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);

        return view;
    }
}
