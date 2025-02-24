package com.example.appholaagri.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.PlanAppInitFormModel.PlanAppInitFormResponse;

import java.util.List;

public class PlantationAdapter extends RecyclerView.Adapter<PlantationAdapter.ViewHolder> {

    private List<PlanAppInitFormResponse.Plantation> plantationList;
    private OnItemClickListener listener;
    private int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(String name);
    }

    public PlantationAdapter(List<PlanAppInitFormResponse.Plantation> plantationList, OnItemClickListener listener) {
        this.plantationList = plantationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plantation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PlanAppInitFormResponse.Plantation plantation = plantationList.get(position);
        holder.textView.setText(plantation.getName());

        if (position == selectedPosition) {
            holder.textView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.maincolor));
//            holder.textView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.maincolor)); // Màu nền khi chọn
        } else {
            holder.textView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.textcolor));
//            holder.textView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent)); // Màu nền mặc định
        }

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;  // Lưu lại vị trí trước đó
            selectedPosition = position;

            // Chỉ cập nhật lại 2 item thay vì toàn bộ danh sách để tối ưu hiệu suất
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            listener.onItemClick(plantation.getName());

        });
    }


    @Override
    public int getItemCount() {
        return plantationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewPlantation);
        }
    }
}
