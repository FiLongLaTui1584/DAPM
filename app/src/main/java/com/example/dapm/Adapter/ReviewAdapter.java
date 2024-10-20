package com.example.dapm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dapm.R;
import com.example.dapm.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        holder.userAvatar.setImageResource(review.getUserAvatar());
        holder.userName.setText(review.getUserName());
        holder.userComment.setText(review.getUserComment());
        holder.timestamp.setText(review.getTimestamp());
        holder.productImage.setImageResource(review.getProductImage());
        holder.productName.setText(review.getProductName());
        holder.productPrice.setText(review.getProductPrice());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar, productImage;
        TextView userName, userComment, timestamp, productName, productPrice;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            userAvatar = itemView.findViewById(R.id.img_user_avatar);
            userName = itemView.findViewById(R.id.tv_user_name);
            userComment = itemView.findViewById(R.id.tv_user_comment);
            timestamp = itemView.findViewById(R.id.tv_timestamp);
            productImage = itemView.findViewById(R.id.img_product);
            productName = itemView.findViewById(R.id.tv_img_name);
            productPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}

