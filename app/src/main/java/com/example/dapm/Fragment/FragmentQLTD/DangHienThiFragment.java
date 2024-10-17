package com.example.dapm.Fragment.FragmentQLTD;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dapm.R;
import com.example.dapm.databinding.ActivityMainBinding;

public class DangHienThiFragment extends Fragment {

    ActivityMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dang_hien_thi, container, false);
    }
}