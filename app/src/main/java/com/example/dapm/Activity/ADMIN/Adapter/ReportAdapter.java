package com.example.dapm.Activity.ADMIN.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm.Activity.ADMIN.model.Report;
import com.example.dapm.Activity.ReportDetailActivity;
import com.example.dapm.R;
import com.google.firebase.firestore.FirebaseFirestore;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private Context context;
    private List<Report> reportList;
    private FirebaseFirestore db;

    public ReportAdapter(Context context, List<Report> reportList) {
        this.context = context;
        this.reportList = reportList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bao_cao_user, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);

        // Gán lý do và ID của người tố cáo
        holder.reason.setText(report.getReason());
        holder.reporter.setText(report.getReporterUserID());

        // Lấy tên của người bị tố cáo từ Firestore
        db.collection("users").document(report.getReportedUserID())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String reportedUserName = documentSnapshot.getString("name");
                        holder.userName.setText(reportedUserName);
                    } else {
                        holder.userName.setText("Tên không tồn tại");
                    }
                })
                .addOnFailureListener(e -> {
                    holder.userName.setText("Lỗi khi lấy tên");
                });

        // Lấy tên của người tố cáo
        db.collection("users").document(report.getReporterUserID())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String reporterUserName = documentSnapshot.getString("name");
                        holder.reporter.setText(reporterUserName);
                    } else {
                        holder.reporter.setText("Tên không tồn tại");
                    }
                })
                .addOnFailureListener(e -> {
                    holder.reporter.setText("Lỗi khi lấy tên");
                });

        // Tải hình ảnh của người bị tố cáo
        Glide.with(context)
                .load(report.getAvatarUrl()) // Đường dẫn hình ảnh
                .placeholder(R.drawable.def2) // Hình ảnh placeholder nếu cần
                .into(holder.imageView);

        // Thêm sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetailActivity.class);
            intent.putExtra("reportedUserID", report.getReportedUserID());
            intent.putExtra("reporterUserID", report.getReporterUserID());
            intent.putExtra("reason", report.getReason());
            intent.putExtra("userAvatarUrl", report.getAvatarUrl()); // Truyền đường dẫn hình ảnh
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        TextView userName, reason, reporter;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rp_img);
            userName = itemView.findViewById(R.id.rp_username);
            reason = itemView.findViewById(R.id.rp_reason);
            reporter = itemView.findViewById(R.id.rp_reporter);
        }
    }
}
