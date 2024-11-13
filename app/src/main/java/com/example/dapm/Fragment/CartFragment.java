package com.example.dapm.Fragment;

import android.content.Intent;
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

import com.example.dapm.Activity.CheckOutActivity;
import com.example.dapm.Activity.DangNhapActivity;
import com.example.dapm.Adapter.CartAdapter;
import com.example.dapm.R;
import com.example.dapm.model.CartItem;
import com.example.dapm.model.OrderThanhToan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private Button cartDelete, cartCheckOut;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        mAuth = FirebaseAuth.getInstance();

        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getActivity(), DangNhapActivity.class);
            startActivity(intent);
            getActivity().finish();
            return rootView;
        }

        recyclerView = rootView.findViewById(R.id.cartRecyclerview);
        cartCheckOut = rootView.findViewById(R.id.cartCheckOut);
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
        // Xử lý sự kiện nhấn nút thanh toán
        cartCheckOut.setOnClickListener(v -> proceedToCheckout());

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

    //Hàm chuyển danh sách sp qua checkout
    private void proceedToCheckout() {
        ArrayList<String> selectedProductIDs = new ArrayList<>();
        ArrayList<Integer> selectedQuantities = new ArrayList<>();

        for (CartItem item : cartItemList) {
            if (item.isSelected()) {
                selectedProductIDs.add(item.getProductID());
                selectedQuantities.add(item.getCartQuantity());
            }
        }

        if (selectedProductIDs.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy sellerID từ sản phẩm đầu tiên đã chọn
        if (!selectedProductIDs.isEmpty()) {
            getProductSellerID(selectedProductIDs.get(0), selectedProductIDs, selectedQuantities);
        }
    }

    private void getProductSellerID(String productID, ArrayList<String> selectedProductIDs, ArrayList<Integer> selectedQuantities) {
        db.collection("products").document(productID)
                .get()
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    String sellerID = document.getString("sellerID");

                                    // Chuyển sang CheckOutActivity sau khi có sellerID
                                    Intent intent = new Intent(getContext(), CheckOutActivity.class);
                                    intent.putStringArrayListExtra("selectedProductIDs", selectedProductIDs); // Truyền danh sách productID
                                    intent.putIntegerArrayListExtra("selectedQuantities", selectedQuantities);
                                    intent.putExtra("sellerID", sellerID); // Truyền sellerID
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Không tìm thấy thông tin người bán.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Lỗi khi lấy thông tin người bán.", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }
}
