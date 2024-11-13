package com.example.dapm.Activity.ADMIN.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dapm.Activity.DangNhapActivity;
import com.example.dapm.R;
import com.google.firebase.auth.FirebaseAuth;


public class CaNhanAdminFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ca_nhan_admin, container, false);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Tìm nút đăng xuất
        Button logoutButton = view.findViewById(R.id.logoutButton);

        // Thêm sự kiện khi nhấn nút đăng xuất
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();  // Đăng xuất người dùng
            Intent intent = new Intent(getActivity(), DangNhapActivity.class);  // Mở màn hình đăng nhập
            startActivity(intent);
            getActivity().finish();  // Đóng hoạt động hiện tại
        });

        return view;
    }
}