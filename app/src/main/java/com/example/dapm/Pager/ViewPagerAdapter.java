package com.example.dapm.Pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.dapm.Fragment.FragmentQLTD.BiTuChoiFragment;
import com.example.dapm.Fragment.FragmentQLTD.ChoDuyetFragment;
import com.example.dapm.Fragment.FragmentQLTD.DangHienThiFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DangHienThiFragment();
            case 1:
                return new ChoDuyetFragment();
            case 2:
                return new BiTuChoiFragment();
            default:
                return new DangHienThiFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title= "Đang hiển thị";
                break;
            case 1:
                title= "Chờ duyệt";
                break;
            case 2:
                title= "Bị từ chối";
                break;
        }
        return title;
    }
}
