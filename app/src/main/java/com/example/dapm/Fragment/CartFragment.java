package com.example.dapm.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.CartAdapter;
import com.example.dapm.R;
import com.example.dapm.model.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView cartTotalPrice;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button cartDelete;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = rootView.findViewById(R.id.cartRecyclerview);
        cartTotalPrice = rootView.findViewById(R.id.cartTotalPrice);
        cartDelete = rootView.findViewById(R.id.cartDelete);

        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(getContext(), cartItemList, this::onQuantityChanged, this::onCheckboxClicked);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cartAdapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loadData();
        // Xử lý sự kiện nhấn nút xóa
        cartDelete.setOnClickListener(v -> deleteSelectedItems());


        return rootView;
    }

    private void loadData() {
        String userID = mAuth.getCurrentUser().getUid();
        db.collection("carts").document(userID).collection("cartItems")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cartItemList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String productID = document.getId();
                            int cartQuantity = document.getLong("cartQuantity").intValue();
                            getProductInfo(productID, cartQuantity);
                        }
                    } else {
                        Toast.makeText(getContext(), "Không thể tải giỏ hàng!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getProductInfo(String productID, int cartQuantity) {
        db.collection("products").document(productID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String productName = document.getString("productTitle");
                            String productImage = document.getString("productImage1");
                            int productPrice = document.getLong("productPrice").intValue();

                            CartItem cartItem = new CartItem(productID, productName, productImage, productPrice, cartQuantity);
                            cartItemList.add(cartItem);
                            cartAdapter.notifyDataSetChanged();
                            updateTotalPrice();
                        }
                    } else {
                        Toast.makeText(getContext(), "Không thể tải thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTotalPrice() {
        int totalPrice = 0;
        for (CartItem item : cartItemList) {
            if (item.isSelected()) {
                totalPrice += item.getCartProductPrice() * item.getCartQuantity();
            }
        }
        cartTotalPrice.setText(String.format("%,d VNĐ",totalPrice));
    }

    private void onQuantityChanged() {
        updateTotalPrice();
    }

    private void onCheckboxClicked() {
        updateTotalPrice();
    }

    // Hàm xóa các sản phẩm được chọn
    private void deleteSelectedItems() {
        String userID = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lọc các sản phẩm được chọn
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : cartItemList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }

        // Nếu không có sản phẩm nào được chọn
        if (selectedItems.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn sản phẩm để xóa!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xóa các sản phẩm đã chọn
        for (CartItem item : selectedItems) {
            db.collection("carts")
                    .document(userID)
                    .collection("cartItems")
                    .document(item.getProductID())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Xóa thành công, cập nhật lại giỏ hàng
                        cartItemList.remove(item);
                        cartAdapter.notifyDataSetChanged();
                        updateTotalPrice();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
