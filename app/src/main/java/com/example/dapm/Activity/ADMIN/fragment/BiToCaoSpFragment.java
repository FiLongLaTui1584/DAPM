package com.example.dapm.Activity.ADMIN.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.model.ReportSP;
import com.example.dapm.R;
import com.example.dapm.Adapter.ReportAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BiToCaoSpFragment extends Fragment implements ReportAdapter.OnBlockProductListener{

    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private List<ReportSP> reportList;

    public BiToCaoSpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bi_to_cao_sp, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewReports);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(reportList, this);
        recyclerView.setAdapter(reportAdapter);

        loadReports();
        return view;
    }

    private void loadReports() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reports_sp")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ReportSP report = document.toObject(ReportSP.class);
                            reportList.add(report);
                        }
                        // Gọi loadProductInfos sau khi đã tải xong reportList
                        reportAdapter.notifyDataSetChanged();
                        reportAdapter.loadProductInfos(); // Gọi phương thức này để tải thông tin sản phẩm
                    } else {
                        Toast.makeText(getContext(), "Lỗi tải báo cáo.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onBlockProduct(ReportSP report) {
        // Hiển thị AlertDialog để xác nhận việc khóa sản phẩm
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận khóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn khóa sản phẩm này?")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Nếu người dùng chọn OK, tiến hành khóa sản phẩm
                    lockProduct(report.getProductID());
                })
                .setNegativeButton("Hủy", null) // Không làm gì nếu người dùng chọn Hủy
                .show();
    }

    private void lockProduct(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").document(productId)
                .update("isApproved", "locked")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Sản phẩm đã bị khóa", Toast.LENGTH_SHORT).show();
                        // Có thể cập nhật lại danh sách báo cáo nếu cần
                    } else {
                        Toast.makeText(getContext(), "Không thể khóa sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
