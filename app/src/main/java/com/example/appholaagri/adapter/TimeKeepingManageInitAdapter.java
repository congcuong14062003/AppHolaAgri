package com.example.appholaagri.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.TimeKeepingManageModel.TimeKeepingManageData;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TimeKeepingManageInitAdapter extends RecyclerView.Adapter<TimeKeepingManageInitAdapter.ViewHolder> {

    private List<TimeKeepingManageData> timeKeepingDataList;
    private Set<Integer> selectedPositions = new HashSet<>(); // Lưu các vị trí được chọn

    public TimeKeepingManageInitAdapter(List<TimeKeepingManageData> dataList) {
        this.timeKeepingDataList = dataList;
    }
    public interface OnSelectionChangedListener {
        void onSelectionChanged(boolean hasSelection);
    }
    private OnSelectionChangedListener selectionChangedListener;
    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionChangedListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timekeeping_manage_item_recycle_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TimeKeepingManageData data = timeKeepingDataList.get(position);

        // Hiển thị thông tin từ model
        holder.tvDate.setText(data.getAttendance().getDate());
        Picasso.get()
                .load(data.getEmployee().getAvatarUrl())
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(holder.imgAvatar);
        holder.tvEmployeeCode.setText(data.getEmployee().getCode());
        holder.tvEmployeeName.setText(data.getEmployee().getName());
        holder.tvToSanXuat.setText(data.getDivision().getName());
        holder.checkoutTime.setText(data.getCheckoutTime() != null && !data.getCheckoutTime().isEmpty() ? data.getCheckoutTime() : "--:--");
        holder.checkinTime.setText(data.getCheckinTime() != null && !data.getCheckinTime().isEmpty() ? data.getCheckinTime() : "--:--");
        holder.shiftCode.setText(data.getShift().getCode());
        holder.period.setText("(" + data.getShift().getStartTime() + "-" + data.getShift().getEndTime() + ")");

        // Hiển thị hoặc ẩn biểu tượng "checked" dựa trên trạng thái
        if (selectedPositions.contains(position)) {
            holder.imgCheckedIcon.setVisibility(View.VISIBLE);
        } else {
            holder.imgCheckedIcon.setVisibility(View.GONE);
        }

        // Sự kiện click cho mỗi item
        holder.itemView.setOnClickListener(v -> {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position); // Bỏ chọn
            } else {
                selectedPositions.add(position); // Chọn
            }
            notifyItemChanged(position); // Chỉ làm mới item hiện tại
            // Gọi callback để cập nhật giao diện
            if (selectionChangedListener != null) {
                selectionChangedListener.onSelectionChanged(!selectedPositions.isEmpty());
            }
        });
    }
    public boolean areAllItemsChecked() {
        return selectedPositions.size() == timeKeepingDataList.size();
    }

    // Chọn tất cả item
    public void selectAll() {
        for (int i = 0; i < timeKeepingDataList.size(); i++) {
            selectedPositions.add(i);
        }
        notifyDataSetChanged(); // Làm mới toàn bộ danh sách
        if (selectionChangedListener != null) {
            selectionChangedListener.onSelectionChanged(true);
        }
    }

    // Bỏ chọn tất cả item
    public void clearSelection() {
        selectedPositions.clear(); // Xóa danh sách các item đã chọn
        notifyDataSetChanged(); // Làm mới toàn bộ danh sách
        if (selectionChangedListener != null) {
            selectionChangedListener.onSelectionChanged(false);
        }
    }

    @Override
    public int getItemCount() {
        return timeKeepingDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvEmployeeCode, tvEmployeeName, tvToSanXuat, shiftCode, period, checkoutTime, checkinTime, reason_refused_text;
        public ImageView imgAvatar, imgCheckedIcon;
        public LinearLayout reason_refused_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgAvatar = itemView.findViewById(R.id.imgAvatarConfirm);
            tvEmployeeCode = itemView.findViewById(R.id.tvEmployeeCode);
            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
            tvToSanXuat = itemView.findViewById(R.id.tvToSanXuatConfirm);
            period = itemView.findViewById(R.id.periodConfirm);
            checkinTime = itemView.findViewById(R.id.checkinTimeConfirm);
            checkoutTime = itemView.findViewById(R.id.checkoutTimeConfirm);
            shiftCode = itemView.findViewById(R.id.shiftCodeConfirm);
            reason_refused_text = itemView.findViewById(R.id.reason_refused_text);
            reason_refused_layout = itemView.findViewById(R.id.reason_refused_layout);

            // Biểu tượng "checked"
            imgCheckedIcon = itemView.findViewById(R.id.imgCheckedIcon);
        }
    }
}
