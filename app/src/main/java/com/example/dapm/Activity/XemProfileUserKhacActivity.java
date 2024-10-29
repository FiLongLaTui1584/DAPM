package com.example.dapm.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.dapm.Adapter.ProfilePagerAdapter;
import com.example.dapm.Fragment.FragmentProfile.DangBanFragment;
import com.example.dapm.Fragment.FragmentProfile.DanhGiaFragment;
import com.example.dapm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class XemProfileUserKhacActivity extends AppCompatActivity {
    private ImageButton reportButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ImageView userAvatar, cancel;
    private TextView userName;
    private TextView userDescription;

    private String sellerID;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_profile_user_khac);

        db = FirebaseFirestore.getInstance();
        sellerID = getIntent().getStringExtra("sellerID");

        addControl();
        addEvent();
        setupViewPager_TabLayout(sellerID);
        loadUserProfile();
    }

    private void setupViewPager_TabLayout(String sellerID) {
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getSupportFragmentManager());

        DangBanFragment dangBanFragment = new DangBanFragment();
        dangBanFragment.setSellerID(sellerID);

        adapter.addFragment(dangBanFragment, "Đang Bán");
        adapter.addFragment(new DanhGiaFragment(), "Đánh Giá");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void addEvent() {
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportBottomSheet();
            }
        });
        cancel.setOnClickListener(v -> finish());
    }

    private void addControl() {
        reportButton = findViewById(R.id.reportButton1);
        tabLayout = findViewById(R.id.tab_layout_profile);
        viewPager = findViewById(R.id.view_pager_profile);
        userAvatar = findViewById(R.id.userAvatar);
        userName = findViewById(R.id.userName);
        userDescription = findViewById(R.id.userDescription);
        cancel = findViewById(R.id.cancel);
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

    private void loadUserProfile() {
        db.collection("users").document(sellerID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String avatarUrl = document.getString("avatar");
                                String description = document.getString("description");

                                userName.setText(name);
                                userDescription.setText(description);
                                Picasso.get().load(avatarUrl).into(userAvatar);
                            }
                        }
                    }
                });
    }
}
