package com.example.dapm.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.dapm.Activity.DangNhapActivity;
import com.example.dapm.Pager.ViewPagerAdapter;
import com.example.dapm.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class TinDangFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView sellerName;
    private ImageView sellerAvatar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tin_dang, container, false);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize seller name and avatar views
        sellerName = view.findViewById(R.id.sellerName);
        sellerAvatar = view.findViewById(R.id.sellerAvatar);

        // Check if user is logged in
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getActivity(), DangNhapActivity.class);
            startActivity(intent);
            getActivity().finish();
            return view;
        }else {
            updateUI(currentUser);
        }
        addControls(view);
        return view;
    }

    private void updateUI(FirebaseUser currentUser) {
        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userName = documentSnapshot.getString("name");
                        sellerName.setText(userName);

                        String avatarUrl = documentSnapshot.getString("avatar");
                        if (avatarUrl != null) {
                            Glide.with(this).load(avatarUrl).into(sellerAvatar);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("TinDangFragment", "Error fetching user data", e));
    }

    private void addControls(View view) {
        mTabLayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }
}
