package com.example.dapm.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.dapm.Adapter.ProfilePagerAdapter;
import com.example.dapm.Fragment.FragmentProfile.DangBanFragment;
import com.example.dapm.Fragment.FragmentProfile.DanhGiaFragment;
import com.example.dapm.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

public class XemProfileUserKhacActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_profile_user_khac);

        ImageButton reportButton = findViewById(R.id.reportButton1);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportBottomSheet();
            }
        });

        // Khởi tạo TabLayout và ViewPager
        TabLayout tabLayout = findViewById(R.id.tab_layout_profile);
        ViewPager viewPager = findViewById(R.id.view_pager_profile);

        // Thiết lập adapter cho ViewPager
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DangBanFragment(), "Đang Bán");
        adapter.addFragment(new DanhGiaFragment(), "Đánh Giá");

        // Thiết lập adapter cho ViewPager
        viewPager.setAdapter(adapter);

        // Liên kết TabLayout với ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showReportBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(XemProfileUserKhacActivity.this);

        View bottomSheetView = getLayoutInflater().inflate(R.layout.bao_cao_user, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetView.findViewById(R.id.cancelButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
}
