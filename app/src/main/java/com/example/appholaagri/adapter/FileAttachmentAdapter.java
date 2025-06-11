package com.example.appholaagri.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.Comments;

import java.util.ArrayList;
import java.util.List;

public class FileAttachmentAdapter extends RecyclerView.Adapter<FileAttachmentAdapter.FileViewHolder> {
    private List<Comments.FileAttachment> files = new ArrayList<>();

    public void setFiles(List<Comments.FileAttachment> files) {
        this.files.clear(); // Xóa danh sách cũ để tránh trùng lặp
        if (files != null) {
            this.files.addAll(files); // Thêm toàn bộ danh sách mới
        }
        Log.d("FileAttachmentAdapter", "Setting files, new size: " + this.files.size());
        notifyDataSetChanged(); // Đảm bảo cập nhật giao diện
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discuss_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        Comments.FileAttachment file = files.get(position);
        holder.fileName.setText(file.getName());

        // Debug: Kiểm tra file tại vị trí hiện tại
        Log.d("FileAttachmentAdapter", "Binding file at position " + position + ": " + file.getName());

        // Kiểm tra status và đặt icon check/uncheck
        if (file.getStatus() == 2) {
            holder.btnCheckFile.setImageResource(R.drawable.checked_radio); // Icon checked
        } else if (file.getStatus() == 1) {
            holder.btnCheckFile.setImageResource(R.drawable.no_check); // Icon uncheck
        }

        // Xử lý click để xem file
        holder.itemView.setOnClickListener(v -> {
            // Cần context từ activity để gọi showFileWebView
            // Ví dụ: ((Activity) holder.itemView.getContext()).showFileWebView(Uri.parse(file.getPath()), file.getName());
        });

        // Xử lý download file
        holder.btnDownload.setOnClickListener(v -> {
            // Cần context từ activity để gọi downloadFile
            // Ví dụ: ((Activity) holder.itemView.getContext()).downloadFile(file.getPath(), file.getName());
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView btnCheckFile, btnDownload;
        TextView fileName;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCheckFile = itemView.findViewById(R.id.btn_check_file);
            btnDownload = itemView.findViewById(R.id.btn_download);
            fileName = itemView.findViewById(R.id.file_name);
        }
    }
}