package com.example.dapm.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dapm.Adapter.TinDangAdapter;
import com.example.dapm.Adapter.ViewPagerAdapter;
import com.example.dapm.R;
import com.example.dapm.model.TinDang;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TinDangFragment extends Fragment {
    RecyclerView recyclerViewTD;
    TinDangAdapter tinDangAdapter;
    ArrayList<TinDang> arr_TinDang;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tin_dang, container, false);
        addControls(view);
        loadData();

        return view;
    }

    private void loadData() {
        arr_TinDang.add(new TinDang(1, "New", "Warranty", "Vietnam", "Instructions", "Title 1", "Detailed description 1", "100", R.drawable.def));
        arr_TinDang.add(new TinDang(2, "Used", "No Warranty", "USA", "Instructions", "Title 2", "Detailed description 2", "200", R.drawable.def2));

        tinDangAdapter.notifyDataSetChanged();
    }

    private void addControls(View view) {
        recyclerViewTD = view.findViewById(R.id.recyclerTinDang);

        recyclerViewTD.setLayoutManager(new LinearLayoutManager(getActivity()));


        arr_TinDang = new ArrayList<>();


        tinDangAdapter = new TinDangAdapter(getActivity(), arr_TinDang);
        recyclerViewTD.setAdapter(tinDangAdapter);


        mTabLayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.view_pager);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }
}
