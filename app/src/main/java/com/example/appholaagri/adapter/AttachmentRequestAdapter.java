package com.example.appholaagri.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AttachmentRequestAdapter extends RecyclerView.Adapter<AttachmentRequestAdapter.ViewHolder> {
    private final Context context;
    private final List<RequestDetailData.FileAttachment> attachments;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(RequestDetailData.FileAttachment attachment);
    }

    public AttachmentRequestAdapter(Context context, List<RequestDetailData.FileAttachment> attachments, OnItemClickListener listener) {
        this.context = context;
        this.attachments = attachments;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attachment_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestDetailData.FileAttachment attachment = attachments.get(position);

        // Load ảnh nếu là file hình ảnh
        if (attachment.getPath() != null && isImageFile(attachment.getPath())) {
            Picasso.get()
                    .load(attachment.getPath())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(holder.imageAttachment);
        } else {
            // Hiển thị icon mặc định cho file không phải ảnh
            holder.imageAttachment.setImageResource(R.drawable.avatar);
        }

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(attachment));
    }

    @Override
    public int getItemCount() {
        return attachments != null ? attachments.size() : 0;
    }

    private boolean isImageFile(String path) {
        String lowerPath = path.toLowerCase();
        return lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg") || lowerPath.endsWith(".png") || lowerPath.endsWith(".gif");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAttachment;
        TextView textAttachmentName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAttachment = itemView.findViewById(R.id.image_attachment);
        }
    }
}