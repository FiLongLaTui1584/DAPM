package com.example.dapm.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide; // Thêm thư viện Glide
import com.example.dapm.Activity.ChinhSuaTTCaNhanActivity;
import com.example.dapm.Activity.DangNhapActivity;
import com.example.dapm.Activity.QuanLyYeuThichActivity;
import com.example.dapm.Activity.TheoDoiThuNhapActivity;
import com.example.dapm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccountFragment extends Fragment {
    private LinearLayout FavBut;
    private LinearLayout don_ban;
    private LinearLayout layoutLoggedOut;
    private LinearLayout layoutLoggedIn;
    private LinearLayout dangXuat;
    private TextView accountUserName;
    private ImageView changeAccount, editAccount, accountAvt;
    public static final int PICK_IMAGE_REQUEST = 1; // Mã yêu cầu cho chọn ảnh

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        addControl(view);
        addEvent();
        updateUI();

        return view;
    }

    private void addEvent() {
        FavBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuanLyYeuThichActivity.class);
                startActivity(intent);
            }
        });

        don_ban.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TheoDoiThuNhapActivity.class);
            startActivity(intent);
        });

        layoutLoggedOut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DangNhapActivity.class);
            startActivity(intent);
        });

        changeAccount.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Xác nhận đổi tài khoản")
                    .setMessage("Bạn có chắc chắn muốn đổi tài khoản?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut(); // Đăng xuất tài khoản hiện tại
                        Intent intent = new Intent(getActivity(), DangNhapActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Hủy", null) // Không làm gì nếu người dùng chọn Hủy
                    .show();
        });


        editAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChinhSuaTTCaNhanActivity.class);
            startActivity(intent);
        });

        dangXuat.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Xác nhận đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut(); // Đăng xuất
                        layoutLoggedOut.setVisibility(View.VISIBLE); // Hiện layout_logged_out
                        layoutLoggedIn.setVisibility(View.GONE); // Ẩn layout_logged_in
                    })
                    .setNegativeButton("Hủy", null) // Không làm gì nếu người dùng chọn Hủy
                    .show();
        });


        accountAvt.setOnClickListener(v -> openImageChooser()); // Mở trình chọn ảnh
    }

    private void addControl(View view) {
        don_ban = view.findViewById(R.id.don_ban);
        layoutLoggedOut = view.findViewById(R.id.layout_logged_out);
        layoutLoggedIn = view.findViewById(R.id.layout_logged_in);
        accountUserName = view.findViewById(R.id.account_userName);
        changeAccount = view.findViewById(R.id.change_account);
        editAccount = view.findViewById(R.id.edit_account);
        dangXuat = view.findViewById(R.id.dang_xuat);
        accountAvt = view.findViewById(R.id.account_avt);
        FavBut= view.findViewById(R.id.FavBut);
    }

    private void updateUI() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            layoutLoggedOut.setVisibility(View.GONE);
            layoutLoggedIn.setVisibility(View.VISIBLE);

            // Lấy tên người dùng từ Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name");
                            accountUserName.setText(userName);

                            // Cập nhật avatar nếu có
                            String avatarUrl = documentSnapshot.getString("avatar");
                            if (avatarUrl != null) {
                                // Sử dụng Glide để tải ảnh vào ImageView
                                Glide.with(this).load(avatarUrl).into(accountAvt);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.e("AccountFragment", "Error fetching user data", e));
        } else {
            layoutLoggedOut.setVisibility(View.VISIBLE);
            layoutLoggedIn.setVisibility(View.GONE);
        }
    }

    //Mở thư viện ảnh của thiết bị
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadAvatar(imageUri);
        }
    }

    private void uploadAvatar(Uri imageUri) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("user_avatar/" + currentUser.getUid() + ".jpg");

            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String avatarUrl = uri.toString();
                            updateUserAvatar(avatarUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("AccountFragment", "Lỗi khi cập nhật avatar", e);
                    });
        }
    }

    private void updateUserAvatar(String avatarUrl) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(currentUser.getUid())
                    .update("avatar", avatarUrl)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("AccountFragment", "Cập nhật ảnh đại diện thành công!");
                        // Cập nhật avatar mới và ẩn ảnh mẫu
                        Glide.with(this)
                                .load(avatarUrl)
                                .into(accountAvt);
                        accountAvt.setImageDrawable(null); // Ẩn ảnh mẫu
                    })
                    .addOnFailureListener(e -> {
                        Log.e("AccountFragment", "Lỗi khi cập nhật avatar", e);
                    });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
