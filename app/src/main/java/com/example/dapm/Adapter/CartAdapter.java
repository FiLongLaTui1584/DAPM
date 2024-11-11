package com.example.dapm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.R;
import com.example.dapm.model.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartItemList;
    private OnQuantityChangedListener onQuantityChangedListener;
    private OnCheckboxClickedListener onCheckboxClickedListener;

    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }

    public interface OnCheckboxClickedListener {
        void onCheckboxClicked();
    }

    public CartAdapter(Context context, List<CartItem> cartItemList,
                       OnQuantityChangedListener onQuantityChangedListener,
                       OnCheckboxClickedListener onCheckboxClickedListener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.onQuantityChangedListener = onQuantityChangedListener;
        this.onCheckboxClickedListener = onCheckboxClickedListener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.cartProductName.setText(cartItem.getCartProductName());
        holder.cartProductPrice.setText(String.format("%,d VNĐ", cartItem.getCartProductPrice()));
        holder.cartQuantity.setText(String.valueOf(cartItem.getCartQuantity()));

        Picasso.get().load(cartItem.getCartProductImage()).into(holder.cartProductImage);

        holder.increaseQuantity.setOnClickListener(v -> {
            int currentQuantity = cartItem.getCartQuantity();
            cartItem.setCartQuantity(currentQuantity + 1);
            notifyItemChanged(position);
            onQuantityChangedListener.onQuantityChanged();
            updateCartItemQuantity(cartItem.getProductID(), cartItem.getCartQuantity());
        });

        holder.decreaseQuantity.setOnClickListener(v -> {
            int currentQuantity = cartItem.getCartQuantity();
            if (currentQuantity > 1) {
                cartItem.setCartQuantity(currentQuantity - 1);
                notifyItemChanged(position);
                onQuantityChangedListener.onQuantityChanged();
                updateCartItemQuantity(cartItem.getProductID(), cartItem.getCartQuantity());
            }
        });

        holder.cartCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartItem.setSelected(isChecked);
            onCheckboxClickedListener.onCheckboxClicked();
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    private void updateCartItemQuantity(String productID, int newQuantity) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cartItemRef = db.collection("carts").document(userID)
                .collection("cartItems").document(productID);

        cartItemRef.update("cartQuantity", newQuantity)
                .addOnSuccessListener(aVoid -> {
                    // Thành công
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Cập nhật số lượng thất bại!", Toast.LENGTH_SHORT).show();
                });
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView cartProductName, cartProductPrice, cartQuantity;
        ImageView cartProductImage;
        Button increaseQuantity, decreaseQuantity;
        CheckBox cartCheckbox;

        public CartViewHolder(View itemView) {
            super(itemView);
            cartProductName = itemView.findViewById(R.id.cartProductName);
            cartProductPrice = itemView.findViewById(R.id.cartProductPrice);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            cartProductImage = itemView.findViewById(R.id.cartProductImage);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            cartCheckbox = itemView.findViewById(R.id.cartCheckbox);
        }
    }
}
