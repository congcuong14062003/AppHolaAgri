package com.example.appholaagri.adapter;

import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.ViewHolder> {
    private List<Comments> comments = new ArrayList<>();
    private boolean hasApproveStatus = false; // Biến kiểm tra xem có trạng thái "Duyệt" không
    // Callback interface để thông báo thay đổi status lên activity/fragment
    public interface OnFileStatusChangedListener {
        void onStatusChanged(Comments.FileAttachment file, int filePosition, int commentPosition);
    }

    private OnFileStatusChangedListener listener;

    public void setOnFileStatusChangedListener(OnFileStatusChangedListener listener) {
        this.listener = listener;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments != null ? comments : new ArrayList<>();
        notifyDataSetChanged();
    }
    // Phương thức để đặt thông tin có trạng thái "Duyệt" không
    public void setHasApproveStatus(boolean hasApproveStatus) {
        this.hasApproveStatus = hasApproveStatus;
        notifyDataSetChanged(); // Cập nhật lại toàn bộ adapter để áp dụng thay đổi
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

        // Debug
        if (fileAttachments != null) {
            Log.d("DiscussionAdapter", "Comment at position " + position + " has " + fileAttachments.size() + " files");
        } else {
            Log.d("DiscussionAdapter", "Comment at position " + position + " has no file attachments");
        }

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
        if (holder.fileAdapter == null) {
            holder.recyclerViewFileDiscus.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
            holder.fileAdapter = new FileAttachmentAdapter();
            holder.recyclerViewFileDiscus.setAdapter(holder.fileAdapter);
            // Cài đặt listener với vị trí comment
            holder.fileAdapter.setOnFileStatusChangedListener((file, filePosition, commentPos) -> {
                if (listener != null) {
                    listener.onStatusChanged(file, filePosition, commentPos);
                }
            }, position);
        }
        holder.fileAdapter.setHasApproveStatus(hasApproveStatus);
        holder.fileAdapter.setFiles(fileAttachments);
        Log.d("DiscussionAdapter", "Setting files for position " + position + ", size: " + (fileAttachments != null ? fileAttachments.size() : 0));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivStatusIcon;
        TextView tvUserName, tvComment, tvCreatedDate;
        RecyclerView recyclerViewFileDiscus;
        FileAttachmentAdapter fileAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            ivStatusIcon = itemView.findViewById(R.id.btn_check_file);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvCreatedDate = itemView.findViewById(R.id.tvCreatedDate);
            recyclerViewFileDiscus = itemView.findViewById(R.id.recyclerViewFileDiscus);
        }
    }
}