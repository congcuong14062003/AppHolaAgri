package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.Comments;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.ViewHolder> {
    private List<Comments> comments = new ArrayList<>();

    public void setComments(List<Comments> comments) {
        this.comments = comments != null ? comments : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discussion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comments comment = comments.get(position);
        Comments.User user = comment.getUser();
        List<Comments.FileAttachment> fileAttachments = comment.getFileAttachments();

        // Thiết lập avatar
        String avatarUrl = user.getUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Picasso.get().load(avatarUrl).placeholder(R.drawable.avatar).into(holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.avatar);
        }

        // Thiết lập tên người dùng và ngày giờ
        holder.tvUserName.setText(user.getName());
        holder.tvCreatedDate.setText(comment.getCreatedDate());

        // Thiết lập nội dung comment
        holder.tvComment.setText(comment.getComment());

        // Cấu hình RecyclerView cho file attachments
        holder.recyclerViewFileDiscus.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        FileAttachmentAdapter fileAdapter = new FileAttachmentAdapter();
        holder.recyclerViewFileDiscus.setAdapter(fileAdapter);
        fileAdapter.setFiles(fileAttachments);



    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivStatusIcon;
        TextView tvUserName, tvComment, tvCreatedDate;
        RecyclerView recyclerViewFileDiscus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            ivStatusIcon = itemView.findViewById(R.id.btn_check_file); // Sử dụng btn_check_file làm trạng thái
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvCreatedDate = itemView.findViewById(R.id.tvCreatedDate);
            recyclerViewFileDiscus = itemView.findViewById(R.id.recyclerViewFileDiscus);
        }
    }

    // Adapter con cho file attachments
    private class FileAttachmentAdapter extends RecyclerView.Adapter<FileAttachmentAdapter.FileViewHolder> {
        private List<Comments.FileAttachment> files = new ArrayList<>();

        public void setFiles(List<Comments.FileAttachment> files) {
            this.files = files != null ? files : new ArrayList<>();
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
            holder.fileName.setText(file.getName());

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
}