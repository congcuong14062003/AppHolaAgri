package com.example.appholaagri.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionResponse;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;
import com.example.appholaagri.view.QrContentRecordCondition;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecordConditionAdapter extends RecyclerView.Adapter<RecordConditionAdapter.ViewHolder> {
    private final List<RecordConditionResponse.RecordData> recordDataList;


    public RecordConditionAdapter(List<RecordConditionResponse.RecordData> recordDataList) {
        this.recordDataList = new ArrayList<>(recordDataList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record_condition, parent, false);
        return new ViewHolder(view);
    }
    public void updateData(List<RecordConditionResponse.RecordData> newData) {
        recordDataList.clear();
        recordDataList.addAll(newData);
        notifyDataSetChanged();
    }
    // Cập nhật dữ liệu mới khi tải thêm trang
    public void addData(List<RecordConditionResponse.RecordData> newData) {
        if (newData != null && !newData.isEmpty()) {
            int startPosition = recordDataList.size();
            recordDataList.addAll(newData);
            notifyItemRangeInserted(startPosition, newData.size());
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordConditionResponse.RecordData recordData = recordDataList.get(position);
        holder.date_record.setText(recordData.getAtDate());
        holder.employee_record.setText(recordData.getEmployee().getCode() + " - " + recordData.getEmployee().getName());
        holder.txt_name_plant.setText(recordData.getCropVarieties().getCode() + " - " + recordData.getCropVarieties().getName());
        holder.txt_type_plant.setText(recordData.getPlantation().getName() + " - " + recordData.getCultivationArea().getName());
        holder.problem_plant.setText(recordData.getProblem());
        holder.infor_plant.setText(recordData.getDevelopment());


        Picasso.get()
                .load(recordData.getEmployee().getUrl())
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(holder.avatar_record);
        // Xử lý sự kiện click, truyền chỉ id của item
        holder.itemView.setOnClickListener(v -> {
            // Truyền id qua Intent khi item được click
            Intent intent = new Intent(holder.itemView.getContext(), QrContentRecordCondition.class);
            intent.putExtra("record_id", recordData.getId()); // Truyền id của item
            holder.itemView.getContext().startActivity(intent); // Mở Activity QrContentRecordCondition
        });
    }

    @Override
    public int getItemCount() {
        return recordDataList.size();
    }



    // Xóa hết dữ liệu (dùng cho refresh)
    public void clearData() {
        recordDataList.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date_record, employee_record, txt_name_plant, txt_type_plant, problem_plant, infor_plant;
        public ImageView avatar_record;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar_record = itemView.findViewById(R.id.avatar_record);
            date_record = itemView.findViewById(R.id.date_record);
            employee_record = itemView.findViewById(R.id.employee_record);
            txt_name_plant = itemView.findViewById(R.id.txt_name_plant);
            txt_type_plant = itemView.findViewById(R.id.txt_type_plant);
            problem_plant = itemView.findViewById(R.id.problem_plant);
            infor_plant = itemView.findViewById(R.id.infor_plant);
        }
    }
}

