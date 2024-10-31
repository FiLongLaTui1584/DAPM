package com.example.dapm.Fragment.FragmentQLTD;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dapm.Adapter.TinDangAdapter;
import com.example.dapm.R;
import com.example.dapm.databinding.ActivityMainBinding;
import com.example.dapm.model.TinDang;

import java.util.ArrayList;

public class DangHienThiFragment extends Fragment {

    RecyclerView recyclerViewTD;
    TinDangAdapter tinDangAdapter;
    ArrayList<TinDang> arr_TinDang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_hien_thi, container, false);
        addControls(view);
        loadData();
        return view;
    }

    private void addControls(View view) {
        recyclerViewTD = view.findViewById(R.id.recyclerTinDang);

        recyclerViewTD.setLayoutManager(new LinearLayoutManager(getActivity()));


        arr_TinDang = new ArrayList<>();


        tinDangAdapter = new TinDangAdapter(getActivity(), arr_TinDang);
        recyclerViewTD.setAdapter(tinDangAdapter);
    }


    private void loadData() {

        tinDangAdapter.notifyDataSetChanged();
    }

}