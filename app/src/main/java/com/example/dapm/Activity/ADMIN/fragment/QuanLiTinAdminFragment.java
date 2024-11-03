package com.example.dapm.Activity.ADMIN.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dapm.Activity.ADMIN.Pager.ViewPageerQLTinAdapter;
import com.example.dapm.R;
import com.google.android.material.tabs.TabLayout;

public class QuanLiTinAdminFragment extends Fragment {

    private TabLayout mTabLayoutQuanLiTinDang;
    private ViewPager mViewPagerQuanLiTinDang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_li_tin_admin, container, false);
        setupViewPagerAndTabs(view);
        return view;
    }

    private void setupViewPagerAndTabs(View view) {
        mTabLayoutQuanLiTinDang = view.findViewById(R.id.tab_layout_qltindang);
        mViewPagerQuanLiTinDang = view.findViewById(R.id.view_pager_qltindang);

        // Set up ViewPager with the custom adapter
        ViewPageerQLTinAdapter viewPagerAdapter = new ViewPageerQLTinAdapter(
                getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );
        mViewPagerQuanLiTinDang.setAdapter(viewPagerAdapter);

        // Link TabLayout and ViewPager
        mTabLayoutQuanLiTinDang.setupWithViewPager(mViewPagerQuanLiTinDang);
    }
}
