package com.example.dapm.Activity.ADMIN.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dapm.Activity.ADMIN.Adapter.ReportAdapter;
import com.example.dapm.Activity.ADMIN.model.Report;
import com.example.dapm.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BiBaoCaoFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private List<Report> reportList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bi_bao_cao, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewReports);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Report list and Adapter
        reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(getContext(), reportList);
        recyclerView.setAdapter(reportAdapter);

        // Load Reports from Firebase
        loadReportsFromFirebase();

        return view;
    }

    private void loadReportsFromFirebase() {
        CollectionReference reportsRef = db.collection("reports");
        reportsRef.whereEqualTo("isClosed", false).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reportList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Report report = document.toObject(Report.class);
                    reportList.add(report);

                    fetchUserAvatar(report.getReportedUserID(), report);
                }
                reportAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Lỗi khi tải báo cáo", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void fetchUserAvatar(String reportedUserID, Report report) {
        db.collection("users").document(reportedUserID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String avatarUrl = task.getResult().getString("avatar");
                report.setAvatarUrl(avatarUrl);
                reportAdapter.notifyDataSetChanged(); // Notify adapter to refresh view
            } else {

            }
        });

    }
}
