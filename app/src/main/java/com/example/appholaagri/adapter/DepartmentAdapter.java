package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.DepartmentModel.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {
    private List<Department> departmentList;
    private List<Integer> selectedDepartmentIds;
    private OnDepartmentClickListener listener;

    public interface OnDepartmentClickListener {
        void onDepartmentClick(Department department);
    }

    public DepartmentAdapter(List<Department> departmentList, List<Integer> selectedDepartmentIds, OnDepartmentClickListener listener) {
        this.departmentList = new ArrayList<>(departmentList);
        this.selectedDepartmentIds = new ArrayList<>(selectedDepartmentIds);
        this.listener = listener;
    }

    public void updateList(List<Department> newList, List<Integer> selectedDepartmentIds) {
        this.departmentList = new ArrayList<>(newList);
        this.selectedDepartmentIds = new ArrayList<>(selectedDepartmentIds);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Department department = departmentList.get(position);
        holder.tvDepartmentName.setText(department.getName() != null ? department.getName() : "");
        holder.imgSelected.setImageDrawable(ContextCompat.getDrawable(
                holder.itemView.getContext(),
                selectedDepartmentIds.contains(department.getId()) ? R.drawable.checked_radio : R.drawable.unchecked_radio
        ));

        holder.itemView.setOnClickListener(v -> listener.onDepartmentClick(department));
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepartmentName;
        ImageView imgSelected;

        ViewHolder(View itemView) {
            super(itemView);
            tvDepartmentName = itemView.findViewById(R.id.tv_department_name);
            imgSelected = itemView.findViewById(R.id.img_department_selected);
        }
    }
}