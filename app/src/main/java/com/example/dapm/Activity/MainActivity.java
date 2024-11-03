package com.example.dapm.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Adapter.UploadImageAdapter;
import com.example.dapm.Fragment.AccountFragment;
import com.example.dapm.Fragment.CartFragment;
import com.example.dapm.Fragment.HomeFragment;
import com.example.dapm.Fragment.TinDangFragment;
import com.example.dapm.R;
import com.example.dapm.databinding.ActivityMainBinding;
import com.example.dapm.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private final List<Uri> selectedImages = new ArrayList<>();
    private UploadImageAdapter imageAdapter;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private int imageUploadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();



        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.tindang) {
                replaceFragment(new TinDangFragment());
            } else if (itemId == R.id.giohang) {
                replaceFragment(new CartFragment());
            } else if (itemId == R.id.taikhoan) {
                replaceFragment(new AccountFragment());
            }
            return true;
        });

        binding.fab.setOnClickListener(v -> showBottomDialog());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {
        // Kiểm tra người dùng đã đăng nhập
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để đăng sản phẩm.", Toast.LENGTH_SHORT).show();
            return; // Thoát nếu chưa đăng nhập
        }

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheetlayout);

        RecyclerView recyclerViewImages = bottomSheetDialog.findViewById(R.id.recyclerViewImages);
        Button addImagesButton = bottomSheetDialog.findViewById(R.id.addImagesButton);
        Button saveButton = bottomSheetDialog.findViewById(R.id.saveButton);

        ImageView cancel = bottomSheetDialog.findViewById(R.id.cancelButton);
        EditText titleInput = bottomSheetDialog.findViewById(R.id.uploadTiltle);
        EditText priceInput = bottomSheetDialog.findViewById(R.id.uploadPrice);
        EditText descriptionInput = bottomSheetDialog.findViewById(R.id.uploadDescription);
        EditText locationInput = bottomSheetDialog.findViewById(R.id.uploadLocation);
        EditText tinhTrangInput = bottomSheetDialog.findViewById(R.id.uploadTinhTrang);
        EditText baoHanhInput = bottomSheetDialog.findViewById(R.id.uploadBaoHanh);
        EditText xuatXuInput = bottomSheetDialog.findViewById(R.id.uploadXuatXu);
        EditText hdsdInput = bottomSheetDialog.findViewById(R.id.uploadHDSD);

        imageAdapter = new UploadImageAdapter(selectedImages);
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setAdapter(imageAdapter);

        addImagesButton.setOnClickListener(v -> openGallery());

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String price = priceInput.getText().toString();
            String description = descriptionInput.getText().toString();
            String location = locationInput.getText().toString();
            String tinhTrang = tinhTrangInput.getText().toString();
            String baoHanh = baoHanhInput.getText().toString();
            String xuatXu = xuatXuInput.getText().toString();
            String hdsd = hdsdInput.getText().toString();

            if (title.isEmpty() || price.isEmpty() || description.isEmpty() || location.isEmpty() || selectedImages.size() < 3) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đủ thông tin và chọn 3 ảnh", Toast.LENGTH_SHORT).show();
            } else {
                uploadImagesToStorage(title, Integer.parseInt(price), description, location, tinhTrang, baoHanh, xuatXu, hdsd);
                Toast.makeText(MainActivity.this, "Sản phẩm đã được tải lên thành công", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Chờ quản trị viên duyệt sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    if (result.getData().getClipData() != null) {
                        int count = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < count && i < 3; i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            selectedImages.add(imageUri);
                        }
                    } else if (result.getData().getData() != null) {
                        Uri imageUri = result.getData().getData();
                        selectedImages.add(imageUri);
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            }
    );

    private void uploadImagesToStorage(String title, int price, String description, String location, String tinhTrang, String baoHanh, String xuatXu, String hdsd) {
        List<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < selectedImages.size(); i++) {
            Uri imageUri = selectedImages.get(i);
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("product_image/" + UUID.randomUUID() + ".jpg");

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrls.add(uri.toString());
                        imageUploadCount++;

                        if (imageUploadCount == selectedImages.size()) {
                            saveProductToFirestore(title, price, description, location, tinhTrang, baoHanh, xuatXu, hdsd, imageUrls);
                        }
                    }))
                    .addOnFailureListener(e -> Log.e("MainActivity", "Error uploading image", e));
        }
    }

    private void saveProductToFirestore(String title, int price, String description, String location, String tinhTrang, String baoHanh, String xuatXu, String hdsd, List<String> imageUrls) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String sellerID = currentUser.getUid();

        // Tạo một tài liệu mới và lấy ID tự động sinh ra
        DocumentReference productRef = firestore.collection("products").document();

        Product product = new Product(
                productRef.getId(), imageUrls.get(0), imageUrls.get(1), imageUrls.get(2),
                title, price, location, description, tinhTrang, baoHanh, xuatXu, hdsd, sellerID, "pending"
        );

        productRef.set(product)
                .addOnSuccessListener(aVoid -> {
                    // Hiển thị thông báo khi sản phẩm được đăng thành công
                    Toast.makeText(MainActivity.this, "Đăng sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    // Reset imageUploadCount và selectedImages sau khi đăng thành công
                    imageUploadCount = 0;
                    selectedImages.clear();
                    imageAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("MainActivity", "Lỗi khi lưu sản phẩm", e);
                    Toast.makeText(MainActivity.this, "Đăng sản phẩm thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                });
    }


}
