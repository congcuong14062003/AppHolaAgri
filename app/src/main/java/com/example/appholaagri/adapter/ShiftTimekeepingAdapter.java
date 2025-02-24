package com.example.appholaagri.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;

import java.util.ArrayList;
import java.util.List;

public class ShiftTimekeepingAdapter extends RecyclerView.Adapter<ShiftTimekeepingAdapter.ViewHolder> {

    private List<TimekeepingStatisticsData.Shift> shiftList;

    public ShiftTimekeepingAdapter(List<TimekeepingStatisticsData.Shift> shiftList) {
        this.shiftList = (shiftList != null) ? shiftList : new ArrayList<>(); // Tránh null
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_shift_timekeeping, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimekeepingStatisticsData.Shift shift = shiftList.get(position);

        holder.shiftCode.setText(shift.getShiftCode());
        holder.period.setText("(" + shift.getPeriod() + ")");

        // Kiểm tra checkinTime
        if (shift.getCheckinTime() != null) {
            holder.checkinTime.setText(shift.getCheckinTime());
            holder.checkinTime.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.textcolor)); // Màu mặc định
        } else {
            holder.checkinTime.setText("--:--");
            holder.checkinTime.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red_error)); // Màu #EB5A5A
        }

        // Kiểm tra checkoutTime
        if (shift.getCheckoutTime() != null) {
            holder.checkoutTime.setText(shift.getCheckoutTime());
            holder.checkoutTime.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.textcolor)); // Màu mặc định
        } else {
            holder.checkoutTime.setText("--:--");
            holder.checkoutTime.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red_error)); // Màu #EB5A5A
        }

        // Hiển thị lý do từ chối nếu có
        if (shift.getReason() != null) {
            holder.reason_refused_layout.setVisibility(View.VISIBLE);
            holder.reason_refused.setText(shift.getReason());
        } else {
            holder.reason_refused_layout.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shiftCode, period, checkinTime, checkoutTime, reason_refused;
        LinearLayout reason_refused_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shiftCode = itemView.findViewById(R.id.shiftCode);
            period = itemView.findViewById(R.id.period);
            checkinTime = itemView.findViewById(R.id.checkinTime);
            checkoutTime = itemView.findViewById(R.id.checkoutTime);
            reason_refused_layout = itemView.findViewById(R.id.reason_refused_layout);
            reason_refused = itemView.findViewById(R.id.reason_refused);
        }
    }
}
