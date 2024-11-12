package com.example.dapm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.CheckOutAdapter;
import com.example.dapm.R;
import com.example.dapm.model.OrderThanhToan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {

    private RecyclerView checkOutView;
    private CheckOutAdapter checkOutAdapter;
    private List<OrderThanhToan> orderList;
    private EditText buyerName, buyerPhone, buyerAddress;
    private TextView sellerBankName, sellerBankNumber;
    private TextView tongTienHang, tienVanChuyen, tongHoaDon;
    private static final int TIEN_VAN_CHUYEN = 30000;
    private ImageView sellerBankImage, cancel;
    private Button checkOutBtn;
    private RadioGroup paymentOptions;
    private RadioButton cash, banking;
    private LinearLayout bankingOption;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        addControl();
        addEvent();

        // Nhận danh sách productID và số lượng từ Intent
        ArrayList<String> productIDs = getIntent().getStringArrayListExtra("selectedProductIDs");
        ArrayList<Integer> quantities = getIntent().getIntegerArrayListExtra("selectedQuantities");

        // Khởi tạo danh sách orderList và adapter
        orderList = new ArrayList<>();

        checkOutAdapter = new CheckOutAdapter(orderList, this);
        checkOutView.setAdapter(checkOutAdapter);

        // Gọi hàm để tải thông tin sản phẩm từ Firestore
        if (productIDs != null && quantities != null) {
            loadProductDetails(productIDs, quantities);
        }

        loadBuyerInfo();

        String sellerID = getIntent().getStringExtra("sellerID");
        if (sellerID != null) {
            loadSellerInfo(sellerID);
        }

        // Tính toán tổng chi phí và hiển thị
        calculateTotal();
    }

    private void addControl() {
        checkOutView = findViewById(R.id.RecyclerViewCheckout);
        checkOutView.setLayoutManager(new LinearLayoutManager(this));

        paymentOptions = findViewById(R.id.paymentOptions);
        cash = findViewById(R.id.cash);
        banking = findViewById(R.id.banking);
        bankingOption = findViewById(R.id.banking_Option);

        buyerName = findViewById(R.id.buyerName);
        buyerPhone = findViewById(R.id.buyerPhone);
        buyerAddress = findViewById(R.id.buyerAddress);
        sellerBankName = findViewById(R.id.seller_bankName);
        sellerBankNumber = findViewById(R.id.seller_bankNumber);
        sellerBankImage = findViewById(R.id.seller_bankImage);

        cancel = findViewById(R.id.cancel);
        checkOutBtn = findViewById(R.id.checkOutBtn);

        tongTienHang = findViewById(R.id.tong_tien_hang);
        tienVanChuyen = findViewById(R.id.tien_van_chuyen);
        tongHoaDon = findViewById(R.id.tong_hoa_don);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void addEvent() {
        bankingOption.setVisibility(View.GONE);

        paymentOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.cash) {
                bankingOption.setVisibility(View.GONE);
            } else if (checkedId == R.id.banking) {
                bankingOption.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(v -> finish());

        checkOutBtn.setOnClickListener(v -> createOrder());
    }

    // Hàm lấy thông tin sản phẩm từ Firestore dựa vào danh sách productID

    private void loadProductDetails(ArrayList<String> productIDs, ArrayList<Integer> quantities) {
        for (int i = 0; i < productIDs.size(); i++) {
            String productID = productIDs.get(i);
            int quantity = quantities.get(i);

            db.collection("products").document(productID)
                    .get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            String image = document.getString("productImage1");
                            String name = document.getString("productTitle");
                            int price = document.getLong("productPrice").intValue();

                            OrderThanhToan order = new OrderThanhToan(image, name, price, quantity);
                            orderList.add(order);

                            checkOutAdapter.notifyDataSetChanged();
                            calculateTotal();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Lỗi khi tải thông tin sản phẩm.", Toast.LENGTH_SHORT).show());
        }
    }

    // Hàm lấy thông tin người mua
    private void loadBuyerInfo() {
        String userID = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String phone = documentSnapshot.getString("phone");
                        String address = documentSnapshot.getString("address");

                        buyerName.setText(name);
                        buyerPhone.setText(phone);
                        buyerAddress.setText(address);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi khi tải thông tin người mua", Toast.LENGTH_SHORT).show());
    }

    // Hàm lấy thông tin ngân hàng của người bán dựa vào sellerID
    private void loadSellerInfo(String sellerID) {
        db.collection("users").document(sellerID)
                .get()
                .addOnSuccessListener(sellerSnapshot -> {
                    if (sellerSnapshot.exists()) {
                        String bankName = sellerSnapshot.getString("bankName");
                        String bankNumber = sellerSnapshot.getString("bankNumber");
                        String bankImageUrl = sellerSnapshot.getString("bankImage");

                        // Gán thông tin vào các view tương ứng
                        sellerBankName.setText(bankName);
                        sellerBankNumber.setText(bankNumber);

                        // Sử dụng Picasso để load ảnh từ URL
                        Picasso.get().load(bankImageUrl).into(sellerBankImage);
                    } else {
                        Toast.makeText(this, "Thông tin người bán không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi khi tải thông tin người bán", Toast.LENGTH_SHORT).show());
    }

    private void calculateTotal() {
        int totalProductPrice = 0;

        // Tính tổng tiền hàng
        for (OrderThanhToan order : orderList) {
            totalProductPrice += order.getPrice() * order.getQuantity();
        }

        // Hiển thị tổng tiền hàng
        tongTienHang.setText(String.format("%,d VNĐ", totalProductPrice));

        // Hiển thị tiền vận chuyển (mặc định 30,000 VNĐ)
        tienVanChuyen.setText(String.format("%,d VNĐ", TIEN_VAN_CHUYEN));

        // Tính và hiển thị tổng hóa đơn
        int totalBill = totalProductPrice + TIEN_VAN_CHUYEN;
        tongHoaDon.setText(String.format("%,d VNĐ", totalBill));
    }








    // Tạo đơn hàng trong Firestore
    private void createOrder() {
        String buyerID = mAuth.getCurrentUser().getUid();
        String sellerID = getIntent().getStringExtra("sellerID");
        int totalAmount = calculateTotalAmount();

        // Tạo mảng chứa các sản phẩm
        ArrayList<Map<String, Object>> products = new ArrayList<>();
        ArrayList<String> productIDs = getIntent().getStringArrayListExtra("selectedProductIDs");
        ArrayList<Integer> quantities = getIntent().getIntegerArrayListExtra("selectedQuantities");

        // Duyệt qua danh sách sản phẩm và tạo mảng sản phẩm cho Firestore
        for (int i = 0; i < productIDs.size(); i++) {
            Map<String, Object> product = new HashMap<>();
            product.put("productID", productIDs.get(i)); // Thêm productID vào
            product.put("quantity", quantities.get(i)); // Thêm số lượng vào
            products.add(product);
        }

        // Tạo đối tượng Order
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("buyerID", buyerID);
        orderData.put("sellerID", sellerID);
        orderData.put("totalAmount", totalAmount);
        orderData.put("status", "Chờ xác nhận"); // Trạng thái đơn hàng ban đầu là "Chờ xác nhận"
        orderData.put("createdAt", FieldValue.serverTimestamp());
        orderData.put("products", products);  // Danh sách sản phẩm

        // Lưu đơn hàng vào Firestore
        db.collection("orders")
                .add(orderData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(CheckOutActivity.this, "Đơn hàng đã được tạo!", Toast.LENGTH_SHORT).show();

                    // Sau khi tạo đơn hàng thành công, xóa giỏ hàng của người dùng
                    clearCart(buyerID);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CheckOutActivity.this, "Lỗi khi tạo đơn hàng!", Toast.LENGTH_SHORT).show();
                });
    }

    // Tính tổng số tiền (bao gồm sản phẩm và phí vận chuyển)
    private int calculateTotalAmount() {
        int totalProductPrice = 0;

        // Tính tổng tiền sản phẩm
        for (OrderThanhToan order : orderList) {
            totalProductPrice += order.getPrice() * order.getQuantity();
        }

        // Thêm tiền vận chuyển
        return totalProductPrice + TIEN_VAN_CHUYEN;
    }


    // Xóa giỏ hàng của người dùng sau khi đơn hàng được tạo
    private void clearCart(String buyerID) {
        db.collection("carts")
                .document(buyerID)
                .collection("cartItems")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        documentSnapshot.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(CheckOutActivity.this, "Giỏ hàng đã được làm sạch.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(CheckOutActivity.this, "Lỗi khi xóa sản phẩm trong giỏ hàng.", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CheckOutActivity.this, "Lỗi khi lấy giỏ hàng.", Toast.LENGTH_SHORT).show();
                });
    }
}
