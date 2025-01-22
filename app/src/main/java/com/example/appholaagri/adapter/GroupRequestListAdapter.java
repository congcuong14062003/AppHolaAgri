package com.example.appholaagri.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestGroupModel.RequestGroupResponse;
import com.example.appholaagri.model.RequestModel.RequestListData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupRequestListAdapter extends RecyclerView.Adapter<GroupRequestListAdapter.ViewHolder> {

    private List<RequestGroupResponse.RequestGroup> dayDataList;
    // Listener xử lý sự kiện click
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Integer GroupRequestId); // Truyền ID của request khi item được click
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public GroupRequestListAdapter(List<RequestGroupResponse.RequestGroup> dayDataList) {
        this.dayDataList = dayDataList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_request_recycleview, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }
    public void addData(List<RequestGroupResponse.RequestGroup> newData) {
        if (newData != null && !newData.isEmpty()) {
            int startPosition = dayDataList.size();
            dayDataList.addAll(newData); // Thêm dữ liệu mới
            notifyItemRangeInserted(startPosition, newData.size());
        }
    }
    public void clearData() {
        if (dayDataList != null) {
            dayDataList.clear();  // Xóa hết dữ liệu trong danh sách
            notifyDataSetChanged(); // Cập nhật giao diện RecyclerView
        }
    }
    public void updateData(List<RequestGroupResponse.RequestGroup> newData) {
        this.dayDataList.clear();
        this.dayDataList.addAll(newData);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RequestGroupResponse.RequestGroup dayData = dayDataList.get(position);
        holder.txt_request_group_name.setText(dayData.getName());
        Picasso.get()
                .load(dayData.getUrl())  // URL ảnh cần tải
                .placeholder(R.drawable.avatar)           // Ảnh mặc định khi chưa tải được
                .error(R.drawable.avatar)                 // Ảnh khi có lỗi tải
                .into(holder.avt_request_group);
        // Gắn ID của request vào view
        // Gắn ID và type vào view
        // Sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(dayData.getId());
            }
        });

    }


    @Override
    public int getItemCount() {
        return dayDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_request_group_name;
        public ImageView avt_request_group;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            txt_request_group_name = itemView.findViewById(R.id.txt_request_group_name);
            avt_request_group = itemView.findViewById(R.id.avt_request_group);
        }
    }
}