package com.example.dapm.Fragment.FragmentProfile;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.dapm.Adapter.ReviewAdapter;
import com.example.dapm.R;
import com.example.dapm.model.Review;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DanhGiaFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private String sellerID;

    public static DanhGiaFragment newInstance(String sellerID) {
        DanhGiaFragment fragment = new DanhGiaFragment();
        Bundle args = new Bundle();
        args.putString("sellerID", sellerID);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danh_gia, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_danh_gia);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(getContext(), reviewList);
        recyclerView.setAdapter(reviewAdapter);

        // Lấy sellerID từ các tham số
        if (getArguments() != null) {
            sellerID = getArguments().getString("sellerID");
        }

        if (sellerID != null) {
            loadReviews();
        } else {
            Log.e("DanhGiaFragment", "sellerID is null");
        }

        return view;
    }

    private void loadReviews() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reviewsRef = db.collection("reviews");

        reviewsRef.whereEqualTo("sellerID", sellerID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewList.clear();
                QuerySnapshot querySnapshot = task.getResult();

                for (QueryDocumentSnapshot document : querySnapshot) {
                    Review review = document.toObject(Review.class);
                    reviewList.add(review);
                }
                reviewAdapter.notifyDataSetChanged();
            } else {
                Log.e("DanhGiaFragment", "Error loading reviews", task.getException());
            }
        });
    }
}
