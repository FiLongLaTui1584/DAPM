package com.example.dapm.Activity.ADMIN;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.dapm.Activity.ADMIN.fragment.CaNhanAdminFragment;
import com.example.dapm.Activity.ADMIN.fragment.GiaoDichAdminFragment;
import com.example.dapm.Activity.ADMIN.fragment.HomeFragment;
import com.example.dapm.Activity.ADMIN.fragment.NguoiDungAdminFragment;
import com.example.dapm.Activity.ADMIN.fragment.QuanLiTinAdminFragment;
import com.example.dapm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        addControls();
        addEvents();
    }
    void loadFragment(Fragment fmNew){
        FragmentTransaction fmTran=getSupportFragmentManager().beginTransaction();
        fmTran.replace(R.id.main_frame,fmNew);
        fmTran.addToBackStack(null);
        fmTran.commit();
    }
    private void addEvents() {
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fmNew;
                if(item.getItemId()==R.id.bottomTrangChu)
                {
                    fmNew=new HomeFragment();
                    loadFragment(fmNew);
                    return true;
                }
                if(item.getItemId()==R.id.bottomQuanLiTin)
                {
                    fmNew=new QuanLiTinAdminFragment();
                    loadFragment(fmNew);
                    return true;
                }
                if(item.getItemId()==R.id.bottomGiaoDich)
                {
                    fmNew=new GiaoDichAdminFragment();
                    loadFragment(fmNew);
                    return true;
                }
                if(item.getItemId()==R.id.bottomNguoiDung)
                {
                    fmNew=new NguoiDungAdminFragment();
                    loadFragment(fmNew);
                    return true;
                }
                if(item.getItemId()==R.id.bottomCaNhan)
                {
                    fmNew=new CaNhanAdminFragment();
                    loadFragment(fmNew);
                    return true;
                }
                return true;
            }
        });
    }

    private void addControls() {
        bottomNav=findViewById(R.id.bottomNavAdmin);
    }
}