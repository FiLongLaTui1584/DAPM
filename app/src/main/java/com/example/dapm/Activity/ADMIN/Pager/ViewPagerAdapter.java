package com.example.dapm.Activity.ADMIN.Pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.dapm.Activity.ADMIN.fragment.BiBaoCaoFragment;
import com.example.dapm.Activity.ADMIN.fragment.BiKhoaFragment;
import com.example.dapm.Activity.ADMIN.fragment.TatCaNguoiDungFragment;
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
                return new BiBaoCaoFragment();
            case 1:
                return new BiKhoaFragment();
            default:
                return new BiBaoCaoFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title= "Bị báo cáo";
                break;
            case 1:
                title= "Bị khoá";
                break;
        }
        return title;
    }
}
