package com.example.dapm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dapm.R;
import com.example.dapm.model.Review;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviewList;
    private FirebaseFirestore db;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
        this.db = FirebaseFirestore.getInstance();
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

        holder.userName.setText("Loading...");
        holder.userAvatar.setImageResource(R.drawable.sample_image);
        holder.userComment.setText(review.getReviewContent());
        holder.ratingBar.setRating(review.getRate());

        // Lấy thông tin người đánh giá từ Firestore
        db.collection("users").document(review.getReviewerID()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userName = documentSnapshot.getString("name");
                        String userAvatarUrl = documentSnapshot.getString("avatar");

                        holder.userName.setText(userName);
                        if (userAvatarUrl != null) {
                            Picasso.get().load(userAvatarUrl).into(holder.userAvatar);
                        }
                    }
                })
                .addOnFailureListener(e -> {});
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userName, userComment;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            userAvatar = itemView.findViewById(R.id.reviewerAvatar);
            userName = itemView.findViewById(R.id.reviewerName);
            userComment = itemView.findViewById(R.id.reviewerContent);
            ratingBar = itemView.findViewById(R.id.reviewerRating);
        }
    }
}
