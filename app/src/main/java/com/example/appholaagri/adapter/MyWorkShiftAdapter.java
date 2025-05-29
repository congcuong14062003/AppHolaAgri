package com.example.appholaagri.adapter;

import android.util.Log;
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
    private OnItemClickListener onItemClickListener;
    private boolean isTeamShift;
    // Interface cho callback khi nhấn item
    public interface OnItemClickListener {
        void onItemClick(ListWorkShiftResponse.WorkShiftData workShiftData);
    }

    public MyWorkShiftAdapter(List<ListWorkShiftResponse.WorkShiftData> workShiftData, boolean isTeamShift) {
        this.workShiftData = workShiftData != null ? workShiftData : new ArrayList<>();
        this.isTeamShift = isTeamShift;
    }

    // Phương thức để set listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void updateData(List<ListWorkShiftResponse.WorkShiftData> newData) {
        workShiftData.clear();
        workShiftData.addAll(newData != null ? newData : new ArrayList<>());
        notifyDataSetChanged();
    }

    public void addData(List<ListWorkShiftResponse.WorkShiftData> newData) {
        if (newData != null && !newData.isEmpty()) {
            int startPosition = workShiftData.size();
            workShiftData.addAll(newData);
            notifyItemRangeInserted(startPosition, newData.size());
        }
    }

    public void clearData() {
        workShiftData.clear();
        notifyDataSetChanged();
    }

    @Override
    public MyWorkShiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes = isTeamShift ? R.layout.team_work_shift : R.layout.my_work_shift;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyWorkShiftAdapter.ViewHolder holder, int position) {
        ListWorkShiftResponse.WorkShiftData data = workShiftData.get(position);
        if (data == null) {
            Log.e("MyWorkShiftAdapter", "WorkShiftData at position " + position + " is null");
            return;
        }

        // Kiểm tra employee
        if (data.getEmployee() != null) {
            Picasso.get()
                    .load(data.getEmployee().getUrl())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(holder.avatar_work_shift);
            holder.name_work_shift.setText(data.getEmployee().getCode() + " - " + data.getEmployee().getName());
        } else {
            Log.e("MyWorkShiftAdapter", "Employee is null for WorkShiftData at position " + position);
            holder.name_work_shift.setText("N/A");
            holder.avatar_work_shift.setImageResource(R.drawable.avatar);
        }

        // Kiểm tra team
        if (data.getTeam() != null) {
            holder.department_work_shift.setText(data.getTeam().getName());
        } else {
            Log.e("MyWorkShiftAdapter", "Team is null for WorkShiftData at position " + position);
            holder.department_work_shift.setText("N/A");
        }

        // Kiểm tra workShiftListDetail
        List<ListWorkShiftResponse.WorkShiftDetail> detailList = data.getWorkShiftListDetail();
        if (detailList == null) {
            Log.w("MyWorkShiftAdapter", "WorkShiftListDetail is null for WorkShiftData at position " + position);
            detailList = new ArrayList<>();
        }

        ListWorkShiftDetailAdapter childAdapter = new ListWorkShiftDetailAdapter(detailList);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerView.setAdapter(childAdapter);

        // Log dữ liệu để gỡ lỗi
        Log.d("MyWorkShiftAdapter", "Binding position " + position + ": Employee=" + (data.getEmployee() != null ? data.getEmployee().getName() : "null") +
                ", Team=" + (data.getTeam() != null ? data.getTeam().getName() : "null") +
                ", WorkShiftListDetail size=" + detailList.size());

        // Xử lý sự kiện nhấn item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null && data != null) {
                Log.d("MyWorkShiftAdapter", "Item clicked at position " + position + ": " + (data.getEmployee() != null ? data.getEmployee().getName() : "null"));
                onItemClickListener.onItemClick(data);
            } else {
                Log.e("MyWorkShiftAdapter", "Item click ignored: data or listener is null at position " + position);
            }
        });
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