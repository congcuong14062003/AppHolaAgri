package com.example.appholaagri.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.ApprovalLogs;
import com.example.appholaagri.model.SalaryTableModel.SalaryTableData;
import com.example.appholaagri.view.SalaryTableDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActionRequestDetailAdapter extends RecyclerView.Adapter<ActionRequestDetailAdapter.ViewHolder> {

    private List<ApprovalLogs> approvalLogs;

    public ActionRequestDetailAdapter(List<ApprovalLogs> approvalLogs) {
        this.approvalLogs = approvalLogs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action_request_detail, parent, false);
        return new ViewHolder(view);
    }

    public void addData(List<ApprovalLogs> newData) {
        int startPosition = approvalLogs.size();
        approvalLogs.addAll(newData);
        notifyItemRangeInserted(startPosition, newData.size());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Lấy dữ liệu ngày
        ApprovalLogs approvalLog = approvalLogs.get(position);

        // Hiển thị ngày
        holder.tvDate.setText(approvalLog.getDate());
        holder.tvTime.setText(approvalLog.getTime());
        // Khi người dùng nhấn vào item
        Picasso.get()
                .load(approvalLog.getUrl())  // URL ảnh cần tải
                .placeholder(R.drawable.avatar)           // Ảnh mặc định khi chưa tải được
                .error(R.drawable.avatar)                 // Ảnh khi có lỗi tải
                .into(holder.ivAvatar);

        holder.tvName.setText(approvalLog.getName());
        holder.tvStatus.setText(approvalLog.getStatusName());
        if(approvalLog.getStatusName().equals("Đang chờ duyệt") || approvalLog.getStatusName().equals("Lượt duyệt kế tiếp") ) {
            holder.ivStatus.setImageResource(R.drawable.cho_duyet);
        } else if (approvalLog.getStatusName().equals("Từ chối đề xuất")) {
            holder.ivStatus.setImageResource(R.drawable.no_duyet);
        }

        // Kiểm tra nếu là item cuối cùng
        if (position == getItemCount() - 1) {
            holder.lineDownImageView.setVisibility(View.GONE); // Ẩn line_down
        } else {
            holder.lineDownImageView.setVisibility(View.VISIBLE); // Hiển thị line_down
        }
        if(approvalLog.getReason() != null) {
            holder.tvReason.setText("Lí do: " + approvalLog.getReason());
        } else {
            holder.tvReason.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return (approvalLogs != null) ? approvalLogs.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvTime, tvName, tvStatus, tvReason;
        public ImageView ivAvatar, ivStatus;
        public LinearLayout lineDownImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvReason = itemView.findViewById(R.id.tvReason);
            lineDownImageView = itemView.findViewById(R.id.lineDownImageView);
        }
    }
}