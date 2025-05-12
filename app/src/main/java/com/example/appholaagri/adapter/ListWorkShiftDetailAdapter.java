package com.example.appholaagri.adapter;
import com.example.appholaagri.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.model.ListWorkShiftModel.ListWorkShiftResponse;

import java.util.ArrayList;
import java.util.List;

public class ListWorkShiftDetailAdapter extends RecyclerView.Adapter<ListWorkShiftDetailAdapter.ViewHolder> {
    private List<ListWorkShiftResponse.WorkShiftDetail> dayList;

    public ListWorkShiftDetailAdapter(List<ListWorkShiftResponse.WorkShiftDetail> dayList) {
        this.dayList = dayList;
    }

    @Override
    public ListWorkShiftDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_shift, parent, false);
        return new ViewHolder(view);
    }
    public void updateData(List<ListWorkShiftResponse.WorkShiftDetail> newData) {
        this.dayList.clear();
        if (newData != null) {
            this.dayList.addAll(newData);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ListWorkShiftDetailAdapter.ViewHolder holder, int position) {
        ListWorkShiftResponse.WorkShiftDetail day = dayList.get(position);
        holder.dayTextView.setText(day.getDisplayDate());

        // Hiển thị danh sách ca làm trong ngày
        List<ListWorkShiftResponse.ShiftDetail> shiftList = day.getShiftDetail();
        if (shiftList == null) shiftList = new ArrayList<>();

        WorkShiftDetailAdapter shiftAdapter = new WorkShiftDetailAdapter(shiftList);
        holder.shiftRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.shiftRecyclerView.setAdapter(shiftAdapter);
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public RecyclerView shiftRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.displayDate);
            shiftRecyclerView = itemView.findViewById(R.id.list_shift);
        }
    }
}
