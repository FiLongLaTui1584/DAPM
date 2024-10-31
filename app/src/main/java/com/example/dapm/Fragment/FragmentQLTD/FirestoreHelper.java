package com.example.dapm.Fragment.FragmentQLTD;

import com.example.dapm.Adapter.TinDangAdapter;
import com.example.dapm.model.TinDang;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FirestoreHelper {
    private FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public void getTinDang(ArrayList<TinDang> arr_TinDang, TinDangAdapter adapter) {
        db.collection("products") // Tên bộ sưu tập trong Firestore
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TinDang tinDang = document.toObject(TinDang.class);
                            tinDang.setProductID(document.getId()); // Gán ID từ Firestore vào productID
                            arr_TinDang.add(tinDang);
                        }
                        adapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                    } else {
                        // Xử lý lỗi
                    }
                });
    }
}
