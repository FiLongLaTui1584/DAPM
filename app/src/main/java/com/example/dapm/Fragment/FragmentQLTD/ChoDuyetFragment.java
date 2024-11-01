    package com.example.dapm.Fragment.FragmentQLTD;

    import android.app.Activity;
    import android.content.Intent;
    import android.net.Uri;
    import android.os.Bundle;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Toast;

    import com.example.dapm.Activity.DangNhapActivity;
    import com.example.dapm.R;
    import com.example.dapm.model.TinDang;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.firestore.CollectionReference;
    import com.google.firebase.firestore.DocumentReference;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;
    import com.google.android.material.bottomsheet.BottomSheetDialog;
    import com.example.dapm.Adapter.TinDangAdapter;
    import com.example.dapm.Fragment.FragmentQLTD.FirestoreHelper;

    import java.util.ArrayList;


    public class ChoDuyetFragment extends Fragment {





        public ImageView etProductImage1, etProductImage2, etProductImage3;
        private Uri selectedImageUri1, selectedImageUri2, selectedImageUri3;
        private FirebaseFirestore db;


        private BottomSheetDialog bottomSheetDialog;
        private RecyclerView recyclerView;
        private TinDangAdapter adapter;
        private ArrayList<TinDang> arr_TinDang;

        // Firestore Collection Reference
        private CollectionReference tinDangCollectionRef;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_cho_duyet, container, false);

            db = FirebaseFirestore.getInstance();
            tinDangCollectionRef = db.collection("products");

            // Initialize ImageView variables

            // Button to trigger BottomSheetDialog
            Button addTinDangButton = view.findViewById(R.id.addTinDangButton);
            addTinDangButton.setOnClickListener(v -> showBottomSheetDialog(null));

            recyclerView = view.findViewById(R.id.recyclerTinDang);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            arr_TinDang = new ArrayList<>();
            adapter = new TinDangAdapter(getActivity(), arr_TinDang);
            recyclerView.setAdapter(adapter);

            // Set click listeners for each ImageView


            // Fetch data from Firestore
            FirestoreHelper firestoreHelper = new FirestoreHelper();
            firestoreHelper.getTinDang(arr_TinDang, adapter);

            return view;
        }

        private void showBottomSheetDialog(TinDang existingTinDang) {

            bottomSheetDialog = new BottomSheetDialog(requireContext());
            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheetlayout, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            // Initialize Fields

            EditText etProductTitle = bottomSheetView.findViewById(R.id.uploadTopic);
            EditText etProductPrice = bottomSheetView.findViewById(R.id.uploadLang);
            EditText etProductLocation = bottomSheetView.findViewById(R.id.uploadLocation);
            EditText etProductDescription = bottomSheetView.findViewById(R.id.uploadDesc);
            EditText etProductTinhTrang = bottomSheetView.findViewById(R.id.uploadTinhTrang);
            EditText etProductBaoHanh = bottomSheetView.findViewById(R.id.uploadBaoHanh);
            EditText etProductXuatXu = bottomSheetView.findViewById(R.id.uploadXuatXu);
            EditText etProductHDSD = bottomSheetView.findViewById(R.id.uploadHDSD);

            // Cancel Button
            ImageView cancelButton = bottomSheetView.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(v -> bottomSheetDialog.dismiss());

            // Nếu có tin đăng đã tồn tại, hiển thị các giá trị hiện tại trong các trường đầu vào
            if (existingTinDang != null) {
                etProductTitle.setText(existingTinDang.getProductTitle());
                etProductPrice.setText(String.valueOf(existingTinDang.getProductPrice()));
                etProductLocation.setText(existingTinDang.getProductLocation());
                etProductDescription.setText(existingTinDang.getProductDescription());
                etProductTinhTrang.setText(existingTinDang.getProductTinhTrang());
                etProductBaoHanh.setText(existingTinDang.getProductBaoHanh());
                etProductXuatXu.setText(existingTinDang.getProductXuatXu());
                etProductHDSD.setText(existingTinDang.getProductHDSD());
            }

            // Save Button
            Button saveButton = bottomSheetView.findViewById(R.id.saveButton);
            saveButton.setOnClickListener(v -> {
                String productImage1 = String.valueOf(etProductImage1.getTextDirection());
                String productImage2 = String.valueOf(etProductImage2.getTextDirection());
                String productImage3 = String.valueOf(etProductImage3.getTextDirection());
                String productTitle = etProductTitle.getText().toString();

                int productPrice;
                try {
                    productPrice = Integer.parseInt(etProductPrice.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Vui lòng nhập giá hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String productLocation = etProductLocation.getText().toString();
                String productDescription = etProductDescription.getText().toString();
                String productTinhTrang = etProductTinhTrang.getText().toString();
                String productBaoHanh = etProductBaoHanh.getText().toString();
                String productXuatXu = etProductXuatXu.getText().toString();
                String productHDSD = etProductHDSD.getText().toString();

                if (existingTinDang == null) {
                    addTinDang(productImage1, productImage2, productImage3, productTitle, productPrice,
                            productLocation, productDescription, productTinhTrang, productBaoHanh,
                            productXuatXu, productHDSD);
                } else {
                    updateTinDang(existingTinDang.getProductID(), productImage1, productImage2, productImage3,
                            productTitle, productPrice, productLocation, productDescription, productTinhTrang,
                            productBaoHanh, productXuatXu, productHDSD);
                }
            });

            bottomSheetDialog.show();
        }
        public void onSelectImage1(View view) {
            openImageSelector(1);
        }

        public void onSelectImage2(View view) {
            openImageSelector(2);
        }

        public void onSelectImage3(View view) {
            openImageSelector(3);
        }

        private void openImageSelector(int imageNumber) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*"); // Chỉ định loại tệp là hình ảnh
            startActivityForResult(intent, imageNumber); // Khởi động trình chọn ảnh
        }

        private void addTinDang(String productImage1, String productImage2, String productImage3,
                                String productTitle, int productPrice, String productLocation,
                                String productDescription, String productTinhTrang, String productBaoHanh,
                                String productXuatXu, String productHDSD) {

            // Lấy ID của người dùng hiện tại từ FirebaseAuth
            String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                    ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                    : null;

            if (userId == null) {
                Toast.makeText(getContext(), "Vui lòng đăng nhập để tạo tin đăng mới", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), DangNhapActivity.class);
                startActivity(intent);
                return;
            }

            // Tạo đối tượng TinDang với sellerID từ userId
            TinDang newTinDang = new TinDang(productImage1, productImage2, productImage3, productTitle,
                    productPrice, productLocation, productDescription, productTinhTrang,
                    productBaoHanh, productXuatXu, productHDSD, userId);
            newTinDang.setTinhTrangDuyet("Chưa duyệt"); // Đặt giá trị mặc định cho trạng thái duyệt

            // Tiến hành thêm tin đăng vào Firestore
            tinDangCollectionRef.add(newTinDang)
                    .addOnSuccessListener(documentReference -> {
                        newTinDang.setProductID(documentReference.getId()); // Gán ID từ Firestore vào productID
                        arr_TinDang.add(newTinDang);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Tin đăng mới đã được thêm", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Thêm thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        }




        private void updateTinDang(String id, String productImage1, String productImage2, String productImage3,
                                   String productTitle, int productPrice, String productLocation,
                                   String productDescription, String productTinhTrang, String productBaoHanh,
                                   String productXuatXu, String productHDSD) {
            // Lấy tài liệu hiện có để lấy sellerID
            tinDangCollectionRef.document(id).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Lấy sellerID từ tài liệu
                            String sellerID = documentSnapshot.getString("sellerID");

                            // Cập nhật các trường mà không thay đổi sellerID
                            tinDangCollectionRef.document(id)
                                    .update("productImage1", productImage1,
                                            "productImage2", productImage2,
                                            "productImage3", productImage3,
                                            "productTitle", productTitle,
                                            "productPrice", productPrice,
                                            "productLocation", productLocation,
                                            "productDescription", productDescription,
                                            "productTinhTrang", productTinhTrang,
                                            "productBaoHanh", productBaoHanh,
                                            "productXuatXu", productXuatXu,
                                            "productHDSD", productHDSD,
                                            "sellerID", sellerID) // Giữ nguyên sellerID
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getContext(), "Tin đăng đã được cập nhật", Toast.LENGTH_SHORT).show();
                                        bottomSheetDialog.dismiss();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(getContext(), "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                    );
                        } else {
                            Toast.makeText(getContext(), "Tin đăng không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Lấy thông tin thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        }


       /* private void deleteTinDang(String id) {
            tinDangCollectionRef.document(id)
                    .delete()
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Tin đăng đã bị xóa", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }*/
    }