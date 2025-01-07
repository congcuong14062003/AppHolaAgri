package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;

import java.util.List;

public class TimekeepingAdapter extends RecyclerView.Adapter<TimekeepingAdapter.ViewHolder> {

    private List<TimekeepingStatisticsData.DayData> dayDataList;

    public TimekeepingAdapter(List<TimekeepingStatisticsData.DayData> dayDataList) {
        this.dayDataList = dayDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timekeeping_statistic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Lấy dữ liệu ngày
        TimekeepingStatisticsData.DayData dayData = dayDataList.get(position);

        // Hiển thị ngày
        holder.dayTextView.setText(dayData.getDay());

        // Lấy danh sách các ca làm việc trong ngày
        if (dayData.getShiftRes() != null && !dayData.getShiftRes().isEmpty()) {
            // Lấy ca làm việc đầu tiên (nếu có)
            TimekeepingStatisticsData.Shift shift = dayData.getShiftRes().get(0);

            // Hiển thị mã ca và thời gian
            holder.shiftCode.setText(shift.getShiftCode());
            holder.period.setText("(" + shift.getPeriod() + ")");

            // Hiển thị giờ vào (nếu có)
            if (shift.getCheckinTime() != null) {
                holder.checkinTime.setText(shift.getCheckinTime());
            } else {
                holder.checkinTime.setText("--:--"); // Không có dữ liệu
            }

            // Hiển thị giờ ra (nếu có)
            if (shift.getCheckoutTime() != null) {
                holder.checkoutTime.setText(shift.getCheckoutTime());
            } else {
                holder.checkoutTime.setText("--:--"); // Không có dữ liệu
            }
        } else {
            // Không có ca làm việc trong ngày
            holder.shiftCode.setText("Không có ca");
            holder.period.setText("");
            holder.checkinTime.setText("--:--");
            holder.checkoutTime.setText("--:--");
        }
    }



    @Override
    public int getItemCount() {
        return dayDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public TextView shiftCode;
        public TextView period;
        public TextView checkinTime;
        public TextView checkoutTime;

        public ViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            shiftCode = itemView.findViewById(R.id.shiftCode);
            period = itemView.findViewById(R.id.period);
            checkinTime = itemView.findViewById(R.id.checkinTime);
            checkoutTime = itemView.findViewById(R.id.checkoutTime);
        }
    }
}