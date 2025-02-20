package com.example.appholaagri.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestModel.RequestListData;
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
    public void addData(List<TimekeepingStatisticsData.DayData> newData) {
        if (newData != null && !newData.isEmpty()) {
            int startPosition = dayDataList.size();
            dayDataList.addAll(newData);
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
                SpannableString checkInText = new SpannableString("--:--");
                checkInText.setSpan(new ForegroundColorSpan(Color.parseColor("#ea5b5d")), 0, checkInText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.checkinTime.setText(checkInText);
            }

            // Hiển thị giờ ra (nếu có)
            if (shift.getCheckoutTime() != null) {
                holder.checkoutTime.setText(shift.getCheckoutTime());
            } else {
                SpannableString checkInText = new SpannableString("--:--");
                checkInText.setSpan(new ForegroundColorSpan(Color.parseColor("#ea5b5d")), 0, checkInText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.checkoutTime.setText(checkInText);
            }
        } else {
            // Không có ca làm việc trong ngày
            holder.shiftCode.setText("Không có ca");
            holder.period.setText("");
            holder.checkinTime.setText("--:--");
            holder.checkoutTime.setText("--:--");
            SpannableString checkInText = new SpannableString("--:--");
            checkInText.setSpan(new ForegroundColorSpan(Color.parseColor("#ea5b5d")), 0, checkInText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.checkinTime.setText(checkInText);


            SpannableString checkOutText = new SpannableString("--:--");
            checkInText.setSpan(new ForegroundColorSpan(Color.parseColor("#ea5b5d")), 0, checkInText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.checkoutTime.setText(checkOutText);
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