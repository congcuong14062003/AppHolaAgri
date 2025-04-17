package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ListWorkShiftModel.ListWorkShiftResponse;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class MyWorkShiftAdapter extends RecyclerView.Adapter<MyWorkShiftAdapter.ViewHolder> {

    private List<ListWorkShiftResponse.WorkShiftData> workShiftData;

    public MyWorkShiftAdapter(List<ListWorkShiftResponse.WorkShiftData> workShiftData) {
        this.workShiftData = workShiftData;
    }

    public void updateData(List<ListWorkShiftResponse.WorkShiftData> newData) {
        workShiftData.clear();
        workShiftData.addAll(newData);
        notifyDataSetChanged();
    }

    public void addData(List<ListWorkShiftResponse.WorkShiftData> newData) {
        int start = workShiftData.size();
        workShiftData.addAll(newData);
        notifyItemRangeInserted(start, newData.size());
    }

    public void clearData() {
        workShiftData.clear();
        notifyDataSetChanged();
    }

    @Override
    public MyWorkShiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_work_shift, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyWorkShiftAdapter.ViewHolder holder, int position) {
        ListWorkShiftResponse.WorkShiftData data = workShiftData.get(position);
        Picasso.get()
                .load(data.getEmployee().getUrl())  // URL ảnh cần tải
                .placeholder(R.drawable.avatar)           // Ảnh mặc định khi chưa tải được
                .error(R.drawable.avatar)                 // Ảnh khi có lỗi tải
                .into(holder.avatar_work_shift);

        holder.name_work_shift.setText(data.getEmployee().getCode() + " - " +data.getEmployee().getName());
        holder.department_work_shift.setText(data.getTeam().getName());

        List<ListWorkShiftResponse.WorkShiftDetail> detailList = data.getWorkShiftListDetail();
        if (detailList == null) detailList = new ArrayList<>();

        ListWorkShiftDetailAdapter childAdapter = new ListWorkShiftDetailAdapter(detailList);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return workShiftData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView department_work_shift, name_work_shift;
        public RecyclerView recyclerView;
        private ImageView avatar_work_shift;

        public ViewHolder(View itemView) {
            super(itemView);
            department_work_shift = itemView.findViewById(R.id.department_work_shift);
            name_work_shift = itemView.findViewById(R.id.name_work_shift);
            recyclerView = itemView.findViewById(R.id.list_detail_shift);
            avatar_work_shift = itemView.findViewById(R.id.avatar_work_shift);
        }
    }
}

