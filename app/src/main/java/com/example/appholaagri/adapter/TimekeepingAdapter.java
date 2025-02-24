package com.example.appholaagri.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestModel.RequestListData;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;

import java.util.ArrayList;
import java.util.List;

public class TimekeepingAdapter extends RecyclerView.Adapter<TimekeepingAdapter.ViewHolder> {

    private List<TimekeepingStatisticsData.DayData> dayDataList;
    private List<TimekeepingStatisticsData.Shift> shiftList;

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
        // Đảm bảo shiftRes không null
        List<TimekeepingStatisticsData.Shift> shiftList = dayData.getShiftRes();
        if (shiftList == null) {
            shiftList = new ArrayList<>(); // Tránh null bằng danh sách trống
        }

        ShiftTimekeepingAdapter shiftTimekeepingAdapter = new ShiftTimekeepingAdapter(shiftList);
        holder.listShiftRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.listShiftRecyclerView.setAdapter(shiftTimekeepingAdapter);

    }



    @Override
    public int getItemCount() {
        return dayDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public RecyclerView listShiftRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            listShiftRecyclerView = itemView.findViewById(R.id.list_shift);
        }
    }
}