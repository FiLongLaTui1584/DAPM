package com.example.dapm.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dapm.Activity.DanhSachChatActivity;
import com.example.dapm.Activity.TheoDoiThuNhapActivity;
import com.example.dapm.R;


public class AccountFragment extends Fragment {

    LinearLayout don_ban;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        addControl(view);
        addIntent();

        return view;
    }

    private void addControl(View view) {
        don_ban=view.findViewById(R.id.don_ban);
    }

    private void addIntent() {
        don_ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TheoDoiThuNhapActivity.class);
                startActivity(intent);
            }
        });
    }
}