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
import com.example.appholaagri.model.RequestModel.RequestListData;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.ViewHolder> {

    private List<RequestListData.RequestData> dayDataList;
    // Listener xử lý sự kiện click
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Integer requestId, Integer typeRequest, Integer groupRequestType); // Truyền ID của request khi item được click
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public RequestListAdapter(List<RequestListData.RequestData> dayDataList) {
        this.dayDataList = dayDataList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item_recycle_view, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }
    public void addData(List<RequestListData.RequestData> newData) {
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


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Lấy dữ liệu ngày
        RequestListData.RequestData dayData = dayDataList.get(position);

        // Hiển thị ngày
//        holder.dayTextView.setText(dayData.getDay());
        holder.txt_request_name.setText(dayData.getRequestName());
        holder.txt_employee_name.setText(dayData.getEmployee().getEmployeeName());
        holder.txt_createdDate.setText(dayData.getCreatedDate());
        holder.txt_status.setText(dayData.getStatus().getName());

        try {
            String colorCode = dayData.getStatus().getColor(); // Lấy mã màu (dạng #cccccc)
            int color = Color.parseColor(colorCode); // Chuyển mã màu thành int

            // Đặt màu chữ
            holder.txt_status.setTextColor(color);

            // Tạo màu nền nhạt hơn (thêm alpha)
            int backgroundColor = Color.argb(50, Color.red(color), Color.green(color), Color.blue(color)); // Alpha = 50 (~20% độ trong suốt)

            // Đặt màu nền
            holder.txt_status.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));

        } catch (IllegalArgumentException e) {
            // Xử lý trường hợp mã màu không hợp lệ
            Log.e("RequestAdapter", "Invalid color code: " + dayData.getStatus().getColor());
            holder.txt_status.setTextColor(Color.BLACK); // Đặt màu chữ mặc định (ví dụ: đen)
            holder.txt_status.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY)); // Đặt màu nền mặc định (ví dụ: xám nhạt)
        }

        Picasso.get()
                .load(dayData.getEmployee().getEmployeeAvatar())  // URL ảnh cần tải
                .placeholder(R.drawable.avatar)           // Ảnh mặc định khi chưa tải được
                .error(R.drawable.avatar)                 // Ảnh khi có lỗi tải
                .into(holder.img_avatar_empployee);
        // Gắn ID của request vào view
        // Gắn ID và type vào view
        // Sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(dayData.getRequestId(), dayData.getStatusIdx(), dayData.getGroupRequestType());
            }
        });

    }


    @Override
    public int getItemCount() {
        return dayDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_request_name, txt_employee_name, txt_createdDate, txt_status;
        public ImageView img_avatar_empployee;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            txt_request_name = itemView.findViewById(R.id.txt_request_name);
            txt_employee_name = itemView.findViewById(R.id.txt_employee_name);
            txt_createdDate = itemView.findViewById(R.id.txt_createdDate);
            txt_status = itemView.findViewById(R.id.txt_status);
            img_avatar_empployee = itemView.findViewById(R.id.img_avatar_empployee);
        }
    }
}