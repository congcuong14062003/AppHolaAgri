package com.example.appholaagri.adapter;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.Comments;
import com.example.appholaagri.view.FileWebViewActivity;
import com.squareup.picasso.Picasso;

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

    public void setHasApproveStatus(boolean hasApproveStatus) {
        this.hasApproveStatus = hasApproveStatus;
        notifyDataSetChanged();
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
        // Thêm gạch chân cho tên file
        SpannableString spannableFileName = new SpannableString(file.getName());
        spannableFileName.setSpan(new UnderlineSpan(), 0, file.getName().length(), 0);
        holder.fileName.setText(spannableFileName);

        // Debug
        Log.d("FileAttachmentAdapter", "Binding file at position " + position + ": " + file.getName());

        // Cập nhật icon dựa trên status
        updateIcon(holder, file.getStatus());

        // Xử lý click để toggle status, chỉ cho phép khi có trạng thái "Duyệt"
        holder.itemView.setOnClickListener(v -> {
            if (hasApproveStatus) {
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

        // Xử lý click vào tên file để mở file
        holder.fileName.setOnClickListener(v -> {
            String fileName = file.getName();
            String fileUrl = file.getPath();
            if (fileUrl != null && !fileUrl.isEmpty()) {
                Uri fileUri = Uri.parse(fileUrl);
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                Context context = holder.itemView.getContext();

                if ("pdf".equals(fileExtension) || "doc".equals(fileExtension) || "docx".equals(fileExtension) ||
                        "xls".equals(fileExtension) || "xlsx".equals(fileExtension)) {
                    // Mở file trong web view
                    Intent intent = new Intent(context, FileWebViewActivity.class);
                    intent.putExtra("fileUrl", fileUrl);
                    intent.putExtra("fileName", fileName);
                    context.startActivity(intent);
                } else {
                    // Hiển thị dialog chi tiết cho file ảnh
                    showImageDetailDialog(context, fileUri, fileName);
                }
            } else {
                Log.e("FileAttachmentAdapter", "File URL is null or empty for file: " + fileName);
                Toast.makeText(holder.itemView.getContext(), "Không thể mở file: URL trống", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý download file
        holder.btnDownload.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            String fileUrl = file.getPath();
            String fileName = file.getName();

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

            long downloadId = downloadManager.enqueue(request);
            Log.d("FileAttachmentAdapter", "Download started with ID: " + downloadId + " for file: " + fileName);

            Toast.makeText(context, "Đang tải file: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Lỗi khi tải file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("FileAttachmentAdapter", "Download failed: " + e.getMessage());
        }
    }

    // Phương thức hiển thị dialog chi tiết cho file ảnh
    private static void showImageDetailDialog(Context context, Uri fileUri, String fileName) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_image_detail);

        ImageView imageView = dialog.findViewById(R.id.image_detail);
        TextView textFileName = dialog.findViewById(R.id.text_file_name);
        ImageView btnClose = dialog.findViewById(R.id.btn_close_dialog);

        // Hiển thị ảnh
        if (fileUri.toString().startsWith("http")) {
            Picasso.get()
                    .load(fileUri.toString())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(imageView);
        } else {
            imageView.setImageURI(fileUri);
        }

        // Hiển thị tên file
        textFileName.setText(fileName);

        // Đóng dialog khi chạm vào ảnh
//        imageView.setOnClickListener(v -> dialog.dismiss());

        // Đóng dialog khi nhấn nút x_mark
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Ngăn đóng dialog khi chạm ngoài
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
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