package com.example.appholaagri.adapter;

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

import java.util.List;

public class TimeKeepingManageInitAdapter extends RecyclerView.Adapter<TimeKeepingManageInitAdapter.ViewHolder> {

    private List<TimeKeepingManageData> timeKeepingDataList;

    public TimeKeepingManageInitAdapter(List<TimeKeepingManageData> dataList) {
        this.timeKeepingDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timekeeping_manage_item_recycle_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TimeKeepingManageData data = timeKeepingDataList.get(position);
        // Hiển thị các thông tin từ model
        holder.tvDate.setText(data.getAttendance().getDate());
        Picasso.get()
                .load(data.getEmployee().getAvatarUrl())  // URL ảnh cần tải
                .placeholder(R.drawable.avatar)           // Ảnh mặc định khi chưa tải được
                .error(R.drawable.avatar)                 // Ảnh khi có lỗi tải
                .into(holder.imgAvatar);
        holder.tvEmployeeCode.setText(data.getEmployee().getCode());
        holder.tvEmployeeName.setText(data.getEmployee().getName());
        holder.tvToSanXuat.setText(data.getDivision().getName());
        holder.checkoutTime.setText(data.getCheckoutTime() != null && !data.getCheckoutTime().isEmpty() ? data.getCheckoutTime() : "--:--");
        holder.checkinTime.setText(data.getCheckinTime() != null && !data.getCheckinTime().isEmpty() ? data.getCheckinTime() : "--:--");
        holder.shiftCode.setText(data.getShift().getCode());
        holder.period.setText("(" +data.getShift().getStartTime() + "-" + data.getShift().getEndTime() + ")");
        // Thiết lập sự kiện click cho mỗi item
        holder.itemView.setOnClickListener(v -> {
            // Khi click vào item, hiển thị mã nhân viên
            String employeeCode = data.getEmployee().getCode();
            // Hiển thị mã nhân viên trong một Toast (hoặc có thể làm gì đó khác)
            Toast.makeText(v.getContext(), "Mã nhân viên: " + employeeCode, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return timeKeepingDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvEmployeeCode, tvEmployeeName, tvToSanXuat, shiftCode, period, checkoutTime, checkinTime, reason_refused_text;
        public ImageView imgAvatar;
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
        }
    }
}
