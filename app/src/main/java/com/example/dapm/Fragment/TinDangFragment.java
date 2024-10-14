package com.example.dapm.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dapm.Adapter.TinDangAdapter;
import com.example.dapm.R;
import com.example.dapm.model.TinDang;

import java.util.ArrayList;

public class TinDangFragment extends Fragment {
    RecyclerView recyclerViewTD;
    TinDangAdapter tinDangAdapter;
    ArrayList<TinDang> arr_TinDang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment's view
        View view = inflater.inflate(R.layout.fragment_tin_dang, container, false);

        // Initialize controls after view is inflated
        addControls(view);
        loadData();

        return view;
    }

    private void loadData() {
        // Load your data here (e.g., adding dummy data for testing)
        arr_TinDang.add(new TinDang(1, "New", "Warranty", "Vietnam", "Instructions", "Title 1", "Detailed description 1", "100$", R.drawable.def));
        arr_TinDang.add(new TinDang(2, "Used", "No Warranty", "USA", "Instructions", "Title 2", "Detailed description 2", "200$", R.drawable.def2));

        // Notify the adapter about data changes
        tinDangAdapter.notifyDataSetChanged();
    }

    private void addControls(View view) {
        recyclerViewTD = view.findViewById(R.id.recyclerTinDang);

        recyclerViewTD.setLayoutManager(new LinearLayoutManager(getActivity()));


        arr_TinDang = new ArrayList<>();


        tinDangAdapter = new TinDangAdapter(getActivity(), arr_TinDang);
        recyclerViewTD.setAdapter(tinDangAdapter);
    }
}
