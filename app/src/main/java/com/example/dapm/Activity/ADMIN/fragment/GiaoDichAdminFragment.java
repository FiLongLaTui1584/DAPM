package com.example.dapm.Activity.ADMIN.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dapm.Activity.ADMIN.Pager.ViewPagerAdapter;
import com.example.dapm.Activity.ADMIN.Pager.ViewPagerQLGDAdapter;
import com.example.dapm.R;
import com.google.android.material.tabs.TabLayout;

public class GiaoDichAdminFragment extends Fragment {

    private TabLayout mTabLayoutQuanLiGiaoDich;
    private ViewPager mViewPagerQuanLiGiaoDich;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giao_dich_admin, container, false);
        addControls(view);
        return view;
    }

    private void addControls(View view) {

        mViewPagerQuanLiGiaoDich=view.findViewById(R.id.view_pager_qlgiaodich);
        mTabLayoutQuanLiGiaoDich=view.findViewById(R.id.tab_layout_qlgiaodich);

        ViewPagerQLGDAdapter viewPagerQLGDAdapter =new ViewPagerQLGDAdapter(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mViewPagerQuanLiGiaoDich.setAdapter(viewPagerQLGDAdapter);

        mTabLayoutQuanLiGiaoDich.setupWithViewPager(mViewPagerQuanLiGiaoDich);
    }
}