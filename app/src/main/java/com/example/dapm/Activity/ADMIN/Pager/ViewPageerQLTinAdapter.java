package com.example.dapm.Activity.ADMIN.Pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.dapm.Activity.ADMIN.fragment.BiBaoCaoFragment;
import com.example.dapm.Activity.ADMIN.fragment.BiKhoaFragment;
import com.example.dapm.Activity.ADMIN.fragment.BiTuChoiFragment;
import com.example.dapm.Activity.ADMIN.fragment.DaDuyetFragment;
import com.example.dapm.Activity.ADMIN.fragment.DangChoFragment;
import com.example.dapm.Activity.ADMIN.fragment.TatCaNguoiDungFragment;

public class ViewPageerQLTinAdapter extends FragmentStatePagerAdapter {
    public ViewPageerQLTinAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DangChoFragment();
            case 1:
                return new DaDuyetFragment();
            case 2:
                return new BiTuChoiFragment();
            default:
                return new DangChoFragment();
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
                title= "Đang chờ";
                break;
            case 1:
                title= "Đã duyệt";
                break;
            case 2:
                title= "Bị từ chối";
                break;
        }
        return title;
    }
}
