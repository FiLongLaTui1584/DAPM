package com.example.dapm.Fragment.FragmentProfile;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class DanhGiaFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danh_gia, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_danh_gia);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reviewList = new ArrayList<>();
        reviewList.add(new Review(R.drawable.sample_image, "Mèo của Giáp", "Thằng này scam tôi"));
        reviewList.add(new Review(R.drawable.sample_image, "Mèo của Giáp", "Thằng này scam tôi"));
        reviewList.add(new Review(R.drawable.sample_image, "Mèo của Giáp", "Thằng này scam tôi"));
        reviewList.add(new Review(R.drawable.sample_image, "Mèo của Giáp", "Thằng này scam tôi"));


        reviewAdapter = new ReviewAdapter(getContext(), reviewList);
        recyclerView.setAdapter(reviewAdapter);

        return view;
    }
}
