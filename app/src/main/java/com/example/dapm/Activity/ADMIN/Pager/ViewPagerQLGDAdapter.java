package com.example.dapm.Activity.ADMIN.Pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.dapm.Activity.ADMIN.fragment.BiBaoCaoFragment;
import com.example.dapm.Activity.ADMIN.fragment.BiKhoaFragment;
import com.example.dapm.Activity.ADMIN.fragment.TatCaNguoiDungFragment;
import com.example.dapm.Activity.ADMIN.fragment.ThongKeGiaoDichFragment;
import com.example.dapm.Activity.ADMIN.fragment.YeuCauHoanTraFragment;

public class ViewPagerQLGDAdapter extends FragmentStatePagerAdapter {
    public ViewPagerQLGDAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ThongKeGiaoDichFragment();
            case 1:
                return new YeuCauHoanTraFragment();
            default:
                return new ThongKeGiaoDichFragment();
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
                title= "Thống kê giao dịch";
                break;
            case 1:
                title= "Yêu cầu hoàn trả";
                break;
        }
        return title;
    }
}
