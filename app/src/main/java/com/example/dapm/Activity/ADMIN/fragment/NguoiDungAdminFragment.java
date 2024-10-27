package com.example.dapm.Activity.ADMIN.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dapm.Activity.ADMIN.Pager.ViewPagerAdapter;
import com.example.dapm.R;
import com.google.android.material.tabs.TabLayout;

public class NguoiDungAdminFragment extends Fragment {
    private TabLayout mTabLayoutQuanLiNguoiDung;
    private ViewPager mViewPagerQuanLiNguoiDung;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nguoi_dung_admin, container, false);
        addControls(view);
        return view;
    }
    private void addControls(View view) {
        mViewPagerQuanLiNguoiDung=view.findViewById(R.id.view_pager_qlnguoidung);
        mTabLayoutQuanLiNguoiDung=view.findViewById(R.id.tab_layout_qlnguoidung);

        ViewPagerAdapter viewPagerAdapter =new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mViewPagerQuanLiNguoiDung.setAdapter(viewPagerAdapter);

        mTabLayoutQuanLiNguoiDung.setupWithViewPager(mViewPagerQuanLiNguoiDung);

    }
}