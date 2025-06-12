package com.example.appholaagri.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.Comments;

import java.util.ArrayList;
import java.util.List;

public class FileAttachmentAdapter extends RecyclerView.Adapter<FileAttachmentAdapter.FileViewHolder> {
    private List<Comments.FileAttachment> files = new ArrayList<>();
    private boolean hasApproveStatus = false;
    // Callback interface để thông báo thay đổi status
    public interface OnFileStatusChangedListener {
        void onStatusChanged(Comments.FileAttachment file, int filePosition, int commentPosition);
    }

    private OnFileStatusChangedListener listener;
    private int commentPosition;

    public void setOnFileStatusChangedListener(OnFileStatusChangedListener listener, int commentPosition) {
        this.listener = listener;
        this.commentPosition = commentPosition;
    }

    public void setFiles(List<Comments.FileAttachment> files) {
        this.files.clear();
        if (files != null) {
            this.files.addAll(files);
        }
        Log.d("FileAttachmentAdapter", "Setting files, new size: " + this.files.size());
        notifyDataSetChanged();
    }
    // Phương thức để đặt thông tin có trạng thái "Duyệt" không
    public void setHasApproveStatus(boolean hasApproveStatus) {
        this.hasApproveStatus = hasApproveStatus;
        notifyDataSetChanged(); // Cập nhật lại toàn bộ adapter để áp dụng thay đổi
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

        // Debug
        Log.d("FileAttachmentAdapter", "Binding file at position " + position + ": " + file.getName());

        // Cập nhật icon dựa trên status
        updateIcon(holder, file.getStatus());

        // Xử lý click để toggle status
        // Xử lý click để toggle status, chỉ cho phép khi có trạng thái "Duyệt" trong listStatus
        holder.itemView.setOnClickListener(v -> {
            if (hasApproveStatus) { // Chỉ cho phép toggle nếu có trạng thái "Duyệt"
                if (file.getStatus() == 1) {
                    file.setStatus(2);
                    updateIcon(holder, 2);
                    Log.d("FileAttachmentAdapter", "Changed status to checked at position " + position);
                } else if (file.getStatus() == 2) {
                    file.setStatus(1);
                    updateIcon(holder, 1);
                    Log.d("FileAttachmentAdapter", "Changed status to uncheck at position " + position);
                }
                if (listener != null) {
                    listener.onStatusChanged(file, position, commentPosition);
                }
                notifyItemChanged(position);
            } else {
                Log.d("FileAttachmentAdapter", "Toggle disabled, no approve status in listStatus");
            }
        });

        // Xử lý download file
        holder.btnDownload.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            String fileUrl = file.getPath(); // Đường dẫn file (có thể là URL)
            String fileName = file.getName(); // Tên file

            if (fileUrl != null && !fileUrl.isEmpty()) {
                downloadFile(context, fileUrl, fileName);
            } else {
                Log.e("FileAttachmentAdapter", "File URL is null or empty for file: " + fileName);
            }
        });
    }

    private void updateIcon(FileViewHolder holder, int status) {
        if (status == 2) {
            holder.btnCheckFile.setImageResource(R.drawable.checked_radio);
        } else if (status == 1) {
            holder.btnCheckFile.setImageResource(R.drawable.bg_circle);
        }
    }
    // Phương thức download file
    private void downloadFile(Context context, String fileUrl, String fileName) {
        try {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(fileUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri)
                    .setTitle(fileName)
                    .setDescription("Đang tải file: " + fileName)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);

            // Thực hiện download
            long downloadId = downloadManager.enqueue(request);
            Log.d("FileAttachmentAdapter", "Download started with ID: " + downloadId + " for file: " + fileName);

            // (Tùy chọn) Hiển thị thông báo khi hoàn tất (có thể theo dõi trạng thái download nếu cần)
            Toast.makeText(context, "Đang tải file: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Lỗi khi tải file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("FileAttachmentAdapter", "Download failed: " + e.getMessage());
        }
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