package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ListWorkShiftModel.ListWorkShiftResponse;

import java.util.ArrayList;
import java.util.List;

public class WorkShiftDetailAdapter extends RecyclerView.Adapter<WorkShiftDetailAdapter.ViewHolder> {
    private List<ListWorkShiftResponse.ShiftDetail> dayList;

    public WorkShiftDetailAdapter(List<ListWorkShiftResponse.ShiftDetail> dayList) {
        this.dayList = dayList;
    }
    @Override
    public WorkShiftDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_work_shift, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(WorkShiftDetailAdapter.ViewHolder holder, int position) {
        ListWorkShiftResponse.ShiftDetail day = dayList.get(position);
        holder.dayTextView.setText(day.getDisplayName());

    }
    @Override
    public int getItemCount() {
        return dayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.displayDate);
        }
    }
}
